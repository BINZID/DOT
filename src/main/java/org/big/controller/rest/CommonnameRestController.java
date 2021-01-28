package org.big.controller.rest;

import com.alibaba.fastjson.JSON;

import org.big.service.CommonnameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

/**
 *<p><b>CommonnameController的Rest风格类</b></p>
 *<p> CommonnameController的Rest风格类</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/console/commonname/rest")
public class CommonnameRestController {

    @Autowired
    private CommonnameService commonnameService;

	/**
	 *<b>Commonname的已上传listJSON</b>
	 *<p> Commonname的已上传listJSON</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @param timestamp
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value = "/uploaded/{timestamp}")
	public JSON list(HttpServletRequest request, @PathVariable Timestamp timestamp) {
		return this.commonnameService.findUploadedCommonnameList(timestamp, request);
	}
    
    /**
	 * <b>Commonname添加</b>
	 * <p> Commonname添加</p>
	 * @param id
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	@PostMapping(value="/add/{id}")
	public JSON addCommonname(@PathVariable String id, HttpServletRequest request) {
		return this.commonnameService.addCommonname(id, request);
		
	}
	
	/**
	 * <b>Commonname编辑</b>
	 * <p> Commonname编辑</p>
	 * @param id
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value="/edit/{id}")
	public JSON editCommonname(@PathVariable String id) {
		return this.commonnameService.editCommonname(id);
		
	}

    /**
     *<b>删除多个</b>
     *<p> 根据id序列一次性删除多个</p>
     * @author WangTianshan (王天山)
     * @param ids id序列，用"￥"分隔
     * @return boolean
     */
    @RequestMapping(value="/removeMany/{ids}",method = {RequestMethod.GET})
    public boolean removeMany(@PathVariable String ids) {
        try{
            //获取id列表字符串
            String [] idList;
            idList = ids.split("￥");
            for(int i=0;i<idList.length;i++){
                this.commonnameService.removeOne(idList[i]);
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     *<b>删除单个</b>
     *<p> 根据id删除单个</p>
     * @author WangTianshan (王天山)
     * @param id 实体id
     * @return boolean
     */
    @RequestMapping(value="/remove/{id}",method = {RequestMethod.GET})
    public boolean Remove(@PathVariable String id) {
        try{
            this.commonnameService.removeOne(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
	/**
     *<b>Commonname信息添加后的删除</b>
     *<p> Commonname信息添加后的删除</p>
     * @author BINZI
     * @param request 页面请求
     * @return 
     */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public boolean delete(HttpServletRequest request){
		return this.commonnameService.deleteOne(request);
	}
	
	/**
     *<b>根据TaxonId查找对应Taxon下的所有Commonname详情</b>
     *<p> 根据TaxonId查找对应Taxon下的所有Commonname详情</p>
     * @author BINZI
     * @param request 页面请求
     * @return 
     */
	@GetMapping("/commonnameList/{id}")
	public JSON commonnameList(HttpServletRequest request, @PathVariable String id) {
		return this.commonnameService.findCommonnameListByTaxonId(id, request);
	}
	
	/**
	 * <b>解析导入的Commonname相关的Excel文件</b>
	 * <p> 解析导入的Commonname相关的Excel文件</p>
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/upload/{taxasetId}")
    public JSON upload(@RequestParam("uploadCommonname") MultipartFile file, @PathVariable String taxasetId, HttpServletRequest request) throws Exception{
		return this.commonnameService.uploadFile(file, taxasetId, request);
	}
}
