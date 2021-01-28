package org.big.service;
import com.alibaba.fastjson.JSON;
import org.big.entity.Dataset;
import org.big.entity.Team;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 *<p><b>Dataset的Service类接口</b></p>
 *<p> Dataset的Service类接口，与Dataset有关的业务逻辑方法</p>
 * @author BINZI
 *<p>Created date: 2018/04/09 17:22</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public interface DatasetService {
	
	/**
     *<b>查询指定团队下的所有Dataset</b>
     *<p> 查询指定团队下的所有Dataset</p>
     * @author BINZI
     * @param request 页面请求
     * @return 
     */
    List<Dataset> findAllDatasetsByTeamId(String teamId);
	
	/**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    JSON findbyInfo(HttpServletRequest request);

    /**
     *<b>带分页排序的条件查询（当前用户组）</b>
     *<p> 带分页排序的条件查询（当前用户组）</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    JSON findMyTeamDatasetsByTid(HttpServletRequest request);

    /**
     *<b>存储媒体实体</b>
     *<p> 存储媒体实体</p>
     * @author BINZI
     * @param thisDataset 实体
     * @return java.lang.String
     */
    void saveOne(Dataset thisDataset);

    /**
     *<b>存储一个新的媒体实体</b>
     *<p> 存储一个新的媒体实体</p>
     * @author BINZI
     * @param thisDataset 实体
     * @return java.lang.String
     */
    void addOne(Dataset thisDataset, HttpServletRequest request);

    /**
     *<b>根据id 逻辑删除一个实体</b>
     *<p> 据id 逻辑删除一个实体</p>
     * @author BINZI
     * @param ID 实体的id
     * @return void
     */
    Boolean logicRemove(String Id);

    /**
     *<b>根据id 物理删除一个实体</b>
     *<p> 据id 物理删除一个实体</p>
     * @author BINZI
     * @param ID 实体的id
     * @return void
     */
    Boolean deleteOne(HttpServletRequest request,String ID);

    /**
     *<b>根据id查找一个实体</b>
     *<p> 据id查找一个实体</p>
     * @author BINZI
     * @param ID 实体的id
     * @return org.big.entity.Dataset
     */
    Dataset findbyID(String ID);

    /**
     *<b>根据dsabstraction查找一个实体</b>
     *<p> 据dsabstraction查找一个实体</p>
     * @author BINZI
     * @param dsabstraction
     * @param thisUser
     * @return org.big.entity.Dataset
     */
    Dataset findOneByDsabstractAndTeam(String dsabstraction,Team thisTeam);

    /**
     *<b>查找默认的那个实体</b>
     *<p> 查找默认的那个实体</p>
     * @author BINZI
     * @return org.big.entity.Dataset
     */
    Dataset findDefaultByUser();


    /**
     *<b>根据userId查找它所属的所有Team信息</b>
     *<p> 根据userId查找它所属的所有Team信息</p>
     * @author WangTianshan(王天山)
     * @param request
     * @return
     */
    JSON findDatasetListByTeamId(String teamId, HttpServletRequest request);
    
    /**
     *<b> 修改一个实体</b>
     *<p> 修改一个实体</p>
     * @param thisDataset
     * @return
     */
	void updateOneById(Dataset thisDataset);

    /**
     *<b>根据TeamId统计有几个dataset</b>
     *<p> 根据TeamId统计有几个dataset</p>
     * @author WangTianshan (王天山)
     * @param teamId
     * @param status
     * @return long
     */
    long countDatasetByTeam_IdAndStatus(String teamId,int status);

    /**
     *<b>选择数据集页面新建数据集</b>
     *<p> 选择数据集页面新建数据集</p>
     * @author BINZI
     * @param thisDataset
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON newOne(Dataset thisDataset, HttpServletRequest request);
	
	/**
     *<b>根据dsname统计Dataset实体个数</b>
     *<p> 根据dsname统计Dataset实体个数</p>
     * @author BINZI
     * @param request 页面请求
     * @return 
     */
	boolean countDatasetsByDsname(HttpServletRequest request);
}