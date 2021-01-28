package org.big.service;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.Taxkey;

import com.alibaba.fastjson.JSON;

public interface TaxkeyService {
	/**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findTaxkeyList(HttpServletRequest request);
	
	/**
	 * <b>添加Taxkey实体</b>
	 * <p> 添加Taxkey实体</p>
	 * @author BINZI
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON addTaxkeyForKeyitem(HttpServletRequest request, String taxonId);

	/**
	 *<b>根据id删除一个实体</b>
     *<p> 根据id删除一个实体</p>
	 * @param id
	 * @return
	 */
	boolean deleteOneById(String id);
	
	/**
	 *<b>根据id删除一个已添加的实体</b>
     *<p> 根据id删除一个已添加的实体</p>
	 * @param request
	 * @return
	 */
	boolean deleteOne(HttpServletRequest request);
	
	/**
	 * <b>添加Taxkey实体</b>
	 * <p> 添加Taxkey实体</p>
	 * @author BINZI
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON addTaxkey(String taxonId,HttpServletRequest request);
	
	/**
	 * <b>添加Keyitem实体</b>
	 * <p> 添加Keyitem实体</p>
	 * @author BINZI
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON addKeyitem(String taxonId, Enumeration<String> paraNames, HttpServletRequest request, String taxkeyId, String keyitemId);
	
	/**
	 *<b>根据taxonId查找Taxon下的所有Taxkey实体</b>
     *<p> 根据taxonId查找Taxon下的所有Taxkey实体</p>
	 * @param request
	 * @return
	 */
	JSON findTaxkeyListByTaxonId(String taxonId, HttpServletRequest request);
	
	 /**
     *<b>根据id查找一个Taxkey实体</b>
     *<p> 据id查找一个实体</p>
     * @author BINZI
     * @param id 实体的id
     * @return org.big.entity.Taxkey
     */
	Taxkey findOneById(String taxkeyId);
	
	/**
	 * <b>根据taxonId修改Taxon下的Taxkey</b>
	 * <p> 根据taxonId修改Taxon下的Taxkey</p>
	 * @param request
	 * @return
	 */
	JSON editTaxkey(String taxonId);
	/**
	 * @Description 根据taxonId删除相关检索表
	 * @author BINZI
	 * @param taxonId
	 */
	void delTaxkeyByTaxonId(String taxonId);
}
