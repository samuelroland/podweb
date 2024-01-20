/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package podweb.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class ModelTest {
    static Query<Podcast> q = new Query<>(Podcast.class);

    @BeforeEach
    public void setup() throws SQLException {
        Query.startTransaction();
    }

    @AfterEach
    public void finish() throws SQLException {
        Query.rollback();
    }

    @Test
    public void model_all_returns_all_items() {
        ArrayList<Podcast> podcasts = Podcast.o.all();
        assertNotNull(podcasts);
        assertEquals(35, podcasts.size());
        assertEquals("LJDS Le Journal Des Stratèges", podcasts.getFirst().title);
    }

    @Test
    public void model_find_works_on_table_without_default_id() {
        Queue q = Queue.o.find(Map.of("episode_id", 87, "user_id", 36));
        assertNotNull(q);
        assertEquals(87, q.episode_id);
        assertEquals(36, q.user_id);
        assertEquals(0, q.index);

        // Inverted fields order
        assertNotNull(Queue.o.find(Map.of("user_id", 36, "episode_id", 87)));

        // Non existant queue record
        assertNull(Queue.o.find(Map.of("user_id", 22, "episode_id", 222)));
    }

    @Test
    public void model_find_fails_on_table_without_all_non_ids_keys() {
        // Wrong method
        assertThrows(RuntimeException.class, () -> {
            Queue.o.find(87);
        });

        assertThrows(RuntimeException.class, () -> {
            Queue.o.find(Map.of("episode_id", 87));
        });

        assertThrows(RuntimeException.class, () -> {
            Queue.o.find(Map.of("user_id", 87));
        });

        // No exception when just not found
        Queue q = Queue.o.find(Map.of("episode_id", 8237, "user_id", 32236));
        assertNull(q);
    }

    @Test
    public void model_getby_works() {
        ArrayList<Episode> episodes = Episode.o.getBy("podcast_id", 27);
        assertEquals(169, episodes.size());
    }

    @Test
    public void model_count_works() {
        assertEquals(35, Podcast.o.count());
    }

    @Test
    public void model_getfirstby_works() {
        Queue firstQueue = Queue.o.all().getFirst();
        assertNotNull(Queue.o.getFirstBy(new String[] { "user_id", "episode_id" },
                new Integer[] { firstQueue.user_id, firstQueue.episode_id }));
    }

    @Test
    public void model_exists_works() {
        assertTrue(Podcast.o.exists(27));
        assertFalse(Podcast.o.exists(4455));
    }

    @Test
    public void model_exists_works_with_table_without_id() {
        assertTrue(Queue.o.exists(Map.of("user_id", 36, "episode_id", 87)));
        assertTrue(Queue.o.exists(Map.of("episode_id", 87, "user_id", 36))); // inverted fields order
        assertTrue(Queue.o.exists(Map.of("user_id", 38, "episode_id", 4157)));

        assertFalse(Queue.o.exists(Map.of("user_id", 87, "episode_id", 36))); // inverted values
        assertFalse(Queue.o.exists(Map.of("user_id", 36, "episode_id", 111111)));
        assertFalse(Queue.o.exists(Map.of("user_id", 2322336, "episode_id", 87)));
        assertFalse(Queue.o.exists(Map.of("user_id", 0, "episode_id", 0)));
    }

    @Test
    public void model_create_correctly_create_element() {
        int pCount = Podcast.o.count();

        Podcast p = new Podcast();
        // p.id; //undefined
        p.title = "hey there";
        p.description = "some desc";
        p.rss_feed = "feed.rss" + Math.random();
        p.image = "image.png";
        p.author = "me";
        p.episodes_count = 200;
        assertEquals(35, Podcast.o.all().size());
        assertTrue(p.create());
        assertNotNull(p.id);
        System.out.println(p);
        assertNotEquals(0, p.id);

        assertEquals(pCount + 1, Podcast.o.count());

        var foundP = Podcast.o.find(p.id);
        assertEquals(foundP.id, p.id);
        assertEquals(foundP.title, p.title);
        assertEquals(foundP.description, p.description);
        assertEquals(foundP.image, p.image);
        assertEquals(foundP.rss_feed, p.rss_feed);
        assertEquals(foundP.episodes_count, p.episodes_count);
        assertEquals(foundP.author, p.author);
    }

    @Test
    public void model_create_correctly_create_element_without_id() {
        int qCount = Queue.o.count();

        Queue queueItem = new Queue();
        queueItem.user_id = 12;
        queueItem.episode_id = 322;
        queueItem.index = 3;
        queueItem.create();
        assertNotNull(queueItem.getFirstBy(new String[] { "user_id", "episode_id" }, new Integer[] { 12, 322 }));

        assertEquals(qCount + 1, Queue.o.count());
    }

    @Test
    public void model_create_correctly_create_element_with_null_value() {
        int cCount = Comment.o.count();

        Comment c = new Comment();
        c.content = "haha";
        c.note = 12;
        c.date = new Timestamp(System.currentTimeMillis());
        c.user_id = 2;
        c.episode_id = 1;

        assertTrue(c.create());
        assertNotNull(c.id);
        System.out.println("set id c.id: " + c.id);
        var foundComment = Comment.o.find(c.id);
        assertNotNull(foundComment);
        assertEquals(foundComment.content, "haha");
        assertEquals(cCount + 1, Comment.o.count());
    }

    @Test
    public void model_update_works_on_table_with_id() {
        Podcast p = Podcast.o.find(27);
        assertEquals("Underscore_", p.title);
        p.title = "THE LIVE !";
        assertTrue(p.update());
        Podcast p2 = Podcast.o.find(27);
        assertNotNull(p2);
        assertEquals("THE LIVE !", p2.title);
    }

    @Test
    public void model_update_works_on_table_without_default_id() {
        Queue q = Queue.o.all().getFirst();
        int startIndex = q.index;

        q.index++;
        assertTrue(q.update());
        assertEquals(startIndex + 1, q.index);
        Queue newQ = Queue.o.find(Map.of("user_id", q.user_id, "episode_id", q.episode_id));
        assertEquals(startIndex + 1, newQ.index);
    }

    @Test
    public void model_delete_works() {
        int cCount = Comment.o.count();

        Comment c = new Comment();
        c.content = "haha";
        c.note = 12;
        c.date = new Timestamp(System.currentTimeMillis());
        c.user_id = 2;
        c.episode_id = 1;
        assertTrue(c.create());

        assertEquals(cCount + 1, Comment.o.count());

        assertTrue(c.delete());
        assertEquals(cCount, Comment.o.count());

        assertFalse(c.delete()); // not found the second time
    }

    @Test
    public void model_delete_returns_false_on_fail() throws SQLException {
        Podcast p = Podcast.o.find(27);
        assertFalse(p.delete());

        // We expect a crash and all commands in the transaction to be ignored, so let's
        // start another transaction
        Query.rollback();
        Query.startTransaction();

        // Make sure it has not been deleted
        assertNotNull(Podcast.o.find(27));
    }

    @Test
    public void model_delete_on_table_without_default_id() {
        int qCount = Queue.o.count();
        Queue q = Queue.o.all().getFirst();
        assertTrue(q.delete());
        assertNull(Queue.o.find(Map.of("episode_id", q.episode_id, "user_id", q.user_id)));

        assertEquals(qCount - 1, Queue.o.count());
    }

    @Test
    public void model_getbyidlist_works() {
        LinkedList<Integer> ids = new LinkedList<>();
        ids.add(1);
        ids.add(29);
        ids.add(23);

        var map = User.o.getByIdList(ids);
        assertEquals(3, map.size());
        assertEquals(1, map.get(1).id);
        assertEquals("Eulalia", map.get(1).firstname);
        assertEquals(29, map.get(29).id);
        assertEquals(23, map.get(23).id);
    }
}