import DAO.UserDAO;
import DAO.BookDAO;
import Exceptions.BookSignedOutException;
import Exceptions.UserHasBooksSignedOut;
import Model.User;
import Model.Book;
import Util.ConnectionSingleton;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import Service.UserService;

import java.sql.Connection;
import java.util.List;

public class UserDAOTest {

    Connection conn;
    UserDAO userDAO;
    BookDAO bookDAO;

    UserService userService;

    @Before
    public void setUp(){
        conn = ConnectionSingleton.getConnection();
        ConnectionSingleton.resetTestDatabase();
        bookDAO = new BookDAO(conn);
        userDAO = new UserDAO(conn);
        userService = new UserService(userDAO, bookDAO);
    }

    @Test
    public void testCreateUser() {
        User user = new User(18, "testname");
        userDAO.createUser(user);
        Assert.assertTrue(userService.checkUser("testname"));
    }

    @Test
    public void testDeleteUser() {
        User user = new User(12, "testname");
        userDAO.createUser(user);
        userDAO.deleteUser(user);
        Assert.assertFalse(userService.checkUser("testname"));
    }

    /**
     * test when user tries to delete their account with books signed out, exception is thrown
     * @throws UserHasBooksSignedOut
     */
    @Test
    public void testDeleteUserFailsWhenBooksSignedOut() throws UserHasBooksSignedOut {
        User user = new User(44, "keepingBooks123");
        Book testBook = new Book(33, "testAuthor", "testTitle");

        userDAO.createUser(user);
        bookDAO.insertBook(testBook);
        bookDAO.updateSignedOutBy(testBook, user.getUserId());

        Assert.assertThrows(UserHasBooksSignedOut.class, () -> {
            userService.deleteUser(user.getUserId(), user.getUserId());
        });
    }
}
