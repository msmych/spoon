package uk.matvey.spoon.director;

import com.fasterxml.jackson.annotation.JsonFormat;
import info.movito.themoviedbapi.model.people.credits.MovieCredits;
import java.net.URI;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public record DirectorMoviesResultItem(
    int id,
    String title,
    Optional<String> originalTitle,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Optional<LocalDate> releaseDate,
    Optional<URI> posterUrl
) {

    public static List<DirectorMoviesResultItem> listFrom(MovieCredits movieCredits) {
        return movieCredits.getCrew().stream()
            .filter(item -> item.getJob().equals("Director"))
            .map(item -> {
                final var title = item.getTitle();
                final var originalTitle = Optional.of(item.getOriginalTitle())
                    .filter(ot -> !ot.equals(title));
                final var releaseDate = Optional.of(item.getReleaseDate())
                    .filter(rd -> !rd.isBlank())
                    .map(LocalDate::parse);
                final var posterUrl = Optional.ofNullable(item.getPosterPath())
                    .map(pp -> URI.create("https://image.tmdb.org/t/p/w342" + pp));
                return new DirectorMoviesResultItem(item.getId(), title, originalTitle, releaseDate, posterUrl);
            })
            .sorted(Comparator.comparing((DirectorMoviesResultItem item) -> item.releaseDate().orElse(LocalDate.MIN)).reversed())
            .toList();
    }
}
