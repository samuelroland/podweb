Prérequis
1. JDK 21
2. NodeJS pour avoir NPM et ainsi facilement installer TailwindCSS.
3. Gradle (optionnel, mais recommandé)
4. Docker

Déploiement
Voici les commandes pour déployer notre application sur un serveur. Les commandes sont à lancer dans le dossier `podweb`.

D'abord, il faut faire un build optimisé pour la production des styles CSS. Le fichier produit est toujours le même : `./app/src/main/static/out.css`, il est cependant minifié pour être le plus léger possible.

npm run prod


Pour builder le projet dans un "fat jar" c'est-à-dire une archive `.jar` qui contient toutes les dépendances utiles en dehors du développement :

gradle uberJar

L'archive générée est présente au chemin suivant : `app/build/libs/app-uber.jar`.

Pour exécuter notre archive avec `java -jar ...`, la variable d'environnement `PODWEB_PRODUCTION` doit être définie, elle permet d'indiquer qu'il faut utiliser les versions compilées des templates JTE chargées dans le JAR et non les chercher dans le dossier originel. Il faut également qu'un dossier static soit présent à côté et que le `out.css` soit copié dedans.

PODWEB_PRODUCTION="" java -jar app/build/libs/app-uber.jar


Image docker
Afin de facilement lancer ou déployer notre application, nous avons créé un `Dockerfile` dédié :

Note : les commandes sont toujours à lancer dans le dossier `podweb`.

Préparatifs et docker build de l'image :

./prod.sh

docker compose up


### Mise en place
Note : Si vous n'avez pas installé Gradle, il suffit de substituer les `gradle` dans les commandes suivantes par `./gradlew` sous Linux ou MacOS (`chmod +x ./gradlew` si erreur d'exécution), et par `.\gradlew` sous Windows (installer Gradle permet de se simplifier un peu la vie).

1. Cloner le repos

    git clone https://github.com/samuelroland/podweb

2. Pour installer tailwindcss via NPM

    npm install -D tailwindcss

3. Pour charger le style une première fois (obligatoire lorsqu'il y a du changement dans les pages html ou css)

    npm run prod

4. Pour configurer la base de données, il suffit de créer un fichier `.env` et d'indiquer les identifiants

    DB_PORT=5432
    DB_USER=
    DB_PWD=

5. Pour lancer notre base de données PostgreSQL (se mettre dans le dossier `podweb`)

     docker compose up -d

6. Pour lancer l'application directement (build + run)

    gradle run

7. Ouvrir son navigateur en `localhost:7000`