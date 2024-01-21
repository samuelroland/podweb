# Rapport PodWeb 
Réalisé par Arthur Junod, Samuel Roland et Edwin Häffner

## Introduction 

Dans le cadre du cours de base de données relationnelles, nous avons réalisé un projet de développement d'une
application web en Java permettant d'écouter et d'interagir avec des podcasts, ajouter des commentaires, des notes, etc.



## Fonctionnalités

Notre application web permet de faire les choses suivantes:

1. Parcours des podcasts et de leurs épisodes. Chaque épisode appartient à un podcast. 
Chaque podcast peut avoir 0 à plusieurs épisodes. On peut voir l'image des podcasts, le nombre d'épisodes publiés ainsi que leur auteur.

2. Recherche fulltext des podcasts et épisodes et auteurs et affichage des résultats.

3. Ranking des podcasts les plus écoutés

4. Lors de l'écoute d'un épisode, l'application sauvegarde toutes les 15 secondes la progression de l'écoute dans la base de données.
Permet de reprendre l'écoute là où on s'était arrêté.

5. Possibilité de se connecter avec des identifiants déjà existants pour pouvoir commenter et noter les épisodes et sauvegarder les progressions 
d'écoute.



## Implémentation

### Base de données

Nous avons utilisé PostgreSQL pour la base de données. Nous avons créé 12 tables mais nous n'en utilisons
que 10 dans l'application pour des raisons de temps. 

![mld.svg](./db/docs/mld.svg)

### Application web

Nous avons utilisé Javalin pour le serveur web et JDBC pour faire le lien avec la base de données. 


