package org.big.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.big.entity.Dataset;
import org.big.service.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *<p><b>超级管理员相关的Controller类</b></p>
 *<p> 超级管理员相关的Controller</p>
 * @author BINZI
 *<p>Created date: 2018/04/10 11:07</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/dataset")
public class DatasetController {
	@Autowired
	private DatasetService datasetService;
	/**
     *<b>Dataset管理页</b>
     *<p> 包含所有Dataset的信息列表、操作选项</p>
     * @author BINZI
     * @return java.lang.String
     */
	@RequestMapping(value = "", method = { RequestMethod.GET })
	public String index() {
		return "dataset/index";
	}
    
	/**
     *<b>在指定用户组下添加Dataset</b>
     *<p> 添加新的Dataset的编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/add", method = { RequestMethod.GET })
	public String AddDatasetForTeam(Model model, HttpServletRequest request) {
		Dataset thisDataset = new Dataset();
		thisDataset.setId(UUID.randomUUID().toString());
		model.addAttribute("thisDataset", thisDataset);
		return "dataset/add";
	}
    /**
     *<b>编辑Dataset</b>
     *<p> 对已有的Dataset进行编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @param id 被编辑Dataset实体id
     * @return java.lang.String
     */
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String Edit(Model model, @PathVariable String id, HttpServletRequest request) {
		Dataset thisDataset = this.datasetService.findbyID(id);
		model.addAttribute("thisDataset", thisDataset);
		model.addAttribute("editdsname", thisDataset.getDsname());
		return "dataset/edit";
	}
    
    /**
     *<b>Dataset详情</b>
     *<p> 对已有的Dataset进行详情查看管理</p>
     * @author BINZI
     * @param model 初始化模型
     * @param id 被编辑Dataset实体id
     * @return java.lang.String
     */
	@RequestMapping(value = "/show/{id}", method = { RequestMethod.GET })
	public String Show(Model model, @PathVariable String id, HttpServletRequest request) {
		String teamId = (String) request.getSession().getAttribute("teamId");
		List<Dataset> dsList = this.datasetService.findAllDatasetsByTeamId(teamId);
		for (Dataset dataset : dsList) {
			if (dataset.getId().equals(id)) {
				model.addAttribute("thisDataset", dataset);
				int offset_serch = 0;
				try {
					offset_serch = Integer.parseInt(request.getParameter("offset"));
				} catch (Exception e) {
				}
				model.addAttribute("thisPage", offset_serch);
				return "dataset/show";
			}
		}
		return "redirect:/select/dataset";
	}
    
    /**
     *<b>添加新的Dataset</b>
     *<p> 添加新的Dataset实体保存</p>
     * @author BINZI
     * @param thisDataset 传入的Dataset实体
     * @return java.lang.String
     */
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String Add(@ModelAttribute("thisDataset") @Valid Dataset thisDataset, BindingResult result, Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			String errorMsg = "";
			for (ObjectError error : list) {
				errorMsg = errorMsg + error.getDefaultMessage() + ";";
			}
			model.addAttribute("thisDataset", thisDataset);
			model.addAttribute("errorMsg", errorMsg);
			return "dataset/add";
		}
		this.datasetService.addOne(thisDataset, request);
		return "redirect:/console/dataset";
	}
    
    /**
     *<b>保存修改后的Dataset</b>
     *<p> 将传入的Dataset实体保存</p>
     * @author BINZI
     * @param thisDataset 传入的Dataset实体
     * @return java.lang.String
     */
	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public String Save(@ModelAttribute("thisDataset") @Valid Dataset thisDataset, BindingResult result, Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			String errorMsg = "";
			for (ObjectError error : list) {
				errorMsg = errorMsg + error.getDefaultMessage() + ";";
			}
			model.addAttribute("thisDataset", thisDataset);
			model.addAttribute("errorMsg", errorMsg);
			return "dataset/edit";
		}
		this.datasetService.saveOne(thisDataset);
		return "redirect:/console/" + thisDataset.getTeam().getId();
	}
	
    /**
     *<b>选择数据集页面新建数据集</b>
     *<p> 选择数据集页面新建数据集</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
    @GetMapping(value="/add/mark")
    public String Adds(Model model, HttpServletRequest request) {
    	Dataset thisDataset = new Dataset();
        model.addAttribute("thisDataset", thisDataset);
        return "dataset/addModal";
    }
    
    /**
     * <b>切换数据集div的背景</b>
     * <p>切换数据集div的背景</p>
     * @param id
     * @param mark
     * @param model
     * @return
     */
    @GetMapping("/changeBg/{id}/{mark}")
    public String changeBg(@PathVariable String id, @PathVariable String mark, Model model) {
    	model.addAttribute("id", id);
    	model.addAttribute("mark", mark);
    	return "console/changeBg";
	}
}
