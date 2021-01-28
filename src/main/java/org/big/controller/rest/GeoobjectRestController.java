package org.big.controller.rest;

import com.alibaba.fastjson.JSON;
import org.big.service.GeoobjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *<p><b>Geoobject相关的Controller的Rest风格类</b></p>
 *<p> Geoobject相关的Controller的Rest风格类</p>
 * @author  WangTianshan (王天山)
 *<p>Created date: 2018/06/19 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController
@RequestMapping(value = "/console/geoobject/rest")
public class GeoobjectRestController {
	@Autowired
	private GeoobjectService geoobjectService;

	/**
	 *<b>Geoobject的select列表</b>
	 *<p> 当前Dataset下的Geoobject的select检索列表</p>
	 * @author WangTianshan (王天山)
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public JSON select(HttpServletRequest request) {
		return this.geoobjectService.findBySelect(request);
	}

	/**
	 *<b>Geoobject的select列表</b>
	 *<p> 当前Dataset下的Geoobject的select检索列表</p>
	 * @author WangTianshan (王天山)
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value = "/select/administrative", method = RequestMethod.GET)
	public JSON selectAdministrative(HttpServletRequest request) {
		return this.geoobjectService.findAdministrativeBySelect(request);
	}


	/**
	 *<b>Geoobject的select列表</b>
	 *<p> 当前Dataset下的Geoobject的select检索列表</p>
	 * @author WangTianshan (王天山)
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value = "/buildpy", method = RequestMethod.GET)
	public Boolean buildPy() {
		return this.geoobjectService.buildPy();
	}
	
	/**
	 * <b>根据选中的地理实体Id补充选中地理实体文本框的展示</b>
	 * <p> 根据选中的地理实体Id补充选中地理实体文本框的展示</p>
	 * @author BINZI
	 * @param geoids
	 * @return
	 */
	@GetMapping(value = "/getGeoobject/{geoids}")
	public JSON getGeoobject(@PathVariable String geoids) {
		return this.geoobjectService.getGeoobjectById(geoids);
	}
}
