package org.big.controller.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.big.service.NamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *<p><b>Names相关的Controller类</b></p>
 *<p> Names相关的Controller类</p>
 * @author BINZI
 *<p>Created date: 2018/06/11 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
@RequestMapping("/console/names")
public class NamesController {
	@Autowired
	private NamesService namesService;
	
	/**
	 *<b>下载指定team下的所有dataset的Names名称表</b>
	 *<p> 下载指定team下的所有dataset的Names名称表</p>
	 * @author BINZI
	 * @param response 页面响应
	 * @throws IOException 
	 */
	@GetMapping(value = "/export")
	public void list(HttpServletResponse response) throws IOException {
		this.namesService.export(response);
	}	
}
