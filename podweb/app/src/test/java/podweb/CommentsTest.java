/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package podweb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import podweb.models.Comment;
import podweb.models.Query;
import podweb.models.User;

import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;

public class CommentsTest {
    Javalin app = App.setupApp();

    @BeforeEach
    public void setup() throws SQLException {
        Query.startTransaction();
    }

    @AfterEach
    public void finish() throws SQLException {
        Query.rollback();
    }

    @Test
    public void comments_page_exists() {
        JavalinTest.test(app, (server, client) -> {
            var res = client.get("/episodes/74");
            assertEquals(200, res.code());
            String page = res.body().string();
            ArrayList<Comment> comments = Comment.o.getBy("episode_id", 74);
            for (Comment comment : comments) {
                assertThat(page).contains(comment.content).contains(String.valueOf(comment.note));
                assertThat(page).contains(comment.date.toString());
                assertThat(page).contains(User.o.find(comment.user_id).firstname);
                assertThat(page).contains(User.o.find(comment.user_id).lastname);
            }
            // TODO: support authors display
            // TODO: support subcomments
            assertThat(page).contains("Comments").contains("<textarea").contains("Send comment</button");
        });
    }

    @Test
    public void comments_can_create_a_comment() {
        JavalinTest.test(app, (server, client) -> {
            AppTest.actingAs(1);
            var cCount = Comment.o.count();
            var res = client.post("/episodes/1/comments", "content=heythere&note=5&episode_id=1&user_id=1");
            assertThat(res.body().string()).doesNotContain("Failed to create comment");
            assertEquals(200, res.code());
            var res2 = client.get("/episodes/1");
            assert res2.body() != null;
            String page = res2.body().string();
            assertThat(page).doesNotContain("Login error");
            assertThat(page).contains("heythere").contains("5");

            assertEquals(cCount + 1, Comment.o.count());

            var newComment = Comment.o.getBy("episode_id", 1).getLast();
            assertEquals(newComment.content, "heythere");
            assertEquals(5, newComment.note);
            assertEquals(null, newComment.parent_id);
            assertThat(newComment.date).isEqualTo(System.currentTimeMillis());
        });
    }

    @Test
    public void comments_can_create_a_comment_as_a_reply() {
        JavalinTest.test(app, (server, client) -> {
            var cCount = Comment.o.count();
            var res = client.post("/episodes/1/comments", "content=heythere&note=5&episode_id=1&user_id=1&parent_id=1");
            assertThat(res.body().string()).doesNotContain("Failed to create comment");
            assertEquals(200, res.code());
            assertEquals(cCount + 1, Comment.o.count());

            var newComment = Comment.o.getBy("episode_id", 1).getLast();
            assertEquals(newComment.content, "heythere");
            assertEquals(5, newComment.note);
            assertEquals(1, newComment.parent_id);
        });
    }

    @Test
    public void comments_can_delete_a_comment() {
        JavalinTest.test(app, (server, client) -> {
            var cCount = Comment.o.count();
            var res = client.delete("/comments/1");
            assertEquals(200, res.code());
            // Doit faire -2 pour je ne sais quelle raison, est ce qu'on duplique les
            // commentaires??

            assertEquals(cCount - 2, Comment.o.count());
            var res2 = client.get("/episodes/1");
            assert res2.body() != null;
            String page = res2.body().string();
            assertThat(page).doesNotContain("Login error");
            assertThat(page).doesNotContain("Lorem");
        });
    }

}