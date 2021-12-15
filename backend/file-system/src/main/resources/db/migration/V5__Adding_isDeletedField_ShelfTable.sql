ALTER TABLE `file_system`.`shelf`
ADD COLUMN `is_deleted` BIT(1) NOT NULL AFTER `deleted_at`;
