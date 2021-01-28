package org.big.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.alibaba.fastjson.JSONArray;
import org.big.entity.Ref;
import org.big.sp2000.entity.Reference;
import org.big.sp2000.entity.ReferenceLink;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

public interface RefService {
	/**
	 *<b>Ref的index页面的列表查询</b>
	 *<p> Ref的index页面的列表查询</p>
	 * @author BINZI
	 * @param request
	 * @return
	 */
	JSON findRefList(HttpServletRequest request);

	/**
	 *<b>Ref的index页面的列表查询</b>
	 *<p> Ref的index页面的列表查询</p>
	 * @author WangTianshan
	 * @param request
	 * @return
	 */
	JSON findUploadedRefList(String refFileMark,HttpServletRequest request);
	
	/**
	 * <b>根据id查询Datasource实体</b>
	 * <p> 根据id查询Datasource实体</p>
	 * @param id
	 * @return
	 */
	Ref findOneById(String id);
	
	/**
     *<b>保存一个实体</b>
     *<p> 保存一个实体</p>
     * @author BINZI
     * @param thisRef 实体
     * @return void
     */
	void saveOne(@Valid Ref thisRef, HttpServletRequest request);
	
	 /**
     *<b>根据id删除一个实体</b>
     *<p> 据id删除一个实体</p>
     * @author BINZI
     * @param ID 实体的id
     * @return void
     */
    void removeOne(String Id);
    
    /**
     *<b> 根据id逻辑删除一个实体</b>
     *<p> 据id逻辑删除一个实体</p>
     * @param id
     * @return
     */
    boolean logicRemove(String id);
    
    /**
     *<b>修改一个实体</b>
     *<p> 修改一个实体</p>
     * @author BINZI
     * @param thisRef 实体
     * @return void
     */
	void updateOneById(@Valid Ref thisRef);

    /**
     *<b>Ref的select列表</b>
     *<p> 当前Taxon下的Ref的select检索列表</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findBySelect(HttpServletRequest request);
	/**
     *<b>存储一个新的Datasource实体</b>
     *<p> 存储一个新的Datasource实体</p>
     * @author BINZI
     * @param request 实体
     * @return com.alibaba.fastjson.JSON
     */	
	JSON newOne(@Valid Ref thisRef, HttpServletRequest request);


	/**
	 *<b>构造参考文献</b>
	 *<p> 构造参考文献</p>
	 * @author WangTianshan
	 * @param
	 * @return com.alibaba.fastjson.JSON
	 */
	JSONArray buildRef(HttpServletRequest request);

	/**
	 * <b>构造实体的参考文献</b>
	 * <p> 构造实体的参考文献</p>
	 * @author BINZI
	 * @param refjson
	 */
	JSONArray refactoringRef(String refjson);
	
	/**
	 * <b>构造导出参考文献的数据</b>
	 * <p> 构造导出参考文献的数据</p>
	 * @author BINZI
	 * @param refjson
	 */
	String handleRefjsonForExport(String refjson);

	/**
     *<b>导出当前登录用户的数据源</b>
     *<p> 导出当前登录用户的数据源</p>
     * @author BINZI
	 * @param response
	 * @return
	 */
	void export(HttpServletResponse response) throws IOException;

	/**
	 * <b>列表查询导入的Ref数据</b>
	 * <p> 列表查询导入的Ref数据</p>
	 * @author BINZI
	 * @param file
	 * @param request
	 * @return
	 */
	JSON uploadFile(MultipartFile file, HttpServletRequest request) throws Exception;
	
	/**
	 * <b>将参考文献Id的list集合拼成JSON数组</b>
	 * <p>将参考文献Id的list集合拼成JSON数组</p>
	 * @param refIds
	 * @return
	 */
	JSONArray getRefJSONArrayByIds(List<String> refIds);

	/**
	 * <b>根据录入参考文献的完整题录自动解析出题目、作者、年代等信息</b>
	 * <p> 根据录入参考文献的完整题录自动解析出题目、作者、年代等信息</p>
	 * @param request
	 * @return
	 */
	JSON handleRefstr(HttpServletRequest request);
	
	/**
	 * @Description 采集系统导名录：将根据录入人查询到的对应的参考文献封装成名录数据格式
	 * @author BINZI
	 * @param inputer
	 * @return
	 * @throws Exception
	 */
	List<Reference> findRefByInputerTurnToReference(String inputer) throws Exception;

	/**
	 * @Description 查询指定分类单元集下的Taxon、Citation、Distribution、Commonname的Ref -- 封装成名录RefLink实体数据
	 * @author BINZI
	 * @param taxasetId
	 * @return
	 */
	List<ReferenceLink> getReferenceLinkByTaxaset(String taxasetId);

}
