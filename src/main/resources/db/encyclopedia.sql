-- MySQL dump 10.13  Distrib 5.7.24, for Win64 (x86_64)
--
-- Host: localhost    Database: encyclopedia
-- ------------------------------------------------------
-- Server version	5.7.24-log

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
-- Table structure for table `appinfo`
--

DROP TABLE IF EXISTS `appinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appinfo` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `nsid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '名称表ID',
  `dpid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '描述表ID',
  `dptype` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '描述信息类型',
  `taxonid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '物种ID',
  `status` int(11) DEFAULT '0' COMMENT '状态（默认1、可用；0、不可用）',
  `inputtime` datetime DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `birds`
--

DROP TABLE IF EXISTS `birds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `birds` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `newid` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '新id',
  `oldid` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '老id',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '含义',
  `inputtime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '状态（默认1、可用；0、不可用）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `citation`
--

DROP TABLE IF EXISTS `citation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `citation` (
  `id` varchar(50) NOT NULL,
  `scientificname` varchar(500) DEFAULT NULL COMMENT '引证名称',
  `pinyin` varchar(500) DEFAULT NULL COMMENT '拼音',
  `nametype` varchar(500) DEFAULT NULL COMMENT '名称类型',
  `authorship` varchar(500) DEFAULT NULL COMMENT '命名信息',
  `citationstr` varchar(5000) DEFAULT NULL COMMENT '完整引证',
  `status` int(11) DEFAULT '0' COMMENT '状态（默认1、可用；0、不可用）',
  `taxon_id` varchar(50) DEFAULT NULL,
  `shortrefs` json DEFAULT NULL COMMENT '对citaitonstr进行拆分得到的短引证',
  `sourcesid` varchar(100) DEFAULT NULL COMMENT '数据源',
  `specialist` varchar(100) DEFAULT NULL COMMENT '审核专家',
  `referencejson` json DEFAULT NULL COMMENT '相关参考文献的json数组',
  `inputer` varchar(50) DEFAULT NULL COMMENT '录入人',
  `inputtime` date DEFAULT NULL COMMENT '录入时间',
  `synchdate` date DEFAULT NULL COMMENT '最后同步日期',
  `remark` longtext,
  PRIMARY KEY (`id`),
  KEY `FK9krf2ip0pi1fbmdnhvwpqh7b8` (`taxon_id`),
  CONSTRAINT `FK9krf2ip0pi1fbmdnhvwpqh7b8` FOREIGN KEY (`taxon_id`) REFERENCES `taxon` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `collection`
--

DROP TABLE IF EXISTS `collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collection` (
  `id` varchar(50) NOT NULL,
  `name` varchar(200) DEFAULT NULL COMMENT '收藏名称',
  `description` longtext COMMENT '收藏描述',
  `userId` varchar(50) DEFAULT NULL COMMENT '用户ID',
  `inputer` varchar(50) DEFAULT NULL COMMENT '录入人',
  `inputtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` varchar(50) NOT NULL,
  `content` longtext,
  `userId` varchar(50) DEFAULT NULL COMMENT '用户ID',
  `dataId` varchar(50) DEFAULT NULL COMMENT 'taxon下某一条信息的ID',
  `taxon_id` varchar(50) DEFAULT NULL,
  `inputer` varchar(50) DEFAULT NULL COMMENT '录入人',
  `inputtime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '状态（默认1、可用；0、不可用）',
  PRIMARY KEY (`id`),
  KEY `FKmrcox438h4wgrng3yjb3gpvr3` (`taxon_id`),
  CONSTRAINT `FKmrcox438h4wgrng3yjb3gpvr3` FOREIGN KEY (`taxon_id`) REFERENCES `taxon` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `commonname`
--

DROP TABLE IF EXISTS `commonname`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `commonname` (
  `id` varchar(50) NOT NULL,
  `commonname` varchar(500) DEFAULT NULL COMMENT '俗名',
  `pinyin` varchar(500) DEFAULT NULL COMMENT '拼音',
  `language` varchar(500) DEFAULT NULL COMMENT '俗名语言',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `sourcesid` varchar(50) DEFAULT NULL COMMENT '数据源id',
  `taxon_id` varchar(50) DEFAULT NULL,
  `specialist` varchar(500) DEFAULT NULL COMMENT '审核专家',
  `referencejson` json DEFAULT NULL COMMENT '参考文献',
  `inputer` varchar(50) DEFAULT NULL COMMENT '录入人',
  `inputtime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '状态（默认1、可用；0、不可用）',
  `synchdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhc949ai5fddf9rvf6vj7m8d3n` (`taxon_id`),
  CONSTRAINT `FKhc949ai5fddf9rvf6vj7m8d3n` FOREIGN KEY (`taxon_id`) REFERENCES `taxon` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `count`
--

DROP TABLE IF EXISTS `count`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `count` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `inputer` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人',
  `inputtime` datetime DEFAULT NULL,
  `num` int(11) DEFAULT NULL COMMENT '浏览数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `countplant`
--

DROP TABLE IF EXISTS `countplant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `countplant` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `scientificname` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '拉丁名',
  `chinesename` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '中文名',
  `path` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '图片路径',
  `status` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '是否有百科',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `datasources`
--

DROP TABLE IF EXISTS `datasources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `datasources` (
  `id` varchar(50) NOT NULL,
  `dsname` varchar(100) DEFAULT NULL COMMENT '数据源名称',
  `dstype` varchar(100) DEFAULT NULL COMMENT '数据源类型',
  `version` varchar(100) DEFAULT NULL COMMENT '版本',
  `copyright` varchar(500) DEFAULT NULL COMMENT '版权声明',
  `rightsholder` varchar(100) DEFAULT NULL COMMENT '版权所有者',
  `creater` varchar(100) DEFAULT NULL COMMENT '创建者',
  `createtime` varchar(100) DEFAULT NULL COMMENT '创建时间',
  `dsabstract` varchar(1000) DEFAULT NULL COMMENT '数据源摘要',
  `dskeyword` varchar(100) DEFAULT NULL COMMENT '关键字',
  `dsurl` varchar(1000) DEFAULT NULL COMMENT '链接',
  `info` longtext,
  `specialist` varchar(100) DEFAULT NULL COMMENT '审核专家',
  `datalevel` int(11) DEFAULT '0' COMMENT '数据源等级',
  `inputer` varchar(100) DEFAULT NULL COMMENT '录入人',
  `inputtime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '状态（默认1、可用；0、不可用）',
  `synchdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `description`
--

DROP TABLE IF EXISTS `description`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `description` (
  `id` varchar(50) NOT NULL,
  `destitle` varchar(600) DEFAULT NULL COMMENT '学名',
  `descontent` longtext COMMENT '描述内容',
  `describer` varchar(500) DEFAULT NULL COMMENT '描述人',
  `desdate` varchar(50) DEFAULT NULL COMMENT '描述时间',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `language` varchar(50) DEFAULT NULL COMMENT '语言',
  `copyright` varchar(500) DEFAULT NULL COMMENT '版权声明',
  `rightsholder` varchar(500) DEFAULT NULL COMMENT '版权所有者',
  `licenseid` varchar(500) DEFAULT NULL COMMENT '共享协议',
  `datalevel` int(11) DEFAULT '0' COMMENT '资料完善度',
  `sourcesid` varchar(50) DEFAULT NULL COMMENT '数据源',
  `specialist` varchar(100) DEFAULT NULL COMMENT '审核专家',
  `referencejson` json DEFAULT NULL COMMENT '参考文献',
  `inputer` varchar(100) DEFAULT NULL COMMENT '录入人',
  `inputtime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '状态（默认1、可用；0、不可用）',
  `synchdate` datetime DEFAULT NULL,
  `descriptiontype_id` varchar(50) DEFAULT NULL,
  `taxon_id` varchar(50) DEFAULT NULL,
  `appstatus` int(11) DEFAULT '0' COMMENT '状态（1、app可用；0、app不可用）',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FKo7fig71pa13sjhgc9uuem0e2d` (`descriptiontype_id`) USING BTREE,
  KEY `FKaxt4l2m47rpe204mwel9sy66i` (`taxon_id`) USING BTREE,
  CONSTRAINT `FKaxt4l2m47rpe204mwel9sy66i` FOREIGN KEY (`taxon_id`) REFERENCES `taxon` (`id`),
  CONSTRAINT `FKo7fig71pa13sjhgc9uuem0e2d` FOREIGN KEY (`descriptiontype_id`) REFERENCES `descriptiontype` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `descriptiontype`
--

DROP TABLE IF EXISTS `descriptiontype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `descriptiontype` (
  `id` varchar(50) NOT NULL,
  `termname` varchar(100) DEFAULT NULL COMMENT '术语名称',
  `dtorder` int(11) DEFAULT NULL COMMENT '父级顺序的ID',
  `meaning` varchar(100) DEFAULT NULL COMMENT '含义',
  `pid` varchar(100) DEFAULT NULL COMMENT '父级ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `homepage`
--

DROP TABLE IF EXISTS `homepage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `homepage` (
  `id` varchar(50) NOT NULL,
  `scientificname` varchar(1000) DEFAULT NULL COMMENT '学名:正式接受的拉丁名 ,不包括命名人和命名年代',
  `chinesename` varchar(500) DEFAULT NULL COMMENT '中文名',
  `type` varchar(45) DEFAULT NULL COMMENT '类别',
  `path` varchar(2000) DEFAULT NULL COMMENT '图片路径',
  `taxonID` varchar(50) DEFAULT NULL COMMENT '分类单元ID',
  `descontent` text COMMENT '描述信息',
  `inputer` varchar(50) DEFAULT NULL COMMENT '录入人',
  `inputtime` date DEFAULT NULL COMMENT '录入时间',
  `status` int(11) DEFAULT '0' COMMENT '状态（默认1、可用；0、不可用）',
  `synchdate` date DEFAULT NULL COMMENT '最后同步日期',
  `level` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `license`
--

DROP TABLE IF EXISTS `license`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `license` (
  `id` varchar(50) NOT NULL,
  `licensetitle` varchar(200) DEFAULT NULL COMMENT 'CC协议名称',
  `licenseinfo` longtext,
  `imageurl` varchar(200) DEFAULT NULL COMMENT '链接',
  `inputer` varchar(50) DEFAULT NULL COMMENT '录入人',
  `inputtime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '状态（默认1、可用；0、不可用）',
  `synchdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `id` varchar(50) NOT NULL,
  `apiservice` longtext COMMENT '申请理由',
  `domainname` varchar(500) DEFAULT NULL COMMENT '域名',
  `sendtime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '未审核\n0：审核通过\n1：审核未通过\n2',
  `userid` varchar(100) DEFAULT NULL COMMENT '申请人ID',
  `apikey` varchar(50) DEFAULT NULL COMMENT 'API密钥',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `multimedia`
--

DROP TABLE IF EXISTS `multimedia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `multimedia` (
  `id` varchar(50) NOT NULL,
  `title` varchar(1000) DEFAULT NULL COMMENT '标题',
  `medialabel` varchar(2000) DEFAULT NULL COMMENT '图注',
  `path` varchar(2000) DEFAULT NULL COMMENT '存储路径',
  `sourcesid` varchar(50) DEFAULT NULL COMMENT '数据源的id',
  `taxon_id` varchar(50) DEFAULT NULL,
  `taxasetid` varchar(50) DEFAULT NULL COMMENT '数据源的id',
  `findtime` datetime DEFAULT NULL,
  `mediatype` varchar(50) DEFAULT NULL COMMENT '类型',
  `mediainfo` varchar(200) DEFAULT NULL COMMENT '简介',
  `originurl` varchar(200) DEFAULT NULL COMMENT '原始路径',
  `copyright` varchar(200) DEFAULT NULL COMMENT '版权声明',
  `rightsholder` varchar(60) DEFAULT NULL COMMENT '版权人',
  `country` varchar(200) DEFAULT NULL COMMENT '国家',
  `province` varchar(200) DEFAULT NULL COMMENT '省',
  `city` varchar(200) DEFAULT NULL COMMENT '市',
  `county` varchar(200) DEFAULT NULL COMMENT '县',
  `locality` varchar(200) DEFAULT NULL COMMENT '小地点',
  `lng` double DEFAULT NULL COMMENT '经度',
  `lat` double DEFAULT NULL COMMENT '纬度',
  `location` varchar(200) DEFAULT NULL COMMENT '详细地点',
  `announcer` varchar(50) DEFAULT NULL COMMENT '发布人',
  `contributor` varchar(50) DEFAULT NULL COMMENT '贡献人',
  `desid` varchar(200) DEFAULT NULL COMMENT '描述的ID，及该记录来源于哪个描述，可以为空',
  `lisenceid` varchar(200) DEFAULT NULL COMMENT '共享协议',
  `specialist` varchar(200) DEFAULT NULL COMMENT '审核专家',
  `referencejson` json DEFAULT NULL COMMENT '相关参考文献的json数组',
  `inputer` varchar(50) DEFAULT NULL COMMENT '录入人',
  `inputtime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '状态（默认1、可用；0、不可用）',
  `synchdate` datetime DEFAULT NULL,
  `mpath` varchar(200) DEFAULT NULL COMMENT '存储路径',
  `urltype` varchar(200) DEFAULT NULL COMMENT '存储路径类别',
  PRIMARY KEY (`id`),
  KEY `FK82587eukreboldyptwuye4lnp` (`taxon_id`),
  KEY `index_sourcesid` (`sourcesid`),
  KEY `index_title` (`title`),
  KEY `index_taxon_id` (`taxon_id`),
  KEY `index_taxasetid` (`taxasetid`),
  CONSTRAINT `FK82587eukreboldyptwuye4lnp` FOREIGN KEY (`taxon_id`) REFERENCES `taxon` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `namedata`
--

DROP TABLE IF EXISTS `namedata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `namedata` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `scientificname` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '学名:正式接受的拉丁名 ,不包括命名人和命名年代',
  `chinesename` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '中文名',
  `type` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '类型',
  `source` longtext COLLATE utf8_bin,
  `sortjson` json DEFAULT NULL COMMENT '分类地位',
  PRIMARY KEY (`id`),
  KEY `index_chinesename` (`chinesename`),
  KEY `index_scientificname` (`scientificname`),
  KEY `index_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `namesource`
--

DROP TABLE IF EXISTS `namesource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `namesource` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `scientificname` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `chinesename` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `source` text COLLATE utf8_bin,
  PRIMARY KEY (`id`),
  KEY `index_scientificname` (`scientificname`),
  KEY `index_chinesename` (`chinesename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `protection`
--

DROP TABLE IF EXISTS `protection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `protection` (
  `id` varchar(50) NOT NULL,
  `proassessment` varchar(1500) DEFAULT NULL COMMENT '保护评估',
  `sourcesid` varchar(50) DEFAULT NULL COMMENT '数据源id',
  `referencejson` json DEFAULT NULL COMMENT '相关参考文献的json数组',
  `inputer` varchar(50) DEFAULT NULL COMMENT '录入人',
  `inputtime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '状态（默认1、可用；0、不可用）',
  `synchdate` datetime DEFAULT NULL,
  `protectstandard_id` int(11) DEFAULT NULL,
  `taxon_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn5qhlsaan6sjx89wfbv2vcihb` (`taxon_id`),
  KEY `FKoia2d2lad8lbc290s9yxapn5j` (`protectstandard_id`),
  CONSTRAINT `FKn5qhlsaan6sjx89wfbv2vcihb` FOREIGN KEY (`taxon_id`) REFERENCES `taxon` (`id`),
  CONSTRAINT `FKoia2d2lad8lbc290s9yxapn5j` FOREIGN KEY (`protectstandard_id`) REFERENCES `protectstandard` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `protectstandard`
--

DROP TABLE IF EXISTS `protectstandard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `protectstandard` (
  `id` int(11) NOT NULL,
  `standardname` varchar(50) DEFAULT NULL COMMENT '保护标准名称',
  `standardinfo` varchar(200) DEFAULT NULL COMMENT '标准简介',
  `version` varchar(50) DEFAULT NULL COMMENT '版本',
  `level` varchar(50) DEFAULT NULL COMMENT '等级',
  `releasedate` varchar(50) DEFAULT NULL COMMENT '发布时间',
  `source` varchar(50) DEFAULT NULL COMMENT '标准来源，IUCN、CITES等',
  `inputer` varchar(50) DEFAULT NULL COMMENT '录入人',
  `inputtime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '状态（默认1、可用；0、不可用）',
  `synchdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ranks`
--

DROP TABLE IF EXISTS `ranks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ranks` (
  `id` int(11) NOT NULL,
  `rankcn` varchar(100) DEFAULT NULL COMMENT '等级的中文名',
  `ranken` varchar(100) DEFAULT NULL COMMENT '等级的英文名',
  `sort` varchar(30) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `refs`
--

DROP TABLE IF EXISTS `refs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `refs` (
  `id` varchar(50) NOT NULL,
  `refstr` varchar(3000) DEFAULT NULL COMMENT '完整题录',
  `author` varchar(2000) DEFAULT NULL COMMENT '作者',
  `pyear` varchar(500) DEFAULT NULL COMMENT '发表年代',
  `title` varchar(3000) DEFAULT NULL COMMENT '标题',
  `reftype` varchar(50) DEFAULT NULL COMMENT '文献类型',
  `journal` varchar(1000) DEFAULT NULL COMMENT '期刊/专著/论文集',
  `language` varchar(100) DEFAULT NULL COMMENT '文献语言',
  `orignlang` varchar(100) DEFAULT NULL COMMENT '原始语言',
  `keywords` varchar(500) DEFAULT NULL COMMENT '关键字',
  `translator` varchar(500) DEFAULT NULL COMMENT '编译者',
  `press` varchar(500) DEFAULT NULL COMMENT '出版社',
  `place` varchar(500) DEFAULT NULL COMMENT '出版地',
  `issue` varchar(300) DEFAULT NULL COMMENT '期',
  `pagenumber` varchar(300) DEFAULT NULL COMMENT '总页数',
  `wordnum` varchar(300) DEFAULT NULL COMMENT '总字数',
  `version` varchar(50) DEFAULT NULL COMMENT '版本',
  `refurl` varchar(1000) DEFAULT NULL COMMENT '链接地址',
  `isbn` varchar(100) DEFAULT NULL COMMENT '文献标识',
  `startpage` varchar(300) DEFAULT NULL COMMENT '起始页',
  `endpage` varchar(300) DEFAULT NULL COMMENT '终止页',
  `volume` varchar(300) DEFAULT NULL COMMENT '卷',
  `remark` longtext,
  `inputer` varchar(50) DEFAULT NULL COMMENT '录入人',
  `inputtime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '状态（默认1、可用；0、不可用）',
  `synchdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `specialist`
--

DROP TABLE IF EXISTS `specialist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `specialist` (
  `id` varchar(50) NOT NULL,
  `cnname` varchar(500) DEFAULT NULL COMMENT '专家姓名',
  `enname` varchar(500) DEFAULT NULL COMMENT '专家英文名',
  `email` varchar(50) DEFAULT NULL COMMENT '电子邮箱',
  `cnaddress` varchar(1000) DEFAULT NULL COMMENT '单位地址',
  `enaddress` varchar(1000) DEFAULT NULL COMMENT '单位地址（英文）',
  `cncompany` varchar(1000) DEFAULT NULL COMMENT '单位名称',
  `encompany` varchar(1000) DEFAULT NULL COMMENT '单位英文名称',
  `cnhomepage` varchar(500) DEFAULT NULL COMMENT '专家主页',
  `enhomepage` varchar(500) DEFAULT NULL COMMENT '专家主页（英文）',
  `introduction` varchar(1000) DEFAULT NULL COMMENT '简介',
  `inputer` varchar(50) DEFAULT NULL COMMENT '录入人',
  `inputtime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '状态（默认1、可用；0、不可用）',
  `synchdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `taxon`
--

DROP TABLE IF EXISTS `taxon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `taxon` (
  `id` varchar(50) NOT NULL,
  `scientificname` varchar(80) DEFAULT NULL COMMENT '学名:正式接受的拉丁名 ,不包括命名人和命名年代',
  `chinesename` varchar(255) DEFAULT NULL COMMENT '中文名',
  `pinyin` varchar(200) DEFAULT NULL COMMENT '拼音',
  `type` varchar(200) DEFAULT NULL COMMENT '类型',
  `assort` varchar(200) DEFAULT NULL COMMENT '二级分类',
  `authorship` varchar(255) DEFAULT NULL COMMENT '命名信息',
  `epithet` varchar(200) DEFAULT NULL COMMENT '种加词 或 亚种加词',
  `rankid` int(11) DEFAULT NULL COMMENT '分类等级',
  `nomencode` varchar(50) DEFAULT NULL COMMENT '命名法规',
  `remark` longtext COMMENT '备注',
  `tci` varchar(100) DEFAULT NULL COMMENT 'taxon concept identifier\\ntaxon 概念标识',
  `sourcesid` varchar(50) DEFAULT NULL COMMENT '数据源',
  `specialist` varchar(200) DEFAULT NULL COMMENT '审核专家',
  `refclassification` varchar(200) DEFAULT NULL COMMENT '参考分类体系',
  `referencejson` json DEFAULT NULL COMMENT '参考文献',
  `status` int(11) DEFAULT '0' COMMENT '状态（默认1、可用；0、不可用）',
  `inputer` varchar(50) DEFAULT NULL COMMENT '录入人',
  `inputtime` datetime DEFAULT NULL,
  `synchdate` datetime DEFAULT NULL,
  `browse` int(11) DEFAULT NULL COMMENT '浏览次数',
  `taxonidjson` json DEFAULT NULL COMMENT '中文名相同',
  PRIMARY KEY (`id`),
  KEY `index_scientificname` (`scientificname`),
  KEY `index_type` (`type`),
  KEY `index_browse` (`browse`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `taxon_has_collection`
--

DROP TABLE IF EXISTS `taxon_has_collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `taxon_has_collection` (
  `collection_id` varchar(50) NOT NULL,
  `taxon_id` varchar(50) NOT NULL,
  `inputtime` datetime DEFAULT NULL,
  PRIMARY KEY (`collection_id`,`taxon_id`),
  KEY `FKd4j9ita5xgndvjex6mt6k8uc5` (`taxon_id`),
  CONSTRAINT `FKd4j9ita5xgndvjex6mt6k8uc5` FOREIGN KEY (`taxon_id`) REFERENCES `taxon` (`id`),
  CONSTRAINT `FKkx8cl4iid247rgv2l3br8ovia` FOREIGN KEY (`collection_id`) REFERENCES `collection` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `taxoncount`
--

DROP TABLE IF EXISTS `taxoncount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `taxoncount` (
  `id` varchar(50) NOT NULL,
  `dcount` int(11) DEFAULT NULL COMMENT '描述数量',
  `dfcount` int(11) DEFAULT NULL COMMENT '形态描述数量',
  `multimedia` int(11) DEFAULT NULL COMMENT '多媒体数量',
  `taxonID` varchar(50) DEFAULT NULL COMMENT '分类单元ID',
  `type` varchar(45) DEFAULT NULL COMMENT '类别',
  `assort` varchar(200) DEFAULT NULL COMMENT '二级分类',
  `inputer` varchar(50) DEFAULT NULL COMMENT '录入人',
  `inputtime` date DEFAULT NULL COMMENT '录入时间',
  `status` int(11) DEFAULT '0' COMMENT '状态（默认1、可用；0、不可用）',
  `synchdate` date DEFAULT NULL COMMENT '最后同步日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `taxonds`
--

DROP TABLE IF EXISTS `taxonds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `taxonds` (
  `id` varchar(50) NOT NULL,
  `taxonid` varchar(50) DEFAULT NULL COMMENT 'taxonID',
  `sourcesid` varchar(50) DEFAULT NULL COMMENT '数据源ID',
  `oldtaxonid` varchar(50) DEFAULT NULL COMMENT 'oldID',
  `oldsourcesid` varchar(50) DEFAULT NULL COMMENT 'old数据源ID',
  `taxasetid` varchar(50) DEFAULT NULL COMMENT '分类单元集ID',
  `relations` varchar(1000) DEFAULT NULL COMMENT '状态（0:自身、1:拉丁名相同中文相同、2:拉丁名相同中文名不同、3:拉丁名不同中文名相同）',
  `sortjson` json DEFAULT NULL COMMENT '分类地位',
  `oldsortjson` json DEFAULT NULL COMMENT 'old分类地位',
  `status` int(11) DEFAULT '0' COMMENT '状态（默认1、可用；0、不可用）',
  `inputer` varchar(100) DEFAULT NULL COMMENT '录入人',
  `inputtime` datetime DEFAULT NULL,
  `synchdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` varchar(36) NOT NULL,
  `username` varchar(45) NOT NULL,
  `nickname` varchar(50) DEFAULT NULL COMMENT '用户昵称',
  `realName` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `pwd` varchar(50) DEFAULT NULL COMMENT '用户密码',
  `password` varchar(45) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号码',
  `profilePicture` varchar(50) DEFAULT NULL COMMENT '个人资料图片',
  `role` varchar(45) DEFAULT NULL,
  `countryCode` varchar(50) DEFAULT NULL COMMENT '国家代码',
  `lastSignInTime` varchar(50) DEFAULT NULL COMMENT '退出时间',
  `signUpTime` varchar(50) DEFAULT NULL COMMENT '登陆时间',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `verificationCode` varchar(50) DEFAULT NULL COMMENT '验证码',
  `verificationCodeExpiryTime` datetime DEFAULT NULL,
  `token` varchar(50) DEFAULT NULL COMMENT 'token',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-11-25 16:09:29
