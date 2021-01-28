package org.big.controller.rest;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.big.service.ProtectionService;
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
 *<p><b>Protection相关的Controller的Rest风格类</b></p>
 *<p> Protection相关的Controller的Rest风格类</p>
 * @author BINZI
 *<p>Created date: 2018/06/14 13:58</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/console/protection/rest")
public class ProtectionRestController {
	@Autowired
	private ProtectionService protectionService;
	
	/**
	 *<b>Protection的已上传listJSON</b>
	 *<p> Protection的已上传listJSON</p>
	 * @author BINZI
	 * @param request
	 * @param timestamp
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value = "/uploaded/{timestamp}")
	public JSON list(HttpServletRequest request, @PathVariable Timestamp timestamp) {
		return this.protectionService.findUploadedProtectionList(timestamp, request);
	}
	
	/**
	 * <b> Protection实体添加</b>
	 * <p> Protection实体添加</p>
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value="/edit/{id}")
	public JSON editProtection(@PathVariable String id) {
		return this.protectionService.editProtection(id);
	}
	
	/**
	 * <b> Protection实体修改</b>
	 * <p> Protection实体修改</p>
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value="/add/{id}", method = {RequestMethod.POST})
	public JSON addCitation(HttpServletRequest request, @PathVariable String id) {
		return this.protectionService.addProtection(id, request);
	}
	
	/**
	 * <b> 根据Id批量逻辑删除指定Protection</b>
	 * <p> 根据Id批量逻辑删除指定Protection</p>
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/removeMany/{ids}", method = RequestMethod.GET)
	public int removeMany(@PathVariable String ids) {
		try {
			String[] idArr = ids.split("￥");
			int isRemove = 0;
			for (String id : idArr) {
				if (this.protectionService.logicRemove(id)) {
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
	 * <b> 根据Id单个逻辑删除指定Protection</b>
	 * <p> 根据Id单个逻辑删除指定Protection</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public boolean remove(@PathVariable String id) {
		try {
			return this.protectionService.logicRemove(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
     *<b>Protection的select列表(保护标准级别)</b>
     *<p> 当前用户的Protection的select检索列表(保护标准级别)</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public JSON select(HttpServletRequest request){
		return this.protectionService.findBySelect(request);
	}
	
	/**
     *<b>Protection信息添加后的删除</b>
     *<p> Protection信息添加后的删除</p>
     * @author BINZI
     * @param request 页面请求
     * @return 
     */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public boolean delete(HttpServletRequest request){
		return this.protectionService.deleteOne(request);
	}
	
	/**
     *<b>根据TaxonId查找对应Taxon下的所有Protection</b>
     *<p> 根据TaxonId查找对应Taxon下的所有Protection</p>
     * @author BINZI
     * @param request 页面请求
     * @return 
     */
	@GetMapping("/protectionList/{id}")
	public JSON protectionList(HttpServletRequest request,@PathVariable String id) {
		return this.protectionService.findProtectionListByTaxonId(id, request);
	}
	
	/**
	 * <b>解析导入的Protection相关的Excel文件</b>
	 * <p> 解析导入的Protection相关的Excel文件</p>
	 * @param file
	 * @param request
	 * @param taxasetId
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/upload/{taxasetId}")
    public JSON upload(@RequestParam("uploadProtection") MultipartFile file, @PathVariable String taxasetId, HttpServletRequest request) throws Exception{
		return this.protectionService.uploadFile(file, taxasetId, request);
	}
}
