package org.big.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.big.sp2000.entity.Reference;
import org.big.sp2000.entity.ReferenceLink;
import org.big.common.BuildEntity;
import org.big.common.ExcelUtil;
import org.big.common.QueryTool;
import org.big.common.UUIDUtils;
import org.big.entity.*;
import org.big.repository.CitationRepository;
import org.big.repository.CommonnameRepository;
import org.big.repository.DistributiondataRepository;
import org.big.repository.RefRepository;
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
public class RefServiceImpl implements RefService {
    @Autowired
    private RefRepository refRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private ExcelService excelService;
    @Autowired
    private BatchInsertService batchInsertService;
    @Autowired
	private TaxonRepository taxonRepository;
    @Autowired
	private CitationRepository citationRepository;
    @Autowired
	private DistributiondataRepository distributiondataRepository;
    @Autowired
	private CommonnameRepository commonnameRepository;
    @Override
    public JSON findRefList(HttpServletRequest request) {
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
        List<Ref> thisList = new ArrayList<>();
        UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Ref> thisPage = this.refRepository.searchInfo(searchText,
                QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), thisUser.getId());
        thisTable.put("total", thisPage.getTotalElements());
        thisList = thisPage.getContent();
        thisTable.put("rows", findRefListForTable(thisList));
        return thisTable;
    }

    @Override
    public JSON findUploadedRefList(String refFileMark,HttpServletRequest request) {
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
        Page<Object> thisPage = this.refRepository.findUploadedRefList(refFileMark,searchText, thisUser.getId(),
                QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
        thisTable.put("total", thisPage.getTotalElements());
        thisList = thisPage.getContent();
        JSONArray rows = new JSONArray();
        for(int i=0;i<thisList.size();i++){
            JSONObject row= new JSONObject();
            UploadedRef thisUploadedRef = BuildEntity.buildUploadedRef(thisList.get(i));
            row.put("b.serialNum","[" + thisUploadedRef.getNum() + "]");
            row.put("refstr", thisUploadedRef.getRefstr());
            row.put("author", thisUploadedRef.getAuthor());
            row.put("title", thisUploadedRef.getTitle());
            row.put("pyear", thisUploadedRef.getPyear());
            row.put("journal", thisUploadedRef.getJournal());
            row.put("volume", thisUploadedRef.getrVolume());
            row.put("period", thisUploadedRef.getrPeriod());

            String ptype = thisUploadedRef.getPtype();
            if(ptype==null){
                row.put("ptype", "未指定");
            }
            else{
                switch (thisUploadedRef.getPtype()) {
                    case "1":
                        row.put("ptype", "期刊[J]");
                        break;
                    case "2":
                        row.put("ptype", "专著[M]");
                        break;
                    case "3":
                        row.put("ptype", "论文集[C]");
                        break;
                    default:
                        row.put("ptype", "其他");
                        break;
                }
            }
            row.put("press", thisUploadedRef.getPress());
            row.put("version", thisUploadedRef.getVersion());
            row.put("inputer", this.userService.findbyID(thisUploadedRef.getInputer()).getNickname());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String addTime = "";
            String editTime = "";
            try {
                addTime = formatter.format(thisUploadedRef.getInputtime());
                editTime = formatter.format(thisUploadedRef.getSynchdate());
            } catch (Exception e) {
            }
            row.put("inputtime", addTime);
            row.put("synchdate", editTime);
            rows.add(i,row);
        }
        thisTable.put("rows",rows);
        return thisTable;
    }

    @Override
    public Ref findOneById(String id) {
        return this.refRepository.findOneById(id);
    }

    @Override
    public void saveOne(@Valid Ref thisRef, HttpServletRequest request) {
        thisRef.setId(UUID.randomUUID().toString());
        UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        thisRef.setInputer(thisUser.getId());
        thisRef.setSynchdate(new Timestamp(System.currentTimeMillis()));
        thisRef.setSynchstatus(0);
        thisRef.setStatus(1);
        
        this.refRepository.save(thisRef);
    }

    @Override
    public void removeOne(String Id) {
        this.refRepository.deleteOneById(Id);
    }

    @Override
    public boolean logicRemove(String id) {
        Ref thisRef = this.refRepository.findOneById(id);
        if (null != thisRef && 1 == thisRef.getStatus()) {
            thisRef.setStatus(0);
            this.refRepository.save(thisRef);
            return true;
        }
        return false;
    }

    @Override
    public void updateOneById(@Valid Ref thisRef) {
        thisRef.setSynchdate(new Timestamp(System.currentTimeMillis()));
        this.refRepository.save(thisRef);
    }

    @Override
    public JSON findBySelect(HttpServletRequest request) {
    	String searchFiled = request.getParameter("searchFiled");
    	String searchContent = request.getParameter("searchContent");
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
        String sort = "pyear";
        String order = "desc";
        
        JSONObject thisSelect = new JSONObject();
        JSONArray items = new JSONArray();
        List<Ref> thisList = new ArrayList<>();
       
        Team thisTeam = (Team) request.getSession().getAttribute("team");
        Page<Ref> thisPage = null;
        
        if (StringUtils.isNotBlank(searchFiled) && StringUtils.isNotBlank(searchContent)) {
			switch (searchFiled) {
			case "refstr":
				thisPage = this.refRepository.searchByRefstrAndRefstr(findText,
		        		QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), thisTeam.getId(), searchContent);
				break;
			case "author":
				thisPage = this.refRepository.searchByRefstrAndAuthor(findText,
		        		QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), thisTeam.getId(), searchContent);		
				break;
			case "pyear":
				thisPage = this.refRepository.searchByRefstrAndPyear(findText,
		        		QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), thisTeam.getId(), searchContent);
				break;
			case "title":
				thisPage = this.refRepository.searchByRefstrAndTitle(findText,
		        		QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), thisTeam.getId(), searchContent);
				break;
			default:
				break;
			}
		}else{
			thisPage = this.refRepository.searchByRefstr(findText,
					QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), thisTeam.getId());
		}
        
        thisSelect.put("total_count", thisPage.getTotalElements());
        Boolean incompleteResulte = true;
        if ((thisPage.getTotalElements() / 30) > findPage) {
            incompleteResulte = false;
        }
        thisSelect.put("incompleteResulte", incompleteResulte);
        thisList = thisPage.getContent();
        if (findPage == 1) {
            JSONObject row = new JSONObject();
            row.put("id", "addNew");
            row.put("text", "新建参考文献");
            items.add(row);
        }
        for (int i = 0; i < thisList.size(); i++) {
            JSONObject row = new JSONObject();
            row.put("id", thisList.get(i).getId());
            row.put("text", thisList.get(i).getRefstr());
            items.add(row);
        }
        thisSelect.put("items", items);
        return thisSelect;
    }

    @Override
    public JSON newOne(@Valid Ref thisRef, HttpServletRequest request) {
        JSONObject thisResult = new JSONObject();
        try {
            String id = UUID.randomUUID().toString();
            thisRef.setId(id);
            UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            thisRef.setInputer(thisUser.getId());
            Date inputtime = thisRef.getInputtime();
            if (null != inputtime) {
                thisRef.setInputtime((Timestamp)inputtime);
            }
            thisRef.setSynchdate(new Timestamp(System.currentTimeMillis()));
            thisRef.setSynchstatus(0);
            thisRef.setStatus(1);

            this.refRepository.save(thisRef);

            thisResult.put("result", true);
            thisResult.put("newId", this.refRepository.findOneById(id).getId());
            thisResult.put("newTitle", this.refRepository.findOneById(id).getTitle());
        } catch (Exception e) {
            thisResult.put("result", false);
        }
        return thisResult;
    }

    @Override
    public JSONArray buildRef(HttpServletRequest request) {
        JSONArray refs = new JSONArray();
        JSONArray refList=JSONArray.parseArray(request.getParameter("refjson"));
        Ref thisRef = null;
        JSONObject ref = null;
        try {
            for(int i=0;i<refList.size();i++){
                thisRef = refRepository.findOneById(refList.getJSONObject(i).getString("refId"));
                ref = new JSONObject();
                ref.put("num", "["+(i+1)+"]");
                ref.put("refstr",thisRef.getRefstr());
                ref.put("refId",thisRef.getId());
                ref.put("page",refList.getJSONObject(i).getString("refS")+"~"+refList.getJSONObject(i).getString("refE"));
                refs.add(ref);
            }
        } catch (Exception e) {
        }
        return refs;
    }

    @Override
    public JSONArray refactoringRef(String refjson) {
        JSONArray refs = new JSONArray();
        JSONArray refList = new JSONArray();
        Ref thisRef = null;
        JSONObject ref = null;
        try {
        	if (StringUtils.isNotBlank(refjson)) {
        		refList =JSONArray.parseArray(refjson);
        	}
            for (int i = 0; i < refList.size(); i++) {
                thisRef = refRepository.findOneById(refList.getJSONObject(i).getString("refId"));
                ref = new JSONObject();
                ref.put("num", (i + 1));
                ref.put("refId", thisRef.getId());
                ref.put("refstr", thisRef.getRefstr());
                ref.put("refS", StringUtils.isNotBlank(refList.getJSONObject(i).getString("refS")) ? refList.getJSONObject(i).getString("refS") : 0);
                ref.put("refE", StringUtils.isNotBlank(refList.getJSONObject(i).getString("refE")) ? refList.getJSONObject(i).getString("refE") : 0);
                String refType = refList.getJSONObject(i).getString("refType");
                if (StringUtils.isNotBlank(refType)) {
                    ref.put("refType", refType);
                }else {
                    ref.put("refType", 2);
                }
                refs.add(ref);
            }
        } catch (Exception e) {
        }
        return refs;
    }

    @Override
    public String handleRefjsonForExport(String refjson) {
        StringBuffer refs = new StringBuffer();
        JSONArray refList = new JSONArray();
        Ref thisRef = null;
        if (StringUtils.isNotBlank(refjson)) {
            refList =JSONArray.parseArray(refjson);
        }
        try {
            for (int i = 0; i < refList.size(); i++) {
                thisRef = refRepository.findOneById(refList.getJSONObject(i).getString("refId"));
				if (i == refList.size() - 1) {
					refs.append(thisRef.getRefstr());
				}else {
					refs.append(thisRef.getRefstr());
					refs.append(",");
				}
            }
        } catch (Exception e) {
        }
        return refs.toString();
    }

    @Override
    public void export(HttpServletResponse response) throws IOException {
        String[] columnName = {"*序号", "ID", "*文献类型", "*完整题录", "*作者", "*发表年代", "*题目", "期刊/专著/论文集名", "卷", "期", "起始页", "终止页", "ISBN",
        		"出版地（专著）", "出版社（专著）", "译/编者", "关键字", "总页数", "总字数", "文献语言", "原始语言", "版本", "备注"};
        UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 当前用户下的参考文献
        List<Ref> thisList = this.refRepository.findAllByUserId(thisUser.getId());
        // 第1步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第2步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("参考文献");

        // 第3.1步，创建表头的列
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < columnName.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(columnName[i]);
            cell.setCellStyle(ExcelUtil.setTitleStyle(wb));
        }

        sheet.setColumnWidth(1, 36 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 65 * 256);
        sheet.setColumnWidth(4, 25 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 30 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(13, 25 * 256);
        sheet.setColumnWidth(14, 25 * 256);
        sheet.setColumnWidth(16, 30 * 256);
        sheet.setColumnWidth(19, 24 * 256);
        sheet.setColumnWidth(20, 24 * 256);
        sheet.setColumnWidth(22, 30 * 256);
		
        String ptype = "";
        // 第3.2步，创建单元格，并设置值
        for (int i = 0; i < thisList.size(); i++) {
            HSSFRow rows = sheet.createRow((int) (i + 1));
            Ref ref = thisList.get(i);
            rows.createCell(0).setCellValue(String.valueOf((i + 1)));	// 设置第1列序号
            rows.createCell(1).setCellValue(ref.getId());
            HSSFCell cell2 = rows.createCell(2);
            ptype = ref.getPtype();
            switch (ptype) {
                case "1":
                    cell2.setCellValue("期刊[J]");
                    break;
                case "2":
                    cell2.setCellValue("专著[M]");
                    break;
                case "3":
                    cell2.setCellValue("论文集[C]");
                    break;
                default:
                    cell2.setCellValue("其他");
                    break;
            }
            rows.createCell(3).setCellValue(ref.getRefstr());
            rows.createCell(4).setCellValue(ref.getAuthor());
            rows.createCell(5).setCellValue(ref.getPyear());
            rows.createCell(6).setCellValue(ref.getTitle());
            rows.createCell(7).setCellValue(ref.getJournal());
            rows.createCell(8).setCellValue(ref.getrVolume());
            rows.createCell(9).setCellValue(ref.getrPeriod());
            rows.createCell(10).setCellValue(ref.getRefs());
            rows.createCell(11).setCellValue(ref.getRefe());
            rows.createCell(12).setCellValue(ref.getIsbn());
            rows.createCell(13).setCellValue(ref.getPlace());
            rows.createCell(14).setCellValue(ref.getPress());
            rows.createCell(15).setCellValue(ref.getTranslator());
            rows.createCell(16).setCellValue(ref.getKeywords());
            rows.createCell(17).setCellValue(ref.getTchar());
            rows.createCell(18).setCellValue(ref.getTpage());
            rows.createCell(19).setCellValue(this.languageService.handleLanguageDropdown(ref.getLanguages()));
            rows.createCell(20).setCellValue(this.languageService.handleLanguageDropdown(ref.getOlang()));
            rows.createCell(21).setCellValue(ref.getVersion());
            rows.createCell(22).setCellValue(ref.getRemark());
        }

        // 第4步，将文件存到浏览器设置的下载位置
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("参考文献".getBytes("gb2312"), "iso8859-1")+".xls");
        OutputStream out = response.getOutputStream();
        try {
            wb.write(out);// 将数据写出去
        } catch (Exception e) {
        } finally {
            out.close();
        }
    }

    @Override
    public JSON uploadFile(MultipartFile file, HttpServletRequest request) throws Exception {
    	JSONObject thisResult = new JSONObject();
        UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Ref> refList = new ArrayList<>();					// 上传文件的Reference数据
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
                	
                	/*String tableHeadArray[] = {"*序号", "ID", "*文献类型", "*完整题录", "*作者", "*发表年代", "*题目", "期刊/专著/论文集名", "卷", "期", "起始页", "终止页", "ISBN",
                    		"出版地（专著）", "出版社（专著）", "译/编者", "关键字", "总页数", "总字数", "文献语言", "原始语言", "版本", "备注"};*/
                	String tableHeadArray[] = {"序号", "ID", "文献类型", "完整题录", "作者", "发表年代", "题目", "期刊/专著/论文集名", "卷", "期", "起始页", "终止页", "ISBN",
                			"出版地（专著）", "出版社（专著）", "译/编者", "关键字", "总页数", "总字数", "文献语言", "原始语言", "版本", "备注"};
                    if(this.excelService.judgeRowConsistent(23, tableHeadArray, sheet.getRow(0))){
                        //构造对照表（临时表）
                        try {
                            String refFileMark = UUIDUtils.getUUID32();			// 文件标记
                            Ref thisRef = new Ref();							// Ref对象
                            Baseinfotmp thisBaseinfotmp = new Baseinfotmp();	// 临时表数据对象
                            Row row = null;										// 记录行
                            String serialNum = null;							// 记录行的序号

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
                                if (uniqSerialNum.size() <= 0) {
                                    for (int i = 1; i <= rowNum; i++) {
                                        row = sheet.getRow(i);
                                        if (null == sheet.getRow(i)) { 			// 空行
                                        	continue; 
                                        }	
                                        serialNum = excelService.getStringValueFromCell(row.getCell(0));
                                        StringBuilder sb = new StringBuilder();
                                        String refid = excelService.getStringValueFromCell(row.getCell(1));
                                        if (StringUtils.isBlank(refid)) {
											refid = UUIDUtils.getUUID32();
										}else {
											String[] split = refid.split("-");
											for (int j = 0; j < split.length; j++) {
												sb.append(split[j]);
											}
											refid = sb.toString();
										}
                                        //构造临时表对象
                                        thisBaseinfotmp = new Baseinfotmp(
                                                UUIDUtils.getUUID32(),
                                                refid,
                                                serialNum,
                                                refFileMark,
                                                0,
                                                thisUser.getId(),
                                                new Timestamp(System.currentTimeMillis()));
                                        baseInfoTmpList.add(thisBaseinfotmp);
                                        //构建参考文献对象
                                        thisRef = new Ref(
                                        		refid,
                                                excelService.getStringValueFromCell(row.getCell(2)),
                                                excelService.getStringValueFromCell(row.getCell(3)),
                                                excelService.getStringValueFromCell(row.getCell(4)),
                                                excelService.getStringValueFromCell(row.getCell(5)),
                                                excelService.getStringValueFromCell(row.getCell(6)),
                                                excelService.getStringValueFromCell(row.getCell(7)),
                                                excelService.getStringValueFromCell(row.getCell(8)),
                                                excelService.getStringValueFromCell(row.getCell(9)),
                                                excelService.getStringValueFromCell(row.getCell(10)),
                                                excelService.getStringValueFromCell(row.getCell(11)),
                                                excelService.getStringValueFromCell(row.getCell(12)),
                                                excelService.getStringValueFromCell(row.getCell(13)),
                                                excelService.getStringValueFromCell(row.getCell(14)),
                                                excelService.getStringValueFromCell(row.getCell(15)),
                                                excelService.getStringValueFromCell(row.getCell(16)),
                                                excelService.getStringValueFromCell(row.getCell(17)),
                                                excelService.getStringValueFromCell(row.getCell(18)),
                                                this.languageService.getValueofLanguage(excelService.getStringValueFromCell(row.getCell(19))),
                                                this.languageService.getValueofLanguage(excelService.getStringValueFromCell(row.getCell(20))),
                                                excelService.getStringValueFromCell(row.getCell(21)),
                                                excelService.getStringValueFromCell(row.getCell(22)));
                                        refList.add(thisRef);
                                    }
                                    long start = System.currentTimeMillis();
                                    JSONArray verifyListEntity = verifyListEntity(refList, baseInfoTmpList, thisUser);
                                    long end = System.currentTimeMillis();
                                    System.err.println("校验完成：" + (end - start));
                                    if (verifyListEntity.size() > 0) {
                                    	thisResult.put("message", "录入数据异常，必填字段不能为空");
                                        thisResult.put("status", false);
                                        thisResult.put("code", -4);
                                        //处理必填字段未写table的json
                                        JSONArray errorData=new JSONArray();
                                        try{
                                            for(int i=0;i<verifyListEntity.size();i++){
                                                JSONObject json=new JSONObject();
                                                json.put("num",verifyListEntity.getJSONObject(i).get("num"));
                                                json.put("ptype",verifyListEntity.getJSONObject(i).get("ptype"));
                                                json.put("refstr",verifyListEntity.getJSONObject(i).get("refstr"));
                                                json.put("author",verifyListEntity.getJSONObject(i).get("author"));
                                                json.put("pyear",verifyListEntity.getJSONObject(i).get("pyear"));
                                                json.put("title",verifyListEntity.getJSONObject(i).get("title"));
                                                errorData.add(json);
                                            }
                                        }
                                        catch (Exception e){
                                        	e.printStackTrace();
                                        }
                                        thisResult.put("errorData", verifyListEntity);
                                    }else {
                                        thisResult.put("message", "上传成功");
                                        thisResult.put("status", true);
                                        thisResult.put("code", 1);
										thisResult.put("refFileMark", refFileMark);
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
                                            if(uniqSerialNum.getJSONObject(i).get("rowNum")==null || uniqSerialNum.getJSONObject(i).get("rowNum").equals("")){

                                            }
                                            else{
                                                thisUniqData.put("num","["+uniqSerialNum.getJSONObject(i).get("rowNum")+"]");
                                                thisUniqData.put("row",uniqSerialNum.getJSONObject(i).getInteger("rowIndex")+2);
                                                thisUniqData.put("title",excelService.getStringValueFromCell(sheet.getRow(uniqSerialNum.getJSONObject(i).getInteger("rowIndex")+1).getCell(3)));
                                                errorTable.add(thisUniqData);
                                            }
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
    private JSONArray verifyListEntity(List<Ref> list, List<Baseinfotmp> baseInfoTmpList, User thisUser) throws Exception {
    	JSONArray refsArr = new JSONArray();
    	List<Ref> failList = new ArrayList<>();
        Ref thisRef = new Ref();
        boolean flag = true;
        for (int i = 0; i < list.size(); i++) {
        	boolean mark = true;
        	thisRef = list.get(i);
            if (StringUtils.isBlank(thisRef.getPtype())) {
                thisRef.setPtype("<span style='color:red'>文献类型不能为空</span>");
                mark = false;
            };
            if (StringUtils.isBlank(thisRef.getRefstr())) {
                thisRef.setRefstr("<span style='color:red'>完整题录不能为空</span>");
                mark = false;
            };
            if (StringUtils.isBlank(thisRef.getAuthor())) {
                thisRef.setAuthor("<span style='color:red'>作者不能为空</span");
                mark = false;
            };
            if (StringUtils.isBlank(thisRef.getPyear())) {
                thisRef.setPyear("<span style='color:red'>发表年代不能为空</span>");
                mark = false;
            };
            if (StringUtils.isBlank(thisRef.getTitle())) {
                thisRef.setTitle("<span style='color:red'>题目不能为空</span>");
                mark = false;
            };
            if (!mark) {
            	thisRef.setSynchstatus(i + 1);
            	failList.add(thisRef);
            	flag = mark;
			}
        }
        /* 构建的集合若通过必填字段的校验，去重，补充属性值*/
        /**
         * 去重：
         * 1.先判断Id是否与数据库已有相同数据，有 -- 将Id存到临时表，无 -- 第二步
         * 2.根据当前数据的完整题录查数据库是否已有相同数据， 有 -- 不存，无 -- 存
         */
        if (flag) {
        	List<Ref> refList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
            	Ref ref = this.refRepository.findOneByRefstrAndInputer(list.get(i).getRefstr(), thisUser.getId());
            	if(null == ref){											// (根据完整题录去重) 用户下不存在 -- 保存
            		String ptype = getValueOfPtype(list.get(i).getPtype());
            		list.get(i).setPtype(ptype);
            		list.get(i).setInputer(thisUser.getId());
            		list.get(i).setInputtime(new Timestamp(System.currentTimeMillis()));
            		list.get(i).setSynchdate(new Timestamp(System.currentTimeMillis()));
            		list.get(i).setStatus(1);
            		list.get(i).setSynchstatus(0);
            		/*this.refRepository.save(list.get(i));*/
            		refList.add(list.get(i));
				}else {
					baseInfoTmpList.get(i).setRefDsId(ref.getId());
				}
            }
            long start = System.currentTimeMillis();
            this.batchInsertService.batchInsertRef(refList);
            this.batchInsertService.batchInsertBaseinfotmps(baseInfoTmpList);
            long end = System.currentTimeMillis();
            System.err.println("Ref批量存储完成：" + (end - start));
        }else {
			for (int i = 0; i < failList.size(); i++) {
				JSONObject json = new JSONObject();
				json.put("num", failList.get(i).getSynchstatus());
				json.put("ptype", failList.get(i).getPtype());
				json.put("refstr", failList.get(i).getRefstr());
				json.put("author", failList.get(i).getAuthor());
				json.put("pyear", failList.get(i).getPyear());
				json.put("title", failList.get(i).getTitle());
				refsArr.add(i, json);
			}
		}
        return refsArr;
    }
	/**
	 * <b>ptype下拉选value与值的转换，取value</b>
	 * <p> ptype下拉选value与值的转换，取value</p>
	 * @param ptype
	 * @return
	 */
	private String getValueOfPtype(String ptype) {
		String rsl = "";
		if (StringUtils.isNotBlank(ptype)) {
			switch (ptype) {
			case "期刊[J]":
				rsl = "1";
				break;
			case "专著[M]":
				rsl = "2";
				break;
			case "论文集[C]":
				rsl = "3";
				break;
			default:
				rsl = "4";
				break;
			}
		}
		return rsl;
	}

	/**
	 * <b>封装参考文献列表的JSON数据</b>
	 * <p> 封装参考文献列表的JSON数据</p>
	 * @param thisList
	 * @return
	 */
	private JSONArray findRefListForTable(List<Ref> thisList) {
		JSONArray rows = new JSONArray();
		String thisSelect;
		String thisEdit;
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
		    thisSelect="<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
		    thisEdit=
		            "<a class=\"wts-table-edit-icon\" onclick=\"editThisObject('" + thisList.get(i).getId() + "','ref')\" >" +
		                    "<span class=\"glyphicon glyphicon-edit\"></span>" +
		                    "</a> &nbsp;&nbsp;&nbsp;" +
		                    "<a class=\"hidden wts-table-edit-icon\" onclick=\"removeThisObject('" + thisList.get(i).getId() + "','ref')\" >" +
		                    "<span class=\"glyphicon glyphicon-remove\"></span>" +
		                    "</a>";
		    row.put("select", thisSelect);
		    row.put("refstr", thisList.get(i).getRefstr());
		    row.put("author", thisList.get(i).getAuthor());
		    row.put("title", thisList.get(i).getTitle());
		    row.put("pyear", thisList.get(i).getPyear());
		    row.put("journal", thisList.get(i).getJournal());
		    row.put("volume", thisList.get(i).getrVolume());
		    row.put("period", thisList.get(i).getrPeriod());
			
		    String ptype = thisList.get(i).getPtype();
            if(ptype==null){
                row.put("ptype", "4");
            }
            else{
                switch (thisList.get(i).getPtype()) {
                    case "1":
                        row.put("ptype", "期刊[J]");
                        break;
                    case "2":
                        row.put("ptype", "专著[M]");
                        break;
                    case "3":
                        row.put("ptype", "论文集[C]");
                        break;
                    default:
                        row.put("ptype", "其他");
                        break;
                }
            }
		    row.put("press", thisList.get(i).getPress());
		    row.put("version", thisList.get(i).getVersion());
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
		return rows;
	}

	@Override
	public JSONArray getRefJSONArrayByIds(List<String> refIds) {
		JSONArray thisTable = new JSONArray();
		List<Ref> list = new ArrayList<>();
		for (String refId : refIds) {
			Ref thisRef = this.refRepository.findOneById(refId);
			if (null != thisRef) {
				list.add(thisRef);
			}
		}
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				JSONObject json = new JSONObject();
				json.put("refE", StringUtils.isNotBlank(list.get(i).getRefe()) ? list.get(i).getRefe() : 0);
				json.put("refS", StringUtils.isNotBlank(list.get(i).getRefs()) ? list.get(i).getRefs() : 0);
				json.put("refId", list.get(i).getId());
				json.put("refType", list.get(i).getPtype());
				thisTable.add(i, json);
			}
		}
		return thisTable;
	}

	@Override
	public JSON handleRefstr(HttpServletRequest request) {
		JSONObject thisResult = new JSONObject();
		String refstr = request.getParameter("refstr");
		boolean flag = false;
		if (StringUtils.isNotBlank(refstr)) {
			if (isContainChinese(refstr)) {
				refstr = refstr.replace(".", "．").replace(":", "：");
			}else {
				refstr = refstr.replace("．", ".").replace("：", ":");
			}
			flag = true;
		}else {
			flag = false;
		}
		if (flag) {
			if (refstr.contains("[J]")) {		// 期刊 Journal
				thisResult =  parseRefstrOfJournal(refstr);
			}else if (refstr.contains("[M]")) {	// 专著 Monograph
				thisResult =  parseRefstrOfMonograph(refstr);
			}else if (refstr.contains("[C]")) {	// 论文集	Vol.
				thisResult =  parseRefstrOfVol(refstr);
			}else {								// 期刊 Journal
				thisResult =  parseRefstrOfJournal(refstr);
			}
		}
		return thisResult;
	}
	
	/**
	 * <b>解析中文期刊</b>
	 * <p> 解析中文期刊</p>
	 * @param refstr
	 * @return
	 */
	/* 1.期刊：著者．题名[J]．刊名．出版年，卷（期）∶起止页码 */
	private JSONObject parseRefstrOfJournal(String refstr) {
		JSONObject json = new JSONObject();
		
		try {
			String journal = null;															// 期刊名称
			int pageNumMarkIndex = 0; 														//4.页码索引
			int authorMarkIndex = 0;														//2.作者索引
			int tmpIndex = refstr.indexOf("]");												//1.临时索引，根据期刊标记[J]分两部分
			String tmpRefPre = refstr.substring(0, tmpIndex + 2).trim();
			String tmpRefSuf = refstr.substring(tmpIndex + 2, refstr.length()).trim();
			int yearMarkIndex = getYearStart(tmpRefSuf);									//3.日期索引
			String rVolume = null;															// 卷期
			if (isContainChinese(refstr)) {
				authorMarkIndex = tmpRefPre.indexOf("．");
				pageNumMarkIndex = tmpRefSuf.lastIndexOf("：");
				journal = tmpRefSuf.substring(0, yearMarkIndex - 1).trim();
				rVolume = tmpRefSuf.substring(yearMarkIndex + 5, pageNumMarkIndex - 1).trim();
			}else {
				authorMarkIndex = tmpRefPre.indexOf(".");
				pageNumMarkIndex = tmpRefSuf.lastIndexOf(":");
				journal = tmpRefSuf.substring(0, yearMarkIndex - 2).trim();
				rVolume = tmpRefSuf.substring(yearMarkIndex + 5, pageNumMarkIndex).trim();
			}
			
			String author = tmpRefPre.substring(0, authorMarkIndex).trim();							// 作者
			String title = tmpRefPre.substring(authorMarkIndex + 1, tmpRefPre.length() - 1).trim();	// 标题
			String pyear = tmpRefSuf.substring(yearMarkIndex, yearMarkIndex + 4).trim();			// 年份
			String pageNum = tmpRefSuf.substring(pageNumMarkIndex + 1, tmpRefSuf.length() - 1).trim();	// 起始页码
			
			if (pageNum.contains("-")) {
				String[] page = pageNum.split("-");
				json.put("refs", page[0]);
				json.put("refe", page[1]);
			}else {
				json.put("refs", pageNum);
				json.put("refe", pageNum);
			}
			json.put("author", author);
			json.put("title", title);
			json.put("pyear", pyear);
			json.put("journal", journal);
			json.put("rVolume", rVolume);
			json.put("ptype", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * <b>解析中文专著</b>
	 * <p> 解析中文期刊</p>
	 * @param refstr
	 * @return
	 */
	/* 2.专著：著者．书名[M]．版本（第一版不录）．出版地∶出版者，出版年∶起止页码 */
	private JSONObject parseRefstrOfMonograph(String refstr) {
		JSONObject json = new JSONObject();
		try {
			String journal = null;															// 期刊名称
			int pageNumMarkIndex = 0; 														//4.页码索引
			int authorMarkIndex = 0;														//2.作者索引
			int tmpIndex = refstr.indexOf("]");												//1.临时索引，根据期刊标记[J]分两部分
			String tmpRefPre = refstr.substring(0, tmpIndex + 2).trim();
			String tmpRefSuf = refstr.substring(tmpIndex + 2, refstr.length()).trim();
			int yearMarkIndex = 0;									//3.日期索引
			int placeMarkIndex = 0;									//5.出版地
			int pressMarkIndex = 0;									//6.出版社
			String pyear = null;			// 年份
			if (isContainChinese(refstr)) {
				authorMarkIndex = tmpRefPre.indexOf("．");
				pageNumMarkIndex = tmpRefSuf.lastIndexOf("：");
				placeMarkIndex = tmpRefSuf.indexOf("：");
				pressMarkIndex = tmpRefSuf.indexOf("，");
				yearMarkIndex = getYearStart(tmpRefSuf);
				journal = tmpRefSuf.substring(0, yearMarkIndex - 1).trim();
				pyear = tmpRefSuf.substring(yearMarkIndex, yearMarkIndex + 4).trim();
			}else {
				authorMarkIndex = tmpRefPre.indexOf(".");
				pageNumMarkIndex = tmpRefSuf.lastIndexOf(":");
				placeMarkIndex = tmpRefSuf.indexOf(":");
				pressMarkIndex = tmpRefSuf.indexOf(",");
				yearMarkIndex = tmpRefSuf.lastIndexOf(",");
				journal = tmpRefSuf.substring(pageNumMarkIndex, yearMarkIndex).trim();
				pyear = tmpRefSuf.substring(yearMarkIndex + 1, tmpRefSuf.length()).trim();
			}
			
			String author = tmpRefPre.substring(0, authorMarkIndex).trim();							// 作者
			String title = tmpRefPre.substring(authorMarkIndex + 1, tmpRefPre.length() - 1).trim();	// 标题
			String pageNum = tmpRefSuf.substring(pageNumMarkIndex + 1, tmpRefSuf.length() - 1).trim();	// 起始页码
			String place = tmpRefSuf.substring(0, placeMarkIndex).trim();								// 出版地
			String press = tmpRefSuf.substring(placeMarkIndex + 1, pressMarkIndex).trim();
			if (isContainChinese(refstr)) {
				if (pageNum.contains("-")) {
					String[] page = pageNum.split("-");
					json.put("refs", page[0]);
					json.put("refe", page[1]);
				}else {
					json.put("refs", pageNum);
					json.put("refe", pageNum);
				}
			}
			json.put("author", author);
			json.put("title", title);
			json.put("pyear", pyear);
			json.put("journal", journal);
			json.put("place", place);
			json.put("press", press);
			json.put("ptype", 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * <b>解析中文论文集</b>
	 * <p> 解析中文论文集</p>
	 * @param refstr
	 * @return
	 */
	/* 3.论文集：著者．题名．编者．论文集名[C]．出版地∶出版者，出版年∶起止页码 */
	private JSONObject parseRefstrOfVol(String refstr) {
		JSONObject json = new JSONObject();
		try {
			int tmpIndex = refstr.indexOf("]");												//1.临时索引，根据论文集标记[C]分两部分
			int authorMarkIndex = 0;														//2.作者索引
			int placeMarkIndex = 0;															//5.出版地
			int translatorMarkIndex = 0;													//6.编译者
			int journalMarkIndex = 0;														//7.论文集名称
			String tmpRefPre = refstr.substring(0, tmpIndex + 2).trim();					// 著者．题名．编者．论文集名[C]．
			String tmpRefSuf = refstr.substring(tmpIndex + 2, refstr.length()).trim();		// 出版地∶出版者，出版年∶起止页码
			int yearMarkIndex = getYearStart(tmpRefSuf);									//3.日期索引
			String press = null;
			String journal = null;															// 期刊名称
			String place = null;															// 期刊名称
			if (isContainChinese(refstr)) {
				translatorMarkIndex = tmpRefSuf.indexOf("．");
				journalMarkIndex = tmpRefSuf.indexOf("．", translatorMarkIndex + 1);
				journal = tmpRefSuf.substring(translatorMarkIndex + 1, journalMarkIndex).trim();
				authorMarkIndex = tmpRefPre.indexOf("．");
				placeMarkIndex = tmpRefSuf.indexOf("：");
				place = tmpRefSuf.substring(journalMarkIndex + 1, placeMarkIndex).trim();
				press = tmpRefSuf.substring(placeMarkIndex + 1, tmpRefSuf.lastIndexOf("，")).trim();
			}else {
				authorMarkIndex = tmpRefPre.indexOf(".");
				journal = tmpRefSuf.substring(0, tmpRefSuf.indexOf("．")).trim();
				placeMarkIndex = tmpRefSuf.indexOf(":");
				place = tmpRefSuf.substring(tmpRefSuf.indexOf("．") + 1, placeMarkIndex).trim();
				press = tmpRefSuf.substring(placeMarkIndex + 1, tmpRefSuf.lastIndexOf(",")).trim();
			}
			
			String author = tmpRefPre.substring(0, authorMarkIndex).trim();							// 作者
			String title = tmpRefPre.substring(authorMarkIndex + 1, tmpRefPre.length() - 1).trim();	// 标题
			String pyear = tmpRefSuf.substring(yearMarkIndex, yearMarkIndex + 4).trim();			// 年份
			String pageNum = tmpRefSuf.substring(yearMarkIndex + 5, tmpRefSuf.length() - 1).trim();	// 起始页码
			String translator = tmpRefSuf.substring(0, translatorMarkIndex).trim();
			if (pageNum.contains("-")) {
				String[] page = pageNum.split("-");
				json.put("refs", page[0]);
				json.put("refe", page[1]);
			}else {
				json.put("refs", pageNum);
				json.put("refe", pageNum);
			}
			json.put("author", author);
			json.put("title", title);
			json.put("pyear", pyear);
			json.put("journal", journal);
			json.put("place", place);
			json.put("press", press);
			json.put("translator", translator);
			json.put("ptype", 3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public int getYearStart(String refstr) {
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

	public boolean isContainChinese(String refstr) {
		boolean temp = false;
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(refstr);
		if (m.find()) {
			temp = true;
		}
		return temp;
	}

	@Override
	public List<Reference> findRefByInputerTurnToReference(String inputer) throws Exception {
		List<Object[]> list = refRepository.findAllByInputer(inputer);
		List<Reference> resultlist = new ArrayList<>();
		for (Object[] obj : list) {
			Reference ref = new Reference();
			ref.setRecordId(obj[0].toString());

			String refstr = (null != obj[1]) ? obj[1].toString() : "";
			String title = (null != obj[2]) ? obj[2].toString() : "";
			String author = (null != obj[3]) ? obj[3].toString() : "";
			String pyear = (null != obj[4]) ? obj[4].toString() : "";
			
			String mark = null;
			if (StringUtils.isNotBlank(refstr)) {
				mark = refstr.trim().substring(0, 1);
				boolean flag = isEnglish(mark);
				if (flag) { // 英文
					ref.setSource(refstr);
					ref.setSourceC("");
				}else {		// 中文
					ref.setSource("");
					ref.setSourceC(refstr);
				}
			}else {
				ref.setSource("");
				ref.setSourceC("");
			}
			
			if (StringUtils.isNotBlank(author)) {
				boolean flag = isEnglish(mark);
				if (flag) { // 英文
					ref.setAuthor(author.length() > 200 ? author.substring(0, 200) : author);
					ref.setAuthorC("");
				}else {		// 中文
					ref.setAuthor("");
					ref.setAuthorC(author.length() > 200 ? author.substring(0, 200) : author);
				}
			}else {
				ref.setAuthor("");
				ref.setAuthorC("");
			}
			
			if (StringUtils.isNotBlank(title)) {
				boolean flag = isEnglish(mark);
				if (flag) { // 英文
					ref.setTitle(title.length() > 200 ? title.substring(0, 200) : title);
					ref.setTitleC("");
				}else {		// 中文
					ref.setTitle("");
					ref.setTitleC(title.length() > 200 ? title.substring(0, 200) : title);
				}
			}else {
				ref.setTitle("");
				ref.setTitleC("");
			}
			
			if (StringUtils.isNotBlank(pyear)) {
				if (pyear.length() > 50) {
					ref.setYear(pyear.length() > 50 ? pyear.substring(0, 50) : pyear);
				}else {
					ref.setYear(pyear);
				}
			}else {
				ref.setYear("unknown");
			}
			ref.setDatabaseId(null);
			resultlist.add(ref);
		}
		return resultlist;
	}
	
	public boolean isEnglish(String charaString) {
		if (StringUtils.isNotBlank(charaString)) {
			return charaString.matches("^[a-zA-Z0-9]*");
		}else {
			return false;
		}
	}

	@Override
	public List<ReferenceLink> getReferenceLinkByTaxaset(String taxasetId) {
		String[] refTypeArr = {"taxon", "citation", "distribution", "commonname"};
		List<ReferenceLink> resultlist = new ArrayList<>();		// 最终结果
		List<ReferenceLink> taxonList = new ArrayList<>();		// TaxonRef
		List<ReferenceLink> citationAcList = new ArrayList<>();	// CitationAcceptRef
		List<ReferenceLink> citationSyList = new ArrayList<>();	// CitationSynchRef
		List<ReferenceLink> distributList = new ArrayList<>();	// DistributRef
		List<ReferenceLink> commonList = new ArrayList<>();		// CommonRef
		for (String refType : refTypeArr) {
			switch (refType) {
			case "taxon":
				List<Object[]> tlist = taxonRepository.findReferenceLinkByTaxasetAndTaxon(taxasetId);
				taxonList = handleReferenceLinkType(tlist);
				System.out.println("taxonRef's size:" + taxonList.size());
				break;
			case "citation":
				List<Object[]> cAclist = citationRepository.findAcceptReferenceLinkByTaxasetAndCitation(taxasetId);
				List<Object[]> cSylist = citationRepository.findSynchReferenceLinkByTaxasetAndCitation(taxasetId);
				citationAcList = handleReferenceLinkType(cAclist);
				citationSyList = handleReferenceLinkType(cSylist);
				System.out.println("citationAcRef's size:" + citationAcList.size());
				System.out.println("citationSyRef's size:" + citationSyList.size());
				break;
			case "distribution":
				List<Object[]> dislist = distributiondataRepository.findReferenceLinkByTaxasetAndDistribution(taxasetId);
				distributList = handleReferenceLink(dislist);
				System.out.println("distributionRef's size:" + distributList.size());
				break;
			case "commonname":
				List<Object[]> comlist = commonnameRepository.findReferenceLinkByTaxasetAndCommonname(taxasetId);
				commonList = handleReferenceLinkType(comlist);
				System.out.println("commonnameRef's size:" + commonList.size());
				break;
			}
		}
		resultlist.addAll(taxonList);
		resultlist.addAll(citationAcList);
		resultlist.addAll(citationSyList);
		resultlist.addAll(distributList);
		resultlist.addAll(commonList);
		return resultlist;
	}
	private List<ReferenceLink> handleReferenceLinkType(List<Object[]> list) {
		System.out.println("基础数据：" + list.size());
		List<ReferenceLink> resultlist = new ArrayList<>();
		for (Object[] obj : list) {
			String refjson = obj[1].toString();
			List<String> refList = getRefIdAndRefTypeByRefjson(refjson);
			for (String refId : refList) {
				ReferenceLink entity = new ReferenceLink();
				Ref thisRef = this.refRepository.findOneById(refId);
				if (null != obj[2] && StringUtils.isNotBlank(thisRef.getRefstr()) && thisRef.getRefstr().contains(obj[2].toString())) {
					entity.setReferenceType("NomRef");		
				}else {
					entity.setReferenceType("TaxRef");		
				}
				entity.setRecordId(obj[0].toString());		// 对应目标的id(实体Id)
				entity.setReferenceId(refId);
				resultlist.add(entity);
			}
		}
		return resultlist;
	}

	/**
	 * DisRef是分布地的参考文献
	 * TaxRef是非原始参考文献
	 * NomRef是原始命名文献
	 * @param list
	 * @param resultlist
	 * @param referenceType
	 * @return
	 */
	private List<ReferenceLink> handleReferenceLink(List<Object[]> list) {
		List<ReferenceLink> resultlist = new ArrayList<>();
		for (Object[] obj : list) {
			String refjson = obj[1].toString();
			List<String> refList = getRefIdAndRefTypeByRefjson(refjson);
			for (String refId : refList) {
				ReferenceLink entity = new ReferenceLink();
				entity.setRecordId(obj[0].toString());		// 对应目标的id(实体Id)
				entity.setReferenceId(refId);
				entity.setReferenceType("DisRef");	
				resultlist.add(entity);
			}
		}
		return resultlist;
	}

	private List<String> getRefIdAndRefTypeByRefjson(String refjson) {
		JSONArray refList=JSONArray.parseArray(refjson);
		List<String> list = new ArrayList<>();
        try {
            for(int i=0;i<refList.size();i++){
            	list.add(refList.getJSONObject(i).getString("refId"));
            }
        } catch (Exception e) {
        }
		return list;
	}

}
