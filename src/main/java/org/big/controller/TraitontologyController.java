package org.big.controller;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.big.entity.Datasource;
import org.big.entity.Traitontology;
import org.big.service.TraitontologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *<p><b>Traitontology相关的Controller类</b></p>
 *<p> Traitontology相关的Controller</p>
 * @author BINZI
*<p>Created date: 2018/06/22 17:21</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/traitontology")
public class TraitontologyController {
	@Autowired
	private TraitontologyService traitontologyService;
	/**
     *<b>Traitontology的列表页面</b>
     *<p> Traitontology的列表页面</p>
     * @author BINZI
     * @return java.lang.String
     */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String toIndex() {
		return "traitontology/index";
	}
	/**
     *<b>添加Traitontology</b>
     *<p> 添加新的Traitontology的编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) {
		Traitontology thisTraitontology = new Traitontology();
		thisTraitontology.setInputtime(new Timestamp(System.currentTimeMillis()));
		model.addAttribute("thisTraitontology", thisTraitontology);
		return "traitontology/add";
	}
	
	/**
     *<b>修改Traitontology</b>
     *<p> 修改Traitontology的编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable String id) {
		Traitontology thisTraitontology = new Traitontology();
		model.addAttribute("thisTraitontology", thisTraitontology);
		return "traitontology/edit";
	}
	
	/**
     *<b>保存Traitontology实体</b>
     *<p> 保存Traitontology实体</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("thisTraitontology") @Valid Traitontology thisTraitontology, BindingResult result, Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			String errorMsg = "";
			for (ObjectError error : list) {
				errorMsg = errorMsg + error.getDefaultMessage() + ";";
			}
			model.addAttribute("thisTraitontology", thisTraitontology);
			model.addAttribute("errorMsg", errorMsg);
			return "traitontology/add";
		}else {
			this.traitontologyService.saveTraitontology(thisTraitontology);
			return "redirect:/console/traitontology";
		}
	
	}
	
	/**
     *<b>Traitontology的添加页面Select下拉选添加Datasource</b>
     *<p> 添加新的Datasource的编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/addDatasource", method = RequestMethod.GET)
	public String addDatasource(Model model, HttpServletRequest request) {
		Datasource thisDatasource = new Datasource();
        model.addAttribute("thisDatasource", thisDatasource);
		return "traitontology/addDatasourcesModal";
	}
}
