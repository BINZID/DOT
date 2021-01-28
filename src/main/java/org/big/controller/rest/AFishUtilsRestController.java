package org.big.controller.rest;

import org.big.service.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  //返回json
@Controller
@RequestMapping("/console/fish/rest")
public class AFishUtilsRestController {
	@Autowired
	private TaxonService taxonService;
	/**
	 * 脊椎动物 - 鱼类编码
	 */
	@GetMapping("/getFishData")
	private void getFishData() {
		String treeNodeId = "88c8f1519dab48b597537f0f2d0fc124";
		this.taxonService.getFishByTreeNodeId(treeNodeId );
	}
	/**
	 * 脊椎动物 - 中文
	 */
	@GetMapping("/getFishDatas")
	private void getFishDatas() {
		String taxasetId = "3de37e21e3cc41fc86880d7fa2d2e7e2";
		this.taxonService.getFishDatasByTaxasetIdAndRank(taxasetId);
	}
	
	/**
	 * 脊椎动物 - 中文
	 */
	@GetMapping("/getFishRemark")
	private void getFishRemark() {
		String taxasetId = "3de37e21e3cc41fc86880d7fa2d2e7e2";
		this.taxonService.getFishRemarkByTaxasetIdAndRank(taxasetId);
	}
	
	@GetMapping("/parseRedlistDatas")
	private void parseRedlistDatas() throws Exception {
		String path = "E:/中国生物多样性红色名录-数据整合.xlsx";
    	this.taxonService.parseRedlistDatas(path);
	}
	/**
	 * 脊椎动物编码
	 */
	@GetMapping("/getChordataCode")
	private void getChordataCode() throws Exception {
		String path = "E:/脊椎动物名录（爬行纲-两栖纲）-2020.xlsx";
		String reptilia = "RE00000000000000";
		String amphbia = "AM00000000000000";
    	this.taxonService.getChordataCode(path, reptilia, amphbia);
	}
	
	/**
	 * 脊椎动物2009-word转指定excel格式
	 */
	@GetMapping("/wordTransExcel")
	private void wordTransExcel() throws Exception {
		String path = "C:/Users/BigIOZ/Desktop/脊椎动物/2009脊椎动物/标准2009.xlsx";
    	this.taxonService.wordTransExcel(path);
	}
}
