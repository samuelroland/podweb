# Podweb - Projet de BDR
Groupe: Samuel Roland, Arthur Junod et Edwin Haeffner.

## Un player web de podcasts en Java

En s'appuyant sur la base de données et le service [podcastindex.org](podcastindex.org), nous allons créer une application web en Java permettant d'écouter et d'interagir avec des podcasts, leurs épisodes et permettre l'écoute multiple via une file d'attente.

## Fonctionnalités
1. Parcours des podcasts et de leurs épisodes. Chaque épisode appartient à un podcast. Chaque podcast peut avoir 0 à plusieurs épisodes. On peut voir l'image des podcasts, le nombre d'épisodes publiés ainsi que leur auteur. Les podcasts peuvent appartenir de 0 à plusieurs catégories.
1. Recherche fulltext des podcasts et épisodes et auteurs.
1. Ranking des podcasts les plus écoutés, incluant leurs épisodes les plus écoutés, filtrable par catégorie
1. Quand on écoute un épisode, on sauvegarde toutes les 30 secondes la progression de l'écoute sur le serveur. Une fois terminé, on sauvegarde l'écoute de l'épisode, ce qui permet de voir si on a déjà écouté un épisode quand on le voit dans la liste. Chaque écoute est comptée séparément, on peut donc voir le nombre d'écoutes personnel. En plus, on peut voir le nombre d'écoutes total parmi tous les utilisateurs.
1. Une gestion de file d'attente d'écoute d'épisodes. Pour chaque épisode dont on a commencé la lecture, on peut savoir et reprendre la lecture à l'endroit où on s'est arrêté la dernière fois.
1. Lancement et mise en pause de l'épisode en cours ou d'un autre épisode dans la file d'attente ou ailleurs. Le lancement d'un épisode le place toujours en haut de la file d'attente. Il est possible aussi de retirer n'importe quel épisode de la file d'attente (dans ce cas le temps restant sur l'épisode n'est pas supprimé).
1. Création, modification et suppression de playlists permettant d'agréger une liste d'épisodes dans un ordre défini. On peut ajouter et retirer des épisodes depuis n'importe quel liste d'épisodes. Une playlist peut être vide.
1. Création de compte, connexion et déconnexion et suppression de compte.

<div class="page">

## Entités nécessaires
1. Podcasts
   1. Titre
   1. Description
   1. Flux RSS
   1. Image
   1. Auteur
   1. Nombre d'épisodes
1. Épisodes
   1. Titre
   1. Description
   1. Date de publication
   1. Lien fichier audio
1. Catégories
   1. Nom
1. Utilisateurs
   1. Prénom
   1. Nom
   1. Email
   1. Mot de passe
1. Playlists
   1. Nom
   1. Description

<div class="page">

## Fourniture en données
Pour ne pas devoir créer à la main ou générer des données de développement, nous allons utiliser quelques milliers d'enregistrements de la base de données publique et ouverte du registre PodcastIndex. Ce registre répertorie plus de 4 millions de podcasts, nous allons en prendre qu'une toute petite partie parmi les podcasts et de leurs épisodes.

La base de données ([téléchargeable ici](https://public.podcastindex.org/podcastindex_feeds.db.tgz)) fait déjà 4 Go et ne contient malheureusement pas les épisodes. Il nous faut donc aller les chercher via leur API ou directement via leur flux RSS. Les catégories sont définies dans 10 attributs de la table podcasts, il nous faut en conséquence les extraire et créer les catégories et les associations vers chaque podcast.

Il nous restera ainsi à générer aléatoirement des utilisateurs et des playlists avec des épisodes, des écoutes d'épisodes pour chaque utilisateur. Une partie des utilisateurs auront déjà des files d'attentes remplies avec quelques épisodes. Au final, nous aurons donc une base de données légère avec seulement les attributs des podcasts qui nous intéressent et les autres tables.

## Badges
Nous allons développer un système de *badges* attribués aux utilisateurs quand ils atteignent certains niveaux d'engagement sur Podweb. Voici la liste des badges et leur détails.

| Type             | Name                  | Points        | Condition            | Description                                                              |
| ---------------- | --------------------- | ------------- | -------------------- | ------------------------------------------------------------------------ |
| ListeningCount   | BabyListener          | 100 pts       | 50 listenings        |                                                                          |
| ListeningCount   | BigBabyListener       | 400 pts       | 100 listenings       |                                                                          |
| ListeningCount   | HeavyListener         | 10000 pts     | 1000 listenings      |                                                                          |
| ListeningCount   | PetaListener          | 100000000 pts | 10000 listenings     | You are a peta listener, do you even have a life ?                       |
| RegistrationDate | BabyCaster            | 100 pts       | 1 month passed       | You are not new as a month ago...                                        |
| RegistrationDate | TeenCaster            | 300 pts       | 6 months passed      | Starting to rebel as a teen listening to podcasts instead of TV.         |
| RegistrationDate | MatureCaster          | 1000 pts      | 1 year passed        |                                                                          |
| RegistrationDate | SagedCaster           | 5000 pts      | 5 years passed       |                                                                          |
| PlaylistCreation | PlaylistNewbie        | 40 pts        | created 1 playlist   | You find the best button !                                               |
| PlaylistCreation | PlaylistBoss          | 200pts        | 10 playlist          |                                                                          |
| CommentsCount    | CourageousCommentator | 10 pts        | posted 1 comment     | The first one is the hardest, congratulations !                          |
| CommentsCount    | MatureCommentator     | 1000 pts      | posted 100 comments  | Almost full time commentator...                                          |
| CommentsCount    | VeryMatureCommentator | 10000 pts     | posted 1000 comments | Podcasters will thank you but now you should give your fingers a break ! |

Les 4 types suivants sont possibles:
1. `ListeningCount`: le badge sera attribué à partir d'un certain **nombre d'écoutes d'épisodes** (2 écoutes du même épisode compte bien 2 fois)
1. `RegistrationDate`: le badge sera attribué à partir d'un certain **temps passé après la date de création de compte**
1. `PlaylistCreation`: le badge sera attribué à partir d'un certain nombres **de playlists créées**
1. `CommentsCount`: le badge sera attribué à partir d'un certain nombres de **commentaires postés**

L'attribution des badges pourra probablement se faire via des triggers développé en SQL qui s'exécutera à chaque fois qu'une écoute est ajoutée par ex.
