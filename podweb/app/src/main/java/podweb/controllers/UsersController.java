package podweb.controllers;

import java.util.ArrayList;
import java.util.Map;

import io.javalin.http.Context;
import podweb.App;
import podweb.models.Badge;
import podweb.models.Playlist;
import podweb.models.User;

public class UsersController {
    public void loginPage(Context ctx) {
        renderLoginPage(ctx, false);
    }

    public void login(Context ctx) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        User user = User.o.getFirstBy("email", email);
        boolean validPwd = false;

        if (user != null && password != null) {
            validPwd = password.equals(user.password);
        }

        if (validPwd) {
            ctx.sessionAttribute("user", user);
            ctx.redirect("/");
        } else {
            renderLoginPage(ctx, true);
        }
    }

    private void renderLoginPage(Context ctx, boolean error) {
        ctx.render("login.jte", Map.of("error", error));
    }

    public void logout(Context ctx) {
        App.testingLoggedUser = null;
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    public void showProfile(Context ctx) {

        try {
            User u = User.o.find(Integer.parseInt(ctx.pathParam("id")));
            ArrayList<Badge> b = Badge.byUser(u.id);
            ArrayList<Playlist> p = Playlist.o.getBy("user_id", u.id);
            ctx.render("user.jte", Map.of("loggedUser", App.loggedUser(ctx), "user", u, "badges", b, "playlists", p));
        } catch (NumberFormatException e) {
            ctx.status(404);
        }
    }

    public void showAllUsers(Context ctx) {
        ArrayList<User> users = User.o.all();
        ctx.render("users.jte", Map.of("loggedUser", App.loggedUser(ctx), "users", users));
    }
}
