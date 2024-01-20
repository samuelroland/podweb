package podweb.models;

public class Queue extends Model<Queue> {
    public int user_id;
    public int episode_id;
    public int index;
    public static Queue o = new Queue();
    
    private static String[] keys = new String[] { "user_id", "episode_id" };
    private static Query<Queue> q = new Query<>(Queue.class);

    @Override
    public String table() {
        return "queue";
    }

    @Override
    public Query<Queue> getQuery() {
        return q;
    }

    @Override
    public String[] intPrimaryKeys() {
        return keys;
    }

    public String toString() {
        return user_id + ":" + episode_id + ":" + index;
    }
}
