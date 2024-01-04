package podweb.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;

public class Query {
	private static Connection connection;

	static private void setup() throws SQLException {
		// Establish a connection to the database
		String url = "jdbc:postgresql://localhost:5432/?options=-c%20search_path=podweb%20";
		String username = "postgres";
		String password = "postgres";
		connection = DriverManager.getConnection(url, username, password);
	}

	// TODO: add try with resources
	public static ResultSet query(String query) {
		try {
			setup();
			PreparedStatement statement = connection.prepareStatement(query);
			return statement.executeQuery();
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public static ResultSet query(String query, Object[] params) {
		try {
			setup();
			PreparedStatement statement = connection.prepareStatement(query);
			return statement.executeQuery();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public static boolean update(String query, Object[] params) {
		try {
			setup();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.executeUpdate();
			return true;// operation success
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
}
