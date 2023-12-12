-- Project: Podweb
-- Date: 03.12.2023

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
