CREATE DATABASE IF NOT EXISTS `tenant1`;
CREATE TABLE IF NOT EXISTS `tenant1`.`users` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
	`user_name` VARCHAR(255) NOT NULL,
	`full_name` VARCHAR(255) NOT NULL,
	`email` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`user_id`)
);

CREATE DATABASE IF NOT EXISTS `tenant2`;
CREATE TABLE IF NOT EXISTS `tenant2`.`users` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
	`user_name` VARCHAR(255) NOT NULL,
	`full_name` VARCHAR(255) NOT NULL,
	`email` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`user_id`)
);