package podweb.models;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Episode {
    public int id;
    public String title;
    public String description;
    public int duration;
    public Date released_at;
    public String audio_url;
    public int podcast_id;

    private static Episode setAssignement(ResultSet set) throws SQLException {
        var e = new Episode();
        e.id = set.getInt("id");
        e.title = set.getString("title");
        e.description = set.getString("description");
        e.duration = set.getInt("duration");
        e.released_at = set.getDate("released_at");
        e.audio_url = set.getString("audio_url");
        e.podcast_id = set.getInt("podcast_id");
        return e;
    }

    public static ArrayList<Episode> getAllFromPodcast(int podcast_id) throws SQLException {
        String query = "SELECT * FROM episodes WHERE podcast_id = ? LIMIT 10;";
        PreparedStatement preparedStatement = Query.prepareStatement(query);
        preparedStatement.setInt(1, podcast_id);
        ResultSet set = preparedStatement.executeQuery();
        ArrayList<Episode> episodes = new ArrayList<>();
        if (set == null) {
            System.out.println("Set is null");
            return episodes;
        }
        try {
            System.out.println("Found elements !");
            while (set.next()) {
                episodes.add(setAssignement(set));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return episodes;
    }

}
