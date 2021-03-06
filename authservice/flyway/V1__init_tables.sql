-- TestDB.`User` definition

CREATE TABLE `User` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(64) NOT NULL,
  `email` VARCHAR(256) NOT NULL,
  `name` VARCHAR(256) NOT NULL,
  `street` VARCHAR(256) NOT NULL DEFAULT "",
  `suite` VARCHAR(64) NOT NULL DEFAULT "",
  `city` VARCHAR(64) NOT NULL DEFAULT "",
  `zipcode` VARCHAR(32) NOT NULL DEFAULT "",
  `lat` DECIMAL(10.5) NOT NULL DEFAULT 0.0,
  `lng` DECIMAL(10.5) NOT NULL DEFAULT 0.0,
  `phone` VARCHAR(64) NOT NULL DEFAULT "",
  `website` VARCHAR(256) NOT NULL DEFAULT "",
  `company` VARCHAR(64) NOT NULL DEFAULT "",
  `catchPhrase` VARCHAR(256) NOT NULL DEFAULT "",
  `bs` VARCHAR(256) NOT NULL DEFAULT "",
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;

-- TestDB.`Todo` definition

CREATE TABLE `Todo` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `userId` BIGINT(20) NOT NULL,
  `title` VARCHAR(256) NOT NULL,
  `completed` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  CONSTRAINT `Todo_User_FK` FOREIGN KEY (`userId`) REFERENCES `User` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) DEFAULT CHARSET=utf8;