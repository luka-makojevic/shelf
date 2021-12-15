ALTER TABLE `file_system`.`file`
ADD COLUMN `parent_folder_id` BIGINT NULL AFTER `shelf_id`,
CHANGE COLUMN `updated_at` `updated_at` DATETIME(6) NULL DEFAULT NULL AFTER `created_at`,
CHANGE COLUMN `is_folder` `is_deleted` BIT(1) NOT NULL AFTER `deleted_at`,
CHANGE COLUMN `id` `id` BIGINT NOT NULL AUTO_INCREMENT ,
CHANGE COLUMN `name` `name` VARCHAR(50) NOT NULL ;
