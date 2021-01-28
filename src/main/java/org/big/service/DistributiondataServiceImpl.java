package org.big.service;

import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.big.sp2000.entity.Distribution;
import org.junit.Ignore;
import org.big.common.ACSearch;
import org.big.common.CommUtils;
import org.big.common.QueryTool;
import org.big.common.StringSearchResult;
import org.big.common.UUIDUtils;
import org.big.entity.Description;
import org.big.entity.Distributiondata;
import org.big.entity.Expert;
import org.big.entity.Geoobject;
import org.big.entity.Taxon;
import org.big.entity.UserDetail;
import org.big.repository.DescriptionRepository;
import org.big.repository.DistributiondataRepository;
import org.big.repository.ExpertRepository;
import org.big.repository.GeoobjectRepository;
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
public class DistributiondataServiceImpl implements DistributiondataService {
	@Autowired
	private DistributiondataRepository distributiondataRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private TaxonService taxonService;
	@Autowired
	private DatasourceService datasourceService;
	@Autowired
	private GeoobjectService geoobjectService;
	@Autowired
	private RefService refService;
	@Autowired
    private TaxonRepository taxonRepository;
	@Autowired
	private ExcelService excelService;
	@Autowired
	private ExpertService expertService;
	@Autowired
	private BaseinfotmpService baseinfotmpService;
	@Autowired
	private GeoobjectRepository geoobjectRepository;
	@Autowired
	private ExpertRepository expertRepository;
	@Autowired
	private DescriptionRepository descriptionRepository;
	@Autowired
	private LicenseService licenseService;
	@Autowired
	private DescriptiontypeService descriptiontypeService;
	@Autowired
	private BatchInsertService batchInsertService;
	@Override
	public JSON findUploadedDistributiondataList(Timestamp timestamp, HttpServletRequest request) {
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
		List<Distributiondata> thisList = new ArrayList<>();
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Page<Distributiondata> thisPage = this.distributiondataRepository.searchInfo(searchText, timestamp, thisUser.getId(), QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		String thisSelect = "";
		String thisEdit = "";
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
	        thisSelect="<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
	        thisEdit=
	             "<a class=\"wts-table-edit-icon\" onclick=\"removeThisObject('" + thisList.get(i).getId() + "','distributiondata')\" >" +
	             "<span class=\"glyphicon glyphicon-remove\"></span>" +
	             "</a>";
			row.put("select", thisSelect);
			row.put("edit", thisEdit);
			
			String geojson = thisList.get(i).getGeojson();
			try {
				JSONObject obj = JSONObject.parseObject(geojson);
				String[] geoArr = obj.get("geoIds").toString().split("&");
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < geoArr.length; j++) {
					Geoobject thisGeoobject = this.geoobjectRepository.findOneById(geoArr[j]);
					if (null != thisGeoobject) {
						if (j == geoArr.length-1) {
							sb.append(thisGeoobject.getShortName());
						}else {
							sb.append(thisGeoobject.getShortName());
							sb.append("、");
						}
					}
				}
				row.put("geojson", sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
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
			rows.add(i, row);
		}
		thisTable.put("rows", rows);
		return thisTable;
    }

	@Override
	public JSON addDistributiondata(String taxonId, HttpServletRequest request) {
		Description thisDescription = new Description();
		Distributiondata thisDistributiondata = new Distributiondata();
		Taxon thisTaxon = taxonService.findOneById(taxonId);
		thisDistributiondata.setTaxon(thisTaxon);
		thisDistributiondata.setInputtime(new Timestamp(System.currentTimeMillis()));
		
		thisDescription.setTaxon(thisTaxon);
		thisDescription.setInputtime(new Timestamp(System.currentTimeMillis()));
		
		Enumeration<String> paraNames = request.getParameterNames();
		String paraName = null;
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			
			if (paraName.indexOf("disdescriptionId_") == 0) {
				String descriptionId = request.getParameter(paraName);
				thisDescription.setId(descriptionId);
				thisDistributiondata.setDescid(descriptionId);				// 关联 
			}
			if (paraName.indexOf("distitle_") == 0) {
				thisDescription.setDestitle(request.getParameter(paraName));
			}
			if (paraName.indexOf("discriber_") == 0) {
				thisDescription.setDescriber(request.getParameter(paraName));
			}
			if (paraName.indexOf("disdate_") == 0) {
				thisDescription.setDesdate(request.getParameter(paraName));
			}
			if (paraName.indexOf("discontent_") == 0) {
				String content = request.getParameter(paraName);
				if (StringUtils.isNotBlank(content)) {
					thisDescription.setDescontent(content);
					thisDistributiondata.setDiscontent(content);
				}
			}
			if (paraName.indexOf("disremark_") == 0) {
				thisDescription.setRemark(request.getParameter(paraName));
			}
			if (paraName.indexOf("distributiondatasourcesid_") == 0) {
				String sourceId = request.getParameter(paraName);
				thisDescription.setSourcesid(sourceId);
				thisDistributiondata.setSourcesid(sourceId);
			}
			if (paraName.indexOf("disrelationDes_") == 0) {
				thisDescription.setRelationDes(request.getParameter(paraName));
			}
			if (paraName.indexOf("disrelationId_") == 0) {
				thisDescription.setRelationId(request.getParameter(paraName));
			}
			if (paraName.indexOf("disrightsholder_") == 0) {
				thisDescription.setRightsholder(request.getParameter(paraName));
			}
			if (paraName.indexOf("distypeid_") == 0) {
				thisDescription.setDestypeid(request.getParameter(paraName));
				thisDescription.setDescriptiontype(this.descriptiontypeService.findOneById(request.getParameter(paraName)));
			}
			if (paraName.indexOf("dislicenseid_") == 0) {
				thisDescription.setLicenseid(request.getParameter(paraName));
			}
			if (paraName.indexOf("dislanguage_") == 0) {
				thisDescription.setLanguage(request.getParameter(paraName));
			}
			
			if (paraName.indexOf("distributiondataId_") == 0) {
				thisDistributiondata.setId(request.getParameter(paraName));
			}
			
			// Distributiondata_Has_Geoobject
			if (paraName.indexOf("geojson_") == 0) {
				String[] geoIds = request.getParameterValues(paraName);
				String geoStr = "{" 
							+ "\"geoIds\"" + ":\"" + StringUtils.join(geoIds, "&") + "\""
							+ "}";
				JSONObject geojson = JSON.parseObject(geoStr);
				
				thisDistributiondata.setGeojson(geojson.toJSONString());
			}
		}
		
		JSONObject thisResult = new JSONObject();
		try {
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			thisDescription.setInputer(thisUser.getId());
			thisDescription.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisDescription.setStatus(1);
			thisDescription.setSynchstatus(0);
			
			thisDistributiondata.setInputer(thisUser.getId());
			thisDistributiondata.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisDistributiondata.setStatus(1);
			thisDistributiondata.setSynchstatus(0);
			JSONArray handleReferenceToJson = (JSONArray) handleReferenceToJson(request);
			if (handleReferenceToJson.size() > 0) {
				thisDistributiondata.setRefjson(handleReferenceToJson.toJSONString());
				thisDescription.setRefjson(handleReferenceToJson.toJSONString());
			}
			
			this.descriptionRepository.save(thisDescription);
			this.distributiondataRepository.save(thisDistributiondata);
			
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
		int countDistributiondataNum = 0;
		String distributiondataReferencesPageE = null;
		String distributiondataReferencesPageS = null;
		String distributiondataReferenceId = null;
		String jsonStr = null;
		
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("countDistributiondataReferences_") == 0) {
				countDistributiondataNum = Integer.parseInt(request.getParameter(paraName));
			}
			if (paraName.indexOf("distributiondataId_") == 0) {
				formNum = Integer.parseInt(paraName.substring(paraName.indexOf("_") + 1));
			}
		}
		
