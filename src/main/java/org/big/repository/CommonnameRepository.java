package org.big.repository;

import java.sql.Timestamp;
import java.util.List;

import org.big.entity.Commonname;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p><b>Commonname的DAO类接口</b></p>
 *<p> Commonname的DAO类接口，与Commonname有关的持久化操作方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface CommonnameRepository extends BaseRepository<Commonname, String> {

    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author WangTianshan (王天山)
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Commonname>
     */
    @Query(value = "select c from Commonname c where (c.commonname like %?1% or c.language like %?1%) and"
    		+ " c.status = 1 and c.synchdate > ?2 and c.inputer = ?3")
    Page<Commonname> searchInfo(String findText, Timestamp timestamp, String uid, Pageable pageable);
  
	/**
     *<b>通过Id查找一个实体</b>
     *<p> 通过Id查找一个实体</p>
     * @author BINZI
     * @param id
     * @return org.big.entity.Commonname
     */
	@Query(value = "Select cn from Commonname cn where cn.id = ?1")
	Commonname findOneById(String commonnameId);

	/**
     *<b>通过Id删除一个实体</b>
     *<p> 通过Id删除一个实体</p>
     * @author BINZI
     * @param taxkeyId
     */
	@Modifying
	@Transactional
	@Query("Delete Commonname cn where cn.id =?1")
	void deleteOneById(String commonnameId);

	/**
     *<b>通过TaxonId查询Taxon下的所有Commonname实体</b>
     *<p> 通过TaxonId查询Taxon下的所有Commonname实体</p>
     * @author BINZI
     * @param taxonId
     */
	@Query(value = "Select c from Commonname c Where c.taxon.id = ?1 and c.status = 1")
	Page<Commonname> searchCommonnamesByTaxonId(Pageable pageable, String taxonId);

	/**
	 * <b>根据taxonId修改Taxon下的Commonnames</b>
	 * <p> 根据taxonId修改Taxon下的Commonnames</p>
	 * @param taxonId
	 * @return
	 */
	@Query(value = "Select c from Commonname c Where c.taxon.id = ?1 and c.status = 1")
	List<Commonname> findCommonnameListByTaxonId(String taxonId);

	/**
	 * <b>导出当前登录用户下的Commonname数据</b>
	 * <p> 导出当前登录用户下的Commonname数据</p>
	 * @param request
	 * @return
	 */
	@Query(value = "Select c from Commonname c Where c.inputer = ?1 and c.status = 1")
	List<Commonname> findCommonnameListByUserId(String uid);

	/**
	 * <b>上传Commonname数据根据俗名和Taxo的Id去重</b>
	 * <p> 上传Commonname数据根据俗名和Taxo的Id去重</p>
	 * @param commonname
	 * @param tid
	 * @return
	 */
	@Query(value = "Select c from Commonname c Where c.commonname = ?1 and c.taxon.id = ?2 and c.status = 1")
	List<String> findIdsByCommonnameAndTaxonId(String commonname, String tid);

	/**
	 * <p><b>根据TaxonId查询与之相关的Commonname实体的Id</b></p>
	 * <p>根据TaxonId查询与之相关的Commonname实体的Id</p>
	 * @param id
	 * @return
	 */
	@Query(value = "Select c.id from Commonname c Where c.taxon.id = ?1 and c.status = 1")
	List<String> findCommonnameIdListByTaxonId(String id);
	/**
	 * @Description 查询指定分类单元集下所有的Taxon的俗名数据
	 * @author BINZI
	 * @param taxasetId
	 * @return
	 */
	@Query(value = "select c.id as cid, t.id as tid, c.commonname, c.language, t.rank_id from commonname as c left join taxon as t on t.id = c.taxon_id left join rank as r on t.rank_id = r.id where t.taxaset_id = ?1", nativeQuery = true)
	List<Object[]> findCommonNameByTaxaset(String taxasetId);

	/**
	 * @Description 查询指定分类单元集下俗名的参考文献
	 * @author BINZI
	 * @param taxasetId
	 * @return
	 */	
	@Query(value = "select c.id, c.refjson, t.authorstr from commonname as c left join taxon as t on c.taxon_id = t.id where t.taxaset_id = ?1 and c.refjson is not null", nativeQuery = true)
	List<Object[]> findReferenceLinkByTaxasetAndCommonname(String taxasetId);
	/**
	 * @Description 根据TaxonId删除与之相关联的所有俗名
	 * @author BINZI 
	 * @param taxonId
	 */
	@Transactional
    @Modifying
    @Query(value = "delete from commonname where taxon_id = ?1",nativeQuery = true)
	void delCommonnameByTaxonId(String taxonId);
	@Query(value = "Select * from commonname Where language = ?1 and inputer = ?2", nativeQuery = true)
	List<Commonname> findByLangAndInputer(String lang, String inputer);
	
	@Query(value = "select * from commonname where taxon_id in ("
			+ "select id from taxon where taxaset_id = ?1) and remark is not null", nativeQuery = true)
	List<Commonname> findCommonnameListByTaxasetIdAndRemark(String taxasetId);

	@Query(value = "select * from commonname where taxon_id in ("
			+ "select id from taxon where taxaset_id = ?1)", nativeQuery = true)
	List<Commonname> findCommonnameListByTaxasetId(String taxasetId);

	@Query(value = "select * from commonname where commonname = ?1 and taxon_id in ("
			+ "select id from taxon where taxaset_id = ?2) limit 1", nativeQuery = true)
	Commonname findOneByCommonnameAndTaxasetId(String name, String taxasetId);
}
