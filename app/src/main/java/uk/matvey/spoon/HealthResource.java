package uk.matvey.spoon;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class HealthResource implements Resource {

    public void register() {
        path("/health", () -> {
            get(ctx -> ctx.result("OK"));
        });
    }
}
