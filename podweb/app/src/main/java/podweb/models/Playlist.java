package podweb.models;

public class Playlist extends Model<Playlist>{
    public int id;
    public String name;
    public String description;
    public int user_id;
    public static Playlist o = new Playlist();

    public static Query<Playlist> q = new Query<>(Playlist.class);

    @Override
    public String table(){
        return "playlists";
    }

    @Override
    public Query<Playlist> getQuery(){
        return q;
    }

}
