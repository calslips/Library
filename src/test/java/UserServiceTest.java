import DAO.BookDAO;
import DAO.UserDAO;
import Exceptions.UserHasBooksSignedOut;
import Model.Book;
import Model.User;
import Service.BookService;
import Service.UserService;
import Util.ConnectionSingleton;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class UserServiceTest {
    Connection conn;
    UserDAO mockUserDAO;
    BookDAO mockBookDAO;
    UserService mockUserService;
    BookService mockBookService;
    UserDAO realUserDAO;
    BookDAO realBookDAO;
    UserService realUserService;
    BookService realBookService;

    @Before
    public void setUp(){
        mockUserDAO = Mockito.mock(UserDAO.class);
        mockBookDAO = Mockito.mock(BookDAO.class);
        mockUserService = new UserService(mockUserDAO, mockBookDAO);
        mockBookService = new BookService(mockBookDAO);

        conn = ConnectionSingleton.getConnection();
        ConnectionSingleton.resetTestDatabase();
        realUserDAO = new UserDAO(conn);
        realBookDAO = new BookDAO(conn);
        realUserService = new UserService(realUserDAO, realBookDAO);
        realBookService = new BookService(realBookDAO);
    }

    /**
     * Tests that the userService allows us to create a user via the userDAO.
     */
    @Test
    public void createUserSuccessfulTestMocked() {
        User testUser = new User(29, "testerino");

        Mockito.when(mockUserDAO.createUser(testUser)).thenReturn(testUser);

        User createdTestUser = mockUserService.createUser(testUser);

        Assert.assertEquals(testUser, createdTestUser);
        Mockito.verify(mockUserDAO).createUser(testUser);
    }

    /**
     * Tests that the userService does not create a user if the input username is empty.
     */
    @Test
    public void createUserUnsuccessfulUsernameEmptyTestMocked() {
        User testUser = new User(729, "");
        User notCreatedUser = mockUserService.createUser(testUser);

        Assert.assertNull(notCreatedUser);

        Mockito.verify(mockUserDAO, Mockito.times(0)).createUser(Mockito.any());
    }

    /**
     * Tests that the userService does not create a user if the input username is all whitespace.
     */
    @Test
    public void createUserUnsuccessfulUsernameAllWhitespaceTestMocked() {
        User testUser = new User(927, "     ");
        User notCreatedUser = mockUserService.createUser(testUser);

        Assert.assertNull(notCreatedUser);

        Mockito.verify(mockUserDAO, Mockito.times(0)).createUser(Mockito.any());
    }

    /**
     * Tests that the userService does not create a user if the input username is already in use.
     */
    @Test
    public void createUserUnsuccessfulUsernameInUseTestUnmocked() {
        User testUser1 = new User(29, "testortle");
        User testUser2 = new User(30, "testortle");

        User createdUser = realUserService.createUser(testUser1);
        User notCreatedUser = realUserService.createUser(testUser2);

        Assert.assertEquals(testUser1, createdUser);
        Assert.assertNull(notCreatedUser);
    }

    /**
     * Tests that the userService interacts with the DAO to retrieve all users.
     */
    @Test
    public void getAllUsersTestMocked() {
        List<User> expectedUsers = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            User user = new User(i, "user" + i);
            expectedUsers.add(user);
        }
        Mockito.when(mockUserDAO.getAllUsers()).thenReturn(expectedUsers);
        List<User> actualUsers = mockUserService.getAllUsers();

        Assert.assertEquals(expectedUsers, actualUsers);
        Mockito.verify(mockUserDAO).getAllUsers();
    }

    /**
     * Tests that the userService interacts with the book DAO to determine that
     * a user has books signed out.
     */
    @Test
    public void testUserHasBooksSignedOutMocked() {
        User user = new User(432, "idohavebooks");
        List<Book> booksSignedOut = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            Book book = new Book(i, "author" + i, "title" + i, 432);
            booksSignedOut.add(book);
        }

        Mockito.when(mockBookDAO.queryBooksSignedOutByUser(432)).thenReturn(booksSignedOut);
        boolean hasBooks = mockUserService.hasBooksSignedOut(432);

        Assert.assertTrue(hasBooks);
        Mockito.verify(mockBookDAO).queryBooksSignedOutByUser(432);
    }

    /**
     * Tests that the userService interacts with the book DAO to determine that
     * a user does not have books signed out.
     */
    @Test
    public void testUserDoesNotHaveBooksSignedOutMocked() {
        User user = new User(678, "ihavenobooks");
        List<Book> booksSignedOut = new ArrayList<>();

        Mockito.when(mockBookDAO.queryBooksSignedOutByUser(678)).thenReturn(booksSignedOut);
        boolean hasBooks = mockUserService.hasBooksSignedOut(678);

        Assert.assertFalse(hasBooks);
        Mockito.verify(mockBookDAO).queryBooksSignedOutByUser(678);
    }

    /**
     * Tests that the userService interacts with the user DAO to determine that
     * a user exists based on the input username.
     */
    @Test
    public void testUserExistsByUsernameMocked() {
        User existingUser = new User(5432, "iexist!");

        Mockito.when(mockUserDAO.userExists(existingUser.getUsername())).thenReturn(true);
        boolean userFound = mockUserService.checkUser(existingUser.getUsername());

        Assert.assertTrue(userFound);
        Mockito.verify(mockUserDAO).userExists("iexist!");
    }

    /**
     * Tests that the userService interacts with the user DAO to determine that
     * a user exists based on the input username.
     */
    @Test
    public void testUserDoesNotExistByUsernameMocked() {
        User nonexistingUser = new User(2345, "idontexist");

        Mockito.when(mockUserDAO.userExists(nonexistingUser.getUsername())).thenReturn(false);
        boolean userFound = mockUserService.checkUser(nonexistingUser.getUsername());

        Assert.assertFalse(userFound);
        Mockito.verify(mockUserDAO).userExists("idontexist");
    }

    /**
     * Tests that the userService interacts with the user DAO to determine that
     * a user exists based on the input user id.
     */
    @Test
    public void testUserExistsByIdMocked() {
        User existingUser = new User(5432, "iexist!");

        Mockito.when(mockUserDAO.userExists(existingUser.getUserId())).thenReturn(true);
        boolean userFound = mockUserService.checkUser(existingUser.getUserId());

        Assert.assertTrue(userFound);
        Mockito.verify(mockUserDAO).userExists(5432);
    }

    /**
     * Tests that the userService interacts with the user DAO to determine that
     * a user exists based on the input username.
     */
    @Test
    public void testUserDoesNotExistByIdMocked() {
        User nonexistingUser = new User(2345, "idontexist");

        Mockito.when(mockUserDAO.userExists(nonexistingUser.getUserId())).thenReturn(false);
        boolean userFound = mockUserService.checkUser(nonexistingUser.getUserId());

        Assert.assertFalse(userFound);
        Mockito.verify(mockUserDAO).userExists(2345);
    }

    /**
     * Tests that a user successfully deletes their account.
     */
    @Test
    public void testDeleteUserSucceedsUnmocked() {
        User user = new User(543, "deleteMeTest");

        realUserService.createUser(user);

        User deleteAttempt = realUserService.deleteUser(user.getUserId(), user.getUserId());
        Boolean userStillExists = realUserService.checkUser(user.getUserId());

        Assert.assertEquals(user, deleteAttempt);
        Assert.assertFalse(userStillExists);
    }

    /**
     * Tests that a user fails to delete another user
     */
    @Test
    public void testDeleteUserFailsWhenCredentialsAreInvalidUnmocked() {
        User currentUser = new User(97, "currentTestUser");
        User otherUser = new User(79, "otherTestUser");

        realUserService.createUser(currentUser);
        realUserService.createUser(otherUser);

        User deleteAttempt = realUserService.deleteUser(currentUser.getUserId(), otherUser.getUserId());
        Boolean otherUserStillExists = realUserService.checkUser(otherUser.getUserId());

        Assert.assertNull(deleteAttempt);
        Assert.assertTrue(otherUserStillExists);
    }

    /**
     * Tests that the userService throws an exception when the user attempts to delete their account
     * when they have books signed out.
     * @throws Exceptions.UserHasBooksSignedOut
     */
    @Test
    public void testDeleteUserUnsuccessfulTestMocked() throws UserHasBooksSignedOut {
        Book testBook = new Book(75, "testAuthor", "testTitle");
        User testUser = new User(51, "stillGotBooks");

        mockUserService.createUser(testUser);
        mockBookService.addBook(testBook);
        Mockito.verify(mockUserDAO).createUser(testUser);
        Mockito.verify(mockBookDAO).insertBook(testBook);

        Mockito.doThrow(UserHasBooksSignedOut.class).when(mockBookDAO).queryBooksSignedOutByUser(testUser.getUserId());
        Assert.assertThrows(UserHasBooksSignedOut.class, () -> mockUserService.deleteUser(51, 51));
    }
}