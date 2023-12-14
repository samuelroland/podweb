-- Project: Podweb
-- Date: 03.12.2023

set search_path = podweb;

CREATE OR REPLACE VIEW episodes_ranking
AS
	SELECT id, title, duration, podcast_id, sum(listening_count) as count from episodes
	INNER JOIN listen ON id = episode_id
	GROUP BY user_id, id;
CREATE OR REPLACE VIEW podcasts_ranking
AS
	SELECT p.id, p.title, p.image, p.author, sum(count) as count from podcasts p
	INNER JOIN episodes_ranking ON episodes_ranking.podcast_id = podcast_id
	GROUP BY podcast_id, p.id;


CREATE OR REPLACE FUNCTION listening_badge_check()
RETURNS TRIGGER AS
$$
DECLARE
    badge RECORD;
BEGIN
	IF NEW.listening_count IS DISTINCT FROM OLD.listening_count THEN
	    -- ListeningCount est le type 0
		FOR badge IN SELECT * FROM badges WHERE type = 0 LOOP
			IF NEW.listening_count = badge.condition_value THEN
			    INSERT INTO obtain (user_id, badge_id) VALUES (NEW.user_id, badge.id);
			    EXIT;
			END IF;
		END LOOP;
	END IF;
	RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION registration_badge_check()
RETURNS TRIGGER AS
$$
DECLARE
    badge RECORD;
    days_diff INT;
BEGIN
    -- Calculate the difference in days between the new registration date and now
    days_diff := DATE_PART('day', now() - NEW.registration_date);

    -- Check if it's a different day
    IF days_diff IS DISTINCT FROM DATE_PART('day', now() - OLD.registration_date) THEN
        -- Iterate through the badges with type 1, type 1 is RegistrationDate
        FOR badge IN SELECT * FROM badges WHERE type = 1 LOOP
            -- Check if the condition value matches the days difference
            IF days_diff = badge.condition_value THEN
                -- Insert a record into the obtain table
                INSERT INTO obtain (user_id, badge_id) VALUES (NEW.user_id, badge.id);
                EXIT;
            END IF;
        END LOOP;
    END IF;

    RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION playlist_badge_check()
RETURNS TRIGGER AS
$$
DECLARE
    badge RECORD;
    count_playlists INT;
    badge_exists INT;
BEGIN

    SELECT COUNT(*) INTO count_playlists FROM playlists WHERE user_id = NEW.user_id;

	IF NEW.count_playlists IS DISTINCT FROM OLD.count_playlists THEN
	    -- Check si le badge existe déjà, au cas ou on enlève une playlist
        SELECT COUNT(*) INTO badge_exists FROM obtain WHERE user_id = NEW.user_id AND badge_id = NEW.badge_id;
        IF(badge_exists >= 1) THEN
            RETURN NEW;
        END IF;
	    -- PlaylistCreation est le type 2
		FOR badge IN SELECT * FROM badges WHERE type = 2 LOOP
			IF NEW.count_playlists = badge.condition_value THEN
			    INSERT INTO obtain (user_id, badge_id) VALUES (NEW.user_id, badge.id);
			    EXIT;
			END IF;

		END LOOP;
	END IF;
	RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION comments_badge_check()
RETURNS TRIGGER AS
$$
DECLARE
    badge RECORD;
    count_comments INT;
    badge_exists INT;
BEGIN

    SELECT COUNT(*) INTO count_comments FROM comments WHERE user_id = NEW.user_id;

	IF NEW.count_comments IS DISTINCT FROM OLD.count_comments THEN
	    -- Check si le badge existe déjà, au cas ou on enlève une playlist
        SELECT COUNT(*) INTO badge_exists FROM obtain WHERE user_id = NEW.user_id AND badge_id = NEW.badge_id;
        IF(badge_exists >= 1) THEN
            RETURN NEW;
        END IF;
	    -- CommentsCount est le type 3
		FOR badge IN SELECT * FROM badges WHERE type = 3 LOOP
			IF NEW.count_comments = badge.condition_value THEN
			    INSERT INTO obtain (user_id, badge_id) VALUES (NEW.user_id, badge.id);
			    EXIT;
			END IF;

		END LOOP;
	END IF;
	RETURN NEW;
END;
$$
LANGUAGE plpgsql;


CREATE OR REPLACE TRIGGER listening_change
AFTER INSERT OR UPDATE ON listen
FOR EACH ROW
EXECUTE FUNCTION listening_badge_check();

CREATE OR REPLACE TRIGGER playlist_change
AFTER INSERT OR UPDATE ON playlists
FOR EACH ROW
EXECUTE FUNCTION playlist_badge_check();

CREATE OR REPLACE TRIGGER comments_change
AFTER INSERT OR UPDATE ON comments
FOR EACH ROW
EXECUTE FUNCTION comments_badge_check();

-- CREATE OR REPLACE TRIGGER registration_badge_check
-- AFTER INSERT OR UPDATE ON login -- On ne sait pas encore comment on va faire le trigger de ça
-- FOR EACH ROW
-- EXECUTE FUNCTION registration_badge_check();

