package podweb.controllers;

import podweb.App;
import podweb.models.Comment;
import podweb.models.Episode;
import podweb.models.EpisodeSearch;
import podweb.models.Podcast;
import podweb.models.User;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import io.javalin.http.Context;

import static java.lang.Integer.valueOf;

public class PodcastsController {

    public void index(Context ctx) {
        ArrayList<Podcast> podcasts = Podcast.o.all();

        ctx.render("podcasts.jte", Map.of("loggedUser", App.loggedUser(ctx), "podcasts", podcasts));
    }

    public void detailPodcast(Context ctx) {
        try {
            Podcast p = Podcast.o.find(Integer.parseInt(ctx.pathParam("id")));
            ArrayList<Episode> e = Episode.o.getBy("podcast_id", p.id);
            ctx.render("podcast.jte", Map.of("loggedUser", App.loggedUser(ctx), "podcast", p, "episodes", e));
        } catch (NumberFormatException e) {
            ctx.status(404);
        }
    }

    public void search(Context ctx) {
        try {
            String keyword = ctx.queryParam("q");
            ArrayList<EpisodeSearch> e = EpisodeSearch.search(keyword);
            ctx.render("resultSearch.jte", Map.of("loggedUser", App.loggedUser(ctx), "episodes", e));
        } catch (NumberFormatException e) {
            ctx.status(404);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void ranking(Context ctx) {
        ArrayList<Podcast> p = Podcast.ranking();
        ctx.render("ranking.jte", Map.of("loggedUser", App.loggedUser(ctx), "rankedPodcasts", p));
    }

    public void comments(Context ctx) {
        try {
            Episode e = Episode.o.find(Integer.parseInt(ctx.pathParam("id")));
            ArrayList<Comment> comments = Comment.o.getBy("episode_id", e.id);
            ArrayList<Integer> userIds = new ArrayList<>(comments.size());
            for (var comment : comments) {
                userIds.add(comment.user_id);
            }
            Map<Integer, User> authors = User.o.getByIdList(userIds);
            ctx.render("comment.jte",
                    Map.of("loggedUser", App.loggedUser(ctx), "episode", e, "comments", comments, "authors", authors));
        } catch (NumberFormatException e) {
            ctx.status(404);
        }
    }

    public void addComment(Context ctx) {
        Comment comment = new Comment();
        comment.content = ctx.formParam("content");
        comment.note = Integer.parseInt(Objects.requireNonNull(ctx.formParam("note")));
        comment.episode_id = Integer.parseInt(Objects.requireNonNull(ctx.formParam("episode_id")));
        comment.user_id = Integer.parseInt(Objects.requireNonNull(ctx.formParam("user_id")));
        comment.parent_id = Integer.parseInt(Objects.requireNonNull(ctx.formParam("parent_id")));
        comment.date = java.sql.Timestamp.valueOf(java.time.LocalDateTime.now());
        // comment.date = Timestamp.valueOf("2023-03-14 14:30:42.123 456 789");

        if (comment.create()) {
            ctx.status(200);
        } else {
            ctx.status(500);
        }

    }

    public void deleteComment(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id2"));
            if (Comment.o.deleteById(id)) {
                // todo: back to previous page
            } else {
                // todo: previous page with error message
            }
        } catch (NumberFormatException e) {
            System.err.println(e);
            ctx.status(400);
        }
    }

}
