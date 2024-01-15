package podweb.models;

import java.sql.SQLException;
import java.util.ArrayList;

public class Obtain extends Model<Obtain>{
    int badge_id;
    int user_id;

    public static Query<Obtain> q = new Query<>(Obtain.class);
    public static Obtain o = new Obtain();


    @Override
    public String table() {
        return "obtain";
    }

    @Override
    public Query<Obtain> getQuery() {
        return q;
    }


}
