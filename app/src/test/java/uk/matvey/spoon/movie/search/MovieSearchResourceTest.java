package uk.matvey.spoon.movie.search;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.matvey.spoon.FunctionalTestBase;
import static org.assertj.core.api.Assertions.assertThat;

class MovieSearchResourceTest extends FunctionalTestBase {

    private static final Logger log =  LoggerFactory.getLogger(MovieSearchResourceTest.class);

    @Test
    @EnabledIfEnvironmentVariable(named = "TMDB_API_KEY", matches = ".+")
    void shouldSearchMovies() {
        final var rs = httpGet("/api/movies/search?q=pulp%20f");

        assertThat(rs.statusCode()).isEqualTo(200);
        final var body = rs.body();
        assertThat(body).contains("Pulp Fiction");
    }
}