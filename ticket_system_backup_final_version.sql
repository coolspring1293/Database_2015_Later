-- MySQL dump 10.13  Distrib 5.6.26, for osx10.8 (x86_64)
--
-- Host: localhost    Database: ticket_system
-- ------------------------------------------------------
-- Server version	5.6.26-log

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
-- Table structure for table `purchasing`
--

DROP TABLE IF EXISTS `purchasing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchasing` (
  `user_name` varchar(20) NOT NULL,
  `ticket_no` int(8) NOT NULL,
  `amount` int(11) DEFAULT '0',
  PRIMARY KEY (`user_name`,`ticket_no`),
  KEY `ticket_no` (`ticket_no`),
  CONSTRAINT `purchasing_ibfk_1` FOREIGN KEY (`user_name`) REFERENCES `user` (`user_name`),
  CONSTRAINT `purchasing_ibfk_2` FOREIGN KEY (`ticket_no`) REFERENCES `ticket` (`ticket_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchasing`
--

LOCK TABLES `purchasing` WRITE;
/*!40000 ALTER TABLE `purchasing` DISABLE KEYS */;
INSERT INTO `purchasing` VALUES ('13349073',1,58),('13349076',3,38),('13349076',7,23),('test',3,2);
/*!40000 ALTER TABLE `purchasing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `station`
--

DROP TABLE IF EXISTS `station`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `station` (
  `station_no` int(6) NOT NULL,
  `station_name` varchar(20) NOT NULL,
  `station_rank` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`station_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `station`
--

LOCK TABLES `station` WRITE;
/*!40000 ALTER TABLE `station` DISABLE KEYS */;
INSERT INTO `station` VALUES (1,'北京','CEN'),(2,'山海关','COM'),(3,'沈阳','COM'),(4,'长春','COM'),(5,'哈尔滨','TER'),(6,'石家庄','COM'),(7,'郑州','EXC'),(8,'武汉','COM'),(9,'长沙','COM'),(10,'株洲','EXC'),(11,'广州','COM'),(12,'深圳','TER'),(13,'天津','COM'),(14,'济南','COM'),(15,'徐州','EXC'),(16,'南京','COM'),(17,'上海','TER'),(18,'包头','COM'),(19,'兰州','EXC'),(20,'西宁','COM'),(21,'喇萨','TER'),(22,'乌鲁木齐','TER'),(23,'连云港','TER'),(24,'西安','COM'),(25,'宝鸡','EXC'),(26,'成都','COM'),(27,'昆明','TER'),(28,'杭州','COM'),(29,'贵阳','COM'),(30,'赣州','COM'),(31,'东莞','COM');
/*!40000 ALTER TABLE `station` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ticket` (
  `ticket_no` int(8) NOT NULL,
  `trip_no` varchar(4) NOT NULL,
  `ticket_date` date NOT NULL,
  `departure_station_no` int(6) NOT NULL,
  `terminal_station_no` int(6) NOT NULL,
  `total_count` int(11) DEFAULT NULL,
  `remaining_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`ticket_no`),
  KEY `trip_no` (`trip_no`),
  KEY `departure_station_no` (`departure_station_no`),
  KEY `terminal_station_no` (`terminal_station_no`),
  CONSTRAINT `ticket_ibfk_1` FOREIGN KEY (`trip_no`) REFERENCES `timetable` (`trip_no`),
  CONSTRAINT `ticket_ibfk_2` FOREIGN KEY (`departure_station_no`) REFERENCES `station` (`station_no`),
  CONSTRAINT `ticket_ibfk_3` FOREIGN KEY (`terminal_station_no`) REFERENCES `station` (`station_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket`
--

LOCK TABLES `ticket` WRITE;
/*!40000 ALTER TABLE `ticket` DISABLE KEYS */;
INSERT INTO `ticket` VALUES (1,'G381','2016-01-01',1,13,100,100),(2,'G381','2016-01-01',1,2,100,100),(3,'G381','2016-01-01',1,3,100,30),(4,'G381','2016-01-01',1,4,100,100),(5,'G381','2016-01-01',1,5,100,100),(6,'G381','2016-01-01',2,5,100,100),(7,'D1','2016-01-01',1,3,50,27),(8,'G381','2016-02-01',3,5,100,100),(9,'D1','2016-01-08',1,4,100,100),(10,'D1','2016-02-02',1,3,100,100);
/*!40000 ALTER TABLE `ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `timetable`
--

DROP TABLE IF EXISTS `timetable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `timetable` (
  `trip_no` varchar(4) NOT NULL,
  `station_no` int(6) NOT NULL DEFAULT '0',
  `arrive_time` time DEFAULT NULL,
  `leave_time` time DEFAULT NULL,
  `distance` int(11) DEFAULT NULL,
  PRIMARY KEY (`trip_no`,`station_no`),
  KEY `station_no` (`station_no`),
  CONSTRAINT `timetable_ibfk_1` FOREIGN KEY (`station_no`) REFERENCES `station` (`station_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `timetable`
--

LOCK TABLES `timetable` WRITE;
/*!40000 ALTER TABLE `timetable` DISABLE KEYS */;
INSERT INTO `timetable` VALUES ('D1',1,NULL,'18:08:00',0),('D1',3,'23:20:00',NULL,703),('G381',1,NULL,'07:53:00',0),('G381',2,'09:51:00','09:53:00',315),('G381',3,'12:25:00','12:28:00',703),('G381',4,'13:40:00','13:43:00',1003),('G381',5,'14:59:00',NULL,1249),('G381',13,'08:38:00','08:40:00',136);
/*!40000 ALTER TABLE `timetable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_name` varchar(20) NOT NULL,
  `nick_name` varchar(20) NOT NULL,
  `password` varchar(20) DEFAULT NULL,
  `age` int(3) DEFAULT NULL,
  `sex` varchar(2) DEFAULT NULL,
  `isAdmin` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('1','3','2',10,'F',0),('13349073','liusy7','1234',20,'M',0),('13349074','liuw53','12345',22,'M',1),('13349076','liuzh26','1234',20,'M',0),('13349092','Zz','1234',10,'F',0),('13349110','wangk47','wangke',19,'F',0),('13349134','xiezhh3','1234',21,'M',0),('13359173','zhongjw27','1234',10,'F',0),('test','DB_Lab','test',10,'男',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-01-17  8:35:56
