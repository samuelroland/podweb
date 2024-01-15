package podweb.models;

import java.security.Timestamp;

public class Comment extends Model<Comment>{
    public int id;
    public int user_id;
    public int episode_id;
    public int parent_id;
    public int note;
    public String content;
    public Timestamp date;
    public static Comment o = new Comment();

    private static Query<Comment> q = new Query<>(Comment.class);
    
    @Override
    public String table(){
        return "comment";
    }

    @Override
    public Query<Comment> getQuery(){
        return q;
    }
}
