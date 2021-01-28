package org.big.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.big.common.QueryTool;
import org.big.common.UUIDUtils;
import org.big.entity.Dataset;
import org.big.entity.Team;
import org.big.entity.UserDetail;
import org.big.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class DatasetServiceImpl implements DatasetService {
	@Autowired
	private DatasetRepository datasetRepository;
	@Autowired
	private TeamService teamService;
	@Autowired
	private LicenseRepository LicenseRepository;
	
	@Override  // 超级管理员 -- 数据集列表
	public JSON findbyInfo(HttpServletRequest request) {
		JSON json = null;
		String searchText = request.getParameter("search");
		if (searchText == null || searchText.length() <= 0) {
			searchText = "";
		}
		int limit_serch = Integer.parseInt(request.getParameter("limit"));
		int offset_serch = Integer.parseInt(request.getParameter("offset"));
		String sort = "synchdate";
		String order = "desc";
		
		JSONObject thisTable = new JSONObject();
		JSONArray rows = new JSONArray();
		List<Dataset> thisList = new ArrayList<>();
		Page<Dataset> thisPage = this.datasetRepository.searchInfo(searchText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			String thisSelect = "<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
			String thisEdit =   	
				"<a class=\"wts-table-edit-icon\" title=\"逻辑删除（假删）\" onclick=\"removeThisObject('"+thisList.get(i).getId()+"','dataset')\" >" +
                	"<span class=\"glyphicon glyphicon-remove\"></span>" +
                "</a>&nbsp;&nbsp;"+
                "<a class=\"table-edit-icon\" title=\"物理删除（真删）\" onclick=\"deleteThisObject('"+thisList.get(i).getId()+"','dataset')\" >" +
                	"<span class=\"fa fa-trash\"></span>" +
            	"</a>";
			row.put("select", thisSelect);
			row.put("dsname", "<a href=\"console/dataset/show/" + thisList.get(i).getId() + "\">" + thisList.get(i).getDsname() + "</a>");
			row.put("team", thisList.get(i).getTeam().getName());
			row.put("creator", thisList.get(i).getCreator().getUserName());
			String thisStatus = "";
			switch (thisList.get(i).getStatus()) {
			case 0:
				thisStatus = "私有";
				break;
			case 1:
				thisStatus = "公开";
				break;
			case -1:
				thisStatus = "弃用";
				break;
			default:
				thisStatus = "未知";
				break;
			}
			row.put("status", thisStatus);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String addTime = "";
			String editTime = "";
			try {
				addTime = formatter.format(thisList.get(i).getCreatedDate());
				editTime = formatter.format(thisList.get(i).getSynchdate());
			} catch (Exception e) {
			}
			row.put("createdDate", addTime);
			row.put("synchdate", editTime);
			row.put("edit", thisEdit);
			rows.add(i, row);
		}
		thisTable.put("rows", rows);
		json = thisTable;
		return json;
	}

	@Override
	@Transactional
	public JSON findMyTeamDatasetsByTid(HttpServletRequest request) {
		JSON json = null;
		String searchText = request.getParameter("search");
		if (searchText == null || searchText.length() <= 0) {
			searchText = "";
		}
		int limit_serch = Integer.parseInt(request.getParameter("limit"));
		int offset_serch = Integer.parseInt(request.getParameter("offset"));
		String sort = "synchdate";
		String order = "desc";
		
		JSONObject thisTable = new JSONObject();
		JSONArray rows = new JSONArray();

        //获取当前登录用户
		String teamId = (String) request.getSession().getAttribute("teamId");
		Page<Dataset> thisPage = this.datasetRepository.searchMyTeamDataInfo(searchText, 
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), teamId);
		List<Dataset> thisList = new ArrayList<>();

		thisList = thisPage.getContent();
		thisTable.put("total", thisPage.getTotalElements());
		String thisSelect = "";
		String thisEdit = "";
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
				thisSelect = "<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
				thisEdit = "<a class=\"table-edit-icon\" onclick=\"editThisObject('" + thisList.get(i).getId()
						+ "','dataset')\" >" + "<span class=\"glyphicon glyphicon-edit\"></span>" + "</a>&nbsp;&nbsp;"
						+ "<a class=\"table-edit-icon\" onclick=\"removeThisObject('" + thisList.get(i).getId()
						+ "','dataset')\" >" + "<span class=\"glyphicon glyphicon-remove\"></span>" + "</a>";
			row.put("select", thisSelect);
			/*row.put("dsname", "<a href=\"change/dataset/" + thisList.get(i).getId() + "\">"
					+ thisList.get(i).getDsname() + "</a>");*/
			row.put("dsname", thisList.get(i).getDsname());
			String lisenceid = thisList.get(i).getLisenceid();
			if (StringUtils.isNotBlank(lisenceid)) {
				row.put("license", this.LicenseRepository.findOneById(lisenceid).getTitle());
			}else {
				row.put("license", this.LicenseRepository.findOneById("1").getTitle());
			}
			row.put("team", thisList.get(i).getTeam().getName());
			row.put("creator", thisList.get(i).getCreator().getUserName());
			String thisStatus = "";
			switch (thisList.get(i).getStatus()) {
			case 0:
				thisStatus = "私有";
				break;
			case 1:
				thisStatus = "公开";
				break;
			case -1:
				thisStatus = "弃用";
				break;
			default:
				thisStatus = "未知";
				break;
			}
			row.put("status", thisStatus);
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String addTime = "";
			String editTime = "";
			try {
				addTime = formatter.format(thisList.get(i).getCreatedDate());
				editTime = formatter.format(thisList.get(i).getSynchdate());
			} catch (Exception e) {
			}
			row.put("createdDate", addTime);
			row.put("synchdate", editTime);
			row.put("edit", thisEdit);
			rows.add(i, row);
		}
		thisTable.put("rows", rows);
		json = thisTable;
		return json;
	}

	@Override
	public void saveOne(Dataset thisDataset) {
		thisDataset.setSynchdate(new Timestamp(System.currentTimeMillis()));
		if (thisDataset.getDsabstract().equals("Default")) {
			thisDataset.setDsabstract("default");
		}
		this.datasetRepository.save(thisDataset);
	}

	@Override
	public void addOne(Dataset thisDataset, HttpServletRequest request) {
		if (thisDataset.getDsabstract().equals("Default")) {
			thisDataset.setDsabstract("default");
		}
		String teamId = (String) request.getSession().getAttribute("teamId");
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		thisDataset.setTeam(this.teamService.findbyID(teamId));
		thisDataset.setStatus(1);
		thisDataset.setSynchstatus(0);
		thisDataset.setCreator(thisUser); // 创建人 -- 当前数据集的负责人
		thisDataset.setCreatedDate(new Timestamp(System.currentTimeMillis())); // 创建日期
		thisDataset.setSynchdate(new Timestamp(System.currentTimeMillis())); // 最后同步日期
		thisDataset.setMark(UUID.randomUUID().toString());
		thisDataset.setBgurl("/img/dataset-bg.jpg");
		this.datasetRepository.save(thisDataset);
	}

	@Override
	public Boolean logicRemove(String Id) {
		Dataset thisDataset = this.datasetRepository.findOneById(Id);
		if (!thisDataset.getMark().equals("Default")) {
			if (1 == thisDataset.getStatus()) {
				thisDataset.setStatus(0);
			} else {
				thisDataset.setStatus(1);
			}
			this.datasetRepository.save(thisDataset);
			return true;
		}
		return false;
	}

	@Override
	public Boolean deleteOne(HttpServletRequest request, String ID) {
		Dataset thisDataset = datasetRepository.findOneById(ID);
		if (!thisDataset.getDsabstract().equals("Default")) { // 判断当前数据集 -- 不是默认数据集 -- 则执行if语句 -- 物理删除当前dataset
			this.datasetRepository.delete(thisDataset);
			return true;
		}
		return false;
	}

	@Override
	public Dataset findbyID(String ID) {
		return this.datasetRepository.getOne(ID);
	}

	@Override
	public Dataset findOneByDsabstractAndTeam(String dsabstraction, Team thisTeam) {
		return this.datasetRepository.findOneByDsabstractAndTeam(dsabstraction, thisTeam);
	}

	@Override
	public Dataset findDefaultByUser() {
		return null;
	}

	@Override
	public List<Dataset> findAllDatasetsByTeamId(String teamId) {
		return this.datasetRepository.findAllDatasetsByTeamId(teamId);
	}

	@Override
	public JSON findDatasetListByTeamId(String teamId, HttpServletRequest request) {
		int limit_serch = Integer.parseInt(request.getParameter("limit"));		// 1.limit 一次查询返回的个数，默认值10
		int offset_serch = Integer.parseInt(request.getParameter("offset"));	// 2.offset从第几个开始查，默认值0
		String sort = "synchdate";
		String order = "desc";
		
		JSONObject datasetList = new JSONObject();
		String searchText = "";
		JSONArray rows = new JSONArray();
		Page<Dataset> thisPage = this.datasetRepository.searchMyTeamDataInfo(searchText, QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order),teamId);
		List<Dataset> thisList =thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			Dataset thisDataset = thisList.get(i);
			row.put("id", thisDataset.getId());
			row.put("dsname", thisDataset.getDsname());
			row.put("dsabstract", thisDataset.getDsabstract());
			row.put("bgurl", thisDataset.getBgurl());
			/*row.put("taxon", this.taxonRepository.countByStatusAndTaxaset_Dataset_Id(1,thisDataset.getId()));
			row.put("description",
					this.descriptionRepository.countByStatusAndTaxon_Taxaset_Dataset_Id(1,thisDataset.getId())
//					+this.descriptionRepository.countByStatusAndAndDataset_Id(1,thisDataset.getId())
			);
			row.put("taxtree", this.taxtreeRepository.countByStatusAndDataset_Id(1,thisDataset.getId()));
			//row.put("phytree", this.phytreeRepository.countByDataset_Id(1,thisDataset.getId()));
			row.put("phytree", 0);*/
			rows.add(i, row);
		}
		datasetList.put("total", thisPage.getTotalElements());		// NO1：总数
		datasetList.put("page", offset_serch);						// NO2：offset
		datasetList.put("rows", rows);								// NO3：Taxon下的所有Dataset
		return datasetList;
	}

	@Override
	public void updateOneById(Dataset thisDataset) {
		thisDataset.setSynchdate(new Timestamp(System.currentTimeMillis()));
		this.datasetRepository.save(thisDataset);
	}

	@Override
	public long countDatasetByTeam_IdAndStatus(String teamId,int status) {
		return this.datasetRepository.countDatasetByTeam_IdAndStatus(teamId,status);
	}

	@Override
	public JSON newOne(Dataset thisDataset, HttpServletRequest request) {
		JSONObject thisResult = new JSONObject();
		try {
			thisDataset.setId(UUIDUtils.getUUID32());
			String teamId = (String) request.getSession().getAttribute("teamId");
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			thisDataset.setTeam(this.teamService.findbyID(teamId));
			thisDataset.setCreator(thisUser);
			thisDataset.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			thisDataset.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisDataset.setStatus(1);
			thisDataset.setSynchstatus(0);
			thisDataset.setMark(UUIDUtils.getUUID32());
			thisDataset.setBgurl("/img/dataset-bg.jpg");
			this.datasetRepository.save(thisDataset);
			
			thisResult.put("result", true);
			thisResult.put("newId", thisDataset.getId());
			thisResult.put("teamId", teamId);
		} catch (Exception e) {
			thisResult.put("result", false);
		}
		return thisResult;
	}

	@Override
	public boolean countDatasetsByDsname(HttpServletRequest request) {
		String dsname = request.getParameter("name");
		String teamId = (String) request.getSession().getAttribute("teamId");
		long num = 0;
		if (StringUtils.isNotBlank(dsname)) {
			num = this.datasetRepository.countDatasetsByDsname(dsname, teamId);
		}
		if (num > 0) {
			return false;
		}else {
			return true;
		}
	}
}