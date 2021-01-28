package org.big.controller.rest;

import com.alibaba.fastjson.JSON;

import org.big.service.DatasetService;
import org.big.service.MessageService;
import org.big.service.TeamService;
import org.big.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 *<p><b>超级管理员Controller的Rest风格类</b></p>
 *<p> 超级管理员Controller的Rest风格类</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/super")
public class SuperRestController {
	@Autowired
	private DatasetService datasetService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    
    /**
     *<b>Team列表</b>
     *<p> 所有Team的检索列表</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    @RequestMapping("/team/rest/list")
    public JSON TeamList(HttpServletRequest request) {
        return this.teamService.findbyInfo(request);
    }

    /**
     *<b>删除多个Team</b>
     *<p> 根据Team id序列一次性删除多个</p>
     * @author WangTianshan (王天山)
     * @param ids Team id序列，用"￥"分隔
     * @return boolean
     */
    @RequestMapping(value="/team/rest/removeMany/{ids}",method = {RequestMethod.GET})
    public boolean RemoveManyTeam(@PathVariable String ids) {
        try{
            //获取id列表字符串
            String [] idList;
            idList = ids.split("￥");
            for(int i=0;i<idList.length;i++){
                this.teamService.removeOne(idList[i]);
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     *<b>删除单个Team</b>
     *<p> 根据Team id删除单个</p>
     * @author WangTianshan (王天山)
     * @param id Team id
     * @return boolean
     */
    @RequestMapping(value="/team/rest/remove/{id}",method = {RequestMethod.GET})
    public boolean RemoveTeam(@PathVariable String id) {
        try{
            this.teamService.removeOne(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }
// -------------------------------------------------------------------------------
    /**
     *<b>User列表</b>
     *<p> 所有User的检索列表</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    @RequestMapping("/user/rest/list")
    public JSON UserList(HttpServletRequest request) {
        return this.userService.findbyInfo(request);
    }

    /**
     *<b>删除多个User</b>
     *<p> 根据User id序列一次性删除多个</p>
     * @author WangTianshan (王天山)
     * @param ids User id序列，用"￥"分隔
     * @return boolean
     */
    @RequestMapping(value="/user/rest/removeMany/{ids}",method = {RequestMethod.GET})
    public boolean RemoveManyUser(@PathVariable String ids) {
        try{
            //获取id列表字符串
            String [] idList = ids.split("￥");
            for(int i=0;i<idList.length;i++){
                this.userService.removeOne(idList[i]);
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     *<b>删除单个User</b>
     *<p> 根据User id删除单个</p>
     * @author WangTianshan (王天山)
     * @param id User id
     * @return boolean
     */
    @RequestMapping(value="/user/rest/remove/{id}",method = {RequestMethod.GET})
    public boolean RemoveUser(@PathVariable String id) {
        try{
            this.userService.removeOne(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     *<b>Message列表</b>
     *<p> 所有Message的检索列表</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    @RequestMapping("/message/rest/list")
    public JSON MessageList(HttpServletRequest request) {
        return this.messageService.findbyInfo(request);
    }
// -------------------------------------------------------------------------------
    /**
     *<b>Dataset列表</b>
     *<p> 所有Dataset的检索列表</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    @RequestMapping("/dataset/rest/list")
    public JSON DatasetList(HttpServletRequest request) {
        return this.datasetService.findbyInfo(request);
    }

    /**
     *<b>批量逻辑删除删除多个Dataset</b>
     *<p> 根据Dataset id序列一次性删除多个</p>
     * @author BINZI
     * @param ids Media id序列，用"￥"分隔
     * @return boolean
     */
    @RequestMapping(value="/dataset/rest/removeMany/{ids}",method = {RequestMethod.GET})
    public int RemoveManyDataset(@PathVariable String ids) {
    	try{
            //获取id列表字符串
            String[] idList = ids.split("￥");
            int isRemove=0;
            for(int i=0;i<idList.length;i++){
                if(this.datasetService.logicRemove(idList[i])) {
                    isRemove=isRemove+1;
                }
            }
            return isRemove;
        }catch(Exception e){
            return -1;
        }
    }

    /**
     *<b>逻辑删除单个Dataset</b>
     *<p> 根据Dataset id删除单个</p>
     * @author BINZI
     * @param id Media id
     * @return boolean
     */
    @RequestMapping(value="/dataset/rest/remove/{id}",method = {RequestMethod.GET})
    public boolean RemoveDataset(@PathVariable String id) {
        try{
            return this.datasetService.logicRemove(id);
        }catch(Exception e){
            return false;
        }
    }
    
    /**
     *<b>物理删除多个Dataset</b>
     *<p> 根据Dataset id序列一次性物理删除多个</p>
     * @author WangTianshan (王天山)
     * @param ids Media id序列，用"￥"分隔
     * @return boolean
     */
    @RequestMapping(value="/dataset/rest/deleteMany/{ids}",method = {RequestMethod.GET})
    public int DeleteManyDataset(HttpServletRequest request,@PathVariable String ids) {
        try{
            //获取id列表字符串
            String [] idList = ids.split("￥");
            int isRemove=0;
            for(int i=0;i<idList.length;i++){
                if(this.datasetService.deleteOne(request, (idList[i])))
                    isRemove=isRemove+1;
            }
            return isRemove;
        }catch(Exception e){
            return -1;
        }
    }

    /**
     *<b>物理删除单个Dataset</b>
     *<p> 根据Dataset id物理删除单个</p>
     * @author WangTianshan (王天山)
     * @param id Media id
     * @return boolean
     */
    @RequestMapping(value="/dataset/rest/delete/{id}",method = {RequestMethod.GET})
    public boolean DeleteDataset(HttpServletRequest request,@PathVariable String id) {
        try{
            return this.datasetService.deleteOne(request,id);
        }catch(Exception e){
            return false;
        }
    }
    
}
