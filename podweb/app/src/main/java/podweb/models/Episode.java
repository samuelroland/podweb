package podweb.models;

import java.sql.Date;
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

    public static ArrayList<Episode> getAllFromPodcast(int podcast_id) {
        String query = "SELECT * FROM episodes WHERE podcast_id = " + podcast_id + ";";
        ResultSet set = Query.query(query);
        ArrayList<Episode> episodes = new ArrayList<>();
        if (set == null) {
            System.out.println("Set is null");
            return episodes;
        }
        try {
            System.out.println("Found elements !");
            while (set.next()) {
                var e = new Episode();
                e.id = set.getInt("id");
                e.title = set.getString("title");
                e.description = set.getString("description");
                e.duration = set.getInt("duration");
                e.released_at = set.getDate("released_at");
                e.audio_url = set.getString("audio_url");
                e.podcast_id = set.getInt("podcast_id");
                episodes.add(e);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return episodes;
    }

//    public static ArrayList<Episode> search(String keyword){
//        String query = "SELECT * FROM episodes WHERE podcast_id = " + podcast_id + ";";
//        ResultSet set = Query.query(query);
//        ArrayList<Episode> episodes = new ArrayList<>();
//        if (set == null) {
//            System.out.println("Set is null");
//            return episodes;
//        }
//        try {
//            System.out.println("Found elements !");
//            while (set.next()) {
//                var e = new Episode();
//                e.id = set.getInt("id");
//                e.title = set.getString("title");
//                e.description = set.getString("description");
//                e.duration = set.getInt("duration");
//                e.released_at = set.getDate("released_at");
//                e.audio_url = set.getString("audio_url");
//                e.podcast_id = set.getInt("podcast_id");
//                episodes.add(e);
//            }
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return episodes;
//    }
}
