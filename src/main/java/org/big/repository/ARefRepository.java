package org.big.repository;

import org.big.entity.ARef;
import org.big.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface ARefRepository extends BaseRepository<ARef, String> {
	
	@Query(value = "select * from aref where id = ?1", nativeQuery = true)
	ARef findOneById(String refs);
	
}
