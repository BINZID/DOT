package org.big.repository;

import java.sql.Timestamp;
import java.util.List;

import org.big.entity.Description;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p><b>Description的DAO类接口</b></p>
 *<p> Description的DAO类接口，与User有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/13 10:59</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface DescriptionRepository extends BaseRepository<Description, String> {
	/**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Citation>
     */
	@Query(value = "Select d from Description d where ("
			+ "d.destitle like %?1% or "
			+ "d.describer like %?1% or "
			+ "d.desdate like %?1% or "
			+ "d.rightsholder like %?1% or "
			+ "d.language like %?1% or "
			+ "d.destypeid like %?1% or "
			+ "d.inputer like %?1% or "
			+ "d.inputtime like %?1% or "
			+ "d.synchdate like %?1%) and "
			+ "d.status = 1 and d.synchdate > ?2 and d.inputer = ?3")
	Page<Description> searchInfo(String searchText, Timestamp timestamp, Pageable pageable, String uid);

	/**
     *<b>通过Id查找一个实体</b>
     *<p> 通过Id查找一个实体</p>
     * @author BINZI
     * @param id
     * @return org.big.entity.Description
     */
	@Query(value = "Select d from Description d where d.id = ?1")
	Description findOneById(String id);
	
	/**
     *<b>通过Id删除一个实体</b>
     *<p> 通过Id删除一个实体</p>
     * @author BINZI
     * @param descriptionId
     */
	@Modifying
	@Transactional
	@Query("Delete Description d where d.id =?1")
	void deleteOneById(String descriptionId);
	
	/**
     *<b>Traitset的select列表</b>
     *<p> Traitset的select检索列表</p>
     * @author BINZI
     * @param findText
     * @param pageable
     * @return com.alibaba.fastjson.JSON
     */
	@Query(value = "Select d from Description d where (d.destitle like %?1%) and d.status = 1")
	Page<Description> searchByDescriptionInfo(String findText, Pageable pageable);
	
	/**
     *<b>通过TaxonId查询Taxon下的所有Description实体</b>
     *<p> 通过TaxonId查询Taxon下的所有Description实体</p>
     * @author BINZI
     * @param taxonId
     */
	@Query(value = "Select d from Description d Where d.taxon.id = ?1 and d.status = 1")
	Page<Description> searchDescriptionsByTaxonId(Pageable pageable, String taxonId);

	/**
	 * <b>根据taxonId修改Taxon下的Descriptions</b>
	 * <p> 根据taxonId修改Taxon下的Descriptions</p>
	 * @param taxonId
	 * @return
	 */
	@Query(value = "Select d from Description d Where d.taxon.id = ?1 and d.status = 1")
	List<Description> findDescriptionListByTaxonId(String taxonId);

	/**
	 *<b>根据dataset统计符合条件的Description</b>
	 *<p> 根据dataset统计符合条件的Description</p>
	 * @author WangTianshan (王天山)
	 * @param status 状态
	 * @param id Dataset的id
	 * @return long
	 */
	long countByStatusAndTaxon_Taxaset_Dataset_Id(int status,String id);

	/**
	 * <b>Taxon下的描述信息实体的关系下拉选</b>
	 * <p> Taxon下的描述信息实体的关系下拉选</p>
	 *  @author BINZI
	 * @param findText
	 * @param taxonId
	 * @param pageable
	 * @return
	 */
	@Query(value = "Select d from Description d Where (d.destitle like %?1%) and d.taxon.id = ?2 and d.status = 1")
	Page<Description> searchDescListByTaxonId(String findText, String taxonId, Pageable pageable);

	/**
	 * <b>导出当前登录用户下的Description数据</b>
	 * <p> 导出当前登录用户下的Description数据</p>
	 * @param request
	 * @return
	 */
	@Query(value = "Select d from Description d Where d.inputer = ?1 and d.status = 1")
	List<Description> findDescriptionListByUserId(String uid);

	/**
	 * <p><b>根据TaxonId查询与之相关的Description实体的Id</b></p>
	 * <p>根据TaxonId查询与之相关的Description实体的Id</p>
	 * @param id
	 * @return
	 */
	@Query(value = "Select d.id from Description d Where d.taxon.id = ?1 and d.status = 1")
	List<String> findDescriprionIdListByTaxonId(String id);

	/**
	 * @Description 根据TaxonId删除与之相关联的所有描述数据
	 * @author BINZI 
	 * @param taxonId
	 */
	@Transactional
    @Modifying
    @Query(value = "delete from description where taxon_id = ?1",nativeQuery = true)
	void delDescriptionByTaxaSetId(String taxonId);
	/**
	 * 根据录入人检索指定描述类型的物种描述
	 * @param id
	 * @param types
	 * @return
	 */
	@Query(value = "Select * from description Where inputer = ?1 and descriptiontype_id in ?2", nativeQuery = true)
	List<Description> findDescListByInputerAndType(String id, String[] types);
	/**
	 * 根据录入人检索可用的物种描述
	 * @param inputer
	 * @return
	 */
	@Query(value = "Select * from description Where inputer = ?1 and status = 1", nativeQuery = true)
	List<Description> findAllByInputer(String inputer);
	/**
	 * 检索指定分了单元集下特定描述类型的物种描述（左匹配描述内容）
	 * @param type
	 * @param mark
	 * @param taxasetId
	 * @return
	 */
	@Query(value = "select * from description where destypeid = ?1 and descontent like %?2% and taxon_id in("
			+ "select id from taxon where taxaset_id = ?3)", nativeQuery = true)
	List<Description> findListByTypeAndTaxasetId(String type, String mark, String taxasetId);
	/**
	 * 查询指定分类单元集下的物种描述(描述的remark字段非空)
	 * @param taxasetId
	 * @return
	 */
	@Query(value = "select * from description where taxon_id in ("
			+ "select id from taxon where taxaset_id = ?1) and remark is not null", nativeQuery = true)
	List<Description> findDescriptionListByTaxasetIdAndRemark(String taxasetId);
	
	/**
	 * 查询指定分类单元集下的物种描述
	 * @param taxasetId
	 * @return
	 */
	@Query(value = "select * from description where taxon_id in ("
			+ "select id from taxon where taxaset_id = ?1)", nativeQuery = true)
	List<Description> findDescriptionListByTaxasetId(String taxasetId);
	/**
	 * 根据物种Id和描述类型查询物种描述
	 * @param taxonId
	 * @param desctype
	 * @return
	 */
	@Query(value = "select * from description where taxon_id = ?1 and descriptiontype_id = ?2", nativeQuery = true)
	List<Description> findDescListByDesctypeAndTaxonId(String taxonId, String desctype);
	/**
	 * 根据数据源Id和描述类型查询物种描述
	 * @param sourcesId
	 * @param string
	 * @return
	 */
	@Query(value = "select * from description where sourcesid = ?1 and descriptiontype_id = ?2", nativeQuery = true)
	List<Description> findDescListByDesctypeAndSourcesId(String sourcesId, String string);
	/**
	 * 检索指定分类单元集下的物种的描述（左匹配描述内容和描述标题）
	 * @param descontent
	 * @param latin
	 * @param taxasetId
	 * @return
	 */
	@Query(value = "select * from description where descontent like ?1% and destitle like ?2% and taxon_id in ("
			+ "select id from taxon where taxaset_id = ?3) limit 1", nativeQuery = true)
	Description findOneByContentAndLatinAndTaxasetId(String descontent, String latin, String taxasetId);


}
