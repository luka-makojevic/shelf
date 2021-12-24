ALTER TABLE `file_system`.`file`
ADD COLUMN `visible` BIT(1) AFTER `deleted`;

ALTER TABLE `file_system`.`folder`
ADD COLUMN `visible` BIT(1) AFTER `deleted`;



