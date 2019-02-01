/*
Navicat MySQL Data Transfer

Source Server         : admin
Source Server Version : 50639
Source Host           : 192.168.20.251:3306
Source Database       : zhu_analysis

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2018-05-29 09:34:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `analysis_eggs`
-- ----------------------------
DROP TABLE IF EXISTS `analysis_eggs`;
CREATE TABLE `analysis_eggs` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `hammer` int(2) NOT NULL COMMENT '=1铜锤 =2金锤 =3紫锤',
  `datetimes` int(8) NOT NULL DEFAULT '0' COMMENT '日期[yyyyMMdd]',
  `times` int(5) NOT NULL DEFAULT '0' COMMENT '砸蛋次数',
  `consume` int(10) NOT NULL DEFAULT '0' COMMENT '消耗猪头数',
  `gid1` int(11) NOT NULL DEFAULT '0' COMMENT '礼物一',
  `gid2` int(11) NOT NULL DEFAULT '0' COMMENT '礼物二',
  `gid3` int(11) NOT NULL DEFAULT '0' COMMENT '礼物三',
  `gid4` int(11) NOT NULL DEFAULT '0' COMMENT '礼物四',
  `gid5` int(11) NOT NULL DEFAULT '0' COMMENT '礼物五',
  `gid6` int(11) NOT NULL DEFAULT '0' COMMENT '礼物六',
  `gid7` int(11) NOT NULL DEFAULT '0' COMMENT '礼物七',
  `gid8` int(11) NOT NULL DEFAULT '0' COMMENT '礼物八',
  `gets` int(11) NOT NULL DEFAULT '0' COMMENT '砸出猪头',
  `addtime` int(11) NOT NULL DEFAULT '0' COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `hammer-datetimes` (`hammer`,`datetimes`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='铜锤 10猪币[礼物排序 小色猪*5   小色猪*7   小黄鸡*2 小黄鸡*3 亲亲*3   亲亲*6  金砖        跑车]\n金锤 25猪币[礼物排序 小色猪*10 小色猪*15  小黄鸡*5 小黄鸡*7 亲亲*7   亲亲*10 甲壳虫     兰博基尼]\n紫锤 50猪币[礼物排序 小色猪*15 小色猪*20 小黄鸡*8 小黄鸡*10 亲亲*10 亲亲*15 兰博基尼 飞机]';

-- ----------------------------
-- Records of analysis_eggs
-- ----------------------------
INSERT INTO `analysis_eggs` VALUES ('1', '1', '20170926', '12', '120', '3', '3', '0', '5', '1', '0', '0', '0', '81', '1506478605');
INSERT INTO `analysis_eggs` VALUES ('2', '1', '20170929', '42', '420', '17', '15', '5', '2', '1', '1', '1', '0', '467', '1506719700');
INSERT INTO `analysis_eggs` VALUES ('3', '1', '20171009', '12', '120', '4', '4', '3', '0', '0', '1', '0', '0', '90', '1507583700');
INSERT INTO `analysis_eggs` VALUES ('4', '1', '20171010', '22', '220', '10', '9', '2', '1', '0', '0', '0', '0', '127', '1507670100');
INSERT INTO `analysis_eggs` VALUES ('5', '1', '20171012', '111', '1110', '45', '34', '23', '7', '1', '1', '0', '0', '642', '1507842900');
INSERT INTO `analysis_eggs` VALUES ('6', '1', '20171013', '4', '40', '2', '0', '2', '0', '0', '0', '0', '0', '18', '1507929300');
INSERT INTO `analysis_eggs` VALUES ('7', '1', '20171016', '386', '3860', '150', '122', '76', '25', '7', '5', '1', '0', '2513', '1508188500');
INSERT INTO `analysis_eggs` VALUES ('8', '1', '20171018', '15', '150', '5', '7', '2', '1', '0', '0', '0', '0', '88', '1508361300');
INSERT INTO `analysis_eggs` VALUES ('9', '1', '20171024', '11', '110', '7', '2', '0', '1', '0', '0', '1', '0', '255', '1508879700');
INSERT INTO `analysis_eggs` VALUES ('10', '3', '20171024', '1', '50', '0', '0', '0', '0', '1', '0', '0', '0', '50', '1508879700');
INSERT INTO `analysis_eggs` VALUES ('11', '1', '20171031', '24', '240', '11', '9', '1', '3', '0', '0', '0', '0', '140', '1509484500');
INSERT INTO `analysis_eggs` VALUES ('12', '1', '20171101', '7', '70', '4', '0', '2', '1', '0', '0', '0', '0', '34', '1509570900');
INSERT INTO `analysis_eggs` VALUES ('13', '1', '20171102', '1', '10', '1', '0', '0', '0', '0', '0', '0', '0', '5', '1509657300');
INSERT INTO `analysis_eggs` VALUES ('14', '1', '20171109', '4', '40', '0', '3', '1', '0', '0', '0', '0', '0', '25', '1510262100');
INSERT INTO `analysis_eggs` VALUES ('15', '1', '20171110', '1', '10', '1', '0', '0', '0', '0', '0', '0', '0', '5', '1510348500');
INSERT INTO `analysis_eggs` VALUES ('16', '1', '20171115', '2', '20', '0', '1', '0', '1', '0', '0', '0', '0', '13', '1510780500');
INSERT INTO `analysis_eggs` VALUES ('17', '1', '20171116', '11', '110', '6', '2', '1', '2', '0', '0', '0', '0', '60', '1510866900');
INSERT INTO `analysis_eggs` VALUES ('18', '1', '20171120', '49', '490', '26', '11', '9', '2', '1', '0', '0', '0', '270', '1511212500');
INSERT INTO `analysis_eggs` VALUES ('19', '2', '20171120', '22', '550', '10', '6', '4', '1', '1', '0', '0', '0', '279', '1511212500');
INSERT INTO `analysis_eggs` VALUES ('20', '3', '20171120', '1', '50', '0', '1', '0', '0', '0', '0', '0', '0', '20', '1511212500');
INSERT INTO `analysis_eggs` VALUES ('21', '1', '20171121', '62', '620', '18', '20', '18', '5', '0', '1', '0', '0', '362', '1511298900');
INSERT INTO `analysis_eggs` VALUES ('22', '3', '20171121', '346', '17300', '119', '125', '66', '21', '4', '7', '3', '1', '22152', '1511298900');
INSERT INTO `analysis_eggs` VALUES ('23', '1', '20171122', '96', '960', '36', '36', '18', '4', '2', '0', '0', '0', '558', '1511385300');
INSERT INTO `analysis_eggs` VALUES ('24', '1', '20171123', '80', '800', '31', '25', '16', '5', '3', '0', '0', '0', '469', '1511471700');
INSERT INTO `analysis_eggs` VALUES ('25', '2', '20171123', '41', '1025', '14', '18', '6', '0', '3', '0', '0', '0', '575', '1511471700');
INSERT INTO `analysis_eggs` VALUES ('26', '3', '20171126', '8', '400', '0', '0', '0', '0', '0', '0', '0', '0', '21056', '1511730900');
INSERT INTO `analysis_eggs` VALUES ('27', '3', '20171127', '38', '1900', '0', '0', '0', '0', '0', '0', '0', '0', '310286', '1511817300');
INSERT INTO `analysis_eggs` VALUES ('28', '1', '20180118', '12', '120', '0', '0', '0', '0', '0', '0', '0', '0', '144800', '1516310100');

-- ----------------------------
-- Table structure for `analysis_gift_consume`
-- ----------------------------
DROP TABLE IF EXISTS `analysis_gift_consume`;
CREATE TABLE `analysis_gift_consume` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `times` int(8) NOT NULL COMMENT '时间[yyyyMMdd]',
  `gid` int(4) NOT NULL DEFAULT '0' COMMENT '礼物ID',
  `gname` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '礼物名称',
  `counts` int(11) NOT NULL DEFAULT '0' COMMENT '礼物消耗数量',
  `amount` int(11) NOT NULL DEFAULT '0' COMMENT '礼物消耗猪币数',
  `adminID` int(11) NOT NULL DEFAULT '0' COMMENT '=0系统',
  `addtime` int(11) NOT NULL DEFAULT '0' COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `times_gid` (`times`,`gid`)
) ENGINE=InnoDB AUTO_INCREMENT=1148 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='礼物消耗数据的静态化';

-- ----------------------------
-- Records of analysis_gift_consume
-- ----------------------------
INSERT INTO `analysis_gift_consume` VALUES ('1', '20170926', '24', '小萌猪', '2', '0', '0', '1506478607');
INSERT INTO `analysis_gift_consume` VALUES ('2', '20170927', '24', '小萌猪', '3', '0', '0', '1506567960');
INSERT INTO `analysis_gift_consume` VALUES ('3', '20170929', '24', '小萌猪', '2', '0', '0', '1506718200');
INSERT INTO `analysis_gift_consume` VALUES ('4', '20170929', '45', '骑士守护', '1', '1314', '0', '1506718200');
INSERT INTO `analysis_gift_consume` VALUES ('5', '20170930', '5', '金砖', '1', '200', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('6', '20170930', '6', '留声机', '3', '1500', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('7', '20170930', '8', '钻戒', '34', '2244', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('8', '20170930', '9', '钻石', '1', '1314', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('9', '20170930', '10', '兰博基尼', '2', '6000', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('10', '20170930', '11', '私人游艇', '1', '8888', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('11', '20170930', '13', '棒棒糖', '2', '2', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('12', '20170930', '15', '亲亲', '1', '5', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('13', '20170930', '18', '飞机', '2', '13332', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('14', '20170930', '19', '私人海岛', '3', '39420', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('15', '20170930', '21', '火箭', '1', '9999', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('16', '20170930', '22', '流星雨', '1', '980', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('17', '20170930', '26', '甲壳虫', '1', '660', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('18', '20170930', '34', '幸运流氓蕉', '6', '6', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('19', '20170930', '45', '骑士守护', '1', '1314', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('20', '20170930', '46', '王子守护', '2', '26280', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('21', '20170930', '96', '来一炮', '1', '980', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('22', '20170930', '102', '烟花', '1', '888', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('23', '20170930', '105', '金豆', '2', '2', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('24', '20170930', '106', '龙头', '1', '88', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('25', '20170930', '107', '三岁', '1', '333', '0', '1506804600');
INSERT INTO `analysis_gift_consume` VALUES ('26', '20171009', '1', '弹幕', '1', '1', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('27', '20171009', '5', '金砖', '2', '400', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('28', '20171009', '6', '留声机', '2', '1000', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('29', '20171009', '8', '钻戒', '1', '66', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('30', '20171009', '10', '兰博基尼', '1', '3000', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('31', '20171009', '11', '私人游艇', '1', '8888', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('32', '20171009', '13', '棒棒糖', '16', '16', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('33', '20171009', '21', '火箭', '2', '19998', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('34', '20171009', '22', '流星雨', '2', '1960', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('35', '20171009', '23', '喇叭', '1', '188', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('36', '20171009', '24', '小萌猪', '4', '0', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('37', '20171009', '26', '甲壳虫', '7', '4620', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('38', '20171009', '34', '幸运流氓蕉', '6', '6', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('39', '20171009', '46', '王子守护', '1', '13140', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('40', '20171009', '83', '香吻', '1', '1', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('41', '20171009', '96', '来一炮', '2', '1960', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('42', '20171009', '105', '金豆', '1', '1', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('43', '20171009', '106', '龙头', '1', '88', '0', '1507582200');
INSERT INTO `analysis_gift_consume` VALUES ('44', '20171010', '1', '弹幕', '1', '1', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('45', '20171010', '5', '金砖', '1', '200', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('46', '20171010', '9', '钻石', '2', '2628', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('47', '20171010', '11', '私人游艇', '1', '8888', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('48', '20171010', '13', '棒棒糖', '8', '8', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('49', '20171010', '14', '小黄鸡', '1', '2', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('50', '20171010', '15', '亲亲', '1', '5', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('51', '20171010', '16', '情书', '3', '30', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('52', '20171010', '18', '飞机', '1', '6666', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('53', '20171010', '19', '私人海岛', '1', '13140', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('54', '20171010', '21', '火箭', '1', '9999', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('55', '20171010', '22', '流星雨', '5', '4900', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('56', '20171010', '23', '喇叭', '3', '564', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('57', '20171010', '24', '小萌猪', '2', '0', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('58', '20171010', '32', '幸运玫瑰', '2', '2', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('59', '20171010', '33', '幸运啤酒', '124', '248', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('60', '20171010', '34', '幸运流氓蕉', '16', '16', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('61', '20171010', '45', '骑士守护', '2', '2628', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('62', '20171010', '78', '生日蛋糕', '1', '1000', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('63', '20171010', '82', '丘比特之箭', '1', '520', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('64', '20171010', '92', '口红', '1', '199', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('65', '20171010', '96', '来一炮', '6', '5880', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('66', '20171010', '102', '烟花', '2', '1776', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('67', '20171010', '104', '蓝色妖姬', '1', '88', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('68', '20171010', '105', '金豆', '2', '2', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('69', '20171010', '107', '三岁', '1', '333', '0', '1507668600');
INSERT INTO `analysis_gift_consume` VALUES ('70', '20171012', '5', '金砖', '13', '2600', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('71', '20171012', '7', '心动', '1', '1', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('72', '20171012', '8', '钻戒', '15', '990', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('73', '20171012', '9', '钻石', '1', '1314', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('74', '20171012', '10', '兰博基尼', '7', '21000', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('75', '20171012', '11', '私人游艇', '9', '79992', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('76', '20171012', '13', '棒棒糖', '272', '272', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('77', '20171012', '14', '小黄鸡', '2', '4', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('78', '20171012', '15', '亲亲', '2', '10', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('79', '20171012', '18', '飞机', '2', '13332', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('80', '20171012', '19', '私人海岛', '1', '13140', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('81', '20171012', '22', '流星雨', '3', '2940', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('82', '20171012', '24', '小萌猪', '9', '0', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('83', '20171012', '26', '甲壳虫', '18', '11880', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('84', '20171012', '32', '幸运玫瑰', '1', '1', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('85', '20171012', '33', '幸运啤酒', '1', '2', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('86', '20171012', '34', '幸运流氓蕉', '29', '29', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('87', '20171012', '46', '王子守护', '3', '39420', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('88', '20171012', '73', '奋斗', '25', '25', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('89', '20171012', '78', '生日蛋糕', '1', '1000', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('90', '20171012', '80', '小色猪', '6', '6', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('91', '20171012', '81', '鼓掌', '33', '33', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('92', '20171012', '82', '丘比特之箭', '1', '520', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('93', '20171012', '83', '香吻', '8', '8', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('94', '20171012', '84', '相互伤害', '2', '4', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('95', '20171012', '92', '口红', '8', '1592', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('96', '20171012', '93', '圣诞老人', '82', '164', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('97', '20171012', '96', '来一炮', '5', '4900', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('98', '20171012', '102', '烟花', '1', '888', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('99', '20171012', '105', '金豆', '57', '57', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('100', '20171012', '106', '龙头', '20', '1760', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('101', '20171012', '107', '三岁', '2', '666', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('102', '20171012', '108', '飞fdgfg', '1', '6661', '0', '1507841400');
INSERT INTO `analysis_gift_consume` VALUES ('103', '20171013', '1', '弹幕', '1', '1', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('104', '20171013', '2', '玫瑰花', '5', '5', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('105', '20171013', '4', '猫咪', '1', '5', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('106', '20171013', '5', '金砖', '8', '1600', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('107', '20171013', '10', '兰博基尼', '1', '3000', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('108', '20171013', '13', '棒棒糖', '2', '2', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('109', '20171013', '14', '小黄鸡', '1', '2', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('110', '20171013', '15', '亲亲', '2', '10', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('111', '20171013', '21', '火箭', '1', '9999', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('112', '20171013', '22', '流星雨', '3', '2940', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('113', '20171013', '24', '小萌猪', '2', '0', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('114', '20171013', '26', '甲壳虫', '18', '11880', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('115', '20171013', '34', '幸运流氓蕉', '5', '5', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('116', '20171013', '45', '骑士守护', '2', '2628', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('117', '20171013', '81', '鼓掌', '1', '1', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('118', '20171013', '83', '香吻', '13', '13', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('119', '20171013', '92', '口红', '1', '199', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('120', '20171013', '93', '圣诞老人', '3', '6', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('121', '20171013', '102', '烟花', '1', '888', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('122', '20171013', '104', '蓝色妖姬', '3', '264', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('123', '20171013', '105', '金豆', '37', '37', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('124', '20171013', '106', '龙头', '6', '528', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('125', '20171013', '107', '三岁', '6', '1998', '0', '1507927800');
INSERT INTO `analysis_gift_consume` VALUES ('126', '20171016', '2', '玫瑰花', '520', '520', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('127', '20171016', '3', '猪头', '28', '56', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('128', '20171016', '4', '猫咪', '3', '15', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('129', '20171016', '5', '金砖', '310', '62000', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('130', '20171016', '6', '留声机', '1', '500', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('131', '20171016', '8', '钻戒', '2', '132', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('132', '20171016', '9', '钻石', '3', '3942', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('133', '20171016', '10', '兰博基尼', '11', '33000', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('134', '20171016', '11', '私人游艇', '4', '35552', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('135', '20171016', '13', '棒棒糖', '71', '71', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('136', '20171016', '14', '小黄鸡', '25', '50', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('137', '20171016', '15', '亲亲', '1', '5', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('138', '20171016', '18', '飞机', '2', '13332', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('139', '20171016', '19', '私人海岛', '1', '13140', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('140', '20171016', '21', '火箭', '1', '9999', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('141', '20171016', '22', '流星雨', '10', '9800', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('142', '20171016', '24', '小萌猪', '11', '0', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('143', '20171016', '26', '甲壳虫', '8', '5280', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('144', '20171016', '29', '守护人气猪', '3', '0', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('145', '20171016', '33', '幸运啤酒', '9', '18', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('146', '20171016', '34', '幸运流氓蕉', '2720', '2720', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('147', '20171016', '45', '骑士守护', '7', '9198', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('148', '20171016', '46', '王子守护', '7', '91980', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('149', '20171016', '59', '约定今生', '1', '520', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('150', '20171016', '73', '奋斗', '1', '1', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('151', '20171016', '78', '生日蛋糕', '1', '1000', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('152', '20171016', '80', '小色猪', '423', '423', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('153', '20171016', '81', '鼓掌', '3', '3', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('154', '20171016', '82', '丘比特之箭', '7', '3640', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('155', '20171016', '83', '香吻', '1045', '1045', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('156', '20171016', '84', '相互伤害', '12', '24', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('157', '20171016', '92', '口红', '5', '995', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('158', '20171016', '93', '圣诞老人', '23', '46', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('159', '20171016', '96', '来一炮', '21', '20580', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('160', '20171016', '102', '烟花', '1', '888', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('161', '20171016', '104', '蓝色妖姬', '3', '264', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('162', '20171016', '105', '金豆', '184', '184', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('163', '20171016', '106', '龙头', '22', '1936', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('164', '20171016', '107', '三岁', '3', '999', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('165', '20171016', '108', '飞fdgfg', '1', '6661', '0', '1508187000');
INSERT INTO `analysis_gift_consume` VALUES ('166', '20171017', '1', '弹幕', '2', '2', '0', '1508273400');
INSERT INTO `analysis_gift_consume` VALUES ('167', '20171017', '5', '金砖', '198', '39600', '0', '1508273400');
INSERT INTO `analysis_gift_consume` VALUES ('168', '20171017', '13', '棒棒糖', '6', '6', '0', '1508273400');
INSERT INTO `analysis_gift_consume` VALUES ('169', '20171017', '23', '喇叭', '1', '188', '0', '1508273400');
INSERT INTO `analysis_gift_consume` VALUES ('170', '20171017', '24', '小萌猪', '1', '0', '0', '1508273400');
INSERT INTO `analysis_gift_consume` VALUES ('171', '20171017', '34', '幸运流氓蕉', '520', '520', '0', '1508273400');
INSERT INTO `analysis_gift_consume` VALUES ('172', '20171017', '45', '骑士守护', '2', '2628', '0', '1508273400');
INSERT INTO `analysis_gift_consume` VALUES ('173', '20171017', '80', '小色猪', '11', '11', '0', '1508273400');
INSERT INTO `analysis_gift_consume` VALUES ('174', '20171017', '102', '烟花', '3', '2664', '0', '1508273400');
INSERT INTO `analysis_gift_consume` VALUES ('175', '20171017', '105', '金豆', '13582', '13582', '0', '1508273400');
INSERT INTO `analysis_gift_consume` VALUES ('176', '20171017', '106', '龙头', '1', '88', '0', '1508273400');
INSERT INTO `analysis_gift_consume` VALUES ('177', '20171018', '5', '金砖', '1', '200', '0', '1508359800');
INSERT INTO `analysis_gift_consume` VALUES ('178', '20171018', '11', '私人游艇', '1', '8888', '0', '1508359800');
INSERT INTO `analysis_gift_consume` VALUES ('179', '20171018', '18', '飞机', '2', '13332', '0', '1508359800');
INSERT INTO `analysis_gift_consume` VALUES ('180', '20171018', '19', '私人海岛', '1', '13140', '0', '1508359800');
INSERT INTO `analysis_gift_consume` VALUES ('181', '20171018', '21', '火箭', '1', '9999', '0', '1508359800');
INSERT INTO `analysis_gift_consume` VALUES ('182', '20171018', '24', '小萌猪', '1', '0', '0', '1508359800');
INSERT INTO `analysis_gift_consume` VALUES ('183', '20171018', '26', '甲壳虫', '1', '660', '0', '1508359800');
INSERT INTO `analysis_gift_consume` VALUES ('184', '20171018', '32', '幸运玫瑰', '47', '47', '0', '1508359800');
INSERT INTO `analysis_gift_consume` VALUES ('185', '20171018', '34', '幸运流氓蕉', '17', '17', '0', '1508359800');
INSERT INTO `analysis_gift_consume` VALUES ('186', '20171018', '45', '骑士守护', '2', '2628', '0', '1508359800');
INSERT INTO `analysis_gift_consume` VALUES ('187', '20171018', '46', '王子守护', '1', '13140', '0', '1508359800');
INSERT INTO `analysis_gift_consume` VALUES ('188', '20171018', '49', '烛光晚餐', '1', '188', '0', '1508359800');
INSERT INTO `analysis_gift_consume` VALUES ('189', '20171018', '59', '约定今生', '3', '1560', '0', '1508359800');
INSERT INTO `analysis_gift_consume` VALUES ('190', '20171018', '80', '小色猪', '3', '3', '0', '1508359800');
INSERT INTO `analysis_gift_consume` VALUES ('191', '20171018', '93', '圣诞老人', '3', '6', '0', '1508359800');
INSERT INTO `analysis_gift_consume` VALUES ('192', '20171018', '105', '金豆', '1', '1', '0', '1508359800');
INSERT INTO `analysis_gift_consume` VALUES ('193', '20171018', '106', '龙头', '1', '88', '0', '1508359800');
INSERT INTO `analysis_gift_consume` VALUES ('194', '20171019', '3', '猪头', '1', '2', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('195', '20171019', '5', '金砖', '1', '200', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('196', '20171019', '7', '心动', '1', '1', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('197', '20171019', '8', '钻戒', '1', '66', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('198', '20171019', '13', '棒棒糖', '548', '548', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('199', '20171019', '14', '小黄鸡', '1', '2', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('200', '20171019', '18', '飞机', '1', '6666', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('201', '20171019', '21', '火箭', '1', '9999', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('202', '20171019', '22', '流星雨', '16', '15680', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('203', '20171019', '24', '小萌猪', '2', '0', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('204', '20171019', '33', '幸运啤酒', '1', '2', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('205', '20171019', '34', '幸运流氓蕉', '13', '13', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('206', '20171019', '45', '骑士守护', '1', '1314', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('207', '20171019', '46', '王子守护', '8', '105120', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('208', '20171019', '73', '奋斗', '2', '2', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('209', '20171019', '80', '小色猪', '2', '2', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('210', '20171019', '82', '丘比特之箭', '1', '520', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('211', '20171019', '83', '香吻', '10', '10', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('212', '20171019', '92', '口红', '4', '796', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('213', '20171019', '93', '圣诞老人', '6', '12', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('214', '20171019', '102', '烟花', '4', '3552', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('215', '20171019', '104', '蓝色妖姬', '2', '176', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('216', '20171019', '105', '金豆', '2', '2', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('217', '20171019', '107', '三岁', '1', '333', '0', '1508446200');
INSERT INTO `analysis_gift_consume` VALUES ('218', '20171023', '1', '弹幕', '1', '1', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('219', '20171023', '5', '金砖', '5', '1000', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('220', '20171023', '7', '心动', '1', '1', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('221', '20171023', '13', '棒棒糖', '3', '3', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('222', '20171023', '14', '小黄鸡', '11', '22', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('223', '20171023', '21', '火箭', '3', '29997', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('224', '20171023', '22', '流星雨', '2', '1960', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('225', '20171023', '24', '小萌猪', '2', '0', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('226', '20171023', '27', '心动', '17', '17', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('227', '20171023', '34', '幸运流氓蕉', '556', '556', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('228', '20171023', '46', '王子守护', '3', '39420', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('229', '20171023', '49', '烛光晚餐', '2', '376', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('230', '20171023', '59', '约定今生', '1', '520', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('231', '20171023', '80', '小色猪', '1', '1', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('232', '20171023', '81', '鼓掌', '99', '99', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('233', '20171023', '82', '丘比特之箭', '1', '520', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('234', '20171023', '84', '相互伤害', '7', '14', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('235', '20171023', '93', '圣诞老人', '5', '10', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('236', '20171023', '96', '来一炮', '6', '5880', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('237', '20171023', '105', '金豆', '10319', '10319', '0', '1508791800');
INSERT INTO `analysis_gift_consume` VALUES ('238', '20171024', '9', '钻石', '1', '1314', '0', '1508878200');
INSERT INTO `analysis_gift_consume` VALUES ('239', '20171024', '13', '棒棒糖', '1', '1', '0', '1508878200');
INSERT INTO `analysis_gift_consume` VALUES ('240', '20171024', '21', '火箭', '1', '9999', '0', '1508878200');
INSERT INTO `analysis_gift_consume` VALUES ('241', '20171024', '24', '小萌猪', '1', '0', '0', '1508878200');
INSERT INTO `analysis_gift_consume` VALUES ('242', '20171024', '32', '幸运玫瑰', '1', '1', '0', '1508878200');
INSERT INTO `analysis_gift_consume` VALUES ('243', '20171024', '34', '幸运流氓蕉', '104', '104', '0', '1508878200');
INSERT INTO `analysis_gift_consume` VALUES ('244', '20171024', '45', '骑士守护', '2', '2628', '0', '1508878200');
INSERT INTO `analysis_gift_consume` VALUES ('245', '20171024', '78', '生日蛋糕', '1', '1000', '0', '1508878200');
INSERT INTO `analysis_gift_consume` VALUES ('246', '20171024', '83', '香吻', '1', '1', '0', '1508878200');
INSERT INTO `analysis_gift_consume` VALUES ('247', '20171024', '84', '相互伤害', '1', '2', '0', '1508878200');
INSERT INTO `analysis_gift_consume` VALUES ('248', '20171024', '93', '圣诞老人', '1', '2', '0', '1508878200');
INSERT INTO `analysis_gift_consume` VALUES ('249', '20171024', '96', '来一炮', '2', '1960', '0', '1508878200');
INSERT INTO `analysis_gift_consume` VALUES ('250', '20171024', '105', '金豆', '2', '2', '0', '1508878200');
INSERT INTO `analysis_gift_consume` VALUES ('251', '20171025', '3', '猪头', '4', '8', '0', '1508964600');
INSERT INTO `analysis_gift_consume` VALUES ('252', '20171025', '5', '金砖', '1', '200', '0', '1508964600');
INSERT INTO `analysis_gift_consume` VALUES ('253', '20171025', '6', '留声机', '6', '3000', '0', '1508964600');
INSERT INTO `analysis_gift_consume` VALUES ('254', '20171025', '7', '心动', '12', '12', '0', '1508964600');
INSERT INTO `analysis_gift_consume` VALUES ('255', '20171025', '15', '亲亲', '1', '5', '0', '1508964600');
INSERT INTO `analysis_gift_consume` VALUES ('256', '20171025', '21', '火箭', '1', '9999', '0', '1508964600');
INSERT INTO `analysis_gift_consume` VALUES ('257', '20171025', '24', '小萌猪', '4', '0', '0', '1508964600');
INSERT INTO `analysis_gift_consume` VALUES ('258', '20171025', '26', '甲壳虫', '1', '660', '0', '1508964600');
INSERT INTO `analysis_gift_consume` VALUES ('259', '20171025', '34', '幸运流氓蕉', '103', '103', '0', '1508964600');
INSERT INTO `analysis_gift_consume` VALUES ('260', '20171025', '102', '烟花', '1', '888', '0', '1508964600');
INSERT INTO `analysis_gift_consume` VALUES ('261', '20171025', '105', '金豆', '38', '38', '0', '1508964600');
INSERT INTO `analysis_gift_consume` VALUES ('262', '20171025', '109', '111', '3', '6', '0', '1508964600');
INSERT INTO `analysis_gift_consume` VALUES ('263', '20171026', '24', '小萌猪', '1', '0', '0', '1509051000');
INSERT INTO `analysis_gift_consume` VALUES ('264', '20171101', '5', '金砖', '2', '400', '0', '1509569400');
INSERT INTO `analysis_gift_consume` VALUES ('265', '20171101', '24', '小萌猪', '3', '0', '0', '1509569400');
INSERT INTO `analysis_gift_consume` VALUES ('266', '20171101', '34', '幸运流氓蕉', '8', '8', '0', '1509569400');
INSERT INTO `analysis_gift_consume` VALUES ('267', '20171101', '93', '圣诞老人', '7', '14', '0', '1509569400');
INSERT INTO `analysis_gift_consume` VALUES ('268', '20171101', '106', '龙头', '3', '264', '0', '1509569400');
INSERT INTO `analysis_gift_consume` VALUES ('269', '20171102', '13', '棒棒糖', '7', '7', '0', '1509655800');
INSERT INTO `analysis_gift_consume` VALUES ('270', '20171102', '24', '小萌猪', '2', '0', '0', '1509655800');
INSERT INTO `analysis_gift_consume` VALUES ('271', '20171102', '59', '约定今生', '1', '520', '0', '1509655800');
INSERT INTO `analysis_gift_consume` VALUES ('272', '20171102', '93', '圣诞老人', '6', '12', '0', '1509655800');
INSERT INTO `analysis_gift_consume` VALUES ('273', '20171110', '5', '金砖', '8', '1600', '0', '1510347000');
INSERT INTO `analysis_gift_consume` VALUES ('274', '20171110', '14', '小黄鸡', '1', '2', '0', '1510347000');
INSERT INTO `analysis_gift_consume` VALUES ('275', '20171110', '27', '心动', '2', '2', '0', '1510347000');
INSERT INTO `analysis_gift_consume` VALUES ('276', '20171110', '29', '守护人气猪', '3', '0', '0', '1510347000');
INSERT INTO `analysis_gift_consume` VALUES ('277', '20171110', '34', '幸运流氓蕉', '5', '5', '0', '1510347000');
INSERT INTO `analysis_gift_consume` VALUES ('278', '20171110', '80', '小色猪', '2', '2', '0', '1510347000');
INSERT INTO `analysis_gift_consume` VALUES ('279', '20171110', '105', '金豆', '1', '1', '0', '1510347000');
INSERT INTO `analysis_gift_consume` VALUES ('280', '20171110', '106', '龙头', '1', '88', '0', '1510347000');
INSERT INTO `analysis_gift_consume` VALUES ('281', '20171113', '1', '弹幕', '1', '1', '0', '1510606200');
INSERT INTO `analysis_gift_consume` VALUES ('282', '20171114', '1', '弹幕', '3', '3', '0', '1510692600');
INSERT INTO `analysis_gift_consume` VALUES ('283', '20171114', '23', '喇叭', '4', '752', '0', '1510692600');
INSERT INTO `analysis_gift_consume` VALUES ('284', '20171114', '26', '甲壳虫', '1', '660', '0', '1510692600');
INSERT INTO `analysis_gift_consume` VALUES ('285', '20171114', '27', '心动', '22', '22', '0', '1510692600');
INSERT INTO `analysis_gift_consume` VALUES ('286', '20171114', '93', '圣诞老人', '4', '8', '0', '1510692600');
INSERT INTO `analysis_gift_consume` VALUES ('287', '20171114', '96', '来一炮', '1', '980', '0', '1510692600');
INSERT INTO `analysis_gift_consume` VALUES ('288', '20171115', '1', '弹幕', '3', '3', '0', '1510779000');
INSERT INTO `analysis_gift_consume` VALUES ('289', '20171115', '5', '金砖', '9', '1800', '0', '1510779000');
INSERT INTO `analysis_gift_consume` VALUES ('290', '20171115', '10', '兰博基尼', '1', '3000', '0', '1510779000');
INSERT INTO `analysis_gift_consume` VALUES ('291', '20171115', '11', '私人游艇', '2', '17776', '0', '1510779000');
INSERT INTO `analysis_gift_consume` VALUES ('292', '20171115', '13', '棒棒糖', '23', '23', '0', '1510779000');
INSERT INTO `analysis_gift_consume` VALUES ('293', '20171115', '22', '流星雨', '1', '980', '0', '1510779000');
INSERT INTO `analysis_gift_consume` VALUES ('294', '20171115', '23', '喇叭', '4', '752', '0', '1510779000');
INSERT INTO `analysis_gift_consume` VALUES ('295', '20171115', '26', '甲壳虫', '1', '660', '0', '1510779000');
INSERT INTO `analysis_gift_consume` VALUES ('296', '20171115', '34', '幸运流氓蕉', '20', '20', '0', '1510779000');
INSERT INTO `analysis_gift_consume` VALUES ('297', '20171115', '59', '约定今生', '2', '1040', '0', '1510779000');
INSERT INTO `analysis_gift_consume` VALUES ('298', '20171115', '73', '奋斗', '99', '99', '0', '1510779000');
INSERT INTO `analysis_gift_consume` VALUES ('299', '20171115', '92', '口红', '1', '199', '0', '1510779000');
INSERT INTO `analysis_gift_consume` VALUES ('300', '20171115', '93', '圣诞老人', '51', '102', '0', '1510779000');
INSERT INTO `analysis_gift_consume` VALUES ('301', '20171115', '96', '来一炮', '1', '980', '0', '1510779000');
INSERT INTO `analysis_gift_consume` VALUES ('302', '20171115', '102', '烟花', '1', '888', '0', '1510779000');
INSERT INTO `analysis_gift_consume` VALUES ('303', '20171115', '106', '龙头', '12', '1056', '0', '1510779000');
INSERT INTO `analysis_gift_consume` VALUES ('304', '20171115', '107', '三岁', '1', '333', '0', '1510779000');
INSERT INTO `analysis_gift_consume` VALUES ('305', '20171116', '5', '金砖', '1', '200', '0', '1510865400');
INSERT INTO `analysis_gift_consume` VALUES ('306', '20171116', '27', '心动', '5', '5', '0', '1510865400');
INSERT INTO `analysis_gift_consume` VALUES ('307', '20171116', '93', '圣诞老人', '1', '2', '0', '1510865400');
INSERT INTO `analysis_gift_consume` VALUES ('308', '20171117', '1', '弹幕', '1', '1', '0', '1510951800');
INSERT INTO `analysis_gift_consume` VALUES ('309', '20171117', '5', '金砖', '1', '200', '0', '1510951800');
INSERT INTO `analysis_gift_consume` VALUES ('310', '20171117', '22', '流星雨', '2', '1960', '0', '1510951800');
INSERT INTO `analysis_gift_consume` VALUES ('311', '20171117', '26', '甲壳虫', '1', '660', '0', '1510951800');
INSERT INTO `analysis_gift_consume` VALUES ('312', '20171117', '92', '口红', '1', '199', '0', '1510951800');
INSERT INTO `analysis_gift_consume` VALUES ('313', '20171117', '93', '圣诞老人', '3', '6', '0', '1510951800');
INSERT INTO `analysis_gift_consume` VALUES ('314', '20171120', '1', '弹幕', '1', '1', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('315', '20171120', '3', '猪头', '2', '4', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('316', '20171120', '5', '金砖', '9', '1800', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('317', '20171120', '6', '留声机', '1', '500', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('318', '20171120', '9', '钻石', '2', '2628', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('319', '20171120', '10', '兰博基尼', '2', '6000', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('320', '20171120', '11', '私人游艇', '1', '8888', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('321', '20171120', '13', '棒棒糖', '1', '1', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('322', '20171120', '14', '小黄鸡', '1', '2', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('323', '20171120', '15', '亲亲', '3', '15', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('324', '20171120', '16', '情书', '2', '20', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('325', '20171120', '18', '飞机', '1', '6666', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('326', '20171120', '19', '私人海岛', '3', '39420', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('327', '20171120', '21', '火箭', '3', '29997', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('328', '20171120', '22', '流星雨', '1', '980', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('329', '20171120', '24', '小萌猪', '1', '0', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('330', '20171120', '26', '甲壳虫', '6', '3960', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('331', '20171120', '29', '守护人气猪', '1', '0', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('332', '20171120', '33', '幸运啤酒', '41', '82', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('333', '20171120', '34', '幸运流氓蕉', '1', '1', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('334', '20171120', '49', '烛光晚餐', '1', '188', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('335', '20171120', '59', '约定今生', '1', '520', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('336', '20171120', '78', '生日蛋糕', '2', '2000', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('337', '20171120', '80', '小色猪', '28', '28', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('338', '20171120', '82', '丘比特之箭', '1', '520', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('339', '20171120', '83', '香吻', '2', '2', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('340', '20171120', '84', '相互伤害', '1', '2', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('341', '20171120', '93', '圣诞老人', '29', '58', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('342', '20171120', '96', '来一炮', '1', '980', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('343', '20171120', '102', '烟花', '2', '1776', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('344', '20171120', '104', '蓝色妖姬', '1', '88', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('345', '20171120', '105', '金豆', '14', '14', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('346', '20171120', '106', '龙头', '14', '1232', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('347', '20171120', '107', '三岁', '1', '333', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('348', '20171120', '108', '飞fdgfg', '3', '19983', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('349', '20171120', '109', '111', '1', '2', '0', '1511211000');
INSERT INTO `analysis_gift_consume` VALUES ('350', '20171121', '5', '金砖', '1', '200', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('351', '20171121', '6', '留声机', '4', '2000', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('352', '20171121', '10', '兰博基尼', '7', '21000', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('353', '20171121', '14', '小黄鸡', '55', '110', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('354', '20171121', '15', '亲亲', '2', '10', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('355', '20171121', '18', '飞机', '4', '26664', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('356', '20171121', '21', '火箭', '3', '29997', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('357', '20171121', '23', '喇叭', '1', '188', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('358', '20171121', '24', '小萌猪', '4', '0', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('359', '20171121', '26', '甲壳虫', '1', '660', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('360', '20171121', '27', '心动', '7', '7', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('361', '20171121', '29', '守护人气猪', '122', '0', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('362', '20171121', '34', '幸运流氓蕉', '1', '1', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('363', '20171121', '45', '骑士守护', '1', '1314', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('364', '20171121', '78', '生日蛋糕', '2', '2000', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('365', '20171121', '80', '小色猪', '35', '35', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('366', '20171121', '81', '鼓掌', '16', '16', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('367', '20171121', '82', '丘比特之箭', '2', '1040', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('368', '20171121', '92', '口红', '300', '59700', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('369', '20171121', '93', '圣诞老人', '3', '6', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('370', '20171121', '96', '来一炮', '1', '980', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('371', '20171121', '102', '烟花', '3', '2664', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('372', '20171121', '105', '金豆', '319', '319', '0', '1511297400');
INSERT INTO `analysis_gift_consume` VALUES ('373', '20171122', '1', '弹幕', '2', '2', '0', '1511383800');
INSERT INTO `analysis_gift_consume` VALUES ('374', '20171122', '3', '猪头', '1', '2', '0', '1511383800');
INSERT INTO `analysis_gift_consume` VALUES ('375', '20171122', '4', '猫咪', '1', '5', '0', '1511383800');
INSERT INTO `analysis_gift_consume` VALUES ('376', '20171122', '18', '飞机', '1', '6666', '0', '1511383800');
INSERT INTO `analysis_gift_consume` VALUES ('377', '20171122', '22', '流星雨', '1', '980', '0', '1511383800');
INSERT INTO `analysis_gift_consume` VALUES ('378', '20171122', '23', '喇叭', '1', '188', '0', '1511383800');
INSERT INTO `analysis_gift_consume` VALUES ('379', '20171122', '24', '小萌猪', '5', '0', '0', '1511383800');
INSERT INTO `analysis_gift_consume` VALUES ('380', '20171122', '29', '守护人气猪', '2', '0', '0', '1511383800');
INSERT INTO `analysis_gift_consume` VALUES ('381', '20171122', '33', '幸运啤酒', '1', '2', '0', '1511383800');
INSERT INTO `analysis_gift_consume` VALUES ('382', '20171122', '78', '生日蛋糕', '7', '7000', '0', '1511383800');
INSERT INTO `analysis_gift_consume` VALUES ('383', '20171122', '92', '口红', '2', '398', '0', '1511383800');
INSERT INTO `analysis_gift_consume` VALUES ('384', '20171122', '93', '圣诞老人', '1', '2', '0', '1511383800');
INSERT INTO `analysis_gift_consume` VALUES ('385', '20171123', '5', '金砖', '316', '63200', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('386', '20171123', '7', '心动', '3', '3', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('387', '20171123', '8', '钻戒', '300', '19800', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('388', '20171123', '9', '钻石', '1', '1314', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('389', '20171123', '10', '兰博基尼', '3', '9000', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('390', '20171123', '13', '棒棒糖', '353', '353', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('391', '20171123', '21', '火箭', '1', '9999', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('392', '20171123', '22', '流星雨', '2', '1960', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('393', '20171123', '23', '喇叭', '1', '188', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('394', '20171123', '24', '小萌猪', '14', '0', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('395', '20171123', '26', '甲壳虫', '1', '660', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('396', '20171123', '27', '心动', '5', '5', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('397', '20171123', '29', '守护人气猪', '12', '0', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('398', '20171123', '34', '幸运流氓蕉', '175', '175', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('399', '20171123', '45', '骑士守护', '2', '2628', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('400', '20171123', '46', '王子守护', '1', '13140', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('401', '20171123', '49', '烛光晚餐', '1', '188', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('402', '20171123', '78', '生日蛋糕', '11', '11000', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('403', '20171123', '80', '小色猪', '25', '25', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('404', '20171123', '93', '圣诞老人', '1', '2', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('405', '20171123', '96', '来一炮', '1', '980', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('406', '20171123', '105', '金豆', '5178', '5178', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('407', '20171123', '106', '龙头', '5', '440', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('408', '20171123', '107', '三岁', '2', '666', '0', '1511470200');
INSERT INTO `analysis_gift_consume` VALUES ('409', '20171124', '1', '弹幕', '1', '1', '0', '1511556600');
INSERT INTO `analysis_gift_consume` VALUES ('410', '20171124', '5', '金砖', '5', '1000', '0', '1511556600');
INSERT INTO `analysis_gift_consume` VALUES ('411', '20171124', '6', '留声机', '7', '3500', '0', '1511556600');
INSERT INTO `analysis_gift_consume` VALUES ('412', '20171124', '8', '钻戒', '6', '396', '0', '1511556600');
INSERT INTO `analysis_gift_consume` VALUES ('413', '20171124', '13', '棒棒糖', '32', '32', '0', '1511556600');
INSERT INTO `analysis_gift_consume` VALUES ('414', '20171124', '22', '流星雨', '4', '3920', '0', '1511556600');
INSERT INTO `analysis_gift_consume` VALUES ('415', '20171124', '24', '小萌猪', '7', '0', '0', '1511556600');
INSERT INTO `analysis_gift_consume` VALUES ('416', '20171124', '34', '幸运流氓蕉', '3', '3', '0', '1511556600');
INSERT INTO `analysis_gift_consume` VALUES ('417', '20171124', '40', '鹊桥', '1', '7', '0', '1511556600');
INSERT INTO `analysis_gift_consume` VALUES ('418', '20171124', '83', '香吻', '14', '14', '0', '1511556600');
INSERT INTO `analysis_gift_consume` VALUES ('419', '20171124', '105', '金豆', '10', '10', '0', '1511556600');
INSERT INTO `analysis_gift_consume` VALUES ('420', '20171125', '4', '猫咪', '3', '15', '0', '1511643000');
INSERT INTO `analysis_gift_consume` VALUES ('421', '20171125', '5', '金砖', '1', '200', '0', '1511643000');
INSERT INTO `analysis_gift_consume` VALUES ('422', '20171125', '6', '留声机', '6', '3000', '0', '1511643000');
INSERT INTO `analysis_gift_consume` VALUES ('423', '20171125', '10', '兰博基尼', '1', '3000', '0', '1511643000');
INSERT INTO `analysis_gift_consume` VALUES ('424', '20171125', '24', '小萌猪', '1', '0', '0', '1511643000');
INSERT INTO `analysis_gift_consume` VALUES ('425', '20171125', '34', '幸运流氓蕉', '1', '1', '0', '1511643000');
INSERT INTO `analysis_gift_consume` VALUES ('426', '20171125', '40', '鹊桥', '1', '7', '0', '1511643000');
INSERT INTO `analysis_gift_consume` VALUES ('427', '20171125', '106', '龙头', '1', '88', '0', '1511643000');
INSERT INTO `analysis_gift_consume` VALUES ('428', '20171126', '1', '弹幕', '2', '2', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('429', '20171126', '2', '玫瑰花', '1', '1', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('430', '20171126', '5', '金砖', '242', '48400', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('431', '20171126', '6', '留声机', '101', '50500', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('432', '20171126', '9', '钻石', '2', '2628', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('433', '20171126', '10', '兰博基尼', '2', '6000', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('434', '20171126', '15', '亲亲', '3', '15', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('435', '20171126', '18', '飞机', '1', '6666', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('436', '20171126', '22', '流星雨', '99', '97020', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('437', '20171126', '26', '甲壳虫', '2', '1320', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('438', '20171126', '27', '心动', '2', '2', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('439', '20171126', '29', '守护人气猪', '9', '0', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('440', '20171126', '34', '幸运流氓蕉', '1342', '1342', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('441', '20171126', '40', '鹊桥', '5', '35', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('442', '20171126', '46', '王子守护', '1', '13140', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('443', '20171126', '80', '小色猪', '28', '28', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('444', '20171126', '82', '丘比特之箭', '4', '2080', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('445', '20171126', '83', '香吻', '2', '2', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('446', '20171126', '93', '圣诞老人', '4', '8', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('447', '20171126', '96', '来一炮', '1', '980', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('448', '20171126', '102', '烟花', '2', '1776', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('449', '20171126', '104', '蓝色妖姬', '2', '176', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('450', '20171126', '105', '金豆', '11314', '11314', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('451', '20171126', '106', '龙头', '11', '968', '0', '1511729400');
INSERT INTO `analysis_gift_consume` VALUES ('452', '20171127', '5', '金砖', '2', '400', '0', '1511815800');
INSERT INTO `analysis_gift_consume` VALUES ('453', '20171127', '13', '棒棒糖', '50', '50', '0', '1511815800');
INSERT INTO `analysis_gift_consume` VALUES ('454', '20171127', '22', '流星雨', '1', '980', '0', '1511815800');
INSERT INTO `analysis_gift_consume` VALUES ('455', '20171127', '24', '小萌猪', '1', '0', '0', '1511815800');
INSERT INTO `analysis_gift_consume` VALUES ('456', '20171128', '5', '金砖', '10', '2000', '0', '1511902200');
INSERT INTO `analysis_gift_consume` VALUES ('457', '20171128', '22', '流星雨', '3', '2940', '0', '1511902200');
INSERT INTO `analysis_gift_consume` VALUES ('458', '20171128', '24', '小萌猪', '1', '0', '0', '1511902200');
INSERT INTO `analysis_gift_consume` VALUES ('459', '20171128', '105', '金豆', '2', '2', '0', '1511902200');
INSERT INTO `analysis_gift_consume` VALUES ('460', '20171128', '106', '龙头', '1', '88', '0', '1511902200');
INSERT INTO `analysis_gift_consume` VALUES ('461', '20171130', '5', '金砖', '3', '600', '0', '1512075000');
INSERT INTO `analysis_gift_consume` VALUES ('462', '20171130', '18', '飞机', '1', '6666', '0', '1512075000');
INSERT INTO `analysis_gift_consume` VALUES ('463', '20171130', '22', '流星雨', '2', '1960', '0', '1512075000');
INSERT INTO `analysis_gift_consume` VALUES ('464', '20171130', '24', '小萌猪', '1', '0', '0', '1512075000');
INSERT INTO `analysis_gift_consume` VALUES ('465', '20171130', '34', '幸运流氓蕉', '1', '1', '0', '1512075000');
INSERT INTO `analysis_gift_consume` VALUES ('466', '20171130', '46', '王子守护', '12', '157680', '0', '1512075000');
INSERT INTO `analysis_gift_consume` VALUES ('467', '20171130', '80', '小色猪', '3344', '3344', '0', '1512075000');
INSERT INTO `analysis_gift_consume` VALUES ('468', '20171130', '105', '金豆', '3', '3', '0', '1512075000');
INSERT INTO `analysis_gift_consume` VALUES ('469', '20171130', '106', '龙头', '4', '352', '0', '1512075000');
INSERT INTO `analysis_gift_consume` VALUES ('470', '20171201', '1', '弹幕', '2', '2', '0', '1512161400');
INSERT INTO `analysis_gift_consume` VALUES ('471', '20171202', '105', '金豆', '1', '1', '0', '1512247800');
INSERT INTO `analysis_gift_consume` VALUES ('472', '20171204', '1', '弹幕', '5', '5', '0', '1512420600');
INSERT INTO `analysis_gift_consume` VALUES ('473', '20171204', '23', '喇叭', '5', '940', '0', '1512420600');
INSERT INTO `analysis_gift_consume` VALUES ('474', '20171205', '1', '弹幕', '2', '2', '0', '1512507000');
INSERT INTO `analysis_gift_consume` VALUES ('475', '20171205', '5', '金砖', '1', '200', '0', '1512507000');
INSERT INTO `analysis_gift_consume` VALUES ('476', '20171205', '10', '兰博基尼', '1', '3000', '0', '1512507000');
INSERT INTO `analysis_gift_consume` VALUES ('477', '20171205', '13', '棒棒糖', '5', '5', '0', '1512507000');
INSERT INTO `analysis_gift_consume` VALUES ('478', '20171205', '23', '喇叭', '3', '564', '0', '1512507000');
INSERT INTO `analysis_gift_consume` VALUES ('479', '20171205', '34', '幸运流氓蕉', '23', '23', '0', '1512507000');
INSERT INTO `analysis_gift_consume` VALUES ('480', '20171205', '45', '骑士守护', '1', '1314', '0', '1512507000');
INSERT INTO `analysis_gift_consume` VALUES ('481', '20171205', '80', '小色猪', '1', '1', '0', '1512507000');
INSERT INTO `analysis_gift_consume` VALUES ('482', '20171205', '93', '圣诞老人', '2', '4', '0', '1512507000');
INSERT INTO `analysis_gift_consume` VALUES ('483', '20171205', '106', '龙头', '7', '616', '0', '1512507000');
INSERT INTO `analysis_gift_consume` VALUES ('484', '20171206', '6', '留声机', '1', '500', '0', '1512593400');
INSERT INTO `analysis_gift_consume` VALUES ('485', '20171206', '80', '小色猪', '36', '36', '0', '1512593400');
INSERT INTO `analysis_gift_consume` VALUES ('486', '20171206', '106', '龙头', '3', '264', '0', '1512593400');
INSERT INTO `analysis_gift_consume` VALUES ('487', '20171207', '13', '棒棒糖', '2', '2', '0', '1512679800');
INSERT INTO `analysis_gift_consume` VALUES ('488', '20171207', '34', '幸运流氓蕉', '1', '1', '0', '1512679800');
INSERT INTO `analysis_gift_consume` VALUES ('489', '20171208', '10', '兰博基尼', '1', '3000', '0', '1512766200');
INSERT INTO `analysis_gift_consume` VALUES ('490', '20171208', '18', '飞机', '1', '6666', '0', '1512766200');
INSERT INTO `analysis_gift_consume` VALUES ('491', '20171208', '19', '私人海岛', '1', '13140', '0', '1512766200');
INSERT INTO `analysis_gift_consume` VALUES ('492', '20171208', '24', '小萌猪', '1', '0', '0', '1512766200');
INSERT INTO `analysis_gift_consume` VALUES ('493', '20171208', '34', '幸运流氓蕉', '1', '1', '0', '1512766200');
INSERT INTO `analysis_gift_consume` VALUES ('494', '20171208', '45', '白银守护', '24', '31536', '0', '1512766200');
INSERT INTO `analysis_gift_consume` VALUES ('495', '20171208', '46', '黄金守护', '20', '262800', '0', '1512766200');
INSERT INTO `analysis_gift_consume` VALUES ('496', '20171208', '80', '小色猪', '9999', '9999', '0', '1512766200');
INSERT INTO `analysis_gift_consume` VALUES ('497', '20171208', '105', '金豆', '29999', '29999', '0', '1512766200');
INSERT INTO `analysis_gift_consume` VALUES ('498', '20171209', '5', '金砖', '306', '61200', '0', '1512852600');
INSERT INTO `analysis_gift_consume` VALUES ('499', '20171209', '13', '棒棒糖', '26281', '26281', '0', '1512852600');
INSERT INTO `analysis_gift_consume` VALUES ('500', '20171209', '34', '幸运流氓蕉', '15770', '15770', '0', '1512852600');
INSERT INTO `analysis_gift_consume` VALUES ('501', '20171209', '92', '口红', '3944', '784856', '0', '1512852600');
INSERT INTO `analysis_gift_consume` VALUES ('502', '20171209', '93', '圣诞老人', '9999', '19998', '0', '1512852600');
INSERT INTO `analysis_gift_consume` VALUES ('503', '20171209', '105', '金豆', '34971', '34971', '0', '1512852600');
INSERT INTO `analysis_gift_consume` VALUES ('504', '20171210', '4', '猫咪', '1', '5', '0', '1512939000');
INSERT INTO `analysis_gift_consume` VALUES ('505', '20171210', '24', '小萌猪', '1', '0', '0', '1512939000');
INSERT INTO `analysis_gift_consume` VALUES ('506', '20171210', '29', '守护人气猪', '1', '0', '0', '1512939000');
INSERT INTO `analysis_gift_consume` VALUES ('507', '20171210', '34', '幸运流氓蕉', '1', '1', '0', '1512939000');
INSERT INTO `analysis_gift_consume` VALUES ('508', '20171210', '45', '白银守护', '1', '1314', '0', '1512939000');
INSERT INTO `analysis_gift_consume` VALUES ('509', '20171210', '46', '黄金守护', '2', '26280', '0', '1512939000');
INSERT INTO `analysis_gift_consume` VALUES ('510', '20171211', '18', '飞机', '1', '6666', '0', '1513025400');
INSERT INTO `analysis_gift_consume` VALUES ('511', '20171212', '5', '金砖', '1', '200', '0', '1513111800');
INSERT INTO `analysis_gift_consume` VALUES ('512', '20171212', '6', '留声机', '1', '500', '0', '1513111800');
INSERT INTO `analysis_gift_consume` VALUES ('513', '20171212', '13', '棒棒糖', '2', '2', '0', '1513111800');
INSERT INTO `analysis_gift_consume` VALUES ('514', '20171212', '22', '流星雨', '2', '1960', '0', '1513111800');
INSERT INTO `analysis_gift_consume` VALUES ('515', '20171212', '26', '甲壳虫', '1', '660', '0', '1513111800');
INSERT INTO `analysis_gift_consume` VALUES ('516', '20171212', '33', '幸运啤酒', '1', '2', '0', '1513111800');
INSERT INTO `analysis_gift_consume` VALUES ('517', '20171212', '34', '幸运流氓蕉', '3', '3', '0', '1513111800');
INSERT INTO `analysis_gift_consume` VALUES ('518', '20171212', '73', '奋斗', '6', '6', '0', '1513111800');
INSERT INTO `analysis_gift_consume` VALUES ('519', '20171212', '107', '三岁', '1', '333', '0', '1513111800');
INSERT INTO `analysis_gift_consume` VALUES ('520', '20171213', '4', '猫咪', '1', '5', '0', '1513198200');
INSERT INTO `analysis_gift_consume` VALUES ('521', '20171213', '5', '金砖', '1', '200', '0', '1513198200');
INSERT INTO `analysis_gift_consume` VALUES ('522', '20171213', '6', '留声机', '1', '500', '0', '1513198200');
INSERT INTO `analysis_gift_consume` VALUES ('523', '20171213', '7', '心动', '1', '1', '0', '1513198200');
INSERT INTO `analysis_gift_consume` VALUES ('524', '20171213', '10', '兰博基尼', '1', '3000', '0', '1513198200');
INSERT INTO `analysis_gift_consume` VALUES ('525', '20171213', '16', '情书', '1', '10', '0', '1513198200');
INSERT INTO `analysis_gift_consume` VALUES ('526', '20171213', '33', '幸运啤酒', '2', '4', '0', '1513198200');
INSERT INTO `analysis_gift_consume` VALUES ('527', '20171213', '104', '蓝色妖姬', '1', '88', '0', '1513198200');
INSERT INTO `analysis_gift_consume` VALUES ('528', '20171215', '5', '金砖', '3', '600', '0', '1513371000');
INSERT INTO `analysis_gift_consume` VALUES ('529', '20171215', '24', '小萌猪', '1', '0', '0', '1513371000');
INSERT INTO `analysis_gift_consume` VALUES ('530', '20171215', '92', '口红', '1', '199', '0', '1513371000');
INSERT INTO `analysis_gift_consume` VALUES ('531', '20171220', '93', '圣诞老人', '1', '2', '0', '1513803000');
INSERT INTO `analysis_gift_consume` VALUES ('532', '20171221', '1', '弹幕', '2', '2', '0', '1513889400');
INSERT INTO `analysis_gift_consume` VALUES ('533', '20171221', '5', '金砖', '5', '1000', '0', '1513889400');
INSERT INTO `analysis_gift_consume` VALUES ('534', '20171221', '9', '钻石', '1', '1314', '0', '1513889400');
INSERT INTO `analysis_gift_consume` VALUES ('535', '20171221', '10', '兰博基尼', '1', '3000', '0', '1513889400');
INSERT INTO `analysis_gift_consume` VALUES ('536', '20171221', '11', '私人游艇', '2', '17776', '0', '1513889400');
INSERT INTO `analysis_gift_consume` VALUES ('537', '20171221', '13', '棒棒糖', '35', '35', '0', '1513889400');
INSERT INTO `analysis_gift_consume` VALUES ('538', '20171221', '22', '流星雨', '1', '980', '0', '1513889400');
INSERT INTO `analysis_gift_consume` VALUES ('539', '20171221', '23', '喇叭', '4', '752', '0', '1513889400');
INSERT INTO `analysis_gift_consume` VALUES ('540', '20171221', '93', '圣诞老人', '1', '2', '0', '1513889400');
INSERT INTO `analysis_gift_consume` VALUES ('541', '20171221', '96', '来一炮', '2', '1960', '0', '1513889400');
INSERT INTO `analysis_gift_consume` VALUES ('542', '20171221', '106', '龙头', '3', '264', '0', '1513889400');
INSERT INTO `analysis_gift_consume` VALUES ('543', '20171222', '1', '弹幕', '2', '2', '0', '1513975800');
INSERT INTO `analysis_gift_consume` VALUES ('544', '20171222', '5', '金砖', '9', '1800', '0', '1513975800');
INSERT INTO `analysis_gift_consume` VALUES ('545', '20171222', '19', '私人海岛', '1', '13140', '0', '1513975800');
INSERT INTO `analysis_gift_consume` VALUES ('546', '20171222', '22', '流星雨', '4', '3920', '0', '1513975800');
INSERT INTO `analysis_gift_consume` VALUES ('547', '20171222', '84', '相互伤害', '1', '2', '0', '1513975800');
INSERT INTO `analysis_gift_consume` VALUES ('548', '20171225', '13', '棒棒糖', '1314', '1314', '0', '1514235000');
INSERT INTO `analysis_gift_consume` VALUES ('549', '20171226', '1', '弹幕', '11', '11', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('550', '20171226', '5', '金砖', '10', '2000', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('551', '20171226', '6', '留声机', '2', '1000', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('552', '20171226', '8', '钻戒', '1', '66', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('553', '20171226', '9', '钻石', '3', '3942', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('554', '20171226', '11', '私人游艇', '1', '8888', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('555', '20171226', '19', '私人海岛', '7', '91980', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('556', '20171226', '22', '流星雨', '9', '8820', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('557', '20171226', '23', '喇叭', '3', '564', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('558', '20171226', '26', '甲壳虫', '1', '660', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('559', '20171226', '34', '幸运流氓蕉', '12', '12', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('560', '20171226', '49', '烛光晚餐', '1', '188', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('561', '20171226', '59', '约定今生', '9', '4680', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('562', '20171226', '73', '奋斗', '1', '1', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('563', '20171226', '78', '生日蛋糕', '1', '1000', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('564', '20171226', '84', '相互伤害', '66', '132', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('565', '20171226', '93', '圣诞老人', '7', '14', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('566', '20171226', '106', '龙头', '1', '88', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('567', '20171226', '107', '三岁', '1', '333', '0', '1514321400');
INSERT INTO `analysis_gift_consume` VALUES ('568', '20171227', '1', '弹幕', '1', '1', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('569', '20171227', '5', '金砖', '4', '800', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('570', '20171227', '9', '钻石', '1', '1314', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('571', '20171227', '10', '兰博基尼', '3', '9000', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('572', '20171227', '11', '私人游艇', '18', '159984', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('573', '20171227', '13', '棒棒糖', '50', '50', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('574', '20171227', '14', '小黄鸡', '9', '18', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('575', '20171227', '15', '亲亲', '40', '200', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('576', '20171227', '21', '火箭', '3', '29997', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('577', '20171227', '22', '流星雨', '11', '10780', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('578', '20171227', '26', '甲壳虫', '10', '6600', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('579', '20171227', '34', '幸运流氓蕉', '18', '18', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('580', '20171227', '49', '烛光晚餐', '1', '188', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('581', '20171227', '59', '约定今生', '2', '1040', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('582', '20171227', '78', '生日蛋糕', '6', '6000', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('583', '20171227', '83', '香吻', '22', '22', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('584', '20171227', '91', '圣诞雪橇', '1', '300', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('585', '20171227', '93', '圣诞老人', '15', '30', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('586', '20171227', '102', '烟花', '1', '888', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('587', '20171227', '104', '蓝色妖姬', '2', '176', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('588', '20171227', '105', '金豆', '2659', '2659', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('589', '20171227', '106', '龙头', '7', '616', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('590', '20171227', '107', '三岁', '2', '666', '0', '1514407800');
INSERT INTO `analysis_gift_consume` VALUES ('591', '20171228', '1', '弹幕', '5', '5', '0', '1514494200');
INSERT INTO `analysis_gift_consume` VALUES ('592', '20171228', '5', '金砖', '3', '600', '0', '1514494200');
INSERT INTO `analysis_gift_consume` VALUES ('593', '20171228', '6', '留声机', '7', '3500', '0', '1514494200');
INSERT INTO `analysis_gift_consume` VALUES ('594', '20171228', '9', '钻石', '1', '1314', '0', '1514494200');
INSERT INTO `analysis_gift_consume` VALUES ('595', '20171228', '22', '流星雨', '32', '31360', '0', '1514494200');
INSERT INTO `analysis_gift_consume` VALUES ('596', '20171228', '23', '喇叭', '5', '940', '0', '1514494200');
INSERT INTO `analysis_gift_consume` VALUES ('597', '20171228', '34', '幸运流氓蕉', '4', '4', '0', '1514494200');
INSERT INTO `analysis_gift_consume` VALUES ('598', '20171228', '78', '生日蛋糕', '2', '2000', '0', '1514494200');
INSERT INTO `analysis_gift_consume` VALUES ('599', '20171228', '80', '小色猪', '5', '5', '0', '1514494200');
INSERT INTO `analysis_gift_consume` VALUES ('600', '20171228', '82', '丘比特之箭', '2', '1040', '0', '1514494200');
INSERT INTO `analysis_gift_consume` VALUES ('601', '20171228', '83', '香吻', '5', '5', '0', '1514494200');
INSERT INTO `analysis_gift_consume` VALUES ('602', '20171228', '96', '来一炮', '1', '980', '0', '1514494200');
INSERT INTO `analysis_gift_consume` VALUES ('603', '20171228', '105', '金豆', '1', '1', '0', '1514494200');
INSERT INTO `analysis_gift_consume` VALUES ('604', '20171228', '107', '三岁', '1', '333', '0', '1514494200');
INSERT INTO `analysis_gift_consume` VALUES ('605', '20171229', '13', '棒棒糖', '28', '28', '0', '1514580600');
INSERT INTO `analysis_gift_consume` VALUES ('606', '20171229', '22', '流星雨', '1', '980', '0', '1514580600');
INSERT INTO `analysis_gift_consume` VALUES ('607', '20171229', '46', '黄金守护', '3', '39420', '0', '1514580600');
INSERT INTO `analysis_gift_consume` VALUES ('608', '20171229', '80', '小色猪', '2', '2', '0', '1514580600');
INSERT INTO `analysis_gift_consume` VALUES ('609', '20171229', '83', '香吻', '1', '1', '0', '1514580600');
INSERT INTO `analysis_gift_consume` VALUES ('610', '20171229', '105', '金豆', '29', '29', '0', '1514580600');
INSERT INTO `analysis_gift_consume` VALUES ('611', '20180102', '5', '金砖', '1', '200', '0', '1514926200');
INSERT INTO `analysis_gift_consume` VALUES ('612', '20180102', '10', '兰博基尼', '1', '3000', '0', '1514926200');
INSERT INTO `analysis_gift_consume` VALUES ('613', '20180102', '13', '棒棒糖', '4', '4', '0', '1514926200');
INSERT INTO `analysis_gift_consume` VALUES ('614', '20180102', '19', '私人海岛', '1', '13140', '0', '1514926200');
INSERT INTO `analysis_gift_consume` VALUES ('615', '20180102', '21', '火箭', '1', '9999', '0', '1514926200');
INSERT INTO `analysis_gift_consume` VALUES ('616', '20180102', '22', '流星雨', '2', '1960', '0', '1514926200');
INSERT INTO `analysis_gift_consume` VALUES ('617', '20180102', '26', '甲壳虫', '1', '660', '0', '1514926200');
INSERT INTO `analysis_gift_consume` VALUES ('618', '20180102', '49', '烛光晚餐', '1', '188', '0', '1514926200');
INSERT INTO `analysis_gift_consume` VALUES ('619', '20180102', '102', '烟花', '1', '888', '0', '1514926200');
INSERT INTO `analysis_gift_consume` VALUES ('620', '20180102', '104', '蓝色妖姬', '1', '88', '0', '1514926200');
INSERT INTO `analysis_gift_consume` VALUES ('621', '20180102', '105', '金豆', '2', '2', '0', '1514926200');
INSERT INTO `analysis_gift_consume` VALUES ('622', '20180102', '106', '龙头', '1', '88', '0', '1514926200');
INSERT INTO `analysis_gift_consume` VALUES ('623', '20180102', '107', '三岁', '1', '333', '0', '1514926200');
INSERT INTO `analysis_gift_consume` VALUES ('624', '20180103', '1', '弹幕', '8', '8', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('625', '20180103', '2', '玫瑰花', '4', '4', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('626', '20180103', '5', '金砖', '1', '200', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('627', '20180103', '6', '留声机', '103', '51500', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('628', '20180103', '7', '心动', '16', '16', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('629', '20180103', '11', '私人游艇', '1', '8888', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('630', '20180103', '21', '火箭', '1', '9999', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('631', '20180103', '22', '流星雨', '1', '980', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('632', '20180103', '26', '甲壳虫', '15', '9900', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('633', '20180103', '29', '守护人气猪', '18', '0', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('634', '20180103', '34', '幸运流氓蕉', '3', '3', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('635', '20180103', '59', '约定今生', '2', '1040', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('636', '20180103', '78', '生日蛋糕', '4', '4000', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('637', '20180103', '91', '圣诞雪橇', '1', '300', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('638', '20180103', '102', '烟花', '1', '888', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('639', '20180103', '104', '蓝色妖姬', '1', '88', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('640', '20180103', '105', '金豆', '6', '6', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('641', '20180103', '107', '三岁', '1', '333', '0', '1515012600');
INSERT INTO `analysis_gift_consume` VALUES ('642', '20180104', '10', '兰博基尼', '1', '3000', '0', '1515099000');
INSERT INTO `analysis_gift_consume` VALUES ('643', '20180104', '22', '流星雨', '1', '980', '0', '1515099000');
INSERT INTO `analysis_gift_consume` VALUES ('644', '20180104', '26', '甲壳虫', '1', '660', '0', '1515099000');
INSERT INTO `analysis_gift_consume` VALUES ('645', '20180105', '34', '幸运流氓蕉', '1', '1', '0', '1515185400');
INSERT INTO `analysis_gift_consume` VALUES ('646', '20180108', '5', '金砖', '1', '200', '0', '1515444600');
INSERT INTO `analysis_gift_consume` VALUES ('647', '20180108', '13', '棒棒糖', '1', '1', '0', '1515444600');
INSERT INTO `analysis_gift_consume` VALUES ('648', '20180108', '105', '金豆', '4', '4', '0', '1515444600');
INSERT INTO `analysis_gift_consume` VALUES ('649', '20180109', '13', '棒棒糖', '1', '1', '0', '1515531000');
INSERT INTO `analysis_gift_consume` VALUES ('650', '20180109', '34', '幸运流氓蕉', '1', '1', '0', '1515531000');
INSERT INTO `analysis_gift_consume` VALUES ('651', '20180109', '45', '白银守护', '3', '3942', '0', '1515531000');
INSERT INTO `analysis_gift_consume` VALUES ('652', '20180109', '84', '相互伤害', '1', '2', '0', '1515531000');
INSERT INTO `analysis_gift_consume` VALUES ('653', '20180109', '105', '金豆', '1', '1', '0', '1515531000');
INSERT INTO `analysis_gift_consume` VALUES ('654', '20180110', '1', '弹幕', '5', '5', '0', '1515617400');
INSERT INTO `analysis_gift_consume` VALUES ('655', '20180110', '5', '金砖', '1', '200', '0', '1515617400');
INSERT INTO `analysis_gift_consume` VALUES ('656', '20180110', '13', '棒棒糖', '5', '5', '0', '1515617400');
INSERT INTO `analysis_gift_consume` VALUES ('657', '20180110', '22', '流星雨', '2', '1960', '0', '1515617400');
INSERT INTO `analysis_gift_consume` VALUES ('658', '20180110', '23', '喇叭', '3', '564', '0', '1515617400');
INSERT INTO `analysis_gift_consume` VALUES ('659', '20180110', '26', '甲壳虫', '3', '1980', '0', '1515617400');
INSERT INTO `analysis_gift_consume` VALUES ('660', '20180110', '45', '白银守护', '4', '5256', '0', '1515617400');
INSERT INTO `analysis_gift_consume` VALUES ('661', '20180110', '80', '小色猪', '1', '1', '0', '1515617400');
INSERT INTO `analysis_gift_consume` VALUES ('662', '20180110', '83', '香吻', '1', '1', '0', '1515617400');
INSERT INTO `analysis_gift_consume` VALUES ('663', '20180110', '105', '金豆', '4', '4', '0', '1515617400');
INSERT INTO `analysis_gift_consume` VALUES ('664', '20180111', '1', '弹幕', '8', '8', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('665', '20180111', '5', '金砖', '29', '5800', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('666', '20180111', '6', '留声机', '2', '1000', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('667', '20180111', '9', '钻石', '1', '1314', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('668', '20180111', '13', '棒棒糖', '23', '23', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('669', '20180111', '19', '私人海岛', '2', '26280', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('670', '20180111', '22', '流星雨', '4', '3920', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('671', '20180111', '23', '喇叭', '6', '1128', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('672', '20180111', '26', '甲壳虫', '7', '4620', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('673', '20180111', '34', '幸运流氓蕉', '4', '4', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('674', '20180111', '59', '约定今生', '2', '1040', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('675', '20180111', '78', '生日蛋糕', '1', '1000', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('676', '20180111', '80', '小色猪', '2', '2', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('677', '20180111', '82', '丘比特之箭', '1', '520', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('678', '20180111', '102', '烟花', '1', '888', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('679', '20180111', '104', '蓝色妖姬', '2', '176', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('680', '20180111', '105', '金豆', '520', '520', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('681', '20180111', '107', '三岁', '2', '666', '0', '1515703800');
INSERT INTO `analysis_gift_consume` VALUES ('682', '20180112', '2', '玫瑰花', '3', '3', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('683', '20180112', '3', '猪头', '2', '4', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('684', '20180112', '4', '猫咪', '2', '10', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('685', '20180112', '5', '金砖', '6029', '1205800', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('686', '20180112', '6', '留声机', '402', '201000', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('687', '20180112', '7', '心动', '2', '2', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('688', '20180112', '8', '钻戒', '40', '2640', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('689', '20180112', '9', '钻石', '52', '68328', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('690', '20180112', '10', '兰博基尼', '10', '30000', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('691', '20180112', '11', '私人游艇', '17', '151096', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('692', '20180112', '13', '棒棒糖', '1404', '1404', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('693', '20180112', '14', '小黄鸡', '2', '4', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('694', '20180112', '15', '亲亲', '1', '5', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('695', '20180112', '16', '情书', '4', '40', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('696', '20180112', '18', '飞机', '5', '33330', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('697', '20180112', '19', '私人海岛', '112', '1471680', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('698', '20180112', '21', '火箭', '5', '49995', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('699', '20180112', '22', '流星雨', '6', '5880', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('700', '20180112', '23', '喇叭', '1', '188', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('701', '20180112', '26', '甲壳虫', '533', '351780', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('702', '20180112', '32', '幸运玫瑰', '4', '4', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('703', '20180112', '33', '幸运啤酒', '2', '4', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('704', '20180112', '34', '幸运流氓蕉', '1824', '1824', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('705', '20180112', '45', '白银守护', '1', '1314', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('706', '20180112', '46', '黄金守护', '1', '13140', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('707', '20180112', '49', '烛光晚餐', '3', '564', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('708', '20180112', '59', '约定今生', '5', '2600', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('709', '20180112', '73', '奋斗', '4', '4', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('710', '20180112', '78', '生日蛋糕', '7', '7000', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('711', '20180112', '80', '小色猪', '5', '5', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('712', '20180112', '81', '鼓掌', '4', '4', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('713', '20180112', '82', '丘比特之箭', '4', '2080', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('714', '20180112', '83', '香吻', '10005', '10005', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('715', '20180112', '84', '相互伤害', '2', '4', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('716', '20180112', '91', '圣诞雪橇', '12', '3600', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('717', '20180112', '92', '口红', '1', '199', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('718', '20180112', '96', '来一炮', '3', '2940', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('719', '20180112', '102', '烟花', '8', '7104', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('720', '20180112', '104', '蓝色妖姬', '4', '352', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('721', '20180112', '105', '金豆', '31657', '31657', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('722', '20180112', '106', '龙头', '10', '880', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('723', '20180112', '107', '三岁', '2', '666', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('724', '20180112', '108', '飞fdgfg', '1', '6661', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('725', '20180112', '109', '111', '2', '4', '0', '1515790200');
INSERT INTO `analysis_gift_consume` VALUES ('726', '20180115', '5', '金砖', '6', '1200', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('727', '20180115', '6', '留声机', '1', '500', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('728', '20180115', '8', '钻戒', '300', '19800', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('729', '20180115', '10', '兰博基尼', '1', '3000', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('730', '20180115', '11', '私人游艇', '2', '17776', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('731', '20180115', '13', '棒棒糖', '4424', '4424', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('732', '20180115', '19', '私人海岛', '1', '13140', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('733', '20180115', '22', '流星雨', '2', '1960', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('734', '20180115', '26', '甲壳虫', '11', '7260', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('735', '20180115', '34', '幸运流氓蕉', '1008', '1008', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('736', '20180115', '40', '鹊桥', '8', '56', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('737', '20180115', '46', '黄金守护', '1', '13140', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('738', '20180115', '49', '烛光晚餐', '1', '188', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('739', '20180115', '78', '生日蛋糕', '2', '2000', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('740', '20180115', '81', '鼓掌', '300', '300', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('741', '20180115', '91', '圣诞雪橇', '4', '1200', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('742', '20180115', '92', '口红', '2', '398', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('743', '20180115', '105', '金豆', '612', '612', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('744', '20180115', '106', '龙头', '7', '616', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('745', '20180115', '107', '三岁', '2', '666', '0', '1516049400');
INSERT INTO `analysis_gift_consume` VALUES ('746', '20180118', '5', '金砖', '39', '7800', '0', '1516308600');
INSERT INTO `analysis_gift_consume` VALUES ('747', '20180118', '6', '留声机', '300', '150000', '0', '1516308600');
INSERT INTO `analysis_gift_consume` VALUES ('748', '20180118', '11', '私人游艇', '3', '26664', '0', '1516308600');
INSERT INTO `analysis_gift_consume` VALUES ('749', '20180118', '13', '棒棒糖', '4', '4', '0', '1516308600');
INSERT INTO `analysis_gift_consume` VALUES ('750', '20180118', '22', '流星雨', '8', '7840', '0', '1516308600');
INSERT INTO `analysis_gift_consume` VALUES ('751', '20180118', '26', '甲壳虫', '26', '17160', '0', '1516308600');
INSERT INTO `analysis_gift_consume` VALUES ('752', '20180118', '29', '守护人气猪', '1', '0', '0', '1516308600');
INSERT INTO `analysis_gift_consume` VALUES ('753', '20180118', '34', '幸运流氓蕉', '20', '20', '0', '1516308600');
INSERT INTO `analysis_gift_consume` VALUES ('754', '20180118', '45', '白银守护', '1', '1314', '0', '1516308600');
INSERT INTO `analysis_gift_consume` VALUES ('755', '20180118', '46', '黄金守护', '2', '26280', '0', '1516308600');
INSERT INTO `analysis_gift_consume` VALUES ('756', '20180118', '78', '生日蛋糕', '3', '3000', '0', '1516308600');
INSERT INTO `analysis_gift_consume` VALUES ('757', '20180118', '80', '小色猪', '1', '1', '0', '1516308600');
INSERT INTO `analysis_gift_consume` VALUES ('758', '20180118', '91', '圣诞雪橇', '5', '1500', '0', '1516308600');
INSERT INTO `analysis_gift_consume` VALUES ('759', '20180118', '96', '来一炮', '1', '980', '0', '1516308600');
INSERT INTO `analysis_gift_consume` VALUES ('760', '20180118', '105', '金豆', '1993', '1993', '0', '1516308600');
INSERT INTO `analysis_gift_consume` VALUES ('761', '20180118', '106', '龙头', '1', '88', '0', '1516308600');
INSERT INTO `analysis_gift_consume` VALUES ('799', '20180121', '5', '金砖', '1', '200', '0', '1516654200');
INSERT INTO `analysis_gift_consume` VALUES ('800', '20180121', '9', '钻石', '2', '2628', '0', '1516654200');
INSERT INTO `analysis_gift_consume` VALUES ('801', '20180121', '13', '棒棒糖', '2634', '2634', '0', '1516654200');
INSERT INTO `analysis_gift_consume` VALUES ('802', '20180121', '19', '私人海岛', '1', '13140', '0', '1516654200');
INSERT INTO `analysis_gift_consume` VALUES ('803', '20180121', '80', '小色猪', '2628', '2628', '0', '1516654200');
INSERT INTO `analysis_gift_consume` VALUES ('804', '20180122', '6', '留声机', '2', '1000', '0', '1516654200');
INSERT INTO `analysis_gift_consume` VALUES ('805', '20180122', '45', '白银守护', '1', '1314', '0', '1516654200');
INSERT INTO `analysis_gift_consume` VALUES ('806', '20180122', '102', '烟花', '1', '888', '0', '1516654200');
INSERT INTO `analysis_gift_consume` VALUES ('807', '20180122', '13', '棒棒糖', '1314', '1314', '0', '1516740600');
INSERT INTO `analysis_gift_consume` VALUES ('808', '20180122', '110', 'G蛋节', '1', '500', '0', '1516740600');
INSERT INTO `analysis_gift_consume` VALUES ('809', '20180126', '34', '幸运流氓蕉', '2', '2', '0', '1516999800');
INSERT INTO `analysis_gift_consume` VALUES ('810', '20180126', '45', '白银守护', '2', '2628', '0', '1516999800');
INSERT INTO `analysis_gift_consume` VALUES ('811', '20180130', '1', '弹幕', '5', '5', '0', '1517345400');
INSERT INTO `analysis_gift_consume` VALUES ('812', '20180130', '6', '留声机', '2', '1000', '0', '1517345400');
INSERT INTO `analysis_gift_consume` VALUES ('813', '20180130', '23', '喇叭', '1', '188', '0', '1517345400');
INSERT INTO `analysis_gift_consume` VALUES ('814', '20180130', '34', '幸运流氓蕉', '1', '1', '0', '1517345400');
INSERT INTO `analysis_gift_consume` VALUES ('815', '20180130', '106', '龙头', '2', '176', '0', '1517345400');
INSERT INTO `analysis_gift_consume` VALUES ('816', '20180226', '4', '猫咪', '2630', '13150', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('817', '20180226', '5', '金砖', '2', '400', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('818', '20180226', '6', '留声机', '2', '1000', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('819', '20180226', '7', '心动', '2', '2', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('820', '20180226', '8', '钻戒', '2', '132', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('821', '20180226', '9', '钻石', '3', '3942', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('822', '20180226', '15', '亲亲', '1', '5', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('823', '20180226', '16', '情书', '1', '10', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('824', '20180226', '21', '火箭', '1', '9999', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('825', '20180226', '26', '甲壳虫', '1', '660', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('826', '20180226', '33', '幸运啤酒', '1', '2', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('827', '20180226', '34', '幸运流氓蕉', '4', '4', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('828', '20180226', '102', '烟花', '2', '1776', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('829', '20180226', '107', '三岁', '2', '666', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('830', '20180226', '110', 'G蛋节', '3', '1500', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('831', '20180226', '111', '比心', '9', '594', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('832', '20180226', '112', '啤酒', '3', '2640', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('833', '20180226', '113', '城堡', '10', '123120', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('834', '20180226', '114', '告白气球', '11', '253', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('835', '20180226', '115', '火车', '7', '161', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('836', '20180226', '116', '火箭', '19', '2204', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('837', '20180226', '117', '么么哒', '17', '1989', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('838', '20180226', '118', '跑车', '29', '3422', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('839', '20180226', '119', '生日蛋糕', '314', '37366', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('840', '20180226', '120', '桃花', '10', '1200', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('841', '20180226', '121', '星空之恋', '6', '726', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('842', '20180226', '124', 'test3IOS', '11', '1364', '0', '1519678200');
INSERT INTO `analysis_gift_consume` VALUES ('843', '20180227', '6', '留声机', '1', '500', '0', '1519764600');
INSERT INTO `analysis_gift_consume` VALUES ('844', '20180227', '7', '心动', '1', '1', '0', '1519764600');
INSERT INTO `analysis_gift_consume` VALUES ('845', '20180227', '8', '钻戒', '1', '66', '0', '1519764600');
INSERT INTO `analysis_gift_consume` VALUES ('846', '20180227', '13', '棒棒糖', '3', '3', '0', '1519764600');
INSERT INTO `analysis_gift_consume` VALUES ('847', '20180227', '34', '幸运流氓蕉', '23', '23', '0', '1519764600');
INSERT INTO `analysis_gift_consume` VALUES ('848', '20180227', '114', '告白气球', '1', '23', '0', '1519764600');
INSERT INTO `analysis_gift_consume` VALUES ('849', '20180227', '115', '火车', '2', '46', '0', '1519764600');
INSERT INTO `analysis_gift_consume` VALUES ('850', '20180227', '116', '火箭', '1', '116', '0', '1519764600');
INSERT INTO `analysis_gift_consume` VALUES ('851', '20180227', '118', '跑车', '2', '236', '0', '1519764600');
INSERT INTO `analysis_gift_consume` VALUES ('852', '20180227', '119', '生日蛋糕', '1', '119', '0', '1519764600');
INSERT INTO `analysis_gift_consume` VALUES ('853', '20180227', '120', '桃花', '1', '120', '0', '1519764600');
INSERT INTO `analysis_gift_consume` VALUES ('854', '20180228', '4', '猫咪', '520', '2600', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('855', '20180228', '5', '金砖', '1', '200', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('856', '20180228', '7', '心动', '10001', '10001', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('857', '20180228', '8', '钻戒', '5', '330', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('858', '20180228', '10', '兰博基尼', '1', '3000', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('859', '20180228', '13', '棒棒糖', '1', '1', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('860', '20180228', '16', '情书', '32851', '328510', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('861', '20180228', '26', '甲壳虫', '1', '660', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('862', '20180228', '32', '幸运玫瑰', '9999', '9999', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('863', '20180228', '73', '奋斗', '1314', '1314', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('864', '20180228', '82', '丘比特之箭', '1', '520', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('865', '20180228', '84', '相互伤害', '11322', '22644', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('866', '20180228', '102', '烟花', '1', '888', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('867', '20180228', '105', '金豆', '16004', '16004', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('868', '20180228', '106', '龙头', '1315', '115720', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('869', '20180228', '107', '三岁', '522', '173826', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('870', '20180228', '111', '比心', '5', '330', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('871', '20180228', '112', '啤酒', '5', '4400', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('872', '20180228', '113', '城堡', '1', '12312', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('873', '20180228', '114', '告白气球', '1', '23', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('874', '20180228', '115', '火车', '2', '46', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('875', '20180228', '116', '火箭', '1', '116', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('876', '20180228', '117', '么么哒', '1', '117', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('877', '20180228', '118', '跑车', '1', '118', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('878', '20180228', '119', '生日蛋糕', '5', '595', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('879', '20180228', '120', '桃花', '4', '480', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('880', '20180228', '121', '星空之恋', '1', '121', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('881', '20180228', '122', '彩虹', '5', '610', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('882', '20180228', '123', '飞机', '5', '615', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('883', '20180228', '124', '皇冠', '10', '1240', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('884', '20180228', '125', '轮船', '8', '1000', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('885', '20180228', '126', '荧光棒', '15', '1890', '0', '1519851000');
INSERT INTO `analysis_gift_consume` VALUES ('886', '20180305', '33', '幸运啤酒', '1', '2', '0', '1520283000');
INSERT INTO `analysis_gift_consume` VALUES ('887', '20180305', '113', '城堡', '4', '49248', '0', '1520283000');
INSERT INTO `analysis_gift_consume` VALUES ('888', '20180305', '119', '生日蛋糕', '3', '357', '0', '1520283000');
INSERT INTO `analysis_gift_consume` VALUES ('889', '20180305', '121', '星空之恋', '3', '363', '0', '1520283000');
INSERT INTO `analysis_gift_consume` VALUES ('890', '20180305', '124', '皇冠', '1', '124', '0', '1520283000');
INSERT INTO `analysis_gift_consume` VALUES ('891', '20180305', '125', '轮船', '2', '250', '0', '1520283000');
INSERT INTO `analysis_gift_consume` VALUES ('892', '20180305', '126', '荧光棒', '3', '378', '0', '1520283000');
INSERT INTO `analysis_gift_consume` VALUES ('893', '20180306', '5', '金砖', '1561', '312200', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('894', '20180306', '6', '留声机', '1040', '520000', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('895', '20180306', '7', '心动', '1', '1', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('896', '20180306', '8', '钻戒', '15', '990', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('897', '20180306', '9', '钻石', '2', '2628', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('898', '20180306', '18', '飞机', '1', '6666', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('899', '20180306', '19', '私人海岛', '1', '13140', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('900', '20180306', '21', '火箭', '2', '19998', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('901', '20180306', '22', '流星雨', '1', '980', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('902', '20180306', '40', '鹊桥', '1', '7', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('903', '20180306', '59', '约定今生', '2', '1040', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('904', '20180306', '84', '相互伤害', '301', '602', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('905', '20180306', '104', '蓝色妖姬', '1', '88', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('906', '20180306', '110', 'G蛋节', '1', '500', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('907', '20180306', '113', '城堡', '1', '12312', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('908', '20180306', '114', '告白气球', '4', '92', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('909', '20180306', '115', '火车', '1', '23', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('910', '20180306', '116', '火箭', '6', '696', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('911', '20180306', '117', '么么哒', '1', '117', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('912', '20180306', '118', '跑车', '1', '118', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('913', '20180306', '119', '生日蛋糕', '3', '357', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('914', '20180306', '120', '桃花', '1', '120', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('915', '20180306', '121', '星空之恋', '1', '121', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('916', '20180306', '122', '彩虹', '2', '244', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('917', '20180306', '123', '飞机', '1', '123', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('918', '20180306', '124', '皇冠', '3', '372', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('919', '20180306', '125', '轮船', '1', '125', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('920', '20180306', '126', '荧光棒', '2', '252', '0', '1520369400');
INSERT INTO `analysis_gift_consume` VALUES ('921', '20180310', '113', '城堡', '2', '24624', '0', '1520715000');
INSERT INTO `analysis_gift_consume` VALUES ('922', '20180312', '10', '兰博基尼', '1', '3000', '0', '1520887800');
INSERT INTO `analysis_gift_consume` VALUES ('923', '20180312', '11', '私人游艇', '528', '4692864', '0', '1520887800');
INSERT INTO `analysis_gift_consume` VALUES ('924', '20180312', '13', '棒棒糖', '2', '2', '0', '1520887800');
INSERT INTO `analysis_gift_consume` VALUES ('925', '20180312', '18', '飞机', '5', '33330', '0', '1520887800');
INSERT INTO `analysis_gift_consume` VALUES ('926', '20180312', '26', '甲壳虫', '2', '1320', '0', '1520887800');
INSERT INTO `analysis_gift_consume` VALUES ('927', '20180312', '32', '幸运玫瑰', '3', '3', '0', '1520887800');
INSERT INTO `analysis_gift_consume` VALUES ('928', '20180312', '34', '幸运流氓蕉', '2', '2', '0', '1520887800');
INSERT INTO `analysis_gift_consume` VALUES ('929', '20180312', '123', '飞机', '6', '738', '0', '1520887800');
INSERT INTO `analysis_gift_consume` VALUES ('930', '20180313', '6', '留声机', '101', '50500', '0', '1520974200');
INSERT INTO `analysis_gift_consume` VALUES ('931', '20180313', '83', '香吻', '1', '1', '0', '1520974200');
INSERT INTO `analysis_gift_consume` VALUES ('932', '20180315', '5', '金砖', '1', '200', '0', '1521147000');
INSERT INTO `analysis_gift_consume` VALUES ('933', '20180316', '5', '金砖', '48', '9600', '0', '1521233400');
INSERT INTO `analysis_gift_consume` VALUES ('934', '20180316', '6', '留声机', '3', '1500', '0', '1521233400');
INSERT INTO `analysis_gift_consume` VALUES ('935', '20180316', '11', '私人游艇', '11', '97768', '0', '1521233400');
INSERT INTO `analysis_gift_consume` VALUES ('936', '20180316', '19', '私人海岛', '4', '52560', '0', '1521233400');
INSERT INTO `analysis_gift_consume` VALUES ('937', '20180316', '26', '甲壳虫', '201', '132660', '0', '1521233400');
INSERT INTO `analysis_gift_consume` VALUES ('938', '20180316', '34', '幸运流氓蕉', '2', '2', '0', '1521233400');
INSERT INTO `analysis_gift_consume` VALUES ('939', '20180316', '96', '来一炮', '1', '980', '0', '1521233400');
INSERT INTO `analysis_gift_consume` VALUES ('940', '20180316', '114', '告白气球', '3', '69', '0', '1521233400');
INSERT INTO `analysis_gift_consume` VALUES ('941', '20180316', '116', '火箭', '1', '116', '0', '1521233400');
INSERT INTO `analysis_gift_consume` VALUES ('942', '20180316', '117', '么么哒', '2', '234', '0', '1521233400');
INSERT INTO `analysis_gift_consume` VALUES ('943', '20180316', '123', '飞机', '1', '123', '0', '1521233400');
INSERT INTO `analysis_gift_consume` VALUES ('944', '20180316', '124', '皇冠', '1', '124', '0', '1521233400');
INSERT INTO `analysis_gift_consume` VALUES ('945', '20180317', '5', '金砖', '50', '10000', '0', '1521319800');
INSERT INTO `analysis_gift_consume` VALUES ('946', '20180317', '11', '私人游艇', '2', '17776', '0', '1521319800');
INSERT INTO `analysis_gift_consume` VALUES ('947', '20180317', '26', '甲壳虫', '41', '27060', '0', '1521319800');
INSERT INTO `analysis_gift_consume` VALUES ('948', '20180317', '104', '蓝色妖姬', '26', '2288', '0', '1521319800');
INSERT INTO `analysis_gift_consume` VALUES ('949', '20180317', '105', '金豆', '1', '1', '0', '1521319800');
INSERT INTO `analysis_gift_consume` VALUES ('950', '20180317', '106', '龙头', '1', '88', '0', '1521319800');
INSERT INTO `analysis_gift_consume` VALUES ('951', '20180318', '5', '金砖', '2', '400', '0', '1521406200');
INSERT INTO `analysis_gift_consume` VALUES ('952', '20180318', '26', '甲壳虫', '2', '1320', '0', '1521406200');
INSERT INTO `analysis_gift_consume` VALUES ('953', '20180319', '5', '金砖', '1', '200', '0', '1521492600');
INSERT INTO `analysis_gift_consume` VALUES ('954', '20180319', '14', '小黄鸡', '21', '42', '0', '1521492600');
INSERT INTO `analysis_gift_consume` VALUES ('955', '20180319', '26', '甲壳虫', '1', '660', '0', '1521492600');
INSERT INTO `analysis_gift_consume` VALUES ('956', '20180319', '32', '幸运玫瑰', '1', '1', '0', '1521492600');
INSERT INTO `analysis_gift_consume` VALUES ('957', '20180319', '114', '告白气球', '1', '23', '0', '1521492600');
INSERT INTO `analysis_gift_consume` VALUES ('958', '20180319', '119', '生日蛋糕', '1', '119', '0', '1521492600');
INSERT INTO `analysis_gift_consume` VALUES ('959', '20180319', '121', '星空之恋', '1', '121', '0', '1521492600');
INSERT INTO `analysis_gift_consume` VALUES ('960', '20180319', '123', '飞机', '3', '369', '0', '1521492600');
INSERT INTO `analysis_gift_consume` VALUES ('961', '20180319', '124', '皇冠', '10', '1240', '0', '1521492600');
INSERT INTO `analysis_gift_consume` VALUES ('962', '20180319', '126', '荧光棒', '1', '126', '0', '1521492600');
INSERT INTO `analysis_gift_consume` VALUES ('963', '20180319', '127', '鼓掌', '5', '635', '0', '1521492600');
INSERT INTO `analysis_gift_consume` VALUES ('964', '20180319', '128', '钻戒', '2', '256', '0', '1521492600');
INSERT INTO `analysis_gift_consume` VALUES ('965', '20180319', '131', '666', '1', '131', '0', '1521492600');
INSERT INTO `analysis_gift_consume` VALUES ('966', '20180319', '132', '金话筒', '4', '528', '0', '1521492600');
INSERT INTO `analysis_gift_consume` VALUES ('967', '20180319', '133', '打call', '1', '133', '0', '1521492600');
INSERT INTO `analysis_gift_consume` VALUES ('968', '20180320', '2', '玫瑰花', '1', '1', '0', '1521579000');
INSERT INTO `analysis_gift_consume` VALUES ('969', '20180320', '5', '金砖', '17', '3400', '0', '1521579000');
INSERT INTO `analysis_gift_consume` VALUES ('970', '20180320', '11', '私人游艇', '99', '879912', '0', '1521579000');
INSERT INTO `analysis_gift_consume` VALUES ('971', '20180320', '13', '棒棒糖', '1', '1', '0', '1521579000');
INSERT INTO `analysis_gift_consume` VALUES ('972', '20180320', '22', '流星雨', '2', '1960', '0', '1521579000');
INSERT INTO `analysis_gift_consume` VALUES ('973', '20180320', '26', '甲壳虫', '9', '5940', '0', '1521579000');
INSERT INTO `analysis_gift_consume` VALUES ('974', '20180320', '34', '幸运流氓蕉', '2', '2', '0', '1521579000');
INSERT INTO `analysis_gift_consume` VALUES ('975', '20180320', '59', '约定今生', '3', '1560', '0', '1521579000');
INSERT INTO `analysis_gift_consume` VALUES ('976', '20180320', '80', '小色猪', '1', '1', '0', '1521579000');
INSERT INTO `analysis_gift_consume` VALUES ('977', '20180320', '107', '三岁', '1', '333', '0', '1521579000');
INSERT INTO `analysis_gift_consume` VALUES ('978', '20180320', '125', '轮船', '3', '375', '0', '1521579000');
INSERT INTO `analysis_gift_consume` VALUES ('979', '20180321', '13', '棒棒糖', '2', '2', '0', '1521665400');
INSERT INTO `analysis_gift_consume` VALUES ('980', '20180321', '22', '流星雨', '1', '980', '0', '1521665400');
INSERT INTO `analysis_gift_consume` VALUES ('981', '20180321', '26', '甲壳虫', '24', '15840', '0', '1521665400');
INSERT INTO `analysis_gift_consume` VALUES ('982', '20180321', '32', '幸运玫瑰', '1', '1', '0', '1521665400');
INSERT INTO `analysis_gift_consume` VALUES ('983', '20180321', '34', '幸运流氓蕉', '1', '1', '0', '1521665400');
INSERT INTO `analysis_gift_consume` VALUES ('984', '20180321', '91', '圣诞雪橇', '3', '900', '0', '1521665400');
INSERT INTO `analysis_gift_consume` VALUES ('985', '20180321', '96', '来一炮', '3', '2940', '0', '1521665400');
INSERT INTO `analysis_gift_consume` VALUES ('986', '20180321', '115', '火车', '1', '23', '0', '1521665400');
INSERT INTO `analysis_gift_consume` VALUES ('987', '20180323', '26', '甲壳虫', '4', '2640', '0', '1521838200');
INSERT INTO `analysis_gift_consume` VALUES ('988', '20180323', '91', '圣诞雪橇', '2', '600', '0', '1521838200');
INSERT INTO `analysis_gift_consume` VALUES ('989', '20180323', '96', '来一炮', '1', '980', '0', '1521838200');
INSERT INTO `analysis_gift_consume` VALUES ('990', '20180323', '105', '金豆', '1', '1', '0', '1521838200');
INSERT INTO `analysis_gift_consume` VALUES ('991', '20180323', '106', '龙头', '2', '176', '0', '1521838200');
INSERT INTO `analysis_gift_consume` VALUES ('992', '20180327', '13', '棒棒糖', '1', '1', '0', '1522183800');
INSERT INTO `analysis_gift_consume` VALUES ('993', '20180327', '26', '甲壳虫', '2', '1320', '0', '1522183800');
INSERT INTO `analysis_gift_consume` VALUES ('994', '20180327', '34', '幸运流氓蕉', '2', '2', '0', '1522183800');
INSERT INTO `analysis_gift_consume` VALUES ('995', '20180327', '91', '圣诞雪橇', '2', '600', '0', '1522183800');
INSERT INTO `analysis_gift_consume` VALUES ('996', '20180327', '105', '金豆', '1', '1', '0', '1522183800');
INSERT INTO `analysis_gift_consume` VALUES ('997', '20180328', '21', '火箭', '1', '9999', '0', '1522270200');
INSERT INTO `analysis_gift_consume` VALUES ('998', '20180328', '26', '甲壳虫', '2', '1320', '0', '1522270200');
INSERT INTO `analysis_gift_consume` VALUES ('999', '20180328', '91', '圣诞雪橇', '1', '300', '0', '1522270200');
INSERT INTO `analysis_gift_consume` VALUES ('1000', '20180328', '92', '口红', '1', '199', '0', '1522270200');
INSERT INTO `analysis_gift_consume` VALUES ('1001', '20180328', '106', '龙头', '1', '88', '0', '1522270200');
INSERT INTO `analysis_gift_consume` VALUES ('1002', '20180328', '111', '比心', '1', '66', '0', '1522270200');
INSERT INTO `analysis_gift_consume` VALUES ('1003', '20180328', '113', '城堡', '1', '12312', '0', '1522270200');
INSERT INTO `analysis_gift_consume` VALUES ('1004', '20180328', '114', '告白气球', '1', '23', '0', '1522270200');
INSERT INTO `analysis_gift_consume` VALUES ('1005', '20180328', '121', '星空之恋', '1', '121', '0', '1522270200');
INSERT INTO `analysis_gift_consume` VALUES ('1006', '20180328', '122', '彩虹', '1', '122', '0', '1522270200');
INSERT INTO `analysis_gift_consume` VALUES ('1007', '20180328', '131', '666', '1', '131', '0', '1522270200');
INSERT INTO `analysis_gift_consume` VALUES ('1008', '20180328', '133', '打call', '1', '133', '0', '1522270200');
INSERT INTO `analysis_gift_consume` VALUES ('1009', '20180331', '13', '棒棒糖', '1', '1', '0', '1522529400');
INSERT INTO `analysis_gift_consume` VALUES ('1010', '20180331', '26', '甲壳虫', '8', '5280', '0', '1522529400');
INSERT INTO `analysis_gift_consume` VALUES ('1011', '20180331', '104', '蓝色妖姬', '1', '88', '0', '1522529400');
INSERT INTO `analysis_gift_consume` VALUES ('1012', '20180331', '105', '金豆', '11', '11', '0', '1522529400');
INSERT INTO `analysis_gift_consume` VALUES ('1013', '20180331', '117', '么么哒', '31', '3627', '0', '1522529400');
INSERT INTO `analysis_gift_consume` VALUES ('1014', '20180331', '120', '桃花', '1', '120', '0', '1522529400');
INSERT INTO `analysis_gift_consume` VALUES ('1015', '20180331', '122', '彩虹', '34', '4148', '0', '1522529400');
INSERT INTO `analysis_gift_consume` VALUES ('1016', '20180331', '126', '荧光棒', '4', '504', '0', '1522529400');
INSERT INTO `analysis_gift_consume` VALUES ('1017', '20180331', '131', '666', '1', '131', '0', '1522529400');
INSERT INTO `analysis_gift_consume` VALUES ('1018', '20180402', '13', '棒棒糖', '1', '1', '0', '1522702200');
INSERT INTO `analysis_gift_consume` VALUES ('1019', '20180402', '26', '甲壳虫', '3', '1980', '0', '1522702200');
INSERT INTO `analysis_gift_consume` VALUES ('1020', '20180402', '34', '幸运流氓蕉', '20', '20', '0', '1522702200');
INSERT INTO `analysis_gift_consume` VALUES ('1021', '20180402', '91', '圣诞雪橇', '1', '300', '0', '1522702200');
INSERT INTO `analysis_gift_consume` VALUES ('1022', '20180410', '7', '心动', '76', '76', '0', '1523393400');
INSERT INTO `analysis_gift_consume` VALUES ('1023', '20180410', '10', '兰博基尼', '22', '66000', '0', '1523393400');
INSERT INTO `analysis_gift_consume` VALUES ('1024', '20180410', '59', '约定今生', '4', '2080', '0', '1523393400');
INSERT INTO `analysis_gift_consume` VALUES ('1025', '20180410', '96', '来一炮', '30', '29400', '0', '1523393400');
INSERT INTO `analysis_gift_consume` VALUES ('1026', '20180413', '6', '留声机', '4', '2000', '0', '1523652600');
INSERT INTO `analysis_gift_consume` VALUES ('1027', '20180413', '19', '私人海岛', '5', '65700', '0', '1523652600');
INSERT INTO `analysis_gift_consume` VALUES ('1028', '20180413', '22', '流星雨', '2', '1960', '0', '1523652600');
INSERT INTO `analysis_gift_consume` VALUES ('1029', '20180413', '26', '甲壳虫', '28', '18480', '0', '1523652600');
INSERT INTO `analysis_gift_consume` VALUES ('1030', '20180413', '49', '烛光晚餐', '2', '376', '0', '1523652600');
INSERT INTO `analysis_gift_consume` VALUES ('1031', '20180413', '84', '相互伤害', '3', '6', '0', '1523652600');
INSERT INTO `analysis_gift_consume` VALUES ('1032', '20180413', '91', '圣诞雪橇', '2', '600', '0', '1523652600');
INSERT INTO `analysis_gift_consume` VALUES ('1033', '20180413', '96', '来一炮', '36', '35280', '0', '1523652600');
INSERT INTO `analysis_gift_consume` VALUES ('1034', '20180413', '106', '龙头', '5', '440', '0', '1523652600');
INSERT INTO `analysis_gift_consume` VALUES ('1035', '20180413', '111', '比心', '1', '66', '0', '1523652600');
INSERT INTO `analysis_gift_consume` VALUES ('1036', '20180413', '115', '火车', '12', '276', '0', '1523652600');
INSERT INTO `analysis_gift_consume` VALUES ('1037', '20180413', '129', '口红', '1', '129', '0', '1523652600');
INSERT INTO `analysis_gift_consume` VALUES ('1038', '20180414', '3', '猪头', '14', '28', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1039', '20180414', '5', '金砖', '12', '2400', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1040', '20180414', '6', '留声机', '3', '1500', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1041', '20180414', '13', '棒棒糖', '2204', '2204', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1042', '20180414', '19', '私人海岛', '8', '105120', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1043', '20180414', '24', '小萌猪', '1', '0', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1044', '20180414', '27', '心动', '1', '1', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1045', '20180414', '29', '守护人气猪', '18', '0', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1046', '20180414', '45', '白银守护', '14', '18396', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1047', '20180414', '46', '黄金守护', '7', '91980', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1048', '20180414', '80', '小色猪', '1', '1', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1049', '20180414', '105', '金豆', '2', '2', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1050', '20180414', '113', '城堡', '1', '12312', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1051', '20180414', '114', '告白气球', '1', '23', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1052', '20180414', '116', '火箭', '1', '116', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1053', '20180414', '128', '钻戒', '4', '512', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1054', '20180414', '130', '锤子', '3', '390', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1055', '20180414', '132', '金话筒', '7', '924', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1056', '20180414', '133', '打call', '15', '1995', '0', '1523739000');
INSERT INTO `analysis_gift_consume` VALUES ('1057', '20180416', '26', '甲壳虫', '4', '2640', '0', '1523911800');
INSERT INTO `analysis_gift_consume` VALUES ('1058', '20180416', '96', '来一炮', '27', '26460', '0', '1523911800');
INSERT INTO `analysis_gift_consume` VALUES ('1059', '20180416', '106', '龙头', '1', '88', '0', '1523911800');
INSERT INTO `analysis_gift_consume` VALUES ('1060', '20180416', '111', '比心', '1', '1688', '0', '1523911800');
INSERT INTO `analysis_gift_consume` VALUES ('1061', '20180416', '113', '城堡', '1', '131400', '0', '1523911800');
INSERT INTO `analysis_gift_consume` VALUES ('1062', '20180416', '114', '告白气球', '1', '5888', '0', '1523911800');
INSERT INTO `analysis_gift_consume` VALUES ('1063', '20180416', '122', '彩虹', '1', '68888', '0', '1523911800');
INSERT INTO `analysis_gift_consume` VALUES ('1064', '20180416', '125', '轮船', '1', '99988', '0', '1523911800');
INSERT INTO `analysis_gift_consume` VALUES ('1065', '20180416', '133', '打call', '1', '388', '0', '1523911800');
INSERT INTO `analysis_gift_consume` VALUES ('1066', '20180417', '18', '飞机old', '21', '139986', '0', '1523998200');
INSERT INTO `analysis_gift_consume` VALUES ('1067', '20180417', '112', '啤酒', '3', '2664', '0', '1523998200');
INSERT INTO `analysis_gift_consume` VALUES ('1068', '20180417', '113', '城堡', '2', '262800', '0', '1523998200');
INSERT INTO `analysis_gift_consume` VALUES ('1069', '20180417', '116', '火箭', '3', '77664', '0', '1523998200');
INSERT INTO `analysis_gift_consume` VALUES ('1070', '20180417', '119', '生日蛋糕', '1', '8888', '0', '1523998200');
INSERT INTO `analysis_gift_consume` VALUES ('1071', '20180417', '124', '皇冠', '1', '2488', '0', '1523998200');
INSERT INTO `analysis_gift_consume` VALUES ('1072', '20180417', '125', '轮船', '1', '99988', '0', '1523998200');
INSERT INTO `analysis_gift_consume` VALUES ('1073', '20180417', '129', '口红', '2', '116', '0', '1523998200');
INSERT INTO `analysis_gift_consume` VALUES ('1074', '20180417', '130', '锤子', '1', '588', '0', '1523998200');
INSERT INTO `analysis_gift_consume` VALUES ('1075', '20180417', '131', '666', '3', '54', '0', '1523998200');
INSERT INTO `analysis_gift_consume` VALUES ('1076', '20180418', '18', '飞机', '2', '13332', '0', '1524084600');
INSERT INTO `analysis_gift_consume` VALUES ('1077', '20180418', '80', '小色猪', '3', '3', '0', '1524084600');
INSERT INTO `analysis_gift_consume` VALUES ('1078', '20180419', '9', '钻石', '1', '11888', '0', '1524171000');
INSERT INTO `analysis_gift_consume` VALUES ('1079', '20180419', '32', '幸运玫瑰', '4', '272', '0', '1524171000');
INSERT INTO `analysis_gift_consume` VALUES ('1080', '20180419', '45', '白银守护', '1', '150000', '0', '1524171000');
INSERT INTO `analysis_gift_consume` VALUES ('1081', '20180419', '111', '比心', '1', '1688', '0', '1524171000');
INSERT INTO `analysis_gift_consume` VALUES ('1082', '20180419', '112', '啤酒', '1', '888', '0', '1524171000');
INSERT INTO `analysis_gift_consume` VALUES ('1083', '20180419', '114', '告白气球', '1', '6888', '0', '1524171000');
INSERT INTO `analysis_gift_consume` VALUES ('1084', '20180419', '115', '火车', '1', '11888', '0', '1524171000');
INSERT INTO `analysis_gift_consume` VALUES ('1085', '20180419', '116', '火箭', '1', '5888', '0', '1524171000');
INSERT INTO `analysis_gift_consume` VALUES ('1086', '20180419', '117', '么么哒', '1', '3688', '0', '1524171000');
INSERT INTO `analysis_gift_consume` VALUES ('1087', '20180419', '120', '桃花', '1', '8888', '0', '1524171000');
INSERT INTO `analysis_gift_consume` VALUES ('1088', '20180419', '122', '彩虹', '1', '68888', '0', '1524171000');
INSERT INTO `analysis_gift_consume` VALUES ('1089', '20180419', '123', '飞机', '1', '58888', '0', '1524171000');
INSERT INTO `analysis_gift_consume` VALUES ('1090', '20180419', '124', '皇冠', '1', '2488', '0', '1524171000');
INSERT INTO `analysis_gift_consume` VALUES ('1091', '20180419', '125', '轮船', '1', '38888', '0', '1524171000');
INSERT INTO `analysis_gift_consume` VALUES ('1092', '20180419', '126', '荧光棒', '1', '1288', '0', '1524171000');
INSERT INTO `analysis_gift_consume` VALUES ('1093', '20180420', '5', '金砖', '2', '13776', '0', '1524257400');
INSERT INTO `analysis_gift_consume` VALUES ('1094', '20180420', '9', '钻石', '11', '130768', '0', '1524257400');
INSERT INTO `analysis_gift_consume` VALUES ('1095', '20180420', '16', '情书', '2', '136', '0', '1524257400');
INSERT INTO `analysis_gift_consume` VALUES ('1096', '20180420', '112', '啤酒', '1', '888', '0', '1524257400');
INSERT INTO `analysis_gift_consume` VALUES ('1097', '20180420', '114', '告白气球', '1', '6888', '0', '1524257400');
INSERT INTO `analysis_gift_consume` VALUES ('1098', '20180420', '117', '么么哒', '1', '3688', '0', '1524257400');
INSERT INTO `analysis_gift_consume` VALUES ('1099', '20180420', '126', '荧光棒', '2', '2576', '0', '1524257400');
INSERT INTO `analysis_gift_consume` VALUES ('1100', '20180423', '46', '黄金守护', '1', '300000', '0', '1524516600');
INSERT INTO `analysis_gift_consume` VALUES ('1101', '20180423', '128', '钻戒', '4', '1152', '0', '1524516600');
INSERT INTO `analysis_gift_consume` VALUES ('1102', '20180423', '129', '口红', '6', '348', '0', '1524516600');
INSERT INTO `analysis_gift_consume` VALUES ('1103', '20180423', '131', '666', '2', '36', '0', '1524516600');
INSERT INTO `analysis_gift_consume` VALUES ('1104', '20180423', '132', '金话筒', '5', '340', '0', '1524516600');
INSERT INTO `analysis_gift_consume` VALUES ('1105', '20180423', '133', '打call', '14', '5432', '0', '1524516600');
INSERT INTO `analysis_gift_consume` VALUES ('1106', '20180426', '2', '玫瑰花', '404', '404', '0', '1524775800');
INSERT INTO `analysis_gift_consume` VALUES ('1107', '20180426', '112', '啤酒', '2825', '2508600', '0', '1524775800');
INSERT INTO `analysis_gift_consume` VALUES ('1108', '20180426', '118', '跑车', '3', '77664', '0', '1524775800');
INSERT INTO `analysis_gift_consume` VALUES ('1109', '20180426', '120', '桃花', '1', '8888', '0', '1524775800');
INSERT INTO `analysis_gift_consume` VALUES ('1110', '20180426', '121', '星空之恋', '99', '8799912', '0', '1524775800');
INSERT INTO `analysis_gift_consume` VALUES ('1111', '20180426', '126', '荧光棒', '1', '1288', '0', '1524775800');
INSERT INTO `analysis_gift_consume` VALUES ('1112', '20180426', '127', '鼓掌', '4', '752', '0', '1524775800');
INSERT INTO `analysis_gift_consume` VALUES ('1113', '20180426', '129', '口红', '301', '17458', '0', '1524775800');
INSERT INTO `analysis_gift_consume` VALUES ('1114', '20180426', '133', '打call', '1', '388', '0', '1524775800');
INSERT INTO `analysis_gift_consume` VALUES ('1115', '20180427', '22', '流星雨', '100', '68800', '0', '1524862200');
INSERT INTO `analysis_gift_consume` VALUES ('1116', '20180427', '112', '啤酒', '4032', '3580416', '0', '1524862200');
INSERT INTO `analysis_gift_consume` VALUES ('1117', '20180427', '118', '跑车', '399', '10329312', '0', '1524862200');
INSERT INTO `analysis_gift_consume` VALUES ('1118', '20180427', '126', '荧光棒', '99', '127512', '0', '1524862200');
INSERT INTO `analysis_gift_consume` VALUES ('1119', '20180427', '127', '鼓掌', '1', '188', '0', '1524862200');
INSERT INTO `analysis_gift_consume` VALUES ('1120', '20180427', '128', '钻戒', '1', '288', '0', '1524862200');
INSERT INTO `analysis_gift_consume` VALUES ('1121', '20180427', '129', '口红', '1', '58', '0', '1524862200');
INSERT INTO `analysis_gift_consume` VALUES ('1122', '20180427', '130', '锤子', '1', '588', '0', '1524862200');
INSERT INTO `analysis_gift_consume` VALUES ('1123', '20180427', '131', '666', '1', '18', '0', '1524862200');
INSERT INTO `analysis_gift_consume` VALUES ('1124', '20180518', '45', '白银守护', '1', '150000', '0', '1526676600');
INSERT INTO `analysis_gift_consume` VALUES ('1125', '20180518', '128', '钻戒', '3', '864', '0', '1526676600');
INSERT INTO `analysis_gift_consume` VALUES ('1126', '20180518', '129', '口红', '3', '174', '0', '1526676600');
INSERT INTO `analysis_gift_consume` VALUES ('1127', '20180518', '131', '666', '8', '144', '0', '1526676600');
INSERT INTO `analysis_gift_consume` VALUES ('1128', '20180518', '132', '金话筒', '2', '136', '0', '1526676600');
INSERT INTO `analysis_gift_consume` VALUES ('1129', '20180518', '133', '打call', '2', '776', '0', '1526676600');
INSERT INTO `analysis_gift_consume` VALUES ('1130', '20180519', '46', '黄金守护', '1', '300000', '0', '1526763000');
INSERT INTO `analysis_gift_consume` VALUES ('1131', '20180519', '127', '鼓掌', '1', '188', '0', '1526763000');
INSERT INTO `analysis_gift_consume` VALUES ('1132', '20180519', '128', '钻戒', '1', '288', '0', '1526763000');
INSERT INTO `analysis_gift_consume` VALUES ('1133', '20180519', '129', '口红', '1', '58', '0', '1526763000');
INSERT INTO `analysis_gift_consume` VALUES ('1134', '20180519', '131', '666', '1', '18', '0', '1526763000');
INSERT INTO `analysis_gift_consume` VALUES ('1135', '20180520', '112', '啤酒', '1', '888', '0', '1526849400');
INSERT INTO `analysis_gift_consume` VALUES ('1136', '20180520', '113', '城堡', '1', '238800', '0', '1526849400');
INSERT INTO `analysis_gift_consume` VALUES ('1137', '20180520', '114', '告白气球', '2', '13776', '0', '1526849400');
INSERT INTO `analysis_gift_consume` VALUES ('1138', '20180520', '116', '火箭', '1', '5888', '0', '1526849400');
INSERT INTO `analysis_gift_consume` VALUES ('1139', '20180520', '118', '跑车', '1', '25888', '0', '1526849400');
INSERT INTO `analysis_gift_consume` VALUES ('1140', '20180520', '120', '桃花', '1', '8888', '0', '1526849400');
INSERT INTO `analysis_gift_consume` VALUES ('1141', '20180521', '45', '白银守护', '1', '150000', '0', '1526935800');
INSERT INTO `analysis_gift_consume` VALUES ('1142', '20180525', '9', '钻石', '1', '11888', '0', '1527281400');
INSERT INTO `analysis_gift_consume` VALUES ('1143', '20180525', '22', '流星雨', '1', '688', '0', '1527281400');
INSERT INTO `analysis_gift_consume` VALUES ('1144', '20180525', '27', '心动', '2', '576', '0', '1527281400');
INSERT INTO `analysis_gift_consume` VALUES ('1145', '20180525', '82', '丘比特之箭', '1', '1288', '0', '1527281400');
INSERT INTO `analysis_gift_consume` VALUES ('1146', '20180525', '131', '666', '1', '18', '0', '1527281400');
INSERT INTO `analysis_gift_consume` VALUES ('1147', '20180528', '131', '666', '24', '432', '0', '1527540600');

-- ----------------------------
-- Table structure for `analysis_remain`
-- ----------------------------
DROP TABLE IF EXISTS `analysis_remain`;
CREATE TABLE `analysis_remain` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `times` int(11) NOT NULL DEFAULT '0' COMMENT '日期',
  `os` int(3) NOT NULL DEFAULT '0' COMMENT '平台[=1安卓 =2苹果 =3H5 =5WEB]',
  `channel` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '渠道',
  `newUser` int(11) NOT NULL COMMENT '新用户数',
  `remain1` int(11) NOT NULL DEFAULT '0' COMMENT '次日留存',
  `remain2` int(11) NOT NULL DEFAULT '0' COMMENT '2日留存',
  `remain3` int(11) NOT NULL DEFAULT '0' COMMENT '3日留存',
  `remain4` int(11) NOT NULL DEFAULT '0' COMMENT '4日留存',
  `remain5` int(11) NOT NULL DEFAULT '0' COMMENT '5日留存',
  `remain6` int(11) NOT NULL DEFAULT '0' COMMENT '6日留存',
  `remain7` int(11) NOT NULL DEFAULT '0' COMMENT '7日留存',
  `remain14` int(11) NOT NULL DEFAULT '0' COMMENT '14日留存',
  `remain30` int(11) NOT NULL DEFAULT '0' COMMENT '30日留存',
  `addtime` int(11) NOT NULL DEFAULT '0' COMMENT '操作日期',
  `platform` int(3) DEFAULT '1' COMMENT '平台；1：小猪直播；2：暴风秀场，默认为1',
  `category` int(3) DEFAULT '1' COMMENT '1：新增注册用户数；2：新增登录用户数；3：新增付费登录用户数',
  PRIMARY KEY (`id`),
  KEY `times-os-channel` (`times`,`os`,`channel`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of analysis_remain
-- ----------------------------
INSERT INTO `analysis_remain` VALUES ('1', '1506355200', '1', 'official', '2', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1506478604', '1', '1');
INSERT INTO `analysis_remain` VALUES ('2', '1506355200', '1', 'test', '3', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1506478604', '1', '1');
INSERT INTO `analysis_remain` VALUES ('3', '1506355200', '2', 'appstore', '2', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1506478604', '1', '1');
INSERT INTO `analysis_remain` VALUES ('4', '1506355200', '2', 'ios', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1506478604', '1', '1');
INSERT INTO `analysis_remain` VALUES ('5', '1506614400', '1', 'test', '5', '1', '0', '0', '0', '0', '1', '0', '0', '0', '1506715800', '1', '1');
INSERT INTO `analysis_remain` VALUES ('6', '1506614400', '2', 'appstore', '2', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1506715800', '1', '1');
INSERT INTO `analysis_remain` VALUES ('7', '1506700800', '1', 'test', '4', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1506802200', '1', '1');
INSERT INTO `analysis_remain` VALUES ('8', '1506700800', '2', 'appstore', '2', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1506802200', '1', '1');
INSERT INTO `analysis_remain` VALUES ('9', '1507478400', '1', 'test', '3', '3', '0', '0', '0', '0', '0', '0', '0', '0', '1507579800', '1', '1');
INSERT INTO `analysis_remain` VALUES ('10', '1507478400', '2', 'ios', '1', '1', '0', '1', '0', '0', '0', '0', '0', '0', '1507579800', '1', '1');
INSERT INTO `analysis_remain` VALUES ('11', '1507564800', '2', 'appstore', '1', '0', '1', '1', '0', '0', '1', '1', '1', '0', '1507666200', '1', '1');
INSERT INTO `analysis_remain` VALUES ('12', '1507564800', '2', 'ios', '2', '0', '0', '0', '0', '0', '0', '2', '0', '0', '1507666200', '1', '1');
INSERT INTO `analysis_remain` VALUES ('13', '1507737600', '1', 'official', '1', '1', '0', '0', '0', '0', '1', '1', '0', '0', '1507839000', '1', '1');
INSERT INTO `analysis_remain` VALUES ('14', '1507737600', '2', 'appstore', '2', '1', '0', '0', '2', '0', '1', '1', '0', '0', '1507839000', '1', '1');
INSERT INTO `analysis_remain` VALUES ('15', '1507824000', '1', 'official', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1507925400', '1', '1');
INSERT INTO `analysis_remain` VALUES ('16', '1507824000', '2', 'appstore', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1507925400', '1', '1');
INSERT INTO `analysis_remain` VALUES ('17', '1508083200', '1', 'official', '3', '2', '2', '1', '0', '0', '0', '0', '0', '0', '1508184600', '1', '1');
INSERT INTO `analysis_remain` VALUES ('18', '1508083200', '1', 'test', '2', '1', '2', '1', '0', '0', '0', '1', '0', '0', '1508184600', '1', '1');
INSERT INTO `analysis_remain` VALUES ('19', '1508083200', '2', 'appstore', '5', '0', '3', '0', '0', '0', '0', '0', '0', '0', '1508184600', '1', '1');
INSERT INTO `analysis_remain` VALUES ('20', '1508169600', '1', 'test', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1508271000', '1', '1');
INSERT INTO `analysis_remain` VALUES ('21', '1508169600', '2', 'appstore', '2', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1508271000', '1', '1');
INSERT INTO `analysis_remain` VALUES ('22', '1508169600', '2', 'ios', '2', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1508271000', '1', '1');
INSERT INTO `analysis_remain` VALUES ('23', '1508256000', '2', 'appstore', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1508357400', '1', '1');
INSERT INTO `analysis_remain` VALUES ('24', '1508342400', '1', 'official', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1508443800', '1', '1');
INSERT INTO `analysis_remain` VALUES ('25', '1508342400', '1', 'test', '2', '0', '0', '0', '0', '2', '2', '0', '0', '0', '1508443800', '1', '1');
INSERT INTO `analysis_remain` VALUES ('26', '1508342400', '2', 'appstore', '2', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1508443800', '1', '1');
INSERT INTO `analysis_remain` VALUES ('27', '1508342400', '2', 'ios', '1', '0', '0', '0', '1', '1', '0', '0', '0', '0', '1508443800', '1', '1');
INSERT INTO `analysis_remain` VALUES ('28', '1508774400', '1', 'test', '1', '1', '1', '0', '0', '0', '1', '1', '0', '0', '1508875800', '1', '1');
INSERT INTO `analysis_remain` VALUES ('29', '1508860800', '2', 'appstore', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1508962200', '1', '1');
INSERT INTO `analysis_remain` VALUES ('30', '1509292800', '2', 'appstore', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1509394200', '1', '1');
INSERT INTO `analysis_remain` VALUES ('31', '1509465600', '1', 'official', '1', '0', '0', '0', '0', '1', '1', '0', '1', '0', '1509567000', '1', '1');
INSERT INTO `analysis_remain` VALUES ('32', '1509465600', '1', 'test', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1509567000', '1', '1');
INSERT INTO `analysis_remain` VALUES ('33', '1509984000', '1', 'test', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1510085400', '1', '1');
INSERT INTO `analysis_remain` VALUES ('34', '1510675200', '1', 'test', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1510776600', '1', '1');
INSERT INTO `analysis_remain` VALUES ('35', '1510675200', '2', 'appstore', '1', '0', '1', '0', '0', '0', '0', '0', '0', '0', '1510776600', '1', '1');
INSERT INTO `analysis_remain` VALUES ('36', '1510848000', '2', 'appstore', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1510949400', '1', '1');
INSERT INTO `analysis_remain` VALUES ('37', '1511193600', '1', 'test', '1', '0', '0', '0', '0', '0', '0', '1', '0', '0', '1511295000', '1', '1');
INSERT INTO `analysis_remain` VALUES ('38', '1511193600', '2', 'appstore', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1511295000', '1', '1');
INSERT INTO `analysis_remain` VALUES ('39', '1511280000', '2', 'appstore', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1511381400', '1', '1');
INSERT INTO `analysis_remain` VALUES ('40', '1511366400', '2', 'appstore', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1511467800', '1', '1');
INSERT INTO `analysis_remain` VALUES ('41', '1511452800', '1', 'test', '10', '2', '0', '1', '1', '0', '0', '0', '0', '0', '1511554200', '1', '1');
INSERT INTO `analysis_remain` VALUES ('42', '1511452800', '2', 'appstore', '7', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1511554200', '1', '1');
INSERT INTO `analysis_remain` VALUES ('43', '1511539200', '2', 'appstore', '8', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1511640600', '1', '1');
INSERT INTO `analysis_remain` VALUES ('44', '1511625600', '2', 'appstore', '3', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1511727000', '1', '1');
INSERT INTO `analysis_remain` VALUES ('45', '1511712000', '2', 'appstore', '2', '1', '0', '1', '0', '0', '0', '0', '0', '0', '1511813400', '1', '1');
INSERT INTO `analysis_remain` VALUES ('46', '1511798400', '1', 'test', '3', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1511899800', '1', '1');
INSERT INTO `analysis_remain` VALUES ('47', '1511798400', '2', 'appstore', '5', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1511899800', '1', '1');
INSERT INTO `analysis_remain` VALUES ('48', '1511971200', '1', 'official', '6', '0', '0', '0', '0', '2', '0', '0', '0', '0', '1512072600', '1', '1');
INSERT INTO `analysis_remain` VALUES ('49', '1511971200', '1', 'test', '5', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1512072600', '1', '1');
INSERT INTO `analysis_remain` VALUES ('50', '1511971200', '2', 'appstore', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1512072600', '1', '1');
INSERT INTO `analysis_remain` VALUES ('51', '1512057600', '1', 'official', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1512159000', '1', '1');
INSERT INTO `analysis_remain` VALUES ('52', '1512057600', '1', 'test', '4', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1512159000', '1', '1');
INSERT INTO `analysis_remain` VALUES ('53', '1512057600', '2', 'appstore', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1512159000', '1', '1');
INSERT INTO `analysis_remain` VALUES ('54', '1512316800', '1', 'test', '1', '1', '0', '0', '1', '1', '1', '1', '0', '0', '1512418200', '1', '1');
INSERT INTO `analysis_remain` VALUES ('55', '1512403200', '1', 'test', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1512504600', '1', '1');
INSERT INTO `analysis_remain` VALUES ('56', '1512403200', '2', 'appstore', '3', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1512504600', '1', '1');
INSERT INTO `analysis_remain` VALUES ('57', '1512489600', '2', 'appstore', '2', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1512591000', '1', '1');
INSERT INTO `analysis_remain` VALUES ('58', '1512576000', '2', 'appstore', '2', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1512677400', '1', '1');
INSERT INTO `analysis_remain` VALUES ('59', '1512662400', '1', 'official', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1512763800', '1', '1');

-- ----------------------------
-- Table structure for `analysis_remain_test`
-- ----------------------------
DROP TABLE IF EXISTS `analysis_remain_test`;
CREATE TABLE `analysis_remain_test` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `times` date NOT NULL,
  `newUser` int(11) NOT NULL,
  `remain1` double(10,5) NOT NULL DEFAULT '0.00000',
  `remain2` double(10,5) NOT NULL DEFAULT '0.00000',
  `remain3` double(10,5) NOT NULL DEFAULT '0.00000',
  `remain4` double(10,5) NOT NULL DEFAULT '0.00000',
  `remain5` double(10,5) NOT NULL DEFAULT '0.00000',
  `remain6` double(10,5) NOT NULL DEFAULT '0.00000',
  `remain7` double(10,5) NOT NULL DEFAULT '0.00000',
  `remain14` double(10,5) NOT NULL DEFAULT '0.00000',
  `remain30` double(10,5) NOT NULL DEFAULT '0.00000',
  PRIMARY KEY (`id`),
  KEY `times` (`times`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of analysis_remain_test
-- ----------------------------

-- ----------------------------
-- Table structure for `analysis_summary`
-- ----------------------------
DROP TABLE IF EXISTS `analysis_summary`;
CREATE TABLE `analysis_summary` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `times` int(11) NOT NULL DEFAULT '0' COMMENT '日期',
  `os` int(3) NOT NULL DEFAULT '0' COMMENT '平台[=1安卓 =2苹果 =3H5 =5WEB]',
  `equipmentsNew` int(11) NOT NULL DEFAULT '0' COMMENT '新增激活设备数 排重',
  `registers` int(11) NOT NULL DEFAULT '0' COMMENT '新增注册数 排重',
  `equipments` int(11) NOT NULL DEFAULT '0' COMMENT '设备活跃数 排重',
  `actives` int(11) NOT NULL DEFAULT '0' COMMENT '活跃账号数 排重',
  `amounts` double NOT NULL DEFAULT '0' COMMENT '充值金额数',
  `payUsers` int(11) NOT NULL DEFAULT '0' COMMENT '充值人数 排重',
  `addtime` int(11) DEFAULT '0' COMMENT '操作时间',
  `registAccounts` int(11) DEFAULT NULL COMMENT '当日注册用户充值总金额',
  `registPayUsers` int(11) DEFAULT NULL COMMENT '当日注册用户充值总人数',
  `channelName` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '渠道',
  PRIMARY KEY (`id`),
  UNIQUE KEY `timesOsChannel` (`times`,`os`,`channelName`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备新增激活，新增注册，充值金额，充值人数';

-- ----------------------------
-- Records of analysis_summary
-- ----------------------------

-- ----------------------------
-- Table structure for `analysis_summary_test`
-- ----------------------------
DROP TABLE IF EXISTS `analysis_summary_test`;
CREATE TABLE `analysis_summary_test` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `times` date NOT NULL,
  `amounts` int(11) NOT NULL DEFAULT '0',
  `actives` int(11) NOT NULL DEFAULT '0',
  `registers` int(11) NOT NULL DEFAULT '0',
  `payUsers` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `times` (`times`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of analysis_summary_test
-- ----------------------------

-- ----------------------------
-- Table structure for `analysis_wages`
-- ----------------------------
DROP TABLE IF EXISTS `analysis_wages`;
CREATE TABLE `analysis_wages` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `times` int(11) NOT NULL DEFAULT '0' COMMENT '日期[yyyyMM]',
  `anchorid` int(11) NOT NULL DEFAULT '0' COMMENT '主播UID',
  `unionid` int(11) NOT NULL DEFAULT '0' COMMENT '工会ID',
  `validday` int(11) NOT NULL DEFAULT '0' COMMENT '有效天数',
  `airtime` int(11) NOT NULL DEFAULT '0' COMMENT '开播时长(秒)',
  `credits` int(11) NOT NULL DEFAULT '0' COMMENT '声援值',
  `weekstar` int(11) NOT NULL DEFAULT '0' COMMENT '周星奖励',
  `activity` int(11) NOT NULL DEFAULT '0' COMMENT '活动奖励',
  `exchange` int(11) NOT NULL DEFAULT '0' COMMENT '内兑',
  `withdraw` int(11) NOT NULL DEFAULT '0' COMMENT '提现',
  `addtime` int(11) NOT NULL DEFAULT '0' COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `times` (`times`,`anchorid`),
  KEY `anchorid` (`anchorid`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='主播结算的基本信息';

-- ----------------------------
-- Records of analysis_wages
-- ----------------------------
INSERT INTO `analysis_wages` VALUES ('1', '201709', '14', '0', '0', '1702', '0', '0', '0', '0', '0', '1506478606');
INSERT INTO `analysis_wages` VALUES ('2', '201709', '16', '0', '0', '974', '0', '0', '0', '0', '0', '1506478606');
INSERT INTO `analysis_wages` VALUES ('3', '201709', '17', '0', '0', '1004', '0', '0', '0', '0', '0', '1506478606');
INSERT INTO `analysis_wages` VALUES ('4', '201709', '10000000', '0', '0', '2048', '0', '0', '0', '0', '0', '1506478606');
INSERT INTO `analysis_wages` VALUES ('5', '201709', '10000002', '0', '0', '381', '0', '0', '0', '0', '0', '1506478606');
INSERT INTO `analysis_wages` VALUES ('6', '201709', '10000001', '0', '0', '3992', '0', '0', '0', '0', '0', '1506567960');
INSERT INTO `analysis_wages` VALUES ('7', '201709', '10000016', '0', '0', '1743', '0', '0', '0', '0', '0', '1506717900');
INSERT INTO `analysis_wages` VALUES ('8', '201709', '10000018', '0', '0', '2117', '0', '0', '0', '10500', '0', '1506717900');
INSERT INTO `analysis_wages` VALUES ('9', '201709', '10000020', '0', '0', '3045', '10512', '0', '0', '0', '0', '1506717900');
INSERT INTO `analysis_wages` VALUES ('10', '201709', '10000026', '0', '0', '629', '657930', '0', '0', '0', '0', '1506804300');
INSERT INTO `analysis_wages` VALUES ('11', '201709', '10000025', '0', '0', '0', '431208', '0', '0', '0', '0', '1506804300');
INSERT INTO `analysis_wages` VALUES ('12', '201710', '10000025', '0', '0', '3011', '301388', '0', '0', '0', '0', '1507581900');
INSERT INTO `analysis_wages` VALUES ('13', '201710', '10000026', '0', '0', '1765', '10', '0', '0', '0', '0', '1507581900');
INSERT INTO `analysis_wages` VALUES ('14', '201710', '10000030', '0', '0', '3137', '318480', '0', '0', '0', '0', '1507581900');
INSERT INTO `analysis_wages` VALUES ('15', '201710', '10000033', '0', '4', '63157', '1737760', '100000', '0', '513000', '0', '1507668300');
INSERT INTO `analysis_wages` VALUES ('16', '201710', '10000034', '0', '1', '14311', '876228', '0', '0', '0', '0', '1507668300');
INSERT INTO `analysis_wages` VALUES ('17', '201710', '10000036', '0', '1', '15028', '407620', '0', '0', '1500', '0', '1507668300');
INSERT INTO `analysis_wages` VALUES ('18', '201710', '10000041', '0', '0', '8105', '392062', '0', '0', '0', '0', '1507841100');
INSERT INTO `analysis_wages` VALUES ('19', '201710', '10000046', '0', '0', '1068', '32336', '0', '0', '0', '0', '1507841100');
INSERT INTO `analysis_wages` VALUES ('20', '201710', '10000047', '0', '0', '420', '0', '0', '0', '0', '0', '1507841100');
INSERT INTO `analysis_wages` VALUES ('21', '201710', '10000049', '0', '0', '781', '1182066', '0', '0', '0', '0', '1507841100');
INSERT INTO `analysis_wages` VALUES ('22', '201710', '10000054', '0', '0', '980', '648564', '0', '0', '0', '0', '1507841100');
INSERT INTO `analysis_wages` VALUES ('23', '201710', '10000040', '0', '0', '0', '3980', '0', '0', '0', '0', '1507841100');
INSERT INTO `analysis_wages` VALUES ('24', '201710', '10000042', '0', '1', '18456', '2889641', '0', '0', '0', '0', '1507841100');
INSERT INTO `analysis_wages` VALUES ('25', '201710', '10000045', '0', '2', '30527', '179916', '100000', '0', '117000', '0', '1507841100');
INSERT INTO `analysis_wages` VALUES ('26', '201710', '10000053', '0', '0', '699', '35480', '0', '0', '0', '0', '1507841100');
INSERT INTO `analysis_wages` VALUES ('27', '201710', '10000043', '0', '1', '13736', '103690', '0', '0', '0', '0', '1508186700');
INSERT INTO `analysis_wages` VALUES ('28', '201710', '10000044', '0', '1', '12707', '9930', '0', '0', '0', '0', '1508186700');
INSERT INTO `analysis_wages` VALUES ('29', '201710', '10000060', '0', '0', '2929', '12606', '0', '0', '0', '0', '1508186700');
INSERT INTO `analysis_wages` VALUES ('30', '201710', '10000063', '0', '0', '658', '106230', '0', '0', '0', '0', '1508186700');
INSERT INTO `analysis_wages` VALUES ('31', '201710', '10000064', '0', '1', '12617', '0', '0', '0', '0', '0', '1508186700');
INSERT INTO `analysis_wages` VALUES ('32', '201710', '10000067', '0', '1', '14209', '0', '0', '0', '0', '0', '1508186700');
INSERT INTO `analysis_wages` VALUES ('33', '201710', '10000061', '0', '0', '455', '10512', '0', '0', '22500', '0', '1508186700');
INSERT INTO `analysis_wages` VALUES ('34', '201710', '10000065', '0', '0', '0', '0', '0', '0', '0', '0', '1508186700');
INSERT INTO `analysis_wages` VALUES ('35', '201710', '10000051', '0', '1', '20609', '226894', '0', '0', '0', '0', '1508359500');
INSERT INTO `analysis_wages` VALUES ('36', '201710', '10000072', '0', '0', '1285', '0', '0', '0', '0', '0', '1508359500');
INSERT INTO `analysis_wages` VALUES ('37', '201710', '10000075', '0', '0', '653', '122232', '0', '0', '0', '0', '1508359500');
INSERT INTO `analysis_wages` VALUES ('38', '201710', '10000068', '0', '0', '2759', '0', '0', '0', '0', '0', '1508445900');
INSERT INTO `analysis_wages` VALUES ('39', '201710', '10000038', '0', '1', '7413', '509708', '100000', '0', '1363', '0', '1508791500');
INSERT INTO `analysis_wages` VALUES ('40', '201711', '10000033', '0', '14', '180456', '3113991', '0', '0', '0', '0', '1509569100');
INSERT INTO `analysis_wages` VALUES ('41', '201711', '10000036', '0', '5', '87641', '576256', '0', '0', '0', '0', '1509569100');
INSERT INTO `analysis_wages` VALUES ('42', '201711', '10000051', '0', '6', '67602', '3234640', '0', '0', '0', '0', '1509569100');
INSERT INTO `analysis_wages` VALUES ('43', '201711', '10000038', '0', '4', '54805', '59726', '0', '0', '0', '0', '1510260300');
INSERT INTO `analysis_wages` VALUES ('44', '201711', '10000034', '0', '2', '29390', '898802', '0', '0', '0', '0', '1510692300');
INSERT INTO `analysis_wages` VALUES ('45', '201711', '10000041', '0', '2', '26237', '808657', '0', '0', '0', '0', '1511297100');
INSERT INTO `analysis_wages` VALUES ('46', '201711', '10000043', '0', '0', '0', '0', '0', '0', '0', '0', '1511469900');
INSERT INTO `analysis_wages` VALUES ('47', '201711', '10000054', '0', '0', '0', '0', '0', '0', '0', '0', '1511469900');
INSERT INTO `analysis_wages` VALUES ('48', '201711', '10000065', '0', '0', '0', '0', '0', '0', '0', '0', '1511469900');
INSERT INTO `analysis_wages` VALUES ('49', '201712', '10000033', '0', '4', '64843', '378008', '0', '0', '0', '0', '1512161100');
INSERT INTO `analysis_wages` VALUES ('50', '201712', '10000036', '0', '4', '70527', '1908584', '0', '0', '0', '0', '1512161100');
INSERT INTO `analysis_wages` VALUES ('51', '201712', '10000038', '0', '5', '101829', '2062660', '0', '0', '0', '0', '1512161100');
INSERT INTO `analysis_wages` VALUES ('52', '201712', '10000041', '0', '2', '36001', '141924', '0', '0', '0', '0', '1512161100');
INSERT INTO `analysis_wages` VALUES ('53', '201712', '10000051', '0', '4', '56502', '332758', '0', '0', '0', '0', '1512161100');
INSERT INTO `analysis_wages` VALUES ('54', '201712', '10000034', '0', '1', '50702', '865740', '0', '0', '0', '0', '1512420300');
INSERT INTO `analysis_wages` VALUES ('55', '201712', '10000044', '0', '0', '808', '0', '0', '0', '0', '0', '1512506700');
INSERT INTO `analysis_wages` VALUES ('56', '201712', '10000158', '0', '11', '179621', '11858132', '0', '0', '0', '0', '1512765900');
INSERT INTO `analysis_wages` VALUES ('57', '201712', '10000089', '0', '0', '1193', '0', '0', '0', '0', '0', '1513111500');
INSERT INTO `analysis_wages` VALUES ('58', '201712', '10000193', '0', '1', '30556', '709212', '0', '0', '0', '0', '1513111500');
INSERT INTO `analysis_wages` VALUES ('59', '201712', '10000092', '0', '1', '12131', '0', '0', '0', '0', '0', '1514321100');
INSERT INTO `analysis_wages` VALUES ('60', '201712', '10000077', '0', '0', '1065', '73110', '0', '0', '0', '0', '1514407500');
INSERT INTO `analysis_wages` VALUES ('61', '201801', '10000036', '0', '2', '36988', '67868', '100000', '0', '0', '0', '1514839500');
INSERT INTO `analysis_wages` VALUES ('62', '201801', '10000038', '0', '0', '4937', '908248', '0', '0', '0', '0', '1514925900');
INSERT INTO `analysis_wages` VALUES ('63', '201801', '10000051', '0', '3', '58048', '10576225', '0', '0', '0', '0', '1514925900');
INSERT INTO `analysis_wages` VALUES ('64', '201801', '10000158', '0', '0', '5102', '0', '0', '0', '0', '0', '1514925900');
INSERT INTO `analysis_wages` VALUES ('65', '201801', '10000033', '0', '4', '44740', '182636', '0', '0', '0', '0', '1515012300');
INSERT INTO `analysis_wages` VALUES ('66', '201801', '10000034', '0', '0', '8872', '1276738', '0', '0', '0', '0', '1515012300');
INSERT INTO `analysis_wages` VALUES ('67', '201801', '10000041', '0', '2', '45088', '330372', '0', '0', '0', '0', '1515098700');
INSERT INTO `analysis_wages` VALUES ('68', '201801', '10000077', '0', '1', '13407', '801566', '0', '0', '0', '0', '1515098700');
INSERT INTO `analysis_wages` VALUES ('69', '201801', '10000193', '0', '1', '17251', '3020418', '0', '0', '0', '0', '1515098700');
INSERT INTO `analysis_wages` VALUES ('70', '201801', '10000089', '0', '0', '3964', '0', '0', '0', '0', '0', '1515703500');
INSERT INTO `analysis_wages` VALUES ('71', '201801', '10000040', '0', '1', '9275', '25430300', '0', '0', '0', '0', '1515789900');
INSERT INTO `analysis_wages` VALUES ('72', '201801', '10000204', '0', '0', '2661', '27510', '0', '0', '0', '0', '1515789900');
INSERT INTO `analysis_wages` VALUES ('73', '201801', '10000206', '0', '0', '0', '6', '0', '0', '0', '0', '1516049100');
INSERT INTO `analysis_wages` VALUES ('74', '201801', '10000202', '0', '0', '0', '0', '0', '0', '0', '0', '1516049100');
INSERT INTO `analysis_wages` VALUES ('75', '201801', '10000205', '0', '0', '1668', '115632', '0', '0', '0', '0', '1516308300');
INSERT INTO `analysis_wages` VALUES ('76', '201801', '10000207', '0', '0', '5129', '534888', '0', '0', '0', '0', '1516394700');
INSERT INTO `analysis_wages` VALUES ('77', '201801', '10000045', '0', '0', '0', '0', '0', '0', '0', '0', '1516653900');
INSERT INTO `analysis_wages` VALUES ('78', '201802', '10000034', '0', '0', '1566', '39230', '0', '0', '0', '0', '1519677900');
INSERT INTO `analysis_wages` VALUES ('79', '201802', '10000077', '0', '0', '4337', '0', '0', '0', '0', '0', '1519677900');
INSERT INTO `analysis_wages` VALUES ('80', '201802', '10000038', '0', '0', '0', '1353940', '0', '0', '0', '0', '1519677900');
INSERT INTO `analysis_wages` VALUES ('81', '201802', '10000041', '0', '0', '0', '92572', '0', '0', '0', '0', '1519677900');
INSERT INTO `analysis_wages` VALUES ('82', '201802', '10000051', '0', '0', '0', '237774', '0', '0', '0', '0', '1519677900');
INSERT INTO `analysis_wages` VALUES ('83', '201802', '10000033', '0', '0', '8178', '5433276', '0', '0', '0', '0', '1519764300');
INSERT INTO `analysis_wages` VALUES ('84', '201802', '10000036', '0', '0', '3052', '0', '0', '0', '0', '0', '1519850700');
INSERT INTO `analysis_wages` VALUES ('85', '201803', '10000036', '0', '10', '166060', '2374700', '100000', '0', '0', '0', '1519937100');
INSERT INTO `analysis_wages` VALUES ('86', '201803', '10000041', '0', '1', '25731', '198718', '0', '0', '0', '0', '1520282700');
INSERT INTO `analysis_wages` VALUES ('87', '201803', '10000033', '0', '7', '111639', '8787920', '0', '0', '0', '0', '1520369100');
INSERT INTO `analysis_wages` VALUES ('88', '201803', '10000210', '0', '0', '1457', '9050502', '0', '0', '0', '0', '1520369100');
INSERT INTO `analysis_wages` VALUES ('89', '201803', '10000051', '0', '7', '97386', '1831344', '0', '0', '0', '0', '1520541900');
INSERT INTO `analysis_wages` VALUES ('90', '201803', '10000077', '0', '1', '12903', '6610', '0', '0', '0', '0', '1520541900');
INSERT INTO `analysis_wages` VALUES ('91', '201803', '10000034', '0', '0', '23639', '1032', '0', '0', '0', '0', '1520628300');
INSERT INTO `analysis_wages` VALUES ('92', '201803', '10000038', '0', '4', '40062', '47235780', '100000', '0', '0', '0', '1520887500');
INSERT INTO `analysis_wages` VALUES ('93', '201803', '10000193', '0', '0', '0', '2000', '0', '0', '0', '0', '1521146700');
INSERT INTO `analysis_wages` VALUES ('94', '201803', '10000209', '0', '0', '306', '0', '0', '0', '0', '0', '1521492300');
INSERT INTO `analysis_wages` VALUES ('95', '201803', '10000216', '0', '1', '18183', '6600', '0', '0', '0', '0', '1521492300');
INSERT INTO `analysis_wages` VALUES ('96', '201803', '10000217', '0', '0', '12777', '0', '0', '0', '0', '0', '1521492300');
INSERT INTO `analysis_wages` VALUES ('97', '201804', '10000216', '0', '1', '18139', '2640151', '0', '0', '0', '0', '1522701900');
INSERT INTO `analysis_wages` VALUES ('98', '201804', '10000041', '0', '1', '27254', '268533', '0', '0', '0', '0', '1522701900');
INSERT INTO `analysis_wages` VALUES ('99', '201804', '10000033', '0', '2', '22213', '2749790', '0', '0', '0', '0', '1522788300');
INSERT INTO `analysis_wages` VALUES ('100', '201804', '10000051', '0', '0', '2541', '12321146', '0', '0', '0', '0', '1523393100');
INSERT INTO `analysis_wages` VALUES ('101', '201804', '10000217', '0', '1', '30864', '288', '0', '0', '0', '0', '1523652300');
INSERT INTO `analysis_wages` VALUES ('102', '201804', '10000034', '0', '0', '5194', '147168', '0', '0', '0', '0', '1523738700');
INSERT INTO `analysis_wages` VALUES ('103', '201804', '10000036', '0', '0', '5785', '12507482', '0', '0', '0', '0', '1523738700');
INSERT INTO `analysis_wages` VALUES ('104', '201804', '10000193', '0', '0', '2019', '0', '0', '0', '0', '0', '1523738700');
INSERT INTO `analysis_wages` VALUES ('105', '201804', '10000038', '0', '1', '12139', '136656', '0', '0', '0', '0', '1523738700');
INSERT INTO `analysis_wages` VALUES ('106', '201804', '10000019', '0', '0', '0', '150000', '0', '0', '0', '0', '1524170700');
INSERT INTO `analysis_wages` VALUES ('107', '201804', '10000210', '0', '0', '942', '0', '0', '0', '0', '0', '1524602700');
INSERT INTO `analysis_wages` VALUES ('108', '201804', '10000213', '0', '0', '3811', '1353312', '0', '0', '0', '0', '1524602700');
INSERT INTO `analysis_wages` VALUES ('109', '201805', '10000036', '0', '0', '7235', '0', '0', '0', '0', '0', '1525466700');
INSERT INTO `analysis_wages` VALUES ('110', '201805', '10000033', '0', '0', '7298', '150004', '0', '0', '0', '0', '1526417100');
INSERT INTO `analysis_wages` VALUES ('111', '201805', '10000051', '0', '0', '1592', '0', '0', '0', '0', '0', '1526417100');
INSERT INTO `analysis_wages` VALUES ('112', '201805', '10000041', '0', '0', '7024', '152094', '0', '0', '0', '0', '1526676300');
INSERT INTO `analysis_wages` VALUES ('113', '201805', '10000034', '0', '0', '863', '300552', '0', '0', '0', '0', '1526762700');
INSERT INTO `analysis_wages` VALUES ('114', '201805', '10000220', '0', '0', '2760', '294128', '0', '0', '0', '0', '1526762700');
INSERT INTO `analysis_wages` VALUES ('115', '201805', '10000216', '0', '0', '0', '3', '0', '0', '0', '0', '1527281100');
INSERT INTO `analysis_wages` VALUES ('116', '201805', '10000217', '0', '0', '0', '20', '0', '0', '0', '0', '1527540300');
