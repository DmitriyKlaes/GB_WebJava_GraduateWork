package ru.app.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Repository;
import ru.app.model.Book;
import ru.app.properties.CsvFilePathProperties;
import ru.app.util.CsvUtil;

import java.util.ArrayList;
import java.util.List;

@Repository
@EnableConfigurationProperties(CsvFilePathProperties.class)
public class BookRepository {

    private final CsvFilePathProperties path;
    private final List<Book> books;

    public BookRepository(CsvFilePathProperties path) {
        this.path = path;
        this.books = new ArrayList<>();
    }

    @PostConstruct
    private void fillBooks() {
        CsvUtil.fillListBooksFromCsv(path.getPath(), this.books);
    }

    public List<Book> getBooks() {
        return List.copyOf(this.books);
    }

}
