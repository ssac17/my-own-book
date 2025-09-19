package com.myownbook.api.controller;

import com.myownbook.api.dto.BookResponseDTO;
import com.myownbook.api.dto.BookSearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookResponseDTOAssembler extends RepresentationModelAssemblerSupport<BookResponseDTO, BookResponseDTO> {

    public BookResponseDTOAssembler() {
        super(BookController.class, BookResponseDTO.class);
    }

    @Override
    public BookResponseDTO toModel(BookResponseDTO entity) {
        entity.add(linkTo(methodOn(BookController.class).getBookById(entity.getId())).withSelfRel());
        entity.add(linkTo(methodOn(BookController.class).findAll(new BookSearchCondition(), Pageable.unpaged())).withRel("all-books"));
        return entity;
    }
}
