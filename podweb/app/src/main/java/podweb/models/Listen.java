package podweb.models;

public class Listen extends Model<Listen> {
    public int episode_id;
    public int user_id;
    public int progression;
    public int listening_count;
    public static Listen o = new Listen();
    private static String[] keys = new String[] { "episode_id", "user_id" };

    private static Query<Listen> q = new Query<>(Listen.class);

    public static int updateListeningsCount(int episode_id, int user_id, int progression) {
        return q.update("update listen set progression = ? where episode_id = ? and user_id = ?;",
                new Object[] { progression, episode_id, user_id });
    }

    @Override
    public String table() {
        return "listen";
    }

    @Override
    public String[] intPrimaryKeys() {
        return keys;
    }

    @Override
    public Query<Listen> getQuery() {
        return q;
    }
}
