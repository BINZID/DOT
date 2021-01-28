package org.big.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.big.common.ExcelUtil;
import org.big.common.QueryTool;
import org.big.entity.License;
import org.big.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
@Service
public class LicenseServiceImpl implements LicenseService{
	@Autowired
	private LicenseRepository licenseRepository;
	
	@Override
	public JSON findBySelect(HttpServletRequest request) {
		String findText = request.getParameter("find");
		if (StringUtils.isBlank(findText)) {
			findText = "";
		}
		int findPage = 1;
		try {
			findPage = Integer.valueOf(request.getParameter("page"));
		} catch (Exception e) {
		}
		int limit_serch = 30;
		int offset_serch = (findPage - 1) * 30;
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
		JSONArray items = new JSONArray();
		List<License> thisList = new ArrayList<>();
		Page<License> thisPage = this.licenseRepository.searchByEnname(findText, 
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
			row.put("text", thisList.get(i).getTitle());
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public License findOneById(String id) {
		return this.licenseRepository.findOneById(id);
	}

	@Override
	public JSON findLicenseList(HttpServletRequest request) {
	        String searchText=request.getParameter("search");
	        if(searchText==null || searchText.length()<=0){
	            searchText="";
	        }
	        int limit_serch=Integer.parseInt(request.getParameter("limit"));
	        int offset_serch=Integer.parseInt(request.getParameter("offset"));
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
	        JSONObject thisTable= new JSONObject();
	        JSONArray rows = new JSONArray();
	        List<License> thisList=new ArrayList<>();

	        Page<License> thisPage=this.licenseRepository.findLicenseList(searchText,QueryTool.buildPageRequest(offset_serch,limit_serch,sort,order));
	        thisTable.put("total",thisPage.getTotalElements());
	        thisList=thisPage.getContent();
	        for(int i=0;i<thisList.size();i++){
	            JSONObject row= new JSONObject();
	            
	            String thisSelect="";
	            String thisEdit="";
	            thisSelect="<input type='checkbox' name='checkbox' id='sel_"+thisList.get(i).getId()+"' />";
	            thisEdit=
	                    "<a class=\"table-edit-icon\" href=\"javascript:void(0)\" >" +
	                    	"<span class=\"glyphicon glyphicon-edit\"></span>" + "</a>&nbsp;&nbsp;&nbsp;"+
	                     "<a class=\"table-edit-icon\" href=\"javascript:void(0)\" >" +
	                     	"<span class=\"glyphicon glyphicon-remove\"></span>" +
	                     "</a>";
	            String thisImage=
	                    "<a href=\""+thisList.get(i).getImageurl()+"\" target=\"_blank\" class=\"maxWidth100\">" +
	                            "<img src=\""+thisList.get(i).getImageurl()+"\" alt=\""+thisList.get(i).getImageurl()+"\" />" +
	                            "</a>";
	            row.put("select",thisSelect);
	            row.put("title", "&nbsp;&nbsp;" + thisList.get(i).getTitle());
	            row.put("imageurl",thisImage);
	            row.put("text", "&nbsp;&nbsp;" + thisList.get(i).getText());
	            row.put("edit",thisEdit);
	            rows.add(i,row);
	        }
	        thisTable.put("rows",rows);
	        return thisTable;
	    }

	@Override
	public void export(HttpServletResponse response) throws IOException {
		int columnNum = 3;
		String[] columnName = {"序号", "共享协议名称", "描述"};
		List<License> thisList = this.licenseRepository.findAll();
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("共享协议");
		
		sheet.setColumnWidth(1, 40 * 256);
		sheet.setColumnWidth(2, 65 * 256);
		
		HSSFRow title = sheet.createRow(0);
		title.setHeightInPoints(20);
		for (int i = 0; i < columnNum; i++) {
			HSSFCell cell = title.createCell(i);
			cell.setCellValue(columnName[i]);
			cell.setCellStyle(ExcelUtil.setTitleStyle(wb));
		}
		
		for (int i = 0; i < thisList.size(); i++) {
			HSSFRow rows = sheet.createRow(i + 1);
			rows.createCell(0).setCellValue((i + 1));
			rows.createCell(1).setCellValue(thisList.get(i).getTitle());
			rows.createCell(2).setCellValue(thisList.get(i).getText());
		}
		
		response.setHeader("content-Type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=" + new String("共享协议".getBytes("gb2312"), "iso8859-1") + ".xls");
		OutputStream out = response.getOutputStream();
		try {
			wb.write(out);
		} catch (Exception e) {
		} finally {
			out.close();
		}
	}
}
