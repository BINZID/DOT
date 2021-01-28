package org.big.controller.rest;

import org.big.service.AParseUtilsService;
import org.big.service.AmphibianAndReptiliaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
/**
 * 解析江建平 -- 两栖、爬行
 * @author BIGIOZ
 *
 */
@RestController
@Controller
@RequestMapping("/console/parse/rest")
public class AmphibianAndReptiliaRestController {

	@Autowired
    private AmphibianAndReptiliaService aars;
	@Autowired
	private AParseUtilsService aParseUtilsService;
	/**
     * 解析两栖爬行Taxon数据
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseARTaxon")
	public JSON parseARTaxon() throws Exception {
    	String path = "";
    	this.aars.parseARTaxon(path);
		return null;
	}
	/**
     * 解析两栖爬行Description数据
     * @return
     */
    @RequestMapping("/parseARDescription")
	public JSON parseARDescription() throws Exception {
    	String path = "";
    	this.aars.parseARDescription(path);
		return null;
	}
    
	/**
     * 解析入侵植物物种分布 2018
     * @return
     * @throws Exception
     */
    @RequestMapping("/parseARDist")
	public JSON parseARDist() throws Exception {
    	String path = "";
    	this.aars.parseARDist(path);
    	this.aParseUtilsService.parseRQZWDist(path);
		return null;
	}
 
}

