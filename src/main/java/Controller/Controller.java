package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;


public class Controller {

    public Javalin getAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
}
