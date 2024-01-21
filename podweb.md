# Podweb
Réalisé par Arthur Junod, Samuel Roland et Edwin Häffner
<!-- TODO: faire une joli page de titre avec saut de page ! -->
<!-- TODO: inclure photo de l'app sur la page de titre ! -->
<!-- TODO: faire un joli export -->
<img src="PodWebLogo.png" width="500" height="500" alt="Logo de podweb">

<p style="page-break-before: always"></p>


## Introduction

Dans le cadre du cours de base de données relationnelles, nous avons réalisé un projet de développement d'une
application web en Java permettant d'écouter et d'interagir avec des podcasts, ajouter des commentaires,
des notes, etc.

## Fonctionnalités

Notre application web permet de faire les choses suivantes :

1. Parcours des podcasts et de leurs épisodes. On peut voir l'image des podcasts, le nombre d'épisodes publiés ainsi que leur auteur.

2. Recherche fulltext des épisodes (titre, description, auteur, titre du podcast) et affichage des résultats.

3. Ranking des podcasts les plus écoutés

4. Lors de l'écoute d'un épisode, l'application sauvegarde toutes les 15 secondes la progression de l'écoute dans la base de données.
   Permet de reprendre l'écoute là où on s'était arrêté.

5. Possibilité de se connecter avec des identifiants déjà existants pour pouvoir commenter et noter les épisodes et sauvegarder les progressions
   d'écoute.

6. Commenter les épisodes et répondre à d'autres commentaires.

7. Possibilité de voir son profil et les badges obtenus (badges d'écoute, de commentaires, etc.)


## Implémentation

### Base de données

Nous avons utilisé PostgreSQL pour la base de données. Nous avons créé 12 tables, mais nous n'en utilisons
que 10 dans l'application pour des raisons de temps et de complexité d'implémentation.

![mld.svg](./db/docs/mld.svg)

Les tables `categories` et `queue` ne sont pas utilisées dans l'application, mais sont encore présentes dans la base de
données. Pour d'eventuelles futures améliorations.

### Java stack
Voici les outils que nous utilisons pour implémenter notre application web en Java:
1. Javalin: un petit framework web léger et rapide
2. Gradle: nous voulions tester autre chose que Maven pour gérer les dépendences, les builds et l'exécution de tests, nous avons pris son alternative.
3. [JTE](https://jte.gg/): système de template permettant d'écrire facilement des vues en HTML
4. TailwindCSS: un framework CSS très puissant et orienté sur des classes utilitaires
5. Junit: le classique framework de test en Java

## Développement

### Prérequis
1. JDK 21
2. [NodeJS](https://nodejs.org/en) (pour avoir NPM et ainsi facilement installer TailwindCSS). Ou alors il est possible d'installer le [CLI Tailwindcss directement également...](https://tailwindcss.com/blog/standalone-cli), dans ce cas les commandes `npm run <x>` ne marcheront pas mais peuvent être reprise du `package.json`.
3. [Gradle](https://gradle.org/install/) (optionnel mais recommandé)
4. [Docker](https://docs.docker.com/get-docker/)
5. [NPM](https://www.npmjs.com/get-npm) (utilisé pour mettre à jour le CSS avec TailwindCSS)

## Déploiement
Voici les commandes pour déployer notre application sur un serveur. Les commandes sont à lancer dans le dossier `podweb`.

D'abord, il faut faire un build optimisé pour la production des styles CSS. Le fichier produit est toujours le même : `./app/src/main/static/out.css`, il est cependant minifié pour être le plus léger possible.
```sh
npm run prod
```

Pour builder le projet dans un "fat jar" c'est-à-dire une archive `.jar` qui contient toutes les dépendances utiles en dehors du développement :
```sh
gradle uberJar
```
L'archive générée est présente au chemin suivant : `app/build/libs/app-uber.jar`.

Pour exécuter notre archive avec `java -jar ...`, la variable d'environnement `PODWEB_PRODUCTION` doit être définie, elle permet d'indiquer qu'il faut utiliser les versions compilées des templates JTE chargées dans le JAR et non les chercher dans le dossier originel. Il faut également qu'un dossier static soit présent à côté et que le `out.css` soit copié dedans.
```sh
PODWEB_PRODUCTION="" java -jar app/build/libs/app-uber.jar
```

### Image docker
Afin de facilement lancer ou déployer notre application, nous avons créé un `Dockerfile` dédié :

Note : les commandes sont toujours à lancer dans le dossier `podweb`.

Préparatifs et docker build de l'image :
```sh
./prod.sh
```

```sh
docker compose up
```

### Mise en place
Note: Si vous n'avez pas installé Gradle, il suffit de substituer les `gradle` dans les commandes suivantes par `./gradlew` sous Linux ou MacOS (`chmod +x ./gradlew` si erreur d'exécution), et par `.\gradlew` sous Windows (installer Gradle permet de se simplifier un peu la vie).

1. Cloner le repos
    ```sh
    git clone https://github.com/samuelroland/podweb
    ```
1. Pour installer tailwindcss via NPM
    ```sh
    npm install -D tailwindcss
    ```
1. Pour charger le style une première fois (obligatoire lorsqu'il y a du changement dans les pages html ou css)
    ```sh
    npm run prod
    ```
1. Pour configurer la base de données, il suffit de créer un fichier `.env` et d'indiquer les identifiants
   ```env
    DB_PORT=5432
    DB_USER=
    DB_PWD=
   ```
11. Pour lancer notre base de données PostgreSQL (se mettre dans le dossier `podweb`)
     ```sh
     docker compose up -d
     ```
1. Pour lancer l'application directement (build + run)
    ```sh
    gradle run
    ```
1. Ouvrir son navigateur en `localhost:7000`

Autres commandes utiles :

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


### Tricks mis en place pour l'écriture de tests automatisés

Permet de développer plus rapidement et vérifier que ça marche sans devoir constamment rebuilder le serveur et tester dans son navigateur.

1. Toutes les classes de tests contenant des tests sur des actions faisant des écritures dans la base de données doivent configurer les requêtes pour être lancés dans des transactions SQL et rollback à la fin. Ceci permet d'avoir toujours la même base de données fraîche et non modifiée au début de chaque test !
    ```java
    @BeforeEach
    public void setup() throws SQLException {
        Query.startTransaction();
    }

    @AfterEach
    public void finish() throws SQLException {
        Query.rollback();
    }
    ```

1. Se connecter programmatiquement
    ```java
    AppTest.actingAs(1);    //User 1 is Eulalia Botsford
    ```
1. Tester qu'un bout de texte se trouve bien dans la page retournée. Permet de savoir si les données utiles sont bien présentes.
    ```java
    var res = client.get("/login");
    assertThat(res.body().string()).contains("<h1>Login").contains("<input").contains("Submit");
    ```

## Divers
- Toutes les icones en SVG viennent de [heroicons.com](heroicons.com) sous licence MIT.
