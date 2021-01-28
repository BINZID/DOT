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
 * 解析入侵动物数据
 * @author BIGIOZ
 *
 */
@RestController
@Controller
@RequestMapping("/console/animal/rest")
public class AInvasiveAnimalRestController {

	@Autowired
    private AParseUtilsService aParseUtilsService;
	private String uploadPath = "upload/";
	
	/**
	 * 入侵数据合并（动物、植物）
	 * 以2018年数据为主，将其他年份的物种相关的描述、俗名、分布数据关联到2018
	 */
    @RequestMapping("/mergeRuqinData")
	private String mergeRuqinData() {
    	/*TaxasetId
    	196621bf74b648839f24f4fd5cfbdeda	入侵动物 2018		E:\入侵物种年度数据\入侵动物\2018\images
    	67a17cd2b2c54af3a366c51c73e0f03a	入侵动物 2019		E:\入侵物种年度数据\入侵动物\2019\img-small\images

    	4ce0685f14a043d784926ca22ee25424	入侵植物 2018		E:\入侵物种年度数据\入侵植物\2018\彩色照片
    	d6569c3b9f1c4e59920136d64bc014c5	入侵植物 2019		E:\入侵物种年度数据\入侵植物\2019\第四子课题入侵植物专题\先导专项——150种外来入侵
*/    	
    	// String[] taxasetsA = {"196621bf74b648839f24f4fd5cfbdeda", "67a17cd2b2c54af3a366c51c73e0f03a"}; // 入侵植物 - 2018 || 2019
    	String[] taxasetsP = {"4ce0685f14a043d784926ca22ee25424", "d6569c3b9f1c4e59920136d64bc014c5"}; // 入侵植物 - 2018 || 2019
    	this.aParseUtilsService.mergeRuqinData(taxasetsP);
    	return null;
	}
	
	
	
	
	/**
     * 解析入侵动物参考文献
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseRQDWRef")
	public JSON parseRQDWRef() throws Exception {
    	String path = "E:/入侵动物/5-2-4-1参考文献.xlsx";
    	this.aParseUtilsService.parseRQDWRef(path);
		return null;
	}
	/**
     * 解析入侵动物分类单元 2018 || 2019
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseRQDWTaxon")
	public JSON parseRQDWTaxon() throws Exception {
    	//String path = "E:/入侵动物/5-2-4-1分类单元信息.xlsx";
    	String path = "E:/入侵物种年度数据/入侵动物/2019/5-2-4-1分类单元信息（2019）.xlsx";
    	this.aParseUtilsService.parseRQDWTaxon(path);
		return null;
	}
    
	/**
     * 解析入侵植物物种分布 2018
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseRQZWDist")
	public JSON parseRQZWDist() throws Exception {
    	//String path = "E:/入侵动物/5-2-4-1分类单元信息.xlsx";
    	String path = "E:/入侵物种年度数据/入侵植物/2018/数据/3-分类单元信息.xlsx";
    	this.aParseUtilsService.parseRQZWDist(path);
		return null;
	}
    
    /**
     * 解析入侵动物分类单元
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseRQDWMedia")
	public JSON parseRQDWMedia() throws Exception {
    	//String path = "E:/入侵动物/5-2-4-1-多媒体.xlsx";
    	String path = "E:/入侵物种年度数据/入侵动物/2019/5-2-4-1-多媒体（2019）.xlsx";
    	this.aParseUtilsService.parseRQDWMedia(path);
		return null;
	}
    /**
     * 入侵动物图片、视频
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadRQDWImg")
   	public JSON uploadRQDWImg(HttpServletRequest request) throws Exception {
       	//String path = "E:/入侵动物/";
       	String path = "E:/入侵物种年度数据/入侵动物/2019/img-small/";
       	this.aParseUtilsService.uploadRQDWImg(request, path, uploadPath + "images/");
   		return null;
   	}
    
    /**
     * 解析入侵动物其他信息Excel	2018 || 2019
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseRQDWInfo")
   	public JSON parseRQDWInfo() throws Exception {
       	//String path = "E:/入侵动物/2018年名录";
       	String path = "E:/入侵物种年度数据/入侵动物/2019/物种名录";
       	this.aParseUtilsService.parseRQDWInfo(path);
   		return null;
   	}
    
    /**
     * 解析入侵动物其他信息Excel	2018 || 2019
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseRQDWRefInfo")
   	public JSON parseRQDWRefInfo() throws Exception {
       	this.aParseUtilsService.parseRQDWRefInfo();
   		return null;
   	}
    
    /**
     * 补充入侵动物描述数据的参考文献
     * @return
     */
    @RequestMapping("/getRQDWRefOfDesc")
    public JSON getRQDWRefOfDesc() {
    	this.aParseUtilsService.getRQDWRefOfDesc();
    	return null;
	}
    /**
     * 补充入侵动物描述数据的分布数据
     * @return
     */
    @RequestMapping("/getRQDWDisOfDesc")
    public JSON getRQDWDisOfDesc() {
    	this.aParseUtilsService.getRQDWDisOfDesc();
    	return null;
	}
    /**
     * 修改入侵动物的参考文献
     * @return
     * @throws IOException
     */
    @RequestMapping("/updateRef")
    public JSON updateRef() throws IOException {
    	String path = "E:/入侵动物/5-2-4-1参考文献.xlsx";
    	this.aParseUtilsService.updateRef(path);
		return null;
	}
    /**
     * 入侵动物俗名的拼音
     * @return
     * @throws DocumentException
     */
    @RequestMapping("/commonnameOfPY")
    public JSON commonnameOfPY() throws DocumentException  {
    	this.aParseUtilsService.commonnameOfPY();
		return null;
	}
    
