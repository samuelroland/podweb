package podweb.controllers;

import podweb.models.Episode;
import podweb.models.User;
import podweb.models.Podcast;

import java.util.ArrayList;
import java.util.Map;

import io.javalin.http.Context;

public class PodcastsController {
    public void index(Context ctx) {
        ArrayList<Podcast> podcasts = Podcast.all();
        User fakeUser = new User();
        fakeUser.firstname = "John";
        fakeUser.lastname = "Milan";
        ctx.render("podcasts.jte", Map.of("loggedUser", fakeUser, "podcasts", podcasts));
    }

    public void detailPodcast(Context ctx) {
        try {
            Podcast p = Podcast.getPodcast(Integer.parseInt(ctx.pathParam("id")));
            ArrayList<Episode> e = Episode.getAllFromPodcast(p.id);
            ctx.render("detailPodcast.jte", Map.of("podcast", p, "episodes", e));
        } catch (NumberFormatException e) {
            ctx.status(404);
        }
    }

    // public void search(Context ctx) {
    // String keyword = ctx.queryParam("keyword");
    // ArrayList<Episode> e = Episode.search(keyword);
    // ctx.render("resultSearch.jte", Map.of("episodes", e));
    //
    //
    // }
}
