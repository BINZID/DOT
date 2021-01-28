package org.big.controller.rest;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.big.entity.Dataset;
import org.big.entity.UserDetail;
import org.big.service.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 *<p><b>Dataset相关的Controller的Rest风格类</b></p>
 *<p> Dataset相关的Controller的Rest风格类</p>
 * @author BINZI
 *<p>Created date: 2018/06/11 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/console/dataset/rest")
public class DatasetRestController {
	@Autowired
    private DatasetService datasetService;

    /**
     *<b>根据teamId查询对应Team下的Dataset列表</b>
     *<p> 根据teamId查询对应Team下的Dataset列表</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    @RequestMapping("/list")
    public JSON DataSetTeamList(HttpServletRequest request) {
    	return this.datasetService.findMyTeamDatasetsByTid(request);
    }
    
    /**
     *<b>删除多个Dataset</b>
     *<p> 根据Dataset id序列一次性删除多个</p>
     * @author BINZI
     * @param ids Media id序列，用"￥"分隔
     * @return boolean
     */
	@RequestMapping(value = "/removeMany/{ids}", method = RequestMethod.GET)
	public int RemoveManyDataset(@PathVariable String ids) {
		try {
			// 获取id列表字符串
			String[] idList = ids.split("￥");
			int isRemove = 0;
			for (int i = 0; i < idList.length; i++) {
				if (this.datasetService.logicRemove(idList[i])){
					isRemove = isRemove + 1;
				}
			}
			return isRemove;
		} catch (Exception e) {
			return -1;
		}
	}
    
    /**
     *<b>删除单个Dataset</b>
     *<p> 根据Dataset id删除单个</p>
     * @author BINZI
     * @param id Media id
     * @return boolean
     */
    @RequestMapping(value="/remove/{id}", method = RequestMethod.GET)
    public boolean RemoveDataset(@PathVariable String id) {
    	try{
            return this.datasetService.logicRemove(id);
        }catch(Exception e){
            return false;
        }
    }

    /**
     *<b>选择数据集页面新建数据集</b>
     *<p> 选择数据集页面新建数据集</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	@RequestMapping(value = "/addNew", method = RequestMethod.POST)
	public JSON New(Model model, @ModelAttribute("thisDataset") Dataset thisDataset, HttpServletRequest request) {
		if (StringUtils.isNotBlank(thisDataset.getDsabstract()) && "Default".equals(thisDataset.getDsabstract())) {
			thisDataset.setDsabstract(thisDataset.getDsabstract().toLowerCase());
		}
		return this.datasetService.newOne(thisDataset, request);
	}

	/**
	 *<b>根据TeamId查找所属Dataset（分页）</b>
	 *<p> 根据TeamId查找所属Dataset（分页）</p>
	 * @author WangTianshan(王天山)
	 * @param request 页面请求
	 * @return
	 */
	@GetMapping("/datasetList")
	public JSON DatasetList(HttpServletRequest request) {
		String teamId = request.getSession().getAttribute("teamId").toString();	// 获取当前Team
		return this.datasetService.findDatasetListByTeamId(teamId, request);
	}
	
	/**
     * <b>是否有删除数据集权限</b>
     * <p> 是否有删除数据集权限</p>
	 * @author BINZI
	 * @param request
	 * @param dsid
	 * @return
	 */
    @GetMapping("/canRemoveObj/{dsid}")
    public JSON canRemoveObj(HttpServletRequest request, @PathVariable String dsid) {
    	JSONObject thisResult = new JSONObject();
    	try {
    		String leader = this.datasetService.findbyID(dsid).getTeam().getLeader();
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
    	return datasetService.countDatasetsByDsname(request);
    }
}
