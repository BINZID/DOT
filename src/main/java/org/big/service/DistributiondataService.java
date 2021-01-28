package org.big.service;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.big.sp2000.entity.Distribution;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

public interface DistributiondataService {
	/**
	 *<b>Distributiondata的index页面的列表查询</b>
	 *<p> Distributiondata的index页面的列表查询</p>
	 * @author BINZI
	 * @param timestamp
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON findUploadedDistributiondataList(Timestamp timestamp, HttpServletRequest request);
	/**
	 * <b> 添加Distributiondata实体</b>
	 * <p> 添加Distributiondata实体</p>
	 * @author BINZI
	 * @param thisDistributiondata
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON addDistributiondata(String taxonId, HttpServletRequest request);

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
     *<b>Distributiondata的select列表</b>
     *<p> Distributiondata的select检索列表</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findBySelect(HttpServletRequest request);
	
	/**
	 *<b>根据taxonId查找Taxon下的所有Distributiondata实体</b>
     *<p> 根据taxonId查找Taxon下的所有Distributiondata实体</p>
	 * @param request
	 * @return
	 */
	JSON findDistributiondataListByTaxonId(String taxonId, HttpServletRequest request);

	/**
	 * <b>根据taxonId修改Taxon下的Distributiondata</b>
	 * <p> 根据taxonId修改Taxon下的Distributiondata</p>
	 * @param request
	 * @return
	 */
	JSON editDistributiondata(String taxonId);

	/**
	 * <b>解析导入的Distributiondata相关的Excel文件</b>
	 * <p> 解析导入的Distributiondata相关的Excel文件</p>
	 * @author BINZI
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	JSON uploadFile(MultipartFile file, String taxasetId, HttpServletRequest request) throws Exception;
	
	/**
     * <b>根据分布描述内容自动解析省份</b>
     * <p> 根据分布描述内容自动解析省份</p>
     * @author BIZNI
     * @param disContent
     * @return
     */
	JSON parseGeoobject(String disContent, String parseType);
	
	/**
	 * @Description 查询指定分类单元集下所有Taxon的分布数据 -- 拼装成名录的Distribution实体数据
	 * @author ZXY
	 * @param taxasetId
	 * @return
	 */
	List<Distribution> getDistributionByTaxaset(String taxasetId);
	/**
	 * @Description 查询指定分类单元集下所有Taxon的分布数据 -- 拼装成名录的Distribution实体数据(针对鱼类数据)
	 * @author BINZI
	 * @param taxasetId
	 * @return
	 */
	List<Distribution> getDistributionByTaxasetForFish(String taxasetId);
	
	JSON handelDistributionData();
	
	String getGeoJSON(String geojsonStr);

}
