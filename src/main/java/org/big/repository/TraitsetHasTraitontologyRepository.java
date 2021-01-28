package org.big.repository;
/**
 *<p><b>TraitsetHasTraitontology的DAO类接口</b></p>
 *<p> TraitsetHasTraitontology的DAO类接口，与TraitsetHasTraitontology有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2019/01/14 14:50</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
import org.big.entity.TraitsetHasTraitontology;
import org.big.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface TraitsetHasTraitontologyRepository extends BaseRepository<TraitsetHasTraitontology, String> {
	/**
	 *<b>获取TraitdescriptionRepository表的最大Id</b>
	 *<p> 获取TraitdescriptionRepository表的最大Id</p>
	 * @author BINZI
	 * @return
	 */
	@Query(value = "SELECT max(t.id + 0) FROM TraitsetHasTraitontology as t")
	Integer findMaxId();
}
