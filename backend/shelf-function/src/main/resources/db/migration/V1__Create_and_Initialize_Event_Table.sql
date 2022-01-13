CREATE TABLE `event` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `event` VALUES (1,'upload'),(2,'download'),(3,'delete'),(4,'daily'),(5,'weekly'),(6,'monthly'),(7,'synchronized');