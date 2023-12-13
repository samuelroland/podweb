-- Project: Podweb
-- MySQL Script generated by MySQL Workbench
-- converted into Postgresql Script
-- Thu Nov 23 12:42:04 2023
-- Version: 2.0

-- Drop schema if exists
DROP SCHEMA IF EXISTS podweb CASCADE;

-- Create schema
CREATE SCHEMA podweb;

-- Use schema
SET search_path TO podweb;

-- Table users
CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  firstname VARCHAR(100) NOT NULL,
  lastname VARCHAR(100) NOT NULL,
  email VARCHAR(254) NOT NULL,
  password VARCHAR(255) NOT NULL,
  registration_date TIMESTAMPTZ NOT NULL,
  CONSTRAINT email_unique UNIQUE (email)
);

-- Table playlists
CREATE TABLE playlists (
  id SERIAL PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  description TEXT,
  user_id INT NOT NULL,
  CONSTRAINT user_playlist_unique UNIQUE (id, user_id),
  CONSTRAINT playlist_name_user_unique UNIQUE (name, user_id),
  CONSTRAINT fk_playlists_users1
      FOREIGN KEY (user_id)
          REFERENCES users (id)
          ON DELETE CASCADE
          ON UPDATE NO ACTION
);

-- Table podcasts
CREATE TABLE podcasts (
  id SERIAL PRIMARY KEY,
  title VARCHAR(250) NOT NULL,
  description TEXT,
  rss_feed VARCHAR(2000) NOT NULL,
  image VARCHAR(2000) NOT NULL,
  author VARCHAR(200) NOT NULL,
  episodes_count INT NOT NULL,
  CONSTRAINT rss_feed_unique UNIQUE (rss_feed)
);

-- Table episodes
CREATE TABLE episodes (
  id SERIAL PRIMARY KEY,
  title VARCHAR(250) NOT NULL,
  description TEXT,
  duration INT NOT NULL,
  released_at TIMESTAMPTZ NOT NULL,
  audio_url VARCHAR(2000) NOT NULL,
  podcast_id INT NOT NULL,
  CONSTRAINT audio_url_unique UNIQUE (audio_url),
  CONSTRAINT fk_episodes_podcasts
      FOREIGN KEY (podcast_id)
          REFERENCES podcasts (id)
          ON DELETE CASCADE
          ON UPDATE NO ACTION
);

-- Table categories
CREATE TABLE categories (
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  CONSTRAINT name_unique_categories UNIQUE (name)
);

-- Table categorize
CREATE TABLE categorize (
  podcast_id INT NOT NULL,
  category_id INT NOT NULL,
  PRIMARY KEY (podcast_id, category_id),
  CONSTRAINT fk_podcasts_has_categories_podcasts1
      FOREIGN KEY (podcast_id)
          REFERENCES podcasts (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION,
  CONSTRAINT fk_podcasts_has_categories_categories1
      FOREIGN KEY (category_id)
          REFERENCES categories (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION
);

-- Table queue
CREATE TABLE queue (
  episode_id INT NOT NULL,
  user_id INT NOT NULL,
  index SMALLINT,
  PRIMARY KEY (episode_id, user_id),
  CONSTRAINT fk_episodes_has_users_episodes1
      FOREIGN KEY (episode_id)
          REFERENCES episodes (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION,
  CONSTRAINT fk_episodes_has_users_users1
      FOREIGN KEY (user_id)
          REFERENCES users (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION
);

-- Table listen
CREATE TABLE listen (
  episode_id INT NOT NULL,
  user_id INT NOT NULL,
  progression INT NOT NULL,
  listening_count SMALLINT,
  PRIMARY KEY (episode_id, user_id),
  CONSTRAINT fk_episodes_has_users_episodes2
      FOREIGN KEY (episode_id)
          REFERENCES episodes (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION,
  CONSTRAINT fk_episodes_has_users_users2
      FOREIGN KEY (user_id)
          REFERENCES users (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION
);

-- Table list
CREATE TABLE list (
  playlist_id INT NOT NULL,
  episode_id INT NOT NULL,
  PRIMARY KEY (playlist_id, episode_id),
  CONSTRAINT fk_playlists_has_episodes_playlists1
      FOREIGN KEY (playlist_id)
          REFERENCES playlists (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION,
  CONSTRAINT fk_playlists_has_episodes_episodes1
      FOREIGN KEY (episode_id)
          REFERENCES episodes (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION
);

-- Table comments
CREATE TABLE comments (
  id SERIAL,
  note SMALLINT NOT NULL,
  content TEXT,
  date TIMESTAMPTZ NOT NULL,
  user_id INT NOT NULL,
  parent_id INT,
  episode_id INT NOT NULL,
  PRIMARY KEY (id, user_id),
  CONSTRAINT fk_comments_users1
      FOREIGN KEY (user_id)
          REFERENCES users (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION,
  CONSTRAINT fk_comments_comments1
      FOREIGN KEY (parent_id)
          REFERENCES comments (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION,
  CONSTRAINT fk_comments_episodes1
      FOREIGN KEY (episode_id)
          REFERENCES episodes (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION,
  CONSTRAINT unique_comment_id UNIQUE (id)
);

-- Table badges
CREATE TABLE badges (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  points INT NOT NULL,
  description VARCHAR(1000) NOT NULL,
  type SMALLINT NOT NULL,
  condition_value INT NOT NULL,
  CONSTRAINT name_unique_badges UNIQUE (name)
);

-- Table obtain
CREATE TABLE obtain (
  badge_id INT NOT NULL,
  user_id INT NOT NULL,
  PRIMARY KEY (badge_id, user_id),
  CONSTRAINT fk_badges_has_users_badges1
      FOREIGN KEY (badge_id)
          REFERENCES badges (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION,
  CONSTRAINT fk_badges_has_users_users1
      FOREIGN KEY (user_id)
          REFERENCES users (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION
);