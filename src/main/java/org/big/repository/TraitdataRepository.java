package org.big.repository;

import java.sql.Timestamp;
import java.util.List;

import org.big.entity.Traitdata;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TraitdataRepository extends BaseRepository<Traitdata, String> {
	/**
     *<b>通过Id查找一个实体</b>
     *<p> 通过Id查找一个实体</p>
     * @author BINZI
     * @param traitdataId
     * @return org.big.entity.Traitdata
     */
	@Query(value = "Select td from Traitdata td where td.id = ?1")
	Traitdata findOneById(String traitdataId);
	
	/**
     *<b>通过Id删除一个实体</b>
     *<p> 通过Id删除一个实体</p>
     * @author BINZI
     * @param traitdataId
     */
	@Modifying
	@Transactional
	@Query("Delete Traitdata td where td.id =?1")
	void deleteOneById(String traitdataId);
	
	/**
     *<b>通过TaxonId查询Taxon下的所有Traitdata实体</b>
     *<p> 通过TaxonId查询Taxon下的所有Traitdata实体</p>
     * @author BINZI
	 * @param pageable
	 * @param taxonId
	 * @return
     */
	@Query(value = "Select t from Traitdata t Where t.taxon.id = ?1 and t.status = 1")
	Page<Traitdata> searchTraitdatasByTaxonId(Pageable pageable, String taxonId);

	/**
	 * <b>根据taxonId修改Taxon下的Traitdatas</b>
	 * <p> 根据taxonId修改Taxon下的Traitdatas</p>
	 * @param request
	 * @return
	 */
	@Query(value = "Select t from Traitdata t Where t.taxon.id = ?1 and t.status = 1")
	List<Traitdata> findTraitdataListByTaxonId(String taxonId);

	/**
	 * <b>导出当前登录用户下的Traitdata数据</b>
	 * <p> 导出当前登录用户下的Traitdata数据</p>
     * @author BINZI
	 * @param uid
	 * @return
	 */
	@Query(value = "Select t from Traitdata t Where t.inputer = ?1 and t.status = 1")
	List<Traitdata> findTraitdataListByUserId(String uid);
	/**
	 * <b>导出当前登录用户下的Traitdata数据</b>
	 * <p> 导出当前登录用户下的Traitdata数据</p>
     * @author BINZI
	 * @param searchText
	 * @param uid
	 * @param pageable
	 * @return
	 */
	@Query(value = "Select t from Traitdata t where ("
			+ "t.expert like %?1% or "
			+ "t.traitjson like %?1% or "
			+ "t.inputtime like %?1% or "
			+ "t.synchdate like %?1%) and "
			+ "t.status = 1 and t.inputer = ?2 and t.synchdate > ?3")
	Page<Traitdata> searchTraitdatasByUid(String searchText, String uid, Timestamp timestamp, Pageable pageable);

	/**
	 * <p><b>根据TaxonId查询与之相关的Traitdata实体的Id</b></p>
	 * <p>根据TaxonId查询与之相关的Traitdata实体的Id</p>
	 * @param id
	 * @return
	 */
	@Query(value = "Select t.id from Traitdata t Where t.taxon.id = ?1 and t.status = 1")
	List<String> findTraitdataIdListByTaxonId(String id);
	/**
	 * @Description 根据TaxonId删除与之相关联的所有特征数据
	 * @author BINZI 
	 * @param taxonId
	 */
	@Transactional
    @Modifying
    @Query(value = "delete from traitdata where taxon_id = ?1", nativeQuery = true)
	void delTraitdataByTaxonId(String taxonId);

}
