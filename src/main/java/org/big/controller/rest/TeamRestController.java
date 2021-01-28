package org.big.controller.rest;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.big.entity.Team;
import org.big.entity.UserDetail;
import org.big.service.TeamService;
import org.big.service.UserService;
import org.big.service.UserTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 *<p><b>TeamController的Rest风格类</b></p>
 *<p> TeamController的Rest风格类</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/console/team/rest")
public class TeamRestController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserTeamService userTeamService;
    /**
     *<b>列表</b>
     *<p> 当前用户所能查看权限的列表</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    @RequestMapping("/list")
    public JSON List(HttpServletRequest request) {
        return this.teamService.findbyUser(request);
    }

    /**
     *<b>删除多个</b>
     *<p> 根据id序列一次性删除多个</p>
     * @author WangTianshan (王天山)
     * @param ids id序列，用"￥"分隔
     * @return boolean
     */
    @RequestMapping(value="/removeMany/{ids}",method = {RequestMethod.GET})
    public boolean RemoveMany(@PathVariable String ids) {
    	try {
			//获取id列表字符串
			String [] idList = ids.split("￥");
			for (String id : idList) {
				return this.teamService.removeOne(id);
			}
		} catch (Exception e) {
			return false;
		}
    	return false;
    }

    /**
     *<b>删除单个</b>
     *<p> 根据id删除单个</p>
     * @author WangTianshan (王天山)
     * @param id 实体id
     * @return boolean
     */
    @RequestMapping(value="/remove/{id}",method = {RequestMethod.GET})
    public boolean Remove(@PathVariable String id,HttpServletRequest request) {
        try{
            request.getSession().setAttribute("operationError","");
            if(request.getSession().getAttribute("operationError").equals("authority")){
            	request.getSession().setAttribute("operationError","");
            }
            return this.teamService.removeOneByUser(id);
        }catch(Exception e){
            return false;
        }
    }

    /**
     *<b>Team成员列表</b>
     *<p> 当前用户所能查看权限的Team成员列表</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    @RequestMapping("/memberList")
    public JSON MemberList(HttpServletRequest request) {
        return this.userService.findbyTeamId(request);
    }

    /**
     *<b>删除单个</b>
     *<p> 根据id删除单个</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return boolean
     */
    @RequestMapping(value="/delMember",method = {RequestMethod.POST})
    public boolean delMember(HttpServletRequest request) {
    	return this.teamService.removeMembersByTeamIdAndUserId(request);
    }

    /**
     *<b>团队负责人授权</b>
     *<p> 团队负责人授权</p>
     * @author BINZI
     * @param request 页面请求
     * @return boolean
     */
    @RequestMapping(value="/transMember",method = {RequestMethod.POST})
    public boolean TransMember(HttpServletRequest request) {
        try{
            this.teamService.updateTeamInfoByLeader(request);
            this.userTeamService.SendEmailTransPermissionAdvice(request);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    /**
     *<b>Team的新建Team实体</b>
     *<p> Team的新建Team实体</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	@RequestMapping(value = "/addNew", method = RequestMethod.POST)
	public JSON New(Model model, @ModelAttribute("thisTeam") Team thisTeam, HttpServletRequest request) {
		if (StringUtils.isNotBlank(thisTeam.getNote()) && "Default".equals(thisTeam.getNote())) {
			thisTeam.setNote(thisTeam.getNote().toLowerCase());
		}
		return this.teamService.newOne(thisTeam, request);
	}

    /**
     *<b>根据uid查找team，用户是所在Team的leader</b>
     *<p> 根据uid查找team，用户是所在Team的leader</p>
     * @author BINZI
     * @param request 页面请求
     * @return
     */
    @GetMapping("/myTeamList")
    public JSON myTeamList(HttpServletRequest request) {
        UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.teamService.findMyTeamsByUserId(thisUser.getId(), request);
    }
    
    /**
     *<b>根据uid查找team，用户不是所在Team的leader</b>
     *<p> 根据uid查找team，用户不是所在Team的leader</p>
     * @author BINZI
     * @param request 页面请求
     * @return
     */
    @GetMapping("/myJoinTeamList")
    public JSON myJoinTeamList(HttpServletRequest request) {
        UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.teamService.findMyJoinTeamsByUserId(thisUser.getId(), request);
    }
    /**
     * <b>是否有授权权限</b>
     * <p> 是否有授权权限</p>
     * @param request
     * @return
     */
    @GetMapping("/canTransMember/{tid}")
    public JSON canTransMember(HttpServletRequest request, @PathVariable String tid) {
    	JSONObject thisResult = new JSONObject();
    	try {
			Team thisTeam = this.teamService.findbyID(tid);
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (thisUser.getId().equals(thisTeam.getLeader())) {
				thisResult.put("rsl", true);
			}else {
				thisResult.put("rsl", false);
			}
		} catch (Exception e) {
		}
    	return thisResult;
	}
    
    /**
     * <b>是否有邀请权限</b>
     * <p> 是否有邀请权限</p>
     * @param request
     * @return
     */
    @GetMapping("/canInviteUser/{tid}")
    public JSON canInviteUser(HttpServletRequest request, @PathVariable String tid) {
    	JSONObject thisResult = new JSONObject();
    	try {
			Team thisTeam = this.teamService.findbyID(tid);
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (thisUser.getId().equals(thisTeam.getLeader())) {
				thisResult.put("rsl", true);
			}else {
				thisResult.put("rsl", false);
			}
		} catch (Exception e) {
		}
    	return thisResult;
	}
    
	/**
     * <b>是否有删除Team权限</b>
     * <p> 是否有删除Team权限</p>
	 * @author BINZI
	 * @param request
	 * @param id
	 * @return
	 */
    @GetMapping("/canRemoveObj/{id}")
    public JSON canRemoveObj(HttpServletRequest request, @PathVariable String id) {
    	JSONObject thisResult = new JSONObject();
    	try {
    		Team thisTeam = this.teamService.findbyID(id);
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (thisUser.getId().equals(thisTeam.getLeader())) {
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
    	return teamService.countTeamsByName(request);
    }
}