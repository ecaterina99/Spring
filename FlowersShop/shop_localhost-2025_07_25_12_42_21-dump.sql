-- MySQL dump 10.13  Distrib 8.4.4, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: shop
-- ------------------------------------------------------
-- Server version	8.4.4

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `category` enum('bouquet','plant','gift') NOT NULL,
  `description` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `stock_quantity` int,
  `barcode` varchar(255) NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (24,'Lily Bouquet','bouquet','Discover this wonderful white bouquet, where the chic and the elegance of the shades of white flowers blend naturally into soft foliage with tender greens.',35,6,'134567','/innerFolder/products/162bef1b-7167-49e9-b7bb-c8d86fcd90be_1.jpg'),(25,'Rose & Eucalyptus','bouquet','Peony Bloom has imagined for you this superb bouquet in sparkling colors, where the wonderful pink flowers blend into generous foliage with brilliant and tonic greens.',45,0,'234567','/innerFolder/products/81d24484-8a42-442a-9752-080710b6e93e_2.jpg'),(26,'7 white peonies','bouquet','Lots of tenderness and sweetness in this sublime armful of white peonies. This bouquet is ideal to show all your affection and tenderness to a loved one or to offer during a birth.',50,4,'243568','/innerFolder/products/356c0de4-f233-412c-be02-88df28a7ebd6_4.jpg'),(31,'White Avalanche','bouquet','White Avalanche spring flower bouquet.This bouquet is delivered with fresh flowers still in bud, for greater longevity, and to allow you to fully enjoy their blooming.',28,8,'243568','/innerFolder/products/f96c2ff4-4d17-44fc-8d79-51fcdd53abdb_3.jpg'),(32,'9 pink peonies','bouquet','A delicate bouquet of 9 lush pink peonies, symbolizing romance, grace, and gentle affection. Perfect for celebrating love, special moments, or simply brightening someone\'s day.',45,4,'423456','/innerFolder/products/4c7555f6-aaea-4fba-b31d-fde49962ad20_5.jpg'),(33,'Bubble gum','bouquet','This charming bouquet of vibrant pink peonies in a playful \'Bubble Gum\' shade brings joy and freshness to any occasion. A sweet and cheerful choice to lift the mood and show you care.',55,7,'567123','/innerFolder/products/2d74928c-a906-4ea4-86c5-b419e06db0a7_6.jpeg'),(34,'Silvia peonies','bouquet','An elegant blend of delicate Silvia peonies and airy hydrangeas, this bouquet exudes softness and sophistication. Ideal for romantic gestures or graceful celebrations.',60,5,'423456','/innerFolder/products/c4dc01a9-d133-4c5e-9b6d-4cb8ed00d615_7.jpeg'),(35,'Pink dream','bouquet','A luxurious bouquet of lush pink peonies, \'Pink Dream\' is a true expression of elegance and tenderness. Its generous volume and soft tones make it perfect for unforgettable moments and heartfelt emotions.',70,4,'985412','/innerFolder/products/e34c5eda-3e9d-4d58-8aaa-25d3bb67c9dd_8.jpeg'),(38,'Eucalyptus Dario','plant','Eucalyptus Dario\" is a stylish potted eucalyptus plant that brings freshness, calm, and natural beauty into any space. Its soothing scent and minimalist look make it a perfect accent for modern interiors or thoughtful gifts',50,3,'485412','/innerFolder/products/aa3e7c12-7cab-4e2b-9211-db273ed8841a_6.avif'),(39,'Olivier Luciano','plant','Olivier Luciano\" is an elegant potted olive tree that adds Mediterranean charm and timeless beauty to any setting. Symbolizing peace and prosperity, it’s a perfect gift or a refined touch for your home decor',70,2,'998541','/innerFolder/products/33e26c86-8b1a-4944-8ef7-7a431b8bd1b3_7.avif'),(40,'Medinilla Linda ','plant','Medinilla Linda\" is an exotic flowering plant with cascading pink blooms that add a tropical touch to any interior. Its elegant appearance and unique charm make it a stunning centerpiece and a conversation starter',38,2,'457841','/innerFolder/products/124edda3-cde4-4208-8fcf-a16bd4e9478e_1.avif'),(41,'Green Avalanche','plant','Green Avalanche\" is a lush bouquet of soft green flowers, radiating elegance and freshness. Its subtle, unique color makes it a refined choice for modern celebrations or sophisticated home decor',28,0,'134568','/innerFolder/products/1bf65d4d-3a74-4cca-b86b-f6bf3c7bf12f_2.avif'),(42,'Schefflera Etna','plant','Schefflera Etna\" is a vibrant potted plant known for its glossy, umbrella-like leaves that bring a touch of tropical greenery to any space. Easy to care for, it’s perfect for brightening up homes or offices with natural beauty',35,5,'547891','/innerFolder/products/e97d17b9-4f97-4d4e-bc1f-b695c170f24c_3.avif'),(43,'Cactus Gus','plant','Cactus Gus\" is a charming, low-maintenance cactus with a quirky personality, perfect for adding a touch of desert spirit to your space. Ideal for busy lifestyles, it thrives with minimal care while bringing unique texture and style',25,8,'123874','/innerFolder/products/83cd242a-33b9-45bc-aecc-9b8a27427f4f_4.avif'),(44,'Olivier Milo','plant','\"Olivier Milo\" is a graceful olive tree in a stylish pot, symbolizing peace and longevity. Its slender branches and silvery-green leaves add a serene Mediterranean vibe to any room or office.',40,5,'243561','/innerFolder/products/7aa90def-6e60-46f3-b0dd-62748c05825b_5.avif'),(45,'Eucalyptus Dario Green','plant','Eucalyptus Dario\" is a stylish potted eucalyptus plant that brings freshness, calm, and natural beauty into any space. Its soothing scent and minimalist look make it a perfect accent for modern interiors or thoughtful gifts',70,4,'123456','/innerFolder/products/de4b6d5f-bfbd-48eb-a2ec-1b35c8daa7da_aa3e7c12-7cab-4e2b-9211-db273ed8841a_6.avif'),(46,'Green vase','gift','A minimalist green vase that perfectly complements any bouquet or plant. Its clean design adds elegance and freshness to any interior.',40,5,'567123','/innerFolder/products/6c4c9253-bbb3-4af2-a711-f66feb8ee684_10.avif'),(47,'Scented Candle','gift','A beautifully crafted scented candle that fills the room with warmth and gentle fragrance. Ideal for cozy evenings and thoughtful gifting.',24,14,'143569','/innerFolder/products/eceedd26-ad8b-444f-b23c-07cf7a9d5e23_11.avif'),(48,'Vanilla Scented Candle','gift','This soft vanilla-scented candle creates a comforting and inviting atmosphere. A perfect addition to any relaxation routine or special moment.',19,18,'785412','/innerFolder/products/492ebf29-79be-43a6-bad8-af177edc1585_12.avif'),(49,'Alain Ducasse Chocolate','gift','Indulge in the rich, refined taste of Alain Ducasse chocolate — a gourmet treat crafted by one of the world’s finest chocolatiers.',16,25,'789123','/innerFolder/products/1ab95d64-6e37-4c66-90d9-be5b02b784c2_13.avif'),(50,'MOËT & CHANDON','gift','A classic bottle of MOËT & CHANDON champagne to celebrate life’s most joyful occasions with elegance and sparkle.',100,25,'874561','/innerFolder/products/11ffc92b-2464-4f4a-a31a-4f0ba9d8179f_14.jpg'),(51,'MOËT & CHANDON ROSÉ','gift','Add a touch of romance with MOËT & CHANDON ROSÉ — a vibrant and luxurious champagne, perfect for love-filled toasts and special memories.',100,23,'789156','/innerFolder/products/94d14f84-1af4-47f9-a5ef-a011cab7ce4e_15.jpg'),(52,'\"The kiss\"','gift','A single romantic balloon inspired by \"The Kiss\" — a sweet and artistic gesture that adds charm to your floral gift.',25,8,'547832','/innerFolder/products/3db243ec-2664-4bb4-a048-fcad5a021275_16.jpg'),(53,'10 pink heart balloons','gift','A dreamy bundle of 10 pink heart-shaped balloons, perfect for romantic surprises, birthdays, or any celebration filled with love.',50,10,'584123','/innerFolder/products/91e9aadd-b05c-463c-b194-9dee36dcd330_17.jpg');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `product_fk` (`product_id`),
  KEY `user_fk` (`user_id`),
  CONSTRAINT `product_fk` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `user_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=904 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (2,5,26,1),(52,5,24,1),(53,5,25,1),(54,5,26,1),(102,5,26,1),(153,5,24,1),(202,5,25,1),(203,5,26,1),(252,5,24,1),(302,5,24,1),(553,5,26,1),(602,9,24,1),(652,5,26,1),(702,5,26,1),(752,5,24,1),(802,5,24,1),(852,5,39,1),(853,5,44,1),(902,5,52,1),(903,5,47,1);
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_seq`
--

