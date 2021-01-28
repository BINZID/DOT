package org.big.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *<p><b>Molecular相关的Controller类</b></p>
 *<p> Molecular相关的Controller</p>
 * @author BINZI
 *<p>Created date: 2018/07/12 15:32</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/molecular")
public class MolecularController {
	/**
     *<b>Molecular的列表页面</b>
     *<p> Molecular的列表页面</p>
     * @author BINZI
     * @return java.lang.String
     */
	@GetMapping(value = "")
	public String toIndex() {
		return "molecular/index";
	}
	
}
