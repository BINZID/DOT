package org.big.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.big.sp2000.entity.Specialist;
import org.big.entity.Expert;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

public interface ExpertService {
	/**
	 *<b>带分页排序的条件查询</b>
	 *<p> 带分页排序的条件查询功能</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON findExpertList(HttpServletRequest request);

	/**
	 *<b>带分页排序的条件查询已上传列表</b>
	 *<p> 带分页排序的条件查询功能已上传列表</p>
	 * @author WangTianshan
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON findUploadedExpertList(HttpServletRequest request,String fileMark);

	/**
     *<b>Expert的select列表</b>
     *<p> 当前Expert的select检索列表</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findBySelect(HttpServletRequest request);

	/**
     *<b>导出审核专家数据</b>
     *<p> 导出审核专家数据</p>
     * @author BINZI
	 * @param response
	 * @return
	 */
	void export(HttpServletResponse response) throws IOException;

	/**
     *<b>根据id查找Expert实体</b>
     *<p> 根据id查找Expert实体</p>
     * @author BINZI
	 * @param id
	 * @return
	 */
	Expert findOneById(String id);

	/**
	 * <b>解析导入的Expert相关的Excel文件</b>
	 * <p> 解析导入的Expert相关的Excel文件</p>
	 * @param file
	 * @param request
	 * @param string
	 * @return
	 */
	JSON uploadFile(MultipartFile file, HttpServletRequest request)  throws Exception;

	/**
	 * <b>专家信息上传列表页面</b>
	 * <p>专家信息上传列表页面</p>
	 * @author BINZI
	 * @param request
	 * @return
	 */
	JSON findUploadList(HttpServletRequest request);

	/**
     * <b>通过解析专家的姓名、邮箱获取专家的Id</b>
     * <p> 通过解析专家的姓名、邮箱获取专家的Id</P>
	 * @param expertInfo
	 * @return
	 */
	String findOneByExpertInfo(String expertInfo);
	/**
	 * @Description 查询采集系统审核专家 -- 封装成名录Specialist实体数据
	 * @return
	 */
	List<Specialist> getExpertInFo();

}
