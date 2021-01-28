package org.big.repository;

import org.big.entity.TaxonHasTaxtree;
import org.big.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *<p><b>Traitset的DAO类接口</b></p>
 *<p> Traitset的DAO类接口，与Traitontology有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/22 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface TaxonHasTaxtreeRepository extends BaseRepository<TaxonHasTaxtree, String> {
	/**
	 *<b>根据taxonId&taxtreeId查找一个TaxonHasTaxtree实体</b>
	 *<p> 据id查找一个实体</p>
	 * @author WangTianshan (王天山)
	 * @param taxonId
	 * @param taxtreeId
	 * @return org.big.entity.TaxonHasTaxtree
	 */
	@Query(value = "Select tht From TaxonHasTaxtree tht Where tht.taxonId = ?1 and tht.taxtreeId = ?2 ")
	TaxonHasTaxtree findOneByIds(String taxonId,String taxtreeId);

	/**
	 *<b>根据taxonId&taxtreeId查找所有子节点</b>
	 *<p> 根据taxonId&taxtreeId查找所有子节点</p>
	 * @author WangTianshan (王天山)
	 * @param taxonId
	 * @param taxtreeId
	 * @return org.springframework.data.domain.Page<org.big.entity.Team
	 */
	/*@Query(value = "select tt.* "
			+ "from taxon_has_taxtree as tt "
			+ "left join "
			+ "taxon as t on tt.taxon_id = t.id "
			+ "where "
			+ "tt.pid = ?1 and tt.taxtree_id = ?2 order by t.scientificname asc", nativeQuery = true)*/
	List<TaxonHasTaxtree> findTaxonHasTaxtreesByPidAndAndTaxtreeId(String taxonId,String taxtreeId);

	/**
	 *<b>根据taxonId&taxtreeId查找所有子节点</b>
	 *<p> 根据taxonId&taxtreeId查找所有子节点</p>
	 * @author WangTianshan (王天山)
	 * @param taxonId
	 * @param taxtreeId
	 * @return org.springframework.data.domain.Page<org.big.entity.Team
	 */
	@Query(value = "Select tht From TaxonHasTaxtree tht Where tht.pid = ?1 and tht.taxtreeId = ?2 ")
	List<TaxonHasTaxtree> findChildren(String taxonId,String taxtreeId);

	/**
	 *<b>根据taxonId&taxtreeId查找子节点个数</b>
	 *<p> 根据taxonId&taxtreeId查找子节点个数</p>
	 * @author WangTianshan (王天山)
	 * @param taxonId
	 * @param taxtreeId
	 * @return org.springframework.data.domain.Page<org.big.entity.Team
	 */
	int countTaxonHasTaxtreesByPidAndTaxtreeId(String taxonId,String taxtreeId);

	/**
	 *<b>根据taxonId&taxtreeId删除节点</b>
	 *<p> 根据taxonId&taxtreeId删除节点</p>
	 * @author WangTianshan (王天山)
	 * @param taxonId
	 * @param taxtreeId
	 * @return void
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from TaxonHasTaxtree tht where tht.taxonId = ?1 and tht.taxtreeId = ?2")
	void removeByTaxonIdAndTaxtreeId(String taxonId,String taxtreeId);

	/**
	 *<b>根据taxonId&taxtreeId删除子节点</b>
	 *<p> 根据taxonId&taxtreeId删除子节点</p>
	 * @author WangTianshan (王天山)
	 * @param taxonId
	 * @param taxtreeId
	 * @return void
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from TaxonHasTaxtree tht where tht.pid = ?1 and tht.taxtreeId = ?2 ")
	void removeByPidAndTaxtreeId(String taxonId,String taxtreeId);


	/**
	 *<b>根据taxonId&taxtreeId找到下一个节点</b>
	 *<p> 根据taxonId&taxtreeId找到下一个节点</p>
	 * @author WangTianshan (王天山)
	 * @param taxonId
	 * @param taxtreeId
	 * @return org.springframework.data.domain.Page<org.big.entity.Team
	 */
	TaxonHasTaxtree findTaxonHasTaxtreesByPrevTaxonAndTaxtreeId(String taxonId,String taxtreeId);
	/**
	 * @Description 根据pId、taxteeeId查询分类树根节点
	 * @param pid
	 * @param taxtreeId
	 * @return
	 */
	@Query(value = "select tht from TaxonHasTaxtree tht where tht.pid = ?1 and tht.taxtreeId = ?2 ")
	List<TaxonHasTaxtree> findRootNodeByTaxtreeId(String pid,String taxtreeId);
	/**
	 * @Description 根据分类树查询对应关系
	 * @param taxtreeId
	 * @return
	 */
	List<TaxonHasTaxtree> findByTaxtreeId(String taxtreeId);
	/**
	 * @Description 根据taxonId删除分类树节点
	 * @param taxonId
	 */
	@Transactional
	@Modifying
	@Query(value = "delete from taxon_has_taxtree where taxon_id = ?1", nativeQuery = true)
	void delTaxonHasTaxtreesByTaxonId(String taxonId);
}
