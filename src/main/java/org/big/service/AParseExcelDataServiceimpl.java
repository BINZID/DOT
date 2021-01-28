package org.big.service;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.big.common.TransApi;
import org.big.entity.Category;
import org.big.entity.Quarantineprodb;
import org.big.repository.CategoryRepository;
import org.big.repository.GeoobjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class AParseExcelDataServiceimpl implements AParseExcelDataService {
	@Autowired
	private ExcelService excelService;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private QuarantineprodbRepository quarantineprodbRepository;
	@Autowired
	private GeoobjectRepository geoobjectRepository;
	@Override
	public void parseCategoryData(String path) throws Exception {
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
			}else {
				String id = excelService.getStringValueFromCell(row.getCell(0));
				String tableCn = excelService.getStringValueFromCell(row.getCell(1));
				String tableEn = excelService.getStringValueFromCell(row.getCell(2));
				String fieldCn = excelService.getStringValueFromCell(row.getCell(3));
				String fieldEn = excelService.getStringValueFromCell(row.getCell(4));
				String content = excelService.getStringValueFromCell(row.getCell(5));
				String pid = excelService.getStringValueFromCell(row.getCell(6));
				String sort = excelService.getStringValueFromCell(row.getCell(7));
				String status = excelService.getStringValueFromCell(row.getCell(8));
				Category category = new Category(id, tableCn, tableEn, fieldCn, fieldEn, content, null, Integer.parseInt(sort), 1);
				this.categoryRepository.save(category);
			}	
		}
	}

	@Override
	public String getPY(String cname) throws Exception {
		String str="";
		if (StringUtils.isBlank(cname)) {
			return str;
		}
		cname=cname.trim();
		System.out.println(cname);
		if(cname.contains("（")){
			cname=cname.replace("（", "(").replace("）", ")");
		}
		cname=cname.replace(" ", "");
		String pyUrl="http://chinese.biodinfo.org/ChineseCharactorWebService.asmx/GetWordPinyinGoogle";
		String param="word="+cname+"&psd=";
		pyUrl=pyUrl+"?"+param;
		//ResponseEntity<String> results = restTemplate.exchange(pyUrl, HttpMethod.GET, null, String.class);
		//String document = results.getBody();
		/*SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(pyUrl);*/

		/*if(document!=null){
			Element root = document.getRootElement();
			str=(String) root.getData();
			System.out.println(document.toString());
		}*/
		return str;
	}

	@Override
	public void parseProdbData(String path) throws Exception {
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
				String id = excelService.getStringValueFromCell(row.getCell(0));
				String type = excelService.getStringValueFromCell(row.getCell(1));
				String host = excelService.getStringValueFromCell(row.getCell(2));
				String pest = excelService.getStringValueFromCell(row.getCell(3));
				String measures = excelService.getStringValueFromCell(row.getCell(4));
				String remark = excelService.getStringValueFromCell(row.getCell(5));
				String htmlcode = excelService.getStringValueFromCell(row.getCell(6));
				
				Quarantineprodb quarantineprodb = new Quarantineprodb();
				if (StringUtils.isNotBlank(htmlcode)) {
					quarantineprodb.setHtmlcode(htmlcode);
					if (!htmlcode.endsWith("table>")) {
						System.out.println(id + "\t" + type + "\t" + host + "\t" + pest + "\t" + measures);
					}
				}
				quarantineprodb.setId(id);
				quarantineprodb.setType(type);
				quarantineprodb.setHost(host);
				quarantineprodb.setPest(pest);
				quarantineprodb.setMeasures(measures);
				quarantineprodb.setInputtime(new Timestamp(System.currentTimeMillis()));
				quarantineprodb.setStatus(1);
				quarantineprodb.setRemark(remark);
				quarantineprodbRepository.save(quarantineprodb);
			}
		}
	}
	@Override
	public void getEnnameOfDistribution(String path) throws Exception {
		String APP_ID = "20200811000539945";
		String SECURITY_KEY = "ZpwiMTC8ZR81d95VaZfR";
		/*List<Geoobject> list = this.geoobjectRepository.findAllGeoobject();
		String APP_ID = "20200811000539945";
		String SECURITY_KEY = "ZpwiMTC8ZR81d95VaZfR";

		for (Geoobject geoobject : list) {
			String cngeoname = geoobject.getCngeoname();
			TransApi api = new TransApi(APP_ID, SECURITY_KEY);
			String result = api.getTransResult(cngeoname, "zh", "en");
	        String arrStr = JSONObject.parseObject(result).getString("trans_result");
	        JSONArray array = JSONArray.parseArray(arrStr);
	        JSONObject jsonObj = array.getJSONObject(0);
	        String engeoname = jsonObj.getString("dst");
			System.out.println(cngeoname + "\t" + engeoname);
			
		}*/
		
		// 创建XSSFWorkbook
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		TransApi api = new TransApi(APP_ID, SECURITY_KEY);
		for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				
				String id = excelService.getStringValueFromCell(row.getCell(0));
				String cngeoname = excelService.getStringValueFromCell(row.getCell(1));
				String engeoname = excelService.getStringValueFromCell(row.getCell(2));
				String shortname = excelService.getStringValueFromCell(row.getCell(3));
				String adcode = excelService.getStringValueFromCell(row.getCell(4));
				String pid = excelService.getStringValueFromCell(row.getCell(5));
				String geotype = excelService.getStringValueFromCell(row.getCell(6));
				String version = excelService.getStringValueFromCell(row.getCell(7));
				String centerx = excelService.getStringValueFromCell(row.getCell(8));
				String centery = excelService.getStringValueFromCell(row.getCell(9));
				String citycode = excelService.getStringValueFromCell(row.getCell(10));
				String geogroup_id = excelService.getStringValueFromCell(row.getCell(11));
				String geodata = excelService.getStringValueFromCell(row.getCell(12));
				String relation = excelService.getStringValueFromCell(row.getCell(13));
				String status = excelService.getStringValueFromCell(row.getCell(14));
				String inputer = excelService.getStringValueFromCell(row.getCell(15));
				String inputtime = excelService.getStringValueFromCell(row.getCell(16));
				String synchstatus = excelService.getStringValueFromCell(row.getCell(17));
				String synchdate = excelService.getStringValueFromCell(row.getCell(18));
				String remark = excelService.getStringValueFromCell(row.getCell(19));
				
				String result = api.getTransResult(cngeoname, "zh", "en");
		        String arrStr = JSONObject.parseObject(result).getString("trans_result");
		        JSONArray array = JSONArray.parseArray(arrStr);
		        JSONObject jsonObj = array.getJSONObject(0);
		        String engeonamedest = jsonObj.getString("dst");
				System.out.println(id + "\t" + cngeoname + "\t" + engeonamedest + "\t" + shortname + "\t" + adcode + "\t"
						+ pid + "\t" + geotype + "\t" + version + "\t" + centerx + "\t" + centery + "\t" + citycode + "\t" 
						+ "A1A25AA0D98C4441997D891A229F35E6" + "\t" + geodata + "\t" + relation + "\t" + status + "\t" + inputer + "\t" + "2020/9/21 11:10:06" + "\t"
						+ synchstatus + "\t" + synchdate + "\t" + remark);
				Thread.sleep(1*1000L);
			}
		}
	}
}
