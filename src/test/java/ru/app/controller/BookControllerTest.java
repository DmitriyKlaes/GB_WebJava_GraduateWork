package ru.app.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.app.model.Book;
import ru.app.service.BookService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    BookService bookService;

    private List<Book> getBooks() {
        List<Book> resultTestListBook = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book book = new Book();
            book.setTitle("testbook" + i);
            book.setAuthors("testauthor" + i);
            book.setNumPages(i);
            book.setPublicationDate(formatStringToLocalDate("January " + i + ", 2024"));
            book.setRatingScore(4.0 + 1.0 * i / 100);
            book.setNumRatings(i);
            resultTestListBook.add(book);
        }
        return resultTestListBook;
    }

    private LocalDate formatStringToLocalDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"book", "author", "numPages", "publicationDate", "rating", "numberOfVoters"})
    void top10WithoutYearParameterSuccessTest(String input) throws Exception {

        Mockito.when(this.bookService.getSortedBooksByParams(input, "ASC"))
                .thenReturn(getBooks());

        mvc.perform(get("/api/top10?column=" + input + "&sort=ASC"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.length()").value(10));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ASC", "DESC"})
    void top10WithYearParameterSuccessTest(String input) throws Exception {

        Mockito.when(this.bookService.getSortedBooksByParams(2024, "book", input))
                .thenReturn(getBooks());

        mvc.perform(get("/api/top10?year=2024&column=book&sort=" + input))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10));
    }

    @Test
    void top10BadColumnParamFailureTest() throws Exception {

        Mockito.when(this.bookService.getSortedBooksByParams(2024, "badparam", "ASC"))
                .thenReturn(getBooks());

        mvc.perform(get("/api/top10?year=2024&column=badparam&sort=ASC"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void top10BadSortParamFailureTest() throws Exception {

        Mockito.when(this.bookService.getSortedBooksByParams(2024, "book", "badparam"))
                .thenReturn(getBooks());

        mvc.perform(get("/api/top10?year=2024&column=book&sort=badparam"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void top10WithoutRequiredParamFailureTest() throws Exception {

        mvc.perform(get("/api/top10"))
                .andExpect(status().isBadRequest());
    }
}