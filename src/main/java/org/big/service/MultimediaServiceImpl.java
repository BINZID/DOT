package org.big.service;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.io.FileUtils;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.big.common.FileInfo;
import org.big.common.QueryTool;
import org.big.common.ReturnCode;
import org.big.common.UUIDUtils;
import org.big.entity.Multimedia;
import org.big.entity.Taxaset;
import org.big.entity.Dataset;
import org.big.entity.Expert;
import org.big.entity.License;
import org.big.entity.Taxon;
import org.big.entity.Taxtree;
import org.big.entity.Team;
import org.big.entity.User;
import org.big.entity.UserDetail;
import org.big.repository.DatasetRepository;
import org.big.repository.ExpertRepository;
import org.big.repository.LicenseRepository;
import org.big.repository.MultimediaRepository;
import org.big.repository.TaxasetRepository;
import org.big.repository.TaxonRepository;
import org.big.repository.TaxtreeRepository;
import org.big.repository.TeamRepository;
import org.big.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class MultimediaServiceImpl implements MultimediaService {
	@Autowired
	private MultimediaRepository multimediaRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private LicenseRepository licenseRepository;
	@Autowired
	private DatasourceService datasourceService;
	@Autowired
	private RefService refService;
	@Autowired
	private LicenseService licenseService;
	@Autowired
	private ExcelService excelService;
	@Autowired
	private BaseinfotmpService baseinfotmpService;
	@Autowired
	private ExpertService expertService;
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private DatasetRepository datasetRepository;
	@Autowired
	private TaxasetRepository taxasetRepository;
	@Autowired
	private TaxtreeRepository taxtreeRepository;
	@Autowired
	private BatchInsertService batchInsertService;
	@Autowired
	private ExpertRepository expertRepository;
	
	@Override
	public JSON findUploadedMultimediaList(Timestamp timestamp, HttpServletRequest request) {
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
		List<Multimedia> thisList = new ArrayList<>();
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Page<Multimedia> thisPage = this.multimediaRepository.searchInfo(searchText, timestamp, thisUser.getId(),
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		String thisSelect = "";
		String thisEdit = "";
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
	        thisSelect="<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
	        thisEdit=
	        	 "<a class=\"wts-table-edit-icon\" onclick=\"editThisObject('" + thisList.get(i).getId() + "','multimedia')\" >" +
	             "<span class=\"glyphicon glyphicon-edit\"></span>" +
	             "</a> &nbsp;&nbsp;&nbsp;" +
	             "<a class=\"wts-table-edit-icon\" onclick=\"removeThisObject('" + thisList.get(i).getId() + "','multimedia')\" >" +
	             "<span class=\"glyphicon glyphicon-remove\"></span>" +
	             "</a>";
			row.put("select", thisSelect);
			row.put("title", thisList.get(i).getTitle());
			String mediatype = thisList.get(i).getMediatype();
			switch (mediatype) {
			case "1":
				row.put("mediatype", "图片");
				break;
			case "2":
				row.put("mediatype", "音频");
				break;
			case "3":
				row.put("mediatype", "视频");
				break;
			case "4":
				row.put("mediatype", "表格");
				break;
			case "5":
				row.put("mediatype", "文本文件");
				break;
			case "6":
				row.put("mediatype", "地图资源");
				break;
			default:
				row.put("mediatype", "无");
				break;
			}
			row.put("license", thisList.get(i).getLicense().getTitle());
			try {
				Expert thisExpert = this.expertService.findOneById(thisList.get(i).getExpert());
				if (null != thisExpert) {
					row.put("expert", thisExpert.getCnName() + "(" + thisExpert.getExpEmail() + ")");
				}else {
					row.put("expert", "无审核人");
				}
			} catch (Exception e1) {
				row.put("expert", "无审核人");
			}
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
	public JSON addMultimedia(String taxonId , HttpServletRequest request) {
		Multimedia thisMultimedia = new Multimedia();
		Enumeration<String> paraNames = request.getParameterNames();
		String paraName = null;
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("multimediaId_") == 0) {
				thisMultimedia.setId(request.getParameter(paraName));
			}
			if (paraName.indexOf("multimediaPath_") == 0 && StringUtils.isNotBlank(request.getParameter(paraName))) {
				thisMultimedia.setPath(request.getParameter(paraName));
			}
			if (paraName.indexOf("multimedia_country_") == 0) {
				thisMultimedia.setCountry(request.getParameter(paraName));
			}
			if (paraName.indexOf("multimedia_province_") == 0) {
				thisMultimedia.setProvince(request.getParameter(paraName));
			}
			if (paraName.indexOf("multimedia_city_") == 0) {
				thisMultimedia.setCity(request.getParameter(paraName));
			}
			if (paraName.indexOf("multimedia_county_") == 0) {
				thisMultimedia.setCounty(request.getParameter(paraName));
			}
			if (paraName.indexOf("multimedia_location_") == 0) {
				thisMultimedia.setLocation(request.getParameter(paraName));
			}
			if (paraName.indexOf("multimedia_locality_") == 0) {
				thisMultimedia.setLocality(request.getParameter(paraName));
			}
			if (paraName.indexOf("multimediaSourcesid_") == 0) {
				thisMultimedia.setSourcesid(request.getParameter(paraName));
			}
			if (paraName.indexOf("multimedia_rightsholder_") == 0) {
				thisMultimedia.setRightsholder(request.getParameter(paraName));
			}
			if (paraName.indexOf("multimedia_context_") == 0) {
				thisMultimedia.setContext(request.getParameter(paraName));
			}
			if (paraName.indexOf("multimedia_title_") == 0) {
				thisMultimedia.setTitle(request.getParameter(paraName));
			}
			if (paraName.indexOf("multimedia_licenseid_") == 0 && StringUtils.isNotBlank(request.getParameter(paraName))) {
				thisMultimedia.setLicense(this.licenseRepository.findOneById(request.getParameter(paraName)));
			}
			if (paraName.indexOf("multimedia_lat_") == 0 && StringUtils.isNotBlank(request.getParameter(paraName))) {
				thisMultimedia.setLat(Double.parseDouble(request.getParameter(paraName)));
			}
			if (paraName.indexOf("multimedia_lng_") == 0 && StringUtils.isNotBlank(request.getParameter(paraName))) {
				thisMultimedia.setLng(Double.parseDouble(request.getParameter(paraName)));
			}
			if (paraName.indexOf("media_type_") == 0) {
				thisMultimedia.setMediatype(request.getParameter(paraName));
			}
			if (paraName.indexOf("multimedia_oldpath_") == 0) {
				thisMultimedia.setOldPath(request.getParameter(paraName));
			}
		}
		
		JSONObject thisResult = new JSONObject();
		try {
			Taxon thisTaxon = this.taxonRepository.findOneById(taxonId);
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			thisMultimedia.setInputer(thisUser.getId());
			thisMultimedia.setInputtime(new Timestamp(System.currentTimeMillis()));
			thisMultimedia.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisMultimedia.setTaxon(thisTaxon);
			thisMultimedia.setStatus(1);
			thisMultimedia.setSynchstatus(0);
			JSONArray handleReferenceToJson = (JSONArray) handleReferenceToJson(request);
			if (handleReferenceToJson.size() > 0) {
				thisMultimedia.setRefjson(handleReferenceToJson.toJSONString());
			}
			this.multimediaRepository.save(thisMultimedia);
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
		int countMultimediaNum = 0;
		String multimediaReferencesPageE = null;
		String multimediaReferencesPageS = null;
		String multimediaReferencesId = null;
		String jsonStr = null;
		
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("countMultimediaReferences_") == 0) {
				countMultimediaNum = Integer.parseInt(request.getParameter(paraName));
			}
			if (paraName.indexOf("multimediaId_") == 0) {
				formNum = Integer.parseInt(paraName.substring(paraName.indexOf("_") + 1));
			}
		}
		for (int i = 1; i <= countMultimediaNum; i++) {
			multimediaReferencesId = request.getParameter("multimediaReferences_" + formNum + "_" + i);
			multimediaReferencesPageS = request.getParameter("multimediaReferencesPageS_" + formNum + "_" + i);
			multimediaReferencesPageE = request.getParameter("multimediaReferencesPageE_" + formNum + "_" + i);
			if (StringUtils.isNotBlank(multimediaReferencesId) && StringUtils.isNotBlank(multimediaReferencesPageS)
					&& StringUtils.isNotBlank(multimediaReferencesPageE)) {
				jsonStr = "{"
						+ "\"refId\"" + ":\"" + multimediaReferencesId + "\","
						+ "\"refS\"" + ":\"" + multimediaReferencesPageS + "\"," 
						+ "\"refE\"" + ":\"" + multimediaReferencesPageE + "\""
						+ "}";
				JSONObject jsonText = JSON.parseObject(jsonStr);
				jsonArray.add(jsonText);
			}
		}
		
		return jsonArray;
	}
	
	@Override
	public boolean logicRemove(String id) {
		Multimedia thisMultimedia = this.multimediaRepository.findOneById(id);
		if (null != thisMultimedia && 1 == thisMultimedia.getStatus()) {
			thisMultimedia.setStatus(0);
			this.multimediaRepository.save(thisMultimedia);
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteOne(HttpServletRequest request) {
		String multimediaId = request.getParameter("multimediaId");
		if (StringUtils.isNotBlank(multimediaId)) {
			if (null != this.multimediaRepository.findOneById(multimediaId)) {
				this.multimediaRepository.deleteOneById(multimediaId);
			}
			return true;
		}
		return false;
	}

	@Override
	public JSON batchImages(String taxonId, HttpServletRequest request, MultipartFile file, String uploadPath) {
		JSONObject thisResult = new JSONObject();
		if (null == file || file.isEmpty()) {
			System.out.println("没有上传文件");
			// 构建返回结果
			thisResult.put("code", ReturnCode.RECORD_NULL.getCode());
			thisResult.put("message", ReturnCode.RECORD_NULL.getMessage_zh());
			thisResult.put("data", "");
		} else {
			// 初始化媒体
			Multimedia thisMultimedia = new Multimedia();
			
			if (file.getSize() > 100 * 1024 * 1024) {
				thisResult.put("code", ReturnCode.FAILURE.getCode());
				thisResult.put("message", ReturnCode.FAILURE.getMessage_zh());
				thisResult.put("data", "");
			}
			String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);	// 后缀名
			String newFileName = UUIDUtils.getUUID32() + "." + suffix;												// 文件名
			String realPath = request.getSession().getServletContext().getRealPath(uploadPath);						// 本地存储路径
			// 构造路径 -- Team/Dataset/Taxon/文件
			String teamId = (String) request.getSession().getAttribute("teamId");									// 当前Team
			String datasetId = (String) request.getSession().getAttribute("datasetID");								// 当前Dataset
			realPath = realPath + teamId + "/" + datasetId + "/" + taxonId + "/";
			
			try {
				// 先把文件保存到本地
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, newFileName));
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("文件保存到本地发生异常:" + e1.getMessage());
				// 构建返回结果
				thisResult.put("code", ReturnCode.FAILURE.getCode());
				thisResult.put("message", ReturnCode.FAILURE.getMessage_zh());
				thisResult.put("data", "");
			}
			
			// 图片经纬度
			String lat = request.getParameter("multimedia_lat");
			if (StringUtils.isNotBlank(lat)) {
				thisMultimedia.setLat(Double.parseDouble(lat));
			}
			String lng = request.getParameter("multimedia_lng");
			if (StringUtils.isNotBlank(lng)) {
				thisMultimedia.setLng(Double.parseDouble(lng));
			}
			
			if (file.getContentType().contains("image")) {
				// 获取经纬度
				try {
					JSONObject thisImageInfo = FileInfo.GetImageInfo(realPath + newFileName);
					// latitude
					thisMultimedia.setLat(thisImageInfo.getDouble("latitude"));
					// longitude
					thisMultimedia.setLng(thisImageInfo.getDouble("longitude"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			thisMultimedia.setId(UUIDUtils.getUUID32());
			thisMultimedia.setSourcesid(request.getParameter("multimediaSourcesid"));
			thisMultimedia.setTitle(request.getParameter("multimedia_title"));
			
			thisMultimedia.setContext(request.getParameter("multimedia_context"));
			thisMultimedia.setRightsholder(request.getParameter("multimedia_rightsholder"));
			thisMultimedia.setOldPath(request.getParameter("multimedia_oldpath"));
			String licenseid = request.getParameter("multimedia_licenseid");
			if (StringUtils.isNotBlank(licenseid)) {
				License thisLicense = this.licenseRepository.findOneById(licenseid);
				thisMultimedia.setLisenceid(thisLicense.getId());
				thisMultimedia.setLicense(thisLicense);
			}
			
			thisMultimedia.setCity(request.getParameter("multimedia_city"));
			thisMultimedia.setCountry(request.getParameter("multimedia_country"));
			thisMultimedia.setCounty(request.getParameter("multimedia_county"));
			
			thisMultimedia.setLocality(request.getParameter("multimedia_locality"));
			thisMultimedia.setLocation(request.getParameter("multimedia_location"));
			thisMultimedia.setProvince(request.getParameter("multimedia_province"));
			
			thisMultimedia.setMediatype(request.getParameter("media_type"));
			thisMultimedia.setPath(teamId + "/" + datasetId + "/" + taxonId + "/" + newFileName);
			thisMultimedia.setSuffix(suffix);
			// 参考文献
			int countMultimediaReferences = Integer.parseInt(request.getParameter("countMultimediaReferences"));	// 参考文献总数量(无用)
			String multimediaReferences = request.getParameter("multimediaReferences");				// 参考文献内容
			
			if (StringUtils.isNotBlank(multimediaReferences)) {
				String[] multimediaReference = multimediaReferences.split(",");
				
				JSONArray jsonArray = new JSONArray();
				String jsonStr = null;
				for (int i = 0; i < multimediaReference.length; i++) {
					String[] context = multimediaReference[i].split("&");
					jsonStr = "{"
							+ "\"refId\"" + ":\"" + context[0] + "\","
							+ "\"refS\"" + ":\"" + context[1] + "\"," 
							+ "\"refE\"" + ":\"" + context[2] + "\""
							+ "}";
					JSONObject jsonText = JSON.parseObject(jsonStr);
					jsonArray.add(jsonText);
				}
				
				thisMultimedia.setRefjson(jsonArray.toJSONString());
			}
			
			this.saveMultimedia(taxonId, thisMultimedia);
			
			// 构建返回结果
			thisResult.put("data", "");
		}
		return thisResult;
	}

	@Override
	public JSON findMultimediaListByTaxonId(String taxonId, HttpServletRequest request) {
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
		Page<Multimedia> thisPage = this.multimediaRepository.searchMultimediasByTaxonId(
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), taxonId);
		
		JSONArray rows = new JSONArray();
		List<Multimedia> thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			String sourcesid = thisList.get(i).getSourcesid();
			if (StringUtils.isNotBlank(sourcesid)) {
				thisList.get(i).setSourcesid(this.datasourceService.findOneById(sourcesid).getTitle());
			}
			String path = thisList.get(i).getPath();
			if (StringUtils.isNotBlank(path) && path.contains(taxonId)) {
				String value = "upload/images/" + thisList.get(i).getPath();
				thisList.get(i).setPath(value);
			}
	
			rows.add(i, thisList.get(i));
		}
		
		thisSelect.put("total", thisPage.getTotalElements());		// NO1：总数
		thisSelect.put("page", offset_serch);						// NO2：offset
		thisSelect.put("rows", rows);								// NO3：Taxon下的所有Citation
		return thisSelect;
	}

	@Override
	public void saveMultimedia(String taxonId, Multimedia thisMultimedia) {
		Taxon thisTaxon = this.taxonRepository.findOneById(taxonId);
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		thisMultimedia.setInputer(thisUser.getId());
		thisMultimedia.setInputtime(new Timestamp(System.currentTimeMillis()));
		thisMultimedia.setSynchdate(new Timestamp(System.currentTimeMillis()));
		thisMultimedia.setTaxon(thisTaxon);
		thisMultimedia.setStatus(1);
		thisMultimedia.setSynchstatus(0);
		
		this.multimediaRepository.save(thisMultimedia);
	}

	@Override
	public JSON editMultimedia(String taxonId) {
		JSONObject multimedias = new JSONObject();
		JSONArray multimediaArr = new JSONArray();
		try {
			if (StringUtils.isNotBlank(taxonId)) {
				List<Multimedia> list = this.multimediaRepository.findMultimediaListByTaxonId(taxonId);
				for (int i = 0; i < list.size(); i++) {
					JSONArray refjson = this.refService.refactoringRef(list.get(i).getRefjson());
					JSONObject json = new JSONObject();
					json.put("id", list.get(i).getId());
					json.put("title", list.get(i).getTitle());
					json.put("mediatype", list.get(i).getMediatype());
					json.put("context", list.get(i).getContext());
					json.put("rightsholder", list.get(i).getRightsholder());

					json.put("city", list.get(i).getCity());
					json.put("country", list.get(i).getCountry());
					json.put("county", list.get(i).getCounty());
					json.put("province", list.get(i).getProvince());
					json.put("location", list.get(i).getLocation());
					json.put("locality", list.get(i).getLocality());
					json.put("lat", list.get(i).getLat());
					json.put("lng", list.get(i).getLng());
					json.put("path", list.get(i).getPath());
					
					json.put("refjson", refjson.toJSONString());
					// 数据源
					String sourcesid = list.get(i).getSourcesid();
					String sourcesTitle = this.datasourceService.findOneById(sourcesid).getTitle();
					json.put("sourcesid", sourcesid);
					json.put("sourcesTitle", sourcesTitle);
					// 共享协议
					String licenseid = list.get(i).getLicense().getId();
					String licenseTitle = this.licenseService.findOneById(licenseid).getTitle();
					json.put("licenseid", licenseid);
					json.put("licenseTitle", licenseTitle);
					
					multimediaArr.add(i, json);
				}
				multimedias.put("multimedias", multimediaArr);
			}
		} catch (Exception e) {
		}
		return multimedias;
	}

	@Override
	public JSON uploadFile(MultipartFile file, String taxasetId, HttpServletRequest request) throws Exception {
		JSONObject thisResult = new JSONObject();
		List<Multimedia> mediaList = new ArrayList<>();				// 上传文件的数据
        UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String[] fileMark = new String[3];
        fileMark[0] = request.getParameter("refFileMark");			// 参考文献文件标记
        fileMark[1] = request.getParameter("dsFileMark");			// 数据源文件标记
        fileMark[2] = request.getParameter("expFileMark");			// 专家文件标记
        /*判断上传文件是否为空及是否是目标文件*/
        if (null == file || file.isEmpty()) {
            thisResult.put("message", "未找到上传的文件，请刷新页面或更换浏览器");
            thisResult.put("status", false);
            thisResult.put("code", -1);
            System.out.println("未找到上传的文件，请刷新页面或更换浏览器");
        }else if (StringUtils.isBlank(fileMark[0]) || StringUtils.isBlank(fileMark[1]) || StringUtils.isBlank(fileMark[2])) {
			thisResult.put("message", "未找到本次上传的对应专家信息、参考文献或数据源，请重新上传【1.专家文献】【2.参考文献】或【3.数据源信息】");
			thisResult.put("status", false);
			thisResult.put("code", -10);
		}else if (file.getSize() > 100 * 1024 * 1024) {
            thisResult.put("message", "上传文件太大");
            thisResult.put("status", false);
            thisResult.put("code", -2);
        } else {
            //先解析EXCEL
            try {
            	Workbook wb =  WorkbookFactory.create(file.getInputStream());
            	Sheet sheet = wb.getSheetAt(0);
            	int rowNum = sheet.getLastRowNum();
                //判断表头是否符合规则
                try {
                    /*String tableHeadArray[] = {"*学名", "*媒体类型", "*注解（图注）", "标签", "*文件地址", "* 数据源", "*审核专家", "*版权所有者", "*版权声明", "*共享协议", "原始链接", 
                    		"创建时间", "创建者", "发布者", "贡献者", "国家", "省/州", "市", "县/区", "小地名", "经度", "纬度", "参考文献"};*/
                    String tableHeadArray[] = {"学名", "媒体类型", "注解", "标签", "文件地址", "数据源", "审核专家", "版权所有者", "版权声明", "共享协议", "原始链接", 
                    		"创建时间", "创建者", "发布者", "贡献者", "国家", "省/州", "市", "县/区", "小地名", "经度", "纬度", "参考文献"};
                    if(this.excelService.judgeRowConsistent(23,tableHeadArray,sheet.getRow(0))){
                        try {
                        	Multimedia thisMultimedia = new Multimedia();		// Multimedia对象
                            Row row = null;										// 记录行
                            for (int i = 1; i <= rowNum; i++) {
                                row = sheet.getRow(i);
                                if (null == sheet.getRow(i)) { 
                                	continue; 									// 空行
                                }else {											// 构建集合对象
                                	String scientificname = excelService.getStringValueFromCell(row.getCell(0));
                                	Taxon thisTaxon = this.taxonRepository.findOneByScientificnameAndTaxasetId(scientificname, taxasetId);
                                	String licenseId = excelService.getStringValueFromCell(row.getCell(9));
                                	License thisLicense = this.licenseRepository.findOneById(licenseId);
                                	thisMultimedia = new Multimedia(
                                			excelService.getStringValueFromCell(row.getCell(1)),
                                			excelService.getStringValueFromCell(row.getCell(2)),
                                			excelService.getStringValueFromCell(row.getCell(3)),
                                			excelService.getStringValueFromCell(row.getCell(4)),
                                			excelService.getStringValueFromCell(row.getCell(5)),
                                			excelService.getStringValueFromCell(row.getCell(6)),
                                			excelService.getStringValueFromCell(row.getCell(7)),
                                			excelService.getStringValueFromCell(row.getCell(8)),
                                			licenseId,
                                			excelService.getStringValueFromCell(row.getCell(10)),
                                			excelService.getStringValueFromCell(row.getCell(11)),
                                			excelService.getStringValueFromCell(row.getCell(12)),
                                			excelService.getStringValueFromCell(row.getCell(13)),
                                			excelService.getStringValueFromCell(row.getCell(14)),
                                			excelService.getStringValueFromCell(row.getCell(15)),
                                			excelService.getStringValueFromCell(row.getCell(16)),
                                			excelService.getStringValueFromCell(row.getCell(17)),
                                			excelService.getStringValueFromCell(row.getCell(18)),
                                			excelService.getStringValueFromCell(row.getCell(19)),
                                			Double.parseDouble(
        											StringUtils.isNotBlank(excelService.getStringValueFromCell(row.getCell(20)))
        													? excelService.getStringValueFromCell(row.getCell(20)) : "0"),
        									Double.parseDouble(
        											StringUtils.isNotBlank(excelService.getStringValueFromCell(row.getCell(21)))
        													? excelService.getStringValueFromCell(row.getCell(21)) : "0"),
                                			excelService.getStringValueFromCell(row.getCell(22)),
                                			thisLicense,
                                			thisTaxon);
                                	mediaList.add(thisMultimedia);
								}	
                            }
                            long start = System.currentTimeMillis();
                            JSONArray verifyListEntity = verifyListEntity(mediaList, thisUser, request, fileMark);
                            long end = System.currentTimeMillis();
                            System.out.println("校验完成：" + (end - start));
                            if (verifyListEntity.size() <= 0) {
                            	thisResult.put("message", "上传成功");
                                thisResult.put("status", true);
                                thisResult.put("code", 1);
                            	System.out.println("上传成功");
							}else {
								thisResult.put("message", "录入数据异常，必填字段不能为空");
                                thisResult.put("status", false);
                                thisResult.put("code", -4);
								thisResult.put("errorData", verifyListEntity);
							}

                        } catch (Exception e) {
                        	e.printStackTrace();
                        }
                    }else{
                    	thisResult.put("message", "表头解析异常，请与模板比对是否完全一致");
                        thisResult.put("status", false);
                        thisResult.put("code", -7);
                        System.out.println("表头解析异常，请与模板比对是否完全一致1");
                    }

                } catch (Exception e) {
                	thisResult.put("message", "表头解析异常，请与模板比对是否完全一致");
                    thisResult.put("status", false);
                    thisResult.put("code", -8);
                    System.out.println("表头解析异常，请与模板比对是否完全一致2");
                    e.printStackTrace();
                }

            } catch (Exception e) {
            	thisResult.put("message", "Excel文件解析失败，请确认文件是否损坏或存在非法脚本");
                thisResult.put("status", false);
                thisResult.put("code", -9);
                System.out.println("Excel文件解析失败，请确认文件是否损坏或存在非法脚本");
                e.printStackTrace();
            }
        }
        return thisResult;
    }

	/**
	 * <b>校验成功的数据是否有重复并为实体初始化唯一标识及部分属性值</b>
	 * <p> 校验通过的数据是否有重复并为实体初始化唯一标识及部分属性值</p>
	 * @author BINZI
	 * @param list
	 * @return
	 */
	private JSONArray verifyListEntity(List<Multimedia> list, User thisUser, HttpServletRequest request, String[] fileMark) {
		JSONArray mediaArr = new JSONArray();
		List<Multimedia> failList = new ArrayList<>();
        boolean flag = true;
        for (int i = 0; i < list.size(); i++) {
        	Multimedia thisMultimedia = list.get(i);
        	boolean mark = true;
        	if (null == thisMultimedia.getTaxon()) {
        		thisMultimedia.setTaxon(new Taxon("<span style='color:red'>未匹配到对应分类单元，请先上传分类单元</span>"));
        		mark = false;
			}
        	String mediatype = thisMultimedia.getMediatype();
        	if (StringUtils.isBlank(mediatype)) {
        		thisMultimedia.setContext("<span style='color:red'>媒体类型不能为空</span>");
        		mark = false;
        	}else if ("图片".equals(mediatype)) {
        		if (StringUtils.isBlank(thisMultimedia.getContext())) {
        			thisMultimedia.setContext("<span style='color:red'>图片注解不能为空</span>");
        			mark = false;
        		};
        		if (StringUtils.isBlank(thisMultimedia.getOldPath())) {
        			thisMultimedia.setOldPath("<span style='color:red'>文件地址不能为空</span> ");
        			mark = false;
        		};
			};
            if (StringUtils.isBlank(thisMultimedia.getRightsholder())) {
            	thisMultimedia.setExpert("<span style='color:red'>版权所有者不能为空</span>");
            	mark = false;
            };
            if (StringUtils.isBlank(thisMultimedia.getCopyright())) {
            	thisMultimedia.setExpert("<span style='color:red'>版权声明不能为空</span>");
            	mark = false;
            };
            if (null == thisMultimedia.getLicense()) {
            	thisMultimedia.setLicense(new License("<span style='color:red'>共享协议不能为空</span>"));
            	mark = false;
            };
            
            /*数据源*/
            if (StringUtils.isBlank(thisMultimedia.getSourcesid())) {
            	thisMultimedia.setSourcesid("<span style='color:red'>数据源不能为空</span>");
            	mark = false;
            }else  {
            	String source = this.baseinfotmpService.findDsIdByFilemarkAndSerialNumAndFileType(fileMark[1], thisMultimedia.getSourcesid(), 1);
            	if (StringUtils.isNotBlank(source)) {
					thisMultimedia.setSourcesid(source);
				}else {
					thisMultimedia.setSourcesid("<span style='color:red'>未找到引用数据源，请检查引用数据是否存在或是否引用错误！</span>");
					mark = false;
				}
			};
			
			/*审核专家*/
			if (StringUtils.isBlank(thisMultimedia.getExpert())) {
				thisMultimedia.setExpert("<span style='color:red'>专家信息不能为空</span>");
				mark = false;
            }else{
            	boolean tmp = thisMultimedia.getExpert().matches("[0-9]*");
            	String expid = null;
            	if (tmp) {
            		expid = this.baseinfotmpService.findExpIdByFilemarkAndSerialNumAndFileType(fileMark[2], 2, thisMultimedia.getExpert());
				}else {
					String expertId = this.expertRepository.findIdByCnName(thisMultimedia.getExpert());
					if (StringUtils.isNotBlank(expertId)) {
						expid = expertId;
					}else {
						thisMultimedia.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
						mark = false;
					}
				}            	
            	
            	if (StringUtils.isNotBlank(expid)) {
            		thisMultimedia.setExpert(expid);
				}else{
					thisMultimedia.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
					mark = false;
				}
            };
            /*共享协议*/
            if (StringUtils.isBlank(thisMultimedia.getLisenceid())) {
            	thisMultimedia.setLisenceid("<span style='color:red'>共享协议不能为空</span>");
            	mark = false;
            }else{
            	License license = this.licenseService.findOneById(thisMultimedia.getLisenceid());
            	if (null == license) {
            		thisMultimedia.setLisenceid("<span style='color:red'>系统数据缺失，未找到对应数据，请联系管理员！</span>");
            		mark = false;
				}else {
					thisMultimedia.setLisenceid(license.getId());
				}
			};
            
            /*比对参考文献*/
        	String refjson = thisMultimedia.getRefjson();
        	List<String> refIds = new ArrayList<>();
        	if (StringUtils.isNotBlank(refjson)) {
        		String[] refStr = refjson.replace("，", ",").split(",");
        		refIds = this.baseinfotmpService.findRefIdByFilemarkAndSerialNumAndFileType(fileMark[0], 0, refStr);
        		if (!refIds.isEmpty()) {
        			JSONArray refjsonArr = this.refService.getRefJSONArrayByIds(refIds);
        			if (!refjsonArr.isEmpty()) {
        				thisMultimedia.setRefjson(refjsonArr.toString());
        			}else {
        				thisMultimedia.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
						mark = false;
					}
        		}else {
        			thisMultimedia.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
					mark = false;
        		}
        	};
        	
        	if (!mark) {
        		thisMultimedia.setSynchstatus(i + 1);
            	failList.add(thisMultimedia);
            	flag = mark;
			}
        }
        /* 构建的集合若通过必填字段的校验，去重，比对数据源、参考文献，补充属性值*/
        if (flag) {
        	List<Multimedia> multimediaList = new ArrayList<>();
        	String teamId = (String) request.getSession().getAttribute("teamId");									// 当前Team
        	String datasetId = (String) request.getSession().getAttribute("datasetID");								// 当前Dataset
            for (int i = 0; i < list.size(); i++) {
            	/*path*/
                String oldPath = list.get(i).getOldPath();
                if (StringUtils.isNotBlank(oldPath)) {
        			list.get(i).setPath(teamId + "/" + datasetId + "/" + list.get(i).getTaxon().getId() + "/" + UUIDUtils.getUUID32() + ".png");
    			}
            	
            	/* 根据学名去重*/
            	String mediatype = list.get(i).getMediatype();
            	switch (mediatype) {
				case "图片":
					list.get(i).setMediatype("1");
					break;
				case "音频":
					list.get(i).setMediatype("2");
					break;
				case "视频":
					list.get(i).setMediatype("3");
					break;
				case "表格":
					list.get(i).setMediatype("4");
					break;
				case "文本文件":
					list.get(i).setMediatype("5");
					break;
				case "地图资源":
					list.get(i).setMediatype("6");
					break;
				}
            	list.get(i).setId(UUIDUtils.getUUID32());
            	list.get(i).setInputer(thisUser.getId());
            	list.get(i).setInputtime(new Timestamp(System.currentTimeMillis()));
            	list.get(i).setSynchdate(new Timestamp(System.currentTimeMillis()));
            	list.get(i).setStatus(1);
            	list.get(i).setSynchstatus(0);
            	multimediaList.add(list.get(i));
				/*this.multimediaRepository.save(list.get(i));*/
            }
            long start = System.currentTimeMillis();
            this.batchInsertService.batchInsertMultimedia(multimediaList);
            long end = System.currentTimeMillis();
            System.out.println("Multimedia批量存储完成：" + (end - start));
        }else {
			for (int i = 0; i < failList.size(); i++) {
				JSONObject json = new JSONObject();
				json.put("num", (i+2));
				json.put("scientificname", failList.get(i).getTaxon().getScientificname());
				json.put("mediatype", failList.get(i).getMediatype());
				json.put("context", failList.get(i).getContext());
				json.put("oldPath", failList.get(i).getOldPath());
				json.put("sourcesid", failList.get(i).getSourcesid());
				json.put("expert", failList.get(i).getExpert());
				json.put("rightsholder", failList.get(i).getRightsholder());
				json.put("copyright", failList.get(i).getCopyright());
				json.put("license", failList.get(i).getLicense().getTitle());
				mediaArr.add(i, json);
			}
		}
        return mediaArr;
    }

	@Override
	public JSON changeBg(HttpServletRequest request, MultipartFile file, String uploadPath) {
		JSONObject thisResult = new JSONObject();
		if (null == file || file.isEmpty()) {
			System.out.println("没有上传文件");
			// 构建返回结果
			thisResult.put("code", -1);
			thisResult.put("message", "没有上传文件");
			thisResult.put("data", "");
		} else {
			if (file.getSize() > 200 * 1024) {
				System.out.println();
				thisResult.put("code", -2);
				thisResult.put("message", "上传图片超过了允许大小");
				thisResult.put("data", "");
			}
			String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);	// 后缀名
			String newFileName = UUIDUtils.getUUID32() + "." + suffix;												// 文件名
			String realPath = request.getSession().getServletContext().getRealPath(uploadPath);						// 本地存储路径
			
			try {
				// 先把文件保存到本地
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, newFileName));
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("文件保存到本地发生异常:" + e1.getMessage());
				// 构建返回结果
				thisResult.put("code", -3);
				thisResult.put("message", "文件保存到本地发生异常");
				thisResult.put("data", "");
			}
			
			try {
				String entityId = request.getParameter("entityId");
				String mark = request.getParameter("mark");
				switch (mark) {
				case "team":
					Team thisTeam = this.teamRepository.findOneById(entityId);
					thisTeam.setBgurl(uploadPath + newFileName);
					this.teamRepository.save(thisTeam);
					break;
				case "dataset":
					Dataset thisDataset = this.datasetRepository.findOneById(entityId);
					thisDataset.setBgurl(uploadPath + newFileName);
					this.datasetRepository.save(thisDataset);
					break;
				case "taxaset":
					Taxaset thisTaxaset = this.taxasetRepository.findOneById(entityId);
					thisTaxaset.setBgurl(uploadPath + newFileName);
					this.taxasetRepository.save(thisTaxaset);
					break;
				case "taxtree":
					Taxtree thisTaxtree = this.taxtreeRepository.findOneById(entityId);
					thisTaxtree.setBgurl(uploadPath + newFileName);
					this.taxtreeRepository.save(thisTaxtree);
					break;
				}
				// 构建返回结果
				thisResult.put("code", 1);
				thisResult.put("message", "切换成功");
				thisResult.put("data", uploadPath + newFileName);
			} catch (Exception e) {
				// 构建返回结果
				thisResult.put("code", 0);
				thisResult.put("message", "切换失败");
				thisResult.put("data", uploadPath + newFileName);
				e.printStackTrace();
			}
		}
		return thisResult;
	}

}
