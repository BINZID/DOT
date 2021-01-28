package org.big.repository;

import java.util.List;

import org.big.entity.Baseinfotmp;

import org.big.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p><b>Baseinfo的DAO类接口</b></p>
 *<p> Baseinfo的DAO类接口，</p>
 * @author BINZI
 *<p>Created date: 2018/11/21 13:52</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface BaseinfotmpRepository extends BaseRepository<Baseinfotmp, String> {
	/**
	 * <b>列表展示上传的参考文献，通过文件标记、文件类型，查询对应的参考文献的Id</b>
	 * <p> 列表展示上传的参考文献，通过文件标记、文件类型，查询对应的参考文献的Id</p>
	 * @author BINZI
	 * @param filemark
	 * @param type
	 * @return
	 */
	@Query("Select bit.refDsId from Baseinfotmp as bit where bit.filemark = ?1 and bit.fileType = ?2")
	List<String> findRefDsIdByFilemarkAndFileType(String filemark, Integer type);

	/**
	 * <b>通过文件标记、文件类型，序号查询对应的数据源的Id</b>
	 * <p> 通过文件标记、文件类型，序号查询对应的数据源的Id</p>
	 * @author BINZI
	 * @param dsFileMark
	 * @param serialNum
	 * @param type
	 * @return
	 */
	@Query("Select bit.refDsId from Baseinfotmp as bit where bit.filemark = ?1 and bit.serialNum = ?2 and bit.fileType = ?3")
	String findDsIdByFilemarkAndSerialNumAndFileType(String dsFileMark, String serialNum, Integer type);

	/**
	 * <b>通过文件标记、文件类型，序号查询对应的参考文献的Id</b>
	 * <p> 通过文件标记、文件类型，序号查询对应的参考文献的Id</p>
	 * @author BINZI
	 * @param refFileMark
	 * @param type
	 * @param serialNum
	 * @return
	 */
	@Query("Select bit.refDsId from Baseinfotmp as bit where bit.filemark = ?1 and bit.fileType = ?2 and bit.serialNum in (?3)")
	List<String> findRefIdByFilemarkAndSerialNumAndFileType(String refFileMark, Integer type, String[] serialNum);

	/**
	 * <b>通过文件标记、文件类型，序号查询对应的专家的Id</b>
	 * <p> 通过文件标记、文件类型，序号查询对应的专家的Id</p>
	 * @author BINZI
	 * @param expFileMark
	 * @param serialNum
	 * @param type
	 * @return
	 */
	@Query("Select bit.refDsId from Baseinfotmp as bit where bit.filemark = ?1 and bit.fileType = ?2 and bit.serialNum = ?3")
	String findExpIdByFilemarkAndSerialNumAndFileType(String expFileMark, Integer fileType, String serialNum);

	/**
	 * <b>通过文件标记、文件类型，序号查询对应的数据源的Id</b>
	 * <p> 通过文件标记、文件类型，序号查询对应的数据源的Id</p>
	 * @author BINZI
	 * @param uid
	 * @return
	 */
	@Transactional
	@Modifying
	@Query("delete from Baseinfotmp as bit where bit.inputer = ?1")
	void deleteAllByUid(String uid);
	
}
