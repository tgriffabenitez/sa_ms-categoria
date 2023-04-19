package com.microservice.category.mscategory.repository;

import com.microservice.category.mscategory.model.Categoria;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends BaseRepository<Categoria, Long> {

    Categoria findByCategoria(String category);
}
