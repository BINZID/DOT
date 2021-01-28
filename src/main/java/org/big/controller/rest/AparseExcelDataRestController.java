package org.big.controller.rest;

import org.big.service.AParseExcelDataService;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p><b>自动识别Controller的Rest风格类</b></p>
 * <p> 自动识别Controller的Rest风格类</p>
 *
 * @author 观察君（monitor@yunhoutech.com）
 * <p>Created date: 2020/01/01</p>
 * <p>Copyright: 云后科技 (北京) 有限公司[ http://www.yunhoutech.com ]</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping({"/console/excel/rest"})
public class AparseExcelDataRestController {
	@Autowired
	private AParseExcelDataService aParseExcelDataService;
	/**
	 * <p><b>解析Excel，导入category数据</b></p>
	 * <p> 解析Excel，导入category数据</p>
	 * @author Bin
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/parseCategoryData")
	public void parseCategoryData() throws Exception {
    	String path = "E:/category.xlsx";
    	this.aParseExcelDataService.parseCategoryData(path);
	}
    
    @RequestMapping("/getPY/{cname}")
   	public void getPY(@PathVariable String cname) throws Exception {
       	String py = this.aParseExcelDataService.getPY(cname);
       	System.out.println(cname + "：" + py);
   	}
    
    
	/**
	 * <p><b>检验检疫处理</b></p>
	 * <p> 检验检疫处理</p>
	 * @author Bin
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/parseProdbData")
	public void parseProdbData() throws Exception {
    	String path = "E:/quarantineprodb.xlsx";
    	this.aParseExcelDataService.parseProdbData(path);
	}
    
	/**
	 * <p><b>行政区分布地英文名</b></p>
	 * <p> 行政区分布地英文名</p>
	 * @author Bin
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/getEnnameOfDistribution")
	public void getEnnameOfDistribution() throws Exception {
    	String path = "E:/geoobject2.xlsx";
    	this.aParseExcelDataService.getEnnameOfDistribution(path);
	}
}