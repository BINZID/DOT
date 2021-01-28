package org.big.controller.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import org.big.service.KeyitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

/**
 *<p><b>Keyitem相关的Controller的Rest风格类</b></p>
 *<p> Keyitem相关的Controller的Rest风格类</p>
 * @author BINZI
 *<p>Created date: 2018/08/03 13:58</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/console/keyitem/rest")
public class KeyitemRestController {
	@Autowired
	private KeyitemService keyitemService;
	
	private String uploadPath = "upload/";
	
	@GetMapping(value = "/featureImgList")
	public JSON featureImgList(HttpServletRequest request) {
		return this.keyitemService.findFeatureImgList(request);
	}
	
	/**
	 * <b> Taxkey的Index页面分页列表查询</b>
	 * <p> Taxkey的Index页面分页列表查询</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	public JSON list(@PathVariable String id) {
		return this.keyitemService.findKeyitemList(id);
	}
	
	/**
     *<b>特征图片上传（批量）</b>
     *<p> 特征图片上传上传</p>
     * @author BINZI
	 * @param id
	 * @param file
	 * @param request
	 * @param response
	 * @return com.alibaba.fastjson.JSON
	 * @throws IOException
	 */
	@PostMapping(value = "/batch")
	public JSON addImages(@RequestParam("input-fa[]") MultipartFile file, HttpServletRequest request)
            throws IOException {
		String id = request.getParameter("keyitemId");
       return this.keyitemService.batchImages(id, request, file, uploadPath + "images/");
    }
	
    /**
     *<b>删除单个Keyitem</b>
     *<p> 根据KeyitemId删除单个</p>
     * @author BINZI
 	 * @param id
     * @return boolean
     */
    @RequestMapping(value="/remove/{id}", method = RequestMethod.GET)
    public boolean RemoveDataset(@PathVariable String id) {
    	try{
            return this.keyitemService.delOne(id);
        }catch(Exception e){
            return false;
        }
    }

	/**
	 * <b> 根据TaxkeyId返回检索表详情</b>
	 * <p> 根据TaxkeyId返回检索表详情</p>
	 * @author WangTianshan（王天山）
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/table", method = RequestMethod.GET)
	public JSONArray Table(HttpServletRequest request) {
		return this.keyitemService.findKeyitemTable(request.getParameter("taxkeyId"));
	}
}
