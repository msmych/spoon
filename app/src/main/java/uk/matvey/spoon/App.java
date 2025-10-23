package uk.matvey.spoon;

import info.movito.themoviedbapi.TmdbApi;

public class App {

    static void main(String... args) {
        int port = 8080;
        if (args.length > 0) {
            if (args[0].equals("PROD")) {
                port = 10000;
            }
        }
        final var tmdbApi = new TmdbApi(System.getenv("TMDB_API_KEY"));
        new ServerModule(tmdbApi).javalinServer().start(port);
    }
}
