package org.big.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.big.common.ExcelUtil;
import org.big.common.QueryTool;
import org.big.common.UUIDUtils;
import org.big.entity.*;
import org.big.repository.DescriptionRepository;
import org.big.repository.DescriptiontypeRepository;
import org.big.repository.ExpertRepository;
import org.big.repository.TaxonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class DescriptionServiceImpl implements DescriptionService {
	@Autowired
	private DescriptionRepository descriptionRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private LicenseService licenseService;
	@Autowired
	private TaxonService taxonService;
	@Autowired
	private DescriptiontypeService descriptiontypeService;
	@Autowired
	private DatasourceService datasourceService;
	@Autowired
	private RefService refService;
	@Autowired
	private LanguageService languageService;
	@Autowired
	private ExcelService excelService;
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private BaseinfotmpService baseinfotmpService;
	@Autowired
	private DescriptiontypeRepository descriptiontypeRepository;
	@Autowired
	private ExpertService expertService;
	@Autowired
	private ExpertRepository expertRepository;
	@Autowired
	private BatchInsertService batchInsertService;
	@Override
	public JSON findUploadedDescriptionList(Timestamp timestamp, HttpServletRequest request) {
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
		List<Description> thisList = new ArrayList<>();
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Page<Description> thisPage = this.descriptionRepository.searchInfo(searchText, timestamp, QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), thisUser.getId());
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		String thisSelect = "";
		String thisEdit = "";
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
	        thisSelect="<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
	        thisEdit=
	        	 "<a class=\"wts-table-edit-icon\" onclick=\"editThisObject('" + thisList.get(i).getId() + "','description')\" >" +
	             "<span class=\"glyphicon glyphicon-edit\"></span>" +
	             "</a>&nbsp;&nbsp;" +
	             "<a class=\"wts-table-edit-icon\" onclick=\"removeThisObject('" + thisList.get(i).getId() + "','description')\" >" +
	             "<span class=\"glyphicon glyphicon-remove\"></span>" +
	             "</a>";
			row.put("select", thisSelect);
			if (StringUtils.isNotBlank(thisList.get(i).getDestitle())) {
				row.put("destitle", thisList.get(i).getDestitle());
			}else{
				row.put("destitle", "无标题");
			}
			row.put("describer", thisList.get(i).getDescriber());
			row.put("desdate", thisList.get(i).getDesdate());
			row.put("destypeid", thisList.get(i).getDescriptiontype().getName());
			String licenseid = thisList.get(i).getLicenseid();
			License thisLicense = this.licenseService.findOneById(licenseid);
			row.put("license", thisLicense.getTitle());
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
			row.put("rightsholder", thisList.get(i).getRightsholder());
			row.put("language", this.languageService.handleLanguageDropdown(thisList.get(i).getLanguage()));
			row.put("inputer", this.userService.findbyID(thisList.get(i).getInputer()).getNickname());
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
	public JSON addDescription(String taxonId, HttpServletRequest request) {
		Description thisDescription = new Description();
		// 关系(关系类型)
		Enumeration<String> paraNames = request.getParameterNames();
		String paraName = null;
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("descriptionId_") == 0) {
				thisDescription.setId(request.getParameter(paraName));
			}
			if (paraName.indexOf("destitle_") == 0) {
				thisDescription.setDestitle(request.getParameter(paraName));
			}
			if (paraName.indexOf("describer_") == 0) {
				thisDescription.setDescriber(request.getParameter(paraName));
			}
			if (paraName.indexOf("desdate_") == 0) {
				thisDescription.setDesdate(request.getParameter(paraName));
			}
			if (paraName.indexOf("descontent_") == 0) {
				String content = request.getParameter(paraName);
				if (StringUtils.isNotBlank(content)) {
					thisDescription.setDescontent(content);
				}else {
					thisDescription.setDescontent("无");
				}
			}
			if (paraName.indexOf("descriptionremark_") == 0) {
				thisDescription.setRemark(request.getParameter(paraName));
			}
			if (paraName.indexOf("descriptionsourcesid_") == 0) {
				thisDescription.setSourcesid(request.getParameter(paraName));
			}
			if (paraName.indexOf("relationDes_") == 0) {
				thisDescription.setRelationDes(request.getParameter(paraName));
			}
			if (paraName.indexOf("relationId_") == 0) {
				thisDescription.setRelationId(request.getParameter(paraName));
			}
			if (paraName.indexOf("rightsholder_") == 0) {
				thisDescription.setRightsholder(request.getParameter(paraName));
			}
			if (paraName.indexOf("destypeid_") == 0) {
				thisDescription.setDestypeid(request.getParameter(paraName));
				thisDescription.setDescriptiontype(this.descriptiontypeService.findOneById(request.getParameter(paraName)));
			}
			if (paraName.indexOf("licenseid_") == 0) {
				thisDescription.setLicenseid(request.getParameter(paraName));
			}
			if (paraName.indexOf("language_") == 0) {
				thisDescription.setLanguage(request.getParameter(paraName));
			}
		}
		thisDescription.setInputtime(new Timestamp(System.currentTimeMillis()));
		
		JSONObject thisResult = new JSONObject();
		try {
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			thisDescription.setInputer(thisUser.getId());
			thisDescription.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisDescription.setStatus(1);
			thisDescription.setSynchstatus(0);
			JSONArray handleReferenceToJson = (JSONArray) handleReferenceToJson(request);
			if (handleReferenceToJson.size() > 0) {
				thisDescription.setRefjson(handleReferenceToJson.toJSONString());
			}
			thisDescription.setTaxon(taxonService.findOneById(taxonId));
			
			this.descriptionRepository.save(thisDescription);
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
		int countDescriptionNum = 0;
		String descriptionReferencesPageE = null;
		String descriptionReferencesPageS = null;
		String descriptionReferenceId = null;
		String jsonStr = null;
		
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("countDescriptionReferences_") == 0) {
				countDescriptionNum = Integer.parseInt(request.getParameter(paraName));
			}
			if (paraName.indexOf("descriptionId_") == 0) {
				formNum = Integer.parseInt(paraName.substring(paraName.indexOf("_") + 1));
			}
			
		}
		
		for (int i = 1; i <= countDescriptionNum; i++) {
			descriptionReferenceId = request.getParameter("descriptionReferences_" + formNum + "_" + i);
			descriptionReferencesPageS = request.getParameter("descriptionReferencesPageS_" + formNum + "_" + i);
			descriptionReferencesPageE = request.getParameter("descriptionReferencesPageE_" + formNum + "_" + i);
			if (StringUtils.isNotBlank(descriptionReferenceId) && StringUtils.isNotBlank(descriptionReferencesPageS)
					&& StringUtils.isNotBlank(descriptionReferencesPageE)) {
				jsonStr = "{" 
						+ "\"refId\"" + ":\"" + descriptionReferenceId + "\","
						+ "\"refS\"" + ":\"" + descriptionReferencesPageS + "\"," 
						+ "\"refE\"" + ":\"" + descriptionReferencesPageE + "\""
						+ "}";
				JSONObject jsonText = JSON.parseObject(jsonStr);
				jsonArray.add(jsonText);
			}
		}
		
		return jsonArray;
	}

	@Override
	public boolean logicRemove(String id) {
		Description thisDescription = this.descriptionRepository.findOneById(id);
		if (null != thisDescription && 1 == thisDescription.getStatus()) {
			thisDescription.setStatus(0);
			this.descriptionRepository.save(thisDescription);
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteOne(HttpServletRequest request) {
		String descriptionId = request.getParameter("descriptionId");
		if (StringUtils.isNotBlank(descriptionId)) {
			if (null != this.descriptionRepository.findOneById(descriptionId)) {
				this.descriptionRepository.deleteOneById(descriptionId);
			}
			return true;
		}
		return false;
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
		List<Description> thisList = new ArrayList<>();
		Page<Description> thisPage = this.descriptionRepository.searchByDescriptionInfo(findText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisSelect.put("total_count", thisPage.getTotalElements());
		Boolean incompleteResulte = true;
		if ((thisPage.getTotalElements() / 30) > findPage)
			incompleteResulte = false;
		thisSelect.put("incompleteResulte", incompleteResulte);
		thisList = thisPage.getContent();

		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			row.put("id", thisList.get(i).getId());
			row.put("text", thisList.get(i).getDestitle());
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public JSON findDescriptionListByTaxonId(String taxonId, HttpServletRequest request) {
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
		Page<Description> thisPage = this.descriptionRepository.searchDescriptionsByTaxonId(
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), taxonId);
		
		JSONArray rows = new JSONArray();
		List<Description> thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			String sourcesid = thisList.get(i).getSourcesid();
			if (StringUtils.isNotBlank(sourcesid)) {
				thisList.get(i).setSourcesid(this.datasourceService.findOneById(sourcesid).getTitle());
			}
			String licenseid = thisList.get(i).getLicenseid();
			if (StringUtils.isNotBlank(licenseid)) {
				thisList.get(i).setLicenseid(this.licenseService.findOneById(licenseid).getTitle());
			}

			String language = thisList.get(i).getLanguage();
			thisList.get(i).setLanguage(this.languageService.handleLanguageDropdown(language));
			/*thisList.get(i).setLanguage(this.languageService.findLanguageByCode(thisList.get(i).getLanguage()));*/
			rows.add(i, thisList.get(i));
		}
		
		thisSelect.put("total", thisPage.getTotalElements());		// NO1：总数
		thisSelect.put("page", offset_serch);						// NO2：offset
		thisSelect.put("rows", rows);								// NO3：Taxon下的所有Citation
		return thisSelect;
	}

	@Override
	public Description findOneById(String desid) {
		return this.descriptionRepository.findOneById(desid);
	}

	@Override
	public JSON editDescriptions(String taxonId) {
		JSONObject descriptions = new JSONObject();
		JSONArray descriptionArr = new JSONArray();
		
		try {
			if (StringUtils.isNotBlank(taxonId)) {
				List<Description> list = this.descriptionRepository.findDescriptionListByTaxonId(taxonId);
				for (int i = 0; i < list.size(); i++) {
					JSONArray refjson = this.refService.refactoringRef(list.get(i).getRefjson());
					JSONObject json = new JSONObject();
					json.put("id", list.get(i).getId());
					json.put("destitle", list.get(i).getDestitle());
					// 描述类型
					String destypeid = list.get(i).getDestypeid();
					String destypeName = this.descriptiontypeService.findOneById(destypeid).getName();
					json.put("destypeid", destypeid);
					json.put("destypeName", destypeName);
					// 关系
					try {
						String relationDes = list.get(i).getRelationDes();
						if (StringUtils.isNotBlank(relationDes) && !"-1".equals(relationDes)) {
							String destitle = this.descriptionRepository.findOneById(relationDes).getDestitle();
							json.put("relationDes", relationDes);
							json.put("relationDestitle", destitle);
						}else {
							json.put("relationDes", "-1");
							json.put("relationDestitle", "无");
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("关联描述为空");
					}
					
					json.put("relationId", list.get(i).getRelationId());
					// 数据源
					String sourcesid = list.get(i).getSourcesid();
					String sourcesTitle = this.datasourceService.findOneById(sourcesid).getTitle();
					json.put("sourcesid", sourcesid);
					json.put("sourcesTitle", sourcesTitle);
					// 共享协议
					String licenseid = list.get(i).getLicenseid();
					String licenseTitle = this.licenseService.findOneById(licenseid).getTitle();
					json.put("licenseid", licenseid);
					json.put("licenseTitle", licenseTitle);
					
					json.put("desdate", list.get(i).getDesdate());
					json.put("describer", list.get(i).getDescriber());
					json.put("rightsholder", list.get(i).getRightsholder());
					json.put("descontent", list.get(i).getDescontent());
					json.put("remark", list.get(i).getRemark());
					json.put("refjson", refjson.toJSONString());
					json.put("language", list.get(i).getLanguage());
					descriptionArr.add(i, json);
				}
				descriptions.put("descriptions", descriptionArr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("描述：" + descriptions.toJSONString());
		return descriptions;
	}

	@Override
	public JSON findDescListByTaxonId(String taxonId, HttpServletRequest request) {
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
		List<Description> thisList = new ArrayList<>();
		Page<Description> thisPage = this.descriptionRepository.searchDescListByTaxonId(findText, taxonId,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisSelect.put("total_count", thisPage.getTotalElements());
		Boolean incompleteResulte = true;
		if ((thisPage.getTotalElements() / 30) > findPage)
			incompleteResulte = false;
		thisSelect.put("incompleteResulte", incompleteResulte);
		thisList = thisPage.getContent();

		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			row.put("id", thisList.get(i).getId());
			row.put("text", thisList.get(i).getDescriptiontype().getName() + "(" + thisList.get(i).getDestitle() + ")");
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public void export(HttpServletResponse response) throws IOException{
		String [] columnName = { "*学名", "描述时间", "描述人", "*语言", "*描述类型", "*描述内容", "备注", "*参考文献", "*数据源",
				"*审核专家", "*版权所有者", "*版权声明", "*共享协议" };
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Description> thisList = this.descriptionRepository.findDescriptionListByUserId(thisUser.getId());
		
		// 第1步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第2步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("描述信息");

		// 第3.1步，创建表头的列
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < columnName.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(columnName[i]);
			cell.setCellStyle(ExcelUtil.setTitleStyle(wb));
		}

		sheet.setColumnWidth(0, 10 * 256);
		sheet.setColumnWidth(1, 12 * 256);
		sheet.setColumnWidth(2, 12 * 256);
		sheet.setColumnWidth(3, 20 * 256);
		sheet.setColumnWidth(4, 10 * 256);
		sheet.setColumnWidth(5, 20 * 256);
		sheet.setColumnWidth(6, 20 * 256);
		sheet.setColumnWidth(7, 12 * 256);
		sheet.setColumnWidth(8, 12 * 256);
		sheet.setColumnWidth(9, 15 * 256);
		sheet.setColumnWidth(10, 15 * 256);
		sheet.setColumnWidth(11, 12 * 256);
		sheet.setColumnWidth(12, 15 * 256);

		// 第3.2步，创建单元格，并设置值
		for (int i = 0; i < thisList.size(); i++) {
			HSSFRow rows = sheet.createRow((int) (i + 1));
			Description thisDescription = thisList.get(i);
			rows.createCell(0).setCellValue(thisDescription.getTaxon().getChname()); // 设置第1列序号
			rows.createCell(1).setCellValue(thisDescription.getDesdate());
			rows.createCell(2).setCellValue(thisDescription.getDescriber());
			rows.createCell(3).setCellValue(this.languageService.handleLanguageDropdown(thisDescription.getLanguage()));
			rows.createCell(4).setCellValue(thisDescription.getDescriptiontype().getName());
			rows.createCell(5).setCellValue(thisDescription.getDescontent());
			rows.createCell(6).setCellValue(thisDescription.getRemark());
			rows.createCell(7).setCellValue(thisDescription.getRefjson());
			rows.createCell(8).setCellValue(thisDescription.getSourcesid());
			rows.createCell(9).setCellValue(thisDescription.getExpert());
			rows.createCell(10).setCellValue(thisDescription.getRightsholder());
			rows.createCell(11).setCellValue(thisDescription.getdCopyright());
			rows.createCell(12).setCellValue(this.licenseService.findOneById(thisDescription.getLicenseid()).getTitle());
		}
		// 第4步，将文件存到浏览器设置的下载位置
		// 告诉浏览器用什么软件可以打开此文件
		response.setHeader("content-Type", "application/vnd.ms-excel");
		// 下载文件的默认名称
		response.setHeader("Content-Disposition", "attachment; filename=" + new String("描述信息".getBytes("gb2312"), "iso8859-1") + ".xls");
		OutputStream out = response.getOutputStream();
		try {
			wb.write(out);// 将数据写出去
		} catch (Exception e) {
		} finally {
			out.close();
		}
	}

	@Override
	public JSON uploadFile(MultipartFile file, String taxasetId, HttpServletRequest request) throws Exception {
		JSONObject thisResult = new JSONObject();
		List<Description> descriptionList = new ArrayList<>();				// 上传文件的数据
		
		String refFileMark = request.getParameter("refFileMark");			// 参考文献文件标记
		String dsFileMark = request.getParameter("dsFileMark");				// 数据源文件标记
		String expFileMark = request.getParameter("expFileMark");			// 专家文件标记
        UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        /*判断上传文件是否为空及是否是目标文件*/
        if (null == file || file.isEmpty()) {
            thisResult.put("message", "未找到上传的文件，请刷新页面或更换浏览器");
            thisResult.put("status", false);
            thisResult.put("code", -1);
            System.out.println("未找到上传的文件，请刷新页面或更换浏览器");
        }else if (StringUtils.isBlank(refFileMark) || StringUtils.isBlank(dsFileMark) || StringUtils.isBlank(expFileMark)) {
			thisResult.put("message", "未找到本次上传的对应专家信息、参考文献或数据源，请重新上传【1.专家文献】【2.参考文献】或【3.数据源信息】");
			thisResult.put("status", false);
			thisResult.put("code", -10);
		}else {
        	//先解析EXCEL
			InputStream in = file.getInputStream();
            try {
            	Workbook wb =  WorkbookFactory.create(in);
            	in.close(); 
            	/*Workbook wb =  ExcelUtil.getWorkBook(file);*/
            	Sheet sheet = wb.getSheetAt(0);
            	int rowNum = sheet.getLastRowNum();
                //判断表头是否符合规则
                try {
					/*String tableHeadArray[] = { "*学名", "描述时间", "描述人", "*语言", "*描述类型", "*描述内容", "备注", "*参考文献", "*数据源",
							"*审核专家", "*版权所有者", "*版权声明", "*共享协议" };*/
					String tableHeadArray[] = { "学名", "描述时间", "描述人", "语言", "描述类型", "描述内容", "备注", "参考文献", "数据源",
							"审核专家", "版权所有者", "版权声明", "共享协议" };
                    if(this.excelService.judgeRowConsistent(13,tableHeadArray,sheet.getRow(0))){
                        try {
                        	Description thisDescription = new Description();				// Citation对象
                            Row row = null;										// 记录行
                            for (int i = 1; i <= rowNum; i++) {
                                row = sheet.getRow(i);
                                if (null == sheet.getRow(i)) { 
                                	continue; 									// 空行
                                }else {											// 构建集合对象
                                	String scientificname = excelService.getStringValueFromCell(row.getCell(0));
                                	Taxon thisTaxon = this.taxonRepository.findOneByScientificnameAndTaxasetId(scientificname, taxasetId);
                                	thisDescription = new Description(
                                			excelService.getStringValueFromCell(row.getCell(1)),
                                			excelService.getStringValueFromCell(row.getCell(2)),
                                			this.languageService.getValueofLanguage(excelService.getStringValueFromCell(row.getCell(3))),
                                			excelService.getStringValueFromCell(row.getCell(4)),
                                			excelService.getStringValueFromCell(row.getCell(5)),
                                			excelService.getStringValueFromCell(row.getCell(6)),
                                			excelService.getStringValueFromCell(row.getCell(7)),
                                			excelService.getStringValueFromCell(row.getCell(8)),
                                			excelService.getStringValueFromCell(row.getCell(9)),
                                			excelService.getStringValueFromCell(row.getCell(10)),
                                			excelService.getStringValueFromCell(row.getCell(11)),
                                			excelService.getStringValueFromCell(row.getCell(12)),
                                			thisTaxon);
									
                                	descriptionList.add(thisDescription);
								}	
                            }
                            long start = System.currentTimeMillis();
                            JSONArray verifyListEntity = verifyListEntity(descriptionList, thisUser, refFileMark, dsFileMark, expFileMark);
                            long end = System.currentTimeMillis();
                            System.out.println("校验及上传结束：" + (end - start));
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
                	e.printStackTrace();
                	thisResult.put("message", "表头解析异常，请与模板比对是否完全一致");
                    thisResult.put("status", false);
                    thisResult.put("code", -8);
                    System.out.println("表头解析异常，请与模板比对是否完全一致2");
                }

            } catch (Exception e) {
            	e.printStackTrace();
            	thisResult.put("message", "Excel文件解析失败，请确认文件是否损坏或存在非法脚本");
                thisResult.put("status", false);
                thisResult.put("code", -9);
                System.out.println("Excel文件解析失败，请确认文件是否损坏或存在非法脚本");
            }finally {
            	in.close();
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
	 * @throws Exception 
	 */
	private JSONArray verifyListEntity(List<Description> list, User thisUser, String refFileMark, String dsFileMark, String expFileMark) throws Exception {
		JSONArray descArr = new JSONArray();
		List<Description> failList = new ArrayList<>();
        boolean flag = true;
        for (int i = 0; i < list.size(); i++) {
        	Description thisDescription = list.get(i);
        	boolean mark = true;
        	if (null == thisDescription.getTaxon()) {
        		thisDescription.setTaxon(new Taxon("<span style='color:red'>未匹配到对应分类单元，请先上传分类单元</span>"));
        		mark = false;
			}
        	if (StringUtils.isBlank(thisDescription.getLanguage())) {
        		thisDescription.setLanguage("<span style='color:red'>描述语言不能为空</span> ");
        		mark = false;
            };
            
            if (StringUtils.isBlank(thisDescription.getDestypeid())) {
            	thisDescription.setDestypeid("<span style='color:red'>描述类型不能为空</span>");
            	mark = false;
            }else{
            	Descriptiontype type = this.descriptiontypeRepository.findOneByName(thisDescription.getDestypeid());
            	String descriptionTypeUUID = UUIDUtils.getUUID32();
            	if (null == type) {
            		Descriptiontype descriptiontype = new Descriptiontype(descriptionTypeUUID, thisDescription.getDestypeid(), "0");
            		thisDescription.setDescriptiontype(descriptiontype);
            		thisDescription.setDestypeid(descriptionTypeUUID);
				}else {
					thisDescription.setDescriptiontype(type);
					thisDescription.setDestypeid(type.getId());
				}
			};
            
            if (StringUtils.isBlank(thisDescription.getDescontent())) {
            	thisDescription.setDescontent("<span style='color:red'>描述内容不能为空</span>");
            	mark = false;
            };
            if (StringUtils.isBlank(thisDescription.getRightsholder())) {
            	thisDescription.setRightsholder("<span style='color:red'>权利所有人不能为空</span>");
            	mark = false;
            };
            if (StringUtils.isBlank(thisDescription.getdCopyright())) {
            	thisDescription.setdCopyright("<span style='color:red'>*版权声明不能为空</span>");
            	mark = false;
            };
            /*共享协议*/
            if (StringUtils.isBlank(thisDescription.getLicenseid())) {
            	thisDescription.setLicenseid("<span style='color:red'>共享协议不能为空</span>");
            	mark = false;
            }else{
            	License license = this.licenseService.findOneById(thisDescription.getLicenseid());
            	if (null == license) {
            		thisDescription.setLicenseid("<span style='color:red'>系统数据缺失，未找到对应数据，请联系管理员！</span>");
            		mark = false;
				}else {
					thisDescription.setLicenseid(license.getId());
				}
			};
            
            /*数据源*/
            if (StringUtils.isBlank(thisDescription.getSourcesid())) {
            	thisDescription.setSourcesid("<span style='color:red'>数据源不能为空</span>");
            	mark = false;
            }else  {
            	String source = this.baseinfotmpService.findDsIdByFilemarkAndSerialNumAndFileType(dsFileMark, thisDescription.getSourcesid(), 1);
            	if (StringUtils.isNotBlank(source)) {
					thisDescription.setSourcesid(source);
				}else {
					thisDescription.setSourcesid("<span style='color:red'>未找到引用数据源，请检查引用数据存在或是否引用错误！</span>");
					mark = false;
				}
			};
			
			/*审核专家*/
			if (StringUtils.isBlank(thisDescription.getExpert())) {
				thisDescription.setExpert("<span style='color:red'>专家信息不能为空</span>");
				mark = false;
            }else{
            	boolean tmp = thisDescription.getExpert().matches("[0-9]*");
            	String expid = null;
            	if (tmp) {
            		expid = this.baseinfotmpService.findExpIdByFilemarkAndSerialNumAndFileType(expFileMark, 2, thisDescription.getExpert());
				}else {
					String expertId = this.expertRepository.findIdByCnName(thisDescription.getExpert());
					if (StringUtils.isNotBlank(expertId)) {
						expid = expertId;
					}else {
						thisDescription.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
						mark = false;
					}
				}            	
            	
            	if (StringUtils.isNotBlank(expid)) {
            		thisDescription.setExpert(expid);
				}else{
					thisDescription.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
					mark = false;
				}
            };
            
            /*比对参考文献*/
        	String refjson = thisDescription.getRefjson();
        	List<String> refIds = new ArrayList<>();
        	if (StringUtils.isNotBlank(refjson)) {
        		String[] refStr = refjson.replace("，", ",").split(",");
        		refIds = this.baseinfotmpService.findRefIdByFilemarkAndSerialNumAndFileType(refFileMark, 0, refStr);
        		if (!refIds.isEmpty()) {
        			JSONArray refjsonArr = this.refService.getRefJSONArrayByIds(refIds);
        			if (!refjsonArr.isEmpty()) {
        				thisDescription.setRefjson(refjsonArr.toString());
        			}else {
        				thisDescription.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
						mark = false;
					}
        		}else {
        			thisDescription.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
					mark = false;
        		}
        	};
        	
        	if (!mark) {
        		thisDescription.setSynchstatus(i + 1);
            	failList.add(thisDescription);
            	flag = mark;
			}
        }
        /* 构建的集合若通过必填字段的校验，去重，比对数据源、参考文献，补充属性值*/
        if (flag) {
        	List<Description> descriptionList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
            	/* 根据学名去重*/
            	list.get(i).setDestitle(list.get(i).getDescriptiontype().getName());
            	list.get(i).setId(UUIDUtils.getUUID32());
            	list.get(i).setInputer(thisUser.getId());
            	list.get(i).setInputtime(new Timestamp(System.currentTimeMillis()));
            	list.get(i).setSynchdate(new Timestamp(System.currentTimeMillis()));
            	list.get(i).setStatus(1);
            	list.get(i).setSynchstatus(0);
            	list.get(i).setSynchstatus(0);
				/*this.descriptionRepository.save(list.get(i));*/
            	descriptionList.add(list.get(i));
            }
            long start = System.currentTimeMillis();
            this.batchInsertService.batchInsertDescription(descriptionList);
            System.out.println("上传条数：" + descriptionList.size());
            long end = System.currentTimeMillis();
            System.out.println("Description批量存储完成：" + (end - start));
        }else {
			for (int i = 0; i < failList.size(); i++) {
				JSONObject json = new JSONObject();
				json.put("num", failList.get(i).getSynchstatus());
				json.put("scientificname", failList.get(i).getTaxon().getScientificname());
				json.put("language", failList.get(i).getLanguage());
				json.put("destypeid", failList.get(i).getDestypeid());
				json.put("descontent", failList.get(i).getDescontent());
				json.put("sourcesid", failList.get(i).getSourcesid());
				json.put("expert", failList.get(i).getExpert());
				json.put("rightsholder", failList.get(i).getRightsholder());
				json.put("dCopyright", failList.get(i).getdCopyright());
				json.put("licenseid", failList.get(i).getLicenseid());
				descArr.add(i, json);
			}
		}
        return descArr;
    }

	@Override
	public JSON getDescContentById(String descid) {
		JSONObject thisResult = new JSONObject();
		try {
			Description thisDescription = this.descriptionRepository.findOneById(descid);
			thisResult.put("distitle", thisDescription.getDestitle());
			thisResult.put("disdate", thisDescription.getDesdate());
			thisResult.put("discriber", thisDescription.getDescriber());
			thisResult.put("disrightsholder", thisDescription.getRightsholder());
			thisResult.put("discontent", thisDescription.getDescontent());
			thisResult.put("dislanguage", thisDescription.getLanguage());
			
			// 共享协议
			String licenseid = thisDescription.getLicenseid();
			String licenseTitle = this.licenseService.findOneById(licenseid).getTitle();
			thisResult.put("licenseid", licenseid);
			thisResult.put("licenseTitle", licenseTitle);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return thisResult;
	}
}