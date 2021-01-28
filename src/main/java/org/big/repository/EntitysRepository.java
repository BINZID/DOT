package org.big.repository;

import java.util.List;

import org.big.entity.Entitys;
import org.big.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface EntitysRepository extends BaseRepository<Entitys, String> {
	@Query(value = "Select t From Entitys t")
	List<Entitys> findListEntitys();

}
