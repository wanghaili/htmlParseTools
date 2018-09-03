CREATE DATABASE IF NOT EXISTS htmlparsertools CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*
Navicat MySQL Data Transfer

Source Server         : 本地数据库
Source Server Version : 50525
Source Host           : localhost:3306
Source Database       : htmlparsertools

Target Server Type    : MYSQL
Target Server Version : 50525
File Encoding         : 65001

Date: 2016-06-19 20:01:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `web_page_feature_t`
-- ----------------------------
DROP TABLE IF EXISTS `web_page_feature_t`;
CREATE TABLE `web_page_feature_t` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`url`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL ,
`slash_num`  int(10) NOT NULL ,
`no_link_ratio`  double(9,4) NOT NULL ,
`text_link_ratio`  double(9,4) NOT NULL ,
`punctuation_ratio`  double(9,4) NOT NULL ,
`type`  int(10) NOT NULL ,
`create_time`  bigint(20) NULL DEFAULT NULL ,
`update_time`  bigint(20) NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of web_page_feature_t
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Auto increment value for `web_page_feature_t`
-- ----------------------------
ALTER TABLE `web_page_feature_t` AUTO_INCREMENT=1;