	/**
     * 解析入侵动物分类单元
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseRQZWTaxon")
	public JSON parseRQZWTaxon() throws Exception {
    	String path = "E:/入侵植物-最终合并数据/数据/3-分类单元信息1.xlsx";
    	this.aParseUtilsService.parseRQZWTaxon(path);
		return null;
	}
    /**
     * 解析入侵植物原始数据生成UUID
     * @return
     * @throws Exception
     */
    @RequestMapping("/setRQUUID")
    public JSON setRQUUID() throws Exception {
    	this.aParseUtilsService.setRQUUID();
    	return null;
    }
    /**
     * 解析入侵植物原始数据 -- 物种数据
     * @return
     * @throws Exception
     */
    @RequestMapping("/getTaxonDataFormRQZW")
    public JSON getTaxonData() throws Exception {
    	this.aParseUtilsService.getTaxonData();
    	return null;
    }
    
    /**
     * 解析入侵动物多媒体数据 2019
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
     * 解析入侵植物分类单元2
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
     * 解析入侵植物分类单元命名信息
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
     * 解析入侵植物分类单元引证信息
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
     * 解析入侵植物分类单元多媒体信息
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseRQZWMultimedia")
    public JSON parseRQZWMultimedia() throws Exception {
    	String path = "E:/入侵植物-最终合并数据/数据/10-多媒体.xlsx";
    	this.aParseUtilsService.parseRQZWMultimedia(path);
    	return null;
    }
    
    @RequestMapping("/parseRQZWDescdata")
    public JSON parseRQZWDescdata() throws Exception {
    	String path = "E:/入侵植物-最终合并数据/数据/7-分布数据.xlsx";
    	this.aParseUtilsService.parseRQZWDescdata(path);
    	return null;
    }
    /**
     * 入侵植物多媒体图片
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
     * 入侵植物入侵分布数据
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
     * 处理入侵动物 - 昆虫名称列表
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
     * 处理入侵动物 - 昆虫名称列表
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
     * 昆虫 - 训练 - 列表
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
     * 去首尾空格
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
     * 去首尾空格
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
     * 去首尾空格
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
     * 检验检疫拉丁名更正
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
     * YD命名信息更正
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/handleYD")
    public JSON handleYD() throws Exception {
    	String path = "E:/双翅目蝇类名录/双翅目蝇类名录.xlsx";
    	this.aParseUtilsService.handleYD(path);
    	return null;
    }
    /**
     * YD统计引证
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/countCitation")
    public JSON countCitation() {
    	return this.aParseUtilsService.countCitation();
    }
    
    /**
     * YD统计引证
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/handleCitation")
    public JSON handleCitation() {
    	return this.aParseUtilsService.handleCitation();
    }
    /**
     * YD统计引证
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/citationMatchRef")
    public JSON citationMatchRef() {
    	return this.aParseUtilsService.citationMatchRef();
    }
    
    @RequestMapping("/contionsMatchRef")
    public JSON contionsMatchRef() throws Exception {
    	return this.aParseUtilsService.contionsMatchRef("E:/双翅目蝇类名录/citation.xlsx");
    }
    // --------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------
    /**
     * 解析模板文献
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseExcelRef")
    public JSON parseExcelRef() throws Exception {
    	return this.aParseUtilsService.parseExcelRef("E:/2019数据/5-2-1-姚一建/2019菌物数据库专题数据汇交/2-参考文献-菌物-2019.xlsx");
    }
    /**
     * 解析模板分类单元 - 为此次解析存储的数据做标记
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseExcelTaxon")
    public JSON parseExcelTaxon() throws Exception {
    	return this.aParseUtilsService.parseExcelTaxon("E:/2019数据/5-2-1-姚一建/2019菌物数据库专题数据汇交/3-分类单元信息-菌物-2019.xlsx");
    }
    /**
     * 解析模板引证 - 通过标记关联此次解析模板分类单元
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseExcelCitation")
    public JSON parseExcelCitation() throws Exception {
    	return this.aParseUtilsService.parseExcelCitation("E:/2019数据/5-2-1-姚一建/2019菌物数据库专题数据汇交/4-引证信息-菌物-2019.xlsx");
    }
    /**
     * 解析模板描述 - 通过标记关联此次解析模板分类单元
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseExcelDescription")
    public JSON parseExcelDescription() throws Exception {
    	return this.aParseUtilsService.parseExcelDescription("E:/2019数据/5-2-1-姚一建/2019菌物数据库专题数据汇交/5-描述信息-菌物-2019.xlsx");
    }
    /**
     * 解析模板俗名 - 通过标记关联此次解析模板分类单元
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseExcelCommonname")
    public JSON parseExcelCommonname() throws Exception {
    	return this.aParseUtilsService.parseExcelCommonname("E:/2019数据/5-2-1-姚一建/2019菌物数据库专题数据汇交/6-俗名信息-菌物-2019.xlsx");
    }
    /**
     * 解析模板分布 - 通过标记关联此次解析模板分类单元
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseExcelDistributiondate")
    public JSON parseExcelDistributiondate() throws Exception {
    	return this.aParseUtilsService.parseExcelDistributiondate("E:/2019数据/5-2-1-姚一建/2019菌物数据库专题数据汇交/7-分布数据-菌物-2019.xlsx");
    }
    /**
     * 解析模板特征 - 通过标记关联此次解析模板分类单元
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseExcelTraitdata")
    public JSON parseExcelTraitdata() throws Exception {
    	return this.aParseUtilsService.parseExcelTraitdata("E:/2019数据/5-2-1-姚一建/2019菌物数据库专题数据汇交/8-特征数据-菌物-2019.xlsx");
    }
    /**
     * 解析模板多媒体 - 通过标记关联此次解析模板分类单元
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseExcelMultimedia")
    public JSON parseExcelMultimedia() throws Exception {
    	return this.aParseUtilsService.parseExcelMultimedia("E:/2019数据/5-2-1-姚一建/2019菌物数据库专题数据汇交/10-多媒体-菌物-2019.xlsx");
    }
    
    /**
     * %杨定数据文献匹配终版%
     * @return
     * @throws Exception
     */
    @RequestMapping("/citationMatchRefYD")
    public JSON citationMatchRefYD() throws Exception {
    	return this.aParseUtilsService.citationMatchRefYD();
    }
    /**
     * 文献页码
     * @return
     * @throws Exception
     */
    @RequestMapping("/citationMatchRefYDPageNum")
    public JSON citationMatchRefYDPageNum() throws Exception {
    	return this.aParseUtilsService.citationMatchRefYDPageNum();
    }
    @RequestMapping("/parseExcelCitationOfRef")
    public JSON parseExcelCitationOfRef() throws Exception {
    	return this.aParseUtilsService.parseExcelCitationOfRef("E:/citationref.xlsx");
    }
    @RequestMapping("/matchCitationOfRefIsNull")
    public JSON matchCitationOfRefIsNull() throws Exception {
    	return this.aParseUtilsService.matchCitationOfRefIsNull();
    }
    @RequestMapping("/unParseExcelCitationOfRef")
    public JSON unParseExcelCitationOfRef() throws Exception {
    	return this.aParseUtilsService.unParseExcelCitationOfRef("E:/citationref.xlsx");
    }
    
