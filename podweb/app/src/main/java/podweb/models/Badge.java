package podweb.models;

import java.util.ArrayList;

public class Badge extends Model<Badge>{
    public int id;
    public String name;
    public int points;
    public String description;
    public int type;
    public int condition_value;
    public static Badge o = new Badge();

    private static Query<Badge> q = new Query<>(Badge.class);

    @Override
    public String table(){
        return "badges";
    }

    @Override
    public Query<Badge> getQuery(){
        return q;
    }

    public static ArrayList<Badge> byUser(int user_id){
        String query = "SELECT b.* FROM badges b INNER JOIN obtain o ON b.id = o.badge_id WHERE o.user_id = ?;";
        return q.query(query, new Object[] {user_id});
    }
}
