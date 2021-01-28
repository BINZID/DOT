package org.big.repository;

import java.util.List;

import org.big.entity.Ref;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p><b>Ref的DAO类接口</b></p>
 *<p> Ref的DAO类接口，与User有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/13 10:59</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface RefRepository extends BaseRepository<Ref, String> {
	/**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Taxaset>
     */
    @Query(value = "Select r from Ref r "
    		+ "where ("
    		+ "r.author like %?1% or "
    		+ "r.title like %?1% or "
    		+ "r.ptype like %?1% or "
    		+ "r.journal like %?1% or "
    		+ "r.press like %?1% or "
    		+ "r.pyear like %?1% or "
    		+ "r.version like %?1%) and "
    		+ "r.status = 1 and r.inputer = ?2")
	Page<Ref> searchInfo(String searchText, Pageable pageable, String inputer);


	/**
	 *<b>带分页排序的条件查询已上传列表</b>
	 *<p> 带分页排序的条件查询已上传列表</p>
	 * @author WangTianshan
	 * @param searchText 条件关键词，这里是模糊匹配
	 * @param pageable 分页排序方案实体
	 * @return org.springframework.data.domain.Page<org.big.entity.License>
	 */
	@Query(value = "Select b.serialNum,r from Ref r " +
			"left join Baseinfotmp b on " +
			" b.refDsId=r.id where b.filemark=?1 and ("
			+ "r.author like %?2% or "
			+ "r.refstr like %?2% or "
			+ "r.title like %?2% or "
			+ "r.ptype like %?2% or "
			+ "r.journal like %?2% or "
			+ "r.press like %?2% or "
			+ "r.pyear like %?2% or "
			+ "r.version like %?2%) and "
			+ "r.status = 1 and "
			+ "r.inputer = ?3 and b.fileType = 0")
	Page<Object> findUploadedRefList(String fileMark,String searchText, String inputer, Pageable pageable);

    /**
     *<b>根据RefId查找一个Reference实体</b>
     *<p> 据id查找一个实体</p>
     * @author BINZI
     * @param Id 实体的id
     * @return org.big.entity.Ref
     */
	@Query(value = "Select r From Ref r Where r.id = ?1")
	Ref findOneById(String id);
	
    /**
     *<b>根据id删除一个实体</b>
     *<p> 据id删除一个实体</p>
     * @author BINZI
     * @param ID 实体的id
     * @return void
     */
	@Transactional
	@Modifying
	@Query(value = "Delete From Ref r Where r.id = ?1")
	void deleteOneById(String id);

	/**
	 *<b>Ref的select列表</b>
	 *<p> Ref的select检索列表</p>
	 * @author BINZI
	 * @param findText
	 * @param dsId
	 * @param pageable
	 * @return com.alibaba.fastjson.JSON
	 */
	@Query(value = "Select r from Ref r where (r.title like %?1%) and r.status = 1")
	Page<Ref> searchByTitle(String findText, Pageable pageable);

	/**
	 *<b>Ref的select列表</b>
	 *<p> Ref的select检索列表</p>
	 * @author WangTianshan(王天山)
	 * @param findText
	 * @param pageable
	 * @return com.alibaba.fastjson.JSON
	 */
	@Query(value = "Select r from Ref r left join UserTeam ut on r.inputer = ut.userId where ("
			+ "r.refstr like ?1% or "
			+ "r.title like ?1% or "
			+ "r.author like ?1% or "
			+ "r.journal like ?1% or "
			+ "r.pyear like ?1%) and r.status = 1 and ut.teamId = ?2")
	Page<Ref> searchByRefstr(String findText, Pageable pageable, String inputer);
	
	/**
     *<b>根据用户id查找Ref集合</b>
     *<p> 根据用户id查找Ref集合</p>
     * @author BINZI
	 * @param id
	 * @return
	 */
	@Query(value = "Select r From Ref r Where r.inputer = ?1 and r.status = 1")
	List<Ref> findAllByUserId(String id);

	/**
	 *<b>根据uid属性查询指定用户下的Ref的Id</b>
     *<p> 根据uid属性查询指定用户下的Ref的Id</p>
	 * @param uid
	 * @return 
	 */
	@Query(value = "Select r.id from Ref r where r.inputer = ?1 and r.status = 1")
	List<String> findIdsByUid(String uid);
	/**
	 *<b>根据title和pyear属性查询指定用户下的Ref的Id</b>
     *<p> 根据title和pyear属性查询指定用户下的Ref的Id</p>
	 * @param title
	 * @param pyear
	 * @return
	 */
	@Query(value = "Select r.id from Ref r where r.title = ?1 and r.pyear = ?2 and r.inputer = ?3 and r.status = 1")
	List<String> findRefIdByTitleAndPyear(String title, String pyear, String uid);

	/**
	 * <b>根据refstr对上传文件去重</b>
	 * <p> 根据refstr对上传文件去重</p>
	 * @param refstr
	 * @return
	 */
	@Query(value = "Select * from refs where refstr like %?1% and inputer = ?2 limit 1", nativeQuery = true)
	Ref findOneByRefstrAndInputer(String refstr, String uid);

	/**
	 *<b>Ref的select列表，按年份联合查询</b>
	 *<p> Ref的select检索列表，按年份联合查询</p>
	 * @author WangTianshan(王天山)
	 * @param findText
	 * @param pageable
	 * @return com.alibaba.fastjson.JSON
	 */
	@Query(value = "Select r from Ref r left join UserTeam ut on r.inputer = ut.userId where ("
			+ "r.refstr like ?1% or "
			+ "r.title like ?1% or "
			+ "r.author like ?1% or "
			+ "r.journal like ?1%) and r.status = 1 and ut.teamId = ?2 and r.pyear like ?3%")
	Page<Ref> searchByRefstrAndPyear(String findText, Pageable pageable, String teamId, String pyear);
	/**
	 *<b>Ref的select列表，按完整题录联合查询</b>
	 *<p> Ref的select检索列表，按完整题录联合查询</p>
	 * @author BINZI
	 * @param findText
	 * @param pageable
	 * @return com.alibaba.fastjson.JSON
	 */
	@Query(value = "Select r from Ref r left join UserTeam ut on r.inputer = ut.userId where ("
			+ "r.pyear like ?1% or "
			+ "r.title like ?1% or "
			+ "r.author like ?1% or "
			+ "r.journal like ?1%) and r.status = 1 and ut.teamId = ?2 and r.refstr like %?3%")
	Page<Ref> searchByRefstrAndRefstr(String findText, Pageable pageable, String teamId, String searchContent);

	/**
	 *<b>Ref的select列表，按作者联合查询</b>
	 *<p> Ref的select检索列表，按作者联合查询</p>
	 * @author BINZI
	 * @param findText
	 * @param pageable
	 * @return com.alibaba.fastjson.JSON
	 */
	@Query(value = "Select r from Ref r left join UserTeam ut on r.inputer = ut.userId where ("
			+ "r.refstr like ?1% or "
			+ "r.title like ?1% or "
			+ "r.pyear like ?1% or "
			+ "r.journal like ?1%) and r.status = 1 and ut.teamId = ?2 and r.author like %?3%")
	Page<Ref> searchByRefstrAndAuthor(String findText, Pageable pageable, String teamId, String searchContent);

	/**
	 *<b>Ref的select列表，按表题联合查询</b>
	 *<p> Ref的select检索列表，按标题联合查询</p>
	 * @author BINZI
	 * @param findText
	 * @param pageable
	 * @return com.alibaba.fastjson.JSON
	 */
	@Query(value = "Select r from Ref r left join UserTeam ut on r.inputer = ut.userId where ("
			+ "r.refstr like ?1% or "
			+ "r.pyear like ?1% or "
			+ "r.author like ?1% or "
			+ "r.journal like ?1%) and r.status = 1 and ut.teamId = ?2 and r.title like %?3%")
	Page<Ref> searchByRefstrAndTitle(String findText, Pageable pageable, String teamId, String searchContent);
	/**
	 * @Description 根据录入人查询参考文献的指定字段，用以拼装名录数据
	 * @author BINZI
	 * @param inputer
	 * @return
	 */
	@Query(value = "Select r.id, r.refstr, r.title, r.author, r.pyear From biodata.refs as r Where r.inputer = ?1 and r.status = 1", nativeQuery = true)
	List<Object[]> findAllByInputer(String inputer);
	/**
	 * 查参考文献
	 * @param string
	 * @return
	 */
	@Query(value = "Select * from refs Where tchar = ?1 and inputer = ?2", nativeQuery = true)
	Ref findOneByTcharAndInputer(String tchar, String inputer);

	@Query(value = "Select * from refs Where title = ?1 and inputer = ?2", nativeQuery = true)
	Ref findOneByTitleAndInputer(String title, String inputer);

	@Query(value = "select * from refs where remark = ?4 and refstr like %?1% and refstr like %?2% and refstr like %?3%", nativeQuery = true)
	List<Ref> findByYearAndVolumeAndAuthorAndRemark(String year, String volume, String author, String remark);

	@Query(value = "select * from refs where refstr like %?1% and refstr like %?2% and refstr like %?3% and remark = '双翅目蝇类名录' limit 1", nativeQuery = true)
	Ref findByPyearAndAuthorAndPress(String year, String author, String pressStr);


	@Query(value = "select * from refs where refstr like %?1% and remark = ?2", nativeQuery = true)
	List<Ref> findByYear(String year, String remark);


	@Query(value = "select * from refs where refstr = ?1 and remark = ?2", nativeQuery = true)
	List<Ref> findListByRefstrAndRemark(String refstr, String remark);

	@Query(value = "select * from refs where ptype = ?1 and author = ?2"
			+ " and pyear = ?3 and title = ?4 and journal = ?5 and r_volume = ?6"
			+ " and refs = ?7 and refe = ?8 and place = ?9 and press = ?10 and remark = ?11", nativeQuery = true)
	List<Ref> findListByPtypeAndAuthorAndPyearAndTitleAndJournalAndRVolumeAndRefsAndRefeAndPlaceAndPressAndRemark(
			String ptype, String author, String pyear, String title, String journal, String rVolume, String refs,
			String refe, String place, String press, String remark);


	@Query(value = "select * from refs where remark = ?1", nativeQuery = true)
	List<Ref> findListByRemark(String remark);

	@Query(value = "select * from refs where author like %?1% and pyear like %?2% and inputer = ?3 limit 1", nativeQuery = true)
	Ref findByPyearAndAuthorAndInputer(String author, String pyear, String inputer);

	@Query(value = "select * from refs where author like %?1% and refstr like %?2% and pyear like %?3% and inputer = ?4 limit 1", nativeQuery = true)
	Ref findByPyearAndAuthorsAndInputer(String author1, String author2, String pyear, String inputer);

	@Query(value = "select * from refs where inputer = ?1", nativeQuery = true)
	List<Ref> findByInputer(String inputer);


	@Query(value = "select * from refs where author like %?1% and refstr like %?2% and refstr like %?3% and pyear like %?4% and inputer = ?5 limit 1", nativeQuery = true)
	Ref findByPyearAndAuthorsAndInputer(String author1, String author2, String author3, String pyear, String inputer);

}