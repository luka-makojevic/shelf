ALTER TABLE `file_system`.`file`
CHANGE COLUMN `name` `name` VARCHAR(255) NOT NULL ;

ALTER TABLE `file_system`.`folder`
CHANGE COLUMN `name` `name` VARCHAR(255) NOT NULL ;

ALTER TABLE `file_system`.`shelf`
CHANGE COLUMN `name` `name` VARCHAR(255) NOT NULL ;

