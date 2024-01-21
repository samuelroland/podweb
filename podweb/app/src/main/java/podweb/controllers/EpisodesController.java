package podweb.controllers;

import podweb.App;
import podweb.models.Comment;
import podweb.models.Episode;
import podweb.models.Listen;
import podweb.models.Podcast;
import podweb.models.User;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import io.javalin.http.Context;

public class EpisodesController {
    public void episodeDetails(Context ctx) {
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
            ctx.render("episode.jte",
                    Map.of("loggedUser", App.loggedUser(ctx),
                            "episode", e, "comments", comments,
                            "authors", authors,
                            "commentsLevelById", commentsLevelById));
        } catch (NumberFormatException e) {
            ctx.status(404);
        }
    }

    // Update listen progression for a given episode for the logged user
    // possibly update the listening count too if progression is >= episode duration
    public void updateListenProgression(Context ctx) {
        if (!App.logged(ctx)) {
            ctx.result("Logged out");
            return;
        }

        int user_id = ((User) App.loggedUser(ctx)).id;

        Episode episode = Episode.o.find(Integer.parseInt(ctx.pathParam("id")));
        if (episode == null) {
            ctx.result("Episode not found");
            return;
        }

        try {
            int progression = Integer.parseInt(ctx.formParam("progression"));
            System.out.println("episode id" + episode.id + " and userid " + user_id);
            Listen listen = Listen.o.find(Map.of("user_id", user_id, "episode_id", episode.id));

            System.out.println("goooo");
            boolean newListen = false;
            if (listen == null) {
                newListen = true;
                listen = new Listen();
                listen.episode_id = episode.id;
                listen.user_id = user_id;
                listen.listening_count = 0;
            }
            if (progression >= episode.duration) {
                listen.progression = 0;
                listen.listening_count++;
            } else {
                listen.progression = progression;
            }

            System.out.println("fix it !" + newListen);
            if (newListen) {
                listen.create();
            } else {
                listen.update();
            }
            ctx.result("success!");
        } catch (NumberFormatException e) {
            ctx.result("Invalid progression");
            return;
        }
    }
}
