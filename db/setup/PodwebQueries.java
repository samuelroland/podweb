import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PodwebQueries {
    private final Connection connection;

    public PodwebQueries() throws SQLException {
        // Establish a connection to the database
        String url = "jdbc:postgresql://localhost:5432/database_name";
        String username = "your_username";
        String password = "your_password";
        connection = DriverManager.getConnection(url, username, password);
    }

    public ResultSet getAllPodcasts() throws SQLException {
        String query = "SELECT * FROM podcasts;";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeQuery();
    }

    public ResultSet getAllEpisodes() throws SQLException {
        String query = "SELECT * FROM episodes;";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeQuery();
    }

    public ResultSet getAllCategories() throws SQLException {
        String query = "SELECT * FROM categories;";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeQuery();
    }

    public ResultSet getAllComments() throws SQLException {
        String query = "SELECT * FROM comments;";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeQuery();
    }

    public ResultSet getAllBadges() throws SQLException {
        String query = "SELECT * FROM badges;";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeQuery();
    }

    public ResultSet getAllUsers() throws SQLException {
        String query = "SELECT * FROM users;";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeQuery();
    }

    public ResultSet getAllPlaylists() throws SQLException {
        String query = "SELECT * FROM playlists;";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeQuery();
    }

    public ResultSet searchPodcastsEpisodesAuthors(String keyword) throws SQLException {
        String query = "SELECT * FROM podcasts WHERE to_tsvector(title) @@ to_tsquery(?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, keyword);
        return statement.executeQuery();
    }

    public ResultSet searchEpisodes(String keyword) throws SQLException {
        String query = "SELECT * FROM episodes WHERE to_tsvector(title) @@ to_tsquery(?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, keyword);
        return statement.executeQuery();
    }

    public ResultSet searchPodcastsByAuthor(String keyword) throws SQLException {
        String query = "SELECT * FROM podcasts WHERE to_tsvector(author) @@ to_tsquery(?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, keyword);
        return statement.executeQuery();
    }

    public ResultSet getPopularPodcastsByCategory(int categoryId) throws SQLException {
        String query = "SELECT * FROM podcasts_ranking INNER JOIN podcasts ON podcasts_ranking.id = podcasts.id " +
                "INNER JOIN categorize ON podcasts.id = categorize.podcast_id WHERE category_id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, categoryId);
        return statement.executeQuery();
    }

    public void updateEpisodeProgression(int episodeId, int progression) throws SQLException {
        String query = "UPDATE listen SET progression = ? WHERE episode_id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, progression);
        statement.setInt(2, episodeId);
        statement.executeUpdate();
    }

    public ResultSet getEpisodeProgression(int userId) throws SQLException {
        String query = "SELECT episode_id, progression, duration, listening_count FROM listen " +
                "INNER JOIN episodes ON listen.episode_id = episodes.id " +
                "WHERE (progression > 0 OR listening_count > 1) AND user_id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        return statement.executeQuery();
    }

    public void addToQueue(int userId, int episodeId) throws SQLException {
        String query = "INSERT INTO queue (user_id, episode_id) VALUES (?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        statement.setInt(2, episodeId);
        statement.executeUpdate();
    }

    public void removeFromQueue(int userId, int episodeId) throws SQLException {
        String query = "DELETE FROM queue WHERE user_id = ? AND episode_id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        statement.setInt(2, episodeId);
        statement.executeUpdate();
    }

    public ResultSet getQueue(int userId) throws SQLException {
        String query = "SELECT * FROM queue INNER JOIN episodes ON queue.episode_id = episodes.id WHERE user_id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        return statement.executeQuery();
    }

    public ResultSet getCurrentEpisode(int userId) throws SQLException {
        String query = "SELECT * FROM queue WHERE index = 1 AND user_id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        return statement.executeQuery();
    }

    public void createPlaylist(int userId, String name, String description) throws SQLException {
        String query = "INSERT INTO playlists (user_id, name, description) VALUES (?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        statement.setString(2, name);
        statement.setString(3, description);
        statement.executeUpdate();
    }

    public void deletePlaylist(int playlistId) throws SQLException {
        String query = "DELETE FROM playlists WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, playlistId);
        statement.executeUpdate();
    }

    public void addEpisodeToPlaylist(int playlistId, int episodeId) throws SQLException {
        String query = "INSERT INTO list (playlist_id, episode_id) VALUES (?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, playlistId);
        statement.setInt(2, episodeId);
        statement.executeUpdate();
    }

    public void removeEpisodeFromPlaylist(int playlistId, int episodeId) throws SQLException {
        String query = "DELETE FROM list WHERE playlist_id = ? AND episode_id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, playlistId);
        statement.setInt(2, episodeId);
        statement.executeUpdate();
    }

    public ResultSet getPlaylists() throws SQLException {
        String query = "SELECT * FROM playlists;";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeQuery();
    }

    public ResultSet getUserPlaylist(int playlistId, int userId) throws SQLException {
        String query = "SELECT * FROM playlists WHERE id = ? AND user_id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, playlistId);
        statement.setInt(2, userId);
        return statement.executeQuery();
    }

    public ResultSet getPlaylistEpisodes(int playlistId) throws SQLException {
        String query = "SELECT * FROM episodes INNER JOIN list ON list.episode_id = episodes.id WHERE playlist_id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, playlistId);
        return statement.executeQuery();
    }

    public void createUser(String firstName, String lastName, String email, String password) throws SQLException {
        String query = "INSERT INTO users (firstname, lastname, email, password, registration_date) VALUES (?, ?, ?, ?, now());";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, email);
        statement.setString(4, password);
        statement.executeUpdate();
    }

    public ResultSet loginUser(String email, String password) throws SQLException {
        String query = "SELECT id, firstname, lastname, email, registration_date FROM users WHERE email = ? AND password = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        statement.setString(2, password);
        return statement.executeQuery();
    }

    public void deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        statement.executeUpdate();
    }
}
