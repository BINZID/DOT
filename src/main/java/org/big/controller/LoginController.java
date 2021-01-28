package org.big.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *<p><b>注册Controller类</b></p>
 *<p> 注册相关的Controller</p>
 * @author BINZI
 *<p>Created date: 2018/03/05 11：46</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private HttpServletRequest request;

    /**
     *<b>登录页面</b>
     *<p> 登录页面</p>
     * @author WangTianshan (王天山)
     * @param model 初始化模型
     * @return java.lang.String
     */
    @RequestMapping(value="", method = RequestMethod.GET)
    public String login(Model model) {
        String loginErrorMsg = "";
        try {
            if (request.getSession().getAttribute("loginError").equals("name")) {
                model.addAttribute("loginError", 1);
                loginErrorMsg = "无此用户名";
            } else if (request.getSession().getAttribute("loginError").equals("password")) {
                model.addAttribute("loginError", 1);
                loginErrorMsg = "密码错误";
            } else if (request.getSession().getAttribute("loginError").equals("token")) {
                model.addAttribute("loginError", 1);
                loginErrorMsg = "验证码错误";
            } else if (request.getSession().getAttribute("loginError").equals("status")) {
            	model.addAttribute("loginError", 1);
                loginErrorMsg = "该账号未激活";
			} else {
                model.addAttribute("loginError", 0);
            }
            model.addAttribute("loginErrorMsg", loginErrorMsg);
        } catch (Exception e) {
        }
        return "login";
    }
}
