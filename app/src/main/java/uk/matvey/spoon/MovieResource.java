package uk.matvey.spoon;

import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.tools.appendtoresponse.MovieAppendToResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class MovieResource implements Resource {

    private static final Logger log = LoggerFactory.getLogger(MovieResource.class);

    private final TmdbMovies tmdbMovies;

    public MovieResource(TmdbMovies tmdbMovies) {
        this.tmdbMovies = tmdbMovies;
    }

    public void register() {
        path("/movies", () -> {
            get("/{id}", ctx -> {
                final var movieId = Integer.parseInt(ctx.pathParam("id"));
                final var details = tmdbMovies.getDetails(movieId, "en-US", MovieAppendToResponse.CREDITS);
                ctx.json(details);
            });
        });
    }
}
