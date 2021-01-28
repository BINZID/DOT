package org.big.controller.rest;

import org.big.service.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

@RestController  //返回json
@Controller
@RequestMapping("/console/amphbia/rest")
public class AAmphbiaUtilsRestController {
	@Autowired
	private TaxonService taxonService;
	/**
	 * 两栖动物 - 物种
	 */
	@GetMapping("/handleAmphbiaTaxon")
	private void handleAmphbiaTaxon() {
		String inputer = "4a4327184629404ba0566e2236d495da";
		this.taxonService.handleAmphbiaTaxon(inputer);
	}
	/**
	 * 两栖动物 - 引证
	 */
	@GetMapping("/handleAmphbiaCitation")
	private void handleAmphbiaCitation() {
		String inputer = "4a4327184629404ba0566e2236d495da";
		this.taxonService.handleAmphbiaCitation(inputer);
	}
	
	/**
	 * 两栖动物 - 分布
	 */
	@GetMapping("/formatAmphbiaDis")
	private void formatAmphbiaDis() {
		String inputer = "4a4327184629404ba0566e2236d495da";
		this.taxonService.formatAmphbiaDis(inputer);
	}

	/**
	 * 两栖动物 - 俗名
	 * @throws Exception
	 */
	@GetMapping("/parseAmphbiaCommonname")
	private void parseAmphbiaCommonname() throws Exception {
		String path = "E:/两栖动物/两栖动物俗名.xlsx";
    	this.taxonService.parseAmphbiaCommonname(path);
	}
	
	/**
	 * 两栖动物 - 物种文献匹配
	 * @throws Exception
	 */
	@GetMapping("/parseAmphbiaRefs")
	private void parseAmphbiaRefs() {
		String inputer = "4a4327184629404ba0566e2236d495da";
    	this.taxonService.parseAmphbiaRefs(inputer);
	}
	
    /**
     * 两栖动物 - 引证文献匹配
     * @return
     * @throws Exception
     */
    @RequestMapping("/citationMatchRefOfAmphbia")
    public void citationMatchRefOfAmphbia() throws Exception {
    	String sourcesId = "61a8d179-00c9-4a9c-9b06-561992b1d89c";
    	this.taxonService.citationMatchRefOfAmphbia(sourcesId);
    }
}
