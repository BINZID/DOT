package org.big.controller.rest;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.big.entity.Traitontology;
import org.big.service.TraitontologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 *<p><b>Traitontology相关的Controller的Rest风格类</b></p>
 *<p> Traitontology相关的Controller的Rest风格类</p>
 * @author BINZI
 *<p>Created date: 2018/06/22 10:20</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController
@RequestMapping(value = "/console/traitontology/rest")
public class TraitontologyRestController {

	@Autowired
	private TraitontologyService traitontologyService;
	
	/**
	 * <b>Traitontology的Index页面分页列表查询</b>
	 * <p> Traitontology的Index页面分页列表查询</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JSON list(HttpServletRequest request) {
		return this.traitontologyService.findTraitontologyServiceList(request);
	}
	
	
	/**
	 * <b> Traitontology的Select2查询</b>
	 * <p> Traitontology的Select2查询</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public JSON data(HttpServletRequest request) {
		String trainsetid = (String)request.getSession().getAttribute("traitsetSel");
		return this.traitontologyService.findBySelect(request, trainsetid);
	}
	
	/**
	 * <b> 根据Id批量逻辑删除指定Traitontology</b>
	 * <p> 根据Id批量逻辑删除指定Traitontology</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/removeMany/{ids}", method = RequestMethod.GET)
	public int removeMany(@PathVariable String ids) {
		try {
			String[] idArr = ids.split("￥");
			int isRemove = 0;
			for (int i = 0; i < idArr.length; i++) {
				if (this.traitontologyService.logicRemove(idArr[i])) {
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
	 * <b> 根据Id单个逻辑删除指定Traitontology</b>
	 * <p> 根据Id单个逻辑删除指定Traitontology</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public boolean remove(@PathVariable String id) {
		try {
			return this.traitontologyService.logicRemove(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * <b>获取Select2联动选中值，将其存到Session中</b>
	 * <p> 获取Select2联动选中值，将其存到Session中</p>
	 * @param request
	 * @return
	 */
	@GetMapping("/sel")
	public Boolean traitsetSel(HttpServletRequest request) {
		String traitsetSel = request.getParameter("traitsetSel");
		if (StringUtils.isNotBlank(traitsetSel)) {
			request.getSession().setAttribute("traitsetSel", traitsetSel);
			return true;
		}
		return false;
	}
	
	/**
	 * <b>清除Session中Select2联动选中值</b>
	 * <p> 清除Session中Select2联动选中值</p>
	 * @param request
	 * @return
	 */
	@GetMapping("/removeSel")
	public void removeSel(HttpServletRequest request) {
		String traitsetSel = (String) request.getSession().getAttribute("traitsetSel");
		if (StringUtils.isNotBlank(traitsetSel)) {
			request.getSession().removeAttribute("traitsetSel");
		}
	}
	
	/**
	 * <b>Taxon下的特征数据实体的描述编辑</b>
	 * <p>Taxon下的特征数据实体的描述编辑</p>
	 * @param id
	 * @return
	 */
	@GetMapping(value="/editTraitontology/{id}")
	public JSON editTraitontology(@PathVariable String id) {
		JSONObject thisResult = new JSONObject();
		Traitontology thisTraitontology = this.traitontologyService.findOneById(id);
		thisResult.put("text", thisTraitontology.getCnterm() + "(" + thisTraitontology.getEnterm() + ")");
		return thisResult;
	}
}
