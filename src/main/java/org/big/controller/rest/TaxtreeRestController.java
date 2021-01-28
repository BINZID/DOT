package org.big.controller.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.big.entity.Dataset;
import org.big.entity.TaxonHasTaxtree;
import org.big.entity.Taxtree;
import org.big.entity.UserDetail;
import org.big.service.TaxtreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController  //返回json
@Controller
@RequestMapping("/console/taxtree/rest")
public class TaxtreeRestController {
	@Autowired
	private TaxtreeService taxtreeService;

	/**
	 * <b> Taxtree的Index页面分页列表查询</b>
	 * <p> Taxtree的Index页面分页列表查询</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JSON list(HttpServletRequest request) {
		return this.taxtreeService.findTaxtreeList(request);
	}

	/**
	 * <b> 根据Id批量逻辑删除指定Taxtree</b>
	 * <p> 根据Id批量逻辑删除指定Taxtree</p>
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/removeMany/{ids}", method = RequestMethod.GET)
	public int removeMany(@PathVariable String ids) {
		try {
			String[] idArr = ids.split("￥");
			int isRemove = 0;
			for (String id : idArr) {
				if (this.taxtreeService.logicRemove(id)) {
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
	 * <b> 根据Id单个逻辑删除指定Taxtree</b>
	 * <p> 根据Id单个逻辑删除指定Taxtree</p>
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public boolean remove(@PathVariable String id) {
		try {
			return this.taxtreeService.logicRemove(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 *<b>Taxtree的select列表</b>
	 *<p> 当前用户的Taxtree的select检索列表</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public JSON select(HttpServletRequest request){
		return this.taxtreeService.findBySelect(request);
	}

	/**
	 *<b>Taxtree的select列表(含新建)</b>
	 *<p> 当前用户的Taxtree的select检索列表(含新建)</p>
	 * @author WangTianshan (王天山)
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value = "/selectAndNew", method = RequestMethod.GET)
	public JSON selectAndNew(HttpServletRequest request){
		return this.taxtreeService.findBySelectAndNew(request);
	}

	/**
	 *<b>Taxtree的select列表之新建Taxtree实体</b>
	 *<p> Taxtree的select列表之新建Taxtree实体</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public JSON New(@ModelAttribute("thisTaxtree") @Valid Taxtree thisTaxtree, BindingResult result, Model model, HttpServletRequest request) {
		return this.taxtreeService.newOne(thisTaxtree, request);
	}

	/**
	 * <b> 添加一个taxon进入tree</b>
	 * <p> 添加一个taxon进入tree</p>
	 * @author WangTianshan (王天山)
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/addOneTaxon", method = RequestMethod.GET)
	public boolean addOneTaxon(HttpServletRequest request) {
		TaxonHasTaxtree thisTaxonHasTaxtree=new TaxonHasTaxtree();
		thisTaxonHasTaxtree.setTaxtreeId(request.getParameter("taxTreeId"));
		thisTaxonHasTaxtree.setTaxonId(request.getParameter("taxonId"));
		if(request.getParameter("targetTaxonId").length()<=0 || request.getParameter("targetTaxonId").equals("null")) {		//无目标节点
			thisTaxonHasTaxtree.setPid(request.getParameter("taxTreeId"));
			//查询最后一个节点
			TaxonHasTaxtree lastNode = this.taxtreeService.findLastNode(this.taxtreeService
					.findChildren(request.getParameter("taxTreeId"), request.getParameter("taxTreeId")));
			if(lastNode!=null){
				thisTaxonHasTaxtree.setPrevTaxon(lastNode.getTaxonId());	//有
			} else {
				thisTaxonHasTaxtree.setPrevTaxon(null);						//无
			}
		} else {		//有目标节点
			switch (request.getParameter("moveType")){	//判断位置
				case "inner":
					thisTaxonHasTaxtree.setPid(request.getParameter("targetTaxonId"));
					List<TaxonHasTaxtree> chindrenNodes=this.taxtreeService.findChildren(request.getParameter("targetTaxonId"),request.getParameter("taxTreeId"));
					if(chindrenNodes.size()>0){			//判断是否有子节点
						thisTaxonHasTaxtree.setPrevTaxon(this.taxtreeService.findLastNode(chindrenNodes).getTaxonId());	//有子节点
					}else {
						thisTaxonHasTaxtree.setPrevTaxon(null);															//无子节点
					}
					break;
				case "prev":
					TaxonHasTaxtree prevNode=this.taxtreeService.findOneTaxonHasTaxtreeByIds(request.getParameter("targetTaxonId"),request.getParameter("taxTreeId"));
					thisTaxonHasTaxtree.setPid(prevNode.getPid());
					if(prevNode.getPrevTaxon()==null){	//判断是否被置为首节点
						thisTaxonHasTaxtree.setPrevTaxon(null);						//被置为首节点
					}else{
						thisTaxonHasTaxtree.setPrevTaxon(prevNode.getPrevTaxon());	//未被置为首节点
					}
					prevNode.setPrevTaxon(thisTaxonHasTaxtree.getTaxonId());
					this.taxtreeService.hasTaxonHasTaxtree(prevNode);
					break;
				case "next":	
					TaxonHasTaxtree oldNextNode=this.taxtreeService.findNextNode(request.getParameter("targetTaxonId"),request.getParameter("taxTreeId"));	//查找原来排在后面的
					if(oldNextNode!=null){				//判断是否有子节点
						oldNextNode.setPrevTaxon(thisTaxonHasTaxtree.getTaxonId());
						this.taxtreeService.saveOneTaxonHasTaxtree(oldNextNode);
					}
					TaxonHasTaxtree targetNode=this.taxtreeService.findOneTaxonHasTaxtreeByIds(request.getParameter("targetTaxonId"),request.getParameter("taxTreeId"));
					thisTaxonHasTaxtree.setPid(targetNode.getPid());
					thisTaxonHasTaxtree.setPrevTaxon(request.getParameter("targetTaxonId"));
					break;
				default:
					break;
			}
		}
		if(this.taxtreeService.hasTaxonHasTaxtree(thisTaxonHasTaxtree)){
			return false;
		}
		else{
			this.taxtreeService.saveOneTaxonHasTaxtree(thisTaxonHasTaxtree);
			return true;
		}
	}

	/**
	 * <b> 添加部分tree进入tree</b>
	 * <p> 添加部分tree进入tree</p>
	 * @author WangTianshan (王天山)
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/addOtherTaxTree", method = RequestMethod.GET)
	public boolean addOtherTaxTree(HttpServletRequest request) {
		TaxonHasTaxtree thisTaxonHasTaxtree=new TaxonHasTaxtree();
		thisTaxonHasTaxtree.setTaxtreeId(request.getParameter("taxTreeId"));
		thisTaxonHasTaxtree.setTaxonId(request.getParameter("taxonId"));
		if(request.getParameter("targetTaxonId").length()<=0) {
			thisTaxonHasTaxtree.setPid(request.getParameter("taxTreeId"));//无目标节点
			//查询最后一个节点
			TaxonHasTaxtree lastNode=this.taxtreeService.findLastNode(this.taxtreeService.findChildren(request.getParameter("taxTreeId"),request.getParameter("taxTreeId")));
			if(lastNode!=null){
				//有
				thisTaxonHasTaxtree.setPrevTaxon(lastNode.getTaxonId());
			}
			else
				thisTaxonHasTaxtree.setPrevTaxon(null);
		}
		else {//有目标节点
			//判断位置
			switch (request.getParameter("moveType")){
				case "inner":
					thisTaxonHasTaxtree.setPid(request.getParameter("targetTaxonId"));
					//判断是否有子节点
					List<TaxonHasTaxtree> chindrenNodes=this.taxtreeService.findChildren(request.getParameter("targetTaxonId"),request.getParameter("taxTreeId"));
					if(chindrenNodes.size()>0){
						//有子节点
						thisTaxonHasTaxtree.setPrevTaxon(this.taxtreeService.findLastNode(chindrenNodes).getTaxonId());
					}
					else
						thisTaxonHasTaxtree.setPrevTaxon(null);
					break;
				case "prev":
					TaxonHasTaxtree prevNode=this.taxtreeService.findOneTaxonHasTaxtreeByIds(request.getParameter("targetTaxonId"),request.getParameter("taxTreeId"));
					thisTaxonHasTaxtree.setPid(prevNode.getPid());
					//判断是否被置为首节点
					if(prevNode.getPrevTaxon()==null){
						//被置为首节点
						thisTaxonHasTaxtree.setPrevTaxon(null);
					}
					else{
						thisTaxonHasTaxtree.setPrevTaxon(prevNode.getPrevTaxon());
					}
					prevNode.setPrevTaxon(thisTaxonHasTaxtree.getTaxonId());
					this.taxtreeService.hasTaxonHasTaxtree(prevNode);
					break;
				case "next":
					//查找原来排在后面的
					//判断是否有子节点
					TaxonHasTaxtree oldNextNode=this.taxtreeService.findNextNode(request.getParameter("targetTaxonId"),request.getParameter("taxTreeId"));
					if(oldNextNode!=null){
						oldNextNode.setPrevTaxon(thisTaxonHasTaxtree.getTaxonId());
						this.taxtreeService.saveOneTaxonHasTaxtree(oldNextNode);
					}
					TaxonHasTaxtree targetNode=this.taxtreeService.findOneTaxonHasTaxtreeByIds(request.getParameter("targetTaxonId"),request.getParameter("taxTreeId"));
					thisTaxonHasTaxtree.setPid(targetNode.getPid());
					thisTaxonHasTaxtree.setPrevTaxon(request.getParameter("targetTaxonId"));
					break;
				default:
					break;
			}
		}
		this.taxtreeService.addNodeAndAllChindren(request.getParameter("taxonId"),request.getParameter("taxTreeId"),request.getParameter("origTaxtreeId"));
		this.taxtreeService.saveOneTaxonHasTaxtree(thisTaxonHasTaxtree);
		return true;
	}
	/**
	 * <b> 修改一个在tree中的taxon</b>
	 * <p> 修改一个在tree中的taxon</p>
	 * @author WangTianshan (王天山)
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/moveOneTaxon", method = RequestMethod.GET)
	public boolean moveOneTaxon(HttpServletRequest request) {
		TaxonHasTaxtree thisTaxonHasTaxtree=this.taxtreeService.findOneTaxonHasTaxtreeByIds(request.getParameter("taxonId"),request.getParameter("taxTreeId"));
		if(request.getParameter("targetTaxonId").length()<=0) {
			thisTaxonHasTaxtree.setPid(request.getParameter("taxTreeId"));//无目标节点
			//查询最后一个节点
			TaxonHasTaxtree lastNode = this.taxtreeService.findLastNode(this.taxtreeService.findChildren(request.getParameter("taxTreeId"), request.getParameter("taxTreeId")));
			if (lastNode != null) {
				//有
				thisTaxonHasTaxtree.setPrevTaxon(lastNode.getTaxonId());
			} else
				thisTaxonHasTaxtree.setPrevTaxon(null);
		}
		else {//有目标节点
			//查找原有位置的下一个节点
			TaxonHasTaxtree oldNodeNext=this.taxtreeService.findNextNode(thisTaxonHasTaxtree.getTaxonId(),request.getParameter("taxTreeId"));
			//判断位置
			switch (request.getParameter("moveType")){
				case "inner":
					//处理原位置
					//查找原有位置的下一个节点
					if(oldNodeNext!=null){//有节点
						oldNodeNext.setPrevTaxon(thisTaxonHasTaxtree.getPrevTaxon());
						this.taxtreeService.saveOneTaxonHasTaxtree(oldNodeNext);
					}
					thisTaxonHasTaxtree.setPid(request.getParameter("targetTaxonId"));
					//判断是否有子节点
					List<TaxonHasTaxtree> chindrenNodes=this.taxtreeService.findChildren(request.getParameter("targetTaxonId"),request.getParameter("taxTreeId"));
					if(chindrenNodes.size()>0){
						//有子节点
						thisTaxonHasTaxtree.setPrevTaxon(this.taxtreeService.findLastNode(chindrenNodes).getTaxonId());
					}
					else
						thisTaxonHasTaxtree.setPrevTaxon(null);
					break;
				case "prev":
					//处理原位置
					if(oldNodeNext!=null){//有节点
						oldNodeNext.setPrevTaxon(thisTaxonHasTaxtree.getPrevTaxon());
						this.taxtreeService.saveOneTaxonHasTaxtree(oldNodeNext);
					}
					TaxonHasTaxtree prevNode=this.taxtreeService.findOneTaxonHasTaxtreeByIds(request.getParameter("targetTaxonId"),request.getParameter("taxTreeId"));
					thisTaxonHasTaxtree.setPid(prevNode.getPid());
					//判断是否被置为首节点
					if(prevNode.getPrevTaxon()==null){
						//被置为首节点
						thisTaxonHasTaxtree.setPrevTaxon(null);
					}
					else{
						thisTaxonHasTaxtree.setPrevTaxon(prevNode.getPrevTaxon());
					}
					prevNode.setPrevTaxon(thisTaxonHasTaxtree.getTaxonId());
					this.taxtreeService.hasTaxonHasTaxtree(prevNode);
					break;
				case "next":
					//处理原位置
					if(oldNodeNext!=null){//有节点
						oldNodeNext.setPrevTaxon(thisTaxonHasTaxtree.getPrevTaxon());
						this.taxtreeService.saveOneTaxonHasTaxtree(oldNodeNext);
					}
					//查找原来排在后面的
					//判断是否有子节点
					TaxonHasTaxtree oldNextNode=this.taxtreeService.findNextNode(request.getParameter("targetTaxonId"),request.getParameter("taxTreeId"));
					if(oldNextNode!=null){
						oldNextNode.setPrevTaxon(thisTaxonHasTaxtree.getTaxonId());
						this.taxtreeService.saveOneTaxonHasTaxtree(oldNextNode);
					}
					TaxonHasTaxtree targetNode=this.taxtreeService.findOneTaxonHasTaxtreeByIds(request.getParameter("targetTaxonId"),request.getParameter("taxTreeId"));
					thisTaxonHasTaxtree.setPid(targetNode.getPid());
					thisTaxonHasTaxtree.setPrevTaxon(request.getParameter("targetTaxonId"));
					break;
				default:
					break;
			}
		}
		this.taxtreeService.saveOneTaxonHasTaxtree(thisTaxonHasTaxtree);
		return true;
	}
	/**
	 * <b> 判断tree里是否有目标taxon</b>
	 * <p> 判断tree里是否有目标taxon</p>
	 * @author WangTianshan (王天山)
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/hasThisTaxon", method = RequestMethod.GET)
	public boolean hasThisTaxon(HttpServletRequest request) {
		TaxonHasTaxtree thisTaxonHasTaxtree=new TaxonHasTaxtree();
		thisTaxonHasTaxtree.setTaxtreeId(request.getParameter("taxTreeId"));
		thisTaxonHasTaxtree.setTaxonId(request.getParameter("taxonId"));
		return this.taxtreeService.hasTaxonHasTaxtree(thisTaxonHasTaxtree);
	}

	/**
	 *<b>浏览Taxtree时根据父节点检索子节点</b>
	 *<p> 浏览Taxtree时根据父节点检索子节点</p>
	 * @author WangTianshan (王天山)
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value = "/showChildren", method = RequestMethod.GET)
	public JSON showChildren(HttpServletRequest request){
		//获取父级阶元id
		String pid=request.getParameter("id");
		//有父级阶元
		if(pid == null){
			pid=request.getParameter("taxtreeId");
		}
		/*this.taxtreeService.updatePreTaxonByOrderNum(pid, request.getParameter("taxtreeId"));*/
		return this.taxtreeService.showChildren(pid, request.getParameter("taxtreeId"));
	}

	/**
	 *<b>删除单一节点</b>
	 *<p> 删除单一节点</p>
	 * @author WangTianshan (王天山)
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value = "/removeOneNode", method = RequestMethod.POST)
	public boolean removeOneNode(HttpServletRequest request){
		return this.taxtreeService.removeOneNode(request.getParameter("taxonId"),request.getParameter("taxtreeId"));
	}

	/**
	 *<b>删除节点及子节点</b>
	 *<p> 删除单一节点</p>
	 * @author WangTianshan (王天山)
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value = "/removeNodeAndAllChindren", method = RequestMethod.POST)
	public boolean removeNodeAndAllChindren(HttpServletRequest request){
		//处理节点顺序
		//获取当前节点
		TaxonHasTaxtree thisNode=this.taxtreeService.findOneTaxonHasTaxtreeByIds(request.getParameter("taxonId"),request.getParameter("taxtreeId"));
		//获得后一个节点
		TaxonHasTaxtree nextNode=this.taxtreeService.findNextNode(request.getParameter("taxonId"),request.getParameter("taxtreeId"));
		//判断是否有后节点
		if(nextNode!=null){
			//判断是否有是首节点
			if(thisNode.getPrevTaxon()!=null){
				//获取前一个节点
				TaxonHasTaxtree prevNode=this.taxtreeService.findOneTaxonHasTaxtreeByIds(thisNode.getPrevTaxon(),request.getParameter("taxtreeId"));
				nextNode.setPrevTaxon(prevNode.getTaxonId());
			}
			else
				nextNode.setPrevTaxon(null);
			this.taxtreeService.saveOneTaxonHasTaxtree(nextNode);
		}
		return this.taxtreeService.removeNodeAndAllChindren(request.getParameter("taxonId"),request.getParameter("taxtreeId"));
	}
	
	/**
	 *<b>根据DatasetId查找所属Taxtree（分页）</b>
	 *<p> 根据DatasetId查找所属Taxtree（分页）</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @return
	 */
	@GetMapping(value = "/taxtreeList/{dsid}")
	public JSON taxtreeList(Model model, @PathVariable String dsid, HttpServletRequest request) {
		return this.taxtreeService.findTaxtreeListByDatasetId(dsid, request);
	}
	
	/**
     * <b>是否有删除Taxtree权限</b>
     * <p> 是否有删除Taxtree权限</p>
	 * @author BINZI
	 * @param request
	 * @param id
	 * @return
	 */
    @GetMapping("/canRemoveObj/{id}")
    public JSON canRemoveObj(HttpServletRequest request, @PathVariable String id) {
    	JSONObject thisResult = new JSONObject();
    	try {
    		Dataset thisDataset = this.taxtreeService.findOneById(id).getDataset();
    		String leader = thisDataset.getTeam().getLeader();
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
     *<b>Title重复</b>
     *<p> Title重复</p>
     * @author BINZI
     * @param request 页面请求
     * @return Boolean
     */
    @RequestMapping(value="/isReDsname", method = {RequestMethod.GET})
    public Boolean isReTitle(HttpServletRequest request) {
    	return taxtreeService.countTaxtreesByTreename(request);
    }
    /**
     *<b>根据新建节点信息查询其tree结构</b>
     *<p> 根据新建节点信息查询其tree结构</p>
     * @author BINZI
     * @param request 页面请求
     * @return 
     */
    @GetMapping(value="/taxaTreeChildren")
    public JSON taxaTreeChildren(HttpServletRequest request) {
    	return taxtreeService.findTaxaTreeChildren(request);
	}
    /**
     *<b>根据修改节点信息查询其tree结构，刷新tree</b>
     *<p> 根据修改节点信息查询其tree结构，刷新tree</p>
     * @author BINZI
     * @param request 页面请求
     * @return 
     */
    @GetMapping(value="/parentTreeNodesId")
    public JSON parentTreeNodesId(HttpServletRequest request) {
    	return taxtreeService.findParentTreeNodesId(request);
    }
    /**
     *<b>根据修改节点信息查询其tree结构，刷新tree</b>
     *<p> 根据修改节点信息查询其tree结构，刷新tree</p>
     * @author BINZI
     * @param request 页面请求
     * @return 
     */
    @GetMapping(value="/findTreeNode/{treeId}")
    public JSON findTreeNode(@PathVariable String treeId, HttpServletRequest request) {
    	return taxtreeService.findTreeNodeByTreeId(treeId, request);
    }
    
    
}
