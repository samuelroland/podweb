package podweb.models;

import java.util.ArrayList;

public abstract class Model<T> {
    abstract public String table();

    public static Podcast o = new Podcast();

    abstract public Query<T> getQuery();

    public ArrayList<T> all() {
        String query = "select * from " + table() + ";";
        return getQuery().query(query);
    }

    public ArrayList<T> getBy(String foreignKey, int id) {
        String query = "select * from " + table()
                + " where " + foreignKey + " = ? ;";
        return getQuery().query(query, new Object[] { id });
    }

    public ArrayList<T> getBy(String[] foreignKeys, int[] ids) {
        String query = "select * from " + table()
                + " where ";

        for (int i = 0; i < foreignKeys.length; i++) {
            if (i > 0) {
                query += " AND ";
            }
            query += foreignKeys[i] + "= ?";
        }
        return getQuery().query(query, ids);
    }

    public T find(int id) {
        ArrayList<T> list = getQuery().query("select * from " + table()
                + " where id = ?", new Object[] { id });
        if (list != null) {
            return list.getFirst();
        }
        return null;
    }

    public boolean exists(int id) {
        return getQuery().query("select top 1 from " + table() +
                " where id = ?", new Object[] { id }) == null;
    }

    // public T create() {
    // // TODO
    // String foreignKeysList = "";
    // String integerrogationMarks = "";
    // for (int i = 0; i < foreignKeys.length; i++) {
    // if (i > 0) {
    // foreignKeysList += ", ";
    // integerrogationMarks += ", ";
    // }
    // foreignKeysList += foreignKeys[i];
    // integerrogationMarks += "?";
    // }
    // query += "" +
    // }

    public boolean delete(int id) {
        return getQuery().update("delete from " + table()
                + " where id = ?", new Object[] { id }) == 1;
    }
}
