ALTER TABLE `shelf`.`email_verify_token`
DROP FOREIGN KEY `email_verify_token_ibfk_1`;
ALTER TABLE `shelf`.`email_verify_token`
ADD CONSTRAINT `email_verify_token_ibfk_1`
  FOREIGN KEY (`user_id`)
  REFERENCES `shelf`.`user` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
