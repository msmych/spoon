package uk.matvey.spoon.movie;

import com.fasterxml.jackson.annotation.JsonFormat;
import info.movito.themoviedbapi.model.core.NamedIdElement;
import info.movito.themoviedbapi.model.movies.Credits;
import info.movito.themoviedbapi.model.movies.MovieDb;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record MovieDetails(
    int id,
    String title,
    String overview,
    Optional<String> originalTitle,
    Optional<String> tagline,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Optional<LocalDate> releaseDate,
    String runTime,
    Optional<URI> posterUrl,
    Optional<URI> backdropUrl,
    Optional<List<Director>> directors,
    List<String> genres,
    Map<String, URI> links
) {

    public record Director(int id, String name) {
    }

    public static MovieDetails from(MovieDb movieDb) {
        final var title = movieDb.getTitle();
        final var originalTitle = Optional.of(movieDb.getOriginalTitle())
            .filter(ot -> !ot.equals(title));
        final var releaseDate = Optional.of(movieDb.getReleaseDate())
            .filter(rd -> !rd.isBlank())
            .map(LocalDate::parse);
        final var posterUrl = Optional.ofNullable(movieDb.getPosterPath())
            .map(pp -> URI.create("https://image.tmdb.org/t/p/w342" + pp));
        final var backdropUrl = Optional.ofNullable(movieDb.getBackdropPath())
            .map(bp -> URI.create("https://image.tmdb.org/t/p/w780" + bp));
        final var directors = Optional.ofNullable(movieDb.getCredits())
            .map(Credits::getCrew)
            .map(crew -> crew.stream()
                .filter(member -> member.getJob().equals("Director"))
                .map(d -> new Director(d.getId(), d.getName()))
                .toList());
        final var runTimeMinutes = Duration.ofMinutes(movieDb.getRuntime());
        final var runTime = String.format("%d:%02d", runTimeMinutes.toHoursPart(), runTimeMinutes.toMinutesPart());
        return new MovieDetails(
            movieDb.getId(),
            title,
            movieDb.getOverview(),
            originalTitle,
            Optional.ofNullable(movieDb.getTagline()),
            releaseDate,
            runTime,
            posterUrl,
            backdropUrl,
            directors,
            movieDb.getGenres().stream().map(NamedIdElement::getName).toList(),
            Stream.of(
                    Map.entry("TMDb", URI.create("https://www.themoviedb.org/movie/" + movieDb.getId())),
                    Map.entry("IMDb", Optional.ofNullable(movieDb.getImdbID()).map(imdbId -> URI.create("https://www.imdb.com/title/" + imdbId)).orElse(null))
                )
                .filter(e -> e.getValue() != null)
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }
}
