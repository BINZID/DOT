package org.big.service;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

public interface KeyitemService {
	/**
	 *<b>带分页排序的条件查询</b>
	 *<p> 带分页排序的条件查询</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON findKeyitemList(String keyitemId);

	/**
	 * <b> 根据TaxkeyId返回检索表详情</b>
	 * <p> 根据TaxkeyId返回检索表详情</p>
	 * @author WangTianshan（王天山）
	 * @param taxkeyId
	 * @return com.alibaba.fastjson.JSON
	 */
	JSONArray findKeyitemTable(String taxkeyId);

	/**
     *<b>批量上传图片</b>
     *<p> 批量上传图片</p>
     * @author BINZI
	 * @param keyitemId
	 * @param request
	 * @param file
	 * @param uploadPath
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON batchImages(String keyitemId, HttpServletRequest request, MultipartFile file, String uploadPath);
	
	/**
	 *<b>特征图片分页列表查询</b>
     * <p> 特征图片分页列表查询</p>
	 * @author BINZI
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON findFeatureImgList(HttpServletRequest request);
	
	/**
	 *<b>根据Id删除Keyitem实体</b>
	 *<p>根据Id删除Keyitem实体</p> 
	 * @param keyitemId
	 * @return
	 */
	boolean delOne(String keyitemId);
}
