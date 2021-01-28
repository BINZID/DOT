package org.big.service;

import org.big.entity.Customs;
import org.big.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomsRepository extends BaseRepository<Customs, String> {

}
