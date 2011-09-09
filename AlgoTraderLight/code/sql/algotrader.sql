# SQL Manager 2005 for MySQL 3.7.0.1
# ---------------------------------------
# Host     : localhost
# Port     : 3306
# Database : AlgoTrader


SET FOREIGN_KEY_CHECKS=0;

DROP DATABASE IF EXISTS `AlgoTrader`;

CREATE DATABASE `AlgoTrader`
    CHARACTER SET 'latin1'
    COLLATE 'latin1_swedish_ci';

USE `AlgoTrader`;
#
# Structure for the `security_family` table : 
#

DROP TABLE IF EXISTS `security_family`;

CREATE TABLE `security_family` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `MARKET` enum('AMEX','AUTO','CBOE','CBOT','CFE','CME','DTB','GLOBEX','ECBOT','IDEALPRO','NYBOT','NYMEX','NASDAQ','NYSE','OTCCBB','PINK','SMART','SOFFEX','LMAX') NOT NULL,
  `CURRENCY` enum('CHF','EUR','USD','GBP') NOT NULL,
  `CONTRACT_SIZE` int(11) NOT NULL,
  `TICK_SIZE` double NOT NULL,
  `COMMISSION` decimal(10,5) DEFAULT NULL,
  `MARKET_OPEN` time NOT NULL,
  `MARKET_CLOSE` time NOT NULL,
  `TRADEABLE` bit(1) DEFAULT NULL,
  `EXPIRABLE` bit(1) NOT NULL,
  `UNDERLAYING_FK` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `SECURITY_FAMILY_UNDERLAYING_FC` (`UNDERLAYING_FK`),
  CONSTRAINT `SECURITY_FAMILY_UNDERLAYING_FC` FOREIGN KEY (`UNDERLAYING_FK`) REFERENCES `security` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for the `security` table : 
#

DROP TABLE IF EXISTS `security`;

CREATE TABLE `security` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ISIN` varchar(20) DEFAULT NULL,
  `SYMBOL` varchar(30) NOT NULL,
  `UNDERLAYING_FK` int(11) DEFAULT NULL,
  `SECURITY_FAMILY_FK` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `SYMBOL` (`SYMBOL`),
  UNIQUE KEY `ISIN` (`ISIN`),
  KEY `SECURITY_UNDERLAYING_FKC` (`UNDERLAYING_FK`),
  KEY `SECURITY_SECURITY_FAMILY_FKC` (`SECURITY_FAMILY_FK`),
  CONSTRAINT `SECURITY_SECURITY_FAMILY_FKC` FOREIGN KEY (`SECURITY_FAMILY_FK`) REFERENCES `security_family` (`id`),
  CONSTRAINT `SECURITY_UNDERLAYING_FKC` FOREIGN KEY (`UNDERLAYING_FK`) REFERENCES `security` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=latin1;

#
# Structure for the `ask` table : 
#

DROP TABLE IF EXISTS `ask`;

CREATE TABLE `ask` (
  `ID` int(11) NOT NULL,
  `DATE_TIME` datetime NOT NULL,
  `SECURITY_FK` int(11) NOT NULL,
  `PRICE` double NOT NULL,
  `SIZE` bigint(20) NOT NULL,
  `EXT_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `VALID` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `MARKET_DATA_EVENT_SECURITY_FKC268f53e449d465cfe59` (`SECURITY_FK`),
  CONSTRAINT `MARKET_DATA_EVENT_SECURITY_FKC268f53e449d465cfe59` FOREIGN KEY (`SECURITY_FK`) REFERENCES `security` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `bar` table : 
#

DROP TABLE IF EXISTS `bar`;

