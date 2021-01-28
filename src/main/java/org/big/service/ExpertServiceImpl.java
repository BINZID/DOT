package org.big.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import org.big.sp2000.entity.Specialist;
import org.big.common.BuildEntity;
import org.big.common.ExcelUtil;
import org.big.common.QueryTool;
import org.big.common.UUIDUtils;
import org.big.entity.Baseinfotmp;
import org.big.entity.Expert;
import org.big.entity.UploadedExpert;
import org.big.entity.UserDetail;
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
public class ExpertServiceImpl implements ExpertService {
	@Autowired
	private ExpertRepository expertRepository;
	@Autowired
	private ExcelService excelService;
	@Autowired
	private BatchInsertService batchInsertService;
	@Override
	public JSON findExpertList(HttpServletRequest request) {
        String searchText=request.getParameter("search");
        if(searchText==null || searchText.length()<=0){
            searchText="";
        }
        int limit_serch=Integer.parseInt(request.getParameter("limit"));
        int offset_serch=Integer.parseInt(request.getParameter("offset"));
		String sort=request.getParameter("sort");
		String order=request.getParameter("order");
		if(StringUtils.isBlank(sort)){
			sort = "cnName";
			order = "asc";
		}
		
        JSONObject thisTable= new JSONObject();
        JSONArray rows = new JSONArray();
        List<Expert> thisList=new ArrayList<>();

        Page<Expert> thisPage = this.expertRepository.findExpertList(searchText, QueryTool.buildPageRequest(offset_serch,limit_serch,sort,order));
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
            
            row.put("select",thisSelect);
            row.put("cnName","<a href=\"" + thisList.get(i).getCnHomePage() + "\" target=\"_blank\">" + thisList.get(i).getCnName() + "</a>");
            row.put("enName",thisList.get(i).getEnName());
            row.put("expEmail",thisList.get(i).getExpEmail());
            row.put("cnCompany",thisList.get(i).getCnCompany());
            row.put("enCompany",thisList.get(i).getEnCompany());
            row.put("cnAddress",thisList.get(i).getCnAddress());
            row.put("enAddress",thisList.get(i).getEnAddress());
            row.put("cnHomePage","<a href=\"" + thisList.get(i).getCnHomePage() + "\" target=\"_blank\">" + thisList.get(i).getCnHomePage() + "</a>");
            row.put("enHomePage","<a href=\"" + thisList.get(i).getEnHomePage() + "\" target=\"_blank\">" + thisList.get(i).getEnHomePage() + "</a>");
            row.put("expInfo",thisList.get(i).getExpInfo());
            row.put("edit",thisEdit);
            rows.add(i,row);
        }
        thisTable.put("rows",rows);
        return thisTable;
    }

	@Override
	public JSON findUploadedExpertList(HttpServletRequest request, String fileMark) {
		String searchText=request.getParameter("search");
		if(searchText==null || searchText.length()<=0){
			searchText="";
		}
		int limit_serch=Integer.parseInt(request.getParameter("limit"));
		int offset_serch=Integer.parseInt(request.getParameter("offset"));
		String sort=request.getParameter("sort");
		String order=request.getParameter("order");
		if(StringUtils.isBlank(sort)){
			sort = "b.serialNum";
			order = "asc";
		}

		JSONObject thisTable= new JSONObject();
		JSONArray rows = new JSONArray();
		List<Object> thisList=new ArrayList<>();

		Page<Object> thisPage = this.expertRepository.findUploadedExpertList(fileMark,searchText, QueryTool.buildPageRequest(offset_serch,limit_serch,sort,order));

		thisTable.put("total",thisPage.getTotalElements());
		thisList=thisPage.getContent();
		for(int i=0;i<thisList.size();i++){
			JSONObject row= new JSONObject();
			UploadedExpert thisUploadedExpert = BuildEntity.buildUploadedExpert(thisList.get(i));
			row.put("b.serialNum","[" + thisUploadedExpert.getNum() + "]");
			row.put("cnName","<a href=\"" + thisUploadedExpert.getCnHomePage() + "\" target=\"_blank\">" + thisUploadedExpert.getCnName() + "</a>");
			row.put("enName",thisUploadedExpert.getEnName());
			row.put("expEmail",thisUploadedExpert.getExpEmail());
			row.put("cnCompany",thisUploadedExpert.getCnCompany());
			row.put("enCompany",thisUploadedExpert.getEnCompany());
			row.put("cnAddress",thisUploadedExpert.getCnAddress());
			row.put("enAddress",thisUploadedExpert.getEnAddress());
			row.put("cnHomePage","<a href=\"" + thisUploadedExpert.getCnHomePage() + "\" target=\"_blank\">" + thisUploadedExpert.getCnHomePage() + "</a>");
			row.put("enHomePage","<a href=\"" + thisUploadedExpert.getEnHomePage() + "\" target=\"_blank\">" + thisUploadedExpert.getEnHomePage() + "</a>");
			row.put("expInfo",thisUploadedExpert.getExpInfo());
			rows.add(i,row);
		}
		thisTable.put("rows",rows);
		return thisTable;
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
		String sort = "expEmail";
		String order = "desc";
		
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		List<Expert> thisList = new ArrayList<>();
		Page<Expert> thisPage = this.expertRepository.searchByInfo(findText, 
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
			row.put("text", thisList.get(i).getCnName() + "<" + thisList.get(i).getExpEmail() + ">");
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public void export(HttpServletResponse response) throws IOException {
		
		String[] columnName = {"*序号", "*专家姓名", "专家英文名", "*单位名称", "单位英文名称", "*单位地址", "单位地址（英文）", "*电子邮件", "*专家主页", "专家主页（英文）", "专家简介"};

		List<Expert> thisList = this.expertRepository.findAll();
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("专家信息");
		
		sheet.setColumnWidth(1, 12 * 256);
		sheet.setColumnWidth(2, 15 * 256);
		sheet.setColumnWidth(3, 18 * 256);
		sheet.setColumnWidth(4, 20 * 256);
		sheet.setColumnWidth(5, 20 * 256);
		sheet.setColumnWidth(6, 20 * 256);
		sheet.setColumnWidth(7, 20 * 256);
		sheet.setColumnWidth(8, 40 * 256);
		sheet.setColumnWidth(9, 20 * 256);
		sheet.setColumnWidth(10, 65 * 256);
		
		HSSFRow title = sheet.createRow(0);
		title.setHeightInPoints(20);
		for (int i = 0; i < columnName.length; i++) {
			HSSFCell cell = title.createCell(i);
			cell.setCellValue(columnName[i]);
			cell.setCellStyle(ExcelUtil.setTitleStyle(wb));
		}
		
		for (int i = 0; i < thisList.size(); i++) {
			HSSFRow rows = sheet.createRow(i + 1);
			rows.createCell(0).setCellValue((i + 1));
			rows.createCell(1).setCellValue(thisList.get(i).getCnName());
			rows.createCell(2).setCellValue(thisList.get(i).getEnName());
			rows.createCell(3).setCellValue(thisList.get(i).getCnCompany());
			rows.createCell(4).setCellValue(thisList.get(i).getEnCompany());
			rows.createCell(5).setCellValue(thisList.get(i).getCnAddress());
			rows.createCell(6).setCellValue(thisList.get(i).getEnAddress());
			rows.createCell(7).setCellValue(thisList.get(i).getExpEmail());
			rows.createCell(8).setCellValue(thisList.get(i).getCnHomePage());
			rows.createCell(9).setCellValue(thisList.get(i).getEnHomePage());
			rows.createCell(10).setCellValue(thisList.get(i).getExpInfo());
		}
		
		response.setHeader("content-Type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=" + new String("专家信息".getBytes("gb2312"), "iso8859-1") + ".xls");
		OutputStream out = response.getOutputStream();
		try {
			wb.write(out);
		} catch (Exception e) {
		} finally {
			out.close();
		}
	}

	@Override
	public Expert findOneById(String id) {
		return this.expertRepository.findOneById(id);
	}

	@Override
	public JSON uploadFile(MultipartFile file, HttpServletRequest request) throws Exception {
		JSONObject thisResult = new JSONObject();
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Expert> expList = new ArrayList<>();				// 上传文件的Expert数据
        List<Baseinfotmp> baseInfoTmpList = new ArrayList<>();	// 临时表数据
        List<String> rowList = new ArrayList<>();				// 上传文件的序号，判断上传文件序号是否有重复

        /*判断上传文件是否为空及是否是目标文件*/
        if (null == file || file.isEmpty()) {
            thisResult.put("message", "未找到上传的文件，请刷新页面或更换浏览器");
            thisResult.put("status", false);
            thisResult.put("code", -1);
            System.out.println("未找到上传的文件，请刷新页面或更换浏览器");
        }else {
        	//先解析EXCEL
        	InputStream in = file.getInputStream();
            try {
            	Workbook wb =  WorkbookFactory.create(in);
            	Sheet sheet = wb.getSheetAt(0);
            	int rowNum = sheet.getLastRowNum();
                //判断表头是否符合规则
                try {
					/*String tableHeadArray[] = { "*序号", "*专家姓名", "专家英文名", "*单位名称", "单位英文名称", "*单位地址", "单位地址（英文）", "*电子邮件",
							"*专家主页", "专家主页（英文）", "专家简介" };*/
                	String tableHeadArray[] = { "序号", "专家姓名", "专家英文名", "单位名称", "单位英文名称", "*单位地址", "单位地址（英文）", "电子邮件",
							"专家主页", "专家主页（英文）", "专家简介" };
                    if(this.excelService.judgeRowConsistent(11,tableHeadArray,sheet.getRow(0))){
                        //构造对照表（临时表）
                        try {
                            String expFileMark = UUIDUtils.getUUID32();			// 文件标记
                            Expert thisExpert = new Expert();					// Expert对象
                            Baseinfotmp thisBaseinfotmp = new Baseinfotmp();	// 临时表数据对象
                            Row row = null;										// 记录行
                            String serialNum = null;								// 记录行的序号
                            
                            //4.遍历数据
                            rowList = this.excelService.getRowList(sheet, row);

                            JSONArray uniqSerialNum = new JSONArray();
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
                                        if (null == sheet.getRow(i)) { continue; }	// 空行
                                        serialNum = (excelService.getStringValueFromCell(row.getCell(0)));	
                                        //构造临时表对象
                                        thisBaseinfotmp = new Baseinfotmp(
                                                UUIDUtils.getUUID32(),
                                                uuid32,
                                                serialNum,
                                                expFileMark,
                                                2,
                                                thisUser.getId(),
                                                new Timestamp(System.currentTimeMillis()));
                                        baseInfoTmpList.add(thisBaseinfotmp);
                                        //构建参考文献对象
                                        thisExpert = new Expert(
                                        		uuid32,
                                        		excelService.getStringValueFromCell(row.getCell(1)),
                                    			excelService.getStringValueFromCell(row.getCell(2)),
                                    			excelService.getStringValueFromCell(row.getCell(3)),
                                    			excelService.getStringValueFromCell(row.getCell(4)),
                                    			excelService.getStringValueFromCell(row.getCell(5)),
                                    			excelService.getStringValueFromCell(row.getCell(6)),
                                    			excelService.getStringValueFromCell(row.getCell(7)),
                                    			excelService.getStringValueFromCell(row.getCell(8)),
                                    			excelService.getStringValueFromCell(row.getCell(9)),
                                    			excelService.getStringValueFromCell(row.getCell(10)));
                                        expList.add(thisExpert);
                                    }
                                    long start = System.currentTimeMillis();
                                    JSONArray verifyListEntity = verifyListEntity(expList, baseInfoTmpList, thisUser);
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
										thisResult.put("expFileMark", expFileMark);
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
                                            thisUniqData.put("title",excelService.getStringValueFromCell(sheet.getRow(uniqSerialNum.getJSONObject(i).getInteger("rowIndex")+1).getCell(1)));
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

		/**
	     * <b>校验数据是否有空值或有重复并为实体初始化唯一标识及部分属性值</b>
	     * <p> 校验数据是否有空值或有重复并为实体初始化唯一标识及部分属性值</p>
	     * @author BINZI
	     * @param list
	     * @return
		 * @throws Exception 
	     */
	private JSONArray verifyListEntity(List<Expert> list, List<Baseinfotmp> baseInfoTmpList, UserDetail thisUser) throws Exception {
    	JSONArray expArr = new JSONArray();
    	List<Expert> failList = new ArrayList<>();
    	Expert thisExpert = new Expert();
        boolean flag = true;
        for (int i = 0; i < list.size(); i++) {
        	boolean mark = true;
        	thisExpert = list.get(i);
        	
        	if (StringUtils.isBlank(thisExpert.getCnName())) {
            	thisExpert.setCnName("<span style='color:red'>专家姓名不能为空</span> ");
            	mark = false;
            };
            if (StringUtils.isBlank(thisExpert.getCnCompany())) {
            	thisExpert.setCnCompany("<span style='color:red'>单位名称不能为空</span>");
            	mark = false;
            };
            if (StringUtils.isBlank(thisExpert.getCnAddress())) {
            	thisExpert.setCnAddress("<span style='color:red'>单位地址不能为空</span>");
            	mark = false;
            };
            if (StringUtils.isBlank(thisExpert.getCnHomePage())) {
            	thisExpert.setCnHomePage("<span style='color:red'>专家主页不能为空</span>");
            	mark = false;
            };
            if (StringUtils.isBlank(thisExpert.getExpEmail())) {
            	thisExpert.setExpEmail("<span style='color:red'>邮箱不能为空</span>");
            	mark = false;
            };
            if (!mark) {
            	thisExpert.setSynchstatus(i + 1);
            	failList.add(thisExpert);
            	flag = mark;
			}
        }
        
        /* 构建的集合若通过必填字段的校验，去重，补充属性值*/
        if (flag) {
        	List<Expert> expertList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
            	/* 根据专家姓名 + 邮箱去重 不存在不新建 -- 用户导*/
            	String expertId = this.expertRepository.findIdsByCnNameAndExpEmail(list.get(i).getCnName(),list.get(i).getExpEmail());
            	System.out.println(list.get(i).getCnName() + "\t" + list.get(i).getExpEmail() + "\t" + expertId);
				if(StringUtils.isNotBlank(expertId)){	//有重复
					baseInfoTmpList.get(i).setRefDsId(expertId);
				}else{
					list.get(i).setId(baseInfoTmpList.get(i).getRefDsId());
					list.get(i).setInputer(thisUser.getId());
					list.get(i).setInputtime(new Timestamp(System.currentTimeMillis()));
					list.get(i).setSynchdate(new Timestamp(System.currentTimeMillis()));
					list.get(i).setStatus(1);
					list.get(i).setSynchstatus(0);
					/*this.expertRepository.save(list.get(i));*/
					expertList.add(list.get(i));
				}
            }
            long start = System.currentTimeMillis();
            this.batchInsertService.batchInsertExpert(expertList);
            this.batchInsertService.batchInsertBaseinfotmps(baseInfoTmpList);
            long end = System.currentTimeMillis();
            System.out.println("Expert批量存储完成：" + (end - start));
        }else {
			for (int i = 0; i < failList.size(); i++) {
				JSONObject json = new JSONObject();
				json.put("num", failList.get(i).getSynchstatus());
				json.put("cnName", failList.get(i).getCnName());
				json.put("cnCompany", failList.get(i).getCnCompany());
				json.put("cnAddress", failList.get(i).getCnAddress());
				json.put("cnHomePage", failList.get(i).getCnHomePage());
				json.put("expEmail", failList.get(i).getExpEmail());
				expArr.add(i, json);
			}
		}
        return expArr;
    }
		@Override
		public JSON findUploadList(HttpServletRequest request) {
			return null;
		}

		@Override
		public String findOneByExpertInfo(String expertInfo) {
			String rsl = null;
			String expertId = null;
			if (expertInfo.contains("@")) {
				/* 找出指定的2个字符在 该字符串里面的 位置 */
		        int strStartIndex = expertInfo.indexOf("<");
		        int strEndIndex = expertInfo.indexOf(">");
		        /* 开始截取 */
		        String cnName = expertInfo.substring(0, strStartIndex);
		        String expEmail = expertInfo.substring(strStartIndex + 1, strEndIndex);
		        expertId = this.expertRepository.findIdsByCnNameAndExpEmail(cnName, expEmail);
			}else {
				expertId = this.expertRepository.findIdByCnName(expertInfo);
			}
			
			if (StringUtils.isBlank(expertId)) {
				return rsl;
        	}else {
				return rsl = expertId;
			}
	    }

		@Override
		public List<Specialist> getExpertInFo() {
			List<Expert> refslist = expertRepository.findAll();
			List<Specialist> resultlist = new ArrayList<>();
			for (Expert expert : refslist) {
				Specialist obj = new Specialist();
				obj.setRecordId(expert.getId());
				obj.setAddress(expert.getEnAddress());
				obj.setAddressChinese(expert.getCnAddress());
				obj.setDatabaseId(null);
				obj.setEmail(expert.getExpEmail());
				obj.setHomepage(expert.getEnHomePage());
				obj.setHomepageChinese(expert.getCnHomePage());
				obj.setInstitute(expert.getEnCompany());
				obj.setInstituteChinese(expert.getCnCompany());
				obj.setSpecialistName(expert.getEnName());
				obj.setSpecialistNameChinese(expert.getCnName());
				resultlist.add(obj);
			}
			return resultlist;
		}
}
