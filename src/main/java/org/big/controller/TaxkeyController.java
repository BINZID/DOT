package org.big.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *<p><b>Taxkey相关的Controller类</b></p>
 *<p> Taxkey相关的Controller</p>
 * @author BINZI
 *<p>Created date: 2018/06/20 15:29</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/taxkey")
public class TaxkeyController {
	/**
     *<b> Taxkey的列表页面</b>
     *<p> Taxkey的列表页面</p>
     * @author BINZI
     * @return java.lang.String
     */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String toIndex() {
		return "taxkey/index";
	}
	
	/**
     *<b>Taxkey特征图片管理页面</b>
     *<p> Taxkey特征图片管理页面</p>
     * @author BINZI
     * @return java.lang.String
     */
	@GetMapping(value = "/optionFeatureImg/{id}")
	public String optionFeatureImg(@PathVariable String id, HttpServletRequest request) {
		request.getSession().setAttribute("keyitemId", id);
		return "taxkey/optionFeatureImg";
	}
	
	/**
     *<b>Taxkey特征图片上传页面</b>
     *<p> Taxkey特征图片上传页面</p>
     * @author BINZI
     * @return java.lang.String
     */
	@GetMapping(value = "/uploadFeatureImg/{id}")
	public String uploadFeatureImg(@PathVariable String id, Model model) {
		model.addAttribute("keyitemId", id);
		return "taxkey/uploadFeatureImg";
	}
}
