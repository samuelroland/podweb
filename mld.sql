-- MySQL Workbench Synchronization
-- Generated: 2023-10-30 15:54
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: xlo

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE IF NOT EXISTS `mydb`.`users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(100) NOT NULL,
  `lastname` VARCHAR(100) NOT NULL,
  `email` VARCHAR(254) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `mydb`.`playlists` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(150) NOT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `user_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_playlists_users1_idx` (`user_id` ASC) VISIBLE,
  UNIQUE INDEX `user_playlist_unique` (`id` ASC, `user_id` ASC) VISIBLE,
  CONSTRAINT `fk_playlists_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `mydb`.`episodes` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(250) NOT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `duration` INT(11) NOT NULL,
  `released_at` DATETIME NOT NULL,
  `audio_url` VARCHAR(2000) NOT NULL,
  `podcast_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `audio_url_UNIQUE` (`audio_url` ASC) VISIBLE,
  INDEX `fk_episodes_podcasts_idx` (`podcast_id` ASC) VISIBLE,
  CONSTRAINT `fk_episodes_podcasts`
    FOREIGN KEY (`podcast_id`)
    REFERENCES `mydb`.`podcasts` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `mydb`.`podcasts` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(250) NOT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `rss_feed` VARCHAR(2000) NOT NULL,
  `image` VARCHAR(2000) NOT NULL,
  `author` VARCHAR(200) NOT NULL,
  `episodes_count` MEDIUMINT(9) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `rss_feed_UNIQUE` (`rss_feed` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `mydb`.`categories` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `mydb`.`categorize` (
  `podcast_id` INT(11) NOT NULL,
  `category_id` INT(11) NOT NULL,
  PRIMARY KEY (`podcast_id`, `category_id`),
  INDEX `fk_podcasts_has_categories_categories1_idx` (`category_id` ASC) VISIBLE,
  INDEX `fk_podcasts_has_categories_podcasts1_idx` (`podcast_id` ASC) VISIBLE,
  CONSTRAINT `fk_podcasts_has_categories_podcasts1`
    FOREIGN KEY (`podcast_id`)
    REFERENCES `mydb`.`podcasts` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_podcasts_has_categories_categories1`
    FOREIGN KEY (`category_id`)
    REFERENCES `mydb`.`categories` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `mydb`.`queue` (
  `episode_id` INT(11) NOT NULL,
  `user_id` INT(11) NOT NULL,
  `index` SMALLINT(6) NULL DEFAULT NULL,
  PRIMARY KEY (`episode_id`, `user_id`),
  INDEX `fk_episodes_has_users_users1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_episodes_has_users_episodes1_idx` (`episode_id` ASC) VISIBLE,
  CONSTRAINT `fk_episodes_has_users_episodes1`
    FOREIGN KEY (`episode_id`)
    REFERENCES `mydb`.`episodes` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_episodes_has_users_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `mydb`.`listen` (
  `episode_id` INT(11) NOT NULL,
  `user_id` INT(11) NOT NULL,
  `progression` INT(11) NOT NULL,
  `listening_count` TINYINT(4) NULL DEFAULT NULL,
  PRIMARY KEY (`episode_id`, `user_id`),
  INDEX `fk_episodes_has_users_users2_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_episodes_has_users_episodes2_idx` (`episode_id` ASC) VISIBLE,
  CONSTRAINT `fk_episodes_has_users_episodes2`
    FOREIGN KEY (`episode_id`)
    REFERENCES `mydb`.`episodes` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_episodes_has_users_users2`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `mydb`.`list` (
  `playlist_id` INT(11) NOT NULL,
  `episode_id` INT(11) NOT NULL,
  PRIMARY KEY (`playlist_id`, `episode_id`),
  INDEX `fk_playlists_has_episodes_episodes1_idx` (`episode_id` ASC) VISIBLE,
  INDEX `fk_playlists_has_episodes_playlists1_idx` (`playlist_id` ASC) VISIBLE,
  CONSTRAINT `fk_playlists_has_episodes_playlists1`
    FOREIGN KEY (`playlist_id`)
    REFERENCES `mydb`.`playlists` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_playlists_has_episodes_episodes1`
    FOREIGN KEY (`episode_id`)
    REFERENCES `mydb`.`episodes` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
