# Podweb - Projet de BDR
## Un player web de podcasts

En s'appuyant sur la base de données et le service [podcastindex.org](podcastindex.org) nous allons créer une application web en Java permettant d'écouter et d'intéragir avec des podcasts, leurs épisodes et permettre l'écoute multiple via une fil d'attente.

## Fonctionnalités
1. Parcours des podcasts et de leurs épisodes. On peut voir l'image des podcasts, le nombre d'épisodes publiés ainsi que leur auteur si défini.
1. Recherche fulltext des podcasts et épisodes et auteurs.
1. Ranking des podcasts les plus écoutés, incluant leurs épisodes les plus écoutés, filtrable par catégorie
1. Une gestion de fil d'attente d'écoute d'épisodes. Pour chaque épisode dont on a commencé la lecture, on peut savoir et reprendre la lecture à l'endroit où on s'est arrếté la dernière fois.
1. Lancement et pause de l'épisode en cours ou d'un autre épisode dans la fil d'attente ou ailleurs
1. Création, modification et suppression de playlists permettant d'aggréger une liste d'épisodes dans un ordre défini. On peut ajouter et retirer des épisodes depuis n'importe quel liste d'épisodes.
1. Création de compte, connexion et deconnexion et suppression de compte.

### Entités nécessaires
1. Podcasts
1. Episodes
1. Auteurs
1. Users
1. Playlists
1. Ecoutes (table de jointure entre Users et Episodes)
1. Abonnements (table de jointure entre Users et Podcasts)

