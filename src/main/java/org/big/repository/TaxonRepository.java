package org.big.repository;

import org.big.entity.Rank;
import org.big.entity.Taxaset;
import org.big.entity.Taxon;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 *<p><b>Taxon的DAO类接口</b></p>
 *<p> Taxon的DAO类接口，与User有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/13 10:59</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface TaxonRepository extends BaseRepository<Taxon, String> {
    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param searchText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Taxon>
     */
    @Query(value = "Select t from Taxon t where ("
    		+ "t.scientificname like %?1% or "
    		+ "t.chname like %?1% or "
    		+ "t.authorstr like %?1% or "
    		+ "t.inputer like %?1% or "
    		+ "t.inputtime like %?1% or "
    		+ "t.synchdate like %?1%) and t.taxaset.id =?2 and t.status = 1 order by t.orderNum asc, t.inputtime asc")
	Page<Taxon> searchInfo(String searchText, Pageable pageable, String taxasetId);

    /**
     *<b>根据TaxonId查找一个Taxon实体</b>
     *<p> 据id查找一个实体</p>
     * @author BINZI
     * @param Id 实体的id
     * @return org.big.entity.Taxon
     */
	@Query(value = "Select t From Taxon t Where t.id = ?1")
	Taxon findOneById(String id);
	/**
     *<b>Taxon的select列表</b>
     *<p> 当前Taxkey下的Taxon的select检索列表</p>
     * @author BINZI
     * @param findText
     * @param pageable
     * @return com.alibaba.fastjson.JSON
     */
    @Query(value = "select t from Taxon t where (t.scientificname like %?1%) and t.status = 1 and t.taxaset.id = ?2")
	Page<Taxon> searchByTaxonInfo(String findText, Pageable pageable, String taxonsetId);

	/**
	 *<b>根据Rank和TaxonSet返回TaxonList </b>
	 *<p> 根据Rank和TaxonSet返回TaxonList</p>
	 * @author WangTianshan (王天山)
	 * @param thisTaxaset TaxonSet
	 * @param thisRank Rank
	 * @return org.springframework.data.domain.Page<org.big.entity.Taxon>
	 */
	List<Taxon> findTaxonByTaxasetAndRankAndStatus(Taxaset thisTaxaset, Rank thisRank,int status);


	/**
	 *<b>根据Rank和TaxonSet和关键名字返回TaxonList </b>
	 *<p> 根据Rank和TaxonSet和关键名字返回TaxonList</p>
	 * @author WangTianshan (王天山)
	 * @param thisTaxaset TaxonSet
	 * @param thisRank Rank
	 * @param taxonName 关键名字
	 * @return org.springframework.data.domain.Page<org.big.entity.Taxon>
	 */
	@Query(value = "select t from Taxon t where " +
			"((t.scientificname like %?3%) or (t.chname like %?3%))" +
			" and t.status = ?4" +
			" and t.rank.id = ?2" +
			" and t.taxaset.id = ?1 and t.inputtime >= '2019-04-25 13:31:56' order by t.scientificname asc")
	List<Taxon> searchTaxonByTaxasetAndRankAndTaxonNameAndStatus(String thisTaxaset, String thisRank, String taxonName, int status);

	/**
	 *<b>根据dataset统计符合条件的taxon</b>
	 *<p> 根据dataset统计符合条件的taxon</p>
	 * @author WangTianshan (王天山)
	 * @param status 状态
	 * @param id Dataset的id
	 * @return long
	 */
	long countByStatusAndTaxaset_Dataset_Id(int status,String id);

	/**
	 * <b>导出指定Taxaset下的Taxon</b>
	 * <p> 导出指定Taxaset下的Taxon</p>
	 * @author BINZI
	 * @param taxasetId
	 * @return
	 */
	@Query(value = "select t from Taxon t where t.taxaset.id = ?1 and t.status = 1")
	List<Taxon> findTaxonByTaxasetId(String taxasetId);

	/**
	 * <b>查询指定Taxaset下的Taxon的学名</b>
	 * <p> 查询指定Taxaset下的Taxon的学名</p> 
	 * @author BINZI
	 * @param taxasetId
	 * @return
	 */
	List<String> findScientificnameByTaxasetId(String taxasetId);

	/**
	 * <b>根据学名查询Taxon</b>
	 * <p> 根据学名查询Taxon</p> 
	 * @author BINZI
	 * @param tid
	 * @return
	 */
	@Query(value = "select * from taxon t where t.scientificname = ?1 and t.taxaset_id = ?2 and t.status = 1 limit 1", nativeQuery = true)
	Taxon findOneByScientificnameAndTaxasetId(String scientificname, String taxasetId);

	/**
	 * <b>根据学名查询Taxon</b>
	 * <p> 根据学名查询Taxon</p> 
	 * @author BINZI
	 * @param tid
	 * @return
	 */
	@Query(value = "select t from Taxon t where t.scientificname = ?1 and t.taxaset.id = ?2 and t.status = 1")
	List<Taxon> findByScientificnameAndTaxasetId(String scientificname, String taxasetId);
	
	/**
	 * <b>根据Id更新Taxon的数据</b>
	 * <p> 根据Id更新Taxon的数据</p>
	 * @param sourcesid
	 * @param sourcesid2
	 * @param expert
	 * @param id
	 */
	@Transactional
	@Modifying
	@Query("update Taxon as t set t.sourcesid = ?1, t.refjson = ?2, t.expert = ?3, t.synchdate = ?4 where t.id = ?5")
	void updateTaxonById(String sourcesid, String refjson, String expert, Timestamp timestamp, String id);

	/**
	 *<b>带分页排序的条件查询已上传列表</b>
	 *<p> 带分页排序的条件查询已上传列表</p>
	 * @author BINZI
	 * @param searchText 条件关键词，这里是模糊匹配
	 * @param pageable 分页排序方案实体
	 * @return org.springframework.data.domain.Page<java.lang.Object>
	 */
    @Query(value = "Select t from Taxon t where ("
    		+ "t.scientificname like %?1% or "
    		+ "t.authorstr like %?1% or "
    		+ "t.inputer like %?1% or "
    		+ "t.synchdate like %?1%) and "
    		+ "t.taxaset.id =?2 and t.status = 1 and t.synchdate > ?3")
	Page<Taxon> searchInfo(String searchText, Pageable pageable, String taxasetId, Timestamp timestamp);

    /**
     * <p><b>查询指定分类单元集下的分类单元的最大添加序列数</b></p>
     * <p>查询指定分类单元集下的分类单元的最大添加序列数</p>
     * @author BINZI
     * @param taxasetId
     * @return
     */
    @Query(value = "SELECT coalesce(max(t.orderNum), 0) FROM Taxon as t where t.taxaset.id=?1")
	int findMaxOrderNumByTaxasetId(String taxasetId);
    
    @Query(value = "select id from taxon where id in ?1 order by order_num, scientificname asc", nativeQuery = true)
	List<String> findIdByOrderNum(List<String> ids);
	/**
	 * @Description 根据inputer查询Taxon
	 * @param id
	 * @return
	 */
    @Query(value = "select * from taxon as t where t.inputer = ?1 and t.status = 1", nativeQuery = true)
	List<Taxon> findByInputer(String inputer);
	/**
	 * @Description 鱼类属下亚种种加词/亚种加词处理
	 * @param i
	 * @param id
	 */
    @Query(value = "select * from taxon as t where t.rank_id = ?1 and t.inputer = ?2", nativeQuery = true)
    List<Taxon> handleFishTaxon(int rankId, String inputer);

    @Query(value = "select * from taxon as t where (t.inputer = ?1 or t.inputer = ?2) and (rank_id = '7' OR rank_id = '42') and t.status = 1 and t.scientificname is not null", nativeQuery = true)
	List<Taxon> findByInputers(String string, String string2);

	/**
	 * @Description 查询指定分类单元集下rank为family的Taxon(为了节约内存，只查询部分字段)
	 * @author ZXY
	 * @param taxasetId
	 * @param family
	 * @return
	 */
	@Query(value = "select t.id, t.scientificname,t.chname,t.rank_id,t.epithet,t.authorstr,t.remark from taxon t left join rank r on r.id = t.rank_id where  t.taxaset_id = ?1 and r.enname =?2",nativeQuery = true)
	List<Object[]> findByTaxasetAndRank(String taxasetId,String family);
	
	/**
	 * @Description 查询指定分类单元集下在rankNames范围内的Taxon
	 * @author ZXY
	 * @param taxasetId
	 * @param rankNames
	 * @return
	 */
	@Query(value = "select t.id, t.scientificname,t.chname,t.rank_id,t.epithet,t.authorstr,t.remark from taxon t left join rank r on r.id = t.rank_id where  t.taxaset_id = ?1 and r.enname in(?2)",nativeQuery = true)
	List<Object[]> findByTaxasetAndRankIn(String taxasetId,List<String> rankNames);
	/**
	 * @Description 查询指定分类单元集下所有Taxon的参考文献（种/种下）
	 * @author BINZI
	 * @param taxasetId
	 * @return
	 */
	@Query(value = "select t.id, t.refjson, t.authorstr from taxon as t where t.taxaset_id = ?1 and t.refjson is not null and (t.rank_id = 7 or t.rank_id = 42)", nativeQuery = true)
	List<Object[]> findReferenceLinkByTaxasetAndTaxon(String taxasetId);

	@Query(value = "select * from taxon where scientificname = ?1", nativeQuery = true)
	List<Taxon> findTaxonByGenus(String str);

	@Query(value = "select * from taxon where scientificname = ?1 and (chname is null or chname = '')", nativeQuery = true)
	List<Taxon> findTaxonByGenusIsNull(String str);
	/**
	 * @Description 根据TaxonId删除Taxon
	 * @author BINZI 
	 * @param taxonId
	 */
	@Transactional
    @Modifying
    @Query(value = "delete from taxon where id = ?1",nativeQuery = true)
	void deleteTaxonByTaxonId(String taxonId);

	@Query(value = "select * from taxon where chname = ?1 and inputer = ?2", nativeQuery = true)
	Taxon findOneByChnameAndInputer(String trim, String id);

	@Query(value = "select * from taxon where scientificname = ?1 and tci = ?2", nativeQuery = true)
	Taxon findByScientificnameAndTci(String sciname, String tci);

	@Query(value = "select * from taxon where scientificname = ?1 and inputer = ?2 limit 1", nativeQuery = true)
	Taxon findByScientificnameAndInputer(String sciname, String inputer);

	@Query(value = "select * from taxon where taxaset_id = ?1 and rank_id in (3,4,5,6,7) order by remark asc", nativeQuery = true)
	List<Taxon> getFishByTaxasetIdAndRank(String taxasetId);

	@Query(value = "select * from taxon where id in ("
			+ "select taxon_id from taxon_has_taxtree where pid = ?1) "
			+ "and rank_id in (3,4,5,6,7,11,12) order by scientificname asc;", nativeQuery = true)
	List<Taxon> getTaxonListByTreeNodeId(String treeNodeId);

	@Query(value = "select * from taxon where inputer = ?1 and refjson is not null and refjson like '%otherRef%'", nativeQuery = true)
	List<Taxon> findByInputerAndRefjson(String inputer);

	@Query(value = "select * from taxon where scientificname = ?1 and inputer = ?2 limit 1", nativeQuery = true)
	Taxon findOneByScientificnameAndInputer(String scientificname, String inputer);

	/**
	 * 查2019新增入侵物种
	 * @param taxaset2018
	 * @param taxaset2019
	 * @return
	 */
	@Query(value = "select * from taxon where taxaset_id = ?2 and scientificname not in ("
			+ "select scientificname from taxon where taxaset_id = ?1)", nativeQuery = true)
	List<Taxon> findTaxonListByTaxasets(String taxaset2018, String taxaset2019);
	
}
