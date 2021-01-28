package org.big.controller;

import org.big.entity.Dataset;
import org.big.entity.Taxtree;
import org.big.repository.TaxonRepository;
import org.big.repository.TaxtreeRepository;
import org.big.service.DatasetService;
import org.big.service.TaxtreeService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;

/**
 *<p><b>Taxtree实体的Controller类</b></p>
 *<p> Taxtree实体的Controller</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2018/08/02 23:57</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/taxtree")
public class TaxtreeController {
	@Autowired
	private TaxtreeService taxtreeService;
	@Autowired
	private DatasetService datasetService;
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private TaxtreeRepository taxtreeRepository;
	/**
     *<b>taxtree管理页</b>
     *<p> 包含所有taxtree的信息列表、操作选项</p>
     * @author WangTianshan (王天山)
     * @return java.lang.String
     */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index() {
		return "taxtree/index";
	}
	
	/**
     *<b>添加Taxtree</b>
     *<p> 添加新的Taxtree的编辑的页面</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) {
		Taxtree thisTaxtree = new Taxtree();
		model.addAttribute("thisTaxtree", thisTaxtree);
		return "taxtree/add";
	}
	
	/**
     *<b>修改Taxtree</b>
     *<p> 修改Taxtree的编辑的页面</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable String id) {
		Taxtree thisTaxtree = this.taxtreeService.findOneById(id);
		model.addAttribute("thisTaxtree", thisTaxtree);
		model.addAttribute("editdsname", thisTaxtree.getTreename());
		return "taxtree/edit";
	}
	
	/**
     *<b>保存修改的Taxtree实体</b>
     *<p> 保存修改的Taxtree实体</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("thisTaxtree") @Valid Taxtree thisTaxtree, BindingResult result, Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			isError(thisTaxtree, result, model);
			return "taxtree/edit";
		}else {
			this.taxtreeService.updateOneById(thisTaxtree);
			return "redirect:/console/taxtree/toTaxtreePage/" + thisTaxtree.getDataset().getId();
		}
	}
	
	/**
     *<b>保存新增的Taxtree实体</b>
     *<p> 保存新增的Taxtree实体</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String newTaxtree(@ModelAttribute("thisTaxtree") @Valid Taxtree thisTaxtree, BindingResult result, Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			isError(thisTaxtree, result, model);
			return "taxtree/add";
		}else {
			String datasetID = (String) request.getSession().getAttribute("datasetID");
			thisTaxtree.setDataset(this.datasetService.findbyID(datasetID));
			thisTaxtree.setInputtime(new Timestamp(System.currentTimeMillis()));
			this.taxtreeService.saveOne(thisTaxtree);
			return "redirect:/console/taxtree/toTaxtreePage/" + datasetID;
		}
	}

	/**
	 * <b> 新增|修改 重复代码提取</b>
	 * <p> 新增|修改 重复代码提取</p>
	 * @param thisTaxtree
	 * @param result
	 * @param model
	 */
	private void isError(Taxtree thisTaxtree, BindingResult result, Model model) {
		List<ObjectError> list = result.getAllErrors();
		String errorMsg = "";
		for (ObjectError error : list) {
			errorMsg = errorMsg + error.getDefaultMessage() + ";";
		}
		model.addAttribute("thisTaxtree", thisTaxtree);
		model.addAttribute("errorMsg", errorMsg);
	}
	
	/**
     *<b>添加Taxtree</b>
     *<p> 添加新的Taxtree的编辑的页面</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/addNew", method = RequestMethod.GET)
	public String addNew(Model model, HttpServletRequest request) {
		String type = request.getParameter("type");
		Taxtree thisTaxtree = new Taxtree();
		model.addAttribute("thisTaxtree", thisTaxtree);
		if(type!=null){
			switch (type){
				case "dataset":
					model.addAttribute("type", type);
					break;
				default:
			}
		}
		return "taxtree/addModal";
	}

	/**
	 *<b>显示Taxtree详情</b>
	 *<p> 对已有的添加Taxtree进行详情查看管理</p>
	 * @author wangtianshan
	 * @param model 初始化模型
	 * @param id 被编辑添加Taxtree实体id
	 * @return java.lang.String
	 */
	@RequestMapping(value = "/show/{id}", method = { RequestMethod.GET })
	public String Show(Model model, @PathVariable String id, HttpServletRequest request) {
		Taxtree thisTaxtree=taxtreeService.findOneById(id);
		model.addAttribute("thisTaxtree", thisTaxtree);
		return "taxtree/show";
	}

	/**
	 *<b>构建Taxtree</b>
	 *<p> 对已有Taxtree进行taxon管理</p>
	 * @author wangtianshan
	 * @param model 初始化模型
	 * @param id 被编辑添加Taxtree实体id
	 * @return java.lang.String
	 */
	@RequestMapping(value = "/build/{id}", method = { RequestMethod.GET })
	public String Build(Model model, @PathVariable String id, HttpServletRequest request) {
		Taxtree thisTaxtree=taxtreeService.findOneById(id);
		model.addAttribute("thisTaxtree", thisTaxtree);
		return "taxtree/build";
	}
	
	/**
	 *<b>根据DatasetId查找所属Taxtree（分页）</b>
	 *<p> 根据DatasetId查找所属Taxtree（分页）</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @return
	 */
	@GetMapping(value = "/toTaxtreePage/{dsid}")
	public String toTaxtreePage(Model model, @PathVariable String dsid, HttpServletRequest request) {
		request.getSession().setAttribute("datasetID", dsid);	// 将datasetId存放到Session中
		request.getSession().setAttribute("datasetName", this.datasetService.findbyID(dsid).getDsname());
		request.getSession().setAttribute("dataset", this.datasetService.findbyID(dsid));
		
		String teamId = (String) request.getSession().getAttribute("teamId");
		List<Dataset> dsList = this.datasetService.findAllDatasetsByTeamId(teamId);
		for (Dataset dataset : dsList) {
			if (dataset.getId().equals(dsid)) {
				model.addAttribute("thisDataset", dataset);
				model.addAttribute("taxon", this.taxonRepository.countByStatusAndTaxaset_Dataset_Id(1,dataset.getId()));
				model.addAttribute("taxtree",  this.taxtreeRepository.countByStatusAndDataset_Id(1,dataset.getId()));
				model.addAttribute("phytree",  0);
				int offset_serch = 0;
				try {
					offset_serch = Integer.parseInt(request.getParameter("offset"));
				} catch (Exception e) {
				}
				model.addAttribute("thisPage", offset_serch);
				return "taxtree/taxtree";
			}
		}
		return "taxtree/taxtree";
	}
	
    /**
     * <b>切换Taxtreediv的背景</b>
     * <p>切换Taxtreediv的背景</p>
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
