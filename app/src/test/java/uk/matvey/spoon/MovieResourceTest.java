package uk.matvey.spoon;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class MovieResourceTest extends FunctionalTestBase {

    @Test
    void shouldReturnMovieDetailsById() {
        final var rs = httpGet("/api/movies/100");

        assertThat(rs.statusCode()).isEqualTo(200);
        final var body = rs.body();
        assertThat(body).contains("100");
    }
}