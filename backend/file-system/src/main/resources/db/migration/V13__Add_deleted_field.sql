ALTER TABLE `file_system`.`file`
ADD COLUMN `deleted` BIT(1) NOT NULL AFTER `path`;

ALTER TABLE `file_system`.`folder`
ADD COLUMN `deleted` BIT(1) NOT NULL AFTER `path`;