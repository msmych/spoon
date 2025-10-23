package uk.matvey.spoon.movie.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

public record MovieSearchResultItem(
    int id,
    String title,
    Optional<String> originalTitle,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Optional<LocalDate> releaseDate,
    String overview,
    Optional<URI> posterUrl,
    Optional<URI> tinyPosterUrl
) {

}
