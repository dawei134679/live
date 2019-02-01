/*
Navicat MySQL Data Transfer

Source Server         : admin
Source Server Version : 50639
Source Host           : 192.168.20.251:3306
Source Database       : zhu_pay

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2018-05-29 09:36:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `bill_201701`
-- ----------------------------
DROP TABLE IF EXISTS `bill_201701`;
CREATE TABLE `bill_201701` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `gid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '礼物gid',
  `count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '数量(赠送或者收到)',
  `price` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单价(赠送或者收到)',
  `srcuid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '消费者uid',
  `srcnickname` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消费者昵称',
  `srcleftmoney` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '消费者剩余充值币数量',
  `srcwealth` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '消费后财富值',
  `srccredit` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '消费后（直播等级）收到的',
  `dstnickname` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '接收者的昵称',
  `dstuid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '接收者的uid',
  `dstleftmoney` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '接收者剩余充值币数量',
  `dstwealth` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '接收者收礼后财富值',
  `dstcredit` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '接收者收礼后（直播等级）收到的',
  `addtime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '时间',
  `type` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '操作类型',
  `getmoney` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '获得充值币',
  `os` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '所在的平台',
  `bak` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '操作说明',
  PRIMARY KEY (`id`),
  KEY `srcuid,type,addtime` (`srcuid`,`type`,`addtime`) USING BTREE,
  KEY `dstuid,type,addtime` (`dstuid`,`type`,`addtime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='账单表';

-- ----------------------------
-- Records of bill_201701
-- ----------------------------

-- ----------------------------
-- Table structure for `bill_201702`
-- ----------------------------
DROP TABLE IF EXISTS `bill_201702`;
CREATE TABLE `bill_201702` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `gid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '礼物gid',
  `count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '数量(赠送或者收到)',
  `price` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单价(赠送或者收到)',
  `srcuid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '消费者uid',
  `srcnickname` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消费者昵称',
  `srcleftmoney` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '消费者剩余充值币数量',
  `srcwealth` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '消费后财富值',
  `srccredit` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '消费后（直播等级）收到的',
  `dstnickname` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '接收者的昵称',
  `dstuid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '接收者的uid',
  `dstleftmoney` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '接收者剩余充值币数量',
  `dstwealth` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '接收者收礼后财富值',
  `dstcredit` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '接收者收礼后（直播等级）收到的',
  `addtime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '时间',
  `type` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '操作类型',
  `getmoney` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '获得充值币',
  `os` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '所在的平台',
  `bak` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '操作说明',
  PRIMARY KEY (`id`),
  KEY `srcuid,type,addtime` (`srcuid`,`type`,`addtime`) USING BTREE,
  KEY `dstuid,type,addtime` (`dstuid`,`type`,`addtime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='账单表';

-- ----------------------------
-- Records of bill_201702
-- ----------------------------

-- ----------------------------
-- Table structure for `bill_201703`
-- ----------------------------
DROP TABLE IF EXISTS `bill_201703`;
CREATE TABLE `bill_201703` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `gid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '礼物gid',
  `count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '数量(赠送或者收到)',
  `price` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单价(赠送或者收到)',
  `srcuid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '消费者uid',
  `srcnickname` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消费者昵称',
  `srcleftmoney` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '消费者剩余充值币数量',
  `srcwealth` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '消费后财富值',
  `srccredit` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '消费后（直播等级）收到的',
  `dstnickname` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '接收者的昵称',
  `dstuid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '接收者的uid',
  `dstleftmoney` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '接收者剩余充值币数量',
  `dstwealth` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '接收者收礼后财富值',
  `dstcredit` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '接收者收礼后（直播等级）收到的',
  `addtime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '时间',
  `type` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '操作类型',
  `getmoney` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '获得充值币',
  `os` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '所在的平台',
  `bak` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '操作说明',
  PRIMARY KEY (`id`),
  KEY `srcuid,type,addtime` (`srcuid`,`type`,`addtime`) USING BTREE,
  KEY `dstuid,type,addtime` (`dstuid`,`type`,`addtime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='账单表';

-- ----------------------------
-- Records of bill_201703
-- ----------------------------

-- ----------------------------
-- Table structure for `bill_201704`
-- ----------------------------
DROP TABLE IF EXISTS `bill_201704`;
CREATE TABLE `bill_201704` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `gid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '礼物gid',
  `count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '数量(赠送或者收到)',
  `price` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单价(赠送或者收到)',
  `srcuid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '消费者uid',
  `srcnickname` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消费者昵称',
  `srcleftmoney` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '消费者剩余充值币数量',
  `srcwealth` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '消费后财富值',
  `srccredit` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '消费后（直播等级）收到的',
  `dstnickname` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '接收者的昵称',
  `dstuid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '接收者的uid',
  `dstleftmoney` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '接收者剩余充值币数量',
  `dstwealth` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '接收者收礼后财富值',
  `dstcredit` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '接收者收礼后（直播等级）收到的',
  `addtime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '时间',
  `type` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '操作类型',
  `getmoney` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '获得充值币',
  `os` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '所在的平台',
  `bak` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '操作说明',
  PRIMARY KEY (`id`),
  KEY `srcuid,type,addtime` (`srcuid`,`type`,`addtime`) USING BTREE,
  KEY `dstuid,type,addtime` (`dstuid`,`type`,`addtime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='账单表';

-- ----------------------------
-- Records of bill_201704
-- ----------------------------

-- ----------------------------
-- Table structure for `lottory_consume_list`
-- ----------------------------
DROP TABLE IF EXISTS `lottory_consume_list`;
CREATE TABLE `lottory_consume_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `activityId` int(11) NOT NULL COMMENT '活动Id',
  `consume` int(11) NOT NULL COMMENT '抽奖消耗',
  `des` varchar(100) DEFAULT '' COMMENT '抽奖备注说明',
  `addtime` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `uid_time` (`uid`,`addtime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽奖活动消耗记录表';

-- ----------------------------
-- Records of lottory_consume_list
-- ----------------------------

-- ----------------------------
-- Table structure for `pay_account`
-- ----------------------------
DROP TABLE IF EXISTS `pay_account`;
CREATE TABLE `pay_account` (
  `uid` int(10) unsigned NOT NULL COMMENT '用户uid',
  `wx_openid` varchar(100) DEFAULT NULL,
  `wx_unionid` varchar(100) DEFAULT NULL,
  `alipay` varchar(100) DEFAULT '' COMMENT '支付宝账号',
  `isUse` int(1) DEFAULT '0' COMMENT '=0没有提现过 其他则提过现',
  `createAt` int(11) DEFAULT '0' COMMENT '添加时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='用户与提现账号关联';

-- ----------------------------
-- Records of pay_account
-- ----------------------------
INSERT INTO `pay_account` VALUES ('11', null, '(null)', '', '0', '1506061680');
INSERT INTO `pay_account` VALUES ('18', null, '(null)', '', '0', '1506411162');
INSERT INTO `pay_account` VALUES ('10000070', null, '(null)', '', '0', '1508206263');
INSERT INTO `pay_account` VALUES ('10000071', null, 'o8lYtwUojXhi5RfIjwSgDKvu1BQ8', '', '0', '1508206927');
INSERT INTO `pay_account` VALUES ('10000083', null, 'o8lYtwVYVAyXyb4Ylx5f7gyb-_io', '', '0', '1508402537');

-- ----------------------------
-- Table structure for `pay_activity`
-- ----------------------------
DROP TABLE IF EXISTS `pay_activity`;
CREATE TABLE `pay_activity` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(10) NOT NULL COMMENT '用户UID',
  `source` int(2) NOT NULL COMMENT '=1任务 =2活动',
  `act_id` int(5) NOT NULL COMMENT '活动ID',
  `act_name` varchar(100) NOT NULL DEFAULT '' COMMENT '活动名称',
  `zhutou` int(5) NOT NULL COMMENT '猪头数',
  `credit` int(11) NOT NULL DEFAULT '0' COMMENT '声援值',
  `createAt` int(10) NOT NULL COMMENT '时间',
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`),
  KEY `createAt` (`createAt`),
  KEY `actId` (`act_id`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pay_activity
-- ----------------------------
INSERT INTO `pay_activity` VALUES ('1', '10000033', '2', '0', '用户打赏奖励', '188', '0', '1507777063');
INSERT INTO `pay_activity` VALUES ('2', '10000033', '2', '0', '用户打赏奖励', '188', '0', '1507777087');
INSERT INTO `pay_activity` VALUES ('3', '10000033', '2', '0', '用户打赏奖励', '188', '0', '1507777094');
INSERT INTO `pay_activity` VALUES ('4', '10000061', '2', '0', '用户打赏奖励', '5', '0', '1508125353');
INSERT INTO `pay_activity` VALUES ('5', '10000065', '2', '0', '用户打赏奖励', '1', '0', '1508132769');
INSERT INTO `pay_activity` VALUES ('6', '10000061', '2', '0', '用户打赏奖励', '1', '0', '1508140837');
INSERT INTO `pay_activity` VALUES ('7', '10000061', '2', '0', '用户打赏奖励', '188', '0', '1508140844');
INSERT INTO `pay_activity` VALUES ('8', '10000061', '2', '0', '用户打赏奖励', '188', '0', '1508140853');
INSERT INTO `pay_activity` VALUES ('9', '10000043', '2', '0', '用户打赏奖励', '5', '0', '1508204597');
INSERT INTO `pay_activity` VALUES ('10', '10000060', '2', '0', '用户打赏奖励', '1', '0', '1509342171');
INSERT INTO `pay_activity` VALUES ('11', '10000045', '2', '0', '用户打赏奖励', '1', '0', '1509413817');
INSERT INTO `pay_activity` VALUES ('12', '10000065', '2', '0', '用户打赏奖励', '5', '0', '1511416332');
INSERT INTO `pay_activity` VALUES ('13', '10000043', '2', '0', '用户打赏奖励', '5', '0', '1511416348');
INSERT INTO `pay_activity` VALUES ('14', '10000054', '2', '0', '用户打赏奖励', '10', '0', '1511416360');
INSERT INTO `pay_activity` VALUES ('15', '10000041', '2', '0', '用户打赏奖励', '1', '0', '1512547559');
INSERT INTO `pay_activity` VALUES ('16', '10000041', '2', '0', '用户打赏奖励', '5', '0', '1512547610');
INSERT INTO `pay_activity` VALUES ('17', '10000033', '2', '0', '用户打赏奖励', '1', '0', '1512547669');
INSERT INTO `pay_activity` VALUES ('18', '10000033', '2', '0', '用户打赏奖励', '5', '0', '1512547686');
INSERT INTO `pay_activity` VALUES ('19', '10000033', '2', '0', '用户打赏奖励', '188', '0', '1512547694');
INSERT INTO `pay_activity` VALUES ('20', '10000033', '2', '0', '用户打赏奖励', '1', '0', '1512547705');
INSERT INTO `pay_activity` VALUES ('21', '10000033', '2', '0', '用户打赏奖励', '1', '0', '1512548113');
INSERT INTO `pay_activity` VALUES ('22', '10000041', '2', '0', '用户打赏奖励', '1', '0', '1512611558');
INSERT INTO `pay_activity` VALUES ('23', '10000041', '2', '0', '用户打赏奖励', '5', '0', '1512611609');
INSERT INTO `pay_activity` VALUES ('24', '10000041', '2', '0', '用户打赏奖励', '10', '0', '1512611644');
INSERT INTO `pay_activity` VALUES ('25', '10000034', '2', '0', '用户打赏奖励', '10', '0', '1512613487');
INSERT INTO `pay_activity` VALUES ('26', '10000034', '2', '0', '用户打赏奖励', '10', '0', '1512613496');
INSERT INTO `pay_activity` VALUES ('27', '10000034', '2', '0', '用户打赏奖励', '10', '0', '1512613499');
INSERT INTO `pay_activity` VALUES ('28', '10000034', '2', '0', '用户打赏奖励', '10', '0', '1512613509');
INSERT INTO `pay_activity` VALUES ('29', '10000034', '2', '0', '用户打赏奖励', '10', '0', '1512613553');
INSERT INTO `pay_activity` VALUES ('30', '10000034', '2', '0', '用户打赏奖励', '10', '0', '1512613578');
INSERT INTO `pay_activity` VALUES ('31', '10000034', '2', '0', '用户打赏奖励', '10', '0', '1512613582');
INSERT INTO `pay_activity` VALUES ('32', '10000036', '2', '0', '用户打赏奖励', '10', '0', '1512613632');
INSERT INTO `pay_activity` VALUES ('33', '10000036', '2', '0', '用户打赏奖励', '10', '0', '1512613638');
INSERT INTO `pay_activity` VALUES ('34', '10000036', '2', '0', '用户打赏奖励', '10', '0', '1512613649');
INSERT INTO `pay_activity` VALUES ('35', '10000041', '2', '0', '用户打赏奖励', '98', '0', '1512613974');
INSERT INTO `pay_activity` VALUES ('36', '10000158', '2', '0', '用户打赏奖励', '1', '0', '1513043180');
INSERT INTO `pay_activity` VALUES ('37', '10000158', '2', '0', '用户打赏奖励', '2', '0', '1514517692');
INSERT INTO `pay_activity` VALUES ('38', '10000034', '2', '0', '用户打赏奖励', '1', '0', '1515651284');
INSERT INTO `pay_activity` VALUES ('39', '10000034', '2', '0', '用户打赏奖励', '500', '0', '1515651305');
INSERT INTO `pay_activity` VALUES ('40', '10000034', '2', '0', '用户打赏奖励', '1', '0', '1515740297');
INSERT INTO `pay_activity` VALUES ('41', '10000034', '2', '0', '用户打赏奖励', '88', '0', '1515740303');
INSERT INTO `pay_activity` VALUES ('42', '10000041', '2', '0', '用户打赏奖励', '1', '0', '1515747296');
INSERT INTO `pay_activity` VALUES ('43', '10000041', '2', '0', '用户打赏奖励', '1', '0', '1515747307');
INSERT INTO `pay_activity` VALUES ('44', '10000036', '2', '0', '用户打赏奖励', '98', '0', '1515750692');
INSERT INTO `pay_activity` VALUES ('45', '10000034', '2', '0', '用户打赏奖励', '10', '0', '1515980764');
INSERT INTO `pay_activity` VALUES ('46', '10000158', '2', '0', '用户打赏奖励', '4', '0', '1515980781');
INSERT INTO `pay_activity` VALUES ('47', '10000034', '2', '0', '用户打赏奖励', '50', '0', '1515988443');
INSERT INTO `pay_activity` VALUES ('48', '10000202', '2', '0', '用户打赏奖励', '50', '0', '1515999555');
INSERT INTO `pay_activity` VALUES ('49', '10000038', '2', '0', '用户打赏奖励', '1', '0', '1516008761');
INSERT INTO `pay_activity` VALUES ('50', '10000040', '2', '0', '用户打赏奖励', '188', '0', '1516010096');
INSERT INTO `pay_activity` VALUES ('51', '10000040', '2', '0', '用户打赏奖励', '5', '0', '1516066107');
INSERT INTO `pay_activity` VALUES ('52', '10000040', '2', '0', '用户打赏奖励', '1', '0', '1516068546');
INSERT INTO `pay_activity` VALUES ('53', '10000034', '2', '0', '用户打赏奖励', '1', '0', '1516255918');
INSERT INTO `pay_activity` VALUES ('54', '10000038', '2', '0', '用户打赏奖励', '188', '0', '1516257253');
INSERT INTO `pay_activity` VALUES ('55', '10000038', '2', '0', '用户打赏奖励', '188', '0', '1516257318');
INSERT INTO `pay_activity` VALUES ('56', '10000038', '2', '0', '用户打赏奖励', '1', '0', '1516262346');
INSERT INTO `pay_activity` VALUES ('57', '10000045', '2', '0', '用户打赏奖励', '5', '0', '1516603869');
INSERT INTO `pay_activity` VALUES ('58', '10000045', '2', '0', '用户打赏奖励', '98', '0', '1516603877');
INSERT INTO `pay_activity` VALUES ('59', '10000045', '2', '0', '用户打赏奖励', '98', '0', '1516603877');
INSERT INTO `pay_activity` VALUES ('60', '10000045', '2', '0', '用户打赏奖励', '98', '0', '1516603877');
INSERT INTO `pay_activity` VALUES ('61', '10000045', '2', '0', '用户打赏奖励', '98', '0', '1516603877');
INSERT INTO `pay_activity` VALUES ('62', '10000045', '2', '0', '用户打赏奖励', '98', '0', '1516603877');
INSERT INTO `pay_activity` VALUES ('63', '10000045', '2', '0', '用户打赏奖励', '2', '0', '1516603884');
INSERT INTO `pay_activity` VALUES ('64', '10000045', '2', '0', '用户打赏奖励', '5', '0', '1516603887');
INSERT INTO `pay_activity` VALUES ('65', '10000045', '2', '0', '用户打赏奖励', '2', '0', '1516603895');
INSERT INTO `pay_activity` VALUES ('66', '10000045', '2', '0', '用户打赏奖励', '2', '0', '1516603895');
INSERT INTO `pay_activity` VALUES ('67', '10000045', '2', '0', '用户打赏奖励', '5', '0', '1516603900');
INSERT INTO `pay_activity` VALUES ('68', '10000045', '2', '0', '用户打赏奖励', '5', '0', '1516603900');
INSERT INTO `pay_activity` VALUES ('69', '10000045', '2', '0', '用户打赏奖励', '5', '0', '1516603900');
INSERT INTO `pay_activity` VALUES ('70', '10000045', '2', '0', '用户打赏奖励', '5', '0', '1516603900');
INSERT INTO `pay_activity` VALUES ('71', '10000045', '2', '0', '用户打赏奖励', '5', '0', '1516603901');
INSERT INTO `pay_activity` VALUES ('72', '10000045', '2', '0', '用户打赏奖励', '5', '0', '1516603902');
INSERT INTO `pay_activity` VALUES ('73', '10000045', '2', '0', '用户打赏奖励', '5', '0', '1516603902');
INSERT INTO `pay_activity` VALUES ('74', '10000045', '2', '0', '用户打赏奖励', '5', '0', '1516603902');
INSERT INTO `pay_activity` VALUES ('75', '10000045', '2', '0', '用户打赏奖励', '5', '0', '1516603903');
INSERT INTO `pay_activity` VALUES ('76', '10000045', '2', '0', '用户打赏奖励', '1', '0', '1516603905');
INSERT INTO `pay_activity` VALUES ('77', '10000045', '2', '0', '用户打赏奖励', '1', '0', '1516603905');
INSERT INTO `pay_activity` VALUES ('78', '10000045', '2', '0', '用户打赏奖励', '1', '0', '1516603905');
INSERT INTO `pay_activity` VALUES ('79', '10000045', '2', '0', '用户打赏奖励', '1', '0', '1516603905');
INSERT INTO `pay_activity` VALUES ('80', '10000045', '2', '0', '用户打赏奖励', '5', '0', '1516603906');
INSERT INTO `pay_activity` VALUES ('81', '10000045', '2', '0', '用户打赏奖励', '5', '0', '1516603907');
INSERT INTO `pay_activity` VALUES ('82', '10000045', '2', '0', '用户打赏奖励', '5', '0', '1516603907');
INSERT INTO `pay_activity` VALUES ('83', '10000051', '2', '0', '用户打赏奖励', '98', '0', '1516678701');
INSERT INTO `pay_activity` VALUES ('84', '10000034', '2', '0', '用户打赏奖励', '5', '0', '1521611849');
INSERT INTO `pay_activity` VALUES ('85', '10000036', '1', '9', '充值', '1500', '0', '1523944440');
INSERT INTO `pay_activity` VALUES ('86', '10000036', '2', '0', '用户打赏奖励', '5', '0', '1526351660');
INSERT INTO `pay_activity` VALUES ('87', '10000220', '1', '1', '绑定手机', '500', '0', '1526701385');
INSERT INTO `pay_activity` VALUES ('88', '10000220', '1', '10', '送礼', '1000', '0', '1526701386');

-- ----------------------------
-- Table structure for `pay_anchor_credit`
-- ----------------------------
DROP TABLE IF EXISTS `pay_anchor_credit`;
CREATE TABLE `pay_anchor_credit` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL DEFAULT '0' COMMENT '主播UID',
  `unionid` int(11) NOT NULL DEFAULT '0' COMMENT '公会ID',
  `credits` int(11) NOT NULL DEFAULT '0' COMMENT '兑换声援 >2000',
  `rate` float(3,2) NOT NULL DEFAULT '0.00' COMMENT '声援分成比率',
  `operatebak` varchar(255) NOT NULL DEFAULT '' COMMENT '运营备注',
  `financebak` varchar(255) NOT NULL DEFAULT '' COMMENT '财务备注',
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '=0主播提交 =1运营审核通过 =2运营驳回 =3财务通过 =4财务驳回',
  `operateVerify` int(11) NOT NULL DEFAULT '0' COMMENT '运营审核UID',
  `verifytime1` int(11) NOT NULL DEFAULT '0' COMMENT '运营审核时间',
  `financeVerify` int(11) NOT NULL DEFAULT '0' COMMENT '财务审核UID',
  `verifytime2` int(11) NOT NULL DEFAULT '0' COMMENT '财务审核时间',
  `addtime` int(11) NOT NULL DEFAULT '0' COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='主播 声援兑换列表';

-- ----------------------------
-- Records of pay_anchor_credit
-- ----------------------------

-- ----------------------------
-- Table structure for `pay_anchor_salary`
-- ----------------------------
DROP TABLE IF EXISTS `pay_anchor_salary`;
CREATE TABLE `pay_anchor_salary` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ymtime` int(11) NOT NULL DEFAULT '0' COMMENT '年月(yyyyMM)',
  `uid` int(11) NOT NULL DEFAULT '0' COMMENT '主播UID',
  `unionid` int(11) NOT NULL DEFAULT '0' COMMENT '公会ID',
  `credits` int(11) NOT NULL DEFAULT '0' COMMENT '兑换声援 >2000',
  `rate` float(3,2) NOT NULL DEFAULT '0.00' COMMENT '声援分成比率',
  `validdates` int(2) NOT NULL DEFAULT '0' COMMENT '有效天数',
  `validtimes` int(5) NOT NULL DEFAULT '0' COMMENT '有效时长(小时)',
  `salary` int(7) NOT NULL DEFAULT '0' COMMENT '定薪',
  `balance` int(7) NOT NULL DEFAULT '0' COMMENT '结算薪资',
  `operatebak` varchar(255) NOT NULL DEFAULT '' COMMENT '运营备注',
  `financebak` varchar(255) NOT NULL DEFAULT '' COMMENT '财务备注',
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '=0主播提交 =1运营审核通过 =2运营驳回 =3财务通过 =4财务驳回',
  `operateVerify` int(11) NOT NULL DEFAULT '0' COMMENT '运营审核UID',
  `verifytime1` int(11) NOT NULL DEFAULT '0' COMMENT '运营审核时间',
  `financeVerify` int(11) NOT NULL DEFAULT '0' COMMENT '财务审核UID',
  `verifytime2` int(11) NOT NULL DEFAULT '0' COMMENT '财务审核时间',
  `addtime` int(11) NOT NULL DEFAULT '0' COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ymtime` (`ymtime`,`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='主播月兑换列表';

-- ----------------------------
-- Records of pay_anchor_salary
-- ----------------------------

-- ----------------------------
-- Table structure for `pay_exchange`
-- ----------------------------
DROP TABLE IF EXISTS `pay_exchange`;
CREATE TABLE `pay_exchange` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(10) NOT NULL,
  `rate` int(3) NOT NULL COMMENT '声援值兑换比率',
  `credit` int(10) NOT NULL COMMENT '消耗掉的声援值',
  `money` int(5) NOT NULL COMMENT '可兑换金额',
  `zhutou` int(10) NOT NULL COMMENT '兑换猪头数',
  `createAT` int(10) NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='主播将声援值兑换成猪头';

-- ----------------------------
-- Records of pay_exchange
-- ----------------------------
INSERT INTO `pay_exchange` VALUES ('1', '10000018', '40', '1500', '6', '63', '1506668710');
INSERT INTO `pay_exchange` VALUES ('2', '10000018', '40', '7500', '30', '315', '1506668727');
INSERT INTO `pay_exchange` VALUES ('3', '10000018', '40', '1500', '6', '63', '1506668805');
INSERT INTO `pay_exchange` VALUES ('4', '10000033', '40', '7500', '30', '315', '1507621651');
INSERT INTO `pay_exchange` VALUES ('5', '10000033', '40', '1500', '6', '63', '1507621666');
INSERT INTO `pay_exchange` VALUES ('6', '10000033', '40', '7500', '30', '315', '1507621668');
INSERT INTO `pay_exchange` VALUES ('7', '10000033', '40', '399500', '1598', '16780', '1507621671');
INSERT INTO `pay_exchange` VALUES ('8', '10000033', '40', '7500', '30', '315', '1507621675');
INSERT INTO `pay_exchange` VALUES ('9', '10000033', '40', '7500', '30', '315', '1507621680');
INSERT INTO `pay_exchange` VALUES ('10', '10000033', '40', '74500', '298', '3129', '1507621687');
INSERT INTO `pay_exchange` VALUES ('11', '10000033', '40', '7500', '30', '315', '1507621693');
INSERT INTO `pay_exchange` VALUES ('12', '10000036', '40', '1500', '6', '63', '1507622157');
INSERT INTO `pay_exchange` VALUES ('13', '10000045', '40', '74500', '298', '3129', '1507875882');
INSERT INTO `pay_exchange` VALUES ('14', '10000045', '40', '24500', '98', '1029', '1507875910');
INSERT INTO `pay_exchange` VALUES ('15', '10000045', '40', '7500', '30', '315', '1507875917');
INSERT INTO `pay_exchange` VALUES ('16', '10000045', '40', '7500', '30', '315', '1507875919');
INSERT INTO `pay_exchange` VALUES ('17', '10000045', '40', '1500', '6', '63', '1507875921');
INSERT INTO `pay_exchange` VALUES ('18', '10000045', '40', '1500', '6', '63', '1507875923');
INSERT INTO `pay_exchange` VALUES ('19', '10000061', '40', '7500', '30', '315', '1508132840');
INSERT INTO `pay_exchange` VALUES ('20', '10000061', '40', '7500', '30', '315', '1508132859');
INSERT INTO `pay_exchange` VALUES ('21', '10000061', '40', '7500', '30', '315', '1508132864');
INSERT INTO `pay_exchange` VALUES ('22', '10000038', '44', '1363', '6', '63', '1508815802');

-- ----------------------------
-- Table structure for `pay_get_redenvelop`
-- ----------------------------
DROP TABLE IF EXISTS `pay_get_redenvelop`;
CREATE TABLE `pay_get_redenvelop` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `envelopeid` int(10) unsigned NOT NULL COMMENT '红包id',
  `srcUid` int(11) unsigned NOT NULL COMMENT '发红包人uid',
  `dstUid` int(11) unsigned NOT NULL COMMENT '收红包人uid',
  `roomId` int(11) unsigned NOT NULL COMMENT '房间号 即主播uid',
  `money` int(8) unsigned NOT NULL COMMENT '猪头数',
  `getTime` int(11) unsigned NOT NULL COMMENT '抢到红包金额',
  PRIMARY KEY (`id`),
  KEY `envelopeid_dstuid` (`envelopeid`,`dstUid`),
  KEY `envelopeid_srcuid` (`envelopeid`,`srcUid`)
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pay_get_redenvelop
-- ----------------------------
INSERT INTO `pay_get_redenvelop` VALUES ('1', '1', '10000002', '10000001', '10000001', '180', '1506481406');
INSERT INTO `pay_get_redenvelop` VALUES ('2', '1', '10000002', '10000002', '10000001', '189', '1506481431');
INSERT INTO `pay_get_redenvelop` VALUES ('3', '2', '10000002', '10000001', '10000001', '684', '1506481459');
INSERT INTO `pay_get_redenvelop` VALUES ('4', '2', '10000002', '10000000', '10000001', '126', '1506481461');
INSERT INTO `pay_get_redenvelop` VALUES ('5', '2', '10000002', '10000002', '10000001', '190', '1506481466');
INSERT INTO `pay_get_redenvelop` VALUES ('6', '3', '10000000', '10000001', '10000001', '382', '1506483083');
INSERT INTO `pay_get_redenvelop` VALUES ('7', '3', '10000000', '10000000', '10000001', '176', '1506483086');
INSERT INTO `pay_get_redenvelop` VALUES ('8', '1', '10000002', '10000048', '10000001', '631', '1506567960');
INSERT INTO `pay_get_redenvelop` VALUES ('9', '4', '10000022', '10000022', '10000020', '8', '1506677429');
INSERT INTO `pay_get_redenvelop` VALUES ('10', '4', '10000022', '10000017', '10000020', '17', '1506677430');
INSERT INTO `pay_get_redenvelop` VALUES ('11', '4', '10000022', '10000020', '10000020', '17', '1506677437');
INSERT INTO `pay_get_redenvelop` VALUES ('12', '5', '10000022', '10000017', '10000020', '5000', '1506678017');
INSERT INTO `pay_get_redenvelop` VALUES ('13', '6', '10000022', '10000017', '10000020', '5000', '1506678034');
INSERT INTO `pay_get_redenvelop` VALUES ('14', '7', '10000022', '10000022', '10000020', '100', '1506679007');
INSERT INTO `pay_get_redenvelop` VALUES ('15', '4', '10000022', '10000048', '10000020', '158', '1506700800');
INSERT INTO `pay_get_redenvelop` VALUES ('16', '8', '10000025', '10000026', '10000026', '100', '1506762413');
INSERT INTO `pay_get_redenvelop` VALUES ('17', '9', '10000034', '10000033', '10000033', '1111', '1507619097');
INSERT INTO `pay_get_redenvelop` VALUES ('18', '10', '10000034', '10000034', '10000033', '75', '1507619420');
INSERT INTO `pay_get_redenvelop` VALUES ('19', '10', '10000034', '10000032', '10000033', '82', '1507619420');
INSERT INTO `pay_get_redenvelop` VALUES ('20', '10', '10000034', '10000033', '10000033', '66', '1507619434');
INSERT INTO `pay_get_redenvelop` VALUES ('21', '11', '10000036', '10000033', '10000033', '100', '1507621375');
INSERT INTO `pay_get_redenvelop` VALUES ('22', '10', '10000034', '10000048', '10000033', '777', '1507651200');
INSERT INTO `pay_get_redenvelop` VALUES ('23', '12', '10000034', '10000034', '99001', '1000', '1507690349');
INSERT INTO `pay_get_redenvelop` VALUES ('24', '13', '10000033', '10000033', '10000036', '1000', '1507780350');
INSERT INTO `pay_get_redenvelop` VALUES ('25', '14', '10000034', '10000041', '10000041', '500', '1507796509');
INSERT INTO `pay_get_redenvelop` VALUES ('26', '15', '10000061', '10000060', '10000042', '116', '1508123423');
INSERT INTO `pay_get_redenvelop` VALUES ('27', '15', '10000061', '10000064', '10000042', '138', '1508123423');
INSERT INTO `pay_get_redenvelop` VALUES ('28', '15', '10000061', '10000061', '10000042', '144', '1508123424');
INSERT INTO `pay_get_redenvelop` VALUES ('29', '15', '10000061', '10000042', '10000042', '82', '1508123425');
INSERT INTO `pay_get_redenvelop` VALUES ('30', '15', '10000061', '10000043', '10000042', '140', '1508123436');
INSERT INTO `pay_get_redenvelop` VALUES ('31', '15', '10000061', '10000065', '10000042', '153', '1508123456');
INSERT INTO `pay_get_redenvelop` VALUES ('32', '16', '10000062', '10000064', '10000042', '1188', '1508123458');
INSERT INTO `pay_get_redenvelop` VALUES ('33', '16', '10000062', '10000061', '10000042', '782', '1508123459');
INSERT INTO `pay_get_redenvelop` VALUES ('34', '16', '10000062', '10000060', '10000042', '864', '1508123461');
INSERT INTO `pay_get_redenvelop` VALUES ('35', '16', '10000062', '10000062', '10000042', '1120', '1508123461');
INSERT INTO `pay_get_redenvelop` VALUES ('36', '16', '10000062', '10000042', '10000042', '1046', '1508123483');
INSERT INTO `pay_get_redenvelop` VALUES ('37', '18', '10000062', '10000036', '10000042', '985', '1508123630');
INSERT INTO `pay_get_redenvelop` VALUES ('38', '18', '10000062', '10000043', '10000042', '524', '1508123631');
INSERT INTO `pay_get_redenvelop` VALUES ('39', '18', '10000062', '10000062', '10000042', '907', '1508123633');
INSERT INTO `pay_get_redenvelop` VALUES ('40', '18', '10000062', '10000052', '10000042', '470', '1508123651');
INSERT INTO `pay_get_redenvelop` VALUES ('41', '19', '10000063', '10000054', '10000042', '103', '1508125805');
INSERT INTO `pay_get_redenvelop` VALUES ('42', '19', '10000063', '10000063', '10000042', '73', '1508125805');
INSERT INTO `pay_get_redenvelop` VALUES ('43', '19', '10000063', '10000045', '10000042', '69', '1508125805');
INSERT INTO `pay_get_redenvelop` VALUES ('44', '19', '10000063', '10000044', '10000042', '83', '1508125805');
INSERT INTO `pay_get_redenvelop` VALUES ('45', '19', '10000063', '10000043', '10000042', '106', '1508125805');
INSERT INTO `pay_get_redenvelop` VALUES ('46', '19', '10000063', '10000042', '10000042', '68', '1508125806');
INSERT INTO `pay_get_redenvelop` VALUES ('47', '20', '10000063', '10000054', '10000042', '55', '1508125873');
INSERT INTO `pay_get_redenvelop` VALUES ('48', '20', '10000063', '10000045', '10000042', '60', '1508125873');
INSERT INTO `pay_get_redenvelop` VALUES ('49', '20', '10000063', '10000043', '10000042', '48', '1508125873');
INSERT INTO `pay_get_redenvelop` VALUES ('50', '20', '10000063', '10000046', '10000042', '652', '1508125873');
INSERT INTO `pay_get_redenvelop` VALUES ('51', '20', '10000063', '10000052', '10000042', '92', '1508125873');
INSERT INTO `pay_get_redenvelop` VALUES ('52', '20', '10000063', '10000042', '10000042', '93', '1508125875');
INSERT INTO `pay_get_redenvelop` VALUES ('53', '21', '10000063', '10000045', '10000042', '180', '1508125936');
INSERT INTO `pay_get_redenvelop` VALUES ('54', '21', '10000063', '10000062', '10000042', '215', '1508125936');
INSERT INTO `pay_get_redenvelop` VALUES ('55', '21', '10000063', '10000043', '10000042', '219', '1508125937');
INSERT INTO `pay_get_redenvelop` VALUES ('56', '21', '10000063', '10000042', '10000042', '169', '1508125938');
INSERT INTO `pay_get_redenvelop` VALUES ('57', '21', '10000063', '10000063', '10000042', '217', '1508125938');
INSERT INTO `pay_get_redenvelop` VALUES ('58', '22', '10000063', '10000063', '10000042', '119', '1508134329');
INSERT INTO `pay_get_redenvelop` VALUES ('59', '22', '10000063', '10000043', '10000042', '49', '1508134330');
INSERT INTO `pay_get_redenvelop` VALUES ('60', '22', '10000063', '10000064', '10000042', '33', '1508134330');
INSERT INTO `pay_get_redenvelop` VALUES ('61', '15', '10000061', '10000048', '10000042', '461', '1508155200');
INSERT INTO `pay_get_redenvelop` VALUES ('62', '17', '10000062', '10000048', '10000042', '5000', '1508155200');
INSERT INTO `pay_get_redenvelop` VALUES ('63', '18', '10000062', '10000048', '10000042', '2114', '1508155200');
INSERT INTO `pay_get_redenvelop` VALUES ('64', '23', '10000051', '10000034', '10000034', '100', '1508397098');
INSERT INTO `pay_get_redenvelop` VALUES ('65', '24', '10000034', '10000034', '10000033', '143', '1508730545');
INSERT INTO `pay_get_redenvelop` VALUES ('66', '24', '10000034', '10000033', '10000033', '223', '1508730862');
INSERT INTO `pay_get_redenvelop` VALUES ('67', '25', '10000051', '10000033', '10000033', '5000', '1510911652');
INSERT INTO `pay_get_redenvelop` VALUES ('68', '26', '10000034', '10000034', '10000033', '180', '1511419218');
INSERT INTO `pay_get_redenvelop` VALUES ('69', '27', '10000034', '10000034', '10000033', '111', '1511419508');
INSERT INTO `pay_get_redenvelop` VALUES ('70', '27', '10000034', '10000089', '10000033', '162', '1511419525');
INSERT INTO `pay_get_redenvelop` VALUES ('71', '27', '10000034', '10000033', '10000033', '145', '1511421899');
INSERT INTO `pay_get_redenvelop` VALUES ('72', '27', '10000034', '10000048', '10000033', '1170', '1511438400');
INSERT INTO `pay_get_redenvelop` VALUES ('73', '28', '10000051', '10000034', '10000034', '1000', '1511943615');
INSERT INTO `pay_get_redenvelop` VALUES ('74', '29', '10000034', '10000034', '10000041', '1000', '1512033663');
INSERT INTO `pay_get_redenvelop` VALUES ('75', '30', '10000193', '10000193', '10000033', '104', '1514273076');
INSERT INTO `pay_get_redenvelop` VALUES ('76', '30', '10000193', '10000033', '10000033', '121', '1514273077');
INSERT INTO `pay_get_redenvelop` VALUES ('77', '31', '10000193', '10000193', '10000033', '42', '1514273145');
INSERT INTO `pay_get_redenvelop` VALUES ('78', '31', '10000193', '10000033', '10000033', '25', '1514273149');
INSERT INTO `pay_get_redenvelop` VALUES ('79', '32', '10000193', '10000193', '10000033', '315', '1514273187');
INSERT INTO `pay_get_redenvelop` VALUES ('80', '32', '10000193', '10000033', '10000033', '218', '1514273211');
INSERT INTO `pay_get_redenvelop` VALUES ('81', '30', '10000193', '10000048', '10000033', '775', '1514304000');
INSERT INTO `pay_get_redenvelop` VALUES ('82', '31', '10000193', '10000048', '10000033', '933', '1514304000');
INSERT INTO `pay_get_redenvelop` VALUES ('83', '33', '10000092', '10000092', '10000036', '136', '1514344213');
INSERT INTO `pay_get_redenvelop` VALUES ('84', '33', '10000092', '10000051', '10000036', '1', '1514344215');
INSERT INTO `pay_get_redenvelop` VALUES ('85', '33', '10000092', '10000036', '10000036', '116', '1514344216');
INSERT INTO `pay_get_redenvelop` VALUES ('86', '34', '10000092', '10000051', '10000036', '370', '1514344256');
INSERT INTO `pay_get_redenvelop` VALUES ('87', '34', '10000092', '10000036', '10000036', '266', '1514344257');
INSERT INTO `pay_get_redenvelop` VALUES ('88', '35', '10000038', '10000038', '10000158', '88', '1514358173');
INSERT INTO `pay_get_redenvelop` VALUES ('89', '35', '10000038', '10000158', '10000158', '58', '1514358182');
INSERT INTO `pay_get_redenvelop` VALUES ('90', '36', '10000038', '10000038', '10000193', '142', '1514358342');
INSERT INTO `pay_get_redenvelop` VALUES ('91', '36', '10000038', '10000193', '10000193', '358', '1514358364');
INSERT INTO `pay_get_redenvelop` VALUES ('92', '37', '10000092', '10000092', '10000193', '77', '1514358445');
INSERT INTO `pay_get_redenvelop` VALUES ('93', '37', '10000092', '10000038', '10000193', '23', '1514358473');
INSERT INTO `pay_get_redenvelop` VALUES ('94', '38', '10000038', '10000038', '10000077', '48', '1514359008');
INSERT INTO `pay_get_redenvelop` VALUES ('95', '33', '10000092', '10000048', '10000036', '2', '1514376000');
INSERT INTO `pay_get_redenvelop` VALUES ('96', '35', '10000038', '10000048', '10000158', '354', '1514390400');
INSERT INTO `pay_get_redenvelop` VALUES ('97', '38', '10000038', '10000048', '10000077', '52', '1514390400');
INSERT INTO `pay_get_redenvelop` VALUES ('98', '39', '10000036', '10000051', '10000051', '100', '1514873798');
INSERT INTO `pay_get_redenvelop` VALUES ('99', '40', '10000036', '10000051', '10000051', '3333', '1515740621');
INSERT INTO `pay_get_redenvelop` VALUES ('100', '41', '10000038', '10000036', '10000051', '53', '1515741043');
INSERT INTO `pay_get_redenvelop` VALUES ('101', '41', '10000038', '10000034', '10000051', '42', '1515741043');
INSERT INTO `pay_get_redenvelop` VALUES ('102', '41', '10000038', '10000038', '10000051', '42', '1515741043');
INSERT INTO `pay_get_redenvelop` VALUES ('103', '42', '10000038', '10000036', '10000051', '64', '1515741373');
INSERT INTO `pay_get_redenvelop` VALUES ('104', '42', '10000038', '10000038', '10000051', '36', '1515741374');
INSERT INTO `pay_get_redenvelop` VALUES ('105', '43', '10000204', '10000203', '10000077', '255', '1515748179');
INSERT INTO `pay_get_redenvelop` VALUES ('106', '44', '10000203', '10000203', '10000077', '250', '1515748227');
INSERT INTO `pay_get_redenvelop` VALUES ('107', '45', '10000202', '10000089', '10000040', '437', '1515750233');
INSERT INTO `pay_get_redenvelop` VALUES ('108', '45', '10000202', '10000202', '10000040', '1785', '1515750233');
INSERT INTO `pay_get_redenvelop` VALUES ('109', '46', '10000202', '10000202', '10000040', '1025', '1515750265');
INSERT INTO `pay_get_redenvelop` VALUES ('110', '46', '10000202', '10000077', '10000040', '943', '1515750265');
INSERT INTO `pay_get_redenvelop` VALUES ('111', '46', '10000202', '10000040', '10000040', '598', '1515750265');
INSERT INTO `pay_get_redenvelop` VALUES ('112', '46', '10000202', '10000089', '10000040', '877', '1515750265');
INSERT INTO `pay_get_redenvelop` VALUES ('113', '47', '10000034', '10000089', '10000040', '456', '1515750633');
INSERT INTO `pay_get_redenvelop` VALUES ('114', '47', '10000034', '10000040', '10000040', '380', '1515750633');
INSERT INTO `pay_get_redenvelop` VALUES ('115', '47', '10000034', '10000034', '10000040', '305', '1515750634');
INSERT INTO `pay_get_redenvelop` VALUES ('116', '47', '10000034', '10000077', '10000040', '309', '1515750634');
INSERT INTO `pay_get_redenvelop` VALUES ('117', '48', '10000034', '10000034', '10000040', '214', '1515750731');
INSERT INTO `pay_get_redenvelop` VALUES ('118', '48', '10000034', '10000040', '10000040', '185', '1515750738');
INSERT INTO `pay_get_redenvelop` VALUES ('119', '48', '10000034', '10000089', '10000040', '267', '1515750746');
INSERT INTO `pay_get_redenvelop` VALUES ('120', '49', '10000038', '10000203', '10000040', '416', '1515751142');
INSERT INTO `pay_get_redenvelop` VALUES ('121', '49', '10000038', '10000038', '10000040', '381', '1515751142');
INSERT INTO `pay_get_redenvelop` VALUES ('122', '49', '10000038', '10000034', '10000040', '365', '1515751143');
INSERT INTO `pay_get_redenvelop` VALUES ('123', '49', '10000038', '10000202', '10000040', '289', '1515751150');
INSERT INTO `pay_get_redenvelop` VALUES ('124', '49', '10000038', '10000040', '10000040', '376', '1515751166');
INSERT INTO `pay_get_redenvelop` VALUES ('125', '41', '10000038', '10000048', '10000051', '751', '1515772800');
INSERT INTO `pay_get_redenvelop` VALUES ('126', '46', '10000202', '10000048', '10000040', '1557', '1515772800');
INSERT INTO `pay_get_redenvelop` VALUES ('127', '47', '10000034', '10000048', '10000040', '550', '1515772800');
INSERT INTO `pay_get_redenvelop` VALUES ('128', '49', '10000038', '10000048', '10000040', '3173', '1515772800');
INSERT INTO `pay_get_redenvelop` VALUES ('129', '50', '10000041', '10000041', '10000204', '1000', '1515999711');
INSERT INTO `pay_get_redenvelop` VALUES ('130', '51', '10000038', '10000048', '10000034', '200', '1516276800');
INSERT INTO `pay_get_redenvelop` VALUES ('131', '52', '10000034', '10000048', '10000193', '1646', '1516291200');
INSERT INTO `pay_get_redenvelop` VALUES ('132', '53', '10000038', '10000034', '10000051', '184', '1516349928');
INSERT INTO `pay_get_redenvelop` VALUES ('133', '53', '10000038', '10000038', '10000051', '416', '1516349928');
INSERT INTO `pay_get_redenvelop` VALUES ('134', '56', '10000038', '10000038', '10000033', '109', '1516350496');
INSERT INTO `pay_get_redenvelop` VALUES ('135', '54', '10000038', '10000048', '10000033', '585', '1516377600');
INSERT INTO `pay_get_redenvelop` VALUES ('136', '55', '10000038', '10000048', '10000033', '500', '1516377600');
INSERT INTO `pay_get_redenvelop` VALUES ('137', '56', '10000038', '10000048', '10000033', '91', '1516377600');

-- ----------------------------
-- Table structure for `pay_grant`
-- ----------------------------
DROP TABLE IF EXISTS `pay_grant`;
CREATE TABLE `pay_grant` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL DEFAULT '0' COMMENT '用户uid',
  `oldzhutou` int(11) NOT NULL DEFAULT '0',
  `oldcredit` int(11) NOT NULL DEFAULT '0',
  `zhutou` int(11) NOT NULL DEFAULT '0' COMMENT '猪头',
  `credit` int(11) NOT NULL DEFAULT '0' COMMENT '声援值',
  `descrip` varchar(225) NOT NULL DEFAULT '' COMMENT '操作说明',
  `adminuid` int(11) NOT NULL DEFAULT '0' COMMENT '操作者UID',
  `addtime` int(11) NOT NULL DEFAULT '0' COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8mb4 COMMENT='人员加猪币';

-- ----------------------------
-- Records of pay_grant
-- ----------------------------
INSERT INTO `pay_grant` VALUES ('1', '10000000', '0', '0', '10000', '100000', '2222', '1', '1506422322');
INSERT INTO `pay_grant` VALUES ('2', '10000001', '0', '0', '10000', '20000', '200000', '1', '1506422355');
INSERT INTO `pay_grant` VALUES ('3', '10000002', '0', '0', '100000', '100000', '0', '1', '1506481348');
INSERT INTO `pay_grant` VALUES ('4', '10000018', '0', '0', '100000', '100000', '100000', '1', '1506668616');
INSERT INTO `pay_grant` VALUES ('5', '10000022', '0', '0', '100000', '100000', '1000000', '1', '1506677366');
INSERT INTO `pay_grant` VALUES ('6', '10000017', '0', '0', '100000', '100000', '1000000000000', '1', '1506677396');
INSERT INTO `pay_grant` VALUES ('7', '10000025', '0', '0', '100000', '0', '0', '1', '1506761862');
INSERT INTO `pay_grant` VALUES ('8', '10000026', '0', '0', '100000', '0', '0', '1', '1506761891');
INSERT INTO `pay_grant` VALUES ('9', '10000030', '0', '0', '100000', '100000', '100000100000', '1', '1507511648');
INSERT INTO `pay_grant` VALUES ('10', '10000031', '0', '0', '100000', '100000', '十万', '1', '1507533657');
INSERT INTO `pay_grant` VALUES ('11', '10000032', '0', '0', '100000', '100000', '0', '1', '1507537781');
INSERT INTO `pay_grant` VALUES ('12', '10000034', '0', '0', '100000', '100000', '补发', '1', '1507617178');
INSERT INTO `pay_grant` VALUES ('13', '10000036', '0', '0', '100000', '100000', '1000000010000000', '1', '1507621047');
INSERT INTO `pay_grant` VALUES ('14', '10000017', '108483', '100000', '100000', '123', '123132', '1', '1507685645');
INSERT INTO `pay_grant` VALUES ('15', '10000038', '0', '0', '100000', '123', '123', '1', '1507685729');
INSERT INTO `pay_grant` VALUES ('16', '10000040', '0', '0', '100000', '100000', '100000000000', '1', '1507772187');
INSERT INTO `pay_grant` VALUES ('17', '10000045', '0', '0', '100000', '100000', '100000000', '1', '1507795202');
INSERT INTO `pay_grant` VALUES ('18', '10000046', '0', '0', '100000', '100000', '10000046', '1', '1507795235');
INSERT INTO `pay_grant` VALUES ('19', '10000053', '0', '0', '100000', '100000', '10000053', '1', '1507795259');
INSERT INTO `pay_grant` VALUES ('20', '10000049', '0', '42690', '100000', '111', '111', '1', '1507795480');
INSERT INTO `pay_grant` VALUES ('21', '10000052', '0', '0', '100000', '100000', '100000', '1', '1507795869');
INSERT INTO `pay_grant` VALUES ('22', '10000054', '0', '0', '100000', '100000', 'v', '1', '1507795904');
INSERT INTO `pay_grant` VALUES ('23', '10000047', '0', '0', '100000', '100000', '100', '1', '1507799535');
INSERT INTO `pay_grant` VALUES ('24', '10000056', '0', '0', '100000', '100000', '100000000000', '1', '1507879052');
INSERT INTO `pay_grant` VALUES ('25', '10000043', '0', '0', '100000', '12312', '测试充值100000', '1', '1508122050');
INSERT INTO `pay_grant` VALUES ('26', '10000044', '0', '0', '100000', '1111', '11111', '1', '1508122064');
INSERT INTO `pay_grant` VALUES ('27', '10000061', '0', '0', '100000', '1231', '23123', '1', '1508122081');
INSERT INTO `pay_grant` VALUES ('28', '10000042', '0', '100', '100000', '100000', '3123', '1', '1508122092');
INSERT INTO `pay_grant` VALUES ('29', '10000062', '0', '0', '100000', '1231', '23123', '1', '1508122363');
INSERT INTO `pay_grant` VALUES ('30', '10000060', '0', '0', '100000', '123', '123123', '1', '1508122375');
INSERT INTO `pay_grant` VALUES ('31', '10000065', '0', '0', '100000', '123', '123123', '1', '1508123129');
INSERT INTO `pay_grant` VALUES ('32', '10000064', '0', '0', '12312', '3123', '123123', '1', '1508123191');
INSERT INTO `pay_grant` VALUES ('33', '10000063', '0', '0', '100000', '100000', '31', '1', '1508123602');
INSERT INTO `pay_grant` VALUES ('34', '10000052', '6868', '100000', '100000', '2312', '3123', '1', '1508123614');
INSERT INTO `pay_grant` VALUES ('35', '10000066', '0', '0', '100000', '3123', '123123', '1', '1508123627');
INSERT INTO `pay_grant` VALUES ('36', '10000067', '0', '0', '100000', '1231', '23123', '1', '1508125817');
INSERT INTO `pay_grant` VALUES ('37', '10000067', '100000', '1231', '100000', '100000', '10000067', '1', '1508125898');
INSERT INTO `pay_grant` VALUES ('38', '10000066', '77972', '3123', '100000', '100000', '10000066', '1', '1508125910');
INSERT INTO `pay_grant` VALUES ('39', '10000052', '107410', '102312', '100000', '100000', '10000052', '1', '1508125922');
INSERT INTO `pay_grant` VALUES ('40', '10000063', '89073', '100000', '100000', '100000', '10000063', '1', '1508125930');
INSERT INTO `pay_grant` VALUES ('41', '10000064', '13638', '3123', '100000', '100000', '10000064', '1', '1508125940');
INSERT INTO `pay_grant` VALUES ('42', '10000065', '100153', '123', '100000', '100000', '10000065', '1', '1508125950');
INSERT INTO `pay_grant` VALUES ('43', '10000060', '100980', '12651', '100000', '100000', '10000060', '1', '1508125959');
INSERT INTO `pay_grant` VALUES ('44', '10000062', '26370', '1231', '100000', '100000', '10000062', '1', '1508125967');
INSERT INTO `pay_grant` VALUES ('45', '10000042', '101158', '1438068', '100000', '100000', '10000042', '1', '1508125976');
INSERT INTO `pay_grant` VALUES ('46', '10000061', '88632', '1231', '100000', '100000', '10000061', '1', '1508125985');
INSERT INTO `pay_grant` VALUES ('47', '10000044', '82031', '1111', '100000', '100000', '10000044', '1', '1508125993');
INSERT INTO `pay_grant` VALUES ('48', '10000043', '100000', '106392', '100000', '100000', '10000043', '1', '1508126001');
INSERT INTO `pay_grant` VALUES ('49', '10000053', '94131', '135480', '100000', '100000', '10000053', '1', '1508126011');
INSERT INTO `pay_grant` VALUES ('50', '10000046', '93637', '132216', '100000', '100000', '10000046', '1', '1508126019');
INSERT INTO `pay_grant` VALUES ('51', '10000045', '61956', '76762', '100000', '100000', '10000045', '1', '1508126028');
INSERT INTO `pay_grant` VALUES ('52', '10000053', '194131', '235480', '100000', '100000', '10000053', '1', '1508126083');
INSERT INTO `pay_grant` VALUES ('53', '10000067', '200000', '101231', '100000', '100000', '10000067', '1', '1508126121');
INSERT INTO `pay_grant` VALUES ('54', '10000067', '300000', '201231', '100000', '100000', '10000067', '1', '1508131985');
INSERT INTO `pay_grant` VALUES ('55', '10000041', '500', '9872', '100000', '1', '2', '1', '1508132065');
INSERT INTO `pay_grant` VALUES ('56', '10000075', '0', '17112', '100000', '100000', '111111', '1', '1508296143');
INSERT INTO `pay_grant` VALUES ('57', '10000078', '0', '0', '100000', '100000', '1000000', '1', '1508392766');
INSERT INTO `pay_grant` VALUES ('58', '10000051', '0', '105120', '100000', '100000', '111111', '1', '1508395571');
INSERT INTO `pay_grant` VALUES ('59', '10000068', '0', '0', '100000', '100000', '1000000', '1', '1508401258');
INSERT INTO `pay_grant` VALUES ('60', '10000034', '6701', '974596', '100000', '100000', '0', '1', '1508485043');
INSERT INTO `pay_grant` VALUES ('61', '10000034', '9681', '1074596', '100000', '100000', '000', '1', '1508485233');
INSERT INTO `pay_grant` VALUES ('62', '10000068', '67202', '100000', '1', '2', '000', '1', '1509429904');
INSERT INTO `pay_grant` VALUES ('63', '10000068', '67203', '100002', '1', '2', '---', '1', '1509429929');
INSERT INTO `pay_grant` VALUES ('64', '10000033', '2444', '2015936', '1', '2', '---', '1', '1509430101');
INSERT INTO `pay_grant` VALUES ('65', '10000036', '441', '634026', '100000', '100000', '100000000000', '1', '1510136136');
INSERT INTO `pay_grant` VALUES ('66', '10000034', '187', '1176256', '100000', '100000', '100000000000', '1', '1510644947');
INSERT INTO `pay_grant` VALUES ('67', '10000038', '1', '1861957', '100000', '100000', '1000000', '1', '1510644985');
INSERT INTO `pay_grant` VALUES ('68', '10000034', '1', '1473368', '100000', '100000', '100000000', '1', '1510901854');
INSERT INTO `pay_grant` VALUES ('69', '10000092', '0', '0', '1000', '0', '0', '1', '1510903106');
INSERT INTO `pay_grant` VALUES ('70', '10000033', '410', '2151362', '100000', '100000', '123123', '1', '1511158305');
INSERT INTO `pay_grant` VALUES ('71', '10000036', '80041', '984206', '100000', '100000', '1000000', '1', '1511158317');
INSERT INTO `pay_grant` VALUES ('72', '10000038', '5', '1962083', '100000', '100000', '123123', '1', '1511158384');
INSERT INTO `pay_grant` VALUES ('73', '10000033', '7970', '2251362', '100000', '100000', '100000000000000', '1', '1511158595');
INSERT INTO `pay_grant` VALUES ('74', '10000033', '107970', '2351362', '100000', '100000', '100000000000000', '1', '1511158611');
INSERT INTO `pay_grant` VALUES ('75', '10000036', '180041', '1084206', '100000', '100000', '100000000000000', '1', '1511158625');
INSERT INTO `pay_grant` VALUES ('76', '10000041', '20128', '393823', '100000', '100000', '1', '1', '1511242956');
INSERT INTO `pay_grant` VALUES ('77', '10000034', '4', '2220848', '100000', '100000', '1000000000000000000000000000', '1', '1511256739');
INSERT INTO `pay_grant` VALUES ('78', '10000034', '100004', '2320848', '100000', '100000', '10000034', '1', '1511256758');
INSERT INTO `pay_grant` VALUES ('79', '10000034', '200004', '2420848', '100000', '100000', '10000034', '1', '1511256766');
INSERT INTO `pay_grant` VALUES ('80', '10000034', '300004', '2520848', '100000', '100000', '10000034', '1', '1511256773');
INSERT INTO `pay_grant` VALUES ('81', '10000034', '400004', '2620848', '100000', '100000', '10000034', '1', '1511256782');
INSERT INTO `pay_grant` VALUES ('82', '10000034', '500004', '2720848', '100000', '100000', '10000034', '1', '1511256789');
INSERT INTO `pay_grant` VALUES ('83', '10000034', '600004', '2820848', '100000', '100000', '10000034', '1', '1511256797');
INSERT INTO `pay_grant` VALUES ('84', '10000041', '7295', '911272', '100000', '100000', '1', '1', '1511257135');
INSERT INTO `pay_grant` VALUES ('85', '10000056', '28567', '100000', '100000', '100000', '10000000000', '1', '1511418135');
INSERT INTO `pay_grant` VALUES ('86', '10000102', '0', '0', '100000', '100000', '1000000', '1', '1511591148');
INSERT INTO `pay_grant` VALUES ('87', '10000158', '0', '0', '10000', '0', '0', '1', '1512717364');
INSERT INTO `pay_grant` VALUES ('88', '10000100', '0', '0', '1', '1', '1', '1', '1512785013');
INSERT INTO `pay_grant` VALUES ('89', '10000041', '298', '1534770', '100000', '100000', 'q', '1', '1512803515');
INSERT INTO `pay_grant` VALUES ('90', '10000041', '298', '1634770', '100000', '100000', '1', '1', '1512803591');
INSERT INTO `pay_grant` VALUES ('91', '10000041', '17758', '1734770', '100000', '100000', '1', '1', '1512803648');
INSERT INTO `pay_grant` VALUES ('92', '10000041', '117758', '1834770', '100000', '100000', '1', '1', '1512803692');
INSERT INTO `pay_grant` VALUES ('93', '10000041', '241138', '1934770', '100000', '100000', '1', '1', '1512803751');
INSERT INTO `pay_grant` VALUES ('94', '10000041', '190366', '2034770', '100000', '100000', '1', '1', '1513153328');
INSERT INTO `pay_grant` VALUES ('95', '10000158', '357', '11587592', '100000', '1', '1', '1', '1514189320');
INSERT INTO `pay_grant` VALUES ('96', '10000193', '0', '0', '100000', '0', '1', '1', '1514190520');
INSERT INTO `pay_grant` VALUES ('97', '10000092', '932', '0', '100000', '0', '0', '1', '1514253205');
INSERT INTO `pay_grant` VALUES ('98', '10000089', '41', '0', '100000', '100000', '10000089', '1', '1514253519');
INSERT INTO `pay_grant` VALUES ('99', '10000092', '317', '0', '100000', '100000', '111111', '1', '1514253945');
INSERT INTO `pay_grant` VALUES ('100', '10000077', '0', '0', '100000', '100000', '10000000000', '1', '1514340013');
INSERT INTO `pay_grant` VALUES ('101', '10000203', '0', '0', '100000', '11111', '1', '1', '1515745378');
INSERT INTO `pay_grant` VALUES ('102', '10000203', '100000', '11111', '100000', '1111', '1', '1', '1515745390');
INSERT INTO `pay_grant` VALUES ('103', '10000204', '0', '0', '100000', '100000', '100', '1', '1515745497');
INSERT INTO `pay_grant` VALUES ('104', '10000202', '0', '0', '100000', '1000', ' ', '1', '1515745509');
INSERT INTO `pay_grant` VALUES ('105', '10000202', '100000', '1000', '100000', '10', ' ', '1', '1515745520');
INSERT INTO `pay_grant` VALUES ('106', '10000204', '100000', '100000', '100000', '1', '1', '1', '1515745531');
INSERT INTO `pay_grant` VALUES ('107', '10000201', '0', '0', '100000', '1000', ' ', '1', '1515745547');
INSERT INTO `pay_grant` VALUES ('108', '10000040', '89491', '103980', '100000', '100000', '10000040', '1', '1515748234');
INSERT INTO `pay_grant` VALUES ('109', '10000040', '189491', '203980', '100000', '123', '123', '1', '1515748242');
INSERT INTO `pay_grant` VALUES ('110', '10000203', '12933', '12222', '100000', '1', '1', '1', '1515750293');
INSERT INTO `pay_grant` VALUES ('111', '10000034', '5949', '245777889', '100000', '100000', '10000034', '1', '1515750822');
INSERT INTO `pay_grant` VALUES ('112', '10000034', '105949', '245877889', '100000', '100000', '10000034', '1', '1515750828');
INSERT INTO `pay_grant` VALUES ('113', '10000034', '205949', '245977889', '100000', '100000', '10000034', '1', '1515750835');
INSERT INTO `pay_grant` VALUES ('114', '10000034', '305949', '246077889', '100000', '100000', '10000034', '1', '1515750842');
INSERT INTO `pay_grant` VALUES ('115', '10000034', '405949', '246177889', '100000', '100000', '10000034', '1', '1515750848');
INSERT INTO `pay_grant` VALUES ('116', '10000034', '505949', '246277889', '100000', '100000', '10000034', '1', '1515750856');
INSERT INTO `pay_grant` VALUES ('117', '10000034', '605949', '246377889', '100000', '100000', '10000034', '1', '1515750868');
INSERT INTO `pay_grant` VALUES ('118', '10000203', '50717', '12223', '100000', '100000', '10000203', '1', '1515750877');
INSERT INTO `pay_grant` VALUES ('119', '10000203', '150717', '112223', '100000', '100000', '10000203', '1', '1515750884');
INSERT INTO `pay_grant` VALUES ('120', '10000203', '250717', '212223', '100000', '100000', '10000203', '1', '1515750891');
INSERT INTO `pay_grant` VALUES ('121', '10000203', '350717', '312223', '100000', '100000', '10000203', '1', '1515750896');
INSERT INTO `pay_grant` VALUES ('122', '10000203', '450717', '412223', '100000', '100000', '10000203', '1', '1515750902');
INSERT INTO `pay_grant` VALUES ('123', '10000202', '314964', '1010', '100000', '100000', '10000202', '1', '1515750911');
INSERT INTO `pay_grant` VALUES ('124', '10000202', '414964', '101010', '100000', '100000', '10000202', '1', '1515750916');
INSERT INTO `pay_grant` VALUES ('125', '10000202', '514964', '201010', '100000', '100000', '10000202', '1', '1515750922');
INSERT INTO `pay_grant` VALUES ('126', '10000204', '260290', '106721', '100000', '100000', '10000204', '1', '1515750932');
INSERT INTO `pay_grant` VALUES ('127', '10000204', '360290', '206721', '100000', '100000', '10000204', '1', '1515750938');
INSERT INTO `pay_grant` VALUES ('128', '10000204', '460290', '306721', '100000', '100000', '10000204', '1', '1515750945');
INSERT INTO `pay_grant` VALUES ('129', '10000041', '21', '2154916', '100000', '0', '1', '1', '1515979470');
INSERT INTO `pay_grant` VALUES ('130', '10000041', '100021', '2154916', '100000', '1', '1', '1', '1515979495');
INSERT INTO `pay_grant` VALUES ('131', '10000041', '200021', '2154917', '100000', '1', '10000', '1', '1515979523');
INSERT INTO `pay_grant` VALUES ('132', '10000205', '0', '0', '100000', '1', '1', '1', '1516001528');
INSERT INTO `pay_grant` VALUES ('133', '10000206', '0', '6', '100000', '1', '1', '1', '1516006403');
INSERT INTO `pay_grant` VALUES ('134', '10000207', '0', '0', '100000', '1', '1', '1', '1516259250');
INSERT INTO `pay_grant` VALUES ('135', '10000207', '100000', '1', '100000', '1', '1', '1', '1516259250');
INSERT INTO `pay_grant` VALUES ('136', '10000207', '200000', '2', '100000', '1', '1', '1', '1516259250');
INSERT INTO `pay_grant` VALUES ('137', '10000207', '300000', '3', '100000', '1', '1', '1', '1516259250');
INSERT INTO `pay_grant` VALUES ('138', '10000207', '400000', '4', '100000', '1', '1', '1', '1516259250');
INSERT INTO `pay_grant` VALUES ('139', '10000207', '500000', '5', '100000', '1', '1', '1', '1516259250');
INSERT INTO `pay_grant` VALUES ('140', '10000207', '600000', '6', '100000', '1', '1', '1', '1516259278');
INSERT INTO `pay_grant` VALUES ('141', '10000207', '700000', '7', '100000', '1', '1', '1', '1516259310');
INSERT INTO `pay_grant` VALUES ('142', '10000210', '0', '221150', '100000', '1', '1', '1', '1521251345');
INSERT INTO `pay_grant` VALUES ('143', '10000209', '0', '0', '100000', '1', '1', '1', '1521428266');
INSERT INTO `pay_grant` VALUES ('144', '10000216', '0', '0', '100000', '1', '1', '1', '1521429180');
INSERT INTO `pay_grant` VALUES ('145', '10000217', '0', '0', '100000', '1', '1', '1', '1521429199');
INSERT INTO `pay_grant` VALUES ('146', '10000041', '2508529', '2712068', '1010000000', '1000000000', '1', '1', '1522459487');
INSERT INTO `pay_grant` VALUES ('147', '10000092', '882', '100000', '1111111111', '1', '1', '1', '1523958620');
INSERT INTO `pay_grant` VALUES ('148', '10000213', '0', '0', '10000000', '21121', 'hhh', '1', '1524537990');
INSERT INTO `pay_grant` VALUES ('149', '10000217', '8018', '289', '100000000', '1', '100000000', '1', '1524737245');
INSERT INTO `pay_grant` VALUES ('150', '10000041', '985499642', '1003033390', '1000', '1', '1', '1', '1526525397');
INSERT INTO `pay_grant` VALUES ('151', '10000220', '0', '0', '20000000', '0', '测试', '1', '1526631849');
INSERT INTO `pay_grant` VALUES ('152', '10000034', '687', '247369431', '10000034', '10000034', '10000034', '1', '1526864421');
INSERT INTO `pay_grant` VALUES ('153', '10000003', '0', '0', '10000003', '10000003', '10000003', '1', '1526870812');

-- ----------------------------
-- Table structure for `pay_mall_list`
-- ----------------------------
DROP TABLE IF EXISTS `pay_mall_list`;
CREATE TABLE `pay_mall_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gid` int(11) NOT NULL COMMENT '商品id',
  `gname` varchar(100) NOT NULL COMMENT '商品名称',
  `srcuid` int(11) NOT NULL COMMENT '购买的用户id',
  `srcnickname` varchar(50) NOT NULL COMMENT '购买的用户昵称',
  `dstuid` int(11) NOT NULL COMMENT '受益人的id',
  `dstnickname` varchar(50) NOT NULL COMMENT '受益人的昵称',
  `count` int(11) NOT NULL COMMENT '数量',
  `price` int(11) NOT NULL COMMENT '单价',
  `realprice` int(11) NOT NULL COMMENT '实际支付单价',
  `pricetotal` int(11) NOT NULL COMMENT '总价',
  `realpricetotal` int(11) NOT NULL COMMENT '实际支付总价',
  `credit` int(11) NOT NULL COMMENT '声援值',
  `starttime` int(11) NOT NULL COMMENT '开始时间 (冗余写入当前购买的服务开始时间)',
  `endtime` int(11) NOT NULL COMMENT '结束时间(冗余写入当前购买的服务的结束时间)',
  `type` int(2) NOT NULL COMMENT '类型 1守护 2vip 3商城道具',
  `createAt` int(11) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=220 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of pay_mall_list
-- ----------------------------
INSERT INTO `pay_mall_list` VALUES ('1', '45', '骑士守护', '10000017', '135****2959', '10000020', '197****2222', '1', '1314', '1314', '1314', '1314', '10512', '1506677891', '1509269891', '1', '1506677892');
INSERT INTO `pay_mall_list` VALUES ('2', '45', '骑士守护', '10000026', '151****0762', '10000025', '151****8955', '1', '1314', '1314', '1314', '1314', '10512', '1506762118', '1509354118', '1', '1506762119');
INSERT INTO `pay_mall_list` VALUES ('3', '46', '王子守护', '10000026', '151****0762', '10000025', '151****8955', '1', '13140', '13140', '13140', '13140', '105120', '1506762167', '1509354167', '1', '1506762167');
INSERT INTO `pay_mall_list` VALUES ('4', '46', '王子守护', '10000025', '151****8955', '10000026', '151****0762', '1', '13140', '13140', '13140', '13140', '105120', '1506762587', '1509354587', '1', '1506762587');
INSERT INTO `pay_mall_list` VALUES ('5', '46', '王子守护', '10000032', '159****5378', '10000030', '哈喽', '1', '13140', '13140', '13140', '13140', '105120', '1507537833', '1510216233', '1', '1507537834');
INSERT INTO `pay_mall_list` VALUES ('6', '45', '骑士守护', '10000036', '151****0762', '10000033', '151****8955', '1', '1314', '1314', '1314', '1314', '10512', '1507621296', '1510299696', '1', '1507621296');
INSERT INTO `pay_mall_list` VALUES ('7', '45', '骑士守护', '10000032', '159****5378', '10000033', '还好', '1', '1314', '1314', '1314', '1314', '10512', '1507623848', '1510302248', '1', '1507623848');
INSERT INTO `pay_mall_list` VALUES ('8', '46', '王子守护', '10000052', '177****3873', '10000049', '150****2022', '1', '13140', '13140', '13140', '13140', '105120', '1507795914', '1510474314', '1', '1507795914');
INSERT INTO `pay_mall_list` VALUES ('9', '46', '王子守护', '10000049', '150****2022', '10000054', '188****4360', '1', '13140', '13140', '13140', '13140', '105120', '1507798243', '1510476643', '1', '1507798243');
INSERT INTO `pay_mall_list` VALUES ('10', '46', '王子守护', '10000047', '孤独患者', '10000033', '还好', '1', '13140', '13140', '13140', '13140', '105120', '1507799824', '1510478224', '1', '1507799824');
INSERT INTO `pay_mall_list` VALUES ('11', '45', '骑士守护', '10000036', '喜羊羊', '10000045', '泰迪', '1', '1314', '1314', '1314', '1314', '10512', '1507877067', '1510555467', '1', '1507877067');
INSERT INTO `pay_mall_list` VALUES ('12', '43', '白金VIP', '10000036', '喜羊羊', '10000036', '喜羊羊', '1', '999', '999', '999', '999', '0', '1507877387', '1510555787', '2', '1507877387');
INSERT INTO `pay_mall_list` VALUES ('13', '91', '圣诞雪橇', '10000036', '喜羊羊', '10000036', '喜羊羊', '1', '300', '300', '300', '300', '0', '1507877422', '1510555822', '4', '1507877422');
INSERT INTO `pay_mall_list` VALUES ('14', '45', '骑士守护', '10000056', '132****1855', '10000033', '还好', '1', '1314', '1314', '1314', '1314', '10512', '1507879154', '1510557554', '1', '1507879154');
INSERT INTO `pay_mall_list` VALUES ('15', '87', '兰博基尼', '10000034', '小伙子', '10000034', '小伙子', '1', '2980', '2980', '2980', '2980', '0', '1507881477', '1510559877', '4', '1507881477');
INSERT INTO `pay_mall_list` VALUES ('16', '44', '钻石VIP', '10000034', '小伙子', '10000034', '小伙子', '1', '9999', '9999', '9999', '9999', '0', '1507881500', '1510559900', '2', '1507881500');
INSERT INTO `pay_mall_list` VALUES ('17', '46', '王子守护', '10000036', '村长', '10000042', '188****2809', '1', '13140', '13140', '13140', '13140', '105120', '1508123623', '1510802023', '1', '1508123624');
INSERT INTO `pay_mall_list` VALUES ('18', '45', '骑士守护', '10000061', '马云龙', '10000060', '陈妍', '1', '1314', '1314', '1314', '1314', '10512', '1508124294', '1510802694', '1', '1508124294');
INSERT INTO `pay_mall_list` VALUES ('19', '60', '萌猪卡', '10000042', '小钰', '10000044', '计宏雷', '1', '150', '150', '150', '150', '0', '1508125224', '1510803624', '3', '1508125224');
INSERT INTO `pay_mall_list` VALUES ('20', '60', '萌猪卡', '10000042', '小钰', '10000042', '小钰', '1', '150', '150', '150', '150', '0', '1508125281', '1510803681', '3', '1508125281');
INSERT INTO `pay_mall_list` VALUES ('21', '87', '兰博基尼', '10000044', '计宏雷', '10000044', '计宏雷', '1', '2980', '2980', '2980', '2980', '0', '1508125302', '1510803702', '4', '1508125302');
INSERT INTO `pay_mall_list` VALUES ('22', '45', '骑士守护', '10000044', '计宏雷', '10000042', '小钰', '1', '1314', '1314', '1314', '1314', '10512', '1508125405', '1510803805', '1', '1508125405');
INSERT INTO `pay_mall_list` VALUES ('23', '46', '王子守护', '10000044', '计宏雷', '10000042', '小钰', '1', '13140', '13140', '13140', '13140', '105120', '1508125646', '1510804046', '1', '1508125646');
INSERT INTO `pay_mall_list` VALUES ('24', '45', '骑士守护', '10000060', '陈妍', '10000061', '马云龙', '1', '1314', '1314', '1314', '1314', '10512', '1508126153', '1510804553', '1', '1508126153');
INSERT INTO `pay_mall_list` VALUES ('25', '45', '骑士守护', '10000062', '赵延龙', '10000042', '小钰', '1', '1314', '1314', '1314', '1314', '10512', '1508126203', '1510804603', '1', '1508126203');
INSERT INTO `pay_mall_list` VALUES ('26', '46', '王子守护', '10000062', '赵延龙', '10000042', '小钰', '1', '13140', '13140', '13140', '13140', '105120', '1508126239', '1510804639', '1', '1508126239');
INSERT INTO `pay_mall_list` VALUES ('27', '44', '钻石VIP', '10000061', '马云龙', '10000061', '马云龙', '1', '9999', '9999', '9999', '9999', '0', '1508132929', '1510811329', '2', '1508132929');
INSERT INTO `pay_mall_list` VALUES ('28', '87', '兰博基尼', '10000061', '马云龙', '10000061', '马云龙', '1', '2980', '2980', '2980', '2980', '0', '1508132947', '1510811347', '4', '1508132947');
INSERT INTO `pay_mall_list` VALUES ('29', '60', '萌猪卡', '10000061', '马云龙', '10000061', '马云龙', '1', '150', '150', '150', '150', '0', '1508132956', '1510811356', '3', '1508132956');
INSERT INTO `pay_mall_list` VALUES ('30', '45', '骑士守护', '10000052', '177****3873', '10000042', '小钰', '1', '1314', '1314', '1314', '1314', '10512', '1508133389', '1510811789', '1', '1508133389');
INSERT INTO `pay_mall_list` VALUES ('31', '45', '骑士守护', '10000045', '泰迪', '10000054', '王小文', '1', '1314', '1314', '1314', '1314', '10512', '1508133433', '1510811833', '1', '1508133433');
INSERT INTO `pay_mall_list` VALUES ('32', '46', '王子守护', '10000052', '177****3873', '10000042', '小钰', '1', '13140', '13140', '13140', '13140', '105120', '1508133518', '1510811918', '1', '1508133518');
INSERT INTO `pay_mall_list` VALUES ('33', '45', '骑士守护', '10000054', '王小文', '10000045', '泰迪', '1', '1314', '1314', '1314', '1314', '10512', '1508133570', '1510811970', '1', '1508133570');
INSERT INTO `pay_mall_list` VALUES ('34', '46', '王子守护', '10000063', '喜剧之王', '10000042', '小钰', '1', '13140', '13140', '13140', '13140', '105120', '1508133614', '1510812014', '1', '1508133615');
INSERT INTO `pay_mall_list` VALUES ('35', '46', '王子守护', '10000061', '马云龙', '10000042', '小钰', '1', '13140', '13140', '13140', '13140', '105120', '1508134768', '1510813168', '1', '1508134768');
INSERT INTO `pay_mall_list` VALUES ('36', '44', '钻石VIP', '10000063', '喜剧之王', '10000063', '喜剧之王', '1', '9999', '9999', '9999', '9999', '0', '1508134926', '1510813326', '2', '1508134926');
INSERT INTO `pay_mall_list` VALUES ('37', '87', '兰博基尼', '10000065', '小新', '10000065', '小新', '1', '2980', '2980', '2980', '2980', '0', '1508135230', '1510813630', '4', '1508135230');
INSERT INTO `pay_mall_list` VALUES ('38', '43', '白金VIP', '10000065', '小新', '10000065', '小新', '1', '999', '999', '999', '999', '0', '1508135340', '1510813740', '2', '1508135340');
INSERT INTO `pay_mall_list` VALUES ('39', '46', '王子守护', '10000064', '张宇飞', '10000063', '喜剧之王', '1', '13140', '13140', '13140', '13140', '105120', '1508135625', '1510814025', '1', '1508135625');
INSERT INTO `pay_mall_list` VALUES ('40', '44', '钻石VIP', '10000034', '小伙子', '10000034', '小伙子', '1', '9999', '9999', '9999', '9999', '0', '1507881500', '1513151900', '2', '1508201910');
INSERT INTO `pay_mall_list` VALUES ('41', '43', '白金VIP', '10000038', '135****2959', '10000038', '135****2959', '1', '999', '999', '999', '999', '0', '1508227974', '1510906374', '2', '1508227974');
INSERT INTO `pay_mall_list` VALUES ('42', '45', '骑士守护', '10000038', '135****2959', '10000034', '0000', '1', '1314', '1314', '1314', '1314', '10512', '1508228570', '1510906970', '1', '1508228570');
INSERT INTO `pay_mall_list` VALUES ('43', '45', '骑士守护', '10000041', '186****9235', '10000034', '0000', '1', '1314', '1314', '1314', '1314', '10512', '1508229457', '1510907857', '1', '1508229457');
INSERT INTO `pay_mall_list` VALUES ('44', '45', '骑士守护', '10000060', '美妍妍', '10000075', '152****8533', '1', '1314', '1314', '1314', '1314', '10512', '1508295991', '1510974391', '1', '1508295991');
INSERT INTO `pay_mall_list` VALUES ('45', '46', '王子守护', '10000061', '马云龙', '10000075', '152****8533', '1', '13140', '13140', '13140', '13140', '105120', '1508297090', '1510975490', '1', '1508297090');
INSERT INTO `pay_mall_list` VALUES ('46', '45', '骑士守护', '10000067', '177****2658', '10000042', '小钰', '1', '1314', '1314', '1314', '1314', '10512', '1508297546', '1510975946', '1', '1508297547');
INSERT INTO `pay_mall_list` VALUES ('47', '88', 'mini', '10000034', '0000', '10000034', '0000', '1', '660', '660', '660', '660', '0', '1508320468', '1510998868', '4', '1508320468');
INSERT INTO `pay_mall_list` VALUES ('48', '46', '王子守护', '10000034', '0000', '10000041', '186****9235', '1', '13140', '13140', '13140', '13140', '105120', '1508379756', '1511058156', '1', '1508379756');
INSERT INTO `pay_mall_list` VALUES ('49', '46', '王子守护', '10000041', '186****9235', '10000034', '0000', '1', '13140', '13140', '13140', '13140', '105120', '1508380011', '1511058411', '1', '1508380011');
INSERT INTO `pay_mall_list` VALUES ('50', '46', '王子守护', '10000038', '135****2959', '10000036', '村长', '1', '13140', '13140', '13140', '13140', '105120', '1508381391', '1511059791', '1', '1508381391');
INSERT INTO `pay_mall_list` VALUES ('51', '46', '王子守护', '10000034', '0000', '10000041', '186****9235', '1', '13140', '10512', '13140', '10512', '105120', '1508379756', '1513650156', '1', '1508381562');
INSERT INTO `pay_mall_list` VALUES ('52', '46', '王子守护', '10000041', '186****9235', '10000036', '村长', '1', '13140', '13140', '13140', '13140', '105120', '1508381698', '1511060098', '1', '1508381698');
INSERT INTO `pay_mall_list` VALUES ('53', '46', '王子守护', '10000036', '村长', '10000033', '还好', '1', '13140', '13140', '13140', '13140', '105120', '1508392139', '1511070539', '1', '1508392139');
INSERT INTO `pay_mall_list` VALUES ('54', '46', '王子守护', '10000036', '村长', '10000051', '185****9958', '1', '13140', '13140', '13140', '13140', '105120', '1508392270', '1511070670', '1', '1508392270');
INSERT INTO `pay_mall_list` VALUES ('55', '46', '王子守护', '10000078', '123****8912', '10000033', '还好', '1', '13140', '13140', '13140', '13140', '105120', '1508392829', '1511071229', '1', '1508392829');
INSERT INTO `pay_mall_list` VALUES ('56', '45', '骑士守护', '10000051', '185****9958', '10000036', '村长', '1', '1314', '1314', '1314', '1314', '10512', '1508395709', '1511074109', '1', '1508395709');
INSERT INTO `pay_mall_list` VALUES ('57', '46', '王子守护', '10000068', '噶咯', '10000036', '村长', '1', '13140', '13140', '13140', '13140', '105120', '1508467361', '1511145761', '1', '1508467361');
INSERT INTO `pay_mall_list` VALUES ('58', '46', '王子守护', '10000034', '0000', '10000038', '135****2959', '1', '13140', '13140', '13140', '13140', '105120', '1508738930', '1511417330', '1', '1508738930');
INSERT INTO `pay_mall_list` VALUES ('59', '46', '王子守护', '10000034', '0000', '10000038', '135****2959', '1', '13140', '10512', '13140', '10512', '105120', '1508738930', '1514009330', '1', '1508739097');
INSERT INTO `pay_mall_list` VALUES ('60', '46', '王子守护', '10000034', '0000', '10000038', '135****2959', '1', '13140', '10512', '13140', '10512', '105120', '1508738930', '1516687730', '1', '1508739145');
INSERT INTO `pay_mall_list` VALUES ('61', '45', '骑士守护', '10000033', '还好', '10000051', '185****9958', '1', '1314', '1314', '1314', '1314', '10512', '1508824723', '1511503123', '1', '1508824723');
INSERT INTO `pay_mall_list` VALUES ('62', '45', '骑士守护', '10000078', '123****8912', '10000051', '185****9958', '1', '1314', '1314', '1314', '1314', '10512', '1508826331', '1511504731', '1', '1508826331');
INSERT INTO `pay_mall_list` VALUES ('63', '60', '魔法卡', '10000038', '卖火柴的001', '10000038', '卖火柴的001', '1', '150', '150', '150', '150', '0', '1511159291', '1513751291', '3', '1511159291');
INSERT INTO `pay_mall_list` VALUES ('64', '43', '白金VIP', '10000038', '卖火柴的001', '10000038', '卖火柴的001', '1', '999', '999', '999', '999', '0', '1511159312', '1513751312', '2', '1511159312');
INSERT INTO `pay_mall_list` VALUES ('65', '45', '骑士守护', '10000038', '卖火柴的001', '10000041', '186****9235', '1', '1314', '1314', '1314', '1314', '10512', '1511244808', '1513836808', '1', '1511244808');
INSERT INTO `pay_mall_list` VALUES ('66', '45', '骑士守护', '10000041', '186****9235', '10000033', '星空', '1', '1314', '1314', '1314', '1314', '10512', '1511404830', '1513996830', '1', '1511404830');
INSERT INTO `pay_mall_list` VALUES ('67', '45', '骑士守护', '10000033', '星空', '10000036', '村长', '1', '1314', '1314', '1314', '1314', '10512', '1511410493', '1514002493', '1', '1511410493');
INSERT INTO `pay_mall_list` VALUES ('68', '46', '王子守护', '10000038', '卖火柴的001', '10000036', '村长', '1', '13140', '10512', '13140', '10512', '105120', '1508381391', '1513651791', '1', '1511410537');
INSERT INTO `pay_mall_list` VALUES ('69', '60', '魔法卡', '10000056', '132****1855', '10000056', '132****1855', '1', '150', '150', '150', '150', '0', '1511418636', '1514010636', '3', '1511418636');
INSERT INTO `pay_mall_list` VALUES ('70', '60', '魔法卡', '10000034', '0000', '10000034', '0000', '1', '150', '150', '150', '150', '0', '1511418747', '1514010747', '3', '1511418747');
INSERT INTO `pay_mall_list` VALUES ('71', '46', '王子守护', '10000036', '村长', '10000051', '喜羊羊', '1', '13140', '13140', '13140', '13140', '105120', '1511681941', '1514273941', '1', '1511681941');
INSERT INTO `pay_mall_list` VALUES ('72', '46', '王子守护', '10000041', '186****9235', '10000033', '星空', '12', '13140', '13140', '157680', '157680', '1261440', '1512025508', '1543561508', '1', '1512025508');
INSERT INTO `pay_mall_list` VALUES ('73', '89', 'smart', '10000036', '村长', '10000036', '村长', '1', '200', '200', '200', '200', '0', '1512437823', '1515116223', '4', '1512437823');
INSERT INTO `pay_mall_list` VALUES ('74', '45', '骑士守护', '10000051', '喜羊羊', '10000036', '村长', '1', '1314', '1314', '1314', '1314', '10512', '1512442434', '1515120834', '1', '1512442434');
INSERT INTO `pay_mall_list` VALUES ('75', '91', '圣诞雪橇', '10000051', '喜羊羊', '10000051', '喜羊羊', '1', '300', '300', '300', '300', '0', '1512442459', '1515120859', '4', '1512442459');
INSERT INTO `pay_mall_list` VALUES ('76', '87', '兰博基尼', '10000034', '0000', '10000034', '0000', '1', '2980', '2980', '2980', '2980', '0', '1512610104', '1515288504', '4', '1512610104');
INSERT INTO `pay_mall_list` VALUES ('77', '86', '奥迪TT', '10000034', '0000', '10000034', '0000', '1', '880', '880', '880', '880', '0', '1512610130', '1515288530', '4', '1512610130');
INSERT INTO `pay_mall_list` VALUES ('78', '86', '奥迪TT', '10000036', '村长', '10000036', '村长', '1', '880', '880', '880', '880', '0', '1512610179', '1515288579', '4', '1512610179');
INSERT INTO `pay_mall_list` VALUES ('79', '88', 'mini', '10000036', '村长', '10000036', '村长', '1', '660', '660', '660', '660', '0', '1512610207', '1515288607', '4', '1512610207');
INSERT INTO `pay_mall_list` VALUES ('80', '91', '圣诞雪橇', '10000036', '村长', '10000036', '村长', '1', '300', '300', '300', '300', '0', '1512610234', '1515288634', '4', '1512610234');
INSERT INTO `pay_mall_list` VALUES ('81', '86', '奥迪TT', '10000051', '喜羊羊', '10000051', '喜羊羊', '1', '880', '880', '880', '880', '0', '1512610286', '1515288686', '4', '1512610286');
INSERT INTO `pay_mall_list` VALUES ('82', '90', '莲花GTE', '10000051', '喜羊羊', '10000051', '喜羊羊', '1', '1880', '1880', '1880', '1880', '0', '1512610305', '1515288705', '4', '1512610305');
INSERT INTO `pay_mall_list` VALUES ('83', '91', '圣诞雪橇', '10000033', '星空', '10000033', '星空', '1', '300', '300', '300', '300', '0', '1512610352', '1515288752', '4', '1512610352');
INSERT INTO `pay_mall_list` VALUES ('84', '88', 'mini', '10000033', '星空', '10000033', '星空', '1', '660', '660', '660', '660', '0', '1512610366', '1515288766', '4', '1512610366');
INSERT INTO `pay_mall_list` VALUES ('85', '89', 'smart', '10000033', '星空', '10000033', '星空', '1', '200', '200', '200', '200', '0', '1512610376', '1515288776', '4', '1512610376');
INSERT INTO `pay_mall_list` VALUES ('86', '89', 'smart', '10000034', '0000', '10000034', '0000', '1', '200', '200', '200', '200', '0', '1512610438', '1515288838', '4', '1512610438');
INSERT INTO `pay_mall_list` VALUES ('87', '88', 'mini', '10000034', '0000', '10000034', '0000', '1', '660', '660', '660', '660', '0', '1512610441', '1515288841', '4', '1512610441');
INSERT INTO `pay_mall_list` VALUES ('88', '46', '王子守护', '10000041', '186****9235', '10000158', '鑫哥专用-大V', '1', '13140', '13140', '13140', '13140', '105120', '1512731832', '1515410232', '1', '1512731832');
INSERT INTO `pay_mall_list` VALUES ('89', '46', '王子守护', '10000041', '186****9235', '10000158', '鑫哥专用-大V', '1', '13140', '10512', '13140', '10512', '105120', '1512731832', '1518088632', '1', '1512731840');
INSERT INTO `pay_mall_list` VALUES ('90', '44', '钻石VIP', '10000041', '186****9235', '10000041', '186****9235', '9', '9999', '9999', '89991', '89991', '0', '1512732651', '1536406251', '2', '1512732651');
INSERT INTO `pay_mall_list` VALUES ('91', '44', '钻石VIP', '10000041', '186****9235', '10000041', '186****9235', '1', '9999', '9999', '9999', '9999', '0', '1512732651', '1538998251', '2', '1512732658');
INSERT INTO `pay_mall_list` VALUES ('92', '44', '钻石VIP', '10000041', '186****9235', '10000041', '186****9235', '9', '9999', '9999', '89991', '89991', '0', '1512732651', '1562585451', '2', '1512732666');
INSERT INTO `pay_mall_list` VALUES ('93', '46', '王子守护', '10000041', '186****9235', '10000158', '鑫哥专用-大V', '1', '13140', '10512', '13140', '10512', '105120', '1512731832', '1520507832', '1', '1512732834');
INSERT INTO `pay_mall_list` VALUES ('94', '46', '王子守护', '10000041', '186****9235', '10000158', '鑫哥专用-大V', '1', '13140', '10512', '13140', '10512', '105120', '1512731832', '1523186232', '1', '1512732921');
INSERT INTO `pay_mall_list` VALUES ('95', '46', '王子守护', '10000041', '186****9235', '10000158', '鑫哥专用-大V', '1', '13140', '10512', '13140', '10512', '105120', '1512731832', '1525778232', '1', '1512732989');
INSERT INTO `pay_mall_list` VALUES ('96', '46', '王子守护', '10000041', '186****9235', '10000158', '鑫哥专用-大V', '1', '13140', '10512', '13140', '10512', '105120', '1512731832', '1528456632', '1', '1512733506');
INSERT INTO `pay_mall_list` VALUES ('97', '46', '王子守护', '10000041', '186****9235', '10000158', '鑫哥专用-大V', '1', '13140', '10512', '13140', '10512', '105120', '1512731832', '1531048632', '1', '1512733914');
INSERT INTO `pay_mall_list` VALUES ('98', '46', '黄金守护', '10000041', '186****9235', '10000158', '鑫哥专用-大V', '1', '13140', '10512', '13140', '10512', '105120', '1512731832', '1533727032', '1', '1512734324');
INSERT INTO `pay_mall_list` VALUES ('99', '46', '黄金守护', '10000041', '186****9235', '10000158', '鑫哥专用-大V', '3', '13140', '10512', '39420', '31536', '315360', '1512731832', '1541675832', '1', '1512734379');
INSERT INTO `pay_mall_list` VALUES ('100', '46', '黄金守护', '10000034', '干嘛呢', '10000158', '鑫哥专用-大V', '9', '13140', '13140', '118260', '118260', '946080', '1512734409', '1536408009', '1', '1512734409');
INSERT INTO `pay_mall_list` VALUES ('101', '45', '白银守护', '10000041', '186****9235', '10000034', '干嘛呢', '12', '1314', '1314', '15768', '15768', '126144', '1512734525', '1544270525', '1', '1512734525');
INSERT INTO `pay_mall_list` VALUES ('102', '45', '白银守护', '10000041', '186****9235', '10000034', '干嘛呢', '12', '1314', '1182', '15768', '14191', '126144', '1512734525', '1575806525', '1', '1512735386');
INSERT INTO `pay_mall_list` VALUES ('103', '45', '白银守护', '10000034', '干嘛呢', '10000033', '星空', '1', '1314', '1314', '1314', '1314', '10512', '1512878081', '1515556481', '1', '1512878081');
INSERT INTO `pay_mall_list` VALUES ('104', '46', '黄金守护', '10000034', '干嘛呢', '10000033', '星空', '1', '13140', '13140', '13140', '13140', '105120', '1512878139', '1515556539', '1', '1512878139');
INSERT INTO `pay_mall_list` VALUES ('105', '46', '黄金守护', '10000034', '干嘛呢', '10000033', '星空', '1', '13140', '10512', '13140', '10512', '105120', '1512878139', '1518234939', '1', '1512878178');
INSERT INTO `pay_mall_list` VALUES ('106', '91', '圣诞雪橇', '10000077', '159****5378', '10000077', '159****5378', '1', '300', '300', '300', '300', '0', '1514341213', '1517019613', '4', '1514341213');
INSERT INTO `pay_mall_list` VALUES ('107', '87', '兰博基尼', '10000092', '185****8950', '10000092', '185****8950', '1', '2980', '2980', '2980', '2980', '0', '1514516030', '1517194430', '4', '1514516030');
INSERT INTO `pay_mall_list` VALUES ('108', '87', '兰博基尼', '10000092', '185****8950', '10000092', '185****8950', '1', '2980', '2980', '2980', '2980', '0', '1514516030', '1519786430', '4', '1514516079');
INSERT INTO `pay_mall_list` VALUES ('109', '43', '白金VIP', '10000092', '185****8950', '10000092', '185****8950', '1', '999', '999', '999', '999', '0', '1514516415', '1517194815', '2', '1514516415');
INSERT INTO `pay_mall_list` VALUES ('110', '43', '白金VIP', '10000092', '185****8950', '10000092', '185****8950', '1', '999', '999', '999', '999', '0', '1514516415', '1519786815', '2', '1514516433');
INSERT INTO `pay_mall_list` VALUES ('111', '44', '钻石VIP', '10000092', '185****8950', '10000092', '185****8950', '1', '9999', '9999', '9999', '9999', '0', '1514525466', '1517203866', '2', '1514525466');
INSERT INTO `pay_mall_list` VALUES ('112', '91', '圣诞雪橇', '10000092', '185****8950', '10000092', '185****8950', '1', '300', '300', '300', '300', '0', '1514525672', '1517204072', '4', '1514525672');
INSERT INTO `pay_mall_list` VALUES ('113', '46', '黄金守护', '10000033', '星空', '10000158', '鑫哥专用-大V', '1', '13140', '13140', '13140', '13140', '105120', '1514525847', '1517204247', '1', '1514525847');
INSERT INTO `pay_mall_list` VALUES ('114', '46', '黄金守护', '10000033', '星空', '10000158', '鑫哥专用-大V', '1', '13140', '10512', '13140', '10512', '105120', '1514525847', '1519796247', '1', '1514525964');
INSERT INTO `pay_mall_list` VALUES ('115', '46', '黄金守护', '10000092', '185****8950', '10000033', '星空', '1', '13140', '13140', '13140', '13140', '105120', '1514526887', '1517205287', '1', '1514526887');
INSERT INTO `pay_mall_list` VALUES ('116', '90', '莲花GTE', '10000092', '185****8950', '10000092', '185****8950', '1', '1880', '1880', '1880', '1880', '0', '1514526934', '1517205334', '4', '1514526934');
INSERT INTO `pay_mall_list` VALUES ('117', '88', 'mini', '10000092', '185****8950', '10000092', '185****8950', '1', '660', '660', '660', '660', '0', '1514526939', '1517205339', '4', '1514526939');
INSERT INTO `pay_mall_list` VALUES ('118', '44', '钻石VIP', '10000092', '185****8950', '10000092', '185****8950', '1', '9999', '9999', '9999', '9999', '0', '1514525466', '1519795866', '2', '1514526953');
INSERT INTO `pay_mall_list` VALUES ('119', '86', '奥迪TT', '10000092', '185****8950', '10000092', '185****8950', '1', '880', '880', '880', '880', '0', '1514527192', '1517205592', '4', '1514527192');
INSERT INTO `pay_mall_list` VALUES ('120', '44', '钻石VIP', '10000092', '185****8950', '10000041', '186****9235', '1', '9999', '9999', '9999', '9999', '0', '1512732651', '1583235051', '2', '1514529338');
INSERT INTO `pay_mall_list` VALUES ('121', '44', '钻石VIP', '10000092', '185****8950', '1', '135****2959', '3', '9999', '9999', '29997', '29997', '0', '1514529363', '1522305363', '2', '1514529363');
INSERT INTO `pay_mall_list` VALUES ('122', '86', '奥迪TT', '10000092', '185****8950', '10000041', '186****9235', '1', '880', '880', '880', '880', '0', '1514531020', '1517209420', '4', '1514531020');
INSERT INTO `pay_mall_list` VALUES ('123', '44', '钻石VIP', '10000092', '185****8950', '10000041', '186****9235', '1', '9999', '9999', '9999', '9999', '0', '1512732651', '1585913451', '2', '1514531069');
INSERT INTO `pay_mall_list` VALUES ('124', '45', '白银守护', '10000036', '村长', '10000051', '喜羊羊', '1', '1314', '1314', '1314', '1314', '10512', '1515482671', '1518161071', '1', '1515482671');
INSERT INTO `pay_mall_list` VALUES ('125', '45', '白银守护', '10000033', '星空', '10000051', '喜羊羊', '1', '1314', '1314', '1314', '1314', '10512', '1515482708', '1518161108', '1', '1515482708');
INSERT INTO `pay_mall_list` VALUES ('126', '45', '白银守护', '10000051', '喜羊羊', '10000036', '村长', '1', '1314', '1182', '1314', '1182', '10512', '1512442434', '1517799234', '1', '1515487141');
INSERT INTO `pay_mall_list` VALUES ('127', '45', '白银守护', '10000036', '村长', '10000051', '喜羊羊', '1', '1314', '1182', '1314', '1182', '10512', '1515482671', '1520580271', '1', '1515548770');
INSERT INTO `pay_mall_list` VALUES ('128', '45', '白银守护', '10000051', '喜羊羊', '10000041', '186****9235', '1', '1314', '1314', '1314', '1314', '10512', '1515548953', '1518227353', '1', '1515548953');
INSERT INTO `pay_mall_list` VALUES ('129', '45', '白银守护', '10000051', '喜羊羊', '10000033', '星空', '1', '1314', '1314', '1314', '1314', '10512', '1515548997', '1518227397', '1', '1515548997');
INSERT INTO `pay_mall_list` VALUES ('130', '45', '白银守护', '10000089', '123****8900', '10000033', '星空', '1', '1314', '1314', '1314', '1314', '10512', '1515549025', '1518227425', '1', '1515549025');
INSERT INTO `pay_mall_list` VALUES ('131', '43', '白金VIP', '10000204', '大橙子要来了啊', '10000204', '大橙子要来了啊', '1', '999', '999', '999', '999', '0', '1515747469', '1518425869', '2', '1515747469');
INSERT INTO `pay_mall_list` VALUES ('132', '45', '白银守护', '10000089', '123****8900', '10000040', '177****9573', '1', '1314', '1314', '1314', '1314', '10512', '1515750459', '1518428859', '1', '1515750459');
INSERT INTO `pay_mall_list` VALUES ('133', '46', '黄金守护', '10000034', '干嘛呢', '10000040', '177****9573', '1', '13140', '13140', '13140', '13140', '105120', '1515750596', '1518428996', '1', '1515750596');
INSERT INTO `pay_mall_list` VALUES ('134', '44', '钻石VIP', '10000202', '大橙子来了', '10000202', '大橙子来了', '1', '9999', '9999', '9999', '9999', '0', '1515980074', '1518658474', '2', '1515980074');
INSERT INTO `pay_mall_list` VALUES ('135', '87', '兰博基尼', '10000202', '大橙子来了', '10000202', '大橙子来了', '1', '2980', '2980', '2980', '2980', '0', '1515980154', '1518658554', '4', '1515980154');
INSERT INTO `pay_mall_list` VALUES ('136', '88', 'mini', '10000202', '大橙子来了', '10000202', '大橙子来了', '1', '660', '660', '660', '660', '0', '1515980158', '1518658558', '4', '1515980158');
INSERT INTO `pay_mall_list` VALUES ('137', '46', '黄金守护', '10000204', '大橙子要来了啊', '10000034', '干嘛呢', '1', '13140', '13140', '13140', '13140', '105120', '1515997236', '1518675636', '1', '1515997236');
INSERT INTO `pay_mall_list` VALUES ('138', '44', '钻石VIP', '10000041', '大为', '10000041', '大为', '3', '9999', '9999', '29997', '29997', '0', '1516257168', '1524029568', '2', '1516257168');
INSERT INTO `pay_mall_list` VALUES ('139', '46', '黄金守护', '10000034', '清风徐来', '10000193', '小岛', '1', '13140', '13140', '13140', '13140', '105120', '1516260628', '1518939028', '1', '1516260628');
INSERT INTO `pay_mall_list` VALUES ('140', '46', '黄金守护', '10000034', '清风徐来', '10000205', 'sober', '1', '13140', '13140', '13140', '13140', '105120', '1516260725', '1518939125', '1', '1516260725');
INSERT INTO `pay_mall_list` VALUES ('141', '45', '白银守护', '10000038', '蓝精灵', '10000205', 'sober', '1', '1314', '1314', '1314', '1314', '10512', '1516261397', '1518939797', '1', '1516261397');
INSERT INTO `pay_mall_list` VALUES ('142', '91', '圣诞雪橇', '10000036', '村长', '10000036', '村长', '1', '300', '300', '300', '300', '0', '1516603253', '1519281653', '4', '1516603253');
INSERT INTO `pay_mall_list` VALUES ('143', '86', '奥迪TT', '10000036', '村长', '10000036', '村长', '1', '880', '880', '880', '880', '0', '1516603279', '1519281679', '4', '1516603279');
INSERT INTO `pay_mall_list` VALUES ('144', '89', 'smart', '10000036', '村长', '10000036', '村长', '1', '200', '200', '200', '200', '0', '1516603311', '1519281711', '4', '1516603312');
INSERT INTO `pay_mall_list` VALUES ('145', '89', 'smart', '10000036', '村长', '10000036', '村长', '1', '200', '200', '200', '200', '0', '1516603311', '1521700911', '4', '1516603594');
INSERT INTO `pay_mall_list` VALUES ('146', '89', 'smart', '10000036', '村长', '10000036', '村长', '1', '200', '200', '200', '200', '0', '1516603311', '1524379311', '4', '1516603606');
INSERT INTO `pay_mall_list` VALUES ('147', '89', 'smart', '10000036', '村长', '10000036', '村长', '1', '200', '200', '200', '200', '0', '1516603311', '1526971311', '4', '1516603616');
INSERT INTO `pay_mall_list` VALUES ('148', '89', 'smart', '10000036', '村长', '10000036', '村长', '1', '200', '200', '200', '200', '0', '1516603311', '1529649711', '4', '1516603788');
INSERT INTO `pay_mall_list` VALUES ('149', '43', '白金VIP', '10000036', '村长', '10000036', '村长', '1', '999', '999', '999', '999', '0', '1516603860', '1519282260', '2', '1516603860');
INSERT INTO `pay_mall_list` VALUES ('150', '45', '白银守护', '10000038', '蓝精灵', '10000207', '晓东', '1', '1314', '1314', '1314', '1314', '10512', '1516603867', '1519282267', '1', '1516603867');
INSERT INTO `pay_mall_list` VALUES ('151', '89', 'smart', '10000036', '村长', '10000036', '村长', '1', '200', '200', '200', '200', '0', '1516603311', '1532241711', '4', '1516687803');
INSERT INTO `pay_mall_list` VALUES ('152', '45', '白银守护', '10000193', '小岛', '10000051', '喜羊羊', '1', '1314', '1314', '1314', '1314', '10512', '1516951320', '1519629720', '1', '1516951320');
INSERT INTO `pay_mall_list` VALUES ('153', '45', '白银守护', '10000193', '小岛', '10000051', '喜羊羊', '1', '1314', '1182', '1314', '1182', '10512', '1516951320', '1522048920', '1', '1516951357');
INSERT INTO `pay_mall_list` VALUES ('154', '44', '钻石VIP', '10000038', '蓝精灵', '10000038', '蓝精灵', '12', '9999', '9999', '119988', '119988', '0', '1521453992', '1552989992', '2', '1521453992');
INSERT INTO `pay_mall_list` VALUES ('155', '45', '白银守护', '10000034', '1五个零34', '10000033', '星空', '1', '1314', '1314', '1314', '1314', '10512', '1523670530', '1526262530', '1', '1523670530');
INSERT INTO `pay_mall_list` VALUES ('156', '45', '白银守护', '10000193', '小岛', '10000034', '1五个零34', '1', '1314', '1314', '1314', '1314', '10512', '1523671783', '1526263783', '1', '1523671783');
INSERT INTO `pay_mall_list` VALUES ('157', '46', '黄金守护', '10000041', '大为', '10000216', '风', '1', '13140', '13140', '13140', '13140', '105120', '1523672555', '1526264555', '1', '1523672556');
INSERT INTO `pay_mall_list` VALUES ('158', '46', '黄金守护', '10000193', '小岛', '10000033', '星空', '3', '13140', '13140', '39420', '39420', '315360', '1523672571', '1531534971', '1', '1523672571');
INSERT INTO `pay_mall_list` VALUES ('159', '45', '白银守护', '10000034', '1五个零34', '10000041', '大为', '1', '1314', '1314', '1314', '1314', '10512', '1523689090', '1526281090', '1', '1523689090');
INSERT INTO `pay_mall_list` VALUES ('160', '45', '白银守护', '10000034', '1五个零34', '10000216', '风', '1', '1314', '1314', '1314', '1314', '10512', '1523691375', '1526283375', '1', '1523691375');
INSERT INTO `pay_mall_list` VALUES ('161', '45', '白银守护', '10000034', '1五个零34', '10000033', '星空', '1', '1314', '1182', '1314', '1182', '10512', '1523670530', '1528940930', '1', '1523695500');
INSERT INTO `pay_mall_list` VALUES ('162', '45', '白银守护', '10000038', '蓝精灵', '10000033', '星空', '3', '1314', '1314', '3942', '3942', '31536', '1523695533', '1531557933', '1', '1523695534');
INSERT INTO `pay_mall_list` VALUES ('163', '46', '黄金守护', '10000034', '1五个零34', '10000216', '风', '1', '13140', '13140', '13140', '13140', '105120', '1523696661', '1526288661', '1', '1523696661');
INSERT INTO `pay_mall_list` VALUES ('164', '45', '白银守护', '10000216', '风', '10000034', '1五个零34', '1', '1314', '1314', '1314', '1314', '10512', '1523697111', '1526289111', '1', '1523697111');
INSERT INTO `pay_mall_list` VALUES ('165', '45', '白银守护', '10000041', '大为', '10000034', '1五个零34', '1', '1314', '1182', '1314', '1182', '10512', '1512734525', '1578484925', '1', '1523697135');
INSERT INTO `pay_mall_list` VALUES ('166', '45', '白银守护', '10000041', '大为', '10000034', '1五个零34', '1', '1314', '1182', '1314', '1182', '10512', '1512734525', '1581163325', '1', '1523697141');
INSERT INTO `pay_mall_list` VALUES ('167', '46', '黄金守护', '10000216', '风', '10000034', '1五个零34', '1', '13140', '13140', '13140', '13140', '105120', '1523697167', '1526289167', '1', '1523697168');
INSERT INTO `pay_mall_list` VALUES ('168', '45', '白银守护', '10000041', '大为', '10000038', '蓝精灵', '3', '1314', '1314', '3942', '3942', '31536', '1523698396', '1531560796', '1', '1523698396');
INSERT INTO `pay_mall_list` VALUES ('169', '46', '黄金守护', '10000041', '大为', '10000038', '蓝精灵', '1', '13140', '13140', '13140', '13140', '105120', '1523698458', '1526290458', '1', '1523698458');
INSERT INTO `pay_mall_list` VALUES ('170', '43', '白金VIP', '10000034', '1五个零34', '10000034', '1五个零34', '1', '999', '999', '999', '999', '0', '1523951155', '1526543155', '2', '1523951155');
INSERT INTO `pay_mall_list` VALUES ('171', '90', '莲花GTE', '10000038', '蓝精灵', '10000038', '蓝精灵', '1', '1880', '1880', '1880', '1880', '0', '1523952975', '1526544975', '4', '1523952975');
INSERT INTO `pay_mall_list` VALUES ('172', '88', 'mini', '10000038', '蓝精灵', '10000038', '蓝精灵', '1', '660', '660', '660', '660', '0', '1523952986', '1526544986', '4', '1523952986');
INSERT INTO `pay_mall_list` VALUES ('173', '90', '莲花GTE', '10000038', '蓝精灵', '10000038', '蓝精灵', '1', '1880', '0', '1880', '0', '0', '1523952975', '1529223375', '4', '1524044788');
INSERT INTO `pay_mall_list` VALUES ('174', '90', '莲花GTE', '10000038', '蓝精灵', '10000038', '蓝精灵', '1', '1880', '0', '1880', '0', '0', '1523952975', '1531815375', '4', '1524044889');
INSERT INTO `pay_mall_list` VALUES ('175', '90', '莲花GTE', '10000038', '蓝精灵', '10000038', '蓝精灵', '1', '1880', '0', '1880', '0', '0', '1523952975', '1534493775', '4', '1524044906');
INSERT INTO `pay_mall_list` VALUES ('176', '90', '莲花GTE', '10000038', '蓝精灵', '10000038', '蓝精灵', '1', '1880', '0', '1880', '0', '0', '1523952975', '1537172175', '4', '1524044961');
INSERT INTO `pay_mall_list` VALUES ('177', '90', '莲花GTE', '10000038', '蓝精灵', '10000038', '蓝精灵', '1', '1880', '0', '1880', '0', '0', '1523952975', '1539764175', '4', '1524045053');
INSERT INTO `pay_mall_list` VALUES ('178', '90', '莲花GTE', '10000038', '蓝精灵', '10000038', '蓝精灵', '1', '1880', '0', '1880', '0', '0', '1523952975', '1542442575', '4', '1524045058');
INSERT INTO `pay_mall_list` VALUES ('179', '90', '莲花GTE', '10000038', '蓝精灵', '10000038', '蓝精灵', '1', '1880', '0', '1880', '0', '0', '1523952975', '1545034575', '4', '1524045064');
INSERT INTO `pay_mall_list` VALUES ('180', '90', '莲花GTE', '10000038', '蓝精灵', '10000038', '蓝精灵', '1', '1880', '0', '1880', '0', '0', '1523952975', '1547712975', '4', '1524045081');
INSERT INTO `pay_mall_list` VALUES ('181', '90', '莲花GTE', '10000038', '蓝精灵', '10000038', '蓝精灵', '1', '1880', '0', '1880', '0', '0', '1523952975', '1550391375', '4', '1524045103');
INSERT INTO `pay_mall_list` VALUES ('182', '90', '莲花GTE', '10000038', '蓝精灵', '10000038', '蓝精灵', '1', '1880', '0', '1880', '0', '0', '1523952975', '1552810575', '4', '1524045116');
INSERT INTO `pay_mall_list` VALUES ('183', '44', '钻石VIP', '10000038', '蓝精灵', '10000038', '蓝精灵', '1', '500000', '0', '500000', '0', '0', '1521453992', '1555668392', '2', '1524045361');
INSERT INTO `pay_mall_list` VALUES ('184', '44', '钻石VIP', '10000038', '蓝精灵', '10000038', '蓝精灵', '1', '500000', '0', '500000', '0', '0', '1521453992', '1558260392', '2', '1524045523');
INSERT INTO `pay_mall_list` VALUES ('185', '90', '莲花GTE', '10000038', '蓝精灵', '10000038', '蓝精灵', '1', '1880', '0', '1880', '0', '0', '1523952975', '1555488975', '4', '1524101644');
INSERT INTO `pay_mall_list` VALUES ('186', '90', '莲花GTE', '10000033', '星空', '10000033', '星空', '1', '1880', '0', '1880', '0', '0', '1524106311', '1526698311', '4', '1524106311');
INSERT INTO `pay_mall_list` VALUES ('187', '90', '莲花GTE', '10000033', '星空', '10000033', '星空', '1', '1880', '0', '1880', '0', '0', '1524106311', '1529376711', '4', '1524106321');
INSERT INTO `pay_mall_list` VALUES ('188', '90', '莲花GTE', '10000033', '星空', '10000033', '星空', '1', '1880', '0', '1880', '0', '0', '1524106311', '1531968711', '4', '1524106361');
INSERT INTO `pay_mall_list` VALUES ('189', '90', '莲花GTE', '10000033', '星空', '10000033', '星空', '1', '1880', '0', '1880', '0', '0', '1524106311', '1534647111', '4', '1524106698');
INSERT INTO `pay_mall_list` VALUES ('190', '44', '钻石VIP', '10000033', '星空', '10000033', '星空', '1', '500000', '0', '500000', '0', '0', '1524106716', '1526698716', '2', '1524106716');
INSERT INTO `pay_mall_list` VALUES ('191', '90', '莲花GTE', '10000033', '星空', '10000033', '星空', '1', '1880', '0', '1880', '0', '0', '1524106311', '1537325511', '4', '1524106716');
INSERT INTO `pay_mall_list` VALUES ('192', '90', '莲花GTE', '10000033', '星空', '10000033', '星空', '1', '1880', '0', '1880', '0', '0', '1524106311', '1539917511', '4', '1524106890');
INSERT INTO `pay_mall_list` VALUES ('193', '90', '莲花GTE', '10000033', '星空', '10000033', '星空', '1', '1880', '0', '1880', '0', '0', '1524106311', '1542595911', '4', '1524106921');
INSERT INTO `pay_mall_list` VALUES ('194', '90', '莲花GTE', '10000033', '星空', '10000033', '星空', '1', '1880', '0', '1880', '0', '0', '1524106311', '1545187911', '4', '1524106928');
INSERT INTO `pay_mall_list` VALUES ('195', '44', '钻石VIP', '10000033', '星空', '10000033', '星空', '1', '500000', '0', '500000', '0', '0', '1524106716', '1529377116', '2', '1524106937');
INSERT INTO `pay_mall_list` VALUES ('196', '90', '莲花GTE', '10000033', '星空', '10000033', '星空', '1', '1880', '0', '1880', '0', '0', '1524106311', '1547866311', '4', '1524106937');
INSERT INTO `pay_mall_list` VALUES ('197', '90', '莲花GTE', '10000033', '星空', '10000033', '星空', '1', '1880', '0', '1880', '0', '0', '1524106311', '1550544711', '4', '1524106945');
INSERT INTO `pay_mall_list` VALUES ('198', '44', '钻石VIP', '10000033', '星空', '10000033', '星空', '1', '500000', '0', '500000', '0', '0', '1524106716', '1531969116', '2', '1524106963');
INSERT INTO `pay_mall_list` VALUES ('199', '90', '莲花GTE', '10000033', '星空', '10000033', '星空', '1', '1880', '0', '1880', '0', '0', '1524106311', '1552963911', '4', '1524106963');
INSERT INTO `pay_mall_list` VALUES ('200', '90', '莲花GTE', '10000033', '星空', '10000033', '星空', '1', '1880', '0', '1880', '0', '0', '1524106311', '1555642311', '4', '1524107330');
INSERT INTO `pay_mall_list` VALUES ('201', '90', '莲花GTE', '10000036', '村长', '10000036', '村长', '1', '1880', '0', '1880', '0', '0', '1524107381', '1526699381', '4', '1524107381');
INSERT INTO `pay_mall_list` VALUES ('202', '45', '白银守护', '10000036', '村长', '10000019', '198****2222', '1', '150000', '150000', '150000', '150000', '150000', '1524116286', '1526708286', '1', '1524116286');
INSERT INTO `pay_mall_list` VALUES ('203', '44', '钻石VIP', '10000041', '大为', '10000217', '飞', '12', '500000', '500000', '6000000', '6000000', '0', '1524130011', '1555666011', '2', '1524130011');
INSERT INTO `pay_mall_list` VALUES ('204', '46', '黄金守护', '10000036', '村长', '10000216', '风', '1', '300000', '300000', '300000', '300000', '300000', '1524473188', '1527065188', '1', '1524473188');
INSERT INTO `pay_mall_list` VALUES ('205', '90', '莲花GTE', '10000213', '略略略', '10000213', '略略略', '1', '1880', '0', '1880', '0', '0', '1524647753', '1527239753', '4', '1524647753');
INSERT INTO `pay_mall_list` VALUES ('206', '90', '莲花GTE', '10000213', '略略略', '10000213', '略略略', '1', '1880', '0', '1880', '0', '0', '1524647753', '1529918153', '4', '1524647769');
INSERT INTO `pay_mall_list` VALUES ('207', '43', '白金VIP', '10000034', '1五个零34', '10000034', '1五个零34', '1', '350000', '350000', '350000', '350000', '0', '1526630594', '1529308994', '2', '1526630594');
INSERT INTO `pay_mall_list` VALUES ('208', '43', '白金VIP', '10000220', '测试', '10000220', '测试', '1', '350000', '350000', '350000', '350000', '0', '1526632125', '1529310525', '2', '1526632125');
INSERT INTO `pay_mall_list` VALUES ('209', '45', '白银守护', '10000220', '测试', '10000041', '大为', '1', '150000', '150000', '150000', '150000', '150000', '1526632155', '1529310555', '1', '1526632155');
INSERT INTO `pay_mall_list` VALUES ('210', '90', '莲花GTE', '10000220', '测试', '10000220', '测试', '1', '1880', '1880', '1880', '1880', '0', '1526632162', '1529310562', '4', '1526632162');
INSERT INTO `pay_mall_list` VALUES ('211', '88', 'mini', '10000220', '测试', '10000041', '大为', '1', '660', '660', '660', '660', '0', '1526632174', '1529310574', '4', '1526632174');
INSERT INTO `pay_mall_list` VALUES ('212', '44', '钻石VIP', '10000220', '测试', '10000220', '测试', '1', '500000', '500000', '500000', '500000', '0', '1526702469', '1529380869', '2', '1526702469');
INSERT INTO `pay_mall_list` VALUES ('213', '91', '圣诞雪橇', '10000220', '测试', '10000220', '测试', '3', '300', '300', '900', '900', '0', '1526702479', '1534651279', '4', '1526702479');
INSERT INTO `pay_mall_list` VALUES ('214', '86', '奥迪TT', '10000220', '测试', '10000035', '(null)', '1', '880', '880', '880', '880', '0', '1526702503', '1529380903', '4', '1526702503');
INSERT INTO `pay_mall_list` VALUES ('215', '46', '黄金守护', '10000220', '测试', '10000034', '1五个零34', '1', '300000', '300000', '300000', '300000', '300000', '1526702528', '1529380928', '1', '1526702528');
INSERT INTO `pay_mall_list` VALUES ('216', '44', '钻石VIP', '10000034', '1五个零34', '10000033', '星空', '1', '500000', '500000', '500000', '500000', '0', '1524106716', '1534647516', '2', '1526864506');
INSERT INTO `pay_mall_list` VALUES ('217', '45', '白银守护', '10000034', '1五个零34', '10000033', '星空', '1', '150000', '135000', '150000', '135000', '150000', '1523670530', '1531532930', '1', '1526864574');
INSERT INTO `pay_mall_list` VALUES ('218', '87', '兰博基尼', '10000034', '1五个零34', '10000033', '星空', '1', '2980', '2980', '2980', '2980', '0', '1526864661', '1529543061', '4', '1526864661');
INSERT INTO `pay_mall_list` VALUES ('219', '90', '莲花GTE', '10000034', '10000034', '10000034', '10000034', '1', '1880', '0', '1880', '0', '0', '1527055310', '1529733710', '4', '1527055310');

-- ----------------------------
-- Table structure for `pay_order`
-- ----------------------------
DROP TABLE IF EXISTS `pay_order`;
CREATE TABLE `pay_order` (
  `order_id` char(30) NOT NULL COMMENT '充值号',
  `srcuid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '充值操作人uid',
  `dstuid` int(10) NOT NULL DEFAULT '0' COMMENT '充值目的uid',
  `amount` double(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '充值金额',
  `zhutou` int(11) NOT NULL DEFAULT '0' COMMENT '对应金币数',
  `creatAt` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '充值下单时间',
  `paytime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '发币时间',
  `os` int(2) NOT NULL COMMENT '充值类型',
  `paytype` char(30) NOT NULL DEFAULT '' COMMENT '=alipay =weixin =apple',
  `status` int(1) unsigned NOT NULL DEFAULT '0' COMMENT '支付状态 =0生成支付单 =1以到支付平台等待支付 =2已支付 =3取消',
  `inpour_no` char(100) DEFAULT NULL,
  `details` varchar(200) NOT NULL DEFAULT '' COMMENT '充值描述',
  `userSource` varchar(100) NOT NULL DEFAULT '' COMMENT '用户来源',
  `channel` varchar(100) DEFAULT '' COMMENT '渠道',
  `registtime` int(10) NOT NULL COMMENT '用户注册时间',
  `data_type` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '数据类型 1:真实数据;2:虚拟数据',
  `flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '删除标识 0:已删除;1:正常',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `create_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建人ID',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改人时间',
  `update_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '修改人ID',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `rechargesn` (`order_id`) USING BTREE,
  KEY `uid` (`srcuid`),
  KEY `index_userSource` (`userSource`),
  KEY `paytime` (`paytime`) USING BTREE,
  KEY `registtime` (`registtime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='充值记录';

-- ----------------------------
-- Records of pay_order
-- ----------------------------
INSERT INTO `pay_order` VALUES ('1180129092549726', '10000033', '10000033', '6.00', '0', '1517189149', '0', '1', 'alipay', '0', '', '充值600金币_6_alipay', 'test', 'test', '1507601417', '2', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180129093257035', '10000089', '10000089', '6.00', '0', '1517189577', '0', '1', 'alipay', '0', '', '充值600金币_6_alipay', 'official', 'test', '1509500159', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180129100456610', '10000033', '10000033', '6.00', '0', '1517191496', '0', '1', 'alipay', '0', '', '充值600金币_6_alipay', 'test', 'test', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180129103210102', '10000033', '10000033', '6.00', '0', '1517193130', '0', '1', 'alipay', '0', '', '充值600金币_6_alipay', 'test', 'test', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180129105542166', '10000033', '10000033', '6.00', '0', '1517194542', '0', '1', 'alipay', '0', '', '充值600金币_6_alipay', 'test', 'test', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180129111326579', '10000033', '10000033', '30.00', '0', '1517195606', '0', '1', 'alipay', '0', '', '充值3000金币_30_alipay', 'test', 'test', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180129112948083', '10000033', '10000033', '30.00', '0', '1517196588', '0', '1', 'alipay', '0', '', '充值3000金币_30_alipay', 'test', 'test', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180129114659294', '10000033', '10000033', '6.00', '600', '1517197619', '1517197627', '1', 'alipay', '2', '2018012921001004210200233477', '充值600金币_6_alipay', 'test', 'test', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180129114729584', '10000033', '10000033', '30.00', '3000', '1517197649', '1517197672', '1', 'alipay', '2', '2018012921001004210200233317', '充值3000金币_30_alipay', 'test', 'test', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180129142905714', '10000034', '10000034', '6.00', '600', '1517207345', '1517207381', '1', 'alipay', '2', '2018012921001004210200233478', '充值600金币_6_alipay', 'test', 'official', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180129160752578', '10000034', '10000034', '6.00', '600', '1517213272', '1517213287', '1', 'alipay', '2', '2018012921001004210200233482', '充值600金币_6_alipay', 'test', 'official', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180129160817347', '10000034', '10000034', '6.00', '600', '1517213297', '1517213305', '1', 'alipay', '2', '2018012921001004210200233483', '充值600金币_6_alipay', 'test', 'official', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180129161202144', '10000034', '10000034', '98.00', '9800', '1517213522', '1517213528', '1', 'alipay', '2', '2018012921001004210200233318', '充值9800金币_98_alipay', 'test', 'official', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180129161225960', '10000034', '10000034', '6.00', '600', '1517213545', '1517213552', '1', 'alipay', '2', '2018012921001004210200233484', '充值600金币_6_alipay', 'test', 'official', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180129161451234', '10000034', '10000034', '98.00', '0', '1517213691', '0', '1', 'alipay', '0', '', '充值9800金币_98_alipay', 'test', 'official', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180129161517746', '10000034', '10000034', '30.00', '0', '1517213717', '0', '1', 'alipay', '0', '', '充值3000金币_30_alipay', 'test', 'official', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180129161528120', '10000034', '10000034', '30.00', '0', '1517213728', '0', '1', 'alipay', '0', '', '充值3000金币_30_alipay', 'test', 'official', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180129163132770', '10000034', '10000034', '6.00', '600', '1517214692', '1517214711', '1', 'alipay', '2', '2018012921001004210200233321', '充值600金币_6_alipay', 'test', 'official', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180130154830863', '10000089', '10000089', '6.00', '600', '1517298510', '1517298519', '1', 'alipay', '2', '2018013021001004210200233803', '充值600金币_6_alipay', 'official', 'test', '1509500159', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222092336150', '10000034', '10000034', '1000.00', '100000', '1519262616', '1519262628', '1', 'alipay', '2', '2018022221001004210200237392', '充值100000金币_1000_alipay', 'test', 'test', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222112743647', '10000033', '10000033', '6.00', '0', '1519270063', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'test', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222113714083', '10000033', '10000033', '6.00', '0', '1519270634', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'test', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222114005283', '10000033', '10000033', '6.00', '0', '1519270805', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'test', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222114915754', '10000033', '10000033', '6.00', '0', '1519271355', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222115811784', '10000033', '10000033', '3.00', '0', '1519271891', '0', '1', 'reapal_weixin', '0', '', '充值300金币_3_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222120441668', '10000033', '10000033', '1.00', '0', '1519272281', '0', '1', 'reapal_weixin', '0', '', '充值100金币_1_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222134124635', '10000033', '10000033', '1.00', '100', '1519278084', '1519278085', '1', 'reapal_weixin', '3', '', '充值100金币_1_weixin', 'test', 'official', '1507601417', '2', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222134435805', '10000033', '10000033', '0.00', '0', '1519278275', '1519278277', '1', 'reapal_weixin', '3', '', '充值600金币_6_weixin', 'test', 'official', '1507601417', '2', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222134453230', '10000033', '10000033', '0.00', '0', '1519278293', '1519278293', '1', 'reapal_weixin', '3', '', '充值100金币_1_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222134519534', '10000033', '10000033', '0.00', '0', '1519278319', '1519278319', '1', 'reapal_weixin', '3', '', '充值600金币_6_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222134709000', '10000033', '10000033', '0.00', '0', '1519278429', '1519278429', '1', 'reapal_weixin', '2', '', '充值600金币_6_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222135347358', '10000033', '10000033', '0.00', '0', '1519278827', '1519278827', '1', 'reapal_weixin', '2', '', '充值600金币_6_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222135443922', '10000033', '10000033', '0.00', '0', '1519278883', '1519278884', '1', 'reapal_weixin', '3', '', '充值600金币_6_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222140952474', '10000033', '10000033', '0.00', '0', '1519279792', '1519279793', '1', 'reapal_weixin', '2', '', '充值600金币_6_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222141120131', '10000033', '10000033', '0.00', '0', '1519279880', '1519279880', '1', 'reapal_weixin', '2', '', '充值600金币_6_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222141136687', '10000089', '10000089', '0.00', '0', '1519279896', '1519279897', '1', 'reapal_weixin', '3', '', '充值600金币_6_weixin', 'official', 'official', '1509500159', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222141202488', '10000089', '10000089', '6.00', '0', '1519279922', '0', '1', 'alipay', '0', '', '充值600金币_6_alipay', 'official', 'official', '1509500159', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222141233704', '10000033', '10000033', '0.00', '0', '1519279953', '1519279953', '1', 'reapal_weixin', '3', '', '充值600金币_6_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222145151763', '10000033', '10000033', '0.01', '1', '1519282311', '1519282312', '1', 'reapal_weixin', '2', '', '充值600金币_6_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222145731421', '10000033', '10000033', '0.01', '1', '1519282651', '1519282651', '1', 'reapal_weixin', '3', '', '充值600金币_6_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222150525097', '10000033', '10000033', '6.00', '600', '1519283125', '1519283177', '1', 'alipay', '2', '2018022221001004210200237735', '充值600金币_6_alipay', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222150637683', '10000033', '10000033', '0.01', '1', '1519283197', '1519283198', '1', 'reapal_weixin', '2', '', '充值600金币_6_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180222174645224', '10000033', '10000033', '0.01', '1', '1519292805', '1519292805', '1', 'reapal_weixin', '2', '', '充值600金币_6_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180223101002153', '10000033', '10000033', '0.01', '1', '1519351802', '1519351802', '1', 'reapal_weixin', '2', '', '充值600金币_6_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180223101626020', '10000033', '10000033', '0.01', '1', '1519352186', '1519352186', '1', 'reapal_weixin', '2', '', '充值600金币_6_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180223101644879', '10000033', '10000033', '0.01', '1', '1519352204', '1519352205', '1', 'reapal_weixin', '2', '', '充值600金币_6_weixin', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180223113058246', '10000033', '10000033', '0.01', '0', '1519356658', '0', '1', 'alipay', '0', '', '充值600金币_6_alipay', 'test', 'official', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180223113439172', '10000033', '10000033', '0.00', '0', '1519356879', '1519356887', '1', 'alipay', '2', '2018022321001004500212762350', '充值600金币_6_alipay', 'test', 'test', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180223113522991', '10000033', '10000033', '0.00', '0', '1519356922', '1519356928', '1', 'alipay', '2', '2018022321001004500210584752', '充值600金币_6_alipay', 'test', 'test', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180223114246212', '10000033', '10000033', '0.01', '1', '1519357366', '1519357373', '1', 'alipay', '2', '2018022321001004500210874090', '充值600金币_6_alipay', 'test', 'test', '1507601417', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180504133041312', '10000041', '10000041', '6.00', '0', '1525411841', '0', '1', 'alipay', '0', '', '充值600金币_6_alipay', 'test', 'official', '1507777069', '2', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180504134301492', '10000041', '10000041', '0.01', '0', '1525412581', '0', '1', 'alipay', '0', '', '充值600金币_6_alipay', 'test', 'official', '1507777069', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180504134301522', '10000041', '10000041', '6.00', '0', '1525412581', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'official', '1507777069', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180504134307085', '10000041', '10000041', '98.00', '0', '1525412587', '0', '1', 'reapal_weixin', '0', '', '充值9800金币_98_weixin', 'test', 'official', '1507777069', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180504134309715', '10000041', '10000041', '6.00', '0', '1525412589', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'official', '1507777069', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180504135331705', '10000041', '10000041', '6.00', '0', '1525413211', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'official', '1507777069', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('1180504135351104', '10000041', '10000041', '0.01', '0', '1525413231', '0', '1', 'alipay', '0', '', '充值9800金币_98_alipay', 'test', 'official', '1507777069', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180129092204027', '10000036', '10000036', '6.00', '0', '1517188924', '0', '2', 'alipay', '0', '', '充值600个金币_6_alipay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180129092219927', '10000036', '10000036', '6.00', '0', '1517188939', '0', '2', 'alipay', '0', '', '充值600个金币_6_alipay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222112235893', '10000036', '10000036', '6.00', '0', '1519269755', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222112842970', '10000036', '10000036', '6.00', '0', '1519270122', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222114906329', '10000036', '10000036', '6.00', '0', '1519271346', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222141248041', '10000036', '10000036', '6.00', '0', '1519279968', '0', '2', 'alipay', '0', '', '充值600个金币_6_alipay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222145455766', '10000036', '10000036', '0.01', '0', '1519282495', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222150206609', '10000036', '10000036', '0.01', '0', '1519282926', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222150430601', '10000036', '10000036', '0.01', '0', '1519283070', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222150521203', '10000036', '10000036', '0.01', '0', '1519283121', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222150654358', '10000036', '10000036', '0.01', '0', '1519283214', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222150845381', '10000036', '10000036', '0.01', '0', '1519283325', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222160920726', '10000036', '10000036', '0.01', '0', '1519286960', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222161759859', '10000036', '10000036', '0.01', '1', '1519287479', '1519287487', '2', 'reapal_weixin', '2', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222161907485', '10000036', '10000036', '0.01', '1', '1519287547', '1519287547', '2', 'reapal_weixin', '2', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222171751782', '10000036', '10000036', '0.01', '1', '1519291071', '1519291072', '2', 'reapal_weixin', '2', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222171915601', '10000036', '10000036', '0.01', '1', '1519291155', '1519291155', '2', 'reapal_weixin', '2', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222172208847', '10000036', '10000036', '0.01', '1', '1519291328', '1519291333', '2', 'reapal_weixin', '2', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222172326061', '10000036', '10000036', '0.01', '1', '1519291406', '1519291414', '2', 'reapal_weixin', '2', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222172616068', '10000036', '10000036', '0.01', '1', '1519291576', '1519291592', '2', 'reapal_weixin', '3', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222172649156', '10000036', '10000036', '0.01', '1', '1519291609', '1519291609', '2', 'reapal_weixin', '3', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222173008409', '10000036', '10000036', '0.01', '1', '1519291808', '1519291808', '2', 'reapal_weixin', '2', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222175203815', '10000036', '10000036', '0.01', '1', '1519293123', '1519293124', '2', 'reapal_weixin', '2', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222175231744', '10000036', '10000036', '0.01', '1', '1519293151', '1519293152', '2', 'reapal_weixin', '2', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222175305115', '10000036', '10000036', '0.01', '1', '1519293185', '1519293185', '2', 'reapal_weixin', '2', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180222175441672', '10000036', '10000036', '0.01', '1', '1519293281', '1519293281', '2', 'reapal_weixin', '2', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223090849891', '10000036', '10000036', '0.01', '1', '1519348129', '1519348130', '2', 'reapal_weixin', '2', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223091917027', '10000036', '10000036', '0.01', '1', '1519348757', '1519348757', '2', 'reapal_weixin', '2', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223092753199', '10000036', '10000036', '0.01', '1', '1519349273', '1519349273', '2', 'reapal_weixin', '2', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223093056099', '10000036', '10000036', '0.01', '1', '1519349456', '1519349456', '2', 'reapal_weixin', '2', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223093619055', '10000036', '10000036', '0.01', '1', '1519349779', '1519349780', '2', 'reapal_weixin', '2', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223093641411', '10000036', '10000036', '6.00', '0', '1519349801', '0', '2', 'alipay', '0', '', '充值600个金币_6_alipay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223112510701', '10000034', '10000034', '6.00', '0', '1519356310', '0', '2', 'alipay', '0', '', '充值600个金币_6_alipay', 'test', '', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223112832909', '10000034', '10000034', '0.00', '0', '1519356512', '1519356522', '2', 'alipay', '2', '2018022321001004350590707466', '充值600个金币_6_alipay', 'test', '', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223114207317', '10000034', '10000034', '0.01', '1', '1519357327', '1519357333', '2', 'alipay', '2', '2018022321001004350590555668', '充值8800个金币_88_alipay', 'test', '', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223114303924', '10000034', '10000034', '0.01', '1', '1519357383', '1519357389', '2', 'alipay', '2', '2018022321001004350590614864', '充值8800个金币_88_alipay', 'test', '', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223115741788', '10000051', '10000051', '0.01', '1', '1519358261', '1519358272', '2', 'alipay', '2', '2018022321001004050563059101', '充值8800个金币_88_alipay', 'appstore', '', '1507790709', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223125822876', '10000051', '10000051', '0.01', '1', '1519361902', '1519361916', '2', 'alipay', '2', '2018022321001004050565172365', '充值8800个金币_88_alipay', 'appstore', '', '1507790709', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223130919484', '10000051', '10000051', '0.01', '1', '1519362559', '1519362572', '2', 'alipay', '2', '2018022321001004050564131851', '充值8800个金币_88_alipay', 'appstore', '', '1507790709', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223131221324', '10000051', '10000051', '0.01', '1', '1519362741', '1519362755', '2', 'alipay', '2', '2018022321001004050565103868', '充值8800个金币_88_alipay', 'appstore', '', '1507790709', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223131246585', '10000051', '10000051', '0.01', '1', '1519362766', '1519362785', '2', 'alipay', '2', '2018022321001004050563080462', '充值8800个金币_88_alipay', 'appstore', '', '1507790709', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223182320087', '10000034', '10000034', '0.01', '0', '1519381400', '0', '2', 'alipay', '0', '', '充值8800个金币_88_alipay', 'test', '', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223182344779', '10000034', '10000034', '0.01', '1', '1519381424', '1519381424', '2', 'reapal_weixin', '3', '', '充值8800个金币_88_weixin', 'test', '', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180223182509684', '10000034', '10000034', '0.01', '0', '1519381509', '0', '2', 'alipay', '0', '', '充值8800个金币_88_alipay', 'test', '', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224141303768', '10000036', '10000036', '6.00', '0', '1519452783', '0', '2', 'apple_innerpay', '0', '1000000378280383', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224141323398', '10000036', '10000036', '6.00', '0', '1519452803', '0', '2', 'apple_innerpay', '0', '1000000378452160', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224141339789', '10000036', '10000036', '6.00', '0', '1519452819', '0', '2', 'apple_innerpay', '0', '1000000378452178', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224144652194', '10000036', '10000036', '6.00', '600', '1519454812', '1519454812', '2', 'apple_innerpay', '2', '1000000378453183', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224144828583', '10000036', '10000036', '6.00', '600', '1519454908', '1519454908', '2', 'apple_innerpay', '2', '1000000378455654', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224144923742', '10000036', '10000036', '6.00', '600', '1519454963', '1519454963', '2', 'apple_innerpay', '2', '1000000378455718', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224145609064', '10000036', '10000036', '6.00', '600', '1519455369', '1519455369', '2', 'apple_innerpay', '2', '1000000378456344', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224150106250', '10000036', '10000036', '6.00', '600', '1519455666', '1519455666', '2', 'apple_innerpay', '2', '1000000378457068', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224150124668', '10000036', '10000036', '6.00', '600', '1519455684', '1519455684', '2', 'apple_innerpay', '2', '1000000378457372', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224150131273', '10000036', '10000036', '6.00', '600', '1519455691', '1519455691', '2', 'apple_innerpay', '2', '1000000378457378', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224150137861', '10000036', '10000036', '12.00', '1200', '1519455697', '1519455697', '2', 'apple_innerpay', '2', '1000000378457382', '充值1200个金币_12_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224150206893', '10000036', '10000036', '12.00', '1200', '1519455726', '1519455726', '2', 'apple_innerpay', '2', '1000000378457402', '充值1200个金币_12_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224150446130', '10000036', '10000036', '6.00', '600', '1519455886', '1519455886', '2', 'apple_innerpay', '2', '1000000378457872', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224151253299', '10000036', '10000036', '6.00', '600', '1519456373', '1519456373', '2', 'apple_innerpay', '2', '1000000378457872', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224151302737', '10000036', '10000036', '6.00', '600', '1519456382', '1519456382', '2', 'apple_innerpay', '2', '1000000378457872', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224151316036', '10000036', '10000036', '6.00', '600', '1519456396', '1519456396', '2', 'apple_innerpay', '2', '1000000378458617', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224151329443', '10000036', '10000036', '6.00', '600', '1519456409', '1519456409', '2', 'apple_innerpay', '2', '1000000378458640', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224151910182', '10000036', '10000036', '6.00', '600', '1519456750', '1519456750', '2', 'apple_innerpay', '2', '1000000378459744', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224154234565', '10000036', '10000036', '6.00', '600', '1519458154', '1519458154', '2', 'apple_innerpay', '2', '1000000378460014', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224154538865', '10000036', '10000036', '6.00', '600', '1519458338', '1519458338', '2', 'apple_innerpay', '2', '1000000378463017', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224154849456', '10000036', '10000036', '6.00', '600', '1519458529', '1519458529', '2', 'apple_innerpay', '2', '1000000378463256', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224154910838', '10000036', '10000036', '6.00', '600', '1519458550', '1519458550', '2', 'apple_innerpay', '2', '1000000378463307', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224154929190', '10000036', '10000036', '6.00', '600', '1519458569', '1519458569', '2', 'apple_innerpay', '2', '1000000378463315', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224154935519', '10000036', '10000036', '40.00', '4000', '1519458575', '1519458575', '2', 'apple_innerpay', '2', '1000000378463317', '充值4000个金币_40_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224154952646', '10000036', '10000036', '25.00', '2500', '1519458592', '1519458592', '2', 'apple_innerpay', '2', '1000000378463322', '充值2500个金币_25_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155012460', '10000036', '10000036', '40.00', '4000', '1519458612', '1519458612', '2', 'apple_innerpay', '2', '1000000378463327', '充值4000个金币_40_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155012472', '10000036', '10000036', '40.00', '4000', '1519458612', '1519458612', '2', 'apple_innerpay', '2', '1000000378463319', '充值4000个金币_40_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155020171', '10000036', '10000036', '25.00', '2500', '1519458620', '1519458620', '2', 'apple_innerpay', '2', '1000000378463333', '充值2500个金币_25_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155027634', '10000036', '10000036', '18.00', '1800', '1519458627', '1519458627', '2', 'apple_innerpay', '2', '1000000378463338', '充值1800个金币_18_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155035548', '10000036', '10000036', '6.00', '600', '1519458635', '1519458635', '2', 'apple_innerpay', '2', '1000000378463358', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155040755', '10000036', '10000036', '12.00', '1200', '1519458640', '1519458640', '2', 'apple_innerpay', '2', '1000000378463360', '充值1200个金币_12_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155048177', '10000036', '10000036', '25.00', '2500', '1519458648', '1519458648', '2', 'apple_innerpay', '2', '1000000378463389', '充值2500个金币_25_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155053676', '10000036', '10000036', '30.00', '3000', '1519458653', '1519458653', '2', 'apple_innerpay', '2', '1000000378463627', '充值3000个金币_30_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155059708', '10000036', '10000036', '40.00', '4000', '1519458659', '1519458659', '2', 'apple_innerpay', '2', '1000000378463630', '充值4000个金币_40_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155314753', '10000036', '10000036', '6.00', '600', '1519458794', '1519458794', '2', 'apple_innerpay', '2', '1000000378464245', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155321435', '10000036', '10000036', '12.00', '1200', '1519458801', '1519458801', '2', 'apple_innerpay', '2', '1000000378464250', '充值1200个金币_12_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155326631', '10000036', '10000036', '18.00', '1800', '1519458806', '1519458806', '2', 'apple_innerpay', '2', '1000000378464251', '充值1800个金币_18_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155332771', '10000036', '10000036', '25.00', '2500', '1519458812', '1519458812', '2', 'apple_innerpay', '2', '1000000378464255', '充值2500个金币_25_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155337935', '10000036', '10000036', '30.00', '3000', '1519458817', '1519458818', '2', 'apple_innerpay', '2', '1000000378464257', '充值3000个金币_30_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155343222', '10000036', '10000036', '40.00', '4000', '1519458823', '1519458823', '2', 'apple_innerpay', '2', '1000000378464258', '充值4000个金币_40_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155403816', '10000036', '10000036', '40.00', '4000', '1519458843', '1519458843', '2', 'apple_innerpay', '2', '1000000378464273', '充值4000个金币_40_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155409857', '10000036', '10000036', '30.00', '3000', '1519458849', '1519458849', '2', 'apple_innerpay', '2', '1000000378464274', '充值3000个金币_30_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155414563', '10000036', '10000036', '25.00', '2500', '1519458854', '1519458854', '2', 'apple_innerpay', '2', '1000000378464259', '充值2500个金币_25_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155415052', '10000036', '10000036', '25.00', '2500', '1519458855', '1519458855', '2', 'apple_innerpay', '2', '1000000378464276', '充值2500个金币_25_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155419978', '10000036', '10000036', '18.00', '1800', '1519458859', '1519458859', '2', 'apple_innerpay', '2', '1000000378464280', '充值1800个金币_18_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155425631', '10000036', '10000036', '12.00', '1200', '1519458865', '1519458865', '2', 'apple_innerpay', '2', '1000000378464282', '充值1200个金币_12_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155436477', '10000036', '10000036', '6.00', '600', '1519458876', '1519458876', '2', 'apple_innerpay', '2', '1000000378464393', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155441509', '10000036', '10000036', '12.00', '1200', '1519458881', '1519458881', '2', 'apple_innerpay', '2', '1000000378464396', '充值1200个金币_12_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155447140', '10000036', '10000036', '18.00', '1800', '1519458887', '1519458887', '2', 'apple_innerpay', '2', '1000000378464399', '充值1800个金币_18_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155452252', '10000036', '10000036', '30.00', '3000', '1519458892', '1519458892', '2', 'apple_innerpay', '2', '1000000378464402', '充值3000个金币_30_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155457348', '10000036', '10000036', '40.00', '4000', '1519458897', '1519458897', '2', 'apple_innerpay', '2', '1000000378464403', '充值4000个金币_40_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155503217', '10000036', '10000036', '30.00', '3000', '1519458903', '1519458903', '2', 'apple_innerpay', '2', '1000000378464407', '充值3000个金币_30_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180224155512939', '10000036', '10000036', '40.00', '4000', '1519458912', '1519458912', '2', 'apple_innerpay', '2', '1000000378464417', '充值4000个金币_40_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180226104011180', '10000034', '10000034', '0.01', '1', '1519612811', '1519612811', '2', 'reapal_weixin', '3', '', '充值8800个金币_88_weixin', 'test', '', '1507602362', '2', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180301163611234', '10000036', '10000036', '6.00', '600', '1519893371', '1519893371', '2', 'apple_innerpay', '2', '1000000379785549', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180301165327643', '10000036', '10000036', '6.00', '600', '1519894407', '1519894407', '2', 'apple_innerpay', '2', '1000000379791357', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180301165431316', '10000036', '10000036', '12.00', '1200', '1519894471', '1519894471', '2', 'apple_innerpay', '2', '1000000379791667', '充值1200个金币_12_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180301172827882', '10000036', '10000036', '6.00', '600', '1519896507', '1519896507', '2', 'apple_innerpay', '2', '1000000379802946', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180302154830766', '10000034', '10000034', '0.01', '1', '1519976910', '1519976911', '2', 'reapal_weixin', '3', '', '充值8800个金币_88_weixin', 'test', '', '1507602362', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180314142906051', '10000036', '10000036', '0.01', '0', '1521008946', '0', '2', 'reapal_weixin', '0', '', '充值8800个金币_88_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180320091241612', '10000092', '10000092', '0.01', '0', '1521508361', '0', '2', 'reapal_weixin', '0', '', '充值8800个金币_88_weixin', 'appstore', '', '1510710718', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180504135137250', '10000036', '10000036', '6.00', '600', '1525413097', '1525413097', '2', 'apple_innerpay', '2', '1000000395825347', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180504135149541', '10000036', '10000036', '6.00', '600', '1525413109', '1525413109', '2', 'apple_innerpay', '2', '1000000395825371', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180504135220279', '10000036', '10000036', '88.00', '0', '1525413140', '0', '2', 'reapal_weixin', '0', '', '充值8800个金币_88_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180504135353054', '10000036', '10000036', '88.00', '0', '1525413233', '0', '2', 'reapal_weixin', '0', '', '充值8800个金币_88_weixin', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180504135402256', '10000036', '10000036', '88.00', '0', '1525413242', '0', '2', 'alipay', '0', '', '充值8800个金币_88_alipay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('2180504151527063', '10000036', '10000036', '6.00', '600', '1525418127', '1525418127', '2', 'apple_innerpay', '2', '1000000395846571', '充值600个金币_6_appleinnerpay', 'appstore', '', '1507620575', '1', '1', '0', '0', '0', '0');
INSERT INTO `pay_order` VALUES ('320180517171801636', '10000036', '0', '12.00', '0', '1526462289', '1526462289', '3', '', '3', '1526548689413258', '充值金币1200个金币_12.0reapal_weixin', 'official', '', '1507620575', '2', '1', '1526548681636', '1', '0', '0');
INSERT INTO `pay_order` VALUES ('320180517172026412', '10000034', '0', '10000.00', '0', '1526548826', '1526548826', '3', '', '3', '1526548826422380', '充值金币1000000个金币_10000.0reapal_weixin', 'official', '', '1507602362', '2', '1', '1526548826412', '1', '0', '0');
INSERT INTO `pay_order` VALUES ('320180517172217902', '10000033', '0', '12.00', '0', '1526548937', '1526548937', '3', '', '3', '1526548937911080', '充值金币1200个金币_12.0reapal_weixin', 'official', '', '1507601417', '2', '1', '1526548937902', '1', '0', '0');
INSERT INTO `pay_order` VALUES ('320180517172332801', '10000034', '0', '12.00', '0', '1526549012', '1526549012', '3', '', '2', '1526549012810589', '充值金币1200个金币_12.0reapal_weixin', 'official', '', '1507602362', '2', '1', '1526549012801', '1', '0', '0');
INSERT INTO `pay_order` VALUES ('320180517172549395', '10000033', '10000033', '12.00', '1200', '1526549149', '1526549149', '3', 'reapal_weixin', '2', '1526549149404961', '充值金币1200个金币_12.0reapal_weixin', 'official', '', '1507601417', '2', '0', '1526549149395', '1', '1526550652417', '1');
INSERT INTO `pay_order` VALUES ('320180517172614331', '10000033', '10000033', '1111.00', '111100', '1526376374', '1526376374', '3', 'alipay', '2', '1526549174339199', '充值金币111100个金币_1111.0alipay', 'official', '', '1507601417', '2', '0', '1526549174331', '1', '1526550604220', '1');
INSERT INTO `pay_order` VALUES ('320180517175449584', '10000034', '10000034', '10000034.00', '1000003400', '1526550889', '1526550889', '3', 'reapal_weixin', '2', '1526550889594934', '充值金币1000003400个金币_1.0000034E7reapal_weixin', 'official', '', '1507602362', '2', '1', '1526550889584', '1', '0', '0');
INSERT INTO `pay_order` VALUES ('320180517175504844', '10000033', '10000033', '10000033.00', '1000003300', '1526550904', '1526550904', '3', 'reapal_weixin', '2', '1526550904854329', '充值金币1000003300个金币_1.0000033E7reapal_weixin', 'official', '', '1507601417', '2', '0', '1526550904844', '1', '1526550931796', '1');
INSERT INTO `pay_order` VALUES ('320180517175513691', '10000033', '10000033', '10000033.00', '1000003300', '1526464513', '1526464513', '3', 'alipay', '2', '1526550913698765', '充值金币1000003300个金币_1.0000033E7alipay', 'official', '', '1507601417', '2', '0', '1526550913691', '1', '1526553454878', '1');
INSERT INTO `pay_order` VALUES ('320180517175523429', '10000033', '10000033', '10000033.00', '1000003300', '1526378123', '1526378123', '3', 'apple_innerpay', '2', '1526550923440322', '充值金币1000003300个金币_1.0000033E7apple_innerpay', 'official', '', '1507601417', '2', '0', '1526550923429', '1', '1526607636833', '1');
INSERT INTO `pay_order` VALUES ('320180518091836456', '10000041', '10000041', '12222.00', '1222200', '1526606316', '1526606316', '3', 'alipay', '2', '1526606316467876', '充值金币1222200个金币_12222.0alipay', 'official', '', '1507777069', '2', '1', '1526606316456', '1', '0', '0');
INSERT INTO `pay_order` VALUES ('320180518091859539', '10000051', '10000051', '231.00', '23100', '1526606339', '1526606339', '3', 'alipay', '2', '1526606339548885', '充值金币23100个金币_231.0alipay', 'official', '', '1507790709', '2', '1', '1526606339539', '1', '0', '0');
INSERT INTO `pay_order` VALUES ('320180518134532337', '10000051', '10000051', '100.00', '10000', '1526622332', '1526622332', '3', 'reapal_weixin', '2', '1526622332347554', '充值金币10000个金币_100.0reapal_weixin', 'official', '', '1507790709', '2', '0', '1526622332337', '1', '1526622338986', '1');
INSERT INTO `pay_order` VALUES ('320180518134609782', '10000034', '10000034', '122.00', '12200', '1526363169', '1526363169', '3', 'alipay', '2', '1526622369791589', '充值金币12200个金币_122.0alipay', 'official', '', '1507602362', '2', '1', '1526622369782', '1', '0', '0');
INSERT INTO `pay_order` VALUES ('320180518154308024', '10000041', '10000041', '12123.00', '1212300', '1526629388', '1526629388', '3', 'reapal_weixin', '2', '1526629388035727', '充值金币1212300个金币_12123.0reapal_weixin', 'official', '', '1507777069', '2', '1', '1526629388024', '1', '0', '0');
INSERT INTO `pay_order` VALUES ('320180518174422555', '10000034', '10000034', '12.00', '1200', '1526463862', '1526463862', '3', 'reapal_weixin', '2', '1526636662564905', '充值金币1200个金币_12.0reapal_weixin', 'official', '', '1507602362', '2', '1', '1526636662555', '1', '0', '0');
INSERT INTO `pay_order` VALUES ('320180518175505882', '10000034', '10000034', '12.00', '1200', '1526550905', '1526550905', '3', 'alipay', '2', '1526637305892782', '充值金币1200个金币_12.0alipay', 'official', '', '1507602362', '2', '1', '1526637305882', '1', '0', '0');
INSERT INTO `pay_order` VALUES ('320180518175704501', '10000034', '10000034', '1.00', '100', '1526637431', '1526637431', '3', 'alipay', '2', '1526637424509159', '充值金币100个金币_1.0alipay', 'official', '', '1507602362', '2', '0', '1526637424501', '1', '1526693766613', '1');
INSERT INTO `pay_order` VALUES ('320180518175945683', '10000034', '10000034', '123.00', '12300', '1526637592', '1526637592', '3', 'alipay', '2', '1526637585691583', '充值金币12300个金币_123.0alipay', 'official', '', '1507602362', '2', '0', '1526637585683', '1', '1526640933871', '1');
INSERT INTO `pay_order` VALUES ('320180518180038542', '10000034', '10000034', '12.00', '1200', '1526637638', '1526637638', '3', 'alipay', '2', '1526637638551971', '充值金币1200个金币_12.0alipay', 'official', '', '1507602362', '2', '0', '1526637638542', '1', '1526693576015', '1');
INSERT INTO `pay_order` VALUES ('320180518180824859', '10000034', '10000034', '123.00', '12300', '1526638104', '1526638104', '3', 'reapal_weixin', '2', '1526638104870807', '充值金币12300个金币_123.0reapal_weixin', 'official', '', '1507602362', '2', '0', '1526638104859', '1', '1526640946486', '1');
INSERT INTO `pay_order` VALUES ('320180518191101301', '10000041', '10000041', '10000041.00', '1000004100', '1526641861', '1526641861', '3', 'apple_innerpay', '2', '1526641861308877', '充值金币1000004100个金币_1.0000041E7apple_innerpay', 'official', '', '1507777069', '2', '0', '1526641861301', '1', '1526641878055', '1');
INSERT INTO `pay_order` VALUES ('320180519093407150', '10000034', '10000034', '12.00', '1200', '1526693649', '1526693649', '3', 'reapal_weixin', '2', '1526693649159501', '充值金币1200个金币_12.0reapal_weixin', 'official', '', '1507602362', '2', '0', '1526693647150', '1', '1526693759780', '1');
INSERT INTO `pay_order` VALUES ('320180519093435744', '10000034', '10000034', '23.00', '2300', '1526693677', '1526693677', '3', 'alipay', '2', '1526693677777370', '充值金币2300个金币_23.0alipay', 'official', '', '1507602362', '2', '0', '1526693675744', '1', '1526693752534', '1');
INSERT INTO `pay_order` VALUES ('320180519105008469', '10000220', '10000220', '12.00', '1200', '1526698208', '1526698208', '3', 'reapal_weixin', '2', '1526698208470335', '充值金币1200个金币_12.0reapal_weixin', 'official', '', '1526631531', '2', '1', '1526698208469', '1', '0', '0');
INSERT INTO `pay_order` VALUES ('320180519122543679', '10000220', '10000220', '1211.00', '121100', '1526703943', '1526703943', '3', 'alipay', '2', '1526703943681838', '充值金币121100个金币_1211.0alipay', 'official', '', '1526631531', '2', '1', '1526703943679', '1', '0', '0');
INSERT INTO `pay_order` VALUES ('320180520084103831', '10000220', '10000220', '23.00', '2300', '1526776863', '1526776863', '3', 'reapal_weixin', '2', '1526776863862801', '充值金币2300个金币_23.0reapal_weixin', 'official', '', '1526631531', '2', '1', '1526776863831', '1', '0', '0');
INSERT INTO `pay_order` VALUES ('320180524105526042', '10000041', '10000041', '1000.00', '100000', '1527130526', '1527130526', '3', 'unpay_weixin', '2', '1527130526051654', '充值金币100000个金币_1000.0unpay_weixin', 'official', '', '1507777069', '2', '1', '1527130526042', '1', '0', '0');

-- ----------------------------
-- Table structure for `pay_order_bak_20180222`
-- ----------------------------
DROP TABLE IF EXISTS `pay_order_bak_20180222`;
CREATE TABLE `pay_order_bak_20180222` (
  `order_id` char(30) NOT NULL COMMENT '充值号',
  `srcuid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '充值操作人uid',
  `dstuid` int(10) NOT NULL DEFAULT '0' COMMENT '充值目的uid',
  `amount` double(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '充值金额',
  `zhutou` int(11) NOT NULL DEFAULT '0' COMMENT '对应金币数',
  `creatAt` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '充值下单时间',
  `paytime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '发币时间',
  `os` int(2) NOT NULL COMMENT '充值类型',
  `paytype` char(30) NOT NULL DEFAULT '' COMMENT '=alipay =weixin =apple',
  `status` int(1) unsigned NOT NULL DEFAULT '0' COMMENT '支付状态 =0生成支付单 =1以到支付平台等待支付 =2已支付 =3取消',
  `inpour_no` char(100) DEFAULT NULL,
  `details` varchar(200) NOT NULL DEFAULT '' COMMENT '充值描述',
  `userSource` varchar(100) NOT NULL DEFAULT '' COMMENT '用户来源',
  `channel` varchar(100) DEFAULT '' COMMENT '渠道',
  `registtime` int(10) NOT NULL COMMENT '用户注册时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pay_order_bak_20180222
-- ----------------------------
INSERT INTO `pay_order_bak_20180222` VALUES ('1180222092522890', '10000034', '10000034', '6.00', '0', '1519262722', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'test', '1507602362');
INSERT INTO `pay_order_bak_20180222` VALUES ('1180222092624499', '10000034', '10000034', '6.00', '0', '1519262784', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'test', '1507602362');
INSERT INTO `pay_order_bak_20180222` VALUES ('1180222093213792', '10000034', '10000034', '6.00', '0', '1519263133', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'test', '1507602362');
INSERT INTO `pay_order_bak_20180222` VALUES ('1180222093313613', '10000034', '10000034', '6.00', '0', '1519263193', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'test', '1507602362');
INSERT INTO `pay_order_bak_20180222` VALUES ('1180222094512592', '10000033', '10000033', '6.00', '0', '1519263912', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'official', '1507601417');
INSERT INTO `pay_order_bak_20180222` VALUES ('1180222094734447', '10000033', '10000033', '6.00', '0', '1519264054', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'official', '1507601417');
INSERT INTO `pay_order_bak_20180222` VALUES ('1180222094803267', '10000033', '10000033', '6.00', '0', '1519264083', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'official', '1507601417');
INSERT INTO `pay_order_bak_20180222` VALUES ('1180222094834168', '10000033', '10000033', '6.00', '0', '1519264114', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'official', '1507601417');
INSERT INTO `pay_order_bak_20180222` VALUES ('1180222103619809', '10000033', '10000033', '6.00', '0', '1519266979', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'official', '1507601417');
INSERT INTO `pay_order_bak_20180222` VALUES ('1180222105212005', '10000033', '10000033', '6.00', '0', '1519267932', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'official', '1507601417');
INSERT INTO `pay_order_bak_20180222` VALUES ('1180222105441042', '10000033', '10000033', '6.00', '0', '1519268081', '0', '1', 'reapal_weixin', '0', '', '充值600金币_6_weixin', 'test', 'official', '1507601417');
INSERT INTO `pay_order_bak_20180222` VALUES ('2180129092214238', '10000036', '10000036', '6.00', '0', '1517188934', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575');
INSERT INTO `pay_order_bak_20180222` VALUES ('2180129092217888', '10000036', '10000036', '6.00', '0', '1517188937', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575');
INSERT INTO `pay_order_bak_20180222` VALUES ('2180222104148491', '10000036', '10000036', '6.00', '0', '1519267308', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575');
INSERT INTO `pay_order_bak_20180222` VALUES ('2180222104228232', '10000036', '10000036', '6.00', '0', '1519267348', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575');
INSERT INTO `pay_order_bak_20180222` VALUES ('2180222104524568', '10000036', '10000036', '6.00', '0', '1519267524', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575');
INSERT INTO `pay_order_bak_20180222` VALUES ('2180222104638523', '10000036', '10000036', '6.00', '0', '1519267598', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575');
INSERT INTO `pay_order_bak_20180222` VALUES ('2180222104651946', '10000036', '10000036', '6.00', '0', '1519267611', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575');
INSERT INTO `pay_order_bak_20180222` VALUES ('2180222104803983', '10000036', '10000036', '6.00', '0', '1519267683', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575');
INSERT INTO `pay_order_bak_20180222` VALUES ('2180222111505560', '10000036', '10000036', '6.00', '0', '1519269305', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575');
INSERT INTO `pay_order_bak_20180222` VALUES ('2180222111532275', '10000036', '10000036', '6.00', '0', '1519269332', '0', '2', 'reapal_weixin', '0', '', '充值600个金币_6_weixin', 'appstore', '', '1507620575');
INSERT INTO `pay_order_bak_20180222` VALUES ('2180222112007968', '10000036', '10000036', '6.00', '0', '1519269607', '0', '2', 'reapal_weixin', '0', '', '充值1个金币_6_weixin', 'appstore', '', '1507620575');

-- ----------------------------
-- Table structure for `pay_order_baofeng`
-- ----------------------------
DROP TABLE IF EXISTS `pay_order_baofeng`;
CREATE TABLE `pay_order_baofeng` (
  `order_id` char(50) NOT NULL DEFAULT '' COMMENT '充值号',
  `srcuid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '充值操作人uid',
  `dstuid` int(10) NOT NULL DEFAULT '0' COMMENT '充值目的uid',
  `amount` double(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '充值金额',
  `zhutou` int(11) NOT NULL DEFAULT '0' COMMENT 'å¯¹æ¢çš„çŒªå¤´æ•°',
  `creatAt` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '充值下单时间',
  `paytime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '发币时间',
  `os` int(2) NOT NULL COMMENT '充值类型 1=>android，2=>iphone, 3=>微信公众号，4=>h5, 5=>官网web, 6=>风秀web',
  `paytype` char(30) NOT NULL DEFAULT '' COMMENT '=alipay =weixin =apple',
  `status` int(1) unsigned NOT NULL DEFAULT '0' COMMENT '支付状态 =0生成支付单 =1以到支付平台等待支付 =2已支付 =3取消',
  `inpour_no` char(100) DEFAULT NULL,
  `details` varchar(200) NOT NULL DEFAULT '' COMMENT '充值描述',
  `userSource` varchar(100) NOT NULL DEFAULT '' COMMENT '用户来源',
  `channel` varchar(100) DEFAULT '' COMMENT '用户支付渠道',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `rechargesn` (`order_id`) USING BTREE,
  KEY `uid` (`srcuid`),
  KEY `paytime` (`paytime`,`status`),
  KEY `index_userSource` (`userSource`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='暴风充值记录';

-- ----------------------------
-- Records of pay_order_baofeng
-- ----------------------------

-- ----------------------------
-- Table structure for `pay_supplement`
-- ----------------------------
DROP TABLE IF EXISTS `pay_supplement`;
CREATE TABLE `pay_supplement` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL DEFAULT '0' COMMENT '被补人的UID',
  `amount` int(6) NOT NULL DEFAULT '0' COMMENT '丢失的充值金额',
  `zhutou` int(8) NOT NULL DEFAULT '0' COMMENT '需要补的金额',
  `bak` varchar(255) NOT NULL DEFAULT '' COMMENT '补单说明',
  `orderNo` varchar(50) NOT NULL DEFAULT '' COMMENT '单号',
  `paytype` varchar(30) NOT NULL DEFAULT '' COMMENT '支付方式[apple,weixin,alipay,wechat]',
  `adminid` int(11) NOT NULL COMMENT '操作者UID',
  `addtime` int(11) NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值异常补单';

-- ----------------------------
-- Records of pay_supplement
-- ----------------------------

-- ----------------------------
-- Table structure for `pay_union_support`
-- ----------------------------
DROP TABLE IF EXISTS `pay_union_support`;
CREATE TABLE `pay_union_support` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ymdtime` int(11) NOT NULL DEFAULT '0' COMMENT '年月日(yyyyMMdd)',
  `uid` int(11) NOT NULL DEFAULT '0' COMMENT '扶持号UID',
  `unionid` int(11) NOT NULL DEFAULT '0' COMMENT '扶持公会ID',
  `pays` int(11) NOT NULL DEFAULT '0' COMMENT '充值猪币数',
  `money` int(11) NOT NULL DEFAULT '0' COMMENT '剩余猪币',
  `consume` int(11) NOT NULL DEFAULT '0' COMMENT '对外消费猪币',
  `amount` int(11) NOT NULL DEFAULT '0' COMMENT '总消费数',
  `addtime` int(11) NOT NULL DEFAULT '0' COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_ymduid` (`ymdtime`,`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='扶持号 每日数据汇总';

-- ----------------------------
-- Records of pay_union_support
-- ----------------------------

-- ----------------------------
-- Table structure for `pay_week_star`
-- ----------------------------
DROP TABLE IF EXISTS `pay_week_star`;
CREATE TABLE `pay_week_star` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(10) NOT NULL DEFAULT '0',
  `gid` int(5) NOT NULL DEFAULT '0',
  `usertype` int(11) NOT NULL DEFAULT '0' COMMENT '=1主播 =2用户',
  `cycle` varchar(10) NOT NULL DEFAULT '' COMMENT '发放周期[yyyyMMdd]',
  `scores` int(11) NOT NULL DEFAULT '0' COMMENT '获得数量',
  `amount` int(11) NOT NULL COMMENT '主播:声援值 用户:猪头数',
  `adminuid` int(11) NOT NULL COMMENT '操作人',
  `addtime` int(11) NOT NULL DEFAULT '0' COMMENT '操作日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `gid_uid_cycle_type` (`uid`,`gid`,`usertype`,`cycle`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='周星发放';

-- ----------------------------
-- Records of pay_week_star
-- ----------------------------
INSERT INTO `pay_week_star` VALUES ('1', '10000033', '21', '1', '20171023', '3', '100000', '0', '1509310800');
INSERT INTO `pay_week_star` VALUES ('2', '10000034', '21', '2', '20171023', '2', '2000', '0', '1509310800');
INSERT INTO `pay_week_star` VALUES ('3', '10000038', '22', '1', '20171023', '1', '100000', '0', '1509310800');
INSERT INTO `pay_week_star` VALUES ('4', '10000036', '22', '2', '20171023', '1', '2000', '0', '1509310800');
INSERT INTO `pay_week_star` VALUES ('5', '10000045', '5', '1', '20171023', '1', '100000', '0', '1509310800');
INSERT INTO `pay_week_star` VALUES ('6', '10000034', '5', '2', '20171023', '2', '2000', '0', '1509310800');
INSERT INTO `pay_week_star` VALUES ('7', '10000036', '5', '1', '20171225', '5', '100000', '0', '1514754000');
INSERT INTO `pay_week_star` VALUES ('8', '10000051', '5', '2', '20171225', '6', '2000', '0', '1514754000');
INSERT INTO `pay_week_star` VALUES ('9', '10000038', '11', '1', '20180312', '6951060', '100000', '0', '1521406800');
INSERT INTO `pay_week_star` VALUES ('10', '10000041', '11', '2', '20180312', '7082460', '2000', '0', '1521406800');
INSERT INTO `pay_week_star` VALUES ('11', '10000036', '18', '1', '20180312', '33330', '100000', '0', '1521406800');
INSERT INTO `pay_week_star` VALUES ('12', '10000041', '18', '2', '20180312', '33330', '2000', '0', '1521406800');
INSERT INTO `pay_week_star` VALUES ('13', '10000210', '11', '1', '20180319', '1300860', '100000', '0', '1522011600');
INSERT INTO `pay_week_star` VALUES ('14', '10000041', '11', '2', '20180319', '1300860', '2000', '0', '1522011600');
INSERT INTO `pay_week_star` VALUES ('15', '10000210', '5', '1', '20180319', '1800', '100000', '0', '1522011600');
INSERT INTO `pay_week_star` VALUES ('16', '10000209', '5', '2', '20180319', '1800', '2000', '0', '1522011600');
INSERT INTO `pay_week_star` VALUES ('17', '10000041', '131', '1', '20180326', '131', '0', '0', '1522616400');
INSERT INTO `pay_week_star` VALUES ('18', '10000033', '131', '2', '20180326', '131', '0', '0', '1522616400');
INSERT INTO `pay_week_star` VALUES ('19', '10000041', '117', '1', '20180326', '468', '0', '0', '1522616400');
INSERT INTO `pay_week_star` VALUES ('20', '10000033', '117', '2', '20180326', '468', '0', '0', '1522616400');
INSERT INTO `pay_week_star` VALUES ('21', '10000051', '10', '1', '20180409', '51000', '0', '0', '1523826000');
INSERT INTO `pay_week_star` VALUES ('22', '10000041', '10', '2', '20180409', '30000', '0', '0', '1523826000');
INSERT INTO `pay_week_star` VALUES ('23', '10000216', '96', '1', '20180409', '33320', '0', '0', '1523826000');
INSERT INTO `pay_week_star` VALUES ('24', '10000033', '96', '2', '20180409', '32340', '0', '0', '1523826000');
INSERT INTO `pay_week_star` VALUES ('25', '10000051', '7', '1', '20180409', '55', '0', '0', '1523826000');
INSERT INTO `pay_week_star` VALUES ('26', '10000041', '7', '2', '20180409', '29', '0', '0', '1523826000');

-- ----------------------------
-- Table structure for `pay_withdraw`
-- ----------------------------
DROP TABLE IF EXISTS `pay_withdraw`;
CREATE TABLE `pay_withdraw` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `billno` varchar(35) NOT NULL COMMENT '提现单号',
  `uid` int(10) unsigned NOT NULL,
  `type` varchar(50) NOT NULL COMMENT '=weixin微信 =alipay支付宝',
  `amount` double(10,2) NOT NULL COMMENT '金额 单位元',
  `credit` int(10) NOT NULL COMMENT '消耗的声援值',
  `sendListid` varchar(35) DEFAULT '' COMMENT '红包订单的微信单号',
  `isSecc` int(1) DEFAULT '0' COMMENT '=0待成功 =1成功',
  `createAt` int(11) NOT NULL DEFAULT '0' COMMENT '产生提现单时间',
  `sendTime` bigint(20) DEFAULT '0' COMMENT '红包发送时间',
  `openid` varchar(40) DEFAULT '' COMMENT '接受红包用户openid',
  `adminid` int(11) NOT NULL DEFAULT '0' COMMENT '审核人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `billno` (`billno`),
  KEY `uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='提现列表';

-- ----------------------------
-- Records of pay_withdraw
-- ----------------------------
