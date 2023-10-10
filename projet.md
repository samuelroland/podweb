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

