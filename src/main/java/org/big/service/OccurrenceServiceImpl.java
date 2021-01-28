package org.big.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.big.common.QueryTool;
import org.big.entity.Occurrence;
import org.big.entity.Taxon;
import org.big.entity.UserDetail;
import org.big.repository.OccurrenceRepository;
import org.big.repository.TaxonRepository;
import org.big.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class OccurrenceServiceImpl implements OccurrenceService {
	@Autowired
	private OccurrenceRepository occurrenceRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private DatasourceService datasourceService;
	@Autowired
	private RefService refService;

	@Override
	public JSON findOccurrenceList(HttpServletRequest request) {
		JSON json = null;
		String searchText = request.getParameter("search");
		if (searchText == null || searchText.length() <= 0) {
			searchText = "";
		}
		int limit_serch = Integer.parseInt(request.getParameter("limit"));
		int offset_serch = Integer.parseInt(request.getParameter("offset"));
		String sort = "synchdate";
		String order = "desc";
		sort = request.getParameter("sort");
		order = request.getParameter("order");
		if (StringUtils.isBlank(sort)) {
			sort = "synchdate";
		}
		if (StringUtils.isBlank(order)) {
			order = "desc";
		}
		JSONObject thisTable = new JSONObject();
		JSONArray rows = new JSONArray();
		List<Occurrence> thisList = new ArrayList<>();
		Page<Occurrence> thisPage = this.occurrenceRepository.searchInfo(searchText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		String thisSelect = "";
		String thisEdit = "";
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
	        thisSelect="<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
	        thisEdit=
	        	 "<a class=\"wts-table-edit-icon\" onclick=\"editThisObject('" + thisList.get(i).getId() + "','occurrence')\" >" +
	             "<span class=\"glyphicon glyphicon-edit\"></span>" +
	             "</a> &nbsp;&nbsp;&nbsp;" +
	             "<a class=\"wts-table-edit-icon\" onclick=\"removeThisObject('" + thisList.get(i).getId() + "','occurrence')\" >" +
	             "<span class=\"glyphicon glyphicon-remove\"></span>" +
	             "</a>";
			row.put("select", thisSelect);
			row.put("title", thisList.get(i).getTitle());
			row.put("inputer", this.userRepository.findOneById(thisList.get(i).getInputer()).getNickname());
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
	public JSON addOccurrence(String taxonId, HttpServletRequest request) {
		Occurrence thisOccurrence = new Occurrence();
		Enumeration<String> paraNames = request.getParameterNames();
		String paraName = null;
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("occurrenceId_") == 0) {
				thisOccurrence.setId(request.getParameter(paraName));
			}
			if (paraName.indexOf("eventdate_") == 0) {
				thisOccurrence.setEventdate(request.getParameter(paraName));
			}
			if (paraName.indexOf("occurrence_city_") == 0) {
				thisOccurrence.setCity(request.getParameter(paraName));
			}
			if (paraName.indexOf("occurrence_country_") == 0) {
				thisOccurrence.setCountry(request.getParameter(paraName));
			}
			if (paraName.indexOf("occurrence_province_") == 0) {
				thisOccurrence.setProvince(request.getParameter(paraName));
			}
			if (paraName.indexOf("occurrence_county_") == 0) {
				thisOccurrence.setCounty(request.getParameter(paraName));
			}
			if (paraName.indexOf("occurrence_location_") == 0) {
				thisOccurrence.setLocation(request.getParameter(paraName));
			}
			if (paraName.indexOf("occurrence_locality_") == 0) {
				thisOccurrence.setLocality(request.getParameter(paraName));
			}
			if (paraName.indexOf("occurrenceSourcesid_") == 0) {
				thisOccurrence.setSourcesid(request.getParameter(paraName));
			}
			if (paraName.indexOf("occurrence_type_") == 0) {
				thisOccurrence.setType(request.getParameter(paraName));
			}
			if (paraName.indexOf("occurrence_lat_") == 0 && StringUtils.isNotBlank(request.getParameter(paraName))) {
				thisOccurrence.setLat(Double.parseDouble(request.getParameter(paraName)));
			}
			if (paraName.indexOf("occurrence_lng_") == 0 && StringUtils.isNotBlank(request.getParameter(paraName))) {
				thisOccurrence.setLng(Double.parseDouble(request.getParameter(paraName)));
			}
		}
		
		JSONObject thisResult = new JSONObject();
		try {
			//thisOccurrence.setId((String)request.getSession().getAttribute("uuid32"));
			//System.out.println("test=="+(String)request.getSession().getAttribute("uuid32"));
			Taxon thisTaxon = this.taxonRepository.findOneById(taxonId);
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			thisOccurrence.setInputer(thisUser.getId());
			thisOccurrence.setInputtime(new Timestamp(System.currentTimeMillis()));
			thisOccurrence.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisOccurrence.setTaxon(thisTaxon);
			thisOccurrence.setStatus(1);
			thisOccurrence.setSynchstatus(0);
			thisOccurrence.setTaxonRankId(thisTaxon.getRank().getId());
			thisOccurrence.setTaxonTaxasetId(thisTaxon.getTaxaset().getId());
			JSONArray handleReferenceToJson = (JSONArray) handleReferenceToJson(request);
			if (handleReferenceToJson.size() > 0) {
				thisOccurrence.setRefjson(handleReferenceToJson.toJSONString());
			}
			this.occurrenceRepository.save(thisOccurrence);
			thisResult.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			thisResult.put("result", false);
		}
		return thisResult;
	}

	@Override
	public JSON handleReferenceToJson(HttpServletRequest request) {
		JSONArray jsonArray = new JSONArray();
		Enumeration<String> paraNames = request.getParameterNames();
		String paraName = null;
		int formNum = 0;
		int countOccurrenceNum = 0;
		String occurrenceReferencesPageE = null;
		String occurrenceReferencesPageS = null;
		String occurrenceReferencesId = null;
		String jsonStr = null;
		
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("countOccurrenceReferences_") == 0) {
				countOccurrenceNum = Integer.parseInt(request.getParameter(paraName));
			}
			if (paraName.indexOf("occurrenceId_") == 0) {
				formNum = Integer.parseInt(paraName.substring(paraName.indexOf("_") + 1));
			}
		}
		
		for (int i = 1; i <= countOccurrenceNum; i++) {
			occurrenceReferencesId = request.getParameter("occurrenceReferences_" + formNum + "_" + i);
			occurrenceReferencesPageS = request.getParameter("occurrenceReferencesPageS_" + formNum + "_" + i);
			occurrenceReferencesPageE = request.getParameter("occurrenceReferencesPageE_" + formNum + "_" + i);
			if (StringUtils.isNotBlank(occurrenceReferencesId) && StringUtils.isNotBlank(occurrenceReferencesPageS)
					&& StringUtils.isNotBlank(occurrenceReferencesPageE)) {
				jsonStr = "{"
						+ "\"refId\"" + ":\"" + occurrenceReferencesId + "\","
						+ "\"refS\"" + ":\"" + occurrenceReferencesPageS + "\"," 
						+ "\"refE\"" + ":\"" + occurrenceReferencesPageE + "\""
						+ "}";
				JSONObject jsonText = JSON.parseObject(jsonStr);
				jsonArray.add(jsonText);
			}
		}
		
		return jsonArray;
	}

	@Override
	public boolean logicRemove(String id) {
		Occurrence thisOccurrence = this.occurrenceRepository.findOneById(id);
		if (null != thisOccurrence && 1 == thisOccurrence.getStatus()) {
			thisOccurrence.setStatus(0);
			this.occurrenceRepository.save(thisOccurrence);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean deleteOne(HttpServletRequest request) {
		String occurrenceId = request.getParameter("occurrenceId");
		if (StringUtils.isNotBlank(occurrenceId)) {
			if (null != this.occurrenceRepository.findOneById(occurrenceId)) {
				this.occurrenceRepository.deleteOneById(occurrenceId);
			}
			return true;
		}
		return false;
	}

	@Override
	public JSON findOccurrenceListByTaxonId(String taxonId, HttpServletRequest request) {
		int limit_serch = Integer.parseInt(request.getParameter("limit"));		// 1.limit 一次查询返回的个数，默认值10
		int offset_serch = Integer.parseInt(request.getParameter("offset"));	// 2.offset从第几个开始查，默认值0
		
		String sort = "synchdate";
		String order = "desc";
		sort = request.getParameter("sort");
		order = request.getParameter("order");
		if (StringUtils.isBlank(sort)) {
			sort = "synchdate";
		}
		if (StringUtils.isBlank(order)) {
			order = "desc";
		}

		JSONObject thisSelect = new JSONObject();
		Page<Occurrence> thisPage = this.occurrenceRepository.searchOccurrencesByTaxonId(
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), taxonId);
		
		JSONArray rows = new JSONArray();
		List<Occurrence> thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			thisList.get(i).setSourcesid(this.datasourceService.findOneById(thisList.get(i).getSourcesid()).getTitle());
			rows.add(i, thisList.get(i));
		}
		
		thisSelect.put("total", thisPage.getTotalElements());		// NO1：总数
		thisSelect.put("page", offset_serch);						// NO2：offset
		thisSelect.put("rows", rows);								// NO3：Taxon下的所有Citation
		return thisSelect;
	}

	@Override
	public JSON editOccurrence(String taxonId) {
		JSONObject occurrences = new JSONObject();
		JSONArray occurrenceArr = new JSONArray();
		try {
			if (StringUtils.isNotBlank(taxonId)) {
				List<Occurrence> list = this.occurrenceRepository.findOccurrenceListByTaxonId(taxonId);
				for (int i = 0; i < list.size(); i++) {
					JSONArray refjson = this.refService.refactoringRef(list.get(i).getRefjson());
					JSONObject json = new JSONObject();
					json.put("id", list.get(i).getId());
					json.put("eventdate", list.get(i).getEventdate());
					json.put("city", list.get(i).getCity());
					json.put("country", list.get(i).getCountry());
					json.put("county", list.get(i).getCounty());
					json.put("province", list.get(i).getProvince());
					json.put("location", list.get(i).getLocation());
					json.put("locality", list.get(i).getLocality());
					json.put("type", list.get(i).getType());
					json.put("lat", list.get(i).getLat());
					json.put("lng", list.get(i).getLng());
					json.put("refjson", refjson.toJSONString());
					
					String sourcesid = list.get(i).getSourcesid();
					String sourcesTitle = this.datasourceService.findOneById(sourcesid).getTitle();
					json.put("sourcesid", sourcesid);
					json.put("sourcesTitle", sourcesTitle);
					occurrenceArr.add(i, json);
				}
				occurrences.put("occurrences", occurrenceArr);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return occurrences;
	}
}
