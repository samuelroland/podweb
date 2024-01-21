package podweb.models;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class User extends Model<User> {
    public int id;
    public String firstname;
    public String lastname;
    public String email;
    public String password;
    public Timestamp registration_date;
    public static User o = new User();

    private static Query<User> q = new Query<>(User.class);

    @Override
    public String table() {
        return "users";
    }

    @Override
    public Query<User> getQuery() {
        return q;
    }

    public String fullname() {
        return firstname + " " + lastname;
    }

    public String link() {
        return "<a class='user-link' href='/users/" + id + "'>" + fullname() + "</a>";
    }

    public String registration_date() {
        // https://stackoverflow.com/questions/41144296/how-to-convert-timestamp-to-appropriate-date-format
        DateFormat f = new SimpleDateFormat("dd.MM.YY HH:mm");
        return f.format(registration_date);
    }
}
