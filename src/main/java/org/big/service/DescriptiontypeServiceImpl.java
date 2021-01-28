package org.big.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.big.common.QueryTool;
import org.big.entity.Descriptiontype;
import org.big.repository.DescriptiontypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class DescriptiontypeServiceImpl implements DescriptiontypeService {
	@Autowired
	private DescriptiontypeRepository descriptiontypeRepository;
	
	@Override
	public JSON findBySelect(HttpServletRequest request, String mark) {
		String findText = request.getParameter("find");
		if (findText == null || findText.length() <= 0) {
			findText = "";
		}
		int findPage = 1;
		try {
			findPage = Integer.valueOf(request.getParameter("page"));
		} catch (Exception e) {
		}
		int limit_serch = 100;
		int offset_serch = (findPage - 1) * 100;
		String sort = "name";
		String order = "desc";
		sort = request.getParameter("sort");
		order = request.getParameter("order");
		if (StringUtils.isBlank(sort)) {
			sort = "name";
		}
		if (StringUtils.isBlank(order)) {
			order = "desc";
		}
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		List<Descriptiontype> thisList = new ArrayList<>();
		Page<Descriptiontype> thisPage = this.descriptiontypeRepository.searchByDescTypeList(findText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisSelect.put("total_count", thisPage.getTotalElements());
		Boolean incompleteResulte = true;
		if ((thisPage.getTotalElements() / 100) > findPage)
			incompleteResulte = false;
		thisSelect.put("incompleteResulte", incompleteResulte);
		thisList = thisPage.getContent();
		
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			row.put("id", thisList.get(i).getId());
			String pid = thisList.get(i).getPid();	
			if (thisList.get(i).getName().contains("分布") && mark.equals("distribution")) {
				if (!pid.equals("0")) {
					row.put("text", "&nbsp;&nbsp;&nbsp;<i>" + thisList.get(i).getName() + "</i>");
				}else{
					row.put("text", "<b>" + thisList.get(i).getName() + "</b>");
				}
				items.add(row);
			}else if (!thisList.get(i).getName().contains("分布") && mark.equals("description")) {
				if (!pid.equals("0")) {
					row.put("text", "&nbsp;&nbsp;&nbsp;<i>" + thisList.get(i).getName() + "</i>");
				}else{
					row.put("text", "<b>" + thisList.get(i).getName() + "</b>");
				}
				items.add(row);
			}
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public Descriptiontype findOneById(String id) {
		return this.descriptiontypeRepository.findOneById(id);
	}
}
