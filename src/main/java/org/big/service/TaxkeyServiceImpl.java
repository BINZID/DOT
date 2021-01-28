package org.big.service;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.big.common.QueryTool;
import org.big.entity.Keyitem;
import org.big.entity.Taxkey;
import org.big.repository.KeyitemRepository;
import org.big.repository.ResourceRepository;
import org.big.repository.TaxkeyRepository;
import org.big.repository.TaxonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class TaxkeyServiceImpl implements TaxkeyService {
	@Autowired
	private TaxkeyRepository taxkeyRepository;
	@Autowired
	private TaxonRepository taxonRespository;
	@Autowired
	private KeyitemRepository keyitemRepository;
	@Autowired
	private KeyitemService keyitemService;
	@Autowired
	private ResourceRepository resourceRepository;
	
	@Override
	public JSON findTaxkeyList(HttpServletRequest request) {
		JSON json = null;
		String searchText = request.getParameter("search");
		if (searchText == null || searchText.length() <= 0) {
			searchText = "";
		}
		int limit_serch = Integer.parseInt(request.getParameter("limit"));
		int offset_serch = Integer.parseInt(request.getParameter("offset"));
		
		String sort = "keytitle";
		String order = "desc";
		sort = request.getParameter("sort");
		order = request.getParameter("order");
		if (StringUtils.isBlank(sort)) {
			sort = "keytitle";
		}
		if (StringUtils.isBlank(order)) {
			order = "desc";
		}
	
		JSONObject thisTable = new JSONObject();
		JSONArray rows = new JSONArray();
		List<Taxkey> thisList = new ArrayList<>();
		Page<Taxkey> thisPage = this.taxkeyRepository.searchInfo(searchText, QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		String thisSelect = "";
		String thisEdit = "";
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
	        thisSelect="<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
	        thisEdit=
	        	 "<a class=\"wts-table-edit-icon\" onclick=\"editThisObject('" + thisList.get(i).getId() + "','taxkey')\" >" +
	             "<span class=\"glyphicon glyphicon-edit\"></span>" +
	             "</a> &nbsp;&nbsp;&nbsp;" +
	             "<a class=\"wts-table-edit-icon\" onclick=\"removeThisObject('" + thisList.get(i).getId() + "','taxkey')\" >" +
	             "<span class=\"glyphicon glyphicon-remove\"></span>" +
	             "</a>";
			row.put("select", thisSelect);
			row.put("keytitle", "<a href=\"console/description/show/" + thisList.get(i).getId() + "\">" + thisList.get(i).getKeytitle() + "</a>");
			row.put("abstraction", thisList.get(i).getAbstraction());
			row.put("edit", thisEdit);
			rows.add(i, row);
		}
		thisTable.put("rows", rows);
		json = thisTable;
		return json;
    }

	@Override
	public JSON addTaxkey(String taxonId, HttpServletRequest request) {
		Taxkey thisTaxkey = new Taxkey();
		Enumeration<String> paraNames = request.getParameterNames();
		String paraName = null;
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("taxkeyId_") == 0) {
				thisTaxkey.setId(request.getParameter(paraName));
				request.getSession().setAttribute("taxkeyId", thisTaxkey.getId());
			}
			if (paraName.indexOf("abstraction_") == 0) {
				thisTaxkey.setAbstraction(request.getParameter(paraName));
			}
			if (paraName.indexOf("keytitle_") == 0) {
				thisTaxkey.setKeytitle(request.getParameter(paraName));
			}
			if (paraName.indexOf("keytype_") == 0) {
				thisTaxkey.setKeytype(request.getParameter(paraName));
			}
		}
		
		JSONObject thisResult = new JSONObject();
		try {
			thisTaxkey.setTaxon(taxonRespository.findOneById(taxonId));
			
			this.taxkeyRepository.save(thisTaxkey);
			
			thisResult.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			thisResult.put("result", false);
		}
		return thisResult;
}
	
	@Override
	public JSON addTaxkeyForKeyitem(HttpServletRequest request, String taxonId) {
		Taxkey thisTaxkey = new Taxkey();
		
		String taxkeyId = request.getParameter("taxkeyId");
		String abstraction = request.getParameter("abstraction");
		String keytitle = request.getParameter("keytitle");
		String keytype = request.getParameter("keytype");
		if (StringUtils.isNotBlank(taxkeyId) && StringUtils.isNotBlank(keytype)) {
			request.getSession().setAttribute("taxkeyId", taxkeyId);
			thisTaxkey.setId(taxkeyId);
			thisTaxkey.setAbstraction(abstraction);
			thisTaxkey.setKeytitle(keytitle);
			thisTaxkey.setKeytype(keytype);
		}
		JSONObject thisResult = new JSONObject();
		try {
			thisTaxkey.setTaxon(this.taxonRespository.findOneById(taxonId));
			this.taxkeyRepository.save(thisTaxkey);
			
			thisResult.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			thisResult.put("result", false);
		}
		return thisResult;
	}
	
	@Override
	public JSON addKeyitem(String taxonId, Enumeration<String> paraNames, HttpServletRequest request, String taxkeyId, String keyitemId) {
		addTaxkey(taxonId, request);
		Taxkey thisTaxkey = this.taxkeyRepository.findOneById(taxkeyId);
		Keyitem thisKeyitem = new Keyitem();
		thisKeyitem.setId(keyitemId);
		thisKeyitem.setTaxkey(thisTaxkey);
		JSONObject thisResult = new JSONObject();
		while (paraNames.hasMoreElements()) {
			String paraName = (String) paraNames.nextElement();
			// ---------------------- KeyItem -------------------
			if (paraName.indexOf("item_") == 0) {
				thisKeyitem.setItem(request.getParameter(paraName));
			}
			if (paraName.indexOf("innerorder_") == 0) {
				if (StringUtils.isNotBlank(request.getParameter(paraName))) {
					thisKeyitem.setInnerorder(Integer.parseInt(request.getParameter(paraName)));
				}
			}
			if (paraName.indexOf("orderid_") == 0) {
				if (StringUtils.isNotBlank(request.getParameter(paraName))) {
					thisKeyitem.setOrderid(Integer.parseInt(request.getParameter(paraName)));
				}
			}
			if (paraName.indexOf("branchid_") == 0) {
				if (StringUtils.isNotBlank(request.getParameter(paraName))) {
					thisKeyitem.setBranchid(Integer.parseInt(request.getParameter(paraName)));
				}
			}
			if (paraName.indexOf("taxonid_") == 0) {
				thisKeyitem.setTaxonid(request.getParameter(paraName));
			}
			if (paraName.indexOf("keytype_") == 0) {
				thisKeyitem.setKeytype(request.getParameter(paraName));
			}
		}
		try {
			this.keyitemRepository.save(thisKeyitem);
			thisResult.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			thisResult.put("result", false);
		}
		return thisResult;
	}

	@Override
	public boolean deleteOneById(String id) {
		if (null != this.taxkeyRepository.findOneById(id)) {
			this.taxkeyRepository.deleteOneById(id);
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteOne(HttpServletRequest request) {
		String taxkeyId = request.getParameter("taxkeyId");
		if (StringUtils.isNotBlank(taxkeyId)) {
			if (null != this.taxkeyRepository.findOneById(taxkeyId)) {
				this.resourceRepository.delResourceBYTaxkeyId(taxkeyId);
				this.keyitemRepository.deleteByFk(taxkeyId);
				this.taxkeyRepository.deleteOneById(taxkeyId);
			}
			return true;
		}
		return false;
	}

	@Override
	public JSON findTaxkeyListByTaxonId(String taxonId, HttpServletRequest request) {
		int limit_serch = Integer.parseInt(request.getParameter("limit"));		// 1.limit 一次查询返回的个数，默认值10
		int offset_serch = Integer.parseInt(request.getParameter("offset"));	// 2.offset从第几个开始查，默认值0
		String sort = "keytitle";
		String order = "desc";
		sort = request.getParameter("sort");
		order = request.getParameter("order");
		if (StringUtils.isBlank(sort)) {
			sort = "keytitle";
		}
		if (StringUtils.isBlank(order)) {
			order = "desc";
		}

		JSONObject thisSelect = new JSONObject();
		Page<Taxkey> thisPage = this.taxkeyRepository.searchTaxkeysByTaxonId(
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), taxonId);
		
		JSONArray rows = new JSONArray();
		List<Taxkey> thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			rows.add(i, thisList.get(i));
		}
		
		thisSelect.put("total", thisPage.getTotalElements());		// NO1：总数
		thisSelect.put("page", offset_serch);						// NO2：offset
		thisSelect.put("rows", rows);								// NO3：Taxon下的所有Citation
		return thisSelect;
	}

	@Override
	public Taxkey findOneById(String taxkeyId) {
		return this.taxkeyRepository.findOneById(taxkeyId);
	}

	@Override
	public JSON editTaxkey(String taxonId) {
		JSONObject taxkeys = new JSONObject();
		JSONArray taxkeyArr = new JSONArray();
		if (StringUtils.isNotBlank(taxonId)) {
			List<Taxkey> list = this.taxkeyRepository.findTaxkeyListByTaxonId(taxonId);
			for (int i = 0; i < list.size(); i++) {
				JSONObject json = new JSONObject();
				json.put("id", list.get(i).getId());
				json.put("keytype", list.get(i).getKeytype());
				json.put("title", list.get(i).getKeytitle());
				json.put("abstraction", list.get(i).getAbstraction());
				json.put("keyitems", this.keyitemService.findKeyitemList(list.get(i).getId()));
				
				taxkeyArr.add(i, json);
			}
			taxkeys.put("taxkeys", taxkeyArr);
		}
		return taxkeys;
	}

	@Override
	public void delTaxkeyByTaxonId(String taxonId) {
		resourceRepository.delResourceByTaxonId(taxonId);
		keyitemRepository.delKeyItemByTaxonId(taxonId);
		taxkeyRepository.delTaxkeyByTaxonId(taxonId);
	}
}
