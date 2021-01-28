package org.big.controller.rest;

import javax.servlet.http.HttpServletRequest;

import org.big.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;


/**
 *<p><b>UserController的Rest风格类</b></p>
 *<p> UserController的Rest风格类</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/console/user/rest")
public class UserRestController {

    @Autowired
    private UserService userService;

    //List
    @RequestMapping("/list")
    public JSON List(HttpServletRequest request) {
        return this.userService.findbyInfo(request);
    }
    
    //removeMany
    @RequestMapping(value="/removeMany/{ids}",method = {RequestMethod.GET})
    public boolean RemoveMany(@PathVariable String ids) {
        try{
            //获取id列表字符串
            String [] idList;
            idList = ids.split("￥");
            for(int i=0;i<idList.length;i++){
                this.userService.removeOne(idList[i]);
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }

    //removeOne
    @RequestMapping(value="/remove/{id}",method = {RequestMethod.GET})
    public boolean Remove(@PathVariable String id) {
        try{
            this.userService.removeOne(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    /**
     *<b>选择邮件接收人</b>
     *<p> 选择邮件接收人</p>
     * @author BINZI
     * @param request
     * @return
     */
    @RequestMapping("/userInfo")
    @ResponseBody
    public JSON userInfo(HttpServletRequest request) {
        return this.userService.findAllUserInfo(request);
    }
    
	/**
     *<b>Team下的用户select列表</b>
     *<p> Team下的用户select列表</p>
     * @author BINZI
     * @param request
     * @param tid
     * @return com.alibaba.fastjson.JSON
     */
	@GetMapping(value = "/select/{tid}")
	public JSON select(HttpServletRequest request, @PathVariable String tid) {
		return this.userService.findUsersByTid(request, tid);
	}
    
}