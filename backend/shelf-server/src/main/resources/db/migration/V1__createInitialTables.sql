-- MySQL Distrib 8.0.27, for Linux (x86_64)
--
-- Host: localhost    Database: shelf
-- ------------------------------------------------------
-- Server version	8.0.27-0ubuntu0.20.04.1

-- Table structure for table `role`

CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table `role`

INSERT INTO `role` VALUES (1,'super-admin'),(2,'moderator'),(3,'user');

-- Table structure for table `user`

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `email_verified` bit(1) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`email`),
  FOREIGN KEY (`role_id`) REFERENCES `role`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table `user`

INSERT INTO `user` VALUES (1,NOW(),NULL,'admin',_binary '','Admin','Admin','$2a$10$e8tgcSblVDBbMD9JVuqL9.1WbutyqJsGNBZbrlh8chy5vN57cspji','00ErvyFq',NULL,1);

-- Table structure for table `email_verify_token`

CREATE TABLE `email_verify_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `expires_at` datetime(6) NOT NULL,
  `token` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

