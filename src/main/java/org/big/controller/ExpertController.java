package org.big.controller;

import javax.servlet.http.HttpServletResponse;

import org.big.service.ExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *<p><b>Expert的Controller类</b></p>
 *<p> Expert的Controller</p>
 * @author BINZI
 *<p>Created date: 2018/10/15 15:22</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/expert")
public class ExpertController {
	@Autowired
	private ExpertService expertService;
	/**
     *<b>Expert管理页</b>
     *<p> 包含所有Expert的信息列表、操作选项</p>
     * @author BINZI
     * @return java.lang.String
     */
	@RequestMapping(value = "", method = { RequestMethod.GET })
	public String index() {
		return "expert/index";
	}
    
	/**
	 * <b>导出当前登录用户的数据源数据</b>
	 * <p>导出当前登录用户的数据源数据</p>
	 * @author BINZI
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
		this.expertService.export(response);
	}
}