package uk.matvey.spoon;

import info.movito.themoviedbapi.TmdbApi;
import io.javalin.Javalin;
import java.util.List;
import static io.javalin.apibuilder.ApiBuilder.path;

public class ServerModule {

    private final List<Resource> resources;

    public ServerModule(TmdbApi tmdbApi) {
        this.resources = List.of(
            new MovieResource(tmdbApi.getMovies())
        );
    }

    public Javalin javalinServer() {
        return Javalin.create(config -> {
            config.router.apiBuilder(() -> {
                new HealthResource().register();

                path("/api", () -> {
                    resources.forEach(Resource::register);
                });
            });
        });
    }
}
