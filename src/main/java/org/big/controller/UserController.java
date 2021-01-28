package org.big.controller;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.User;
import org.big.entity.UserDetail;
import org.big.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *<p><b>User相关的Controller类</b></p>
 *<p> User相关的Controller</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/12 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
	 *<b>用户手册显示</b>
	 *<p>用户手册显示</p>
	 *@param request 页面请求
	 *@param model 初始化模型
	 *@return java.lang.String
	 */
    @RequestMapping(value = "/manual")
	public String UserManual(HttpServletRequest request, Model model) {
		return "user/manual";
	}

    /**
     *<b>添加</b>
     *<p> 添加新的实体的编辑的页面</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @return java.lang.String
     */
    @RequestMapping(value="/add", method = {RequestMethod.GET})
    public String Add(Model model) {
        User thisUser=new User();
        thisUser.setRole("ROLE_USER");
        model.addAttribute("thisUser", thisUser);
        return "user/add";
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
    public String Edit(Model model,@PathVariable String id) {
        User thisUser=this.userService.findbyID(id);
        model.addAttribute("thisUser", thisUser);
        return "user/edit";
    }

    /**
     *<b>保存</b>
     *<p> 将传入的实体保存</p>
     * @author WangTianshan (王天山)
     * @param thisUser 传入的实体id
     * @return java.lang.String
     */
    @RequestMapping(value="/save", method = {RequestMethod.POST})
    public String Save(@ModelAttribute("thisUser") User thisUser) {
        this.userService.editUserBySuoer(thisUser);
        return "redirect:/user";
    }
    
    /**
     *<b>我的数据中心</b>
     *<p> 我的数据中心</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
    @RequestMapping(value="/info", method = {RequestMethod.GET})
    public String Info(Model model) {
        //获取当前登录用户
        model.addAttribute("thisUser", (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "user/info";
    }

    /**
     *<b>我的个人中心</b>
     *<p> 我的个人中心</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
    @RequestMapping(value="/myinfo", method = {RequestMethod.GET})
    public String MyInfo(Model model) {
        //获取当前登录用户
        User thisUser=(UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("thisUser",this.userService.findbyID(thisUser.getId()));
        return "user/myinfo";
    }
    
    /**
     *<b>Taxon的选择页面Select下拉选(权限转移)</b>
     *<p> Taxon的选择页面Select下拉选(权限转移)</p>
     * @author BINZI
     * @param model 初始化模型
     * @return java.lang.String
     */
	@GetMapping(value = "/select/{tid}")
	public String toSelectUser(Model model, @PathVariable String tid) {
		model.addAttribute("tid", tid);
		return "team/selectUser";
	}
    
}