    @RequestMapping("/reUnParseExcelCitationOfRef")
    public JSON reUnParseExcelCitationOfRef() throws Exception {
    	return this.aParseUtilsService.reUnParseExcelCitationOfRef();
    }
    
    /**
     * %国家重点保护野生植物 - 第n批%
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseProtect")
    public JSON protect() throws Exception {
    	return this.aParseUtilsService.parseProtect("E:/数据整理/国家重点保护野生植物（第二批）物种库.xlsx");
    }
    
    /**
     * %年度行政区划补充pid - 第n批%
     * @return
     * @throws Exception
     */
    @RequestMapping("/addDistributionPid")
    public JSON addPid() throws Exception {
    	return this.aParseUtilsService.addDistributionPid("E:/中国行政区划/行政区划版本.xlsx");
    }
    
    /**
     * %年度行政区划差异 - 第n批%
     * @return
     * @throws Exception
     */
    @RequestMapping("/compareTo2020")
    public JSON compareTo2020() throws Exception {
    	return this.aParseUtilsService.compareTo2020();
    }
    
    /**
     * %年度行政区划补充ppid - 第n批%
     * @return
     * @throws Exception
     */
    @RequestMapping("/addPpid")
    public JSON addPpid() throws Exception {
    	return this.aParseUtilsService.addPpid();
    }
    
