package org.big.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.big.entity.Dataset;
import org.big.entity.Taxaset;
import org.big.entity.UserDetail;
import org.big.service.TaxasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@RestController  //返回json
@Controller
@RequestMapping("/console/taxaset/rest")
public class TaxasetRestController {
	@Autowired
	private TaxasetService taxasetService;
	
	/**
	 * <b> Taxaset的Index页面分页列表查询</b>
	 * <p> Taxaset的Index页面分页列表查询</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JSON list(HttpServletRequest request) {
		return this.taxasetService.findTaxasetList(request);
	}
	
	/**
	 * <b> 根据Id批量逻辑删除指定Taxaset</b>
	 * <p> 根据Id批量逻辑删除指定Taxaset</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/removeMany/{ids}", method = RequestMethod.GET)
	public int removeMany(@PathVariable String ids) {
		try {
			String[] idArr = ids.split("￥");
			int isRemove = 0;
			for (String id : idArr) {
				if (this.taxasetService.logicRemove(id)) {
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
	 * <b> 根据Id单个逻辑删除指定Taxaset</b>
	 * <p> 根据Id单个逻辑删除指定Taxaset</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public boolean remove(@PathVariable String id) {
		try {
			return this.taxasetService.logicRemove(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 *<b>Taxaset的select列表</b>
	 *<p> 当前用户的Taxaset的select检索列表</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public JSON select(HttpServletRequest request){
		return this.taxasetService.findBySelect(request);
	}

	/**
	 *<b>Taxaset的select列表(含新建)</b>
	 *<p> 当前用户的Taxaset的select检索列表(含新建)</p>
	 * @author WangTianshan (王天山)
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	@RequestMapping(value = "/selectAndNew", method = RequestMethod.GET)
	public JSON selectAndNew(HttpServletRequest request){
		return this.taxasetService.findBySelectAndNew(request);
	}
	
    /**
     *<b>Taxaset的select列表之新建Taxaset实体</b>
     *<p> Taxaset的select列表之新建Taxaset实体</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public JSON New(@ModelAttribute("thisTaxaset") @Valid Taxaset thisTaxaset, BindingResult result, Model model, HttpServletRequest request) {
		return this.taxasetService.newOne(thisTaxaset, request);
	}
	
	/**
	 *<b>根据DatasetId查找所属Taxaset（分页）</b>
	 *<p> 根据DatasetId查找所属Taxaset（分页）</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @return
	 */
	@GetMapping(value = "/taxasetList/{dsid}")
	public JSON taxasetList(Model model, @PathVariable String dsid, HttpServletRequest request) {
		return this.taxasetService.findTaxasetListByDatasetId(dsid, request);
	}
	
	/**
     * <b>是否有删除Taxaset权限</b>
     * <p> 是否有删除Taxaset权限</p>
	 * @author BINZI
	 * @param request
	 * @param id
	 * @return
	 */
    @GetMapping("/canRemoveObj/{id}")
    public JSON canRemoveObj(HttpServletRequest request, @PathVariable String id) {
    	JSONObject thisResult = new JSONObject();
    	try {
    		Dataset thisDataset = this.taxasetService.findOneById(id).getDataset();
    		String leader = thisDataset.getTeam().getLeader();
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (thisUser.getId().equals(leader)) {
				thisResult.put("rsl", true);
			}else {
				thisResult.put("rsl", false);
			}
		} catch (Exception e) {
		}
    	return thisResult;
	}
    /**
     *<b>Title重复</b>
     *<p> Title重复</p>
     * @author BINZI
     * @param request 页面请求
     * @return Boolean
     */
    @RequestMapping(value="/isReDsname", method = {RequestMethod.GET})
    public Boolean isReTitle(HttpServletRequest request) {
    	return taxasetService.countTaxasetsByTsname(request);
    }
}
