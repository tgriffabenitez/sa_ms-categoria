package com.microservice.category.mscategory.controller;

import com.microservice.category.mscategory.exception.ErrorDetail;
import com.microservice.category.mscategory.model.Base;
import com.microservice.category.mscategory.model.Category;
import com.microservice.category.mscategory.service.BaseServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

public abstract class BaseControllerImpl<E extends Base, S extends BaseServiceImpl<E, Long>> implements BaseController<E, Long> {
    @Autowired
    protected S service;

    @Operation(summary = "Obtiene el listado de todas las categorias en formato paginado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna todas las categorias.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))
            }),
            @ApiResponse(responseCode = "404", description = "El recurso no existe", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            }),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            }),
    })
    @GetMapping("/categorias")
    public ResponseEntity<?> getAll( ) {
        try {
            List<E> entity = service.findAll();
            if (entity.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(entity, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Obtiene una categoria segun el id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna una categoria.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))
            }),
            @ApiResponse(responseCode = "400", description = "No se pudo procesar la solicitud.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            }),
            @ApiResponse(responseCode = "404", description = "El recurso no existe", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            }),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            }),
    })
    @GetMapping("/categoria/{id}")
    public ResponseEntity<?> getById(@PathVariable @Valid Long id) {
        try {
            if (id == null || id <= 0)
                throw new IllegalArgumentException("El id ingresado no es valido");

            if (service.findById(id) == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));

        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Da de alta una categoria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna la categoria dada de alta.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))
            }),
            @ApiResponse(responseCode = "400", description = "No se pudo procesar la solicitud.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            }),
            @ApiResponse(responseCode = "409", description = "El recurso ya existe en la base de datos", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            }),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            }),
    })
    @PostMapping("/categorias")
    public ResponseEntity<?> save(@RequestBody @Valid E entity) {
        try {
            if (entity == null)
                throw new ValidationException("La entidad no puede ser nula");

            return ResponseEntity.status(HttpStatus.OK).body(service.save(entity));

        } catch (DataIntegrityViolationException ex) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.CONFLICT.value(), ex.getMessage(), "Conflict", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDetail);

        } catch (ValidationException ex) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), "Bad Request", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);

        } catch (Exception ex) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ocurrió un error interno en el servidor", "Internal Server Error", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetail);
        }
    }

    @Operation(summary = "Modifica una categoria segun el id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna la categoria modificada.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))
            }),
            @ApiResponse(responseCode = "400", description = "No se pudo procesar la solicitud.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            }),
            @ApiResponse(responseCode = "404", description = "El recurso no existe", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            }),
            @ApiResponse(responseCode = "409", description = "El recurso ya existe en la base de datos", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            }),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            }),
    })
    @PutMapping("/categoria/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid E entity) {
        try {
            if (id == null || id <= 0)
                throw new IllegalArgumentException("El id ingresado no es valido");

            if (service.findById(id) == null)
                throw new EntityNotFoundException("No se encontro la categoria con el id: " + id);

            E updateEntity = service.update(id, entity);
            return ResponseEntity.status(HttpStatus.OK).body(updateEntity);

        } catch (DataIntegrityViolationException ex) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.CONFLICT.value(), ex.getMessage(), "Conflict", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDetail);

        } catch (IllegalArgumentException | ValidationException ex) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), "Bad Request", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);

        } catch (EntityNotFoundException ex) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.NOT_FOUND.value(), ex.getMessage(), "Not Found", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetail);

        } catch (Exception e) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), "Internal Server Error", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetail);
        }
    }

    @Operation(summary = "Elimina una categoria segun el id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "La categoria fue eliminada .", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", description = "No se pudo procesar la solicitud.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            }),
            @ApiResponse(responseCode = "404", description = "El recurso no existe", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            }),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetail.class))
            }),
    })
    @DeleteMapping("/categoria/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("El id ingresado no es valido");
            }
            if (service.findById(id) == null)
                throw new EntityNotFoundException("No se encontro la categoria con el id: " + id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(service.delete(id));

        } catch (EntityNotFoundException ex) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.NOT_FOUND.value(), ex.getMessage(), "Not Found", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetail);

        } catch (IllegalArgumentException ex) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), "Bad Request", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);

        } catch (Exception ex) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), "Internal Server Error", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetail);
        }
    }
}

