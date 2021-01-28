package org.big.repository;

import org.big.entity.Protectplus;
import org.big.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 *<p><b>Protectplus的DAO类接口</b></p>
 *<p> Protectplus的DAO类接口，与Protectplus有关的持久化操作方法</p>
 * @author BIN
 *<p>Created date: 2020/07/28 10:59</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface ProtectplusRepository extends BaseRepository<Protectplus, String> {

}
