package org.big.controller.rest;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.big.entity.Datasource;
import org.big.service.DatasourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
 *<p><b>Datasource相关的Controller的Rest风格类</b></p>
 *<p> Datasource相关的Controller的Rest风格类</p>
 * @author BINZI
 *<p>Created date: 2018/06/11 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController
@RequestMapping(value = "/console/datasource/rest")
public class DatasourceRestController {
	@Autowired
	private DatasourceService datasourceService;
	
	/**
     *<b>Datasource的index页面</b>
     *<p> Datasource的index页面</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JSON list(HttpServletRequest request) {
		return this.datasourceService.findDatasourceList(request);
	}
	
	/**
	 * <b> 根据Id批量逻辑删除指定Datasource</b>
	 * <p> 根据Id批量逻辑删除指定Datasource</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/removeMany/{ids}", method = RequestMethod.GET)
	public int removeMany(@PathVariable String ids) {
		try {
			String[] idArr = ids.split("￥");
			int isRemove = 0;
			for (String id : idArr) {
				System.out.println(id);
				if (this.datasourceService.logicRemove(id)) {
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
	 * <b> 根据Id单个逻辑删除指定Datasource</b>
	 * <p> 根据Id单个逻辑删除指定Datasource</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public boolean remove(@PathVariable String id) {
		try {
			return this.datasourceService.logicRemove(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
     *<b>Datasource的select列表</b>
     *<p> 当前Dataset下的Datasource的select检索列表</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public JSON select(HttpServletRequest request) {
		return this.datasourceService.findBySelect(request);
	}
	
	/**
     *<b>Datasource的select列表之新建Datasource实体</b>
     *<p> Datasource的select列表之新建Datasource实体</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public JSON New(@ModelAttribute("thisDatasource") @Valid Datasource thisDatasource, BindingResult result, Model model, HttpServletRequest request) {
		return this.datasourceService.newOne(thisDatasource, request);
	}
	
	/**
	 * <b>Taxon下的实体的数据源编辑</b>
	 * <p>Taxon下的实体的数据源编辑</p>
	 * @param id
	 * @return
	 */
	@GetMapping(value="/editSource/{id}")
	public JSON editSource(@PathVariable String id) {
		JSONObject thisResult = new JSONObject();
		try {
			thisResult.put("text", this.datasourceService.findOneById(id).getTitle());
		} catch (Exception e) {
		}
		return thisResult;
	}
	
	/**
	 * <b>解析导入的Datasources相关的Excel文件</b>
	 * <p> 解析导入的Datasources相关的Excel文件</p>
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/upload")
    public JSON upload(@RequestParam("uploadDatasource") MultipartFile file, HttpServletRequest request) throws Exception{
		return this.datasourceService.uploadFile(file, request);
	}
	
	/**
	 *<b>Datasource的已上传listJSON</b>
	 *<p> Datasource的已上传listJSON</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value = "/uploaded/{dsFileMark}")
	public JSON list(HttpServletRequest request, @PathVariable String dsFileMark) {
		return this.datasourceService.findUploadedDatasourceList(dsFileMark,request);
	}
}
