package podweb.models;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class Model<T> {
    private static String[] defaultKeys = new String[] { "id" };

    abstract public String table();

    public String[] intPrimaryKeys() {
        return defaultKeys;
    }

    abstract public Query<T> getQuery();

    public ArrayList<T> all() {
        String query = "SELECT * FROM " + table() + ";";
        return getQuery().query(query);
    }

    // Get By -> useful to filter table entries by one or more integer fields
    public ArrayList<T> getBy(String foreignKey, int id) {
        String query = "SELECT * FROM " + table()
                + " WHERE " + foreignKey + " = ? ;";
        return getQuery().query(query, new Object[] { id });
    }

    public Map<Integer, T> getByIdList(Collection<Integer> ids) {
        makeSureEntityUsesIdField();
        String query = "SELECT * FROM " + table()
                + " WHERE id IN (";

        for (int i = 0; i < ids.size(); i++) {
            if (i > 0)
                query += ",";
            query += "?";
        }
        query += ")";
        ArrayList<T> list = getQuery().query(query, ids.toArray());
        Map<Integer, T> elementsById = new TreeMap<>();
        for (T el : list) {
            try {
                elementsById.put(getClass().getField("id").getInt(el), el);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }
        return elementsById;
    }

    public ArrayList<T> getBy(String[] foreignKeys, Integer[] ids) {
        String query = "SELECT * FROM " + table()
                + " WHERE ";

        for (int i = 0; i < foreignKeys.length; i++) {
            if (i > 0) {
                query += " AND ";
            }
            query += foreignKeys[i] + "= ?";
        }
        return getQuery().query(query, ids);
    }

    // Get first by -> like Get By but only gives the first element
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

    // Really useful ??
    public ArrayList<T> getBy(String foreignKey, Object o) {
        return getBy(foreignKey, new Object[] { o });
    }

    public ArrayList<T> getBy(String foreignKey, Object[] o) {
        String query = "SELECT * FROM " + table()
                + " WHERE " + foreignKey + " = ? ;";
        return getQuery().query(query, o);
    }

    public T getFirstBy(String foreignKey, Object[] o) {
        ArrayList<T> list = getBy(foreignKey, o);
        if (!list.isEmpty()) {
            return list.getFirst();
        }
        return null;
    }

    // Find (by id or by other key fields)
    public T find(int id) {
        makeSureEntityUsesIdField();
        return find("WHERE id = ?", new Object[] { id });
    }

    public T find(Map<String, Integer> fields) {
        return find(buildWhereClauseWithKeysMap(fields.keySet()), valuesOrderedByKey(fields).toArray());
    }

    private T find(String whereClause, Object[] values) {
        String query = "SELECT * FROM " + table() + " " + whereClause;

        ArrayList<T> list = getQuery().query(query, values);
        if (list != null && list.size() == 1) {
            return list.getFirst();
        }
        return null;
    }

    // Exists (whether find() returns null)
    public boolean exists(int id) {
        return find(id) != null;
    }

    public boolean exists(Map<String, Integer> fields) {
        return find(fields) != null;
    }

    // Simple table count
    public int count() {
        return Query.count(table());
    }

    // Create an element with attributes on the current object
    public boolean create() {
        String query = "INSERT INTO " + table() + " (";

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
        Integer nb = getQuery().update(query, this);
        if (nb > 0) {
            if (hasId()) {
                try {
                    // Try to set id (it may fails)
                    getClass().getField("id").set(this, nb);
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
                        | SecurityException e) {
                    e.printStackTrace();
                }
            }
        } else if (nb == 0) {
            return false; // not an id but 0 line affected so action failed
        }
        return true;
    }

    // Update all attributes of current object in DB
    // It does not update any key fields (in intPrimaryKeys())
    public boolean update() {
        String query = "UPDATE " + table() + "\nSET ";

        String attributes = "";
        int i = 0;
        LinkedList<Object> values = new LinkedList<>();
        Set<String> excludeKeys = new HashSet<String>();
        excludeKeys.add("id");
        excludeKeys.add("o");
        excludeKeys.add("q");
        for (Field field : getQuery().ref.getFields()) {
            if (excludeKeys.contains(field.getName()))
                continue;

            if (i > 0) {
                attributes += ", ";
            }
            attributes += field.getName() + " = ?";
            try {
                values.add(field.get(this));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            i++;
        }

        query += attributes + "\n" + buildWhereClauseAndFillValuesWithInstance(values);

        int nb = getQuery().update(query, values.toArray());
        return nb == 1;
    }

    // Returns the element given by id or the element loaded in the current object
    // Returns false on fails (not found or other errors like foreign key errors)
    // Returns true if one line was affected (if there are more lines this is a bug)
    public boolean deleteById(int id) {
        return getQuery().update("DELETE FROM " + table()
                + " WHERE id = ?", new Object[] { id }) == 1;
    }

    public boolean delete() {
        ArrayList<Object> params = new ArrayList<>();
        var nb = getQuery().update("DELETE FROM " + table() + " " +
                buildWhereClauseAndFillValuesWithInstance(params), params.toArray());
        return nb == 1;
    }

    // It looks at the object, build the where clause for unique selection and fill
    // param values on given collection reference
    private String buildWhereClauseAndFillValuesWithInstance(Collection<Object> values) {
        String whereClause;

        // If it has an id, it's easy
        if (hasId()) {
            whereClause = "WHERE id = ?";
            try {
                Field field = getClass().getField("id");
                field.setAccessible(true);
                values.add(field.getInt(this));
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
                throw new RuntimeException("The class " + getClass().getSimpleName()
                        + " should have an 'int id' field or defines other keys");
            }
            return whereClause;
        } else {
            // Othwerise we have to take other keys to identify the element
            LinkedList<String> fields = new LinkedList<>();
            for (String key : intPrimaryKeys()) {
                try {
                    fields.add(key);
                    values.add(getClass().getField(key).getInt(this));
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
                        | SecurityException e) {
                    e.printStackTrace();
                }
            }
            return buildWhereClauseWithKeysMap(fields);
        }
    }

    // --- Private helpers for various operations

    // Given a map of int indexed by string like {"user_id": 2, "episode_id": 3}
    // it will check that all required keys for this entity (returned by
    // intPrimaryKeys()) are present in the map
    // It will then return the where clause "where user_id = ? and episode_id = ?"
    private String buildWhereClauseWithKeysMap(Collection<String> fields) {
        String queryPart = "WHERE ";
        int count = 0;
        String[] keys = intPrimaryKeys();
        for (String key : keys) {
            if (!fields.contains(key))
                throw new RuntimeException(
                        "Model.buildWhereClauseWithKeysMap(): field "
                                + key + " not found in given Map but necessary to find a unique element on " + table());
            if (count++ > 0) {
                queryPart += " and ";
            }
            queryPart += key + " = ?";
        }

        return queryPart;
    }

    // Returns a list of values read from the given map
    // in the order defined by intPrimaryKeys()
    // This makes sure we have the same order that buildWhereClauseWithKeysMap()
    private ArrayList<Object> valuesOrderedByKey(Map<String, Integer> fields) {
        ArrayList<Object> values = new ArrayList<>();
        for (String key : intPrimaryKeys()) {
            values.add(fields.get(key));
        }
        return values;
    }

    // Make sure the current uses the id field as a primary key
    // Useful to catch code error when using wrong methods
    private void makeSureEntityUsesIdField() {
        // Checking we don't use this method in case the primary key is not "id"
        String[] keys = intPrimaryKeys();
        if (keys.length == 0 || !keys[0].equals("id")) {
            throw new RuntimeException(
                    "Using Model.find() on model without an 'id' is not possible. Use the Map variant.");
        }
    }

    // Returns true in case the entity has an entity
    // (by looking if the default keys list is used)
    private boolean hasId() {
        return intPrimaryKeys() == defaultKeys;
    }
}
