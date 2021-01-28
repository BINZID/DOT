package org.big.repository;

import java.util.List;

import org.big.entity.Expert;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ExpertRepository extends BaseRepository<Expert, Integer> {

	/**
	 *<b>带分页排序的条件查询</b>
	 *<p> 带分页排序的条件查询</p>
	 * @author BINZI
	 * @param searchText 条件关键词，这里是模糊匹配
	 * @param pageable 分页排序方案实体
	 * @return org.springframework.data.domain.Page<org.big.entity.License>
	 */
	@Query(value = "Select e from Expert e where ("
			+ "e.cnName like %?1% or "
			+ "e.enName like %?1% or "
			+ "e.cnCompany like %?1% or "
			+ "e.enCompany like %?1% or "
			+ "e.cnAddress like %?1% or "
			+ "e.enAddress like %?1% or "
			+ "e.expEmail like %?1%)")
	Page<Expert> findExpertList(String searchText, Pageable pageable);

	/**
	 *<b>带分页排序的条件查询已上传列表</b>
	 *<p> 带分页排序的条件查询已上传列表</p>
	 * @author WangTianshan
	 * @param searchText 条件关键词，这里是模糊匹配
	 * @param pageable 分页排序方案实体
	 * @return org.springframework.data.domain.Page<org.big.entity.License>
	 */
	@Query(value = "Select b.serialNum,e from Expert e " +
			"left join Baseinfotmp b on " +
			" b.refDsId=e.id where b.fileType = 2 and b.filemark=?1 and ("
			+ "e.cnName like %?2% or "
			+ "e.enName like %?2% or "
			+ "e.cnCompany like %?2% or "
			+ "e.enCompany like %?2% or "
			+ "e.cnAddress like %?2% or "
			+ "e.enAddress like %?2% or "
			+ "e.expEmail like %?2%)")
	Page<Object> findUploadedExpertList(String fileMark,String searchText, Pageable pageable);

	/**
     *<b>根据Expert的属性查询Expert列表</b>
     *<p> 根据Expert的属性查询Expert列表</p>
     * @author BINZI
     * @param findText
	 * @param pageable
     * @return
     */
    @Query(value = "Select e from Expert e where ("
    		+ "e.cnName like %?1% or "
    		+ "e.enName like %?1%)")
	Page<Expert> searchByInfo(String findText, Pageable pageable);
    
    /**
     *<b>根据id属性查询Expert对象</b>
     *<p> 根据id属性查询Expert对象</p>
     * @author BINZI
     * @param id
     * @return
     */
	Expert findOneById(String id);

	/**
	 *<b>根据cnName、expEmail属性查询Expert对象</b>
	 *<p> 根据cnName、expEmail属性查询Expert对象</p>
	 * @author WangTianshan
	 * @return
	 */
	@Query(value = "Select e.id from expert e where e.cn_name = ?1 and e.exp_email = ?2 limit 1", nativeQuery = true)
	String findIdsByCnNameAndExpEmail(String cnName,String expEmail);

	/**
	 *<b>根据专家邮箱查询Expert的Id</b>
     *<p> 根据专家邮箱查询Expert的Id</p>
	 * @author BINZI
	 * @param expEmail
	 * @return
	 */
	@Query(value = "Select e.id from Expert e where e.expEmail = ?1")
	String findIdByExpEmail(String expEmail);

	/**
	 *<b>根据专家姓名查询Expert的Id</b>
     *<p> 根据专家姓名属性查询Expert的Id</p>
	 * @author BINZI
	 * @param cnname
	 * @return
	 */
	@Query(value = "Select e.id from expert e where e.cn_name = ?1 limit 1", nativeQuery = true)
	String findIdByCnName(String cnname);
}
