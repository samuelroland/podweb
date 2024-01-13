/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package podweb.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.javalin.Javalin;
import podweb.App;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class QueryTest {

    static Javalin app = App.setupApp();
    static Query<Podcast> q = new Query<>(Podcast.class);

    @BeforeEach
    public void setup() throws SQLException {
        q.startTransaction();
    }

    @AfterEach
    public void finish() throws SQLException {
        q.rollback();
    }

    @Test
    public void query_returns_a_list_podcasts() {
        ArrayList<Podcast> podcasts = q.query("select * from podcasts;");
        assertNotNull(podcasts);
        assertEquals(35, podcasts.size());
        assertEquals("LJDS Le Journal Des Stratèges", podcasts.getFirst().title);
    }

    @Test
    public void query_returns_a_list_with_searched_podcast() {
        ArrayList<Podcast> podcasts = q.query("select * from podcasts where id = 27;");
        assertNotNull(podcasts);
        assertEquals(1, podcasts.size());
        assertEquals("Underscore_", podcasts.getFirst().title);
    }

    @Test
    public void query_works_with_map_of_params() {
        ArrayList<Podcast> podcasts = q.query("select * from podcasts where id = ?;", new Object[] { 27 });
        assertNotNull(podcasts);
        assertEquals(1, podcasts.size());
        assertEquals("Underscore_", podcasts.getFirst().title);
    }

    @Test
    public void query_works_with_an_object() {
        Podcast p = new Podcast();
        p.id = (int) Math.random() + 1000;
        p.title = "hey there";
        p.description = "some desc";
        p.rss_feed = "feed.rss";
        p.image = "image.png";
        p.author = "me";
        p.episodes_count = 200;
        assertEquals(35, Podcast.all().size());

        int nb = q.update(
                "insert into podcasts (id, title, description, rss_feed, image, author, episodes_count) values (?,?,?,?,?,?,?);",
                p);
        assertNotEquals(-1, nb);
        assertEquals(36, Podcast.all().size());
        var foundP = Podcast.find(p.id);
        assertNotNull(foundP);
    }

    @Test
    public void query_count_works() {
        assertEquals(33, Queue.count());
    }

    @Test
    public void tests_run_on_fresh_data() {
        int countBefore = Queue.count();
        assertEquals(33, countBefore);
        Queue queueItem = new Queue();
        queueItem.user_id = 2;
        queueItem.episode_id = 5;
        assertTrue(queueItem.create());
        assertEquals(countBefore + 1, Queue.count());
    }
}