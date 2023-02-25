package com.microservice.category.mscategory.service;

import com.microservice.category.mscategory.model.Base;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

public interface BaseService <E extends Base, ID extends Serializable> {
    Page<E> findAllPageable(Pageable pageable) throws Exception;
    E findById(ID id) throws Exception;
    E save(E entity) throws Exception;
    E update(ID id, E entity) throws Exception;
    boolean delete(ID id) throws Exception;

}
