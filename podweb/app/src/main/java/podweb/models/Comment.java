package podweb.models;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Comment extends Model<Comment> {
    public int id;
    public int user_id;
    public int episode_id;
    public Integer parent_id;
    public int note;
    public String content;
    public Timestamp date;
    public static Comment o = new Comment();

    private static Query<Comment> q = new Query<>(Comment.class);

    @Override
    public String table() {
        return "comments";
    }

    @Override
    public Query<Comment> getQuery() {
        return q;
    }

    // Get comments for a given episodes but ordered by parent first then
    // subcomments so it is directly in the correct order for the view
    // (only level needs to be calculated in controller)
    // https://stackoverflow.com/questions/48468366/group-by-id-followed-by-parent-id
    public static ArrayList<Comment> getByEpisodesSortedByParentFirst(int episodeId) {
        String query = "SELECT * FROM comments WHERE episode_id = ? ORDER BY CASE WHEN parent_id IS NULL THEN id ELSE parent_id END, id";
        return q.query(query, new Object[] { episodeId });
    }

    public String date() {
        // https://stackoverflow.com/questions/41144296/how-to-convert-timestamp-to-appropriate-date-format
        DateFormat f = new SimpleDateFormat("dd.MM.YY HH:mm");
        return f.format(date);
    }
}
