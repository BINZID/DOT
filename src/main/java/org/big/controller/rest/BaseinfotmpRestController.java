package org.big.controller.rest;

import org.big.service.BaseinfotmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

/**
 *<p><b>Baseinfotmp相关的Controller的Rest风格类</b></p>
 *<p> Baseinfotmp相关的Controller的Rest风格类</p>
 * @author BINZI
 *<p>Created date: 2018/12/05 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/console/baseinfotmp/rest")
public class BaseinfotmpRestController {
	@Autowired
	private BaseinfotmpService baseinfotmpService;
	/**
     *<b> Baseinfotmp的列表页面</b>
     *<p> Baseinfotmp的列表页面</p>
     * @author BINZI
	 * @param taxasetId
	 * @return
     */
	@GetMapping(value = "/toTaxasetShow")
	public JSON showTaxaset() {
		return baseinfotmpService.clearBaseinfotmpData();
	}
	
}
