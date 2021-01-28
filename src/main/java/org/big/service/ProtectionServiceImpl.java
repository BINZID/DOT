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
import org.big.entity.Expert;
import org.big.entity.Protection;
import org.big.entity.Protectstandard;
import org.big.entity.Taxon;
import org.big.entity.User;
import org.big.entity.UserDetail;
import org.big.repository.ExpertRepository;
import org.big.repository.ProtectionRepository;
import org.big.repository.ProtectstandardRepository;
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
public class ProtectionServiceImpl implements ProtectionService {
	@Autowired
	private ProtectionRepository protectionRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private TaxonService taxonService;
	@Autowired
	private ProtectstandardRepository protectstandardRepository;
	@Autowired
	private DatasourceService datasourceService;
	@Autowired
	private RefService refService;
	@Autowired
	private ExcelService excelService;
	@Autowired
	private BaseinfotmpService baseinfotmpService;
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private ExpertService expertService;
	@Autowired
	private ExpertRepository expertRepository;
	@Autowired
	private BatchInsertService batchInsertService;
	
	@Override
	public JSON findUploadedProtectionList(Timestamp timestamp, HttpServletRequest request) {
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
		List<Protection> thisList = new ArrayList<>();
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Page<Protection> thisPage = this.protectionRepository.searchInfo(searchText, timestamp, QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), thisUser.getId());
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		String thisSelect = "";
		String thisEdit = "";
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
	        thisSelect="<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
	        thisEdit=
	        	 "<a class=\"wts-table-edit-icon\" onclick=\"editThisObject('" + thisList.get(i).getId() + "','protection')\" >" +
	             "<span class=\"glyphicon glyphicon-edit\"></span>" +
	             "</a> &nbsp;&nbsp;&nbsp;" +
	             "<a class=\"wts-table-edit-icon\" onclick=\"removeThisObject('" + thisList.get(i).getId() + "','protection')\" >" +
	             "<span class=\"glyphicon glyphicon-remove\"></span>" +
	             "</a>";
			row.put("select", thisSelect);
			Protectstandard thisProtectstandard = this.protectionRepository.findOneById(thisList.get(i).getId()).getProtectstandard();
			row.put("standardname", thisProtectstandard.getStandardname());
			row.put("version", thisProtectstandard.getVersion());
			row.put("protlevel", thisList.get(i).getProtlevel());
			row.put("inputer", this.userService.findbyID(thisList.get(i).getInputer()).getNickname());
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
		return thisTable;
    }
	@Override
	public JSON addProtection(String taxonId, HttpServletRequest request) {
		Protection thisProtection = new Protection();
		Enumeration<String> paraNames = request.getParameterNames();
		String protectstandardId = null;
		String paraName = null;
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("protectionId_") == 0) {
				thisProtection.setId(request.getParameter(paraName));
			}
			if (paraName.indexOf("protlevel_") == 0) {
				thisProtection.setProtlevel(request.getParameter(paraName));
				protectstandardId = request.getParameter(paraName);
			}
			if (paraName.indexOf("protectionsourcesid_") == 0) {
				thisProtection.setSourcesid(request.getParameter(paraName));
			}
			if (paraName.indexOf("proassessment_") == 0) {
				thisProtection.setProassessment(request.getParameter(paraName));
			}
		}
		thisProtection.setInputtime(new Timestamp(System.currentTimeMillis()));
		
		JSONObject thisResult = new JSONObject();
		try {
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			thisProtection.setInputer(thisUser.getId());
			thisProtection.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisProtection.setStatus(1);
			thisProtection.setSynchstatus(0);
			JSONArray handleReferenceToJson = (JSONArray) handleReferenceToJson(request);
			if (handleReferenceToJson.size() > 0) {
				thisProtection.setRefjson(handleReferenceToJson.toJSONString());
			}
			thisProtection.setTaxon(taxonService.findOneById(taxonId));
			Protectstandard thisProtectstandard = protectstandardRepository.findOneById(protectstandardId);
			if (null != thisProtectstandard) {
				thisProtection.setProtectstandard(thisProtectstandard);
			}
			
			this.protectionRepository.save(thisProtection);
			thisResult.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			thisResult.put("result", false);
		}
		return thisResult;
	}

	@Override
	public JSON handleReferenceToJson(HttpServletRequest request) {
		Enumeration<String> paraNames = request.getParameterNames();
		JSONArray jsonArray = new JSONArray();
		
		String paraName = null;
		int formNum = 0;
		int countProtectionNum = 0;
		String protectionReferencesPageE = null;
		String protectionReferencesPageS = null;
		String protectionReferenceId = null;
		String jsonStr = null;
		
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("countProtectionReferences_") == 0) {
				countProtectionNum = Integer.parseInt(request.getParameter(paraName));
			}	
			if (paraName.indexOf("protectionId_") == 0) {
				formNum = Integer.parseInt(paraName.substring(paraName.indexOf("_") + 1));
			}
		}
		
		for (int i = 1; i <= countProtectionNum; i++) {
			protectionReferenceId = request.getParameter("protectionReferences_" + formNum + "_" + i);
			protectionReferencesPageS = request.getParameter("protectionReferencesPageS_" + formNum + "_" + i);
			protectionReferencesPageE = request.getParameter("protectionReferencesPageE_" + formNum + "_" + i);
			if (StringUtils.isNotBlank(protectionReferenceId) && StringUtils.isNotBlank(protectionReferencesPageS)
					&& StringUtils.isNotBlank(protectionReferencesPageE)) {
				jsonStr = "{" 
						+ "\"refId\"" + ":\"" + protectionReferenceId + "\","
						+ "\"refS\"" + ":\"" + protectionReferencesPageS + "\"," 
						+ "\"refE\"" + ":\"" + protectionReferencesPageE + "\""
						+ "}";
				JSONObject jsonText = JSON.parseObject(jsonStr);
				jsonArray.add(jsonText);
			}
		}
		
		return jsonArray;
	}
	@Override
	public boolean logicRemove(String id) {
		Protection thisProtection = this.protectionRepository.findOneById(id);
		if (null != thisProtection && 1 == thisProtection.getStatus()) {
			thisProtection.setStatus(0);
			this.protectionRepository.save(thisProtection);
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
		List<Protection> thisList = new ArrayList<>();
		// 获取当前选中Taxon下的Proteciton
		Page<Protection> thisPage = this.protectionRepository.searchByProtlevel(findText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisSelect.put("total_count", thisPage.getTotalElements());
		Boolean incompleteResulte = true;
		if ((thisPage.getTotalElements() / 30) > findPage) {
			incompleteResulte = false;
		}
		thisSelect.put("incompleteResulte", incompleteResulte);
		thisList = thisPage.getContent();
		/*if (findPage == 1) {
			JSONObject row = new JSONObject();
			row.put("id", "addNew");
			row.put("text", "新建一个分类单元集");
			items.add(row);
		}*/
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			row.put("id", thisList.get(i).getId());
			row.put("text", thisList.get(i).getProtlevel());
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}
	@Override
	public boolean deleteOne(HttpServletRequest request) {
		String protectionId = request.getParameter("protectionId");
		if (StringUtils.isNotBlank(protectionId)) {
			if (null != this.protectionRepository.findOneById(protectionId)) {
				this.protectionRepository.deleteOneById(protectionId);
			}
			return true;
		}
		return false;
	}
	@Override
	public JSON findProtectionListByTaxonId(String taxonId, HttpServletRequest request) {
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
		Page<Protection> thisPage = this.protectionRepository.searchProtectionsByTaxonId(
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), taxonId);
		
		JSONArray rows = new JSONArray();
		List<Protection> thisList = thisPage.getContent();
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
	public JSON editProtection(String taxonId) {
		JSONObject protections = new JSONObject();
		JSONArray protectionArr = new JSONArray();
		try {
			if (StringUtils.isNotBlank(taxonId)) {
				List<Protection> list = this.protectionRepository.findProtectionListByTaxonId(taxonId);
				for (int i = 0; i < list.size(); i++) {
					JSONArray refjson = this.refService.refactoringRef(list.get(i).getRefjson());
					JSONObject json = new JSONObject();
					json.put("id", list.get(i).getId());
					json.put("proassessment", list.get(i).getProassessment());
					json.put("protectstandard", list.get(i).getProtectstandard());
					json.put("refjson", refjson.toJSONString());
					// 数据源
					String sourcesid = list.get(i).getSourcesid();
					String sourcesTitle = this.datasourceService.findOneById(sourcesid).getTitle();
					json.put("sourcesid", sourcesid);
					json.put("sourcesTitle", sourcesTitle);
					protectionArr.add(i, json);
				}
				protections.put("protections", protectionArr);
			}
		} catch (Exception e) {
		}
		return protections;
	}
	@Override
	public void export(HttpServletResponse response) throws IOException {
		String [] columnName = { "*学名", "*保护标准", "*标准版本", "*保护级别", "评估依据", "*数据源", "参考文献", "*审核专家",
				"版权所有者", "版权声明", "共享协议" };
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Protection> thisList = this.protectionRepository.findProtectionListByUserId(thisUser.getId());
		
		// 第1步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第2步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("保护数据");

		// 第3.1步，创建表头的列
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < columnName.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(columnName[i]);
			cell.setCellStyle(ExcelUtil.setTitleStyle(wb));
		}

		sheet.setColumnWidth(0, 10 * 256);
		sheet.setColumnWidth(1, 14 * 256);
		sheet.setColumnWidth(2, 12 * 256);
		sheet.setColumnWidth(3, 14 * 256);
		sheet.setColumnWidth(4, 14 * 256);
		sheet.setColumnWidth(5, 14 * 256);
		sheet.setColumnWidth(6, 14 * 256);
		sheet.setColumnWidth(7, 14 * 256);

		// 第3.2步，创建单元格，并设置值
		for (int i = 0; i < thisList.size(); i++) {
			HSSFRow rows = sheet.createRow((int) (i + 1));
			Protection thisProtection = thisList.get(i);
			rows.createCell(0).setCellValue(thisProtection.getTaxon().getChname()); 
			Protectstandard thisProtectstandard = thisProtection.getProtectstandard();
			rows.createCell(1).setCellValue(thisProtectstandard.getStandardname());
			rows.createCell(2).setCellValue(thisProtectstandard.getVersion());
			rows.createCell(3).setCellValue(thisProtectstandard.getProtlevel());
			rows.createCell(4).setCellValue(thisProtection.getProassessment());
			rows.createCell(5).setCellValue(thisProtection.getSourcesid());
			rows.createCell(6).setCellValue(thisProtection.getRefjson());
			rows.createCell(7).setCellValue(thisProtection.getExpert());
		}
		// 第4步，将文件存到浏览器设置的下载位置
		// 告诉浏览器用什么软件可以打开此文件
		response.setHeader("content-Type", "application/vnd.ms-excel");
		// 下载文件的默认名称
		response.setHeader("Content-Disposition", "attachment; filename=" + new String("保护数据".getBytes("gb2312"), "iso8859-1") + ".xls");
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
		List<Protection> protectionList = new ArrayList<>();							// 上传文件的数据
        UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String[] fileMark = new String[3];
        fileMark[0] = request.getParameter("refFileMark");			// 参考文献文件标记
        fileMark[1] = request.getParameter("dsFileMark");				// 数据源文件标记
        fileMark[2] = request.getParameter("expFileMark");			// 专家文件标记
    	String[] protInfo = new String[3];
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
		}else {
            //先解析EXCEL
        	InputStream in = file.getInputStream();
            try {
            	Workbook wb =  WorkbookFactory.create(in);
            	Sheet sheet = wb.getSheetAt(0);
            	int rowNum = sheet.getLastRowNum();
                //判断表头是否符合规则
                try {
					/*String tableHeadArray[] = { "*学名", "*保护标准", "*标准版本", "*保护级别", "评估依据", "*数据源", "参考文献", "*审核专家",
							"版权所有者", "版权声明", "共享协议" };*/
					String tableHeadArray[] = { "学名", "保护标准", "标准版本", "保护级别", "评估依据", "数据源", "参考文献", "审核专家",
							"版权所有者", "版权声明", "共享协议" };
                    if(this.excelService.judgeRowConsistent(11,tableHeadArray,sheet.getRow(0))){
                        try {
                        	Protection thisProtection = new Protection();		// Protection对象
                            Row row = null;										// 记录行
                            for (int i = 1; i <= rowNum; i++) {
                                row = sheet.getRow(i);
                                if (null == sheet.getRow(i)) { 
                                	continue; 									// 空行
                                }else {											// 构建集合对象
                                	String scientificname = excelService.getStringValueFromCell(row.getCell(0));
                                	Taxon thisTaxon = this.taxonRepository.findOneByScientificnameAndTaxasetId(scientificname, taxasetId);
                                	
                                	protInfo[0] = excelService.getStringValueFromCell(row.getCell(1));
                                	protInfo[1] = excelService.getStringValueFromCell(row.getCell(2)); 
                                	protInfo[2] = excelService.getStringValueFromCell(row.getCell(3)); 
                                	
                                	
                                	thisProtection = new Protection(
                                			excelService.getStringValueFromCell(row.getCell(3)),/*根据保护标准、标准版本确定保护级别*/
                                			excelService.getStringValueFromCell(row.getCell(4)),
                                			excelService.getStringValueFromCell(row.getCell(5)),
                                			excelService.getStringValueFromCell(row.getCell(6)),
                                			excelService.getStringValueFromCell(row.getCell(7)),
                                			thisTaxon);
                                	
                                	String remark = "{"
                                			+ "\"" + "rightsholder" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(8)) + "\","
                                			+ "\"" + "copyright" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(9)) + "\","
                                			+ "\"" + "license" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(10)) + "\","
                                			+ "}";
									
                                	thisProtection.setRemark(remark);
                                	protectionList.add(thisProtection);
								}	
                            }
                            long start = System.currentTimeMillis();
                            JSONArray verifyListEntity = verifyListEntity(protectionList, thisUser, protInfo, fileMark);
                            long end = System.currentTimeMillis();
							System.out.println("上传校验：" + (end - start));
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
	private JSONArray verifyListEntity(List<Protection> list, User thisUser, String[] protInfo, String[] fileMark) throws Exception {
		JSONArray protectArr = new JSONArray();
		List<Protection> failList = new ArrayList<>();
        boolean flag = true;
        for (int i = 0; i < list.size(); i++) {
        	boolean mark = true;
        	Protection thisProtection = list.get(i);
        	if (null == thisProtection.getTaxon()) {
        		thisProtection.setTaxon(new Taxon("<span style='color:red'>未匹配到对应分类单元，请先上传分类单元</span>"));
        		mark = false;
			}
        	if (StringUtils.isBlank(thisProtection.getProtlevel())) {
        		thisProtection.setProtlevel("<span style='color:red'>保护级别不能为空</span>");
        		mark = false;
        	};
			if (StringUtils.isBlank(protInfo[0]) || StringUtils.isBlank(protInfo[1]) || StringUtils.isBlank(list.get(i).getProtlevel())) {
				thisProtection.setProtlevel("<span style='color:red'>保护标准名称、标准版本、级别均不能为空</span>");
				mark = false;
			}else {
				Protectstandard standard = this.protectstandardRepository.findOneByStandardnameAndVersionAndProtlevel(protInfo[0], protInfo[1], list.get(i).getProtlevel());
				if (null != standard) {
					thisProtection.setProtectstandard(standard);
					thisProtection.setProtlevel(standard.getId());
				}else {
					thisProtection.setProtlevel("<span style='color:red'>系统数据缺失，未找到对应数据，请联系管理员！</span>");
					mark = false;
				}
			}
        	 /*数据源*/
            if (StringUtils.isBlank(thisProtection.getSourcesid())) {
            	thisProtection.setSourcesid("<span style='color:red'>数据源不能为空</span>");
            	mark = false;
            }else {
            	String source = this.baseinfotmpService.findDsIdByFilemarkAndSerialNumAndFileType(fileMark[1], thisProtection.getSourcesid(), 1);
            	if (StringUtils.isNotBlank(source)) {
					thisProtection.setSourcesid(source);
				}else {
					thisProtection.setSourcesid("<span style='color:red'>未找到引用数据源，请检查引用数据是否存在或是否引用错误！</span>");
					mark = false;
				}
			};
			
			/*审核专家*/
			if (StringUtils.isBlank(thisProtection.getExpert())) {
				thisProtection.setExpert("<span style='color:red'>专家信息不能为空</span>");
				mark = false;
            }else{
            	boolean tmp = thisProtection.getExpert().matches("[0-9]*");
            	String expid = null;
            	if (tmp) {
            		expid = this.baseinfotmpService.findExpIdByFilemarkAndSerialNumAndFileType(fileMark[2], 2, thisProtection.getExpert());
				}else {
					String expertId = this.expertRepository.findIdByCnName(thisProtection.getExpert());
					if (StringUtils.isNotBlank(expertId)) {
						expid = expertId;
					}else {
						thisProtection.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
					}
				}
            	
            	if (StringUtils.isNotBlank(expid)) {
            		thisProtection.setExpert(expid);
				}else{
					thisProtection.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
					mark = false;
				}
            };
            
            /*比对参考文献*/
        	String refjson = thisProtection.getRefjson();
        	List<String> refIds = new ArrayList<>();
        	if (StringUtils.isNotBlank(refjson)) {
        		String[] refStr = refjson.replace("，", ",").split(",");
        		refIds = this.baseinfotmpService.findRefIdByFilemarkAndSerialNumAndFileType(fileMark[0], 0, refStr);
        		if (!refIds.isEmpty()) {
        			JSONArray refjsonArr = this.refService.getRefJSONArrayByIds(refIds);
        			if (!refjsonArr.isEmpty()) {
        				thisProtection.setRefjson(refjsonArr.toString());
        			}else {
        				thisProtection.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
						mark = false;
					}
        		}else {
        			thisProtection.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
					mark = false;
        		}
        	};
        	
        	if (!mark) {
        		thisProtection.setSynchstatus(i + 1);
            	failList.add(thisProtection);
            	flag = mark;
			}
        }
        /* 构建的集合若通过必填字段的校验，去重，比对数据源、参考文献，补充属性值*/
        if (flag) {
        	List<Protection> protectionList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
            	/* 根据学名去重*/
            	list.get(i).setId(UUIDUtils.getUUID32());
            	list.get(i).setInputer(thisUser.getId());
            	list.get(i).setInputtime(new Timestamp(System.currentTimeMillis()));
            	list.get(i).setSynchdate(new Timestamp(System.currentTimeMillis()));
            	list.get(i).setStatus(1);
            	list.get(i).setSynchstatus(0);
				/*this.protectionRepository.save(list.get(i));*/
            	protectionList.add(list.get(i));
            }
            long start = System.currentTimeMillis();
            this.batchInsertService.batchInsertProtection(protectionList);
            long end = System.currentTimeMillis();
			System.out.println("Protection批量存储完成：" + (end - start));
        }else {
			for (int i = 0; i < failList.size(); i++) {
				JSONObject json = new JSONObject();
				json.put("num", failList.get(i).getSynchstatus());
				json.put("scientificname", failList.get(i).getTaxon().getScientificname());
				json.put("protlevel", failList.get(i).getProtlevel());
				json.put("sourcesid", failList.get(i).getSourcesid());
				json.put("expert", failList.get(i).getExpert());
				protectArr.add(i, json);
			}
		}
        return protectArr;
    }
}
