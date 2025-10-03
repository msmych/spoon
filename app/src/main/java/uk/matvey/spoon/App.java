package uk.matvey.spoon;

import info.movito.themoviedbapi.TmdbApi;

public class App {

    static void main() {
        final var tmdbApi = new TmdbApi(System.getenv("TMDB_API_KEY"));
        new ServerModule(tmdbApi).javalinServer().start(8080);
    }
}
