package org.big.controller;

import javax.servlet.http.HttpServletResponse;

import org.big.entity.Commonname;
import org.big.service.CommonnameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *<p><b>Commonname相关的Controller类</b></p>
 *<p> Commonname相关的Controller</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/12 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/commonname")
public class CommonnameController {

    @Autowired
    private CommonnameService commonnameService;

    /**
     *<b>默认页面</b>
     *<p> 展示列表和操作选项</p>
     * @author WangTianshan (王天山)
     * @param
     * @return java.lang.String
     */
    @RequestMapping(value="", method = {RequestMethod.GET})
    public String FindOne() {
        return "commonname/index";
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
        Commonname thisCommonname=new Commonname();
        model.addAttribute("thisCommonname", thisCommonname);
        return "commonname/add";
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
        Commonname thisCommonname=this.commonnameService.findbyID(id);
        model.addAttribute("thisCommonname", thisCommonname);
        return "commonname/edit";
    }

    /**
     *<b>保存</b>
     *<p> 将传入的实体保存</p>
     * @author WangTianshan (王天山)
     * @param thisCommonname 传入的实体
     * @return java.lang.String
     */
    @RequestMapping(value="/save", method = {RequestMethod.POST})
    public String Save(@ModelAttribute("thisCommonname") Commonname thisCommonname) {
        this.commonnameService.saveOne(thisCommonname);
        return "redirect:/commonname";
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
        this.commonnameService.removeOne(id);
        return "index";
    }
    
	
	/**
	 * <b>导出当前登录用户的Commonname数据</b>
	 * <p>导出当前登录用户的Commonname数据</p>
	 * @author BINZI
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
    	this.commonnameService.export(response);
	}

}
