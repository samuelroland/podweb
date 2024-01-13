package podweb.models;

import java.sql.SQLException;
import java.util.ArrayList;

public class EpisodeSearch {
    public int id;
    public String title;
    public String description;
    public int duration;
    public String released_at;
    public String audio_url;
    public int podcast_id;
    public String podcast_title;
    public String podcast_description;
    public String podcast_author;

    private static Query<EpisodeSearch> q = new Query<>(EpisodeSearch.class);

    public EpisodeSearch() {
    }

    public EpisodeSearch(int id, String title, String description, int duration, String released_at, String audio_url,
            int podcast_id, String podcast_title, String podcast_description, String podcast_author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.released_at = released_at;
        this.audio_url = audio_url;
        this.podcast_id = podcast_id;
        this.podcast_title = podcast_title;
        this.podcast_description = podcast_description;
        this.podcast_author = podcast_author;
    }

    @Override
    public String toString() {
        return "EpisodeSearch [audio_url=" + audio_url + ", description=" + description + ", duration=" + duration
                + ", id=" + id + ", podcast_author=" + podcast_author + ", podcast_description=" + podcast_description
                + ", podcast_id=" + podcast_id + ", podcast_title=" + podcast_title + ", released_at=" + released_at
                + ", title=" + title + "]";
    }

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
                " FROM episodes e " +
                "INNER JOIN podcasts p ON e.podcast_id = p.id " +
                "WHERE p.title LIKE ? " +
                "OR e.title LIKE ? " +
                "OR p.description LIKE ? " +
                "OR e.description LIKE ? " +
                "OR p.author LIKE ? " +
                "LIMIT 10;";

        String[] texts = new String[5];
        for (int i = 0; i < 5; i++) {
            texts[i] = "%" + keyword + "%";
        }

        return q.query(query, texts);
    }
}
