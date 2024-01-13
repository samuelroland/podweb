package podweb.models;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Query<T> {
    Class<T> ref;
    private static Connection connection;

    public Query(Class<T> ref) {
        this.ref = ref;
    }

    static private void setup() throws SQLException {
        // Establish a connection to the database
        String url = "jdbc:postgresql://localhost:5432/?options=-c%20search_path=podweb%20";
        String username = "postgres";
        String password = "postgres";
        connection = DriverManager.getConnection(url, username, password);
    }

    // TODO: add try with resources
    public ArrayList<T> query(String query) {
        return query(query, null);
    }

    public ArrayList<T> query(String query, Object object) {
        return query(query, objectFieldsToArray(object));
    }

    public ArrayList<T> query(String query, Object[] list) {
        try {
            setup();
            PreparedStatement statement = connection.prepareStatement(query);
            if (list != null)
                applyParamsOnStatement(statement, list);
            ResultSet result = statement.executeQuery();
            return resultToObjects(result);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public int update(String query, Object object) {
        return update(query, objectFieldsToArray(object));
    }

    public int update(String query, Object[] list) {
        try {
            setup();
            PreparedStatement statement = connection.prepareStatement(query);
            if (list != null)
                applyParamsOnStatement(statement, list);
            return statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
        return -1;
    }

    private static void applyParamsOnStatement(PreparedStatement statement, Object[] list)
            throws SQLException {
        int cnt = 1;
        for (Object object : list) {
            if (object instanceof Integer o) {
                statement.setInt(cnt++, o);
            }
            if (object instanceof Double o) {
                statement.setDouble(cnt++, o);
            }
            if (object instanceof String o) {
                statement.setString(cnt++, o);
            }
        }
    }

    private static Object[] objectFieldsToArray(Object object) {
        ArrayList<Object> list = new ArrayList<>();
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                list.add(field.get(object));
            }
        } catch (Exception e) {
            System.out.println("Query object to map error: " + e);
        }

        return list.toArray();
    }

    private ArrayList<T> resultToObjects(ResultSet set) {
        ArrayList<T> items = new ArrayList<>();

        Field[] fields = ref.getFields();
        if (set == null) {
            return items;
        }
        try {
            while (set.next()) {
                Constructor<T> ctor = ref.getConstructor();
                T item = ctor.newInstance();
                for (Field field : fields) {
                    try {
                        set.findColumn(field.getName());
                        field.set(item, set.getObject(field.getName()));
                    } catch (SQLException e) {
                        // The field is not found, just skip it
                        System.out.println("field not found " + field.getName() + " : ");
                        continue;
                    }
                }
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }
}
