package podweb.models;

public class Podcast extends Model<Podcast> {
    public int id;
    public String title;
    public String description;
    public String rss_feed;
    public String image;
    public String author;
    public int episodes_count;

    @Override
    public String table() {
        return "podcasts";
    }

    @Override
    public Query<Podcast> getQuery() {
        return new Query<Podcast>(Podcast.class);
    }

    public String toString() {
        return id + " " + title + " " + image;
    }
}
