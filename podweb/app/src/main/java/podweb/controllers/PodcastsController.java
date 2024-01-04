package podweb.controllers;

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
}
