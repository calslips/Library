import DAO.UserDAO;
import DAO.BookDAO;
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
        userService = new UserService(userDAO);
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
        userDAO.deleteUser(user.getUsername());
        Assert.assertFalse(userService.checkUser("testname"));
    }


}
