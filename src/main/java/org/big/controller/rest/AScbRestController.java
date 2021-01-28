package org.big.controller.rest;

import org.big.service.AScbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

/**
 *<p><b>Description相关的Controller的Rest风格类</b></p>
 *<p> Description相关的Controller的Rest风格类</p>
 * @author BINZI
 *<p>Created date: 2018/06/13 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/console/scb/rest")
public class AScbRestController {
	@Autowired
	private AScbService aScbService;
	/**
     * confirmSCB数据
     * @return
     * @throws Exception
     */
    @RequestMapping("/confirmSCB")
	public JSON confirmSCB() throws Exception {
    	String path = "E:/检疫检验统计2003-2019/检疫检验统计2003-2019-1.xlsx";
		return this.aScbService.confirmSCB(path);
	}
    /**
     * 山东口岸进境植物及其产品关注有害生物名录-最新1
     * @return
     * @throws Exception
     */
    @RequestMapping("/shandongPis")
	public JSON shandongPis() throws Exception {
    	String path = "E:/检疫检验统计2003-2019/山东口岸进境植物及其产品关注有害生物名录-最新1.xlsx";
		return this.aScbService.shandongPis(path);
	}
    /**
     * 山东口岸进境植物及其产品关注有害生物名录
	 * 拆国家、货物
	 * 拆拉丁名、命名信息
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseShandongPis")
    public JSON parseShandongPis() throws Exception {
    	String path = "E:/检疫检验统计2003-2019/山东口岸进境植物及其产品关注有害生物名录-最新1.xlsx";
    	return this.aScbService.parseShandongPis(path);
    }
    
    /**
     * 中华人民共和国进境植物检疫性有害生物名录-441种
	 * wordTransformExcel
     * @return
     * @throws Exception
     */
    @RequestMapping("/wordTransformExcel")
    public JSON wordTransformExcel() throws Exception {
    	String path = "E:/检疫检验统计2003-2019/中华人民共和国进境植物检疫性有害生物名录-441种.xlsx";
    	return this.aScbService.wordTransformExcel(path);
    }
    
    /**
     * 中华人民共和国进境植物检疫性有害生物名录-441种
	 * 匹配检疫物种
     * @return
     * @throws Exception
     */
    @RequestMapping("/matchPis")
    public JSON matchPis() throws Exception {
    	String path1 = "E:/检验检疫物种列表/中华人民共和国进境植物检疫性有害生物名录-439种.xlsx";
    	String path2 = "E:/检疫检验统计2003-2019/检疫检验统计2003-2019-按类别、重要性分16表.xlsx";
    	return this.aScbService.matchPis(path1, path2);
    }
    
    /**
     * 山东口岸进境植物及其产品关注有害生物名录 - 拉丁名检查
     * @return
     * @throws Exception
     */
    @RequestMapping("/checkPis")
    public JSON checkPis() throws Exception {
    	return this.aScbService.checkPis();
    }
    
    /**
	 * getData2003-2019
     * 
     */
    @RequestMapping("/getFinalData")
    public JSON getFinalData() throws Exception {
    	String path1 = "E:/检验检疫物种列表/中华人民共和国进境植物检疫性有害生物名录-439种.xlsx";
    	String path2 = "E:/检疫检验统计2003-2019/检疫检验统计2003-2019-按类别、重要性分16表.xlsx";
    	return this.aScbService.getFinalData(path1, path2);
	}
    
    @RequestMapping("/trimData")
    public JSON trimData() throws Exception {
    	String path = "E:/检验检疫物种列表/data.xlsx";
    	return this.aScbService.trimData(path);
	}
    
    @RequestMapping("/getData")
    public JSON getData() throws Exception {
    	return this.aScbService.getData();
	}
    /**
     * 检验检疫2003-2019 拉丁名、中文名确认
     * @return
     * @throws Exception
     */
    @RequestMapping("/rename")
    public JSON rename() throws Exception {
    	String path1 = "E:/检验检疫物种列表/中华人民共和国进境植物检疫性有害生物名录-439种.xlsx";
    	String path2 = "E:/检疫检验统计2003-2019/检疫检验统计2003-2019-按类别、重要性分16表.xlsx";
    	return this.aScbService.rename(path1, path2);
	}
    
    /**
     * 检验检疫2003-2019 确认分类等级
     * @return
     * @throws Exception
     */
    @RequestMapping("/matchRank")
    public JSON matchRank() throws Exception {
    	String path = "E:/检疫检验统计2003-2019/20200114工作簿2.xlsx";
    	return this.aScbService.matchRank(path);
	}
    
    /**
     * 检验检疫2003-2019 确认分类等级 && 山东口岸进境植物及其产品关注有害生物名录
     * @return
     * @throws Exception
     */
    @RequestMapping("/mergeData")
    public JSON mergeData() throws Exception {
    	String path = "E:/检验检疫物种列表/工作簿6.xlsx";
    	return this.aScbService.mergeData(path);
	}
    /**
     * Customs 海关数据
     * @return
     * @throws Exception
     */
    @RequestMapping("/getCustoms")
    public JSON getCustoms() throws Exception {
    	String path = "E:/海关代码.xlsx";
    	return this.aScbService.getCustoms(path);
    }
}
