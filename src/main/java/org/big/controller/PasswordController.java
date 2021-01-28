package org.big.controller;

import org.big.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *<p><b>IndexController类</b></p>
 *<p> Index的Controller</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/11/1 10:23</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/password")
public class PasswordController {

    @Autowired
    private UserService userService;
    @Value("${spring.mail.username}") private String fromEmail;
    @Autowired
    private MessageSource messageSource;

    /**
     *<b>忘记密码页</b>
     *<p> 忘记密码页</p>
     * @author WangTianshan (王天山)
     * @param model 空的model
     * @return java.lang.String
     */
    @RequestMapping(value="/forgot", method = {RequestMethod.GET})
    public String Forgot(Model model) {
        return "user/forgot";
    }

    /**
     *<b>忘记 | 重置密码发送邮件</b>
     *<p> 忘记 | 重置密码发送邮件</p>
     * @author WangTianshan (王天山)
     * @return java.lang.String
     */
    @RequestMapping(value="/sendEmail", method = {RequestMethod.POST})
    public String SendEmail(HttpServletRequest request, HttpServletResponse response,Model model) {
        String resultMsg=this.userService.sendPasswordEmail(request,response);
        if(resultMsg.equals("none")){
            request.getSession().setAttribute("adminEmail",fromEmail);
            return "redirect:/password/sendsuccess";
        }
        else if(resultMsg.equals("email")){
            model.addAttribute("errorMsg", messageSource.getMessage("forget_erroremail", null, LocaleContextHolder.getLocale()));
            model.addAttribute("email", request.getParameter("email"));
            model.addAttribute("token", request.getParameter("token"));
            return "user/forgot";
        }
        else if(resultMsg.equals("token")){
            model.addAttribute("errorMsg", messageSource.getMessage("forget_errortoken", null, LocaleContextHolder.getLocale()));
            model.addAttribute("email", request.getParameter("email"));
            model.addAttribute("token", request.getParameter("token"));
            return "user/forgot";
        }
        else{
            model.addAttribute("errorMsg", messageSource.getMessage("forget_resetunsuccessful", null, LocaleContextHolder.getLocale()));
            model.addAttribute("token", request.getParameter("token"));
            model.addAttribute("errorMsg", "error");
            return "user/forgot";
        }
    }

    /**
     *<b>邮件发送成功页面</b>
     *<p> 邮件发送成功页面</p>
     * @author WangTianshan (王天山)
     * @return java.lang.String
     */
    @RequestMapping(value="/sendsuccess")
    public String sendSuccess(Model model,HttpServletRequest request) {
        //model.addAttribute("registerEmail", request.getParameter("email"));
        return "user/sendSuccess";

    }

    /**
     *<b>修改密码页</b>
     *<p> 修改密码页</p>
     * @author WangTianshan (王天山)
     * @return java.lang.String
     */
    @RequestMapping(value="/reset/{username}/{mark}")
    public String active(Model model,HttpServletRequest request, HttpServletResponse response,@PathVariable String username, @PathVariable String mark) {
        if(this.userService.canRestPassword(username,mark)){
            model.addAttribute("username", username);
            model.addAttribute("mark", mark);
            return "user/resetPassword";
        }
        else{
            model.addAttribute("errorMsg", messageSource.getMessage("forget_linkpast", null, LocaleContextHolder.getLocale()));
            return "user/forgot";
        }
    }

    /**
     *<b>改密码</b>
     *<p> 改密码</p>
     * @author WangTianshan (王天山)
     * @return java.lang.String
     */
    @RequestMapping(value="/resetpwd", method = {RequestMethod.POST})
    public String resetPwd(HttpServletRequest request, HttpServletResponse response, Model model) {
        //验证
        if(this.userService.canRestPassword(request.getParameter("username"),request.getParameter("mark"))){
            if(this.userService.restPassword(request.getParameter("username"),request.getParameter("pwd"))){
                model.addAttribute("activeMsg", messageSource.getMessage("forget_resetsuccessful", null, LocaleContextHolder.getLocale()));
                model.addAttribute("activeStatus", true);
                model.addAttribute("loginUsername", request.getParameter("username"));
                return "login";
            }
            model.addAttribute("errorMsg", messageSource.getMessage("forget_resetunsuccessful", null, LocaleContextHolder.getLocale()));
            return "user/forgot";
        }
        else{
            model.addAttribute("errorMsg", messageSource.getMessage("forget_linkpast", null, LocaleContextHolder.getLocale()));
            return "user/forgot";
        }
    }
}