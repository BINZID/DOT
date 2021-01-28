package org.big.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.big.common.BuildEntity;
import org.big.common.ExcelUtil;
import org.big.common.QueryTool;
import org.big.common.UUIDUtils;
import org.big.entity.Baseinfotmp;
import org.big.entity.Datasource;
import org.big.entity.Expert;
import org.big.entity.Team;
import org.big.entity.UploadedDatasource;
import org.big.entity.UserDetail;
import org.big.repository.DatasourceRepository;
import org.big.repository.ExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class DatasourceServiceImpl implements DatasourceService{
	@Autowired
	private DatasourceRepository datasourceRepository;
	@Autowired 
	private UserService userService;
	@Autowired
	private ExpertRepository expertRepository;
	@Autowired
	private ExcelService excelService; 
	@Autowired
	private BaseinfotmpService baseinfotmpService;
	@Autowired
	private BatchInsertService batchInsertService; 
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
		
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		List<Datasource> thisList = new ArrayList<>();
		/*UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();*/
		
		Team thisTeam = (Team) request.getSession().getAttribute("team");
		Page<Datasource> thisPage = this.datasourceRepository.searchByDsname(findText, thisTeam.getId(),
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		
		thisSelect.put("total_count", thisPage.getTotalElements());
		Boolean incompleteResulte = true;
		if ((thisPage.getTotalElements() / 30) > findPage){
			incompleteResulte = false;
		}
		thisSelect.put("incompleteResulte", incompleteResulte);
		thisList = thisPage.getContent();
		if (findPage == 1) {
			JSONObject row = new JSONObject();
			row.put("id", "addNew");
			row.put("text", "新建一个数据源");
			items.add(row);
		}
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
	public void saveOne(@Valid Datasource thisDatasource, HttpServletRequest request) {	
		try {
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			thisDatasource.setInputer(thisUser.getId());
			thisDatasource.setId(UUID.randomUUID().toString());
			thisDatasource.setStatus(1);
			thisDatasource.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisDatasource.setSynchstatus(0);
			
			this.datasourceRepository.save(thisDatasource);
		} catch (Exception e) {
		}
	}

	@Override
	public void removeOne(String Id) {
		this.datasourceRepository.deleteOneById(Id);
	}

	@Override
	public boolean logicRemove(String id) {
		Datasource thisDatasource = this.datasourceRepository.findOneById(id);
		if (null != thisDatasource && 1 == thisDatasource.getStatus()) {
			thisDatasource.setStatus(0);
			this.datasourceRepository.save(thisDatasource);
			return true;
		}
		return false;
	}
	
	@Override
	public void updateOneById(@Valid Datasource thisDatasource) {
		thisDatasource.setSynchdate(new Timestamp(System.currentTimeMillis()));
		this.datasourceRepository.save(thisDatasource);
	}

	@Override
	public JSON findDatasourceList(HttpServletRequest request) {
		String searchText = request.getParameter("search");
		if (searchText == null || searchText.length() <= 0) {
			searchText = "";
		}
		int limit_serch = Integer.parseInt(request.getParameter("limit"));
		int offset_serch = Integer.parseInt(request.getParameter("offset"));
		String sort=request.getParameter("sort");
		String order=request.getParameter("order");
		if(StringUtils.isBlank(sort)){
			sort = "inputtime";
			order = "desc";
		}
		JSONObject thisTable = new JSONObject();
		List<Datasource> thisList = new ArrayList<>();
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Page<Datasource> thisPage = this.datasourceRepository.searchInfo(searchText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), thisUser.getId());
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		thisTable.put("rows", findDatasourceListForTable(thisList));
		return thisTable;
    }
	
	@Override
	public Datasource findOneById(String id) {
		return this.datasourceRepository.findOneById(id);
	}

	@Override
	public JSON newOne(Datasource thisDatasource, HttpServletRequest request) {
		
		JSONObject thisResult = new JSONObject();
		try {
			thisDatasource.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisDatasource.setStatus(1);
			String id = UUID.randomUUID().toString();
			thisDatasource.setId(id);
			thisDatasource.setSynchstatus(0);
			// 获取当前登录用户
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			thisDatasource.setInputer(thisUser.getId());
			
			this.datasourceRepository.save(thisDatasource);
			
			thisResult.put("result", true);
			thisResult.put("newId", thisDatasource.getId());
			thisResult.put("newTitle", thisDatasource.getTitle());
		} catch (Exception e) {
			thisResult.put("result", false);
		}
		return thisResult;
	}

	@Override
	public void export(HttpServletResponse response) throws IOException {
		String[] columnName = {"*序号", "* 数据源名称", "数据源类型", "* 版本", "创建者", "创建时间", "*数据源摘要", "关键字", "数据源链接", "参考文献", "*审核专家", "版权所有者", "版权声明", "说明/备注"};
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		// 当前用户下的数据源
		List<Datasource> thisList = this.datasourceRepository.findAllByUserId(thisUser.getId());
		// 第1步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第2步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("数据源");
		
		// 第3.1步，创建表头的列
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < columnName.length; i++) {
			HSSFCell cell = row.createCell(i);
     		cell.setCellValue(columnName[i]);
     		cell.setCellStyle(ExcelUtil.setTitleStyle(wb));
     	}
		
		sheet.setColumnWidth(1, 36 * 256);
		sheet.setColumnWidth(2, 12 * 256);
		sheet.setColumnWidth(3, 12 * 256);
		sheet.setColumnWidth(4, 12 * 256);
		sheet.setColumnWidth(5, 10 * 256);
		sheet.setColumnWidth(6, 14 * 256);
		sheet.setColumnWidth(7, 20 * 256);
		sheet.setColumnWidth(8, 20 * 256);
		sheet.setColumnWidth(9, 20 * 256);
		sheet.setColumnWidth(10, 12 * 256);
		sheet.setColumnWidth(11, 25 * 256);
		sheet.setColumnWidth(12, 20 * 256);
		sheet.setColumnWidth(13, 20 * 256);
		sheet.setColumnWidth(14, 20 * 256);
		
		// 第3.2步，创建单元格，并设置值
		for (int i = 0; i < thisList.size(); i++) {
			HSSFRow rows = sheet.createRow((int) (i + 1));
			Datasource thisDatasource = thisList.get(i);
			rows.createCell(0).setCellValue((i + 1));	// 设置第1列序号
			rows.createCell(1).setCellValue(thisDatasource.getTitle());
			HSSFCell cell2 = rows.createCell(2);
			if (StringUtils.isNotBlank(thisDatasource.getdType())) {
				switch (thisDatasource.getdType()) {
				case "1":
					cell2.setCellValue("数据库");
					break;
				case "2":
					cell2.setCellValue("网页");
					break;
				case "3":
					cell2.setCellValue("期刊专著");
					break;
				default:
					cell2.setCellValue("其他");
					break;
				}
			}
			
			rows.createCell(3).setCellValue(thisDatasource.getVersions());	
			rows.createCell(4).setCellValue(thisDatasource.getCreater());	
			rows.createCell(5).setCellValue(thisDatasource.getCreatetime());	
			rows.createCell(6).setCellValue(thisDatasource.getdAbstract());	
			rows.createCell(7).setCellValue(thisDatasource.getdKeyword());	
			rows.createCell(8).setCellValue(thisDatasource.getdLink());	
			rows.createCell(9).setCellValue("");	
			rows.createCell(10).setCellValue(thisDatasource.getdVerifier());	
			rows.createCell(11).setCellValue(thisDatasource.getdRightsholder());	
			rows.createCell(12).setCellValue(thisDatasource.getdCopyright());	
			rows.createCell(13).setCellValue(thisDatasource.getInfo());	
		}
		// 第4步，将文件存到浏览器设置的下载位置
		// 告诉浏览器用什么软件可以打开此文件
	    response.setHeader("content-Type", "application/vnd.ms-excel");
	    // 下载文件的默认名称
	 	response.setHeader("Content-Disposition", "attachment; filename=" + new String("数据源".getBytes("gb2312"), "iso8859-1")+".xls");
		OutputStream out = response.getOutputStream();
		try {
			wb.write(out);// 将数据写出去
		} catch (Exception e) {
		} finally {
			out.close();
		}
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
	public JSON uploadFile(MultipartFile file, HttpServletRequest request) throws Exception {
		JSONObject thisResult = new JSONObject();
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Datasource> dsList = new ArrayList<>();			// 上传文件的Datasource数据
        List<Baseinfotmp> baseInfoTmpList = new ArrayList<>();	// 临时表数据
        List<String> rowList = new ArrayList<>();				// 上传文件的序号，判断上传文件序号是否有重复
        String expFileMark = request.getParameter("expFileMark");			// 专家文件标记
        /*判断上传文件是否为空及是否是目标文件*/
        if (null == file || file.isEmpty()) {
            thisResult.put("message", "未找到上传的文件，请刷新页面或更换浏览器");
            thisResult.put("status", false);
            thisResult.put("code", -1);
            System.out.println("未找到上传的文件，请刷新页面或更换浏览器");
        }else if (StringUtils.isBlank(expFileMark)) {
			thisResult.put("message", "未找到本次上传的对应专家信息，请重新上传【1.专家信息】");
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
					/*String tableHeadArray[] = { "*序号", "*数据源名称", "数据源类型", "*版本", "创建者", "创建时间", "*数据源摘要", "关键字",
							"数据源链接", "参考文献", "*审核专家", "版权所有者", "版权声明", "说明/备注" };*/
					String tableHeadArray[] = { "序号", "数据源名称", "数据源类型", "版本", "创建者", "创建时间", "数据源摘要", "关键字",
							"数据源链接", "参考文献", "审核专家", "版权所有者", "版权声明", "说明/备注" };
                    if(this.excelService.judgeRowConsistent(14,tableHeadArray,sheet.getRow(0))){
                        //构造对照表（临时表）
                        try {
                            String dsFileMark = UUIDUtils.getUUID32();			// 文件标记
                            Datasource thisDatasource = new Datasource();		// Ref对象
                            Baseinfotmp thisBaseinfotmp = new Baseinfotmp();	// 临时表数据对象
                            Row row = null;										// 记录行
                            String serialNum = null;							// 记录行的序号
                            //4.遍历数据
                            rowList = this.excelService.getRowList(sheet, row);

                            JSONArray uniqSerialNum = null;
                            if (rowList.isEmpty()) {
                            	thisResult.put("message", "数据为空");
                                thisResult.put("status", false);
                                thisResult.put("code", -3);
                                System.out.println("数据为空");
                            }else {
                                uniqSerialNum = excelService.uniqSerialNum(rowList);
                                if (uniqSerialNum.size()<=0) {
                                    for (int i = 1; i <= rowNum; i++) {
                                        String uuid32 = UUIDUtils.getUUID32();		// dsId
                                        row = sheet.getRow(i);
                                        if (null == sheet.getRow(i)) { 				// 空行
                                        	continue; 
                                        }	
                                        serialNum = (excelService.getStringValueFromCell(row.getCell(0)));	
                                        //构造临时表对象
                                        thisBaseinfotmp = new Baseinfotmp(
                                                UUIDUtils.getUUID32(),
                                                uuid32,
                                                serialNum,
                                                dsFileMark,
                                                1,
                                                thisUser.getId(),
                                                new Timestamp(System.currentTimeMillis()));
                                        baseInfoTmpList.add(thisBaseinfotmp);
                                        //构建参考文献对象
                                        thisDatasource = new Datasource(
                                        		uuid32,
                                                excelService.getStringValueFromCell(row.getCell(1)),
                                                excelService.getStringValueFromCell(row.getCell(2)),
                                                excelService.getStringValueFromCell(row.getCell(3)),
                                                excelService.getStringValueFromCell(row.getCell(4)),
                                                excelService.getStringValueFromCell(row.getCell(5)),
                                                excelService.getStringValueFromCell(row.getCell(6)),
                                                excelService.getStringValueFromCell(row.getCell(7)),
                                                excelService.getStringValueFromCell(row.getCell(8)),
                                                excelService.getStringValueFromCell(row.getCell(10)),
                                                excelService.getStringValueFromCell(row.getCell(11)),
                                                excelService.getStringValueFromCell(row.getCell(12)),
                                                excelService.getStringValueFromCell(row.getCell(13)));
                                        dsList.add(thisDatasource);
                                    }
                                    long start = System.currentTimeMillis();
                                    JSONArray verifyListEntity = verifyListEntity(dsList, baseInfoTmpList, thisUser, expFileMark);
                                    long end = System.currentTimeMillis();
                                    System.out.println("校验完成：" + (end - start));
                                    if (verifyListEntity.size() > 0) {
                                    	thisResult.put("message", "录入数据异常，必填字段不能为空");
                                        thisResult.put("status", false);
                                        thisResult.put("code", -4);
                                        //处理必填字段未写table的json
                                        thisResult.put("errorData", verifyListEntity);
									}else {
										thisResult.put("message", "上传成功");
                                        thisResult.put("status", true);
                                        thisResult.put("code", 1);
										thisResult.put("dsFileMark", dsFileMark);
									}
                                }else {
                                	thisResult.put("message", "未通过校验，上传文件存在重复序号，见下表，请处理完毕后重新上传");
                                    thisResult.put("status", false);
                                    thisResult.put("code", -5);
                                    //处理重复table的json
                                    JSONArray errorTable=new JSONArray();
                                    try{
                                        for(int i=0;i<uniqSerialNum.size();i++){
                                            JSONObject thisUniqData=new JSONObject();
                                            thisUniqData.put("num","["+uniqSerialNum.getJSONObject(i).get("rowNum")+"]");
                                            thisUniqData.put("row",uniqSerialNum.getJSONObject(i).getInteger("rowIndex")+2);
                                            thisUniqData.put("title",excelService.getStringValueFromCell(sheet.getRow(uniqSerialNum.getJSONObject(i).getInteger("rowIndex")+1).getCell(2)));
                                            errorTable.add(thisUniqData);
                                        }
                                    }
                                    catch (Exception e){
                                    	e.printStackTrace();
                                    }
                                    thisResult.put("errorTable", errorTable);
                                    System.out.println("解析失败，上传文件存在重复序号：" + uniqSerialNum);
                                }
                            }

                        } catch (Exception e) {
                        	 e.printStackTrace();
                             thisResult.put("message", "判断重复序号错误，疑似数据类型错误");
                             thisResult.put("status", false);
                             thisResult.put("code", -6);
                             System.out.println("判断重复序号错误，疑似数据类型错误");
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

	private JSONArray verifyListEntity(List<Datasource> list, List<Baseinfotmp> baseInfoTmpList, UserDetail thisUser, String expFileMark) throws Exception {
    	JSONArray dsArr = new JSONArray();
    	List<Datasource> failList = new ArrayList<>();
        boolean flag = true;
        for (int i = 0; i < list.size(); i++) {
        	boolean mark = true;
        	Datasource thisDatasource = list.get(i);
            if (StringUtils.isBlank(thisDatasource.getTitle())) {
            	thisDatasource.setTitle("<span style='color:red'>数据源名称不能为空</span>");
            	mark = false;
            };
            if (StringUtils.isBlank(thisDatasource.getVersions())) {
            	thisDatasource.setVersions("<span style='color:red'>版本不能为空</span>");
            	mark = false;
            };
            if (StringUtils.isBlank(thisDatasource.getdAbstract())) {
            	thisDatasource.setdAbstract("<span style='color:red'>数据源摘要不能为空</span>");
            	mark = false;
            };
            
            if (StringUtils.isBlank(thisDatasource.getdVerifier())) {
            	thisDatasource.setdVerifier("<span style='color:red'>专家信息不能为空</span>");
            	mark = false;
            }else{
            	boolean tmp = thisDatasource.getdVerifier().matches("[0-9]*");
            	String expid = null;
            	if (tmp) {
            		expid = this.baseinfotmpService.findExpIdByFilemarkAndSerialNumAndFileType(expFileMark, 2, thisDatasource.getdVerifier());
				}else {
					String cnName = this.expertRepository.findIdByCnName(thisDatasource.getdVerifier());
					if (StringUtils.isNotBlank(cnName)) {
						expid = cnName;
					}else {
						thisDatasource.setdVerifier("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
					}
				}
            	
            	if (StringUtils.isNotBlank(expid)) {
            		thisDatasource.setdVerifier(expid);
				}else{
					thisDatasource.setdVerifier("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
					mark = false;
				}
            };
            
            if (!mark) {
            	thisDatasource.setSynchstatus(i + 1);
            	failList.add(thisDatasource);
            	flag = mark;
			}
        }
        /* 构建的集合若通过必填字段的校验，去重，补充属性值*/
        if (flag) {
        	List<Datasource> datasourceList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
            	String dtype = list.get(i).getdType();
            	if (StringUtils.isNotBlank(dtype)) {
            		switch (dtype) {
            		case "数据库":
            			list.get(i).setdType("1");
            			break;
            		case "网页":
            			list.get(i).setdType("2");
            			break;
            		case "期刊专著":
            			list.get(i).setdType("3");
            			break;
            		default:
            			list.get(i).setdType("4");
            			break;
            		}
				}
            	
            	Datasource datasource = this.datasourceRepository.findDsIdByTitleAndVersion(list.get(i).getTitle(), list.get(i).getVersions(), thisUser.getId());
            	if (null != datasource) {	//数据重复
            		baseInfoTmpList.get(i).setRefDsId(datasource.getId());
				}else {
					list.get(i).setInputer(thisUser.getId());
					list.get(i).setInputtime(new Timestamp(System.currentTimeMillis()));
					list.get(i).setSynchdate(new Timestamp(System.currentTimeMillis()));
					list.get(i).setStatus(1);
					list.get(i).setSynchstatus(0);
        			/*this.datasourceRepository.save(list.get(i));*/
					datasourceList.add(list.get(i));
				}
            }
            
            long start = System.currentTimeMillis();
            this.batchInsertService.batchInsertDatasource(datasourceList);
            this.batchInsertService.batchInsertBaseinfotmps(baseInfoTmpList);
            long end = System.currentTimeMillis();
            System.out.println("Datasource批量存储完成：" + (end - start));
        }else {
			for (int i = 0; i < failList.size(); i++) {
				JSONObject json = new JSONObject();
				json.put("num", failList.get(i).getSynchstatus());
				json.put("title", failList.get(i).getTitle());
				json.put("version", failList.get(i).getVersions());
				json.put("dAbstract", failList.get(i).getdAbstract());
				json.put("dVerifier", failList.get(i).getdVerifier());
				dsArr.add(i, json);
			}
		}
        return dsArr;
    }

	/**
	 * <b>封装参考文献列表的JSON数据</b>
	 * <p> 封装参考文献列表的JSON数据</p>
	 * @param thisList
	 * @return
	 */
	private JSONArray findDatasourceListForTable(List<Datasource> thisList) {
		JSONArray rows = new JSONArray();
		String thisSelect;
		String thisEdit;
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
	        thisSelect="<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
	        thisEdit=
	        	 "<a class=\"wts-table-edit-icon\" onclick=\"editThisObject('" + thisList.get(i).getId() + "','datasource')\" >" +
	             "<span class=\"glyphicon glyphicon-edit\"></span>" +
	             "</a>&nbsp;&nbsp;" +
	             "<a class=\"hidden wts-table-edit-icon\" onclick=\"removeThisObject('" + thisList.get(i).getId() + "','datasource')\" >" +
	             "<span class=\"glyphicon glyphicon-remove\"></span>" +
	             "</a>";
			row.put("select", thisSelect);
			row.put("title", thisList.get(i).getTitle());
			String type = thisList.get(i).getdType();
			if (StringUtils.isNotBlank(type)) {
				switch (type) {
				case "1":
					row.put("dType", "数据库");
					break;
				case "2":
					row.put("dType", "网页");
					break;
				case "3":
					row.put("dType", "期刊专著");
					break;
				default:
					row.put("dType", "其他");
					break;
				}
			}
			row.put("version", thisList.get(i).getVersions());
			row.put("inputer", this.userService.findbyID(thisList.get(i).getInputer()).getNickname());
			row.put("inputtime", thisList.get(i).getInputtime());
			row.put("creater", thisList.get(i).getCreater());
			row.put("createtime", thisList.get(i).getCreatetime());
			row.put("dAbstract", thisList.get(i).getdAbstract());
			row.put("dKeyword", thisList.get(i).getdKeyword());
			row.put("dLink", thisList.get(i).getdLink());
			
			String verifier = thisList.get(i).getdVerifier();
			Expert thisExpert = this.expertRepository.findOneById(verifier);
			if (null != thisExpert) {
				row.put("dVerifier", "<a href=\"" + thisExpert.getCnHomePage() + "\">" + thisExpert.getCnName() + "</a>");
			}
			row.put("dRightsholder", thisList.get(i).getdRightsholder());
			row.put("dCopyright", thisList.get(i).getdCopyright());
			row.put("info", thisList.get(i).getInfo());
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
			row.put("versions", thisList.get(i).getVersions());
			row.put("edit", thisEdit);
			rows.add(i, row);
		}
		return rows;
	}

	@Override
	public JSON findUploadedDatasourceList(String dsFileMark, HttpServletRequest request) {
		String searchText = request.getParameter("search");
        if (searchText == null || searchText.length() <= 0) {
            searchText = "";
        }
        int limit_serch = Integer.parseInt(request.getParameter("limit"));
        int offset_serch = Integer.parseInt(request.getParameter("offset"));
        String sort=request.getParameter("sort");
        String order=request.getParameter("order");
        if(StringUtils.isBlank(sort)){
            sort = "b.serialNum";
            order = "asc";
        }
        JSONObject thisTable = new JSONObject();
        List<Object> thisList = new ArrayList<>();
        UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Object> thisPage = this.datasourceRepository.findUploadedDatasourceList(dsFileMark, searchText, thisUser.getId(),
                QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
        thisTable.put("total", thisPage.getTotalElements());
        thisList = thisPage.getContent();
        JSONArray rows = new JSONArray();
        for(int i=0;i<thisList.size();i++){
            JSONObject row= new JSONObject();
            UploadedDatasource thisUploadedDatasource = BuildEntity.buildUploadedDatasource(thisList.get(i));
            row.put("b.serialNum","[" + thisUploadedDatasource.getNum() + "]");
            row.put("title", thisUploadedDatasource.getTitle());
            row.put("version", thisUploadedDatasource.getTitle());
            row.put("creater", thisUploadedDatasource.getCreater());
            row.put("createtime", thisUploadedDatasource.getCreatetime());
            row.put("dAbstract", thisUploadedDatasource.getdAbstract());
            row.put("dKeyword", thisUploadedDatasource.getdKeyword());
            row.put("dLink", thisUploadedDatasource.getdLink());
            
            try {
				String verifier = thisUploadedDatasource.getdVerifier();
				Expert thisExpert = this.expertRepository.findOneById(verifier);
				row.put("dVerifier", thisExpert.getCnName() + "(" + thisExpert.getExpEmail() + ")");
			} catch (Exception e) {
				row.put("dVerifier", "无审核人");
			}
            
            row.put("info", thisUploadedDatasource.getInfo());

            String dtype = thisUploadedDatasource.getdType();
            if (StringUtils.isNotBlank(dtype)) {
            	switch (dtype) {
                case "1":
                    row.put("dType", "数据库");
                    break;
                case "2":
                    row.put("dType", "网页");
                    break;
                case "3":
                    row.put("dType", "期刊专著");
                    break;
                default:
                    row.put("dType", "其他");
                    break;
            }
			}else{
                
            }
            row.put("inputer", this.userService.findbyID(thisUploadedDatasource.getInputer()).getNickname());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String addTime = "";
            try {
                addTime = formatter.format(thisUploadedDatasource.getInputtime());
            } catch (Exception e) {
            }
            row.put("inputtime", addTime);
            rows.add(i,row);
        }
        thisTable.put("rows",rows);
        return thisTable;
    }
}
