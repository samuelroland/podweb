package podweb.controllers;

import podweb.App;
import podweb.models.Episode;
import podweb.models.EpisodeSearch;
import podweb.models.Podcast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import io.javalin.http.Context;

public class PodcastsController {

    public void index(Context ctx) {
        ArrayList<Podcast> podcasts = Podcast.o.all();

        ctx.render("podcasts.jte", Map.of("loggedUser", App.loggedUser(ctx), "podcasts", podcasts));
    }

    public void detailPodcast(Context ctx) {
        try {
            Podcast p = Podcast.o.find(Integer.parseInt(ctx.pathParam("id")));
            ArrayList<Episode> e = Episode.o.getBy("podcast_id",p.id);
            ctx.render("podcast.jte", Map.of("loggedUser", App.loggedUser(ctx), "podcast", p, "episodes", e));
        } catch (NumberFormatException e) {
            ctx.status(404);
        }
    }

    public void search(Context ctx) {
        try {
            String keyword = ctx.queryParam("q");
            ArrayList<EpisodeSearch> e = EpisodeSearch.search(keyword);
            ctx.render("resultSearch.jte", Map.of("loggedUser", App.loggedUser(ctx),"episodes", e));
        } catch (NumberFormatException e) {
            ctx.status(404);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void ranking(Context ctx){

    }
}
