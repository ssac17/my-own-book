package com.myownbook.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myownbook.api.dto.BookDTO;
import com.myownbook.api.dto.BookResponseDTO;
import com.myownbook.api.dto.BookSearchCondition;
import com.myownbook.api.model.Category;
import com.myownbook.api.model.RoleEnum;
import com.myownbook.api.security.SecurityConfig;
import com.myownbook.api.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc; //Mocking된 MVC 환경에서 HTTP 요청을 보낼수 있는 객체
    @Autowired
    private ObjectMapper objectMapper; // json <-> Object 변환
    //BookController가 의존하는 클래스를 mocking 함
    @MockitoBean
    private BookService bookService;
    @MockitoBean
    private BookResponseDTOAssembler assembler;
    @MockitoBean
    private PagedResourcesAssembler<BookResponseDTO> pagedResourcesAssembler;
    private BookResponseDTO bookResponseDTO;
    private BookDTO bookDTO;
    @MockitoBean
    private H2ConsoleProperties h2ConsoleProperties;

    @BeforeEach
    void userSetUp() {
        //mocking을 위한 BookDTO 생성
        bookDTO = new BookDTO();
        bookDTO.setTitle("테스트 제목").setAuthor("테스트 저자").setIsbn("979-119-326-265-8")
                .setPublicationDate("2025").setCategory("FICTION").setRecommend((byte) 5);
        // Mocking을 위한 BookResponseDTO 객체 생성 (HATEOAS 링크 포함)
        bookResponseDTO = new BookResponseDTO(1L, "테스트 제목", "테스트 저자", "979-119-326-265-8",
                "2025", Category.FICTION, (byte) 5, 1L, "testuser", RoleEnum.USER);
        bookResponseDTO.add(linkTo(methodOn(BookController.class).getBookById(1L)).withSelfRel());
        bookResponseDTO.add(linkTo(methodOn(BookController.class).findAll(new BookSearchCondition(), Pageable.unpaged())).withRel("all-books"));
        bookResponseDTO.add(linkTo(methodOn(BookController.class).updateBook(1L, null)).withRel("update-book"));
        bookResponseDTO.add(linkTo(methodOn(BookController.class).deleteBook(1L)).withRel("delete-book"));
        //assembler.toModel()이 호출될 때 위에서 정의한 testBookResponseDTO를 반환하도록 Mocking
        when(assembler.toModel(any(BookResponseDTO.class))).thenReturn(bookResponseDTO);
        when(h2ConsoleProperties.getPath()).thenReturn("/h2-console");
    }

    @Test
    @DisplayName("책 생성 - 성공: 201 Created 응답 및 hateoas 링크 포함")
    @WithMockUser(username = "testuser", roles = "USER")
    void addBook_Success() throws Exception {
        // bookService.insert()가 호출될 때 testBookResponseDTO를 반환하도록 Mocking
        when(bookService.insert(any(BookDTO.class))).thenReturn(bookResponseDTO);

        //MockMvc를 사용하여 POST 요청 실행
        mockMvc.perform(post("/books/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO))) // testBookDTO 객체를 JSON 문자열로 변환하여 본문에 포함
                .andDo(print())
                .andExpect(status().isCreated()) // HTTP 상태 코드 201 Created 검증
                .andExpect(jsonPath("$.id").value(1L)) //응답 JSON의 'id' 필드 값 검증
                .andExpect(jsonPath("$.title").value("테스트 제목")) // 응답 JSON의 'title' 필드 값 검증
                // HATEOAS 링크가 응답에 포함되어 있는지 검증
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.all-books.href").exists())
                .andExpect(jsonPath("$._links.update-book.href").exists())
                .andExpect(jsonPath("$._links.delete-book.href").exists());
    }
}