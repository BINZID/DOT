package org.big.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.big.common.UUIDUtils;
import org.big.entity.Taxaset;
import org.big.entity.Taxon;
import org.big.service.DatasourceService;
import org.big.service.RefService;
import org.big.service.TaxasetService;
import org.big.service.TaxonService;
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

import com.alibaba.fastjson.JSONArray;

/**
 * <p><b>TaxonController类</b></p>
 * <p> Taxon的Controller</p>
 *
 * @author WangTianshan (王天山)
 *         <p>Created date: 2018/5/11 09:24</p>
 *         <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/taxon")
public class TaxonController {
	@Autowired
    private TaxonService taxonService;
	@Autowired
	private TaxasetService taxasetService;
	@Autowired
	private DatasourceService datasourceService;
    @Autowired
	private RefService refService;
    
    /**
     * <b>默认管理页面</b>
     * <p> 默认管理页面</p>
     * @return java.lang.String
     * @author WangTianshan (王天山)
     */
    @RequestMapping(value = "", method = {RequestMethod.GET})
    public String Index(Model model) {
        return "taxon/index";
    }

    /**
     * <b>新建页面</b>
     * <p> 新建页面</p>
     *
     * @return java.lang.String
     * @author WangTianshan (王天山)
     */
    @RequestMapping(value = "/add", method = {RequestMethod.GET})
    public String Add(Model model, HttpServletRequest request) {
        Taxon thisTaxon = new Taxon();
        thisTaxon.setId(UUIDUtils.getUUID32());
        thisTaxon.setInputtime(new Timestamp(System.currentTimeMillis()));
        Taxaset thisTaxaset = new Taxaset();
        try{
            thisTaxaset=this.taxasetService.findOneById(request.getParameter("taxonset"));
            thisTaxon.setTaxaset(thisTaxaset);
        }
        catch (Exception e){
        }
        model.addAttribute("thisTaxon", thisTaxon);
        model.addAttribute("thisTaxaset", thisTaxaset);
        return "taxon/add";
    }
    
    /**
     * <b>分类树弹出层新建Taxon页面</b>
     * <p> 分类树弹出层新建Taxon页面</p>
     *
     * @return java.lang.String
     * @author WangTianshan (王天山)
     */
    @GetMapping(value = "/addNewTaxon/{taxTreeId}/{targetTaxonId}/{moveType}")
    public String addNew(Model model, HttpServletRequest request, @PathVariable String taxTreeId, @PathVariable String targetTaxonId, @PathVariable String moveType) {
        Taxon thisTaxon = new Taxon();
        thisTaxon.setId(UUIDUtils.getUUID32());
        thisTaxon.setInputtime(new Timestamp(System.currentTimeMillis()));
        
        model.addAttribute("thisTaxon", thisTaxon);
        model.addAttribute("taxTreeId", taxTreeId);
        model.addAttribute("targetTaxonId", targetTaxonId);
        model.addAttribute("moveType", moveType);
        return "taxon/addModal";
    }
    
    /**
     *<b>修改Taxon的编辑的页面</b>
     *<p> 修改Taxon的编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable String id, HttpServletRequest request) {
		Taxon thisTaxon = this.taxonService.findOneById(id);
		String enname = thisTaxon.getRank().getEnname();
		String chname = thisTaxon.getRank().getChname();
		thisTaxon.getRank().setEnname(enname + "<" + chname + ">");
		String refjson = thisTaxon.getRefjson();
		JSONArray taxonRefs = refService.refactoringRef(refjson);
		thisTaxon.setRefjson(taxonRefs.toJSONString());
		model.addAttribute("thisTaxon", thisTaxon);
		return "taxon/edit";
	}
	
	/**
     *<b>保存修改Taxon</b>
     *<p> 修改Taxon的编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveEdit(@ModelAttribute("thisTaxon") @Valid Taxon thisTaxon, BindingResult result, Model model, HttpServletRequest request){
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			String errorMsg = "";
			for (ObjectError error : list) {
				errorMsg = errorMsg + error.getDefaultMessage() + ";";
			}
			model.addAttribute("thisTaxon", thisTaxon);
			model.addAttribute("errorMsg", errorMsg);
			return "taxon/edit";
		}else {
			thisTaxon.setTaxonCondition(1);
			this.taxonService.updateOneById(thisTaxon);
			return "redirect:/console/taxon";
		}
	}
	/**
	 *<b>Taxon详情</b>
	 *<p> 对已有的添加Taxon进行详情查看管理</p>
	 * @author BINZI
	 * @param model 初始化模型
	 * @param id 被编辑添加Taxon实体id
	 * @return java.lang.String
	 */
	@GetMapping("/show/{id}")
	public String Show(Model model, @PathVariable String id, HttpServletRequest request) {
		Taxon thisTaxon = taxonService.findOneById(id);
		model.addAttribute("thisTaxon", thisTaxon);
		model.addAttribute("thisSources", this.datasourceService.findOneById(thisTaxon.getSourcesid()));
		return "taxon/details";
	}

	/**
	 *<b>Taxon审核通过</b>
	 *<p>Taxon审核通过</p>
	 * @author MengMeng (王孟豪)
	 * @param request
	 * @return java.lang.String
	 */
	@GetMapping("/review/approve")
	public String ApproveReview(HttpServletRequest request) {
		String tid = request.getParameter("tid");
		Taxon thisTaxon = this.taxonService.findOneById(tid);
		this.taxonService.ReviewByTaxonId(tid,2);
		return "redirect:/console/taxaset/show/"+thisTaxon.getTaxaset().getId();
	}

    /**
     *<b>Taxon审核不通过</b>
     *<p>Taxon审核不通过</p>
     * @author WangTianshan (王天山)
     * @param request
     * @return java.lang.String
     */
    @GetMapping("/review/disapprove")
    public String DisapproveReview(HttpServletRequest request) {
        String tid = request.getParameter("tid");
        Taxon thisTaxon = this.taxonService.findOneById(tid);
        this.taxonService.ReviewByTaxonId(tid,-1);
        return "redirect:/console/taxaset/show/"+thisTaxon.getTaxaset().getId();
    }

    /**
     *<b>Taxon提交审核不通过</b>
     *<p>Taxon提交审核不通过</p>
     * @author WangTianshan (王天山)
     * @param request
     * @return java.lang.String
     */
    @GetMapping("/review/submit")
    public String SubmitReview(HttpServletRequest request) {
        String tid = request.getParameter("tid");
        Taxon thisTaxon = this.taxonService.findOneById(tid);
        this.taxonService.ReviewByTaxonId(tid,1);
        return "redirect:/console/taxaset/show/"+thisTaxon.getTaxaset().getId();
    }
    
    /**
     * <b>去文件上传页</b>
     * <p> 去文件上传页</p>
     * @author BINZI
     * @param taxasetId
     * @param model
     * @return
     */
    @RequestMapping("upload/{taxasetId}")
    public String upload(@PathVariable String taxasetId, Model model){
    	Taxon thisTaxon = new Taxon();
        thisTaxon.setId(UUIDUtils.getUUID32());
        Taxaset thisTaxaset = new Taxaset();
        try{
            thisTaxaset=this.taxasetService.findOneById(taxasetId);
            thisTaxon.setTaxaset(thisTaxaset);
        }catch (Exception e){
        }
        model.addAttribute("thisTaxon", thisTaxon);
        model.addAttribute("thisTaxaset", thisTaxaset);
        model.addAttribute("timestamp", new Timestamp(System.currentTimeMillis()));
    	
        return "taxon/uploadFile";
    }
    
    /**
     * <b>导出指定分类单元集下的Taxon数据</b>
     * <p> 导出指定分类单元集下的Taxon数据</p>
     * @author BINZI
     * @param taxasetId
     * @param response
     * @throws IOException
     */
    @RequestMapping("export/{taxasetId}")
    public void export(@PathVariable String taxasetId, HttpServletResponse response) throws IOException {
    	this.taxonService.export(taxasetId, response);
    }

}
