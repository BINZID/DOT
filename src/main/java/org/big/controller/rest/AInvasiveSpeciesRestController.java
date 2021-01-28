package org.big.controller.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.big.service.AParseUtilsService;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
/**
 * 解析入侵植物数据
 * @author BIGIOZ
 *
 */
@RestController
@Controller
@RequestMapping("/console/plant/rest")
public class AInvasiveSpeciesRestController {

	@Autowired
    private AParseUtilsService aParseUtilsService;
	private String uploadPath = "dot/";
	
	/**
	 * 根据分类单元集合并入侵植物数据
	 * 以2018年数据为主，将其他年份的物种相关的描述、俗名、分布数据关联到2018
	 */
    @RequestMapping("/mergeRuqinData")
	private String mergeRuqinData() {
    	/*TaxasetId
    	4ce0685f14a043d784926ca22ee25424	入侵植物 2018		E:\入侵物种年度数据\入侵植物\2018\彩色照片
    	d6569c3b9f1c4e59920136d64bc014c5	入侵植物 2019		E:\入侵物种年度数据\入侵植物\2019\第四子课题入侵植物专题\先导专项——150种外来入侵*/    	
    	
    	String[] taxasetsP = {"4ce0685f14a043d784926ca22ee25424", "d6569c3b9f1c4e59920136d64bc014c5"}; // 入侵植物数据 - 2018 || 2019
    	this.aParseUtilsService.mergeRuqinData(taxasetsP);
    	return null;
	}
   

    /**
     * 数据处理 - 解析入侵植物原始数据生成UUID
     * @return
     * @throws Exception
     */
    @RequestMapping("/setRQUUID")
    public JSON setRQUUID() throws Exception {
    	this.aParseUtilsService.setRQUUID();
    	return null;
    }
    
    /**
     * 数据解析 - 解析入侵植物原始数据 -- 物种数据
     * 数据概况：assess 一张大表，包含物种拉丁名、中文名、物种俗名、描述（国内分布、性状、地理分布图鉴）
     * @return
     * @throws Exception
     */
    @RequestMapping("/getTaxonDataFormRQZW")
    public JSON getTaxonData() throws Exception {
    	this.aParseUtilsService.getTaxonData();
    	return null;
    }
    
