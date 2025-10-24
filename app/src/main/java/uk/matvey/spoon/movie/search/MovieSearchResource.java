package uk.matvey.spoon.movie.search;

import info.movito.themoviedbapi.TmdbSearch;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;
import uk.matvey.spoon.Resource;
import static io.javalin.apibuilder.ApiBuilder.get;

public class MovieSearchResource implements Resource {

    private final TmdbSearch tmdbSearch;

    public MovieSearchResource(TmdbSearch tmdbSearch) {
        this.tmdbSearch = tmdbSearch;
    }

    @Override
    public void register() {
        get("/search", ctx -> {
            final var q = ctx.queryParam("q");
            final var movies = tmdbSearch.searchMovie(q, null, null, null, null, null, null)
                .getResults()
                .stream().map(item -> {
                    final var title = item.getTitle();
                    final var originalTitle = Optional.of(item.getOriginalTitle())
                        .filter(ot -> !ot.equals(title));
                    final var releaseDate = Optional.of(item.getReleaseDate()).filter(rd -> !rd.isBlank())
                        .map(LocalDate::parse);
                    final var posterUrl = Optional.ofNullable(item.getPosterPath())
                        .map(pp -> URI.create("https://image.tmdb.org/t/p/w342" + pp));
                    final var tinyPosterUrl = Optional.ofNullable(item.getPosterPath())
                        .map(pp -> URI.create("https://image.tmdb.org/t/p/w92" + pp));
                    return new MovieSearchResultItem(item.getId(),
                        title,
                        originalTitle,
                        releaseDate,
                        item.getOverview(),
                        posterUrl,
                        tinyPosterUrl);
                })
                .toList();
            ctx.json(movies);
        });
    }
}
