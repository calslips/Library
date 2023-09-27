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
     * the userService SHOULD allow us to create a user via the userDAO
     */
    @Test
    public void createUserSuccessfulTestMocked () {
        User testUser = new User(29, "testerino");

//        // createUser method has a void return
//        // utilize doAnswer to mock the method call but return null
//        Mockito.doAnswer(invocationOnMock -> {
//            Object arg0 = invocationOnMock.getArgument(0);
//            Assert.assertEquals(testUser, arg0);
//            return null;
//        }).when(mockUserDAO).createUser(Mockito.any(User.class));

        mockUserService.createUser(testUser);

        Mockito.verify(mockUserDAO).createUser(Mockito.any());
    }

    /**
     * the userService should NOT create a user if username is already in use
     */
    @Test
    public void createUserUnsuccessfulTestUnmocked () {
        User testUser1 = new User(29, "testortle");
        User testUser2 = new User(30, "testortle");

        User createdUser = realUserService.createUser(testUser1);
        User notCreatedUser = realUserService.createUser(testUser2);

        Assert.assertEquals(testUser1, createdUser);
        Assert.assertNull(notCreatedUser);
    }

    /**
     * the userService should throw an exception when the user attempts to delete their account
     * when they have books signed out
     * @throws Exceptions.UserHasBooksSignedOut
     */
    @Test
    public void signOutBookUnsuccessfulTestMocked() throws UserHasBooksSignedOut {
        Book testBook = new Book(75, "testAuthor", "testTitle");
        User testUser = new User(51, "stillGotBooks");

        mockUserDAO.createUser(testUser);
        mockBookDAO.insertBook(testBook);
        Mockito.verify(mockUserDAO).createUser(Mockito.any(User.class));
        Mockito.verify(mockBookDAO).insertBook(Mockito.any(Book.class));

        Mockito.doThrow(UserHasBooksSignedOut.class).when(mockBookDAO).queryBooksSignedOutByUser(testUser.getUserId());
        Assert.assertThrows(UserHasBooksSignedOut.class, () -> mockUserService.deleteUser(51));
    }
}