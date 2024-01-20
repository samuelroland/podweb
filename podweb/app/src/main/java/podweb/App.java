/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package podweb;

import java.nio.file.Path;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinJte;
import podweb.controllers.*;
import podweb.models.User;

public class App {
    static final int PORT = 7000;
    static Javalin server;
    public static Object testingLoggedUser = null; // the logged user in testing

    public static void main(String[] args) {
        System.out.println("Podweb server has started...");
        server = setupApp().start(PORT);

        server.before(ctx -> {
            if (!ctx.path().endsWith(".css") && !ctx.path().endsWith(".ico"))
                System.out.println("New " + ctx.method() + " request on " + ctx.path());
        });
    }

    // Separated method to easily test the server
    public static Javalin setupApp() {
        JavalinJte.init(createTemplateEngine());

        // Search static files inside the default folder in dev and just a "static"
        // folder in production
        Javalin app = Javalin.create(config -> {
            String folder = "src/main/static";
            if (App.isProduction()) {
                folder = "static";
            }
            config.staticFiles.add(folder, Location.EXTERNAL);
        });

        // Defines routes

        // Podcasts related routes
        PodcastsController podcastsController = new PodcastsController();
        app.get("/", podcastsController::index);
        app.get("/podcasts/{id}", podcastsController::detailPodcast);
        app.get("/search", podcastsController::search);
        app.get("/ranking", podcastsController::ranking);

        // Episodes related routes (mostly comments)
        app.get("/episodes/{id}/comments", podcastsController::comments);
        app.post("/episodes/{id}/comments", podcastsController::addComment);
        app.delete("/episodes/{id1}/comments/{id2}", podcastsController::deleteComment);

        // Auth routes
        UsersController usersController = new UsersController();
        app.get("/login", usersController::loginPage);
        app.post("/login", usersController::login);
        app.get("/logout", usersController::logout);

        // Users related routes
        app.get("/users/{id}", usersController::showProfile);
        app.get("/users", usersController::showAllUsers);

        manageErrorPages(app);
        return app;
    }

    private static void manageErrorPages(Javalin app) {
        // Do not show these sensitive errors in production
        if (isProduction())
            return;

        // Affichage d'une joli page avec l'erreur et sa stack trace
        // très utile pour le debug
        app.exception(Exception.class, (e, ctx) -> {
            String msg = "<div style='font-family: monospace; font-size: 1.5em;'><h1>Java exception</h1>";
            msg += "\n<h2>" + e.toString() + "</h2>\n";
            for (var element : e.getStackTrace()) {
                boolean bold = element.getClassName().startsWith("podweb");
                msg += "<br>" + (bold ? ("<strong>" + element + "</strong>") : element);
            }
            msg += "</div>";
            ctx.html(msg);
            ctx.status(500);
        });

        // Stack trace on 404 error
        app.exception(HttpResponseException.class, (e, ctx) -> {
            String msg = "<div style='font-family: monospace; font-size: 1.5em;'><h1>HttpResponseException</h1>";
            msg += "\n<h2>" + e.toString() + "</h2>\n";
            for (var element : e.getStackTrace()) {
                boolean bold = element.getClassName().startsWith("podweb");
                msg += "<br>" + (bold ? ("<strong>" + element + "</strong>") : element);
            }
            msg += "</div>";
            ctx.html(msg);
            ctx.status(404);
        });

    }

    // Configuration of JTE templates
    // Taken from the Javalin tutorials:
    // https://javalin.io/tutorials/jte#precompiling-templates
    private static TemplateEngine createTemplateEngine() {
        if (isProduction()) {
            // Production mode, use precompiled classes loaded in the JAR
            return TemplateEngine.createPrecompiled(Path.of("jte-classes"), ContentType.Html);
        } else {
            // Dev mode, compile on the fly templates in the default folder src/main/jte
            DirectoryCodeResolver codeResolver = new DirectoryCodeResolver(Path.of("src", "main", "jte"));
            return TemplateEngine.create(codeResolver, ContentType.Html);
        }
    }

    public static Object loggedUser(Context ctx) {
        // Hacky way to login as another user during testing
        if (testingLoggedUser != null) {
            return testingLoggedUser;
        }

        Object possibleUser = ctx.req().getSession().getAttribute("user");
        if (possibleUser == null)
            return 1; // just not a user as Map.of doesn't support null values
        return (User) possibleUser;
    }

    public static boolean isProduction() {
        return System.getenv("PODWEB_PRODUCTION") != null;
    }
}
