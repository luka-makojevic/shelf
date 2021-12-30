ALTER TABLE `file_system`.`file`
CHANGE COLUMN `name` `name` VARCHAR(200) NOT NULL ;

ALTER TABLE `file_system`.`folder`
CHANGE COLUMN `name` `name` VARCHAR(200) NOT NULL ;