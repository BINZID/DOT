package org.big.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.Geoobject;

public interface GeoobjectService {
	/**
	 *<b>Geoobject的select列表</b>
	 *<p> Geoobject的检索列表</p>
	 * @author WangTianshan(王天山)
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON findBySelect(HttpServletRequest request);
	/**
	 *<b>GeoobjectAdministrative的select列表</b>
	 *<p> GeoobjectAdministrative的检索列表</p>
	 * @author WangTianshan(王天山)
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON findAdministrativeBySelect(HttpServletRequest request);
	/**
	 *<b>构造拼音</b>
	 *<p> 构造拼音</p>
	 * @author WangTianshan(王天山)
	 * @return com.alibaba.fastjson.JSON
	 */
	Boolean buildPy();
	
	/**
	 *<b>根据id查找Geoobject实体</b>
	 *<p> 根据id查找Geoobject实体</p>
	 * @author BINZI
	 * @param geoobjectId
	 * @return org.big.entity.Geoobjec
	 */
	Geoobject findOneById(String geoobjectId);
	
	/**
	 *<b>根据Geoobject实体geojson属性构造回显JSON数据</b>
	 *<p> 根据Geoobject实体geojson属性构造回显JSON数据</p> 
	 * @param geojson
	 * @return
	 */
	JSONArray buildGeoJSON(String geojson);
	
	/**
	 * <b>根据选中的地理实体Id补充选中地理实体文本框的展示</b>
	 * <p> 根据选中的地理实体Id补充选中地理实体文本框的展示</p>
	 * @author BINZI
	 * @param geoids
	 * @return
	 */
	JSON getGeoobjectById(String geoids);
}
