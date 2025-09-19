package com.myownbook.api.controller;

import com.myownbook.api.dto.BookResponseDTO;
import com.myownbook.api.dto.BookSearchCondition;
import com.myownbook.api.service.BookService;
import com.myownbook.api.model.Book;
import com.myownbook.api.dto.BookDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@Tag(name = "Book API", description = "도서 관리 API")
@RestController
@RequestMapping("/books")
public class BookController {

    private BookService service;
    private Logger log = LoggerFactory.getLogger(getClass());

    public BookController(BookService service) {
        this.service = service;
    }

    @PostMapping("/add")
    @Operation(summary = "도서 생성", description = "새로운 도서를 등록합니다.", operationId = "1_addBook")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "success", content = {@Content(schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400", description = "bad request")
    })
    public ResponseEntity<BookResponseDTO> add(@RequestBody @Valid BookDTO bookDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insert(bookDTO));
    }

    @GetMapping()
    @Operation(summary = "도서 리스트 조회", description = "전체 도서 목록을 조회합니다, title, author, category, recommend 별로 검색이 가능합니다.", operationId = "2_getListBook")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "400", description = "bad request")
    })
    public ResponseEntity<Page<BookResponseDTO>> findAll(@Valid BookSearchCondition condition) {
        Page<BookResponseDTO> books = service.searchAll(condition);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "도서 상세 조회 - id", description = "id에 해당하는 도서 상세를 조회힙니다.", operationId = "3_getBookById")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = {@Content(schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400", description = "bad request")
    })
    @Parameter(name = "id", description = "id로 도서를 조회", example = "3")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "도서 상세 조회 - isbn", description = "isbn에 해당하는 도서 상세를 조회힙니다.", operationId = "4_getBookByIsbn")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = {@Content(schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400", description = "bad request")
    })
    @Parameter(name = "isbn", description = "isbn로 도서를 조회", example = "979-116-755-330-0")
    public ResponseEntity<BookResponseDTO> getBookByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(service.findByIsbn(isbn));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "도서 수정", description = "id에 해당하는 도서를 수정합니다. (제목과 저자를 수정 해보겠습니다.)", operationId = "5_updateBook",
            parameters = @Parameter(name = "id", description = "수정할 도서 id", required = true, example = "2"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정할 도서 정보 (제목, 저자만 수정하겠습니다)",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "도서 수정 예",
                                            value = """
                                                    {
                                                        "title": "수정한 제목",
                                                        "author": "수정한 저자"
                                                    }
                                                    """
                                    )
                            }
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = {@Content(schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400", description = "bad request")
    })
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long id, @RequestBody @Valid BookDTO bookDTO) {
        return ResponseEntity.ok(service.updateBook(id, bookDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "도서 삭제", description = "id에 해당하는 도서를 삭제합니다.", operationId = "6_deleteBook")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "400", description = "bad request")
    })
    @Parameter(name = "id", description = "삭제할 도서의 id", example = "1")
    private ResponseEntity<Map<String, String>> deleteBook(@PathVariable Long id) {
        String deletedBookTitle = service.deleteBook(id);
        return ResponseEntity.ok().body(Map.of("status", "삭제 되었습니다"));
    }
}
