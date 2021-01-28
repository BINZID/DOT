package org.big.controller;

import javax.servlet.http.HttpServletResponse;

import org.big.service.TraitdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *<p><b>Traitdata相关的Controller类</b></p>
 *<p> Traitdata相关的Controller</p>
 * @author BINZI
 *<p>Created date: 2018/06/21 16:52</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/traitdata")
public class TraitdataController {
	@Autowired
	private TraitdataService traitdataService;
	/**
     *<b> Traitdata的列表页面</b>
     *<p> Traitdata的列表页面</p>
     * @author BINZI
     * @return java.lang.String
     */
	@GetMapping(value = "")
	public String toIndex() {
		return "traitdata/index";
	}
	
	/**
	 * <b>导出当前登录用户的traitdata数据</b>
	 * <p>导出当前登录用户的traitdata数据</p>
	 * @author BINZI
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
    	this.traitdataService.export(response);
	}

}
