package org.big.service;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public interface TraitdataService {
	/**
	 *<b>Traitdata的index页面的列表查询</b>
	 *<p> Traitdata的index页面的列表查询</p>
	 * @author BINZI
	 * @param timestamp
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON findUploadedTraitdataList(Timestamp timestamp, HttpServletRequest request);
	
	/**
	 * <b> 添加Traitdata实体</b>
	 * <p> 添加Traitdata实体</p>
	 * @author BINZI
	 * @param taxonId
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON addTraitdata(String taxonId, HttpServletRequest request);
	/**
     *<b>将特征数据表单的Reference相关属性拼装成JSON数据</b>
     *<p> 将特征淑君表单的Reference相关属性拼装成JSON数据</p>
     * @author BINZI
     * @param request
     * @return com.alibaba.fastjson.JSON
     */
	JSON handleReferenceToJson(HttpServletRequest request);
	
	/**
     *<b>将特征数据表单的'特征描述详情'相关属性拼装成JSON数据</b>
     *<p> 将特征淑君表单的'特征描述详情'相关属性拼装成JSON数据</p>
     * @author BINZI
     * @param request
     * @return com.alibaba.fastjson.JSON
     */
	JSON handleTraitDescToJson(HttpServletRequest request);
	
	/**
	 *<b>根据id删除一个实体</b>
     *<p> 根据id删除一个实体</p>
	 * @param request
	 * @return
	 */
	boolean deleteOne(HttpServletRequest request);
	
	/**
	 *<b>根据taxonId查找Taxon下的所有Traitdata实体</b>
     *<p> 根据taxonId查找Taxon下的所有Traitdata实体</p>
	 * @param taxonId
	 * @param request
	 * @return
	 */
	JSON findTraitdataListByTaxonId(String taxonId, HttpServletRequest request);

	/**
	 * <b>根据taxonId修改Taxon下的Traitdatas</b>
	 * <p> 根据taxonId修改Taxon下的Traitdatas</p>
	 * @param taxonId
	 * @return
	 */
	JSON editTraitdata(String taxonId);

	/**
	 *<b>构造特征列表</b>
	 *<p> 构造特征列表</p>
	 * @author WangTianshan
	 * @param
	 * @return com.alibaba.fastjson.JSON
	 */
	JSONArray BuildTrait(HttpServletRequest request);

	/**
	 * <b>构造实体的参考文献</b>
	 * <p> 构造实体的参考文献</p>
	 * @param refjson
	 */
	JSONArray refactoringTraitDesc(String traitjson);

	/**
     *<b>导出当前登录用户的Traitdata</b>
     *<p> 导出当前登录用户的Traitdata</p>
     * @author BINZI
	 * @param response
	 * @return
	 */
	void export(HttpServletResponse response) throws IOException;

	/**
	 * <b>解析导入的Traitdata相关的Excel文件</b>
	 * <p> 解析导入的Traitdata相关的Excel文件</p>
	 * @author BINZI
	 * @param file
	 * @param taxasetId
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	JSON uploadFile(MultipartFile file, String taxasetId, HttpServletRequest request) throws Exception;

}
