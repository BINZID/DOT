package org.big.repository;

import org.big.entity.Category;
import org.big.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends BaseRepository<Category, String> {

}
