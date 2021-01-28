package org.big.service;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.Multimedia;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

public interface MultimediaService {
	/**
	 *<b>Multimedia的index页面的列表查询</b>
	 *<p> Multimedia的index页面的列表查询</p>
	 * @author BINZI
	 * @param timestamp
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON findUploadedMultimediaList(Timestamp timestamp, HttpServletRequest request);
	
	/**
	 * <b>添加Molecular基础数据</b>
	 * <p> 添加Molecular基础数据</p>
	 * @author BINZI
	 * @param thisMolecular
	 */
	JSON addMultimedia(String taxonId, HttpServletRequest request);
	
	/**
     *<b> 根据id逻辑删除一个实体</b>
     *<p> 据id逻辑删除一个实体</p>
     * @param id
     * @return
     */
	boolean logicRemove(String id);

	/**
	 *<b>根据id删除一个已添加的实体</b>
     *<p> 根据id删除一个已添加的实体</p>
	 * @param request
	 * @return
	 */
	boolean deleteOne(HttpServletRequest request);
	
	/**
     *<b>将引证表单的Reference相关属性拼装成JSON数据</b>
     *<p> 将引证表单的Reference相关属性拼装成JSON数据</p>
     * @author BINZI
     * @param Id 实体的id
     * @return com.alibaba.fastjson.JSON
     */
	JSON handleReferenceToJson(HttpServletRequest request);
	
	/**
     *<b>批量上传图片</b>
     *<p> 批量上传图片</p>
     * @author BINZI
	 * @param taxonId
	 * @param request
	 * @param file
	 * @param string
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON batchImages(String taxonId, HttpServletRequest request, MultipartFile file, String uploadPath);
	
	/**
	 *<b>根据taxonId查找Taxon下的所有Multimedia实体</b>
     *<p> 根据taxonId查找Taxon下的所有Multimedia实体</p>
	 * @param taxonId
	 * @param request
	 * @return
	 */
	JSON findMultimediaListByTaxonId(String taxonId, HttpServletRequest request);
	
	/**
	 *<b>存储Multimedia实体</b>
     *<p> 存储Multimedia实体</p>
	 * @param taxonId
	 * @param thisMultimedia
	 */
	void saveMultimedia(String taxonId, Multimedia thisMultimedia);

	/**
	 *<b>根据taxonId修改Taxon下的所有Multimedia实体</b>
     *<p> 根据taxonId修改Taxon下的所有Multimedia实体</p>
	 * @param taxonId
	 * @return
	 */
	JSON editMultimedia(String taxonId);

	/**
	 * <b>解析导入的Multimedia相关的Excel文件</b>
	 * <p> 解析导入的Multimedia相关的Excel文件</p>
	 * @author BINZI
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	JSON uploadFile(MultipartFile file, String taxasetId, HttpServletRequest request) throws Exception;

	/**
	 * <b>图片上传之切换实体背景图片</b>
	 * <p> 图片上传之切换实体背景图片</p>
	 * @param file
	 * @param request
	 * @return
	 * @throws IOException
	 */
	JSON changeBg(HttpServletRequest request, MultipartFile file, String uploadPath);

}
