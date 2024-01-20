package podweb.models;

import java.util.ArrayList;

public class RankedPodcast{
    public int id;
    public String title;
    public String image;
    public String author;
    public int listenings_total;
    
    private static Query<RankedPodcast> q = new Query<>(RankedPodcast.class);

    public RankedPodcast(){}

    public RankedPodcast(int id, String title, String image, String author, int listenings_total){
        this.id = id;
        this.title = title;
        this.image = image;
        this.author = author;
        this.listenings_total = listenings_total;
    }

    public static ArrayList<RankedPodcast> ranking(){
        String query = "select * from podcasts_ranking;";
        return q.query(query);
    }

}
