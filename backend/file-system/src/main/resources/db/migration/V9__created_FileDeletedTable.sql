CREATE TABLE `file_deleted` (
  `id` bigint NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `path` varchar(200) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `shelf_id` bigint NOT NULL,
  `parent_folder_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;