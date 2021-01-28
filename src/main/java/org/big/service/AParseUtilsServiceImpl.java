package org.big.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hpsf.Array;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.big.common.CommUtils;
import org.big.common.UUIDUtils;
import org.big.entity.ARef;
import org.big.entity.Address;
import org.big.entity.Address2;
import org.big.entity.Citation;
import org.big.entity.Commonname;
import org.big.entity.Description;
import org.big.entity.Descriptiontype;
import org.big.entity.Distributiondata;
import org.big.entity.License;
import org.big.entity.Multimedia;
import org.big.entity.Protect;
import org.big.entity.Protectplus;
import org.big.entity.Rank;
import org.big.entity.Ref;
import org.big.entity.Ruqin;
import org.big.entity.Taxaset;
import org.big.entity.Taxon;
import org.big.entity.Traitdata;
import org.big.entity.User;
import org.big.entity.UserDetail;
import org.big.repository.ARefRepository;
import org.big.repository.Address2Repository;
import org.big.repository.AddressPartRepository;
import org.big.repository.AddressRepository;
import org.big.repository.CitationRepository;
import org.big.repository.CommonnameRepository;
import org.big.repository.DescriptionRepository;
import org.big.repository.DescriptiontypeRepository;
import org.big.repository.DistributiondataRepository;
import org.big.repository.GeoobjectRepository;
import org.big.repository.LicenseRepository;
import org.big.repository.MultimediaRepository;
import org.big.repository.ProtectRepository;
import org.big.repository.ProtectplusRepository;
import org.big.repository.RankRepository;
import org.big.repository.RefRepository;
import org.big.repository.RuqinRepository;
import org.big.repository.TaxasetRepository;
import org.big.repository.TaxonRepository;
import org.big.repository.UserRepository;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.drew.lang.StringUtil;

import javassist.runtime.Desc;

@Service
public class AParseUtilsServiceImpl implements AParseUtilsService {

