# SQL Manager 2005 for MySQL 3.7.0.1
# ---------------------------------------
# Host     : localhost
# Port     : 3306
# Database : algotrader



SET FOREIGN_KEY_CHECKS=0;

CREATE DATABASE `algotrader`
    CHARACTER SET 'latin1'
    COLLATE 'latin1_swedish_ci';

USE `algotrader`;
#
# Structure for the `security_family` table : 
#

DROP TABLE IF EXISTS `security_family`;

CREATE TABLE `security_family` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `MARKET` enum('SOFFEX','DTB','IDEALPRO') NOT NULL,
  `CURRENCY` enum('CHF','EUR','USD') NOT NULL,
  `CONTRACT_SIZE` int(11) NOT NULL,
  `TICK_SIZE` double NOT NULL,
  `COMMISSION` decimal(10,5) DEFAULT NULL,
  `MARKET_OPEN` time NOT NULL,
  `MARKET_CLOSE` time NOT NULL,
  `TRADEABLE` bit(1) DEFAULT NULL,
  `EXPIRABLE` bit(1) NOT NULL,
  `UNDERLAYING_FK` int(11) DEFAULT NULL
  PRIMARY KEY (`id`),
  KEY `SECURITY_FAMILY_UNDERLAYING_FC` (`UNDERLAYING_FK`),
  CONSTRAINT `SECURITY_FAMILY_UNDERLAYING_FC` FOREIGN KEY (`UNDERLAYING_FK`) REFERENCES `security` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

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
  CONSTRAINT `SECURITY_UNDERLAYING_FKC` FOREIGN KEY (`UNDERLAYING_FK`) REFERENCES `security` (`id`),
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

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
  `BASE_CURRENCY` varchar(255) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
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
# Structure for the `strategy` table : 
#

DROP TABLE IF EXISTS `strategy`;

CREATE TABLE `strategy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(30) NOT NULL,
  `FAMILY` varchar(20) NOT NULL,
  `AUTO_ACTIVATE` bit(1) NOT NULL,
  `ALLOCATION` double(15,3) NOT NULL,
  `MODULES` varchar(50) NOT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=latin1;

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
  `CURRENCY` enum('CHF','EUR','USD') NOT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=23831 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=10616 DEFAULT CHARSET=latin1;

#
# Data for the `security_family` table  (LIMIT 0,500)
#

INSERT INTO `security_family` (`id`, `NAME`, `MARKET`, `CURRENCY`, `CONTRACT_SIZE`, `TICK_SIZE`, `COMMISSION`, `MARKET_OPEN`, `MARKET_CLOSE`, `TRADEABLE`, `EXPIRABLE`, `UNDERLAYING_FK`, `SIMULATABLE`, `SPREAD_SLOPE`, `SPREAD_CONSTANT`, `MAX_SPREAD_SLOPE`, `MAX_SPREAD_CONSTANT`, `PERIODICITY`) VALUES 
  (1,'SMI','SOFFEX','CHF',1,0.1,0,'09:00:00','17:20:00',False,False,NULL,0,NULL,NULL,NULL,NULL,''),
  (4,'ESTX50','DTB','EUR',1,0.01,NULL,'09:00:00','17:30:00',False,False,NULL,0,NULL,NULL,NULL,NULL,'');

COMMIT;

#
# Data for the `security` table  (LIMIT 0,500)
#

INSERT INTO `security` (`id`, `ISIN`, `SYMBOL`, `UNDERLAYING_FK`, `SECURITY_FAMILY_FK`, `VOLATILITY_FK`, `INTREST_RATE_FAMILY_FK`) VALUES 
  (2,'DE0009652396','ESTX50',NULL,4,NULL,NULL),
  (4,'CH0008616382','SMI',NULL,1,NULL,NULL);

COMMIT;

#
# Data for the `equity_index` table  (LIMIT 0,500)
#

INSERT INTO `equity_index` (`ID`) VALUES 
  (2),
  (4);

COMMIT;

#
# Data for the `strategy` table  (LIMIT 0,500)
#

INSERT INTO `strategy` (`id`, `NAME`, `FAMILY`, `AUTO_ACTIVATE`, `ALLOCATION`, `MODULES`) VALUES 
  (0,'BASE','BASE',True,0,'base');
UPDATE `strategy` SET `id`=0 WHERE `id`=LAST_INSERT_ID();
INSERT INTO `strategy` (`id`, `NAME`, `FAMILY`, `AUTO_ACTIVATE`, `ALLOCATION`, `MODULES`) VALUES 
  (1,'MOV','MOV',True,1,'mov-main');

COMMIT;

#
# Data for the `transaction` table  (LIMIT 0,500)
#

INSERT INTO `transaction` (`id`, `DATE_TIME`, `QUANTITY`, `PRICE`, `COMMISSION`, `CURRENCY`, `TYPE`, `SECURITY_FK`, `STRATEGY_FK`, `POSITION_FK`, `NUMBER`, `DESCRIPTION`) VALUES 
  (1,'1990-01-01',1,1000000,0,'CHF','CREDIT',NULL,NULL,NULL,NULL,NULL);

COMMIT;

#
# Data for the `watch_list_item` table  (LIMIT 0,500)
#

INSERT INTO `watch_list_item` (`id`, `PERSISTENT`, `SECURITY_FK`, `STRATEGY_FK`) VALUES 
  (4,True,4,1);

COMMIT;

