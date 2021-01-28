package org.big.repository;

import org.big.entity.Redlist;
import org.big.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 *<p><b>Redlist的DAO类接口</b></p>
 *<p> Redlist的DAO类接口，</p>
 * @author BINZI
 *<p>Created date: 2018/11/21 13:52</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface RedlistRepository extends BaseRepository<Redlist, String> {

}
