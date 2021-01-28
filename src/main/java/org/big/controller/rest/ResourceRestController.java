package org.big.controller.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.big.service.KeyitemService;
import org.big.service.ResourceService;
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

import com.alibaba.fastjson.JSON;

/**
 *<p><b>Resource相关的Controller的Rest风格类</b></p>
 *<p> Resource相关的Controller的Rest风格类</p>
 * @author MengMeng (王孟豪)
 *<p>Created date: 2018/08/23 </p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController  //返回json
@Controller
@RequestMapping("/console/resource/rest")
public class ResourceRestController {
	@Autowired
	private ResourceService resourceService;
	
    /**
     *<b>删除单个remove</b>
     *<p> 根据remove删除单个</p>
     * @author MengMeng (王孟豪)
 	 * @param id
     * @return boolean
     */
    @RequestMapping(value="/remove/{id}", method = RequestMethod.GET)
    public boolean RemoveDataset(@PathVariable String id) {
    	try{
            return this.resourceService.delOne(id);
        }catch(Exception e){
            return false;
        }
    }
}
