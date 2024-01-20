package podweb.controllers;

import podweb.App;
import podweb.models.Comment;
import podweb.models.Episode;
import podweb.models.EpisodeSearch;
import podweb.models.Podcast;
import podweb.models.RankedPodcast;
import podweb.models.User;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

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
            String query = ctx.queryParam("q");
            ArrayList<EpisodeSearch> episodes = new ArrayList<>();
            if (query != null && !query.trim().isEmpty()) {
                episodes = EpisodeSearch.search(query);
            }
            ctx.render("resultSearch.jte",
                    Map.of("loggedUser", App.loggedUser(ctx), "episodes", episodes, "query",
                            query == null ? "" : query.trim()));
        } catch (NumberFormatException e) {
            ctx.status(404);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void ranking(Context ctx) {
        ArrayList<RankedPodcast> rP = RankedPodcast.ranking();
        ctx.render("ranking.jte", Map.of("loggedUser", App.loggedUser(ctx), "rankedPodcasts", rP));
    }

    public void comments(Context ctx) {
        try {
            Episode e = Episode.o.find(Integer.parseInt(ctx.pathParam("id")));
            if (e == null) {
                ctx.render("Episode not found");
                return;
            }

            // Get the episodes grouped by parent_id
            ArrayList<Comment> comments = Comment.getByEpisodesSortedByParentFirst(e.id);

            // Only manage comments and search authors if they are found
            Map<Integer, User> authors = new TreeMap<>();
            Map<Integer, Integer> commentsLevelById = new TreeMap<>();
            if (!comments.isEmpty()) {
                ArrayList<Integer> userIds = new ArrayList<>(comments.size());
                for (var comment : comments) {
                    userIds.add(comment.user_id);

                    // Calculate the level of each comment (level is 1 if parent_id == null,
                    // otherwise "parent level + 1")
                    commentsLevelById.put(comment.id,
                            comment.parent_id != null
                                    && commentsLevelById.containsKey(comment.parent_id)
                                            ? commentsLevelById.get(comment.parent_id) + 1
                                            : 1);
                }
                authors = User.o.getByIdList(userIds);
            }
            ctx.render("comments.jte",
                    Map.of("loggedUser", App.loggedUser(ctx), "episode", e, "comments", comments, "authors", authors,
                            "commentsLevelById", commentsLevelById));
        } catch (NumberFormatException e) {
            ctx.status(404);
        }
    }

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
        // comment.date = Timestamp.valueOf("2023-03-14 14:30:42.123 456 789");
        System.out.println(comment);
        if (comment.create()) {
            ctx.redirect("/episodes/" + comment.episode_id + "/comments");
        } else {
            ctx.result("Failed to create comment...");
        }

    }

    public void deleteComment(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Comment comment = Comment.o.find(id);
            if (comment.delete()) {
                ctx.redirect("/episodes/" + comment.episode_id + "/comments");
            } else {
                ctx.status(400);
                ctx.redirect("/episodes/" + comment.episode_id + "/comments");
            }
        } catch (NumberFormatException e) {
            System.err.println(e);
            ctx.status(400);
        }
    }

}
