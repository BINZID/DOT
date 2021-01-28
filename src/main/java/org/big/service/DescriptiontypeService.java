package org.big.service;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.Descriptiontype;

import com.alibaba.fastjson.JSON;

public interface DescriptiontypeService {
	/**
     *<b>Descriptiontype的select列表</b>
     *<p> Descriptiontype的select检索列表</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findBySelect(HttpServletRequest request, String mark);
	
	/**
     *<b>根据Id查找一个实体</b>
     *<p> 根据Id查找一个实体</p>
     * @author BINZI
	 * @param id
     * @return org.big.entity.Descriptiontype
     */
	Descriptiontype findOneById(String id);
}