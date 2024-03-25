package ru.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.app.model.Book;
import ru.app.repository.BookRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final int responseLimit = 10;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getSortedBooksByParams(String columnParam, String sortParam) {
        return bookRepository.getBooks()
                             .stream()
                             .filter(it -> isValidFieldForSort(it, columnParam))
                             .sorted(getComparator(columnParam, sortParam))
                             .limit(responseLimit)
                             .toList();
    }

    public List<Book> getSortedBooksByParams(Integer yearParam, String columnParam, String sortParam) {
        return bookRepository.getBooks()
                             .stream()
                             .filter(it -> it.getPublicationDate() != null &&
                                           Objects.equals(it.getPublicationDate().getYear(), yearParam))
                             .filter(it -> isValidFieldForSort(it, columnParam))
                             .sorted(getComparator(columnParam, sortParam))
                             .limit(responseLimit)
                             .toList();
    }

    private boolean isValidFieldForSort(Book book, String columnParam) {
        switch (columnParam) {
            case "book" -> {
                return book.getTitle() != null;
            }
            case "author" -> {
                return book.getAuthors() != null;
            }
            case "numPages" -> {
                return book.getNumPages() != null;
            }
            case "publicationDate" -> {
                return book.getPublicationDate() != null;
            }
            case "rating" -> {
                return book.getRatingScore() != null;
            }
            case "numberOfVoters" -> {
                return book.getNumRatings() != null;
            }
            default -> {
                return false;
            }
        }
    }

    private Comparator<Book> getComparator(String columnParam, String sortParam) {
        return (o1, o2) -> {
            switch (sortParam) {
                case "ASC" -> {
                    return compareByColumnParam(o1, o2, columnParam);
                }
                case "DESC" -> {
                    return compareByColumnParam(o2, o1, columnParam);
                }
            }
            return 0;
        };
    }

    private int compareByColumnParam(Book firstBook, Book secondBook, String columnParam) {
        switch (columnParam) {
            case "book" -> {
                return firstBook.getTitle().compareTo(secondBook.getTitle());
            }
            case "author" -> {
                return firstBook.getAuthors().compareTo(secondBook.getAuthors());
            }
            case "numPages" -> {
                return firstBook.getNumPages().compareTo(secondBook.getNumPages());
            }
            case "publicationDate" -> {
                return firstBook.getPublicationDate().compareTo(secondBook.getPublicationDate());
            }
            case "rating" -> {
                return firstBook.getRatingScore().compareTo(secondBook.getRatingScore());
            }
            case "numberOfVoters" -> {
                return firstBook.getNumRatings().compareTo(secondBook.getNumRatings());
            }
            default -> {
                log.error("Не найден параметр для сравнения для компаратора!");
                return 0;
            }
        }
    }
}
