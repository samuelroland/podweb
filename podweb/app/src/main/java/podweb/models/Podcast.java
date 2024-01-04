package podweb.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Podcast {
	public int id;
	public String title;
	public String description;
	public String rss_feed;
	public String image;
	public String author;
	public int episodes_count;

	public static ArrayList<Podcast> all() {
		String query = "select * from podcasts;";
		ResultSet set = Query.query(query);
		ArrayList<Podcast> podcasts = new ArrayList<>();
		if (set == null) {
			System.out.println("Set is null");
			return podcasts;
		}
		try {
			System.out.println("Found elements !");
			while (set.next()) {
				var p = new Podcast();
				p.id = set.getInt("id");
				p.title = set.getString("title");
				p.description = set.getString("description");
				p.rss_feed = set.getString("rss_feed");
				p.image = set.getString("image");
				p.author = set.getString("author");
				p.episodes_count = set.getInt("episodes_count");
				podcasts.add(p);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return podcasts;
	}

	public boolean exists(int id) {
		String query = "select top 1 from podcasts where id = :id";
		TreeMap<Integer, Object> params = new TreeMap<>();
		params.put(1, 2);

		return Query.query(query, new Object[] { id }) == null;
	}
}
