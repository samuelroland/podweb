package podweb.controllers;

import java.util.Map;

import io.javalin.http.Context;
import podweb.models.User;

public class UsersController {
    public void loginPage(Context ctx) {
        renderLoginPage(ctx, false);
    }

    public void login(Context ctx) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        User user = User.findByEmail(email);
        boolean validPwd = false;

        System.out.println("email " + email);
        System.out.println("pwd " + password);
        if (user != null && password != null) {
            validPwd = password.equals(user.password);
        }

        if (validPwd) {
            ctx.sessionAttribute("user", user);
            // ctx.cookie("logged_user_id", "1");
            ctx.redirect("/");
        } else {
            renderLoginPage(ctx, true);
        }
    }

    private void renderLoginPage(Context ctx, boolean error) {
        ctx.render("login.jte", Map.of("error", error));
    }
}
