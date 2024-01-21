package podweb.models;

import java.math.BigDecimal;
import java.util.ArrayList;

public class RankedPodcast{
    public int id;
    public String title;
    public String image;
    public String author;
    public BigDecimal listenings_total;
    
    private static Query<RankedPodcast> q = new Query<>(RankedPodcast.class);

    public static ArrayList<RankedPodcast> ranking(){
        String query = "select * from podcasts_ranking;";
        return q.query(query);
    }

}
