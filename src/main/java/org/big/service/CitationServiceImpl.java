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
import org.big.entity.Citation;
import org.big.entity.Expert;
import org.big.entity.Taxon;
import org.big.entity.User;
import org.big.entity.UserDetail;
import org.big.repository.CitationRepository;
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
public class CitationServiceImpl implements CitationService {
	@Autowired
	private CitationRepository citationRepository;
	@Autowired
	private TaxonService taxonService;
	@Autowired
	private UserService userService;
	@Autowired
	private DatasourceService datasourceService;
	@Autowired
	private RefService refService;
	@Autowired
	private ExpertService expertService;
	@Autowired
	private ExcelService excelService;
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private BaseinfotmpService baseinfotmpService;
	@Autowired
	private ExpertRepository expertRepository;
	@Autowired
	private BatchInsertService batchInsertService;
	@Override
	public JSON findUploadedCitationList(Timestamp timestamp, HttpServletRequest request) {
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
		List<Citation> thisList = new ArrayList<>();
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Page<Citation> thisPage = this.citationRepository.searchInfo(searchText, timestamp, QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), thisUser.getId());
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		String thisSelect = "";
		String thisEdit = "";
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
	        thisSelect="<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
	        thisEdit=
	        	 "<a class=\"wts-table-edit-icon\" onclick=\"editThisObject('" + thisList.get(i).getId() + "','citation')\" >" +
	             "<span class=\"glyphicon glyphicon-edit\"></span>" +
	             "</a> &nbsp;&nbsp;&nbsp;" +
	             "<a class=\"wts-table-edit-icon\" onclick=\"removeThisObject('" + thisList.get(i).getId() + "','citation')\" >" +
	             "<span class=\"glyphicon glyphicon-remove\"></span>" +
	             "</a>";
			row.put("select", thisSelect);
			row.put("sciname", thisList.get(i).getSciname());
			row.put("authorship", thisList.get(i).getAuthorship());
			String value = thisList.get(i).getNametype();
			row.put("nametype", getNametypeByValue(value));
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
			row.put("edit", thisEdit);
			rows.add(i, row);
		}
		thisTable.put("rows", rows);
		return thisTable;
    }

	@Override
	public JSON addCitation(String taxonId, HttpServletRequest request) {
		Citation thisCitation = new Citation();
		Enumeration<String> paraNames = request.getParameterNames();
		String paraName = null;
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("citationId_") == 0) {
				thisCitation.setId(request.getParameter(paraName));
			}
				
			if (paraName.indexOf("sciname_") == 0) {
				thisCitation.setSciname(request.getParameter(paraName));
			}
			if (paraName.indexOf("authorship_") == 0) {
				thisCitation.setAuthorship(request.getParameter(paraName));
			} 	
			if (paraName.indexOf("nametype_") == 0) {
				thisCitation.setNametype(request.getParameter(paraName));
			}
			if (paraName.indexOf("citationSourcesid_") == 0) {
				thisCitation.setSourcesid(request.getParameter(paraName));
			}
			if (paraName.indexOf("citationstr_") == 0) {
				thisCitation.setCitationstr(request.getParameter(paraName));
			}
		}
		
		JSONObject thisResult = new JSONObject();
		try {
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			thisCitation.setInputer(thisUser.getId());
			thisCitation.setInputtime(new Timestamp(System.currentTimeMillis()));
			thisCitation.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisCitation.setStatus(1);
			thisCitation.setSynchstatus(0);
			JSONArray handleReferenceToJson = (JSONArray) handleReferenceToJson(request);
			if (handleReferenceToJson.size() > 0) {
				thisCitation.setRefjson(handleReferenceToJson.toJSONString());
			}
			thisCitation.setTaxon(taxonService.findOneById(taxonId));
			this.citationRepository.save(thisCitation);
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
		int countCitationNum = 0;
		String citationReferencesPageE = null;
		String citationReferencesPageS = null;
		String citationReferenceId = null;
		String citationReferenceType = null;
		String jsonStr = null;
		
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("countCitationReferences_") == 0) {
				countCitationNum = Integer.parseInt(request.getParameter(paraName));
			}
			if (paraName.indexOf("citationId_") == 0) {
				formNum = Integer.parseInt(paraName.substring(paraName.indexOf("_") + 1));
			}
		}
		
		for (int i = 1; i <= countCitationNum; i++) {
			citationReferenceId = request.getParameter("citationReferences_" + formNum + "_" + i);
			citationReferencesPageS = request.getParameter("citationReferencesPageS_" + formNum + "_" + i);
			citationReferencesPageE = request.getParameter("citationReferencesPageE_" + formNum + "_" + i);
			citationReferenceType = request.getParameter("citationReferencesId_" + formNum + "_" + i);
			if (StringUtils.isNotBlank(citationReferenceId) && StringUtils.isNotBlank(citationReferencesPageS)
					&& StringUtils.isNotBlank(citationReferencesPageE)) {
				jsonStr = "{" 
						+ "\"refId\"" + ":\"" + citationReferenceId + "\","
						+ "\"refS\"" + ":\"" + citationReferencesPageS + "\"," 
						+ "\"refE\"" + ":\"" + citationReferencesPageE + "\","
						+ "\"refType\"" + ":\"" + citationReferenceType + "\""
						+ "}";
				JSONObject jsonText = JSON.parseObject(jsonStr);
				jsonArray.add(jsonText);
			}
		}
		return jsonArray;
	}

	@Override
	public boolean logicRemove(String id) {
		Citation thisCitation = this.citationRepository.findOneById(id);
		if (null != thisCitation && 1 == thisCitation.getStatus()) {
			thisCitation.setStatus(0);
			this.citationRepository.save(thisCitation);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean deleteOne(HttpServletRequest request) {
		String citationId = request.getParameter("citationId");
		if (StringUtils.isNotBlank(citationId)) {
			if (null != this.citationRepository.findOneById(citationId)) {
				this.citationRepository.deleteOneById(citationId);
			}
			return true;
		}
		return false;
	}

	@Override
	public JSON findCitationListByTaxonId(String taxonId, HttpServletRequest request) {
		int limit_serch = Integer.parseInt(request.getParameter("limit"));		// 1.limit 一次查询返回的个数，默认值10 
		int offset_serch = Integer.parseInt(request.getParameter("offset"));	// 2.offset从第几个开始查，默认值0
		String sort = "synchdate";
		String order = "desc";

		JSONObject thisSelect = new JSONObject();
		Page<Citation> thisPage = this.citationRepository
				.searchCitationsByTaxonId(QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), taxonId);

		JSONArray rows = new JSONArray();
		List<Citation> thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			String sourcesid = thisList.get(i).getSourcesid(); // 拼sourceId的属性名
			if (StringUtils.isNotBlank(sourcesid)) {
				if (null != this.datasourceService.findOneById(sourcesid)) {
					thisList.get(i).setSourcesid(this.datasourceService.findOneById(sourcesid).getTitle());
				}
			}
			JSONObject row = (JSONObject) JSON.toJSON(thisList.get(i));
			String value = thisList.get(i).getNametype();
			row.put("nametype", getNametypeByValue(value));
			rows.add(i, row);
		}

		thisSelect.put("total", thisPage.getTotalElements());		// NO1：总数
		thisSelect.put("page", offset_serch);						// NO2：offset
		thisSelect.put("rows", rows);								// NO3：Taxon下的所有Citation
		return thisSelect;
	}
	/**
	 * <b>获取名称类型</b>
	 * <p>获取名称类型</p>
	 * @param num
	 * @return
	 */
	private String getNametypeByValue(String value) {
		String nametype = "";
		switch (value) {
		case "1":
			nametype = "accepted name";
			break;
		case "2":
			nametype = "ambiguous synonym";
			break;
		case "3":
			nametype = "misapplied name";
			break;
		case "4":
			nametype = "provisionally accepted name";
			break;
		case "5":
			nametype = "synonym";
			break;
		case "7":
			nametype = "uncertain name";
			break;
		default:
			nametype = "无";
			break;
		}
		return nametype;
	}

	@Override
	public JSON editCitations(String taxonId) {
		JSONObject citations = new JSONObject();
		JSONArray citationArr = new JSONArray();
		try {
			if (StringUtils.isNotBlank(taxonId)) {
				List<Citation> list = this.citationRepository.findCitationListByTaxonId(taxonId);
				for (int i = 0; i < list.size(); i++) {
					JSONArray refjson = this.refService.refactoringRef(list.get(i).getRefjson());
					JSONObject json = new JSONObject();
					json.put("id", list.get(i).getId());
					json.put("sciname", list.get(i).getSciname());
					json.put("authorship", list.get(i).getAuthorship());
					json.put("nametype", list.get(i).getNametype());
					json.put("citationstr", list.get(i).getCitationstr());
					json.put("shortrefs", list.get(i).getShortrefs());
					json.put("refjson", list.get(i).getRefjson());
					json.put("refjson", refjson.toJSONString());
					// 数据源
					String sourcesid = list.get(i).getSourcesid();
					String sourcesTitle = this.datasourceService.findOneById(sourcesid).getTitle();
					json.put("sourcesid", sourcesid);
					json.put("sourcesTitle", sourcesTitle);
					citationArr.add(i, json);
				}
				citations.put("citations", citationArr);
			}
		} catch (Exception e) {
		}
		return citations;
	}

	@Override
	public void export(HttpServletResponse response) throws IOException {
		String[] columnName = { "*引证学名", "*命名信息", "*名称状态(类型)", "*完整引证", "*接受名（引证学名的接受名）", "*数据源", "*参考文献",
				"*审核专家", "版权所有者", "版权声明", "共享协议" };
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Citation> thisList = this.citationRepository.findCitationListByUserId(thisUser.getId());
		
		// 第1步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第2步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("引证信息");

		// 第3.1步，创建表头的列
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < columnName.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(columnName[i]);
			cell.setCellStyle(ExcelUtil.setTitleStyle(wb));
		}

		sheet.setColumnWidth(0, 15 * 256);
		sheet.setColumnWidth(1, 12 * 256);
		sheet.setColumnWidth(2, 12 * 256);
		sheet.setColumnWidth(3, 20 * 256);
		sheet.setColumnWidth(4, 20 * 256);
		sheet.setColumnWidth(5, 10 * 256);
		sheet.setColumnWidth(6, 12 * 256);
		sheet.setColumnWidth(7, 12 * 256);
		sheet.setColumnWidth(8, 12 * 256);
		sheet.setColumnWidth(9, 12 * 256);

		// 第3.2步，创建单元格，并设置值
		for (int i = 0; i < thisList.size(); i++) {
			HSSFRow rows = sheet.createRow((int) (i + 1));
			Citation thisCitation = thisList.get(i);
			rows.createCell(0).setCellValue(thisCitation.getSciname()); // 设置第1列序号
			rows.createCell(1).setCellValue(thisCitation.getAuthorship());
			rows.createCell(2).setCellValue(thisCitation.getNametype());
			rows.createCell(3).setCellValue(thisCitation.getCitationstr());
			rows.createCell(4).setCellValue(thisCitation.getTaxon().getScientificname());
			rows.createCell(5).setCellValue(thisCitation.getSourcesid());
			rows.createCell(6).setCellValue(this.refService.handleRefjsonForExport(thisCitation.getRefjson()));
			Expert thisExpert = this.expertService.findOneById(thisCitation.getExpert());
			if (null != thisExpert) {
				rows.createCell(7).setCellValue(thisExpert.getCnName() + "<" + thisExpert.getExpEmail() + ">");
			}
		}
		// 第4步，将文件存到浏览器设置的下载位置
		// 告诉浏览器用什么软件可以打开此文件
		response.setHeader("content-Type", "application/vnd.ms-excel");
		// 下载文件的默认名称
		response.setHeader("Content-Disposition", "attachment; filename=" + new String("引证信息".getBytes("gb2312"), "iso8859-1") + ".xls");
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
		List<Citation> citationList = new ArrayList<>();							// 上传文件的数据
		
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
					/*String tableHeadArray[] = { "*引证学名", "*命名信息", "*名称状态(类型)", "*完整引证", "*接受名（引证学名的接受名）", "*数据源",
							"*参考文献", "*审核专家", "版权所有者", "版权声明", "共享协议" };*/
					String tableHeadArray[] = { "引证学名", "命名信息", "*名称状态", "完整引证", "接受名", "数据源",
							"参考文献", "审核专家", "版权所有者", "版权声明", "共享协议" };
					if(this.excelService.judgeRowConsistent(11,tableHeadArray,sheet.getRow(0))){
                        try {
                        	Citation thisCitation = new Citation();				// Citation对象
                            Row row = null;										// 记录行
                            for (int i = 1; i <= rowNum; i++) {
                                row = sheet.getRow(i);
                                if (null == sheet.getRow(i)) { continue; }
                                
                                String scientificname = excelService.getStringValueFromCell(row.getCell(4)).trim();
                                Taxon thisTaxon = this.taxonRepository.findOneByScientificnameAndTaxasetId(scientificname, taxasetId);	// Citation与Taxon关联
                                thisCitation = new Citation(
                                		excelService.getStringValueFromCell(row.getCell(0)),
                                		excelService.getStringValueFromCell(row.getCell(1)),
                                		excelService.getStringValueFromCell(row.getCell(2)),
                                		excelService.getStringValueFromCell(row.getCell(3)),
                                		excelService.getStringValueFromCell(row.getCell(5)),
                                		excelService.getStringValueFromCell(row.getCell(6)),
                                		excelService.getStringValueFromCell(row.getCell(7)),
                                		thisTaxon);
                                
                                String remark = "{"
	                            		+ "\"" + "rightsholder" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(8)) + "\","
	                            		+ "\"" + "copyright" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(9)) + "\","
	                            		+ "\"" + "license" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(10)) + "\","
	                            		+ "}";
                                thisCitation.setRemark(remark);
                                
                                citationList.add(thisCitation);
                            }
                            long start = System.currentTimeMillis();
                            JSONArray verifyListEntity = verifyListEntity(citationList, thisUser, refFileMark, dsFileMark, expFileMark);
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
	private JSONArray verifyListEntity(List<Citation> list, User thisUser, String refFileMark, String dsFileMark, String expFileMark) throws Exception {
		JSONArray citationArr = new JSONArray();
		List<Citation> failList = new ArrayList<>();
        boolean flag = true;
        for (int i = 0; i < list.size(); i++) {
        	Citation thisCitation = list.get(i);
        	boolean mark = true;
        	if (null == thisCitation.getTaxon()) {
        		thisCitation.setTaxon(new Taxon("<span style='color:red'>未匹配到对应分类单元，请先上传分类单元</span>"));
        		mark = false;
			}
        	if (StringUtils.isBlank(thisCitation.getSciname())) {
				thisCitation.setSciname("<span style='color:red'>引证学名不能为空</span>");
				mark = false;
			}
        	if (StringUtils.isBlank(thisCitation.getAuthorship())) {
        		thisCitation.setAuthorship("<span style='color:red'>命名信息不能为空</span>");
        		mark = false;
            };
            String nametype = thisCitation.getNametype();
            if (StringUtils.isBlank(nametype)) {
            	thisCitation.setNametype("<span style='color:red'>名称状态不能为空</span>");
            	mark = false;
            }else {
				switch (nametype) {
				case "accepted name":
					thisCitation.setNametype("1");
					break;
				case "ambiguous synonym":
					thisCitation.setNametype("2");
					break;
				case "misapplied name":
					thisCitation.setNametype("3");
					break;
				case "provisionally accepted name":
					thisCitation.setNametype("4");
					break;
				case "synonym":
					thisCitation.setNametype("5");
					break;
				case "uncertain name":
					thisCitation.setNametype("6");
					break;
				default:
					thisCitation.setNametype("<span style='color:red'>名称状态不能为空</span>");
	            	mark = false;
					break;
				}
			};
            if (StringUtils.isBlank(thisCitation.getCitationstr())) {
            	thisCitation.setCitationstr("<span style='color:red'>完整引证不能为空</span>");
            	mark = false;
            };
            
            /*数据源*/
            if (StringUtils.isBlank(thisCitation.getSourcesid())) {
            	thisCitation.setSourcesid("<span style='color:red'>数据源不能为空</span>");
            	mark = false;
            }else  {
            	String source = this.baseinfotmpService.findDsIdByFilemarkAndSerialNumAndFileType(dsFileMark, thisCitation.getSourcesid(), 1);
            	if (StringUtils.isNotBlank(source)) {
					thisCitation.setSourcesid(source);
				}else {
					thisCitation.setSourcesid("<span style='color:red'>未找到引用数据源，请检查引用数据存在或是否引用错误！</span>");
					mark = false;
				}
			};
			/*审核专家*/
			if (StringUtils.isBlank(thisCitation.getExpert())) {
				thisCitation.setExpert("<span style='color:red'>专家信息不能为空</span>");
				mark = false;
            }else{
            	boolean tmp = thisCitation.getExpert().matches("[0-9]*");
            	String expid = null;
            	if (tmp) {
            		expid = this.baseinfotmpService.findExpIdByFilemarkAndSerialNumAndFileType(expFileMark, 2, thisCitation.getExpert());
				}else {
					String expertId = this.expertRepository.findIdByCnName(thisCitation.getExpert());
					if (StringUtils.isNotBlank(expertId)) {
						expid = expertId;
					}else {
						thisCitation.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
					}
				}
            	
            	if (StringUtils.isNotBlank(expid)) {
            		thisCitation.setExpert(expid);
				}else{
					thisCitation.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
					mark = false;
				}
            };
            
            /*比对参考文献*/
        	String refjson = thisCitation.getRefjson();
        	List<String> refIds = new ArrayList<>();
        	if (StringUtils.isNotBlank(refjson)) {
        		String[] refStr = refjson.replace("，", ",").split(",");
        		refIds = this.baseinfotmpService.findRefIdByFilemarkAndSerialNumAndFileType(refFileMark, 0, refStr);
        		if (!refIds.isEmpty()) {
        			JSONArray refjsonArr = this.refService.getRefJSONArrayByIds(refIds);
        			if (!refjsonArr.isEmpty()) {
        				thisCitation.setRefjson(refjsonArr.toString());
        			}else {
        				thisCitation.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
						mark = false;
					}
        		}else {
        			thisCitation.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
					mark = false;
        		}
        	};
        	
        	if (!mark) {
        		thisCitation.setSynchstatus(i + 1);
				failList.add(thisCitation);
				flag = mark;
			}
        }
        /* 构建的集合若通过必填字段的校验，去重，比对数据源、参考文献，补充属性值*/
        if (flag) {
        	List<Citation> citationList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
            	String nametype = getValueOfNameType(list.get(i).getNametype());
            	list.get(i).setNametype(nametype);
            	list.get(i).setId(UUIDUtils.getUUID32());
            	list.get(i).setInputer(thisUser.getId());
            	list.get(i).setInputtime(new Timestamp(System.currentTimeMillis()));
            	list.get(i).setSynchdate(new Timestamp(System.currentTimeMillis()));
            	list.get(i).setStatus(1);
            	list.get(i).setSynchstatus(0);
            	/*this.citationRepository.save(list.get(i));*/
            	citationList.add(list.get(i));
            }
            long start = System.currentTimeMillis();
            this.batchInsertService.batchInsertCitation(citationList);
            long end = System.currentTimeMillis();
            System.out.println("Citation批量存储完成：" + (end - start));
        }else {
			for (int i = 0; i < failList.size(); i++) {
				JSONObject json = new JSONObject();
				json.put("num", failList.get(i).getSynchstatus());
				json.put("taxon", failList.get(i).getTaxon().getScientificname());
				json.put("scientificname", failList.get(i).getSciname());
				json.put("authorship", failList.get(i).getAuthorship());
				json.put("nametype", failList.get(i).getNametype());
				json.put("citationstr", failList.get(i).getCitationstr());
				json.put("sourcesid", failList.get(i).getSourcesid());
				json.put("expert", failList.get(i).getExpert());
				citationArr.add(i, json);
			}
		}
        return citationArr;
    }
	/**
	 * 
	 * @param nametype
	 * @return
	 */
	private String getValueOfNameType(String nametype) {
		String rsl = "";
		if (StringUtils.isNotBlank(nametype)) {
			switch (nametype) {
			case "accepted name":
				rsl = "1";
				break;
			case "ambiguous synonym":
				rsl = "2";
				break;
			case "misapplied name":
				rsl = "3";
				break;
			case "provisionally accepted name":
				rsl = "4";
				break;
			case "synonym":
				rsl = "5";
				break;
			case "uncertain name":
				rsl = "6";
				break;
			}
		}
		return rsl;
	}
}
