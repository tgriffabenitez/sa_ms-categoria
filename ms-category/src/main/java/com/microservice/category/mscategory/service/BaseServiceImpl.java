package com.microservice.category.mscategory.service;

import com.microservice.category.mscategory.model.Base;
import com.microservice.category.mscategory.model.Category;
import com.microservice.category.mscategory.repository.BaseRepository;
import com.microservice.category.mscategory.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl <E extends Base, ID extends Serializable> implements BaseService<E, ID> {

    @Autowired
    private CategoryRepository CategoryRepository;

    protected BaseRepository<E, ID> baseRepository;

    public BaseServiceImpl(BaseRepository<E, ID> baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Override
    public List<E> findAll() throws Exception {
        try {
            return baseRepository.findAll();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public E findById(ID id) throws Exception {
        try {
            Optional<E> optional = baseRepository.findById(id);
            return optional.orElse(null);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * OBSERVACION: PARA PRODUCCION USAR ESTE METODO.
     * Guarda una entidad en la base de datos.
     * Verifica si ya existe una categoría con el mismo nombre antes de guardarla.
     *
     * @param entity la entidad a guardar.
     * @return la entidad guardada.
     * @throws DataIntegrityViolationException si ya existe una entidad con los mismos datos en la base de datos.
     * @throws Exception si ocurre un error al guardar la entidad.
     */
    @Override
    public E save(E entity) throws Exception {
        try {
            if (entity instanceof Category category) {
                Category categoryDB = CategoryRepository.findByCategoria(category.getCategoria());
                if (categoryDB != null) {
                    throw new DataIntegrityViolationException("La categoria ya existe");
                }
            }
            return baseRepository.save(entity);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getMessage());

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * OBSERVACION: PARA TESTING CON JMETER UTILIZAR ESTE METODO
     */
//    @Override
//    public E save(E entity) throws Exception {
//        try {
//            return baseRepository.save(entity);
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//    }


    /**
     * Actualiza la entidad con el identificador proporcionado con los datos de la entidad proporcionada. Si la entidad ya existe en la base de datos,
     * lanza una excepción de violación de integridad de datos. Si la entidad no existe en la base de datos, retorna null.
     *
     * @param id el identificador de la entidad que se desea actualizar
     * @param entity la entidad con los datos actualizados
     * @return la entidad actualizada si se pudo actualizar exitosamente, o null si la entidad no existe en la base de datos
     * @throws DataIntegrityViolationException si ya existe una entidad con los mismos datos en la base de datos.
     * @throws Exception si ocurre un error durante la actualización de la entidad o si ya existe otra entidad con los mismos datos en la base de datos
     */
    @Override
    @Transactional
    public E update(ID id, E entity) throws Exception {
        try {
            Optional<E> entityOptional = baseRepository.findById(id);
            if (entityOptional.isPresent()) {
                E entityToUpdate = entityOptional.get();
                if (entityToUpdate instanceof Category categoryToUpdate && entity instanceof Category category) {
                    Category categoryDB = CategoryRepository.findByCategoria(category.getCategoria());
                    if (categoryDB != null && !categoryDB.getId().equals(categoryToUpdate.getId())) {
                        throw new DataIntegrityViolationException("La categoría ya existe");
                    }
                }

                Long categoryId = entityToUpdate.getId();
                entity.setId(categoryId);
                return baseRepository.save(entity);
            }
            return null;

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getMessage());

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * OBSERVACION: PARA TESTING CON JMETER UTILIZAR ESTE METODO
     */
//    @Override
//    @Transactional
//    public E update(ID id, E entity) throws Exception {
//        try {
//            Optional <E> entityOptional = baseRepository.findById(id);
//            E account = entityOptional.get();
//
//            account = baseRepository.save(entity);
//            return account;
//        }catch (Exception e){
//            throw new Exception(e.getMessage());
//        }
//    }



    @Override
    @Transactional
    public boolean delete(ID id) throws Exception {
        try {
            Optional<E> entityOptional = baseRepository.findById(id);
            if (entityOptional.isPresent()) {
                baseRepository.delete(entityOptional.get());
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
