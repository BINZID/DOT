package org.big.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.big.sp2000.entity.CommonName;
import org.big.common.ExcelUtil;
import org.big.common.QueryTool;
import org.big.common.UUIDUtils;
import org.big.entity.Commonname;
import org.big.entity.Expert;
import org.big.entity.Taxon;
import org.big.entity.User;
import org.big.entity.UserDetail;
import org.big.repository.CommonnameRepository;
import org.big.repository.ExpertRepository;
import org.big.repository.TaxonRepository;
import org.big.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 *<p><b>Commonname的Service类</b></p>
 *<p> Commonname的Service类，与Commonname有关的业务逻辑方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Service
public class CommonnameServiceImpl implements CommonnameService {

    @Autowired
    private CommonnameRepository commonnameRepository;
    @Autowired
    private UserRepository userRepository;
	@Autowired
    private TaxonRepository taxonRepository;
	@Autowired
	private DatasourceService datasourceService;
	@Autowired
	private RefService refService;
	@Autowired
	private LanguageService languageService;
	@Autowired
	private ExcelService excelService;
	@Autowired
	private BaseinfotmpService baseinfotmpService;
	@Autowired
	private ExpertService expertService;
	@Autowired
	private ExpertRepository expertRepository;
	@Autowired
	private BatchInsertService batchInsertService;
    @Override
    public JSON findUploadedCommonnameList(Timestamp timestamp, HttpServletRequest request) {
        JSON json= null;
        String searchText=request.getParameter("search");
        if(searchText==null || searchText.length()<=0){
            searchText="";
        }
        int limit_serch=Integer.parseInt(request.getParameter("limit"));
        int offset_serch=Integer.parseInt(request.getParameter("offset"));
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
        JSONObject thisTable= new JSONObject();
        JSONArray rows = new JSONArray();
        List<Commonname> thisList=new ArrayList<>();
        UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Commonname> thisPage=this.commonnameRepository.searchInfo(searchText, timestamp, thisUser.getId(), QueryTool.buildPageRequest(offset_serch,limit_serch,sort,order));
        thisTable.put("total",thisPage.getTotalElements());
        thisList=thisPage.getContent();
        for(int i=0;i<thisList.size();i++){
            JSONObject row= new JSONObject();
            String thisSelect="<input type='checkbox' name='checkbox' id='sel_"+thisList.get(i).getId()+"' />";
            String thisEdit=
                    "<a class=\"wts-table-edit-icon\" onclick=\"editThisObject('"+thisList.get(i).getId()+"','commonname')\" >" +
                            "<span class=\"glyphicon glyphicon-edit\"></span>" +
                            "</a>&nbsp;&nbsp;" +
                            "<a class=\"wts-table-edit-icon\" onclick=\"removeThisObject('"+thisList.get(i).getId()+"','commonname')\" >" +
                            "<span class=\"glyphicon glyphicon-remove\"></span>" +
                            "</a>";
            row.put("select",thisSelect);
            row.put("commonname",thisList.get(i).getCommonname());
            row.put("language",this.languageService.handleLanguageDropdown(thisList.get(i).getLanguage()));
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
    public Commonname findbyID(String ID) {
        return this.commonnameRepository.getOne(ID);
    }

    @Override
    public void saveOne(Commonname thisCommoname) {
        if(thisCommoname.getId()==null||thisCommoname.getId().equals("")||thisCommoname.getId().length()<=0){
            thisCommoname.setId(UUIDUtils.getUUID32());
            thisCommoname.setInputtime(new Timestamp(System.currentTimeMillis()));
            thisCommoname.setSynchdate(null);
        }
        this.commonnameRepository.save(thisCommoname);
    }

    @Override
    public void removeOne(String ID) {
        this.commonnameRepository.deleteById(ID);
    }

	@Override
	public JSON addCommonname(String taxonId, HttpServletRequest request) {
		Commonname thisCommonname = new Commonname();
		Enumeration<String> paraNames = request.getParameterNames();
		String paraName = null;
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("commonnameId_") == 0) {
				thisCommonname.setId(request.getParameter(paraName));
			}
			if (paraName.indexOf("commonname_") == 0) {
				thisCommonname.setCommonname(request.getParameter(paraName));
			}
			if (paraName.indexOf("commonnameLanguage_") == 0) {
				thisCommonname.setLanguage(request.getParameter(paraName));
			}
			if (paraName.indexOf("commonnameSourcesid_") == 0) {
				thisCommonname.setSourcesid(request.getParameter(paraName));
			}
		}
		
		JSONObject thisResult = new JSONObject();
		try {
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			thisCommonname.setInputer(thisUser.getId());
			thisCommonname.setInputtime(new Timestamp(System.currentTimeMillis()));
			thisCommonname.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisCommonname.setTaxon(taxonRepository.findOneById(taxonId));
			JSONArray handleReferenceToJson = (JSONArray) handleReferenceToJson(request);
			if (handleReferenceToJson.size() > 0) {
				thisCommonname.setRefjson(handleReferenceToJson.toJSONString());
			}
			thisCommonname.setStatus(1);
			thisCommonname.setSynchstatus(0);
			this.commonnameRepository.save(thisCommonname);
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
		int countCommonnameNum = 0;
		String commonnameReferencesPageE = null;
		String commonnameReferencesPageS = null;
		String commonnameReferenceId = null;
		String jsonStr = null;
		
		while (paraNames.hasMoreElements()) {
			paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("countCommonnameReferences_") == 0) {
				countCommonnameNum = Integer.parseInt(request.getParameter(paraName));
			}
			if (paraName.indexOf("commonnameId_") == 0) {
				formNum = Integer.parseInt(paraName.substring(paraName.indexOf("_") + 1));
			}
		}
		
		for (int i = 1; i <= countCommonnameNum; i++) {
			commonnameReferenceId = request.getParameter("commonnameReferences_" + formNum + "_" + i);
			commonnameReferencesPageS = request.getParameter("commonnameReferencesPageS_" + formNum + "_" + i);
			commonnameReferencesPageE = request.getParameter("commonnameReferencesPageE_" + formNum + "_" + i);
			if (StringUtils.isNotBlank(commonnameReferenceId) && StringUtils.isNotBlank(commonnameReferencesPageS)
					&& StringUtils.isNotBlank(commonnameReferencesPageE)) {
				jsonStr = "{" 
						+ "\"refId\"" + ":\"" + commonnameReferenceId + "\","
						+ "\"refS\"" + ":\"" + commonnameReferencesPageS + "\"," 
						+ "\"refE\"" + ":\"" + commonnameReferencesPageE + "\""
						+ "}";
				JSONObject jsonText = JSON.parseObject(jsonStr);
				jsonArray.add(jsonText);
			}
		}
		
		return jsonArray;
	}
	

