package uk.matvey.spoon.director;

import info.movito.themoviedbapi.TmdbPeople;
import uk.matvey.spoon.Resource;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class DirectorResource implements Resource {

    private final TmdbPeople tmdbPeople;

    public DirectorResource(TmdbPeople tmdbPeople) {
        this.tmdbPeople = tmdbPeople;
    }

    @Override
    public void register() {
        path("/directors", () -> {
            path("/{id}", () -> {
                get("/movies", ctx -> {
                    final var directorId = Integer.parseInt(ctx.pathParam("id"));
                    final var response = tmdbPeople.getMovieCredits(directorId, null);
                    ctx.json(DirectorMoviesResultItem.listFrom(response));
                });
            });
        });
    }
}
