package org.big.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.big.common.UUIDUtils;
import org.big.entity.Customs;
import org.big.entity.Entitys;
import org.big.entity.Rank;
import org.big.entity.Taxaset;
import org.big.entity.Taxon;
import org.big.repository.EntitysRepository;
import org.big.repository.RankRepository;
import org.big.repository.TaxasetRepository;
import org.big.repository.TaxonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
@Service
public class AScbServiceImpl implements AScbService{
	@Autowired
	private ExcelService excelService;
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private TaxasetRepository taxasetRepository;
	@Autowired
	private RankRepository rankRepository;
	@Autowired
	private EntitysRepository entitysRepository;
	@Autowired
	private CustomsRepository customsRepository;

	public JSON confirmSCB(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(1);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum();
		String inputer = "3a29945023d04ef8a134f0f017d31kkk";
		XSSFRow row = null;
		List<String> addDataList = new ArrayList<>();
		List<String> markDataList = new ArrayList<>();
		List<String> errorDataList = new ArrayList<>();
		List<String> matchDataList = new ArrayList<>();
		Set<String> scinameSet = new HashSet<>();
		Taxon taxon = new Taxon();
		for (int rowNum = 0; rowNum < rowNums; rowNum++) {
			row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				String sciname = excelService.getStringValueFromCell(row.getCell(0)).trim();
				String chname = excelService.getStringValueFromCell(row.getCell(1));
				String importance = excelService.getStringValueFromCell(row.getCell(2));
				String type = excelService.getStringValueFromCell(row.getCell(3));
				String count = excelService.getStringValueFromCell(row.getCell(4));
				try {
					type = getTypeValue(type);
					if (sciname.contains("?") || sciname.contains("？")) {
						String markDataStr = sciname + "\t" + chname + "\t" + importance + "\t" + type + "\t" + count;
						markDataList.add(markDataStr);
						sciname = sciname.replace("?", " ").replace("？", " ").trim();
					}
					scinameSet.add(sciname);	// 记录总数据量 - 不重复
					taxon = taxonRepository.findByScientificnameAndInputer(sciname, inputer);
					if (null != taxon) {
						String matchDataStr = new String();
						if (taxon.getChname().equals(chname)) {
							matchDataStr = sciname + "\t" + chname + "\t" + importance + "\t" + type + "\t" + count;
						} else {
							matchDataStr = sciname + "\t" + chname + "\t" + importance + "\t" + type + "\t" + count + "\t" + taxon.getChname();
						}
						matchDataList.add(matchDataStr);
					} else {
						String addDataStr = sciname + "\t" + chname + "\t" + importance + "\t" + type + "\t" + count;
						addDataList.add(addDataStr);
					}
				} catch (Exception e) {
					String errorDataStr = sciname + "\t" + chname + "\t" + importance + "\t" + type + "\t" + count;
					errorDataList.add(errorDataStr);
				}
			}
		}
		System.out.println();
		System.out.println("文件数据大小：" + rowNums);
		System.out.println("Set集合大小：" + scinameSet.size());
		System.out.println("match数据 ：" + matchDataList.size());
		for (String matchDataStr : matchDataList) {
			System.out.println(matchDataStr);
		}
		System.out.println("mark数据 ：" + markDataList.size());
		for (String markDataStr : markDataList) {
			System.out.println(markDataStr);
		}
		System.out.println("异常数据 ：" + errorDataList.size());
		for (String errorDataStr : errorDataList) {
			System.out.println(errorDataStr);
		}
		System.out.println("有害名录要补充的数据：" + addDataList.size());
		for (String addDataStr : addDataList) {
			System.out.println(addDataStr);
		}
		return null;
	}

	private String getTypeValue(String type) {
		String rsl = new String();
		if ("I".equals(type) && "i".equals(type)) {
			rsl = "昆虫";
		} else if ("M".equals(type) && "m".equals(type)) {
			rsl = "螨类";
		} else if ("W".equals(type) && "w".equals(type)) {
			rsl = "杂草";
		} else if ("N".equals(type) && "n".equals(type)) {
			rsl = "线虫";
		} else if ("F".equals(type) && "f".equals(type)) {
			rsl = "真菌";
		} else if ("B".equals(type) && "b".equals(type)) {
			rsl = "细菌";
		} else if ("V".equals(type) && "v".equals(type)) {
			rsl = "病毒";
		} else if ("O".equals(type) && "o".equals(type)) {
			rsl = "其他";
		} else {
			rsl = "未知";
		}
		return rsl;
	}

	@Override
	public JSON shandongPis(String path) throws Exception {
		Taxaset taxaset = this.taxasetRepository.findOneById("fb0e45204b414426844da5b136df9c12");
		Rank rank = this.rankRepository.findOneById("1");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(1);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum();
		String inputer = "1922a5f7346840aa88c99e8155b3e252";
		XSSFRow row = null;
		String infoStr = new String();
		String sciname = new String();
		String chname = new String();
		String chapter = new String();
		String section = new String();
		String title = new String();
		String type = new String();
		String importance = new String();
		for (int rowNum = 0; rowNum < rowNums; rowNum++) {
			row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				infoStr = excelService.getStringValueFromCell(row.getCell(0)).trim();
				sciname = excelService.getStringValueFromCell(row.getCell(1));
				chname = excelService.getStringValueFromCell(row.getCell(2));
				if (!infoStr.equals(" ")) {
					if (infoStr.contains("第") && infoStr.contains("章")) {
						chapter = infoStr;
					} else if (infoStr.contains("第") && infoStr.contains("节")) {
						int indexOf = infoStr.indexOf("节");
						section = infoStr.substring(indexOf + 1).trim();
					} else if (infoStr.contains("检疫性有害生物名单") && !infoStr.contains("非检疫性有害生物名单")) {
						importance = "检疫性有害";
					} else if (infoStr.contains("非检疫性有害生物名单")) {
						importance = "非检疫性有害";
					} else if (infoStr.contains("有害生物类别")) {
						title = infoStr;
					} else if (infoStr.contains("昆虫") || infoStr.contains("杂草") || infoStr.contains("线虫") || 
							infoStr.contains("真菌") || infoStr.contains("细菌") || infoStr.contains("植原体") ||
							infoStr.contains("病毒") || infoStr.contains("软体动物") || infoStr.contains("螨类") ||
							infoStr.contains("其他") || infoStr.contains("昆虫及其他") || infoStr.contains("类病毒") || 
							infoStr.contains("细菌、植原体")) {
						type = infoStr;
						
						System.out.println(type + "\t" + sciname + "\t" + chname + "\t" + 
								importance + "\t" + section);
						
						Taxon taxon = new Taxon(UUIDUtils.getUUID32(), 
								sciname, 
								chname, 
								"1", 
								"095c4b72-9907-47ea-93b6-03cabd36f422", 
								section, 
								inputer, 
								timestamp, 
								1, 
								timestamp, 
								0, 
								taxaset, 
								rank);
						taxon.setExpert(type);
						taxon.setRefClassSys(importance);
						taxonRepository.save(taxon);
					}
				}
			}
		}
		return null;
	}

	@Override
	public JSON parseShandongPis(String path) throws Exception {
		/**
		 * 1.从Sciname字段中拆出拉丁名、命名信息、种/亚种加词
		 * 2.确认分类等级，目前默认都是界 - 部分
		 * 		确定原数据最高分类等级是什么？
		 * 		现有入库数据如何确定分类等级？
		 * 3.从Remark字段中拆出国家、进口物品 - 部分
		 * 4.核查数据是否录入有误 - 完成
		 */
		List<Taxon> list = this.taxonRepository.findTaxonByTaxasetId("fb0e45204b414426844da5b136df9c12");
		List<String> infoList = new ArrayList<>();
		String regex = "^[A-Z].*?";
		for (Taxon taxon : list) {
			String scientificname = taxon.getScientificname();
			String chname = taxon.getChname();
			String rankId = taxon.getRankid();
			String rank = getRankByChname(rankId, scientificname, chname);
			System.out.println("学名：" + scientificname);
			if (!scientificname.contains("sp.") && !scientificname.contains("spp.") && 
					!scientificname.contains("virus") && !scientificname.contains("(non-") && 
					!scientificname.contains("(The species")) {
				if (scientificname.contains("(")) {
					int indexOf = scientificname.indexOf("(");
					String author = scientificname.substring(indexOf);
					scientificname = scientificname.replace(author, "").trim();
					taxon.setAuthorstr(author);
					taxon.setScientificname(scientificname);
					
					String[] split = scientificname.split(" ");
					String epithet = new String();
					if (split.length == 1) {
					} else if (split.length == 2) {
						epithet = split[1];
						taxon.setEpithet(epithet);
					}
					System.out.println("格式1：" + scientificname + "\t" + author + "\t" + epithet);
				} else {
					String[] split = scientificname.split(" ");
					if (split.length == 2) {
						String mark = split[1];
						if (mark.matches(regex)) {
							taxon.setAuthorstr(mark);
						} else {
							taxon.setEpithet(mark);
						}
						taxon.setScientificname(scientificname);
						System.out.println("格式3：" + scientificname + "\t" + mark);
					} else if (split.length == 3) {
						String mark = split[split.length - 1];
						if (mark.matches(regex)) {
							taxon.setAuthorstr(mark);
							scientificname = scientificname.replace(mark, "").trim();
							taxon.setScientificname(scientificname);
							taxon.setEpithet(split[split.length - 2]);
						} else {
							taxon.setScientificname(scientificname);
						}
						System.out.println("格式4：" + scientificname + "\t" + mark);
					}
					for (int i = 0; i < split.length; i++) {
						split[i] = split[i].trim();
						if (i != 0 && split[i].matches(regex)) {
							String mark = split[i];
							taxon.setScientificname(scientificname.replace(mark, "").trim());
							taxon.setAuthorstr(mark);
							String epithet = new String();
							if (split.length == 3) {
								epithet = split[1];
								taxon.setEpithet(epithet);
							}
							System.out.println("格式2：" + "拉丁名-"+ scientificname.replace(mark, "").trim() + "\t" + mark + "\t" + epithet);
						}
					}
				}
				/*if (count != 1 ) {
					mark = split[split.length - 1] + " " + mark;
				}*/
			}
			this.taxonRepository.save(taxon);
		}
		/*// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(4);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum();
		XSSFRow row = null;
		for (int rowNum = 0; rowNum < rowNums; rowNum++) {
			row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				String info = excelService.getStringValueFromCell(row.getCell(0)).trim();
				String country = excelService.getStringValueFromCell(row.getCell(2));
				String thing = excelService.getStringValueFromCell(row.getCell(3)).trim();
				代码
			}
		}*/
		return null;
	}
		
	private String getRankByChname(String rankId, String scientificname, String chname) {
		String rsl = new String();
		try {
			if (chname.endsWith("界")) {
				rsl = "1";
			} else if (chname.endsWith("门")) {
				rsl = "2";
			} else if (chname.endsWith("纲")) {
				rsl = "3";
			} else if (chname.endsWith("目")) {
				rsl = "4";
			} else if (chname.endsWith("科")) {
				rsl = "5";
			} else if (chname.endsWith("亚门")) {
				rsl = "8";
			} else if (chname.endsWith("亚纲")) {
				rsl = "9";
			} else if (chname.endsWith("亚目")) {
				rsl = "10";
			} else if (chname.endsWith("亚科")) {
				rsl = "11";
			} else if (chname.endsWith("亚属")) {
				rsl = "12";
			} else if (chname.endsWith("亚种") || scientificname.contains("subsp.")) {
				rsl = "42";
			} else {
				if (scientificname.contains("sp.") || scientificname.contains("spp.") ||
						scientificname.contains("(non-") || scientificname.contains("(The species")) {
					rsl = "6";
				} else if (scientificname.contains("var.")) {
					rsl = "31";
				} else if (scientificname.contains("pv.")) {
					rsl = "7";
				} else {
					rsl = "7";
				}
			}
		} catch (Exception e) {
			rsl = "7";
		}
		return rsl;
	}

	@Override
	public JSON wordTransformExcel(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum();
		XSSFRow row = null;
		String type = new String();
		String sciname = new String();
		String chname = new String();
		for (int rowNum = 0; rowNum < rowNums; rowNum++) {
			row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				String data = excelService.getStringValueFromCell(row.getCell(0)).trim();
				if (data.equals("昆虫")) {
					type = "昆虫";
				} else if (data.equals("软体动物")) {
					type = "软体动物";
				} else if (data.equals("真菌")) {
					type = "真菌";
				} else if (data.equals("原核生物")) {
					type = "原核生物";
				} else if (data.equals("线虫")) {
					type = "线虫";
				} else if (data.equals("病毒及类病毒")) {
					type = "病毒及类病毒";
				} else if (data.equals("杂草")) {
					type = "杂草";
				} else {
					String sub = data.substring(0, 1);
					if (isEnglish(sub)) {
						sciname = data;
					} else {
						chname = data;
						System.out.println(sciname + "\t" + chname + "\t" + type);
					}
				}
			}
		}
		return null;
	}
	
	public boolean isEnglish(String charaString) {
		if (StringUtils.isNotBlank(charaString)) {
			return charaString.matches("^[a-zA-Z]*");
		}else {
			return false;
		}
	}

	@Override
	public JSON matchPis(String path1, String path2) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook1 = new XSSFWorkbook(path1);
		XSSFWorkbook workbook2 = new XSSFWorkbook(path2);
		// 获取sheet工作表
		XSSFSheet sheet1 = workbook1.getSheetAt(0);
		XSSFSheet sheet2 = workbook2.getSheetAt(0);
		// 获取总记录数
		int rowNums1 = sheet1.getLastRowNum(); // 439
		int rowNums2 = sheet2.getLastRowNum(); // 13195
		XSSFRow row1 = null;
		XSSFRow row2 = null;
		
		
		for (int rowNum2 = 1; rowNum2 < rowNums2; rowNum2++) {
			row2 = sheet2.getRow(rowNum2);
			if (null == row2) {
				continue;
			} else {
				try {
					String sciname2 = excelService.getStringValueFromCell(row2.getCell(0)).trim();
					String chname2 = excelService.getStringValueFromCell(row2.getCell(1));
					String importance = excelService.getStringValueFromCell(row2.getCell(2));
					String type2 = excelService.getStringValueFromCell(row2.getCell(3));
					String count = excelService.getStringValueFromCell(row2.getCell(4));
					String recount = excelService.getStringValueFromCell(row2.getCell(5));
					String reChname = excelService.getStringValueFromCell(row2.getCell(6));
					if (StringUtils.isBlank(chname2)) {
						chname2 = "null";
					}
					if (sciname2.contains("spp.")) {
						int indexOf = sciname2.indexOf("spp.");
						sciname2 = sciname2.substring(0, indexOf).trim();
					} else if (sciname2.contains("sp.")) {
						int indexOf = sciname2.indexOf("sp.");
						sciname2 = sciname2.substring(0, indexOf).trim();
					} else if (sciname2.contains("(")) {
						int start = sciname2.indexOf("(");
						int end = sciname2.indexOf(")");
						String mark = sciname2.substring(start + 1, end);
						sciname2 = sciname2.replace(mark, "");
					}
					
					for (int rowNum = 1; rowNum < rowNums1; rowNum++) {
						row1 = sheet1.getRow(rowNum);
						if (null == row1) {
							continue;
						} else {
							String sciname = excelService.getStringValueFromCell(row1.getCell(1)).trim();
							String resciname = sciname;
							String chname = excelService.getStringValueFromCell(row1.getCell(3));
							//String type = excelService.getStringValueFromCell(row1.getCell(4));
							
							sciname = sciname.replace("(non-Chinese species)", "").replace("(non-Chinese )", "")
									.replace("(non-Chinese)", "").replace("(maculatus（F.）and non-Chinese)", "")
									.replace("(valens LeConte and non-Chinese)", "")
									.replace("(The species transmit viruses)", "").trim();
							/*if (sciname.contains("virus")) {
								int indexOf = sciname.indexOf("virus");
								sciname = sciname.substring(0, indexOf).trim();
							} else if (sciname.contains("viroid")) {
								int indexOf = sciname.indexOf("viroid");
								sciname = sciname.substring(0, indexOf).trim();
							} else if (sciname.contains("subsp")) {
								int indexOf = sciname.indexOf("subsp");
								sciname = sciname.substring(0, indexOf).trim();
							} else if (sciname.contains("pv.")) {
								int indexOf = sciname.indexOf("pv.");
								sciname = sciname.substring(0, indexOf).trim();
							} else*/ 
							
							if (sciname.contains("spp.")) {
								int indexOf = sciname.indexOf("spp.");
								sciname = sciname.substring(0, indexOf).trim();
							} else if (sciname2.contains("sp.")) {
								int indexOf = sciname.indexOf("sp.");
								sciname2 = sciname2.substring(0, indexOf).trim();
							} else if (sciname.contains("-")) {
								int indexOf = sciname.indexOf("-");
								sciname = sciname.substring(0, indexOf).trim();
							} else if (sciname.contains("’")) {
								int indexOf = sciname.indexOf("’");
								sciname = sciname.substring(0, indexOf).trim();
							} else if (sciname.contains("(")) {
								int start = sciname.indexOf("(");
								int end = sciname.indexOf(")");
								String mark = sciname.substring(start + 1, end);
								sciname = sciname.replace(mark, "");
							}
							
							// 检验检疫的分类等级 <= 中国物种
							//if (sciname2.contains(sciname) || sciname .contains(sciname2)) {
							if (sciname2.contains(sciname)) {
								System.out.println(resciname + "\t" + chname + "\t" + 
										sciname2 + "\t" + chname2 + "\t" + importance + "\t" +
										type2 + "\t" + count + "\t" + recount + "\t" + reChname);
							}
						}
					}
					
					/*System.out.println(sciname2 + "\t" + chname2 + "\t" + importance + "\t" +
							type2 + "\t" + count + "\t" + recount + "\t" + reChname);*/
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}
		}
		
		return null;
	}

	@Override
	public JSON checkPis() throws Exception {
		List<Taxon> list = this.taxonRepository.findTaxonByTaxasetId("fb0e45204b414426844da5b136df9c12");
		String regex = "^[A-Z].*?";
		/*for (Taxon taxon : list) {
			String scientificname = taxon.getScientificname();
			int count = 0;
			String[] split = scientificname.split(" ");
			for (int i = 0; i < split.length; i++) {
				String para = split[i].trim();
				if (para.matches(regex)) {
					count++;
				}
			}
			if (count > 1) {
				System.out.println(taxon.getId());
			}
		}*/
		/*for (Taxon taxon : list) {
			taxon.setScientificname(taxon.getScientificname().trim());
			this.taxonRepository.save(taxon);
		}*/
		for (Taxon taxon : list) {
			String scientificname = taxon.getScientificname();
			int count = 0;
			String[] split = scientificname.split(" ");
			for (int i = 0; i < split.length; i++) {
				String para = split[i].trim();
				if (para.matches(regex)) {
					count++;
				}
			}
			if (count == 0) {
				System.out.println(taxon.getId());
			}
		}
		return null;
	}

	@Override
	public JSON getFinalData(String path1, String path2) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook1 = new XSSFWorkbook(path1);
		XSSFWorkbook workbook2 = new XSSFWorkbook(path2);
		// 获取sheet工作表
		XSSFSheet sheet1 = workbook1.getSheetAt(1);
		XSSFSheet sheet2 = workbook2.getSheetAt(0);
		// 获取总记录数
		int rowNums1 = sheet1.getLastRowNum(); // 439
		int rowNums2 = sheet2.getLastRowNum(); // 13195
		XSSFRow row1 = null;
		XSSFRow row2 = null;
		
		
		for (int rowNum2 = 1; rowNum2 < rowNums2; rowNum2++) {
			row2 = sheet2.getRow(rowNum2);
			if (null == row2) {
				continue;
			} else {
				try {
					String sciname2 = excelService.getStringValueFromCell(row2.getCell(0)).trim();
					String chname2 = excelService.getStringValueFromCell(row2.getCell(1));
					String importance = excelService.getStringValueFromCell(row2.getCell(2));
					String type2 = excelService.getStringValueFromCell(row2.getCell(3));
					String count = excelService.getStringValueFromCell(row2.getCell(4));
					String recount = excelService.getStringValueFromCell(row2.getCell(5));
					String reChname = excelService.getStringValueFromCell(row2.getCell(6));
					System.out.println(sciname2 + "\t" + chname2 + "\t" + importance + "\t" +
							type2 + "\t" + count + "\t" + recount + "\t" + reChname);
					for (int rowNum = 1; rowNum < rowNums1; rowNum++) {
						row1 = sheet1.getRow(rowNum);
						if (null == row1) {
							continue;
						} else {
							String sciname = excelService.getStringValueFromCell(row1.getCell(0)).trim();
							if (sciname2.equals(sciname)) {
								importance = "1";
								System.out.println(sciname2 + "\t" + chname2 + "\t" + importance + "\t" +
										type2 + "\t" + count + "\t" + recount + "\t" + reChname);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}
		}
		return null;
	}

	@Override
	public JSON trimData(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum(); // 439
		XSSFRow row = null;
		int num = 0;
		String mark = "";
		for (int rowNum = 1; rowNum < rowNums; rowNum++) {
			row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				try {
					String sciname = excelService.getStringValueFromCell(row.getCell(0)).trim();
					String chname = excelService.getStringValueFromCell(row.getCell(1));
					String ranks = excelService.getStringValueFromCell(row.getCell(2)).trim();
					String important = excelService.getStringValueFromCell(row.getCell(3)).trim();
					String type = excelService.getStringValueFromCell(row.getCell(4)).trim();
					int count = Integer.parseInt(excelService.getStringValueFromCell(row.getCell(5)).trim());
					
					if (sciname.equals(mark)) {
						num = num + count;
						System.out.println(sciname + "\t" + chname + "\t" + important + "\t" +
								type + "\t" + num + "\t" + ranks);
					} else {
						num = count;
						mark = sciname;
						/*System.out.println(sciname + "\t" + chname + "\t" + important + "\t" +
								type + "\t" + count + "\t" + ranks);*/
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public JSON getData() throws Exception {
		List<Entitys> list = this.entitysRepository.findListEntitys();
		System.out.println(list.size());
		int num = 0;
		String mark = "";
		for (Entitys entitys : list) {
			int count = Integer.parseInt(entitys.getCount());
			String sciname = entitys.getSciname();
			String chname = entitys.getChname();
			String type = entitys.getType();
			String importance = entitys.getImportant();
			String ranks = entitys.getRanks();
			if (sciname.length() == mark.length() && sciname.contains(mark)) {
				num = num + count;
				System.out.println(sciname + "\t" + chname + "\t" + importance + "\t" +
						type + "\t" + num + "\t" + ranks);
			} else {
				num = count;
				mark = sciname;
				/*System.out.println(sciname + "\t" + chname + "\t" + importance + "\t" +
						type + "\t" + count + "\t" + ranks);*/
				System.out.println(mark);
			}
			
		}
		return null;
	}

	@Override
	public JSON rename(String path1, String path2) throws Exception {
		/**
		 * String path1 = "E:/检验检疫物种列表/中华人民共和国进境植物检疫性有害生物名录-439种.xlsx";
		 * ID	拉丁名	命名信息	中文名	类别
    	 * String path2 = "E:/检疫检验统计2003-2019/检疫检验统计2003-2019-按类别、重要性分16表.xlsx";
    	 * 拉丁名	中文名	重要性	类别	次数	次数修改备注
		 */
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook1 = new XSSFWorkbook(path1);
		XSSFWorkbook workbook2 = new XSSFWorkbook(path2);
		// 获取sheet工作表
		XSSFSheet sheet1 = workbook1.getSheetAt(0);
		XSSFSheet sheet2 = workbook2.getSheetAt(0);
		// 获取总记录数
		int rowNums1 = sheet1.getLastRowNum(); // 439
		int rowNums2 = sheet2.getLastRowNum(); // 13195
		XSSFRow row1 = null;
		XSSFRow row2 = null;
		
		List<String> reScinameList = new ArrayList<>();
		List<String> reChnameList = new ArrayList<>();
		List<String> reTypeList = new ArrayList<>();
		for (int rowNum2 = 1; rowNum2 < rowNums2; rowNum2++) {
			row2 = sheet2.getRow(rowNum2);
			if (null == row2) {
				continue;
			} else {
				try {
					String sciname2 = excelService.getStringValueFromCell(row2.getCell(0)).trim();
					String chname2 = excelService.getStringValueFromCell(row2.getCell(1));
					String importance = excelService.getStringValueFromCell(row2.getCell(2));
					String type2 = excelService.getStringValueFromCell(row2.getCell(3));
					String count = excelService.getStringValueFromCell(row2.getCell(4));
					String recount = excelService.getStringValueFromCell(row2.getCell(5));
					for (int rowNum = 1; rowNum < rowNums1; rowNum++) {
						row1 = sheet1.getRow(rowNum);
						if (null == row1) {
							continue;
						} else {
							String sciname = excelService.getStringValueFromCell(row1.getCell(1)).trim();
							String chname = excelService.getStringValueFromCell(row1.getCell(3)).trim();
							String type = excelService.getStringValueFromCell(row1.getCell(4)).trim();
							if (sciname.equals(sciname2) && !chname.equals(chname2)) {	// 拉丁名相同、中文名不同
								String paras = sciname + "\t" + sciname2 + "\t" + chname + "\t" + 
										chname2 + "\t" + importance + "\t" + type2 + "\t" + count + "\t" + recount;
								System.out.println("情景1中文名不同：" + paras);
								if (!type.equals(type2)) {
									String strRsl = "\t" + sciname + "\t" + sciname2 + "\t" + chname + "\t" + chname2 + "\t" + 
											importance + "\t" + type + "\t" + type2 + "\t" + count + "\t" + recount;
									System.out.println("情景1分类不同：" + strRsl);
								}
							}
							if (!sciname.equals(sciname2) && chname.equals(chname2)) {	// 拉丁名不同、中文名相同
								String paras = sciname + "\t" + sciname2 + "\t" + chname + "\t" + 
										chname2 + "\t" + importance + "\t" + type2 + "\t" + count + "\t" + recount;
								System.out.println("情景2拉丁名不同：" + paras);
								if (!type.equals(type2)) {
									String strRsl = "\t" + sciname + "\t" + sciname2 + "\t" + chname + "\t" + chname2 + "\t" + 
											importance + "\t" + type + "\t" + type2 + "\t" + count + "\t" + recount;
									System.out.println("情景2分类不同：" + strRsl);
								}
							}
							if (sciname.equals(sciname2) && chname.equals(chname2) && !type.equals(type2)) {
								String paras = sciname + "\t" + sciname2 + "\t" + chname + "\t" + chname2 + "\t" + 
										importance + "\t" + type + "\t" + type2 + "\t" + count + "\t" + recount;
								System.out.println("情景3分类不同：" + paras);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public JSON matchRank(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum(); 
		System.out.println("总记录数：" + rowNums);
		XSSFRow row = null;
		for (int rowNum = 1; rowNum < rowNums; rowNum++) {
			row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				try {
					String sciname = excelService.getStringValueFromCell(row.getCell(0)).trim();
					String chname = excelService.getStringValueFromCell(row.getCell(1));
					String important = excelService.getStringValueFromCell(row.getCell(2));
					String type = excelService.getStringValueFromCell(row.getCell(3));
					String count = excelService.getStringValueFromCell(row.getCell(4));
					String recount = excelService.getStringValueFromCell(row.getCell(5));
					String rechname = excelService.getStringValueFromCell(row.getCell(6));
					String ranks = excelService.getStringValueFromCell(row.getCell(7));
					if (StringUtils.isNotBlank(ranks)) {
						System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
								+ "\t" + recount + "\t" + rechname + "\t" + ranks);
					} else {
						int length = sciname.split(" ").length;
						if (length == 2) {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "种");
						} else {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "");
						}
					}
					/*System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
							+ "\t" + recount + "\t" + rechname + "\t" + ranks);*/
					/*if (chname.endsWith("界")) {
						System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
								+ "\t" + recount + "\t" + rechname + "\t" + "界");
						num++;
					} else if (chname.endsWith("门")) {
						if (chname.endsWith("总门") || chname.endsWith("超门")) {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "总门");
							num++;
						} else if (chname.endsWith("亚门")) {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "亚门");
							num++;
						} else {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "门");
							num++;
						}
					} else if (chname.endsWith("纲")) {
						if (chname.endsWith("总纲") || chname.endsWith("超纲")) {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "总纲");
							num++;
						} else if (chname.endsWith("亚纲")) {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "亚纲");
							num++;
						} else {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "纲");
							num++;
						}
					} else if (chname.endsWith("目")) {
						if (chname.endsWith("总目") || chname.endsWith("超目")) {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "总目");
							num++;
						} else if (chname.endsWith("亚目")) {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "亚目");
							num++;
						} else {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "目");
							num++;
						}
					} else if (chname.endsWith("科")) {
						if (chname.endsWith("总科") || chname.endsWith("超科")) {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "总科");
							num++;
						} else if (chname.endsWith("亚科")) {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "亚科");
							num++;
						} else {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "科");
							num++;
						}
					} else if (chname.endsWith("属")) {
						if (chname.endsWith("总属") || chname.endsWith("超属")) {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "总属");
							num++;
						} else if (chname.endsWith("亚属")) {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "亚属");
							num++;
						} else {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "属");
							num++;
						}
					} else if (chname.endsWith("种")) {
						if (chname.endsWith("亚种")) {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "亚种");
						} else {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "种");
							num++;
						}
					} else {
						if (sciname.contains("sp.") || sciname.contains("spp.")) {
							System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
									+ "\t" + recount + "\t" + rechname + "\t" + "属");
							num++;
						} else {
							int length = sciname.split(" ").length;
							if (length == 2) {
								System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
										+ "\t" + recount + "\t" + rechname + "\t" + "种");
								num++;
							} else {
								System.out.println(sciname + "\t" + chname + "\t" + important + "\t" + type + "\t" + count
										+ "\t" + recount + "\t" + rechname + "\t" + "");
								num++;
							}
						}
					}*/
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public JSON mergeData(String path) throws Exception {
		String datasource = "山东口岸进境植物及其产品关注有害生物名录,检验检疫2003-2019";
				
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet7 = workbook.getSheetAt(7);
		XSSFSheet sheet8 = workbook.getSheetAt(8);
		// 获取总记录数
		int rowNums7 = sheet7.getLastRowNum(); 
		int rowNums8 = sheet8.getLastRowNum(); 
		System.out.println("总记录数1：" + rowNums7);
		System.out.println("总记录数2：" + rowNums8);
		XSSFRow row7 = null;
		XSSFRow row8 = null;
		for (int rowNum7 = 1; rowNum7 < rowNums7; rowNum7++) {
			row7 = sheet7.getRow(rowNum7);
			if (null == row7) {
				continue;
			} else {
				try {
					String sciname7 = excelService.getStringValueFromCell(row7.getCell(0)).trim();
					String chname7 = excelService.getStringValueFromCell(row7.getCell(1));
					String ranks7 = excelService.getStringValueFromCell(row7.getCell(2));
					String important7 = excelService.getStringValueFromCell(row7.getCell(3));
					String type7 = excelService.getStringValueFromCell(row7.getCell(4));
					Integer count7 = Integer.parseInt(excelService.getStringValueFromCell(row7.getCell(5)));
					
					for (int rowNum8 = 1; rowNum8 < rowNums8; rowNum8++) {
						row8 = sheet8.getRow(rowNum8);
						if (null == row7) {
							continue;
						} else {
							String sciname8 = excelService.getStringValueFromCell(row8.getCell(0)).trim();
							String chname8 = excelService.getStringValueFromCell(row8.getCell(1));
							String ranks8 = excelService.getStringValueFromCell(row8.getCell(2));
							String important8 = excelService.getStringValueFromCell(row8.getCell(3));
							String type8 = excelService.getStringValueFromCell(row8.getCell(4));
							Integer count8 = Integer.parseInt(excelService.getStringValueFromCell(row8.getCell(5)));
							if (sciname7.equals(sciname8)) {
								if (chname7.equals(chname8)) {
									System.out.println(sciname7 + "\t" + chname7 + "\t" + ranks8 + "\t" + important7 + "\t" + type8 + "\t" + (count7 + count8) + "\t" + datasource);
								} else {
									System.out.println(sciname7 + "\t" + (chname7 + "、" + chname8) + "\t" + ranks8 + "\t" + important7 + "\t" + type8 + "\t" + (count7 + count8) + "\t" + datasource);
								}
							}
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public JSON getCustoms(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum(); // 439
		XSSFRow row = null;
		for (int rowNum = 1; rowNum < rowNums; rowNum++) {
			row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				try {
					String id = excelService.getStringValueFromCell(row.getCell(0)).trim();
					String chname = excelService.getStringValueFromCell(row.getCell(1));
					String pid = null;
					Integer sort = null;
					Customs customs = new Customs();
					if (id.contains("0000")) {
						pid = "00";
					} else if (id.substring(2).equals("00")) {
						pid = "0000";
					} else {
						pid = id.substring(0, 2) + "00";
					}
					sort = Integer.parseInt(id);
					if (sort.toString().length() == 3) {
						customs = new Customs(id, chname, pid, 0+sort);
					} else {
						customs = new Customs(id, chname, pid, sort);
					}
					this.customsRepository.save(customs);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}



}
