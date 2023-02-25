package com.microservice.category.mscategory.repository;

import com.microservice.category.mscategory.model.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends BaseRepository<Category, Long> {

    Category findByCategory(String category);
}
