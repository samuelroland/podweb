package podweb.models;

import java.lang.reflect.Field;
import java.util.ArrayList;

public abstract class Model<T> {
    public int id;

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

    public ArrayList<T> getBy(String[] foreignKeys, Integer[] ids) {
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

    public T getFirstBy(String[] foreignKeys, Integer[] ids) {
        ArrayList<T> list = getBy(foreignKeys, ids);
        if (!list.isEmpty()) {
            return list.getFirst();
        }
        return null;
    }

    public T getFirstBy(String foreignKey, Object o) {
        ArrayList<T> list = getBy(foreignKey, new Object[] { o });
        if (!list.isEmpty()) {
            return list.getFirst();
        }
        return null;
    }

    public ArrayList<T> getBy(String foreignKey, Object o) {
        return getBy(foreignKey, new Object[] { o });
    }

    public ArrayList<T> getBy(String foreignKey, Object[] o) {
        String query = "select * from " + table()
                + " where " + foreignKey + " = ? ;";
        return getQuery().query(query, o);
    }

    public T getFirstBy(String foreignKey, Object[] o) {
        ArrayList<T> list = getBy(foreignKey, o);
        if (!list.isEmpty()) {
            return list.getFirst();
        }
        return null;
    }

    public T find(int id) {
        ArrayList<T> list = getQuery().query("select * from " + table()
                + " where id = ?", new Object[] { id });
        if (list != null && list.size() == 1) {
            return list.getFirst();
        }
        return null;
    }

    public boolean exists(int id) {
        // TODO: check sql exists performant way
        ArrayList<T> list = getQuery().query("select 1 from " + table() +
                " where id = ?", new Object[] { id });
        return (list != null && list.size() == 1);
    }

    public int count() {
        return Query.count(table());
    }

    public boolean create() {
        String query = "insert into " + table() + " (";

        String attributes = "";
        String interogationMarks = "";
        int i = 0;
        for (Field field : getQuery().ref.getFields()) {
            if ((field.getName().equals("id") || field.getName().equals("o") || field.getName().equals("q")))
                continue;
            if (i > 0) {
                attributes += ", ";
                interogationMarks += ", ";
            }
            attributes += field.getName();
            interogationMarks += "?";
            i++;
        }
        query += attributes + ") values (" + interogationMarks + ");";
        System.out.println(query);
        int nb = getQuery().update(query, this);
        if (nb > 0) {
            this.id = nb;
        } else if (nb == 0) {
            return false; // not an id but 0 line affected so action failed
        }
        return true;
    }

    public boolean update() {
        String query = "UPDATE " + table() + "\nSET ";

        String attributes = "";
        int i = 0;
        for (Field field : getQuery().ref.getFields()) {
            if ((field.getName().equals("id") || field.getName().equals("o") || field.getName().equals("q")))
                continue;

            if (i > 0) {
                attributes += ", ";
            }
            attributes += field.getName() + " = ?";
            i++;
        }
        query += attributes + "\nWHERE id = ?";
        // TODO: specify ID !!!
        System.out.println(query);
        int nb = getQuery().update(query, this);
        return nb == 1;
    }

    public boolean delete(int id) {
        return getQuery().update("delete from " + table()
                + " where id = ?", new Object[] { id }) == 1;
    }
}
