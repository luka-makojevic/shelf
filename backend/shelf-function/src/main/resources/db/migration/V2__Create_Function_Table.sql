CREATE TABLE `function` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `event_id` bigint NOT NULL,
  `shelf_id` bigint NOT NULL,
  `custom` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;