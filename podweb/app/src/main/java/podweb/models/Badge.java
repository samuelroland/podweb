package podweb.models;



public class Badge extends Model<Badge>{
    public int id;
    public String name;
    public int point;
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
}
