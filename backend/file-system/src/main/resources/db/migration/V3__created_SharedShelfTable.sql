-- Table structure for table `shared_shelf`

CREATE TABLE `shared_shelf` (
  `shelf_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `write` bit(1) NOT NULL,
  PRIMARY KEY (`shelf_id`,`user_id`),
  CONSTRAINT `fk_shared_shelf_id` FOREIGN KEY (`shelf_id`) REFERENCES `shelf` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_shared_user_id` FOREIGN KEY (`user_id`) REFERENCES `shelf`.`user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;