package podweb.models;

public class Categorize extends Model<Categorize> {
    public int category_id;
    public int podcast_id;
    public static Categorize o = new Categorize();

    public static Query<Categorize> q = new Query<>(Categorize.class);

    @Override
    public String table() {
        return "categorize";
    }

    @Override
    public Query<Categorize> getQuery() {
        return q;
    }
}
