package org.big.controller.rest;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.big.service.TaxkeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

/**
 *<p><b>Taxkey相关的Controller的Rest风格类</b></p>
 *<p> Taxkey相关的Controller的Rest风格类</p>
 * @author BINZI
 *<p>Created date: 2018/06/14 13:58</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/console/taxkey/rest")
public class TaxkeyRestController {
	@Autowired
	private TaxkeyService taxkeyService;
	
	/**
	 * <b> Taxkey的Index页面分页列表查询</b>
	 * <p> Taxkey的Index页面分页列表查询</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JSON list(HttpServletRequest request) {
		return this.taxkeyService.findTaxkeyList(request);
	}
	
	/**
	 * <b> 添加Taxkey实体</b>
	 * <p> 添加Taxkey实体</p>
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value="/add/{id}", method = {RequestMethod.POST})
	public JSON add(HttpServletRequest request, @PathVariable String id) {
		Enumeration<String> paraNames = request.getParameterNames();
		while (paraNames.hasMoreElements()) {
			String paraName = (String) paraNames.nextElement();
			if (paraName.indexOf("keyitemId_") == 0 && StringUtils.isNotBlank(request.getParameter(paraName))) {
				String taxkeyId = (String) request.getSession().getAttribute("taxkeyId");
				return this.taxkeyService.addKeyitem(id, paraNames, request, taxkeyId, request.getParameter(paraName));
			}
		}
		return this.taxkeyService.addTaxkey(id, request);
	}
	
	
	/**
	 * <b> Taxkey实体修改</b>
	 * <p> Taxkey实体修改</p>
	 * @param id
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value="/edit/{id}")
	public JSON editTaxkey(@PathVariable String id) {
		return this.taxkeyService.editTaxkey(id);
	}
	
	/**
	 * <b> Taxkey实体添加(for Keyitem)</b>
	 * <p> Taxkey实体添加(for Keyitem)</p>
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value="/addTaxkey/{id}", method = {RequestMethod.POST})
	public JSON addTaxkey(HttpServletRequest request, @PathVariable String id) {
		return this.taxkeyService.addTaxkeyForKeyitem(request, id);
	}
	
	/**
	 * <b> 根据Id批量逻辑删除指定Taxkey</b>
	 * <p> 根据Id批量逻辑删除指定Taxkey</p>
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/removeMany/{ids}", method = RequestMethod.GET)
	public int removeMany(@PathVariable String ids) {
		try {
			String[] idArr = ids.split("￥");
			int isRemove = 0;
			for (String id : idArr) {
				if (this.taxkeyService.deleteOneById(id)) {
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
	 * <b> 根据Id单个逻辑删除指定Taxkey</b>
	 * <p> 根据Id单个逻辑删除指定Taxkey</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public boolean remove(@PathVariable String id) {
		try {
			return this.taxkeyService.deleteOneById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
     *<b>Taxkey信息添加后的删除</b>
     *<p> Taxkey信息添加后的删除</p>
     * @author BINZI
     * @param request 页面请求
     * @return 
     */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public boolean delete(HttpServletRequest request){
		return this.taxkeyService.deleteOne(request);
	}
	
	/**
     *<b>根据TaxonId查找对应Taxon下的所有Taxkey</b>
     *<p> 根据TaxonId查找对应Taxon下的所有Taxkey</p>
     * @author BINZI
     * @param request 页面请求
     * @return 
     */
	@GetMapping("/taxkeyList/{id}")
	public JSON taxkeyList(HttpServletRequest request,@PathVariable String id) {
		return this.taxkeyService.findTaxkeyListByTaxonId(id, request);
	}
	
	/**
	 * <b>清除Session中taxkeyId值</b>
	 * <p> 清除Session中taxkeyId值</p>
	 * @param request
	 * @return
	 */
	@GetMapping("/removeSession")
	public void removeSel(HttpServletRequest request) {
		String traitsetSel = (String) request.getSession().getAttribute("taxkeyId");
		if (StringUtils.isNotBlank(traitsetSel)) {
			request.getSession().removeAttribute("taxkeyId");
		}
	}
}