CREATE TABLE `bar` (
  `ID` int(11) NOT NULL,
  `DATE_TIME` datetime NOT NULL,
  `OPEN` double NOT NULL,
  `HIGH` double NOT NULL,
  `LOW` double NOT NULL,
  `CLOSE` double NOT NULL,
  `ADJ_CLOSE` double DEFAULT NULL,
  `VOL` int(11) NOT NULL,
  `OPEN_INTEREST` int(11) NOT NULL,
  `SECURITY_FK` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `BAR_SECURITY_FKC` (`SECURITY_FK`),
  CONSTRAINT `BAR_SECURITY_FKC` FOREIGN KEY (`SECURITY_FK`) REFERENCES `security` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `bid` table : 
#

DROP TABLE IF EXISTS `bid`;

CREATE TABLE `bid` (
  `ID` int(11) NOT NULL,
  `DATE_TIME` datetime NOT NULL,
  `SECURITY_FK` int(11) NOT NULL,
  `PRICE` double NOT NULL,
  `SIZE` bigint(20) NOT NULL,
  `EXT_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `VALID` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `MARKET_DATA_EVENT_SECURITY_FKC268f53e449d465c100dd` (`SECURITY_FK`),
  CONSTRAINT `MARKET_DATA_EVENT_SECURITY_FKC268f53e449d465c100dd` FOREIGN KEY (`SECURITY_FK`) REFERENCES `security` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `equity_index` table : 
#

DROP TABLE IF EXISTS `equity_index`;

CREATE TABLE `equity_index` (
  `ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `EQUITY_INDEXIFKC` (`ID`),
  CONSTRAINT `EQUITY_INDEXIFKC` FOREIGN KEY (`ID`) REFERENCES `security` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `forex` table : 
#

DROP TABLE IF EXISTS `forex`;

CREATE TABLE `forex` (
  `ID` int(11) NOT NULL,
  `BASE_CURRENCY` enum('CHF','EUR','USD','GBP') NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FOREXIFKC` (`ID`),
  CONSTRAINT `FOREXIFKC` FOREIGN KEY (`ID`) REFERENCES `security` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `future` table : 
#

DROP TABLE IF EXISTS `future`;

CREATE TABLE `future` (
  `ID` int(11) NOT NULL,
  `EXPIRATION` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FUTUREIFKC` (`ID`),
  CONSTRAINT `FUTUREIFKC` FOREIGN KEY (`ID`) REFERENCES `security` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `intrest_rate` table : 
#

DROP TABLE IF EXISTS `intrest_rate`;

CREATE TABLE `intrest_rate` (
  `ID` int(11) NOT NULL,
  `DURATION` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `INTREST_RATEIFKC` (`ID`),
  CONSTRAINT `INTREST_RATEIFKC` FOREIGN KEY (`ID`) REFERENCES `security` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `market_data_event_definition` table : 
#

DROP TABLE IF EXISTS `market_data_event_definition`;

CREATE TABLE `market_data_event_definition` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

#
# Structure for the `numeric_market_data_event` table : 
#

DROP TABLE IF EXISTS `numeric_market_data_event`;

CREATE TABLE `numeric_market_data_event` (
  `ID` int(11) NOT NULL,
  `DATE_TIME` datetime NOT NULL,
  `SECURITY_FK` int(11) NOT NULL,
  `DEFINITION_FK` int(11) NOT NULL,
  `VALUE` double NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `MARKET_DATA_EVENT_SECURITY_FKC8fff8d00a338fcb6` (`SECURITY_FK`),
  KEY `GENERIC_MARKET_DATA_EVENT_DEFCa338fcb6` (`DEFINITION_FK`),
  CONSTRAINT `GENERIC_MARKET_DATA_EVENT_DEFCa338fcb6` FOREIGN KEY (`DEFINITION_FK`) REFERENCES `market_data_event_definition` (`ID`),
  CONSTRAINT `MARKET_DATA_EVENT_SECURITY_FKC8fff8d00a338fcb6` FOREIGN KEY (`SECURITY_FK`) REFERENCES `security` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `strategy` table : 
#

DROP TABLE IF EXISTS `strategy`;

CREATE TABLE `strategy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(30) NOT NULL,
  `FAMILY` varchar(20) NOT NULL,
  `AUTO_ACTIVATE` bit(1) NOT NULL,
  `ALLOCATION` double(15,3) NOT NULL,
  `MODULES` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

#
# Structure for the `position` table : 
#

DROP TABLE IF EXISTS `position`;

CREATE TABLE `position` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `QUANTITY` bigint(20) NOT NULL,
  `EXIT_VALUE` double(15,10) DEFAULT NULL,
  `MAINTENANCE_MARGIN` decimal(17,2) DEFAULT NULL,
  `SECURITY_FK` int(11) NOT NULL,
  `STRATEGY_FK` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `SECURITY_FK_STRATEGY_FK` (`SECURITY_FK`,`STRATEGY_FK`),
  KEY `QUANTITY` (`QUANTITY`),
  KEY `POSITION_STRATEGY_FKC` (`STRATEGY_FK`),
  KEY `POSITION_SECURITY_FKC` (`SECURITY_FK`),
  CONSTRAINT `POSITION_SECURITY_FKC` FOREIGN KEY (`SECURITY_FK`) REFERENCES `security` (`id`),
  CONSTRAINT `POSITION_STRATEGY_FKC` FOREIGN KEY (`STRATEGY_FK`) REFERENCES `strategy` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;

#
# Structure for the `stock` table : 
#

DROP TABLE IF EXISTS `stock`;

CREATE TABLE `stock` (
  `ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `STOCKIFKC` (`ID`),
  CONSTRAINT `STOCKIFKC` FOREIGN KEY (`ID`) REFERENCES `security` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `stock_option` table : 
#

DROP TABLE IF EXISTS `stock_option`;

CREATE TABLE `stock_option` (
  `ID` int(11) NOT NULL,
  `EXPIRATION` datetime NOT NULL,
  `STRIKE` double NOT NULL,
  `TYPE` varchar(255) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `STOCK_OPTIONIFKC` (`ID`),
  CONSTRAINT `STOCK_OPTIONIFKC` FOREIGN KEY (`ID`) REFERENCES `security` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `textual_market_data_event` table : 
#

DROP TABLE IF EXISTS `textual_market_data_event`;

CREATE TABLE `textual_market_data_event` (
  `ID` int(11) NOT NULL,
  `DATE_TIME` datetime NOT NULL,
  `SECURITY_FK` int(11) NOT NULL,
  `DEFINITION_FK` int(11) NOT NULL,
  `VALUE` varchar(255) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `MARKET_DATA_EVENT_SECURITY_FKC8fff8d0031f2ff3c` (`SECURITY_FK`),
  KEY `GENERIC_MARKET_DATA_EVENT_DEFC31f2ff3c` (`DEFINITION_FK`),
  CONSTRAINT `GENERIC_MARKET_DATA_EVENT_DEFC31f2ff3c` FOREIGN KEY (`DEFINITION_FK`) REFERENCES `market_data_event_definition` (`ID`),
  CONSTRAINT `MARKET_DATA_EVENT_SECURITY_FKC8fff8d0031f2ff3c` FOREIGN KEY (`SECURITY_FK`) REFERENCES `security` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `tick` table : 
#

DROP TABLE IF EXISTS `tick`;

CREATE TABLE `tick` (
  `id` int(11) NOT NULL,
  `DATE_TIME` datetime NOT NULL,
  `LAST` decimal(11,2) DEFAULT NULL,
  `LAST_DATE_TIME` datetime DEFAULT NULL,
  `VOL` int(11) NOT NULL,
  `VOL_BID` int(11) NOT NULL,
  `VOL_ASK` int(11) NOT NULL,
  `BID` decimal(11,2) NOT NULL,
  `ASK` decimal(11,2) NOT NULL,
  `OPEN_INTREST` int(11) NOT NULL,
  `SETTLEMENT` decimal(11,2) DEFAULT NULL,
  `SECURITY_FK` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `DATE_TIME_SECURITY_FK` (`DATE_TIME`,`SECURITY_FK`),
  KEY `TICK_SECURITY_FKC` (`SECURITY_FK`),
  KEY `DATE_TIME` (`DATE_TIME`),
  KEY `MARKET_DATA_EVENT_SECURITY_FKC27499d` (`SECURITY_FK`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

#
# Structure for the `trade` table : 
#

DROP TABLE IF EXISTS `trade`;

CREATE TABLE `trade` (
  `ID` int(11) NOT NULL,
  `DATE_TIME` datetime NOT NULL,
  `PRICE` double NOT NULL,
  `SIZE` bigint(20) NOT NULL,
  `SECURITY_FK` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `PRICE_EVENT_SECURITY_FKC4c5f944` (`SECURITY_FK`),
  CONSTRAINT `PRICE_EVENT_SECURITY_FKC4c5f944` FOREIGN KEY (`SECURITY_FK`) REFERENCES `security` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `transaction` table : 
#

DROP TABLE IF EXISTS `transaction`;

CREATE TABLE `transaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `DATE_TIME` datetime NOT NULL,
  `QUANTITY` bigint(20) NOT NULL,
  `PRICE` decimal(9,2) NOT NULL,
  `COMMISSION` decimal(15,2) DEFAULT NULL,
  `CURRENCY` enum('CHF','EUR','USD','GBP') NOT NULL,
  `TYPE` enum('BUY','SELL','EXPIRATION','CREDIT','DEBIT','INTREST','FEES','REBALANCE') NOT NULL,
  `SECURITY_FK` int(11) DEFAULT NULL,
  `STRATEGY_FK` int(11) DEFAULT NULL,
  `POSITION_FK` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `TRANSACTION_POSITION_FKC` (`POSITION_FK`),
  KEY `TRANSACTION_SECURITY_FKC` (`SECURITY_FK`),
  KEY `TRANSACTION_STRATEGY_FKC` (`STRATEGY_FK`),
  CONSTRAINT `TRANSACTION_POSITION_FKC` FOREIGN KEY (`POSITION_FK`) REFERENCES `position` (`id`),
  CONSTRAINT `TRANSACTION_SECURITY_FKC` FOREIGN KEY (`SECURITY_FK`) REFERENCES `security` (`id`),
  CONSTRAINT `TRANSACTION_STRATEGY_FKC` FOREIGN KEY (`STRATEGY_FK`) REFERENCES `strategy` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3969 DEFAULT CHARSET=latin1;

#
# Structure for the `watch_list_item` table : 
#

DROP TABLE IF EXISTS `watch_list_item`;

CREATE TABLE `watch_list_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `PERSISTENT` bit(1) NOT NULL,
  `SECURITY_FK` int(11) NOT NULL,
  `STRATEGY_FK` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `STRATEGY_SECURITY_UNIQUE` (`SECURITY_FK`,`STRATEGY_FK`),
  KEY `WATCH_LIST_ITEM_SECURITY_FKC` (`SECURITY_FK`),
  KEY `WATCH_LIST_ITEM_STRATEGY_FKC` (`STRATEGY_FK`),
  CONSTRAINT `WATCH_LIST_ITEM_SECURITY_FKC` FOREIGN KEY (`SECURITY_FK`) REFERENCES `security` (`id`),
  CONSTRAINT `WATCH_LIST_ITEM_STRATEGY_FKC` FOREIGN KEY (`STRATEGY_FK`) REFERENCES `strategy` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=latin1;

#
# Data for the `security_family` table  (LIMIT 0,500)
#

INSERT INTO `security_family` (`id`, `NAME`, `MARKET`, `CURRENCY`, `CONTRACT_SIZE`, `TICK_SIZE`, `COMMISSION`, `MARKET_OPEN`, `MARKET_CLOSE`, `TRADEABLE`, `EXPIRABLE`, `UNDERLAYING_FK`) VALUES 
  (1,'SMI','SOFFEX','CHF',1,0.1,NULL,'00:00:00','23:59:59',False,False,NULL),
  (4,'ESTX50','DTB','EUR',1,0.01,NULL,'00:00:00','23:59:59',False,False,NULL),
  (8,'CHF FOREX','IDEALPRO','CHF',1,5E-5,0,'00:00:00','23:59:59',True,False,NULL),
  (9,'USD FOREX','IDEALPRO','USD',1,5E-5,0,'00:00:00','23:59:59',True,False,NULL),
  (10,'GOOG','SMART','USD',1,0.1,0,'00:00:00','23:59:59',True,False,NULL),
  (11,'IBM','SMART','USD',1,0.1,0,'00:00:00','23:59:59',True,False,NULL),
  (12,'AAPL','SMART','USD',1,0.1,0,'00:00:00','23:59:59',True,False,NULL),
  (13,'SPX','CBOE','USD',1,0.1,NULL,'00:00:00','23:59:59',False,False,NULL),
  (14,'SPY','SMART','USD',1,0.1,0,'00:00:00','23:59:59',True,False,15),
  (15,'GLD','SMART','USD',1,0.1,0,'00:00:00','23:59:59',True,False,NULL),
  (16,'ES','GLOBEX','USD',0,0.1,NULL,'00:00:00','23:59:59',False,False,15),
  (17,'YM','ECBOT','USD',1,1,NULL,'00:00:00','23:59:59',False,False,NULL),
  (18,'NQ','GLOBEX','USD',1,0.1,NULL,'00:00:00','23:59:59',False,False,NULL),
  (19,'FES','GLOBEX','USD',50,0.25,0,'00:00:00','23:59:59',True,True,18),
  (20,'FYM','ECBOT','USD',5,1,0,'00:00:00','23:59:59',True,True,19),
  (21,'FNQ','GLOBEX','USD',20,0.25,0,'00:00:00','23:59:59',True,True,21);

COMMIT;

#
# Data for the `security` table  (LIMIT 0,500)
#

INSERT INTO `security` (`id`, `ISIN`, `SYMBOL`, `UNDERLAYING_FK`, `SECURITY_FAMILY_FK`) VALUES 
  (2,'DE0009652396','ESTX50',NULL,4),
  (4,'CH0008616382','SMI',NULL,1),
  (8,'EU0009654078','EUR.CHF',NULL,8),
  (9,'XC0009652816','USD.CHF',NULL,8),
  (10,'EU0009652759','EUR.USD',NULL,9),
  (11,'GB0031973075','GBP.USD',NULL,9),
  (12,'US4592001014','IBM',NULL,11),
  (13,'US38259P5089','GOOG',NULL,10),
  (14,'US0378331005','AAPL',NULL,12),
  (15,'US78378X1072','SPX',NULL,13),
  (16,'US78462F1030','SPY',15,14),
  (17,'US78463V1070','GLD',NULL,15),
  (18,'11004968','ES',15,16),
  (19,'14721310','YM',NULL,17),
  (21,'11004958','NQ',NULL,18),
  (22,'0FESUB00000','FES SEP/11 50',18,19),
  (23,'0FESZB00000','FES DEZ/11 50',18,19),
  (24,'0FESHC00000','FES MRZ/12 50',18,19),
  (25,'0FESMC00000','FES JUN/12 50',18,19),
  (26,'0FESUC00000','FES SEP/12 50',18,19),
  (27,'0FYMUB00000','FYM SEP/11 5',19,20),
  (28,'0FYMZB00000','FYM DEZ/11 5',19,20),
  (29,'0FYMHC00000','FYM MRZ/12 5',19,20),
  (30,'0FYMMC00000','FYM JUN/12 5',19,20),
  (31,'0NQUB00000','NQ SEP/11 20',21,21),
  (32,'0NQZB00000','NQ DEZ/11 20',21,21),
  (33,'0NQHC00000','NQ MRZ/12 20',21,21),
  (34,'0NQMC00000','NQ JUN/12 20',21,21),
  (35,'0NQUC00000','NQ SEP/12 20',21,21);

COMMIT;

#
# Data for the `equity_index` table  (LIMIT 0,500)
#

INSERT INTO `equity_index` (`ID`) VALUES 
  (2),
  (4),
  (15),
  (18),
  (19),
  (21);

COMMIT;

#
# Data for the `forex` table  (LIMIT 0,500)
#

INSERT INTO `forex` (`ID`, `BASE_CURRENCY`) VALUES 
  (8,'EUR'),
  (9,'USD'),
  (10,'EUR'),
  (11,'GBP');

COMMIT;

#
# Data for the `future` table  (LIMIT 0,500)
#

INSERT INTO `future` (`ID`, `EXPIRATION`) VALUES 
  (22,'2011-09-21'),
  (23,'2011-12-21'),
  (24,'2012-03-16'),
  (25,'2012-06-15'),
  (26,'2012-09-21'),
  (27,'2011-09-16'),
  (28,'2011-12-21'),
  (29,'2012-03-16'),
  (30,'2012-06-15'),
  (31,'2011-09-16'),
  (32,'2011-12-21'),
  (33,'2012-03-16'),
  (34,'2012-06-15'),
  (35,'2012-09-21');

COMMIT;

#
# Data for the `strategy` table  (LIMIT 0,500)
#

INSERT INTO `strategy` (`id`, `NAME`, `FAMILY`, `AUTO_ACTIVATE`, `ALLOCATION`, `MODULES`) VALUES 
  (0,'BASE','BASE',True,0,'market-data,current-values,trades,portfolio,performance,ib,algo');
UPDATE `strategy` SET `id`=0 WHERE `id`=LAST_INSERT_ID();
INSERT INTO `strategy` (`id`, `NAME`, `FAMILY`, `AUTO_ACTIVATE`, `ALLOCATION`, `MODULES`) VALUES 
  (1,'MOV','MOV',True,1,'mov-main'),
  (2,'MAX','MAX',False,0,'max-main'),
  (3,'PERIODIC','PERIODIC',False,0,'periodic-main');

COMMIT;

#
# Data for the `stock` table  (LIMIT 0,500)
#

INSERT INTO `stock` (`ID`) VALUES 
  (12),
  (13),
  (14),
  (16),
  (17);

COMMIT;

#
# Data for the `transaction` table  (LIMIT 0,500)
#

INSERT INTO `transaction` (`id`, `DATE_TIME`, `QUANTITY`, `PRICE`, `COMMISSION`, `CURRENCY`, `TYPE`, `SECURITY_FK`, `STRATEGY_FK`, `POSITION_FK`) VALUES 
  (1,'1990-01-01',1,1000000,0,'CHF','CREDIT',NULL,NULL,NULL);

COMMIT;

#
# Data for the `watch_list_item` table  (LIMIT 0,500)
#

INSERT INTO `watch_list_item` (`id`, `PERSISTENT`, `SECURITY_FK`, `STRATEGY_FK`) VALUES 
  (34,True,13,1),
  (37,True,13,3);

COMMIT;

