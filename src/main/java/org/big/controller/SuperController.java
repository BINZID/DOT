package org.big.controller;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.Valid;

import org.big.common.MD5Utils;
import org.big.common.UUIDUtils;
import org.big.entity.Dataset;
import org.big.entity.Message;
import org.big.entity.Team;
import org.big.entity.User;
import org.big.service.DatasetService;
import org.big.service.MessageService;
import org.big.service.TeamService;
import org.big.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 *<p><b>超级管理员相关的Controller类</b></p>
 *<p> 超级管理员相关的Controller</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/12 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/super")
public class SuperController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private DatasetService datasetService;
    /********************** Team管理页 ***************************/
    /**
     *<b>Team管理页</b>
     *<p> 包含所有Team的信息列表、操作选项</p>
     * @author WangTianshan (王天山)
     * @param
     * @return java.lang.String
     */
    @RequestMapping(value="/team", method = {RequestMethod.GET})
    public String ViewTeam() {
        return "team/index_admin";
    }

    /**
     *<b>添加Team</b>
     *<p> 添加新的Team的编辑的页面</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @return java.lang.String
     */
    @RequestMapping(value="/team/add", method = {RequestMethod.GET})
    public String AddTeam(Model model) {
        Team thisTeam=new Team();
        model.addAttribute("thisTeam", thisTeam);
        return "team/add";
    }

    /**
     *<b>编辑Team</b>
     *<p> 对已有的Team进行编辑的页面</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @param id 被编辑Team实体id
     * @return java.lang.String
     */
    @RequestMapping(value="/team/edit/{id}", method = {RequestMethod.GET})
    public String EditTeam(Model model,@PathVariable String id) {
        Team thisTeam=this.teamService.findbyID(id);
        model.addAttribute("thisTeam", thisTeam);
        return "team/edit";
    }

    /**
     *<b>保存Team</b>
     *<p> 将传入的Team实体保存</p>
     * @author WangTianshan (王天山)
     * @param thisTeam 传入的Team实体
     * @return java.lang.String
     */
    @RequestMapping(value="/team/save", method = {RequestMethod.POST})
    public String SaveTeam(@ModelAttribute("thisTeam") Team thisTeam) {
        this.teamService.saveOne(thisTeam);
        return "redirect:/super/team";
    }

    /**
     *<b>删除Team</b>
     *<p> 将传入的Team实体删除</p>
     * @author WangTianshan (王天山)
     * @param id 传入的Team实体的id
     * @return java.lang.String
     */
    @RequestMapping(value="/team/remove/{id}", method = {RequestMethod.GET})
    public String RemoveTeam(@PathVariable String id) {
        this.teamService.removeOne(id);
        return "index";
    }
    
    /********************** User管理页 ***************************/
    /**
     *<b>User管理页</b>
     *<p> 包含所有User的信息列表、操作选项</p>
     * @author WangTianshan (王天山)
     * @param
     * @return java.lang.String
     */
    @RequestMapping(value="/user", method = {RequestMethod.GET})
    public String ViewUser() {
        return "user/index_admin";
    }

    /**
     *<b>添加User</b>
     *<p> 添加新的User的编辑的页面</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @return java.lang.String
     */
    @RequestMapping(value="/user/add", method = {RequestMethod.GET})
    public String AddUser(Model model) {
        User thisUser = new User();
        thisUser.setId(UUIDUtils.getUUID32());
    	thisUser.setAdddate(new Timestamp(System.currentTimeMillis()));
        thisUser.setRole("ROLE_USER");
        
        model.addAttribute("thisUser", thisUser);
        return "user/add";
    }

    /**
     *<b>编辑User</b>
     *<p> 对已有的User进行编辑的页面</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @param id 被编辑User实体id
     * @return java.lang.String
     */
    @RequestMapping(value="/user/edit/{id}", method = {RequestMethod.GET})
    public String EditUser(Model model,@PathVariable String id) {
        User thisUser=this.userService.findbyID(id);
        model.addAttribute("thisUser", thisUser);
        return "user/edit";
    }

    /**
     *<b>保存User</b>
     *<p> 将传入的User实体保存</p>
     * @author WangTianshan (王天山)
     * @param thisUser 传入的User实体
     * @return java.lang.String
     */
    @RequestMapping(value="/user/addUserBySuper", method = {RequestMethod.POST})
    public String addUserBySuoer(@ModelAttribute("thisUser") User thisUser) {
    	thisUser.setPassword(MD5Utils.MD532(thisUser.getPassword()));
        this.userService.addUserBySuper(thisUser);
        return "redirect:/super/user";
    }
    
    /**
     *<b>保存User</b>
     *<p> 将传入的User实体保存</p>
     * @author WangTianshan (王天山)
     * @param thisUser 传入的User实体
     * @return java.lang.String
     */
    @RequestMapping(value="/user/editUserBySuoer", method = {RequestMethod.POST})
    public String editUserBySuoer(@ModelAttribute("thisUser") User thisUser) {
    	thisUser.setPassword(MD5Utils.MD532(thisUser.getPassword()));
        this.userService.editUserBySuoer(thisUser);
        return "redirect:/super/user";
    }
    
    /**
     *<b>删除User</b>
     *<p> 将传入的User实体删除</p>
     * @author WangTianshan (王天山)
     * @param id 传入的User实体的id
     * @return java.lang.String
     */
    @RequestMapping(value="/user/remove/{id}", method = {RequestMethod.GET})
    public String RemoveUser(@PathVariable String id) {
        this.userService.removeOne(id);
        return "index";
    }

    /********************** Message管理页 ***************************/
    /**
     *<b>Message管理页</b>
     *<p> 包含所有Message的信息列表、操作选项</p>
     * @author WangTianshan (王天山)
     * @param
     * @return java.lang.String
     */
    @RequestMapping(value="/message", method = {RequestMethod.GET})
    public String ViewMessage() {
        return "message/index_admin";
    }

    /**
     *<b>查看Message详情</b>
     *<p> 查看所有消息的详情，不能变更消息状态</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @param id 实体的id
     * @return java.lang.String
     */
    @RequestMapping(value="/message/read/{id}", method = {RequestMethod.GET})
    public String ReadMessage(Model model,@PathVariable String id) {
        Message thisMessage=this.messageService.findbyID(id);
        User thisSender=this.userService.findbyID(thisMessage.getSender());
        model.addAttribute("thisMessage", thisMessage);
        model.addAttribute("thisSender", "From:"+thisSender.getNickname());
        return "message/read_admin";
    }
    /********************** Dataset管理页 ***************************/
    /**
     *<b>User管理页</b>
     *<p> 包含所有User的信息列表、操作选项</p>
     * @author WangTianshan (王天山)
     * @param
     * @return java.lang.String
     */
    @RequestMapping(value="/dataset", method = RequestMethod.GET)
    public String ViewDataset() {
        return "dataset/index_admin";
    }
    
    /**
     *<b>添加Dataset</b>
     *<p> 添加新的Dataset的编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@RequestMapping(value = "/dataset/add", method = RequestMethod.GET)
	public String Add(Model model) {
		Dataset thisDataset = new Dataset();
		model.addAttribute("thisDataset", thisDataset);
		System.out.println("无Team");
		return "dataset/add";
	}
    
    /**
     *<b>在指定用户组下添加Dataset</b>
     *<p> 添加新的Dataset的编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */ 
    // /super/dataset/add/TeamID
	@RequestMapping(value = "/dataset/add/{id}", method = { RequestMethod.GET })
	public String AddDatasetForTeam(Model model, @PathVariable String id) {
		Dataset thisDataset = new Dataset();
		thisDataset.setTeam(this.teamService.findbyID(id));
		model.addAttribute("thisDataset", thisDataset);
		return "dataset/add";
	}
	
	/**
     *<b>编辑Dataset</b>
     *<p> 对已有的Dataset进行编辑的页面</p>
     * @author BINZI
     * @param model 初始化模型
     * @param id 被编辑Dataset实体id
     * @return java.lang.String
     */
    @RequestMapping(value="/dataset/edit/{id}", method = {RequestMethod.GET})
    public String EditDataset(Model model, @PathVariable String id) {
    	Dataset thisDataset = this.datasetService.findbyID(id);
		model.addAttribute("thisDataset", thisDataset);
		return "dataset/edit";
    }

    /**
     *<b>保存Dataset</b>
     *<p> 将传入的Dataset实体保存</p>
     * @author BINZI
     * @param thisDataset 传入的Dataset实体
     * @return java.lang.String
     */
	@RequestMapping(value = "/dataset/save", method = { RequestMethod.POST })
	public String Save(@ModelAttribute("thisDataset") @Valid Dataset thisDataset, BindingResult result, Model model) {
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			String errorMsg = "";
			for (ObjectError error : list) {
				errorMsg = errorMsg + error.getDefaultMessage() + ";";
			}
			model.addAttribute("thisDataset", thisDataset);
			model.addAttribute("errorMsg", errorMsg);
			return "dataset/edit";
		}
		this.datasetService.saveOne(thisDataset);
		return "redirect:/console/dataset";
	}

}