    /**
     * %年度行政区划匹配 - 第n批%
     * @return
     * @throws Exception
     */
    @RequestMapping("/match")
    public JSON match() throws Exception {
    	return this.aParseUtilsService.match();
    }
    
    /**
     * %年度行政区划去重 - 第n批%
     * @return
     * @throws Exception
     */
    @RequestMapping("/distinct")
    public JSON distinct() throws Exception {
    	return this.aParseUtilsService.distinct("E:/中国行政区划/行政区划年度对比.xlsx");
    }
    
    
    /**
     * %中国哺乳动物分布 - 物种拉丁名\中文名\省份\市县
     * @return
     * @throws Exception
     */
    @RequestMapping("/getDistributData")
    public JSON getDistributData() throws Exception {
    	return this.aParseUtilsService.getDistributData();
    }
    
    /**
     * %中国哺乳动物分布 - 物种拉丁名\中文名\省份\市县　-- 补充省份Adcode
     * @return
     * @throws Exception
     */
    @RequestMapping("/addProvinceAdcode")
    public JSON addProvinceAdcode() throws Exception {
    	return this.aParseUtilsService.addProvinceAdcode("E:/中国行政区划/中国哺乳动物分布.xlsx");
    }
    
    /**
     * %中国哺乳动物分布 - 物种拉丁名\中文名\省份\市县　-- 补充省份Adcode
     * @return
     * @throws Exception
     */
    @RequestMapping("/getLevel")
    public JSON getLevel() throws Exception {
    	return this.aParseUtilsService.getLevel("E:/中国行政区划/中国哺乳动物分布.xlsx");
    }
    
    /**
     * %中国哺乳动物分布 - 物种拉丁名\中文名\省份\市县　-- 补充省份Adcode
     * @return
     * @throws Exception
     */
    @RequestMapping("/getLevelPlus")
    public JSON getLevelPlus() throws Exception {
    	return this.aParseUtilsService.getLevelPlus("E:/中国行政区划/中国哺乳动物分布.xlsx");
    }
    
    
    /**
     * %中国哺乳动物分布 - 物种拉丁名\中文名\省份\市县　-- 补充县级的市
     * @return
     * @throws Exception
     */
    @RequestMapping("/addCountyOfCity")
    public JSON addCountyOfCity() throws Exception {
    	return this.aParseUtilsService.addCountyOfCity("E:/中国行政区划/中国哺乳动物分布.xlsx");
    }
    
    /**
     * %中国哺乳动物分布 - 物种拉丁名\中文名\省份\市县　-- 匹配文献
     * @return
     * @throws Exception
     */
    @RequestMapping("/matchRef")
    public JSON matchAddessRef() throws Exception {
    	return this.aParseUtilsService.matchAddessRef("E:/中国行政区划/中国哺乳动物分布匹配ByLin2.xlsx");
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

