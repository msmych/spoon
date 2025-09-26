package uk.matvey.spoon;

import info.movito.themoviedbapi.TmdbApi;
import io.javalin.Javalin;
import java.util.List;

public class ServerModule {

    private final List<Resource> resources;

    public ServerModule(TmdbApi tmdbApi) {
        this.resources = List.of(
            new HealthResource(),
            new MovieResource(tmdbApi.getMovies())
        );
    }

    public Javalin javalinServer() {
        return Javalin.create(config -> {
            config.router.apiBuilder(() -> {
                resources.forEach(Resource::register);
            });
        });
    }
}
