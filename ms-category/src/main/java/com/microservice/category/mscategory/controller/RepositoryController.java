package com.microservice.category.mscategory.controller;

import com.microservice.category.mscategory.model.Category;
import com.microservice.category.mscategory.service.CategoryServiceImpl;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@OpenAPIDefinition(
        info = @Info(
                title = "Categories",
                version = "1.0",
                description = "Este servicio es el responsable de realizar el CRUD de las categorias.",
                contact = @Contact(
                        name = "Tomas Martin, Griffa Benitez",
                        email = "tgriffabentiez@gmail.com"
                )
        )
)
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1")
public class RepositoryController extends BaseControllerImpl<Category, CategoryServiceImpl> {

}
