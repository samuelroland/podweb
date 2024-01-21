-- Project: Podweb
-- Date: 03.12.2023

set search_path = podweb;

CREATE OR REPLACE VIEW episodes_ranking
AS
	SELECT id, title, duration, podcast_id, sum(listening_count) as listenings_sum from episodes
	INNER JOIN listen ON id = episode_id
	GROUP BY id
	ORDER BY listenings_sum DESC;
CREATE OR REPLACE VIEW podcasts_ranking
AS
	SELECT p.id, p.title, p.image, p.author, sum(listenings_sum) as listenings_total from podcasts p
	INNER JOIN episodes_ranking ON episodes_ranking.podcast_id = p.id
	GROUP BY p.id
	ORDER BY listenings_total DESC;


CREATE OR REPLACE FUNCTION listening_badge_check()
RETURNS TRIGGER AS
$$
DECLARE
    badge RECORD;
BEGIN
	IF NEW.listening_count IS DISTINCT FROM OLD.listening_count THEN
	    -- ListeningCount est le type 0
		FOR badge IN SELECT * FROM badges WHERE type = 1 LOOP
			IF NEW.listening_count >= badge.condition_value THEN
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
        FOR badge IN SELECT * FROM badges WHERE type = 2 LOOP
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
		FOR badge IN SELECT * FROM badges WHERE type = 3 LOOP
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
    badge_count INT;
    badge_id_to_award INT;
BEGIN
    -- Count the comments for the user
    SELECT COUNT(*) INTO badge_count FROM comments WHERE user_id = NEW.user_id;

    -- Find a badge that matches the number of comments
    SELECT id INTO badge_id_to_award
    FROM badges
    WHERE type = 4 AND condition_value <= badge_count;

    IF FOUND THEN
        -- Check if the badge already exists
        IF NOT EXISTS (SELECT 1 FROM obtain WHERE user_id = NEW.user_id AND badge_id = badge_id_to_award) THEN
            -- Award the badge if not already given
            INSERT INTO obtain (user_id, badge_id) VALUES (NEW.user_id, badge_id_to_award);
        END IF;
    END IF;

    RETURN NEW;
END;
$$
LANGUAGE plpgsql;


CREATE TRIGGER listening_change
AFTER INSERT OR UPDATE ON listen
FOR EACH ROW
EXECUTE FUNCTION listening_badge_check();

CREATE TRIGGER playlist_change
AFTER INSERT OR UPDATE ON playlists
FOR EACH ROW
EXECUTE FUNCTION playlist_badge_check();

CREATE TRIGGER check_user_comments_badge
AFTER INSERT ON comments
FOR EACH ROW
EXECUTE FUNCTION comments_badge_check();

-- CREATE OR REPLACE TRIGGER registration_badge_check
-- AFTER INSERT OR UPDATE ON login -- On ne sait pas encore comment on va faire le trigger de ça
-- FOR EACH ROW
-- EXECUTE FUNCTION registration_badge_check();

