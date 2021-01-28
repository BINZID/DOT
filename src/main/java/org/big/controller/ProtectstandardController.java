package org.big.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *<p><b>Protectstandard相关的Controller类</b></p>
 *<p> Protectstandard相关的Controller</p>
 * @author BINZI
 *<p>Created date: 2018/06/14 15:25</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/protectstandard")
public class ProtectstandardController {
	/**
     *<b> Protectstandard的列表页面</b>
     *<p> Protectstandard的列表页面</p>
     * @author BINZI
     * @return java.lang.String
     */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String toIndex() {
		return "protectstandard/index";
	}
}
