package podweb.models;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

public class Query<T> {
    Class<T> ref;
    private static Config config;
    private static Connection connection = null;

    record Config(String host, String port, String database, String username, String password) {
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

        String database = "podweb";
        if(env.containsKey("DB_NAME")) {
            database = System.getenv("DB_NAME");
        }


        String username = env.get("DB_USER");
        if (!env.containsKey("DB_USER")) {
            throw new RuntimeException("No env variable DB_USER found ..");
        }

        String password = env.get("DB_PWD");
        if (!env.containsKey("DB_PWD")) {
            throw new RuntimeException("No env variable DB_PWD found ..");
        }

        config = new Config(host, port, database, username, password);
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
        String url = "jdbc:postgresql://" + config.host + ":" + config.port + "/" + config.database + "?options=-c%20search_path=podweb%20";
        try {
            connection = DriverManager.getConnection(url, config.username, config.password);
        } catch (SQLException e) {
            System.out.println("Error on creating the connection: " + e);
            System.out.println("Loaded configuration " + config);
            System.exit(2);
        }
    }

    static private void setup() {
        if (connection != null)
            return;
        // Establish a connection to the database
        startConnection();
    }

    public static void startTransaction() throws SQLException {
        if (connection == null)
            setup();
        connection.setAutoCommit(false);
    }

    public static void commit() throws SQLException {
        if (connection == null)
            setup();
        connection.commit();
    }

    public static void rollback() throws SQLException {
        if (connection == null)
            setup();
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

    public ArrayList<T> query(String query) {
        return query(query, null);
    }

    public ArrayList<T> query(String query, Object object) {
        return query(query, objectFieldsToArray(object));
    }

    public ArrayList<T> query(String query, Object[] list) {
        System.out.println("Query(): " + query);
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
        boolean isInsert = query.toLowerCase().startsWith("insert into");

        try {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            if (list != null)
                applyParamsOnStatement(statement, list);
            System.out.println("Query update: " + query);
            System.out.println("Params: ");
            for (Object object : list) {
                System.out.print(object + " ");
            }
            int affectedRows = statement.executeUpdate();
            if (isInsert) {
                ResultSet set = statement.getGeneratedKeys();
                try {
                    set.next();
                    int id = set.getInt("id");
                    return id;
                } catch (SQLException e) {
                    return affectedRows; // no id in this table, just ignore
                }
            }
            return affectedRows;
        } catch (Exception e) {
            System.out.println("SQL Error: " + e);
        }
        return -1;
    }

    private static void applyParamsOnStatement(PreparedStatement statement, Object[] list) {
        int cnt = 1;
        try {

            for (Object object : list) {
                if (object == null) {
                    statement.setNull(cnt++, java.sql.Types.NULL);
                }
                if (object instanceof Integer o) {
                    statement.setInt(cnt++, o);
                } else if (object instanceof Double o) {
                    statement.setDouble(cnt++, o);
                } else if (object instanceof String o) {
                    statement.setString(cnt++, o);
                } else if (object instanceof Timestamp o) {
                    statement.setTimestamp(cnt++, o);
                } else if (object instanceof Object o) {
                    statement.setObject(cnt++, o);
                }
            }
        } catch (Exception e) {
            System.out.println("applyParamsOnStatement exceptions: " + e);
        }
    }

    private static Object[] objectFieldsToArray(Object object) {
        ArrayList<Object> list = new ArrayList<>();
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.getName().equals("o") || field.getName().equals("keys") || field.getName().equals("q")
                        || field.getName().equals("id"))
                    continue;
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
                    if (field.getName().equals("o") || field.getName().equals("keys") || field.getName().equals("q"))
                        continue;
                    try {
                        set.findColumn(field.getName());
                        field.set(item, set.getObject(field.getName()));
                    } catch (SQLException e) {
                        // The field is not found, just skip it
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
