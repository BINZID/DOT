package org.big.controller;

import javax.servlet.http.HttpServletResponse;

import org.big.entity.Datasource;
import org.big.service.DescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *<p><b>Description相关的Controller类</b></p>
 *<p> Description相关的Controller</p>
 * @author BINZI
 *<p>Created date: 2018/06/13 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/description")
public class DescriptionController {
	@Autowired
	private DescriptionService descriptionService;

	/**
     *<b> Description的列表页面</b>
     *<p> Description的列表页面</p>
     * @author BINZI
     * @return java.lang.String
     */
	@GetMapping(value = "")
	public String toIndex() {
		return "description/index";
	}
	
	/**
     *<b>Description的添加页面Select下拉选添加Datasource</b>
     *<p> 添加新的Datasource的编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/addDatasource/{sourcesId}/{sourcesForm}", method = RequestMethod.GET)
	public String addDatasource(Model model, @PathVariable String sourcesId, @PathVariable String sourcesForm) {
		Datasource thisDatasource = new Datasource();
        model.addAttribute("thisDatasource", thisDatasource);
        model.addAttribute("sourcesId", sourcesId);
        model.addAttribute("sourcesForm", sourcesForm);
		return "description/addDatasourcesModal";
	}
	
	/**
	 * <b>导出当前登录用户的Citation数据</b>
	 * <p>导出当前登录用户的Citation数据</p>
	 * @author BINZI
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
    	this.descriptionService.export(response);
	}
}
