import DAO.BookDAO;
import DAO.UserDAO;
import Exceptions.BookSignedOutException;
import Model.Book;
import Model.User;
import Service.BookService;
import Service.UserService;
import Util.ConnectionSingleton;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.sql.Connection;

public class BookServiceTest {
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
        mockBookDAO = Mockito.mock(BookDAO.class);
        mockBookService = new BookService(mockBookDAO);

//        mockUserDAO = Mockito.mock(UserDAO.class);
//        mockUserService = Mockito.mock(UserService.class);

        conn = ConnectionSingleton.getConnection();
        ConnectionSingleton.resetTestDatabase();
        realUserDAO = new UserDAO(conn);
        realBookDAO = new BookDAO(conn);
        realUserService = new UserService(realUserDAO, realBookDAO);
        realBookService = new BookService(realBookDAO);
    }

    /**
     * the bookService SHOULD allow us to save a book via the bookDAO
     */
    @Test
    public void addBookSuccesfulTestMocked () {
        Book testBook = new Book(1, "testAuthor", "testBook", 0);

//        // insertBook method has a void return
//        // utilize doAnswer to mock the method call but return null
//        Mockito.doAnswer(invocationOnMock -> {
//            Object arg0 = invocationOnMock.getArgument(0);
//            Assert.assertEquals(testBook, arg0);
//            return null;
//        }).when(mockBookDAO).insertBook(Mockito.any(Book.class));

        mockBookService.addBook(testBook);

        Mockito.verify(mockBookDAO).insertBook(Mockito.any());
    }

    /**
     * the bookService should throw a BookSignedOutException when the user attempts
     * to sign out a book that another user has already signed out
     */
    @Test
    public void signOutBookUnsuccessfulTestUnmocked() throws BookSignedOutException {
        Book testBook = new Book("testAuthor", "testTitle");
        User signedOutUser = new User("signedOut");
        User notSignedOutUser = new User("notSignedOut");

        realBookService.addBook(testBook);
        realUserService.createUser(signedOutUser);
        realUserService.createUser(notSignedOutUser);
        realBookService.updateBookSignedOutBy(testBook.getBookId(), signedOutUser.getUserId());

        Assert.assertThrows(BookSignedOutException.class, () -> {
            realBookService.updateBookSignedOutBy(testBook.getBookId(), notSignedOutUser.getUserId());
        });
    }
}