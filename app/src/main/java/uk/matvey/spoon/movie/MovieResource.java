package uk.matvey.spoon.movie;

import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.tools.appendtoresponse.MovieAppendToResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.matvey.spoon.Resource;
import uk.matvey.spoon.movie.search.MovieSearchResource;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class MovieResource implements Resource {

    private static final Logger log = LoggerFactory.getLogger(MovieResource.class);

    private final MovieSearchResource movieSearchResource;
    private final TmdbMovies tmdbMovies;

    public MovieResource(MovieSearchResource movieSearchResource, TmdbMovies tmdbMovies) {
        this.movieSearchResource = movieSearchResource;
        this.tmdbMovies = tmdbMovies;
    }

    public void register() {
        path("/movies", () -> {
            movieSearchResource.register();
            get("/{id}", ctx -> {
                final var movieId = Integer.parseInt(ctx.pathParam("id"));
                final var movieDb = tmdbMovies.getDetails(movieId, "en-US", MovieAppendToResponse.CREDITS, MovieAppendToResponse.VIDEOS);
                ctx.json(MovieDetails.from(movieDb));
            });
        });
    }
}
