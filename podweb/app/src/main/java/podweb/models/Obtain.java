package podweb.models;

public class Obtain extends Model<Obtain>{
    int badge_id;
    int user_id;

    private static String[] keys = new String[] { "badge_id", "user_id" };

    public static Query<Obtain> q = new Query<>(Obtain.class);
    public static Obtain o = new Obtain();


    @Override
    public String[] intPrimaryKeys() {
        return keys;
    }

    @Override
    public String table() {
        return "obtain";
    }

    @Override
    public Query<Obtain> getQuery() {
        return q;
    }
}
