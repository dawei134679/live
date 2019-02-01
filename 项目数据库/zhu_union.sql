/*
Navicat MySQL Data Transfer

Source Server         : admin
Source Server Version : 50639
Source Host           : 192.168.20.251:3306
Source Database       : zhu_union

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2018-05-29 09:38:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `union_anchor_ref`
-- ----------------------------
DROP TABLE IF EXISTS `union_anchor_ref`;
CREATE TABLE `union_anchor_ref` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `unionid` int(11) unsigned NOT NULL,
  `anchorid` int(11) unsigned NOT NULL,
  `salary` int(11) NOT NULL DEFAULT '0' COMMENT '薪资',
  `rate` double(3,2) NOT NULL DEFAULT '0.00' COMMENT '收礼提成比率',
  `totalmoney` bigint(20) NOT NULL DEFAULT '0' COMMENT '主播房间总消耗',
  `remarks` varchar(100) NOT NULL DEFAULT '',
  `isvalid` int(11) NOT NULL DEFAULT '1' COMMENT '=1有效 其他无效',
  `adminid` int(11) NOT NULL DEFAULT '0' COMMENT '操作者UID',
  `addtime` int(11) NOT NULL DEFAULT '0' COMMENT '添加时间',
  PRIMARY KEY (`id`),
  KEY `anchorid` (`anchorid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of union_anchor_ref
-- ----------------------------
INSERT INTO `union_anchor_ref` VALUES ('1', '1', '10000038', '0', '0.00', '0', '', '1', '1', '1507872359');
INSERT INTO `union_anchor_ref` VALUES ('2', '1', '10000043', '0', '0.00', '0', '', '1', '1', '1508144804');
INSERT INTO `union_anchor_ref` VALUES ('3', '1', '10000042', '0', '0.00', '0', '', '1', '1', '1508144901');
INSERT INTO `union_anchor_ref` VALUES ('4', '1', '10000044', '0', '0.00', '0', '', '1', '1', '1508145198');

-- ----------------------------
-- Table structure for `union_info`
-- ----------------------------
DROP TABLE IF EXISTS `union_info`;
CREATE TABLE `union_info` (
  `unionid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `unionname` varchar(45) NOT NULL DEFAULT '' COMMENT '家族名称',
  `createtime` int(11) NOT NULL COMMENT '添加时间',
  `anchorcount` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '主播数',
  `credit` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '声援值',
  `profit` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '已提现',
  `remarks` varchar(255) NOT NULL DEFAULT '' COMMENT '说明',
  `ownerid` int(10) unsigned NOT NULL COMMENT '添加者',
  `adminname` varchar(45) NOT NULL DEFAULT '' COMMENT '归属人名称',
  `adminuid` int(10) DEFAULT NULL COMMENT '归属人uid',
  `operatoruid` int(10) DEFAULT NULL COMMENT '操作人uid',
  `totalmoney` bigint(20) NOT NULL DEFAULT '0' COMMENT '公会所有房间总消耗',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '家族状态：1启用，0禁用',
  PRIMARY KEY (`unionid`),
  UNIQUE KEY `unionname` (`unionname`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of union_info
-- ----------------------------
INSERT INTO `union_info` VALUES ('1', '火星一族', '1507872281', '0', '0', '0', '火星一族', '10000034', 'test', '1', '1', '0', '1');

-- ----------------------------
-- Table structure for `union_support`
-- ----------------------------
DROP TABLE IF EXISTS `union_support`;
CREATE TABLE `union_support` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL DEFAULT '0' COMMENT '扶持账号',
  `unionid` int(11) NOT NULL DEFAULT '0' COMMENT '扶持公会',
  `amount` int(11) NOT NULL DEFAULT '0' COMMENT '扶持金额(元)',
  `isvalid` int(2) NOT NULL DEFAULT '1' COMMENT '=1 有效 =0 无效',
  `remarks` varchar(255) NOT NULL DEFAULT '' COMMENT '备注说明',
  `addtime` int(11) NOT NULL DEFAULT '0' COMMENT '添加时间',
  `updtime` int(11) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `adminid` int(11) NOT NULL DEFAULT '0' COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='扶持公会的人员名单';

-- ----------------------------
-- Records of union_support
-- ----------------------------

-- ----------------------------
-- Table structure for `user_cover_check`
-- ----------------------------
DROP TABLE IF EXISTS `user_cover_check`;
CREATE TABLE `user_cover_check` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `picCover` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '封面图片',
  `picCover1` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '4:3比例封面图片',
  `picCover2` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '16:9比例封面图片',
  `status` int(2) DEFAULT '0' COMMENT '0：提交待审核；1：审核通过；2：驳回',
  `cause` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '驳回原因',
  PRIMARY KEY (`id`),
  KEY `index_cover_uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user_cover_check
-- ----------------------------
INSERT INTO `user_cover_check` VALUES ('1', '10000038', 'http://oxwi9nqt0.bkt.clouddn.com/10000038/EUiBwHO1508749669436', 'http://oxwi9nqt0.bkt.clouddn.com/10000038/MMbn4P61508749692387', 'http://oxwi9nqt0.bkt.clouddn.com/10000038/rwQsxuX1508749712756', '1', '');
INSERT INTO `user_cover_check` VALUES ('2', '10000038', 'http://oxwi9nqt0.bkt.clouddn.com/10000038/8z0iaYb1508750057445', 'http://oxwi9nqt0.bkt.clouddn.com/10000038/T46iIb61508750040968', 'http://oxwi9nqt0.bkt.clouddn.com/10000038/6mPEEB01508750047274', '2', 'asd');
INSERT INTO `user_cover_check` VALUES ('3', '10000038', 'http://oxwi9nqt0.bkt.clouddn.com/10000038/Q7BKrme1508750317023', 'http://oxwi9nqt0.bkt.clouddn.com/10000038/yHyZbgj1508751121428', 'http://oxwi9nqt0.bkt.clouddn.com/10000038/iRDaJqM1508751175850', '2', 'sd');
INSERT INTO `user_cover_check` VALUES ('4', '10000036', 'http://p0gyccza3.bkt.clouddn.com/10000036', null, null, '2', '123123');
INSERT INTO `user_cover_check` VALUES ('5', '10000033', 'http://p0gyccza3.bkt.clouddn.com/10000033', null, null, '2', '123');
INSERT INTO `user_cover_check` VALUES ('6', '10000041', 'http://p0gyccza3.bkt.clouddn.com/10000041', null, null, '2', '234234');
INSERT INTO `user_cover_check` VALUES ('7', '10000033', 'http://p0gyccza3.bkt.clouddn.com/10000033', null, null, '2', '234');
INSERT INTO `user_cover_check` VALUES ('8', '10000036', 'http://p0gyccza3.bkt.clouddn.com/10000036', null, null, '2', '12');
INSERT INTO `user_cover_check` VALUES ('9', '10000038', 'http://p0gyccza3.bkt.clouddn.com/10000038/ULB4dGG1521438892381', null, null, '1', '');
INSERT INTO `user_cover_check` VALUES ('10', '10000033', 'http://p0gyccza3.bkt.clouddn.com/10000033/0XDwb3x1521439121118', null, null, '1', '');
INSERT INTO `user_cover_check` VALUES ('11', '10000036', 'http://p0gyccza3.bkt.clouddn.com/10000036/zpFUunh1521440208807', null, null, '1', '');
INSERT INTO `user_cover_check` VALUES ('12', '10000036', 'http://p0gyccza3.bkt.clouddn.com/10000036/PGkDTik1521440798002', null, null, '1', '');
INSERT INTO `user_cover_check` VALUES ('13', '10000036', 'http://p0gyccza3.bkt.clouddn.com/10000036/06rjhhI1521441237779', null, null, '1', '');
INSERT INTO `user_cover_check` VALUES ('14', '10000036', 'http://p0gyccza3.bkt.clouddn.com/10000036', null, null, '2', '没上传成功');
INSERT INTO `user_cover_check` VALUES ('15', '10000036', 'http://p0gyccza3.bkt.clouddn.com/10000036/SlX3CvT1521442265090', null, null, '1', '');
INSERT INTO `user_cover_check` VALUES ('16', '10000092', 'http://p0gyccza3.bkt.clouddn.com/10000092', null, null, '2', 'azzzz');
INSERT INTO `user_cover_check` VALUES ('17', '10000034', 'http://p0gyccza3.bkt.clouddn.com/10000034/xzLhYgy1521516836559', null, null, '1', '');
