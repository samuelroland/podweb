package podweb.controllers;

import java.util.ArrayList;
import java.util.Map;

import io.javalin.http.Context;
import podweb.App;
import podweb.models.Episode;
import podweb.models.Podcast;
import podweb.models.User;

public class UsersController {
    public void loginPage(Context ctx) {
        renderLoginPage(ctx, false);
    }

//    public void login(Context ctx) {
//        String email = ctx.formParam("email");
//        String password = ctx.formParam("password");
//        User user = User.o.getBy(email);
//        boolean validPwd = false;
//
//        System.out.println("email " + email);
//        System.out.println("pwd " + password);
//        if (user != null && password != null) {
//            validPwd = password.equals(user.password);
//        }
//
//        if (validPwd) {
//            ctx.sessionAttribute("user", user);
//            ctx.redirect("/");
//        } else {
//            renderLoginPage(ctx, true);
//        }
//    }

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
            ctx.render("podcast.jte", Map.of("loggedUser", App.loggedUser(ctx), "user", u));
        } catch (NumberFormatException e) {
            ctx.status(404);
        }
    }

    public void showAllUsers(Context ctx) {
        ArrayList<User> users = User.o.all();
        ctx.render("users.jte", Map.of("loggedUser", App.loggedUser(ctx), "users", users));
    }
}
