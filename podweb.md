# Podweb rapport
<!-- TODO: faire une joli page de titre ! -->
<!-- TODO: trouver un moyen de faire un joli export -->

## Java stack
Voici les outils que nous utilisons pour implémenter noter application web en Java:
1. Javalin: un petit framework web léger et rapide
1. Gradle: nous voulions tester autre chose que Maven pour gérer les dépendences, les builds et l'exécution de tests, nous avons pris son alternative.
1. JTE: système de template permettant d'écrire facilement des vues en HTML
1. TailwindCSS: un framework CSS très puissant et orienté sur des classes utilitaires
1. TODO: testing tool

## Développement
TODO: commandes gradlew
Pour builder le projet:
```sh
gradle build
```
Pour lancer le serveur (cette commande fait également le build)
```sh
gradle run
# ou en mode quiet pour avoir uniquement l'output de notre serveur et pas celui de Gradle
gradle run -q
```

### Lancement des tests
TODO

### Développement des vues
JTE supporte le rechargement automatique des vues lors que de nouvelles requêtes sont faites. Durant le développement, il n'y a pas besoin de redémarrer le serveur Javalin !

## Déploiement
Pour déployer un serveur podweb, les commandes sont un peu différentes.

Pour builder le projet dans un "fat jar" c'est à dire une archive `.jar` qui contient toutes les dépendances utiles en dehors du développement:
```sh
gradle uberJar
```
L'archive générée est présente au chemin suivant: `app/build/libs/app-uber.jar`.

Pour exécuter notre archive avec `java -jar ...`, la variable d'environement `PODWEB_PRODUCTION` doit être définie, elle permet d'indiquer qu'il faut utiliser les versions compilées des templates JTE chargées dans le JAR et non les chercher dans le dossier originel.
```sh
PODWEB_PRODUCTION="" java -jar app/build/libs/app-uber.jar
```

### Image docker
TODO
