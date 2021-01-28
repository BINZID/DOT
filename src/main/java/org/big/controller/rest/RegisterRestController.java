package org.big.controller.rest;
import org.big.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *<p><b>UserController的Rest风格类</b></p>
 *<p> UserController的Rest风格类</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/11/1 10:19</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/register/rest")
public class RegisterRestController {

    @Autowired
    private UserService userService;

    /**
     *<b>用户名重复</b>
     *<p> 用户名重复</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return Boolean
     */
    @RequestMapping(value="/isReName", method = {RequestMethod.GET})
    public Boolean isReName(HttpServletRequest request) {
        if(userService.findOneByName(request.getParameter("name"))!=null)
            return true;
        else
            return false;
    }

    /**
     *<b>邮箱重复</b>
     *<p> 邮箱重复</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return Boolean
     */
    @RequestMapping(value="/isReEmail", method = {RequestMethod.GET})
    public Boolean isReEmail(HttpServletRequest request) {
        if(userService.findOneByEmail(request.getParameter("email"))!=null)
            return true;
        else
            return false;
    }

    /**
     *<b>重发激活邮件</b>
     *<p> 重发激活邮件</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @param response 页面响应
     * @return String
     */
    @Value("${spring.mail.username}") private String fromEmail;
    @RequestMapping(value="/resendActiveEmail", method = {RequestMethod.POST})
    public String resendActiveEmail(HttpServletRequest request, HttpServletResponse response) {
        return this.userService.sendActiveEmail(request,response);
    }
}