    /**
     * 数据解析 - 解析入侵植物分类单元2 - excel模板数据
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseRQZW2Taxon")
	public JSON parseRQZW2Taxon() throws Exception {
    	String path = "E:/入侵植物-最终合并数据/数据/3-分类单元信息.xlsx";
    	this.aParseUtilsService.parseRQZW2Taxon(path);
		return null;
	}
    
    /**
     * 数据解析 - 解析入侵植物分类单元命名信息 - excel模板数据
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseRQZWTaxonInfo")
	public JSON parseRQZWTaxonInfo() throws Exception {
    	String path = "E:/入侵植物-最终合并数据/数据/3-分类单元信息.xlsx";
    	this.aParseUtilsService.parseRQZWTaxonInfo(path);
		return null;
	}
    
    /**
     * 数据解析 - 解析入侵植物分类单元引证信息
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseRQZWCitation")
	public JSON parseRQZWCitation() throws Exception {
    	String path = "E:/入侵植物-最终合并数据/数据/4-引证信息.xlsx";
    	this.aParseUtilsService.parseRQZWCitation(path);
		return null;
	}
    /**
     * 解析入侵植物分类单元俗名信息
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseRQZWCommonname")
    public JSON parseRQZWCommonname() throws Exception {
    	String path = "E:/入侵物种年度数据/入侵植物/2018/数据/6-俗名信息.xlsx";
    	this.aParseUtilsService.parseRQZWCommonname(path);
    	return null;
    }
    
    /**
     * 解析入侵植物分类单元描述信息
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseRQZWDesc")
    public JSON parseRQZWDesc() throws Exception {
    	String path = "E:/入侵植物-最终合并数据/数据/5-描述信息.xlsx";
    	this.aParseUtilsService.parseRQZWDesc(path);
    	return null;
    }
    
    /**
     * 数据解析 - 解析入侵植物多媒体数据 2019 文件夹下的图片
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseRQZWMedia")
   	public JSON getRQMedia(HttpServletRequest request) throws Exception {
       	//String path = "E:/入侵动物/2018年名录";
       	String path = "E:/入侵物种年度数据/入侵植物/2019/第四子课题入侵植物专题/先导专项——150种外来入侵";
       	this.aParseUtilsService.getRQMedia(path, request, uploadPath + "images/");
   		return null;
   	}
    
    /**
     * 数据解析 - 解析入侵植物分类单元分布数据1
     * 物种的多个分布地拼接
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseRQZWDescdata")
    public JSON parseRQZWDescdata() throws Exception {
    	String path = "E:/入侵植物-最终合并数据/数据/7-分布数据.xlsx";
    	this.aParseUtilsService.parseRQZWDescdata(path);
    	return null;
    }
    
    /**
     * 数据解析 - 解析入侵植物分类单元分布数据2
     * 创建描述、分布实体存储物种分布数据（分布关联描述）
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseRQZWDescdis")
    public JSON parseRQZWDescdis() throws Exception {
    	String path = "E:/入侵植物-最终合并数据/数据/7-分布数据.xlsx";
    	this.aParseUtilsService.parseRQZWDescdis(path);
    	return null;
    }
    
    /**
     * 数据解析 - 解析入侵植物分类单元多媒体信息1
     * （创建物种的多媒体实体，oldPath记录了图片或视频的本地存储路径）
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseRQZWMultimedia")
    public JSON parseRQZWMultimedia() throws Exception {
    	String path = "E:/入侵植物-最终合并数据/数据/10-多媒体.xlsx";
    	this.aParseUtilsService.parseRQZWMultimedia(path);
    	return null;
    }
    
    /**
     * 数据解析 - 解析入侵植物分类单元多媒体信息2
     * （已经创建物种的多媒体实体，oldPath已经记录了图片或视频的本地存储路径，需要构建文件服务器的存储路径）
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadRQZWImg")
   	public JSON uploadRQZWImg(HttpServletRequest request) throws Exception {
       	String path = "E:/入侵植物-最终合并数据/彩色照片";
       	this.aParseUtilsService.uploadRQZWImg(request, path, uploadPath + "images/");
   		return null;
   	}
    
    /**
     * 数据解析 - 解析入侵植物分类单元多媒体信息3 oldPath、Path字段都有值（一步到位）
     * 入侵植物入侵分布图片
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadRQZWDiscImg")
   	public JSON uploadRQZWDiscImg(HttpServletRequest request) throws Exception {
       	String path = "E:/入侵植物-最终合并数据/入侵植物预警图层图片";
       	this.aParseUtilsService.uploadRQZWDiscImg(request, path, uploadPath + "images/");
   		return null;
   	}
    

    
    /**
     * 数据处理 - 处理入侵动物 - 昆虫名称列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/handleRQDW")
    public JSON handleRQDW() throws Exception {
    	String path = "E:/检验检疫物种列表/检验检疫物种-昆虫名称列表.xlsx";
    	this.aParseUtilsService.handleRQDW(path);
    	return null;
    }
    
    /**
     * 数据处理 - 处理入侵动物 - 昆虫名称列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/synchdataToLocal")
    public JSON synchdataToLocal() throws Exception {
    	String path = "E:/同步近期数据/检验检疫数据库Taxon.xlsx";
    	this.aParseUtilsService.synchdataToLocal(path);
    	return null;
    }
    
    /**
     * 数据处理 - 昆虫 - 训练 - 列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/trainAnimalData")
    public JSON trainAnimalData() throws Exception {
    	String path = "E:/训练数据/昆虫-训练-列表.xlsx";
    	this.aParseUtilsService.trainAnimalData(path);
    	return null;
    }
    
    /**
     * 数据处理 - 去首尾空格
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/taxKeyTrim")
    public JSON taxKeyTrim() throws Exception {
    	String path = "E:/检索表/旧采集系统-检索表.xlsx";
    	this.aParseUtilsService.taxKeyTrim(path);
    	return null;
    }
    /**
     * 数据处理 - 去首尾空格
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/compareDataTrim")
    public JSON compareDataTrim() throws Exception {
    	String path = "E:/检索表/有害物种.xlsx";
    	this.aParseUtilsService.compareDataTrim(path);
    	return null;
    }
    /**
     * 数据处理 - 去首尾空格
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/search")
    public JSON search() throws Exception {
    	String path = "E:/检索表/有害物种.xlsx";
    	this.aParseUtilsService.search(path);
    	return null;
    }
    /**
     * 数据处理 - 检验检疫拉丁名更正
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/updatePisLatin")
    public JSON updatePisLatin() throws Exception {
    	String path = "E:/检验检疫物种列表/检验检疫物种.xlsx";
    	this.aParseUtilsService.updatePisLatin(path);
    	return null;
    }
   
  
    /**
     * 解析动物志7.8，合并描述，新建分布
     * @return
     * @throws Exception
     */
    @RequestMapping("/mergeDesc")
    public JSON mergeDesc() throws Exception {
    	return this.aParseUtilsService.mergeDesc();
    }
}

