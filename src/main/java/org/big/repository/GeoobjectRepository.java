package org.big.repository;

import java.util.List;

import org.big.entity.Geoobject;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *<p><b>Geoobject的DAO类接口</b></p>
 *<p> Geoobject的DAO类接口，与User有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/11 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface GeoobjectRepository extends BaseRepository<Geoobject, String> {

    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param searchText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Taxaset>
     */
    @Query(value = "Select go from Geoobject go where ("
    		+ "go.cngeoname like %?1% or "
    		+ "go.engeoname like %?1%) and "
    		+ "go.status = 1")
	Page<Geoobject> searchInfo(String searchText, Pageable pageable, String geogroup_id);
    
    /**
     *<b>根据id查询一个Geoobject实体</b>
     *<p> 根据id查询一个Geoobject实体</p>
     * @author BINZI
     * @param pageable 分页排序方案实体
     * @return org.big.entity.Geoobject
     */
    @Query(value = "Select go from Geoobject go where go.id = ?1 and go.status = 1")
    Geoobject findOneById(String geoobjectId);
	/**
	 *<b>根据adcode查询对应地理实体的id</b>
     *<p> 根据adcode查询对应地理实体的id</p> 
	 * @param dismark
	 * @return
	 */
    @Query(value = "Select go.id from Geoobject go where go.adcode = ?1 and go.status = 1")
	String findIdByAdcode(String adcode);
    /**
	 *<b>根据province 简称查询对应地理实体的id</b>
     *<p> 根据province 简称查询对应地理实体的id</p> 
	 * @param city
	 * @return
	 */
    @Query(value = "Select go.id from geoobject go where go.short_name = ?1 and go.status = 1 limit 1", nativeQuery = true)
	String findGidByShortName(String province);
    /**
     *<b>根据province 全称查询对应地理实体的id</b>
     *<p> 根据province 全称查询对应地理实体的id</p> 
     * @param city
     * @return
     */
    @Query(value = "Select go.id from geoobject go where go.cngeoname = ?1 and go.status = 1 limit 1", nativeQuery = true)
    String findGidByCngeoname(String province);
	/**
	 * <p><b> 查询分布地名称</b></p>
	 * <p>查询分布地名称</p>
	 * @return
	 */
    @Query(value = "Select go.cngeoname from Geoobject go where go.pid = ?1")
	List<String> findGeoobjectCountryCngeoname(String pid);

    /**
	 * <p><b> 查询行政分布地省名称</b></p>
	 * <p>查询行政分布地省名称</p>
	 * @return
	 */
    @Query(value = "Select go.cngeoname from Geoobject go where go.pid like %?1%")
	List<String> findGeoobjectProvinceCngeoname(String pid);
    
    /**
	 * <p><b> 查询行政分布地市名称</b></p>
	 * <p>查询行政分布地市名称</p>
	 * @return
	 */
    @Query(value = "Select go.cngeoname from Geoobject go where go.pid like ?1 and go.pid != ?2")
	List<String> findGeoobjectCityCngeoname(String pid2, String pid1);
    
    /**
     * <p><b> 查询行政分布地县名称</b></p>
     * <p>查询行政分布地县名称</p>
     * @return
     */
    @Query(value = "Select go.cngeoname from Geoobject go where go.pid not like ?1 and go.pid != 0")
	List<String> findGeoobjectCountyCngeoname(String pid2);

    /**
	 * <p><b> 查询地理分布区名称</b></p>
	 * <p>查询地理分布区名称</p>
	 * @return
	 */
    @Query(value = "Select go.cngeoname from Geoobject go where go.pid != '0' and go.citycode is null")
	List<String> findGeoobjectArealCngeoname();
    
    @Query(value = "Select * from geoobject as geo where length(geo.pid) = 6", nativeQuery = true)
	List<Geoobject> findAllGeoobject();
    
    @Query(value = "Select * from geoobject", nativeQuery = true)
    List<Geoobject> findGeoobjectList();
    /**
     * @Description 根据id、pid查询省份分布实体
     * @author BINZI
     * @param geoId
     * @param pid
     * @return
     */
    @Query(value = "Select go from Geoobject go where go.id = ?1 and go.status = 1 and go.pid = ?2")
	Geoobject findOneByIdAndPid(String geoId, String pid);
    /**
     * 简称查全称
     * @param province
     * @return
     */
    @Query(value = "Select go.cngeoname from Geoobject go where go.shortName = ?1 and go.status = 1 and go.adcode like '%0000%'")
	String getProvinceByShortName(String province);
}