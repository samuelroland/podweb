package podweb.models;

import java.sql.Timestamp;

public class User extends Model<User>{
    public Integer id;
    public String firstname;
    public String lastname;
    public String email;
    public String password;
    public Timestamp registration_date;
    public static User o = new User();

    private static Query<User> q = new Query<>(User.class);

    @Override
    public String table(){
        return "users";
    }

    @Override
    public Query<User> getQuery(){
        return q;
    }
}
