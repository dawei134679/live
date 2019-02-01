/*
Navicat MySQL Data Transfer

Source Server         : admin
Source Server Version : 50639
Source Host           : 192.168.20.251:3306
Source Database       : zhu_web

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2018-05-29 09:39:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `mobile_guide_img`
-- ----------------------------
DROP TABLE IF EXISTS `mobile_guide_img`;
CREATE TABLE `mobile_guide_img` (
  `id` smallint(4) unsigned NOT NULL AUTO_INCREMENT,
  `imgsrc` varchar(200) NOT NULL COMMENT '图片地址',
  `link` varchar(100) NOT NULL DEFAULT '' COMMENT '跳转地址',
  `addtime` int(10) unsigned NOT NULL COMMENT '创建时间',
  `sort` smallint(4) unsigned NOT NULL DEFAULT '0' COMMENT '排序',
  `showtime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '显示时间(单位：秒)',
  `status` smallint(2) unsigned NOT NULL DEFAULT '1' COMMENT '引导图开关状态  1为开启   0为关闭',
  PRIMARY KEY (`id`),
  KEY `sort` (`sort`),
  KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mobile_guide_img
-- ----------------------------

-- ----------------------------
-- Table structure for `system_notice`
-- ----------------------------
DROP TABLE IF EXISTS `system_notice`;
CREATE TABLE `system_notice` (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `content` varchar(200) NOT NULL DEFAULT '' COMMENT '系统私信内容',
  `url` varchar(255) NOT NULL DEFAULT '' COMMENT '跳转地址',
  `sendtime` int(10) NOT NULL DEFAULT '0' COMMENT '发送时间',
  `addtime` int(10) NOT NULL DEFAULT '0' COMMENT '添加时间',
  `adminid` int(11) NOT NULL DEFAULT '0' COMMENT '操作者UID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='系统私信公告，默认缓存有效时间 都是7天  7*24*3600=604800';

-- ----------------------------
-- Records of system_notice
-- ----------------------------
INSERT INTO `system_notice` VALUES ('1', '羊驼测试羊驼测试羊驼测试', 'http://www.baidu.com', '1507599536', '1507599536', '1');
INSERT INTO `system_notice` VALUES ('2', 'derfgdfgdf', 'dfgdfg', '1507599763', '1507599763', '1');
INSERT INTO `system_notice` VALUES ('3', 'dsfsdf', 'sfdsf', '1507600188', '1507600188', '1');
INSERT INTO `system_notice` VALUES ('4', '公告：羊驼直播将改版为麦芽直播～', 'http://zhuanzhuanzhibo.com', '1511572473', '1511572473', '1');
INSERT INTO `system_notice` VALUES ('5', '麦芽直播', '123', '1515980735', '1515980735', '1');

-- ----------------------------
-- Table structure for `web_banner`
-- ----------------------------
DROP TABLE IF EXISTS `web_banner`;
CREATE TABLE `web_banner` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `picUrl` varchar(255) NOT NULL COMMENT '图片地址',
  `jumpUrl` varchar(255) NOT NULL COMMENT '跳转地址',
  `anchorUid` int(11) NOT NULL DEFAULT '0' COMMENT '跳转房间UID',
  `webPicUrl` varchar(255) NOT NULL DEFAULT '' COMMENT 'web地址',
  `sort` int(10) unsigned DEFAULT '0' COMMENT '排序',
  `startShow` int(10) DEFAULT '0' COMMENT '开始展示时间',
  `endShow` int(10) DEFAULT '0' COMMENT '结束展示时间',
  `switch` int(11) DEFAULT '0' COMMENT '=0 不显示 =1 显示',
  `createAT` int(10) unsigned DEFAULT NULL COMMENT '添加时间',
  `type` int(2) NOT NULL DEFAULT '1' COMMENT '=1首页轮播图 =0开屏广告图',
  `platform` int(2) DEFAULT '3' COMMENT '平台（1 android, 2 ios, 3 all）',
  `name` varchar(30) DEFAULT NULL,
  `roomId` int(11) NOT NULL DEFAULT '0' COMMENT '跳转的房间ID',
  `roomType` int(3) NOT NULL DEFAULT '0' COMMENT '=0 直播间 =1游戏',
  PRIMARY KEY (`id`),
  KEY `switch` (`switch`,`sort`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='banner图表';

-- ----------------------------
-- Records of web_banner
-- ----------------------------
INSERT INTO `web_banner` VALUES ('1', 'http://owvwocjrz.bkt.clouddn.com/oX3XVoXmOXZSJAsY1507607646609.png', '', '0', '', '1', '1507219200', '1519574400', '1', '1507603149', '0', '9', '活动1', '0', '0');
INSERT INTO `web_banner` VALUES ('11', 'http://p0gy7d6cg.bkt.clouddn.com/VtFAadMxbhJsG1Zk1522806174184.png', 'http://192.168.10.251:8080/TinyPigWebServer/html/ad/banner3.html', '0', 'http://p0gy7d6cg.bkt.clouddn.com/u8nl3kXHusfTe6og1522806175211.png', '0', '1512316800', '1525104000', '1', '1512446654', '1', '9', '1', '0', '0');
INSERT INTO `web_banner` VALUES ('12', 'http://p0gy7d6cg.bkt.clouddn.com/pFx6K7uMR4aFBF6I1522806204006.png', 'http://192.168.10.251:8080/TinyPigWebServer/html/ad/banner2.html', '0', 'http://p0gy7d6cg.bkt.clouddn.com/jdWCTWSCvxaXC5vI1522806204461.png', '0', '1512057600', '1525104000', '1', '1512446672', '1', '9', '2', '0', '0');
INSERT INTO `web_banner` VALUES ('13', 'http://p0gy7d6cg.bkt.clouddn.com/jVhDnTwplCulaqfV1522809698417.png', 'http://192.168.10.251:8080/TinyPigWebServer/html/ad/banner1.html', '0', 'http://p0gy7d6cg.bkt.clouddn.com/SWgxcGcvdUPrrzFB1522745198545.png', '0', '1512057600', '1525104000', '1', '1512446695', '1', '9', '3', '0', '0');

-- ----------------------------
-- Table structure for `web_channel`
-- ----------------------------
DROP TABLE IF EXISTS `web_channel`;
CREATE TABLE `web_channel` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `channelName` varchar(30) NOT NULL DEFAULT '' COMMENT '渠道名',
  `channelCode` varchar(20) NOT NULL DEFAULT '' COMMENT '渠道代码',
  `isvalid` int(11) NOT NULL DEFAULT '1' COMMENT '=1有效 其他无效',
  `adminid` int(11) NOT NULL DEFAULT '0' COMMENT '添加者UID',
  `addtime` int(11) NOT NULL DEFAULT '0' COMMENT '添加时间',
  `edittime` int(11) NOT NULL DEFAULT '0' COMMENT '修改时间',
  `loginport` int(11) NOT NULL DEFAULT '0' COMMENT '登录端',
  `platform` int(11) NOT NULL DEFAULT '0' COMMENT '平台',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of web_channel
-- ----------------------------

-- ----------------------------
-- Table structure for `web_feedback`
-- ----------------------------
DROP TABLE IF EXISTS `web_feedback`;
CREATE TABLE `web_feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(10) unsigned NOT NULL,
  `cls` int(11) NOT NULL COMMENT '反馈类别',
  `des` varchar(255) NOT NULL COMMENT '反馈描述',
  `mobile` varchar(11) DEFAULT '' COMMENT '手机号',
  `createAt` int(11) NOT NULL COMMENT '反馈时间',
  PRIMARY KEY (`id`),
  KEY `type` (`cls`,`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of web_feedback
-- ----------------------------

-- ----------------------------
-- Table structure for `web_recommend_anchor`
-- ----------------------------
DROP TABLE IF EXISTS `web_recommend_anchor`;
CREATE TABLE `web_recommend_anchor` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL COMMENT '推荐主播',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '越大越靠前',
  `isvalid` int(2) NOT NULL DEFAULT '1' COMMENT '=1 有效 =0 无效',
  `adminname` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人名',
  `adminid` int(11) NOT NULL COMMENT '操作者UID',
  `addtime` int(11) NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `isvalid` (`isvalid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动态广场主播推荐列表';

-- ----------------------------
-- Records of web_recommend_anchor
-- ----------------------------

-- ----------------------------
-- Table structure for `web_room_chat`
-- ----------------------------
DROP TABLE IF EXISTS `web_room_chat`;
CREATE TABLE `web_room_chat` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL DEFAULT '' COMMENT '推送内容',
  `starttime` int(11) NOT NULL DEFAULT '0' COMMENT '开始时间',
  `endtime` int(11) NOT NULL DEFAULT '0' COMMENT '结束时间',
  `interval` int(3) NOT NULL DEFAULT '15' COMMENT '间隔显示时间[15分钟]',
  `isvalid` int(2) NOT NULL DEFAULT '1' COMMENT '=1有效 =0无效',
  `adminid` int(11) NOT NULL DEFAULT '0' COMMENT '操作人UID',
  `addtime` int(11) NOT NULL DEFAULT '0' COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `indexTime` (`starttime`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='直播间 聊天信息推送';

-- ----------------------------
-- Records of web_room_chat
-- ----------------------------
INSERT INTO `web_room_chat` VALUES ('1', '测试轮播啦-test', '1507618800', '1508050800', '15', '0', '1', '1506062523');
INSERT INTO `web_room_chat` VALUES ('2', 'sdfasdf', '1506592519', '1509414057', '15', '0', '1', '1506678930');

-- ----------------------------
-- Table structure for `web_ver`
-- ----------------------------
DROP TABLE IF EXISTS `web_ver`;
CREATE TABLE `web_ver` (
  `iosVer` varchar(50) NOT NULL DEFAULT '' COMMENT 'iosç‰ˆæœ¬å·',
  `iosAt` int(10) NOT NULL DEFAULT '0' COMMENT 'iosæœ€åŽæ—¶é—´',
  `androidVer` varchar(50) NOT NULL DEFAULT '' COMMENT 'androidç‰ˆæœ¬å·',
  `androidAt` int(10) NOT NULL DEFAULT '0' COMMENT 'androidæœ€åŽç‰ˆæœ¬æ—¶é—´'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of web_ver
-- ----------------------------
