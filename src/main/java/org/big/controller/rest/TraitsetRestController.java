package org.big.controller.rest;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.Traitset;
import org.big.service.TraitsetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 *<p><b>Traitset相关的Controller的Rest风格类</b></p>
 *<p> Traitset相关的Controller的Rest风格类</p>
 * @author BINZI
 *<p>Created date: 2018/06/22 10:20</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController
@RequestMapping(value = "/console/traitset/rest")
public class TraitsetRestController {

	@Autowired
	private TraitsetService traitsetService;
	
	/**
	 * <b>Traitset的Index页面分页列表查询</b>
	 * <p> Traitset的Index页面分页列表查询</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JSON list(HttpServletRequest request) {
		return this.traitsetService.findTraitsetList(request);
	}
	
	/**
	 * <b>Traitset的Select2查询</b>
	 * <p> Traitset的Select2查询</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public JSON data(HttpServletRequest request) {
		return this.traitsetService.findBySelect(request);
	}
	
	/**
	 * <b>Traitset的Select2查询</b>
	 * <p> Traitset的Select2查询</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/selectForUpload", method = RequestMethod.GET)
	public JSON selectForUpload(HttpServletRequest request) {
		return this.traitsetService.selectForUpload(request);
	}
	
	/**
	 * <b>Taxon下的特征数据实体的术语集编辑</b>
	 * <p>Taxon下的特征数据实体的术语集编辑</p>
	 * @param id
	 * @return
	 */
	@GetMapping(value="/editTraitset/{id}")
	public JSON editTraitset(@PathVariable String id) {
		JSONObject thisResult = new JSONObject();
		thisResult.put("text", this.traitsetService.findOneById(id).getName());
		return thisResult;
	}
	
    /**
     *<b>下拉选新建术语集</b>
     *<p> 下拉选新建术语集</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	@RequestMapping(value = "/addNew", method = RequestMethod.POST)
	public JSON New(Model model, @ModelAttribute("thisTraitset") Traitset thisTraitset, HttpServletRequest request) {
		return this.traitsetService.saveOne(thisTraitset);
	}
	
    /**
     *<b>Title重复</b>
     *<p> Title重复</p>
     * @author BINZI
     * @param request 页面请求
     * @return Boolean
     */
    @RequestMapping(value="/isRename", method = {RequestMethod.GET})
    public Boolean isReTitle(HttpServletRequest request) {
    	return traitsetService.countTraitsetsByName(request);
    }
}
