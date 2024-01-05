CREATE TABLE `tenant1`.`users` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
	`user_name` VARCHAR(255) NOT NULL,
	`full_name` VARCHAR(255) NOT NULL,
	`email` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`user_id`)
);

CREATE TABLE `tenant2`.`users` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
	`user_name` VARCHAR(255) NOT NULL,
	`full_name` VARCHAR(255) NOT NULL,
	`email` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`user_id`)
);