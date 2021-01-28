package org.big.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.big.common.QueryTool;
import org.big.common.StrEditer;
import org.big.entity.Geoobject;
import org.big.repository.GeoobjectRepository;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class GeoobjectServiceImpl implements GeoobjectService{

	@Autowired
	private GeoobjectRepository geoobjectRepository;
	
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
		String sort = "adcode";
		String order = "asc";
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		List<Geoobject> thisList = new ArrayList<>();

		Page<Geoobject> thisPage = this.geoobjectRepository.searchInfo(findText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order),"");
		thisSelect.put("total_count", thisPage.getTotalElements());
		Boolean incompleteResulte = true;
		if ((thisPage.getTotalElements() / 30) > findPage)
			incompleteResulte = false;
		thisSelect.put("incompleteResulte", incompleteResulte);
		thisList = thisPage.getContent();
//		if (findPage == 1) {
//			JSONObject row = new JSONObject();
//			row.put("id", "addNew");
//			row.put("text", "新建一个数据源");
//			items.add(row);
//		}
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			row.put("id", thisList.get(i).getId());
			row.put("text", thisList.get(i).getCngeoname()+"("+thisList.get(i).getEngeoname()+")");
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public JSON findAdministrativeBySelect(HttpServletRequest request) {
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
		String sort = "adcode";
		String order = "asc";
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		List<Geoobject> thisList = new ArrayList<>();

		Page<Geoobject> thisPage = this.geoobjectRepository.searchInfo(findText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order),"");
		thisSelect.put("total_count", thisPage.getTotalElements());
		Boolean incompleteResulte = true;
		if ((thisPage.getTotalElements() / 30) > findPage)
			incompleteResulte = false;
		thisSelect.put("incompleteResulte", incompleteResulte);
		thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			//获取adcode
			String adcode=thisList.get(i).getAdcode();
			row.put("id", thisList.get(i).getId());
			if (this.isNumeric(adcode)) {
				//判断国家
				String nationCode=adcode.substring(1,2);
				//计算省code
				String provinceCode=adcode.substring(0,2);
				//计算市code
				String cityCode=adcode.substring(2,4);
				//计算县code
				String countyCode=adcode.substring(4,6);
				
				//判断级别
				if(countyCode.equals("00")){//市级以上
					row.put("text", "&nbsp;&nbsp;&nbsp;&nbsp;<b>"+thisList.get(i).getCngeoname()+"("+thisList.get(i).getEngeoname()+")</b>");
					if(cityCode.equals("00")){//
						if(nationCode.equals(0)){//国家
							row.put("text", "<i>"+thisList.get(i).getCngeoname()+"("+thisList.get(i).getEngeoname()+")</i>");
						}else{//省级
							row.put("text", "&nbsp;&nbsp;<b><i>"+thisList.get(i).getCngeoname()+"("+thisList.get(i).getEngeoname()+")</i></b>");
						}
					}
				}
				else{//县级
					row.put("text", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+thisList.get(i).getCngeoname()+"("+thisList.get(i).getEngeoname()+")");
				}
				items.add(row);
			}else {
				//界、亚界、区、亚区
				row.put("text", "&nbsp;&nbsp;&nbsp;&nbsp;<b>"+thisList.get(i).getCngeoname()+"("+thisList.get(i).getEngeoname()+")</b>");
				items.add(row);
			}
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public Boolean buildPy() {
		List<Geoobject> thisList = this.geoobjectRepository.findAll();
		StrEditer strEditer=new StrEditer();
		for (int i = 0; i < thisList.size(); i++) {
			Geoobject thisGeoobject=thisList.get(i);
			try {
				thisGeoobject.setEngeoname(strEditer.getPY(thisGeoobject.getCngeoname()));
				this.geoobjectRepository.save(thisGeoobject);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public Geoobject findOneById(String geoobjectId) {
		return this.geoobjectRepository.findOneById(geoobjectId);
	}

	@Override
	public JSONArray buildGeoJSON(String geojson) {
		JSONArray geos = new JSONArray();
		String[] geoIdsArr = null;
		if (StringUtils.isNotBlank(geojson)) {
			JSONObject geoJsonObj = JSON.parseObject(geojson);
			String str = (String) geoJsonObj.get("geoIds");
			geoIdsArr = str.split("&");
		}
		if (null != geoIdsArr) {
			for (String geoId : geoIdsArr) {
				Geoobject thisGeoobject = new Geoobject();
				JSONObject geo = new JSONObject();
				if (StringUtils.isNotBlank(geoId)) {
					thisGeoobject = this.geoobjectRepository.findOneById(geoId);
					geo.put("geoId", geoId);
					geo.put("geoName", thisGeoobject.getCngeoname() + "(" + thisGeoobject.getEngeoname() + ")");
				}
				geos.add(geo);
			}
		}
		return geos;
	}

	@Override
	public JSON getGeoobjectById(String geoids) {
		JSONArray thisResult = new JSONArray();
		String[] geoidArr = geoids.split(",");
		for (int i = 0; i < geoidArr.length; i++) {
			Geoobject thisGeoobject = this.geoobjectRepository.findOneById(geoidArr[i]);
			String cngeoname = thisGeoobject.getCngeoname();
			String adcode = thisGeoobject.getAdcode();
			JSONObject row = new JSONObject();
			if (this.isNumeric(adcode)) {
				if (adcode.contains("000000") && !cngeoname.contains("其他")) {
					row.put("geoobject", cngeoname + " [国家]");
				}else if (cngeoname.contains("其他")) {
					row.put("geoobject", cngeoname + " [其他]");
				}else if (cngeoname.contains("中华人民共和国")){
					row.put("geoobject", cngeoname + " [国家]");
				}else if ((cngeoname.contains("省") || cngeoname.contains("自治区") || cngeoname.contains("北京市") || cngeoname.contains("上海市") || cngeoname.contains("重庆市") || cngeoname.contains("天津市")) && !cngeoname.contains("市辖区")) {
					row.put("geoobject", cngeoname + " [省级]");
				}else if ((cngeoname.contains("市辖区") || cngeoname.contains("市") || cngeoname.contains("自治州")) && !cngeoname.contains("北京市") && !cngeoname.contains("上海市") && !cngeoname.contains("重庆市") && !cngeoname.contains("天津市") && !cngeoname.contains("市辖区")) {
					row.put("geoobject", cngeoname + " [市级]");
				}else if ((cngeoname.contains("县") || cngeoname.contains("自治县") || cngeoname.contains("区") || cngeoname.contains("市辖区")) && !cngeoname.contains("特别行政区")) {
					row.put("geoobject", cngeoname + " [县级]");
				}else if (cngeoname.contains("特别行政区")) {
					row.put("geoobject", cngeoname + " [特别行政区]");
				}else {
					row.put("geoobject", cngeoname + " [县级]");
				}
			}else {
				if (cngeoname.contains("界") && !cngeoname.contains("亚界")){
					row.put("geoobject", cngeoname + " [界]");
				}else if (cngeoname.contains("区") && !cngeoname.contains("亚区")) {
					row.put("geoobject", cngeoname + " [区]");
				}else if (cngeoname.contains("亚区")) {
					row.put("geoobject", cngeoname + " [亚区]");
				}else {
					row.put("geoobject", cngeoname + " [亚界]");
				}
			}
			
			
			thisResult.add(row);
		}
		return thisResult;
	}
	
	/**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    public boolean isNumeric(String str){
           Pattern pattern = Pattern.compile("[0-9]*");
           Matcher isNum = pattern.matcher(str);
           if( !isNum.matches() ){
               return false;
           }
           return true;
    }
}
