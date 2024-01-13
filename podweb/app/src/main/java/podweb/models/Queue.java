package podweb.models;

import java.util.ArrayList;

public class Queue {
    public int user_id;
    public int episode_id;

    private static Query<Queue> q = new Query<>(Queue.class);

    public static ArrayList<Queue> all() {
        return q.query("select * from queue;");
    }

    public static Queue find(int id) {
        ArrayList<Queue> list = q.query("select * from queue where id = ?", new Object[] { id });
        if (list != null) {
            return list.getFirst();
        }
        return null;
    }

    public static boolean exists(int id) {
        return q.query("select top 1 from queue where id = ?", new Object[] { id }) == null;
    }

    public static int count() {
        return Query.count("queue");
    }

    public boolean create() {
        return q.update("INSERT INTO queue (user_id, episode_id) VALUES (?, ?);",
                new Object[] { user_id, episode_id }) == 1;
    }

    public String toString() {
        return user_id + ":" + episode_id;
    }
}
