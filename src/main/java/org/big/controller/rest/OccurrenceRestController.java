package org.big.controller.rest;

import javax.servlet.http.HttpServletRequest;
import org.big.service.OccurrenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

/**
 *<p><b>Occurrence相关的Controller的Rest风格类</b></p>
 *<p> Occurrence相关的Controller的Rest风格类</p>
 * @author  BINZI
 *<p>Created date: 2018/07/13 11:30</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController
@RequestMapping("/console/occurrence/rest")
public class OccurrenceRestController {
	@Autowired
	private OccurrenceService occurrenceService;
	
	/**
	 * <b>Occurrence的Index页面分页列表查询</b>
	 * <p> Occurrence的Index页面分页列表查询</p>
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/list")
	public JSON list(HttpServletRequest request) {
		return this.occurrenceService.findOccurrenceList(request);
	}
	
	/**
	 * <b>Occurrence添加</b>
	 * <p> Occurrence添加</p>
	 * @param request
	 * @param id
	 * @return com.alibaba.fastjson.JSON
	 */
	@PostMapping(value="/add/{id}")
	public JSON addOccurrence(@PathVariable String id, HttpServletRequest request) {
		return this.occurrenceService.addOccurrence(id, request);
	}
	
	/**
	 * <b>Occurrence修改</b>
	 * <p> Occurrence修改</p>
	 * @param request
	 * @param id
	 * @return com.alibaba.fastjson.JSON
	 */
	@GetMapping(value="/edit/{id}")
	public JSON editOccurrence(@PathVariable String id) {
		return this.occurrenceService.editOccurrence(id);
	}

	/**
	 * <b>根据Id批量逻辑删除指定Occurrence</b>
	 * <p> 根据Id批量逻辑删除指定Occurrence</p>
	 * @param request
	 * @return 
	 */
	@GetMapping(value = "/removeMany/{ids}")
	public int removeMany(@PathVariable String ids) {
		try {
			String[] idArr = ids.split("￥");
			int isRemove = 0;
			for (String id : idArr) {
				if (this.occurrenceService.logicRemove(id)) {
					isRemove = isRemove + 1;
				}
			}
			return isRemove;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * <b>根据Id单个逻辑删除指定Occurrence</b>
	 * <p> 根据Id单个逻辑删除指定Occurrence</p>
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/remove/{id}")
	public boolean remove(@PathVariable String id) {
		try {
			return this.occurrenceService.logicRemove(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
     *<b>Occurrence信息添加后的删除</b>
     *<p> Occurrence信息添加后的删除</p>
     * @author BINZI
     * @param request 页面请求
     * @return 
     */
	@PostMapping(value = "/delete")
	public boolean delete(HttpServletRequest request){
		return this.occurrenceService.deleteOne(request);
	}
	
	/**
     *<b>根据TaxonId查找对应Taxon下的所有Occurrence</b>
     *<p> 根据TaxonId查找对应Taxon下的所有Occurrence</p>
     * @author BINZI
	 * @param request
	 * @param id
     * @return 
     */
	@GetMapping("/occurrenceList/{id}")
	public JSON occurrenceList(HttpServletRequest request,@PathVariable String id) {
		return this.occurrenceService.findOccurrenceListByTaxonId(id, request);
	}
}
