package ru.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Book {

    @JsonProperty("id")
    Integer id;

    @JsonProperty("book")
    String title;

    @JsonProperty("series")
    String seriesTitle;

    @JsonProperty("releaseNumber")
    String seriesReleaseNumber;

    @JsonProperty("author")
    String authors;

    @JsonProperty("description")
    String description;

    @JsonProperty("numPages")
    Integer numPages;

    @JsonProperty("format")
    String format;

    @JsonProperty("genres")
    List<String> genres;

    @JsonProperty("publicationDate")
    LocalDate publicationDate;

    @JsonProperty("rating")
    Double ratingScore;

    @JsonProperty("numberOfVoters")
    Integer numRatings;
}