DROP TABLE IF EXISTS `sales_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_seq`
--

LOCK TABLES `sales_seq` WRITE;
/*!40000 ALTER TABLE `sales_seq` DISABLE KEYS */;
INSERT INTO `sales_seq` VALUES (1001);
/*!40000 ALTER TABLE `sales_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `country` varchar(255) DEFAULT NULL,
  `city` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `postal_сode` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `role` enum('admin','buyer') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (5,'Dorin','Bujor','dorin.bjr@gmail.com','0754701039','Romania','IASI','Bulevardul Chimiei 7 B','700291','$2a$12$pbWctFtM1pFNoJXmdoTD7eJZKTy/9em8GB4s9eXYtX1FHYLqp1z..','buyer'),(6,'Ecaterina','Tricolici','tricolicikatea1999@gmail.com','0756435953','Moldova','Falesti','lapusneanu 33','5900','$2a$12$XnQmUx4iXrcyCvGFcLJtLu.Z2lBV3hlnO/pcmxAzb423CO5hvgRVe','admin'),(7,'Maria','Guțu','maria1998@gmail.com','0756435953','Romania','Brasov','bvd Stefan Cel Mare, 45','800128','$2a$12$tweQJ3SmK5b0YY0mNEIHjuukvdKWzEhBxZ3c0W967v8.XmDjtZQn2','buyer'),(8,'Ion','Lungu','ion@gmail.com','0754701039','Romania','IASI','Bulevardul Chimiei 7 B, Bloc A2 Scara A, etajul 7, ap. 49','700291','$2a$12$OUlZ139RegdXd2l48MNtyuyum/OBjR4S/cewxaOz7.9YqItrZ9y3q','buyer'),(9,'Andreea','Crăciun','andreea@gmail.com','0784701058','Romania','IASI','Bulevardul Independentei, Bloc A2 Scara A, etajul 7, ap. 49','800291','$2a$12$RuNzFfwB21hLsUoXnYPvkOM3yuc7JJyJJVcxa/sphTgaJ5zl3MAoS','buyer'),(11,'Mariana ','David','david@gmail.com','0754701057','Romania','Constanta','lapusneanu 14','5900','$2a$12$J/HkUf62a9QHCPuBCVoYvOFmfn2BRSeyJdD.8vvvPpNlu6e1krkvO','buyer');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-25 12:42:21
