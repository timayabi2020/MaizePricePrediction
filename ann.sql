-- MySQL dump 10.13  Distrib 5.7.22, for Linux (i686)
--
-- Host: localhost    Database: ann
-- ------------------------------------------------------
-- Server version	5.7.22-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `modelsettings`
--

DROP TABLE IF EXISTS `modelsettings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `modelsettings` (
  `ID` bigint(20) NOT NULL,
  `LEARNINGRATE` double DEFAULT NULL,
  `MAXERROR` double DEFAULT NULL,
  `MAXITERATIONS` int(11) DEFAULT NULL,
  `TESTINGDATA` int(11) DEFAULT NULL,
  `TRAININGDATA` int(11) DEFAULT NULL,
  `NORMALIZER` double DEFAULT NULL,
  `neurons` int(3) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modelsettings`
--

LOCK TABLES `modelsettings` WRITE;
/*!40000 ALTER TABLE `modelsettings` DISABLE KEYS */;
INSERT INTO `modelsettings` VALUES (1,0.2,0.001,150000,30,70,100,9);
/*!40000 ALTER TABLE `modelsettings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nairobi`
--

DROP TABLE IF EXISTS `nairobi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nairobi` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` varchar(200) DEFAULT '0',
  `predictedvalue` varchar(200) NOT NULL DEFAULT '0',
  `date` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nairobi`
--

LOCK TABLES `nairobi` WRITE;
/*!40000 ALTER TABLE `nairobi` DISABLE KEYS */;
INSERT INTO `nairobi` VALUES (1,'17.32','23.33','Apr-18'),(2,'18.45','22.99','Mar-18'),(3,'20.77','21.83','Feb-18'),(4,'23.02','21.63','Jan-18'),(5,'23.65','25.8','Dec-17'),(6,'23.28','28.59','Nov-17'),(7,'22.08','30.23','Oct-17'),(8,'21.97','26.22','Sep-17'),(9,'27.72','24.19','Aug-17'),(10,'31.28','23.74','Jul-17'),(11,'32.1','22.5','Jun-17'),(12,'27.86','19.68','May-17'),(13,'25.67','19.57','Apr-17'),(14,'26.13','19.37','Mar-17'),(15,'23.93','20.54','Feb-17'),(16,'19.13','19.53','Jan-17'),(17,'19.29','19.64','Dec-16'),(18,'19.56','20.18','Nov-16'),(19,'20.06','20.56','Oct-16'),(20,'18.59','20.3','Sep-16'),(21,'18.62','20.33','Aug-16'),(22,'19.88','20.0','Jul-16'),(23,'20.14','20.55','Jun-16'),(24,'19.49','20.45','May-16'),(25,'19.75','20.88','Apr-16'),(26,'19.49','21.24','Mar-16'),(27,'20.06','21.68','Feb-16'),(28,'20.07','22.98','Jan-16'),(29,'20.49','24.15','Dec-15'),(30,'21.17','23.67','Nov-15'),(31,'21.74','23.02','Oct-15'),(32,'23.51','22.58','Sep-15'),(33,'25.23','21.45','Aug-15'),(34,'24.43','18.58','Jul-15'),(35,'23.55','18.63','Jun-15'),(36,'23.51','19.19','May-15'),(37,'21.95','19.44','Apr-15'),(38,'17.43','19.35','Mar-15'),(39,'17.56','19.26','Feb-15'),(40,'19.09','20.76','Jan-15'),(41,'18.39','19.24','Dec-14'),(42,'17.89','20.92','Nov-14'),(43,'18.27','23.5','Oct-14'),(44,'20.44','24.54','Sep-14'),(45,'18.13','23.66','Aug-14'),(46,'20.03','23.13','Jul-14'),(47,'24.81','22.81','Jun-14'),(48,'25.46','23.04','May-14'),(49,'23.69','23.124664','Apr-14'),(50,'23.92','23.074672','Mar-14'),(51,'24.07','24.04','Feb-14'),(52,'23.9','23.14','Jan-14'),(53,'25.15','21.69','Dec-13'),(54,'25.38','22.78','Nov-13'),(55,'25.09','23.02','Oct-13'),(56,'24.13','23.29','Sep-13'),(57,'22.2','22.93','Aug-13'),(58,'23.73','23.23','Jul-13'),(59,'24.37','23.09','Jun-13'),(60,'23.93','22.99','May-13'),(61,'23.65','25.22','Apr-13'),(62,'24.3','27.65','Mar-13'),(63,'24.1','28.29','Feb-13'),(64,'23.73','27.11','Jan-13'),(65,'26.87','26.19','Dec-12'),(66,'30.17','26.09','Nov-12'),(67,'30.34','27.82','Oct-12'),(68,'28.91','26.18','Sep-12'),(69,'28.45','23.32','Aug-12'),(70,'28.67','22.37','Jul-12'),(71,'30.6','22.71','Jun-12'),(72,'28.5','26.01','May-12'),(73,'24.26','27.04','Apr-12'),(74,'23.66','27.49','Mar-12'),(75,'24.55','26.1','Feb-12'),(76,'28.02','26.54','Jan-12'),(77,'29.18','32.87','Dec-11'),(78,'29.23','35.23','Nov-11'),(79,'28.09','32.53','Oct-11'),(80,'28.85','26.96','Sep-11'),(81,'36.8','22.36','Aug-11'),(82,'39.56','20.33','Jul-11'),(83,'34.95','19.42','Jun-11'),(84,'29.33','17.4','May-11'),(85,'24.71','17.96','Apr-11'),(86,'21.69','17.86','Mar-11'),(87,'19.84','18.5','Feb-11'),(88,'16.14','18.41','Jan-11'),(89,'16.47','17.49','Dec-10'),(90,'16.74','16.55','Nov-10'),(91,'16.75','19.71','Oct-10'),(92,'16.64','19.8','Sep-10'),(93,'15.25','23.15','Aug-10'),(94,'13.65','22.54','Jul-10'),(95,'18.8','23.55','Jun-10'),(96,'19.28','25.98','May-10'),(97,'22.43','27.49','Apr-10'),(98,'22.97','26.2','Mar-10'),(99,'23.76','25.08','Feb-10'),(100,'27.64','26.45','Jan-10'),(101,'29.73','25.72','Dec-09'),(102,'27.54','27.3','Nov-09'),(103,'26.46','27.66','Oct-09'),(104,'29.17','31.45','Sep-09'),(105,'28.11','28.47','Aug-09'),(106,'29.13','29.05','Jul-09'),(107,'30.57','24.89','Jun-09'),(108,'34.72','23.59','May-09'),(109,'31.34','25.49','Apr-09'),(110,'31.23','26.55','Mar-09'),(111,'27.83','25.97','Feb-09'),(112,'25.09','22.77','Jan-09'),(113,'28.16','23.21','Dec-08'),(114,'29.5','22.13','Nov-08'),(115,'27.44','25.25','Oct-08'),(116,'23.57','22.34','Sep-08'),(117,'24.73','19.89','Aug-08'),(118,'23.72','18.68','Jul-08'),(119,'26.58','18.64','Jun-08'),(120,'23.41','19.23','May-08'),(121,'18.82','18.76','Apr-08'),(122,'18.16','19.93','Mar-08'),(123,'18.32','19.67','Feb-08'),(124,'18.24','20.27','Jan-08'),(125,'17.32','19.77','Dec-07'),(126,'19.07','20.01','Nov-07'),(127,'19','19.79','Oct-07'),(128,'19.23','20.42','Sep-07'),(129,'18.97','19.45','Aug-07'),(130,'19.24','20.06','Jul-07'),(131,'19.05','21.23','Jun-07'),(132,'19.83','21.98','May-07'),(133,'18.57','20.9','Apr-07'),(134,'19.16','22.03','Mar-07'),(135,'21.37','21.72','Feb-07'),(136,'22.1','23.2','Jan-07'),(137,'20.27','26.5','Dec-06'),(138,'22.12','28.02','Nov-06'),(139,'22.42','26.11','Oct-06'),(140,'23.57','25.97','Sep-06'),(141,'28.33','23.99','Aug-06'),(142,'30.34','24.37','Jul-06'),(143,'27.25','23.82','Jun-06'),(144,'27.61','23.66','May-06'),(145,'26.28','26.28','Apr-06'),(146,'25.85','24.80','Mar-06'),(147,'25.49','24.40','Feb-06'),(148,'25.02','24.0','Jan-06');
/*!40000 ALTER TABLE `nairobi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `research`
--

DROP TABLE IF EXISTS `research`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `research` (
  `id` int(11) NOT NULL,
  `bananasprice` varchar(200) DEFAULT NULL,
  `maizeprice` varchar(200) NOT NULL,
  `riceprice` varchar(200) NOT NULL,
  `wheatprice` varchar(200) NOT NULL,
  `rainfall` varchar(200) NOT NULL,
  `inflation` varchar(200) NOT NULL,
  `maizeproduction` int(200) NOT NULL,
  `predictedvalue` varchar(200) DEFAULT NULL,
  `year` varchar(200) NOT NULL,
  `beanprice` varchar(200) DEFAULT '0',
  `maizeimport` varchar(200) DEFAULT '0',
  `politics` varchar(200) DEFAULT '0',
  `priceinkg` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `research`
--

LOCK TABLES `research` WRITE;
/*!40000 ALTER TABLE `research` DISABLE KEYS */;
INSERT INTO `research` VALUES (151,NULL,'2396.0','1596.0','3857.0','630.0','27.332',17271,'5.85','1992.0','32.26','415320.0','1.0','2.4'),(152,NULL,'8104.0','1171.0','5650.0','630.0','45.979',15549,'7.06','1993.0','35.78','80051.0','1.0','8.1'),(153,NULL,'9500.0','5245.0','12000.0','630.0','28.814',20400,'9.42','1994.0','43.78','650224.0','1.0','9.5'),(154,NULL,'8000.0','5515.0','13000.0','630.0','1.554',18759,'8.61','1995.0','44.56','40000.0','1.0','8.0'),(155,NULL,'9000.0','12257.5','15630.0','630.0','8.864',14506,'10.92','1996.0','42.14','6759.0','1.0','9.0'),(156,NULL,'13732.0','19000.0','17700.0','630.0','12.096',14713,'13.62','1997.0','39.68','1101105.0','1.0','13.73'),(157,NULL,'12844.0','17623.0','16901.0','630.0','5.612',16697,'11.7','1998.0','47.5','368761.0','1.0','12.84'),(158,NULL,'13859.0','21087.0','18140.0','630.0','4.984',14817,'12.5','1999.0','48.18','73520.0','1.0','13.86'),(159,NULL,'14494.0','22844.0','16075.0','630.0','7.77',14400,'11.62','2000.0','43.4','409416.0','1.0','14.49'),(160,NULL,'13308.0','15745.0','18007.0','630.0','5.824',17012,'12.4','2001.0','50.54','314381.0','1.0','13.31'),(161,NULL,'11144.0','11292.0','17243.0','630.0','2.156',15126,'11.92','2002.0','55.32','16348.0','1.0','11.14'),(162,NULL,'11959.0','16153.0','19389.0','630.0','5.983',16224,'13.15','2003.0','48.04','100132.0','1.0','11.96'),(163,NULL,'15342.0','26000.0','22167.0','630.0','8.381',19293,'14.67','2004.0','46.34','241757.0','1.0','15.34'),(164,NULL,'15237.0','28611.0','18211.0','630.0','7.823',16405,'12.53','2005.0','55.21','43652.0','1.0','15.24'),(165,NULL,'15354.0','29947.0','19496.0','630.0','6.041',17197,'13.33','2006.0','53.23','250352.0','1.0','15.35'),(166,NULL,'15664.0','33977.8','28589.0','630.0','4.265',18132,'20.29','2007.0','47.65','293073.0','1.0','15.66'),(167,NULL,'24453.0','36533.0','31832.0','630.0','15.101',13925,'26.3','2008.0','63.16','93473.0','1.0','24.45'),(168,NULL,'23913.0','57970.2','29368.0','630.0','10.552',12943,'23.52','2009.0','60.12','236000.0','1.0','23.91'),(169,NULL,'17213.0','59999.2','29137.0','630.0','4.309',17251,'21.35','2010.0','58.48','258525.0','1.0','17.21'),(170,NULL,'24999.0','84354.4','30174.0','630.0','14.022',15840,'23.63','2011.0','64.82','229596.0','1.0','25.0'),(171,NULL,'33960.0','61462.5','36223.1','630.0','9.378',17366,'32.29','2012.0','57.86','1508414.0','1.0','33.96'),(172,NULL,'31331.6','47925.6','37448.5','630.0','5.717',16922,'30.32','2013.0','49.98','243656.0','1.0','31.33'),(173,NULL,'33184.9','47235.7','34953.4','630.0','6.878',16602,'27.64','2014.0','55.05','113768.0','1.0','33.18'),(174,NULL,'28700.8','57670.0','35616.2','630.0','6.582',18230,'27.43','2015.0','49.99','147000.0','1.0','28.7'),(175,NULL,'29688.0','55886.1','37184.0','630.0','6.318',14284,'30.58','2016.0','49.0','94000.0','1.0','29.69');
/*!40000 ALTER TABLE `research` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sequence`
--

DROP TABLE IF EXISTS `sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequence` (
  `SEQ_NAME` varchar(50) NOT NULL,
  `SEQ_COUNT` decimal(38,0) DEFAULT NULL,
  PRIMARY KEY (`SEQ_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sequence`
--

LOCK TABLES `sequence` WRITE;
/*!40000 ALTER TABLE `sequence` DISABLE KEYS */;
INSERT INTO `sequence` VALUES ('SEQ_GEN',200);
/*!40000 ALTER TABLE `sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `ID` bigint(20) NOT NULL,
  `CREATEDBY` varchar(255) DEFAULT NULL,
  `DATECRESTED` datetime DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `USERNAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'system','2018-05-09 00:00:00','pass123*','tmwamalwa');
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

-- Dump completed on 2018-07-03  0:29:58
