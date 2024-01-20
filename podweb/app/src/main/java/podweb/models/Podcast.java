package podweb.models;

import java.util.ArrayList;

public class Podcast extends Model<Podcast> {
    public String title;
    public String description;
    public String rss_feed;
    public String image;
    public String author;
    public int episodes_count;
    public static Podcast o = new Podcast();

    public static Query<Podcast> q = new Query<>(Podcast.class);

    @Override
    public String table() {
        return "podcasts";
    }

    @Override
    public Query<Podcast> getQuery() {
        return q;
    }

    public String toString() {
        return id + " " + title + " " + author;
    }


    public static ArrayList<Podcast> ranking(){
        String query = "select * from podcasts_ranking;";
        return q.query(query);
    }
}
