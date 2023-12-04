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
