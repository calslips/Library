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
import java.util.ArrayList;
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

    /**
     * Tests that a new user can be added to the database.
     */
    @Test
    public void testCreateUser() {
        User user = new User(18, "testname");
        userDAO.createUser(user);
        Assert.assertTrue(userService.checkUser("testname"));
    }

    /**
     * Tests that all users can be retrieved from the database.
     */
    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            User user = new User(i, i + "");
            users.add(user);
            userDAO.createUser(user);
        }

        List<User> retrievedUsers = userDAO.getAllUsers();

        Assert.assertEquals(users.size(), retrievedUsers.size());
        Assert.assertTrue(retrievedUsers.containsAll(users));
    }

    /**
     * Tests that a specified user can be retrieved from the database by their unique user id.
     */
    @Test
    public void testGetUserById() {
        User userToGet = new User(123321, "retrieveMe");
        userDAO.createUser(userToGet);
        Assert.assertEquals(userToGet, userDAO.getUserById(123321));
    }

    /**
     * Tests that no user is retrieved from the database when there is no matching user id.
     */
    @Test
    public void testGetNoUserByNonexistentId() {
        User user = new User(32123, "ignoreMe");
        userDAO.createUser(user);
        Assert.assertNull(userDAO.getUserById(90210));
    }

    /**
     * Tests that the existence of a user can be validated by their username.
     */
    @Test
    public void testUserExistsByUsername() {
        User user = new User(53489, "iexistthereforeiam");
        userDAO.createUser(user);
        Assert.assertTrue(userDAO.userExists("iexistthereforeiam"));
    }

    /**
     * Tests that the existence of a user can be invalidated by their username.
     */
    @Test
    public void testUserDoesNotExistByUsername() {
        Assert.assertFalse(userDAO.userExists("whoami"));
    }

    /**
     * Tests that the existence of a user can be validated by their id.
     */
    @Test
    public void testUserExistsById() {
        User user = new User(55, "schfiftyfife");
        userDAO.createUser(user);
        Assert.assertTrue(userDAO.userExists(55));
    }

    /**
     * Tests that the existence of a user can be invalidated by their id.
     */
    @Test
    public void testUserDoesNotExistById() {
        Assert.assertFalse(userDAO.userExists(9000));
    }

    /**
     * Tests a user can delete their own account from the database.
     * @throws UserHasBooksSignedOut
     */
    @Test
    public void testDeleteUser() {
        User user = new User(12, "testname");
        userDAO.createUser(user);
        userDAO.deleteUser(user);
        Assert.assertFalse(userService.checkUser("testname"));
    }

    /**
     * Tests that when a user tries to delete their account with books signed out, an exception is thrown.
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
