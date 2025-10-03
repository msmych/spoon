package uk.matvey.spoon;

import info.movito.themoviedbapi.TmdbApi;
import io.javalin.Javalin;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class FunctionalTestBase {

    private static final TmdbApi tmdbApi = new TmdbApi(System.getenv("TMDB_API_KEY"));
    private static Javalin server;

    protected final HttpClient http = HttpClient.newBuilder().build();

    protected HttpResponse<String> httpGet(String path) {
        try {
            return http.send(HttpRequest.newBuilder()
                .GET().uri(URI.create("http://localhost:8080" + path)).build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        server = new ServerModule(tmdbApi).javalinServer();
        server.start(8080);
    }

    @AfterAll
    static void afterAll() {
        server.stop();
    }
}
