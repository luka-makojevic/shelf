-- Table structure for table `file`

CREATE TABLE `file` (
  `id` bigint NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `path` varchar(200) NOT NULL,
  `is_folder` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `shelf_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_shelf_id` FOREIGN KEY (`shelf_id`) REFERENCES `shelf` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;