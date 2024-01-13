package podweb.models;

import java.sql.Timestamp;
import java.util.ArrayList;

public class User {
    public Integer id;
    public String firstname;
    public String lastname;
    public String email;
    public String password;
    public Timestamp registration_date;

    private static Query<User> q = new Query<>(User.class);

    public static ArrayList<User> all() {
        return q.query("select * from users;");
    }

    public static User find(int id) {
        ArrayList<User> list = q.query("select * from users where id = ?", new Object[] { id });
        if (list != null && list.size() == 1) {
            return list.getFirst();
        }
        return null;
    }

    public static User findByEmail(String email) {
        ArrayList<User> list = q.query("select * from users where email = ?", new Object[] { email });
        if (list != null && list.size() == 1) {
            return list.getFirst();
        }
        return null;
    }

    public static boolean exists(int id) {
        return q.query("select top 1 from users where id = ?", new Object[] { id }) == null;
    }
}
