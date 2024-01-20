package podweb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import podweb.models.Query;

public class UserTest {
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
    public void empty_byUser(){
        JavalinTest.test(app, (server, client) -> {
            var res = client.get("/users/1");
            assertEquals(200, res.code());
        });
    }
}
