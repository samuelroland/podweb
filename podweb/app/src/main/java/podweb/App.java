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
            if (System.getenv("PODWEB_PRODUCTION") != null) {
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

        // Auth routes
        UsersController usersController = new UsersController();
        app.get("/login", usersController::loginPage);
        //app.post("/login", usersController::login);
        app.get("/logout", usersController::logout);

        // Users related routes
        app.get("/user/{id}", usersController::showProfile);
        app.get("/user", usersController::showAllUsers);
        return app;
    }

    // Configuration of JTE templates
    // Taken from the Javalin tutorials:
    // https://javalin.io/tutorials/jte#precompiling-templates
    private static TemplateEngine createTemplateEngine() {
        if (System.getenv("PODWEB_PRODUCTION") != null) {
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
}
