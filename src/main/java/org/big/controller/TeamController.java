package org.big.controller;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.big.entity.Message;
import org.big.entity.Team;
import org.big.entity.User;
import org.big.entity.UserDetail;
import org.big.service.DatasetService;
import org.big.service.TeamService;
import org.big.service.UserService;
import org.big.service.UserTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *<p><b>Team相关的Controller类</b></p>
 *<p> Team相关的Controller</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/12 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/team")
public class TeamController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserTeamService userTeamService;
    @Autowired
    private DatasetService datasetService;

    /**
     *<b>默认页面</b>
     *<p> 展示和自己有关团队的列表和操作选项</p>
     * @author WangTianshan (王天山)
     * @param
     * @return java.lang.String
     */
    @RequestMapping(value="", method = {RequestMethod.GET})
    public String Index(Model model) {
    	Message thisMessage = new Message();
    	thisMessage.setStatus(0);
    	model.addAttribute("thisMessage", thisMessage);
        return "team/myTeam";
    }
    
    /**
     *<b>默认页面</b>
     *<p> 展示和自己有关团队的列表和操作选项</p>
     * @author MengMeng (王孟豪)
     * @param
     * @return java.lang.String
     */
    @RequestMapping(value="/permission", method = {RequestMethod.GET})
    public String PermissionIndex(Model model) {
    	Message thisMessage = new Message();
    	thisMessage.setStatus(0);
    	model.addAttribute("thisMessage", thisMessage);
    	model.addAttribute("Permission", "您没有此权限");
        return "team/myTeam";
    }

    /**
     *<b>查看团队详情</b>
     *<p> 查看团队的详情</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @param id 实体的
     * @return java.lang.String
     */
    @RequestMapping(value="/details/{id}", method = {RequestMethod.GET})
    public String TeamDetails(Model model,@PathVariable String id, HttpServletRequest request) {
    	this.teamService.handleTeamSession(id, request);
    	
    	Team thisTeam=this.teamService.findbyID(id);
        int members=this.teamService.countMembersByTeamId(id);
        User leader=this.userService.findbyID(thisTeam.getLeader());
        model.addAttribute("thisTeam", thisTeam);
        model.addAttribute("members", members);
        model.addAttribute("leader", leader);
        model.addAttribute("countDataset", this.datasetService.countDatasetByTeam_IdAndStatus(id,1));
        return "team/details";
    }
    
    /**
     *<b>登录之后的团队新建</b>
     *<p> 登录之后的团队新建</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
    @RequestMapping(value="/add/mark", method = {RequestMethod.GET})
    public String Adds(Model model, HttpServletRequest request) {
        Team thisTeam=new Team();
        model.addAttribute("thisTeam", thisTeam);
        return "team/addModal";
    }
    
    /**
     *<b>添加</b>
     *<p> 添加新的实体的编辑的页面</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @return java.lang.String
     */
    @RequestMapping(value="/add", method = {RequestMethod.GET})
    public String Add(Model model, HttpServletRequest request) {
        Team thisTeam=new Team();
        thisTeam.setAdddate(new Timestamp(System.currentTimeMillis()));
        model.addAttribute("thisTeam", thisTeam);
        return "team/add";
    }

    /**
     *<b>编辑</b>
     *<p> 对已有的实体进行编辑的页面</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @param id 被编辑实体id
     * @return java.lang.String
     */
    @RequestMapping(value="/edit/{id}", method = {RequestMethod.GET})
    public String Edit(Model model,@PathVariable String id, HttpServletRequest request) {
    	this.teamService.handleTeamSession(id, request);
    	Team thisTeam=this.teamService.findbyID(id);
        model.addAttribute("thisTeam", thisTeam);
        model.addAttribute("editdsname", thisTeam.getName());
        return "team/edit";
    }
    
    /**
     *<b>保存</b>
     *<p> 将传入的实体保存</p>
     * @author WangTianshan (王天山)
     * @param thisTeam 传入的实体id
     * @return java.lang.String
     */
    @RequestMapping(value="/save", method = {RequestMethod.POST})
    public String Save(@ModelAttribute("thisTeam") @Valid Team thisTeam, BindingResult result, Model model, HttpServletRequest request) {
    	if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			String errorMsg = "";
			for (ObjectError error : list) {
				errorMsg = errorMsg + error.getDefaultMessage() + ";";
			}
			model.addAttribute("thisTeam", thisTeam);
			model.addAttribute("errorMsg", errorMsg);
			return "team/edit";
		}
    	// 如果note属性的值是"Default" -- toLowerCase -- "default"
    	if (StringUtils.isNotBlank(thisTeam.getNote()) && "Default".equals(thisTeam.getNote())) {
			thisTeam.setNote(thisTeam.getNote().toLowerCase());
		}
    	this.teamService.saveForUpdate(thisTeam);	// - 有id - 修改
        return "redirect:/select/team";
    }
    
    /**
     *<b>保存</b>
     *<p> 将传入的实体保存</p>
     * @author WangTianshan (王天山)
     * @param thisTeam 传入的实体id
     * @return java.lang.String
     */
    @RequestMapping(value="/new", method = {RequestMethod.POST})
    public String add(@ModelAttribute("thisTeam") @Valid Team thisTeam, BindingResult result, Model model, HttpServletRequest request) {
    	if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			String errorMsg = "";
			for (ObjectError error : list) {
				errorMsg = errorMsg + error.getDefaultMessage() + ";";
			}
			model.addAttribute("thisTeam", thisTeam);
			model.addAttribute("errorMsg", errorMsg);
			return "team/add";
		}
    	this.teamService.saveOneByUser(thisTeam);	// - 无id - 新建
        return "redirect:/select/team";
    }

    /**
     *<b>删除</b>
     *<p> 将传入的实体删除</p>
     * @author WangTianshan (王天山)
     * @param id 传入的实体id
     * @return java.lang.String      
     */
    @RequestMapping(value="/remove/{id}", method = {RequestMethod.GET})
    public String Remove(@PathVariable String id) {
        this.teamService.removeOneByUser(id);
        return "index";
    }
    
    /** 处理用户邀请连接
     *<b>团队邀请</b>
     *<p> 团队邀请通过UserId & TeamId</p>
     * @author BINZI
     * @param id 传入的实体id
     * @return java.lang.String 
     */
    @RequestMapping(value="/invite/{userName}/{teamid}", method = {RequestMethod.GET})
    public String Invite(@PathVariable String userName, @PathVariable String teamid) {
    	// 用户接收邀请
    	userTeamService.saveOne(userService.findOneByName(userName).getId(), teamid);
    	return "redirect:/console/team";
    }
    
    /**
     *<b>团队邀请</b>
     *<p> 根据选中团队的id发出团队邀请</p>
     * @param id 发出邀请的TeamID
     * @param model 初始化模型
     * @return java.lang.String
     */
    @RequestMapping(value="/compose/{id}", method = {RequestMethod.GET})
    public String Add(@PathVariable String id, Model model, HttpServletRequest request) {
    	this.teamService.handleTeamSession(id, request);
    	
		Message thisMessage = new Message();
        UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
        Team team = this.teamService.findbyID(id);																	
        thisMessage.setSender(thisUser.getId());																	// 邀请人
        thisMessage.setTitle(thisUser.getNickname() + "向您发出团队邀请函");												// 邀请信Title
        thisMessage.setText("邀请您加入" + team.getName() + "团队");													// 邀请信内容
        thisMessage.setTeamid(id);																					// 发出邀请的团队
        thisMessage.setType("invitation");																			// 邮件类型，邀请信
        model.addAttribute("thisMessage", thisMessage);
        return "team/compose";
    }
    /**
     * <b>切换团队div的背景</b>
     * <p>切换团队div的背景</p>
     * @param id
     * @param mark
     * @param model
     * @return
     */
    @GetMapping("/changeBg/{id}/{mark}")
    public String changeBg(@PathVariable String id, @PathVariable String mark, Model model) {
    	model.addAttribute("id", id);
    	model.addAttribute("mark", mark);
    	return "console/changeBg";
	}
}
