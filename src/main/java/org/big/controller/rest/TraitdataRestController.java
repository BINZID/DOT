package org.big.controller.rest;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import org.big.service.TraitdataService;
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
/**
 *<p><b>Traitdata相关的Controller的Rest风格类</b></p>
 *<p> Traitdata相关的Controller的Rest风格类</p>
 * @author BINZI
 *<p>Created date: 2018/06/21 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController
@RequestMapping(value = "/console/traitdata/rest")
public class TraitdataRestController {

	@Autowired
	private TraitdataService traitdataService;
	
	/**
	 *<b>Traitdata的已上传listJSON</b>
	 *<p> Traitdata的已上传listJSON</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @param timestamp
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value = "/uploaded/{timestamp}")
	public JSON list(HttpServletRequest request, @PathVariable Timestamp timestamp) {
		return this.traitdataService.findUploadedTraitdataList(timestamp, request);
	}
	
	/**
	 * <b>Traitdata实体添加</b>
	 * <p> Traitdata实体添加</p>
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value="/add/{id}", method = {RequestMethod.POST})
	public JSON addTraitdata(HttpServletRequest request, @PathVariable String id) {
		return this.traitdataService.addTraitdata(id, request);
	}
	
	/**
	 * <b> Traitdata实体修改</b>
	 * <p> Traitdata实体修改</p>
	 * @param id
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value="/edit/{id}")
	public JSON editTraitdata(@PathVariable String id) {
		return this.traitdataService.editTraitdata(id);
	}
	/**
     *<b>Traitdata信息添加后的删除</b>
     *<p> Traitdata信息添加后的删除</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public boolean delete(HttpServletRequest request){
		return this.traitdataService.deleteOne(request);
	}
	
	/**
     *<b>根据TaxonId查找对应Taxon下的所有Traitdata</b>
     *<p> 根据TaxonId查找对应Taxon下的所有Traitdata</p>
     * @author BINZI
	 * @param request
	 * @param id
     * @return 
     */
	@GetMapping("/traitdataList/{id}")
	public JSON traitdataList(HttpServletRequest request,@PathVariable String id) {
		return this.traitdataService.findTraitdataListByTaxonId(id, request);
	}

	/**
	 *<b>构造特征列表</b>
	 *<p> 构造特征列表</p>
	 * @author WangTianshan
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value = "/buildTrait", method = RequestMethod.GET)
	public JSONArray BuildTrait(HttpServletRequest request) {
		return this.traitdataService.BuildTrait(request);
	}
	
	/**
	 * <b>解析导入的Traitdata相关的Excel文件</b>
	 * <p> 解析导入的Traitdata相关的Excel文件</p>
	 * @param file
	 * @param taxasetId
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/upload/{taxasetId}")
    public JSON upload(@RequestParam("uploadTraitdata") MultipartFile file, @PathVariable String taxasetId, HttpServletRequest request) throws Exception{
		return this.traitdataService.uploadFile(file, taxasetId, request);
	}
}
