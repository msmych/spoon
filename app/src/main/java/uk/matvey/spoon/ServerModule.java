package uk.matvey.spoon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import info.movito.themoviedbapi.TmdbApi;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import java.util.List;
import uk.matvey.spoon.director.DirectorResource;
import uk.matvey.spoon.movie.MovieResource;
import uk.matvey.spoon.movie.search.MovieSearchResource;
import static io.javalin.apibuilder.ApiBuilder.path;

public class ServerModule {

    private static final ObjectMapper objectMapper = JsonMapper.builder()
        .addModule(new Jdk8Module())
        .addModule(new JavaTimeModule())
        .build();

    private final List<Resource> resources;

    public ServerModule(TmdbApi tmdbApi) {
        this.resources = List.of(
            new MovieResource(new MovieSearchResource(tmdbApi.getSearch()), tmdbApi.getMovies()),
            new DirectorResource(tmdbApi.getPeople())
        );
    }

    public Javalin javalinServer() {
        return Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson(objectMapper, true));
            config.router.apiBuilder(() -> {
                new HealthResource().register();

                path("/api", () -> {
                    resources.forEach(Resource::register);
                });
            });
        });
    }
}
