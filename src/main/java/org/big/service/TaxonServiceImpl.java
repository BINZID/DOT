package org.big.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.big.sp2000.entity.*;
import org.big.common.*;
import org.big.entity.*;
import org.big.entityColChina.*;
import org.big.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class TaxonServiceImpl implements TaxonService {
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private RedlistRepository redlistRepository;
	@Autowired
	private RefRepository refRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private RankRepository rankRepository;
	@Autowired
	private DatasourceRepository datasourceRepository;
	@Autowired
	private ExcelService excelService;
	@Autowired
	private BaseinfotmpService baseinfotmpService;
	@Autowired
	private ExpertService expertService;
	@Autowired
	private RefService refService;
	@Autowired
	private TaxasetRepository taxasetRepository;
	@Autowired
	private ExpertRepository expertRepository;
	@Autowired
	private CitationRepository citationRepository;
	@Autowired
	private DistributiondataRepository distributiondataRepository;
	@Autowired
	private ProtectionRepository protectionRepository;
	@Autowired
	private TaxkeyService taxkeyService;
	@Autowired
	private TraitdataRepository traitdataRepository;
	@Autowired
	private CommonnameRepository commonnameRepository;
	@Autowired
	private OccurrenceRepository occurrenceRepository;
	@Autowired
	private MultimediaRepository multimediaRepository;
	@Autowired
	private DescriptionRepository descriptionRepository;
	@Autowired
	private TaxonHasTaxtreeRepository taxonHasTaxtreeRepository;
	@Autowired
	private BatchInsertService batchInsertService;
	/*@Autowired
	private TaxonEntityRepository taxonEntityRepository;
	@Autowired
	private DistributionEntityRepository distributionEntityRepository;
	@Autowired
	private GeoObjectChangeRepository geoObjectChangeRepository;
	@Autowired
	private ButterflyRepository butterflyRepository;*/

	@Override
	public JSON findTaxonList(HttpServletRequest request) {
		String taxasetId = request.getParameter("taxasetId");
		String searchText = request.getParameter("search");
		if (searchText == null || searchText.length() <= 0) {
			searchText = "";
		}
		int limit_serch = Integer.parseInt(request.getParameter("limit"));
		int offset_serch = Integer.parseInt(request.getParameter("offset"));
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		if(StringUtils.isBlank(sort)){
			sort = "orderNum";
			order = "asc";
		}
	
		JSONObject thisTable = new JSONObject();
		JSONArray rows = new JSONArray();
		List<Taxon> thisList = new ArrayList<>();
		Page<Taxon> thisPage = this.taxonRepository.searchInfo(searchText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), taxasetId);
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		String thisSelect = "";
		String thisEdit = "";
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
	        thisSelect="<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
	        thisEdit=
	        	"<a href=\"console/taxon/show/" + thisList.get(i).getId() + "\" title=\"查看\" target=\"_blank\">" + 
	        		"<span class=\"glyphicon glyphicon-eye-open\"></span>" + 
	        	"</a>  &nbsp;&nbsp;&nbsp;" +
	            "<a class=\"wts-table-edit-icon\" onclick=\"removeObject('" + thisList.get(i).getId() + "','taxon')\" >" +
	               "<span class=\"glyphicon glyphicon-remove\"></span>" +
	            "</a>";
			row.put("select", thisSelect);				
			row.put("scientificname", "<a href=\"console/taxon/edit/" + thisList.get(i).getId() + "\" title=\"编辑\" target=\"_blank\"><em>" + thisList.get(i).getScientificname() + "</em></a>");
			row.put("chname", thisList.get(i).getChname());
			row.put("authorstr", thisList.get(i).getAuthorstr());
			row.put("nomencode", thisList.get(i).getNomencode());
			switch (thisList.get(i).getTaxonCondition()) {
				case 0:
					row.put("taxonCondition", "<span class=\"label label-default\">未提交审核</span>");
					break;
				case 1:
					row.put("taxonCondition", "<span class=\"label label-info\">已提交审核</span>");
					break;
				case -1:
					row.put("taxonCondition", "<span class=\"label label-danger\">审核不通过</span>");
					break;
				case 2:
					row.put("taxonCondition", "<span class=\"label label-success\">审核通过</span>");
					break;
				default:
					break;
			}
			if(StringUtils.isNotBlank(thisList.get(i).getExpert())){
				User thisUser = this.userService.findbyID(thisList.get(i).getExpert());
				Expert thisExpert = this.expertService.findOneById(thisList.get(i).getExpert());
				try {
					if(null != thisExpert){	//先从Expert取
						row.put("taxonExamine", thisExpert.getCnName() + " (" + thisExpert.getExpEmail() + ") ");
					}else{					//再从User取审核人
						row.put("taxonExamine", thisUser.getNickname() + " (" + thisUser.getEmail() + ") ");
					}
				} catch (Exception e) {
					row.put("taxonExamine", thisExpert.getCnName() + " (" + thisExpert.getExpEmail() + ") ");
					e.printStackTrace();
				}
			}else {
				row.put("taxonExamine", "无审核人");
			}
			row.put("orderNum", thisList.get(i).getOrderNum());
			row.put("epithet", thisList.get(i).getEpithet());
			row.put("inputer", this.userService.findbyID(thisList.get(i).getInputer()).getNickname());
			String rankId = thisList.get(i).getRank().getId();
			Rank thisRank = this.rankRepository.findOneById(rankId);
			row.put("rankid", thisRank.getEnname() + "<" + thisRank.getChname() + ">");
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
	public JSON addTaxonBaseInfo(@Valid Taxon thisTaxon, HttpServletRequest request) {
		JSONObject thisResult = new JSONObject();
		try {
			if (thisTaxon.getOrderNum() == 0) {
				int maxOrderNum = this.taxonRepository.findMaxOrderNumByTaxasetId(thisTaxon.getTaxaset().getId());
				thisTaxon.setOrderNum((maxOrderNum + 1));
			}
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			thisTaxon.setInputer(thisUser.getId());
			thisTaxon.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisTaxon.setStatus(1);
			thisTaxon.setSynchstatus(0);
			JSONArray handleReferenceToJson = (JSONArray) handleReferenceToJson(request);
			if (handleReferenceToJson.size() > 0) {
				thisTaxon.setRefjson(handleReferenceToJson.toJSONString());
			}
			thisTaxon.setTaxonCondition(1);
			this.taxonRepository.save(thisTaxon);
			
			thisResult.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			thisResult.put("result", false);
		}
		
		String treeId = request.getParameter("taxTreeId");
		String targetTaxonId = request.getParameter("targetTaxonId");
		
		int num = 0;
		// 父级节点
		JSONArray nodesArr = new JSONArray();
		JSONArray nodeArr = hasPNode(thisResult, nodesArr, treeId, targetTaxonId, num);
		thisResult.put("nodeArr", nodeArr);
		return thisResult;
	}

	private JSONArray hasPNode(JSONObject thisResult, JSONArray nodesArr, String treeId, String targetTaxonId, int num) {
		TaxonHasTaxtree thisTaxonHasTaxtree = null;
		if (StringUtils.isNotBlank(targetTaxonId)) {
			thisTaxonHasTaxtree = this.taxonHasTaxtreeRepository.findOneByIds(targetTaxonId, treeId);
			if (thisTaxonHasTaxtree != null && StringUtils.isNotBlank(thisTaxonHasTaxtree.getPid())) {
				targetTaxonId = thisTaxonHasTaxtree.getPid();
				nodesArr.add(num, targetTaxonId);
				thisResult.put("num", ++num);
				hasPNode(thisResult, nodesArr, treeId, targetTaxonId, num);
			}
		}
		return nodesArr;
	}
	
	@Override
	public Taxon findOneById(String id) {
		return this.taxonRepository.findOneById(id);
	}

	@Override
	public JSON handleReferenceToJson(HttpServletRequest request) {
		JSONArray jsonArray = new JSONArray();
    	int countReferences = Integer.parseInt(request.getParameter("countReferences"));
		String referencesId = null;
		String referencesPageS = null;
		String referencesPageE = null;
		String jsonStr = null;
		for (int i = 1; i <= countReferences; i++) {
			referencesId = request.getParameter("references_" + i);
			referencesPageS = request.getParameter("referencesPageS_" + i);
			referencesPageE = request.getParameter("referencesPageE_" + i);
			if (StringUtils.isNotBlank(referencesId) && StringUtils.isNotBlank(referencesPageS)
					&& StringUtils.isNotBlank(referencesPageE)) {
				jsonStr = "{"
						+ "\"refId\"" + ":\"" + referencesId + "\","
						+ "\"refS\"" + ":\"" + referencesPageS + "\"," 
						+ "\"refE\"" + ":\"" + referencesPageE + "\""
						+ "}";
				JSONObject jsonText = JSON.parseObject(jsonStr);
				jsonArray.add(jsonText);
			}
		}
		return jsonArray;
	}

	@Override
	public boolean delTaxon(String taxonId) {
		// 0.删除多媒体
		multimediaRepository.delMultimediaByTaxonId(taxonId);
		// 1.删除
		occurrenceRepository.delOccurrenceByTaxonId(taxonId);
		// 2.删除俗名
		commonnameRepository.delCommonnameByTaxonId(taxonId);
		// 3.特征数据
		traitdataRepository.delTraitdataByTaxonId(taxonId);
		// 4.检索表
		taxkeyService.delTaxkeyByTaxonId(taxonId);
		// 5.保护数据
		protectionRepository.delProtectionByTaxonId(taxonId);
		// 6.删除分布
		distributiondataRepository.delDistributiondataByTaxonId(taxonId);
		// 7.删除描述
		descriptionRepository.delDescriptionByTaxaSetId(taxonId);
		// 8.删除引证
		citationRepository.deleteCitationByTaxaSetId(taxonId);
		// 9.删除分类树
		taxonHasTaxtreeRepository.delTaxonHasTaxtreesByTaxonId(taxonId);		
		// 10.删除taxon
		taxonRepository.deleteTaxonByTaxonId(taxonId);
		return true;
	}

	@Override
	public void updateOneById(@Valid Taxon thisTaxon) {
		thisTaxon.setSynchdate(new Timestamp(System.currentTimeMillis()));
		this.taxonRepository.save(thisTaxon);
	}

	@Override
	public JSON findBySelect(HttpServletRequest request, String taxonsetId) {
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
		
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		List<Taxon> thisList = new ArrayList<>();
		Page<Taxon> thisPage = this.taxonRepository.searchByTaxonInfo(findText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), taxonsetId);
		thisSelect.put("total_count", thisPage.getTotalElements());
		Boolean incompleteResulte = true;
		if ((thisPage.getTotalElements() / 30) > findPage)
			incompleteResulte = false;
		thisSelect.put("incompleteResulte", incompleteResulte);
		thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			row.put("id", thisList.get(i).getId());
			row.put("text", thisList.get(i).getScientificname());
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public JSONArray findTaxonByTaxasetAndRank(Taxaset thisTaxaset, Rank thisRank,int status) {
		JSONArray thisArray=new JSONArray();
		List<Taxon> thisList = this.taxonRepository.findTaxonByTaxasetAndRankAndStatus(thisTaxaset,thisRank,status);
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject thisTaxon = new JSONObject();
			thisTaxon.put("id", thisList.get(i).getId());
			thisTaxon.put("name", thisList.get(i).getScientificname());
			thisTaxon.put("title", thisList.get(i).getRemark());
			thisTaxon.put("isParent", false);
			thisTaxon.put("url", null);
			thisTaxon.put("target", null);
			thisArray.add(thisTaxon);
		}
		return thisArray;
	}

	@Override
	public JSONArray findTaxonByTaxasetAndRankAndName(Taxaset thisTaxaset, Rank thisRank,String taxonName,int status) {
		JSONArray thisArray=new JSONArray();
		List<Taxon> thisList = this.taxonRepository.searchTaxonByTaxasetAndRankAndTaxonNameAndStatus(thisTaxaset.getId(),thisRank.getId(),taxonName,status);
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject thisTaxon = new JSONObject();
			thisTaxon.put("id", thisList.get(i).getId());
			if(StringUtils.isBlank(thisList.get(i).getChname()))
				thisTaxon.put("name", thisList.get(i).getScientificname());
			else
				thisTaxon.put("name", thisList.get(i).getScientificname()+" "+thisList.get(i).getChname());
			thisTaxon.put("title", thisList.get(i).getRemark());
			thisTaxon.put("isParent", false);
			thisTaxon.put("url", null);
			thisTaxon.put("target", null);
			thisTaxon.put("click", "showTaxon('"+thisList.get(i).getId()+"')");
			thisArray.add(thisTaxon);
		}
		return thisArray;
	}

	@Override
	public JSON findTaxasetIdByTaxonId(HttpServletRequest request, String id) {
		JSONObject thisResult = new JSONObject();
		try {
			Taxon thisTaxon = this.taxonRepository.findOneById(id);
			String taxasetId = thisTaxon.getTaxaset().getId();
			thisResult.put("taxasetId", taxasetId);
		} catch (Exception e) {
		}
		return thisResult;
	}

	@Override
	public JSON findTaxonBasics(String id){
		JSONObject thisResult = new JSONObject();
		try {
			Taxon thisTaxon = this.taxonRepository.findOneById(id);
			thisResult.put("scientificname", thisTaxon.getScientificname());
			thisResult.put("authorstr", thisTaxon.getAuthorstr());
			thisResult.put("epithet", thisTaxon.getEpithet());
			thisResult.put("nomencode", thisTaxon.getNomencode());
			thisResult.put("rank", thisTaxon.getRank().getChname()+"<"+thisTaxon.getRank().getEnname()+">");
			thisResult.put("datasources", this.datasourceRepository.findOneById(thisTaxon.getSourcesid()).getTitle());
			thisResult.put("tci", thisTaxon.getTci());
			thisResult.put("taxaset", thisTaxon.getTaxaset().getTsname());
			thisResult.put("remark", thisTaxon.getRemark());
			if(thisTaxon.getRefjson()!=null)
				thisResult.put("refjson", JSONArray.parseArray(thisTaxon.getRefjson()).toString());
			else
				thisResult.put("refjson", "none");
		} catch (Exception e) {
		}
		return thisResult;
	}

	@Override
	public void ReviewByTaxonId(String id,int reviewStatus) {
		Taxon thisTaxon = this.taxonRepository.findOneById(id);
		if(reviewStatus==0 || reviewStatus==1){
			thisTaxon.setExpert(null);//重提审核
		}else{
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			thisTaxon.setExpert(thisUser.getId());
		}
		thisTaxon.setTaxonCondition(reviewStatus);
		this.taxonRepository.save(thisTaxon);
	}

	@Override
	public void export(String taxasetId, HttpServletResponse response) throws IOException {
		String[] columnName = {"*学名（接受名）", "*中文名", "*命名信息", "*命名法规", "*学名状态", "*分类等级", "*参考的分类体系", "界",
				"界拉丁名", "门", "门拉丁名", "纲", "纲拉丁名", "目", "目拉丁名", "科", "科拉丁名", "属", "属拉丁名", "*参考文献", "*数据源",
				"*审核专家", "版权所有者", "版权声明", "共享协议", "备注" };
		List<Taxon> thisList = this.taxonRepository.findTaxonByTaxasetId(taxasetId);
		
		// 第1步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第2步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("分类单元信息");

		// 第3.1步，创建表头的列
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < columnName.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(columnName[i]);
			cell.setCellStyle(ExcelUtil.setTitleStyle(wb));
		}

		sheet.setColumnWidth(0, 15 * 256);
		sheet.setColumnWidth(1, 10 * 256);
		sheet.setColumnWidth(2, 12 * 256);
		sheet.setColumnWidth(3, 12 * 256);
		sheet.setColumnWidth(4, 12 * 256);
		sheet.setColumnWidth(5, 12 * 256);
		sheet.setColumnWidth(6, 16 * 256);
		sheet.setColumnWidth(20, 12 * 256);
		sheet.setColumnWidth(21, 10 * 256);
		sheet.setColumnWidth(22, 12 * 256);
		sheet.setColumnWidth(23, 13 * 256);
		sheet.setColumnWidth(24, 12 * 256);
		sheet.setColumnWidth(25, 25 * 256);

		// 第3.2步，创建单元格，并设置值
		try {
			for (int i = 0; i < thisList.size(); i++) {
				HSSFRow rows = sheet.createRow((int) (i + 1));
				Taxon thisTaxon = thisList.get(i);
				rows.createCell(0).setCellValue(thisTaxon.getScientificname()); // 设置第1列序号
				rows.createCell(1).setCellValue(thisTaxon.getChname());
				rows.createCell(2).setCellValue(thisTaxon.getAuthorstr());
				rows.createCell(3).setCellValue(thisTaxon.getNomencode());
				rows.createCell(5).setCellValue(thisTaxon.getRank().getChname());
				rows.createCell(6).setCellValue(thisTaxon.getRefClassSys());
				rows.createCell(19).setCellValue(this.refService.handleRefjsonForExport(thisTaxon.getRefjson()));
				/*Datasource thisDatasource = this.datasourceRepository.findOneById(thisTaxon.getSourcesid());
				String dsStr = thisDatasource.getTitle() + "," + thisDatasource.getVersions() + "," + thisDatasource.getdType();*/
				rows.createCell(20).setCellValue(thisTaxon.getSourcesid());
				Expert thisExpert = this.expertService.findOneById(thisTaxon.getExpert());
				if (null != thisExpert) {
					rows.createCell(21).setCellValue(thisExpert.getCnName() + "<" + thisExpert.getExpEmail() + ">");
				}
				rows.createCell(25).setCellValue(thisTaxon.getRemark());
			}
		} catch (Exception e) {
		}
		// 第4步，将文件存到浏览器设置的下载位置
		// 告诉浏览器用什么软件可以打开此文件
		response.setHeader("content-Type", "application/vnd.ms-excel");
		// 下载文件的默认名称
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String("分类单元信息".getBytes("gb2312"), "iso8859-1") + ".xls");
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
		List<Taxon> taxonList = new ArrayList<>();							// 上传文件的数据

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
            	Sheet sheet = wb.getSheetAt(0);
            	int rowNum = sheet.getLastRowNum();
                //判断表头是否符合规则
                try {
					/*String tableHeadArray[] = {"*学名（接受名）", "*中文名", "*命名信息", "*命名法规", "*学名状态", "*分类等级", "*参考的分类体系", "界",
							"界拉丁名", "门", "门拉丁名", "纲", "纲拉丁名", "目", "目拉丁名", "科", "科拉丁名", "属", "属拉丁名", "*参考文献", "*数据源",
							"*审核专家", "版权所有者", "版权声明", "共享协议", "备注" };*/
					String tableHeadArray[] = {"学名（接受名）", "中文名", "命名信息", "命名法规", "学名状态", "分类等级", "参考的分类体系", "界",
							"界拉丁名", "门", "门拉丁名", "纲", "纲拉丁名", "目", "目拉丁名", "科", "科拉丁名", "属", "属拉丁名", "参考文献", "数据源",
							"审核专家", "版权所有者", "版权声明", "共享协议", "备注" };
                    if(this.excelService.judgeRowConsistent(26, tableHeadArray, sheet.getRow(0))){
                        try {
                        	Taxon thisTaxon = new Taxon();						// Taxon对象
                            Row row = null;										// 记录行
                            for (int i = 1; i <= rowNum; i++) {
                                row = sheet.getRow(i);
                                if (null == sheet.getRow(i)) { 					// 记录行
                                	continue; 									// 空行
                                }else {											// 构建集合对象
                                	String remark = "{"
                                			+ "\"" + "KingdomCn" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(7)) + "\","
                                			+ "\"" + "Kingdom" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(8)) + "\","
                                			+ "\"" + "PhylumCn" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(9)) + "\","
		                            		+ "\"" + "Phylum" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(10)) + "\","
		                            		+ "\"" + "ClassCn" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(11)) + "\","
		                            		+ "\"" + "Class" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(12)) + "\","
		                            		+ "\"" + "OrderCn" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(13)) + "\","
		                            		+ "\"" + "Order" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(14)) + "\","
		                            		+ "\"" + "FamilyCn" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(15)) + "\","
		                            		+ "\"" + "Family" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(16)) + "\","
		                            		+ "\"" + "GenusCn" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(17)) + "\","
		                            		+ "\"" + "Genus" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(18)) + "\","
		                            		+ "\"" + "rightsholder" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(22)) + "\","
		                            		+ "\"" + "copyright" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(23)) + "\","
		                            		+ "\"" + "license" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(24)) + "\","
		                            		+ "\"" + "scientificNameStatus" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(4)) + "\"" 
		                            		+ "}";
                                	thisTaxon = new Taxon(
                                			excelService.getStringValueFromCell(row.getCell(0)),
                                			excelService.getStringValueFromCell(row.getCell(1)),
                                			excelService.getStringValueFromCell(row.getCell(2)),
                                			excelService.getStringValueFromCell(row.getCell(3)),
                                			excelService.getStringValueFromCell(row.getCell(5)),
                                			excelService.getStringValueFromCell(row.getCell(6)),
                                			excelService.getStringValueFromCell(row.getCell(19)),
                                			excelService.getStringValueFromCell(row.getCell(20)),
                                			excelService.getStringValueFromCell(row.getCell(21)),
                                			remark,
                                			excelService.getStringValueFromCell(row.getCell(25)));
									
                                	taxonList.add(thisTaxon);
								}	
                            }
                            long start = System.currentTimeMillis();
                            JSONArray verifyListEntity = verifyListEntity(taxonList, taxasetId, thisUser, refFileMark,dsFileMark,expFileMark);
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
	private JSONArray verifyListEntity(List<Taxon> list, String taxasetId, User thisUser, String refFileMark, String dsFileMark, String expFileMark) throws Exception {
		JSONArray taxonArr = new JSONArray();
		List<Taxon> failList = new ArrayList<>();
        boolean flag = true;
        /*校验必填字段*/
        for (int i = 0; i < list.size(); i++) {
        	boolean mark = true;
        	Taxon thisTaxon = list.get(i);
        	if (StringUtils.isBlank(thisTaxon.getScientificname())) {
        		thisTaxon.setScientificname("<span style='color:red'>学名不能为空</span> ");
                mark = false;
            };
            if (StringUtils.isBlank(thisTaxon.getChname())) {
            	thisTaxon.setChname("<span style='color:red'>中文名不能为空</span>");
            	mark = false;
            };
            if (StringUtils.isBlank(thisTaxon.getAuthorstr())) {
            	thisTaxon.setAuthorstr("<span style='color:red'>命名信息不能为空</span>");
            	mark = false;
            };
            if (StringUtils.isBlank(thisTaxon.getNomencode())) {
            	thisTaxon.setNomencode("<span style='color:red'>命名法规不能为空</span>");
            	mark = false;
            };
            if (StringUtils.isBlank(thisTaxon.getRankid())) {
            	thisTaxon.setRankid("<span style='color:red'>分类等级不能为空</span>");
            	mark = false;
            }else{
            	Rank rank = this.rankRepository.findOneByChnameOrEnname(thisTaxon.getRankid());
            	if (null == rank) {
            		thisTaxon.setRankid("<span style='color:red'>系统数据缺失，未找到对应分类等级，请联系管理员！</span>");
            		mark = false;
				}else {
					thisTaxon.setRank(rank);
					thisTaxon.setRankid(rank.getId());
				}
			};
            if (StringUtils.isBlank(thisTaxon.getRefClassSys())) {
            	thisTaxon.setRefClassSys("<span style='color:red'>参考的分类体系不能为空</span>");
            	mark = false;
            };
            /*数据源*/
            if (StringUtils.isBlank(thisTaxon.getSourcesid())) {
            	thisTaxon.setSourcesid("<span style='color:red'>数据源不能为空</span>");
            	mark = false;
            }else  {
            	String source = this.baseinfotmpService.findDsIdByFilemarkAndSerialNumAndFileType(dsFileMark, thisTaxon.getSourcesid(), 1);
            	if (StringUtils.isNotBlank(source)) {
					thisTaxon.setSourcesid(source);
				}else {
					thisTaxon.setSourcesid("<span style='color:red'>未找到引用数据源，请检查引用数据存在或是否引用错误！</span>");
					mark = false;
				}
			};
			/*审核专家*/
			if (StringUtils.isBlank(thisTaxon.getExpert())) {
				thisTaxon.setExpert("<span style='color:red'>专家信息不能为空</span>");
				mark = false;
            }else{
            	boolean tmp = thisTaxon.getExpert().matches("[0-9]*");
            	String expid = null;
            	if (tmp) {
            		expid = this.baseinfotmpService.findExpIdByFilemarkAndSerialNumAndFileType(expFileMark, 2, thisTaxon.getExpert());
				}else {
					String expertId = this.expertRepository.findIdByCnName(thisTaxon.getExpert());
					if (StringUtils.isNotBlank(expertId)) {
						expid = expertId;
					}else {
						thisTaxon.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
					}
				}
            	
            	if (StringUtils.isNotBlank(expid)) {
            		thisTaxon.setExpert(expid);
				}else{
					thisTaxon.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
					mark = false;
				}
            };
            
            /*比对参考文献*/
        	String refjson = thisTaxon.getRefjson();
        	List<String> refIds = new ArrayList<>();
        	if (StringUtils.isNotBlank(refjson)) {
        		String[] refStr = refjson.replace("，", ",").split(",");
        		refIds = this.baseinfotmpService.findRefIdByFilemarkAndSerialNumAndFileType(refFileMark, 0, refStr);
        		if (!refIds.isEmpty()) {
        			JSONArray refjsonArr = this.refService.getRefJSONArrayByIds(refIds);
        			if (!refjsonArr.isEmpty()) {
        				thisTaxon.setRefjson(refjsonArr.toString());
        			}else {
        				thisTaxon.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
						mark = false;
					}
        		}else {
        			thisTaxon.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
					mark = false;
        		}
        	}
            
        	if (!mark) {
				thisTaxon.setSynchstatus(i + 1);
				failList.add(thisTaxon);
				flag = mark;
			}
        }
        /* 构建的集合若通过必填字段的校验，去重，比对数据源、参考文献，补充属性值*/
        if (flag) {
        	List<Taxon> taxonList = new ArrayList<>();
        	Taxaset thisTaxaset = this.taxasetRepository.findOneById(taxasetId);
        	int maxOrderNum = this.taxonRepository.findMaxOrderNumByTaxasetId(taxasetId);	// 当前taxaset下的最大OrderNum
            for (int i = 0; i < list.size(); i++) {
            	maxOrderNum = maxOrderNum + 1;
            	/* 根据学名去重并补充属性值*/
            	List<Taxon> taxons = this.taxonRepository.findByScientificnameAndTaxasetId(list.get(i).getScientificname(), taxasetId);
				if(!taxons.isEmpty()){
					for (Taxon tax : taxons) {
						list.get(i).setId(tax.getId());
						this.taxonRepository.updateTaxonById(list.get(i).getSourcesid(), list.get(i).getRefjson(), list.get(i).getExpert(), new Timestamp(System.currentTimeMillis()), tax.getId());
					}
					/*this.batchInsertService.updateTaxon(taxon);*/
				}else {
					list.get(i).setTaxaset(thisTaxaset);
					list.get(i).setId(UUIDUtils.getUUID32());
					list.get(i).setInputer(thisUser.getId());
					list.get(i).setInputtime(new Timestamp(System.currentTimeMillis()));
					list.get(i).setSynchdate(new Timestamp(System.currentTimeMillis()));
					list.get(i).setStatus(1);
					list.get(i).setTaxonCondition(2);//审核通过
					list.get(i).setOrderNum(maxOrderNum);
					taxonList.add(list.get(i));
				}
            }
            long start = System.currentTimeMillis();
            this.batchInsertService.batchInsertTaxon(taxonList);
            long end = System.currentTimeMillis();
            System.out.println("Taxon批量存储完成：" + (end - start));
        }else {
			for (int i = 0; i < failList.size(); i++) {
				JSONObject json = new JSONObject();
				json.put("num", failList.get(i).getSynchstatus());
				json.put("scientificname", failList.get(i).getScientificname());
				json.put("chname", failList.get(i).getChname());
				json.put("authorstr", failList.get(i).getAuthorstr());
				json.put("nomencode", failList.get(i).getNomencode());
				json.put("rank", failList.get(i).getRankid());
				json.put("refClassSys", failList.get(i).getRefClassSys());
				json.put("sourcesid", failList.get(i).getSourcesid());
				json.put("expert", failList.get(i).getExpert());
				taxonArr.add(i, json);
			}
		}
        return taxonArr;
    }

	@Override
	public JSON findUploadedTaxonList(String taxasetId, Timestamp timestamp, HttpServletRequest request) {
		String searchText = request.getParameter("search");
		if (searchText == null || searchText.length() <= 0) {
			searchText = "";
		}
		int limit_serch = Integer.parseInt(request.getParameter("limit"));
		int offset_serch = Integer.parseInt(request.getParameter("offset"));
		String sort=request.getParameter("sort");
		String order=request.getParameter("order");
		if(StringUtils.isBlank(sort)){
			sort = "synchdate";
			order = "desc";
		}
	
		JSONObject thisTable = new JSONObject();
		JSONArray rows = new JSONArray();
		List<Taxon> thisList = new ArrayList<>();
		Page<Taxon> thisPage = this.taxonRepository.searchInfo(searchText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), taxasetId, timestamp);
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			row.put("scientificname", "<a href=\"console/taxon/show/" + thisList.get(i).getId() + "\" target=\"_blank\"><em>" + thisList.get(i).getScientificname() + "</em></a>");
			row.put("chname", thisList.get(i).getChname());
			row.put("authorstr", thisList.get(i).getAuthorstr());
			switch (thisList.get(i).getTaxonCondition()) {
				case 0:
					row.put("taxonCondition", "<span class=\"label label-default\">未提交审核</span>");
					break;
				case 1:
					row.put("taxonCondition", "<span class=\"label label-info\">已提交审核</span>");
					break;
				case -1:
					row.put("taxonCondition", "<span class=\"label label-danger\">审核不通过</span>");
					break;
				case 2:
					row.put("taxonCondition", "<span class=\"label label-success\">审核通过</span>");
					break;
				default:
					break;
			}
			if(StringUtils.isNotBlank(thisList.get(i).getExpert())){
				User thisUser = this.userService.findbyID(thisList.get(i).getExpert());
				Expert thisExpert = this.expertService.findOneById(thisList.get(i).getExpert());
				try {
					if(null != thisExpert){	//先从Expert取
						row.put("taxonExamine", thisExpert.getCnName() + " (" + thisExpert.getExpEmail() + ") ");
					}else{					//再从User取审核人
						row.put("taxonExamine", thisUser.getNickname() + " (" + thisUser.getEmail() + ") ");
					}
				} catch (Exception e) {
					row.put("taxonExamine", thisExpert.getCnName() + " (" + thisExpert.getExpEmail() + ") ");
					e.printStackTrace();
				}
			}else {
				row.put("taxonExamine", "无审核人");
			}
			row.put("epithet", thisList.get(i).getEpithet());
			row.put("orderNum", thisList.get(i).getOrderNum());
			row.put("inputer", this.userService.findbyID(thisList.get(i).getInputer()).getNickname());
			String rankId = thisList.get(i).getRank().getId();
			Rank thisRank = this.rankRepository.findOneById(rankId);
			row.put("rankid", thisRank.getEnname() + "<" + thisRank.getChname() + ">");
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
	public void matchSynonymRef(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Citation> citationList = this.citationRepository.findByNametypeAndInputer(5, thisUser.getId());
		Map<String, Citation> map = new HashMap<>();
		for (Citation citation : citationList) {
			map.put(citation.getSciname().trim(), citation);
		}
		
		List<Citation> list = new ArrayList<>();
		InputStream is = new FileInputStream(path);
		//创建XSSFWorkbook对象
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		//获取sheet工作表
		XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
		int rowNums = sheet.getLastRowNum();
		for(int rowNum = 1; rowNum <= rowNums; rowNum++){
			XSSFRow row = sheet.getRow(rowNum);
			if(null == row){
				System.out.println("第"+ (rowNum + 1) +"行有空行");
			}else {
				String sciname = excelService.getStringValueFromCell(row.getCell(0));	// 异名
				String refType = excelService.getStringValueFromCell(row.getCell(1));	// 原始文献 0
				if (StringUtils.isNotBlank(sciname)) {
					Citation citation = map.get(sciname.trim());
					if (null != citation) {
						JSONArray handleReferenceToJson = (JSONArray) handleOriginalRefjson(row, refType, citation);
						if (handleReferenceToJson.size() > 0) {
							citation.setRefjson(handleReferenceToJson.toJSONString());
							System.out.println(citation.getSciname() + "\t" + citation.getRefjson());
							list.add(citation);
							this.citationRepository.save(citation);
						}
					}else {
						/*System.out.println("Citation is null");*/
					}
				}else {
					/*System.out.println("sciname is null");*/
				}
			}
		}
		System.out.println("list's size：" + list.size());
		/*this.batchInsertService.batchUpdateCitation(list);*/
	}

	@Override
	public void matchAcceptRef(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Citation> citationList = this.citationRepository.findByNametypeAndInputer(1, thisUser.getId());
		List<Taxon> taxonList = this.taxonRepository.findByInputer(thisUser.getId());
		Map<String, Citation> citationMap = new HashMap<>();
		Map<String, Taxon> taxonMap = new HashMap<>();
		for (Citation citation : citationList) {
			citationMap.put(citation.getSciname().trim(), citation);
		}
		for (Taxon taxon : taxonList) {
			taxonMap.put(taxon.getScientificname().trim(), taxon);
		}
		
		List<Citation> cList = new ArrayList<>();
		List<Taxon> tList = new ArrayList<>();
		InputStream is = new FileInputStream(path);
		//创建XSSFWorkbook对象
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		//获取sheet工作表
		XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
		int rowNums = sheet.getLastRowNum();
		for(int rowNum = 1; rowNum <= rowNums; rowNum++){
			XSSFRow row = sheet.getRow(rowNum);
			if(null == row){
				System.out.println("第"+ (rowNum + 1) +"行有空行");
			}else {
				String sciname = excelService.getStringValueFromCell(row.getCell(0));	// 接受名
				String refType = excelService.getStringValueFromCell(row.getCell(1));	// 原始文献 0
				if (refType.equals("0")) {	// 非原始文献 - Taxon
					Taxon taxon = matchRefToTaxon(sciname, taxonMap, row);
					if (null != taxon) {
						tList.add(taxon);
						this.taxonRepository.save(taxon);
					}
				}else {						// 原始文献 - Citation
					Citation citation = matchRefToCitation(sciname, refType, citationMap, row);
					if (null != citation) {
						cList.add(citation);
						this.citationRepository.save(citation);
					}
				}
			}
		}
		System.out.println("tList's size：" + tList.size());
		System.out.println("cList's size：" + cList.size());
		/*this.batchInsertService.batchUpdateTaxon(tList);
		this.batchInsertService.batchUpdateCitation(cList);*/
	}

	/**
	 * @Description 为Taxon匹配参考文献
	 * @param sciname
	 * @param taxonMap
	 * @param row
	 * @return
	 */
	private Taxon matchRefToTaxon(String sciname, Map<String, Taxon> taxonMap, XSSFRow row) {
		if (StringUtils.isNotBlank(sciname)) {
			Taxon taxon = taxonMap.get(sciname.trim());
			if (null != taxon) {
				JSONArray handleReferenceToJson = (JSONArray) handleRefjson(row, taxon);
				if (handleReferenceToJson.size() > 0) {
					taxon.setRefjson(handleReferenceToJson.toJSONString());
					/*System.out.println("Taxon：" + taxon.getScientificname() + "\t" + handleReferenceToJson.toJSONString());*/
					return taxon;
				}
			}else {
				/*System.out.println("Taxon is null");*/
			}
		}else {
			/*System.out.println("TaxonSciname is null");*/
		}
		return null;
	}

	/**
	 * @Description 为Citation匹配原始参考文献
	 * @param sciname
	 * @param refType
	 * @param citationMap
	 * @param row
	 * @return
	 */
	private Citation matchRefToCitation(String sciname, String refType, Map<String, Citation> citationMap, XSSFRow row) {
		if (StringUtils.isNotBlank(sciname)) {
			Citation citation = citationMap.get(sciname.trim());
			if (null != citation) {
				JSONArray handleReferenceToJson = (JSONArray) handleOriginalRefjson(row, refType, citation);
				if (handleReferenceToJson.size() > 0) {
					citation.setRefjson(handleReferenceToJson.toJSONString());
					/*System.out.println("Citation：" + citation.getSciname() + "\t" + handleReferenceToJson.toJSONString());*/
					return citation;
				}
			}else {
				/*System.out.println("Citation is null");*/
			}
		}else {
			/*System.out.println("CitationSciname is null");*/
		}
		return null;
	}
	/**
	 * @Description 分类单元匹配参考文献
	 * @param row
	 * @return
	 */
	private JSONArray handleRefjson(XSSFRow row, Taxon taxon) {
		JSONArray jsonArray = new JSONArray();
		String PP = excelService.getStringValueFromCell(row.getCell(2));		// 页码
		String paperID = excelService.getStringValueFromCell(row.getCell(3));	// 参考文献ID
		String[] refIdArr = paperID.split("-");
		StringBuilder refId = new StringBuilder();
		// 处理参考文献页码
		if (StringUtils.isBlank(PP)) {
			PP = "unknown";
		}
		// 处理参考文献ID
		for (int index = 0; index < refIdArr.length; index++) {
			refId.append(refIdArr[index]);
		}
		Ref thisRef = this.refService.findOneById(refId.toString());
		// 拼装JSON格式数据
		String jsonStr = "{" 
				+ "\"refId\"" + ":\"" + thisRef.getId() + "\","
				+ "\"refS\"" + ":\"" + PP + "\"," 
				+ "\"refE\"" + ":\"" + PP + "\""
				+ "}";
		JSONObject jsonText = JSON.parseObject(jsonStr);
		String refjson = taxon.getRefjson();
		if (StringUtils.isNotBlank(refjson)) {
			jsonArray = JSONArray.parseArray(refjson);
			jsonArray.fluentAdd(jsonText);
			/*jsonArray.add(jsonText);*/
		}else {
			jsonArray.add(jsonText);
		}
		return jsonArray;
	}

	/**
	 * @Description 引证匹配参考文献
	 * @param row
	 * @return
	 */
	private JSONArray handleOriginalRefjson(XSSFRow row, String refType, Citation citation) {
		JSONArray jsonArray = new JSONArray();
		String PP = excelService.getStringValueFromCell(row.getCell(2));		// 页码
		String paperID = excelService.getStringValueFromCell(row.getCell(3));	// 参考文献ID
		String[] refIdArr = paperID.split("-");
		StringBuilder refId = new StringBuilder();
		// 处理参考文献类型
		if (refType.equals("0")) {
			refType = "1";	// 其他文献
		}else {
			refType = "0";	// 原始文献
		}
		// 处理参考文献页码
		if (StringUtils.isBlank(PP)) {
			PP = "unknown";
		}
		// 处理参考文献ID
		for (int index = 0; index < refIdArr.length; index++) {
			refId.append(refIdArr[index]);
		}
		Ref thisRef = this.refService.findOneById(refId.toString());
		// 拼装JSON格式数据
		String jsonStr = "{" 
				+ "\"refId\"" + ":\"" + thisRef.getId() + "\","
				+ "\"refS\"" + ":\"" + PP + "\"," 
				+ "\"refE\"" + ":\"" + PP + "\","
				+ "\"refType\"" + ":\"" + refType + "\""
				+ "}";
		JSONObject jsonText = JSON.parseObject(jsonStr);
		String refjson = citation.getRefjson();
		if (StringUtils.isNotBlank(refjson)) {
			jsonArray = JSONArray.parseArray(refjson);
			jsonArray.fluentAdd(jsonText);
		}else {
			jsonArray.add(jsonText);
		}
		return jsonArray;
	}

	@Override
	public void handleFishTaxon() {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Taxon> list = this.taxonRepository.handleFishTaxon(42,thisUser.getId());
		for (Taxon taxon : list) {
			StringBuffer buff = new StringBuffer();
			String[] scinameArr = taxon.getScientificname().trim().split(" ");
			if (scinameArr.length == 3) {
				String speciesName = scinameArr[0] + " " + scinameArr[1];
				String speciesEpithet = scinameArr[1].trim();
				String subspeciesEpithet = scinameArr[1].trim() + " " + scinameArr[2].trim();
				taxon.setEpithet(subspeciesEpithet);
				
				Taxon thisTaxon = new Taxon();
				thisTaxon.setId(UUIDUtils.getUUID32());
				thisTaxon.setScientificname(speciesName);
				thisTaxon.setAuthorstr(taxon.getAuthorstr());
				thisTaxon.setEpithet(speciesEpithet);
				thisTaxon.setNomencode("ICZN");
				Rank thisRank = this.rankRepository.findOneById("7");
				thisTaxon.setRank(thisRank);
				thisTaxon.setRankid(thisRank.getId());
				thisTaxon.setSourcesid(taxon.getSourcesid());
				thisTaxon.setTaxaset(taxon.getTaxaset());
				thisTaxon.setInputer(taxon.getInputer());
				thisTaxon.setInputtime(new Timestamp(System.currentTimeMillis()));
				thisTaxon.setSynchdate(new Timestamp(System.currentTimeMillis()));
				thisTaxon.setSynchstatus(0);
				thisTaxon.setStatus(1);
				
				this.taxonRepository.save(taxon);		// 完善亚种种加词/亚种加词
				this.taxonRepository.save(thisTaxon);	// 补充亚种加词的种
			}
		}
	}

	@Override
	public void handleDistributiondata() {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Distributiondata> list = this.distributiondataRepository.findByInputer(thisUser.getId());
		for (int i = 0; i < list.size(); i++) {
			String discontent = list.get(i).getDiscontent();
			if (discontent.contains("分布(distribution)：")) {
				int start1 = discontent.indexOf("：");
				discontent = discontent.substring(start1 + 1);
			}else if (discontent.contains("分布（distribution）：")) {
				int start2 = discontent.indexOf("：");
				discontent = discontent.substring(start2 + 1);
			}
			int end = discontent.lastIndexOf("。");
			if (end != -1) {
				list.get(i).setDiscontent(discontent.substring(0, end));
				System.out.println(discontent.substring(0, end));
			}else {
				list.get(i).setDiscontent(discontent.substring(0));
				System.out.println(discontent.substring(0));
			}
			this.distributiondataRepository.save(list.get(i));
		}
	}

	@Override
	public void handleTaxonSciname() {
		List<Taxon> list = this.taxonRepository.findByInputer("3a29945066666ef8a134f0f017d316f0");
		for (Taxon taxon : list) {
			String sciname = taxon.getScientificname().trim();
			String[] scinameArr = sciname.split(" ");
			if (scinameArr.length == 2) {		// 种
				taxon.setEpithet(scinameArr[1].trim());
				this.taxonRepository.save(taxon);
			}else if (scinameArr.length == 3) { // 亚种
				taxon.setEpithet(scinameArr[1].trim() + " " + scinameArr[2].trim());
				this.taxonRepository.save(taxon);
			}else if (scinameArr.length > 3) {
				System.out.println("学名分组大于3：" + sciname);
			}
		}
	}

	@Override
	public void handleCitationSciname() {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Citation> list = this.citationRepository.findByInputer(thisUser.getId());
		for (Citation citation : list) {
			String sciname = citation.getSciname();
			String authorship = citation.getAuthorship();	// 作者
			String[] split0 = sciname.trim().split(" ");
			if (StringUtils.isNotBlank(authorship)) {
				String trim = authorship.trim();
				boolean lowerCase = Character.isLowerCase(trim.charAt(0));
				String[] split = authorship.split(" ");
				if (lowerCase && !split[0].equals("des") && !split[0].equals("ab.") && !split[0].equals("of") && !split[0].equals("v.d.Wulp") && !split[0].equals("v.d.") && !split[0].equals("misidentification") && !split[0].equals("de") && !split[0].equals("von") && !split[0].equals("van") && !split[0].equals("v.") && !split[0].equals("f.")) {
					if (split[0].equals("et")) {
						citation.setSciname(sciname.replace(split0[(split0.length - 1)], ""));
						citation.setAuthorship(split0[(split0.length - 1)] + " " + authorship);
					}else if (split[0].equals("var") || split[0].equals("ssp.")) {
						citation.setSciname(sciname + " " + split[0] + " " + split[1]);
						citation.setAuthorship(authorship.replace(split[0] + " " + split[1], ""));
					}else {
						citation.setSciname(sciname + " " + split[0]);
						citation.setAuthorship(authorship.replace(split[0], ""));
					}
					/*this.citationRepository.save(citation);*/
					System.out.println("数据1：" + split[0] + "\t" + trim + "\t" + sciname + "\t" + citation.getCitationstr());
				}else {
					System.out.println("数据2：" + split[0] + "\t" + trim + "\t" + sciname + "\t" + citation.getCitationstr());
				}
			}
		}
	}

	@Override
	public List<Family> getFamilyDataByTaxaset(String taxasetId, String taxtreeId) {
		// 1、查询分类单元集下rank = 科 的taxon
		List<PartTaxonVO> familylist = findFamilyByTaxaset(taxasetId);
		// 2、查询分类单元集下rank高于科（不包含科）的所有taxon
		Map<String, PartTaxonVO> higherThanfamilyMap = findHigherThanFamilyByTaxaset(taxasetId);
		// 3、根据分类树查询对应关系
		List<TaxonHasTaxtree> relationlist = taxonHasTaxtreeRepository.findByTaxtreeId(taxtreeId);
		Map<String, String> relationMap = convertToMap(relationlist);
		// 4、重建结构
		if (null == familylist) {
			return null;
		}else {
			List<Family> list = new ArrayList<>(familylist.size() + 5);
			for (PartTaxonVO part : familylist) {
				Family entity = new Family();
				String id = part.getId();
				entity.setRecordId(id);
				entity.setFamily(part.getScientificname().trim());
				entity.setFamilyC(part.getChname());
				rebuild(id, relationMap, higherThanfamilyMap, entity, taxtreeId);
				list.add(entity);
			}
			return list;
		}
	}
	
	/**
	 * 查询分类单元集下rank = 科 的taxon
	 * @param taxasetId
	 * @return
	 */
	private List<PartTaxonVO> findFamilyByTaxaset(String taxasetId) {
		List<Object[]> familylist = taxonRepository.findByTaxasetAndRank(taxasetId, "Family");
		return turnObject2TaxonToFamiles(familylist);
	}

	/**
	 * 将分类单元集下Rank等于科的taxon封装成Family实体
	 * @param objs
	 * @return
	 */
	private List<PartTaxonVO> turnObject2TaxonToFamiles(List<Object[]> objs) {
		List<PartTaxonVO> list = null;
		if (objs != null && objs.size() > 0) {
			list = new ArrayList<>();
			for (Object[] obj : objs) {
				if (null != obj) {
					list.add(turnPartTaxonVO(obj));
				}
			}
		}
		return list;
	}
	/**
	 * @Description 封装PartTaxonVO实体
	 * @param obj
	 * @return
	 */
	private PartTaxonVO turnPartTaxonVO(Object[] obj) {
		return new PartTaxonVO(
				String.valueOf(obj[0]), 
				String.valueOf(obj[1]), 
				String.valueOf(obj[2]),
				Integer.parseInt(obj[3].toString()), 
				String.valueOf(obj[4]), 
				String.valueOf(obj[5]),
				String.valueOf(obj[6])
				);
	}

	/**
	 * 2、查询分类单元集下rank高于科（不包含科）的所有taxon
	 * @param taxasetId
	 * @return
	 */
	private Map<String, PartTaxonVO> findHigherThanFamilyByTaxaset(String taxasetId) {
		List<Object[]> higherThanfamilylist = taxonRepository.findByTaxasetAndRankIn(taxasetId,
				RankRange.higherThanfamilyRankNames());
		return turnToPartTaxonVOMap(higherThanfamilylist);
	}

	public Map<String, PartTaxonVO> turnToPartTaxonVOMap(List<Object[]> lowerThanfamilylist) {
		Map<String, PartTaxonVO> map = new HashMap<>();
		for (Object[] obj : lowerThanfamilylist) {
			if (null != obj) {
				PartTaxonVO entity = turnPartTaxonVO(obj);
				map.put(entity.getId(), entity);
			}
		}
		return map;
	}
	
	private Map<String, String> convertToMap(List<TaxonHasTaxtree> relationlist) {
		Map<String, String> map = new HashMap<>();
		for (TaxonHasTaxtree taxonHasTaxtree : relationlist) {
			String taxonId = taxonHasTaxtree.getTaxonId();
			String pid = taxonHasTaxtree.getPid();
			map.put(taxonId, pid);
		}
		return map;
	}

	private void rebuild(String id, Map<String, String> relationMap, Map<String, PartTaxonVO> higherThanfamilyMap,
			Family entity, String taxtreeId) {
		String pid = relationMap.get(id);
		// 判断是否已经查询到根节点
		if (taxtreeId.equals(pid) || StringUtils.isEmpty(pid)) {
			return;
		}
		PartTaxonVO parentTaxon = higherThanfamilyMap.get(pid);
		if (parentTaxon == null) {
			return;
		}
		int rankId = parentTaxon.getRankId();
		String scientificname = parentTaxon.getScientificname().trim();
		String chname = parentTaxon.getChname();
		switch (rankId) {
		case 1:// 界
			entity.setKingdom(scientificname);
			entity.setKingdomC(chname);
			break;
		case 2:// 门
			entity.setPhylum(scientificname);
			entity.setPhylumC(chname);
			break;
		case 3:// 纲
			entity.setClass_(scientificname);
			entity.setClassC(chname);
			break;
		case 4:// 目
			entity.setOrder(scientificname);
			entity.setOrderC(chname);
			break;
		case 40:
			entity.setSuperfamily(scientificname);
			entity.setSuperfamilyC(chname);
			break;
		default:
			throw new ValidationException("未定义的rank");
		}
		rebuild(parentTaxon.getId(), relationMap, higherThanfamilyMap, entity, taxtreeId);
	}

	@Override
	public List<ScientificName> getScientificNamesByTaxaset(String taxasetId, String taxtreeId) {
		// 某分类单元集下rank<=family的所有taxon
		Map<String, PartTaxonVO> lowerThanfamilyMap = turnToPartTaxonVOMap(
				taxonRepository.findByTaxasetAndRankIn(taxasetId, RankRange.lowerThanfamilyInculdeRankNames()));
		// 根据分类树查询出的上下级关系
		Map<String, String> relationMap = convertToMap(taxonHasTaxtreeRepository.findByTaxtreeId(taxtreeId));
		// insert的数据
		// 1、分类单元集下的所有species
		List<PartTaxonVO> specieslist = turnObject2TaxonToFamiles(
				taxonRepository.findByTaxasetAndRank(taxasetId, "Species"));
		List<ScientificName> scientificNamelist = new ArrayList<>();
		for (PartTaxonVO speciesTaxon : specieslist) {
			ScientificName scientificName = new ScientificName();
			String id = speciesTaxon.getId();
			scientificName.setNameCode(id);
			getHigherUntilGenusInfo(scientificName, id, lowerThanfamilyMap, relationMap, taxtreeId);
			scientificName.setSpecies(speciesTaxon.getEpithet().trim());
			scientificName.setSpeciesC(speciesTaxon.getChname());
			scientificName.setAcceptedNameCode(id);
			scientificName.setScrutinyDate(CommUtils.getCurrentDate("yyyy-MM-dd"));
			scientificName.setSp2000StatusId(1);
			scientificName.setAuthor(speciesTaxon.getAuthorstr());
			scientificName.setFamilyId(getPointRankId(id, relationMap, lowerThanfamilyMap, RankEnum.family.getIndex()));
			scientificName.setIsAcceptedName(1);
			scientificName.setCanonicalName(scientificName.getGenus() + " " + scientificName.getSpecies());// 种阶元是属+空格+种加词拉丁名
			String comments = speciesTaxon.getScientificname().trim() + " " + speciesTaxon.getAuthorstr();
			scientificName.setComments(comments.trim());// 一般是名称全称，即canonical_name+作者信息
			scientificNamelist.add(scientificName);
		}
		// 2、某分类单元集下的所有subspecies
		List<PartTaxonVO> subspecieslist = turnObject2TaxonToFamiles(
				taxonRepository.findByTaxasetAndRank(taxasetId, "subspecies"));
		for (PartTaxonVO subspeciesTaxon : subspecieslist) {
			ScientificName scientificName = new ScientificName();
			String id = subspeciesTaxon.getId();
			scientificName.setNameCode(id);
			getHigherUntilGenusInfo(scientificName, id, lowerThanfamilyMap, relationMap, taxtreeId);
			String[] split = subspeciesTaxon.getEpithet().trim().split(" ");
			if (split.length == 2) {
				scientificName.setInfraspecies(split[1]);
			} else {
				scientificName.setInfraspecies(split[0]);
			}
			scientificName.setInfraspeciesC(subspeciesTaxon.getChname());
			/*
			 * scientificName.setAcceptedNameCode( getPointRankId(id, relationMap, lowerThanfamilyMap, RankEnum.species.getIndex()));
			 */
			scientificName.setAcceptedNameCode(id);
			scientificName.setScrutinyDate(CommUtils.getCurrentDate("yyyy-MM-dd"));
			scientificName.setSp2000StatusId(1);
			scientificName.setAuthor(subspeciesTaxon.getAuthorstr());
			scientificName.setFamilyId(getPointRankId(id, relationMap, lowerThanfamilyMap, RankEnum.family.getIndex()));
			scientificName.setIsAcceptedName(1);// 如果是接受名，则为1，否则为0
			if (StringUtils.isNotEmpty(scientificName.getSpecies()) && !subspeciesTaxon.getScientificname().contains(scientificName.getSpecies())) {
				/*
				 * throw new ValidationException("亚种和种的父级关系不对：亚种id=" + subspeciesTaxon.getId() + "，亚种中文名=" + subspeciesTaxon.getChname() + ",亚种学名=" + subspeciesTaxon.getScientificname());
				 */
				System.out.println("亚种和种的父级关系不对：亚种id=" + subspeciesTaxon.getId() + "，亚种中文名=" + subspeciesTaxon.getChname() + ",亚种学名=" + subspeciesTaxon.getScientificname());
			}

			// 亚种设置标记，并拼接CanonicalName
			scientificName.setInfraspeciesMarker("subsp.");
			scientificName.setCanonicalName(scientificName.getGenus() + " " + scientificName.getSpecies() + " "
					+ "subsp." + " " + scientificName.getInfraspecies());// genus+空格+species+空格+infraspecies_mark+空格+infraspecies

			String comments = subspeciesTaxon.getScientificname().trim() + " " + subspeciesTaxon.getAuthorstr();
			scientificName.setComments(comments.trim());// 一般是名称全称，即canonical_name+作者信息
			scientificNamelist.add(scientificName);
		}
		// 3、species和subspecies的所有异名
		List<String> rankNameIn = new ArrayList<>();
		rankNameIn.add(String.valueOf(RankEnum.species.getIndex()));
		rankNameIn.add(String.valueOf(RankEnum.subsp.getIndex()));
		List<Citation> ctationlist = convertToCitation(citationRepository.findByNametypeAndTaxaSetAndRankIn(taxasetId,
				NametypeEnum.acceptedName.getIndex(), rankNameIn));
		for (Citation citation : ctationlist) {
			ScientificName scientificName = new ScientificName();
			scientificName.setNameCode(citation.getId());// 主键
			String sciname = citation.getSciname().trim();
			if (sciname.contains("(")) {// 去掉亚属
				sciname = CommUtils.cutByStrBefore(sciname, "(").trim() + " " + CommUtils.cutByStrAfter(sciname, ")").trim();
			}
			getHigherUntilGenusInfo(scientificName, citation.getTaxon().getId(), lowerThanfamilyMap, relationMap,
					taxtreeId);
			int rankid = Integer.parseInt(citation.getTaxon().getRankid());
			switch (rankid) {
			case 7:// species的异名
				String[] spli = sciname.split(" ");
				if (spli.length >= 2) {
					scientificName.setSpecies(spli[1].trim());
				}
				if (spli.length >= 3) {
					scientificName.setInfraspecies(spli[spli.length - 1].trim());
				}
				break;
			case 42:// subspecies的异名
				String[] splits = sciname.split(" ");
				if (splits.length >= 2) {
					scientificName.setSpecies(splits[1].trim());

				}
				if (splits.length >= 3) {
					scientificName.setInfraspecies(splits[splits.length - 1].trim());
				}
				break;
			default:
				throw new ValidationException("CITATION 0000C 未定义的rank:" + rankid);
			}
			scientificName.setSpeciesC(null);
			scientificName.setAuthor(citation.getAuthorship());
			scientificName.setAcceptedNameCode(citation.getTaxon().getId());// 指向taxon
			scientificName.setScrutinyDate(CommUtils.getCurrentDate("yyyy-MM-dd"));
			scientificName.setSp2000StatusId(Integer.parseInt(citation.getNametype()));
			citation.getNametype();
			scientificName.setFamilyId(getPointRankId(citation.getTaxon().getId(), relationMap, lowerThanfamilyMap,
					RankEnum.family.getIndex()));
			scientificName.setIsAcceptedName(0);// 如果是接受名，则为1，否则为0
			if (citation.getSciname().contains(" var ") || citation.getSciname().contains(" var.")) {
				scientificName.setInfraspeciesMarker(" var. ");
			} else {
				scientificName.setInfraspeciesMarker("");
			}
			String[] split = sciname.trim().split(" ");
			scientificName.setGenus(split[0]);
			scientificName.setCanonicalName(sciname);
			scientificName.setComments(citation.getCitationstr());
			scientificNamelist.add(scientificName);
		}
		return scientificNamelist;
	}

	/**
	 * 
	 * @Description 填充学名和中文名，直到属（包含属信息）
	 * @param scientificName
	 * @param id                 taxonId,此taxon的学名和中文名不会放到scientificName中
	 * @param lowerThanfamilyMap
	 * @param relationMap
	 * @param taxtreeId
	 * @author ZXY
	 */
	private void getHigherUntilGenusInfo(ScientificName scientificName, String id,
			Map<String, PartTaxonVO> lowerThanfamilyMap, Map<String, String> relationMap, String taxtreeId) {
		String pid = relationMap.get(id);
		// 判断是否已经查询到根节点
		if (taxtreeId.equals(pid) || StringUtils.isEmpty(pid)) {
			return;
		}
		// 有可能是脏数据
		PartTaxonVO parentTaxon = lowerThanfamilyMap.get(pid);
		if (parentTaxon == null) {
			return;
		}
		int rankId = parentTaxon.getRankId();
		Rank rank = rankRepository.findOneById(String.valueOf(rankId));
		if (RankRange.higherThanGenusRankNames().contains(rank.getEnname())) {
			return;
		}
		String scientificname = parentTaxon.getScientificname().trim();
		String epither = parentTaxon.getEpithet();
		String chname = parentTaxon.getChname();
		switch (rankId) {
		case 6:// 属
			scientificName.setGenus(scientificname);
			scientificName.setGenusC(chname);
			break;
		case 7:// 种
			scientificName.setSpecies(epither);
			scientificName.setSpeciesC(chname);
			break;
		default:
			break;
		}
		getHigherUntilGenusInfo(scientificName, pid, lowerThanfamilyMap, relationMap, taxtreeId);
	}
	
	private String getPointRankId(String id, Map<String, String> relationMap,
			Map<String, PartTaxonVO> lowerThanfamilyMap, int rankId) {
		String resultTaxonId = "";
		String pid = "";
		try {
			pid = relationMap.get(id);
			PartTaxonVO parentTaxon = lowerThanfamilyMap.get(pid);
			if (parentTaxon.getRankId() == rankId) {
				resultTaxonId = parentTaxon.getId();
				return resultTaxonId;
			} else {
				resultTaxonId = getPointRankId(parentTaxon.getId(), relationMap, lowerThanfamilyMap, rankId);
			}
			return resultTaxonId;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationException("获取指定rank失败，rankId="+rankId+",id=" + id + ",pid=" + pid);
		}
	}
	
	private List<Citation> convertToCitation(List<Object[]> findByNametypeAndTaxaSet) {
		List<Citation> list = new ArrayList<>();
		for (Object[] obj : findByNametypeAndTaxaSet) {
			if (obj[2] == null) {
				continue;
			}
			Citation c = new Citation();
			c.setId(obj[0].toString());
			c.setTaxon(new Taxon(obj[1].toString(), obj[6].toString()));
			c.setSciname(obj[2].toString());
			c.setAuthorship((null != obj[3]) ? obj[3].toString() : "");
			c.setNametype(obj[4].toString());
			c.setCitationstr((null != obj[5]) ? obj[5].toString() : "");
			list.add(c);
		}
		return list;
	}
	
	@Override
	public void handleTaxon(String path) throws Exception {
		/*// 创建XSSFWorkbook对象
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
		int rowNums = sheet.getLastRowNum();
		try {
			for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
				XSSFRow row = sheet.getRow(rowNum);
				TaxonEntity taxon = new TaxonEntity();
				taxon.setId(UUIDUtils.getUUID32());
				
				taxon.setSciname(
						((StringUtils.isNotEmpty(excelService.getStringValueFromCell(row.getCell(0))))
								? excelService.getStringValueFromCell(row.getCell(0)) : ""));
				taxon.setChname(
						((StringUtils.isNotEmpty(excelService.getStringValueFromCell(row.getCell(1))))
								? excelService.getStringValueFromCell(row.getCell(1)) : ""));
				taxon.setLocation(
						((StringUtils.isNotEmpty(excelService.getStringValueFromCell(row.getCell(2))))
								? excelService.getStringValueFromCell(row.getCell(2)) : ""));
				System.out.println("Taxon：" + taxon);
				taxonEntityRepository.save(taxon);
			}
		} catch (Exception e) {
			System.out.println("空指针异常");
		}*/
	}

	@Override
	public void handleDistribution(String path) throws Exception {
		/*// 创建XSSFWorkbook对象
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet = xssfWorkbook.getSheetAt(1);
		int rowNums = sheet.getLastRowNum();
		try {
			for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
				XSSFRow row = sheet.getRow(rowNum);
				DistributionEntity distribution = new DistributionEntity();
				distribution.setId(UUIDUtils.getUUID32());
				distribution.setName(((StringUtils.isNotEmpty(excelService.getStringValueFromCell(row.getCell(0))))
						? excelService.getStringValueFromCell(row.getCell(0)) : ""));
				distribution.setProvence(((StringUtils.isNotEmpty(excelService.getStringValueFromCell(row.getCell(1))))
						? excelService.getStringValueFromCell(row.getCell(1)) : ""));
				distribution.setCity(((StringUtils.isNotEmpty(excelService.getStringValueFromCell(row.getCell(2))))
						? excelService.getStringValueFromCell(row.getCell(2)) : ""));
				distribution.setCounty(((StringUtils.isNotEmpty(excelService.getStringValueFromCell(row.getCell(3))))
						? excelService.getStringValueFromCell(row.getCell(3)) : ""));
				distributionEntityRepository.save(distribution);
				System.out.println("Distribution：" + distribution);
			}
		} catch (Exception e) {
			System.out.println("空指针异常");
		}*/
	}
	
	@Override
	public void handleGeoobjectChange(String path) throws Exception {
		/*// 创建XSSFWorkbook对象
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet = xssfWorkbook.getSheetAt(3);
		int rowNums = sheet.getLastRowNum();
		try {
			for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
				XSSFRow row = sheet.getRow(rowNum);
				GeoObjectChange geo = new GeoObjectChange();
				geo.setId(UUIDUtils.getUUID32());

				geo.setNewName(((StringUtils.isNotEmpty(excelService.getStringValueFromCell(row.getCell(0))))
						? excelService.getStringValueFromCell(row.getCell(0)) : ""));
				geo.setAdcode(((StringUtils.isNotEmpty(excelService.getStringValueFromCell(row.getCell(1))))
						? excelService.getStringValueFromCell(row.getCell(1)) : ""));
				geo.setOldName(((StringUtils.isNotEmpty(excelService.getStringValueFromCell(row.getCell(2))))
						? excelService.getStringValueFromCell(row.getCell(2)) : ""));
				geo.setOldShortName(((StringUtils.isNotEmpty(excelService.getStringValueFromCell(row.getCell(3))))
						? excelService.getStringValueFromCell(row.getCell(3)) : ""));
				System.out.println("Taxon：" + geo);
				geoObjectChangeRepository.save(geo);
			}
		} catch (Exception e) {
			System.out.println("空指针异常");
		}*/
	}

	@Override
	public void handleButterfly(String path) throws Exception {
		/*// 创建XSSFWorkbook对象
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet = xssfWorkbook.getSheetAt(1);
		int rowNums = sheet.getLastRowNum();
		try {
			for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
				XSSFRow row = sheet.getRow(rowNum);
				Butterfly butt = new Butterfly();
				butt.setId(UUIDUtils.getUUID32());

				butt.setLatin(((StringUtils.isNotEmpty(excelService.getStringValueFromCell(row.getCell(0))))
						? excelService.getStringValueFromCell(row.getCell(0)) : ""));
				butt.setChinese_name(((StringUtils.isNotEmpty(excelService.getStringValueFromCell(row.getCell(1))))
						? excelService.getStringValueFromCell(row.getCell(1)) : ""));
				butt.setRank_id(((StringUtils.isNotEmpty(excelService.getStringValueFromCell(row.getCell(2))))
						? excelService.getStringValueFromCell(row.getCell(2)) : ""));
				System.out.println("Butt" + butt);
				this.butterflyRepository.save(butt);
			}
		} catch (Exception e) {
			System.out.println("空指针异常");
		}*/
	}

	@Override
	public Taxon findOneByScientificnameAndTaxasetId(String scientificname, String taxasetId) {
		return this.taxonRepository.findOneByScientificnameAndTaxasetId(scientificname, taxasetId);
	}
	@Override
	public Taxon findOneByScientificnameAndInputer(String scientificname, String inputer) {
		return this.taxonRepository.findOneByScientificnameAndInputer(scientificname, inputer);
	}

	@Override
	public void getFishByTreeNodeId(String treeNodeId) {
		List<Taxon> classTaxons = this.taxonRepository.getTaxonListByTreeNodeId(treeNodeId); // 查分类树的根节点
		Set<Taxon> taxons = new HashSet<>(); // 批量存
		
		for (Taxon classTaxon : classTaxons) {
			String classPid = classTaxon.getId();
			String classRemark = classTaxon.getRemark();
			
			List<Taxon> orderTaxons = this.taxonRepository.getTaxonListByTreeNodeId(classPid); // 查纲的目
			
			int orderNum = 1;
			if (orderTaxons != null) {
				for (int i = 0; i < orderTaxons.size(); i++) {
					Taxon orderTaxon = orderTaxons.get(i);;
					String orderPid = orderTaxon.getId();
					String orderMark = null;
					if (String.valueOf(orderNum).length() == 1) {
						orderMark = classRemark.substring(0, 2) + "0" + String.valueOf(orderNum) + classRemark.substring(4, 16);
					} else {
						orderMark = classRemark.substring(0, 2) + String.valueOf(orderNum) + classRemark.substring(4, 16);
					}
					
					orderNum = orderNum + 2;
					orderTaxon.setRemark(orderMark);
					taxons.add(orderTaxon);
					this.taxonRepository.save(orderTaxon);
					
					List<Taxon> familyTaxons = this.taxonRepository.getTaxonListByTreeNodeId(orderPid); // 查目的科
					int familyNum = 1;
					if (familyTaxons != null) {
						for (int j = 0; j < familyTaxons.size(); j++) {
							Taxon familyTaxon = familyTaxons.get(j);
							String familyPid = familyTaxon.getId();
							
							String familyMark = null;
							if (String.valueOf(familyNum).length() == 3) {
								familyMark = orderMark.substring(0, 4) + String.valueOf(familyNum) + orderMark.substring(7, 16);
							} else if (String.valueOf(familyNum).length() == 2) {
								familyMark = orderMark.substring(0, 4) + "0" +  String.valueOf(familyNum) + orderMark.substring(7, 16);
							} else {
								familyMark = orderMark.substring(0, 4) + "00" +  String.valueOf(familyNum) + orderMark.substring(7, 16);
							}
							
							familyNum = familyNum + 2;
							familyTaxon.setRemark(familyMark);
							taxons.add(familyTaxon);
							this.taxonRepository.save(familyTaxon);
							/**
							 * 查科的亚科或属
							 */
							List<Taxon> subfamilyOrGenusTaxons = this.taxonRepository.getTaxonListByTreeNodeId(familyPid); // 查科的亚科或属
							if (subfamilyOrGenusTaxons != null) {
								int genusNum = 1;
								for (int k = 0; k < subfamilyOrGenusTaxons.size(); k++) {
									Taxon subfamilyOrGenusTaxon = subfamilyOrGenusTaxons.get(k);
									String subfamilyOrGenusId = subfamilyOrGenusTaxon.getId();
									String subfamilyOrGenusRank = subfamilyOrGenusTaxon.getRank().getId();
									
									if (subfamilyOrGenusRank.equals("11")) {		// 亚科
										List<Taxon> genusTaxons = this.taxonRepository.getTaxonListByTreeNodeId(subfamilyOrGenusId); // 查亚科的属
										if (genusTaxons != null) {
											for (int m = 0; m < genusTaxons.size(); m++) {
												Taxon genusTaxon = genusTaxons.get(m);
												String genusPid = genusTaxon.getId();
												String genusMark = null;
												if (String.valueOf(genusNum).length() == 3) {
													genusMark = familyMark.substring(0, 7) + String.valueOf(genusNum) + familyMark.substring(10, 16);
												} else if (String.valueOf(genusNum).length() == 2) {
													genusMark = familyMark.substring(0, 7) + "0" + String.valueOf(genusNum) + familyMark.substring(10, 16);
												} else {
													genusMark = familyMark.substring(0, 7) + "00" + String.valueOf(genusNum) + familyMark.substring(10, 16);
												}
												
												genusNum = genusNum + 2;
												genusTaxon.setRemark(genusMark);
												taxons.add(genusTaxon);
												this.taxonRepository.save(genusTaxon);
														
												List<Taxon> subgenusOrSpeciesTaxons = this.taxonRepository.getTaxonListByTreeNodeId(genusPid);	// 查属的亚属或种
												if (subgenusOrSpeciesTaxons != null) {
													int speciesNum = 1;
													for (int n = 0; n < subgenusOrSpeciesTaxons.size(); n++) {
														Taxon subgenusOrSpeciesTaxon = subgenusOrSpeciesTaxons.get(n);
														String subgenusOrSpeciesPid = subgenusOrSpeciesTaxon.getId();
														String subgenusOrSpeciesRank = subgenusOrSpeciesTaxon.getRank().getId();
														
														if (subgenusOrSpeciesRank.equals("12")) {				// 亚属
															List<Taxon> speciesTaxons = this.taxonRepository.getTaxonListByTreeNodeId(subgenusOrSpeciesPid); // 查亚属的种
															if (speciesTaxons != null) {
																for (int p = 0; p < speciesTaxons.size(); p++) {
																	Taxon speciesTaxon = speciesTaxons.get(p);
																	String speciesMark = null;
																	if (String.valueOf(speciesNum).length() == 4) {
																		speciesMark = genusMark.substring(0, 10) + String.valueOf(speciesNum) + genusMark.substring(14, 16);
																	} else if (String.valueOf(speciesNum).length() == 3) {
																		speciesMark = genusMark.substring(0, 10) + "0" + String.valueOf(speciesNum) + genusMark.substring(14, 16);
																	} else if (String.valueOf(speciesNum).length() == 2) {
																		speciesMark = genusMark.substring(0, 10) + "00" + String.valueOf(speciesNum) + genusMark.substring(14, 16);
																	} else {
																		speciesMark = genusMark.substring(0, 10) + "000" + String.valueOf(speciesNum) + genusMark.substring(14, 16);
																	}
																	
																	speciesNum = speciesNum + 2;
																	speciesTaxon.setRemark(speciesMark);
																	taxons.add(speciesTaxon);
																	this.taxonRepository.save(speciesTaxon);
																}
															}
														} else {												// 种
															String speciesMark = null;
															if (String.valueOf(speciesNum).length() == 4) {
																speciesMark = genusMark.substring(0, 10) + String.valueOf(speciesNum) + genusMark.substring(14, 16);
															} else if (String.valueOf(speciesNum).length() == 3) {
																speciesMark = genusMark.substring(0, 10) + "0" + String.valueOf(speciesNum) + genusMark.substring(14, 16);
															} else if (String.valueOf(speciesNum).length() == 2) {
																speciesMark = genusMark.substring(0, 10) + "00" + String.valueOf(speciesNum) + genusMark.substring(14, 16);
															} else {
																speciesMark = genusMark.substring(0, 10) + "000" + String.valueOf(speciesNum) + genusMark.substring(14, 16);
															}
															
															speciesNum = speciesNum + 2;
															subgenusOrSpeciesTaxon.setRemark(speciesMark);
															taxons.add(subgenusOrSpeciesTaxon);
															this.taxonRepository.save(subgenusOrSpeciesTaxon);
														}
													}
												} else {
													int speciesNum = 1;
													List<Taxon> speciesTaxons = this.taxonRepository.getTaxonListByTreeNodeId(genusPid); // 查属的种
													if (speciesTaxons != null) {
														for (int o = 0; o < speciesTaxons.size(); o++) {
															Taxon speciesTaxon = speciesTaxons.get(o);
															String speciesMark = null;
															if (String.valueOf(speciesNum).length() == 4) {
																speciesMark = genusMark.substring(0, 10) + String.valueOf(speciesNum) + genusMark.substring(14, 16);
															} else if (String.valueOf(speciesNum).length() == 3) {
																speciesMark = genusMark.substring(0, 10) + "0" + String.valueOf(speciesNum) + genusMark.substring(14, 16);
															} else if (String.valueOf(speciesNum).length() == 2) {
																speciesMark = genusMark.substring(0, 10) + "00" + String.valueOf(speciesNum) + genusMark.substring(14, 16);
															} else {
																speciesMark = genusMark.substring(0, 10) + "000" + String.valueOf(speciesNum) + genusMark.substring(14, 16);
															}
															
															speciesNum = speciesNum + 2;
															speciesTaxon.setRemark(speciesMark);
															taxons.add(speciesTaxon);
															this.taxonRepository.save(speciesTaxon);
														}
													}
												}
											}
										}
									} else {										// 属 -- 科  - 查属的亚属或种
										String genusMark = null;
										if (String.valueOf(genusNum).length() == 3) {
											genusMark = familyMark.substring(0, 7) + String.valueOf(genusNum) + familyMark.substring(10, 16);
										} else if (String.valueOf(genusNum).length() == 2) {
											genusMark = familyMark.substring(0, 7) + "0" + String.valueOf(genusNum) + familyMark.substring(10, 16);
										} else {
											genusMark = familyMark.substring(0, 7) + "00" + String.valueOf(genusNum) + familyMark.substring(10, 16);
										}
										
										genusNum = genusNum + 2;
										subfamilyOrGenusTaxon.setRemark(genusMark);
										taxons.add(subfamilyOrGenusTaxon);
										this.taxonRepository.save(subfamilyOrGenusTaxon);
												
										List<Taxon> subgenusOrSpeciesTaxons = this.taxonRepository.getTaxonListByTreeNodeId(subfamilyOrGenusId);	// 查属的亚属或种
										if (subgenusOrSpeciesTaxons != null) {
											int speciesNum = 1;
											for (int a = 0; a < subgenusOrSpeciesTaxons.size(); a++) {
												Taxon subgenusOrSpeciesTaxon = subgenusOrSpeciesTaxons.get(a);
												String subgenusOrSpeciesPid = subgenusOrSpeciesTaxon.getId();
												String subgenusOrSpeciesRank = subgenusOrSpeciesTaxon.getRank().getId();
												
												if (subgenusOrSpeciesRank.equals("12")) {				// 亚属
													List<Taxon> speciesTaxons = this.taxonRepository.getTaxonListByTreeNodeId(subgenusOrSpeciesPid); // 查亚属的种
													if (speciesTaxons != null) {
														for (int b = 0; b < speciesTaxons.size(); b++) {
															Taxon speciesTaxon = speciesTaxons.get(b);
															String speciesMark = null;
															if (String.valueOf(speciesNum).length() == 4) {
																speciesMark = genusMark.substring(0, 10) + String.valueOf(speciesNum) + genusMark.substring(14, 16);
															} else if (String.valueOf(speciesNum).length() == 3) {
																speciesMark = genusMark.substring(0, 10) + "0" + String.valueOf(speciesNum) + genusMark.substring(14, 16);
															} else if (String.valueOf(speciesNum).length() == 2) {
																speciesMark = genusMark.substring(0, 10) + "00" + String.valueOf(speciesNum) + genusMark.substring(14, 16);
															} else {
																speciesMark = genusMark.substring(0, 10) + "000" + String.valueOf(speciesNum) + genusMark.substring(14, 16);
															}
															
															speciesNum = speciesNum + 2;
															speciesTaxon.setRemark(speciesMark);
															taxons.add(speciesTaxon);
															this.taxonRepository.save(speciesTaxon);
														}
													}
												} else {												// 种
													String speciesMark = null;
													if (String.valueOf(speciesNum).length() == 4) {
														speciesMark = genusMark.substring(0, 10) + String.valueOf(speciesNum) + genusMark.substring(14, 16);
													} else if (String.valueOf(speciesNum).length() == 3) {
														speciesMark = genusMark.substring(0, 10) + "0" + String.valueOf(speciesNum) + genusMark.substring(14, 16);
													} else if (String.valueOf(speciesNum).length() == 2) {
														speciesMark = genusMark.substring(0, 10) + "00" + String.valueOf(speciesNum) + genusMark.substring(14, 16);
													} else {
														speciesMark = genusMark.substring(0, 10) + "000" + String.valueOf(speciesNum) + genusMark.substring(14, 16);
													}
													
													speciesNum = speciesNum + 2;
													subgenusOrSpeciesTaxon.setRemark(speciesMark);
													taxons.add(subgenusOrSpeciesTaxon);
													this.taxonRepository.save(subgenusOrSpeciesTaxon);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		System.out.println("数据量：" + taxons.size());
		/*for (Taxon taxon : taxons) {
			this.taxonRepository.save(taxon);
		}*/
		System.out.println("执行完毕......");
	}

	@Override
	public void getFishDatasByTaxasetIdAndRank(String taxasetId) {
		List<Taxon> taxons = this.taxonRepository.getFishByTaxasetIdAndRank(taxasetId);
		for (Taxon taxon : taxons) {
			String remark = taxon.getRemark();
			String rankId = taxon.getRank().getId();
			String latin = taxon.getScientificname();
			String chname = taxon.getChname();
			switch (rankId) {
			case "3":
				System.out.println(remark + "\t" + latin + "\t" + chname);
				break;
			case "4":
				System.out.println(remark + "\t" + latin + "\t" + "  " + chname);
				break;
			case "5":
				System.out.println(remark + "\t" + latin + "\t" + "    " + chname);
				break;
			case "6":
				System.out.println(remark + "\t" + latin + "\t" + "      " + chname);
				break;
			case "7":
				System.out.println(remark + "\t" + latin + "\t" + "        " + chname);
				break;
			}
		}
	}

	@Override
	public void getFishRemarkByTaxasetIdAndRank(String taxasetId) {
		List<Taxon> genusTaxons = this.taxonRepository.getFishByTaxasetIdAndRank(taxasetId); // 查科的属
		for (Taxon taxon : genusTaxons) {
			String remark = taxon.getRemark();
			String id = taxon.getId();
			String latin = taxon.getScientificname();
			String chname = taxon.getChname();
			if (StringUtils.isNotBlank(remark) && remark.length() > 16) {
				System.out.println(id + "\t" + latin + "\t" + chname + "\t" + remark);
			}
		}
	}

	@Override
	public void parseRedlistDatas(String path) throws Exception {
		// 创建XSSFWorkbook
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		int sheetNum = workbook.getNumberOfSheets();
		for (int i = 0; i < sheetNum; i++) {
			XSSFSheet sheet = workbook.getSheetAt(i);
			// 获取总行数
			int rowNums = sheet.getLastRowNum();
			System.out.println("解析第 " + (i + 1) + " 个Sheet，数据量：" + rowNums);
			for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
				XSSFRow row = sheet.getRow(rowNum);
				if (null == row) {
					continue;
				} else {
					String id = UUIDUtils.getUUID32();
					String familyC = excelService.getStringValueFromCell(row.getCell(0));
					String family = excelService.getStringValueFromCell(row.getCell(1));
					String chname = excelService.getStringValueFromCell(row.getCell(2));
					String scientificname = excelService.getStringValueFromCell(row.getCell(3));
					String status = excelService.getStringValueFromCell(row.getCell(4));
					String assess = excelService.getStringValueFromCell(row.getCell(5));
					String endemic = excelService.getStringValueFromCell(row.getCell(6));
					String type = excelService.getStringValueFromCell(row.getCell(7));
					
					chname = StringUtils.isNotBlank(chname) ? chname : null;
					assess = StringUtils.isNotBlank(assess) ? assess : null;
					endemic = StringUtils.isNotBlank(endemic) ? endemic : null;
					
					Redlist redlist = new Redlist();
					redlist.setId(id);
					redlist.setFamilyC(familyC);
					redlist.setFamily(family);
					redlist.setChname(chname);
					redlist.setScientificname(scientificname);
					redlist.setStatus(status);
					redlist.setAssess(assess);
					redlist.setEndemic(endemic);
					redlist.setType(type);
					this.redlistRepository.save(redlist);
				}
			}
		}
	}

	@Override
	public void handleAmphbiaCitation(String inputer) {
		List<Citation> list = this.citationRepository.findAllByInputerAndAuthorship(inputer);
		for (Citation citation : list) {
			/*String authorship = citation.getAuthorship().trim();
			String sciname = citation.getSciname().trim();
			char firstCharOfLastWord = authorship.charAt(0);
			if (('a' <= firstCharOfLastWord && firstCharOfLastWord <= 'z') || firstCharOfLastWord == '(') {
				int index = authorship.indexOf(" ");
				String newSciname = sciname + " " + authorship.substring(0, index).trim();
				String newAuthorship = authorship.substring(index).trim();
				citation.setSciname(newSciname);
				citation.setAuthorship(newAuthorship);
				this.citationRepository.save(citation);
			}*/
			
			String authorship = citation.getAuthorship().trim();
			String sciname = citation.getSciname().trim();
			int lastIndexOf = sciname.lastIndexOf(" ");
			if (lastIndexOf != -1) {
				String str = sciname.substring(lastIndexOf).trim();
				char firstCharOfLastWord = str.charAt(0);
				if (('A' <= firstCharOfLastWord && firstCharOfLastWord <= 'Z')) {
					/*int index = authorship.indexOf(" ");
				String newSciname = sciname + " " + authorship.substring(0, index).trim();
				String newAuthorship = authorship.substring(index).trim();
				citation.setSciname(newSciname);
				citation.setAuthorship(newAuthorship);
				this.citationRepository.save(citation);*/
					System.out.println(sciname + "\t" + authorship);
				}
			}
		}
		
	}

	@Override
	public void formatAmphbiaDis(String inputer) {
		List<Description> list = this.descriptionRepository.findAllByInputer(inputer);
		for (Description description : list) {
			Distributiondata distributiondata = new Distributiondata();
			distributiondata.setId(UUIDUtils.getUUID32());
			distributiondata.setDescid(description.getId());
			distributiondata.setDiscontent(description.getDescontent());
			distributiondata.setDistype(description.getDescriptiontype().getId());
			distributiondata.setInputer(description.getInputer());
			distributiondata.setSourcesid(description.getSourcesid());
			distributiondata.setInputtime(description.getInputtime());
			distributiondata.setStatus(description.getStatus());
			distributiondata.setSynchdate(description.getSynchdate());
			distributiondata.setTaxon(description.getTaxon());
			distributiondataRepository.save(distributiondata);
		}
	}

	@Override
	public void parseAmphbiaCommonname(String path) throws Exception {
		// 创建XSSFWorkbook
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				String id = UUIDUtils.getUUID32();
				String commonname = excelService.getStringValueFromCell(row.getCell(0));
				String taxonId = excelService.getStringValueFromCell(row.getCell(1));
				Taxon taxon = this.taxonRepository.findOneById(taxonId);
				Commonname thisCommonname = new Commonname(commonname, "1", null, "61a8d179-00c9-4a9c-9b06-561992b1d89c", null, null, taxon);
				thisCommonname.setId(UUIDUtils.getUUID32());
				thisCommonname.setInputer("4a4327184629404ba0566e2236d495da");
				thisCommonname.setInputtime(new Timestamp(System.currentTimeMillis()));
				thisCommonname.setSynchdate(new Timestamp(System.currentTimeMillis()));
				
				this.commonnameRepository.save(thisCommonname);
			}
		}
	}

	@Override
	public void parseAmphbiaRefs(String inputer) {
		/*List<Ref> list = this.refRepository.findByInputer(inputer);
		System.out.println(list.size());
		for (Ref ref : list) {
			String refcontent = ref.getRefstr();
			System.out.println(refcontent);
			if (refcontent.contains("图版")) {
				int index1 = refcontent.lastIndexOf(",");
				refcontent = refcontent.substring(0, index1);
				int index2 = refcontent.lastIndexOf(":");
				refcontent = refcontent.substring(index2).replace(":", "").replace(".", "").trim();
			} else {
				int index = refcontent.lastIndexOf(":");
				refcontent = refcontent.substring(index).replace(":", "").replace(".", "").trim();
			}
			String[] ppArr = refcontent.split("-");
			if (ppArr.length == 2) {
				System.out.println(refcontent + "\t" + ppArr[0] + "\t" + ppArr[1]);
			} else {
				System.out.println("异常数据：" + refcontent);
			}
		}*/
		
		
		List<Taxon> list = this.taxonRepository.findByInputerAndRefjson(inputer);
		for (Taxon taxon : list) {
			String refjson = taxon.getRefjson();
			JSONArray arr = JSON.parseArray(refjson);
			String refstr = arr.getJSONObject(0).get("otherRef").toString();
			String[] split = refstr.split(";");
			StringBuilder sb = new StringBuilder();
			for (String ref : split) {
				ref = ref.trim() + " ";
				int yearStart = getYearStart(ref);
				String authors = ref.substring(0, yearStart).replace(",", "").replace("等", "").replace("and", "-").replace("和", "-").replace("et al.", "").trim();
				String years = ref.substring(yearStart);
				String[] yearArr = years.split(",");
				
				for (int i = 0; i < yearArr.length; i++) {
					//System.out.println(authors + "\t" + yearArr[i].trim());
					String[] authorArr = authors.split("-");
					Ref thisRef = new Ref();
					System.out.println();
					System.out.println(authors + "\t" + yearArr[i].trim() + "\t" + authorArr.length);
					if (authorArr.length == 1) {
						thisRef = this.refRepository.findByPyearAndAuthorAndInputer(authorArr[0].trim(), yearArr[i].trim(), inputer);
					} else {
						thisRef = this.refRepository.findByPyearAndAuthorsAndInputer(authorArr[0].trim(), authorArr[1].trim(), yearArr[i].trim(), inputer);
					}
					if (null != thisRef) {
						String[] refPara = getRefPara(thisRef);
						String jsonStr = new String();
						if (refPara.length == 2) {
							jsonStr = "{" 
									+ "\"refId\"" + ":\"" + thisRef.getId() + "\","
									+ "\"refS\"" + ":\"" + refPara[0] + "\"," 
									+ "\"refE\"" + ":\"" + refPara[1] + "\""
									+ "}";
						} else {
							jsonStr = "{" 
									+ "\"refId\"" + ":\"" + thisRef.getId() + "\","
									+ "\"refS\"" + ":\"" + null + "\"," 
									+ "\"refE\"" + ":\"" + null + "\""
									+ "}";
						}
						sb.append(jsonStr).append(",");
					}
				}
			}
			String rsl = sb.toString().endsWith(",") ? sb.toString().substring(0, sb.toString().length() - 1) : sb.toString();
			System.out.println("文献：" + refstr);
			System.out.println("解析结果：" + "[" + rsl + "]");
			taxon.setRefjson("[" + rsl + "]");
			taxonRepository.save(taxon);
		}
	}
	
	private String[] getRefPara(Ref thisRef) {
		String refcontent = thisRef.getRefstr();
		System.out.println(refcontent);
		if (refcontent.contains("图版")) {
			int index1 = refcontent.lastIndexOf(",");
			refcontent = refcontent.substring(0, index1);
			int index2 = refcontent.lastIndexOf(":");
			refcontent = refcontent.substring(index2).replace(":", "").replace(".", "").trim();
		} else {
			int index = refcontent.lastIndexOf(":");
			refcontent = refcontent.substring(index).replace(":", "").replace(".", "").trim();
		}
		return refcontent.split("-");
	}

	public static int getYearStart(String refstr) {
		int start = -1;
		for (int i = 0; i < refstr.length() - 4; i++) {
			String tmp = refstr.substring(i, i + 4);
			if (isNumeric(tmp)) {
				start = i;
				break;
			}
		}
		return start;
	}
	
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	@Override
	public void citationMatchRefOfAmphbia(String sourcesId) {
		String inputer = "4a4327184629404ba0566e2236d495da";
		// 查询指定数据源的引证
		List<Citation> clist = this.citationRepository.findListBySourcesid(sourcesId);
		System.out.println("待匹配引证数据量：" + clist.size());
		for (Citation citation : clist) {
			String citationstr = new String();
			// 处理完整引证 - 截取、替换
			citationstr = handleCitationStr(citation.getCitationstr(), citation.getSciname(), citation.getAuthorship());
			int lastIndexOf = citationstr.lastIndexOf(":");
			String pp = null;
			if (lastIndexOf != -1) {
				pp = citationstr.substring(lastIndexOf + 1).trim();
			}
			Ref thisRef = null;
			try {
				// 年份起始索引位置
				int yearStart = getYearStart(citationstr);
				// 年份
				String authors = citationstr.substring(0, yearStart).trim();
				if (authors.endsWith(",")) {
					authors = authors.substring(0, authors.length() - 1);
				}
				String[] authorArr = authors.split(",");
				if (yearStart != -1) {
					String year = citationstr.substring(yearStart, yearStart + 4).trim();
					/*System.out.println(citationstr);
					System.out.println(authors + "\t" + year + "\t" + authorArr.length);*/
					if (authorArr.length == 1) {
						thisRef = this.refRepository.findByPyearAndAuthorAndInputer(authorArr[0].trim(), year.trim(), inputer);
					} else if (authorArr.length == 2) {
						thisRef = this.refRepository.findByPyearAndAuthorsAndInputer(authorArr[0].trim(), authorArr[1].trim(), year.trim(), inputer);
					} else if (authorArr.length == 3) {
						thisRef = this.refRepository.findByPyearAndAuthorsAndInputer(authorArr[0].trim(), authorArr[1].trim(), authorArr[2].trim(), year.trim(), inputer);
					} else {
						//thisRef = this.refRepository.findByPyearAndAuthorsAndInputer(authorArr[0].trim(), authorArr[1].trim(), authorArr[2].trim(), authorArr[3].trim(), year.trim(), inputer);
					}
				}
				
				if (null != thisRef) {
					String jsonStr = "[{" 
							+ "\"refId\"" + ":\"" + thisRef.getId() + "\","
							+ "\"refType\"" + ":\"" + 0 + "\"," 
							+ "\"refS\"" + ":\"" + pp + "\"," 
							+ "\"refE\"" + ":\"" + pp + "\""
							+ "}]";
					citation.setRefjson(jsonStr);
					this.citationRepository.save(citation);
				}
			} catch (Exception e) {
				System.out.println("异常：" + citation.getCitationstr());
			}
		}
	}

	private String handleCitationStr(String citationstr, String sciname, String authorship) {
		String rsl = new String();
		rsl = citationstr.replace(sciname, "").replace(" - ", "").trim();
		
		int index1 = rsl.indexOf(" Type ");
		if (index1 != -1) {
			rsl = rsl.substring(0, index1);
		}
		int index2 = rsl.indexOf("Holotype:");
		if (index2 != -1) {
			rsl = rsl.substring(0, index2);
		}
		if (rsl.startsWith(",")) {
			rsl = rsl.substring(1).trim();
		}
		if (rsl.endsWith(".")) {
			rsl = rsl.substring(0, rsl.length() - 1);
		}
		return rsl.trim();
	}

	@Override
	public void handleAmphbiaTaxon(String inputer) {
		List<Taxon> list = this.taxonRepository.findByInputer(inputer);
		for (Taxon taxon : list) {
			String scientificname = taxon.getScientificname().trim();
			String rankId = taxon.getRank().getId();
			if (rankId.equals("7")) {
				String[] split = scientificname.split(" ");
				if (split.length == 2) {
					taxon.setEpithet(split[1].trim());
					this.taxonRepository.save(taxon);
				} else {
					System.out.println("异常：" + scientificname);
				}
			}
		}
	}

	@Override
	public void getChordataCode(String path, String reptilia, String amphbia) throws Exception {
		// 创建XSSFWorkbook
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		String orderCode = null;
		String familyCode = null;
		String genusCode = null;
		String speciesCode = null;
		for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				String chname = excelService.getStringValueFromCell(row.getCell(1));
				String latin = excelService.getStringValueFromCell(row.getCell(2));
				
				if (chname.endsWith("纲")) {
					orderCode = null;
					System.out.println(reptilia + "\t" + chname + "\t" + latin);
				} else if (chname.endsWith("目")) {
					if (StringUtils.isBlank(orderCode)) {
						orderCode = reptilia.substring(0, 2) + "0" + String.valueOf(1) + reptilia.substring(4, 16);
					} else {
						int num = Integer.parseInt(orderCode.substring(2, 4));
						num = num + 2;
						if (String.valueOf(num).length() == 1) {	// 一位数
							orderCode = orderCode.substring(0, 2) + "0" + String.valueOf(num) + orderCode.substring(4, 16);
						} else {									// 两位数
							orderCode = orderCode.substring(0, 2) + String.valueOf(num) + orderCode.substring(4, 16);
						}
					}
					familyCode = null;
					System.out.println(orderCode + "\t" + "  " + chname + "\t" + latin);
				} else if (chname.endsWith("科")) {
					if (StringUtils.isBlank(familyCode)) {
						familyCode = orderCode.substring(0, 4) + "00" + String.valueOf(1) + orderCode.substring(7, 16);
					} else {
						int num = Integer.parseInt(familyCode.substring(4, 7));
						num = num + 2;
						if (String.valueOf(num).length() == 1) {	// 一位数
							familyCode = orderCode.substring(0, 4) + "00" + String.valueOf(num) + orderCode.substring(7, 16);
						} else {									// 两位数
							familyCode = orderCode.substring(0, 4) + "0" + String.valueOf(num) + orderCode.substring(7, 16);
						}
					}
					genusCode = null;
					System.out.println(familyCode  + "\t" + "    " + chname + "\t" + latin);
				} else if (chname.endsWith("属")) {
					if (StringUtils.isBlank(genusCode)) {
						genusCode = familyCode.substring(0, 7) + "00" + String.valueOf(1) + familyCode.substring(10, 16);
					} else {
						int num = Integer.parseInt(genusCode.substring(7, 10));
						num = num + 2;
						if (String.valueOf(num).length() == 1) {	// 一位数
							genusCode = familyCode.substring(0, 7) + "00" + String.valueOf(num) + familyCode.substring(10, 16);
						} else {									// 两位数
							genusCode = familyCode.substring(0, 7) + "0" + String.valueOf(num) + familyCode.substring(10, 16);
						}
					}
					speciesCode = null;
					System.out.println(genusCode + "\t" + "      " + chname + "\t" + latin);
				} else {
					if (StringUtils.isBlank(speciesCode)) {
						speciesCode = genusCode.substring(0, 10) + "000" + String.valueOf(1) + genusCode.substring(14, 16);
					} else {
						int num = Integer.parseInt(speciesCode.substring(10, 14));
						num = num + 2;
						if (String.valueOf(num).length() == 1) {			// 一位数
							speciesCode = genusCode.substring(0, 10) + "000" + String.valueOf(num) + genusCode.substring(14, 16);
						} else if (String.valueOf(num).length() == 2) {		// 两位数
							speciesCode = genusCode.substring(0, 10) + "00" + String.valueOf(num) + genusCode.substring(14, 16);
						} else {											// 三位数
							speciesCode = genusCode.substring(0, 10) + "0" + String.valueOf(num) + genusCode.substring(14, 16);
						}
					}
					System.out.println(speciesCode + "\t" + "        " + chname + "\t" + latin);
				}
			}
		}
	}

	@Override
	public void wordTransExcel(String path) throws Exception {
		// 创建XSSFWorkbook
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		int sheetNum = workbook.getNumberOfSheets();
		
		String classCode = null;
		String orderCode = null;
		String familyCode = null;
		String genusCode = null;
		String speciesCode = null;
		String result = "Animalia" + "\t" + "动物界" + "\t" + "Chordata" + "\t" + "脊索动物门";
		for (int i = 0; i < sheetNum; i++) {
			XSSFSheet sheet = workbook.getSheetAt(i);
			// 获取总行数
			int rowNums = sheet.getLastRowNum();
			System.out.println("解析第 " + i + " 个Sheet，数据量：" + rowNums);
			for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
				XSSFRow row = sheet.getRow(rowNum);
				if (null == row) {
					continue;
				} else {
					String code = excelService.getStringValueFromCell(row.getCell(0));
					String chname = row.getCell(1).getStringCellValue();
					//String chname = excelService.getStringValueFromCell(row.getCell(1));
					String sciname = excelService.getStringValueFromCell(row.getCell(2));
					
					if (chname.endsWith("纲")) {
						classCode = sciname + "\t" + chname.trim();
						
						orderCode = null; 
						familyCode = null; 
						genusCode = null;
						speciesCode = null;
					} else if (chname.endsWith("目")) {
						orderCode = sciname + "\t" + chname.trim();
						
						familyCode = null;
						genusCode = null;
						speciesCode = null;
					} else if (chname.endsWith("科")) {
						familyCode = sciname + "\t" + chname.trim();
						
						genusCode = null;
						speciesCode = null;
					} else if (chname.endsWith("属") || chname.equals("      ")) {
						genusCode = sciname + "\t" + chname.trim();
						
						speciesCode = null;
					} else {
						speciesCode = sciname + "\t" + chname.trim();
						System.out.println(code + "\t" + result + "\t" + classCode + "\t" + orderCode + "\t" + familyCode + "\t" + genusCode + "\t" + speciesCode);
					} 
				}
			}
		}
	}
}
