package org.big.controller.rest;

import com.alibaba.fastjson.JSON;

import org.big.entity.Message;
import org.big.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 *<p><b>MessageController的Rest风格类</b></p>
 *<p> MessageController的Rest风格类</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/console/message/rest")
public class MessageRestController {

    @Autowired
    private MessageService messageService;

    /**
     *<b>收信列表</b>
     *<p> 当前用户所能查看权限的收信列表</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    @RequestMapping("/list")
    public JSON List(HttpServletRequest request) {
        return this.messageService.findInfoByAddressee(request);
    }
    
    /**
     *<b>发送邮件</b>
     *<p> 发送邮件</p>
     * @author BINZI
     * @param thisMessage
     * @param request
     * @return java.lang.String
     */
    @PostMapping(value="/send")
    public JSON send(@ModelAttribute("thisMessage") Message thisMessage, HttpServletRequest request) {
        return this.messageService.sendEmail(thisMessage, request);
    }
    
    /**
     *<b>发信列表</b>
     *<p> 当前用户所能查看权限的发信列表</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    @RequestMapping("/sent")
    public JSON Sent(HttpServletRequest request) {
    	return this.messageService.findInfoBySender(request);
    }
}
