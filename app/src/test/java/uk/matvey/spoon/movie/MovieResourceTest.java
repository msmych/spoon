package uk.matvey.spoon.movie;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import uk.matvey.spoon.FunctionalTestBase;
import static org.assertj.core.api.Assertions.assertThat;

class MovieResourceTest extends FunctionalTestBase {

    @Test
    @EnabledIfEnvironmentVariable(named = "TMDB_API_KEY", matches = ".+")
    void shouldReturnMovieDetailsById() {
        final var rs = httpGet("/api/movies/100");

        assertThat(rs.statusCode()).isEqualTo(200);
        final var body = rs.body();
        assertThat(body).contains("100");
    }
}