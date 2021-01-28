package org.big.controller;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.big.entity.Ref;
import org.big.service.RefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 *<p><b>Ref相关的Controller类</b></p>
 *<p> Ref相关的Controller</p>
 * @author BINZI
 *<p>Created date: 2018/06/11 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/ref")
public class RefController {
	@Autowired
	private RefService refService;
	
	/**
     *<b> Reference的列表页面</b>
     *<p> Reference的列表页面</p>
     * @author BINZI
     * @return java.lang.String
     */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String toIndex() {
		return "reference/index";
	}
	
	/**
     *<b>Reference管理页面之新建Reference实体</b>
     *<p> Reference管理页面之新建Reference实体</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model, HttpServletRequest request) {
		Ref thisRef = new Ref();
		thisRef.setInputtime(new Timestamp(System.currentTimeMillis()));
        model.addAttribute("thisRef", thisRef);
		return "reference/add";
	}
	
	/**
     *<b>修改Datasource</b>
     *<p> 修改Datasource的编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable String id) {
		Ref thisRef = this.refService.findOneById(id);
		model.addAttribute("thisRef", thisRef);
		return "reference/edit";
	}
	
	/**
     *<b>新增Ref实体</b>
     *<p> 新增Ref实体</p>
     * @author BINZI
	 * @param thisRef
	 * @param result
	 * @param model
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/save")
	public String save(@ModelAttribute("thisRef") @Valid Ref thisRef, BindingResult result, Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			isError(thisRef, result, model);
			return "reference/add";
		}else {
			this.refService.saveOne(thisRef, request);
			return "redirect:/console/ref";
		}
	}
	
	/**
     *<b>编辑Ref实体</b>
     *<p> 编辑Ref实体</p>
     * @author BINZI
	 * @param thisRef
	 * @param result
	 * @param model
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/edit")
	public String edit(@ModelAttribute("thisRef") @Valid Ref thisRef, BindingResult result, Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			isError(thisRef, result, model);
			return "reference/edit";
		}else {
			this.refService.updateOneById(thisRef);
			return "redirect:/console/ref";
		}
	}
	/** 
	 * <b> 新增|修改 重复代码提取</b>
	 * <p> 新增|修改 重复代码提取</p>
	 * @param thisDatasource
	 * @param result
	 * @param model
	 */
	public void isError(Ref thisRef, BindingResult result, Model model) {
		List<ObjectError> list = result.getAllErrors();
		String errorMsg = "";
		for (ObjectError error : list) {
			errorMsg = errorMsg + error.getDefaultMessage() + ";";
		}
		model.addAttribute("thisRef", thisRef);
		model.addAttribute("errorMsg", errorMsg);
	}
	
	/**
     *<b>Taxon的添加页面Select下拉选在指定Team下添加Ref</b>
     *<p> 添加新的Ref的编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@GetMapping("/addNew/{refId}/{refForm}")
	public String addNew(Model model, HttpServletRequest request, @PathVariable String refId, @PathVariable String refForm) {
		Ref thisRef = new Ref();
        model.addAttribute("thisRef", thisRef);
        model.addAttribute("refId", refId);
        model.addAttribute("refForm", refForm);
		return "reference/addModal";
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
		this.refService.export(response);
	}
	
}
