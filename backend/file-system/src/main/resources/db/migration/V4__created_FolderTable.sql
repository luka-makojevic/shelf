-- Table structure for table `folder`

CREATE TABLE `folder` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `path` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `shelf_id` bigint NOT NULL,
  `parent_folder_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_shelf_id_folder` (`shelf_id`),
  KEY `parent_folder_id` (`parent_folder_id`),
  CONSTRAINT `fk_shelf_id_folder` FOREIGN KEY (`shelf_id`) REFERENCES `shelf` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `parent_folder_id` FOREIGN KEY (`parent_folder_id`) REFERENCES `folder` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
