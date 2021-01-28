package org.big.repository;

import java.sql.Timestamp;
import java.util.List;

import org.big.entity.Citation;

import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;

/**
 *<p><b>Citation的DAO类接口</b></p>
 *<p> Citation的DAO类接口，与User有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/11 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface CitationRepository extends BaseRepository<Citation, String> {
	/**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param findText 条件关键词，这里是模糊匹配
     * @param timestamp
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Citation>
     */
	@Query(value = "Select c from Citation c where ("
			+ "c.sciname like %?1% or "
			+ "c.authorship like %?1% or "
			+ "c.nametype like %?1% or "
			+ "c.inputer like %?1% or "
			+ "c.inputtime like %?1% or "
			+ "c.synchdate like %?1%) and "
			+ "c.status = 1 and c.synchdate > ?2 and c.inputer = ?3")
	Page<Citation> searchInfo(String searchText, Timestamp timestamp, Pageable pageable, String uid);

	/**
	 *<b>根据id查询一个实体</b>
     *<p> 带分页排序的条件查询</p>
	 * @param id
	 * @return
	 */
	@Query(value = "Select c from Citation c Where c.id = ?1")
	Citation findOneById(String id);
	
	/**
     *<b>通过Id删除一个实体</b>
     *<p> 通过Id删除一个实体</p>
     * @author BINZI
     * @param citationId
     */
	@Modifying
	@Transactional
	@Query("Delete Citation c where c.id =?1")
	void deleteOneById(String citationId);
	
	/**
     *<b>通过TaxonId查询Taxon下的所有Citation实体</b>
     *<p> 通过TaxonId查询Taxon下的所有Citation实体</p>
     * @author BINZI
     * @param taxonId
     */
	@Query(value = "Select c from Citation c Where c.taxon.id = ?1 and c.status = 1")
	Page<Citation> searchCitationsByTaxonId(Pageable pageable, String taxonId);
	
	/**
	 * <b>根据taxonId修改Taxon下的Citations</b>
	 * <p> 根据taxonId修改Taxon下的Citations</p>
	 * @param request
	 * @return
	 */
	@Query(value = "Select c from Citation c Where c.taxon.id = ?1 and c.status = 1")
	List<Citation> findCitationListByTaxonId(String taxonId);

	/**
	 * <b>导出当前登录用户下的Citation数据</b>
	 * <p> 导出当前登录用户下的Citation数据</p>
	 * @param request
	 * @return
	 */
	@Query(value = "Select c from Citation c Where c.inputer = ?1 and c.status = 1")
	List<Citation> findCitationListByUserId(String uid);

	/**
	 * <b>上传Citation数据根据俗名和Taxo的Id去重</b>
	 * <p> 上传Citation数据根据俗名和Taxo的Id去重</p>
	 * @param commonname
	 * @param tid
	 * @return
	 */
	@Query(value = "Select c from Citation c Where c.sciname = ?1 and c.taxon.id = ?2 and c.status = 1")
	JSONArray findIdsByScinameAndTaxonId(String sciname, String tid);
	
	/**
	 * <p><b>根据TaxonId查询与之相关的Citation实体的Id</b></p>
	 * <p>根据TaxonId查询与之相关的Citation实体的Id</p>
	 * @param id
	 * @return
	 */
	@Query(value = "Select c.id from Citation c Where c.taxon.id = ?1 and c.status = 1")
	List<String> findCitationIdListByTaxonId(String id);
	/**
	 * @Description 根据nametype和inputer查询Citation，匹配异名参考文献
	 * @param sciname
	 */
	@Query(value = "select * from citation as c where c.nametype = ?1 and c.status = 1 and c.inputer = ?2", nativeQuery = true)
	List<Citation> findByNametypeAndInputer(int nametype, String inputer);
	/**
	 * @Description 查询指定inputer的Citation
	 * @param id
	 */
	@Query(value = "select * from citation as c where c.status = 1 and c.inputer = ?1", nativeQuery = true)
	List<Citation> findAllByInputer(String inputer);
	/*@Query(value = "select * from citation as c where c.inputer = ?1 and c.citationstr not like '%：%' and c.citationstr not like '%(%'", nativeQuery = true)*/
	@Query(value = "select * from citation as c where c.inputer = ?1", nativeQuery = true)
	List<Citation> findByInputer(String id);

	/**
	 * @Description 根据nametype、分类单元集Id、Rank查询接受名引证
	 * @author ZXY
	 * @param taxasetId
	 * @param nametypeNotEq
	 * @param rankNameIn
	 * @return
	 */
	@Query(value = "select a.id,a.taxon_id,a.sciname,a.authorship,a.nameType,a.citationstr,b.rank_id from citation a left join taxon b on a.taxon_id = b.id where b.taxaset_id = ?1 and a.nametype!=?2 and b.rank_id in (?3)",nativeQuery = true)
	List<Object[]> findByNametypeAndTaxaSetAndRankIn(String taxasetId,int nametypeNotEq,List<String> rankNameIn);

	/**
	 * @Description 查询指定分类单元集下接受名引证的参考文献
	 * @author BINZI
	 * @param taxasetId
	 * @return
	 */
	@Query(value = "select t.id, c.refjson, t.authorstr from citation as c left join taxon as t on c.taxon_id = t.id where c.nametype = 1 and c.refjson is not null and t.taxaset_id = ?1 and c.status = 1 and t.status = 1 and (t.rank_id = 7 or t.rank_id = 42)", nativeQuery = true)
	List<Object[]> findAcceptReferenceLinkByTaxasetAndCitation(String taxasetId);
	
	/**
	 * @Description 查询指定分类单元集下异名引证的参考文献
	 * @author BINZI
	 * @param taxasetId
	 * @return
	 */
	@Query(value = "select c.id, c.refjson, t.authorstr from citation as c left join taxon as t on c.taxon_id = t.id where c.nametype = 5 and c.refjson is not null and t.taxaset_id = ?1 and c.status = 1 and t.status = 1 and (t.rank_id = 7 or t.rank_id = 42)", nativeQuery = true)
	List<Object[]> findSynchReferenceLinkByTaxasetAndCitation(String taxasetId);

	/**
	 * @Description 根据TaxonId删除与之相关联的所有引证
	 * @author BINZI 
	 * @param taxonId
	 */
	@Transactional
    @Modifying
    @Query(value = "delete from citation where taxon_id = ?1",nativeQuery = true)
	void deleteCitationByTaxaSetId(String taxonId);

	@Query(value = "select * from citation where taxon_id in ("
			+ "select id from taxon where taxaset_id = ?1) and refjson is null", nativeQuery = true)
	List<Citation> findCitationIdListByTaxasetId(String taxasetId);
	
	@Query(value = "select * from citation where taxon_id in ("
			+ "select id from taxon where taxaset_id = ?1) and remark is not null", nativeQuery = true)
	List<Citation> findCitationListByTaxasetId(String taxasetId);
	
	@Query(value = "select * from citation where taxon_id in ("
			+ "select id from taxon where taxaset_id = ?1) and refjson is null", nativeQuery = true)
	List<Citation> findCitationIdListByTaxasetId2(String taxasetId);

	@Query(value = "select * from citation where taxon_id = ?1", nativeQuery = true)
	List<Citation> findCitationsByTaxonId(String id);
	
	@Query(value = "select * from citation where sourcesid = ?1", nativeQuery = true)
	List<Citation> findListBySourcesid(String sourcesid);

	@Query(value = "select * from citation where sourcesid = ?1 and shortrefs like ?2%", nativeQuery = true)
	List<Citation> findListBySourcesidAndShortstr(String sourcesid, String shortStr);

	@Query(value = "select * from citation where sourcesid = ?1 and refjson is null", nativeQuery = true)
	List<Citation> findListBySourcesidAndRefjson(String sourcesid);
	
	@Query(value = "select * from citation as c where c.status = 1 and c.inputer = ?1 and authorship is not null", nativeQuery = true)
	List<Citation> findAllByInputerAndAuthorship(String inputer);

}
