# Podweb rapport
<!-- TODO: faire une joli page de titre ! -->
<!-- TODO: trouver un moyen de faire un joli export -->

## Java stack
Voici les outils que nous utilisons pour implémenter noter application web en Java:
1. Javalin: un petit framework web léger et rapide
1. Gradle: nous voulions tester autre chose que Maven pour gérer les dépendences, les builds et l'exécution de tests, nous avons pris son alternative.
1. [JTE](https://jte.gg/): système de template permettant d'écrire facilement des vues en HTML
1. TailwindCSS: un framework CSS très puissant et orienté sur des classes utilitaires
1. Junit: le classique framework de test en Java

## Développement

### Prérequis
1. JDK 21
1. [NodeJS](https://nodejs.org/en) (pour avoir NPM et ainsi facilement installer TailwindCSS). Ou alors il est possible d'installer le [CLI Tailwindcss directement également...](https://tailwindcss.com/blog/standalone-cli), dans ce cas les commandes `npm run <x>` ne marcheront pas mais peuvent être reprise du `package.json`.
1. [Gradle](https://gradle.org/install/) (optionnel mais recommandé)

### Mise en place
Note: Si vous n'avez pas installé Gradle, il suffit de substituer les `gradle` dans les commandes suivantes par `./gradle` sous Linux ou MacOS, et par `.\gradlew` sous Windows (installer Gradle permet de se simplifier un peu la vie).

1. Cloner le repos
    ```sh
    git clone https://github.com/samuelroland/podweb
    ```
1. Pour installer tailwindcss via NPM
    ```sh
    npm install
    # ou en plus court
    npm i
    ```
1. Pour charger le style une première fois
    ```sh
    npm run prod
    ```
1. TODO: setup db podweb
1. Pour configurer la base de données, il suffit de créer un fichier `.env` et d'indiquer les identifiants
   ```env
    DB_PORT=5432
    DB_USER=
    DB_PWD=
   ```
1. Pour lancer l'application directement (build + run)
    ```sh
    gradle run
    ```
1. Ouvrir son navigateur en `localhost:7000`

Autres commandes utiles:

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
Pour lancer les tests, il suffit de les lancer via une intégration d'IDE ou alors en ligne de commande:
```sh
gradle test
# ou encore mieux en mode continuous !
gradle test -t
```

### Développement des vues
Nous avons choisi [JTE - Java Template Engine](https://jte.gg/) pour facilement écrire nos vues, il supporte même le rechargement automatique des vues ! Durant le développement, il n'y a pas besoin de redémarrer le serveur Javalin si on veut juste tester des changements d'interface !

Toutes les vues sont sous `podweb/app/src/main/jte` et notre style CSS sous `podweb/app/src/main/static`.

Pour que les nouvelles classes Tailwind soient bien ajoutés à la volée dès qu'on les ajoutes dans nos templates `.jte` ou qu'on modifie le `style.css`, il faut lancer avoir un processus du CLI tailwindcss en arrière plan qui "recompile" quand il voit des changements. Pour cela, il suffit de lancer:
```sh
npm run watch
```

Nous utilisons NPM pour facilement installer le tailwindcss et le mettre à jour si besoin. NPM utilise un fichier `package.json` définissant la dépendance `tailwindcss > 3.3.1` et 2 scripts `watch` et `prod` documentés dans ce document.

Note: des extensions d'IDE pour supporter la syntaxe JTE existe pour [IntelliJ](https://plugins.jetbrains.com/plugin/14521-jte/) et [VSCode](https://marketplace.visualstudio.com/items?itemName=maj2c.jte-template-syntax-highlight). Très pratique pour avoir des couleurs utiles et avoir de l'autocomplétion HTML et CSS, tout en ayant les couleurs et propositions liées à Java.


## Déploiement
Pour déployer un serveur podweb, les commandes sont un peu différentes.

D'abord, il faut faire un build optimisé pour la production des styles CSS. Le fichier produit est toujours le même: `./app/src/main/static/out.css`, il est cependant minifié pour être le plus léger possible.
```sh
npm run prod
```

Pour builder le projet dans un "fat jar" c'est à dire une archive `.jar` qui contient toutes les dépendances utiles en dehors du développement:
```sh
gradle uberJar
```
L'archive générée est présente au chemin suivant: `app/build/libs/app-uber.jar`.

Pour exécuter notre archive avec `java -jar ...`, la variable d'environement `PODWEB_PRODUCTION` doit être définie, elle permet d'indiquer qu'il faut utiliser les versions compilées des templates JTE chargées dans le JAR et non les chercher dans le dossier originel. Il faut également qu'un dossier static soit présent à coté et que le `out.css` soit copié dedans.
```sh
PODWEB_PRODUCTION="" java -jar app/build/libs/app-uber.jar
```

### Image docker
Afin de facilement lancer ou déployer notre application, nous avons créé un `Dockerfile` dédié:

Note: les commandes sont toujours dans le dossier `podweb`.

Préparatifs et docker build de l'image:
```sh
./prod.sh
```

Et pour lancer notre image sur le port 7000
```sh
docker run -p 7000:7000 podweb
```

TODO: faire un docker-compose.yml pour y ajouter la base de données et connecter les 2 ensembles.
```sh
docker compose up
```