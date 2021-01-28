package org.big.repository;

import java.util.List;

import org.big.entity.Names;
import org.big.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *<p><b>Names的DAO类接口</b></p>
 *<p> Names的DAO类接口，与Names有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/11 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface NamesRepository extends BaseRepository<Names, String> {

	@Query(value = "select distinct(n.scientificname), n.chname, n.authorstr, ds.title from biodata.names as n LEFT JOIN biodata.namesource as ns on n.id = ns.names_id LEFT JOIN biodata.datasources as ds on ns.sources = ds.id", nativeQuery = true)
	List<Object[]> findAllByTeamId();
}