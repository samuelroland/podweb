package podweb.models;

public class List extends Model<List>{
    public int playlist_id;
    public int episode_id;
    public static List o = new List();

    public static Query<List> q = new Query<>(List.class);

    @Override
    public String table(){
        return "list";
    }

    @Override
    public Query<List> getQuery(){
        return q;
    }
}
