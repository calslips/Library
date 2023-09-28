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
     * GET request handler for all books.
     * Can narrow search by both author and title or either value, received via query parameters.
     * Search values 'sanitized' by removing beginning/end whitespace and lower-casing letters.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllBooksHandler(Context context) {
        String title = context.queryParam("title").trim().toLowerCase();
        String author = context.queryParam("author").trim().toLowerCase();

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

    /**
     * POST request handler for creating a book resource.
     * If the book is invalid (null) responds with a status code 400.
     * Otherwise, responds with the book in JSON format.
     * @param context
     */
    private void postBooksHandler(Context context){
        ObjectMapper om = new ObjectMapper();
        try {
            Book b = om.readValue(context.body(), Book.class);
            Book addedBook = bookService.addBook(b);
            if (addedBook == null) {
                context.status(400);
            } else {
                context.json(addedBook);
            }
        }catch(JsonProcessingException e){
            e.printStackTrace();
            context.status(400);
        }
    }

    /**
     * This handler creates a new user.
     * If the username is already in use or invalid username is provided,
     * no user is created and responds with a status code 400.
     * Otherwise, the user is created and user info is contained in the response.
     * @param context
     */
    private void postUsersHandler(Context context){
        try {
            User user = om.readValue(context.body(), User.class);
            User createdUser = userService.createUser(user);
            if (createdUser == null) {
                context.status(400);
            } else {
                context.json(createdUser);
            }
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
            User currentUser = om.readValue(context.body(), User.class);
            int deleteUserId = Integer.parseInt(context.pathParam("id"));
            User deletedUser = userService.deleteUser(currentUser.getUserId(), deleteUserId);
            if (deletedUser == null) {
                context.status(401);
            } else {
                context.json(deletedUser);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            context.status(400);
        } catch (UserHasBooksSignedOut e) {
            e.printStackTrace();
            context.status(400);
        }
    }
}
