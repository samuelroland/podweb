package podweb.models;

import java.sql.Timestamp;

public class Comment extends Model<Comment>{
    public int id;
    public int user_id;
    public int episode_id;
    public Integer parent_id;
    public int note;
    public String content;
    public Timestamp date;
    public static Comment o = new Comment();

    private static Query<Comment> q = new Query<>(Comment.class);
    
    @Override
    public String table(){
        return "comments";
    }

    @Override
    public Query<Comment> getQuery(){
        return q;
    }
}
