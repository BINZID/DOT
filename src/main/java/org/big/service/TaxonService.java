package org.big.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.alibaba.fastjson.JSONArray;

import org.big.sp2000.entity.Family;
import org.big.sp2000.entity.ScientificName;
import org.big.entity.Rank;
import org.big.entity.Taxaset;
import org.big.entity.Taxon;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

public interface TaxonService {

    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findTaxonList(HttpServletRequest request);
	
	/**
	 * <b>添加Taxon基础数据</b>
	 * <p> 添加Taxon基础数据</p>
	 * @author BINZI
	 * @param thisTaxon
	 */
	JSON addTaxonBaseInfo(@Valid Taxon thisTaxon, HttpServletRequest request);
	// 自定义Taxon增删改查
	 /**
     *<b>根据TaxonId查找一个Taxon实体</b>
     *<p> 据id查找一个实体</p>
     * @author BINZI
     * @param id 实体的id
     * @return org.big.entity.Taxon
     */
	Taxon findOneById(String id);

	/**
     *<b>将Form1的Reference相关属性拼装成JSON数据</b>
     *<p> 将Form1的Reference相关属性拼装成JSON数据</p>
     * @author BINZI
     * @param request
     * @return com.alibaba.fastjson.JSON
     */
	JSON handleReferenceToJson(HttpServletRequest request);
	
	/**
     *<b> 根据id逻辑删除一个实体</b>
     *<p> 据id逻辑删除一个实体</p>
     * @param id
     * @return
     */
	boolean delTaxon(String id);
	
	/**
     *<b> 根据id修改一个实体</b>
     *<p> 据id修改一个实体</p>
     * @param thisTaxon
     * @return
     */
	void updateOneById(@Valid Taxon thisTaxon);

	/**
     *<b>Taxon的select列表</b>
     *<p> 当前Taxkey下的Taxon的select检索列表</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findBySelect(HttpServletRequest request, String taxonsetId);

	/**
	 *<b>根据Rank和TaxonSet返回TaxonList </b>
	 *<p> 根据Rank和TaxonSet返回TaxonList</p>
	 * @author WangTianshan (王天山)
	 * @param thisTaxaset TaxonSet
	 * @param thisRank Rank
	 * @return org.springframework.data.domain.Page<org.big.entity.Taxon>
	 */
	JSONArray findTaxonByTaxasetAndRank(Taxaset thisTaxaset, Rank thisRank,int status);

	/**
	 *<b>根据Rank和TaxonSet和关键名字返回TaxonList </b>
	 *<p> 根据Rank和TaxonSet和关键名字返回TaxonList</p>
	 * @author WangTianshan (王天山)
	 * @param thisTaxaset TaxonSet
	 * @param thisRank Rank
	 * @param taxonName 关键名字
	 * @return org.springframework.data.domain.Page<org.big.entity.Taxon>
	 */
	JSONArray findTaxonByTaxasetAndRankAndName(Taxaset thisTaxaset, Rank thisRank,String taxonName,int status);

	/**
	 *<b>根据TaxonId确定所属分类单元集</b>
	 *<p> 根据TaxonId确定所属分类单元集</p>
	 * @author BINZI
	 * @param request
	 * @param id
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON findTaxasetIdByTaxonId(HttpServletRequest request, String id);

	/**
	 *<b>根据TaxonId确定分类单元</b>
	 *<p> 根据TaxonId确定分类单元</p>
	 * @author  WangTianshan (王天山)
	 * @param id
	 * @return
	 */
	JSON findTaxonBasics(String id);


	/**
	 *<b>修改审核状态</b>
	 *<p> 修改审核状态</p>
	 * @author WangTianshan (王天山)
	 * @param id 实体的id
	 * @return org.big.entity.Taxon
	 */
	void ReviewByTaxonId(String id,int reviewStatus);

	  
    /**
     * <b>导出指定分类单元集下的Taxon数据</b>
     * <p> 导出指定分类单元集下的Taxon数据</p>
     * @author BINZI
     * @param taxasetId
     * @param @return
     */
	void export(String taxasetId, HttpServletResponse response) throws IOException;

	/**
	 * <b>解析导入的Taxon相关的Excel文件</b>
	 * <p> 解析导入的Taxon相关的Excel文件</p>
	 * @author BINZI
	 * @param file
	 * @param request
	 * @return
	 */
	JSON uploadFile(MultipartFile file, String taxasetId, HttpServletRequest request) throws Exception;

	/**
	 *<b>Taxon的index页面的列表查询</b>
	 *<p> Taxon的index页面的列表查询</p>
	 * @author BINZI
	 * @param taxasetId
	 * @param timestamp
	 * @param request
	 * @return
	 */
	JSON findUploadedTaxonList(String taxasetId, Timestamp timestamp, HttpServletRequest request);
	/**
	 * @Description 为实体匹配异名参考文献
	 * @author BINZI
	 * @param path
	 * @throws Exception
	 */
	void matchSynonymRef(String path) throws Exception;

	/**
	 * @Description 为实体匹配接受名参考文献
	 * @author BINZI
	 * @param path
	 * @throws Exception
	 */
	void matchAcceptRef(String path) throws Exception;
	/**
	 * @Description 鱼类属下亚种种加词/亚种加词处理
	 */
	void handleFishTaxon();

	void handleDistributiondata();
	/**
	 * @Description 鸟类学名处理
	 */
	void handleTaxonSciname();
	/**
	 * @Description 引证
	 */
	void handleCitationSciname();
	/**
	 * @Description 查询指定分类单元集下的高阶元Taxon，并封装成名录Family数据
	 * @author ZXY
	 * @param taxasetId
	 * @param taxtreeId
	 * @return
	 */
	List<Family> getFamilyDataByTaxaset(String taxasetId, String taxtreeId);
	/**
	 * @Description 查询指定分类单元集下的低阶元Taxon，并封装成名录ScientificName数据
	 * @author ZXY
	 * @param taxasetId
	 * @param taxtreeId
	 * @return
	 */
	List<ScientificName> getScientificNamesByTaxaset(String taxasetId, String taxtreeId);
	/**
	 * @Description 分布数据
	 * @param path
	 */
	void handleTaxon(String path) throws Exception;

	void handleDistribution(String path) throws Exception;

	void handleGeoobjectChange(String path) throws Exception;

	void handleButterfly(String path) throws Exception;
	/**
	 * 通过拉丁名和录入人查Taxon对象
	 * @param scientificname
	 * @param id
	 * @return
	 */
	Taxon findOneByScientificnameAndTaxasetId(String scientificname, String taxasetId);
	/**
	 * 整理鱼类数据
	 * @param taxasetId
	 * @param rank
	 */
	void getFishByTreeNodeId(String treeNodeId);

	void getFishDatasByTaxasetIdAndRank(String taxasetId);

	void getFishRemarkByTaxasetIdAndRank(String taxasetId);

	void parseRedlistDatas(String path) throws Exception;

	void handleAmphbiaCitation(String taxasetId);

	void formatAmphbiaDis(String taxasetId);

	void parseAmphbiaCommonname(String path) throws Exception;

	void parseAmphbiaRefs(String inputer);

	void citationMatchRefOfAmphbia(String sourcesId);

	void handleAmphbiaTaxon(String inputer);

	void getChordataCode(String path, String reptilia, String amphbia) throws Exception;

	void wordTransExcel(String path) throws Exception;

	Taxon findOneByScientificnameAndInputer(String scientificname, String inputer);

}
