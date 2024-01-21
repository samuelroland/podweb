package podweb.controllers;

import io.javalin.http.Context;
import podweb.App;
import podweb.models.Comment;
import podweb.models.User;

import java.util.Objects;

public class CommentController {
    public void addComment(Context ctx) {
        if (!App.logged(ctx)) {
            ctx.render("Logged out, cannot comment.");
            return;
        }
        Comment comment = new Comment();
        comment.content = ctx.formParam("content");
        if (ctx.formParam("note") != null && !ctx.formParam("note").trim().isEmpty()) {
            comment.note = Integer.parseInt(Objects.requireNonNull(ctx.formParam("note")));
        } else {
            comment.note = 0;
        }
        comment.episode_id = Integer.parseInt(Objects.requireNonNull(ctx.formParam("episode_id")));
        comment.user_id = ((User) App.loggedUser(ctx)).id;
        comment.parent_id = null;
        if (ctx.formParam("parent_id") != null && !ctx.formParam("parent_id").trim().isEmpty()) {
            comment.parent_id = Integer.parseInt(ctx.formParam("parent_id"));
        }
        comment.date = java.sql.Timestamp.valueOf(java.time.LocalDateTime.now());
        if (comment.create()) {
            ctx.redirect("/episodes/" + comment.episode_id);
        } else {
            ctx.result("Failed to create comment...");
        }

    }

    public void deleteComment(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Comment comment = Comment.o.find(id);
            if (comment.delete()) {
                ctx.redirect("/episodes/" + comment.episode_id);
            } else {
                ctx.status(400);
                ctx.result("Error: Cannot delete comment.");
            }
        } catch (NumberFormatException e) {
            System.err.println(e);
            ctx.status(400);
        }
    }
}
