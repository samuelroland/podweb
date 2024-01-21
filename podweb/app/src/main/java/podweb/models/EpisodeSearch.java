package podweb.models;

import java.sql.SQLException;
import java.util.ArrayList;

public class EpisodeSearch extends Episode {
    public String podcast_title;
    public String podcast_description;
    public String podcast_author;
    public String podcast_image;

    private static Query<EpisodeSearch> q = new Query<>(EpisodeSearch.class);

    public static ArrayList<EpisodeSearch> search(String keyword) throws SQLException {
        String query = "SELECT " +
                "e.id AS id " +
                ", e.title AS title " +
                ", e.description AS description " +
                ", e.duration AS duration " +
                ", e.released_at AS released_at " +
                ", e.audio_url AS audio_url " +
                ", e.podcast_id AS podcast_id " +
                ", p.title AS podcast_title " +
                ", p.description AS podcast_description " +
                ", p.author AS podcast_author " +
                ", p.image AS podcast_image " +
                " FROM episodes e " +
                "INNER JOIN podcasts p ON e.podcast_id = p.id " +
                "WHERE p.title ILIKE ? " +
                "OR e.title ILIKE ? " +
                "OR p.description ILIKE ? " +
                "OR e.description ILIKE ? " +
                "OR p.author ILIKE ? " +
                "LIMIT 10;";

        String[] texts = new String[5];
        for (int i = 0; i < 5; i++) {
            texts[i] = "%" + keyword + "%";
        }

        return q.query(query, texts);
    }
}
