package org.big.service;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;

public interface OccurrenceService {
	/**
	 *<b>Molecular的select列表</b>
	 *<p> Molecular的检索列表</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON findOccurrenceList(HttpServletRequest request);
	
	/**
	 * <b>添加Molecular基础数据</b>
	 * <p> 添加Molecular基础数据</p>
	 * @author BINZI
	 * @param thisMolecular
	 */
	JSON addOccurrence(String taxonId, HttpServletRequest request);
	
	/**
     *<b> 根据id逻辑删除一个实体</b>
     *<p> 据id逻辑删除一个实体</p>
     * @param id
     * @return
     */
	boolean logicRemove(String id);
	
	/**
     *<b>将引证表单的Reference相关属性拼装成JSON数据</b>
     *<p> 将引证表单的Reference相关属性拼装成JSON数据</p>
     * @author BINZI
     * @param Id 实体的id
     * @return com.alibaba.fastjson.JSON
     */
	JSON handleReferenceToJson(HttpServletRequest request);
	
	/**
	 *<b>根据id删除一个已添加的实体</b>
     *<p> 根据id删除一个已添加的实体</p>
	 * @param request
	 * @return
	 */
	boolean deleteOne(HttpServletRequest request);

	/**
	 *<b>根据taxonId查找Taxon下的所有Occurrence实体</b>
     *<p> 根据taxonId查找Taxon下的所有Occurrence实体</p>
	 * @param taxonId
	 * @param request
	 * @return
	 */
	JSON findOccurrenceListByTaxonId(String taxonId, HttpServletRequest request);

	/**
	 *<b>根据taxonId修改Taxon下的所有Occurrence实体</b>
     *<p> 根据taxonId修改Taxon下的所有Occurrence实体</p>
	 * @param taxonId
	 * @return
	 */
	JSON editOccurrence(String taxonId);
}
