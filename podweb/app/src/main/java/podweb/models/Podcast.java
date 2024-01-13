package podweb.models;

import java.util.ArrayList;
import java.util.Map;

public class Podcast {
    public int id;
    public String title;
    public String description;
    public String rss_feed;
    public String image;
    public String author;
    public int episodes_count;

    private static Query<Podcast> q = new Query<>(Podcast.class);

    public static ArrayList<Podcast> all() {
        return q.query("select * from podcasts;");
    }

    public static Podcast find(int id) {
        ArrayList<Podcast> list = q.query("select * from podcasts where id = ?", new Object[] { id });
        if (list != null) {
            return list.getFirst();
        }
        return null;
    }

    public boolean exists(int id) {
        return q.query("select top 1 from podcasts where id = " + id, new Object[] { id }) == null;
    }

    public String toString() {
        return id + " " + title + " " + image;
    }
}
