package org.big.controller.rest;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.big.service.DescriptionService;
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
@RequestMapping("/console/description/rest")
public class DescriptionRestController {
	@Autowired
	private DescriptionService descriptionService;
	/**
	 *<b>Description的已上传listJSON</b>
	 *<p> Description的已上传listJSON</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @param timestamp
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value = "/uploaded/{timestamp}")
	public JSON list(HttpServletRequest request, @PathVariable Timestamp timestamp) {
		return this.descriptionService.findUploadedDescriptionList(timestamp, request);
	}
	
	/**
	 * <b> Description实体添加</b>
	 * <p> Description实体添加</p>
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value="/add/{id}", method = {RequestMethod.POST})
	public JSON addDescription(HttpServletRequest request, @PathVariable String id) {
		return this.descriptionService.addDescription(id, request);
	}
	
	/**
	 * <b> Description实体修改</b>
	 * <p> Description实体修改</p>
	 * @param id
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value="/edit/{id}")
	public JSON editDescription(@PathVariable String id) {
		return this.descriptionService.editDescriptions(id);
	}
	
	/**
	 * <b> 根据Id批量逻辑删除指定Description</b>
	 * <p> 根据Id批量逻辑删除指定Description</p>
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/removeMany/{ids}", method = RequestMethod.GET)
	public int removeMany(@PathVariable String ids) {
		try {
			String[] idArr = ids.split("￥");
			int isRemove = 0;
			for (String id : idArr) {
				if (this.descriptionService.logicRemove(id)) {
					isRemove = isRemove + 1;
				}
			}
			return isRemove;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * <b> 根据Id单个逻辑删除指定Description</b>
	 * <p> 根据Id单个逻辑删除指定Description</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public boolean remove(@PathVariable String id) {
		try {
			return this.descriptionService.logicRemove(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
     *<b>Description信息添加后的删除</b>
     *<p> Description信息添加后的删除</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public boolean delete(HttpServletRequest request){
		return this.descriptionService.deleteOne(request);
	}
	
	/**
	 * <b>Description的Index页面分页列表查询</b>
	 * <p> Description的Index页面分页列表查询</p>
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/select")
	public JSON data(HttpServletRequest request) {
		return this.descriptionService.findBySelect(request);
	}
	
	/**
     *<b>根据TaxonId查找对应Taxon下的所有Description</b>
     *<p> 根据TaxonId查找对应Taxon下的所有Description</p>
     * @author BINZI
     * @param request 页面请求
     * @return 
     */
	@GetMapping("/descriptionList/{id}")
	public JSON descriptionList(HttpServletRequest request,@PathVariable String id) {
		return this.descriptionService.findDescriptionListByTaxonId(id, request);
	}
	
	/**
	 * <b>Taxon下的描述信息实体的关系下拉选</b>
	 * <p> Taxon下的描述信息实体的关系字段</p>
	 * @param id
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/select/{id}")
	public JSON descSelect(@PathVariable String id, HttpServletRequest request) {
		return this.descriptionService.findDescListByTaxonId(id, request);
	}
	
	/**
	 * <b>解析导入的Description相关的Excel文件</b>
	 * <p> 解析导入的Description相关的Excel文件</p>
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/upload/{taxasetId}")
    public JSON upload(@RequestParam("uploadDescription") MultipartFile file, @PathVariable String taxasetId, HttpServletRequest request) throws Exception{
		return this.descriptionService.uploadFile(file, taxasetId, request);
	}
	
	/**
	 * <b>根据选中的描述Id补充分布描述内容</b>
	 * <p> 根据选中的描述Id补充分布描述内容</p>
	 * @author BINZI
	 * @param descid
	 * @return
	 */
	@GetMapping(value = "/getDescContent/{descid}")
	public JSON getDescContent(@PathVariable String descid) {
		return this.descriptionService.getDescContentById(descid);
	}
	
}
