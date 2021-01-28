package org.big.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.big.common.BuildEntity;
import org.big.common.QueryTool;
import org.big.common.UUIDUtils;
import org.big.entity.Taxon;
import org.big.entity.TaxonHasTaxtree;
import org.big.entity.Taxtree;
import org.big.entity.UserDetail;
import org.big.repository.TaxonHasTaxtreeRepository;
import org.big.repository.TaxonRepository;
import org.big.repository.TaxtreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class TaxtreeServiceImpl implements TaxtreeService {
	@Autowired
	private TaxtreeRepository taxtreeRepository;
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private DatasetService datasetService;
	@Autowired
	private TaxonHasTaxtreeRepository taxonHasTaxtreeRepository;
	
	@Override
	public void saveOne(Taxtree thisTaxtree) {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		thisTaxtree.setId(UUIDUtils.getUUID32());
		thisTaxtree.setInputer(thisUser.getId());
		thisTaxtree.setInputtime(new Timestamp(System.currentTimeMillis()));
		thisTaxtree.setStatus(1);
		thisTaxtree.setSynchdate(new Timestamp(System.currentTimeMillis()));
		thisTaxtree.setSynchstatus(0);
		thisTaxtree.setBgurl("/img/dataset-bg.jpg");
		this.taxtreeRepository.save(thisTaxtree);
	}

	@Override
	public void removeOne(String Id) {
		this.taxtreeRepository.deleteOneById(Id);
	}

	@Override
	public boolean logicRemove(String id) {
		Taxtree thisTaxtree = this.taxtreeRepository.findOneById(id);
		if (null != thisTaxtree && 1 == thisTaxtree.getStatus()) {
			thisTaxtree.setStatus(0);
			this.taxtreeRepository.save(thisTaxtree);
			return true;
		}
		return false;
	}
	
	@Override
	public void updateOneById(Taxtree thisTaxtree) {
		thisTaxtree.setSynchdate(new Timestamp(System.currentTimeMillis()));
		this.taxtreeRepository.save(thisTaxtree);
	}

	@Override
	public Taxtree findOneById(String Id) {
		return this.taxtreeRepository.findOneById(Id);
	}

	@Override
	public JSON findTaxtreeList(HttpServletRequest request) {
		String dsId = (String) request.getSession().getAttribute("datasetID");
		JSON json = null;
		String searchText = request.getParameter("search");
		if (searchText == null || searchText.length() <= 0) {
			searchText = "";
		}
		int limit_serch = Integer.parseInt(request.getParameter("limit"));
		int offset_serch = Integer.parseInt(request.getParameter("offset"));
		String sort=request.getParameter("sort");
		String order=request.getParameter("order");
		if(StringUtils.isBlank(sort)){
			sort = "synchdate";
			order = "asc";
		}
		
		JSONObject thisTable = new JSONObject();
		JSONArray rows = new JSONArray();
		List<Taxtree> thisList = new ArrayList<>();
		Page<Taxtree> thisPage = this.taxtreeRepository.searchInfo(searchText, dsId,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		String thisSelect = "";
		String thisEdit = "";
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
	        thisSelect="<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
	        thisEdit=
	        	 "<a class=\"wts-table-edit-icon\" onclick=\"editThisObject('" + thisList.get(i).getId() + "','taxtree')\" >" +
	             "<span class=\"glyphicon glyphicon-edit\"></span>" +
	             "</a> &nbsp;&nbsp;&nbsp;" +
	             "<a class=\"wts-table-edit-icon\" onclick=\"removeThisObject('" + thisList.get(i).getId() + "','taxtree')\" >" +
	             "<span class=\"glyphicon glyphicon-remove\"></span>" +
	             "</a>";
			row.put("select", thisSelect);
			row.put("treename", "<a href=\"javascript:manageTaxtree('" + thisList.get(i).getId() + "')\">" + thisList.get(i).getTreename() + "</a>");
			//row.put("treename", "<a href=\"console/taxtree/show/" + thisList.get(i).getId() + "\">" + thisList.get(i).getTreename() + "</a>");
			row.put("treeinfo", thisList.get(i).getTreeinfo());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String addTime = "";
			String editTime = "";
			try {
				addTime = formatter.format(thisList.get(i).getInputtime());
				editTime = formatter.format(thisList.get(i).getSynchdate());
			} catch (Exception e) {
			}
			row.put("inputtime", addTime);
			row.put("synchdate", editTime);
			row.put("edit", thisEdit);
			rows.add(i, row);
		}
		thisTable.put("rows", rows);
		json = thisTable;
		return json;
    }

	@Override
	public JSON findBySelect(HttpServletRequest request) {
		String findText = request.getParameter("find");
		if (findText == null || findText.length() <= 0) {
			findText = "";
		}
		int findPage = 1;
		try {
			findPage = Integer.valueOf(request.getParameter("page"));
		} catch (Exception e) {
		}
		int limit_serch = 30;
		int offset_serch = (findPage - 1) * 30;
		String sort = "treename";
		String order = "asc";
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		List<Taxtree> thisList = new ArrayList<>();
		// 获取当前选中Dataset下的Taxtree
		String dsId = (String) request.getSession().getAttribute("datasetID");
		Page<Taxtree> thisPage = this.taxtreeRepository.searchByTreename(findText, dsId,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisSelect.put("total_count", thisPage.getTotalElements());
		Boolean incompleteResulte = true;
		if ((thisPage.getTotalElements() / 30) > findPage) {
			incompleteResulte = false;
		}
		thisSelect.put("incompleteResulte", incompleteResulte);
		thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			row.put("id", thisList.get(i).getId());
			row.put("text", thisList.get(i).getTreename());
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public JSON findBySelectAndNew(HttpServletRequest request) {
		String findText = request.getParameter("find");
		if (findText == null || findText.length() <= 0) {
			findText = "";
		}
		int findPage = 1;
		try {
			findPage = Integer.valueOf(request.getParameter("page"));
		} catch (Exception e) {
		}
		int limit_serch = 30;
		int offset_serch = (findPage - 1) * 30;
		String sort = "treename";
		String order = "asc";
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		List<Taxtree> thisList = new ArrayList<>();
		// 获取当前选中Dataset下的Taxtree
		String dsId = (String) request.getSession().getAttribute("datasetID");
		Page<Taxtree> thisPage = this.taxtreeRepository.searchByTreename(findText, dsId,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisSelect.put("total_count", thisPage.getTotalElements());
		Boolean incompleteResulte = true;
		if ((thisPage.getTotalElements() / 30) > findPage) {
			incompleteResulte = false;
		}
		thisSelect.put("incompleteResulte", incompleteResulte);
		thisList = thisPage.getContent();
		if (findPage == 1) {
			JSONObject row = new JSONObject();
			row.put("id", "addNew");
			row.put("text", "新建一个分类树");
			items.add(row);
		}
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			row.put("id", thisList.get(i).getId());
			row.put("text", thisList.get(i).getTreename());
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public JSON newOne(Taxtree thisTaxtree, HttpServletRequest request) {
		JSONObject thisResult = new JSONObject();
		try {
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			thisTaxtree.setInputer(thisUser.getId());
			thisTaxtree.setInputtime(new Timestamp(System.currentTimeMillis()));
			thisTaxtree.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisTaxtree.setStatus(1);
			thisTaxtree.setSynchstatus(0);
			String id = UUIDUtils.getUUID32();
			thisTaxtree.setId(id);
			thisTaxtree.setBgurl("/img/dataset-bg.jpg");
			// 获取当前选中Dataset
			String dsid = (String) request.getSession().getAttribute("datasetID");
			thisTaxtree.setDataset(datasetService.findbyID(dsid));
			this.taxtreeRepository.save(thisTaxtree);

			thisResult.put("result", true);
			thisResult.put("newId", this.taxtreeRepository.findOneById(id).getId());
			thisResult.put("newTreename", this.taxtreeRepository.findOneById(id).getTreename());
			thisResult.put("dsid", dsid);
		} catch (Exception e) {
			thisResult.put("result", false);
		}
		return thisResult;
	}

	@Override
	public void saveOneTaxonHasTaxtree(TaxonHasTaxtree thisTaxonHasTaxtree) {
		this.taxonHasTaxtreeRepository.save(thisTaxonHasTaxtree);
	}

	@Override
	public TaxonHasTaxtree findOneTaxonHasTaxtree(TaxonHasTaxtree thisTaxonHasTaxtree) {
		return this.taxonHasTaxtreeRepository.findOneByIds(thisTaxonHasTaxtree.getTaxonId(),thisTaxonHasTaxtree.getTaxtreeId());
	}

	@Override
	public boolean hasTaxonHasTaxtree(TaxonHasTaxtree thisTaxonHasTaxtree) {
		if(this.taxonHasTaxtreeRepository.findOneByIds(thisTaxonHasTaxtree.getTaxonId(),thisTaxonHasTaxtree.getTaxtreeId())!=null)
			return true;
		else
			return false;
	}

	@Override
	public JSONArray showChildren(String taxonId,String taxtreeId) {
		JSONArray thisArray=new JSONArray();
		List<TaxonHasTaxtree> thisList = this.taxonHasTaxtreeRepository.findTaxonHasTaxtreesByPidAndAndTaxtreeId(taxonId,taxtreeId);
		try{
			List<TaxonHasTaxtree> sortList=this.sortTaxonHasTaxtree(thisList);
			if(sortList.size()==thisList.size()){
				thisList=sortList;
			}
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("排序失败");
		}
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject thisTaxonHasTaxtree = new JSONObject();
			Taxon thisTaxon=this.taxonRepository.findOneById(thisList.get(i).getTaxonId());
			thisTaxonHasTaxtree.put("id", thisTaxon.getId());
			thisTaxonHasTaxtree.put("name", thisTaxon.getScientificname());
			if(StringUtils.isBlank(thisTaxon.getChname()))
				thisTaxonHasTaxtree.put("name", thisTaxon.getScientificname());
			else
				thisTaxonHasTaxtree.put("name", thisTaxon.getScientificname()+" "+thisTaxon.getChname());
			thisTaxonHasTaxtree.put("title", "点击查看详情");
			if(this.taxonHasTaxtreeRepository.countTaxonHasTaxtreesByPidAndTaxtreeId(thisTaxon.getId(),taxtreeId)>0)
				thisTaxonHasTaxtree.put("isParent", true);
			else
				thisTaxonHasTaxtree.put("isParent", false);
			thisTaxonHasTaxtree.put("url", null);
			thisTaxonHasTaxtree.put("click", "showTaxon('"+thisTaxon.getId()+"')");
			thisArray.add(thisTaxonHasTaxtree);
		}
		return thisArray;
	}

	@Override
	public List<TaxonHasTaxtree> findChildren(String taxonId,String taxtreeId) {
		return this.taxonHasTaxtreeRepository.findTaxonHasTaxtreesByPidAndAndTaxtreeId(taxonId,taxtreeId);
		//return this.taxonHasTaxtreeRepository.findChildren(taxonId,taxtreeId);
	}

	@Override
	public boolean removeOneNode(String taxonId,String taxtreeId){
		try {
			this.taxonHasTaxtreeRepository.removeByTaxonIdAndTaxtreeId(taxonId,taxtreeId);
			return true;
		}
		catch (Exception e){
			return false;
		}
	}

	@Override
	public boolean removeChindren(String taxonId,String taxtreeId){
		try {
			this.taxonHasTaxtreeRepository.removeByPidAndTaxtreeId(taxonId,taxtreeId);
			return true;
		}
		catch (Exception e){
			return false;
		}
	}

	@Override
	public boolean removeNodeAndChindren(String taxonId,String taxtreeId){
		try {
			this.taxonHasTaxtreeRepository.removeByTaxonIdAndTaxtreeId(taxonId,taxtreeId);
			this.taxonHasTaxtreeRepository.removeByPidAndTaxtreeId(taxonId,taxtreeId);
			return true;
		}
		catch (Exception e){
			return false;
		}
	}

	@Override
	public boolean removeNodeAndAllChindren(String taxonId,String taxtreeId){
		try {
			//查所有子节点
			List<TaxonHasTaxtree> thisList = this.taxonHasTaxtreeRepository.findTaxonHasTaxtreesByPidAndAndTaxtreeId(taxonId,taxtreeId);
			//根据id删除自己和所有一级子节点
			removeNodeAndChindren(taxonId,taxtreeId);
			for (int i = 0; i < thisList.size(); i++) {
				removeNodeAndAllChindren(thisList.get(i).getTaxonId(),taxtreeId);
			}
			return true;
		}
		catch (Exception e){
			return false;
		}
	}

	@Override
	public boolean addNodeAndAllChindren(String taxonId,String taxtreeId,String origTaxtreeId){
		try {
			//查所有子节点
			List<TaxonHasTaxtree> thisList = this.taxonHasTaxtreeRepository.findTaxonHasTaxtreesByPidAndAndTaxtreeId(taxonId,origTaxtreeId);
			//保存自己
			TaxonHasTaxtree origTaxonHasTaxtree=this.taxonHasTaxtreeRepository.findOneByIds(taxonId,origTaxtreeId);
			TaxonHasTaxtree thisTaxonHasTaxtree=new TaxonHasTaxtree();
			thisTaxonHasTaxtree.setTaxtreeId(taxtreeId);
			//thisTaxonHasTaxtree.setTreeSort(origTaxonHasTaxtree.getTreeSort());
			thisTaxonHasTaxtree.setPid(origTaxonHasTaxtree.getPid());
			thisTaxonHasTaxtree.setTaxonId(origTaxonHasTaxtree.getTaxonId());
			thisTaxonHasTaxtree.setPrevTaxon(origTaxonHasTaxtree.getPrevTaxon());
			this.taxonHasTaxtreeRepository.save(thisTaxonHasTaxtree);
			for (int i = 0; i < thisList.size(); i++) {
				addNodeAndAllChindren(thisList.get(i).getTaxonId(),taxtreeId,thisList.get(i).getTaxtreeId());
			}
			return true;
		}
		catch (Exception e){
			return false;
		}
	}

	@Override
	public TaxonHasTaxtree findOneTaxonHasTaxtreeByIds(String taxonId,String taxtreeId) {
		return this.taxonHasTaxtreeRepository.findOneByIds(taxonId,taxtreeId);
	}

	@Override
	public List<TaxonHasTaxtree> sortTaxonHasTaxtree(List<TaxonHasTaxtree> nodeList) {
		int size = nodeList.size();
		List<TaxonHasTaxtree> sortlist = new LinkedList<>();
		Iterator<TaxonHasTaxtree> iterator = nodeList.iterator();
		while (iterator.hasNext()) {
			TaxonHasTaxtree taxonHasTaxtree = iterator.next();
			String prevTaxon = taxonHasTaxtree.getPrevTaxon();
			int index = sortlist.size();
			for (int i = 0; i < sortlist.size(); i++) {
				TaxonHasTaxtree sortNode = sortlist.get(i);
				if (sortNode.getTaxonId().equals(prevTaxon)) {
					index = i+1;
					break;
				}
			}
			// 插入节点到sortlist
			sortlist.add(index, taxonHasTaxtree);
			// 从nodeList移除节点
			iterator.remove();
		}
		sortlist.addAll(nodeList);
		if(size != sortlist.size()) {
			throw new ValidationException("排序后节点缺失");
		}
		return sortlist;
	}


	@Override
	public TaxonHasTaxtree findNextNode(List<TaxonHasTaxtree> nodeList,String thisNodeId) {
		TaxonHasTaxtree nextNode=new TaxonHasTaxtree();
		for (int i = 0; i < nodeList.size(); i++) {
			if(thisNodeId.equals(nodeList.get(i).getPrevTaxon())){
				nextNode=nodeList.get(i);
				break;
			}
		}
		return nextNode;
	}

	@Override
	public TaxonHasTaxtree findHeadNode(List<TaxonHasTaxtree> nodeList) {
		TaxonHasTaxtree nextNode=new TaxonHasTaxtree();
		for (int i = 0; i < nodeList.size(); i++) {
			if(nodeList.get(i).getPrevTaxon()==null){
				nextNode=nodeList.get(i);
				break;
			}
		}
		return nextNode;
	}

	@Override
	public TaxonHasTaxtree findLastNode(List<TaxonHasTaxtree> nodeList) {
		List<TaxonHasTaxtree> sortList=this.sortTaxonHasTaxtree(nodeList);
		if(sortList.size()>0)
			return sortList.get(sortList.size() - 1);
		else
			return null;
	}

	@Override
	public int countChindren(String taxonId,String taxtreeId) {
		return this.taxonHasTaxtreeRepository.countTaxonHasTaxtreesByPidAndTaxtreeId(taxonId,taxtreeId);
	}

	@Override
	public TaxonHasTaxtree findNextNode(String taxonId,String taxtreeId) {
		return this.taxonHasTaxtreeRepository.findTaxonHasTaxtreesByPrevTaxonAndTaxtreeId(taxonId,taxtreeId);
	}

	@Override
	public JSON findTaxtreeListByDatasetId(String dsid, HttpServletRequest request) {
		int limit_serch = Integer.parseInt(request.getParameter("limit"));
		int offset_serch = Integer.parseInt(request.getParameter("offset"));
		String sort = "synchdate";
		String order = "desc";
		
		JSONObject thisSelect = new JSONObject();
		String searchText = "";
		JSONArray rows = new JSONArray();
		Page<Taxtree> thisPage = this.taxtreeRepository.searchInfo(searchText, dsid,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		List<Taxtree> thisList = new ArrayList<>();
		thisList = thisPage.getContent();
		thisSelect.put("total", thisPage.getTotalElements());
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			Taxtree thisTaxtree = thisList.get(i);
			row.put("id", thisTaxtree.getId());
			row.put("treename", thisTaxtree.getTreename());
			row.put("treeinfo", thisTaxtree.getTreeinfo());
			row.put("bgurl", thisTaxtree.getBgurl());
			rows.add(i, row);
		}
		thisSelect.put("total", thisPage.getTotalElements());		// NO1：总数
		thisSelect.put("page", offset_serch);						// NO2：offset
		thisSelect.put("rows", rows);								// NO3：Taxon下的所有Dataset
		return thisSelect;
	}

	@Override
	public Boolean countTaxtreesByTreename(HttpServletRequest request) {
		String treename = request.getParameter("name");
		String datasetId = (String) request.getSession().getAttribute("datasetID");
		long num = 0;
		if (StringUtils.isNotBlank(treename)) {
			num = this.taxtreeRepository.countTaxtreesByTreename(treename, datasetId);
		}
		if (num > 0) {
			return false;
		}else {
			return true;
		}
	}

	@Override
	public JSON findTaxaTreeChildren(HttpServletRequest request) {
		String taxTreeId = request.getParameter("taxTreeId");
		String taxonId = request.getParameter("taxonId");
		String targetTaxonId = request.getParameter("targetTaxonId");
		if (StringUtils.isNotBlank(taxonId) && StringUtils.isNotBlank(taxTreeId)) {
			TaxonHasTaxtree thisFirstNode = this.taxonHasTaxtreeRepository.findOneByIds(taxonId, taxTreeId);
			System.out.println(thisFirstNode.getPid() + "\t" + targetTaxonId);
		}
		return null;
	}

	@Override
	public JSON findParentTreeNodesId(HttpServletRequest request) {
		JSONObject thisResult = new JSONObject();
		String targetTaxonId = request.getParameter("targetTaxonId");
		String treeId = request.getParameter("taxaTreeId");
		int num = 0;
		// 父级节点
		JSONArray nodesArr = new JSONArray();
		JSONArray nodeArr = hasPNode(thisResult, nodesArr, treeId, targetTaxonId, num);
		thisResult.put("nodeArr", nodeArr);
		return thisResult;

	}
	
	private JSONArray hasPNode(JSONObject thisResult, JSONArray nodesArr, String treeId, String targetTaxonId, int num) {
		TaxonHasTaxtree thisTaxonHasTaxtree = null;
		if (StringUtils.isNotBlank(targetTaxonId)) {
			thisTaxonHasTaxtree = this.taxonHasTaxtreeRepository.findOneByIds(targetTaxonId, treeId);
			if (thisTaxonHasTaxtree != null && StringUtils.isNotBlank(thisTaxonHasTaxtree.getPid())) {
				targetTaxonId = thisTaxonHasTaxtree.getPid();
				nodesArr.add(num, targetTaxonId);
				thisResult.put("num", ++num);
				hasPNode(thisResult, nodesArr, treeId, targetTaxonId, num);
			}
		}
		return nodesArr;
	}

	@Override
	public JSON findTreeNodeByTreeId(String treeId, HttpServletRequest request) {
		String findText = request.getParameter("find");
		if (findText == null || findText.length() <= 0) {
			findText = "";
		}
		int findPage = 1;
		try {
			findPage = Integer.valueOf(request.getParameter("page"));
		} catch (Exception e) {
		}
		int limit_serch = 30;
		int offset_serch = (findPage - 1) * 30;
		String sort = "scientificname";
		String order = "desc";
		
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		
		Page<Object> thisPage = this.taxtreeRepository.findTreeNodeByTreeId(findText, treeId,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		List<Object> thisList = new ArrayList<>();
		
		thisList = thisPage.getContent();
		thisSelect.put("total_count", thisPage.getTotalElements());
		Boolean incompleteResulte = true;
		if ((thisPage.getTotalElements() / 30) > findPage) {
			incompleteResulte = false;
		}
		thisSelect.put("incompleteResulte", incompleteResulte);
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			Taxon thisTaxon = BuildEntity.buildTaxon(thisList.get(i));
			row.put("id", thisTaxon.getId());
			row.put("text", thisTaxon.getScientificname() + " " + thisTaxon.getChname());
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public void updatePreTaxonByOrderNum(String pid, String taxtreeId) {
		// 查询所有根节点
		List<TaxonHasTaxtree> list = taxonHasTaxtreeRepository.findRootNodeByTaxtreeId(taxtreeId, taxtreeId);
		if (list.isEmpty()) {
			return;
		}
		System.out.println("集合：" + JSON.toJSONString(list));
		// 更新根节点实体数据（根据taxon.OrderNum更新taxonHasTaxtree.PreTaxon）
		List<TaxonHasTaxtree> updateList = updateTaxonHasTaxtree(list);
		// 保存数据库
		/*batchInsertService.updateTaxonHasTaxtree(updateList);*/
		this.taxonHasTaxtreeRepository.saveAll(updateList);
		for (TaxonHasTaxtree taxonHasTaxtree : list) {
			// 分层获取孩子节点并更新到数据库
			getChildNodesAndUpdatePreTaxon(taxonHasTaxtree.getTaxonId(), taxtreeId);
		}
	}
	
	/**
	 * 
	 * @Description 根据taxon.OrderNum更新taxonHasTaxtree.PreTaxon
	 * @param list
	 * @author ZXY
	 */
	private List<TaxonHasTaxtree> updateTaxonHasTaxtree(List<TaxonHasTaxtree> list) {
		List<String> taxonIdList = new ArrayList<>();
		Map<String,TaxonHasTaxtree> map = new HashMap<>();
		for (TaxonHasTaxtree taxonHasTaxtree : list) {
			taxonIdList.add(taxonHasTaxtree.getTaxonId());
			map.put(taxonHasTaxtree.getTaxonId(), taxonHasTaxtree);
		}
		//从小到大排序
		List<String> orderList = taxonRepository.findIdByOrderNum(taxonIdList);
		for (int i = 0; i < orderList.size(); i++) {
			String taxonId = orderList.get(i);
			TaxonHasTaxtree taxonHasTaxtree = map.get(taxonId);
			if (i == 0) {
				//第一个,preTaxon设为null
				taxonHasTaxtree.setPrevTaxon(null);
			}else {
				String prevTaxonId = orderList.get(i-1);
				taxonHasTaxtree.setPrevTaxon(prevTaxonId);
			}
		}
		return list;
	}
	
	public void getChildNodesAndUpdatePreTaxon(String taxonId, String taxtreeId) {
		// 查询孩子节点
		List<TaxonHasTaxtree> childrenNode = taxonHasTaxtreeRepository.findTaxonHasTaxtreesByPidAndAndTaxtreeId(taxonId,
				taxtreeId);
		//查询结果为空，返回
		if(childrenNode.isEmpty()) {
			return;
		}
		List<TaxonHasTaxtree> updateList = this.updateTaxonHasTaxtree(childrenNode);
		// 保存数据库
		/*batchInsertService.updateTaxonHasTaxtree(updateList);*/
		this.taxonHasTaxtreeRepository.saveAll(updateList);
		for (TaxonHasTaxtree taxonHasTaxtree : childrenNode) {
			this.getChildNodesAndUpdatePreTaxon(taxonHasTaxtree.getTaxonId(), taxtreeId);
		}
		return;
	}
}