		for (int i = 1; i <= countDistributiondataNum; i++) {
			distributiondataReferenceId = request.getParameter("distributiondataReferences_" + formNum + "_" + i);
			distributiondataReferencesPageS = request.getParameter("distributiondataReferencesPageS_" + formNum + "_" + i);
			distributiondataReferencesPageE = request.getParameter("distributiondataReferencesPageE_" + formNum + "_" + i);
			if (StringUtils.isNotBlank(distributiondataReferenceId) && StringUtils.isNotBlank(distributiondataReferencesPageS)
					&& StringUtils.isNotBlank(distributiondataReferencesPageE)) {
				jsonStr = "{" 
						+ "\"refId\"" + ":\"" + distributiondataReferenceId + "\","
						+ "\"refS\"" + ":\"" + distributiondataReferencesPageS + "\"," 
						+ "\"refE\"" + ":\"" + distributiondataReferencesPageE + "\""
						+ "}";
				JSONObject jsonText = JSON.parseObject(jsonStr);
				jsonArray.add(jsonText);
			}
		}
		return jsonArray;
	}

	@Override
	public boolean logicRemove(String id) {
		Distributiondata thisDistributiondata = this.distributiondataRepository.findOneById(id);
		if (null != thisDistributiondata && 1 == thisDistributiondata.getStatus()) {
			thisDistributiondata.setStatus(0);
			this.distributiondataRepository.save(thisDistributiondata);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean deleteOne(HttpServletRequest request) {
		String distributiondataId = request.getParameter("distributiondataId");
		if (StringUtils.isNotBlank(distributiondataId)) {
			if (null != this.distributiondataRepository.findOneById(distributiondataId)) {
				this.distributiondataRepository.deleteOneById(distributiondataId);
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
		List<Distributiondata> thisList = new ArrayList<>();
		Page<Distributiondata> thisPage = this.distributiondataRepository.searchByDescriptionInfo(findText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisSelect.put("total_count", thisPage.getTotalElements());
		Boolean incompleteResulte = true;
		if ((thisPage.getTotalElements() / 30) > findPage)
			incompleteResulte = false;
		thisSelect.put("incompleteResulte", incompleteResulte);
		thisList = thisPage.getContent();
		/*if (findPage == 1) {
			JSONObject row = new JSONObject();
			row.put("id", "addNew");
			row.put("text", "新建一个数据集");
			items.add(row);
		}*/
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			row.put("id", thisList.get(i).getId());
			row.put("text", thisList.get(i).getDiscontent());
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public JSON findDistributiondataListByTaxonId(String taxonId, HttpServletRequest request) {
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
		Page<Distributiondata> thisPage = this.distributiondataRepository.searchDistributiondatasByTaxonId(
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), taxonId);
		
		JSONArray rows = new JSONArray();
		List<Distributiondata> thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			thisList.get(i).setSourcesid(this.datasourceService.findOneById(thisList.get(i).getSourcesid()).getTitle());
			
			String geojson = thisList.get(i).getGeojson();
			if (StringUtils.isNotBlank(geojson)) {
				JSONObject parseObject = JSON.parseObject(geojson);
				String geoIds = (String) parseObject.get("geoIds");
				String[] geoIdArr = geoIds.split("&");
				for (int j = 0; j < geoIdArr.length; j++) {
					geoIdArr[j] = this.geoobjectService.findOneById(geoIdArr[j]).getCngeoname() 
							+ "<" + this.geoobjectService.findOneById(geoIdArr[j]).getEngeoname() + ">&nbsp;&nbsp;&nbsp;&nbsp;";
				}
				thisList.get(i).setGeojson(StringUtils.join(geoIdArr, ""));
			}
			rows.add(i, thisList.get(i));
		}
		
		thisSelect.put("total", thisPage.getTotalElements());		// NO1：总数
		thisSelect.put("page", offset_serch);						// NO2：offset
		thisSelect.put("rows", rows);								// NO3：Taxon下的所有Citation
		return thisSelect;
	}

	@Override
	public JSON editDistributiondata(String taxonId) {
		JSONObject distributiondatas = new JSONObject();
		JSONArray distributiondataArr = new JSONArray();
		try {
			if (StringUtils.isNotBlank(taxonId)) {
				List<Distributiondata> list = this.distributiondataRepository.findDistributiondataListByTaxonId(taxonId);
				for (int i = 0; i < list.size(); i++) {
					JSONArray refjson = this.refService.refactoringRef(list.get(i).getRefjson());
					JSONArray geojson = this.geoobjectService.buildGeoJSON(list.get(i).getGeojson());
					JSONObject json = new JSONObject();
					json.put("id", list.get(i).getId());
					if (refjson.size() > 0) {
						json.put("refjson", refjson.toJSONString());
					}
					if (geojson.size() > 0) {
						json.put("geojson", geojson.toJSONString());
					}
					// 数据源
					String sourcesid = list.get(i).getSourcesid();
					String sourcesTitle = this.datasourceService.findOneById(sourcesid).getTitle();
					json.put("sourcesid", sourcesid);
					json.put("sourcesTitle", sourcesTitle);
					
					// 描述
					String descid = list.get(i).getDescid();
					if (StringUtils.isNotBlank(descid) ) {
						Description thisDescription = this.descriptionRepository.findOneById(descid);
						if (null != thisDescription) {
							String descTitle = thisDescription.getDestitle();
							json.put("descid", descid);
							
							// 描述类型
							String destypeid = thisDescription.getDestypeid();
							String destypeName = this.descriptiontypeService.findOneById(destypeid).getName();
							json.put("distypeid", destypeid);
							json.put("distypeName", destypeName);
							
							json.put("disremark", thisDescription.getRemark());
							// 关系
							try {
								String relationDes = thisDescription.getRelationDes();
								if (StringUtils.isNotBlank(relationDes) && !"-1".equals(relationDes)) {
									String destitle = this.descriptionRepository.findOneById(relationDes).getDestitle();
									json.put("disrelationDes", relationDes);
									json.put("disrelationDestitle", destitle);
								}else {
									json.put("disrelationDes", "-1");
									json.put("disrelationDestitle", "无");
								}
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("关联描述为空");
							}
							json.put("distitle", thisDescription.getDestitle());
							json.put("disdate", thisDescription.getDesdate());
							json.put("discriber", thisDescription.getDescriber());
							json.put("disrightsholder", thisDescription.getRightsholder());
							json.put("discontent", thisDescription.getDescontent());
							json.put("dislanguage", thisDescription.getLanguage());
							
							// 共享协议
							String licenseid = thisDescription.getLicenseid();
							String licenseTitle = this.licenseService.findOneById(licenseid).getTitle();
							json.put("licenseid", licenseid);
							json.put("licenseTitle", licenseTitle);
						}else {
							json.put("distitle", "无标题");
							json.put("discontent", list.get(i).getDiscontent());
							json.put("dislanguage", "1");
							json.put("licenseid", "3");
							json.put("licenseTitle", "署名 (BY)");
							json.put("distypeid", "210");
							json.put("distypeName", "其他分布");
						}
						
					}else {
					}

					distributiondataArr.add(i, json);
				}
				distributiondatas.put("distributiondatas", distributiondataArr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return distributiondatas;
	}

	@Override
	public JSON uploadFile(MultipartFile file, String taxasetId, HttpServletRequest request) throws Exception {
		JSONObject thisResult = new JSONObject();
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
		}else { 
            //先解析EXCEL
        	InputStream in = file.getInputStream();
            try {
            	Workbook wb = WorkbookFactory.create(in);	//1.得到上传的表
            	Sheet sheet1 = wb.getSheetAt(0);	//2.1.获取工作表1
            	Sheet sheet2 = wb.getSheetAt(1);	//2.2.获取工作表2
            	int totleNum1 = sheet1.getLastRowNum();	 //3.1.获取总行数
            	int totleNum2 = sheet2.getLastRowNum();	 //3.2.获取总行数
				/*String tableHeadArray1[] = { "*学名", "*地理名称", "*地理标识", "*地图标准", "分布类型", "*数据源", "*参考文献", "*审核专家", "版权所有者",
						"版权声明", "*共享协议" };*/	//11
            	/*String tableHeadArray2[] = { "*学名", "*省级", "*市", "*县", "小地名", "经度", "纬度", "参考文献", "*数据源", "*审核专家",
						"版权所有者", "版权声明", "共享协议" };*/	//13
            	String tableHeadArray1[] = { "学名", "地理名称", "地理标识", "地图标准", "分布类型", "数据源", "参考文献", "审核专家", "版权所有者",
						"版权声明", "共享协议" };	//11
				
				String tableHeadArray2[] = { "学名", "省级", "市", "县", "小地名", "经度", "纬度", "参考文献", "数据源", "审核专家",
						"版权所有者", "版权声明", "共享协议" };	//13
				
				if (totleNum1 > 0) {
					thisResult = handleSheet1(sheet1, totleNum1, tableHeadArray1, fileMark, taxasetId);
				}
				if (totleNum2 > 0) {
					thisResult = handleSheet2(sheet2, totleNum2, tableHeadArray2, fileMark, taxasetId);
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
	 * <b>解析分布模板1数据，拼装list集合</b>
	 * <p> 解析分布模板1数据，拼装list集合</p>
	 * @param sheet1
	 * @param totleNum1
	 * @param tableHeadArray1
	 * @param fileMark
	 * @param taxasetId
	 * @return
	 */
	private JSONObject handleSheet1(Sheet sheet1, int totleNum1, String[] tableHeadArray1, String[] fileMark, String taxasetId) {
		JSONObject thisResult = new JSONObject();
		List<Distributiondata> distributiondataList = new ArrayList<>();							// 上传文件的数据
        UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//判断表头是否符合规则
        try {
            if(this.excelService.judgeRowConsistent(11,tableHeadArray1, sheet1.getRow(0))){
                try {
                	Distributiondata thisDistributiondata= new Distributiondata();			// Distributiondata对象
                    Row row = null;										// 记录行
                    for (int i = 1; i <= totleNum1; i++) {
                        row = sheet1.getRow(i);
                        if (null == sheet1.getRow(i)) { 
                        	continue; 									// 空行
                        }else {											// 构建集合对象
                        	String scientificname = excelService.getStringValueFromCell(row.getCell(0));
                        	Taxon thisTaxon = this.taxonRepository.findOneByScientificnameAndTaxasetId(scientificname, taxasetId);
                        	thisDistributiondata = new Distributiondata(
                        			excelService.getStringValueFromCell(row.getCell(1)), 
                        			excelService.getStringValueFromCell(row.getCell(2)),
                        			excelService.getStringValueFromCell(row.getCell(3)), 
                        			excelService.getStringValueFromCell(row.getCell(4)), 
                        			excelService.getStringValueFromCell(row.getCell(5)), 
                        			excelService.getStringValueFromCell(row.getCell(6)), 
                        			excelService.getStringValueFromCell(row.getCell(7)), 
                        			thisTaxon);
                        	
                        	String remark = "{"
                        			+ "\"" + "rightsholder" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(8)) + "\","
                        			+ "\"" + "copyright" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(9)) + "\","
                        			+ "}";
                        	
                        	thisDistributiondata.setRemark(remark);
                        	distributiondataList.add(thisDistributiondata);
						}	
                    }
                    long start = System.currentTimeMillis();
                    JSONArray verifyListEntity = verifyListEntityOfSheet1(distributiondataList, thisUser, fileMark);
                    long end = System.currentTimeMillis();
                    System.out.println("上传校验：" + (end -start));
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
		return thisResult;
	}
	/**
	 * <b>解析分布模板2数据，拼装list集合</b>
	 * <p> 解析分布模板2数据，拼装list集合</p>
	 * @param sheet2
	 * @param totleNum2
	 * @param tableHeadArray2
	 * @param fileMark
	 * @param taxasetId
	 * @return
	 */
	private JSONObject handleSheet2(Sheet sheet2, int totleNum2, String[] tableHeadArray2, String[] fileMark, String taxasetId) {
		JSONObject thisResult = new JSONObject();
		List<Distributiondata> distributiondataList = new ArrayList<>();							// 上传文件的数据
        UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//判断表头是否符合规则
        try {
            if(this.excelService.judgeRowConsistent(13,tableHeadArray2, sheet2.getRow(0))){
                try {
                	Distributiondata thisDistributiondata= new Distributiondata();			// Distributiondata对象
                    Row row = null;										// 记录行
                    for (int i = 1; i <= totleNum2; i++) {
                        row = sheet2.getRow(i);
                        if (null == sheet2.getRow(i)) { 
                        	continue; 									// 空行
                        }else {											// 构建集合对象
                        	String scientificname = excelService.getStringValueFromCell(row.getCell(0));
                        	Taxon thisTaxon = this.taxonRepository.findOneByScientificnameAndTaxasetId(scientificname, taxasetId);
                        	
                        	thisDistributiondata = new Distributiondata( 
                        			excelService.getStringValueFromCell(row.getCell(1)), 
                        			excelService.getStringValueFromCell(row.getCell(2)),
                        			excelService.getStringValueFromCell(row.getCell(3)), 
                        			excelService.getStringValueFromCell(row.getCell(4)), 
									Double.parseDouble(
											StringUtils.isNotBlank(excelService.getStringValueFromCell(row.getCell(5)))
													? excelService.getStringValueFromCell(row.getCell(5)) : "0"),
									Double.parseDouble(
											StringUtils.isNotBlank(excelService.getStringValueFromCell(row.getCell(6)))
													? excelService.getStringValueFromCell(row.getCell(6)) : "0"), 
                        			excelService.getStringValueFromCell(row.getCell(7)), 
                        			excelService.getStringValueFromCell(row.getCell(8)), 
                        			excelService.getStringValueFromCell(row.getCell(9)),
                        			thisTaxon);
                        	String remark = "{"
                        			+ "\"" + "rightsholder" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(10)) + "\","
                        			+ "\"" + "copyright" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(11)) + "\","
                        			+ "\"" + "license" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(12)) + "\","
                        			+ "}";
                        	thisDistributiondata.setRemark(remark);
                        	distributiondataList.add(thisDistributiondata);
						}	
                    }
                    long start = System.currentTimeMillis();
                    JSONArray verifyListEntity = verifyListEntityOfSheet2(distributiondataList, thisUser, fileMark);
                    long end = System.currentTimeMillis();
                    System.out.println("上传校验：" + (end -start));
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
		return thisResult;
	}

	/**
	 * <b>校验分布模板1拼装的list集合</b>
	 * <p> 校验分布模板1拼装的list集合</p>
	 * @param list
	 * @param thisUser
	 * @param fileMark
	 * @return
	 * @throws Exception 
	 */
	private JSONArray verifyListEntityOfSheet1(List<Distributiondata> list, UserDetail thisUser,
			String[] fileMark) throws Exception {
		JSONArray distArr = new JSONArray();
		List<Distributiondata> failList = new ArrayList<>();
        boolean flag = true;
        for (int i = 0; i < list.size(); i++) {			// 参考保护实现
        	Distributiondata thisDistributiondata = list.get(i);
        	boolean mark = true;
        	if (null == thisDistributiondata.getTaxon()) {
        		thisDistributiondata.setTaxon(new Taxon("<span style='color:red'>未匹配到对应分类单元，请先上传分类单元</span>"));
        		mark = false;
			}
        	if (StringUtils.isBlank(thisDistributiondata.getGeojson())) {
        		thisDistributiondata.setGeojson("<span style='color:red'>地理名称不能为空</span>");
        		mark = false;
        	};
        	if (StringUtils.isBlank(thisDistributiondata.getDismark())) {
        		thisDistributiondata.setDismark("<span style='color:red'>地理标识不能为空</span>");
        		mark = false;
        	}else {
				String distId = this.geoobjectRepository.findIdByAdcode(thisDistributiondata.getDismark());
				if (StringUtils.isNotBlank(distId)) {
					String geoStr = "{" 
							+ "\"geoIds\"" + ":\"" + distId + "\""
							+ "}";
					thisDistributiondata.setGeojson(geoStr);
				}else {
					thisDistributiondata.setDismark("<span style='color:red'>地理标识填写有误，数据库未匹配到对应的地理实体</span>");
					mark = false;
				}
			};
        	if (StringUtils.isBlank(thisDistributiondata.getDismapstandard())) {
        		thisDistributiondata.setDismapstandard("<span style='color:red'>地图标准不能为空</span>");
        		mark = false;
        	};
        	/*数据源*/
            if (StringUtils.isBlank(thisDistributiondata.getSourcesid())) {
            	thisDistributiondata.setSourcesid("<span style='color:red'>数据源不能为空</span>");
            	mark = false;
            }else  {
            	String source = this.baseinfotmpService.findDsIdByFilemarkAndSerialNumAndFileType(fileMark[1], thisDistributiondata.getSourcesid(), 1);
            	if (StringUtils.isNotBlank(source)) {
					thisDistributiondata.setSourcesid(source);
				}else {
					thisDistributiondata.setSourcesid("<span style='color:red'>未找到引用数据源，请检查引用数据存在或是否引用错误！</span>");
					mark = false;
				}
			};
			
			/*审核专家*/
			if (StringUtils.isBlank(thisDistributiondata.getExpert())) {
				thisDistributiondata.setExpert("<span style='color:red'>专家信息不能为空</span>");
				mark = false;
            }else{
            	boolean tmp = thisDistributiondata.getExpert().matches("[0-9]*");
            	String expid = null;
            	if (tmp) {
            		expid = this.baseinfotmpService.findExpIdByFilemarkAndSerialNumAndFileType(fileMark[2], 2, thisDistributiondata.getExpert());
				}else {
					String expertId = this.expertRepository.findIdByCnName(thisDistributiondata.getExpert());
					if (StringUtils.isNotBlank(expertId)) {
						expid = expertId;
					}else {
						thisDistributiondata.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用数据是否存在或是否引用错误！</span>");
					}
				}            	
            	
            	if (StringUtils.isNotBlank(expid)) {
            		thisDistributiondata.setExpert(expid);
				}else{
					thisDistributiondata.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用数据是否存在或是否引用错误！</span>");
					mark = false;
				}
            };
            
            /*比对参考文献*/
            String refjson = thisDistributiondata.getRefjson();
        	List<String> refIds = new ArrayList<>();
        	if (StringUtils.isNotBlank(refjson)) {
        		String[] refStr = refjson.replace("，", ",").split(",");
        		refIds = this.baseinfotmpService.findRefIdByFilemarkAndSerialNumAndFileType(fileMark[0], 0, refStr);
        		if (!refIds.isEmpty()) {
        			JSONArray refjsonArr = this.refService.getRefJSONArrayByIds(refIds);
        			if (!refjsonArr.isEmpty()) {
        				thisDistributiondata.setRefjson(refjsonArr.toString());
					}else {
						thisDistributiondata.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
						mark = false;
					}
        		}else {
        			thisDistributiondata.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
					mark = false;
				}
        	};
        	
        	if (!mark) {
        		thisDistributiondata.setSynchstatus(i + 1);
            	failList.add(thisDistributiondata);
            	flag = mark;
			}
        }
        /* 构建的集合若通过必填字段的校验，去重，比对数据源、参考文献，补充属性值*/
        if (flag) {
        	List<Distributiondata> distributionList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
            	/* 根据学名去重*/
            	list.get(i).setId(UUIDUtils.getUUID32());
            	list.get(i).setInputer(thisUser.getId());
            	list.get(i).setInputtime(new Timestamp(System.currentTimeMillis()));
            	list.get(i).setSynchdate(new Timestamp(System.currentTimeMillis()));
            	list.get(i).setStatus(1);
            	list.get(i).setSynchstatus(0);
				/*this.distributiondataRepository.save(list.get(i));*/
            	distributionList.add(list.get(i));
            }
            long start = System.currentTimeMillis();
            this.batchInsertService.batchInsertDistributiondata(distributionList);
            long end = System.currentTimeMillis();
            System.out.println("Distributiondata批量存储完成：" + (end - start));
        }else {
			for (int i = 0; i < failList.size(); i++) {	// 参考保护
				JSONObject json = new JSONObject();
				json.put("num", failList.get(i).getSynchstatus());
				json.put("scientificname", failList.get(i).getTaxon().getScientificname());
				json.put("geojson", failList.get(i).getGeojson());
				json.put("dismark", failList.get(i).getDismark());
				json.put("dismapstandard", failList.get(i).getDismapstandard());
				json.put("sourcesid", failList.get(i).getSourcesid());
				json.put("expert", failList.get(i).getExpert());
				json.put("refjson", failList.get(i).getRefjson());
				distArr.add(i, json);
			}
		}
        return distArr;
	}
	
	/**
	 * <b>校验分布模板2拼装的list集合</b>
	 * <p> 校验分布模板2拼装的list集合</p>
	 * @param list
	 * @param thisUser
	 * @param fileMark
	 * @return
	 * @throws Exception 
	 */
	private JSONArray verifyListEntityOfSheet2(List<Distributiondata> list, UserDetail thisUser,
			String[] fileMark) throws Exception {
		JSONArray distArr = new JSONArray();
		List<Distributiondata> failList = new ArrayList<>();
        boolean flag = true;
        boolean provinceFlag = true;
        boolean cityFlag = true;
        boolean countyFlag = true;
        for (int i = 0; i < list.size(); i++) {			// 参考保护实现
        	Distributiondata thisDistributiondata = list.get(i);
        	boolean mark = true;
        	if (null == thisDistributiondata.getTaxon()) {
        		thisDistributiondata.setTaxon(new Taxon("<span style='color:red'>未匹配到对应分类单元，请先上传分类单元</span>"));
        		mark = false;
			}
        	
        	String[] geoIds = new String[3];
    		String province = thisDistributiondata.getProvince();
    		String city = thisDistributiondata.getCity();
    		String county = thisDistributiondata.getCounty();
        	if (StringUtils.isBlank(province)) {
        		provinceFlag = false;
        	}else {
        		if (province.contains("省") || province.contains("市") || province.contains("自治区") || province.contains("特别行政区")) {
        			geoIds[0] = StringUtils.isNotBlank(this.geoobjectRepository.findGidByCngeoname(province)) ? this.geoobjectRepository.findGidByCngeoname(province) : "";
        		}else {
        			geoIds[0] = StringUtils.isNotBlank(this.geoobjectRepository.findGidByShortName(province)) ? this.geoobjectRepository.findGidByShortName(province) : "";
        		}
			};
        	if (StringUtils.isBlank(city)) {
        		cityFlag = false;
        	}else {
        		if (city.contains("市") || city.contains("区") || city.contains("盟") || city.contains("自治州") || city.contains("地区") || city.contains("堂区")) {
        			geoIds[1] = StringUtils.isNotBlank(this.geoobjectRepository.findGidByCngeoname(city)) ? this.geoobjectRepository.findGidByCngeoname(city) : "";
				}else {
					geoIds[1] = StringUtils.isNotBlank(this.geoobjectRepository.findGidByShortName(city)) ? this.geoobjectRepository.findGidByShortName(city) : "";
				}
			};
        	if (StringUtils.isBlank(county)) {
        		countyFlag = false;
        	}else {
        		if (county.contains("县") || county.contains("自治县") || county.contains("市") || county.contains("区") || county.contains("旗")) {
        			geoIds[2] = StringUtils.isNotBlank(this.geoobjectRepository.findGidByCngeoname(county)) ? this.geoobjectRepository.findGidByCngeoname(county) : "";
        		}else {
        			geoIds[2] = StringUtils.isNotBlank(this.geoobjectRepository.findGidByShortName(county)) ? this.geoobjectRepository.findGidByShortName(county) : "";
        		}
			};
        	/*geojson*/
        	if (provinceFlag || cityFlag || countyFlag) {
        		String geoStr = "{" 
						+ "\"geoIds\"" + ":\"" + StringUtils.join(geoIds, "&") + "\""
						+ "}";
        		thisDistributiondata.setGeojson(geoStr);
        		thisDistributiondata.setDiscontent(province + "," + city + "," + county);
			}else {
				thisDistributiondata.setCounty("<span style='color:red'>省市县不能都为空</span>");
				mark = false;
			}
        	/*数据源*/
            if (StringUtils.isBlank(thisDistributiondata.getSourcesid())) {
            	thisDistributiondata.setSourcesid("<span style='color:red'>数据源不能为空</span>");
            	mark = false;
            }else  {
            	String source = this.baseinfotmpService.findDsIdByFilemarkAndSerialNumAndFileType(fileMark[1], thisDistributiondata.getSourcesid(), 1);
            	if (StringUtils.isNotBlank(source)) {
					thisDistributiondata.setSourcesid(source);
				}else {
					thisDistributiondata.setSourcesid("<span style='color:red'>未找到引用数据源，请检查引用数据存在或是否引用错误！</span>");
					mark = false;
				}
			};
			
			/*审核专家*/
			if (StringUtils.isBlank(thisDistributiondata.getExpert())) {
				thisDistributiondata.setExpert("<span style='color:red'>专家信息不能为空</span>");
				mark = false;
            }else{
            	boolean tmp = thisDistributiondata.getExpert().matches("[0-9]*");
            	String expid = null;
            	if (tmp) {
            		expid = this.baseinfotmpService.findExpIdByFilemarkAndSerialNumAndFileType(fileMark[2], 2, thisDistributiondata.getExpert());
				}else {
					String expertId = this.expertRepository.findIdByCnName(thisDistributiondata.getExpert());
					if (StringUtils.isNotBlank(expertId)) {
						expid = expertId;
					}else {
						thisDistributiondata.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
					}
				}            	
            	
            	if (StringUtils.isNotBlank(expid)) {
            		thisDistributiondata.setExpert(expid);
				}else{
					thisDistributiondata.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
					mark = false;
				}
            };
            
            /*比对参考文献*/
            String refjson = thisDistributiondata.getRefjson();
        	List<String> refIds = new ArrayList<>();
        	if (StringUtils.isNotBlank(refjson)) {
        		String[] refStr = refjson.replace("，", ",").split(",");
        		refIds = this.baseinfotmpService.findRefIdByFilemarkAndSerialNumAndFileType(fileMark[0], 0, refStr);
        		if (!refIds.isEmpty()) {
        			JSONArray refjsonArr = this.refService.getRefJSONArrayByIds(refIds);
        			if (!refjsonArr.isEmpty()) {
        				thisDistributiondata.setRefjson(refjsonArr.toString());
        			}else {
						thisDistributiondata.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
						mark = false;
					}
        		}else {
        			thisDistributiondata.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
					mark = false;
        		}
        	};
        	
        	if (!mark) {
        		thisDistributiondata.setSynchstatus(i + 1);
            	failList.add(thisDistributiondata);
            	flag = mark;
			}
        }
        /* 构建的集合若通过必填字段的校验，去重，比对数据源、参考文献，补充属性值*/
        if (flag) {
        	List<Distributiondata> distributionList = new ArrayList<>();
        	for (int i = 0; i < list.size(); i++) {
            	/* 根据学名去重*/
            	list.get(i).setId(UUIDUtils.getUUID32());
            	list.get(i).setInputer(thisUser.getId());
            	list.get(i).setInputtime(new Timestamp(System.currentTimeMillis()));
            	list.get(i).setSynchdate(new Timestamp(System.currentTimeMillis()));
            	list.get(i).setStatus(1);
            	list.get(i).setSynchstatus(0);
				/*this.distributiondataRepository.save(list.get(i));*/
            	distributionList.add(list.get(i));
            }
        	long start = System.currentTimeMillis();
        	this.batchInsertService.batchInsertDistributiondata(distributionList);
        	long end = System.currentTimeMillis();
            System.out.println("上传成功：" + (end - start) / 1000);
        }else {
			for (int i = 0; i < failList.size(); i++) {	// 参考保护
				JSONObject json = new JSONObject();
				json.put("num", failList.get(i).getSynchstatus());
				json.put("scientificname", failList.get(i).getTaxon().getScientificname());
				json.put("county", failList.get(i).getCounty());
				json.put("sourcesid", failList.get(i).getSourcesid());
				json.put("expert", failList.get(i).getExpert());
				json.put("refjson", failList.get(i).getRefjson());
				distArr.add(i, json);
			}
		}
        return distArr;
	}
	/**
	 * 方法说明:
	 * 	1、根据parseType获取对应的地理实体全称集合
	 * 	2、将地理实体全称集合转成数组(地理全称)
	 * 	3、根据地理实体全称集合获取地理名称简称数组
	 * 	4、利用AC自动机和地理名称简称数组获取'分布描述'内容中的地理实体名称简称
	 * 	5、 用set集合对第4步的结果去重
	 * 	6、 查询数据库地理实体构建Map集合：key - 地理实体名称， value - 地理实体
	 * 	7、 遍历set集合，从简称数组和全程数组中获取地理名称全称
	 * 	8、 Map集合由键取值
	 * 	9、封装JSONArray
	 * @param disContent
	 * @param parseType
	 * @return
	 */
	@Override
	public JSON parseGeoobject(String disContent, String parseType) {
		String pid1 = "100000";
		String pid2 = "__0000";
		List<String> cngeonames = new ArrayList<String>();									// 全称集合
		switch (parseType) {
		case "1":
			cngeonames = this.geoobjectRepository.findGeoobjectProvinceCngeoname(pid1);		// 省地理分布名称
			break;
		case "2":
			cngeonames = this.geoobjectRepository.findGeoobjectCityCngeoname(pid2, pid1);	// 市地理分布名称
			break;
		case "3":
			cngeonames = this.geoobjectRepository.findGeoobjectCountyCngeoname(pid2);		// 县地理分布名称
			break;
		case "4":	
			cngeonames = this.geoobjectRepository.findGeoobjectArealCngeoname();			// 地理分布区
			break;
		case "5":
			cngeonames = this.geoobjectRepository.findGeoobjectCountryCngeoname("0");		// 国家
			break;
		}
		String[] fullName = cngeonames.toArray(new String[cngeonames.size()]);				// 全称数组
		String[] keywords = new String[cngeonames.size()];									// AC自动机参数 -- 简称数组
		
		if (parseType.equals("1")) {
			for (int i = 0; i < cngeonames.size(); i++) {
				String cngeoname = cngeonames.get(i);
				if (cngeoname.contains("特别行政区") || cngeoname.contains("自治区")) {
					cngeoname = cngeoname.substring(0, 2);
				}else {
					cngeoname = cngeoname.substring(0,cngeoname.length()-1);
				}
				keywords[i] = cngeoname;
			}
		}
		if (parseType.equals("2")) {
			for (int i = 0; i < cngeonames.size(); i++) {
				String cngeoname = cngeonames.get(i);
				if (cngeoname.contains("族自治州") || cngeoname.contains("族自治县")) {
					cngeoname = cngeoname.substring(0, cngeoname.length()-5);
				}else if(cngeoname.contains("地区") || cngeoname.contains("林区") || cngeoname.contains("城区") || cngeoname.contains("堂区") || cngeoname.contains("新区")){
					if (cngeoname.length() > 3) {
						cngeoname = cngeoname.substring(0,cngeoname.length()-2);
					}else {
						cngeoname = cngeoname.substring(0,cngeoname.length());
					}
				}else if(cngeoname.length() > 2){
					cngeoname = cngeoname.substring(0,cngeoname.length()-1);
				}else {
					cngeoname = cngeoname.substring(0,cngeoname.length());
				}
				keywords[i] = cngeoname;
			}
		}
		if (parseType.equals("3")) {
			for (int i = 0; i < cngeonames.size(); i++) {
				String cngeoname = cngeonames.get(i);
				if (cngeoname.contains("自治县")) {
					cngeoname = cngeoname.substring(0, cngeoname.length()-3);
				}else if(cngeoname.length() > 2){
					cngeoname = cngeoname.substring(0,cngeoname.length()-1);
				}else {
					cngeoname = cngeoname.substring(0,cngeoname.length());
				}
				keywords[i] = cngeoname;
			}
		}
		if (parseType.equals("4") || parseType.equals("5")) {
			for (int i = 0; i < cngeonames.size(); i++) {
				keywords[i] = cngeonames.get(i);
			}
		}
        ACSearch search = new ACSearch(keywords);
        StringSearchResult[] findAll = search.findAll(disContent);
        
        Set<String> addressSet = new HashSet<String>();				// set集合为AC自动机处理结果去重
        for (StringSearchResult result : findAll) {
        	addressSet.add(result.keyword());
        }
        
		List<Geoobject> geoList = this.geoobjectRepository.findAll();
		Map<String, Geoobject> geoMap = new HashMap<>();			// key: 地理实体名称(全称)，value：地理实体
		for (int i = 0; i < geoList.size(); i++) {
			geoMap.put(geoList.get(i).getCngeoname(), geoList.get(i));
		}
		
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		
		
		for (String refName : addressSet) {
			JSONObject row = new JSONObject();
			String target = null;
			for (int i = 0; i < keywords.length; i++) {
				if (refName.equals(keywords[i])) {
					target = fullName[i];
					//System.out.println(fullName[i]);
				}
			}
			if (StringUtils.isNotBlank(target)) {
				row.put("id", geoMap.get(target).getId());
				row.put("text", "&nbsp;&nbsp;<b><i>"+ target +"(" + geoMap.get(target).getEngeoname() + ")</i></b>");
				items.add(row);
			}
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public List<Distribution> getDistributionByTaxaset(String taxasetId) {
		List<Object[]> list = distributiondataRepository.findDistributionByTaxaset(taxasetId);
		List<Distribution> resultlist = new ArrayList<>();
		for (Object[] obj : list) {
			Distribution entity = new Distribution();
			entity.setRecordId(obj[0].toString());
			entity.setNameCode(obj[1].toString());
			String geojson = obj[2].toString();
			if (null != obj[3]) {
				try {
					entity.setDistributionBack(obj[3].toString());
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}else {
				entity.setDistributionBack(null);
			}
			if (StringUtils.isNotBlank(geojson)) {
				JSONObject jsonObject = CommUtils.strToJSONObject(geojson);
				String[] geos = jsonObject.get("geoIds").toString().split("&");
				StringBuffer str = new StringBuffer();
				for (String geoId : geos) {
					if (StringUtils.isNotBlank(geoId)) {
						Geoobject geoobject = geoobjectRepository.findOneByIdAndPid(geoId, "100000");
						if (null != geoobject) {
							str.append(geoobject.getShortName());
							str.append(",");
						}else {
							/*throw new ValidationException("找不到分布地或分布地不包含省份，Geoobject.id="+geoId);*/
							System.out.println("找不到分布地或分布地不包含省份，Geoobject.id= " + geoId);
						}
					}
				}
				if (str.toString().length() > 0) {
					String distributionC = str.toString().substring(0,str.toString().length()-1);//去掉最后的逗号
					entity.setDistributionC(distributionC);
					resultlist.add(entity);
				}
			}
		}
		return resultlist;
	}

	@Override
	public List<Distribution> getDistributionByTaxasetForFish(String taxasetId) {
		List<Object[]> list = distributiondataRepository.findDistributionByTaxasetForFish(taxasetId);
		List<Distribution> resultlist = new ArrayList<>();
		//  a.id, a.taxon_id, a.discontent
		for (Object[] obj : list) {
			Distribution entity = new Distribution();
			entity.setRecordId(obj[0].toString());
			entity.setNameCode(obj[1].toString());
			String discontent = obj[2].toString();
			discontent.replace("，", "、").trim();
			discontent.replace("。", "、").trim();
			discontent.replace("；", "、").trim();
			if (null != obj[2]) {
				entity.setDistributionBack(obj[2].toString());
			}else {
				entity.setDistributionBack(null);
			}
			String[] areaArr = discontent.split("、");
			StringBuffer str = new StringBuffer();
			for (String area : areaArr) {
				if (StringUtils.isNotBlank(area)) {
						str.append(area.trim());
						str.append(",");
				}
			}
			if (str.toString().length() > 0) {
				String distributionC = str.toString().substring(0,str.toString().length()-1);//去掉最后的逗号
				entity.setDistributionC(distributionC);
				resultlist.add(entity);
			}
		}
		return resultlist;
	}

	@Override
	public JSON handelDistributionData() {
		List<Distributiondata> list = this.distributiondataRepository.findDistributiondataByDataset();
		for (Distributiondata distribution : list) {
			String geojson = distribution.getGeojson();
			String content = distribution.getDiscontent();
			if (content.contains("市市辖区")) {
				content = content.replace("市市辖区", "");
				distribution.setDiscontent(content);
				System.out.println(content);
				this.distributiondataRepository.save(distribution);
			}
			/*if (StringUtils.isNotBlank(geojson)) {
				String disContent = getDistributionEntity(geojson);
				distribution.setDiscontent(disContent);
				System.out.println(disContent);
				this.distributiondataRepository.save(distribution);
			}*/
		}
		return null;
	}

	private String getDistributionEntity(String geojson) {
		JSONObject obj = JSON.parseObject(geojson);
		String disStr = obj.get("geoIds").toString();
		String[] split = disStr.split("&");
		StringBuilder sb = new StringBuilder();
		sb.append("分布于：");
		for (int i = 0; i < split.length; i++) {
			Geoobject geoobject = this.geoobjectRepository.findOneById(split[i]);
			sb.append(geoobject.getShortName());
			sb.append("、");
		}
		String disContent = sb.toString();
		if (disContent.length() > 0) {
			disContent = disContent.toString().substring(0,disContent.length()-1);//去掉最后的逗号
		}
		return disContent;
	}

	@Override
	public String getGeoJSON(String geojsonStr) {
		JSONObject rowObj = JSONObject.parseObject(geojsonStr);
		System.out.println(rowObj.get("items").toString());
		JSONArray tableData = JSONArray.parseArray(rowObj.get("items").toString());
		JSONObject rowData = new JSONObject();
		StringBuilder geojson = new StringBuilder();
		for (int j = 0; j < tableData.size(); j++) {
			rowData = tableData.getJSONObject(j);
			String geoId = rowData.get("id").toString();
			geojson.append(geoId).append("&");
		}
		String result = geojson.toString();
		if (StringUtils.isNotBlank(result)) {
			result = "{\"geoIds\": \"" + result.substring(0, result.lastIndexOf("&")) + "\"}";
			return result;
		} else {
			return null;
		}
	}
}
