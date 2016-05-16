/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : fileshare

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2015-10-30 09:45:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `fileinfo`
-- ----------------------------
DROP TABLE IF EXISTS `fileinfo`;
CREATE TABLE `fileinfo` (
  `fid` int(11) NOT NULL auto_increment,
  `fname` varchar(255) NOT NULL,
  `createtime` varchar(255) NOT NULL,
  `uid` int(11) NOT NULL,
  PRIMARY KEY  (`fid`),
  UNIQUE KEY `fname` (`fname`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of fileinfo
-- ----------------------------
INSERT INTO `fileinfo` VALUES ('1', 'xtliuke@sina.com', '2015-10-30-09-39-28', '4');

-- ----------------------------
-- Table structure for `userinfo`
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo` (
  `uid` int(11) NOT NULL auto_increment,
  `uname` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `access` int(11) unsigned zerofill NOT NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `uname` (`uname`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userinfo
-- ----------------------------
INSERT INTO `userinfo` VALUES ('1', 'admin', '21232f297a57a5a743894a0e4a801fc3', '00000000002');
INSERT INTO `userinfo` VALUES ('2', 'super', '1b3231655cebb7a1f783eddf27d254ca', '00000000001');
INSERT INTO `userinfo` VALUES ('3', 'localuser', 'e58e28a556d2b4884cb16ba8a37775f0', '00000000000');
INSERT INTO `userinfo` VALUES ('4', '877413703', 'e70b97915b0be262b02e71b4cb62f4bb', '00000000000');
INSERT INTO `userinfo` VALUES ('5', 'xtliuke@sina.com', '91cac655957704957b38fdb016b6d2e7', '00000000000');
