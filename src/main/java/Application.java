import Controller.Controller;
import DAO.UserDAO;
import DAO.BookDAO;
import Service.UserService;
import Service.BookService;
import Util.ConnectionSingleton;

import java.sql.Connection;

public class Application {
    public static void main(String[] args) {
        Connection conn = ConnectionSingleton.getConnection();
        UserDAO userDAO = new UserDAO(conn);
        BookDAO bookDAO = new BookDAO(conn);
        UserService userService = new UserService(userDAO, bookDAO);
        BookService bookService = new BookService(bookDAO);
        Controller controller = new Controller(bookService, userService);
        controller.getAPI().start();
    }
}