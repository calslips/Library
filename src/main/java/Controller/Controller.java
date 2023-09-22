package Controller;

import Service.BookService;
import Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Book;
import Model.User;


public class Controller {
    ObjectMapper om = new ObjectMapper();
    BookService bookService;
    UserService userService;

    public Controller (BookService bookService, UserService userService){
        this.bookService = bookService;
        this.userService = userService;
    }

    public Javalin getAPI() {
        Javalin app = Javalin.create();
        app.get("books", this::getAllBooksHandler);
        app.get("users", this::getAllUsersHandler);
        app.post("books", this::postBooksHandler);
        app.post("users", this::postUsersHandler);
        app.put("books/{id}/signoutbook", this::putSignoutBooksHandler);
        app.put("books/{id}/returnbook", this::putReturnBooksHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllBooksHandler(Context context) {
        String title = context.queryParam("title");
        String author = context.queryParam("author");

        if (title != null && author != null) {
            context.json(bookService.getBooksByTitleAndAuthor(title, author));
        } else if (title != null) {
            context.json(bookService.getBooksByTitle(title));
        } else if (author != null) {
            context.json(bookService.getBooksByAuthor(author));
        } else {
            context.json(bookService.getAllBooks());
        }
    }

    private void getAllUsersHandler(Context context) {
        context.json(userService.getAllUsers());
    }

    private void postBooksHandler(Context context){
        ObjectMapper om = new ObjectMapper();
        try {
            Book b = om.readValue(context.body(), Book.class);
            bookService.addBook(b);
            context.status(201);
        }catch(JsonProcessingException e){
            e.printStackTrace();
            context.status(400);
        }
    }

    private void postUsersHandler(Context context){
        try {
            User user = om.readValue(context.body(), User.class);
            userService.createUser(user);
            context.status(201);
        }catch(JsonProcessingException e){
            e.printStackTrace();
            context.status(400);
        }
    }

    private void putSignoutBooksHandler(Context context) {
        try {
            User user = om.readValue(context.body(), User.class);
            int userId = user.getUserId();
            int bookId = Integer.parseInt(context.pathParam("id"));
            bookService.signOutBook(bookId, userId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            context.status(400);
        }
    }

    private void putReturnBooksHandler(Context context) {
        int bookId = Integer.parseInt(context.pathParam("id"));
        bookService.returnBook(bookId);
    }
}
