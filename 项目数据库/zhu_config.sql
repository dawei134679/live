/*
Navicat MySQL Data Transfer

Source Server         : admin
Source Server Version : 50639
Source Host           : 192.168.20.251:3306
Source Database       : zhu_config

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2018-05-29 09:35:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `config_giftlist`
-- ----------------------------
DROP TABLE IF EXISTS `config_giftlist`;
CREATE TABLE `config_giftlist` (
  `gid` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '礼物id',
  `gname` varchar(100) NOT NULL DEFAULT '' COMMENT '礼物名称',
  `gprice` int(10) NOT NULL COMMENT '礼物价格',
  `wealth` int(10) NOT NULL DEFAULT '0' COMMENT '用户财富值',
  `credit` int(10) NOT NULL DEFAULT '0' COMMENT '声援值',
  `charm` int(10) NOT NULL DEFAULT '0' COMMENT '魅力值',
  `gcover` varchar(100) NOT NULL DEFAULT '' COMMENT '礼物封面图名称',
  `gtype` int(5) NOT NULL DEFAULT '0' COMMENT '礼物类型  =1礼物消息连 =2动态图片(半屏 带离子效果) =3动画效果(半瓶 不带离子效果) =4动画全屏 =5半屏带文字效果',
  `gpctype` int(1) NOT NULL DEFAULT '1' COMMENT 'PC端类型 =1静态 =2SWF',
  `gframeurl` varchar(255) NOT NULL DEFAULT '' COMMENT '小动画zip地址',
  `gframeurlios` varchar(255) NOT NULL DEFAULT '' COMMENT 'ios动画zip地址',
  `simgs` int(5) NOT NULL COMMENT '小动画序列帧数',
  `bimgs` int(5) NOT NULL COMMENT '大动画序列帧数',
  `pimgs` int(5) NOT NULL COMMENT '离子图片名字数组数',
  `gnumtype` varchar(255) NOT NULL DEFAULT '' COMMENT '礼物可先择数量',
  `gduration` float(2,1) NOT NULL COMMENT '小礼物展示一遍总时间',
  `gver` int(5) NOT NULL DEFAULT '1' COMMENT '礼物版本',
  `sver` int(5) NOT NULL DEFAULT '1' COMMENT '礼物资源版本',
  `isshow` tinyint(1) NOT NULL DEFAULT '0' COMMENT '=0不显示 =1显示',
  `isvalid` tinyint(1) NOT NULL DEFAULT '1' COMMENT '=0无效 =1有效',
  `gsort` int(5) NOT NULL DEFAULT '99' COMMENT '排序',
  `createAt` int(10) NOT NULL COMMENT '添加时间',
  `type` int(2) NOT NULL DEFAULT '1' COMMENT '=1礼物 =2喇叭 =3弹幕 =4人气猪 =5时长物品',
  `subtype` int(2) NOT NULL DEFAULT '0' COMMENT '礼物类型(0=>普通礼物 1=>弹幕 2=>喇叭 3=>VIP 31=>VIP徽章 4=>贵族 5=>座驾 6=>徽章 7=>守护 71=>守护徽章 8=>商城道具)',
  `icon` varchar(100) DEFAULT '' COMMENT '礼物图标',
  `skin` int(11) NOT NULL COMMENT '礼物引用皮肤,等于自己的gid就代表强制更新皮肤资源',
  `useDuration` int(6) DEFAULT '0' COMMENT '时效物品有效时间单位为天',
  `category` int(2) NOT NULL DEFAULT '0' COMMENT '=1热门 =2豪华 =3活动 =4专属',
  PRIMARY KEY (`gid`)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of config_giftlist
-- ----------------------------
INSERT INTO `config_giftlist` VALUES ('1', '弹幕', '1', '100', '0', '0', '', '0', '1', '0', '0', '0', '0', '0', '0', '0.0', '5', '3', '1', '1', '99', '1466521362', '1', '1', 'http://oan9o31wd.bkt.clouddn.com/danmu.png', '1', '0', '0');
INSERT INTO `config_giftlist` VALUES ('2', '玫瑰花', '1', '100', '10', '20', 's_2_11', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/meiguihua618.zip', 'http://o8uf793ar.bkt.clouddn.com/meiguihua618.zip', '11', '0', '3', '1,99,300,520,1314,3344,9999', '1.1', '19', '2', '0', '0', '99', '1466521362', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/meiguihua.png', '2', '0', '1');
INSERT INTO `config_giftlist` VALUES ('3', '猪头', '2', '200', '20', '0', 's_3_15', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/zhutou618.zip', 'http://o8uf793ar.bkt.clouddn.com/zhutou618.zip', '16', '0', '1', '1,99,300,520,1314,3344,9999', '2.0', '26', '2', '0', '0', '30', '1466521362', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/zhutou.png', '3', '0', '1');
INSERT INTO `config_giftlist` VALUES ('4', '猫咪', '5', '500', '50', '0', 's_4_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/maomi618.zip', 'http://o8uf793ar.bkt.clouddn.com/maomi618.zip', '12', '0', '1', '1,99,300,520,1314,3344,9999', '1.1', '28', '2', '0', '0', '99', '1466521362', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/maomi.png', '4', '0', '1');
INSERT INTO `config_giftlist` VALUES ('5', '金砖', '6888', '6888', '0', '2000', 's_5_19', '2', '2', 'http://o8uf793ar.bkt.clouddn.com/jinzhuan618.zip', 'http://o8uf793ar.bkt.clouddn.com/jinzhuan618.zip', '20', '23', '3', '1,99,300,520,1314,3344,9999', '2.2', '52', '3', '1', '1', '5', '1466521362', '2', '9', 'http://oan9o31wd.bkt.clouddn.com/jinzhuan.png', '5', '0', '2');
INSERT INTO `config_giftlist` VALUES ('6', '留声机', '6888', '6888', '0', '6888', 's_6_7', '2', '2', 'http://o8uf793ar.bkt.clouddn.com/liushengji620.zip', 'http://o8uf793ar.bkt.clouddn.com/liushengji620.zip', '8', '22', '9', '1,99,300,520,1314,3344,9999', '1.0', '36', '2', '1', '1', '99', '1466521362', '2', '9', 'http://oan9o31wd.bkt.clouddn.com/liushengji.png', '6', '0', '2');
INSERT INTO `config_giftlist` VALUES ('7', '心动', '1', '100', '10', '0', 's_7_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/xindong618.zip', 'http://o8uf793ar.bkt.clouddn.com/xindong618.zip', '8', '0', '1', '1,99,300,520,1314,3344,9999', '0.9', '13', '2', '0', '0', '99', '1466521362', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/xindong.png', '7', '0', '1');
INSERT INTO `config_giftlist` VALUES ('8', '钻戒', '66', '6600', '660', '0', 's_8_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/zuanjie701.zip', 'http://o8uf793ar.bkt.clouddn.com/zuanjie701.zip', '12', '0', '1', '1,99,300,520,1314,3344,9999', '1.2', '28', '3', '0', '0', '99', '1466521362', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/zuanjie.png', '8', '0', '1');
INSERT INTO `config_giftlist` VALUES ('9', '钻石', '11888', '11888', '0', '11888', 's_9_13', '2', '2', 'http://o8uf793ar.bkt.clouddn.com/zuanshi618.zip', 'http://o8uf793ar.bkt.clouddn.com/zuanshi618.zip', '20', '32', '3', '1,99,300,520,1314,3344,9999', '2.5', '22', '2', '1', '1', '8', '1466521363', '2', '9', 'http://oan9o31wd.bkt.clouddn.com/zuanshi.png', '9', '0', '2');
INSERT INTO `config_giftlist` VALUES ('10', '兰博基尼', '3000', '300000', '30000', '0', 's_10_9', '3', '2', 'http://o8uf793ar.bkt.clouddn.com/lanbojini618.zip', 'http://o8uf793ar.bkt.clouddn.com/lanbojini618.zip', '9', '56', '0', '1,99,300,520,1314,3344,9999', '1.1', '14', '2', '0', '0', '16', '1466521363', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/lanbojini.png', '10', '0', '2');
INSERT INTO `config_giftlist` VALUES ('11', '私人游艇', '8888', '888800', '88880', '0', 's_11_7', '3', '2', 'http://o8uf793ar.bkt.clouddn.com/youting618.zip', 'http://o8uf793ar.bkt.clouddn.com/youting618.zip', '7', '59', '0', '1,99,300,520,1314,3344,9999', '0.9', '16', '2', '0', '0', '17', '1466521363', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/sirenyouting.png', '11', '0', '2');
INSERT INTO `config_giftlist` VALUES ('13', '棒棒糖', '68', '68', '0', '68', 's_13_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/bangbangtang618.zip', 'http://o8uf793ar.bkt.clouddn.com/bangbangtang618.zip', '10', '0', '1', '1,99,300,520,1314,3344,9999', '1.0', '31', '2', '1', '1', '2', '1466521363', '2', '9', 'http://oan9o31wd.bkt.clouddn.com/bangbangtang.png', '13', '0', '1');
INSERT INTO `config_giftlist` VALUES ('14', '小黄鸡', '2', '200', '20', '0', 's_14_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/xiaohuangji618.zip', 'http://o8uf793ar.bkt.clouddn.com/xiaohuangji618.zip', '5', '0', '1', '1,99,300,520,1314,3344,9999', '0.8', '24', '2', '0', '0', '29', '1466521363', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/xiaohuangji.png', '14', '0', '1');
INSERT INTO `config_giftlist` VALUES ('15', '亲亲', '5', '500', '50', '0', 's_15_18', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/qinqin618.zip', 'http://o8uf793ar.bkt.clouddn.com/qinqin618.zip', '21', '0', '1', '1,99,300,520,1314,3344,9999', '1.9', '29', '2', '0', '0', '11', '1466521363', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/qinqin.png', '15', '0', '1');
INSERT INTO `config_giftlist` VALUES ('16', '情书', '68', '68', '0', '68', 's_16_14', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/qinshu1231.zip', 'http://o8uf793ar.bkt.clouddn.com/qinshu1231.zip', '14', '0', '6', '1,99,300,520,1314,3344,9999', '1.1', '48', '5', '1', '1', '22', '1466521363', '2', '9', 'http://o8uf793ar.bkt.clouddn.com/qinshu1230.png', '16', '0', '1');
INSERT INTO `config_giftlist` VALUES ('17', '跑车', '880', '88000', '8800', '0', 's_17_1', '3', '2', 'http://o8uf793ar.bkt.clouddn.com/paoche618.zip', 'http://o8uf793ar.bkt.clouddn.com/paoche618.zip', '19', '64', '0', '1,99,300,520,1314,3344,9999', '2.4', '22', '2', '0', '0', '99', '1466521363', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/paoche.png', '17', '0', '2');
INSERT INTO `config_giftlist` VALUES ('18', '飞机', '6666', '666600', '66660', '0', 's_18_1', '3', '2', 'http://o8uf793ar.bkt.clouddn.com/feiji618.zip', 'http://o8uf793ar.bkt.clouddn.com/feiji618.zip', '13', '69', '0', '1,99,300,520,1314,3344,9999', '1.4', '17', '3', '0', '0', '27', '1466521363', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/feiji.png', '18', '0', '2');
INSERT INTO `config_giftlist` VALUES ('19', '私人海岛', '13140', '1314000', '131400', '0', 's_19_8', '3', '2', 'http://o8uf793ar.bkt.clouddn.com/haidao618.zip', 'http://o8uf793ar.bkt.clouddn.com/haidao618.zip', '11', '136', '0', '1,99,300,520,1314,3344,9999', '1.0', '12', '2', '0', '0', '18', '1466521363', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/sirenhaidao.png', '19', '0', '2');
INSERT INTO `config_giftlist` VALUES ('20', '粽子', '5', '500', '50', '0', 's_20_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/zongzi.zip', 'http://o8uf793ar.bkt.clouddn.com/zongzi.zip', '7', '3', '0', '1,99,300,520,1314,3344,9999', '0.0', '2', '2', '0', '0', '99', '1466521363', '2', '0', '', '20', '0', '0');
INSERT INTO `config_giftlist` VALUES ('21', '火箭', '9999', '999900', '99990', '0', 's_21_1', '4', '2', 'http://o8uf793ar.bkt.clouddn.com/huojian618.zip', 'http://o8uf793ar.bkt.clouddn.com/huojian618.zip', '14', '142', '0', '1,99,300,520,1314,3344,9999', '1.4', '12', '2', '0', '0', '28', '1466521363', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/huojian.png', '21', '0', '2');
INSERT INTO `config_giftlist` VALUES ('22', '流星雨', '688', '688', '0', '688', 's_22_1', '4', '2', 'http://o8uf793ar.bkt.clouddn.com/liuxingyu618.zip', 'http://o8uf793ar.bkt.clouddn.com/liuxingyu618.zip', '5', '150', '0', '1,99,300,520,1314,3344,9999', '0.5', '35', '2', '1', '1', '12', '1466521364', '2', '9', 'http://oan9o31wd.bkt.clouddn.com/liuxingyu.png', '22', '0', '2');
INSERT INTO `config_giftlist` VALUES ('23', '喇叭', '188', '18800', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0.0', '5', '2', '1', '1', '99', '0', '1', '2', 'http://oan9o31wd.bkt.clouddn.com/laba.png', '23', '0', '0');
INSERT INTO `config_giftlist` VALUES ('24', '小萌猪', '0', '100', '0', '0', 's_29_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/renqizhu726.zip', 'http://o8uf793ar.bkt.clouddn.com/renqizhu726.zip', '25', '0', '0', '1,99,300,520,1314,3344,9999', '1.9', '5', '2', '0', '0', '99', '0', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/xiaomengzhu.png', '29', '0', '0');
INSERT INTO `config_giftlist` VALUES ('25', '星票', '1', '100', '10', '0', 's_25_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/xingpiao702.zip', 'http://o8uf793ar.bkt.clouddn.com/xingpiao702.zip', '18', '0', '1', '1,99,300,520,1314,3344,9999', '1.8', '24', '2', '0', '0', '99', '0', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/xingpiao.png', '25', '0', '1');
INSERT INTO `config_giftlist` VALUES ('26', '甲壳虫', '660', '66000', '6600', '0', 's_26_1', '3', '2', 'http://o8uf793ar.bkt.clouddn.com/jiakechong704.zip', 'http://o8uf793ar.bkt.clouddn.com/jiakechong704.zip', '18', '56', '0', '1,99,300,520,1314,3344,9999', '1.8', '33', '4', '0', '0', '6', '0', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/jiakechong.png', '26', '0', '2');
INSERT INTO `config_giftlist` VALUES ('27', '心动', '288', '288', '1', '288', 's_27_1', '1', '1', 'http://image-default.maiyalive.com/xindong618_27.zip', 'http://image-default.maiyalive.com/xindong618_27.zip', '8', '0', '1', '1,99,300,520,1314,3344,9999', '0.9', '12', '3', '1', '1', '999', '0', '2', '9', 'http://oan9o31wd.bkt.clouddn.com/xindong.png', '27', '0', '1');
INSERT INTO `config_giftlist` VALUES ('28', '甲壳虫', '688', '688', '0', '688', 's_28_1', '3', '2', 'http://image-default.maiyalive.com/jiakechong704_28.zip', 'http://image-default.maiyalive.com/jiakechong704_28.zip', '18', '56', '0', '1,99,300,520,1314,3344,9999', '1.8', '28', '5', '1', '1', '999', '0', '2', '9', 'http://oan9o31wd.bkt.clouddn.com/jiakechong.png', '28', '0', '0');
INSERT INTO `config_giftlist` VALUES ('29', '守护人气猪', '0', '100', '0', '0', 's_29_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/renqizhu726.zip', 'http://o8uf793ar.bkt.clouddn.com/renqizhu726.zip', '25', '0', '0', '1,99,300,520,1314,3344,9999', '1.9', '8', '1', '0', '0', '99', '0', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/xiaomengzhu.png', '29', '0', '0');
INSERT INTO `config_giftlist` VALUES ('30', '首充徽章', '0', '0', '0', '0', '', '0', '1', '0', '0', '0', '0', '0', '0', '0.0', '3', '2', '0', '1', '99', '0', '3', '6', 'http://oan9o31wd.bkt.clouddn.com/shouchonghuizhang919.png', '30', '3', '0');
INSERT INTO `config_giftlist` VALUES ('31', '首充徽章', '0', '0', '0', '0', '', '0', '1', '0', '0', '0', '0', '0', '0', '0.0', '3', '2', '0', '1', '99', '0', '3', '6', 'http://oan9o31wd.bkt.clouddn.com/shouchonghuizhang919.png', '30', '7', '0');
INSERT INTO `config_giftlist` VALUES ('32', '幸运玫瑰', '68', '68', '0', '68', 's_32_11', '1', '1', 'http://image-default.maiyalive.com/meiguihua618_32.zip', 'http://image-default.maiyalive.com/meiguihua618_32.zip', '11', '0', '3', '1,99,300,520,1314,3344,9999', '1.1', '30', '3', '1', '1', '19', '0', '2', '9', 'http://oan9o31wd.bkt.clouddn.com/xymeigui.png', '32', '0', '3');
INSERT INTO `config_giftlist` VALUES ('33', '幸运啤酒', '2', '200', '12', '0', 's_33_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/pijiu725.zip', 'http://o8uf793ar.bkt.clouddn.com/pijiu725.zip', '13', '0', '3', '1,99,300,520,1314,3344,9999', '0.9', '17', '1', '0', '0', '31', '0', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/xypijiu.png', '33', '0', '3');
INSERT INTO `config_giftlist` VALUES ('34', '幸运流氓蕉', '1', '100', '6', '0', 's_34_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/liumangjiao725.zip', 'http://o8uf793ar.bkt.clouddn.com/liumangjiao725.zip', '20', '0', '3', '1,99,300,520,1314,3344,9999', '1.4', '11', '1', '0', '0', '1', '0', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/xyliumengjiao.png', '34', '0', '3');
INSERT INTO `config_giftlist` VALUES ('35', '猫咪', '188', '188', '0', '188', 's_35_1', '1', '1', 'http://image-default.maiyalive.com/maomi618_35.zip', 'http://image-default.maiyalive.com/maomi618_35.zip', '12', '0', '1', '1,99,300,520,1314,3344,9999', '1.1', '17', '3', '1', '1', '999', '0', '2', '9', 'http://oan9o31wd.bkt.clouddn.com/maomi.png', '35', '0', '0');
INSERT INTO `config_giftlist` VALUES ('36', '猪头', '2', '200', '2', '0', 's_3_15', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/zhutou618.zip', 'http://o8uf793ar.bkt.clouddn.com/zhutou618.zip', '16', '0', '1', '1,99,300,520,1314,3344,9999', '2.0', '10', '2', '0', '0', '999', '0', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/zhutou.png', '3', '0', '0');
INSERT INTO `config_giftlist` VALUES ('37', '奥运金牌', '5', '500', '50', '0', 's_37_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/jinpai805.zip', 'http://o8uf793ar.bkt.clouddn.com/jinpai805.zip', '13', '0', '3', '1,99,300,520,1314,3344,9999', '1.3', '6', '2', '0', '0', '99', '0', '2', '0', '', '37', '0', '0');
INSERT INTO `config_giftlist` VALUES ('38', '中国加油', '1', '100', '10', '0', 's_38_10', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/chinajiayou.zip', 'http://o8uf793ar.bkt.clouddn.com/chinajiayou.zip', '12', '0', '3', '1,99,300,520,1314,3344,9999', '1.2', '6', '1', '0', '0', '99', '0', '2', '0', '', '38', '0', '0');
INSERT INTO `config_giftlist` VALUES ('39', '喜鹊', '2', '200', '20', '0', 's_39_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/xique803.zip', 'http://o8uf793ar.bkt.clouddn.com/xique803.zip', '13', '0', '3', '1,99,300,520,1314,3344,9999', '1.3', '5', '1', '0', '0', '99', '0', '2', '0', '', '39', '0', '0');
INSERT INTO `config_giftlist` VALUES ('40', '鹊桥', '7', '700', '70', '70', 's_40_1', '3', '1', 'http://o8uf793ar.bkt.clouddn.com/queqiao803.zip', 'http://o8uf793ar.bkt.clouddn.com/queqiao803.zip', '15', '75', '0', '0', '1.5', '7', '1', '0', '0', '999', '0', '2', '0', '', '40', '0', '0');
INSERT INTO `config_giftlist` VALUES ('41', '奥运徽章', '0', '0', '0', '0', '', '0', '1', '0', '0', '0', '0', '0', '0', '0.0', '2', '2', '0', '0', '99', '0', '3', '6', 'http://oan9o31wd.bkt.clouddn.com/jinpaixz.png', '41', '30', '0');
INSERT INTO `config_giftlist` VALUES ('42', '中奖特效', '0', '0', '0', '0', '', '5', '2', 'http://o8uf793ar.bkt.clouddn.com/jiangpig805.zip', 'http://o8uf793ar.bkt.clouddn.com/jiangpig805.zip', '0', '47', '0', '0', '3.2', '5', '4', '0', '0', '99', '0', '2', '0', '', '42', '0', '0');
INSERT INTO `config_giftlist` VALUES ('43', '白金VIP', '350000', '350000', '0', '0', '', '0', '1', '0', '0', '0', '0', '0', '0', '0.0', '4', '2', '0', '1', '99', '0', '3', '3', 'http://oan9o31wd.bkt.clouddn.com/baijinvip.png', '43', '0', '0');
INSERT INTO `config_giftlist` VALUES ('44', '钻石VIP', '500000', '500000', '0', '0', '', '0', '1', '0', '0', '0', '0', '0', '0', '0.0', '4', '2', '0', '1', '99', '0', '3', '3', 'http://oan9o31wd.bkt.clouddn.com/zuanshivip.png', '44', '0', '0');
INSERT INTO `config_giftlist` VALUES ('45', '白银守护', '150000', '150000', '150000', '0', '', '0', '1', '0', '0', '0', '0', '0', '0', '0.0', '4', '2', '0', '1', '99', '0', '3', '7', 'http://oan9o31wd.bkt.clouddn.com/qishi.png', '45', '0', '0');
INSERT INTO `config_giftlist` VALUES ('46', '黄金守护', '300000', '300000', '300000', '0', '', '0', '1', '0', '0', '0', '0', '0', '0', '0.0', '4', '2', '0', '1', '99', '0', '3', '7', 'http://oan9o31wd.bkt.clouddn.com/wangzi.png', '46', '0', '0');
INSERT INTO `config_giftlist` VALUES ('47', '白天使座驾', '0', '0', '0', '0', '', '5', '2', 'http://oxoutqnql.bkt.clouddn.com/xinbaitianshi_01.zip', 'http://oxoutqnql.bkt.clouddn.com/xinbaitianshi_01.zip', '0', '82', '0', '0', '0.0', '7', '5', '0', '1', '99', '0', '3', '5', '', '47', '0', '0');
INSERT INTO `config_giftlist` VALUES ('48', '黄天使座驾', '0', '0', '0', '0', '', '5', '2', 'http://oxoutqnql.bkt.clouddn.com/huangtianshi_1.zip', 'http://oxoutqnql.bkt.clouddn.com/huangtianshi_1.zip', '0', '82', '0', '0', '0.0', '9', '7', '0', '1', '99', '0', '3', '5', '', '48', '0', '0');
INSERT INTO `config_giftlist` VALUES ('49', '烛光晚餐', '1188', '1188', '0', '1188', 's_49_1', '3', '2', 'http://o8uf793ar.bkt.clouddn.com/zhuguangwancan823.zip', 'http://o8uf793ar.bkt.clouddn.com/zhuguangwancan823.zip', '27', '140', '0', '0', '2.7', '14', '1', '1', '1', '24', '0', '2', '9', 'http://o8uf793ar.bkt.clouddn.com/zhuguangwangcan.png', '49', '0', '4');
INSERT INTO `config_giftlist` VALUES ('50', '布加迪威龙座驾', '0', '0', '0', '0', '', '5', '2', 'http://o8uf793ar.bkt.clouddn.com/bujiadi0823.zip', 'http://o8uf793ar.bkt.clouddn.com/bujiadi0823.zip', '0', '55', '0', '0', '0.0', '3', '2', '0', '1', '99', '0', '3', '5', 'http://p04fw86it.bkt.clouddn.com/bujiadi_icon.png', '50', '0', '0');
INSERT INTO `config_giftlist` VALUES ('51', '白金VIP徽章', '0', '0', '0', '0', '', '0', '1', '', '', '0', '0', '0', '', '0.0', '1', '1', '0', '1', '99', '0', '3', '31', 'http://oan9o31wd.bkt.clouddn.com/vipbaijin.png', '51', '0', '0');
INSERT INTO `config_giftlist` VALUES ('52', '钻石VIP徽章', '0', '0', '0', '0', '', '0', '1', '', '', '0', '0', '0', '', '0.0', '1', '1', '0', '1', '99', '0', '3', '31', 'http://oan9o31wd.bkt.clouddn.com/vipzuanshi.png', '52', '0', '0');
INSERT INTO `config_giftlist` VALUES ('53', '白天使1徽章', '0', '0', '0', '0', '', '0', '1', ' ', ' ', '0', '0', '0', '0', '0.0', '4', '2', '0', '1', '99', '0', '3', '71', 'http://oxoutqnql.bkt.clouddn.com/qishi1.png', '53', '0', '0');
INSERT INTO `config_giftlist` VALUES ('54', '白天使2徽章', '0', '0', '0', '0', '', '0', '1', ' ', ' ', '0', '0', '0', '0', '0.0', '3', '2', '0', '1', '99', '0', '3', '71', 'http://oxoutqnql.bkt.clouddn.com/qishi2.png', '54', '0', '0');
INSERT INTO `config_giftlist` VALUES ('55', '白天使3徽章', '0', '0', '0', '0', '', '0', '1', ' ', ' ', '0', '0', '0', '0', '0.0', '3', '2', '0', '1', '99', '0', '3', '71', 'http://oxoutqnql.bkt.clouddn.com/qishi3.png', '55', '0', '0');
INSERT INTO `config_giftlist` VALUES ('56', '黄天使1徽章', '0', '0', '0', '0', '', '0', '1', ' ', ' ', '0', '0', '0', '0', '0.0', '2', '2', '0', '1', '99', '0', '3', '71', 'http://oxoutqnql.bkt.clouddn.com/wangzi1.png', '56', '0', '0');
INSERT INTO `config_giftlist` VALUES ('57', '黄天使2徽章', '0', '0', '0', '0', '', '0', '1', ' ', ' ', '0', '0', '0', '0', '0.0', '3', '2', '0', '1', '99', '0', '3', '71', 'http://oxoutqnql.bkt.clouddn.com/wangzi2.png', '57', '0', '0');
INSERT INTO `config_giftlist` VALUES ('58', '黄天使3徽章', '0', '0', '0', '0', '', '0', '1', ' ', ' ', '0', '0', '0', '0', '0.0', '2', '2', '0', '1', '99', '0', '3', '71', 'http://oxoutqnql.bkt.clouddn.com/wangzi3.png', '58', '0', '0');
INSERT INTO `config_giftlist` VALUES ('59', '约定今生', '520', '52000', '5200', '0', 's_59_19', '4', '2', 'http://o8uf793ar.bkt.clouddn.com/jinshengyueding0825.zip', 'http://o8uf793ar.bkt.clouddn.com/jinshengyueding0825.zip', '27', '125', '0', '0', '2.7', '8', '1', '0', '1', '15', '0', '2', '0', 'http://o8uf793ar.bkt.clouddn.com/yuedingjinsheng.png', '59', '0', '4');
INSERT INTO `config_giftlist` VALUES ('60', '魔法卡', '150', '15000', '0', '0', '', '0', '1', ' ', ' ', '0', '0', '0', '0', '0.0', '3', '2', '0', '1', '99', '0', '3', '8', 'http://oxoutqnql.bkt.clouddn.com/ic_prop_mengzhucard.png', '60', '0', '0');
INSERT INTO `config_giftlist` VALUES ('61', '白天使开通特效', '0', '0', '0', '0', '', '5', '2', 'http://oxoutqnql.bkt.clouddn.com/yindunpai_1.zip', 'http://oxoutqnql.bkt.clouddn.com/yindunpai_1.zip', '0', '35', '0', '0', '0.0', '3', '2', '0', '1', '99', '0', '2', '0', '', '61', '0', '0');
INSERT INTO `config_giftlist` VALUES ('62', '黄天使开通特效', '0', '0', '0', '0', '', '5', '2', 'http://oxoutqnql.bkt.clouddn.com/jindunpai_1.zip', 'http://oxoutqnql.bkt.clouddn.com/jindunpai_1.zip', '0', '35', '0', ' ', '1.6', '4', '2', '0', '1', '99', '0', '2', '0', '', '62', '0', '0');
INSERT INTO `config_giftlist` VALUES ('63', '骑士缓冲期座驾', '0', '0', '0', '0', '', '5', '2', 'http://p04fw86it.bkt.clouddn.com/chongzhishouhu64.zip', 'http://p04fw86it.bkt.clouddn.com/chongzhishouhu64.zip', '0', '60', '0', '0', '0.0', '10', '6', '0', '1', '98', '0', '3', '5', '', '63', '0', '0');
INSERT INTO `config_giftlist` VALUES ('64', '王子缓冲期座驾', '0', '0', '0', '0', '', '5', '2', 'http://p04fw86it.bkt.clouddn.com/chongzhishouhu64.zip', 'http://p04fw86it.bkt.clouddn.com/chongzhishouhu64.zip', '0', '60', '0', '0', '0.0', '10', '4', '0', '1', '99', '0', '3', '5', '', '64', '0', '0');
INSERT INTO `config_giftlist` VALUES ('65', '老师好', '1', '100', '10', '0', 's_65_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/jiaoshijie906.zip', 'http://o8uf793ar.bkt.clouddn.com/jiaoshijie906.zip', '25', '0', '3', '1,99,300,520,1314,3344,9999', '2.5', '5', '1', '0', '1', '99', '0', '2', '0', '', '65', '0', '0');
INSERT INTO `config_giftlist` VALUES ('66', '幸运月饼', '1', '100', '6', '0', 's_66_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/mooncake913.zip', 'http://o8uf793ar.bkt.clouddn.com/mooncake913.zip', '8', '0', '3', '1,99,300,520,1314,3344,9999', '0.8', '9', '2', '0', '1', '99', '0', '2', '0', '', '66', '0', '0');
INSERT INTO `config_giftlist` VALUES ('67', '月神徽章', '0', '0', '0', '0', '', '0', '1', '', '', '0', '0', '0', '', '0.0', '1', '1', '0', '1', '99', '0', '3', '6', 'http://oan9o31wd.bkt.clouddn.com/change48.png', '67', '7', '0');
INSERT INTO `config_giftlist` VALUES ('68', '猎神徽章', '0', '0', '0', '0', '', '0', '1', '', '', '0', '0', '0', '', '0.0', '1', '1', '0', '1', '99', '0', '3', '6', 'http://oan9o31wd.bkt.clouddn.com/houyi48.png', '68', '7', '0');
INSERT INTO `config_giftlist` VALUES ('69', '缓冲期徽章', '0', '0', '0', '0', '', '0', '1', '', '', '0', '0', '0', '', '0.0', '1', '1', '0', '1', '99', '0', '3', '71', 'http://oan9o31wd.bkt.clouddn.com/guoqihuizhang3x.png', '69', '7', '0');
INSERT INTO `config_giftlist` VALUES ('70', '爱我中华', '200', '20000', '2000', '0', 's_70_13', '3', '2', 'http://o8uf793ar.bkt.clouddn.com/aiwozhonghua920.zip', 'http://o8uf793ar.bkt.clouddn.com/aiwozhonghua920.zip', '25', '62', '0', '1,99,300,520,1314,3344,9999', '2.5', '8', '1', '0', '1', '99', '0', '2', '0', '', '70', '0', '2');
INSERT INTO `config_giftlist` VALUES ('71', '红旗飘飘', '1', '100', '10', '0', 's_71_7', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/hongqipiaopiao920.zip', 'http://o8uf793ar.bkt.clouddn.com/hongqipiaopiao920.zip', '12', '0', '3', '1,99,300,520,1314,3344,9999', '1.2', '6', '1', '0', '1', '99', '0', '2', '0', '', '71', '0', '3');
INSERT INTO `config_giftlist` VALUES ('72', '国庆徽章', '0', '0', '0', '0', '', '0', '1', '', '', '0', '0', '0', '', '0.0', '1', '1', '0', '1', '99', '0', '3', '6', 'http://oan9o31wd.bkt.clouddn.com/guoqing920.png', '72', '7', '0');
INSERT INTO `config_giftlist` VALUES ('73', '奋斗', '1', '100', '6', '0', 's_73_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/fendou921.zip', 'http://o8uf793ar.bkt.clouddn.com/fendou921.zip', '7', '0', '3', '1,99,300,520,1314,3344,9999', '0.7', '9', '1', '0', '1', '20', '0', '2', '0', 'http://o8uf793ar.bkt.clouddn.com/feidou.png', '73', '0', '3');
INSERT INTO `config_giftlist` VALUES ('74', '活动7天VIP徽章', '0', '0', '0', '0', '', '0', '1', '', '', '0', '0', '0', '', '0.0', '1', '1', '0', '0', '99', '0', '3', '6', 'http://oan9o31wd.bkt.clouddn.com/vipbaijin.png', '74', '7', '0');
INSERT INTO `config_giftlist` VALUES ('75', '活动14天VIP徽章', '0', '0', '0', '0', '', '0', '1', '', '', '0', '0', '0', '', '0.0', '1', '1', '0', '0', '99', '0', '3', '6', 'http://oan9o31wd.bkt.clouddn.com/vipbaijin.png', '75', '14', '0');
INSERT INTO `config_giftlist` VALUES ('76', '活动30天VIP徽章', '0', '0', '0', '0', '', '0', '1', '', '', '0', '0', '0', '', '0.0', '1', '1', '0', '0', '99', '0', '3', '6', 'http://oan9o31wd.bkt.clouddn.com/vipbaijin.png', '76', '30', '0');
INSERT INTO `config_giftlist` VALUES ('77', '多多小美', '1', '100', '6', '0', 's_77_10', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/xiaomei0930.zip', 'http://o8uf793ar.bkt.clouddn.com/xiaomei0930.zip', '15', '0', '3', '1,99,300,520,1314,3344,9999', '1.5', '7', '1', '0', '1', '99', '0', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/s_77_10.png', '77', '0', '3');
INSERT INTO `config_giftlist` VALUES ('78', '生日蛋糕', '1000', '100000', '10000', '0', 's_78_1', '4', '2', 'http://o8uf793ar.bkt.clouddn.com/birthdaycake1014.zip', 'http://o8uf793ar.bkt.clouddn.com/birthdaycake1014.zip', '18', '91', '0', '1,99,300,520,1314,3344,9999', '1.8', '19', '1', '0', '1', '13', '1476435900', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/shengridangao.png', '78', '0', '2');
INSERT INTO `config_giftlist` VALUES ('79', '南瓜灯', '1', '100', '10', '0', 's_79_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/nanguadeng1026.zip', 'http://o8uf793ar.bkt.clouddn.com/nanguadeng1026.zip', '10', '0', '3', '1,99,300,520,1314,3344,9999', '1.0', '9', '1', '0', '1', '35', '0', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/nanguadeng.png', '79', '0', '1');
INSERT INTO `config_giftlist` VALUES ('80', '小色猪', '1', '100', '10', '10', 's_80_7', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/sexiaozhu1231.zip', 'http://o8uf793ar.bkt.clouddn.com/sexiaozhu1231.zip', '7', '0', '3', '1,99,300,520,1314,3344,9999', '0.6', '23', '4', '0', '1', '9', '0', '2', '0', 'http://o8uf793ar.bkt.clouddn.com/sexiaozhu1230.png', '80', '0', '1');
INSERT INTO `config_giftlist` VALUES ('81', '鼓掌', '1', '100', '10', '0', 's_81_5', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/guzhang1026.zip', 'http://o8uf793ar.bkt.clouddn.com/guzhang1026.zip', '5', '0', '3', '1,99,300,520,1314,3344,9999', '0.3', '30', '2', '0', '1', '28', '0', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/guzhang.png', '81', '0', '1');
INSERT INTO `config_giftlist` VALUES ('82', '丘比特之箭', '1288', '1288', '0', '1288', 's_82_1', '3', '2', 'http://o8uf793ar.bkt.clouddn.com/qiubite1108.zip', 'http://o8uf793ar.bkt.clouddn.com/qiubite1108.zip', '10', '90', '0', '0', '1.0', '13', '1', '1', '1', '25', '0', '2', '9', 'http://oan9o31wd.bkt.clouddn.com/qiubite.png', '82', '0', '2');
INSERT INTO `config_giftlist` VALUES ('83', '香吻', '1', '100', '10', '0', 's_83_7', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/kiss1108.zip', 'http://o8uf793ar.bkt.clouddn.com/kiss1108.zip', '10', '0', '3', '1,99,300,520,1314,3344,9999', '1.0', '18', '1', '0', '1', '10', '0', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/xiangwen.png', '83', '0', '1');
INSERT INTO `config_giftlist` VALUES ('84', '相互伤害', '2', '200', '20', '0', 's_84_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/taotao1110.zip', 'http://o8uf793ar.bkt.clouddn.com/taotao1110.zip', '10', '0', '3', '1,99,300,520,1314,3344,9999', '1.0', '17', '1', '0', '1', '21', '0', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/xianghushanghai.png', '84', '0', '1');
INSERT INTO `config_giftlist` VALUES ('85', '发红包', '0', '0', '0', '0', 's_85_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/hongbao.zip', 'http://o8uf793ar.bkt.clouddn.com/hongbao.zip', '1', '0', '0', '1', '1.0', '2', '2', '0', '1', '99', '0', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/hongbao.png', '85', '0', '0');
INSERT INTO `config_giftlist` VALUES ('86', '奥迪TT', '880', '880', '0', '0', '', '5', '2', 'http://p04fw86it.bkt.clouddn.com/aoditt1.zip', 'http://p04fw86it.bkt.clouddn.com/aoditt1.zip', '0', '53', '0', '1', '0.0', '3', '1', '0', '1', '4', '0', '3', '5', 'http://p04fw86it.bkt.clouddn.com/aoditt.png', '86', '0', '0');
INSERT INTO `config_giftlist` VALUES ('87', '兰博基尼', '2980', '2980', '0', '0', '', '5', '2', 'http://o8uf793ar.bkt.clouddn.com/lanbojini1.zip', 'http://o8uf793ar.bkt.clouddn.com/lanbojini1.zip', '0', '56', '0', '1', '0.0', '3', '1', '0', '1', '6', '0', '3', '5', 'http://o8uf793ar.bkt.clouddn.com/lanbojini1.png', '87', '0', '0');
INSERT INTO `config_giftlist` VALUES ('88', 'mini', '660', '660', '0', '0', '', '5', '2', 'http://o8uf793ar.bkt.clouddn.com/mini.zip', 'http://o8uf793ar.bkt.clouddn.com/mini.zip', '0', '50', '0', '1', '0.0', '3', '1', '0', '1', '3', '0', '3', '5', 'http://o8uf793ar.bkt.clouddn.com/mini.png', '88', '0', '0');
INSERT INTO `config_giftlist` VALUES ('89', 'smart', '200', '220', '0', '0', '', '5', '2', 'http://p04fw86it.bkt.clouddn.com/smart.zip', 'http://p04fw86it.bkt.clouddn.com/smart.zip', '0', '50', '0', '1', '0.0', '2', '1', '0', '1', '1', '0', '3', '5', 'http://p04fw86it.bkt.clouddn.com/smart.png', '89', '0', '0');
INSERT INTO `config_giftlist` VALUES ('90', '莲花GTE', '1880', '1880', '0', '0', 's_90_1', '5', '2', 'http://image-default.maiyalive.com/lianhuagte_1.zip', 'http://image-default.maiyalive.com/lianhuagte_1.zip', '1', '58', '0', '1', '0.0', '5', '2', '0', '1', '5', '0', '3', '5', 'http://p04fw86it.bkt.clouddn.com/lianhuagte.png', '90', '0', '0');
INSERT INTO `config_giftlist` VALUES ('91', '圣诞雪橇', '300', '300', '0', '0', 'b_91_39', '5', '2', 'http://o8uf793ar.bkt.clouddn.com/sdxq.zip', 'http://o8uf793ar.bkt.clouddn.com/sdxq.zip', '0', '61', '0', ' ', '0.0', '7', '1', '0', '1', '2', '0', '3', '5', 'http://o8uf793ar.bkt.clouddn.com/sdxq.png', '91', '2', '0');
INSERT INTO `config_giftlist` VALUES ('92', '口红', '199', '19900', '1990', '0', 's_92_10', '2', '2', 'http://o8uf793ar.bkt.clouddn.com/kouhong1214.zip', 'http://o8uf793ar.bkt.clouddn.com/kouhong1214.zip', '10', '58', '6', '1,99,300,520,1314,3344,9999', '0.7', '11', '3', '0', '1', '14', '1481780853', '2', '0', 'http://o8uf793ar.bkt.clouddn.com/kouhong.png', '92', '0', '1');
INSERT INTO `config_giftlist` VALUES ('93', '圣诞老人', '2', '200', '100', '0', 's_93_1', '4', '2', 'http://oxoutqnql.bkt.clouddn.com/res19.zip', 'http://oxoutqnql.bkt.clouddn.com/res19.zip', '5', '81', '3', '1,99,300,520,1314,3344,9999', '2.0', '50', '18', '0', '0', '2', '1481781084', '2', '0', 'http://owvwocjrz.bkt.clouddn.com/b_93_60.png', '93', '0', '1');
INSERT INTO `config_giftlist` VALUES ('94', '圣诞帽', '2', '200', '20', '0', 's_94_10', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/shengdanmao1214.zip', 'http://o8uf793ar.bkt.clouddn.com/shengdanmao1214.zip', '10', '0', '15', '1,99,300,520,1314,3344,9999', '0.7', '7', '1', '0', '1', '23', '1481781254', '2', '0', 'http://o8uf793ar.bkt.clouddn.com/shengdanmao.png', '94', '0', '1');
INSERT INTO `config_giftlist` VALUES ('95', '圣诞袜', '1', '100', '10', '0', 's_95_10', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/shengdanwa1214.zip', 'http://o8uf793ar.bkt.clouddn.com/shengdanwa1214.zip', '10', '0', '15', '1,99,300,520,1314,3344,9999', '0.7', '7', '1', '0', '1', '21', '1481781314', '2', '0', 'http://o8uf793ar.bkt.clouddn.com/shengdanwa.png', '95', '0', '1');
INSERT INTO `config_giftlist` VALUES ('96', '来一炮', '980', '98000', '9800', '0', 's_96_1', '4', '2', 'http://o8uf793ar.bkt.clouddn.com/laiyipao12311.zip', 'http://o8uf793ar.bkt.clouddn.com/laiyipao12311.zip', '20', '113', '0', '1,99,300,520,1314,3344,9999', '1.8', '20', '5', '0', '1', '7', '1483100854', '2', '0', 'http://o8uf793ar.bkt.clouddn.com/laiyipao1230.png', '96', '0', '2');
INSERT INTO `config_giftlist` VALUES ('97', '腊八粥', '1', '100', '10', '0', 's_97_10', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/labazhou1713.zip', 'http://o8uf793ar.bkt.clouddn.com/labazhou1713.zip', '11', '0', '3', '1,99,300,520,1314,3344,9999', '0.7', '5', '2', '0', '1', '97', '1483451039', '2', '0', 'http://o8uf793ar.bkt.clouddn.com/labazhou1713.png', '97', '0', '1');
INSERT INTO `config_giftlist` VALUES ('98', '娱乐教皇', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0.0', '3', '1', '0', '1', '999', '1483451272', '3', '6', 'http://o8uf793ar.bkt.clouddn.com/yulejiaohuang.png', '98', '30', '0');
INSERT INTO `config_giftlist` VALUES ('99', '车票', '1', '100', '10', '0', 's_99_22', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/ticket110.zip', 'http://o8uf793ar.bkt.clouddn.com/ticket110.zip', '28', '0', '6', '1,99,300,520,1314,3344,9999', '2.5', '5', '1', '0', '1', '1', '1484141689', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/ticket22.png', '99', '0', '1');
INSERT INTO `config_giftlist` VALUES ('100', '新年快乐', '1', '100', '10', '0', 's_100_15', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/xinniankuaile2017.zip', 'http://o8uf793ar.bkt.clouddn.com/xinniankuaile2017.zip', '15', '0', '9', '1,99,300,520,1314,3344,9999', '1.2', '4', '1', '0', '1', '99', '1484987588', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/2017xnkl.png', '100', '0', '1');
INSERT INTO `config_giftlist` VALUES ('101', '压岁钱', '88', '8800', '880', '0', 's_101_15', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/yasuiqian2017.zip', 'http://o8uf793ar.bkt.clouddn.com/yasuiqian2017.zip', '15', '0', '6', '1,99,300,520,1314,3344,9999', '1.2', '5', '1', '0', '1', '99', '1484987771', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/2017ysq.png', '101', '0', '1');
INSERT INTO `config_giftlist` VALUES ('102', '烟花', '888', '88800', '8880', '0', 's_102_3', '4', '2', 'http://o8uf793ar.bkt.clouddn.com/dayanhua2017.zip', 'http://o8uf793ar.bkt.clouddn.com/dayanhua2017.zip', '16', '96', '0', '1,99,300,520,1314,3344,9999', '1.3', '8', '2', '0', '1', '26', '1484988515', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/dayanhua2017.png', '102', '0', '2');
INSERT INTO `config_giftlist` VALUES ('103', '元宵', '1', '100', '10', '0', 's_103_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/yuanxiao0208.zip', 'http://o8uf793ar.bkt.clouddn.com/yuanxiao0208.zip', '10', '0', '3', '1,99,300,520,1314,3344,9999', '0.7', '6', '1', '0', '1', '2', '1486607902', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/yuanxiao0208.png', '103', '0', '1');
INSERT INTO `config_giftlist` VALUES ('104', '蓝色妖姬', '688', '688', '0', '688', 's_104_1', '3', '2', 'http://o8uf793ar.bkt.clouddn.com/lanseyaoji0209.zip', 'http://o8uf793ar.bkt.clouddn.com/lanseyaoji0209.zip', '10', '55', '0', '1,99,300,520,1314,3344,9999', '0.7', '10', '2', '1', '1', '23', '1486608227', '2', '9', 'http://oan9o31wd.bkt.clouddn.com/lanseyaoji0208.png', '104', '0', '1');
INSERT INTO `config_giftlist` VALUES ('105', '金豆', '1', '100', '10', '0', 's_105_1', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/jindou223.zip', 'http://o8uf793ar.bkt.clouddn.com/jindou223.zip', '11', '0', '3', '1,99,300,520,1314,3344,9999', '0.7', '4', '2', '0', '1', '3', '1487732012', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/jindou223.png', '105', '0', '1');
INSERT INTO `config_giftlist` VALUES ('106', '龙头', '88', '8800', '880', '0', 's_106_1', '3', '2', 'http://o8uf793ar.bkt.clouddn.com/longtou223.zip', 'http://o8uf793ar.bkt.clouddn.com/longtou223.zip', '13', '51', '0', '1,99,300,520,1314,3344,9999', '1.0', '6', '2', '0', '1', '4', '1487732140', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/longtou223.png', '106', '0', '1');
INSERT INTO `config_giftlist` VALUES ('107', '三岁', '333', '33300', '3330', '0', 's_107_1', '3', '2', 'http://o8uf793ar.bkt.clouddn.com/shansui0301.zip', 'http://o8uf793ar.bkt.clouddn.com/shansui0301.zip', '10', '109', '0', '1,99,300,520,1314,3344,9999', '0.7', '5', '2', '0', '1', '11', '1490067629', '2', '0', 'http://oan9o31wd.bkt.clouddn.com/shansui0301.png', '107', '0', '2');
INSERT INTO `config_giftlist` VALUES ('108', '飞fdgfg', '6661', '666601', '66661', '0', 's_108_1', '1', '1', '0', '0', '12', '70', '0', '1,99,300,520,1314,3344,9999', '1.4', '9', '2', '0', '0', '27', '1490068112', '3', '7', 'http://oan9o31wd.bkt.clouddn.com/feiji.png', '18', '0', '2');
INSERT INTO `config_giftlist` VALUES ('109', '111', '2', '200', '20', '0', 's_3_15', '1', '1', 'http://o8uf793ar.bkt.clouddn.com/zhutou618.zip', 'http://o8uf793ar.bkt.clouddn.com/zhutou618.zip', '16', '0', '1', '1,99,300,520,1314,3344,9999', '2.0', '3', '1', '0', '1', '30', '1490068522', '2', '1', 'http://oan9o31wd.bkt.clouddn.com/zhutou.png', '3', '0', '1');
INSERT INTO `config_giftlist` VALUES ('110', 'G蛋节', '500', '100', '100', '100', 's_110_20', '4', '2', 'http://p0gy7d6cg.bkt.clouddn.com/gdj.zip', 'http://p0gy7d6cg.bkt.clouddn.com/gdj.zip', '20', '20', '0', '1,99,300,520,1314,3344,9999', '2.0', '15', '3', '0', '0', '2', '1516329183', '2', '0', 'http://p0gy7d6cg.bkt.clouddn.com/s_2_201.png', '110', '0', '1');
INSERT INTO `config_giftlist` VALUES ('111', '比心', '1688', '1688', '168', '0', 's_111_1', '5', '2', 'http://p5tpbfd1d.bkt.clouddn.com/bixin111_1.zip', 'http://image-default.maiyalive.com/bixin111_20180420.zip', '10', '30', '0', '1,10,20,30,50,99,520', '5.0', '24', '8', '1', '1', '11', '1519456636', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_111_1.png', '111', '5', '1');
INSERT INTO `config_giftlist` VALUES ('112', '啤酒', '888', '888', '88', '0', 's_112_1', '3', '2', 'http://p5tpbfd1d.bkt.clouddn.com/pijiu112_1.zip', 'http://image-default.maiyalive.com/pijiu112_20180420.zip', '10', '20', '0', '1,10,20,30,50,99,520', '3.0', '21', '9', '1', '1', '9', '1519456861', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_112_1.png', '112', '3', '1');
INSERT INTO `config_giftlist` VALUES ('113', '城堡', '288800', '288800', '28880', '0', 's_113_1', '4', '1', 'http://p5tpbfd1d.bkt.clouddn.com/113chengbao_3.zip', 'http://image-default.maiyalive.com/chengbao113_20180420.zip', '10', '60', '0', '1,10,20,30,50,99,520', '6.0', '21', '10', '1', '1', '24', '1519625255', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_113_1.png', '113', '0', '0');
INSERT INTO `config_giftlist` VALUES ('114', '告白气球', '6888', '6888', '688', '0', 's_114_1', '4', '1', 'http://p5tpbfd1d.bkt.clouddn.com/114gaobaiqiqiu_2.zip', 'http://image-default.maiyalive.com/gaobaiqiqiu114_20180420.zip', '10', '40', '0', '1,10,20,30,50,99,520', '4.0', '18', '9', '1', '1', '15', '1519625373', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_114_1.png', '114', '0', '1');
INSERT INTO `config_giftlist` VALUES ('115', '火车', '18888', '18888', '1888', '0', 's_115_1', '4', '1', 'http://p5tpbfd1d.bkt.clouddn.com/115huoche_2.zip', 'http://image-default.maiyalive.com/huoche115_20180420.zip', '10', '50', '0', '1,10,20,30,50,99,520', '4.0', '18', '8', '0', '1', '17', '1519625509', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_115_1.png', '115', '0', '1');
INSERT INTO `config_giftlist` VALUES ('116', '火箭', '5888', '5888', '588', '0', 's_116_1', '4', '1', 'http://p5tpbfd1d.bkt.clouddn.com/116huojian_2.zip', 'http://image-default.maiyalive.com/huojian116_20180420.zip', '10', '50', '0', '1,10,20,30,50,99,520', '1.5', '17', '7', '1', '1', '14', '1519625632', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_116_1.png', '116', '0', '1');
INSERT INTO `config_giftlist` VALUES ('117', '么么哒', '3688', '3688', '368', '0', 's_117_1', '4', '1', 'http://p5tpbfd1d.bkt.clouddn.com/117memeda_2.zip', 'http://image-default.maiyalive.com/memeda117_20180420.zip', '10', '30', '0', '1,10,20,30,50,99,520', '2.0', '17', '8', '1', '1', '13', '1519625907', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_117_1.png', '117', '0', '1');
INSERT INTO `config_giftlist` VALUES ('118', '跑车', '38888', '38888', '3888', '0', 's_118_1', '4', '1', 'http://p5tpbfd1d.bkt.clouddn.com/118paoche_2.zip', 'http://image-default.maiyalive.com/paoche118_20180420.zip', '10', '70', '0', '1,10,20,30,50,99,520', '5.0', '16', '7', '1', '1', '18', '1519625994', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_118_1.png', '118', '0', '1');
INSERT INTO `config_giftlist` VALUES ('119', '生日蛋糕', '131400', '131400', '13140', '0', 's_119_1', '4', '1', 'http://p5tpbfd1d.bkt.clouddn.com/119shengridangao_2.zip', 'http://image-default.maiyalive.com/shengridangao119_20180423.zip', '10', '90', '0', '1,10,20,30,50,99,520', '6.0', '17', '6', '1', '1', '23', '1519626071', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_119_1.png', '119', '0', '1');
INSERT INTO `config_giftlist` VALUES ('120', '桃花', '8888', '8888', '888', '0', 's_120_1', '4', '1', 'http://p5tpbfd1d.bkt.clouddn.com/120taohua_2.zip', 'http://image-default.maiyalive.com/taohua120_20180420.zip', '10', '60', '0', '1,10,20,30,50,99,520', '4.5', '16', '7', '1', '1', '16', '1519626145', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_120_1.png', '120', '0', '1');
INSERT INTO `config_giftlist` VALUES ('121', '星空之恋', '131400', '131400', '13140', '0', 's_121_1', '4', '1', 'http://p5tpbfd1d.bkt.clouddn.com/121xingkongzhilian_2.zip', 'http://image-default.maiyalive.com/xingkongzhilian121_20180423.zip', '10', '60', '0', '1,10,20,30,50,99,520', '4.0', '16', '6', '1', '1', '22', '1519626204', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_121_1.png', '121', '0', '1');
INSERT INTO `config_giftlist` VALUES ('122', '彩虹', '88888', '88888', '8888', '0', 's_122_1', '4', '1', 'http://p5tpbfd1d.bkt.clouddn.com/122caihong_2.zip', 'http://image-default.maiyalive.com/caihong122_20180420.zip', '10', '60', '0', '1,10,20,30,50,99,520', '2.5', '17', '6', '1', '1', '21', '1519798971', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_122_1.png', '122', '0', '1');
INSERT INTO `config_giftlist` VALUES ('123', '飞机', '68888', '68888', '6888', '0', 's_123_1', '4', '1', 'http://p5tpbfd1d.bkt.clouddn.com/123feiji_2.zip', 'http://image-default.maiyalive.com/feiji123_20180420.zip', '10', '80', '0', '1,10,20,30,50,99,520', '5.0', '15', '6', '1', '1', '20', '1519799273', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_123_1.png', '123', '0', '1');
INSERT INTO `config_giftlist` VALUES ('124', '皇冠', '2488', '2488', '248', '0', 's_124_1', '3', '1', 'http://p5tpbfd1d.bkt.clouddn.com/huangguan124_1.zip', 'http://image-default.maiyalive.com/huangguan124_20180420.zip', '10', '30', '0', '1,10,20,30,50,99,520', '2.0', '16', '6', '1', '1', '12', '1519799373', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_124_1.png', '124', '0', '1');
INSERT INTO `config_giftlist` VALUES ('125', '轮船', '58888', '58888', '5888', '0', 's_125_1', '4', '1', 'http://p5tpbfd1d.bkt.clouddn.com/125lunchuan_2.zip', 'http://image-default.maiyalive.com/lunchuan125_20180420.zip', '10', '88', '0', '1,10,20,30,50,99,520', '5.0', '14', '7', '1', '1', '19', '1519799636', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_125_1.png', '125', '0', '1');
INSERT INTO `config_giftlist` VALUES ('126', '荧光棒', '1288', '1288', '128', '0', 's_126_1', '3', '1', 'http://p5tpbfd1d.bkt.clouddn.com/yingguangbang126_1.zip', 'http://image-default.maiyalive.com/yingguangbang126_20180420.zip', '10', '30', '0', '1,10,20,30,50,99,520', '2.0', '14', '5', '1', '1', '10', '1519799758', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_126_1.png', '126', '0', '1');
INSERT INTO `config_giftlist` VALUES ('127', '鼓掌', '188', '188', '18', '0', 's_127_1', '3', '1', 'http://image-default.maiyalive.com/guzhang127_3.zip', 'http://image-default.maiyalive.com/guzhang127_3.zip', '20', '1', '0', '1,10,20,30,50,99,520', '2.0', '15', '6', '1', '1', '5', '1520230705', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_127_1.png', '127', '0', '1');
INSERT INTO `config_giftlist` VALUES ('128', '钻戒', '288', '288', '28', '0', 's_128_1', '3', '1', 'http://image-default.maiyalive.com/zuanjie128_3.zip', 'http://image-default.maiyalive.com/zuanjie128_3.zip', '20', '1', '0', '1,10,20,30,50,99,520', '2.0', '12', '5', '1', '1', '6', '1520230960', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_128_1.png', '128', '0', '1');
INSERT INTO `config_giftlist` VALUES ('129', '口红', '58', '58', '5', '0', 's_129_1', '3', '1', 'http://image-default.maiyalive.com/kouhong129_3.zip', 'http://image-default.maiyalive.com/kouhong129_3.zip', '20', '1', '0', '1,10,20,30,50,99,520', '2.0', '13', '4', '1', '1', '3', '1520231124', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_129_1.png', '129', '0', '1');
INSERT INTO `config_giftlist` VALUES ('130', '锤子', '588', '588', '58', '0', 's_130_1', '3', '1', 'http://image-default.maiyalive.com/chuizi130_2.zip', 'http://image-default.maiyalive.com/chuizi130_2.zip', '10', '1', '0', '1,10,20,30,50,99,520', '2.0', '11', '4', '1', '1', '8', '1520231309', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_130_1.png', '130', '0', '1');
INSERT INTO `config_giftlist` VALUES ('131', '666', '18', '18', '1', '0', 's_131_1', '3', '1', 'http://image-default.maiyalive.com/666_2.zip', 'http://image-default.maiyalive.com/666_2.zip', '20', '1', '0', '1,10,20,30,50,99,520', '2.0', '20', '4', '1', '1', '2', '1520231399', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_131_1.png', '131', '0', '1');
INSERT INTO `config_giftlist` VALUES ('132', '金话筒', '68', '68', '6', '0', 's_132_1', '3', '1', 'http://image-default.maiyalive.com/jinhuatong132_3.zip', 'http://image-default.maiyalive.com/jinhuatong132_3.zip', '20', '1', '0', '1,10,20,30,50,99,520', '2.0', '14', '5', '1', '1', '4', '1520231517', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_132_1.png', '132', '0', '1');
INSERT INTO `config_giftlist` VALUES ('133', '打call', '388', '388', '38', '0', 's_133_1', '1', '1', 'http://image-default.maiyalive.com/dacall133_4.zip', 'http://image-default.maiyalive.com/dacall133_4.zip', '20', '1', '0', '1,10,20,30,50,99,520', '2.0', '19', '5', '1', '1', '7', '1520231773', '2', '0', 'http://p5tpbfd1d.bkt.clouddn.com/s_133_1.png', '133', '0', '1');
INSERT INTO `config_giftlist` VALUES ('134', '爱的告白', '18888', '18888', '1888', '0', 's_134_1', '4', '2', 'http://image-default.maiyalive.com/aidegaobai134_android_1.zip', 'http://image-default.maiyalive.com/aidegaobai134_ios_1.zip', '1', '50', '0', '1,10,20,30,50,99,520', '2.5', '5', '3', '1', '1', '17', '1526870026', '2', '0', 'http://image-default.maiyalive.com/aidegaobai_icon.png', '134', '0', '1');
INSERT INTO `config_giftlist` VALUES ('135', '摩托车', '1', '0', '0', '0', 'b_135_13', '4', '1', 'http://image-default.maiyalive.com/motuoche135_android.zip', 'http://image-default.maiyalive.com/motuoche135_ios.zip', '0', '30', '0', '1', '2.0', '9', '1', '0', '1', '135', '1526870272', '3', '5', 'http://image-default.maiyalive.com/motuoche_icon_1.png', '135', '0', '1');
INSERT INTO `config_giftlist` VALUES ('136', '三辆跑车', '1', '1', '0', '0', 'b_136_24', '4', '1', 'http://image-default.maiyalive.com/sanliangpaoche136_android.zip', 'http://image-default.maiyalive.com/sanliangpaoche136_ios.zip', '0', '32', '0', '1', '2.0', '3', '1', '1', '1', '136', '1526870477', '2', '9', 'http://image-default.maiyalive.com/sanliangpaoche_icon.png', '136', '0', '1');
INSERT INTO `config_giftlist` VALUES ('137', '战斗机', '1', '1', '0', '0', 'b_137_10', '4', '1', 'http://image-default.maiyalive.com/zhandouji137_android.zip', 'http://image-default.maiyalive.com/zhandouji137_ios.zip', '0', '32', '0', '1', '2.0', '9', '1', '0', '1', '137', '1526870620', '3', '5', 'http://image-default.maiyalive.com/zhandouji_icon_1.png', '137', '0', '1');

-- ----------------------------
-- Table structure for `config_gift_activity`
-- ----------------------------
DROP TABLE IF EXISTS `config_gift_activity`;
CREATE TABLE `config_gift_activity` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `gid` int(11) NOT NULL,
  `atype` int(3) NOT NULL COMMENT '=1周星 =2幸运 =3活动 =4新品 =5vip =6守护',
  `stime` int(10) NOT NULL COMMENT '活动开始时间',
  `etime` int(10) NOT NULL COMMENT '活动结束时间',
  `isvalid` tinyint(1) NOT NULL DEFAULT '1' COMMENT '=0无效 =1有效',
  PRIMARY KEY (`id`),
  KEY `stime_etime` (`stime`,`etime`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of config_gift_activity
-- ----------------------------
INSERT INTO `config_gift_activity` VALUES ('2', '8', '1', '1511107200', '1511625600', '1');
INSERT INTO `config_gift_activity` VALUES ('3', '5', '1', '1514131200', '1514649600', '1');
INSERT INTO `config_gift_activity` VALUES ('7', '8', '1', '1520784000', '1521302400', '1');
INSERT INTO `config_gift_activity` VALUES ('8', '11', '1', '1520784000', '1521302400', '1');
INSERT INTO `config_gift_activity` VALUES ('9', '18', '1', '1520784000', '1521302400', '1');
INSERT INTO `config_gift_activity` VALUES ('11', '8', '1', '1521388800', '1521907200', '1');
INSERT INTO `config_gift_activity` VALUES ('12', '5', '1', '1521388800', '1521907200', '1');
INSERT INTO `config_gift_activity` VALUES ('13', '11', '1', '1521388800', '1521907200', '1');
INSERT INTO `config_gift_activity` VALUES ('16', '117', '1', '1521993600', '1522512000', '1');
INSERT INTO `config_gift_activity` VALUES ('17', '131', '1', '1521993600', '1522512000', '1');
INSERT INTO `config_gift_activity` VALUES ('18', '7', '1', '1523203200', '1523721600', '1');
INSERT INTO `config_gift_activity` VALUES ('19', '96', '1', '1523203200', '1523721600', '1');
INSERT INTO `config_gift_activity` VALUES ('20', '10', '1', '1523203200', '1523721600', '1');

-- ----------------------------
-- Table structure for `config_gift_promotion`
-- ----------------------------
DROP TABLE IF EXISTS `config_gift_promotion`;
CREATE TABLE `config_gift_promotion` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `gid` int(11) NOT NULL COMMENT '礼物gid',
  `promotionName` varchar(30) NOT NULL DEFAULT '' COMMENT '促销活动名',
  `discount` int(2) NOT NULL COMMENT '折扣[%]',
  `disPrice` int(11) NOT NULL COMMENT '促销价',
  `isvalid` int(11) NOT NULL DEFAULT '1' COMMENT '=1有效 其他无效',
  `starttime` int(11) NOT NULL COMMENT '促销开始时间',
  `endtime` int(11) NOT NULL COMMENT '促销结束时间',
  `addtime` int(11) NOT NULL COMMENT '添加时间',
  `adminid` int(11) NOT NULL COMMENT '操作者UID',
  PRIMARY KEY (`id`),
  KEY `gid_pname` (`gid`,`promotionName`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COMMENT='礼物促销列表';

-- ----------------------------
-- Records of config_gift_promotion
-- ----------------------------
INSERT INTO `config_gift_promotion` VALUES ('1', '43', '双十二 促销', '90', '899', '1', '1507564800', '1509379200', '1480562190', '11');
INSERT INTO `config_gift_promotion` VALUES ('2', '44', '双十二 促销', '90', '8999', '1', '1481385600', '1481644800', '1481009274', '11');
INSERT INTO `config_gift_promotion` VALUES ('3', '45', '双十二 促销', '90', '1182', '1', '1481385600', '1481644800', '1481009323', '11');
INSERT INTO `config_gift_promotion` VALUES ('4', '46', '双十二 促销', '90', '11826', '1', '1481385600', '1481644800', '1481009365', '11');
INSERT INTO `config_gift_promotion` VALUES ('5', '60', '双十二 促销', '90', '135', '1', '1481385600', '1481644800', '1481009414', '11');
INSERT INTO `config_gift_promotion` VALUES ('6', '86', '双十二 促销', '90', '792', '1', '1481385600', '1481644800', '1481009558', '11');
INSERT INTO `config_gift_promotion` VALUES ('7', '87', '双十二 促销', '90', '2682', '1', '1481385600', '1481644800', '1481009606', '11');
INSERT INTO `config_gift_promotion` VALUES ('8', '88', '双十二 促销', '90', '594', '1', '1481385600', '1481644800', '1481009661', '11');
INSERT INTO `config_gift_promotion` VALUES ('9', '89', '双十二 促销', '90', '180', '1', '1481385600', '1481644800', '1481009708', '11');
INSERT INTO `config_gift_promotion` VALUES ('10', '90', '双十二 促销', '90', '1692', '1', '1481385600', '1481644800', '1481009751', '11');
INSERT INTO `config_gift_promotion` VALUES ('11', '91', '双十二 促销', '90', '270', '1', '1481385600', '1481644800', '1481009797', '11');

-- ----------------------------
-- Table structure for `game_car_setting`
-- ----------------------------
DROP TABLE IF EXISTS `game_car_setting`;
CREATE TABLE `game_car_setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '游戏ID',
  `probability1` double(10,2) NOT NULL DEFAULT '0.00' COMMENT '一连概率',
  `probability2` double(10,2) NOT NULL DEFAULT '0.00' COMMENT '二连概率',
  `probability3` double(10,2) NOT NULL DEFAULT '0.00' COMMENT '三连概率',
  `game_commission` double(10,2) NOT NULL DEFAULT '0.00' COMMENT '游戏提成',
  `room_inform_money` bigint(20) NOT NULL DEFAULT '0' COMMENT '房间通知金币',
  `platform_inform_money` bigint(20) NOT NULL DEFAULT '0' COMMENT '平台通知金币',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `create_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建用户ID',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  `update_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='游戏配置表';

-- ----------------------------
-- Records of game_car_setting
-- ----------------------------
INSERT INTO `game_car_setting` VALUES ('1', '0.70', '0.20', '0.10', '0.06', '200', '1000', '1510624254055', '1', '1516256635558', '1');

-- ----------------------------
-- Table structure for `game_doll_setting`
-- ----------------------------
DROP TABLE IF EXISTS `game_doll_setting`;
CREATE TABLE `game_doll_setting` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `claw1` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '游戏爪子1金币数',
  `claw2` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '爪子2金币数',
  `claw3` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '爪子3金币数',
  `room_inform_money` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '房间通知金币',
  `platform_inform_money` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '平台通知金币',
  `create_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `create_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建用户ID',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  `update_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of game_doll_setting
-- ----------------------------
INSERT INTO `game_doll_setting` VALUES ('1', '100', '200', '300', '100', '1000', '1511836140225', '1', '1511847645789', '0');

-- ----------------------------
-- Table structure for `gift_config`
-- ----------------------------
DROP TABLE IF EXISTS `gift_config`;
CREATE TABLE `gift_config` (
  `gid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '道具id',
  `giftname` char(30) NOT NULL DEFAULT '' COMMENT '道具名称',
  `gifttype` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '=1普通连送礼物 =2中级礼物 =3奢华礼物',
  `price` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '道具价格',
  `profit` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '对方的收益',
  `showtime` int(3) DEFAULT '0' COMMENT '展现时长',
  `displayMode` int(3) DEFAULT '0' COMMENT '=1礼物消息连 =2动态图片(半屏) =3动画效果(全屏)',
  `displaytimes` int(1) DEFAULT '1' COMMENT '展现次数',
  `isshow` int(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否展示 (0=>不展示 1=>展示)',
  `sort` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '排序',
  `valid` int(1) NOT NULL DEFAULT '0' COMMENT '=0无效 =1有效',
  `createAt` int(11) NOT NULL DEFAULT '0' COMMENT '添加时间',
  PRIMARY KEY (`gid`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gift_config
-- ----------------------------
INSERT INTO `gift_config` VALUES ('1', '弹幕', '0', '1', '0', '0', '0', '1', '1', '0', '0', '1460727761');
INSERT INTO `gift_config` VALUES ('2', '玫瑰花', '1', '1', '100', '4', '1', '1', '1', '11', '0', '1460727761');
INSERT INTO `gift_config` VALUES ('3', '猪头', '1', '2', '100', '4', '1', '1', '1', '12', '0', '1460727761');
INSERT INTO `gift_config` VALUES ('4', '猫咪', '1', '5', '100', '4', '1', '1', '1', '6', '0', '1460727761');
INSERT INTO `gift_config` VALUES ('5', '金砖', '2', '200', '100', '5', '2', '1', '1', '13', '0', '1460727761');
INSERT INTO `gift_config` VALUES ('6', '留声机', '2', '500', '100', '5', '2', '1', '1', '5', '0', '1460727761');
INSERT INTO `gift_config` VALUES ('7', '心动', '1', '1', '100', '4', '1', '1', '1', '1', '0', '1460727761');
INSERT INTO `gift_config` VALUES ('8', '钻戒', '1', '66', '100', '4', '1', '1', '1', '16', '0', '1460727761');
INSERT INTO `gift_config` VALUES ('9', '钻石', '2', '1314', '100', '5', '2', '1', '1', '14', '0', '1460727761');
INSERT INTO `gift_config` VALUES ('10', '兰博基尼', '3', '3000', '100', '7', '3', '1', '1', '15', '0', '1460727761');
INSERT INTO `gift_config` VALUES ('11', '私人游艇', '3', '13140', '100', '8', '3', '1', '1', '10', '0', '1460727761');
INSERT INTO `gift_config` VALUES ('12', '红包', '9', '0', '0', '0', '0', '1', '0', '0', '0', '0');
INSERT INTO `gift_config` VALUES ('13', '棒棒糖', '1', '1', '100', '0', '1', '1', '1', '2', '1', '1463468102');
INSERT INTO `gift_config` VALUES ('14', '小黄鸡', '1', '2', '100', '0', '1', '1', '1', '3', '1', '1463468102');
INSERT INTO `gift_config` VALUES ('15', '亲亲', '1', '5', '100', '0', '1', '1', '1', '4', '1', '1463468102');
INSERT INTO `gift_config` VALUES ('16', '情书', '1', '10', '100', '0', '1', '1', '1', '7', '1', '1463468102');
INSERT INTO `gift_config` VALUES ('17', '跑车', '2', '880', '100', '0', '2', '1', '1', '8', '1', '1463468102');
INSERT INTO `gift_config` VALUES ('18', '飞机', '3', '6666', '100', '0', '2', '1', '1', '9', '1', '1463468102');
INSERT INTO `gift_config` VALUES ('19', '私人海岛', '3', '13140', '100', '0', '2', '1', '1', '10', '1', '1463468104');
INSERT INTO `gift_config` VALUES ('20', '粽子', '1', '5', '100', '0', '0', '1', '1', '1', '1', '1465181277');

-- ----------------------------
-- Table structure for `gift_lucky_probabilitys`
-- ----------------------------
DROP TABLE IF EXISTS `gift_lucky_probabilitys`;
CREATE TABLE `gift_lucky_probabilitys` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `multiples` int(10) NOT NULL COMMENT '倍数',
  `divisor` int(10) NOT NULL COMMENT '除数',
  `dividend` int(10) NOT NULL COMMENT '被除数',
  `isRunWay` int(2) NOT NULL COMMENT '是否上跑道 0不上 1上',
  `maxcount` int(5) NOT NULL COMMENT '连发最高中奖次数',
  `decoratedWord` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '中奖修饰语 如(鸿运当头/人品爆棚/主要看气质等等)',
  `gid` int(11) NOT NULL DEFAULT '0' COMMENT '礼物id，根据此项播放动画特效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gift_lucky_probabilitys
-- ----------------------------

-- ----------------------------
-- Table structure for `group`
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `gid` int(11) NOT NULL,
  `atype` int(3) NOT NULL COMMENT '=1周星 =2幸运 =3活动 =4新品 =5vip =6守护',
  `stime` int(10) NOT NULL COMMENT '活动开始时间',
  `etime` int(10) NOT NULL COMMENT '活动结束时间',
  `isvalid` tinyint(1) NOT NULL DEFAULT '1' COMMENT '=0无效 =1有效',
  PRIMARY KEY (`id`),
  KEY `stime_etime` (`stime`,`etime`)
) ENGINE=InnoDB AUTO_INCREMENT=246 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of group
-- ----------------------------
INSERT INTO `group` VALUES ('241', '102', '1', '1508083200', '1508601600', '1');
INSERT INTO `group` VALUES ('242', '22', '1', '1508688000', '1509206400', '1');
INSERT INTO `group` VALUES ('243', '5', '1', '1508688000', '1509206400', '1');
INSERT INTO `group` VALUES ('245', '21', '1', '1508688000', '1509206400', '1');

-- ----------------------------
-- Table structure for `invitation_config`
-- ----------------------------
DROP TABLE IF EXISTS `invitation_config`;
CREATE TABLE `invitation_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '任务名称',
  `schedule` int(5) NOT NULL COMMENT '任务界限值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='邀请任务表';

-- ----------------------------
-- Records of invitation_config
-- ----------------------------
INSERT INTO `invitation_config` VALUES ('1', '100猪币', '10');
INSERT INTO `invitation_config` VALUES ('2', '500猪币', '30');
INSERT INTO `invitation_config` VALUES ('3', '1000猪币', '50');
INSERT INTO `invitation_config` VALUES ('4', '白金VIP大礼包', '100');
INSERT INTO `invitation_config` VALUES ('5', '钻石VIP大礼包', '300');

-- ----------------------------
-- Table structure for `invitation_rewards_config`
-- ----------------------------
DROP TABLE IF EXISTS `invitation_rewards_config`;
CREATE TABLE `invitation_rewards_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `invitation_id` int(11) NOT NULL COMMENT '任务id',
  `type` int(2) NOT NULL COMMENT '奖励的类型  1:猪币 2：礼物 3：增值服务',
  `rewards` varchar(50) NOT NULL COMMENT '奖励； gid，num',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COMMENT='邀请任务奖励表';

-- ----------------------------
-- Records of invitation_rewards_config
-- ----------------------------
INSERT INTO `invitation_rewards_config` VALUES ('1', '1', '1', '100');
INSERT INTO `invitation_rewards_config` VALUES ('2', '2', '1', '500');
INSERT INTO `invitation_rewards_config` VALUES ('3', '3', '1', '1000');
INSERT INTO `invitation_rewards_config` VALUES ('4', '4', '1', '1000');
INSERT INTO `invitation_rewards_config` VALUES ('5', '4', '2', '7,100');
INSERT INTO `invitation_rewards_config` VALUES ('6', '4', '2', '24,50');
INSERT INTO `invitation_rewards_config` VALUES ('7', '4', '2', '1,20');
INSERT INTO `invitation_rewards_config` VALUES ('8', '4', '2', '23,5');
INSERT INTO `invitation_rewards_config` VALUES ('9', '4', '3', '43,30');
INSERT INTO `invitation_rewards_config` VALUES ('10', '5', '1', '3000');
INSERT INTO `invitation_rewards_config` VALUES ('11', '5', '2', '35,100');
INSERT INTO `invitation_rewards_config` VALUES ('12', '5', '2', '24,100');
INSERT INTO `invitation_rewards_config` VALUES ('13', '5', '2', '1,50');
INSERT INTO `invitation_rewards_config` VALUES ('14', '5', '2', '23,10');
INSERT INTO `invitation_rewards_config` VALUES ('15', '5', '3', '44,30');

-- ----------------------------
-- Table structure for `level_config`
-- ----------------------------
DROP TABLE IF EXISTS `level_config`;
CREATE TABLE `level_config` (
  `lv` int(2) unsigned NOT NULL COMMENT '等级',
  `exp` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '经验值',
  PRIMARY KEY (`lv`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of level_config
-- ----------------------------
INSERT INTO `level_config` VALUES ('1', '0');
INSERT INTO `level_config` VALUES ('2', '8000');
INSERT INTO `level_config` VALUES ('3', '30000');
INSERT INTO `level_config` VALUES ('4', '50000');
INSERT INTO `level_config` VALUES ('5', '90000');
INSERT INTO `level_config` VALUES ('6', '130000');
INSERT INTO `level_config` VALUES ('7', '190000');
INSERT INTO `level_config` VALUES ('8', '250000');
INSERT INTO `level_config` VALUES ('9', '310000');
INSERT INTO `level_config` VALUES ('10', '390000');
INSERT INTO `level_config` VALUES ('11', '470000');
INSERT INTO `level_config` VALUES ('12', '570000');
INSERT INTO `level_config` VALUES ('13', '670000');
INSERT INTO `level_config` VALUES ('14', '790000');
INSERT INTO `level_config` VALUES ('15', '910000');
INSERT INTO `level_config` VALUES ('16', '103000');
INSERT INTO `level_config` VALUES ('17', '1190000');
INSERT INTO `level_config` VALUES ('18', '1350000');
INSERT INTO `level_config` VALUES ('19', '1510000');
INSERT INTO `level_config` VALUES ('20', '1710000');
INSERT INTO `level_config` VALUES ('21', '1910000');
INSERT INTO `level_config` VALUES ('22', '2110000');
INSERT INTO `level_config` VALUES ('23', '2350000');
INSERT INTO `level_config` VALUES ('24', '2590000');
INSERT INTO `level_config` VALUES ('25', '2830000');
INSERT INTO `level_config` VALUES ('26', '3110000');
INSERT INTO `level_config` VALUES ('27', '3390000');
INSERT INTO `level_config` VALUES ('28', '3670000');
INSERT INTO `level_config` VALUES ('29', '4010000');
INSERT INTO `level_config` VALUES ('30', '4350000');
INSERT INTO `level_config` VALUES ('31', '4690000');
INSERT INTO `level_config` VALUES ('32', '5080000');
INSERT INTO `level_config` VALUES ('33', '5470000');
INSERT INTO `level_config` VALUES ('34', '5860000');
INSERT INTO `level_config` VALUES ('35', '6300000');
INSERT INTO `level_config` VALUES ('36', '6740000');
INSERT INTO `level_config` VALUES ('37', '7180000');
INSERT INTO `level_config` VALUES ('38', '7680000');
INSERT INTO `level_config` VALUES ('39', '8180000');
INSERT INTO `level_config` VALUES ('40', '8680000');
INSERT INTO `level_config` VALUES ('41', '9280000');
INSERT INTO `level_config` VALUES ('42', '9880000');
INSERT INTO `level_config` VALUES ('43', '10480000');
INSERT INTO `level_config` VALUES ('44', '11080000');
INSERT INTO `level_config` VALUES ('45', '11820000');
INSERT INTO `level_config` VALUES ('46', '12560000');
INSERT INTO `level_config` VALUES ('47', '13420000');
INSERT INTO `level_config` VALUES ('48', '14280000');
INSERT INTO `level_config` VALUES ('49', '15140000');
INSERT INTO `level_config` VALUES ('50', '16190000');
INSERT INTO `level_config` VALUES ('51', '17240000');
INSERT INTO `level_config` VALUES ('52', '18290000');
INSERT INTO `level_config` VALUES ('53', '19470000');
INSERT INTO `level_config` VALUES ('54', '20650000');
INSERT INTO `level_config` VALUES ('55', '21900000');
INSERT INTO `level_config` VALUES ('56', '23150000');
INSERT INTO `level_config` VALUES ('57', '24400000');
INSERT INTO `level_config` VALUES ('58', '25750000');
INSERT INTO `level_config` VALUES ('59', '27200000');
INSERT INTO `level_config` VALUES ('60', '28650000');
INSERT INTO `level_config` VALUES ('61', '30300000');
INSERT INTO `level_config` VALUES ('62', '31950000');
INSERT INTO `level_config` VALUES ('63', '33700000');
INSERT INTO `level_config` VALUES ('64', '35450000');
INSERT INTO `level_config` VALUES ('65', '37200000');
INSERT INTO `level_config` VALUES ('66', '39200000');
INSERT INTO `level_config` VALUES ('67', '43200000');
INSERT INTO `level_config` VALUES ('68', '43200000');
INSERT INTO `level_config` VALUES ('69', '45200000');
INSERT INTO `level_config` VALUES ('70', '47600000');

-- ----------------------------
-- Table structure for `room_game_management`
-- ----------------------------
DROP TABLE IF EXISTS `room_game_management`;
CREATE TABLE `room_game_management` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '游戏ID',
  `game_key` varchar(255) NOT NULL DEFAULT '' COMMENT '游戏标识(暂时不用)',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '游戏名称',
  `type` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '游戏类型(1:H5;2:原生)',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '游戏状态(1:启用;2:禁用;3:删除;)',
  `server_url` varchar(255) NOT NULL DEFAULT '' COMMENT '服务器地址',
  `init_url` varchar(255) NOT NULL DEFAULT '' COMMENT '初始化接口地址',
  `destory_url` varchar(255) NOT NULL DEFAULT '' COMMENT '销毁接口地址',
  `page_url` varchar(255) NOT NULL DEFAULT '' COMMENT '游戏页面地址',
  `img_url` varchar(255) NOT NULL DEFAULT '' COMMENT '图片地址',
  `game_icon_url` varchar(255) NOT NULL DEFAULT '' COMMENT '游戏小图标',
  `game_commission` double(10,2) NOT NULL DEFAULT '0.00' COMMENT '游戏提成',
  `create_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `create_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建用户ID',
  `update_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '修改时间',
  `update_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '修改用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='直播间游戏管理表';

-- ----------------------------
-- Records of room_game_management
-- ----------------------------
INSERT INTO `room_game_management` VALUES ('1', 'gameKey_001', '欢乐6选3', '1', '1', 'http://192.168.20.251:8081/game', '', '', '/180528105909/index.html', 'http://owvwocjrz.bkt.clouddn.com/oevWIJZuZUGWs2CQ1510564316687.png', 'http://owvwocjrz.bkt.clouddn.com/nyDpDJMc4BAl68BY1510564317061.png', '0.00', '1510208702879', '1', '1527476521090', '1');
INSERT INTO `room_game_management` VALUES ('2', 'gameKey_002', '抓娃娃', '1', '1', 'http://192.168.20.251:8081/game', '', '', '/180528105909/index.html', 'http://owvwocjrz.bkt.clouddn.com/LOLdhCtirscarqBr1511838885817.png', 'http://owvwocjrz.bkt.clouddn.com/FZ0GE4K4g0kJe8ey1511838874620.png', '0.00', '1510208859193', '1', '1527476530354', '1');

-- ----------------------------
-- Table structure for `sign_config`
-- ----------------------------
DROP TABLE IF EXISTS `sign_config`;
CREATE TABLE `sign_config` (
  `day` int(2) unsigned NOT NULL COMMENT '第几天',
  `exp` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '经验值',
  `phase` int(2) unsigned DEFAULT '0' COMMENT '阶段=0 连续7天 =1 连续15天',
  `item` varchar(100) DEFAULT NULL COMMENT '签到赠送礼物 item1-3;item2-2',
  `expIcon` varchar(100) DEFAULT NULL COMMENT '经验对应的图标资源',
  `desc` varchar(100) DEFAULT '' COMMENT '签到描述',
  PRIMARY KEY (`day`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sign_config
-- ----------------------------
INSERT INTO `sign_config` VALUES ('1', '100', '0', null, 'http://owvwocjrz.bkt.clouddn.com/newjingyan_100.png', '连续登录第1天，获得100点经验');
INSERT INTO `sign_config` VALUES ('2', '120', '0', null, 'http://owvwocjrz.bkt.clouddn.com/newjingyan_120.png', '连续登录第2天，获得120点经验');
INSERT INTO `sign_config` VALUES ('3', '140', '0', null, 'http://owvwocjrz.bkt.clouddn.com/newjingyan_140.png', '连续登录第3天，获得140点经验');
INSERT INTO `sign_config` VALUES ('4', '160', '0', null, 'http://owvwocjrz.bkt.clouddn.com/newjingyan_160.png', '连续登录第4天，获得160点经验');
INSERT INTO `sign_config` VALUES ('5', '180', '0', null, 'http://owvwocjrz.bkt.clouddn.com/newjingyan_180.png', '连续登录第5天，获得180点经验');
INSERT INTO `sign_config` VALUES ('6', '200', '0', null, 'http://owvwocjrz.bkt.clouddn.com/newjingyan_200.png', '连续登录第6天，获得200点经验');
INSERT INTO `sign_config` VALUES ('7', '250', '0', null, 'http://owvwocjrz.bkt.clouddn.com/newjingyan_240.png', '连续登录第7天，获得250点经验');
INSERT INTO `sign_config` VALUES ('8', '250', '2', null, 'http://owvwocjrz.bkt.clouddn.com/newjingyan_240.png', '连续登录7天后，获得250点经验');
INSERT INTO `sign_config` VALUES ('15', '1000', '1', null, 'http://owvwocjrz.bkt.clouddn.com/newjingyan_1000.png', '连续登录每满15天（即15日、30日...）额外获得1000点经验值');

-- ----------------------------
-- Table structure for `system_notice`
-- ----------------------------
DROP TABLE IF EXISTS `system_notice`;
CREATE TABLE `system_notice` (
  `id` tinyint(2) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL DEFAULT '' COMMENT '注释内容',
  `utime` int(11) NOT NULL DEFAULT '0' COMMENT '插入时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system_notice
-- ----------------------------
INSERT INTO `system_notice` VALUES ('1', ' ', '0');

-- ----------------------------
-- Table structure for `task_config`
-- ----------------------------
DROP TABLE IF EXISTS `task_config`;
CREATE TABLE `task_config` (
  `id` int(10) unsigned NOT NULL COMMENT '任务id',
  `name` char(30) NOT NULL DEFAULT '' COMMENT '任务名称',
  `type` int(3) unsigned NOT NULL DEFAULT '0' COMMENT '=0新手 =1日常',
  `desc` char(50) NOT NULL DEFAULT '0' COMMENT '任务描述',
  `what` varchar(20) NOT NULL DEFAULT '0' COMMENT '任务提交条件chat',
  `count` int(6) DEFAULT '0',
  `rewards` varchar(50) DEFAULT '0' COMMENT '任务奖励 exp,3#score,10',
  `commitMode` int(3) unsigned NOT NULL DEFAULT '0' COMMENT '=0手动 =1自动',
  `jump` int(2) DEFAULT '-1' COMMENT '=-1 不跳转;=0 首页;=1 个人信息',
  `isValid` int(2) unsigned DEFAULT '1' COMMENT '=1 有效；=0 暂时无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_config
-- ----------------------------
INSERT INTO `task_config` VALUES ('1', '绑定手机', '0', '绑定手机', 'BoundPhone', '1', 'money,500', '0', '2', '1');
INSERT INTO `task_config` VALUES ('2', '完善资料', '0', '完善资料', 'CompleteData', '1', 'exp,300', '0', '3', '1');
INSERT INTO `task_config` VALUES ('3', '身份认证', '0', '身份认证', 'Authentication', '1', 'exp,400', '0', '4', '0');
INSERT INTO `task_config` VALUES ('5', '开播15分钟', '0', '开播15分钟', 'Living15Mins', '1', 'exp,250', '0', '5', '0');
INSERT INTO `task_config` VALUES ('9', '充值', '0', '充值', 'Prepaid', '1', 'money,1500', '0', '6', '1');
INSERT INTO `task_config` VALUES ('10', '送礼', '0', '送礼', 'SendGift', '1', 'money,1000', '0', '0', '1');
INSERT INTO `task_config` VALUES ('11', '发射弹幕', '0', '发射弹幕', 'SendDanmaku', '1', 'exp,400', '0', '0', '0');
INSERT INTO `task_config` VALUES ('13', '完成所有的新手任务', '0', '完成所有的新手任务', 'NewbieFinished', '1', 'money,3000', '0', '0', '1');
INSERT INTO `task_config` VALUES ('14', '首冲超过500元', '0', '首冲超过500元', 'Charge500', '1', 'money,3000', '0', '6', '1');
INSERT INTO `task_config` VALUES ('15', '首次关注一个主播', '0', '首次关注一个主播', 'FollowAnchor', '1', 'exp,100', '0', '0', '1');
INSERT INTO `task_config` VALUES ('16', '首次送主播1个钻戒送礼', '0', '首次送主播1个钻戒送礼', 'SendPopularGift', '1', 'exp,100', '0', '0', '1');
INSERT INTO `task_config` VALUES ('104', '发射弹幕1次', '1', '发射弹幕1次', 'SendDanmaku', '1', 'exp,200', '0', '0', '0');
INSERT INTO `task_config` VALUES ('105', '累计观看直播30分钟', '1', '累计观看直播30分钟', 'Watching30Mins', '1', 'exp,250', '0', '0', '0');
INSERT INTO `task_config` VALUES ('107', '向主播赠送一次付费礼物', '1', '向主播赠送一次付费礼物', 'SendGift', '1', 'exp,200', '0', '0', '1');
INSERT INTO `task_config` VALUES ('108', '累计观看5个直播间', '1', '累计观看5个直播间', 'RoomsEntered', '5', 'exp,250', '0', '0', '1');
INSERT INTO `task_config` VALUES ('109', '开播15分钟', '1', '开播15分钟', 'Living15Mins', '1', 'exp,250', '0', '0', '0');
INSERT INTO `task_config` VALUES ('110', '发送私信1次', '1', '发送私信1次', 'SendLetter', '1', 'exp,250', '0', '0', '0');
INSERT INTO `task_config` VALUES ('111', '观看直播5分钟', '1', '观看直播5分钟', 'WatchLive5Mins', '1', 'exp,100', '0', '0', '1');
INSERT INTO `task_config` VALUES ('112', '每天发射喇叭', '1', '每天发射喇叭', 'SendLouder', '1', 'exp,400', '0', '0', '0');
INSERT INTO `task_config` VALUES ('113', '每天充值成功', '1', '每天充值成功', 'ChargeOk', '1', 'exp,200', '0', '6', '1');
INSERT INTO `task_config` VALUES ('114', '每天累计送出3个钻戒', '1', '每天累计送出3个钻戒', 'SendPopular5', '3', 'exp,250', '0', '0', '1');
INSERT INTO `task_config` VALUES ('115', '每天关注3位主播', '1', '每天关注三位主播', 'Follow3Anchor', '3', 'exp,150', '0', '0', '1');

-- ----------------------------
-- Table structure for `task_config_20170413`
-- ----------------------------
DROP TABLE IF EXISTS `task_config_20170413`;
CREATE TABLE `task_config_20170413` (
  `id` int(10) unsigned NOT NULL COMMENT '任务id',
  `name` char(30) NOT NULL DEFAULT '' COMMENT '任务名称',
  `type` int(3) unsigned NOT NULL DEFAULT '0' COMMENT '=0新手 =1日常',
  `desc` char(50) NOT NULL DEFAULT '0' COMMENT '任务描述',
  `what` varchar(20) NOT NULL DEFAULT '0' COMMENT '任务提交条件chat',
  `count` int(6) DEFAULT '0',
  `rewards` varchar(50) DEFAULT '0' COMMENT '任务奖励 exp,3#score,10',
  `commitMode` int(3) unsigned NOT NULL DEFAULT '0' COMMENT '=0手动 =1自动',
  `jump` int(2) DEFAULT '-1' COMMENT '=-1 不跳转;=0 首页;=1 个人信息',
  `isValid` int(2) unsigned DEFAULT '1' COMMENT '=1 有效；=0 暂时无效'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_config_20170413
-- ----------------------------
INSERT INTO `task_config_20170413` VALUES ('1', '绑定手机', '0', '绑定手机', 'BoundPhone', '1', 'exp,300', '0', '2', '1');
INSERT INTO `task_config_20170413` VALUES ('2', '完善资料', '0', '完善资料', 'CompleteData', '1', 'exp,300', '0', '3', '1');
INSERT INTO `task_config_20170413` VALUES ('3', '身份认证', '0', '身份认证', 'Authentication', '1', 'exp,400', '0', '4', '0');
INSERT INTO `task_config_20170413` VALUES ('5', '开播15分钟', '0', '开播15分钟', 'Living15Mins', '1', 'exp,250', '0', '5', '0');
INSERT INTO `task_config_20170413` VALUES ('9', '充值', '0', '充值', 'Prepaid', '1', 'exp,400', '0', '6', '1');
INSERT INTO `task_config_20170413` VALUES ('10', '送礼', '0', '送礼', 'SendGift', '1', 'exp,400', '0', '0', '1');
INSERT INTO `task_config_20170413` VALUES ('11', '发射弹幕', '0', '发射弹幕', 'SendDanmaku', '1', 'exp,400', '0', '0', '0');
INSERT INTO `task_config_20170413` VALUES ('13', '完成所有的新手任务', '0', '完成所有的新手任务', 'NewbieFinished', '1', 'money,100', '0', '0', '1');
INSERT INTO `task_config_20170413` VALUES ('14', '首冲超过500元', '0', '首冲超过500元', 'Charge500', '1', 'money,500', '0', '6', '1');
INSERT INTO `task_config_20170413` VALUES ('15', '首次关注一个主播', '0', '首次关注一个主播', 'FollowAnchor', '1', 'exp,100', '0', '0', '1');
INSERT INTO `task_config_20170413` VALUES ('16', '首次送主播1个小萌猪送礼', '0', '首次送主播1个小萌猪送礼', 'SendPopularGift', '1', 'exp,100', '0', '0', '1');
INSERT INTO `task_config_20170413` VALUES ('104', '发射弹幕1次', '1', '发射弹幕1次', 'SendDanmaku', '1', 'exp,200', '0', '0', '1');
INSERT INTO `task_config_20170413` VALUES ('105', '累计观看直播30分钟', '1', '累计观看直播30分钟', 'Watching30Mins', '1', 'exp,250', '0', '0', '0');
INSERT INTO `task_config_20170413` VALUES ('107', '向主播赠送一次付费礼物', '1', '向主播赠送一次付费礼物', 'SendGift', '1', 'exp,200', '0', '0', '1');
INSERT INTO `task_config_20170413` VALUES ('108', '累计观看5个直播间', '1', '累计观看5个直播间', 'RoomsEntered', '5', 'exp,250', '0', '0', '1');
INSERT INTO `task_config_20170413` VALUES ('109', '开播15分钟', '1', '开播15分钟', 'Living15Mins', '1', 'exp,250', '0', '0', '0');
INSERT INTO `task_config_20170413` VALUES ('110', '发送私信1次', '1', '发送私信1次', 'SendLetter', '1', 'exp,250', '0', '0', '0');
INSERT INTO `task_config_20170413` VALUES ('111', '观看直播5分钟', '1', '观看直播5分钟', 'WatchLive5Mins', '1', 'exp,100', '0', '0', '1');
INSERT INTO `task_config_20170413` VALUES ('112', '每天发射喇叭', '1', '每天发射喇叭', 'SendLouder', '1', 'exp,400', '0', '0', '1');
INSERT INTO `task_config_20170413` VALUES ('113', '每天充值成功', '1', '每天充值成功', 'ChargeOk', '1', 'exp,200', '0', '6', '1');
INSERT INTO `task_config_20170413` VALUES ('114', '每天累计送出5个小萌猪', '1', '每天累计送出5个小萌猪', 'SendPopular5', '5', 'exp,250', '0', '0', '1');
INSERT INTO `task_config_20170413` VALUES ('115', '每天关注3位主播', '1', '每天关注3位主播', 'Follow3Anchor', '3', 'exp,150', '0', '0', '1');

-- ----------------------------
-- Table structure for `valueadd_level_conf`
-- ----------------------------
DROP TABLE IF EXISTS `valueadd_level_conf`;
CREATE TABLE `valueadd_level_conf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gid` int(11) NOT NULL COMMENT 'VIP或者守护的ID',
  `level` tinyint(5) NOT NULL COMMENT 'vip/守护的级别',
  `exp` int(11) NOT NULL COMMENT 'vip或守护的升级经验',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of valueadd_level_conf
-- ----------------------------
INSERT INTO `valueadd_level_conf` VALUES ('1', '45', '1', '0');
INSERT INTO `valueadd_level_conf` VALUES ('2', '45', '2', '900');
INSERT INTO `valueadd_level_conf` VALUES ('3', '45', '3', '2700');
INSERT INTO `valueadd_level_conf` VALUES ('4', '46', '1', '0');
INSERT INTO `valueadd_level_conf` VALUES ('5', '46', '2', '900');
INSERT INTO `valueadd_level_conf` VALUES ('6', '46', '3', '2700');

-- ----------------------------
-- Table structure for `valueadd_privilege`
-- ----------------------------
DROP TABLE IF EXISTS `valueadd_privilege`;
CREATE TABLE `valueadd_privilege` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gid` int(11) NOT NULL COMMENT '守护/vip ID',
  `level` int(5) NOT NULL COMMENT '守护/vip的等级',
  `permiss_type` int(2) NOT NULL COMMENT '权限类别 : 1 守护 2VIP  仅在权限类别相同时才可操作',
  `permiss_level` int(2) NOT NULL COMMENT '权限级别 : 等级高的可以操作等级低的',
  `kick_user` int(2) NOT NULL COMMENT '是否能踢人 0/1',
  `gag_user` int(2) NOT NULL COMMENT '是否能禁言 0/1',
  `add_rank_score` int(5) NOT NULL COMMENT '增加排名分数',
  `car_id` int(11) NOT NULL COMMENT '座驾id',
  `rq_count` int(5) NOT NULL COMMENT '每日赠送人气礼物的数量',
  `level_speedup` double(5,2) NOT NULL COMMENT '等级加速',
  `first_login_exp` int(5) NOT NULL COMMENT '首次登陆成长值/每天',
  `first_spend_exp` int(5) NOT NULL COMMENT '首次消费成长值/每天',
  `cushion_secs` int(11) NOT NULL DEFAULT '0' COMMENT '保护期时间 (秒数) 守护的默认时间是604800',
  `cushion_icon` int(11) NOT NULL DEFAULT '0' COMMENT '保护期icon',
  `cushion_carid` int(11) NOT NULL COMMENT '保护期座驾',
  `renewal_discount` double(5,2) NOT NULL COMMENT '续费折扣',
  `icon_id` int(11) NOT NULL COMMENT '图标的id',
  `tid` int(11) NOT NULL COMMENT '开通特效id',
  `join_effects` varchar(100) NOT NULL COMMENT '进场特效,色值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of valueadd_privilege
-- ----------------------------
INSERT INTO `valueadd_privilege` VALUES ('1', '43', '1', '2', '1', '1', '1', '5', '0', '0', '0.02', '0', '0', '0', '51', '0', '1.00', '51', '0', '#F8FCF6');
INSERT INTO `valueadd_privilege` VALUES ('2', '44', '1', '2', '2', '1', '1', '10', '50', '0', '0.05', '0', '0', '0', '52', '0', '1.00', '52', '0', '#F8FCF6');
INSERT INTO `valueadd_privilege` VALUES ('3', '45', '1', '1', '1', '1', '1', '7', '47', '15', '0.00', '10', '10', '604800', '69', '63', '0.90', '53', '61', '#F8FCF6');
INSERT INTO `valueadd_privilege` VALUES ('4', '45', '2', '1', '2', '1', '1', '9', '47', '15', '0.00', '10', '10', '604800', '69', '63', '0.90', '54', '61', '#40A42C');
INSERT INTO `valueadd_privilege` VALUES ('5', '45', '3', '1', '3', '1', '1', '11', '47', '15', '0.00', '10', '10', '604800', '69', '63', '0.90', '55', '61', '#4A53D8');
INSERT INTO `valueadd_privilege` VALUES ('6', '46', '1', '1', '11', '1', '1', '15', '48', '55', '0.00', '15', '15', '604800', '69', '64', '0.80', '56', '62', '#EB6100');
INSERT INTO `valueadd_privilege` VALUES ('7', '46', '2', '1', '12', '1', '1', '17', '48', '55', '0.00', '15', '15', '604800', '69', '64', '0.80', '57', '62', '#9430D7');
INSERT INTO `valueadd_privilege` VALUES ('8', '46', '3', '1', '13', '1', '1', '19', '48', '55', '0.00', '15', '15', '604800', '69', '64', '0.80', '58', '62', '#E3031C');
