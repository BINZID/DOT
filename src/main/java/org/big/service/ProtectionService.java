package org.big.service;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

public interface ProtectionService {
	/**
	 *<b>Protection的index页面的列表查询</b>
	 *<p> Protection的index页面的列表查询</p>
	 * @author BINZI
	 * @param timestamp
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON findUploadedProtectionList(Timestamp timestamp, HttpServletRequest request);
	/**
	 * <b> 添加Protection实体</b>
	 * <p> 添加Protection实体</p>
	 * @author BINZI
	 * @param thisProtection
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON addProtection(String taxonId, HttpServletRequest request);

	/**
     *<b>将Protection表单的Reference相关属性拼装成JSON数据</b>
     *<p> 将Protection表单的Reference相关属性拼装成JSON数据</p>
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
     *<b>Protection的select列表(保护标准级别)</b>
     *<p> 当前Taxon下的Protection的select检索列表(保护标准级别)</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findBySelect(HttpServletRequest request);
	
	/**
	 *<b>根据id删除一个实体</b>
     *<p> 根据id删除一个实体</p>
	 * @param request
	 * @return
	 */
	boolean deleteOne(HttpServletRequest request);
	
	/**
	 *<b>根据taxonId查找Taxon下的所有Protection实体</b>
     *<p> 根据taxonId查找Taxon下的所有Protection实体</p>
	 * @param request
	 * @return
	 */
	JSON findProtectionListByTaxonId(String taxonId, HttpServletRequest request);
	
	/**
	 * <b>根据taxonId修改Taxon下的Protections</b>
	 * <p> 根据taxonId修改Taxon下的Protections</p>
	 * @param request
	 * @return
	 */
	JSON editProtection(String id);

	/**
     *<b>导出当前登录用户的Protection</b>
     *<p> 导出当前登录用户的Protection</p>
     * @author BINZI
	 * @param response
	 * @return
	 */
	void export(HttpServletResponse response) throws IOException;

	/**
	 * <b>解析导入的Protection相关的Excel文件</b>
	 * <p> 解析导入的Protection相关的Excel文件</p>
	 * @author BINZI
	 * @param file
	 * @param taxasetId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	JSON uploadFile(MultipartFile file, String taxasetId, HttpServletRequest request) throws Exception;

}
