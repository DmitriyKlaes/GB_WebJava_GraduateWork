package ru.app.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ru.app.model.Book;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


@Slf4j
public class CsvUtil {

    public static void fillListBooksFromCsv(String filePath, List<Book> listForFill) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
                BufferedReader buffReader = new BufferedReader(reader);
                CSVParser csvParser = new CSVParser(buffReader,
                        CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                listForFill.add(mapCsvToBook(csvRecord));
            }
        } catch (IOException e) {
            throw new RuntimeException("Невозможно распарсить данные из CSV файла: " + e.getMessage());
        }
    }

    private static Book mapCsvToBook(CSVRecord csvRecord) {
        Book newBook = new Book();
        try {
            newBook.setId(parseStringToIntegerOrNull(csvRecord.get(0)));
            newBook.setTitle(csvRecord.get("title"));
            newBook.setSeriesTitle(csvRecord.get("series_title"));
            newBook.setSeriesReleaseNumber(csvRecord.get("series_release_number"));
            newBook.setAuthors(csvRecord.get("authors"));
            newBook.setDescription(csvRecord.get("description"));
            newBook.setNumPages(parseStringToIntegerOrNull(csvRecord.get("num_pages")));
            newBook.setFormat(csvRecord.get("format"));
            newBook.setGenres(parseStringToListGenres(csvRecord.get("genres")));
            newBook.setPublicationDate(parseStringToDLocalDateOrNull(csvRecord.get("publication_date")));
            newBook.setRatingScore(parseStringToDoubleOrNull(csvRecord.get("rating_score")));
            newBook.setNumRatings(parseStringToIntegerOrNull(csvRecord.get("num_ratings")));
        } catch (IllegalStateException e) {
            log.error("Не найдены заголовки в файле *.csv");
        } catch (IllegalArgumentException e) {
            log.error("Не найден запрашиваемый заголовок!");
        }
        return newBook;
    }

    private static Integer parseStringToIntegerOrNull(String stringForParse) {
        try {
            String resultString = stringForParse;
            if (stringForParse.contains(".")) {
                resultString = trimFloatingPoint(stringForParse);
            }
            return Integer.parseInt(resultString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String trimFloatingPoint(String stringForParse) {
        int dotIndex = stringForParse.indexOf(".");
        return stringForParse.substring(0, dotIndex);
    }

    private static Double parseStringToDoubleOrNull(String stringForParse) {
        try {
            return Double.parseDouble(stringForParse);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static LocalDate parseStringToDLocalDateOrNull(String stringForParse) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
            return LocalDate.parse(stringForParse, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static List<String> parseStringToListGenres(String stringForParse) {
        return Arrays.stream((stringForParse.replaceAll("[]\\[',]", "")
                                            .trim()
                                            .split(" ")))
                     .toList();
    }
}
