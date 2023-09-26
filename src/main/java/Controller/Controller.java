package Controller;

import Exceptions.BookSignedOutException;
import Exceptions.UserHasBooksSignedOut;
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
        app.patch("books/{id}", this::patchBookSignedOutBy);
        app.delete("users/{id}", this::deleteUserHandler);
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

    /**
     * This is handler updates the signedOutBy property of the book
     * if the current user has the book signed out, they will return it
     * if the book is not currently signed out, the current user will sign it out
     * if the book is signed out by another user, a BookSignedOutException will be thrown/caught
     * @param context
     */
    private void patchBookSignedOutBy(Context context) {
        try {
            User user = om.readValue(context.body(), User.class);
            int userId = user.getUserId();
            int bookId = Integer.parseInt(context.pathParam("id"));
            Book updatedBook = bookService.updateBookSignedOutBy(bookId, userId);
            context.json(updatedBook);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            context.status(400);
        } catch (BookSignedOutException e) {
            e.printStackTrace();
            context.status(400);
        }
    }

    /**
     * endpoint handler used to delete user account
     * @param context
     * */
    private void deleteUserHandler(Context context) {
        try {
            int userId = Integer.parseInt(context.pathParam("id"));
            userService.deleteUser(userId);
        } catch (UserHasBooksSignedOut e) {
            e.printStackTrace();
            context.status(400);
        }
    }
}
