package podweb.models;

import java.sql.Timestamp;

public class Episode extends Model<Episode>{
    public int id;
    public String title;
    public String description;
    public int duration;
    public Timestamp released_at;
    public String audio_url;
    public int podcast_id;
    public static Episode o = new Episode();

    private static Query<Episode> q = new Query<>(Episode.class);

    @Override
    public String table(){
        return "episodes";
    }

    @Override
    public Query<Episode> getQuery(){
        return q;
    }
}