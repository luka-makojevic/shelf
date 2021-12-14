ALTER TABLE `shelf`.`user`
ADD COLUMN `free_space` BIGINT NOT NULL AFTER `salt`;