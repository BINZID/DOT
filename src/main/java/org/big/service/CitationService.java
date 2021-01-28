package org.big.service;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

public interface CitationService {

	/**
	 *<b>Citation的index页面的列表查询</b>
	 *<p> Citation的index页面的列表查询</p>
	 * @author BINZI
	 * @param timestamp
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON findUploadedCitationList(Timestamp timestamp, HttpServletRequest request);
	/**
	 * <b> 添加Citation实体</b>
	 * <p> 添加Citation实体</p>
	 * @author BINZI
	 * @param thisCitation
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON addCitation(String taxonId, HttpServletRequest request);
	
	/**
     *<b>将引证表单的Reference相关属性拼装成JSON数据</b>
     *<p> 将引证表单的Reference相关属性拼装成JSON数据</p>
     * @author BINZI
     * @param Id 实体的id
     * @return com.alibaba.fastjson.JSON
     */
	JSON handleReferenceToJson(HttpServletRequest request);
	
	/**
	 *<b>根据id逻辑删除一个实体</b>
     *<p> 根据id逻辑删除一个实体</p>
	 * @param id
	 * @return
	 */
	boolean logicRemove(String id);

	/**
	 *<b>根据id删除一个实体</b>
     *<p> 根据id删除一个实体</p>
	 * @param request
	 * @return
	 */
	boolean deleteOne(HttpServletRequest request);
	
	/**
	 *<b>根据taxonId查找Taxon下的所有Citation实体</b>
     *<p> 根据taxonId查找Taxon下的所有Citation实体</p>
	 * @param request
	 * @return
	 */
	JSON findCitationListByTaxonId(String taxonId, HttpServletRequest request);

	/**
	 * <b>根据taxonId修改Taxon下的Citations</b>
	 * <p> 根据taxonId修改Taxon下的Citations</p>
	 * @param request
	 * @return
	 */
	JSON editCitations(String taxonId);

	/**
     *<b>导出当前登录用户的Citation</b>
     *<p> 导出当前登录用户的Citation</p>
     * @author BINZI
	 * @param response
	 * @return
	 */
	void export(HttpServletResponse response) throws IOException;

	/**
	 * <b>解析导入的Citation相关的Excel文件</b>
	 * <p> 解析导入的Citation相关的Excel文件</p>
	 * @author BINZI
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	JSON uploadFile(MultipartFile file, String taxasetId, HttpServletRequest request) throws Exception;

}
