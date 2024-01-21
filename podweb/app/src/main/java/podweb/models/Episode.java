package podweb.models;

import java.sql.Timestamp;
import java.util.Map;

public class Episode extends Model<Episode> {
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
    public String table() {
        return "episodes";
    }

    @Override
    public Query<Episode> getQuery() {
        return q;
    }

    public String duration() {
        // https://stackoverflow.com/questions/266825/how-to-format-a-duration-in-java-e-g-format-hmmss
        return String.format("%d:%02d:%02d", duration / 3600, (duration % 3600) / 60, (duration % 60));
    }

    public Listen listen(int userId) {
        return Listen.o.find(Map.of("episode_id", id, "user_id", userId));
    }
}