	@Override
	public boolean deleteOne(HttpServletRequest request) {
		String commonnameId = request.getParameter("commonnameId");
		if (StringUtils.isNotBlank(commonnameId)) {
			if (null != this.commonnameRepository.findOneById(commonnameId)) {
				this.commonnameRepository.deleteOneById(commonnameId);
			}
			return true;
		}
		return false;
	}

	@Override
	public JSON findCommonnameListByTaxonId(String taxonId, HttpServletRequest request) {
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
		Page<Commonname> thisPage = this.commonnameRepository.searchCommonnamesByTaxonId(
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), taxonId);
		
		JSONArray rows = new JSONArray();
		List<Commonname> thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			String sourcesid = thisList.get(i).getSourcesid();		
			if (StringUtils.isNotBlank(sourcesid)) {
				thisList.get(i).setSourcesid(this.datasourceService.findOneById(sourcesid).getTitle());
			}
			
			String language = thisList.get(i).getLanguage();
			thisList.get(i).setLanguage(this.languageService.handleLanguageDropdown(language));
			rows.add(i, thisList.get(i));
		}
		
		thisSelect.put("total", thisPage.getTotalElements());		// NO1：总数
		thisSelect.put("page", offset_serch);						// NO2：offset
		thisSelect.put("rows", rows);								// NO3：Taxon下的所有Citation
		return thisSelect;
	}

	@Override
	public JSON editCommonname(String taxonId) {
		JSONObject commonnames = new JSONObject();
		JSONArray commonnameArr = new JSONArray();
		try {
			if (StringUtils.isNotBlank(taxonId)) {
				List<Commonname> list = this.commonnameRepository.findCommonnameListByTaxonId(taxonId);
				for (int i = 0; i < list.size(); i++) {
					JSONArray refjson = this.refService.refactoringRef(list.get(i).getRefjson());
					JSONObject json = new JSONObject();
					json.put("id", list.get(i).getId());
					json.put("commonname", list.get(i).getCommonname());
					json.put("refjson", refjson.toJSONString());
					json.put("language", list.get(i).getLanguage());
					/*解析JSON文件
					String languageCode = list.get(i).getLanguage();
					if (StringUtils.isNotBlank(languageCode)) {
						String language = this.languageService.findLanguageByCode(languageCode);
						json.put("languageCode", languageCode);
						json.put("language", language);
					}*/

					String sourcesid = list.get(i).getSourcesid();
					String sourcesTitle = this.datasourceService.findOneById(sourcesid).getTitle();
					json.put("sourcesid", sourcesid);
					json.put("sourcesTitle", sourcesTitle);
					commonnameArr.add(i, json);
				}
				commonnames.put("commonnames", commonnameArr);
			}
		} catch (Exception e) {
		}
		return commonnames;
	}

	@Override
	public void export(HttpServletResponse response) throws IOException {
		String [] columnName = { "*学名", "*俗名", "*俗名语言", "*参考文献", "*数据源", "*审核专家", "版权所有者", "版权声明",
				"共享协议", "备注" };
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Commonname> thisList = this.commonnameRepository.findCommonnameListByUserId(thisUser.getId());
		
		// 第1步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第2步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("俗名信息");

		// 第3.1步，创建表头的列
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < columnName.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(columnName[i]);
			cell.setCellStyle(ExcelUtil.setTitleStyle(wb));
		}

		sheet.setColumnWidth(0, 10 * 256);
		sheet.setColumnWidth(1, 12 * 256);
		sheet.setColumnWidth(2, 15 * 256);
		sheet.setColumnWidth(3, 10 * 256);
		sheet.setColumnWidth(4, 10 * 256);
		sheet.setColumnWidth(5, 15 * 256);
		sheet.setColumnWidth(6, 12 * 256);
		sheet.setColumnWidth(7, 12 * 256);
		sheet.setColumnWidth(8, 12 * 256);
		sheet.setColumnWidth(9, 12 * 256);

		// 第3.2步，创建单元格，并设置值
		for (int i = 0; i < thisList.size(); i++) {
			HSSFRow rows = sheet.createRow((int) (i + 1));
			try {
				Commonname thisCommonname = thisList.get(i);
				rows.createCell(0).setCellValue(thisCommonname.getTaxon().getChname());
				rows.createCell(1).setCellValue(thisCommonname.getCommonname());
				rows.createCell(2).setCellValue(this.languageService.handleLanguageDropdown(thisCommonname.getLanguage()));
				rows.createCell(3).setCellValue(thisCommonname.getRefjson());
				rows.createCell(4).setCellValue(thisCommonname.getSourcesid());
				rows.createCell(5).setCellValue(thisCommonname.getExpert());
				rows.createCell(9).setCellValue(thisCommonname.getRemark());
			} catch (Exception e) {
			}
		}
		// 第4步，将文件存到浏览器设置的下载位置
		// 告诉浏览器用什么软件可以打开此文件
		response.setHeader("content-Type", "application/vnd.ms-excel");
		// 下载文件的默认名称
		response.setHeader("Content-Disposition", "attachment; filename=" + new String("俗名信息".getBytes("gb2312"), "iso8859-1") + ".xls");
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
		List<Commonname> commonnameList = new ArrayList<>();							// 上传文件的数据
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
					/*String tableHeadArray[] = { "*学名", "*俗名", "*俗名语言", "*参考文献", "*数据源", "*审核专家", "版权所有者", "版权声明",
							"共享协议", "备注" };*/
                	String tableHeadArray[] = { "学名", "俗名", "俗名语言", "参考文献", "数据源", "审核专家", "版权所有者", "版权声明",
							"共享协议", "备注" };
                    if(this.excelService.judgeRowConsistent(10,tableHeadArray,sheet.getRow(0))){
                        try {
                        	Commonname thisCommonname = new Commonname();				// Citation对象
                            Row row = null;										// 记录行
                            for (int i = 1; i <= rowNum; i++) {
                                row = sheet.getRow(i);
                                if (null == sheet.getRow(i)) { 
                                	continue; 									// 空行
                                }else {											// 构建集合对象
                                	String scientificname = excelService.getStringValueFromCell(row.getCell(0));
                                	Taxon thisTaxon = this.taxonRepository.findOneByScientificnameAndTaxasetId(scientificname, taxasetId);
                                	String remark = "{"
                                			+ "\"" + "rightsholder" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(6)) + "\","
                                			+ "\"" + "copyright" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(7)) + "\","
                                			+ "\"" + "license" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(8)) + "\","
                                			+ "\"" + "remark" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(9)) + "\"," 
                                			+ "}";
                                	thisCommonname = new Commonname(
                                			excelService.getStringValueFromCell(row.getCell(1)),
                                			this.languageService.getValueofLanguage(excelService.getStringValueFromCell(row.getCell(2))),
                                			excelService.getStringValueFromCell(row.getCell(3)),
                                			excelService.getStringValueFromCell(row.getCell(4)),
                                			excelService.getStringValueFromCell(row.getCell(5)),
                                			remark,
                                			thisTaxon);
                                	
                                	commonnameList.add(thisCommonname);
								}	
                            }
                            long start = System.currentTimeMillis();
                            JSONArray verifyListEntity = verifyListEntity(commonnameList, thisUser, refFileMark, dsFileMark, expFileMark);
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
	private JSONArray verifyListEntity(List<Commonname> list, User thisUser, String refFileMark, String dsFileMark, String expFileMark) throws Exception {
		JSONArray commonnameArr = new JSONArray();
		List<Commonname> failList = new ArrayList<>();
        boolean flag = true;
        for (int i = 0; i < list.size(); i++) {
        	Commonname thisCommonname = list.get(i);
        	boolean mark = true;
        	if (null == thisCommonname.getTaxon()) {
        		thisCommonname.setTaxon(new Taxon("<span style='color:red'>未匹配到对应分类单元，请先上传分类单元</span>"));
        		mark = false;
			}
        	if (StringUtils.isBlank(thisCommonname.getCommonname())) {
        		thisCommonname.setCommonname("<span style='color:red'>俗名不能为空</span>");
        		mark = false;
        	};
        	if (StringUtils.isBlank(thisCommonname.getLanguage())) {
        		thisCommonname.setLanguage("<span style='color:red'>俗名语言不能为空</span> ");
        		mark = false;
            };
            /*数据源*/
            if (StringUtils.isBlank(thisCommonname.getSourcesid())) {
            	thisCommonname.setSourcesid("<span style='color:red'>数据源不能为空</span>");
            	mark = false;
            }else  {
            	String source = this.baseinfotmpService.findDsIdByFilemarkAndSerialNumAndFileType(dsFileMark, thisCommonname.getSourcesid(), 1);
            	if (StringUtils.isNotBlank(source)) {
					thisCommonname.setSourcesid(source);
				}else {
					thisCommonname.setSourcesid("<span style='color:red'>未找到引用数据源，请检查引用数据是否存在或是否引用错误！</span>");
					mark = false;
				}
			};
			
			/*审核专家*/
			if (StringUtils.isBlank(thisCommonname.getExpert())) {
				thisCommonname.setExpert("<span style='color:red'>专家信息不能为空</span>");
				mark = false;
            }else{
            	boolean tmp = thisCommonname.getExpert().matches("[0-9]*");
            	String expid = null;
            	if (tmp) {
            		expid = this.baseinfotmpService.findExpIdByFilemarkAndSerialNumAndFileType(expFileMark, 2, thisCommonname.getExpert());
				}else {
					String expertId = this.expertRepository.findIdByCnName(thisCommonname.getExpert());
					if (StringUtils.isNotBlank(expertId)) {
						expid = expertId;
					}else {
						thisCommonname.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
					}
				}
            	
            	if (StringUtils.isNotBlank(expid)) {
            		thisCommonname.setExpert(expid);
				}else{
					thisCommonname.setExpert("<span style='color:red'>未找到引用专家信息，请检查引用的专家数据是否存在或是否引用错误！</span>");
					mark = false;
				}
            };
            
            /*比对参考文献*/
        	String refjson = thisCommonname.getRefjson();
        	List<String> refIds = new ArrayList<>();
        	if (StringUtils.isNotBlank(refjson)) {
        		String[] refStr = refjson.replace("，", ",").split(",");
        		refIds = this.baseinfotmpService.findRefIdByFilemarkAndSerialNumAndFileType(refFileMark, 0, refStr);
        		if (!refIds.isEmpty()) {
        			JSONArray refjsonArr = this.refService.getRefJSONArrayByIds(refIds);
        			if (!refjsonArr.isEmpty()) {
        				thisCommonname.setRefjson(refjsonArr.toString());
        			}else {
        				thisCommonname.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
						mark = false;
					}
        		}else {
        			thisCommonname.setRefjson("<span style='color:red'>未找到引用参考文献，请检查引用数据是否存在或是否引用错误！</span>");
					mark = false;
        		}
        	};
        	
        	if (!mark) {
        		thisCommonname.setSynchstatus(i + 1);
            	failList.add(thisCommonname);
            	flag = mark;
			}
        }
        /* 构建的集合若通过必填字段的校验，去重，比对数据源、参考文献，补充属性值*/
        if (flag) {
        	List<Commonname> commonnameList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
            	/* 根据学名去重*/
            	list.get(i).setId(UUIDUtils.getUUID32());
            	list.get(i).setInputer(thisUser.getId());
            	list.get(i).setInputtime(new Timestamp(System.currentTimeMillis()));
            	list.get(i).setSynchdate(new Timestamp(System.currentTimeMillis()));
            	list.get(i).setStatus(1);
            	list.get(i).setSynchstatus(0);
				/*this.commonnameRepository.save(list.get(i));*/
            	commonnameList.add(list.get(i));
            }
            long start = System.currentTimeMillis();
            this.batchInsertService.batchInsertCommonname(commonnameList);
            long end = System.currentTimeMillis();
            System.out.println("Commonname批量存储完成：" + (end - start));
        }else {
			for (int i = 0; i < failList.size(); i++) {
				JSONObject json = new JSONObject();
				json.put("num", failList.get(i).getSynchstatus());
				json.put("scientificname", failList.get(i).getTaxon().getScientificname());
				json.put("commonname", failList.get(i).getCommonname());
				json.put("language", failList.get(i).getLanguage());
				json.put("sourcesid", failList.get(i).getSourcesid());
				json.put("expert", failList.get(i).getExpert());
				commonnameArr.add(i, json);
			}
		}
        return commonnameArr;
    }

	@Override
	public List<CommonName> getCommonNameByTaxaset(String taxasetId) {
		/** select c.id, t.id, c.commonname, c.language, t.rank_id */
		List<Object[]> list = commonnameRepository.findCommonNameByTaxaset(taxasetId);
		List<CommonName> resultlist = new ArrayList<>();
		for (Object[] obj : list) {
			CommonName entity = new CommonName();
			entity.setRecordId(obj[0].toString());
			entity.setNameCode(obj[1].toString());
			entity.setCommonName(obj[2].toString());
			String langulage = obj[3].toString();
			if (StringUtils.isNotBlank(langulage)) {
				switch (langulage) {
				case "1":
					entity.setLanguage("Chinese");
					break;
				case "2":
					entity.setLanguage("English");
					break;
				case "3":
					entity.setLanguage("French");
					break;
				case "4":
					entity.setLanguage("Russian");
					break;
				case "5":
					entity.setLanguage("Spanish");
					break;
				case "6":
					entity.setLanguage("Other");
					break;
				default:
					break;
				}
			}
			entity.setCountry("China");
			entity.setDatabaseId("");
			String rank = obj[4].toString();
			if (rank.equals("42") || rank.equals("13")) {
				entity.setIsInfraspecies(1);
			}else {
				entity.setIsInfraspecies(0);
			}
			resultlist.add(entity);
		}
		return resultlist;
	}
}
