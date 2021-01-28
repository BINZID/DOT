package org.big.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *<p><b>Multimedia相关的Controller类</b></p>
 *<p> Multimedia相关的Controller</p>
 * @author BINZI
 *<p>Created date: 2018/07/13 11:34</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/multimedia")
public class MultimediaController {
	/**
     *<b>Multimedia的列表页面</b>
     *<p> Multimedia的列表页面</p>
     * @author BINZI
     * @return java.lang.String
     */
	@GetMapping(value = "")
	public String toIndex() {
		return "multimedia/index";
	}

	/**
	 *<b>地图选点</b>
	 *<p> 地图选点</p>
	 * @author 王天山
	 * @return java.lang.String
	 */
	@GetMapping(value = "/map/{id}")
	public String map(Model model, @PathVariable String id) {
		model.addAttribute("forType", "multimedia");
		model.addAttribute("occurrenceId", id);
		return "taxon/map";
	}
	
}