	@Autowired
	private DescriptionRepository descriptionRepository;
	@Autowired
	private ExcelService excelService;
	@Autowired
	private LanguageService languageService;
	@Autowired
	private BatchInsertService batchInsertService;
	@Autowired
	private TaxasetRepository taxasetRepository;
	@Autowired
	private RankRepository rankRepository;
	@Autowired
	private TaxonService taxonService;
	@Autowired
	private LicenseRepository licenseRepository;
	@Autowired
	private MultimediaRepository multimediaRepository;
	@Autowired
	private DescriptiontypeRepository descriptionTypeRepository;
	@Autowired
	private RefRepository refRepository;
	@Autowired
	private CommonnameRepository commonnameRepository;
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private CitationRepository citationRepository;
	@Autowired
	private ProtectRepository protectRepository;
	@Autowired
	private ProtectplusRepository protectplusRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private Address2Repository address2Repository;
	@Autowired
	private AddressPartRepository addressPartRepository;
	@Autowired
	private ARefRepository aRefRepository;
	@Autowired
	private GeoobjectRepository geoobjectRepository;
	@Autowired
	private DistributiondataRepository distributiondataRepository;
	@Autowired
	private DistributiondataService distributiondataService;
	@Autowired
	private RuqinRepository ruqinRepository;
	@Override
	public void parseRQDWRef(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum();
		
		List<Ref> refList = new ArrayList<>();
		for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			}else {
				String valueOfPtype = getValueOfPtype(excelService.getStringValueFromCell(row.getCell(2)));
				Ref ref = new Ref(
						UUIDUtils.getUUID32(),
						valueOfPtype,
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
	                    excelService.getStringValueFromCell(row.getCell(0)),	// 参考文献引用
	                    this.languageService.getValueofLanguage(excelService.getStringValueFromCell(row.getCell(19))),
	                    this.languageService.getValueofLanguage(excelService.getStringValueFromCell(row.getCell(20))),
	                    excelService.getStringValueFromCell(row.getCell(21)),
	                    excelService.getStringValueFromCell(row.getCell(22)));
				ref.setInputer(thisUser.getId());
				ref.setInputtime(new Timestamp(System.currentTimeMillis()));
				ref.setSynchdate(new Timestamp(System.currentTimeMillis()));
				ref.setStatus(1);
				ref.setSynchstatus(0);
				refList.add(ref);
			}
		}
		long start = System.currentTimeMillis();
        this.batchInsertService.batchInsertRef(refList);
        long end = System.currentTimeMillis();
        System.err.println("Ref批量存储完成时间：" + (end - start));
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
	 * <b>ptype下拉选value与值的转换，取value</b>
	 * <p> ptype下拉选value与值的转换，取value</p>
	 * @param ptype
	 * @return
	 */
	private String getValueOfMediatype(String mediaType) {
		String rsl = "";
		if (StringUtils.isNotBlank(mediaType)) {
			switch (mediaType) {
			case "图片":
				rsl = "1";
				break;
			case "音频":
				rsl = "2";
				break;
			case "视频":
				rsl = "3";
				break;
			case "表格":
				rsl = "4";
				break;
			case "文本文件":
				rsl = "5";
				break;
			case "地图资源":
				rsl = "6";
				break;
			}
		}
		return rsl;
	}

	@Override
	public void parseRQDWTaxon(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Taxon> taxonList = new ArrayList<>();
		//String taxasetId = "196621bf74b648839f24f4fd5cfbdeda";	// 2018
		String taxasetId = "67a17cd2b2c54af3a366c51c73e0f03a";		// 2019
		String sourcesId = "98eb2e45-9dad-459f-9be6-e1d825a7bb05";
		String expertId = "4554ccc5350d4886a1e50d15fd6d06ec";
		Taxaset thisTaxaset = this.taxasetRepository.findOneById(taxasetId);
		Rank rank = this.rankRepository.findOneById("7");
		
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		
		Taxon thisTaxon = new Taxon();
		for (int i = 1; i <= rowNums; i++) {
            XSSFRow row = sheet.getRow(i);
            if (null == row) { 			// 记录行
            	continue; 									// 空行
            }else {											// 构建集合对象
            	String epithet = excelService.getStringValueFromCell(row.getCell(26));
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
                		+ "\"" + "scientificNameStatus" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(4)) + "\"," 
                		+ "}";
            	thisTaxon = new Taxon(
            			excelService.getStringValueFromCell(row.getCell(0)),
            			excelService.getStringValueFromCell(row.getCell(1)),
            			excelService.getStringValueFromCell(row.getCell(2)),
            			excelService.getStringValueFromCell(row.getCell(3)),
            			rank.getId(),											//分类等级
            			excelService.getStringValueFromCell(row.getCell(6)),	//分类体系
            			excelService.getStringValueFromCell(row.getCell(19)),	//参考文献
            			sourcesId, expertId, remark,
            			excelService.getStringValueFromCell(row.getCell(25)));
				
            	thisTaxon.setTaxaset(thisTaxaset);
				thisTaxon.setId(UUIDUtils.getUUID32());
				thisTaxon.setInputer(thisUser.getId());
				thisTaxon.setInputtime(new Timestamp(System.currentTimeMillis()));
				thisTaxon.setSynchdate(new Timestamp(System.currentTimeMillis()));
				thisTaxon.setStatus(1);
				thisTaxon.setTaxonCondition(2);//审核通过
				thisTaxon.setRank(rank);
				thisTaxon.setEpithet(expertId);
				taxonList.add(thisTaxon);
			}
        }
		 long start = System.currentTimeMillis();
         this.batchInsertService.batchInsertTaxon(taxonList);
         long end = System.currentTimeMillis();
         System.out.println("Taxon批量存储完成时间：" + (end - start));
	}

	@Override
	public void parseRQDWMedia(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String taxasetId = "196621bf74b648839f24f4fd5cfbdeda";		// 2018
		//String taxasetId = "67a17cd2b2c54af3a366c51c73e0f03a";	// 2019
		List<Multimedia> mediaList = new ArrayList<>();
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
				String valueOfMediatype = getValueOfMediatype(excelService.getStringValueFromCell(row.getCell(1)));
				String scientificname = excelService.getStringValueFromCell(row.getCell(0)).trim();
				Taxon thisTaxon = this.taxonService.findOneByScientificnameAndTaxasetId(scientificname, taxasetId);
				String licenseId = excelService.getStringValueFromCell(row.getCell(9));
            	License thisLicense = this.licenseRepository.findOneById(licenseId);
            	if (null != thisTaxon) {
            		Multimedia thisMultimedia = new Multimedia(
            				valueOfMediatype,
            				excelService.getStringValueFromCell(row.getCell(2)),
            				excelService.getStringValueFromCell(row.getCell(3)),
            				excelService.getStringValueFromCell(row.getCell(4)),
            				excelService.getStringValueFromCell(row.getCell(5)),
            				thisTaxon.getSourcesid(),
            				thisTaxon.getExpert(),
            				excelService.getStringValueFromCell(row.getCell(8)),
            				licenseId,
            				excelService.getStringValueFromCell(row.getCell(10)),
            				excelService.getStringValueFromCell(row.getCell(11)),
            				excelService.getStringValueFromCell(row.getCell(12)),
            				excelService.getStringValueFromCell(row.getCell(13)),
            				excelService.getStringValueFromCell(row.getCell(14)),
            				excelService.getStringValueFromCell(row.getCell(15)),
            				excelService.getStringValueFromCell(row.getCell(16)),
            				excelService.getStringValueFromCell(row.getCell(17)),
            				excelService.getStringValueFromCell(row.getCell(18)),
            				excelService.getStringValueFromCell(row.getCell(19)),
            				-9999D,
            				-9999D,
            				null,
            				thisLicense,
            				thisTaxon);
            		
            		thisMultimedia.setId(UUIDUtils.getUUID32());
            		thisMultimedia.setInputer(thisUser.getId());
            		thisMultimedia.setInputtime(new Timestamp(System.currentTimeMillis()));
            		thisMultimedia.setSynchdate(new Timestamp(System.currentTimeMillis()));
            		thisMultimedia.setStatus(1);
            		thisMultimedia.setSynchstatus(0);
            		
            		this.multimediaRepository.save(thisMultimedia);
            		/*this.multimediaRepository.save(thisMultimedia);
            		mediaList.add(thisMultimedia);*/
				}
			}	
		}
		/*long start = System.currentTimeMillis();
        this.batchInsertService.batchInsertMultimedia(mediaList);
        long end = System.currentTimeMillis();
        System.err.println("Media批量存储完成：" + (end - start));*/
	}

	@Override
	public void uploadRQDWImg(HttpServletRequest request, String path, String uploadPath) throws Exception {
		//String userId = new String();									// 当前用户
		String teamId = new String();									// 当前Team
		String datasetId = new String();								// 当前Dataset
		String taxasetId = new String();;								// 分类单元集Id
		List<String> list = new ArrayList<>();
		list.add("196621bf74b648839f24f4fd5cfbdeda");					// 入侵动物 2018
		list.add("67a17cd2b2c54af3a366c51c73e0f03a");					// 入侵动物 2019
		list.add("4ce0685f14a043d784926ca22ee25424");					// 入侵植物 2018
		list.add("d6569c3b9f1c4e59920136d64bc014c5");					// 入侵植物 2019
		for (String taxaset : list) {
			switch (taxaset) {
			/*case "196621bf74b648839f24f4fd5cfbdeda":
				//userId = "844a21240b744c9db65502f2359c05b2";
				teamId = "51cdf98494b148d1a30ec7642cde9258";
				datasetId = "51134708018046af9cbabda592c27640";
				taxasetId = "196621bf74b648839f24f4fd5cfbdeda";
				path = "E:/入侵物种年度数据/入侵动物/2018/";
				break;*/
			case "67a17cd2b2c54af3a366c51c73e0f03a":
				//userId = "844a21240b744c9db65502f2359c05b2";
				teamId = "51cdf98494b148d1a30ec7642cde9258";
				datasetId = "51134708018046af9cbabda592c27640";
				taxasetId = "67a17cd2b2c54af3a366c51c73e0f03a";
				path = "E:/入侵物种年度数据/入侵动物/2019/";
				break;
			/*case "4ce0685f14a043d784926ca22ee25424":
				//userId = "4c9fd816a7474e76a9c51818bcc2c381";
				teamId = "3711af0a7ae44602a3cb419d40fed6c0";
				datasetId = "f0a2eb51d3194a7d8355096d11799f0f";
				taxasetId = "4ce0685f14a043d784926ca22ee25424";
				path = "E:/入侵物种年度数据/入侵植物/2018/彩色照片/";
				break;*/
			/*case "d6569c3b9f1c4e59920136d64bc014c5":
				//userId = "4c9fd816a7474e76a9c51818bcc2c381";
				teamId = "3711af0a7ae44602a3cb419d40fed6c0";
				datasetId = "f0a2eb51d3194a7d8355096d11799f0f";
				taxasetId = "d6569c3b9f1c4e59920136d64bc014c5";
				path = "E:/入侵物种年度数据/入侵植物/2019/第四子课题入侵植物专题/先导专项——150种外来入侵/";
				break;*/
			}
		}
		addMultimediaInfo(request, path, uploadPath, teamId, datasetId, taxasetId);
	}

	private void addMultimediaInfo(HttpServletRequest request, String path, String uploadPath, String teamId,
			String datasetId, String taxasetId) {
		List<Multimedia> mediaList = this.multimediaRepository.findMultimediasByTaxasetId(taxasetId);
		for (Multimedia media : mediaList) {
			String taxonId = media.getTaxon().getId();
			String oldPath = media.getOldPath();
			/*if (oldPath.contains("_")) {
				path = "E:/入侵物种年度数据/入侵植物/2018/入侵植物预警图层图片/";
			} else if (oldPath.contains("")) {
				
			}*/
			String imgPath = path + media.getOldPath().replace("\\", "/").replace("tif", "jpg");
			File file = new File(imgPath);
			if (!file.exists()) {
				System.out.println("文件未找到：" + oldPath);
				continue;
			}
			// System.out.println("文件路径：" + imgPath);
			String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);						// 后缀名
			String newFileName = UUIDUtils.getUUID32() + "." + suffix;											// 文件名
			String realPath = request.getSession().getServletContext().getRealPath(uploadPath);					// 本地存储路径
			// 构造路径 -- Team/Dataset/Taxon/文件
			realPath = realPath + teamId + "/" + datasetId + "/" + taxonId + "/";
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				// 先把文件保存到本地
				FileUtils.copyInputStreamToFile(fileInputStream, new File(realPath.replace("\\", File.separator), newFileName));
				media.setSuffix(suffix);
				media.setPath(teamId + "/" + datasetId + "/" + taxonId + "/" + newFileName);
				this.multimediaRepository.save(media);
			} catch (IOException e1) {
				System.out.println("文件保存到本地发生异常:" + e1.getMessage());
			}
			
		}
		
	}

	@Override
	public void parseRQDWInfo(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		File fileDesc = new File(path);
		File[] files = fileDesc.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				if (!files[i].getName().contains("~$")) {
					parseExcelByUrl2019(files[i], thisUser);
					//System.out.println(files[i].getAbsolutePath());
				}
			}
		}
	}

	/**
	 * 解析入侵动物Excel
	 * @param file
	 * @param thisUser
	 * @throws Exception
	 */
	private void parseExcelByUrl2019(File file, User thisUser) throws Exception {
		System.out.println("用户信息：" + thisUser.getUserName() + "\t" + thisUser.getId());
		String absolutePath = file.getAbsolutePath();
		//String sourcesid = "74384c0c-84f9-4f6f-82b8-1035cbe5a594";	// 2018
		String sourcesid = "98eb2e45-9dad-459f-9be6-e1d825a7bb05";		// 2019
		String expert = "4554ccc5350d4886a1e50d15fd6d06ec";				// 张润志
		String inputer = thisUser.getId();
		License license = this.licenseRepository.findOneById("6");
		Descriptiontype descriptiontype = new Descriptiontype();
		
		List<Citation> citationList = new ArrayList<>();
		List<Commonname> commonnameList = new ArrayList<>();
		List<Description> descList = new ArrayList<>();
		// 创建XSSFWorkbook
		XSSFWorkbook workbook = new XSSFWorkbook(absolutePath);
		// 获取sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 总记录数
		int rowNums = sheet.getLastRowNum();
		System.out.println("当前解析文件名：" + file.getName());
		Taxon thisTaxon = new Taxon();
		for (int rowNum = 1; rowNum < rowNums; rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			}else {
				// 2018年入侵动物文献该列是文献Excel的编号
				// 2019年入侵动物文献该列是文献原文
				String title = excelService.getStringValueFromCell(row.getCell(0));
				String ref = excelService.getStringValueFromCell(row.getCell(1));
				String cell2 = excelService.getStringValueFromCell(row.getCell(2));
				
				if (StringUtils.isBlank(title)) {
					continue;
				}
				/*if (StringUtils.isBlank(cell2)) {
				} */
				if (StringUtils.isBlank(cell2)) {
					continue;
				} else if (cell2.equals("/") || cell2.equals("不详") || cell2.equals("无")) {
					continue;
				}
				if (StringUtils.isNotBlank(title)) {
					if (title.contains("中文名")) {	// 不处理
						
					} else if (title.contains("拉丁名")) {
						thisTaxon = this.taxonService.findOneByScientificnameAndInputer(cell2, "844a21240b744c9db65502f2359c05b2");
						thisTaxon.setExpert("4554ccc5350d4886a1e50d15fd6d06ec");
						thisTaxon.setInputer("844a21240b744c9db65502f2359c05b2");
						if (StringUtils.isNotBlank(ref)) {
							/*JSONArray refjson = getRefOfEntity(ref, thisUser.getId());
							if (null != refjson) {
								thisTaxon.setRefjson(JSON.toJSONString(refjson));
							}*/
							thisTaxon.setComment(ref);
						}
					} else if (title.contains("异名")) {
						List<Citation> list = getCitationByCell2(cell2, thisTaxon, ref);
						citationList.addAll(list);
					} else if (title.contains("别名")) {
						List<Commonname> list = getCommonnameByCell2(cell2, thisTaxon, ref, "1");
						commonnameList.addAll(list);
					} else if (title.contains("英文名")) {
						List<Commonname> list = getCommonnameByCell2(cell2, thisTaxon, ref, "2");
						commonnameList.addAll(list);
					} else if (title.contains("分类地位")) {
						descriptiontype = this.descriptionTypeRepository.findOneById("407");	// 寄主 206
						Description desc = getDescriptionObj(cell2, thisTaxon, ref, descriptiontype);
						descList.add(desc);
					} else if (title.contains("图片")) {
						// 只有多媒体文献和网址
						
					} else if (title.contains("寄主") || title.contains("寄生") || title.contains("食物") || title.contains("食性")) {
						descriptiontype = this.descriptionTypeRepository.findOneById("161");	// 寄主 206
						Description desc = getDescriptionObj(cell2, thisTaxon, ref, descriptiontype);
						descList.add(desc);
						// 封装描述对象
					} else if (title.contains("原产地")) {
						descriptiontype = this.descriptionTypeRepository.findOneById("206");
						Description desc = getDescriptionObj(cell2, thisTaxon, ref, descriptiontype);
						descList.add(desc);
						// 封装描述对象
					} else if (title.contains("国外分布")) {
						descriptiontype = this.descriptionTypeRepository.findOneById("208");
						Description desc = getDescriptionObj(cell2, thisTaxon, ref, descriptiontype);
						descList.add(desc);
						// 封装描述对象

					} else if (title.contains("中国首次发现时间和地点")) {
						descriptiontype = this.descriptionTypeRepository.findOneById("207");
						Description desc = getDescriptionObj(cell2, thisTaxon, ref, descriptiontype);
						descList.add(desc);
						// 封装描述对象
						
					} else if (title.contains("国内分布")) {
						descriptiontype = this.descriptionTypeRepository.findOneById("209");
						Description desc = getDescriptionObj(cell2, thisTaxon, ref, descriptiontype);
						descList.add(desc);
						// 封装描述对象

					} else if (title.contains("生境类型")) {
						descriptiontype = this.descriptionTypeRepository.findOneById("152");
						Description desc = getDescriptionObj(cell2, thisTaxon, ref, descriptiontype);
						descList.add(desc);
						// 封装描述对象
						
					} else if (title.contains("形态特征")) {
						descriptiontype = this.descriptionTypeRepository.findOneById("1");	
						List<Description> list = getDescriptionByCell2(cell2, thisTaxon, ref, descriptiontype, row);
						descList.addAll(list);
						// 封装描述对象
						
					} else if (title.contains("生物学")) {
						descriptiontype = this.descriptionTypeRepository.findOneById("101");
						List<Description> list = getDescriptionByCell2(cell2, thisTaxon, ref, descriptiontype, row);
						descList.addAll(list);
						// 封装描述对象
						
					} else if (title.contains("危害")) {
						descriptiontype = this.descriptionTypeRepository.findOneById("253");
						List<Description> list = getDescriptionByCell2(cell2, thisTaxon, ref, descriptiontype, row);
						descList.addAll(list);
						// 封装描述对象
						
					} else if (title.contains("扩散特性")) {
						descriptiontype = this.descriptionTypeRepository.findOneById("155");
						List<Description> list = getDescriptionByCell2(cell2, thisTaxon, ref, descriptiontype, row);
						descList.addAll(list);
						// 封装描述对象
						
					} else if (title.contains("防治技术")) {
						List<Description> list = getDescriptionByCells(cell2, thisTaxon, ref, row);
						descList.addAll(list);
					} else {
						System.out.println("未匹配：" + cell2 + "\t" + thisTaxon.getScientificname());
					}
				}
			}
		}
		//System.out.println(thisTaxon.getRefjson());
		//System.out.println("物种名：" + thisTaxon.getScientificname() + "\t" + citationList.size() + "\t" + commonnameList.size() + "\t" + descList.size());
		this.taxonRepository.save(thisTaxon);
		this.batchInsertService.batchInsertCitation(citationList);
		this.batchInsertService.batchInsertCommonname(commonnameList);
		this.batchInsertService.batchInsertDescription(descList);
		
	}
	
	@Override
	public void parseRQDWRefInfo() {
		String taxasetId = "67a17cd2b2c54af3a366c51c73e0f03a";
		// 物种
		// 引证
		List<Citation> cList = this.citationRepository.findCitationListByTaxasetId(taxasetId);
		// 描述
		List<Description> dList = this.descriptionRepository.findDescriptionListByTaxasetIdAndRemark(taxasetId);
		// 俗名
		List<Commonname> commList = this.commonnameRepository.findCommonnameListByTaxasetIdAndRemark(taxasetId);
		
		for (Commonname comm : commList) {
			String remark = comm.getRemark();
			JSONArray refjson = getRefOfEntity(remark, "");
			if (null != refjson) {
				comm.setRefjson(refjson.toJSONString());
				this.commonnameRepository.save(comm);
				System.out.println("俗名：" + comm.getTaxon().getScientificname() + "\t" + refjson.toJSONString());
			}
		}
		for (Description desc : dList) {
			String remark = desc.getRemark();
			JSONArray refjson = getRefOfEntity(remark, "");
			if (null != refjson) {
				desc.setRefjson(refjson.toJSONString());
				System.out.println("描述：" + desc.getTaxon().getScientificname() + "\t" + refjson.toJSONString());
				this.descriptionRepository.save(desc);
			}
		}
		for (Citation c : cList) {
			
		}
	}
	private Description getDescriptionObj(String cell2, Taxon taxon, String ref, Descriptiontype descriptiontype) {
		Description description = new Description();
		
		description.setId(UUIDUtils.getUUID32());
		description.setLanguage("1");
		description.setSourcesid(taxon.getSourcesid());
		description.setDescontent(cell2);
		description.setDestitle(taxon.getScientificname()+"的" + descriptiontype.getName());
		description.setLicenseid("6");
		description.setDescriptiontype(descriptiontype);
		description.setDestypeid(descriptiontype.getId());
		description.setExpert(taxon.getExpert());
		description.setTaxon(taxon);
		description.setStatus(1);
		description.setInputer(taxon.getInputer());
		description.setInputtime(new Timestamp(System.currentTimeMillis()));
		description.setSynchdate(new Timestamp(System.currentTimeMillis()));
		if (StringUtils.isNotBlank(ref)) {
			description.setRemark(ref);
		}
		/*JSONArray refjson = getRefOfEntity(ref, taxon.getId());
		if (null != refjson) {
			//description.setRefjson(JSON.toJSONString(refjson));
			System.out.println("desc" + "\t" + description.getId() + "\t" + JSON.toJSONString(refjson));
		}*/
		return description;
	}
	private List<Description> getDescriptionByCell2(String cell2, Taxon thisTaxon, String ref, 
			Descriptiontype descriptiontype, XSSFRow row) {
		Description thisDescription = new Description();
		
		List<Description> list = new ArrayList<>();
		short cellNums = row.getLastCellNum();
		for (int cellNum = 2; cellNum < cellNums; cellNum++) {
			String cellValue = excelService.getStringValueFromCell(row.getCell(cellNum));
			
			if (StringUtils.isNotBlank(cellValue)) {
				thisDescription = new Description(null, null, "1", descriptiontype.getId(), cellValue, ref, null, thisTaxon.getSourcesid(), thisTaxon.getExpert(), null, null, "6", thisTaxon);
				
				thisDescription.setId(UUIDUtils.getUUID32());
				thisDescription.setDestitle(thisTaxon.getScientificname() + "的形态描述");
				thisDescription.setDescriptiontype(descriptiontype);
				thisDescription.setInputer(thisTaxon.getInputer());
				thisDescription.setInputtime(new Timestamp(System.currentTimeMillis()));
				thisDescription.setSynchdate(new Timestamp(System.currentTimeMillis()));
				thisDescription.setStatus(1);
				thisDescription.setSynchstatus(0);
				
				/*JSONArray refjson = getRefOfEntity(ref, thisTaxon.getInputer());
				if (null != refjson) {
					System.out.println("desc" + "\t" +thisDescription.getId() + "\t" + JSON.toJSONString(refjson));
				}*/
				
				if (StringUtils.isNotBlank(ref)) {
					thisDescription.setRemark(ref);
				}
				list.add(thisDescription);
			}
		}
		return list;
	}
	private List<Description> getDescriptionByCells(String cell2, Taxon thisTaxon, String ref, XSSFRow row) {
		List<Description> list = new ArrayList<>();
		Descriptiontype descriptiontype = new Descriptiontype();
		short cellNums = row.getLastCellNum();
		for (int cellNum = 2; cellNum < cellNums; cellNum++) {
			String cellValue = excelService.getStringValueFromCell(row.getCell(cellNum));
			Description thisDescription = new Description();
			if (StringUtils.isNotBlank(cellValue)) {
				String[] split = cellValue.split("\n");
				String title2 = split[0].replace("\"", "");
				if (title2.contains("管理")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("501");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("检疫") || title2.contains("检验")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("502");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("农业")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("503");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("生物")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("504");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("人工捕捉")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("505");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("化学")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("506");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("生态")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("507");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("土壤")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("508");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("营林")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("509");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("林业")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("510");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("诱杀")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("511");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("人工剪除")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("513");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("物理")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("514");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("仓储")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("515");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("田间")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("516");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("环境")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("517");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("监测") || title2.contains("监管")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("518");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("人工捕捞")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("518");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("宣传教育")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("520");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("媒介昆虫防治")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("521");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else if (title2.contains("其他防治技术") || title2.contains("其他")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("522");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				}  else if (title2.contains("仓储")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("523");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				}  else if (title2.contains("分区")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("524");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				}  else if (title2.contains("检查") || title2.contains("调查")) {
					descriptiontype = this.descriptionTypeRepository.findOneById("524");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				} else {
					descriptiontype = this.descriptionTypeRepository.findOneById("522");
					thisDescription = getDescriptionObj(cellValue, thisTaxon, ref, descriptiontype);
				}
				list.add(thisDescription);
			}
		}
		return list;
	}

	private List<Commonname> getCommonnameByCell2(String cell2, Taxon thisTaxon, String ref, String type) {
		List<Commonname> list = new ArrayList<>();
		// 创建【俗名】实体关联物种及文献
		cell2 = cell2.replace("，", "、");
		// 封装俗名对象
		String[] commonArr = cell2.split("、");
		for (String common : commonArr) {
			Commonname thisCommonname = new Commonname();
			thisCommonname.setId(UUIDUtils.getUUID32());
			thisCommonname.setCommonname(common);
			thisCommonname.setLanguage(type);
			thisCommonname.setSourcesid(thisTaxon.getSourcesid());
			thisCommonname.setExpert(thisTaxon.getExpert());
			thisCommonname.setInputer(thisTaxon.getInputer());
			thisCommonname.setInputtime(new Timestamp(System.currentTimeMillis()));
			thisCommonname.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisCommonname.setStatus(1);
			thisCommonname.setTaxon(thisTaxon);
			if (StringUtils.isNotBlank(ref)) {
				thisCommonname.setRemark(ref);
			}
			/*JSONArray refjson = getRefOfEntity(ref, thisTaxon.getId());
			if (null != refjson) {
				System.out.println("comm" + "\t" +thisCommonname.getId() + "\t" + JSON.toJSONString(refjson));
			}*/
			list.add(thisCommonname);
		}
		return list;
	}

	private List<Citation> getCitationByCell2(String cell2, Taxon thisTaxon, String ref) {
		List<Citation> clist = new ArrayList<>();
		String[] citationArr = null;
		String sciname = new String();
		String author = new String();
		String nametype = new String();
		cell2 = cell2.replace("，", "、");
		citationArr = cell2.split("、");
		// 创建【引证】实体关联物种及文献
		for (String line : citationArr) {
			if (!line.contains(thisTaxon.getScientificname().trim())) {	// 判断印证类型
				nametype = "5";	// 异名
			} else {
				nametype = "1";	// 异名
			}
			if (line.contains("subsp.") || line.contains("var.")) {
				sciname = getSciNameFromCitation(line, 3);
			} else {
				sciname = getSciNameFromCitation(line, 2);
			}
			author = line.replace(sciname, "").trim();
			
			// 封装引证对象
			Citation thisCitation = new Citation();
			thisCitation.setId(UUIDUtils.getUUID32());
			thisCitation.setCitationstr(line);
			thisCitation.setSciname(sciname);
			thisCitation.setNametype(nametype);
			thisCitation.setAuthorship(author);
			thisCitation.setExpert(thisTaxon.getExpert());
			thisCitation.setSourcesid(thisTaxon.getSourcesid());
			thisCitation.setInputer(thisTaxon.getInputer());
			thisCitation.setInputtime(new Timestamp(System.currentTimeMillis()));
			thisCitation.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisCitation.setStatus(1);
			thisCitation.setTaxon(thisTaxon);
			if (StringUtils.isNotBlank(ref)) {
				thisCitation.setRemark(ref);
			}
			/*if (StringUtils.isNotBlank(ref)) {
				JSONArray refjson = getRefOfCitation(ref, line, thisTaxon.getInputer(), author);
				if (null != refjson) {
					System.out.println("citat" + "\t" +thisCitation.getId() + "\t" + JSON.toJSONString(refjson));
				}
			}*/
			clist.add(thisCitation);
		}
		return clist;
	}
	/**
	 * 引证索引
	 * @param ref
	 * @param line
	 * @return
	 */
	private JSONArray getRefOfCitation(String ref, String line, String uid, String author) {
		JSONArray jsonArray = new JSONArray();
		Ref thisRef = new Ref();
		String[] authorArr = null;
		if (StringUtils.isNotBlank(author)) {
			authorArr = author.split(",");
		}
		if (StringUtils.isNotBlank(ref)) {
			ref = ref.replace("\"", "");
			String[] refArr = ref.split("\n");
			for (String refstr : refArr) {
				thisRef = this.refRepository.findOneByRefstrAndInputer(refstr, uid);
				if (null != thisRef) {
					String jsonStr = "{\"refE\":\"" + (StringUtils.isBlank(thisRef.getRefe()) ? "-" : thisRef.getRefe()) + "\"," + 
							"\"refS\":\"" + (StringUtils.isBlank(thisRef.getRefs()) ? "-" : thisRef.getRefs()) + "\"," +
							"\"refId\":\"" + thisRef.getId() + "\",";
					for (String str : authorArr) {
						if (thisRef.getRefstr().contains(str)) {
							jsonStr = jsonStr + "\"refType\":\"" + 1 + "\"}";
						} else {
							jsonStr = jsonStr + "\"refType\":\"" + 0 + "\"}";
						} 
					}
					jsonArray.add(JSON.parseObject(jsonStr));
				}
			}
			return jsonArray;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param line	引证
	 * @param count	从第几个分割
	 * @return
	 */
	public String getSciNameFromCitation(String line, int count) {
		int index = 0;
		int flagCount = 0;
		if (line.contains(" - ")) {
			return line.substring(0, line.indexOf(" - "));
		} else {
			for (int i = 0; i < line.length(); i++) {
				String str = String.valueOf(line.charAt(i));
				index++;
				if (str.equals(" ") || str.equals("：") || str.equals(":") || str.equals("(") || str.equals("（")
						|| str.equals("{")||str.equals(",")||str.equals("，")) {
					flagCount++;
					if (count == flagCount) {
						return line.substring(0, index - 1);
					}
				}
			}
		}
		return line;
	}

	/**
	 * 封装实体关联的文献，返回JOSN格式的字符串
	 * @param ref
	 * @return
	 */
	private JSONArray getRefOfEntity(String ref, String uid) {
		JSONArray jsonArray = new JSONArray();
		if (StringUtils.isNotBlank(ref)) {
			ref = ref.replace("\"", "");
			String[] refArr = ref.split("\n");
			for (String refstr : refArr) {
				Ref thisRef = this.refRepository.findOneByRefstrAndInputer(refstr.trim(), "844a21240b744c9db65502f2359c05b2");
				if (null != thisRef) {
					String jsonStr = "{\"refE\":\"" + (StringUtils.isBlank(thisRef.getRefe()) ? "-" : thisRef.getRefe()) + "\"," + 
							"\"refS\":\"" + (StringUtils.isBlank(thisRef.getRefs()) ? "-" : thisRef.getRefs()) + "\"," +
							"\"refId\":\"" + thisRef.getId() + "\"}";
					jsonArray.add(JSON.parseObject(jsonStr));
				} else {
					System.out.println("未找到：" + ref);
				}
			}
			//System.out.println("结果：" + jsonArray.toJSONString());
			return jsonArray;
		} else {
			return null;
		}
	}

	@Override
	public void getRQDWRefOfDesc() {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Description> descList = this.descriptionRepository.findDescriptionListByUserId(thisUser.getId());
		for (Description desc : descList) {
			String remark = desc.getRemark();
			if (StringUtils.isNotBlank(remark)) {
				remark = desc.getRemark().replace(",", "、").replace("，", "、").replace(" ", "");
				String[] split = remark.split("、");
				if (split.length == 1) {
					Ref ref = this.refRepository.findOneByTcharAndInputer(split[0], thisUser.getId());
					String context = "[{"
							+ "\"refId\"" + ":\"" + ref.getId() + "\","
							+ "\"refS\"" + ":\"" + (StringUtils.isBlank(ref.getRefs()) ? "-" : ref.getRefs()) + "\"," 
							+ "\"refE\"" + ":\"" + (StringUtils.isBlank(ref.getRefe()) ? "-" : ref.getRefe()) + "\""
							+ "}]";
					//System.out.println(remark + "\t" + context);
					desc.setRefjson(context);
					this.descriptionRepository.save(desc);
				} else {
					StringBuilder sb = new StringBuilder();
					sb.append("[");
					for (String num : split) {
						Ref ref = this.refRepository.findOneByTcharAndInputer(num, thisUser.getId());
						String jsonStr = "{"
								+ "\"refId\"" + ":\"" + ref.getId() + "\","
								+ "\"refS\"" + ":\"" + (StringUtils.isBlank(ref.getRefs()) ? "-" : ref.getRefs()) + "\"," 
								+ "\"refE\"" + ":\"" + (StringUtils.isBlank(ref.getRefe()) ? "-" : ref.getRefe()) + "\""
								+ "}";
						sb.append(jsonStr);
						sb.append(",");
					}
					String context = sb.toString();
					if (context.endsWith(",")) {
						context = context.substring(0, context.length() - 1) + "]";
					}
					desc.setRefjson(context);
					this.descriptionRepository.save(desc);
					//System.out.println(remark + "\t" + context);
				}
			}
		}
		//this.batchInsertService.batchInsertDescription(list);
	}

	@Override
	public void getRQDWDisOfDesc() {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String[] types = {"206", "207", "208", "209"};
		List<Description> descList = this.descriptionRepository.findDescListByInputerAndType(thisUser.getId(), types);
		System.out.println(descList.size());
		for (Description desc : descList) {
			System.out.println(desc.getDescontent());
		}
	}

	@Override
	public void updateRef(String path) throws IOException {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum();
		
		List<Ref> refList = new ArrayList<>();
		for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			}else {
				String remark = excelService.getStringValueFromCell(row.getCell(22));
				if (StringUtils.isNotBlank(remark)) {
					String title = excelService.getStringValueFromCell(row.getCell(6));
					Ref ref = this.refRepository.findOneByTitleAndInputer(title, thisUser.getId());
					if (null != ref) {
						ref.setRemark(remark);
						this.refRepository.save(ref);
					}
				}
				//System.out.println(remark);
			}
		}
	}
	
	/**
	 * Gets the pY.
	 *
	 * @param cname the cname
	 * @return the pY
	 * @throws DocumentException the document exception
	 */
	private String getPY(String cname) throws DocumentException {
		String str = "";
		if (cname.trim().equals("")) {
			return str;
		}
		cname = cname.trim();
		System.out.println(cname);
		if (cname.contains("（")) {
			cname = cname.replace("（", "(").replace("）", ")");

		}
		cname = cname.replace(" ", "");
		String pyUrl = "http://chinese.biodinfo.org/ChineseCharactorWebService.asmx/GetWordPinyinGoogle";
		String param = "word=" + cname + "&psd=";
		pyUrl = pyUrl + "?" + param;
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(pyUrl);
		if (document != null) {
			Element root = document.getRootElement();
			str = (String) root.getData();
		}
		return str;
	}

	@Override
	public void commonnameOfPY() throws DocumentException {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Commonname> list = this.commonnameRepository.findByLangAndInputer("1", thisUser.getId());
		System.out.println(list.size());
		for (Commonname comm : list) {
			String py = getPY(comm.getCommonname());
			comm.setRemark(py);
			System.out.println(py);
//			this.commonnameRepository.save(comm);
		}
	}

	@Override
	public void parseRQZWTaxon(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Taxon> taxonList = new ArrayList<>();
		Taxaset thisTaxaset = this.taxasetRepository.findOneById("4ce0685f14a043d784926ca22ee25424");
		Rank rank = this.rankRepository.findOneById("7");
		
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(1);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		
		Taxon thisTaxon = new Taxon();
		for (int i = 1; i <= rowNums; i++) {
            XSSFRow row = sheet.getRow(i);
            if (null == row) { 			// 记录行
            	continue; 									// 空行
            }else {											// 构建集合对象
            	Ref thisRef = this.refRepository.findOneById("ccb5780a-c6aa-4035-974b-bb6e2c663fe2");
            	String context = "[{"
						+ "\"refId\"" + ":\"" + thisRef.getId() + "\","
						+ "\"refS\"" + ":\"" + thisRef.getRefs() + "\"," 
						+ "\"refE\"" + ":\"" + thisRef.getRefe() + "\""
						+ "}]";
            	
            	thisTaxon = new Taxon(
            			excelService.getStringValueFromCell(row.getCell(0)),
            			excelService.getStringValueFromCell(row.getCell(1)),
            			excelService.getStringValueFromCell(row.getCell(2)),
            			"ICBN",
            			"7",	//分类等级
            			"中国植物志",	//分类体系
            			context,	//参考文献
            			"f86d0a20-85c0-43f1-b528-1e42930c35d0",	//数据源
            			"a3ffca7c003c4e4b9bb8fd128810c67c",	//专家
            			null,"");
				
            	thisTaxon.setTaxaset(thisTaxaset);
				thisTaxon.setId(UUIDUtils.getUUID32());
				thisTaxon.setInputer(thisUser.getId());
				thisTaxon.setInputtime(new Timestamp(System.currentTimeMillis()));
				thisTaxon.setSynchdate(new Timestamp(System.currentTimeMillis()));
				thisTaxon.setStatus(1);
				thisTaxon.setTaxonCondition(2);//审核通过
				thisTaxon.setEpithet(excelService.getStringValueFromCell(row.getCell(3)));
				thisTaxon.setRank(rank);
				System.out.println(excelService.getStringValueFromCell(row.getCell(2)) + "\t" + excelService.getStringValueFromCell(row.getCell(3)));
				taxonList.add(thisTaxon);
			}
        }
		 long start = System.currentTimeMillis();
         this.batchInsertService.batchInsertTaxon(taxonList);
         long end = System.currentTimeMillis();
         System.out.println("Taxon批量存储完成：" + (end - start));
	}

	@Override
	public void parseRQZW2Taxon(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Taxon> taxonList = new ArrayList<>();
		Taxaset thisTaxaset = this.taxasetRepository.findOneById("4ce0685f14a043d784926ca22ee25424");
		Rank rank = this.rankRepository.findOneById("7");
		
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		
		Taxon thisTaxon = new Taxon();
		for (int i = 1; i <= rowNums; i++) {
            XSSFRow row = sheet.getRow(i);
            if (null == row) { 			// 记录行
            	continue; 									// 空行
            }else {											// 构建集合对象
            	String scientificname = excelService.getStringValueFromCell(row.getCell(4));
            	String chname = excelService.getStringValueFromCell(row.getCell(1));
            	if (StringUtils.isNotBlank(chname)) {
            		thisTaxon = this.taxonRepository.findOneByChnameAndInputer(chname.trim(), thisUser.getId());
            		if (null == thisTaxon) {
						System.out.println(chname);
					}else {
						taxonList.add(thisTaxon);
					}
            	}
            	/*if (StringUtils.isNotBlank(scientificname)) {
            		thisTaxon = this.taxonRepository.findOneByScientificnameAndInputer(scientificname.trim(), thisUser.getId());
            		if (null == thisTaxon) {
            			String epithet = excelService.getStringValueFromCell(row.getCell(26));
                    	String remark = "{"
                        		+ "\"" + "GenusCn" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(3)) + "\","
                        		+ "\"" + "Genus" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(2)) + "\""
                        		+ "}";
                    	Ref thisRef = this.refRepository.findOneById("ccb5780a-c6aa-4035-974b-bb6e2c663fe2");
                    	String context = "[{"
        						+ "\"refId\"" + ":\"" + thisRef.getId() + "\","
        						+ "\"refS\"" + ":\"" + thisRef.getRefs() + "\"," 
        						+ "\"refE\"" + ":\"" + thisRef.getRefe() + "\""
        						+ "}]";
                    	thisTaxon = new Taxon(
                    			excelService.getStringValueFromCell(row.getCell(4)),
                    			excelService.getStringValueFromCell(row.getCell(1)),
                    			excelService.getStringValueFromCell(row.getCell(2)),
                    			excelService.getStringValueFromCell(row.getCell(3)),
                    			"7",	//分类等级
                    			excelService.getStringValueFromCell(row.getCell(6)),	//分类体系
                    			context,	//参考文献
                    			"f86d0a20-85c0-43f1-b528-1e42930c35d0",	//数据源
                    			"a3ffca7c003c4e4b9bb8fd128810c67c",	//专家
                    			remark);
        				
                    	thisTaxon.setTaxaset(thisTaxaset);
        				thisTaxon.setId(UUIDUtils.getUUID32());
        				thisTaxon.setInputer(thisUser.getId());
        				thisTaxon.setInputtime(new Timestamp(System.currentTimeMillis()));
        				thisTaxon.setSynchdate(new Timestamp(System.currentTimeMillis()));
        				thisTaxon.setStatus(1);
        				thisTaxon.setTaxonCondition(2);//审核通过
        				thisTaxon.setRank(rank);
        				thisTaxon.setEpithet(epithet);
        				taxonList.add(thisTaxon);
					}
				}*/
			}
        }
		System.out.println("SIZE：" + taxonList.size());
		 /*long start = System.currentTimeMillis();
         this.batchInsertService.batchInsertTaxon(taxonList);
         long end = System.currentTimeMillis();
         System.out.println("Taxon批量存储完成：" + (end - start));*/
	}

	@Override
	public void parseRQZWTaxonInfo(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String sourcesid = "f86d0a20-85c0-43f1-b528-1e42930c35d0";
		String inputer = thisUser.getId();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Descriptiontype descriptiontype = this.descriptionTypeRepository.findOneById("209");
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		
		Taxon thisTaxon = new Taxon();
		for (int i = 1; i <= rowNums; i++) {
            XSSFRow row = sheet.getRow(i);
            if (null == row) { 			// 记录行
            	continue; 									// 空行
            }else {											// 构建集合对象
            	String descontent = excelService.getStringValueFromCell(row.getCell(6));
            	if (StringUtils.isNotBlank(descontent)) {
            		String sciname = excelService.getStringValueFromCell(row.getCell(4));
            		thisTaxon = this.taxonRepository.findOneByScientificnameAndInputer(sciname, thisUser.getId());
            		if (null != thisTaxon) {
						Description desc = new Description();
						desc.setId(UUIDUtils.getUUID32());
						desc.setDescontent(descontent);
						desc.setDescriptiontype(descriptiontype);
						desc.setLanguage("1");
						desc.setLicenseid("6");
						desc.setSourcesid(sourcesid);
						desc.setStatus(1);
						desc.setInputer(inputer);
						desc.setInputtime(timestamp);
						desc.setSynchdate(timestamp);
						desc.setSynchstatus(0);
						desc.setDestitle(sciname + "的国内分布"); 
						
						this.descriptionRepository.save(desc);
					}
				}
			}
        }
	}

	@Override
	public void parseRQZWCitation(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String sourcesid = "f86d0a20-85c0-43f1-b528-1e42930c35d0";
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		
		Taxon thisTaxon = new Taxon();
		for (int i = 1; i <= rowNums; i++) {
            XSSFRow row = sheet.getRow(i);
            if (null == row) { 								// 记录行
            	continue; 									// 空行
            }else {											// 构建集合对象
            	String sciname = excelService.getStringValueFromCell(row.getCell(0));
            	if (StringUtils.isNotBlank(sciname)) {
            		Ref thisRef = this.refRepository.findOneById("ccb5780a-c6aa-4035-974b-bb6e2c663fe2");
                	String context = "[{"
    						+ "\"refId\"" + ":\"" + thisRef.getId() + "\","
    						+ "\"refS\"" + ":\"" + thisRef.getRefs() + "\"," 
    						+ "\"refE\"" + ":\"" + thisRef.getRefe() + "\""
    						+ "}]";
            		thisTaxon = this.taxonRepository.findOneByScientificnameAndInputer(sciname, thisUser.getId());
            		String authorStr = excelService.getStringValueFromCell(row.getCell(1));
            		String citationStr = excelService.getStringValueFromCell(row.getCell(3));
            		if (null != thisTaxon) {
            			Citation citation = new Citation();
            			citation.setId(UUIDUtils.getUUID32());
            			citation.setNametype("1");
            			citation.setSciname(sciname);
            			citation.setAuthorship(authorStr);
            			citation.setCitationstr(citationStr);
            			citation.setSourcesid(sourcesid);
            			citation.setRefjson(context);
            			citation.setStatus(1);
            			citation.setSynchstatus(0);
            			citation.setInputer(thisUser.getId());
            			citation.setInputtime(timestamp);
            			citation.setSynchdate(timestamp);
            			citation.setTaxon(thisTaxon);
            			
            			this.citationRepository.save(citation);
            		} else {
						System.out.println(sciname);
					}
				}
			}
        }
	}
	@Override
	public void parseRQZWCommonname(String path) throws Exception {
		String sourcesid = "f86d0a20-85c0-43f1-b528-1e42930c35d0";
		String inputer = "4c9fd816a7474e76a9c51818bcc2c381";
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		List<Commonname> cnameList = new ArrayList<>();
		Taxon thisTaxon = new Taxon();
		for (int i = 1; i <= rowNums; i++) {
			XSSFRow row = sheet.getRow(i);
			if (null == row) { 								// 记录行
				continue; 									// 空行
			}else {											// 构建集合对象
				String sciname = excelService.getStringValueFromCell(row.getCell(0));
				if (StringUtils.isNotBlank(sciname)) {
					Ref thisRef = this.refRepository.findOneById("ccb5780a-c6aa-4035-974b-bb6e2c663fe2");
					String context = "[{\"refId\":\"" + thisRef.getId() + "\","
							+ "\"refS\":\"" + thisRef.getRefs() + "\"," 
							+ "\"refE\":\"" + thisRef.getRefe() + "\"}]";
					thisTaxon = this.taxonRepository.findOneByScientificnameAndInputer(sciname, inputer);
					String cnameStr = excelService.getStringValueFromCell(row.getCell(1));
					
					if (StringUtils.isBlank(cnameStr)) {
						continue;
					} else {
						cnameStr = cnameStr.replace("，", "、").replace(",", "、");
					}
					if (null != thisTaxon) {
						String[] split = cnameStr.split("、");
						for (String cname : split) {
							Commonname commonname = new Commonname();
							commonname.setId(UUIDUtils.getUUID32());
							commonname.setCommonname(cname);
							commonname.setSourcesid(sourcesid);
							commonname.setRefjson(context);
							commonname.setLanguage("1");
							commonname.setStatus(1);
							commonname.setSynchstatus(0);
							commonname.setInputer(inputer);	// 入侵植物
							commonname.setInputtime(timestamp);
							commonname.setSynchdate(timestamp);
							commonname.setTaxon(thisTaxon);
							this.commonnameRepository.save(commonname);
						}
					} else {
						System.out.println("异常：" + "找不到关联物种 - " + sciname);
					}
				}
			}
		}
		//batchInsertService.batchInsertCommonname(cnameList);
	}

	@Override
	public void parseRQZWDesc(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String sourcesid = "f86d0a20-85c0-43f1-b528-1e42930c35d0";
		String expert = "a3ffca7c003c4e4b9bb8fd128810d78d";
		String inputer = thisUser.getId();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Descriptiontype descriptiontype = this.descriptionTypeRepository.findOneById("120");
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		
		Taxon thisTaxon = new Taxon();
		for (int i = 1; i <= rowNums; i++) {
            XSSFRow row = sheet.getRow(i);
            if (null == row) { 			// 记录行
            	continue; 									// 空行
            }else {											// 构建集合对象
            	String sciname = excelService.getStringValueFromCell(row.getCell(0));
            	if (StringUtils.isNotBlank(sciname)) {
            		String descontent = excelService.getStringValueFromCell(row.getCell(5));
            		thisTaxon = this.taxonRepository.findOneByScientificnameAndInputer(sciname, thisUser.getId());
            		if (null != thisTaxon) {
						Description desc = new Description();
						desc.setId(UUIDUtils.getUUID32());
						desc.setDestitle(sciname + "的性状描述"); 
						desc.setDescontent(descontent);
						desc.setDescriptiontype(descriptiontype);
						desc.setLanguage("1");
						desc.setLicenseid("6");
						desc.setSourcesid(sourcesid);
						desc.setExpert(expert);
						desc.setRightsholder("科学出版社");
						desc.setdCopyright("转载或引用本网中的文章，必须提到原作者");
						desc.setStatus(1);
						desc.setInputer(inputer);
						desc.setInputtime(timestamp);
						desc.setSynchdate(timestamp);
						desc.setSynchstatus(0);
						desc.setTaxon(thisTaxon);
						this.descriptionRepository.save(desc);
					} else {
						System.out.println(sciname);
					}
				}
			}
        }
	}

	@Override
	public void parseRQZWMultimedia(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String sourcesid = "f86d0a20-85c0-43f1-b528-1e42930c35d0";
		String inputer = thisUser.getId();
		License license = this.licenseRepository.findOneById("6");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
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
				String scientificname = excelService.getStringValueFromCell(row.getCell(0));
				String content = excelService.getStringValueFromCell(row.getCell(2));
				String oldPath = excelService.getStringValueFromCell(row.getCell(4));
				Taxon thisTaxon = this.taxonService.findOneByScientificnameAndInputer(scientificname, thisUser.getId());
				if (null != thisTaxon) {
	            	Multimedia media = new Multimedia();
	            	media.setId(UUIDUtils.getUUID32());
	            	media.setMediatype("1");
					media.setContext(content);
					media.setLisenceid("6");
					media.setLicense(license);
					media.setSourcesid(sourcesid);
					media.setOldPath(content + File.separator + oldPath + ".jpg");
					media.setRightsholder("于胜祥");
					media.setCopyright("转载或引用本网中的照片，必须提到原作者");
					media.setStatus(1);
					media.setInputer(inputer);
					media.setInputtime(timestamp);
					media.setSynchdate(timestamp);
					media.setSynchstatus(0);
					media.setLat(-9999D);
					media.setLng(-9999D);
					media.setTaxon(thisTaxon);
					this.multimediaRepository.save(media);
				} else {
					System.out.println(scientificname);
				}
			}	
		}
	}

	@Override
	public void parseRQZWDescdata(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String sourcesid = "f86d0a20-85c0-43f1-b528-1e42930c35d0";
		String expert = "a3ffca7c003c4e4b9bb8fd128810d78d";
		String inputer = thisUser.getId();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Descriptiontype descriptiontype = this.descriptionTypeRepository.findOneById("201");
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		String sname = new String();
		Taxon thisTaxon = new Taxon();
		Set<String> set = new HashSet<>();
		StringBuilder sb = new StringBuilder(); 
		for (int i = 1; i <= rowNums; i++) {
            XSSFRow row = sheet.getRow(i);
            if (null == row) { 			// 记录行
            	continue; 									// 空行
            }else {											// 构建集合对象
            	if (StringUtils.isNotBlank(sname)) {
            		String sciname = excelService.getStringValueFromCell(row.getCell(0));
            		if (sname.equals(sciname)) {
            			String province = excelService.getStringValueFromCell(row.getCell(1));
            			String city = excelService.getStringValueFromCell(row.getCell(2));
						if (sb.toString().contains(province)) {
							sb.append(city).append("、");
						} else {
							sb.append("&").append(province).append("：").append(city).append("、");
						}
						System.out.println(sciname + "\t" + sb.toString());
					} else {
						sname = sciname;
						sb = new StringBuilder();
						sb.append(excelService.getStringValueFromCell(row.getCell(1)));
						sb.append("：");
						sb.append(excelService.getStringValueFromCell(row.getCell(2)));
						sb.append("、");
					}
				} else {
					sname = excelService.getStringValueFromCell(row.getCell(0));
					sb.append(excelService.getStringValueFromCell(row.getCell(1)));
					sb.append("：");
					sb.append(excelService.getStringValueFromCell(row.getCell(2)));
					sb.append("、");
				}
            	
            	/*if (StringUtils.isNotBlank(sciname)) {
            		String descontent = excelService.getStringValueFromCell(row.getCell(5));
            		thisTaxon = this.taxonRepository.findOneByScientificnameAndInputer(sciname, thisUser.getId());
            		if (null != thisTaxon) {
						Description desc = new Description();
						desc.setId(UUIDUtils.getUUID32());
						desc.setDestitle(sciname + "的分布信息"); 
						desc.setDescontent(descontent);
						desc.setDescriptiontype(descriptiontype);
						desc.setLanguage("1");
						desc.setLicenseid("6");
						desc.setSourcesid(sourcesid);
						desc.setExpert(expert);
						desc.setStatus(1);
						desc.setInputer(inputer);
						desc.setInputtime(timestamp);
						desc.setSynchdate(timestamp);
						desc.setSynchstatus(0);
						desc.setTaxon(thisTaxon);
						//this.descriptionRepository.save(desc);
					} else {
						set.add(sciname);
					}
				}*/
			}
        }
		
		for (String name : set) {
			System.out.println(name);
		}
	}

	@Override
	public void uploadRQZWImg(HttpServletRequest request, String path, String uploadPath) {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String teamId = (String) request.getSession().getAttribute("teamId");									// 当前Team
		String datasetId = (String) request.getSession().getAttribute("datasetID");								// 当前Dataset
		List<Multimedia> mediaList = this.multimediaRepository.findMultimediasByInputer(thisUser.getId());
		for (Multimedia media : mediaList) {
			String taxonId = media.getTaxon().getId();
			String imgPath = path + File.separator + media.getOldPath().replace("\\", File.separator);
			File file = new File(imgPath);
			
			String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);						// 后缀名
			String newFileName = UUIDUtils.getUUID32() + "." + suffix;											// 文件名
			String realPath = request.getSession().getServletContext().getRealPath(uploadPath);					// 本地存储路径
			// 构造路径 -- Team/Dataset/Taxon/文件
			realPath = realPath + teamId + "/" + datasetId + "/" + taxonId + "/";
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				// 先把文件保存到本地
				FileUtils.copyInputStreamToFile(fileInputStream, new File(realPath.replace("\\", File.separator), newFileName));
			} catch (IOException e1) {
				//e1.printStackTrace();
				System.out.println("文件保存到本地发生异常:" + e1.getMessage());
			}
			media.setSuffix(suffix);
			media.setPath(teamId + "/" + datasetId + "/" + taxonId + "/" + newFileName);
			this.multimediaRepository.save(media);
		}
	}

	@Override
	public void uploadRQZWDiscImg(HttpServletRequest request, String path, String uploadPath) {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String inputer = thisUser.getId();
		String teamId = (String) request.getSession().getAttribute("teamId");									// 当前Team
		String datasetId = (String) request.getSession().getAttribute("datasetID");								// 当前Dataset
		String sourcesid = "f86d0a20-85c0-43f1-b528-1e42930c35d0";
		License license = this.licenseRepository.findOneById("6");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		File fileDesc = new File(path);
		File[] files = fileDesc.listFiles();
		
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				File file = files[i];
				String name = file.getName();
				int indexOf = name.lastIndexOf(".");
				String suffix = name.substring(indexOf + 1);						// 后缀名
				String sciname = name.substring(0, indexOf).replace("_", " ");
				Taxon taxon = this.taxonRepository.findOneByScientificnameAndInputer(sciname, inputer);
				if (null == taxon) {
					System.err.println(sciname);
				} else {
					String newFileName = UUIDUtils.getUUID32() + "." + suffix;											// 文件名
					String realPath = request.getSession().getServletContext().getRealPath(uploadPath);					// 本地存储路径
					// 构造路径 -- Team/Dataset/Taxon/文件
					realPath = realPath + teamId + "/" + datasetId + "/" + taxon.getId() + "/";
					try {
						FileInputStream fileInputStream = new FileInputStream(file);
						// 先把文件保存到本地
						FileUtils.copyInputStreamToFile(fileInputStream, new File(realPath.replace("\\", File.separator), newFileName));
					} catch (IOException e1) {
						e1.printStackTrace();
						System.out.println("文件保存到本地发生异常:" + e1.getMessage());
					}
					
					Multimedia media = new Multimedia();
					media.setId(UUIDUtils.getUUID32());
					media.setMediatype("1");
					media.setContext(taxon.getScientificname() + "的入侵分布");
					media.setLisenceid("6");
					media.setLicense(license);
					media.setSourcesid(sourcesid);
					media.setOldPath(name);
					media.setStatus(1);
					media.setInputer(inputer);
					media.setInputtime(timestamp);
					media.setSynchdate(timestamp);
					media.setSynchstatus(0);
					media.setLat(-9999D);
					media.setLng(-9999D);
					media.setSuffix(suffix);
					media.setPath(teamId + "/" + datasetId + "/" + taxon.getId() + "/" + newFileName);
					media.setTaxon(taxon);
					this.multimediaRepository.save(media);
				}
			}
		}
	}

	@Override
	public void parseRQZWDescdis(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String sourcesid = "f86d0a20-85c0-43f1-b528-1e42930c35d0";
		String inputer = thisUser.getId();
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Descriptiontype descriptiontype = this.descriptionTypeRepository.findOneById("209");
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(1);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		Taxon thisTaxon = new Taxon();
		Set<String> set = new HashSet<>();
		StringBuilder sb = new StringBuilder(); 
		for (int i = 1; i <= rowNums; i++) {
            XSSFRow row = sheet.getRow(i);
            if (null == row) { 			// 记录行
            	continue; 									// 空行
            }else {											// 构建集合对象
            	String sciname = excelService.getStringValueFromCell(row.getCell(0));
            	if (StringUtils.isNotBlank(sciname)) {
            		String discontent = excelService.getStringValueFromCell(row.getCell(1));
            		discontent = discontent.substring(0, discontent.length() - 1);
            		thisTaxon = this.taxonRepository.findOneByScientificnameAndInputer(sciname, thisUser.getId());
            		if (null != thisTaxon) {
						Description desc = new Description();
						desc.setId(UUIDUtils.getUUID32());
						desc.setDestitle(sciname + "的国内分布"); 
						desc.setDescontent(discontent);
						desc.setDescriptiontype(descriptiontype);
						desc.setLanguage("1");
						desc.setLicenseid("6");
						desc.setSourcesid(sourcesid);
						desc.setStatus(1);
						desc.setInputer(inputer);
						desc.setInputtime(timestamp);
						desc.setSynchdate(timestamp);
						desc.setSynchstatus(0);
						desc.setTaxon(thisTaxon);
						this.descriptionRepository.save(desc);
					} else {
						set.add(sciname);
					}
				}
			}
        }
		for (String str : set) {
			System.out.println(str);
		}
	}

	@Override
	public void handleRQDW(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		for (int i = 1; i <= rowNums; i++) {
			XSSFRow row = sheet.getRow(i);
			if (null == row) {	// 记录行
				continue; 		// 空行
			} else {
				String sciname = excelService.getStringValueFromCell(row.getCell(0)).trim();
				String chname = excelService.getStringValueFromCell(row.getCell(1)).trim();
				System.out.println(sciname + "\t" + chname);
			}
		}
	}

	@Override
	public void synchdataToLocal(String path) throws Exception {
		Taxon taxon = new Taxon();
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		for (int i = 1; i <= rowNums; i++) {
			XSSFRow row = sheet.getRow(i);
			if (null == row) { // 记录行
				continue; // 空行
			} else {
				String id = excelService.getStringValueFromCell(row.getCell(0)).trim();
				String sciname = excelService.getStringValueFromCell(row.getCell(1)).trim();
				String chname = excelService.getStringValueFromCell(row.getCell(2)).trim();
				taxon = this.taxonRepository.findOneById(id);
				if (null != taxon) {
					taxon.setScientificname(sciname);
					taxon.setChname(chname);
					this.taxonRepository.save(taxon);
				} else {
					System.out.println("未匹配：" + id + "\t" + sciname + "\t" + chname);
				}
			}
		}
	}

	@Override
	public void trainAnimalData(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String sourcesid = "f86d0a20-85c0-43f1-b528-1e42930c35d0";
		String inputer = thisUser.getId();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Taxaset taxaset = new Taxaset();
		Rank rank = this.rankRepository.findOneById("7");
		Taxon taxon = new Taxon();
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(1);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		for (int i = 1; i <= rowNums; i++) {
			XSSFRow row = sheet.getRow(i);
			if (null == row) { // 记录行
				continue; // 空行
			} else {
				String sciname = excelService.getStringValueFromCell(row.getCell(0)).trim();
				String chname = excelService.getStringValueFromCell(row.getCell(1)).trim();
				String type = excelService.getStringValueFromCell(row.getCell(2)).trim();
				taxon = this.taxonRepository.findOneByScientificnameAndInputer(sciname, inputer);
				if (null != taxon) {
					System.out.println("已存在：" + sciname + "\t" + chname);
				} else {
					String[] split = sciname.split(" ");
					Taxon thisTaxon = new Taxon();
					thisTaxon.setId(UUIDUtils.getUUID32());
					thisTaxon.setScientificname(sciname);
					thisTaxon.setChname(chname);
					thisTaxon.setEpithet(split[1]);
					thisTaxon.setInputer(inputer);
					thisTaxon.setInputtime(timestamp);
					thisTaxon.setSynchdate(timestamp);
					thisTaxon.setStatus(1);
					
					thisTaxon.setRankid("7");
					thisTaxon.setRank(rank);
					
					
					switch (type) {
					case "昆虫":
						taxaset = this.taxasetRepository.findOneById("bcb6d656ca094fa38d59d08457ef732a");
						break;
					case "软体动物":
						taxaset = this.taxasetRepository.findOneById("44cbb5dad5044b599c3125cf921274ec");
						break;
					case "杂草":
						taxaset = this.taxasetRepository.findOneById("44cbb5dad5044b599c3125cf921274ec");
						break;
					case "线虫":
						taxaset = this.taxasetRepository.findOneById("44cbb5dad5044b599c3125cf921274ec");
						break;
					case "原核生物":
						taxaset = this.taxasetRepository.findOneById("68d7c55db3934b588983a5a197147fff");
						break;
					case "真菌":
						taxaset = this.taxasetRepository.findOneById("fefe280f56b94beb9bea301bf52454a9");
						break;

					default:
						break;
					}
					thisTaxon.setTaxaset(taxaset);
					
					// this.taxonRepository.save(thisTaxon);
					System.out.println("数据：" + sciname + "\t" + chname + "\t" + split[1] + "\t" + type + "\t" + taxaset.getTsname());
				}
			}
		}
	}

	@Override
	public void taxKeyTrim(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		for (int i = 1; i <= rowNums; i++) {
			XSSFRow row = sheet.getRow(i);
			if (null == row) { // 记录行
				continue; // 空行
			} else {
				String sciname = excelService.getStringValueFromCell(row.getCell(0)).trim();
				String chname = excelService.getStringValueFromCell(row.getCell(1)).trim();
				String taxkey = excelService.getStringValueFromCell(row.getCell(2));
				taxkey = StringUtils.isNotBlank(taxkey) ? taxkey.trim() : "";
					
				System.out.println("数据：" + sciname + "\t" + chname + "\t" + taxkey);
			}
		}
	}

	@Override
	public void compareDataTrim(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		for (int i = 1; i <= rowNums; i++) {
			XSSFRow row = sheet.getRow(i);
			if (null == row) { // 记录行
				continue; // 空行
			} else {
				String sciname = excelService.getStringValueFromCell(row.getCell(0)).trim();
				String chname = excelService.getStringValueFromCell(row.getCell(1)).trim();
				
				if (sciname.contains("spp.")) {
					System.out.println(sciname + "$" + chname);
				} else if (chname.contains("属") && !sciname.contains("spp.")) {
					String[] split = sciname.split(" ");
					System.out.println(split[0] + "$" + chname + "$" + sciname.replace(split[0], "").trim());
				} else if (sciname.contains("pv.") || sciname.contains("subsp.")){
					String[] split = sciname.split(" ");
					String latin = split[0] + " " + split[1] + " " + split[2] + " " + split[3];
					System.out.println(latin + "$" + chname + "$" + sciname.replace(latin, "").trim());
				} else {
					String[] split = sciname.split(" ");
					String latin = split[0] + " " + split[1];
					System.out.println(latin + "$" + chname + "$" + sciname.replace(latin, "").trim());
				}
				//System.out.println(sciname + "\t" + chname);
			}
		}
	}

	@Override
	public void search(String path) throws Exception {
		String inputer = "3a29945023d04ef8a134f0f017d31kkk";
		Taxon taxon = new Taxon();
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(1);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		for (int i = 0; i <= rowNums; i++) {
			XSSFRow row = sheet.getRow(i);
			if (null == row) { // 记录行
				continue; // 空行
			} else {
				String sciname = excelService.getStringValueFromCell(row.getCell(0)).trim();
				String chname = excelService.getStringValueFromCell(row.getCell(2)).trim();
				String type = excelService.getStringValueFromCell(row.getCell(3));
				taxon = this.taxonRepository.findOneByScientificnameAndInputer(sciname, inputer);
				if (null == taxon) {
					System.out.println(sciname + "\t" + chname + "\t" + type);
				}
			}
		}
	}

	@Override
	public void updatePisLatin(String path) throws Exception {
		String sourcesid = "1123f4fb4d854e9e93b014a833878c55";
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		Taxon taxon = new Taxon();
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		for (int i = 1; i <= rowNums; i++) {
			XSSFRow row = sheet.getRow(i);
			if (null == row) { // 记录行
				continue; // 空行
			} else {
				String id = excelService.getStringValueFromCell(row.getCell(0)).trim();
				String latin = excelService.getStringValueFromCell(row.getCell(1)).trim();
				String chname = excelService.getStringValueFromCell(row.getCell(2));
				taxon = this.taxonRepository.findOneById(id);
				if (null != taxon) {
					taxon.setScientificname(latin);
					this.taxonRepository.save(taxon);
				} else {
					System.out.println(id);
				}
			}
		}
	}

	@Override
	public void handleYD(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		Taxon taxon = new Taxon();
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		for (int i = 0; i <= rowNums; i++) {
			XSSFRow row = sheet.getRow(i);
			if (null == row) { // 记录行
				continue; // 空行
			} else {
				String info = excelService.getStringValueFromCell(row.getCell(1)).trim();
				int index1 = info.indexOf("(");
				String latin = info.substring(0, index1).trim();
				taxon = this.taxonRepository.findOneByScientificnameAndInputer(latin, "");
				String author = info.replace(latin, "").trim();
			}
		}
	}

	@Override
	public JSON countCitation() {
		List<Citation> cList = this.citationRepository.findCitationIdListByTaxasetId("ff4893ee56494c0b95606546e0704a79");
		int num = 0;
		int sign = 0;
		List<String> unRule = new ArrayList<>();
		List<String> unnie = new ArrayList<>();
		for (Citation citation : cList) {
			String authorship = citation.getAuthorship();							// 命名信息 + 年代
			String citationstr = citation.getCitationstr();							// 完整印证
			try {
				int yearStart = getYearStart(citationstr);
				String year = citationstr.substring(yearStart, yearStart + 4);		// 发表年代
				int markIndex = authorship.indexOf(year);
				String author = authorship.substring(0, markIndex - 2);				// 命名信息
				if (author.contains(".")) {
					author = author.replace(".", "");
				} else if (author.contains(year)) {
					author = author.replace(year, "");
				} else if (author.contains(",")) {
					int indexOf = author.lastIndexOf(",");
					author = author.substring(indexOf + 1).trim();
				}
				int markIndex2 = citationstr.indexOf(year);
				String pressStr = citationstr.substring(markIndex2 + 5).trim();
				int markIndex3 = pressStr.indexOf(" ");
				String press = pressStr.substring(0, markIndex3);
				if (press.contains(".")) {
					int indexOf = press.indexOf(".");
					press = press.substring(0, indexOf);
				} else if (press.contains(",")) {
					press = press.replace(",", "").replace(":", "");
					
				} else if (press.contains(":")) {
					press = press.replace(":", "");
				}
				
				Ref ref = this.refRepository.findByPyearAndAuthorAndPress(year, author.trim(), press);
				String context = "[{"
						+ "\"refId\"" + ":\"" + ref.getId() + "\","
						+ "\"refS\"" + ":\"" + ref.getRefs() + "\"," 
						+ "\"refE\"" + ":\"" + ref.getRefe() + "\"," 
						+ "\"refType\"" + ":\"" + 0 + "\""
						+ "}]";
				citation.setRefjson(context);
				this.citationRepository.save(citation);
				num++;
				System.out.println("年代：" + year+"\t"+"命名信息："+author+"\t"+"Press:"+press);
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
		System.out.println("匹配结果：" + num);
		return null;
	}
	
	public static int getVolumeStart(String refstr) {
		int start = -1;
		for (int i = 0; i < refstr.length() - 1; i++) {
			String tmp = refstr.substring(i, i + 1);
			if (isNumeric(tmp)) {
				start = i;
				break;
			}
		}
		return start;
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
	
	public static boolean isMark(String str) {
		Pattern pattern = Pattern.compile("[:]");
		return pattern.matcher(str).matches();
	}
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	@Override
	public JSON handleCitation() {
		List<Citation> cList = this.citationRepository.findCitationIdListByTaxasetId("ff4893ee56494c0b95606546e0704a79");
		int num = 0;
		for (Citation citation : cList) {
			//num++;
			/*String refjson = citation.getRefjson();
			JSONArray arr = JSON.parseArray(refjson);
			if (arr.size() != 1) {
				citation.setRefjson(null);
				citationRepository.save(citation);
				//System.out.println(citation.getId());
			}*/
			// Xanthogramma seximaculatum Huo, Ren et Zheng, 2007. Fanua of Syrphidae from Mt. Qinling-Bashan in China (Insecta: Diptera): 224. Type locality: China: Shaanxi.
			try {
				String authorship = citation.getAuthorship();
				String citationstr = citation.getCitationstr();
				int indexOf = citationstr.indexOf(authorship);
				String authorship1 = authorship.replace(".", "");	// 去掉'.'的命名信息
				String pressStr = citationstr.substring(indexOf + 2).trim();
				int indexOf2 = pressStr.indexOf(".");
				String press = pressStr.substring(0, indexOf2);
				String[] split = authorship1.split(",");
				String year = split[split.length - 1];
				String author = split[split.length - 2];
				
				String volume = "";
				/*Ref ref = this.refRepository.findByPyearAndAuthorAndVolumeAndPress(year, author, volume , press);
				System.out.println(year+"\t"+author+"\t"+pressStr+"\t"+ref==null?"Null":"Not Null");
				String context = "[{"
						+ "\"refId\"" + ":\"" + ref.getId() + "\","
						+ "\"refS\"" + ":\"" + ref.getRefs() + "\"," 
						+ "\"refE\"" + ":\"" + ref.getRefe() + "\"," 
						+ "\"refType\"" + ":\"" + 0 + "\""
						+ "}]";
				citation.setRefjson(context);*/
				this.citationRepository.save(citation);
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			//System.out.println(authorship +"\t" + indexOf);
			
		}
		System.out.println("引证匹配参考文献不唯一：" + cList.size());
		return null;
	}

	@Override
	public JSON citationMatchRef() {
		List<Citation> cList = this.citationRepository.findCitationIdListByTaxasetId2("ff4893ee56494c0b95606546e0704a79");
		int num = 0;
		int sign = 0;
		List<String> unRule = new ArrayList<>();
		List<String> unnie = new ArrayList<>();
		for (Citation citation : cList) {
			String citationstr = citation.getCitationstr();
			try {
				sign++;
				int yearStart = getYearStart(citationstr);
				// 前半部分
				String str1 = citationstr.substring(0, yearStart - 2);
				String[] str1Arr = str1.split(" ");
				String latin = str1Arr[str1Arr.length - 2];
				String author1 = str1Arr[str1Arr.length - 1];					// 命名信息
				// 年代
				String year = citationstr.substring(yearStart, yearStart + 4);	// 发表年代
				// 后半部分:截取从第一个':'字符串Str，获取Str的最后一个'.'的索引位置m，截取
				String str2 = citationstr.substring(yearStart + 5).trim();		// J. August 1st Agric. Coll. 13(2): 46. Type locality: China: Xinjiang: Manasi, Shihezi.
				int markIndex1 = str2.indexOf(":");								
				String str3 = str2.substring(0, markIndex1);					// J. August 1st Agric. Coll. 13(2):
				String volume = null;
				int lastIndexOf = 0;
				if (str3.contains("(")) {
					int markIndex2 = str2.indexOf("(");								
					String str4 = str2.substring(0, markIndex2);					// J. August 1st Agric. Coll. 13(2):
					lastIndexOf = str4.lastIndexOf(".");
					volume = str4.substring(lastIndexOf + 2, markIndex2);
				} else {
					lastIndexOf = str3.lastIndexOf(".");
					volume = str3.substring(lastIndexOf + 2, markIndex1);		// 13(2):卷 -- 索引越界异常
				}
				String pressStr = str2.substring(0, lastIndexOf).replace(",", "");	// 索引越界异常
				
				List<Ref> list = new ArrayList<>();
				if (pressStr.contains(".")) {
					pressStr = pressStr.replace(".", "-");
					String[] authorArr = pressStr.split("-");					// 若干作者
					System.out.println("有." + year+"\t"+author1+"\t"+volume+"\t"+authorArr[0]);
					//list = this.refRepository.findListByPyearAndAuthorAndVolumeAndPress(year, author1, volume, authorArr[0]);
				} else {
					System.out.println("无." + year+"\t"+author1+"\t"+volume+"\t"+pressStr);
					//list = this.refRepository.findListByPyearAndAuthorAndVolumeAndPress(year, author1, volume, pressStr);
				}
				
				if (list.size() != 1) {
					unnie.add(list.size() + "\t" + citation.getId());
				} else {
					num++;
					String context = "[{"
							+ "\"refId\"" + ":\"" + list.get(0).getId() + "\","
							+ "\"refS\"" + ":\"" + list.get(0).getRefs() + "\"," 
							+ "\"refE\"" + ":\"" + list.get(0).getRefe() + "\"," 
							+ "\"refType\"" + ":\"" + 0 + "\""
							+ "}]";
					citation.setRefjson(context);
					System.out.println("匹配结果：" + citation.getId() + "\t" + context);
				}
			} catch (Exception e) {
				// e.printStackTrace();
				unRule.add(citation.getId() + "\t" + citationstr);
			}
		}
		
		for (String unnieStr : unnie) {
			//System.out.println("结果不唯一：" + unnieStr);
		}
		for (String unRuleStr : unRule) {
			//System.out.println("完整引证不符合规则：" + unRuleStr);
		}
		System.out.println("总数量：" + sign + "\t" + "匹配数量：" + num + "\t" 
				+ "不唯一数量：" + unnie.size() + "\t" + "不规则数量：" + unRule.size() + "\t");
		return null;
	}

	@Override
	public JSON contionsMatchRef(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(5);
		Taxon taxon = new Taxon();
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		for (int i = 0; i <= rowNums; i++) {
			XSSFRow row = sheet.getRow(i);
			if (null == row) { // 记录行
				continue; // 空行
			} else {
				try {
					String year = excelService.getStringValueFromCell(row.getCell(0)).trim();
					String author = excelService.getStringValueFromCell(row.getCell(1)).trim();
					String volume = excelService.getStringValueFromCell(row.getCell(2));
					String press = excelService.getStringValueFromCell(row.getCell(3));
					String citationid = excelService.getStringValueFromCell(row.getCell(4));
					String[] arr = press.split("-");
					System.out.println(year + "\t" + author + "\t" + volume + "\t" + arr[0]);
					/*List<Ref> ref = this.refRepository.findByPyearAndAuthorAndVolumeAndPress(year, author, volume, arr[0]);
					if (ref.size() == 1) {
						System.out.println(citationid);
						Citation citation = this.citationRepository.findOneById(citationid);
						
					}*/
				} catch (Exception e) {
					//e.printStackTrace();
					System.out.println("空指针");
				}
			}
		}
		return null;
	}

	@Override
	public JSON parseExcelRef(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String inputer = thisUser.getId();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		int num1 = 0;
		
		String num = null;
		String ptype = null;
		String refstr = null;
		String author = null;	
        String pyear = null;
        String title = null;
        String journal = null;
        String rVolume = null;
        String rPeriod = null;
        String refs = null;
        String refe = null;
        String isbn = null;
        String place = null;
        String press = null;
        String translator = null;	// 空值
        //String keywords = excelService.getStringValueFromCell(row.getCell(17));		// 空值
        String tpage = null;
        String tchar = null;		// 空值
        String languages = null;
        String olang = null;
        String version = null;
        String remark = null;
		
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum();
		
		List<Ref> refList = new ArrayList<>();
		Ref ref = new Ref();
		for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			}else {
				num = excelService.getStringValueFromCell(row.getCell(0));
				ptype = getValueOfPtype(excelService.getStringValueFromCell(row.getCell(2)));
				refstr = excelService.getStringValueFromCell(row.getCell(3)).trim();
				author = excelService.getStringValueFromCell(row.getCell(4));	
                pyear = excelService.getStringValueFromCell(row.getCell(5));
                title = excelService.getStringValueFromCell(row.getCell(6));
                journal = excelService.getStringValueFromCell(row.getCell(7));
                rVolume = excelService.getStringValueFromCell(row.getCell(8));
                rPeriod = excelService.getStringValueFromCell(row.getCell(9));
                refs = excelService.getStringValueFromCell(row.getCell(10));
                refe = excelService.getStringValueFromCell(row.getCell(11));
                isbn = excelService.getStringValueFromCell(row.getCell(12));
                place = excelService.getStringValueFromCell(row.getCell(13));
                press = excelService.getStringValueFromCell(row.getCell(14));
                translator = excelService.getStringValueFromCell(row.getCell(15));	// 空值
                tpage = excelService.getStringValueFromCell(row.getCell(18));
                tchar = excelService.getStringValueFromCell(row.getCell(19));		// 空值
                languages = this.languageService.getValueofLanguage(excelService.getStringValueFromCell(row.getCell(20)));
                olang = this.languageService.getValueofLanguage(excelService.getStringValueFromCell(row.getCell(21)));
                version = excelService.getStringValueFromCell(row.getCell(22));
                remark = excelService.getStringValueFromCell(row.getCell(23));
                
                author = StringUtils.isNotBlank(author) ? author.trim() : null;
                pyear = StringUtils.isNotBlank(pyear) ? pyear.trim() : null;
                title = StringUtils.isNotBlank(title) ? title.trim() : null;
                journal = StringUtils.isNotBlank(journal) ? journal.trim() : null;
                rVolume = StringUtils.isNotBlank(rVolume) ? rVolume.trim() : null;
                rPeriod = StringUtils.isNotBlank(rPeriod) ? rPeriod.trim() : null; 
                refs = StringUtils.isNotBlank(refs) ? refs.trim() : null; 
                refe = StringUtils.isNotBlank(refe) ? refe.trim() : null; 
                isbn = StringUtils.isNotBlank(isbn) ? isbn.trim() : null; 
                place = StringUtils.isNotBlank(place) ? place.trim() : null;
                press = StringUtils.isNotBlank(press) ? press.trim() : null;
                translator = StringUtils.isNotBlank(translator) ? translator.trim() : null;
                tpage = StringUtils.isNotBlank(tpage) ? tpage.trim() : null; 
                tchar = StringUtils.isNotBlank(tchar) ? tchar.trim() : null; 
                languages = StringUtils.isNotBlank(languages) ? languages.trim() : null;
                olang = StringUtils.isNotBlank(olang) ? olang.trim() : null;
                version = StringUtils.isNotBlank(version) ? version.trim() : null;
                remark = StringUtils.isNotBlank(remark) ? remark.trim() : null;
                
                List<Ref> rList = this.refRepository.findListByRefstrAndRemark(refstr, "菌物百科");
                
                if (rList.size() > 0) {
                	for (Ref r : rList) {
    					r.setKeywords(num);
    					this.refRepository.save(r);
    					//refList.add(r);
    					num1++;
    				}
				} else {
					rList = this.refRepository.findListByPtypeAndAuthorAndPyearAndTitleAndJournalAndRVolumeAndRefsAndRefeAndPlaceAndPressAndRemark(
							ptype, author, pyear, title, journal, rVolume, refs, refe, place, press, "菌物百科");
					if (rList.size() > 0) {
						for (Ref r : rList) {
	    					r.setKeywords(num);
	    					//refList.add(r);
	    					this.refRepository.save(r);
	    					num1++;
	    				}
					} else {
						ref = new Ref(
		                		UUIDUtils.getUUID32(), ptype, refstr, author, pyear,
		                		title, journal, rVolume, rPeriod, refs, refe, isbn, place, press, 
		                		translator, num, tpage, tchar, languages, olang, version, remark);
						ref.setInputer(inputer);
						ref.setInputtime(timestamp);
						ref.setSynchdate(timestamp);
						ref.setStatus(1);
						ref.setSynchstatus(0);
						refList.add(ref);
					}
				}
			}
		}
		long start = System.currentTimeMillis();
        this.batchInsertService.batchInsertRef(refList);
        long end = System.currentTimeMillis();
        System.err.println("Ref批量存储完成时间：" + (end - start) + "\t" + "数据量：" + refList.size() + "\t" + "重复数据：" + num1);
		return null;
	}
	
	@Override
	public JSON parseExcelTaxon(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String inputer = thisUser.getId();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Taxaset thisTaxaset = this.taxasetRepository.findOneById("9569e5a97f644959b35aceaa50cec993");
		String sourcesid = "89f92a95-faef-4641-9fbc-c4b82bb92458";
		String expert = "a3ffca7c003c4e4b9bb8fd128810b56b";
		
    	String sciname = null;
    	String enname = null;
    	String chname = null;
    	String authorstr = null;
    	String refClassSys = null;
    	String refjson = null;
    	String remark = null;
		List<Taxon> taxons = new ArrayList<>();
		Taxon thisTaxon = new Taxon();
		Rank rank = new Rank();
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheetx
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		for (int i = 1; i < rowNums; i++) {
			XSSFRow row = sheet.getRow(i);
			if (null == row) {	// 记录行
				continue;		// 空行
			} else {
				//* 学名（接受名）	*中文名	* 命名信息	*命名法规	*学名状态	* 分类等级	*参考的分类体系
				//0				1		2			3		4		5			6		
				//界	界拉丁名	门	门拉丁名	纲	纲拉丁名	目	目拉丁名	科	科拉丁名	属	属拉丁名
				//7	8		9	10		11	12		13	14		15	16		17	18
				//*参考文献	*数据源	*审核专家	版权所有者	版权声明	共享协议	备注
				//19		20		21		22		23		24		25
            	try {
					remark = "{"
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
							+ "\"" + "remark" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(25)) + "\"," 
							+ "\"" + "scientificNameStatus" + "\"" + ":" + "\"" + excelService.getStringValueFromCell(row.getCell(4)) + "\"," 
							+ "}";
					
					sciname = excelService.getStringValueFromCell(row.getCell(0)).trim();
					enname = excelService.getStringValueFromCell(row.getCell(5));
					rank = this.rankRepository.findOneByEnname(enname);
					chname = excelService.getStringValueFromCell(row.getCell(1));
					authorstr = excelService.getStringValueFromCell(row.getCell(2));
					refClassSys = excelService.getStringValueFromCell(row.getCell(6));
					refjson = excelService.getStringValueFromCell(row.getCell(19));
					
					chname = StringUtils.isNotBlank(chname) ? chname.trim() : null;
					authorstr = StringUtils.isNotBlank(authorstr) ? authorstr.trim() : null;
					refClassSys = StringUtils.isNotBlank(refClassSys) ? refClassSys.trim() : null;
					refjson = StringUtils.isNotBlank(refjson) ? refjson.trim() : null;
					
					Taxon existTaxon = this.taxonRepository.findOneByScientificnameAndTaxasetId(sciname, thisTaxaset.getId());
					if (null == existTaxon) {
						//new Taxon(scientificname, chname, authorstr, nomencode, rankid, 
						//			refClassSys, refjson, sourcesid, expert, remark)
						thisTaxon = new Taxon(
								sciname, chname, authorstr, "ICBN",
								rank.getId(), 
								refClassSys, 
								refjson,	
								sourcesid, 
								expert,	
								remark, "");
						
						thisTaxon.setId(UUIDUtils.getUUID32());
						thisTaxon.setInputer(inputer);
						thisTaxon.setInputtime(timestamp);
						thisTaxon.setSynchdate(timestamp);
						thisTaxon.setStatus(1);
						thisTaxon.setTaxonCondition(2);//审核通过
						thisTaxon.setTci("2019菌物数据");
						thisTaxon.setRank(rank);
						thisTaxon.setTaxaset(thisTaxaset);
						
						taxons.add(thisTaxon);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		long start = System.currentTimeMillis();
        this.batchInsertService.batchInsertTaxon(taxons);
        long end = System.currentTimeMillis();
        System.err.println("Taxon批量存储完成时间：" + (end - start) + "\t" + taxons.size());
		return null;
	}

	@Override
	public JSON parseExcelCitation(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String inputer = thisUser.getId();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String expert = "a3ffca7c003c4e4b9bb8fd128810b56b";
		List<Citation> citations = new ArrayList<>();
		Citation thisCitation = new Citation();
		Taxon taxon = new Taxon();
		int num = 0;
		String sciname = null;
		String authorship = null;
		String citationstr = null;
		String refjson = null;
		String sourcesid = null;
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		for (int i = 0; i <= rowNums; i++) {
			XSSFRow row = sheet.getRow(i);
			if (null == row) { // 记录行
				continue; // 空行
			} else {
				// *学名	* 命名信息	* 名称状态(类型)	* 完整引证	* 接受名（引证学名的接受名）	* 数据源	*参考文献	*审核专家	版权所有者	版权声明	共享协议
				// 0	1		2				3		4					5		6		7		8		9		10
				sciname = excelService.getStringValueFromCell(row.getCell(0)).trim();
				taxon = this.taxonRepository.findOneByScientificnameAndTaxasetId(sciname, "9569e5a97f644959b35aceaa50cec993");
				
				authorship = excelService.getStringValueFromCell(row.getCell(1));
				citationstr = excelService.getStringValueFromCell(row.getCell(3));
				refjson = excelService.getStringValueFromCell(row.getCell(6));
				sourcesid = excelService.getStringValueFromCell(row.getCell(5));
				
				authorship = StringUtils.isNotBlank(authorship) ? authorship.trim() : null;
				citationstr = StringUtils.isNotBlank(citationstr) ? citationstr.trim() : null;
				refjson = StringUtils.isNotBlank(refjson) ? refjson.trim() : null;
				sourcesid = (sourcesid == "1") ? "8755a0c3f542412cb469f7c9b94dea8e" : "89f92a95-faef-4641-9fbc-c4b82bb92458";
				if (null != taxon) {
					List<Citation> citationList = this.citationRepository.findCitationsByTaxonId(taxon.getId());
					if (null == citationList) {
						thisCitation = new Citation(
								sciname, authorship, "1", citationstr, sourcesid, refjson, expert, taxon);
						
						thisCitation.setId(UUIDUtils.getUUID32());
						thisCitation.setInputer(inputer);
						thisCitation.setInputtime(timestamp);
						thisCitation.setSynchdate(timestamp);
						thisCitation.setStatus(1);
						citations.add(thisCitation);
						num++;
					}
				} else {
					System.out.println("引证：" + sciname + "，未匹配到Taxon");
				}
			}
		}
		long start = System.currentTimeMillis();
        this.batchInsertService.batchInsertCitation(citations);
        long end = System.currentTimeMillis();
        System.err.println("Citation批量存储完成时间：" + (end - start) + "\t" + "数据量：" + citations.size());
        /*System.err.println("Citation批量存储完成数据量：" + num);*/
		return null;
	}

	@Override
	public JSON parseExcelDescription(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String inputer = thisUser.getId();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String expert = "a3ffca7c003c4e4b9bb8fd128810b56b";	// 姚一建
		String sourcesid = null;
		List<Description> descriptions = new ArrayList<>();
		Description thisDescription = new Description();
		Taxon taxon = new Taxon();
		Descriptiontype descriptiontype = new Descriptiontype();
		int num = 0;
		String sciname = null;
		String desdate = null;
		String describer = null;
		String language = null;//描述语言
		String destypeid = null;//描述类型
		String descontent = null;
		String remark = null;
		String refjson = null;
		String rightsholder = null;
		String dCopyright = null;
		String licenseid = null;//共享协议
		
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		for (int i = 1; i < rowNums; i++) {
			XSSFRow row = sheet.getRow(i);
			if (null == row) { // 记录行
				continue; // 空行
			} else {
				// *学名	描述时间	描述人	*语言	* 描述类型	* 描述内容	备注	*参考文献	
				//	0	1		2		3		4		5	6	7	
				//* 数据源	*审核专家	*版权所有者	*版权声明	*共享协议
				//	8		9		10		11		12
				try {
					sciname = excelService.getStringValueFromCell(row.getCell(0)).trim();
					taxon = this.taxonRepository.findOneByScientificnameAndTaxasetId(sciname, "9569e5a97f644959b35aceaa50cec993");
					desdate = excelService.getStringValueFromCell(row.getCell(1));
					describer = excelService.getStringValueFromCell(row.getCell(2));
					language = this.languageService.getValueofLanguage(excelService.getStringValueFromCell(row.getCell(3)));	//描述语言
					destypeid = excelService.getStringValueFromCell(row.getCell(4));	//描述类型
					descontent = excelService.getStringValueFromCell(row.getCell(5));
					remark = excelService.getStringValueFromCell(row.getCell(6));
					refjson = excelService.getStringValueFromCell(row.getCell(7));
					sourcesid = excelService.getStringValueFromCell(row.getCell(8));
					rightsholder = excelService.getStringValueFromCell(row.getCell(10));
					dCopyright = excelService.getStringValueFromCell(row.getCell(11));
					licenseid = excelService.getStringValueFromCell(row.getCell(12));//共享协议
					
					desdate = StringUtils.isNotBlank(desdate) ? desdate.trim() : null; 
					describer = StringUtils.isNotBlank(describer) ? describer.trim() : null; 
					descontent = StringUtils.isNotBlank(descontent) ? descontent.trim() : null; 
					remark = StringUtils.isNotBlank(desdate) ? remark.trim() : null; 
					refjson = StringUtils.isNotBlank(desdate) ? refjson.trim() : null; 
					rightsholder = StringUtils.isNotBlank(desdate) ? rightsholder.trim() : null; 
					dCopyright = StringUtils.isNotBlank(desdate) ? dCopyright.trim() : null; 
					sourcesid = (sourcesid == "1") ? "8755a0c3f542412cb469f7c9b94dea8e" : "89f92a95-faef-4641-9fbc-c4b82bb92458";
					if (destypeid.contains("讨论")) {
						destypeid = "分类讨论";
					}
					descriptiontype = this.descriptionTypeRepository.findOneByName(destypeid);
					
					if (null != taxon) {
						thisDescription = new Description(
								desdate, describer, language, descriptiontype.getId(), descontent, remark, refjson, sourcesid, expert, rightsholder, dCopyright, licenseid, taxon);
						thisDescription.setId(UUIDUtils.getUUID32());
						thisDescription.setInputer(inputer);
						thisDescription.setInputtime(timestamp);
						thisDescription.setSynchdate(timestamp);
						thisDescription.setStatus(1);
						thisDescription.setDescriptiontype(descriptiontype);
						
						this.descriptionRepository.save(thisDescription);
						num++;
						//descriptions.add(thisDescription);
					} else {
						System.out.println("描述：" + sciname + "，未匹配到Taxon");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		/*long start = System.currentTimeMillis();
        this.batchInsertService.batchInsertDescription(descriptions);
        long end = System.currentTimeMillis();
        System.err.println("Description批量存储完成时间：" + (end - start) + "\t" + "数据量：" + descriptions.size());*/
        System.err.println("Description批量存储完成数据量：" + num);
		return null;
	}

	@Override
	public JSON parseExcelCommonname(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String inputer = thisUser.getId();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String expert = "a3ffca7c003c4e4b9bb8fd128810b56b";	// 姚一建
		String sourcesid = null;
		int num = 0;
		String sciname = null;
		String commonname = null;
		String language = null;
		String refjson = null;
		String remark = null;
		List<Commonname> commonnames = new ArrayList<>();
		Commonname thisCommonname = new Commonname();
		Taxon taxon = new Taxon();
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		for (int i = 1; i < rowNums; i++) {
			XSSFRow row = sheet.getRow(i);
			if (null == row) { // 记录行
				continue; // 空行
			} else {
				//*学名	俗名	*俗名语言	*参考文献	* 数据源	*审核专家	版权所有者	版权声明	共享协议	备注
				//0		1	2		3		4		5		6		7		8		9	
				// new Commonname(commonname, language, refjson, sourcesid, expert, remark, taxon)
				try {
					sciname = excelService.getStringValueFromCell(row.getCell(0)).trim();
					taxon = this.taxonRepository.findOneByScientificnameAndTaxasetId(sciname, "9569e5a97f644959b35aceaa50cec993");
					commonname = excelService.getStringValueFromCell(row.getCell(1));
					language = this.languageService.getValueofLanguage(excelService.getStringValueFromCell(row.getCell(2)));
					refjson = excelService.getStringValueFromCell(row.getCell(3));
					sourcesid = excelService.getStringValueFromCell(row.getCell(4));
					remark = excelService.getStringValueFromCell(row.getCell(9));

					refjson = StringUtils.isNotBlank(refjson) ? refjson.trim() : null;
					remark = StringUtils.isNotBlank(remark) ? remark.trim() : null;
					sourcesid = (sourcesid == "1") ? "8755a0c3f542412cb469f7c9b94dea8e" : "89f92a95-faef-4641-9fbc-c4b82bb92458";
					
					if (null != taxon) {
						thisCommonname = new Commonname(commonname, language, refjson, sourcesid, expert, remark, taxon);
						thisCommonname.setId(UUIDUtils.getUUID32());
						thisCommonname.setInputer(inputer);
						thisCommonname.setInputtime(timestamp);
						thisCommonname.setSynchdate(timestamp);
						thisCommonname.setStatus(1);
						
						this.commonnameRepository.save(thisCommonname);
						num++;
						//commonnames.add(thisCommonname);
					} else {
						System.out.println("俗名：" + sciname + "，未匹配到Taxon");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		/*long start = System.currentTimeMillis();
        this.batchInsertService.batchInsertCommonname(commonnames);
        long end = System.currentTimeMillis();
        System.err.println("Commonname批量存储完成时间：" + (end - start) + "\t" + "数据量：" + commonnames.size());*/
        System.err.println("Commonname批量存储完成数据量：" + num);
		return null;
	}
	
	@Override
	public JSON parseExcelDistributiondate(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String expert = "a3ffca7c003c4e4b9bb8fd128810b56b";	// 姚一建
		String sourcesid = null;
		String inputer = thisUser.getId();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Descriptiontype descriptiontype = this.descriptionTypeRepository.findOneById("201");
	
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		String sname = new String();
		Description thisDescription = new Description();
		Taxon thisTaxon = new Taxon();
		StringBuilder sb = new StringBuilder(); 
		for (int i = 1; i <= rowNums; i++) {
	        XSSFRow row = sheet.getRow(i);
	        if (null == row) { 			// 记录行
	        	continue; 									// 空行
	        }else {											// 构建集合对象
	        	if (StringUtils.isNotBlank(sname)) {// 是否是第一行
	        		String sciname = excelService.getStringValueFromCell(row.getCell(0)).trim();
	        		if (sname.equals(sciname)) { //	前后比较 拉丁名相同
	        			String province = excelService.getStringValueFromCell(row.getCell(1));
	        			String city = excelService.getStringValueFromCell(row.getCell(2));
						if (sb.toString().contains(province)) { // 省份是否已存在
							sb.append(city).append("、");
						} else {								// 省份不存在
							sb.append("&").append(province).append("：").append(city).append("、");
						}
						System.out.println(sciname + "\t" + sb.toString());
					} else {					// 前后比较 拉丁名不同
						sname = sciname;
						sb = new StringBuilder();
						sb.append(excelService.getStringValueFromCell(row.getCell(1)));
						sb.append("：");
						sb.append(excelService.getStringValueFromCell(row.getCell(2)));
						sb.append("、");
					}
				} else {							// 不是第一行
					sname = excelService.getStringValueFromCell(row.getCell(0));
					sb.append(excelService.getStringValueFromCell(row.getCell(1)));
					sb.append("：");
					sb.append(excelService.getStringValueFromCell(row.getCell(2)));
					sb.append("、");
				}
	        	
	        	/*if (StringUtils.isNotBlank(sciname)) {
	        		String descontent = excelService.getStringValueFromCell(row.getCell(5));
	        		thisTaxon = this.taxonRepository.findOneByScientificnameAndInputer(sciname, thisUser.getId());
	        		if (null != thisTaxon) {
						Description desc = new Description();
						desc.setId(UUIDUtils.getUUID32());
						desc.setDestitle(sciname + "的分布信息"); 
						desc.setDescontent(descontent);
						desc.setDescriptiontype(descriptiontype);
						desc.setLanguage("1");
						desc.setLicenseid("6");
						desc.setSourcesid(sourcesid);
						desc.setExpert(expert);
						desc.setStatus(1);
						desc.setInputer(inputer);
						desc.setInputtime(timestamp);
						desc.setSynchdate(timestamp);
						desc.setSynchstatus(0);
						desc.setTaxon(thisTaxon);
						//this.descriptionRepository.save(desc);
					} else {
						set.add(sciname);
					}
				}*/
			}
	    }
		
		return null;
	}

	@Override
	public JSON parseExcelTraitdata(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String inputer = thisUser.getId();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String expert = "a3ffca7c003c4e4b9bb8fd128810b56b";	// 姚一建
		List<Traitdata> traitdatas = new ArrayList<>();
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		for (int i = 0; i <= rowNums; i++) {
			XSSFRow row = sheet.getRow(i);
			if (null == row) { // 记录行
				continue; // 空行
			} else {
				
			}
		}
		long start = System.currentTimeMillis();
        this.batchInsertService.batchInsertTraitdata(traitdatas);
        long end = System.currentTimeMillis();
        System.err.println("Traitdata批量存储完成时间：" + (end - start));
		return null;
	}

	@Override
	public JSON parseExcelMultimedia(String path) throws Exception {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String inputer = thisUser.getId();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String expert = "a3ffca7c003c4e4b9bb8fd128810b56b";	// 姚一建
		String sourcesid = null;
		
		String sciname = null;
		String mediatype = null;
		String context = null;
		String title = null;
		String oldPath = null;
		String rightsholder = null;
		String copyright = null;
		String mpath = null;
		String createtime = null;
		String creator = null;
		String publisher = null;
		String contributor = null;
		String country = null;
		String province = null;
		String city = null;
		String county = null;
		String locality = null;
		String lng = null;
		String lat = null;
		String refjson = null;
		int num = 0;
		List<Multimedia> multimedias = new ArrayList<>();
		Multimedia thisMultimedia = new Multimedia();
		Taxon taxon = new Taxon();
		License license = this.licenseRepository.findOneById("6");
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		for (int i = 1; i < rowNums; i++) {
			XSSFRow row = sheet.getRow(i);
			if (null == row) { // 记录行
				continue; // 空行
			} else {
				//*学名	*媒体类型	*注解（图注）	标签	*文件地址	* 数据源	*审核专家	
				//0		1		2			3	4		5		6	
				//*版权所有者	*版权声明	*共享协议	原始链接	创建时间 	创建者	发布者	
				//7			8		9		10		11		12		13
				//贡献者	国家	省/州	市	县/区	小地名	经度	纬度	参考文献
				//14	15	16	17	18	19		20	21	22
				//new Multimedia(mediatype, context, title, oldPath, sourcesid, expert, 
				//rightsholder, copyright, lisenceid, path, createtime, creator, publisher, 
				//contributor, country, province, city, county, locality, lng, lat, refjson, license, taxon)
				try {
					sciname = excelService.getStringValueFromCell(row.getCell(0)).trim();
					taxon = this.taxonRepository.findOneByScientificnameAndTaxasetId(sciname, "9569e5a97f644959b35aceaa50cec993");
					
					mediatype = excelService.getStringValueFromCell(row.getCell(1));
					context = excelService.getStringValueFromCell(row.getCell(2));
					title = excelService.getStringValueFromCell(row.getCell(3));
					oldPath = excelService.getStringValueFromCell(row.getCell(4));
					rightsholder = excelService.getStringValueFromCell(row.getCell(7));
					copyright = excelService.getStringValueFromCell(row.getCell(8));
					mpath = excelService.getStringValueFromCell(row.getCell(10));
					createtime = excelService.getStringValueFromCell(row.getCell(11));
					creator = excelService.getStringValueFromCell(row.getCell(12));
					publisher = excelService.getStringValueFromCell(row.getCell(13));
					contributor = excelService.getStringValueFromCell(row.getCell(14));
					country = excelService.getStringValueFromCell(row.getCell(15));
					province = excelService.getStringValueFromCell(row.getCell(16));
					city = excelService.getStringValueFromCell(row.getCell(17));
					county = excelService.getStringValueFromCell(row.getCell(18));
					locality = excelService.getStringValueFromCell(row.getCell(19));
					lng = excelService.getStringValueFromCell(row.getCell(20));
					lat = excelService.getStringValueFromCell(row.getCell(21));
					refjson = excelService.getStringValueFromCell(row.getCell(22));
					sourcesid = excelService.getStringValueFromCell(row.getCell(5));

					mediatype = this.getValueOfMediatype(mediatype);
					context = StringUtils.isNotBlank(context) ? context.trim() : null;
					title = StringUtils.isNotBlank(title) ? title.trim() : null;
					oldPath = StringUtils.isNotBlank(oldPath) ? oldPath.trim() : null;
					rightsholder = StringUtils.isNotBlank(rightsholder) ? rightsholder.trim() : null;
					copyright = StringUtils.isNotBlank(copyright) ? copyright.trim() : null;
					mpath = StringUtils.isNotBlank(mpath) ? mpath.trim() : null;
					createtime = StringUtils.isNotBlank(createtime) ? createtime.trim() : null;
					creator = StringUtils.isNotBlank(creator) ? creator.trim() : null;
					publisher = StringUtils.isNotBlank(publisher) ? publisher.trim() : null;
					contributor = StringUtils.isNotBlank(contributor) ? contributor.trim() : null;
					country = StringUtils.isNotBlank(country) ? country.trim() : null;
					province = StringUtils.isNotBlank(province) ? province.trim() : null;
					city = StringUtils.isNotBlank(city) ? city.trim() : null;
					county = StringUtils.isNotBlank(county) ? county.trim() : null;
					locality = StringUtils.isNotBlank(locality) ? locality.trim() : null;
					lng = StringUtils.isNotBlank(lng) ? lng.trim() : null;
					lat = StringUtils.isNotBlank(lat) ? lat.trim() : null;
					refjson = StringUtils.isNotBlank(refjson) ? refjson.trim() : null;
					sourcesid = (sourcesid == "1") ? "8755a0c3f542412cb469f7c9b94dea8e" : "89f92a95-faef-4641-9fbc-c4b82bb92458";
					
					if (null != taxon) {
						thisMultimedia = new Multimedia(mediatype, context, title, oldPath, sourcesid, expert, rightsholder,
								copyright, license.getId(), mpath, createtime, creator, publisher, contributor, country, 
								province, city, county, locality, -9999D, -9999D, refjson, license, taxon);
						
						thisMultimedia.setId(UUIDUtils.getUUID32());
						thisMultimedia.setInputer(inputer);
						thisMultimedia.setInputtime(timestamp);
						thisMultimedia.setSynchdate(timestamp);
						thisMultimedia.setStatus(1);
						
						this.multimediaRepository.save(thisMultimedia);
						num++;
						//multimedias.add(thisMultimedia);
					} else {
						System.out.println("多媒体：" + sciname + "，未匹配到Taxon");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		/*long start = System.currentTimeMillis();
        this.batchInsertService.batchInsertMultimedia(multimedias);
        long end = System.currentTimeMillis();
        System.err.println("Multimedia批量存储完成时间：" + (end - start) + "\t" + "数据量：" + num);*/
        System.err.println("Multimedia批量存储完成数据量：" + num);
		return null;
	}

	@Override
	public JSON citationMatchRefYD() throws Exception {
		String sourcesid = "ae4d6943-a621-4bb4-80a2-0cf186ad950b";
		// 查询指定数据源的引证
		List<Citation> clist = this.citationRepository.findListBySourcesid(sourcesid);
		System.out.println("待匹配引证数据量：" + clist.size());
		// 记录匹配错误的引证
		List<Citation> errorList = new ArrayList<>();
		// 记录包含多个文献的印证
		List<Citation> citationsList = new ArrayList<>();
		for (Citation citation : clist) {
			String citationstr = new String();
			// 处理完整引证 - 截取、替换
			citationstr = handleCitationStr(citation.getCitationstr());
			try {
				// 按空格分割处理后的引证
				List<String> paraList = new ArrayList<>();
				// 记录根据年份、remark查询到的文献
				List<Ref> refList = new ArrayList<>();
				// 记录匹配到的参考文献
				String refjson = new String();
				// 年份起始索引位置
				int yearStart = getYearStart(citationstr);
				// 年份
				String year = citationstr.substring(yearStart, yearStart + 4).trim();
				refList = this.refRepository.findByYear(year, "双翅目蝇类名录");			// 根据年份、remark字段查询符合条件的文献集合
				
				String substring = citationstr.substring(yearStart + 5);			// 从A'中截取卷期页码A''(A'中年份 + 1之后的部分)
				int countMark = 0;
				// 统计指定字符，判断是否是多条引证
				countMark = countMark(substring, countMark);
				if (countMark != 0) {
					citationsList.add(citation);
				} else {
					if (!substring.contains(": ")) {
						substring = substring.replace(":", ": ");
					}
					// 获取引证中页码的索引位置，从而获取页码
					int markIndex = substring.lastIndexOf(":");						// 获取A''指定字符串的索引
					substring = substring.substring(markIndex + 1);					// 从A''中截取页码
					// 替换一些非页码的字符串
					String pageNum = replacePageNumStr(substring.trim());
					// 移除引证中所有的];,. 字符 
					pageNum = removeSingle(pageNum);
					// 补充空字符
					citationstr = addSpace(citationstr);
					paraList = getParaList(citationstr);
					// 获取匹配度最高的文献
					Ref ref = getDescRef(refList, paraList);
					if (null == ref) {
						System.out.println("引证未匹配到文献：" + citation.getId() + "\t" + citation.getTaxon().getScientificname() + "\t" + citation.getTaxon().getChname() + "\t" + citation.getSciname());
					} else {
						/*if (isNumeric(pageNum)) {	// 是数字
							refjson = getRefJsonWithPageNums(ref, pageNum, pageNum);
							//System.out.println(ref.getId() + "\t" + refjson);
						} else if (isPage(pageNum)) {
							String[] num = getNum(pageNum);
							refjson = getRefJsonWithPageNums(ref, num[0], num[1]);
							//System.out.println(ref.getId() + "\t" + refjson);
						} else {					// 不是数字
							refjson = getRefJsonNoPage(ref);
							//System.out.println("无：" + ref.getId() + "\t" + refjson);
						}*/
					}
				}
				/*citation.setRefjson(refjson);
				this.citationRepository.save(citation);*/
			} catch (Exception e) {
				errorList.add(citation);
			}
		}
		// 不规则无法拆分引证匹配对应文献
		matchCitationsRefsWithError(errorList);
		// 完整引证包含多条引证匹配多条文献
		matchCitationsRefs(citationsList);
		return null;
	}

	/* 完整引证包含多条引证匹配多条文献*/	
	private void matchCitationsRefs(List<Citation> citationsList) {
		for (Citation citation : citationsList) {
			// 处理完整引证 - 截取、替换
			String citationStr = handleCitationStr(citation.getCitationstr());
			// 指定字符拆分多条引证
			String[] split = citationStr.split(";");
			// 获取匹配文献
			String refjson = getRefjsonsByCitations(split, citation);
			if (refjson.endsWith(",")) {
				refjson = refjson.substring(0, refjson.length() - 1) + "]";
			}
			JSONArray array = new JSONArray();
			JSONArray jsonArr = JSON.parseArray(refjson);
			if (jsonArr.size() != citationsList.size()) {
				//System.out.println("待审核：" + citation.getId() + "\t" + citation.getCitationstr());
			}
			/*citation.setRefjson(refjson);
			this.citationRepository.save(citation);*/
		}
	}
	/* 不规则无法拆分引证匹配对应文献*/
	private void matchCitationsRefsWithError(List<Citation> errorList) {
		for (Citation error : errorList) {
			// 处理
			String citationStr = handleCitationStr(error.getCitationstr());
			/*System.out.println(error.getTaxon().getScientificname() + "\t" + error.getTaxon().getChname() + "\t" + 
					error.getSciname());*/
			if (StringUtils.isNotBlank(citationStr) && citationStr.split(" ").length > 1) {
				citationStr = addSpace(citationStr);
				int yearStart = getYearStart(citationStr);
				if (-1 != yearStart) {
					String year = citationStr.substring(yearStart, yearStart + 4).trim();
					List<Ref> refList = this.refRepository.findByYear(year, "双翅目蝇类名录");			// 根据年份、remark字段查询符合条件的文献集合
					List<String> paraList = getParaList(citationStr);
					Ref ref = getDescRef(refList, paraList);
					if (null != ref) {
						/*String refjson = getRefJsonNoPage(ref);
						error.setRefjson(refjson);
						this.citationRepository.save(error);*/
					} else {
						System.out.println("引证未匹配到文献：" + error.getId() + "\t" + error.getTaxon().getScientificname() + "\t" + error.getTaxon().getChname() + "\t" + error.getSciname());
					}
				} else {
					//System.out.println("error不匹配的数据：" + citationStr);
				}
			} else {
				//System.out.println("error不匹配的数据：" + citationStr);
			}
		}
	}

	@Override
	public JSON citationMatchRefYDPageNum() throws Exception {
		String sourcesid = "ae4d6943-a621-4bb4-80a2-0cf186ad950b";
		String shortStr = "NotNum&";
		List<Citation> clist = this.citationRepository.findListBySourcesidAndShortstr(sourcesid, shortStr);
		System.out.println(clist.size());
		String shortrefs = new String();
		int yearStart = 0;
		int markIndex = 0;
		String year = new String();
		String substring = new String();
		String pageNum = new String();
		List<Ref> refList = new ArrayList<>();
		int count = 0;
		String startS = new String();
		String endE = new String();
		List<String> paraList = new ArrayList<>();
		Ref ref = new Ref();
		String refjson = new String();
		for (Citation citation : clist) {
			shortrefs = citation.getShortrefs().replace("NotNum&", "");
			yearStart = getYearStart(shortrefs);
			year = shortrefs.substring(yearStart, yearStart + 4).trim();
			refList = this.refRepository.findByYear(year, "双翅目蝇类名录");
			paraList = getParaList(shortrefs);
			
			substring = shortrefs.substring(yearStart + 5);
			markIndex = substring.indexOf(":");									// 获取A''指定字符串的索引
			substring = substring.substring(markIndex + 1);						// 从A''中截取页码
			
			// --- ref = getDescRef(refList, paraList);
			pageNum = substring.replace(".", "")
					.replace("syn. nov", "")
					.replace("Nomen nudum", "")
					.replace(", syn nov", "")
					.replace("Misidentification", "")
					.replace("Name suppressed by ICZN, 1963", "")
					.replace("nomen nudum", "")
					.replace("Naturgesch Thierr", "")
					.replace("?", "")
					.replace("Unavailable name", "")
					.replace("Ann Soc Ent Fr", "")
					.replace(", Figures 10-12", "")
					.replace("(as subgenus", "")
					.replace("Species Insect", "")
					.replace("(Syn as P invicta by Okada, 1988 Kontyû 56", "")
					.replace("Sieboldia Suppl", "")
					.replace("Sicboldia Suppl", "")
					.replace("缺少", "")
					.replace("(In part)", "")
					.replace("[unnecessary new name for Rhyncomya flavibasis sensu Fan, 1992]", "")
					.replace("pl", "")
					.replace(";", "")
					.replace("Mem Soc Sci Agric Lille 1842", "")
					.replace("(Preoccupied by Linnaeus, 1758 [= Temnostoma vespiforme", "")
					.replace(", not Mesnil, 1963  of Chinese specimens", "")
					.replace("reacement name for Aricia Robineau-Desvoidy, 1830", "")
					.replace("Reacement name for Strongylophthalmus Hendel, 1902 nec Mannerheim, 1853", "")
					.replace("Syn of similis Lamb by Brake et Bächli, 2008 World CatIns 9", "")
					.replace("Syst Study Dros Jap", "").trim();						// 处理页码
			if (isNumeric(pageNum)) {	// 是数字
				startS = endE = pageNum;
				refjson = getRefJsonWithPageNums(ref, startS, endE);
			} else if (isPage(pageNum)){
				String[] pages = getNum(pageNum);
				startS = pages[0];
				endE= pages[1];
				refjson = getRefJsonWithPageNums(ref, startS, endE);
			} else {	// 不是数字
				StringBuffer sbf = new StringBuffer();
				pageNum = sbf.append(pageNum).reverse().toString();
				StringBuffer sb = new StringBuffer();
				//pageNum = removeYear(pageNum);
				pageNum = sb.append(pageNum).reverse().toString();
				pageNum = removePageNumStr(pageNum);
				if (pageNum.endsWith(",") || pageNum.startsWith(",")) {
					pageNum = pageNum.replace(",", "").replace("(", "").replace("-", "").replace("34(3)：", "");
					if (isNumeric(pageNum)) {
						startS = endE = pageNum;
						refjson = getRefJsonWithPageNums(ref, startS, endE);
					} else {
						count++;
						System.out.println(pageNum);
					}
				}
			}
			citation.setRefjson(refjson);
			//System.out.println(refjson);
			this.citationRepository.save(citation);
		}
		System.out.println(clist.size() - count);
		return null;
	}

	@Override
	public JSON parseExcelCitationOfRef(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum();

		List<Ref> refList = new ArrayList<>();
		XSSFRow row = null;
		String cId = new String();
		Citation citation = new Citation();
		for (int rowNum = 0; rowNum < rowNums; rowNum++) {
			row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				cId = excelService.getStringValueFromCell(row.getCell(0));
				citation = this.citationRepository.findOneById(cId);
				if (null != citation) {
					if (StringUtils.isNotBlank(citation.getRefjson())) {
						citation.setRefjson(null);
						this.citationRepository.save(citation);
					}
				}
			}
		}
		return null;
	}

	@Override
	public JSON matchCitationOfRefIsNull() throws Exception {
		String sourcesid = "ae4d6943-a621-4bb4-80a2-0cf186ad950b";
		List<Citation> clist = this.citationRepository.findListBySourcesidAndRefjson(sourcesid);
		List<Ref> refList = new ArrayList<>();
		List<String> paraList = new ArrayList<>();
		String citationStr = new String();
		String refjson = new String();
		int yearStart = 0;
		int markIndex = 0;
		String year = new String();
		String substring = new String();
		Ref ref = new Ref();
		for (Citation citation : clist) {
			citationStr = citation.getCitationstr();
			try {
				citationStr = handleCitationStr(citationStr);
				if (citationStr.contains(";")) {
					String[] split = citationStr.split(";");
					refjson = getRefjsonsByCitations(split, citation);
					if (refjson.endsWith(",")) {
						refjson = refjson.substring(0, refjson.length() - 1) + "]";
					}
				} else {
					yearStart = getYearStart(citationStr);
					year = citationStr.substring(yearStart, yearStart + 4).trim();
					refList = this.refRepository.findByYear(year, "双翅目蝇类名录");			// 根据年份、remark字段查询符合条件的文献集合
					paraList = getParaList(citationStr);
					substring = citationStr.substring(yearStart + 5);	
					markIndex = substring.lastIndexOf(":");									// 获取A''指定字符串的索引
					substring = substring.substring(markIndex + 1).trim();						// 从A''中截取页码
					if (isNumeric(substring)) {
						// --- ref = getDescRef(refList, paraList);
						refjson = getRefJsonWithPageNum(ref, substring);
					} else if (isPage(substring)) {
						String[] nums = getNum(substring);
						// --- ref = getDescRef(refList, paraList);
						refjson = getRefJsonWithPageNums(ref, nums[0], nums[1]);
					} else {
						// System.out.println("不是数字：" + substring);
					}
				}
				if (StringUtils.isNotBlank(refjson)) {
					citation.setRefjson(refjson);
					this.citationRepository.save(citation);
				}
			} catch (Exception e) {
				//System.out.println("异常数据：" + citationStr);
			}
		}
		return null;
	}

	private String getRefjsonsByCitations(String[] split, Citation citation) {
		String citationStr = new String();
		List<String> list = new ArrayList<>();
		for (int i = 0; i < split.length; i++) {
			citationStr = handleCitationStr(split[i]);
			try {
				int yearStart = getYearStart(citationStr);
				if (yearStart != -1) {
					String year = citationStr.substring(yearStart, yearStart + 4).trim();
					List<Ref> refList = this.refRepository.findByYear(year, "双翅目蝇类名录");			// 根据年份、remark字段查询符合条件的文献集合
					citationStr = addSpace(citationStr);
					List<String> paraList = getParaList(citationStr);
					String substring = citationStr.substring(yearStart + 5);	
					int markIndex = substring.lastIndexOf(":");								// 获取A''指定字符串的索引
					substring = substring.substring(markIndex + 1).trim();					// 从A''中截取页码
					
					String pageNum = replacePageNumStr(substring);
					pageNum = removeSingle(pageNum);
					
					Ref ref = new Ref();
					ref = getDescRef(refList, paraList);
					if (null == ref) {
						System.out.println("引证未匹配到文献：" + citation.getId() + "\t" + citation.getTaxon().getScientificname() + "\t" + citation.getTaxon().getChname() + "\t" + citation.getSciname());
					} else {
						/*if (isNumeric(substring)) {
							list.add(getRefStrWithPageNum(ref, substring));
						} else if (isPage(substring)) {
							String[] nums = getNum(substring);
							list.add(getRefStrWithPageNums(ref, nums[0], nums[1]));
						} else {
							// System.out.println("不是数字：" + substring);
						}*/
					}
				} else {
					// System.err.println("年份异常：" + citationStr);
				}
			} catch (Exception e) {
				// System.out.println("异常数据：" + citationStr);
			}	
		}
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (String refjson : list) {
			sb.append(refjson).append(",");
		}
		return sb.toString();
	}
	/* 补充空字符*/
	private String addSpace(String citationstr) {
		citationstr = citationstr.replace(":", ": ").replace(".", ". ")
			.replace(",", ", ").replace("–", "– ");
		return citationstr;
	}
	/* 替换页码中的字符串 */
	private String replacePageNumStr(String pageNum) {
		if (pageNum.contains("[")) {
			pageNum = pageNum.substring(0, pageNum.indexOf("[")).trim();
		} else if (pageNum.contains(". unavailable name;")) {
			pageNum = pageNum.substring(0, pageNum.indexOf(". unavailable name;")).trim();
		} else if (pageNum.contains(" ")) {
			pageNum = pageNum.substring(0, pageNum.indexOf(" ")).trim();
		} else if (pageNum.contains(".")) {
			pageNum = pageNum.substring(0, pageNum.indexOf(".")).trim();
		} else if (pageNum.contains(",")) {
			pageNum = pageNum.substring(0, pageNum.indexOf(",")).trim();
		} else if (pageNum.contains(";")) {
			pageNum = pageNum.substring(0, pageNum.indexOf(";")).trim();
		} else if (pageNum.contains(")")) {
			pageNum = pageNum.substring(0, pageNum.indexOf(")")).trim();
		} else {
			pageNum = pageNum.replace(", syn. nov", "")
			.replace("; nomen nudum", "")
			.replace(", nomen nudum", "")
			.replace(". Nomen nudum", "")
			.replace(", syn nov", "")
			.replace(". Misidentification", "")
			.replace("; misidentification; not Chrysosarcophaga Townsend, 1932", "")
			.replace("Name suppressed by ICZN, 1963", "")
			.replace("Naturgesch Thierr", "")
			.replace("Unavailable name", "")
			.replace("Ann Soc Ent Fr", "")
			.replace(", Figures 10-12", "")
			.replace("(as subgenus", "")
			.replace("Species Insect", "")
			.replace("(Syn as P invicta by Okada, 1988 Kontyû 56", "")
			.replace("Sieboldia Suppl", "")
			.replace("Sicboldia Suppl", "")
			.replace("缺少", "")
			.replace("(In part)", "")
			.replace("pl", "")
			.replace("Mem Soc Sci Agric Lille 1842", "")
			.replace("(Preoccupied by Linnaeus, 1758 [= Temnostoma vespiforme", "")
			.replace(", not Mesnil, 1963  of Chinese specimens", "")
			.replace("reacement name for Aricia Robineau-Desvoidy, 1830", "")
			.replace("Reacement name for Strongylophthalmus Hendel, 1902 nec Mannerheim, 1853", "")
			.replace("Syn of similis Lamb by Brake et Bächli, 2008 World CatIns 9", "")
			.replace("Syst Study Dros Jap", "").trim();
		}
		pageNum = removeSingle(pageNum);
		return pageNum;
	}
	/* 获取匹配发参考文献*/
	int num = 0;
	private Ref getDescRef(List<Ref> refList, List<String> paraList) {
		Ref ref = new Ref();
		Ref thisRef = new Ref();
		float rsl = 0.0f;
		for (int i = 0; i < refList.size(); i++) {
			thisRef = refList.get(i);
			Integer count = 0;
			for (String para : paraList) {
				if (thisRef.getRefstr().contains(para)) {
					count++;
				}
			}
			//System.out.println(count + "\t" + thisRef.getRefstr());
			/*if (count > rsl) {
				rsl = count;
				ref = thisRef;
			}*/
			// 
			//System.out.println("结果：" + (float)count / (float)paraList.size());
			if ((float)count / (float)paraList.size() > rsl) {
				rsl = (float)count / (float)paraList.size();
				if (rsl >= 0.5) {
					ref = thisRef;
					num++;
				} else {
					System.out.println("<0.5：" + num);
				}
			}
		}
		//System.out.println("匹配结果：" + ref.getRefstr());
		return ref;
	}
	/* 拆分引证，返回字符串集合*/
	private List<String> getParaList(String citationstr) {
		List<String> list = new ArrayList();
		String[] paraArr = citationstr.split(" ");
		for (int i = 0; i < paraArr.length; i++) {
			paraArr[i] = paraArr[i].replace(",", "").replace(".", "").
					replace(":", "").replace("-", "").replace("?", "").
					replace("[", "").replace("]", "")
					.trim();
			if (StringUtils.isNotBlank(paraArr[i])) {
				list.add(paraArr[i]);
			}
		}
		return list;
	}
	/* 引证中若包含指定字符串，截取指定字符串及其之后的所有字符*/
	private String handleCitationStr(String citationStr) {
		String[] strArr = {"Type locality", "Type localities", "Type-locality",
				"Type ocality", "Typelocality", "Type localites", "type loc",
				"Typelocalities", "Type localitry", "Type- locality",
				"Type-Ioc", "Type locaity", "Type localtiy",
				"Typel ocality", "Type locatity", "Type locatities",
				"Type-species", "Type species", "Typespecies",
				"Type spcies", "Type specise", "Type speices", "Types pecies",
				"Type  species", "Type spceies", "Type speices", "Type speceis", 
				"Invalid in lack of designation", "(new name for Platypeza",
				"T ype locality"};
		for (String special : strArr) {
			if (citationStr.contains(special)) {
				citationStr = citationStr.substring(0, citationStr.indexOf(special));
			}
		}
		citationStr = citationStr.replace(". emendation of Mixtemyia )", "");
		// 移除括号内容
		citationStr = removeMark(citationStr);
		citationStr = removeSingle(citationStr);
		return citationStr.trim();
	}
	/* 从字符串中获取起始页码*/
	private String[] getNum(String pageNum) {
		pageNum = pageNum.trim();
		String[] rsl = new String[2];
		pageNum = pageNum.replace("-", ",");
		pageNum = pageNum.replace("–", ",");
		pageNum = pageNum.replace(" ", ",");
		String[] split = pageNum.split(",");
		rsl[0] = split[0].trim();
		rsl[1] = split[split.length - 1].trim();
		return rsl;
	}
	/* 判断字符串是否是页码*/
	public boolean isPage(String str) {
		// Pattern pattern = Pattern.compile("[0-9, -]*");
		Pattern pattern = Pattern.compile("[0-9, -–]*");
		return pattern.matcher(str).matches();
	}
	/* 统计指定字符，判断是否是多引证*/
	private int countMark(String refstr, int countMark) {
		int indexOf = 0;
		if (refstr.contains(";")) {
			indexOf = refstr.indexOf(";");
			refstr = refstr.substring(indexOf + 1);
			countMark++;
		} else {
			return countMark;
		}
		return countMark(refstr, countMark);
	}
	
	public String removeYear(String refstr) {
		for (int i = 0; i < refstr.length() - 4; i++) {
			String tmp = refstr.substring(i, i + 4);
			if (isNumeric(tmp)) {
				refstr = refstr.replace(tmp, "");
			}
		}
		return refstr;
	}
	/* 移除目标字符串中所有的英文字符*/
	private String removePageNumStr(String pageNum) {
		if(pageNum.matches(".*[a-zA-Zäé].*")){
		    Pattern p = Pattern.compile("[a-zA-zäé]");
		    Matcher matcher = p.matcher(pageNum);
		    pageNum = matcher.replaceAll("").replace(" ", "");	// 字母替换成
		} else {
			return pageNum;
		}
		return removePageNumStr(pageNum);
	}
	/* 移除引证中所有的];,. 字符 */
	private String removeSingle(String citationStr) {
		if (citationStr.endsWith("]") || citationStr.endsWith(",") || 
				citationStr.endsWith(";") || citationStr.endsWith(".") || 
				citationStr.endsWith(" ") || citationStr.endsWith("?") || 
				citationStr.endsWith("])")) {
			citationStr = citationStr.substring(0, citationStr.length() - 1);
		} else {
			return citationStr;
		}
		return removeSingle(citationStr);
	}
	/* 移除引证中所有被括号内及括号本身的字符串  */
	private String removeMark(String citationStr) {
		int beginIndex = 0;
		int endIndex = 0;
		if (citationStr.contains("(") && citationStr.contains(")")) {
			beginIndex = citationStr.indexOf("(");
			endIndex = citationStr.indexOf(")");
			citationStr = citationStr.substring(0, beginIndex) + citationStr.substring(endIndex + 1);
		} else {
			return citationStr;
		}
		return removeMark(citationStr);
	}
	/* 拼文献*/
	private String getRefJsonWithPageNums(Ref ref, String startS, String endE) {
		String context = "[{"
				+ "\"refId\"" + ":\"" + ref.getId() + "\","
				+ "\"refS\"" + ":\"" + startS + "\"," 
				+ "\"refE\"" + ":\"" + endE + "\","
				+ "\"refType\"" + ":\"" + 1 + "\""
				+ "}]";
		return context;
	}
	/* 拼文献*/
	private String getRefJsonWithPageNum(Ref ref, String pageNum) {
		String context = "[{"
				+ "\"refId\"" + ":\"" + ref.getId() + "\","
				+ "\"refS\"" + ":\"" + pageNum + "\"," 
				+ "\"refE\"" + ":\"" + pageNum + "\","
				+ "\"refType\"" + ":\"" + 1 + "\""
				+ "}]";
		return context;
	}
	/* 拼文献*/
	private String getRefJsonNoPage(Ref ref) {
		String context = "[{"
				+ "\"refId\"" + ":\"" + ref.getId() + "\","
				+ "\"refS\"" + ":\"" + "" + "\"," 
				+ "\"refE\"" + ":\"" + "" + "\","
				+ "\"refType\"" + ":\"" + 1 + "\""
				+ "}]";
		return context;
	}
	/* 拼文献*/
	private String getRefStrWithPageNum(Ref ref, String pageNum) {
		String context = "{"
			+ "\"refId\"" + ":\"" + ref.getId() + "\","
			+ "\"refS\"" + ":\"" + pageNum + "\"," 
			+ "\"refE\"" + ":\"" + pageNum + "\","
			+ "\"refType\"" + ":\"" + 1 + "\""
			+ "}";
		return context;
	}
	/* 拼文献*/
	private String getRefStrWithPageNums(Ref ref, String pageNumS, String pageNunE) {
		String context = "{"
				+ "\"refId\"" + ":\"" + ref.getId() + "\","
				+ "\"refS\"" + ":\"" + pageNumS + "\"," 
				+ "\"refE\"" + ":\"" + pageNunE + "\","
				+ "\"refType\"" + ":\"" + 1 + "\""
				+ "}";
		return context;
	}

	@Override
	public JSON unParseExcelCitationOfRef(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum();

		XSSFRow row = null;
		for (int rowNum = 0; rowNum < rowNums; rowNum++) {
			row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				String cid = excelService.getStringValueFromCell(row.getCell(0)).trim();
				Citation citation = this.citationRepository.findOneById(cid);
				System.out.println(citation.getTaxon().getScientificname() + "\t" + citation.getTaxon().getChname() + "\t" + 
						citation.getSciname());
			}
		}
		return null;
	}

	@Override
	public JSON reUnParseExcelCitationOfRef() throws Exception {
		String sourcesid = "ae4d6943-a621-4bb4-80a2-0cf186ad950b";
		List<Citation> clist = this.citationRepository.findListBySourcesid(sourcesid);
		for (Citation citation : clist) {
			String citationstr = citation.getCitationstr();
			String refjson = citation.getRefjson();
			int countMark = 0;
			// 统计指定字符，判断是否是多条引证
			countMark = countMark(citationstr, countMark);
			if (countMark != 0) {
				String[] split = citationstr.split(";");
				JSONArray jsonArr = JSON.parseArray(refjson);
				if (jsonArr.size() != split.length) {
					System.out.println("待审核：" + citation.getId() + "\t" + citation.getCitationstr());
				}
			}
		}
		return null;
	}

	@Override
	public JSON parseProtect(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum();
		
		for (int rowNum = 0; rowNum <= rowNums; rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			}else {
				String json = excelService.getStringValueFromCell(row.getCell(0));
				JSONObject rootObject = JSONObject.parseObject(json); 	// JSON
	            JSONObject paramzObject = rootObject.getJSONObject("JSON");
	            JSONArray feedsArray = paramzObject.getJSONArray("data");
	            for (int i = 0; i < feedsArray.size(); i++) {
	            	Protectplus protectplus = new Protectplus();
	            	Protect protect = new Protect();
	            	try {
						JSONObject dataObj = feedsArray.getJSONObject(i);
						String id = UUIDUtils.getUUID32();
						String kingdom = dataObj.getString("kingdom");
						String phylum = dataObj.getString("phylum");
						String family = dataObj.getString("family");
						String familyC = dataObj.getString("family_c");
						String genus = dataObj.getString("genus");
						String genusC = dataObj.getString("genus_c");
						String cnname = dataObj.getString("common_name");
						String fullName = dataObj.getString("full_name");
						String specimenDesp = dataObj.getString("spe_desp");
						String iucn = dataObj.getString("IUCN");
						String scientificName = dataObj.getString("sname");
						String protectLevel = dataObj.getString("protect_level");
						String rank = dataObj.getString("level");
						String state = dataObj.getString("state");
						String speImgUrl = dataObj.getString("spe_img_url");
						String speImgUrlThumb = dataObj.getString("spe_img_url_thumb");
						String caiImgUrl = dataObj.getString("cai_img_url1");
						
						/** 第一批
						protectplus = new Protectplus(id,  "国家重点保护野生植物（第一批）物种库", kingdom, null, 
								null, phylum, null, null, null, null, family, familyC, genus, genusC, 
								scientificName, cnname, fullName, protectLevel, iucn, "植物", rank, 
								"1998年8月4日", "《国家重点保护野生植物名录（第一批）》于1999年8月4日由国务院批准并由国家林业局和农业部发布，1999年9月9日起施行", 
								specimenDesp, state, speImgUrl, speImgUrlThumb, caiImgUrl);
						protect = new Protect(id, kingdom, null, null, phylum, null, null, null, null, 
								family, familyC, genus, genusC, scientificName, cnname, fullName, 
								protectLevel, iucn, "植物", rank, "1998年8月4日", 
								"《国家重点保护野生植物名录（第一批）》于1999年8月4日由国务院批准并由国家林业局和农业部发布，1999年9月9日起施行",
								"国家重点保护野生植物（第一批）物种库");*/
						// 第二批
						protectplus = new Protectplus(id,  "国家重点保护野生植物（第二批）物种库", kingdom, null, 
								null, phylum, null, null, null, null, family, familyC, genus, genusC, 
								scientificName, cnname, fullName, protectLevel, iucn, "植物", rank, 
								"2019年10月31日", "《国家重点保护野生植物名录（第二批）》一直处于“讨论稿”状态，至今未正式颁布。数据均是截止至2019年10月31日的统计数据。", 
								specimenDesp, state, speImgUrl, speImgUrlThumb, caiImgUrl);
						protect = new Protect(id, kingdom, null, null, phylum, null, null, null, null, 
								family, familyC, genus, genusC, scientificName, cnname, fullName, 
								protectLevel, iucn, "植物", rank, "2019年10月31日", 
								"《国家重点保护野生植物名录（第二批）》一直处于“讨论稿”状态，至今未正式颁布。数据均是截止至2019年10月31日的统计数据。",
								"国家重点保护野生植物（第二批）物种库");
						
					} catch (Exception e) {
						System.out.println("异常:" + i);
					}
	            	this.protectRepository.save(protect);
	            	this.protectplusRepository.save(protectplus);
				}
			}
		}
		long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        System.err.println("国家野生植物保护 - 批量存储完成时间：" + (end - start));
		return null;
	}

	@Override
	public JSON addDistributionPid(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		int sheets = workbook.getNumberOfSheets();
		System.out.println("SHEET总数：" + sheets);
		for (int i = 0; i < sheets; i++) {
			// 获取sheet工作表
			XSSFSheet sheet = workbook.getSheetAt(i);
			// 获取总记录数
			int rowNums = sheet.getLastRowNum();
			System.err.println("年度行政区划：" + sheet.getSheetName() + "\t" + rowNums);
			String pid1 = new String();
			String pid2 = new String();
			
			List<Address> list = new ArrayList<>();
			
			for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
				XSSFRow row = sheet.getRow(rowNum);
				if (null == row) {
					continue;
				} else {
					String adcode = excelService.getStringValueFromCell(row.getCell(0));
					String address = excelService.getStringValueFromCell(row.getCell(1));
					Address thisAddress = new Address();
					String sheetName = sheet.getSheetName();
					try {
						if (StringUtils.isNotBlank(adcode)) {
							if (Integer.parseInt(sheetName) >= 1997) {	// 1997年
								if (address.contains("北京市") || address.contains("天津市") || address.contains("上海市") || address.contains("重庆市")) {
									pid2 = adcode;
									thisAddress = new Address(UUIDUtils.getUUID32(), adcode, address, "CHN", sheetName);
									list.add(thisAddress);
									System.out.println(adcode + "\t" + "CHN" + "\t" + address);
								} else if (adcode.endsWith("0000")) {
									pid1 = adcode;
									pid2 = null;
									thisAddress = new Address(UUIDUtils.getUUID32(), adcode, address, "CHN", sheetName);
									list.add(thisAddress);
									System.out.println(adcode + "\t" + "CHN" + "\t" + address);
								} else if (adcode.endsWith("00")) {
									pid2 = adcode;
									thisAddress = new Address(UUIDUtils.getUUID32(), adcode, address, pid1, sheetName);
									list.add(thisAddress);
									System.out.println(adcode + "\t" + pid1 + "\t" + address);
								} else {
									thisAddress = new Address(UUIDUtils.getUUID32(), adcode, address, pid2, sheetName);
									list.add(thisAddress);
									System.out.println(adcode + "\t" + pid2 + "\t" + address);
								}
							} else {	// 1997年以前
								if (address.contains("北京市") || address.contains("天津市") || address.contains("上海市")) {
									pid2 = adcode;
									thisAddress = new Address(UUIDUtils.getUUID32(), adcode, address, "CHN", sheetName);
									list.add(thisAddress);
									System.out.println(adcode + "\t" + "CHN" + "\t" + address);
								} else if (adcode.endsWith("0000")) {
									pid1 = adcode;
									pid2 = null;
									thisAddress = new Address(UUIDUtils.getUUID32(), adcode, address, "CHN", sheetName);
									list.add(thisAddress);
									System.out.println(adcode + "\t" + "CHN" + "\t" + address);
								} else if (adcode.endsWith("00")) {
									pid2 = adcode;
									thisAddress = new Address(UUIDUtils.getUUID32(), adcode, address, pid1, sheetName);
									list.add(thisAddress);
									System.out.println(adcode + "\t" + pid1 + "\t" + address);
								} else {
									thisAddress = new Address(UUIDUtils.getUUID32(), adcode, address, pid2, sheetName);
									list.add(thisAddress);
									System.out.println(adcode + "\t" + pid2 + "\t" + address);
								}
							}
						}
					} catch (Exception e) {
						//System.err.println("年度行政区划：" + sheet.getSheetName() + "\t" + address);
						e.printStackTrace();
					}
				}
			}
			for (Address address : list) {
				this.addressRepository.save(address);
			}
			
		}
		return null;
	}

	@Override
	public JSON compareTo2020() throws Exception {
		for (int i = 1980; i < 2020; i++) {
			List<Address> list = this.addressRepository.findListByYear(i);	// 查出指定年份的所有行政区
			// 遍历指定年份的行政区划，以Ppid, address为条件逐一在2020年的行政区划中查询
			for (Address thisAddress : list) {
				String id = thisAddress.getId();
				String adcode = thisAddress.getAdcode();
				String address = thisAddress.getAddress();
				String year = thisAddress.getYear();
				String pid = thisAddress.getPid();
				String ppid = thisAddress.getPpid();
				//Address thisAdd1 = this.addressRepository.findOneByAdcodeAndAddressAndPidAndYear(adcode, address, pid, 2020);
				// 在同一省、直辖市、特区下查询指定的行政区
				List<Address> addList = this.addressRepository.findOneByAddressAndPpidAndYear(address, ppid, 2020);
				if (addList.isEmpty()) {
					System.out.println(id + "\t" + adcode + "\t" + pid + "\t" + ppid + "\t" + address + "\t" + year + "\t" + "与2020年相比，无匹配");
				} else if (addList.size() > 1) {
					System.out.println(id + "\t" + adcode + "\t" + pid + "\t" + ppid + "\t" + address + "\t" + year + "\t" + "与2020年相比，匹配多个结果");
				}
			}
		}
		
		return null;
	}

	@Override
	public JSON addPpid() throws Exception {
		List<Address> addList = this.addressRepository.findListByPpid();
		for (Address address : addList) {
			String pid = address.getPid();
			
			if (!pid.endsWith("0000")) {
				Address thisAdd = this.addressRepository.findOneByAdcode(pid);
				String ppid = thisAdd.getPid();
				if (ppid.endsWith("0000")) {
					address.setPpid(ppid);
					this.addressRepository.save(address);
				} else {
					System.out.println("异常：" + address.getId() + "\t" + pid);
				}
			}
		}
		return null;
	}

	@Override
	public JSON match() throws Exception {
		List<Address2> list = this.address2Repository.findAll();	
		// 遍历指定年份的行政区划，以Ppid, address为条件逐一在2020年的行政区划中查询
		for (Address2 thisAddress : list) {
			String id = thisAddress.getId();
			String adcode = thisAddress.getAdcode();
			String address = thisAddress.getAddress();
			String year = thisAddress.getYear();
			String pid = thisAddress.getPid();
			String ppid = thisAddress.getPpid();
			String area = new String();
			
			// 格式化行政区
			if (address.length() <= 3) {
				area = address.substring(0, address.length() - 1);
			} else if (address.length() == 4) {
				if (address.endsWith("地区")) {
					area = address.replace("地区", "");
				} else { // （河区、山区、窑区、矿区、港区、坝区、江区、屯区、子区、）、县、盟、市、旗、区
					area = address.substring(0, address.length() - 1);
				}
			} else if (address.length() >= 5) {
				if (address.endsWith("地区")) {
					area = address.replace("地区", "");
				} else if (address.endsWith("工农区") || address.endsWith("工矿区") || address.endsWith("行政区") || address.endsWith("自治州") || address.endsWith("自治县")){ 
					area = address.substring(0, address.length() - 3);
				} else {
					area = address.substring(0, address.length() - 1);
				}
			}			
			
			// 在同一省、直辖市、特区下查询指定的行政区
			// 以50、51开头的省市县在重庆市、四川省两个省份中查找
			// 先在2020中查询指定省份下的市县区（区分重庆、四川与其他省份）
			List<Address> addList = new ArrayList<>();
			if (adcode.startsWith("50") || adcode.startsWith("51")) {
				addList = this.addressRepository.findListByAddressAndPpidsAndYear(area, 2020);
			} else {
				addList = this.addressRepository.findListByAddressAndPpidAndYear(area, ppid, 2020);
			}
			
			if (addList.isEmpty() || addList.size() <= 0) {
				System.out.println("未匹配" + "\t" + id + "\t" + adcode + "\t" + pid + "\t" + ppid + "\t" + address + "\t" + year);
			} else if (addList.size() == 1) {
				Address address2 = addList.get(0);
				System.out.println( "已匹配" + "\t" + id + "\t" + adcode + "\t" + pid + "\t" + ppid + "\t" + address + "\t" + year + "\t" +
					address2.getAdcode() + "\t" + address2.getAddress() + "\t" + address2.getPid() + "\t" + address2.getPpid());
			} else if (addList.size() > 1) {
				if (adcode.endsWith("00")) {
					Address thisAdd = this.addressRepository.findByAdcodeAndAddressAndPidAndYear(adcode, area, ppid, 2020);
					if (null != thisAdd) {
						System.out.println( "已匹配*" + "\t" + id + "\t" + adcode + "\t" + pid + "\t" + ppid + "\t" + address + "\t" + year + "\t" +
							thisAdd.getAdcode() + "\t" + thisAdd.getAddress() + "\t" + thisAdd.getPid() + "\t" + thisAdd.getPpid());
					} else {
						System.out.println("多个匹配" + "\t" + id + "\t" + adcode + "\t" + pid + "\t" + ppid + "\t" + address + "\t" + year);
					}
				} else {
					System.out.println("多个匹配" + "\t" + id + "\t" + adcode + "\t" + pid + "\t" + ppid + "\t" + address + "\t" + year);
				}
				
				/*if (adcode.startsWith("50") || adcode.startsWith("51")) {
					
				} else {
					
				}
				
				
				if (pid.endsWith("0000")) {
					Address thisAdd = this.addressRepository.findListByAddressAndPidAndPpidAndYear(area, "0000", ppid, 2020);
					if (null == thisAdd) {
						System.out.println(id + "\t" + adcode + "\t" + pid + "\t" + ppid + "\t" + address + "\t" + year + "\t" + "未匹配");
					} else {
						System.out.println(id + "\t" + adcode + "\t" + pid + "\t" + ppid + "\t" + address + "\t" + year + "\t" + "已匹配" + "\t" +
								thisAdd.getAdcode() + "\t" + thisAdd.getAddress() + "\t" + thisAdd.getPid() + "\t" + thisAdd.getPpid());
					}
				} else if (pid.endsWith("00")) {
					List<Address> thisAddList = this.addressRepository.findListByAddressAndPidAndPpidAndYear2(area, "0000", "00", ppid, 2020);
					if (thisAddList.isEmpty() || thisAddList.size() <= 0) {
						System.out.println(id + "\t" + adcode + "\t" + pid + "\t" + ppid + "\t" + address + "\t" + year + "\t" + "未匹配");
					} else if (thisAddList.size() == 1) {
						System.out.println(id + "\t" + adcode + "\t" + pid + "\t" + ppid + "\t" + address + "\t" + year + "\t" + "已匹配" + "\t" +
						thisAddList.get(0).getAdcode() + "\t" + thisAddList.get(0).getAddress() + "\t" + thisAddList.get(0).getPid() + "\t" + thisAddList.get(0).getPpid());
					} else if (thisAddList.size() > 1){
						System.out.println(id + "\t" + adcode + "\t" + pid + "\t" + ppid + "\t" + address + "\t" + year + "\t" + "多个匹配结果");
					} else {
						System.out.println(id + "\t" + adcode + "\t" + pid + "\t" + ppid + "\t" + address + "\t" + year + "\t" + "异常");
					}
				} else {
					System.out.println(id + "\t" + adcode + "\t" + pid + "\t" + ppid + "\t" + address + "\t" + year + "\t" + "多个匹配结果");
				}*/
			} else {
				System.out.println("异常" + "\t" + id + "\t" + adcode + "\t" + pid + "\t" + ppid + "\t" + address + "\t" + year);
			}
		}
		
		return null;
	}

	@Override
	public JSON distinct(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		int sheets = workbook.getNumberOfSheets();
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(1);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum();
		System.out.println("sheetName：" + sheet.getSheetName());
		/**
		 * 代码功能：将各年度与2020年的行政区划比较结果去重（之后匹配）
		 * 比较结果有两种情况：未匹配 && 匹配多个（同一省份下的同名县级行政区分别所属于该省份的不同市级）
		 * 根据行政区、省份去重
		 */
		for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				String id = excelService.getStringValueFromCell(row.getCell(0));
				String adcode = excelService.getStringValueFromCell(row.getCell(1));
				String pid = excelService.getStringValueFromCell(row.getCell(2));
				String ppid = excelService.getStringValueFromCell(row.getCell(3));
				String address = excelService.getStringValueFromCell(row.getCell(4));
				String year = excelService.getStringValueFromCell(row.getCell(5));
				String note = excelService.getStringValueFromCell(row.getCell(6));

				List<Address2> list = this.address2Repository.findListByAddressAndPpid(address, ppid);
				if (list.isEmpty()) {
					Address2 address2 = new Address2(id, adcode, address, pid, year, ppid, note);
					this.address2Repository.save(address2);
				} else {
					System.out.println("异常：" + id + "\t" + adcode + "\t" + address + "\t" + pid + "\t" + ppid + "\t" + year + "\t" + note);
				}
			}
		}
		return null;
	}

	@Override
	public JSON getDistributData() {
		List<Description> list = this.descriptionRepository.findListByTypeAndTaxasetId("209", "：", "3af7d9839f874920a1dd9262462e719f");
		for (Description description : list) {
			String names = description.getTaxon().getScientificname() + "\t" + description.getTaxon().getChname();
			String descontent = description.getDescontent().replace(" ", ""); 	// 多个省份的分布数据
			String[] split = descontent.split("\n");	
			for (String content : split) {
				content = checkPunctuation(content);			
				content = content.replace("；", "、").replace("，", "").trim();
				content = checkSymbol(content);					// 每一个省份的分布数据
				
				String[] split2 = content.split("：");
				String province = new String();
				if (split2.length == 2) {
					province = split2[0];
					province = province.replace("内蒙古", "内蒙").replace("翱南", "湖南").replace("五南", "云南");
					String provinceFullName = this.geoobjectRepository.getProvinceByShortName(province);
					if (StringUtils.isNotBlank(provinceFullName)) {
						province = provinceFullName;
					}
					String areas = split2[1];
					if (areas.contains("（") && areas.contains("）")) {
						areas = replaceSymbol(areas);
					}
					
					String[] split3 = areas.split("、");		
					for (String area : split3) {
						area = area.replace("（）", "");
						System.out.println(names + "\t" + province + "\t" + area);
					}
				} else {
					System.out.println("异常：" + split2.length + "\t" + names + "\n" + content);
					for (String str : split2) {
						System.out.println(str);
					}
				}
			}
		}
			
		return null;
	}
	// 替换影响分割的、
	private String replaceSymbol(String str) {
		int start = str.indexOf("（");
		int end = str.indexOf("）");
		String subStr1 = str.substring(start, (end + 1));
		if (subStr1.contains("、")) {
			str = str.replace(subStr1, subStr1.replace("、", "|"));
			return replaceSymbol(str);
		} else {
			return str;
		}
	}
	
	// 递归替换所有的、、
	private String checkSymbol(String str) {
		if (!str.contains("、、")) {
			return str;
		} else {
			str = str.replaceAll("、、", "、");
			return checkSymbol(str);
		}
	}
	/**
	 * 判断一个字符串是否包含标点符号（中文或者英文标点符号），true 包含。<br/>
	 * 原理：对原字符串做一次清洗，清洗掉所有标点符号。<br/>
	 * 此时，如果入参 ret 包含标点符号，那么清洗前后字符串长度不同，返回true；否则，长度相等，返回false。<br/>
	 *
	 * @param ret
	 * @return true 包含中英文标点符号
	 */
	private String checkPunctuation(String ret) {
		String tmp = ret;
		tmp = tmp.replaceAll("[0-9]", "");			// 替换掉所有数字
		tmp = tmp.replaceAll("[A-Za-z]", "");		// 替换掉所有字母
		return tmp;
	}
	
	@Override
	public JSON addProvinceAdcode(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		int sheets = workbook.getNumberOfSheets();
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum();
		for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				String sciname = excelService.getStringValueFromCell(row.getCell(0));
				String cnname = excelService.getStringValueFromCell(row.getCell(1));
				String province = excelService.getStringValueFromCell(row.getCell(2));
				String areas = excelService.getStringValueFromCell(row.getCell(3));
				String remark = areas;
				
				String adcode = this.addressRepository.findAdcodeByProvinceAndYear(province, 2020);
				areas = areas.replace("()", "").replace("*", "").replace("（）", "");
				if (areas .contains("（")) {
					int start = areas.indexOf("（");
					areas = areas.substring(0, start);
				}
				if (areas .contains("(")) {
					int start = areas.indexOf("(");
					areas = areas.substring(0, start);
				}
				System.out.println(sciname + "\t" + cnname + "\t" + province + "\t" + adcode + "\t" + areas + "\t" + remark);
			}
		}
		return null;
	}

	@Override
	public JSON getLevel(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		int sheets = workbook.getNumberOfSheets();
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(1);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum();
		for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				String sciname = excelService.getStringValueFromCell(row.getCell(0));
				String cnname = excelService.getStringValueFromCell(row.getCell(1));
				String province = excelService.getStringValueFromCell(row.getCell(2));
				String ppid = excelService.getStringValueFromCell(row.getCell(3));
				String area = excelService.getStringValueFromCell(row.getCell(4));
				String remark = excelService.getStringValueFromCell(row.getCell(5));
				List<Address> list = new ArrayList<>();	
				if (ppid.startsWith("50") || ppid.startsWith("51")) {
					list = this.addressRepository.findListByAddressAndPpidsAndYear(area, 2020);
				} else {
					list = this.addressRepository.findListByAddressAndPpidAndYear(area, ppid, 2020);
				}
				if (list.isEmpty() || list.size() <= 0) {
					System.out.println(
						"未匹配：" + "\t" + sciname + "\t" + cnname + "\t" + province + "\t" + 
						ppid + "\t" + area + "\t" + remark);
				} else if (list.size() == 1) {
					Address address = list.get(0);
					String add = address.getAddress();
					String code = address.getAdcode();
					String pid = address.getPid();
					String ppids = address.getPpid();
					
					String finalProvince = "";
					finalProvince = ppid.equals(ppids) ? "" : ppids;
					
					if (code.endsWith("00")) {	// 市级
						System.out.println(
							"已匹配市级：" + "\t" + sciname + "\t" + cnname + "\t" + province + "\t" + 
								ppid + "\t" + area + "\t" + remark + "\t" + add + "\t" + "" + "\t" + finalProvince);
					} else {					// 县级(此处查数据库市为了补充县级的市)
						Address address2 = this.addressRepository.findOneByAdcode(pid); // 查县的市
						if (null != address2) {
							System.out.println(
								"已匹配县级：" + "\t" + sciname + "\t" + cnname + "\t" + province + "\t" + 
									ppid + "\t" + area + "\t" + remark + "\t" + "" + "\t" + add + "\t" + finalProvince);
						} else {
							System.out.println(
								"已匹配县级：" + "\t" + sciname + "\t" + cnname + "\t" + province + "\t" + 
									ppid + "\t" + area + "\t" + remark + "\t" + address2.getAddress() + "\t" + add + "\t" + finalProvince);
						}
					}
				} else {
					Address address = new Address();
					String finalProvince = "";
					if (ppid.startsWith("50") || ppid.startsWith("51")) {
						address = this.addressRepository.findByAdcodeAndAddressAndPpidsAndYear("00", "0000", area, 2020);
					} else {
						address = this.addressRepository.findByAdcodeAndAddressAndPpidAndYear("00", "0000", area, 2020, ppid);
					}
					try {
						String ppid2 = address.getPpid();
						finalProvince = ppid.equals(ppid2) ? "" : ppid2;
						System.out.println(
							"已匹配市级*：" + "\t" + sciname + "\t" + cnname + "\t" + province + "\t" + 
								ppid + "\t" + area + "\t" + remark + "\t" + address.getAddress() + "\t" + "" + "\t" + finalProvince);
					} catch (Exception e) {
						System.out.println(
							"匹配多个：" + list.size() + "\t" + sciname + "\t" + cnname + "\t" + province + "\t" + 
								ppid + "\t" + area + "\t" + remark);
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public JSON getLevelPlus(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		int sheets = workbook.getNumberOfSheets();
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(2);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum();
		for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				String rsl = excelService.getStringValueFromCell(row.getCell(0));
				String sciname = excelService.getStringValueFromCell(row.getCell(1));
				String cnname = excelService.getStringValueFromCell(row.getCell(2));
				String province = excelService.getStringValueFromCell(row.getCell(3));
				String ppid = excelService.getStringValueFromCell(row.getCell(4));
				String area = excelService.getStringValueFromCell(row.getCell(5));
				String remark = excelService.getStringValueFromCell(row.getCell(6));
				String city = excelService.getStringValueFromCell(row.getCell(7));
				String county = excelService.getStringValueFromCell(row.getCell(8));
				String finalProvince = excelService.getStringValueFromCell(row.getCell(9));
				List<Address> list = new ArrayList<>();
				
				if (rsl.contains("未匹配")) {
					if (ppid.startsWith("50") || ppid.startsWith("51")) {
						list = this.addressRepository.findListByAddressAndPpidsAndNoYear(area, 2020);
					} else {
						list = this.addressRepository.findListByAddressAndPpidAndNoYear(area, ppid, 2020);
					}
					if (list.isEmpty() || list.size() <= 0) {
						System.out.println(rsl + "\t" + sciname + "\t" + cnname + "\t" + province + "\t" + 
							area + "\t" + ppid + "\t" + remark + "\t" + city + "\t" + county + "\t" + finalProvince);
					} else {
						Address address = new Address();
						String value = "";
						if (ppid.startsWith("50") || ppid.startsWith("51")) {
							address = this.addressRepository.findOneByAreaAndPpidsAndNoYear(area, 2020);
						} else {
							address = this.addressRepository.findOneByAreaAndPpidAndNoYear(area, 2020, ppid);
						}
						
						try {
							String adcode = address.getAdcode();
							String pid = address.getPid();
							String ppid2 = address.getPpid();
							value = ppid.equals(ppid2) ? "" : ppid2;
							if (adcode.endsWith("00")) {
								System.out.println(
									"已匹配市级**：" + "\t" + sciname + "\t" + cnname + "\t" + province + "\t" + 
									area + "\t" + ppid + "\t" + remark + "\t" + address.getAddress() + "\t" + 
									"" + "\t" + value);
							} else {
								System.out.println(
									"已匹配县级**：" + "\t" + sciname + "\t" + cnname + "\t" + province + "\t" + 
										area + "\t" + ppid + "\t" + remark + "\t" + "" + "\t" + address.getAddress() +
										"\t" + value);
								/*Address address2 = this.addressRepository.findOneByAdcode(pid); // 查县的市
								if (null != address2) {
									System.out.println(
										"已匹配县级**：" + "\t" + sciname + "\t" + cnname + "\t" + province + "\t" + 
										area + "\t" + ppid + "\t" + remark + "\t" + "" + "\t" + address.getAddress() +
										"\t" + value);
								} else {
									System.out.println(
										"已匹配县级**：" + "\t" + sciname + "\t" + cnname + "\t" + province + "\t" + 
										area + "\t" + ppid + "\t" + remark + "\t" + address2.getAddress() + "\t" + 
										address.getAddress() + "\t" + value);
								}*/
							}
						} catch (Exception e) {
							System.out.println(rsl + "\t" + sciname + "\t" + cnname + "\t" + province + "\t" + 
								area + "\t" + ppid + "\t" + remark + "\t" + city + "\t" + county + "\t" + finalProvince);
						}
					}	
				} else {
					System.out.println(rsl + "\t" + sciname + "\t" + cnname + "\t" + province + "\t" + 
						area + "\t" + ppid + "\t" + remark + "\t" + city + "\t" + county + "\t" + finalProvince);
				}
				
				
			}
		}
		return null;
	}
	
	@Override
	public JSON addCountyOfCity(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		int sheets = workbook.getNumberOfSheets();
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(2);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum();
		for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				String sciname = excelService.getStringValueFromCell(row.getCell(0));
				String cnname = excelService.getStringValueFromCell(row.getCell(1));
				String province = excelService.getStringValueFromCell(row.getCell(2));
				String area = excelService.getStringValueFromCell(row.getCell(3));
				String ppid = excelService.getStringValueFromCell(row.getCell(4));
				String area2 = excelService.getStringValueFromCell(row.getCell(5));
				String city = excelService.getStringValueFromCell(row.getCell(6));
				String county = excelService.getStringValueFromCell(row.getCell(7));
				String remark = excelService.getStringValueFromCell(row.getCell(8));
				List<Address> list = new ArrayList<>();

				if (StringUtils.isNotBlank(county)) {
					if (remark.equals("已匹配县级：")) {
						Address address = this.addressRepository.findOneByPpidAndCountyAndYear(ppid, county, 2020);

					} else if (remark.equals("已匹配县级**：")) {
						Address address = this.addressRepository.findOneByPpidAndCountyAndNoYear(ppid, county, 2020);

					} else {
						System.out.println(sciname + "\t" + cnname + "\t" + province + "\t" + area + "\t" + ppid + "\t"
								+ area2 + "\t" + city + "\t" + county + "\t" + remark);
					}
				} else {
					System.out.println(sciname + "\t" + cnname + "\t" + province + "\t" + area + "\t" + ppid + "\t"
							+ area2 + "\t" + city + "\t" + county + "\t" + remark);
				}
			}
		}
		return null;
	}
	
	@Override
	public JSON matchAddessRef(String path) throws Exception {
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		int sheets = workbook.getNumberOfSheets();
		// 获取sheet工作表
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总记录数
		int rowNums = sheet.getLastRowNum();
		for (int rowNum = 1; rowNum <= rowNums; rowNum++) {
			XSSFRow row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			} else {
				String id = excelService.getStringValueFromCell(row.getCell(0));
				String sciname = excelService.getStringValueFromCell(row.getCell(1));
				String cnname = excelService.getStringValueFromCell(row.getCell(2));
				String province = excelService.getStringValueFromCell(row.getCell(3));
				String city = excelService.getStringValueFromCell(row.getCell(4));
				String county = excelService.getStringValueFromCell(row.getCell(5));
				String orignal = excelService.getStringValueFromCell(row.getCell(6));
				String remark = excelService.getStringValueFromCell(row.getCell(7));
				String refs = excelService.getStringValueFromCell(row.getCell(8));
				List<Address> list = new ArrayList<>();

				if (StringUtils.isNotBlank(refs)) {
					if (refs.contains("、")) {
						String[] arr = refs.split("、");
						StringBuilder builder = new StringBuilder();
						
						for (String refId : arr) {
							ARef aref = this.aRefRepository.findOneById(refId);
							if (null != aref) {
								builder.append(aref.getRefcontent()).append("\n");
							}
						}
						String refcontent = builder.toString();
						if (StringUtils.isBlank(refcontent)) {
							System.out.println("异常2：" + "\t" + id + "\t" + sciname + "\t" + cnname + "\t" + 
								province + "\t" + city + "\t" + county + "\t" + orignal + "\t" + remark + "\t" + refs);
						} else {
							int indexOf = refcontent.lastIndexOf("\n");
							refcontent = refcontent.substring(0, indexOf);
							if (refcontent.split("\n").length != arr.length) {
								System.out.println("异常1：" + refcontent.split("\n").length + "-" + arr.length + "\t" + id + "\t" + sciname + "\t" + cnname + "\t" + 
									province + "\t" + city + "\t" + county + "\t" + orignal + "\t" + remark + "\t" + refs + "\t" + refcontent);
							} else {
								System.out.println("匹配：" + "\t" + id + "\t" + sciname + "\t"  + cnname + "\t" + 
									province + "\t" + city + "\t" + county + "\t" + orignal + "\t" + 
									remark + "\t" + refs + "\t" + refcontent);
							}
						}
						
					} else {
						ARef aref = this.aRefRepository.findOneById(refs);
						if (null != aref) {
							System.out.println("匹配：" + "\t" + id + "\t" + sciname + "\t"  + cnname + "\t" + 
								province + "\t" + city + "\t" + county + "\t" + orignal + "\t" + 
								remark + "\t" + refs + "\t" + aref.getRefcontent());
						} else {
							System.out.println("异常2：" + "\t" + id + "\t" + sciname + "\t" + cnname + "\t" + 
								province + "\t" + city + "\t" + county + "\t" + orignal + "\t" + remark + "\t" + refs);
						}
					}
				} else {
					System.out.println("不匹配：" + "\t" + id + "\t" + sciname + "\t" + cnname + "\t" + 
						province + "\t" + city + "\t" + county + "\t" + orignal + "\t" + remark + "\t" + refs);
				}
			}
		}
		return null;
	}

	@Override
	public JSON mergeDesc() {
		String taxasetId = "9191fde0d6f1491d96866674740f576a";
		/*List<Taxon> taxonList = this.taxonRepository.findTaxonByTaxasetId(taxasetId);
		Descriptiontype descriptiontype = this.descriptionTypeRepository.findOneById("1");
		for (Taxon taxon : taxonList) {
			List<Description> descList = this.descriptionRepository.findDescListByDesctypeAndTaxonId(taxon.getId(), "1");
			StringBuilder descContent = new StringBuilder();
			for (Description description : descList) {
				descContent = descContent.append(description.getDescontent()).append("<br>");
			}
			String content = descContent.toString();
			
			try {
				content = content.substring(0, content.lastIndexOf("<br>"));
			} catch (Exception e) {
				System.out.println(taxon.getId() + "\t" + content);
			}
			
			Description thisDesc = new Description();
			thisDesc.setId(UUIDUtils.getUUID32());
			thisDesc.setDescontent(content);
			thisDesc.setDestitle(taxon.getScientificname()+"的形态描述");
			thisDesc.setSourcesid(taxon.getSourcesid());
			thisDesc.setDescriptiontype(descriptiontype);
			thisDesc.setTaxon(taxon);
			thisDesc.setStatus(1);
			thisDesc.setInputer(taxon.getInputer());
			thisDesc.setInputtime(taxon.getInputtime());
			thisDesc.setSynchdate(taxon.getSynchdate());
			thisDesc.setDesdate("2020-08-31 21:53:15.929");
			thisDesc.setDestypeid(descriptiontype.getId());
			thisDesc.setLicenseid("4");
			thisDesc.setLanguage("1");
			thisDesc.setSynchstatus(0);
			thisDesc.setTesta("new");
			this.descriptionRepository.save(thisDesc);
		}*/
		System.out.println("形态描述合并完成，分布开始......");
		String sourcesId = "4a8d1195-b0cd-4d01-b683-3e24db9c1085";
		List<Description> desList = this.descriptionRepository.findDescListByDesctypeAndSourcesId(sourcesId , "201");
		for (Description description : desList) {
			Distributiondata thisDist = new Distributiondata();
			String descontent = description.getDescontent();
			thisDist.setId(UUIDUtils.getUUID32());
			thisDist.setDiscontent(descontent);
			//thisDist.setDestitle(description.getDestitle());
			thisDist.setSourcesid(description.getSourcesid());
			thisDist.setDistype(description.getDescriptiontype().getId());
			thisDist.setTaxon(description.getTaxon());
			thisDist.setStatus(1);
			thisDist.setInputer(description.getInputer());
			thisDist.setInputtime(description.getInputtime());
			thisDist.setSynchdate(description.getSynchdate());
			thisDist.setDescid(description.getId());
			String geojsonStr = distributiondataService.parseGeoobject(descontent, "1").toString();
			String geojson = this.distributiondataService.getGeoJSON(geojsonStr);
			
			thisDist.setGeojson(geojson);
			thisDist.setSynchstatus(0);
			this.distributiondataRepository.save(thisDist);
		}
		System.out.println("分布完成......");
		return null;
	}
	
	@Override
	public void setRQUUID() {
		List<Ruqin> list = this.ruqinRepository.findListRuqin();
		for (Ruqin ruqin : list) {
			// ruqin.setRemark(UUIDUtils.getUUID32());
			this.ruqinRepository.save(ruqin);
		}
	}
	
	@Override
	public void getTaxonData() {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Ruqin> list = this.ruqinRepository.findListRuqin();
		Rank rank = this.rankRepository.findOneById("7");
		Taxaset taxaset = this.taxasetRepository.findOneById("d6569c3b9f1c4e59920136d64bc014c5");
		String inputer = thisUser.getId();
		String sourcesid = "f86d0a20-85c0-43f1-b528-1e42930c35d0";
		String expert = "a3ffca7c003c4e4b9bb8fd128810e89e";		// 于胜祥
		License license = this.licenseRepository.findOneById("6");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Taxon taxon = new Taxon();
		for (Ruqin ruqin : list) {
			String sciname = ruqin.getLatename();
			taxon.setId(ruqin.getId());
			taxon.setScientificname(sciname);
			if (StringUtils.isNotBlank(ruqin.getChinese())) {
				taxon.setChname(ruqin.getChinese());
			} else {
				taxon.setChname("-");
			}
			String remark = "{"
            		+ "\"" + "FamilyCn" + "\"" + ":" + "\"" + ruqin.getFamilycn() + "\","
            		+ "\"" + "Family" + "\"" + ":" + "\"" + ruqin.getFamily() + "\","
            		+ "\"" + "GenusCn" + "\"" + ":" + "\"" + ruqin.getGenusc() + "\","
            		+ "\"" + "Genus" + "\"" + ":" + "\"" + ruqin.getGenus() + "\","
            		+ "}";
			taxon.setRemark(remark);
			taxon.setSourcesid(sourcesid);
			taxon.setTaxaset(taxaset);
			taxon.setExpert(expert);
			String latin = ruqin.getLatin();
			if (StringUtils.isNotBlank(latin)) {
				latin = latin.replace(sciname, "").trim();
				taxon.setAuthorstr(latin);
			} else {
				taxon.setAuthorstr("-");
			}
			taxon.setEpithet(ruqin.getEpithet());
			taxon.setStatus(1);
			taxon.setInputer(inputer);
			taxon.setInputtime(timestamp);
			taxon.setSynchdate(timestamp);
			taxon.setNomencode("ICZN");
			if (sciname.contains("var.")) {
				taxon.setRankid("31");
				taxon.setRank(this.rankRepository.findOneById("31"));
			} else {
				taxon.setRankid("7");
				taxon.setRank(rank);
			}
			taxon.setComment(ruqin.getRank_harm());
			this.taxonRepository.save(taxon);
			
			/**
			 * 俗名
			 */
			String common = ruqin.getCommon();
			if (StringUtils.isNotBlank(common)) {
				common = common.replace(",", "、").replace("，", "、");
				String[] split = common.split("、");
				for (String name : split) {
					Commonname commonname = new Commonname();
					commonname.setId(UUIDUtils.getUUID32());
					commonname.setCommonname(name.trim());
					commonname.setLanguage("1");
					commonname.setSourcesid(sourcesid);
					commonname.setTaxon(taxon);
					commonname.setStatus(1);
					commonname.setInputer(inputer);
					commonname.setInputtime(timestamp);
					commonname.setSynchdate(timestamp);
					this.commonnameRepository.save(commonname);
				}
			}
			
			/**
			 * 描述1 - 国内分布
			 */
			String desc1 = ruqin.getGnfb();
			if (StringUtils.isNotBlank(desc1)) {
				Description description = new Description();
				description.setId(UUIDUtils.getUUID32());
				description.setLanguage("1");
				description.setSourcesid(sourcesid);
				description.setDescontent(desc1);
				description.setDestitle(taxon.getScientificname()+"的国内分布");
				description.setLicenseid("6");
				description.setDescriptiontype(this.descriptionTypeRepository.findOneById("209"));
				description.setTaxon(taxon);
				description.setStatus(1);
				description.setInputer(inputer);
				description.setInputtime(timestamp);
				description.setSynchdate(timestamp);
				this.descriptionRepository.save(description);
			}
			/**
			 * 描述2 - 性状描述
			 */
			String desc2 = ruqin.getXingzhuangdesc();
			if (StringUtils.isNotBlank(desc2)) {
				Description description = new Description();
				description.setId(UUIDUtils.getUUID32());
				description.setLanguage("1");
				description.setSourcesid(sourcesid);
				description.setDescontent(desc2);
				description.setDestitle(taxon.getScientificname()+"的性状描述");
				description.setLicenseid("6");
				description.setDescriptiontype(this.descriptionTypeRepository.findOneById("120"));
				description.setTaxon(taxon);
				description.setStatus(1);
				description.setInputer(inputer);
				description.setInputtime(timestamp);
				description.setSynchdate(timestamp);
				this.descriptionRepository.save(description);
			}
			/**
			 * 描述3 - 地理分布图鉴
			 */
			String desc3 = ruqin.getDilifenbutujian();
			if (StringUtils.isNotBlank(desc3)) {
				Description description = new Description();
				description.setId(UUIDUtils.getUUID32());
				description.setLanguage("1");
				description.setSourcesid(sourcesid);
				description.setDescontent(desc3);
				description.setDestitle(taxon.getScientificname()+"的原产地");
				description.setLicenseid("6");
				description.setDescriptiontype(this.descriptionTypeRepository.findOneById("206"));
				description.setTaxon(taxon);
				description.setStatus(1);
				description.setInputer(inputer);
				description.setInputtime(timestamp);
				description.setSynchdate(timestamp);
				this.descriptionRepository.save(description);
			}
		}
	}
	@Override
	public void getRQMedia(String path, HttpServletRequest request, String uploadPath) throws Exception {
		File fileDesc = new File(path);
		File[] files = fileDesc.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				if (!files[i].getName().contains("~$")) {
					parseMediaFileByUrl2019(files[i], request, uploadPath);
					// System.out.println("文件名：" + files[i].getAbsolutePath());
				} 
			} else {
				// System.out.println("文件夹名：" + files[i].getName());
				getRQMedia(path + "/" + files[i].getName(), request, uploadPath);
			}
		}
	}

	private void parseMediaFileByUrl2019(File file1, HttpServletRequest request, String uploadPath) {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String imgPath = file1.getAbsolutePath();
		String remark = "E:\\入侵物种年度数据\\入侵植物\\2019\\第四子课题入侵植物专题\\先导专项——150种外来入侵\\";
		String suffx = file1.getAbsolutePath().replace(remark, "");
		System.out.println("文件路径：" + imgPath);
		int index = suffx.lastIndexOf("\\");
		suffx = suffx.substring(0, index);
		String[] split = suffx.split("_");
		String names = split[2]; 	// Ambrosia artemisiifolia豚草
		String chinese = CommUtils.cutChinese(names);
		names = names.replace(chinese, "").trim();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		License license = this.licenseRepository.findOneById("6");
		String teamId = (String) request.getSession().getAttribute("teamId");									// 当前Team
		String datasetId = (String) request.getSession().getAttribute("datasetID");								// 当前Dataset
		String taxasetId = "d6569c3b9f1c4e59920136d64bc014c5";
		
		Taxon taxon = taxonRepository.findOneByScientificnameAndTaxasetId(names, taxasetId);
		if (null != taxon) {
			Multimedia multimedia = new Multimedia();
			multimedia.setId(UUIDUtils.getUUID32());
			multimedia.setSourcesid(taxon.getSourcesid());
			multimedia.setLisenceid("6");
			multimedia.setLicense(license);
			multimedia.setTaxon(taxon);
			multimedia.setStatus(1);
			multimedia.setInputer(taxon.getInputer());
			multimedia.setInputtime(timestamp);
			multimedia.setSynchdate(timestamp);
			multimedia.setOldPath(file1.getAbsolutePath().replace(remark, ""));
			File file = new File(imgPath);
			String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);						// 后缀名
			String newFileName = UUIDUtils.getUUID32() + "." + suffix;											// 文件名
			String realPath = request.getSession().getServletContext().getRealPath(uploadPath);					// 本地存储路径
			// 构造路径 -- Team/Dataset/Taxon/文件
			realPath = realPath + teamId + "/" + datasetId + "/" + taxon.getId() + "/";
			if (suffix.contains("jpg") || suffix.contains("png")) {
				multimedia.setMediatype("1");
			} else {
				multimedia.setMediatype("3");
			}
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				// 先把文件保存到本地
				FileUtils.copyInputStreamToFile(fileInputStream, new File(realPath.replace("\\", File.separator), newFileName));
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("文件保存到本地发生异常:" + e1.getMessage());
			}
			
			multimedia.setSuffix(suffix);
			multimedia.setPath(teamId + "/" + datasetId + "/" + taxon.getId() + "/" + newFileName);
			this.multimediaRepository.save(multimedia);
		}
	}
	
	@Override
	public void parseRQZWDist(String path) throws Exception{
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Taxon> taxonList = new ArrayList<>();
		String taxasetId = "4ce0685f14a043d784926ca22ee25424";		// 2018植物
		//String taxasetId = "196621bf74b648839f24f4fd5cfbdeda";	// 2018动物
		//String taxasetId = "67a17cd2b2c54af3a366c51c73e0f03a";	// 2019动物
		String sourcesId = "f86d0a20-85c0-43f1-b528-1e42930c35d0";
		String expertId = "a3ffca7c003c4e4b9bb8fd128810c67c";
		
		// 创建XSSFWorkbook对象
		XSSFWorkbook workbook = new XSSFWorkbook(path);
		// 获取Sheet
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 获取总行数
		int rowNums = sheet.getLastRowNum();
		
		Taxon thisTaxon = new Taxon();
		for (int i = 1; i <= rowNums; i++) {
            XSSFRow row = sheet.getRow(i);
            if (null == row) { 			// 记录行
            	continue; 									// 空行
            }else {											// 构建集合对象
            	String sciname = excelService.getStringValueFromCell(row.getCell(4));
            	String distContent = excelService.getStringValueFromCell(row.getCell(6));
            	if (StringUtils.isNotBlank(distContent)) {
					Taxon taxon = this.taxonRepository.findOneByScientificnameAndTaxasetId(sciname, taxasetId);
					if (null != taxon) {
						Description description = new Description();
						description.setId(UUIDUtils.getUUID32());
						description.setLanguage("1");
						description.setSourcesid(taxon.getSourcesid());
						description.setDescontent(distContent);
						description.setDestitle(taxon.getScientificname()+"的分布信息");
						description.setLicenseid("6");
						description.setDescriptiontype(this.descriptionTypeRepository.findOneById("201"));
						description.setDestypeid("201");
						description.setTaxon(taxon);
						description.setStatus(1);
						description.setInputer(taxon.getInputer());
						description.setInputtime(new Timestamp(System.currentTimeMillis()));
						description.setSynchdate(new Timestamp(System.currentTimeMillis()));
						this.descriptionRepository.save(description);
					}
				}
			}
        }
	}

	@Override
	public String mergeRuqinData(String[] taxasetsP) {
		// 1.合并物种
		Taxaset taxaset = this.taxasetRepository.findOneById(taxasetsP[0]);
		List<Taxon> list = this.taxonRepository.findTaxonListByTaxasets(taxasetsP[0], taxasetsP[1]);
		for (Taxon taxon : list) {
			taxon.setTaxaset(taxaset);
			// this.taxonRepository.save(taxon);
		}
		// 2.合并描述
		mergeRQDWDescription(taxasetsP);
		
		// 3.合并俗名
		mergeRQZWCommonname(taxasetsP);
		return null;
	}

	/**
	 * 合并入侵植物描述
	 * @param taxasetsP
	 */
	private void mergeRQDWDescription(String[] taxasetsP) {
		// 2.合并描述
		// 查 2019 年入侵植物的所有描述
		List<Description> descList = this.descriptionRepository.findDescriptionListByTaxasetId(taxasetsP[1]);
		for (Description description : descList) {
			String latin = description.getTaxon().getScientificname();
			String descontent = description.getDescontent();
			// 在 2018 年的分类单元集中，查 2019 年入侵植物的【物种】描述
			Description thisDesc = this.descriptionRepository.findOneByContentAndLatinAndTaxasetId(descontent, latin, taxasetsP[0]);
			if (null == thisDesc) {
				// 在 2018 年的分类单元集中，查 2019 年入侵植物描述所关联的【物种】
				Taxon taxon = this.taxonRepository.findOneByScientificnameAndTaxasetId(latin, taxasetsP[0]);
				if (null != taxon) {
					description.setTaxon(taxon);
					// this.descriptionRepository.save(description);
				} else {
					System.out.println("异常：" + "该条描述找不到对应物种" + latin + "\t" + descontent);
				}
			} else {
				System.out.println("提示：该条物种描述在 2018 的入侵植物数据中， 已存在！" + latin + "\t" + descontent);
			}
		}
	}

	/**
	 * 合并入侵植物俗名
	 * @param taxasetsP
	 */
	private void mergeRQZWCommonname(String[] taxasetsP) {
		// 3.合并俗名
		// 查 2019 年入侵植物的所有俗名
		List<Commonname> commList = this.commonnameRepository.findCommonnameListByTaxasetId(taxasetsP[1]);
		for (Commonname comm : commList) {
			String latin = comm.getTaxon().getScientificname();
			String name = comm.getCommonname();
			// 在 2018 年的分类单元集中，查 2019 年入侵植物的【物种】俗名
			Commonname thisComm = this.commonnameRepository.findOneByCommonnameAndTaxasetId(name, taxasetsP[0]);
			if (null == thisComm) {
				// 在 2018 年的分类单元集中，查 2019 年入侵植物俗名所关联的【物种】
				Taxon taxon = this.taxonRepository.findOneByScientificnameAndTaxasetId(latin, taxasetsP[0]);
				if (null != taxon) {
					comm.setTaxon(taxon);
					//this.commonnameRepository.save(comm);
				} else {
					System.out.println("异常：" + "该条俗名找不到对应物种");
				}
			} else {
				System.out.println("提示：该条物种俗名在 2018 的入侵植物数据中， 已存在！");
			}
		}
	}
}