package com.microservice.category.mscategory.service;

import com.microservice.category.mscategory.model.Category;
import com.microservice.category.mscategory.repository.BaseRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends BaseServiceImpl<Category, Long> implements CategoryService {
    public CategoryServiceImpl(BaseRepository<Category, Long> baseRepository) {
        super(baseRepository);
    }
}
