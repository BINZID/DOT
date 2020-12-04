package org.big.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *<p><b>Index相关的Controller的Rest风格类</b></p>
 *<p> Index相关的Controller的Rest风格类</p>
 * @author BIN
 *<p>Created date: 2020/12/03 09:30</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController
@RequestMapping({"/console/index/rest"})
public class IndexRestController {
	/**
	 * <p><b>API请求采集系统用户下的团队数据</b></p>
	 * <p>API请求采集系统用户下的团队数据</p>
	 * @author BINZI 
	 * @param id
	 * @param findText
	 * @param request
	 * @return
	 */
	@GetMapping("/")
	public String index() {
		return "success";
	}
}
