package org.big.controller.rest;

import javax.servlet.http.HttpServletRequest;

import org.big.service.ProtectstandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

/**
 *<p><b>Protectstandard相关的Controller的Rest风格类</b></p>
 *<p> Protectstandard相关的Controller的Rest风格类</p>
 * @author BINZI
 *<p>Created date: 2018/06/14 13:58</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/console/protectstandard/rest")
public class ProtectstandardRestController {
	@Autowired
	private ProtectstandardService protectstandardService;
	
	/**
     *<b>Protectstandard的select列表(保护标准名称)</b>
     *<p> 当前用户的Protectstandard的select检索列表(保护标准名称)</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	@RequestMapping(value = "/selectStandard", method = RequestMethod.GET)
	public JSON selectStandard(HttpServletRequest request){
		return this.protectstandardService.findBySelectStandard(request);
	}
	
	/**
     *<b>Protectstandard的select列表(保护标准版本)</b>
     *<p> 当前用户的Protectstandard的select检索列表(保护标准版本)</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	@RequestMapping(value = "/selectVersion", method = RequestMethod.GET)
	public JSON selectVersion(HttpServletRequest request){
		String standardname = request.getParameter("type");
		return this.protectstandardService.findBySelectVersion(request, standardname);
	}
	
	/**
     *<b>Protectstandard的select列表(保护标准级别)</b>
     *<p> 当前用户的Protectstandard的select检索列表(保护标准级别)</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	@RequestMapping(value = "/selectProtlevel", method = RequestMethod.GET)
	public JSON selectProtlevel(HttpServletRequest request){
		String version = request.getParameter("type");
		String standardname = request.getParameter("standardname");
		System.out.println("controller:" + standardname + "\t" + version);
		return this.protectstandardService.findBySelectProtlevel(request, version, standardname);
	}
}
