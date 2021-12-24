ALTER TABLE `file_system`.`file`
CHANGE COLUMN `visible` `trash_visible` BIT(1) NULL DEFAULT b'0' ;

ALTER TABLE `file_system`.`folder`
CHANGE COLUMN `visible` `trash_visible` BIT(1) NULL DEFAULT b'0' ;
