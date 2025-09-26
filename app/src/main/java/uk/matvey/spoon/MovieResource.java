package uk.matvey.spoon;

import info.movito.themoviedbapi.TmdbMovies;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class MovieResource implements Resource {

    private final TmdbMovies tmdbMovies;

    public MovieResource(TmdbMovies tmdbMovies) {
        this.tmdbMovies = tmdbMovies;
    }

    public void register() {
        path("/movies", () -> {
            get("/{id}", ctx -> {
                final var movieId = Integer.parseInt(ctx.pathParam("id"));
                final var details = tmdbMovies.getDetails(movieId, "en-US");
                ctx.json(details);
            });
        });
    }
}
