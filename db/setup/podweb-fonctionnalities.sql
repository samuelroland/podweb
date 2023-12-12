-- Project: Podweb
-- Functionalities of the project

-- Utilisation avec java Spring

-- Forme en PostGreSQL

-- Parcours des podcasts et de leurs épisodes. Chaque épisode appartient à un podcast. Chaque podcast
-- peut avoir 0 à plusieurs épisodes. On peut voir l'image des podcasts, le nombre d'épisodes publiés
-- ainsi que leur auteur. Les podcasts peuvent appartenir de 0 à plusieurs catégories.

SELECT * FROM podcasts;
SELECT * FROM episodes;
SELECT * FROM categories;
SELECT * FROM comments;
SELECT * FROM badges;
SELECT * FROM users;
SELECT * FROM playlists;

-- Recherche fulltext des podcasts , episodes et auteurs.

SELECT * FROM podcasts WHERE to_tsvector(title) @@ to_tsquery(?1);
SELECT * FROM episodes WHERE to_tsvector(title) @@ to_tsquery(?1);
SELECT * FROM podcasts WHERE to_tsvector(author) @@ to_tsquery(?1);

-- Ranking des podcasts les plus écoutés, incluant leurs épisodes les plus écoutés, filtrable par catégorie.

SELECT *
FROM podcasts_ranking
INNER JOIN podcasts ON podcasts_ranking.id = podcasts.id
INNER JOIN categorize ON podcasts.id = categorize.podcast_id
WHERE category_id = ?1;

-- Quand on écoute un épisode, on sauvegarde toutes les 30 secondes la progression de l'écoute sur le
-- serveur. Une fois terminé, on sauvegarde l'écoute de l'épisode, ce qui permet de voir si on a déjà
-- écouté un épisode quand on le voit dans la liste. Chaque écoute est comptée séparément, on peut
-- donc voir le nombre d'écoutes personnel. En plus, on peut voir le nombre d'écoutes total parmi tous
-- les utilisateurs.

-- update de la progression de l'écoute d'un épisode
UPDATE listen
SET progression = ?2
FROM listen
WHERE listen.episode_id = ?1;

-- check si on a déjà écouté un épisode et la progression de celui ci en donnant la progression et
-- la durée de l'épisode

SELECT episode_id,
       progression,
       duration,
       listening_count
FROM listen
INNER JOIN episodes ON listen.episode_id = episodes.id
WHERE (progression > 0 OR listening_count > 1) AND user_id = ?1;

-- Une gestion de file d'attente d'écoute d'épisodes. Pour chaque épisode dont on a commencé la lecture,
-- on peut savoir et reprendre la lecture à l'endroit où on s'est arrêté la dernière fois.

-- Ajout d'un épisode à la file d'attente

INSERT INTO queue (user_id, episode_id)
VALUES (?1, ?2);

-- Suppression d'un épisode de la file d'attente

DELETE FROM queue
WHERE user_id = ?1 AND episode_id = ?2;

-- Récupération de la file d'attente

SELECT * FROM queue
INNER JOIN episodes ON queue.episode_id = episodes.id
WHERE user_id = ?1;

-- Lancement et mise en pause de l'épisode en cours ou d'un autre épisode dans la file d'attente ou
-- ailleurs. Le lancement d'un épisode le place toujours en haut de la file d'attente. Il est possible aussi de
-- retirer n'importe quel épisode de la file d'attente (dans ce cas le temps restant sur l'épisode n'est pas
-- supprimé).

-- Check si un épisode est dans la file d'attente

SELECT * FROM queue
WHERE user_id = ?1 AND episode_id = ?2;

-- En java, on va delete l'épisode de la file d'attente et on va le rajouter en haut de la file d'attente

-- Update de l'index de l'épisode dans la file d'attente

-- décaler tous les épisodes de la file d'attente de 1 vers le bas
UPDATE queue
SET index = index + 1
WHERE user_id = ?1;

-- mettre l'épisode en cours en haut de la file d'attente

UPDATE queue
SET index = 1
WHERE user_id = ?1 AND episode_id = ?2;

-- Récupération de l'épisode en cours

SELECT * FROM queue
WHERE index = 1 AND user_id = ?1;

-- Création, modification et suppression de playlists permettant d'agréger une liste d'épisodes dans un
-- ordre défini. On peut ajouter et retirer des épisodes depuis n'importe quel liste d'épisodes. Une playlist
-- peut être vide

-- Creation d'une playlist

INSERT INTO playlists (user_id, name, description)
VALUES (?1, ?2, ?3);

-- Suppression d'une playlist

DELETE FROM playlists
WHERE id = ?1;

-- Ajout d'un épisode à une playlist

INSERT INTO list (playlist_id, episode_id)
VALUES (?1, ?2);

-- Suppression d'un épisode d'une playlist

DELETE FROM list
WHERE playlist_id = ?1 AND episode_id = ?2;

-- Récupération des playlists

SELECT * FROM playlists
WHERE id = ?1;

-- Récupère une playlist que l'utilisateur a créé

SELECT * FROM playlists
WHERE id = ?1 AND user_id = ?2;

-- Récupération des épisodes d'une playlist

SELECT * FROM episodes
INNER JOIN list ON list.episode_id = episodes.id
WHERE playlist_id = ?1;

-- Creation de compte, connexion, déconnection et suppression de compte.

-- Creation de compte

INSERT INTO users (firstname, lastname, email, password, registration_date)
VALUES (?1, ?2, ?3, ?4, now());

-- Connexion

SELECT id,
       firstname,
       lastname,
       email,
       registration_date
    FROM users
WHERE email = ?1 AND password = ?2;

-- Suppression de compte

DELETE FROM users
WHERE id = ?1;




