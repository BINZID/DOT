package org.big.service;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.big.entity.Description;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

public interface DescriptionService {
	/**
	 *<b>Description的index页面的列表查询</b>
	 *<p> Description的index页面的列表查询</p>
	 * @author BINZI
	 * @param timestamp
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON findUploadedDescriptionList(Timestamp timestamp, HttpServletRequest request);
	
	/**
	 * <b> 添加Description实体</b>
	 * <p> 添加Description实体</p>
	 * @author BINZI
	 * @param thisDescription
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON addDescription(String taxonId , HttpServletRequest request);

	/**
     *<b>将描述表单的Reference相关属性拼装成JSON数据</b>
     *<p> 将描述表单的Reference相关属性拼装成JSON数据</p>
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
     *<b>Description的select列表</b>
     *<p> Description的select检索列表</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findBySelect(HttpServletRequest request);
	
	/**
	 *<b>根据taxonId查找Taxon下的所有Description实体详情</b>
     *<p> 根据taxonId查找Taxon下的所有Description实体详情</p>
	 * @param request
	 * @return
	 */
	JSON findDescriptionListByTaxonId(String taxonId, HttpServletRequest request);
	
	/**
	 *<b>根据id查找Description实体</b>
     *<p> 根据id查找Description实体</p>
	 * @param desid
	 * @return
	 */
	Description findOneById(String desid);
	
	/**
	 * <b>根据taxonId修改Taxon下的Descriptions</b>
	 * <p> 根据taxonId修改Taxon下的Descriptions</p>
	 * @param request
	 * @return
	 */
	JSON editDescriptions(String taxonId);
	
	/**
	 * <b>Taxon下的描述信息实体的关系下拉选</b>
	 * <p> Taxon下的描述信息实体的关系字段</p>
	 * @param taxonId
	 * @param request
	 * @return
	 */
	JSON findDescListByTaxonId(String taxonId, HttpServletRequest request);
	
	/**
	 * <b>导出当前登录用户的Description数据</b>
	 * <p>导出当前登录用户的Description数据</p>
	 * @author BINZI
	 * @param response
	 * @throws IOException
	 */
	void export(HttpServletResponse response) throws IOException;
	
	/**
	 * <b>解析导入的Description相关的Excel文件</b>
	 * <p> 解析导入的Description相关的Excel文件</p>
	 * @author BINZI
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	JSON uploadFile(MultipartFile file, String taxasetId, HttpServletRequest request) throws Exception;
	/**
	 * <b>根据选中的描述Id补充分布描述内容</b>
	 * <p> 根据选中的描述Id补充分布描述内容</p>
	 * @author BINZI
	 * @param descid
	 * @return
	 */
	JSON getDescContentById(String descid);

}
