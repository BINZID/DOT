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
import org.big.entity.Traitdata;
import org.big.entity.Description;
import org.big.entity.Expert;
import org.big.entity.Taxon;
import org.big.entity.Traitontology;
import org.big.entity.Traitproperty;
import org.big.entity.Traitset;
import org.big.entity.TraitsetHasTraitontology;
import org.big.entity.User;
import org.big.entity.UserDetail;
import org.big.repository.ExpertRepository;
import org.big.repository.TaxonRepository;
import org.big.repository.TraitdataRepository;
import org.big.repository.TraitontologyRepository;
import org.big.repository.TraitpropertyRepository;
import org.big.repository.TraitsetHasTraitontologyRepository;
import org.big.repository.TraitsetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class TraitdataServiceImpl implements TraitdataService {
	@Autowired
	private TraitdataRepository traitdataRepository;
	@Autowired
	private TaxonService taxonService;
	@Autowired
	private DatasourceService datasourceService;
	@Autowired
	private DescriptionService descriptionService;
	@Autowired
	private TraitsetService traitsetService;
	@Autowired
	private TraitontologyRepository traitontologyRepository;
	@Autowired
	private TraitpropertyRepository traitpropertyRepository;
	@Autowired
	private RefService refService;
	@Autowired
	private TraitsetRepository traitsetRepository;
	@Autowired
	private UserService userService;
	@Autowired
    private TaxonRepository taxonRepository;
	@Autowired
	private ExcelService excelService;
	@Autowired
	private BaseinfotmpService baseinfotmpService;
	@Autowired
	private ExpertService expertService;
	@Autowired
	private ExpertRepository expertRepository;
	@Autowired
	private TraitsetHasTraitontologyRepository traitsetHasTraitontologyRepository;
	@Autowired
	private BatchInsertService batchInsertService;
	
	@Override
	public JSON addTraitdata(String taxonId, HttpServletRequest request) {
		Traitdata thisTraitdata = new Traitdata();
		// 关系(关系类型)
		Enumeration<String> paraNames = request.getParameterNames();
		String paraName = null;
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("traitdataId_") == 0) {
				thisTraitdata.setId(request.getParameter(paraName));
			}
			
			if (paraName.indexOf("trainsetid_") == 0) {
				thisTraitdata.setTrainsetid(request.getParameter(paraName));
			}
			
			if (paraName.indexOf("trainSourcesid_") == 0) {
				thisTraitdata.setSourcesId(request.getParameter(paraName));
			}
			
			if (paraName.indexOf("desid_") == 0) {
				thisTraitdata.setDesid(request.getParameter(paraName));
			}
		}
		
		thisTraitdata.setInputtime(new Timestamp(System.currentTimeMillis()));
		
		JSONObject thisResult = new JSONObject();
		try {
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			thisTraitdata.setInputer(thisUser.getId());
			thisTraitdata.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisTraitdata.setStatus(1);
			thisTraitdata.setSynchstatus(0);
			JSONArray handleReferenceToJson = (JSONArray) handleReferenceToJson(request);
			if (handleReferenceToJson.size() > 0) {
				thisTraitdata.setRefjson(handleReferenceToJson.toJSONString());
			}
			JSONArray handleTraitDescToJson = (JSONArray)handleTraitDescToJson(request);
			if (handleTraitDescToJson.size() > 0) {
				thisTraitdata.setTraitjson(handleTraitDescToJson.toJSONString());
			}
			
			thisTraitdata.setTaxon(taxonService.findOneById(taxonId));
			
			this.traitdataRepository.save(thisTraitdata);
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
		int countTraitdataNum = 0;
		String traitdataReferencesPageE = null;
		String traitdataReferencesPageS = null;
		String traitdataReferenceId = null;
		String jsonStr = null;
		
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("countTraitdataReferences_") == 0) {
				countTraitdataNum = Integer.parseInt(request.getParameter(paraName));
			}
			if (paraName.indexOf("traitdataId_") == 0) {
				formNum = Integer.parseInt(paraName.substring(paraName.indexOf("_") + 1));
			}
		}
		
		for (int i = 1; i <= countTraitdataNum; i++) {
			traitdataReferenceId = request.getParameter("traitdataReferences_" + formNum + "_" + i);
			traitdataReferencesPageS = request.getParameter("traitdataReferencesPageS_" + formNum + "_" + i);
			traitdataReferencesPageE = request.getParameter("traitdataReferencesPageE_" + formNum + "_" + i);
			if (StringUtils.isNotBlank(traitdataReferenceId) && StringUtils.isNotBlank(traitdataReferencesPageS)
					&& StringUtils.isNotBlank(traitdataReferencesPageE)) {
				jsonStr = "{" 
						+ "\"refId\"" + ":\"" + traitdataReferenceId + "\","
						+ "\"refS\"" + ":\"" + traitdataReferencesPageS + "\"," 
						+ "\"refE\"" + ":\"" + traitdataReferencesPageE + "\""
						+ "}";
				JSONObject jsonText = JSON.parseObject(jsonStr);
				jsonArray.add(jsonText);
			}
		}
		return jsonArray;
	}

	@Override
	public JSON handleTraitDescToJson(HttpServletRequest request) {
		JSONArray jsonArray = new JSONArray();
		Enumeration<String> paraNames = request.getParameterNames();
		String paraName = null;
		int formNum = 0;
		int countTraitdataNum = 0;
		String traitontology = null;
		String traitProperty = null;
		String traitValue = null;
		String traitBase = null;
		String traitUnit = null;
		
		String traitjson = null;
		
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("countTraitdataValue_") == 0) {
				countTraitdataNum = Integer.parseInt(request.getParameter(paraName));
			}
			if (paraName.indexOf("traitdataId_") == 0) {
				formNum = Integer.parseInt(paraName.substring(paraName.indexOf("_") + 1));
			}
		}
		
		for (int i = 1; i <= countTraitdataNum; i++) {
			traitontology = request.getParameter("traitontology_" + formNum + "_" + i);
			traitProperty = request.getParameter("traitdataProperty_" + formNum + "_" + i);
			traitValue = request.getParameter("traitdataValue_" + formNum + "_" + i);
			traitBase = request.getParameter("traitdataBase_" + formNum + "_" + i);
			traitUnit = request.getParameter("traitdataUnit_" + formNum + "_" + i);
			
			if (StringUtils.isNotBlank(traitontology) && StringUtils.isNotBlank(traitProperty)
					&& StringUtils.isNotBlank(traitValue)) {
				traitjson = "{"
						+ "\"traitontology\"" + ":\"" + traitontology + "\","
						+ "\"traitProperty\"" + ":\"" + traitProperty + "\","
						+ "\"traitValue\"" + ":\"" + traitValue + "\","
						+ "\"traitBase\"" + ":\"" + traitBase + "\","
						+ "\"traitUnit\"" + ":\"" + traitUnit + "\""
						+ "}";
				JSONObject jsonText = JSON.parseObject(traitjson);
				jsonArray.add(jsonText);
			}
		}
		return jsonArray;
	}
	
	@Override
	public boolean deleteOne(HttpServletRequest request) {
		String traitdataId = request.getParameter("traitdataId");
		if (StringUtils.isNotBlank(traitdataId)) {
			if (null != this.traitdataRepository.findOneById(traitdataId)) {
				this.traitdataRepository.deleteOneById(traitdataId);
			}
			return true;
		}
		return false;
	}

	@Override
	public JSON findTraitdataListByTaxonId(String taxonId, HttpServletRequest request) {
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
		Page<Traitdata> thisPage = this.traitdataRepository.searchTraitdatasByTaxonId(
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), taxonId);
		
		JSONArray rows = new JSONArray();
		List<Traitdata> thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			String sourcesid = thisList.get(i).getSourcesId();
			if (StringUtils.isNotBlank(sourcesid)) {
				if (null != this.datasourceService.findOneById(sourcesid)) {
					thisList.get(i).setSourcesId(this.datasourceService.findOneById(sourcesid).getTitle());
				}
			}
			String desid = thisList.get(i).getDesid();
			if (StringUtils.isNotBlank(desid)) {
				if (null != this.descriptionService.findOneById(desid)) {
					thisList.get(i).setDesid(this.descriptionService.findOneById(desid).getDestitle());
				}
			}
			
			String trainsetid = thisList.get(i).getTrainsetid();
			if (StringUtils.isNotBlank(trainsetid)) {
				if (null != this.traitsetService.findOneById(trainsetid)) {
					thisList.get(i).setTrainsetid(this.traitsetService.findOneById(trainsetid).getName());
				}
			}
			rows.add(i, thisList.get(i));
		}
		
		thisSelect.put("total", thisPage.getTotalElements());		// NO1：总数
		thisSelect.put("page", offset_serch);						// NO2：offset
		thisSelect.put("rows", rows);								// NO3：Taxon下的所有Citation
		return thisSelect;
	}

	@Override
	public JSON editTraitdata(String taxonId) {
		JSONObject traitdatas = new JSONObject();
		JSONArray traitdataArr = new JSONArray();
		try {
			if (StringUtils.isNotBlank(taxonId)) {
				List<Traitdata> list = this.traitdataRepository.findTraitdataListByTaxonId(taxonId);
				for (int i = 0; i < list.size(); i++) {
					JSONArray refjson = this.refService.refactoringRef(list.get(i).getRefjson());
					JSONArray traitjson = this.refactoringTraitDesc(list.get(i).getTraitjson());
					JSONObject json = new JSONObject();
					json.put("id", list.get(i).getId());
					
					// 数据源
					String sourcesid = list.get(i).getSourcesId();
					String sourcesTitle = this.datasourceService.findOneById(sourcesid).getTitle();
					json.put("sourcesid", sourcesid);
					json.put("sourcesTitle", sourcesTitle);
					
					// 共享协议
					String trainsetid = list.get(i).getTrainsetid();
					String trainsetName = this.traitsetRepository.findOneById(trainsetid).getName();
					json.put("trainsetid", trainsetid);
					json.put("trainsetName", trainsetName);
					
					// 特征描述
					String desid = list.get(i).getDesid();
					if (StringUtils.isNotBlank(desid)) {
						Description thisDescription = this.descriptionService.findOneById(desid);
						json.put("desid", desid);
						json.put("destitle", thisDescription.getDestitle());
					}
					
					json.put("traitjson", traitjson.toJSONString());
					
					json.put("refjson", refjson.toJSONString());
					traitdataArr.add(i, json);
				}
				traitdatas.put("traitdatas", traitdataArr);
			}
		} catch (NumberFormatException e) {
		}
		return traitdatas;
	}

	@Override
	public JSONArray BuildTrait(HttpServletRequest request) {
		JSONArray traits = new JSONArray();
		JSONArray traitList=JSONArray.parseArray(request.getParameter("traitjson"));
		try {
			for(int i=0;i<traitList.size();i++){
				Traitontology thisTraitontology = this.traitontologyRepository.findOneById(traitList.getJSONObject(i).getString("traitontology"));
				Traitproperty thisTraitproperty = this.traitpropertyRepository.findOneById(traitList.getJSONObject(i).getString("traitProperty"));
				JSONObject ref=new JSONObject();
				String enterm = thisTraitontology.getEnterm();
				if (StringUtils.isNotBlank(enterm)) {
					ref.put("traitontology", thisTraitontology.getCnterm() + "（" + thisTraitontology.getEnterm() + "）");
				}else {
					ref.put("traitontology", thisTraitontology.getCnterm());
				}
				ref.put("traitdataProperty", thisTraitproperty.getCnterm());
				ref.put("traitdataValue", traitList.getJSONObject(i).getString("traitValue"));
				ref.put("traitdataBase", traitList.getJSONObject(i).getString("traitBase"));
				String traitUnit = traitList.getJSONObject(i).getString("traitUnit");
				switch (traitUnit) {
				case "1":
					ref.put("traitdataUnit", "毫米(mm)");
					break;
				case "2":
					ref.put("traitdataUnit", "厘米(cm)");
					break;
				case "3":
					ref.put("traitdataUnit", "克(g)");
					break;
				case "4":
					ref.put("traitdataUnit", "千克(kg)");
					break;
				default:
					ref.put("traitdataUnit", traitUnit);
					break;
				}
				traits.add(ref);
			}
		} catch (Exception e) {
		}
		return traits;
	}

	
	@Override
	public JSONArray refactoringTraitDesc(String traitjson) {
		JSONArray traits = new JSONArray();
		JSONArray traitList = new JSONArray();
		if (StringUtils.isNotBlank(traitjson)) {
			traitList =JSONArray.parseArray(traitjson);
		}
		try {
			for (int i = 0; i < traitList.size(); i++) {
				Traitontology thisTraitontology = this.traitontologyRepository.findOneById(traitList.getJSONObject(i).getString("traitontology"));
				Traitproperty thisTraitproperty = this.traitpropertyRepository.findOneById(traitList.getJSONObject(i).getString("traitProperty"));
				JSONObject trait = new JSONObject();
				String enterm = thisTraitontology.getEnterm();
				trait.put("num", (i + 1));
				//术语
				trait.put("traitontology", thisTraitontology.getId());
				if (StringUtils.isNotBlank(enterm)) {
					trait.put("traitontologyName", thisTraitontology.getCnterm() + "（" + thisTraitontology.getEnterm() + "）");
				}else {
					trait.put("traitontologyName", thisTraitontology.getCnterm());
				}
				//属性
				trait.put("traitproperty", thisTraitproperty.getId());
				trait.put("traitpropertyName", thisTraitproperty.getCnterm());
				//值
				trait.put("traitvalue", traitList.getJSONObject(i).getString("traitValue"));
				//测量依据
				trait.put("traitbase", traitList.getJSONObject(i).getString("traitBase"));
				//测量单位
				String traitunit = traitList.getJSONObject(i).getString("traitUnit");
				if (StringUtils.isNotBlank(traitunit)) {
					trait.put("traitunit", traitunit);
				}else {
					trait.put("traitunit", 0);
				}
				traits.add(trait);
			}
		} catch (Exception e) {
		}
		return traits;
	}

	@Override
	public void export(HttpServletResponse response) throws IOException {
		String [] columnName = {"*学名", "*特征(结构部位)", "*属性", "*属性值", "单位", "测量依据", "*参考文献", "*数据源", "*审核专家", "版权所有者", "版权声明", "共享协议"};
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Traitdata> thisList = this.traitdataRepository.findTraitdataListByUserId(thisUser.getId());
		
		// 第1步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第2步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("特征数据");

		// 第3.1步，创建表头的列
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < columnName.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(columnName[i]);
			cell.setCellStyle(ExcelUtil.setTitleStyle(wb));
		}

		sheet.setColumnWidth(0, 10 * 256);
		sheet.setColumnWidth(1, 14 * 256);
		sheet.setColumnWidth(5, 10 * 256);
		sheet.setColumnWidth(6, 10 * 256);
		sheet.setColumnWidth(7, 10 * 256);
		sheet.setColumnWidth(8, 10 * 256);
		sheet.setColumnWidth(9, 12 * 256);
		sheet.setColumnWidth(10, 10 * 256);
		sheet.setColumnWidth(11, 10 * 256);

		// 第3.2步，创建单元格，并设置值
		for (int i = 0; i < thisList.size(); i++) {
			HSSFRow rows = sheet.createRow((int) (i + 1));
			Traitdata thisTraitdata = thisList.get(i);
			rows.createCell(0).setCellValue(thisTraitdata.getTaxon().getChname()); 
			List<String[]> traitjsonList = handelTraitJsonToArr(thisTraitdata.getTraitjson());
			for (int j = 0; j < traitjsonList.size(); j++) {
				rows.createCell(1).setCellValue(traitjsonList.get(j)[0]);
				rows.createCell(2).setCellValue(traitjsonList.get(j)[1]);
				rows.createCell(3).setCellValue(traitjsonList.get(j)[2]);
				rows.createCell(4).setCellValue(traitjsonList.get(j)[3]);
				rows.createCell(5).setCellValue(traitjsonList.get(j)[4]);
			}
			rows.createCell(6).setCellValue(thisTraitdata.getRefjson());
			rows.createCell(7).setCellValue(thisTraitdata.getSourcesId());
			rows.createCell(8).setCellValue(thisTraitdata.getExpert());
		}
		// 第4步，将文件存到浏览器设置的下载位置
		// 告诉浏览器用什么软件可以打开此文件
		response.setHeader("content-Type", "application/vnd.ms-excel");
		// 下载文件的默认名称
		response.setHeader("Content-Disposition", "attachment; filename=" + new String("特征数据".getBytes("gb2312"), "iso8859-1") + ".xls");
		OutputStream out = response.getOutputStream();
		try {
			wb.write(out);// 将数据写出去
		} catch (Exception e) {
		} finally {
			out.close();
		}
	}

	@Override
	public JSON findUploadedTraitdataList(Timestamp timestamp, HttpServletRequest request) {
		String searchText = request.getParameter("search");
		if (StringUtils.isBlank(searchText)) {
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
		List<Traitdata> thisList = new ArrayList<>();
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Page<Traitdata> thisPage = this.traitdataRepository.searchTraitdatasByUid(searchText, thisUser.getId(), timestamp, QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			List<String[]> traitjsonList = handelTraitJsonToArr(thisList.get(i).getTraitjson());
			for (int j = 0; j < traitjsonList.size(); j++) {
				row.put("traitBase", traitjsonList.get(j)[0]);
				row.put("traitUnit", traitjsonList.get(j)[1]);
				row.put("traitValue", traitjsonList.get(j)[2]);
				row.put("traitProperty", traitjsonList.get(j)[3]);
				row.put("traitontology", traitjsonList.get(j)[4]);
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

	/**
	 * <b>解析traitjson数据返回List<String[]>格式数据</b>
	 * <p> 解析traitjson数据返回List<String[]>格式数据</p>
	 * @param traitjson
	 * @return
	 */
	private List<String[]> handelTraitJsonToArr(String traitjson) {
		List<String[]> list = new ArrayList<>();
		String[] traitjsonArr = new String[5];
		if (StringUtils.isNotBlank(traitjson)) {
			JSONArray traitArr = JSONArray.parseArray(traitjson);
			System.out.println(traitArr.toJSONString());
			try {
				for (int i = 0; i < traitArr.size(); i++) {
					traitjsonArr[0] = traitArr.getJSONObject(i).getString("traitBase");
					switch (traitArr.getJSONObject(i).getString("traitUnit")) {
					case "1":
						traitjsonArr[1] = "毫米(mm)";
						break;
					case "2":
						traitjsonArr[1] = "厘米(cm)";
						break;
					case "3":
						traitjsonArr[1] = "克(g)";
						break;
					case "4":
						traitjsonArr[1] = "千克(kg)";
						break;
					default:
						traitjsonArr[1] = traitArr.getJSONObject(i).getString("traitUnit");
						break;
					}				 

					traitjsonArr[2] = traitArr.getJSONObject(i).getString("traitValue");
					String traitProperty = traitArr.getJSONObject(i).getString("traitProperty");
					String traitontology = traitArr.getJSONObject(i).getString("traitontology");
					Traitontology thisTraitontology = this.traitontologyRepository.findOneById(traitontology);
					Traitproperty thisTraitproperty = this.traitpropertyRepository.findOneById(traitProperty);
					traitjsonArr[3] = thisTraitproperty.getCnterm();
					traitjsonArr[4] = thisTraitontology.getCnterm() + "(" + thisTraitontology.getEnterm() + ")";
					list.add(i, traitjsonArr);
				}
			} catch (Exception e) {
			}
		}
		return list;
	}

	@Override
	public JSON uploadFile(MultipartFile file, String taxasetId, HttpServletRequest request) throws Exception {
		JSONObject thisResult = new JSONObject();
		List<Traitdata> traitdataList = new ArrayList<>();							// 上传文件的数据
		String[] fileMark = new String[3];
        fileMark[0] = request.getParameter("refFileMark");			// 参考文献文件标记
        fileMark[1] = request.getParameter("dsFileMark");				// 数据源文件标记
        fileMark[2] = request.getParameter("expFileMark");			// 专家文件标记
        String traitset = request.getParameter("traitset");
        UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        /*判断上传文件是否为空及是否是目标文件*/
        if (null == file || file.isEmpty()) {
            thisResult.put("message", "未找到上传的文件，请刷新页面或更换浏览器");
            thisResult.put("status", false);
            thisResult.put("code", -1);
            System.out.println("未找到上传的文件，请刷新页面或更换浏览器");
        }else if(StringUtils.isBlank(traitset)){
        	thisResult.put("message", "请为本次上传的特征数据选择或新建一个术语集");
        	thisResult.put("status", false);
        	thisResult.put("code", -11);
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
					/*String tableHeadArray[] = { "*学名", "*特征（结构部位）", "*属性", "*属性值", "单位", "测量依据", "*参考文献", "*数据源",
							"*审核专家", "版权所有者", "版权声明", "共享协议" };*/
                	String tableHeadArray[] = { "学名", "特征", "属性", "属性值", "单位", "测量依据", "参考文献", "数据源",
							"审核专家", "版权所有者", "版权声明", "共享协议" };
                    if(this.excelService.judgeRowConsistent(12,tableHeadArray,sheet.getRow(0))){
                        try {
                        	Traitdata thisTraitdata= new Traitdata();			// Traitdata对象
                            Row row = null;										// 记录行
                            List<String[]> traitsList = new ArrayList<>();
                            for (int i = 1; i <= rowNum; i++) {
                            	String[] traitsArr = new String[5];
                                row = sheet.getRow(i);
                                if (null == sheet.getRow(i)) { 
                                	continue; 									// 空行
                                }else {											// 构建集合对象
                                	String scientificname = excelService.getStringValueFromCell(row.getCell(0));
                                	Taxon thisTaxon = this.taxonRepository.findOneByScientificnameAndTaxasetId(scientificname, taxasetId);
                                	/*System.out.println(i + "\t" + scientificname + "\t" + thisTaxon.getScientificname());*/
                                	/*术语   属性   值	   单位   依据*/
                                	traitsArr[0] = excelService.getStringValueFromCell(row.getCell(1));
                                	traitsArr[1] = excelService.getStringValueFromCell(row.getCell(2));
                                	traitsArr[2] = excelService.getStringValueFromCell(row.getCell(3)); 
                                	traitsArr[3] = excelService.getStringValueFromCell(row.getCell(4)); 
                                	traitsArr[4] = excelService.getStringValueFromCell(row.getCell(5)); 
                                	thisTraitdata = new Traitdata(
                                			excelService.getStringValueFromCell(row.getCell(1)),
                                			excelService.getStringValueFromCell(row.getCell(6)),
                                			excelService.getStringValueFromCell(row.getCell(7)),
                                			excelService.getStringValueFromCell(row.getCell(8)),
                                			thisTaxon);
                                	String remark = "{"
                                			+ "\"" + "rightsholder" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(9)) + "\","
                                			+ "\"" + "copyright" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(10)) + "\","
                                			+ "\"" + "license" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(11)) + "\","
                                			+ "}";
                                	thisTraitdata.setRemark(remark);
                                	traitsList.add(traitsArr);
                                	traitdataList.add(thisTraitdata);
								}	
                            }
							JSONArray verifyListEntity = verifyListEntity(traitdataList, thisUser, traitsList, fileMark, traitset);
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
	private JSONArray verifyListEntity(List<Traitdata> list, User thisUser, List<String[]> traitsList, String[] fileMark, String traitset) throws Exception {
		JSONArray traitArr = new JSONArray();
		Traitset thisTraitset = this.traitsetRepository.findOneById(traitset);
		List<Traitdata> failList = new ArrayList<>();
        boolean flag = true;
        for (int i = 0; i < list.size(); i++) {			// 参考保护实现
        	Traitdata thisTraitdata = list.get(i);
        	boolean mark = true;
        	if (null == thisTraitdata.getTaxon()) {
        		thisTraitdata.setTaxon(new Taxon("<span style='color:red'>未匹配到对应分类单元，请先上传分类单元</span>"));
        		mark = false;
			}
			if (StringUtils.isBlank(traitsList.get(i)[0]) && StringUtils.isBlank(traitsList.get(i)[1])
					&& StringUtils.isBlank(traitsList.get(i)[2])) {
        		thisTraitdata.setTraitjson("<span style='color:red'>特征数据：术语、属性、属性值均不能为空</span>");
        		mark = false;
			}else {
				if (StringUtils.isNotBlank(traitsList.get(i)[3])) {
					switch (traitsList.get(i)[3]) {
					case "mm":
						traitsList.get(i)[3] = "1";
						break;
					case "cm":
						traitsList.get(i)[3] = "2";
						break;
					case "g":
						traitsList.get(i)[3] = "3";
						break;
					case "kg":
						traitsList.get(i)[3] = "4";
						break;
					default:
						traitsList.get(i)[3] = traitsList.get(i)[3];
						break;
					}		
				}
				
				String ontologyId = this.traitontologyRepository.findOneByCntermOrEnterm(traitsList.get(i)[0]);
				String propertyId = this.traitpropertyRepository.findOneByCntermOrEnterm(traitsList.get(i)[1]);
				String ontologyUUID = UUIDUtils.getUUID32();
				String propertyUUID = UUIDUtils.getUUID32();
				String traitsetHasTraitontologyUUID = UUIDUtils.getUUID32();
				
				/*术语*/
				if (StringUtils.isNotBlank(ontologyId)) {
					traitsList.get(i)[0] = ontologyId;
				}else {	// 新建术语(保存) -- 返回新建术语Id
					Traitontology traitontology = new Traitontology(ontologyUUID,
							traitsList.get(i)[0], thisUser.getId(), new Timestamp(System.currentTimeMillis()), 1,
							new Timestamp(System.currentTimeMillis()), 0);
					this.traitontologyRepository.save(traitontology);
					TraitsetHasTraitontology traitsetHasTraitontology = new TraitsetHasTraitontology(
							traitsetHasTraitontologyUUID, "1", traitontology, thisTraitset);
					this.traitsetHasTraitontologyRepository.save(traitsetHasTraitontology);
					traitsList.get(i)[0] = ontologyUUID;
				}
				
				/*属性*/
				if (StringUtils.isNotBlank(propertyId)) {
					traitsList.get(i)[1] = propertyId;
				}else {	// 新建属性(保存) -- 返回新建属性Id
					Traitproperty traitproperty = new Traitproperty(propertyUUID, traitsList.get(i)[1], 1, 0);
					this.traitpropertyRepository.save(traitproperty);
					traitsList.get(i)[1] = propertyUUID;
				}
				
				String traitjson = "[{"
						+ "\"traitBase\"" + ":\"" + traitsList.get(i)[4] + "\","
						+ "\"traitUnit\"" + ":\"" + traitsList.get(i)[3] + "\","
						+ "\"traitValue\"" + ":\"" + traitsList.get(i)[2] + "\","
						+ "\"traitProperty\"" + ":\"" + traitsList.get(i)[1] + "\","
						+ "\"traitontology\"" + ":\"" + traitsList.get(i)[0] + "\""
						+ "}]";
				thisTraitdata.setTraitjson(traitjson);
			}
        	
        	/*数据源*/
            if (StringUtils.isBlank(thisTraitdata.getSourcesId())) {
            	thisTraitdata.setSourcesId("<span style='color:red'>数据源不能为空</span>");
            	mark = false;
            }else  {
            	String source = this.baseinfotmpService.findDsIdByFilemarkAndSerialNumAndFileType(fileMark[1], thisTraitdata.getSourcesId(), 1);
            	if (StringUtils.isNotBlank(source)) {
					thisTraitdata.setSourcesId(source);
				}else {
					thisTraitdata.setSourcesId("<span style='color:red'>未找到引用数据源，请检查引用数据存在或是否引用错误！</span>");
					mark = false;
				}
			};
			
			/*审核专家*/
			if (StringUtils.isBlank(thisTraitdata.getExpert())) {
				thisTraitdata.setExpert("<span style='color:red'>专家信息不能为空</span>");
            	mark = false;
            }else{
            	boolean tmp = thisTraitdata.getExpert().matches("[0-9]*");
            	String expid = null;
            	if (tmp) {
            		expid = this.baseinfotmpService.findExpIdByFilemarkAndSerialNumAndFileType(fileMark[2], 2, thisTraitdata.getExpert());
				}else {
					String expertId = this.expertRepository.findIdByCnName(thisTraitdata.getExpert());
					if (StringUtils.isNotBlank(expertId)) {
						expid = expertId;
					}else {
						thisTraitdata.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
					}
				}
            	
            	if (StringUtils.isNotBlank(expid)) {
            		thisTraitdata.setExpert(expid);
				}else{
					thisTraitdata.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
					mark = false;
				}
            };
            
            /*比对参考文献*/
        	String refjson = thisTraitdata.getRefjson();
        	List<String> refIds = new ArrayList<>();
        	if (StringUtils.isNotBlank(refjson)) {
        		String[] refStr = refjson.replace("，", ",").split(",");
        		refIds = this.baseinfotmpService.findRefIdByFilemarkAndSerialNumAndFileType(fileMark[0], 0, refStr);
        		if (!refIds.isEmpty()) {
        			JSONArray refjsonArr = this.refService.getRefJSONArrayByIds(refIds);
        			if (!refjsonArr.isEmpty()) {
        				thisTraitdata.setRefjson(refjsonArr.toString());
        			}else {
        				thisTraitdata.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
						mark = false;
					}
        		}else {
        			thisTraitdata.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
					mark = false;
        		}
        	};
        	
        	if (!mark) {
        		thisTraitdata.setSynchstatus(i + 1);
            	failList.add(thisTraitdata);
            	flag = mark;
			}
        }
        /* 构建的集合若通过必填字段的校验，去重，比对数据源、参考文献，补充属性值*/
        if (flag) {
        	List<Traitdata> traitdataList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
            	/* 根据学名去重*/
            	list.get(i).setTrainsetid(traitset);
            	list.get(i).setId(UUIDUtils.getUUID32());
            	list.get(i).setInputer(thisUser.getId());
            	list.get(i).setInputtime(new Timestamp(System.currentTimeMillis()));
            	list.get(i).setSynchdate(new Timestamp(System.currentTimeMillis()));
            	list.get(i).setStatus(1);
            	list.get(i).setSynchstatus(0);;
				/*this.traitdataRepository.save(list.get(i));*/
            	traitdataList.add(list.get(i));
            }
            long start = System.currentTimeMillis();
            this.batchInsertService.batchInsertTraitdata(traitdataList);
            long end = System.currentTimeMillis();
            System.out.println("Traitdata批量存储完成：" + (end - start));
        }else {
			for (int i = 0; i < failList.size(); i++) {	// 参考保护
				JSONObject json = new JSONObject();
				json.put("num", failList.get(i).getSynchstatus());
				json.put("scientificname", failList.get(i).getTaxon().getScientificname());
				json.put("traitjson", failList.get(i).getTraitjson());
				json.put("sourcesid", failList.get(i).getSourcesId());
				json.put("expert", failList.get(i).getExpert());
				traitArr.add(i, json);
			}
		}
        return traitArr;
    }

}
