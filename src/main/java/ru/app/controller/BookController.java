package ru.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.app.model.Book;
import ru.app.exception.InvalidRequestParameterException;
import ru.app.service.BookService;
import ru.app.util.ValidParameters;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/top10")
    public ResponseEntity<List<Book>> top10(@RequestParam(required = false) Integer year,
                                            @RequestParam(required = false) String column,
                                            @RequestParam(required = false) String sort) {

        if(column == null || sort == null) {
            throw new InvalidRequestParameterException("Отсутствуют обязательные параметры: column и/или sort");
        }
        if (!ValidParameters.isValidColumnParameter(column)) {
            throw new InvalidRequestParameterException("Некорректный параметр column, возможные параметры: ",
                                                        ValidParameters.getListRequestColumnParameters());
        }
        if (!ValidParameters.isValidSortParameter(sort)) {
            throw new InvalidRequestParameterException("Некорректный параметр sort, возможные параметры: ",
                                                        ValidParameters.getListRequestSortParameters());
        }
        if (year == null) {
            return ResponseEntity.ok(bookService.getSortedBooksByParams(column, sort));
        } else {
            return ResponseEntity.ok(bookService.getSortedBooksByParams(year, column, sort));
        }
    }

    @ExceptionHandler(InvalidRequestParameterException.class)
    public ResponseEntity<String> invalidParameters(InvalidRequestParameterException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
