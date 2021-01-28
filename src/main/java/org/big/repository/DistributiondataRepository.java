package org.big.repository;

import java.sql.Timestamp;
import java.util.List;

import org.big.entity.Distributiondata;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p><b>Distributiondata的DAO类接口</b></p>
 *<p> Distributiondata的DAO类接口，与User有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/13 10:59</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface DistributiondataRepository extends BaseRepository<Distributiondata, String> {
	/**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param findText 条件关键词，这里是模糊匹配
	 * @param timestamp
	 * @param uid
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Distributiondata>
     */
	@Query(value = "Select dd from Distributiondata dd where ("
			+ "dd.inputer like %?1% or "
			+ "dd.inputtime like %?1% or "
			+ "dd.synchdate like %?1%) and "
			+ "dd.status = 1 and dd.synchdate > ?2 and dd.inputer = ?3")
	Page<Distributiondata> searchInfo(String searchText, Timestamp timestamp, String uid, Pageable pageable);

	/**
     *<b>通过Id查找一个实体</b>
     *<p> 通过Id查找一个实体</p>
     * @author BINZI
     * @param id
     * @return org.big.entity.Distributiondata
     */
	@Query(value = "Select dd from Distributiondata dd where dd.id = ?1")
	Distributiondata findOneById(String id);

	/**
     *<b>通过Id删除一个实体</b>
     *<p> 通过Id删除一个实体</p>
     * @author BINZI
     * @param distributiondataId
     */
	@Modifying
	@Transactional
	@Query("Delete Distributiondata d where d.id =?1")
	void deleteOneById(String distributiondataId);
	
	/**
     *<b>Distributiondata的select列表</b>
     *<p> Distributiondata的select检索列表</p>
     * @author BINZI
     * @param findText
     * @param pageable
     * @return com.alibaba.fastjson.JSON
     */
	@Query(value = "Select d from Distributiondata d where (d.discontent like %?1%) and status = 1")
	Page<Distributiondata> searchByDescriptionInfo(String findText, Pageable pageable);
	
	/**
     *<b>通过TaxonId查询Taxon下的所有Distributiondata实体</b>
     *<p> 通过TaxonId查询Taxon下的所有Distributiondata实体</p>
     * @author BINZI
     * @param taxonId
     */
	@Query(value = "Select d from Distributiondata d Where d.taxon.id = ?1 and status = 1")
	Page<Distributiondata> searchDistributiondatasByTaxonId(Pageable pageable, String taxonId);

	/**
	 * <b>根据taxonId修改Taxon下的Descriptions</b>
	 * <p> 根据taxonId修改Taxon下的Descriptions</p>
	 * @param request
	 * @return
	 */
	@Query(value = "Select d from Distributiondata d Where d.taxon.id = ?1 and status = 1")
	List<Distributiondata> findDistributiondataListByTaxonId(String taxonId);

	/**
	 * <p><b>根据TaxonId查询与之相关的Distributiondata实体的Id</b></p>
	 * <p>根据TaxonId查询与之相关的Distributiondata实体的Id</p>
	 * @param id
	 * @return
	 */
	@Query(value = "Select d.id from Distributiondata d Where d.taxon.id = ?1 and d.status = 1")
	List<String> findDistributiondataIdListByTaxonId(String id);
	/**
	 * @Description 根据录入人查询分布实体
	 * @param id
	 * @return
	 */
	@Query(value = "Select d from Distributiondata d Where d.inputer = ?1 and d.status = 1 and d.discontent is not null")
	List<Distributiondata> findByInputer(String id);
	/**
	 * @Description 查询指定分类单元集下的所有Taxon实体的分布数据
	 * @param taxasetId
	 * @return
	 */
	@Query(value = "select a.id, a.taxon_id, a.geojson, a.discontent from distributiondata a left join taxon b on b.id = a.taxon_id where b.taxaset_id = ?1 and a.geojson is not null",nativeQuery = true)
	List<Object[]> findDistributionByTaxaset(String taxasetId);
	/**
	 * @Description 查询指定分类单元集下的所有Taxon实体的分布数据(针对鱼类数据)
	 * @param taxasetId
	 * @return
	 */
	@Query(value = "select a.id, a.taxon_id, a.discontent from distributiondata a left join taxon b on b.id = a.taxon_id where b.taxaset_id = ?1 and a.discontent is not null",nativeQuery = true)
	List<Object[]> findDistributionByTaxasetForFish(String taxasetId);

	/**
	 * Distribution 查询指定分类单元集下分布的参考文献
	 * @author BINZI
	 * @param taxasetId
	 * @return
	 */	
	@Query(value = "select d.id, d.refjson from distributiondata as d left join taxon as t on d.taxon_id = t.id where t.taxaset_id = ?1 and d.refjson is not null", nativeQuery = true)
	List<Object[]> findReferenceLinkByTaxasetAndDistribution(String taxasetId);

	@Query(value = "select * from distributiondata where taxon_id in("
			+ "select id from taxon where taxaset_id in("
			+ "select id from taxaset where dataset_id = '7da1c0ac-18c6-4710-addd-c9d49e8a2532') and status = 1) and status = 1 and discontent like '%市市辖区%'", nativeQuery = true)
	List<Distributiondata> findDistributiondataByDataset();

	/**
	 * @Description 根据TaxonId删除与之相关联的所有分布数据
	 * @author BINZI 
	 * @param taxonId
	 */
	@Transactional
    @Modifying
    @Query(value = "delete from distributiondata where taxon_id = ?1",nativeQuery = true)
	void delDistributiondataByTaxonId(String taxonId);
}
