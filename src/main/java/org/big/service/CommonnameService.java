package org.big.service;

import com.alibaba.fastjson.JSON;

import org.big.sp2000.entity.CommonName;
import org.big.entity.Commonname;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *<p><b>Commonname的Service类接口</b></p>
 *<p> Commonname的Service类接口，与Commonname有关的业务逻辑方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public interface CommonnameService {

    /**
     *<b>根据id查找一个实体</b>
     *<p> 据id查找一个实体</p>
     * @author WangTianshan (王天山)
     * @param ID 实体的id
     * @return org.big.entity.Commonname
     */
    Commonname findbyID(String ID);

    /**
     *<b>根据id删除一个实体</b>
     *<p> 据id删除一个实体</p>
     * @author WangTianshan (王天山)
     * @param ID 实体的id
     * @return void
     */
    void removeOne(String ID);

    /**
     *<b>保存一个实体</b>
     *<p> 保存一个实体</p>
     * @author WangTianshan (王天山)
     * @param thisPerson 实体
     * @return void
     */
    void saveOne(Commonname thisPerson);

	/**
	 *<b>Commonname的index页面的列表查询</b>
	 *<p> Commonname的index页面的列表查询</p>
	 * @author BINZI
	 * @param timestamp
	 * @param request
	 * @return com.alibaba.fastjson.JSON
	 */
    JSON findUploadedCommonnameList(Timestamp timestamp, HttpServletRequest request);
    
    /**
	 * <b>添加Commonname基础数据</b>
	 * <p> 添加Commonname基础数据</p>
	 * @author BINZI
	 * @param thisCommonname
	 */
	JSON addCommonname(String taxonId, HttpServletRequest request);
	
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
	 *<b>根据taxonId查找Taxon下的所有Commonname实体</b>
     *<p> 根据taxonId查找Taxon下的所有Commonname实体</p>
	 * @param taxonId
	 * @param request
	 * @return
	 */
	JSON findCommonnameListByTaxonId(String taxonId, HttpServletRequest request);

	/**
	 * <b>根据taxonId修改Taxon下的Commonnames</b>
	 * <p> 根据taxonId修改Taxon下的Commonnames</p>
	 * @param taxonId
	 * @return
	 */
	JSON editCommonname(String taxonId);
	
	/**
	 * <b>导出当前登录用户的Commonname数据</b>
	 * <p>导出当前登录用户的Commonname数据</p>
	 * @author BINZI
	 * @param response
	 * @throws 
	 */
	void export(HttpServletResponse response) throws IOException;

	/**
	 * <b>解析导入的Commonname相关的Excel文件</b>
	 * <p> 解析导入的Commonname相关的Excel文件</p>
	 * @author BINZI
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	JSON uploadFile(MultipartFile file, String taxasetId, HttpServletRequest request) throws Exception;
	/**
	 * @Description 查询指定分类单元集下的所有Taxon的俗名数据 -- 封装成名录CommonName实体数据
	 * @author BINZI
	 * @param taxasetId
	 * @return
	 */
	List<CommonName> getCommonNameByTaxaset(String taxasetId);

}
