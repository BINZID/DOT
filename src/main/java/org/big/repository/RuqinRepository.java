package org.big.repository;

import java.util.List;

import org.big.entity.Ruqin;
import org.big.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface RuqinRepository extends BaseRepository<Ruqin, String> {
	@Query(value = "Select t From Ruqin t")
	List<Ruqin> findListRuqin();

}
