package org.big.controller;

import org.big.entity.Team;
import org.big.entity.UserDetail;
import org.big.service.DatasetService;
import org.big.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 *<p><b>IndexController类</b></p>
 *<p> Index的Controller</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
public class IndexController {
    @Autowired
    private TeamService teamService;
    @Autowired
    private DatasetService datasetService;
    /**
     *<b>默认页面</b>
     *<p> 登录后自动跳转的主页面</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return java.lang.String
     */
    @RequestMapping(value="/", method = {RequestMethod.GET})
    public String Index(HttpServletRequest request) {
        try{
            return "redirect:/select/team";
        }catch(Exception e){
        }
        return "redirect:/login";
    }

    /**
     *<b>选择团队</b>
     *<p> 登录后选择团队页面</p>
     * @author WangTianshan (王天山)
     * @return java.lang.String
     */
    @RequestMapping(value="/select/team", method = {RequestMethod.GET})
    public String SelectTeam(Model model, HttpServletRequest request) {
        try{
        	//删除team中的记录
			
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Team> teamList = this.teamService.selectTeamsByUserId(thisUser.getId());// 根据userid查找所有team
			model.addAttribute("teamList", teamList);
			return "team/select";
        }catch(Exception e){
        }
        return "redirect:/login";
    }

    /**
     *<b>变换团队</b>
     *<p> 选择团队后变换团队</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return java.lang.String
     */
    @RequestMapping(value="/change/team/{teamId}", method = {RequestMethod.GET})
    public String ChangeTeam(Model model, HttpServletRequest request, @PathVariable String teamId) {
    	this.teamService.handleTeamSession(teamId, request);
		return "redirect:/console/" + teamId;
    }
    
    /**
     *<b>变更团队下的数据集</b>
     *<p> 变更团队下的数据集</p>
     * @author WangTianshan (王天山)
     * @return java.lang.String
     */
    @RequestMapping(value="/select/dataset", method = {RequestMethod.GET})
    public String SelectDataset(HttpServletRequest request) {
        try{
        	String teamId = (String) request.getSession().getAttribute("teamId");	// 后台从Session中取TeamId
            return "redirect:/console/" + teamId;
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        return "redirect:/select/team";
    }
    
    /**
     *<b>变换团队下的Dataset</b>
     *<p> 选择团队数据集后变换团队下的数据集</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return java.lang.String
     */
    @RequestMapping(value="/change/dataset/{id}", method = {RequestMethod.GET})
    public String ChangeTeamDataset(HttpServletRequest request, @PathVariable String id) {
    	request.getSession().setAttribute("datasetID", id);	// 将datasetId存放到Session中
        request.getSession().setAttribute("datasetName", this.datasetService.findbyID(id).getDsname());
        request.getSession().setAttribute("dataset", this.datasetService.findbyID(id));
    	return "redirect:/console/dataset/show/" + id;
    }
    
}
