package org.big.controller;


import javax.servlet.http.HttpServletResponse;

import org.big.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;*/

/**
 *<p><b>License相关的Controller类</b></p>
 *<p> License相关的Controller</p>
 * @author BINZI
 *<p>Created date: 2018/09/11 08:44</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping(value = "/console/license")
public class LicenseController {
	@Autowired
	private LicenseService licenseService;
	/**
     *<b> License的列表页面</b>
     *<p> License的列表页面</p>
     * @author BINZI
     * @return java.lang.String
     */
	@GetMapping(value = "")
	public String toIndex() {
		return "license/index";
	}
	
	@RequestMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
    	this.licenseService.export(response);
	}
}
