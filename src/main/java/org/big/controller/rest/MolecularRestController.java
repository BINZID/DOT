package org.big.controller.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.big.entity.Molecular;
import org.big.service.MolecularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

/**
 *<p><b>Molecular相关的Controller的Rest风格类</b></p>
 *<p> Molecular相关的Controller的Rest风格类</p>
 * @author  BINZI
 *<p>Created date: 2018/07/12 15:06</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@RestController
@RequestMapping("/console/molecular/rest")
public class MolecularRestController {
	@Autowired
	private MolecularService molecularService;
	
	/**
	 * <b> Molecular的Index页面分页列表查询</b>
	 * <p> Molecular的Index页面分页列表查询</p>
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/list")
	public JSON list(HttpServletRequest request) {
		return this.molecularService.findMolecularList(request);
	}
	
	/**
	 * <b>Molecular添加</b>
	 * <p> Molecular添加</p>
	 * @param request
	 * @param thisMolecular
	 * @param result
	 * @param model
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	@PostMapping(value="/add")
	public JSON AddMolecular(@ModelAttribute("thisMolecular") @Valid Molecular thisMolecular, BindingResult result, Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			String errorMsg = "";
			for (ObjectError error : list) {
				errorMsg = errorMsg + error.getDefaultMessage() + ";";
			}
			model.addAttribute("thisMolecular", thisMolecular);
			model.addAttribute("errorMsg", errorMsg);
		}
		return this.molecularService.addMolecular(thisMolecular, request);
		
	}

	/**
	 * <b>根据Id批量逻辑删除指定Molecular</b>
	 * <p> 根据Id批量逻辑删除指定Molecular</p>
	 * @param request
	 * @return 
	 */
	@GetMapping(value = "/removeMany/{ids}")
	public int removeMany(@PathVariable String ids) {
		try {
			String[] idArr = ids.split("￥");
			int isRemove = 0;
			for (String id : idArr) {
				if (this.molecularService.logicRemove(id)) {
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
	 * <b>根据Id单个逻辑删除指定Molecular</b>
	 * <p> 根据Id单个逻辑删除指定Molecular</p>
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/remove/{id}")
	public boolean remove(@PathVariable String id) {
		try {
			return this.molecularService.logicRemove(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
