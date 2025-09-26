package uk.matvey.spoon;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class HealthResourceTest extends FunctionalTestBase {

    @Test
    void shouldReturnOK() {
        final var rs = httpGet("/health");

        assertThat(rs.statusCode()).isEqualTo(200);
        assertThat(rs.body()).isEqualTo("OK");
    }
}