package org.big.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.big.common.QueryTool;
import org.big.common.ReturnCode;
import org.big.common.UUIDUtils;
import org.big.entity.Keyitem;
import org.big.entity.Resource;
import org.big.repository.KeyitemRepository;
import org.big.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class KeyitemServiceImpl implements KeyitemService {
	@Autowired
	private KeyitemRepository keyitemRepository; 
	@Autowired
	private TaxonService taxonService;
	@Autowired
	private TaxkeyService taxkeyService;
	@Autowired
	private ResourceRepository resourceRepository;
	@Override
	public JSON findKeyitemList(String taxkeyId) {
		JSONArray rows = new JSONArray();
		String type = null;
		if (StringUtils.isNotBlank(taxkeyId)) {
			type = this.taxkeyService.findOneById(taxkeyId).getKeytype();
		}
		
		List<Keyitem> thisList = this.keyitemRepository.searchInfoSort(taxkeyId, type);
		String thisSelect = "";
		String thisEdit = "";
		String thisFeatureimg = "";
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
	        thisSelect="<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
	        thisEdit=
	             "<a class=\"wts-table-edit-icon\" onclick=\"removeObject('" + thisList.get(i).getId() + "','keyitem')\" >" +
	             "<span class=\"glyphicon glyphicon-remove\"></span>" +
	             "</a>";
	        thisFeatureimg = "<a class=\"wts-table-edit-icon\" onclick=\"optionFeatureimg('" + thisList.get(i).getId() +"');\" >" +
								"<span>特征图片</span>"+
							 "</a>";
	        
	        int keytype = Integer.parseInt(thisList.get(i).getTaxkey().getKeytype());
	        int branchid = thisList.get(i).getBranchid();
	        String taxonid = thisList.get(i).getTaxonid();
	        if (keytype == 1) {			// 双向式
	        	if (i == 0) {
	        		row.put("orderid", thisList.get(i).getOrderid() + ".");
				}else {
					if (thisList.get(i).getOrderid() == thisList.get(i - 1).getOrderid()) {
						row.put("orderid", "");
					}else {
						row.put("orderid", thisList.get(i).getOrderid() + ".");
					}
				}
	        	row.put("select", thisSelect);
	        	row.put("item", thisList.get(i).getItem());
	        	row.put("ellipsis", "……………………………………………………………………………………");
	        	row.put("branchid", (branchid == 0 && null != taxonid) ? this.taxonService.findOneById(taxonid).getScientificname() : branchid);
	        	row.put("images", thisFeatureimg);
	        	row.put("edit", thisEdit);
			}else if (keytype == 2) {	// 单项式
				row.put("select", thisSelect);
	        	row.put("orderid", thisList.get(i).getOrderid() + "(" + thisList.get(i).getBranchid() + ")");
	        	row.put("item", thisList.get(i).getItem());
	        	row.put("ellipsis", (null != taxonid) ? "……………………………………………………………………………………" : "");
	        	row.put("branchid", (null != taxonid) ? this.taxonService.findOneById(taxonid).getScientificname() : "");
	        	row.put("images", thisFeatureimg);
	        	row.put("edit", thisEdit);
			}
			rows.add(i, row);
		}
		return rows;
    }

	@Override
	public JSONArray findKeyitemTable(String taxkeyId) {
		JSONArray rows = new JSONArray();
		String type = null;
		if (StringUtils.isNotBlank(taxkeyId)) {
			type = this.taxkeyService.findOneById(taxkeyId).getKeytype();
		}
		List<Keyitem> thisList = this.keyitemRepository.searchInfoSort(taxkeyId, type);
		String thisFeatureimg = "";
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			thisFeatureimg = "<button type=\"button\" class=\"btn btn-default btn-xs\" " +
					"onclick=\"showTaxkeyImages('" + thisList.get(i).getId() +"');\" >" +
					"<span>特征图片</span>"+
					"</button>";
			int keytype = Integer.parseInt(thisList.get(i).getTaxkey().getKeytype());
			int branchid = thisList.get(i).getBranchid();
			String taxonid = thisList.get(i).getTaxonid();
			if (keytype == 1) {			// 双向式
				if (i == 0) {
					row.put("orderid", thisList.get(i).getOrderid() + ".");
				}else {
					if (thisList.get(i).getOrderid() == thisList.get(i - 1).getOrderid()) {
						row.put("orderid", "");
					}else {
						row.put("orderid", thisList.get(i).getOrderid() + ".");
					}
				}
				row.put("item", thisList.get(i).getItem());
				row.put("ellipsis", "………………………………………………");
				row.put("branchid", (branchid == 0 && null != taxonid) ? this.taxonService.findOneById(taxonid).getScientificname() : branchid);
				row.put("images", thisFeatureimg);
			}else if (keytype == 2) {	// 单项式
				row.put("orderid", thisList.get(i).getOrderid() + "(" + thisList.get(i).getBranchid() + ")");
				row.put("item", thisList.get(i).getItem());
				row.put("ellipsis", (null != taxonid) ? "………………………………………………" : "");
				row.put("branchid", (null != taxonid) ? this.taxonService.findOneById(taxonid).getScientificname() : "");
				row.put("images", thisFeatureimg);
			}
			rows.add(i, row);
		}
		return rows;
	}

	@Override
	public JSON batchImages(String keyitemId, HttpServletRequest request, MultipartFile file, String uploadPath) {
		JSONObject thisResult = new JSONObject();
		if (null == file || file.isEmpty()) {
			System.out.println("没有上传文件");
			// 构建返回结果
			thisResult.put("code", ReturnCode.RECORD_NULL.getCode());
			thisResult.put("message", ReturnCode.RECORD_NULL.getMessage_zh());
			thisResult.put("data", "");
		} else {
			// 初始化资源对象
			Resource thisResource = new Resource();
			Keyitem thisKeyitem = this.keyitemRepository.findOneById(keyitemId);
			
			if (file.getSize() > 100 * 1024 * 1024) {
				thisResult.put("code", ReturnCode.FAILURE.getCode());
				thisResult.put("message", ReturnCode.FAILURE.getMessage_zh());
				thisResult.put("data", "");
			}
			
			if (!file.getContentType().contains("image")) {
                //构建返回结果
                thisResult.put("code",ReturnCode.UNSUPPORTED_TYPE.getCode());
				thisResult.put("message",ReturnCode.UNSUPPORTED_TYPE.getMessage_zh());
                thisResult.put("data","");
            } else {
            	String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);	// 后缀名
            	String newFileName = UUIDUtils.getUUID32() + "." + suffix;												// 文件名
            	String realPath = request.getSession().getServletContext().getRealPath(uploadPath);						// 本地存储路径
            	realPath = realPath + "/" + keyitemId + "/";
            	
            	try {
            		// 先把文件保存到本地
            		FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, newFileName));
            	} catch (IOException e1) {
            		System.out.println("文件保存到本地发生异常:" + e1.getMessage());
            		// 构建返回结果
            		thisResult.put("code", ReturnCode.FAILURE.getCode());
            		thisResult.put("message", ReturnCode.FAILURE.getMessage_zh());
            		thisResult.put("data", "");
            	}
            	
            	String path  = keyitemId + "/" + newFileName;	// 每一张图片的路径
            	thisResource.setId(UUIDUtils.getUUID32());
            	thisResource.setKeyitem(thisKeyitem);
            	thisResource.setResourceName(path);
            	thisResource.setType("检索表");

            	this.resourceRepository.save(thisResource);
            	
            	// 构建返回结果
            	thisResult.put("data", "");
			}
			
		}
		return thisResult;
	}

	@Override
	public JSON findFeatureImgList(HttpServletRequest request) {
		String keyitemId = (String) request.getSession().getAttribute("keyitemId");
        int limit_serch=Integer.parseInt(request.getParameter("limit"));
        int offset_serch=Integer.parseInt(request.getParameter("offset"));
        String sort = "id";
		String order = "desc";
		sort = request.getParameter("sort");
		order = request.getParameter("order");
		if (StringUtils.isBlank(sort)) {
			sort = "id";
		}
		if (StringUtils.isBlank(order)) {
			order = "desc";
		}
        
        JSONObject thisTable= new JSONObject();
        JSONArray rows = new JSONArray();
        List<Resource> thisList=new ArrayList<>();
        Page<Resource> thisPage=this.resourceRepository.findFeatureImgList(QueryTool.buildPageRequest(offset_serch,limit_serch,sort,order), keyitemId);
        thisList=thisPage.getContent();
        thisTable.put("total", thisPage.getTotalElements());
        String thisSelect = "";
        for(int i=0;i<thisList.size();i++){
            JSONObject row= new JSONObject();
            thisSelect="<input type='checkbox' name='checkbox' id=" + thisList.get(i).getId() + " />";
            String thisEdit=
                    "<a class=\"table-edit-icon\" onclick=\"removeThisResource('"+thisList.get(i).getId()+"','resource')\" >" +
                    "<span class=\"glyphicon glyphicon-remove\"></span>" +
                    "</a>";
            String featureimg=
            				"<img  width=\"40%\" height=\"30%\" src=\"upload/images/" + thisList.get(i).getResourceName() + "\" " 
            			  + "class='img-responsive img-thumbnail' title='点击查看大图' alt='点击查看大图' onclick= \"showLargeImage('upload/images/" + thisList.get(i).getResourceName() + "')\">";
            row.put("featureimgjson",featureimg);
            row.put("select",thisSelect);
            row.put("edit",thisEdit);
            rows.add(i,row);
        }
        thisTable.put("rows",rows);
        return thisTable;
    }

	@Override
	public boolean delOne(String keyitemId) {
		boolean flag = false;
		Keyitem thisKeyitem = this.keyitemRepository.findOneById(keyitemId);
		if (null != thisKeyitem) {
			this.keyitemRepository.delOne(keyitemId);
			flag = true;
		}
		return flag;
	}

}
