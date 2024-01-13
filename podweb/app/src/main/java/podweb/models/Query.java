package podweb.models;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class Query<T> {
    Class<T> ref;
    private static Config config;
    private static Connection connection = null;

    record Config(String host, String port, String username, String password) {
    }

    static private void loadConfig() {
        Map<String, String> env = System.getenv();
        String host = "localhost";
        if (env.containsKey("DB_HOST")) {
            host = System.getenv("DB_HOST");
        }

        String port = "5432";
        if (env.containsKey("DB_PORT")) {
            port = System.getenv("DB_PORT");
        }

        String username = env.get("DB_USER");
        if (!env.containsKey("DB_USER")) {
            throw new RuntimeException("No env variable DB_USER found ..");
        }

        String password = env.get("DB_PWD");
        if (!env.containsKey("DB_PWD")) {
            throw new RuntimeException("No env variable DB_PWD found ..");
        }

        config = new Config(host, port, username, password);
    }

    public Query(Class<T> ref) {
        this.ref = ref;
    }

    // Static connection initialisation
    {
        setup();
    }

    static void startConnection() {
        loadConfig();
        String url = "jdbc:postgresql://" + config.host + ":" + config.port + "/?options=-c%20search_path=podweb%20";
        try {
            connection = DriverManager.getConnection(url, config.username, config.password);
        } catch (SQLException e) {
            System.out.println("Error on creating the connection: " + e);
            System.out.println("Loaded configuration " + config);
            System.exit(2);
        }
    }

    static void setup() {
        if (connection != null)
            return;
        // Establish a connection to the database
        startConnection();
    }

    void startTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    void commit() throws SQLException {
        connection.commit();
    }

    void rollback() throws SQLException {
        connection.rollback();
    }

    public static int count(String table) {
        try {
            ResultSet set = rawQuery("select count(*) as count from " + table);
            set.next();
            return set.getInt("count");
        } catch (SQLException e) {
            System.out.println("Query.count() error: " + e);
        }
        return -1;
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

    static private ResultSet rawQuery(String query) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();
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
