INSERT INTO badges (name, points, description, type, condition_value)
VALUES ('First listening', 10, 'Listen to your first podcast', 0, 1);

INSERT INTO users (firstname, lastname, email, password, registration_date)
VALUES ('Cornichon', 'Man', 'cornichon.man@salutatouslesamis.ch', '1234', '2019-01-01');

INSERT INTO podcasts (title, description, rss_feed, image, author, episodes_count)
VALUES ('Cornichonage','miamlescornichons', 'uwu', 'image', 'cornichon-man', 1);

INSERT INTO episodes (title, description, duration, released_at, audio_url, podcast_id)
VALUES ('Cornichonage', 'miamlescornichons', 1, '2019-01-01', 'uwuu', 1);

UPDATE listen
SET listening_count = 1
WHERE user_id = 1 AND episode_id = 1;

INSERT INTO listen (user_id, episode_id, progression, listening_count)
VALUES (1, 1, 0, 1);

SELECT * FROM badges
         INNER JOIN obtain ON badges.id = obtain.badge_id
WHERE user_id = 1;

SELECT * FROM users
            INNER JOIN listen ON users.id = listen.user_id
    WHERE listen.episode_id = 1;