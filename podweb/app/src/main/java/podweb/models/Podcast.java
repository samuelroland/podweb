package podweb.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

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
        return q.query("select * from podcasts where id = ?", Map.of("id", id)).getFirst();
    }

    public boolean exists(int id) {
        String query = "select top 1 from podcasts where id = " + id + ";";
        TreeMap<Integer, Object> params = new TreeMap<>();
        params.put(1, 2);

        return q.query(query, Map.of("id", id, "a", true, "dd", "asdf")) == null;
    }

    public String toString() {
        return id + " " + title + " " + image;
    }
}
