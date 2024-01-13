package podweb.models;

import java.sql.Date;
import java.util.ArrayList;

public class Episode {
    public int id;
    public String title;
    public String description;
    public int duration;
    public Date released_at;
    public String audio_url;
    public int podcast_id;

    private static Query<Episode> q = new Query<>(Episode.class);

    public static ArrayList<Episode> getByPodcast(int id) {
        String query = "SELECT * FROM episodes WHERE podcast_id = ? LIMIT 10;";
        return q.query(query, new Object[] { id });
    }

    public Podcast podcast() {
        return Podcast.find(podcast_id);
    }
}