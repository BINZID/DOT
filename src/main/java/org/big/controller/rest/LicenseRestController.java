package org.big.controller.rest;

import javax.servlet.http.HttpServletRequest;

import org.big.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 *<p><b>License相关的Controller的Rest风格类</b></p>
 *<p> License相关的Controller的Rest风格类</p>
 * @author BINZI
 *<p>Created date: 2018/06/11 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */

@RestController
@RequestMapping(value = "/console/license/rest")
public class LicenseRestController {

	@Autowired
	private LicenseService licenseService;
	
	/**
     *<b>License的index页面</b>
     *<p> License的index页面</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JSON list(HttpServletRequest request) {
		return this.licenseService.findLicenseList(request);
	}
	
	/**
	 * <b>Description的License下拉选查询</b>
	 * <p> Description的License下拉选查询</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public JSON data(HttpServletRequest request) {
		return this.licenseService.findBySelect(request);
	}
	
	/**
	 * <b>数据源的共享协议编辑</b>
	 * <p> 数据源的共享协议编辑</p>
	 * @param id
	 * @return
	 */
	@GetMapping(value="/editLicense/{id}")
	public JSON editLicense(@PathVariable String id) {
		JSONObject thisResult = new JSONObject();
		try {
			thisResult.put("text", this.licenseService.findOneById(id).getTitle());
		} catch (Exception e) {
		}
		return thisResult;
	}
}
