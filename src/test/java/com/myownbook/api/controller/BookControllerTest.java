package com.myownbook.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myownbook.api.dto.BookDTO;
import com.myownbook.api.dto.BookResponseDTO;
import com.myownbook.api.dto.BookSearchCondition;
import com.myownbook.api.model.Category;
import com.myownbook.api.model.RoleEnum;
import com.myownbook.api.security.SecurityConfig;
import com.myownbook.api.service.BookService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    @DisplayName("책 생성 - 실패: 400 Bad request 응답 및 에러")
    @WithMockUser(username = "testuser", roles = "USER")
    void addBook_fail() throws Exception{
        BookDTO wrongBookDTO = new BookDTO();
        wrongBookDTO.setTitle("").setAuthor("").setIsbn("23423").setPublicationDate("202401").setCategory("RELIGION").setRecommend((byte)-1);

        mockMvc.perform(post("/books/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wrongBookDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PARAMETER"))
                .andExpect(jsonPath("$.message").value("Invalid parameter included"))
                .andExpect(jsonPath("$.errors[?(@.field == 'recommend')].message").value("최소 0점부터 입력이 가능합니다."))
                .andExpect(jsonPath("$.errors[?(@.field == 'publicationDate')].message").value("'yyyy' 형식으로 년도만 입력해 주세요."))
                .andExpect(jsonPath("$.errors[?(@.field == 'isbn')].message").value("13자리의 isbn형식을 입력해 주세요."))
                .andExpect(jsonPath("$.errors[?(@.field == 'author')].message").value("저자은 필수 입니다."))
                .andExpect(jsonPath("$.errors[?(@.field == 'title')].message").value("제목은 필수 입니다."));
    }

    @Test
    @DisplayName("책 생성 - 실패: 401 Unauthorized 응답")
    void addZBook_noAuth() throws Exception{
        when(bookService.insert(any(BookDTO.class))).thenReturn(bookResponseDTO);

        mockMvc.perform(post("/books/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(CoreMatchers.is(""))); //빈 body 반환 체크
    }

    @Test
    @DisplayName("책 전체 조회 - 성공: 200 success 응답")
    void findAllBook_success() throws Exception {
        List<BookResponseDTO> contentList = Arrays.asList(bookResponseDTO);
        PageImpl<BookResponseDTO> mockServicePage = new PageImpl<>(contentList, Pageable.ofSize(5).withPage(0), 1);

        when(bookService.searchAll(any(BookSearchCondition.class), any(Pageable.class))).thenReturn(mockServicePage);

        List<BookResponseDTO> entityModelList = Arrays.asList(assembler.toModel(bookResponseDTO));
        Link baseLink = linkTo(methodOn(BookController.class).findAll(new BookSearchCondition(), Pageable.unpaged())).withSelfRel();

        PagedModel<BookResponseDTO> mockPagedModel = PagedModel.of(entityModelList, new PagedModel.PageMetadata(5, 0, 1, 1), baseLink);

        when(pagedResourcesAssembler.toModel(any(Page.class), any(Link.class))).thenReturn(mockPagedModel);

        mockMvc.perform(get("/books")
                .accept(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "5")
                .param("sort", "id, desc")
                .content(objectMapper.writeValueAsString(new BookSearchCondition())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.bookResponseDTOList").isArray())
                .andExpect(jsonPath("$._embedded.bookResponseDTOList[0].id").value(bookResponseDTO.getId()))
                .andExpect(jsonPath("$._embedded.bookResponseDTOList[0].title").value(bookResponseDTO.getTitle()))
                .andExpect(jsonPath("$._embedded.bookResponseDTOList[0].author").value(bookResponseDTO.getAuthor()))
                .andExpect(jsonPath("$._embedded.bookResponseDTOList[0]._links.self.href").exists())
                .andExpect(jsonPath("$.page.size").value(5))
                .andExpect(jsonPath("$.page.totalElements").value(1))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("책 전체 조회 - 실패: 400 Bad Request 응답(검색조건 유효성)")
    void findAllBook_fail() throws Exception {

        mockMvc.perform(get("/books")
                .accept(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "5")
                .param("sort", "id, desc")
                .param("recommend", "6")) //별점 max: 5 이하 검증
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PARAMETER"))
                .andExpect(jsonPath("$.message").value("Invalid parameter included"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[?(@.field == 'recommend')].message").value("최대 5점까지 입력이 가능합니다."));
    }
}