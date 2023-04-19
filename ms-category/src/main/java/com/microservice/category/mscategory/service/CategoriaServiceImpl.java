package com.microservice.category.mscategory.service;

import com.microservice.category.mscategory.model.Categoria;
import com.microservice.category.mscategory.repository.BaseRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoriaServiceImpl extends BaseServiceImpl<Categoria, Long> implements CategoriaService {
    public CategoriaServiceImpl(BaseRepository<Categoria, Long> baseRepository) {
        super(baseRepository);
    }
}
