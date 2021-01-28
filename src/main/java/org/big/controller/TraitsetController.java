package org.big.controller;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.big.entity.Traitset;
import org.big.service.TraitsetService;
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
 *<p><b>Traitset相关的Controller类</b></p>
 *<p> Traitset相关的Controller</p>
 * @author BINZI
 *<p>Created date: 2018/06/22 17:21</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/traitset")
public class TraitsetController {
	@Autowired
	private TraitsetService traitsetService;
	/**
     *<b> Traitset的列表页面</b>
     *<p> Traitset的列表页面</p>
     * @author BINZI
     * @return java.lang.String
     */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String toIndex() {
		return "traitset/index";
	}
	
	/**
     *<b>添加Traitset</b>
     *<p> 添加新的Traitset的编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) {
		Traitset thisTraitset = new Traitset();
		model.addAttribute("thisTraitset", thisTraitset);
		return "traitset/add";
	}
	
	/**
     *<b>弹出层添加Traitset</b>
     *<p> 弹出层添加新的Traitset的编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/addNew", method = RequestMethod.GET)
	public String addNew(Model model) {
		Traitset thisTraitset = new Traitset();
		model.addAttribute("thisTraitset", thisTraitset);
		return "traitset/addModal";
	}
	
	/**
     *<b>修改Traitset</b>
     *<p> 修改Traitset的编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable String id) {
		Traitset thisTraitset = this.traitsetService.findOneById(id);
		model.addAttribute("thisTraitset", thisTraitset);
		return "traitset/edit";
	}
	
	/**
     *<b>保存Traitset实体</b>
     *<p> 保存Traitset实体</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("thisTraitset") @Valid Traitset thisTraitset, BindingResult result, Model model, HttpServletRequest request) {
		if (null == thisTraitset.getId() || "".equals(thisTraitset.getId())) {	// --> 新增
			if (result.hasErrors()) {
				isError(thisTraitset, result, model);
				return "traitset/add";
			}else {
				thisTraitset.setInputtime(new Timestamp(System.currentTimeMillis()));
				this.traitsetService.saveOne(thisTraitset);
				return "redirect:/console/traitset";
			}
		}else{																	// --> 修改
			if (result.hasErrors()) {
				isError(thisTraitset, result, model);
				return "traitset/edit";
			}else {
				this.traitsetService.updateOneById(thisTraitset);
				return "redirect:/console/traitset";
			}
		}
	}
	
	/** 
	 * <b> 新增|修改 重复代码提取</b>
	 * <p> 新增|修改 重复代码提取</p>
	 * @param thisTaxaset
	 * @param result
	 * @param model
	 */
	private void isError(Traitset thisTraitset, BindingResult result, Model model) {
		List<ObjectError> list = result.getAllErrors();
		String errorMsg = "";
		for (ObjectError error : list) {
			errorMsg = errorMsg + error.getDefaultMessage() + ";";
		}
		model.addAttribute("thisTraitset", thisTraitset);
		model.addAttribute("errorMsg", errorMsg);
	}
	
}
