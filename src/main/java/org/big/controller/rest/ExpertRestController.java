package org.big.controller.rest;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.Expert;
import org.big.service.ExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 *<p><b>Expert相关的Controller的Rest风格类</b></p>
 *<p> Expert相关的Controller的Rest风格类</p>
 * @author BINZI
 *<p>Created date: 2018/10/15 15:45</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */

@RestController
@RequestMapping(value = "/console/expert/rest")
public class ExpertRestController {

	@Autowired
	private ExpertService expertService;
	/**
	 *<b>Expert的index页面</b>
	 *<p> Expert的index页面</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value = "/list")
	public JSON list(HttpServletRequest request) {
		return this.expertService.findExpertList(request);
	}

	/**
	 *<b>Expert的已上传列表tablejson</b>
	 *<p> Expert的已上传列表tablejson</p>
	 * @author WangTianshan
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value = "/uploaded/{fileMark}")
	public JSON uploaded(HttpServletRequest request,@PathVariable String fileMark) {
		return this.expertService.findUploadedExpertList(request,fileMark);
	}
	
	/**
	 * <b>专家信息上传列表页面</b>
	 * <p>专家信息上传列表页面</p>
	 * @author BINZI
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadList", method = RequestMethod.GET)
	public JSON uploadList(HttpServletRequest request) {
		return this.expertService.findUploadList(request);
	}
	
	/**
	 * <b>Expert下拉选查询</b>
	 * <p> Expert下拉选查询</p>
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/select")
	public JSON select(HttpServletRequest request) {
		return this.expertService.findBySelect(request);
	}
	
	/**
	 * <b>数据源的审核专家编辑</b>
	 * <p> 数据源的审核专家编辑</p>
	 * @param id
	 * @return
	 */
	@GetMapping(value="/editExpert/{id}")
	public JSON editExpert(@PathVariable String id) {
		JSONObject thisResult = new JSONObject();
		try {
			Expert thisExpert = this.expertService.findOneById(id);
			thisResult.put("text", thisExpert.getCnName() + "<" + thisExpert.getExpEmail() + ">");
		} catch (Exception e) {
		}
		return thisResult;
	}
	
	/**
	 * <b>解析导入的Expert相关的Excel文件</b>
	 * <p> 解析导入的Expert相关的Excel文件</p>
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/upload")
    public JSON upload(@RequestParam("uploadExpert") MultipartFile file, HttpServletRequest request) throws Exception{
		return this.expertService.uploadFile(file, request);
	}
	
}
