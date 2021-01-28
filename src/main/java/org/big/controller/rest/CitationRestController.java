package org.big.controller.rest;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.big.service.CitationService;
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
 *<p><b>Citation相关的Controller的Rest风格类</b></p>
 *<p> Citation相关的Controller的Rest风格类</p>
 * @author BINZI
 *<p>Created date: 2018/06/11 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/console/citation/rest")
public class CitationRestController {
	@Autowired
	private CitationService citationService;
	
	/**
	 *<b>Citation的已上传listJSON</b>
	 *<p> Citation的已上传listJSON</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @param timestamp
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value = "/uploaded/{timestamp}")
	public JSON list(HttpServletRequest request, @PathVariable Timestamp timestamp) {
		return this.citationService.findUploadedCitationList(timestamp, request);
	}
	
	/**
	 * <b> Citation实体添加</b>
	 * <p> Citation实体添加</p>
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	@PostMapping(value="/add/{id}")
	public JSON addCitation(HttpServletRequest request, @PathVariable String id) {
		return this.citationService.addCitation(id, request);
	}
	
	/**
	 * <b> Citation实体修改</b>
	 * <p> Citation实体修改</p>
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value="/edit/{id}")
	public JSON editCitation(@PathVariable String id) {
		return this.citationService.editCitations(id);
	}
	
	/**
	 * <b> 根据Id批量逻辑删除指定Citation</b>
	 * <p> 根据Id批量逻辑删除指定Citation</p>
	 * @param request
	 * @return 
	 */
	@RequestMapping(value = "/removeMany/{ids}", method = RequestMethod.GET)
	public int removeMany(@PathVariable String ids) {
		try {
			String[] idArr = ids.split("￥");
			int isRemove = 0;
			for (String id : idArr) {
				if (this.citationService.logicRemove(id)) {
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
	 * <b> 根据Id单个逻辑删除指定Citation</b>
	 * <p> 根据Id单个逻辑删除指定Citation</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public boolean remove(@PathVariable String id) {
		try {
			return this.citationService.logicRemove(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
     *<b>Citation信息添加后的删除</b>
     *<p> Citation信息添加后的删除</p>
     * @author BINZI
     * @param request 页面请求
     * @return 
     */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public boolean delete(HttpServletRequest request){
		return this.citationService.deleteOne(request);
	}
	
	/**
     *<b>根据TaxonId查找对应Taxon下的所有Citation</b>
     *<p> 根据TaxonId查找对应Taxon下的所有Citation</p>
     * @author BINZI
     * @param request 页面请求
     * @return 
     */
	@GetMapping("/citationList/{id}")
	public JSON citationList(HttpServletRequest request,@PathVariable String id) {
		return this.citationService.findCitationListByTaxonId(id, request);
	}
	
	/**
	 * <b>解析导入的Citation相关的Excel文件</b>
	 * <p> 解析导入的Citation相关的Excel文件</p>
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/upload/{taxasetId}")
    public JSON upload(@RequestParam("uploadCitation") MultipartFile file, @PathVariable String taxasetId, HttpServletRequest request) throws Exception{
		return this.citationService.uploadFile(file, taxasetId, request);
	}
	
}
