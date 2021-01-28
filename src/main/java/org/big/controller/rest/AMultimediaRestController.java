package org.big.controller.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.Geoobject;
import org.big.entity.Multimedia;
import org.big.repository.GeoobjectRepository;
import org.big.repository.MultimediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *<p><b>Description相关的Controller的Rest风格类</b></p>
 *<p> Description相关的Controller的Rest风格类</p>
 * @author BINZI
 *<p>Created date: 2018/06/13 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/console/multimedia/rest")
public class AMultimediaRestController {
	@Autowired
	private MultimediaRepository mp;
	@Autowired
	private GeoobjectRepository gr;
	
	@GetMapping("/copy")
	public void copy(HttpServletRequest request) throws IOException {
		int maxInactiveInterval = request.getSession().getMaxInactiveInterval();
		System.out.println(maxInactiveInterval);
		String inputer = "wwwww45023d04ef8a134f0f017d316f0";
		String dirPath = "F:/images/";
		String orignalPath = "C:/Users/BIGIOZ/Downloads/jdbc/";		// 源文件夹路径
		File oldFile = new File(orignalPath + "images");
		String[] oldFileNames = oldFile.list();
		StringBuffer strBuff = new StringBuffer();
		for (int i = 0; i < oldFileNames.length; i++) {
			strBuff.append(oldFileNames[i]);
		}
		String oldFileNamesStr = strBuff.toString();
		
		List<Multimedia> mulList = this.mp.findMultimediasByInputer(inputer);	// 4604
		for (int i = 0; i < mulList.size(); i++) {
			String path = mulList.get(i).getPath();
			int lastIndexOf = path.lastIndexOf("/");							// team/dataset/taxon
			String aimPath = dirPath + path.substring(0, lastIndexOf + 1);		// 目标文件夹路径构造 -- F:/images/team/dataset/taxon
			String oldPath = orignalPath + mulList.get(i).getOldPath();			// 文件原始路径	 -- C:/Users/BIGIOZ/Downloads/jdbc/images/{图片原始名称}
			String newFileName = path.substring(lastIndexOf + 1);				// 图片新名称
			String oldName = mulList.get(i).getOldPath();
			int lastIndexOf2 = oldName.lastIndexOf("\\");
			oldName = oldName.substring(lastIndexOf2 + 1, oldName.length());
			if (oldFileNamesStr.contains(oldName)) {
				copyFolder(oldPath, aimPath, newFileName);
			}else {
				String taxonId = mulList.get(i).getTaxon().getId();
				String scientificname = mulList.get(i).getTaxon().getScientificname();
				String pathOld = mulList.get(i).getOldPath();
				System.out.println(taxonId + "\t" + scientificname + "\t" + pathOld);
			}
			
		}
	}
	private void copyFolder(String oldPath, String aimPath, String newFileName) throws IOException {
		File mFile = new File(aimPath);
		if(!mFile.exists()&&!mFile.isDirectory()) {							
			(new File(aimPath)).mkdirs();										// 如果路径不存在，新建
		}
		File temp =  new File(oldPath);
		if (temp.isFile()) {
            String fileName = aimPath + "/" + newFileName;
            File testFile = new File(fileName);
            if (!testFile.exists()) {
                FileInputStream input = new FileInputStream(temp);
                FileOutputStream output = new FileOutputStream(fileName);
                byte[] b = new byte[1024 * 5];
                int len;
                while ((len = input.read(b)) != -1) {
                    output.write(b, 0, len);
                }
                output.flush();
                output.close();
                input.close();
            }
        }
	}
	
	@GetMapping("/shortName")
	public void shortName(HttpServletRequest request) {
		List<Geoobject> arrList = new ArrayList<>();
		List<Geoobject> geoList = gr.findAllGeoobject();
		for (int i = 0; i < geoList.size(); i++) {
			Geoobject geoobject = geoList.get(i);
			String cngeoname = geoList.get(i).getCngeoname();
			String adcode = geoList.get(i).getAdcode();
			String shortName = null;
			
			// 判断国家
			String nationCode = adcode.substring(1, 2);
			// 计算省code
			String provinceCode = adcode.substring(0, 2);
			// 计算市code
			String cityCode = adcode.substring(2, 4);
			// 计算县code
			String countyCode = adcode.substring(4, 6);
			
			/**
			 * 100000 省
			 * 110000 市
			 * 130100 县
			 * 140100 市辖区
			 */
			//判断级别
		/*	if(countyCode.equals("00")){//市级以上
				if(cityCode.equals("00")){//
					if(nationCode.equals(0)){//国家
					}else{//省级
					}
				}
			}
			else{//县级
			}*/
			
			//判断级别
			if(countyCode.equals("00")){//市级以上
				if ((cngeoname.contains("族自治州") || cngeoname.contains("族自治县")) && !cngeoname.contains("市辖区")) {
					shortName = cngeoname.substring(0, cngeoname.length()-5);
				}else if(cngeoname.contains("地区") || cngeoname.contains("林区") || cngeoname.contains("城区") || cngeoname.contains("堂区") || cngeoname.contains("新区")){
					if (cngeoname.length() > 3 && !cngeoname.contains("市辖区")) {
						shortName = cngeoname.substring(0,cngeoname.length()-2);
					}else {
						shortName = cngeoname.substring(0,cngeoname.length());
					}
				}else if(cngeoname.length() > 2 && !cngeoname.contains("市辖区")){
					shortName = cngeoname.substring(0,cngeoname.length()-1);
				}else {
					shortName = cngeoname.substring(0,cngeoname.length());
				}
				
				if(cityCode.equals("00")){
					if(nationCode.equals(0)){//国家
						
					}else{//省级
						if ((cngeoname.contains("特别行政区") || cngeoname.contains("自治区")) && !cngeoname.contains("市辖区")) {
							shortName = cngeoname.substring(0, 2);
						}else {
							shortName = cngeoname.substring(0,cngeoname.length()-1);
						}
					}
				}
			}else{//县级
				if (cngeoname.contains("自治县") && !cngeoname.contains("市辖区")) {
					shortName = cngeoname.substring(0, cngeoname.length()-3);
				}else if(cngeoname.length() > 2 && !cngeoname.contains("市辖区")){
					shortName = cngeoname.substring(0,cngeoname.length()-1);
				}else {
					shortName = cngeoname.substring(0,cngeoname.length());
				}
			}
			geoobject.setShortName(shortName);
			arrList.add(geoobject);
		}
		gr.saveAll(arrList);
	}
}
