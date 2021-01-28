package org.big.controller.rest;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.big.common.UUIDUtils;
import org.big.entity.Taxon;
import org.big.entity.UserDetail;
import org.big.service.RankService;
import org.big.service.TaxasetService;
import org.big.service.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

@RestController
@Controller
@RequestMapping("/console/taxon/rest")
public class TaxonRestController {
	@Autowired
	private TaxonService taxonService;
	@Autowired
	private TaxasetService taxasetService;
	@Autowired
	private RankService rankService;

	/**
	 * <b> Taxon的Index页面分页列表查询</b>
	 * <p> Taxon的Index页面分页列表查询</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JSON list(HttpServletRequest request) {
		return this.taxonService.findTaxonList(request);
	}
	
	/**
	 * <b> Taxon添加</b>
	 * <p> Taxon添加</p>
	 * @param request
	 * @param thisTaxon
	 * @param result
	 * @param model
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value="/add", method = {RequestMethod.POST})
	public JSON AddTaxonBaseInfo(@ModelAttribute("thisTaxon") @Valid Taxon thisTaxon, BindingResult result, Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			String errorMsg = "";
			for (ObjectError error : list) {
				errorMsg = errorMsg + error.getDefaultMessage() + ";";
			}
			model.addAttribute("thisTaxon", thisTaxon);
			model.addAttribute("errorMsg", errorMsg);
		}
		
		return this.taxonService.addTaxonBaseInfo(thisTaxon, request);
	}

	/**
	 * <b> 根据Id批量逻辑删除指定Taxon及其相关的数据实体</b>
	 * <p> 根据Id批量逻辑删除指定Taxon及其相关的数据实体</p>
	 * @param
	 * @return 
	 */
	@RequestMapping(value = "/removeMany/{ids}", method = RequestMethod.GET)
	public int removeMany(@PathVariable String ids) {
		try {
			String[] idArr = ids.split("￥");
			int isRemove = 0;
			for (String id : idArr) {
				if (this.taxonService.delTaxon(id)) {
					isRemove = isRemove + 1;
				}
			}
			return isRemove;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * <b> 根据Id单个逻辑删除指定Taxon及其相关的数据实体</b>
	 * <p> 根据Id单个逻辑删除指定Taxon及其相关的数据实体</p>
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public boolean remove(@PathVariable String id) {
		try {
			return this.taxonService.delTaxon(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
     *<b>Taxon的select列表</b>
     *<p> 当前Taxkey下的Taxon的select检索列表</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	@RequestMapping(value = "/select/{id}", method = RequestMethod.GET)
	public JSON select(HttpServletRequest request, @PathVariable String id) {
		return this.taxonService.findBySelect(request, id);
	}
	
	/**
	 * <b> 为Taxon下的各个实体设置唯一标识</b>
	 * <p> 为Taxon下的各个实体设置唯一标识</p>
	 * @return
	 */
	@RequestMapping(value = "/uuid", method = RequestMethod.GET)
	public String uuid(HttpServletRequest request) {
		String uuid32 = UUIDUtils.getUUID32();
		request.getSession().setAttribute("uuid32", uuid32);
		return uuid32;
	}

	/**
	 * <b> 根据Rank和TaxonSet返回TaxonList</b>
	 * <p> 根据TaxonSet返回TaxonList(用作tree)</p>
	 * @author WangTianshan (王天山)
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public JSONArray tree(HttpServletRequest request) {
		String taxasetId = request.getParameter("taxasetId");
		String rankId = request.getParameter("rankId");
		String taxonName = request.getParameter("taxonName");
		return this.taxonService.findTaxonByTaxasetAndRankAndName(
				this.taxasetService.findOneById(taxasetId),
				this.rankService.findOneById(rankId),
				taxonName,
				1
		);
	}

	/**
	 *<b>根据TaxonId确定所属分类单元集</b>
	 *<p> 根据TaxonId确定所属分类单元集</p>
	 * @author BINZI
	 * @param request
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/taxasetId/{id}")
	public JSON getTaxasetId(HttpServletRequest request, @PathVariable String id) {
		return this.taxonService.findTaxasetIdByTaxonId(request, id);
	}

	/**
	 *<b>根据TaxonId确定分类单元</b>
	 *<p> 根据TaxonId确定分类单元</p>
	 * @author  WangTianshan (王天山)
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/taxonBasics/{id}")
	public JSON TaxonBasics( @PathVariable String id) {
		return this.taxonService.findTaxonBasics(id);
	}
	
	/**
	 * <b>解析导入的Taxon相关的Excel文件</b>
	 * <p> 解析导入的Taxon相关的Excel文件</p>
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/upload/{taxasetId}")
    public JSON upload(@RequestParam("uploadTaxon") MultipartFile file, @PathVariable String taxasetId, HttpServletRequest request) throws Exception{
		return this.taxonService.uploadFile(file, taxasetId, request);
	}
	
	/**
	 *<b>Taxon的已上传listJSON</b>
	 *<p> Taxon的已上传listJSON</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value = "/uploaded/{taxasetId}/{timestamp}")
	public JSON list(HttpServletRequest request, @PathVariable String taxasetId, @PathVariable Timestamp timestamp) {
		return this.taxonService.findUploadedTaxonList(taxasetId, timestamp, request);
	}

	/**
     * <b>是否有删除Taxon权限</b>
     * <p> 是否有删除Taxon权限</p>
	 * @author BINZI
	 * @param request
	 * @param id
	 * @return
	 */
    @GetMapping("/canRemoveObj/{id}")
    public JSON canRemoveObj(HttpServletRequest request, @PathVariable String id) {
    	JSONObject thisResult = new JSONObject();
    	try {
    		String leader = this.taxonService.findOneById(id).getTaxaset().getDataset().getTeam().getLeader();
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (thisUser.getId().equals(leader)) {
				thisResult.put("rsl", true);
			}else {
				thisResult.put("rsl", false);
			}
		} catch (Exception e) {
		}
    	return thisResult;
	}
    /**
     * @Description 异名文献匹配
     * @author BINZI
     * @return
     * @throws Exception
     */
    @RequestMapping("/matchSynonymRef")
	public JSON matchSynonymRef() throws Exception {
    	String path = "E:/异名文献数据匹配.xlsx";
    	this.taxonService.matchSynonymRef(path);
		return null;
	}
    /**
     * @Description 接受名文献匹配
     * @author BINZI
     * @return
     * @throws Exception
     */
    @RequestMapping("/matchAcceptRef")
	public JSON matchAcceptRef() throws Exception {
    	String path = "E:/接受名文献数据匹配.xlsx";
    	this.taxonService.matchAcceptRef(path);
		return null;
	}
    
    /**
     * @Description 鱼类属下亚种种加词/亚种加词处理
     * @author BINZI
     * @return
     */
    @RequestMapping("/handleFishTaxon")
	public JSON handleFishTaxon() {
    	this.taxonService.handleFishTaxon();
		return null;
	}
    
    @RequestMapping("/handleDistributiondata")
	public JSON handleDistributiondata() {
    	this.taxonService.handleDistributiondata();
		return null;
	}
    
    @RequestMapping("/handleTaxonSciname")
	public JSON handleTaxonSciname() {
    	this.taxonService.handleTaxonSciname();
		return null;
	}
    @RequestMapping("/handleCitationSciname")
   	public JSON handleCitationSciname() {
       	this.taxonService.handleCitationSciname();
   		return null;
   	}
    
    /**
     * @Description 接受名文献匹配
     * @author BINZI
     * @return
     * @throws Exception
     */
    @RequestMapping("/handleTaxon")
	public JSON handleTaxon() throws Exception {
    	String path = "E:/数据.xlsx";
    	this.taxonService.handleTaxon(path);
		return null;
	}
    
    /**
     * @Description 接受名文献匹配
     * @author BINZI
     * @return
     * @throws Exception		
     */
    @RequestMapping("/handleDistribution")
	public JSON handleDistribution() throws Exception {
    	String path = "E:/两栖.xlsx";
    	this.taxonService.handleDistribution(path);
		return null;
	}
    
    /**
     * @Description 接受名文献匹配
     * @author BINZI
     * @return
     * @throws Exception		
     */
    @RequestMapping("/handleGeoobjectChange")
	public JSON handleGeoobjectChange() throws Exception {
    	String path = "E:/变迁.xlsx";
    	this.taxonService.handleGeoobjectChange(path);
		return null;
	}
    
    @RequestMapping("/handleButterfly")
	public JSON handleButterfly() throws Exception {
    	String path = "E:/蝶蛾类训练编号名称列表_v2.xlsx";
    	this.taxonService.handleButterfly(path);
		return null;
	}

}
