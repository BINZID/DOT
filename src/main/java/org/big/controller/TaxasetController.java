package org.big.controller;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.big.entity.Dataset;
import org.big.entity.Taxaset;
import org.big.repository.TaxonRepository;
import org.big.repository.TaxtreeRepository;
import org.big.service.DatasetService;
import org.big.service.TaxasetService;
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
 *<p><b>Taxaset实体的Controller类</b></p>
 *<p> Taxaset实体的Controller</p>
 * @author BINZI
 *<p>Created date: 2018/05/21 14:07</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/taxaset")
public class TaxasetController {
	@Autowired
	private TaxasetService taxasetService;
	@Autowired
	private DatasetService datasetService;
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private TaxtreeRepository taxtreeRepository;
	
	/**
     *<b>Dataset管理页</b>
     *<p> 包含所有Dataset的信息列表、操作选项</p>
     * @author BINZI
     * @return java.lang.String
     */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index() {
		return "taxaset/index";
	}
	
	/**
     *<b>添加Taxaset</b>
     *<p> 添加新的Taxaset的编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) {
		Taxaset thisTaxaset = new Taxaset();
		model.addAttribute("thisTaxaset", thisTaxaset);
		return "taxaset/add";
	}
	
	/**
     *<b>修改Taxaset</b>
     *<p> 修改Taxaset的编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable String id) {
		Taxaset thisTaxaset = this.taxasetService.findOneById(id);
		model.addAttribute("thisTaxaset", thisTaxaset);
		model.addAttribute("editdsname", thisTaxaset.getTsname());
		return "taxaset/edit";
	}
	
	/**
     *<b>保存修改的Taxaset实体</b>
     *<p> 保存修改的Taxaset实体</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("thisTaxaset") @Valid Taxaset thisTaxaset, BindingResult result, Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			isError(thisTaxaset, result, model);
			return "taxaset/edit";
		}else {
			this.taxasetService.updateOneById(thisTaxaset);
			return "redirect:/console/taxaset/toTaxasetPage/" + thisTaxaset.getDataset().getId();
		}
	}
	
	/**
     *<b>保存新建的Taxaset实体</b>
     *<p> 保存新建的Taxaset实体</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String newTaxaset(@ModelAttribute("thisTaxaset") @Valid Taxaset thisTaxaset, BindingResult result, Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			isError(thisTaxaset, result, model);
			return "taxaset/add";
		}else {
			String datasetID = (String) request.getSession().getAttribute("datasetID");
			thisTaxaset.setDataset(this.datasetService.findbyID(datasetID));
			thisTaxaset.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			this.taxasetService.saveOne(thisTaxaset);
			return "redirect:/console/taxaset/toTaxasetPage/" + datasetID;
		}
	}
	
	public String show(@PathVariable String id, HttpServletRequest request, Model model) { // 指定数据集是否有分类单元集
		String dsId = (String) request.getSession().getAttribute("datasetID");
		List<Taxaset> tsList = this.taxasetService.findTaxasetsByDatasetId(dsId);
		for (Taxaset taxaset : tsList) {
			if (id.equals(taxaset.getId())) {
				Taxaset thisTaxaset = this.taxasetService.findOneById(id);
				model.addAttribute("thisTaxaset", thisTaxaset);
				return "taxaset/show";
			}
		}
		return "403";
	}
	
	/** 
	 * <b> 新增|修改 重复代码提取</b>
	 * <p> 新增|修改 重复代码提取</p>
	 * @param thisTaxaset
	 * @param result
	 * @param model
	 */
	private void isError(Taxaset thisTaxaset, BindingResult result, Model model) {
		List<ObjectError> list = result.getAllErrors();
		String errorMsg = "";
		for (ObjectError error : list) {
			errorMsg = errorMsg + error.getDefaultMessage() + ";";
		}
		model.addAttribute("thisTaxaset", thisTaxaset);
		model.addAttribute("errorMsg", errorMsg);
	}
	
	/**
     *<b>添加Taxaset</b>
     *<p> 添加新的Taxaset的编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/addNew", method = RequestMethod.GET)
	public String addNew(Model model, HttpServletRequest request) {
		String type = request.getParameter("type");
		Taxaset thisTaxaset = new Taxaset();
		model.addAttribute("thisTaxaset", thisTaxaset);
		if(type!=null){
			switch (type){
				case "dataset":
					model.addAttribute("type", type);
					break;
				default:
			}
		}
		return "taxaset/addModal";
	}
	
	/**
	 *<b>添加Taxaset详情</b>
	 *<p> 对已有的添加Taxaset进行详情查看管理</p>
	 * @author wangtianshan
	 * @param model 初始化模型
	 * @param id 被编辑添加Taxaset实体id
	 * @return java.lang.String
	 */
	@RequestMapping(value = "/show/{id}", method = { RequestMethod.GET })
	public String Show(Model model, @PathVariable String id, HttpServletRequest request) {
		Taxaset thisTaxaset=taxasetService.findOneById(id);
		model.addAttribute("thisTaxaset", thisTaxaset);
		return "taxaset/show";
	}
	
	/**
	 *<b>根据DatasetId查找所属Taxaset（分页）</b>
	 *<p> 根据DatasetId查找所属Taxaset（分页）</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @return
	 */
	@GetMapping(value = "/toTaxasetPage/{dsid}")
	public String toTaxasetPage(Model model, @PathVariable String dsid, HttpServletRequest request) {
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
				return "taxaset/taxaset";
			}
		}
		return "taxaset/taxaset";
	}
	
    /**
     * <b>切换Taxasetdiv的背景</b>
     * <p>切换Taxasetdiv的背景</p>
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
