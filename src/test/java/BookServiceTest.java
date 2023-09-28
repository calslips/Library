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
import org.mockito.Mockito;

import java.sql.Connection;
import java.util.List;

public class BookServiceTest {
    Connection conn;
    BookDAO mockBookDAO;
    BookService mockBookService;
    UserDAO realUserDAO;
    BookDAO realBookDAO;
    UserService realUserService;
    BookService realBookService;

    @Before
    public void setUp(){
        mockBookDAO = Mockito.mock(BookDAO.class);
        mockBookService = new BookService(mockBookDAO);

        conn = ConnectionSingleton.getConnection();
        ConnectionSingleton.resetTestDatabase();
        realUserDAO = new UserDAO(conn);
        realBookDAO = new BookDAO(conn);
        realUserService = new UserService(realUserDAO, realBookDAO);
        realBookService = new BookService(realBookDAO);
    }

    /**
     * The bookService SHOULD allow us to save a book via the bookDAO
     */
    @Test
    public void addBookSuccessfulTestMocked () {
        Book testBook = new Book(1, "testAuthor", "testBook", 0);

        mockBookService.addBook(testBook);

        Mockito.verify(mockBookDAO).insertBook(Mockito.any());
    }

    /**
     * The bookService should allow us to save a book and ignore title/author casing
     */
    @Test
    public void addBookIgnoreCaseTestUnmocked() {
        Book testBook = new Book("AutHoR TeSt CasE", "TitLe tEst cASe");
        realBookService.addBook(testBook);
        List<Book> allBooks = realBookService.getAllBooks();
        String expectedAuthor = "author test case";
        String expectedTitle = "title test case";
        int expectedLength = 1;
        Assert.assertEquals(expectedAuthor, allBooks.get(0).getAuthor());
        Assert.assertEquals(expectedTitle, allBooks.get(0).getTitle());
        Assert.assertEquals(expectedLength, allBooks.size());
    }

    /**
     * The bookService should allow us to remove the whitespace padding from title and/or author before saving a book.
     */
    @Test
    public void addBookRemoveWhitespacePaddingFromTitleAndAuthorUnmocked() {
        Book testBook = new Book("   padded author   ", "   padded title   ");
        realBookService.addBook(testBook);
        List<Book> allBooks = realBookService.getAllBooks();
        String expectedAuthor = "padded author";
        String expectedTitle = "padded title";
        int expectedLength = 1;
        Assert.assertEquals(expectedAuthor, allBooks.get(0).getAuthor());
        Assert.assertEquals(expectedTitle, allBooks.get(0).getTitle());
        Assert.assertEquals(expectedLength, allBooks.size());
    }

    /**
     * Tests that bookService prevents the addition of a new book if the title is not included.
     */
    @Test
    public void addBookUnsuccessfulWithoutTitleTestMocked () {
        Book testBook = new Book("Au Thor", "");
        Book notAdded = mockBookService.addBook(testBook);

        Assert.assertNull(notAdded);

        Mockito.verify(mockBookDAO, Mockito.times(0)).insertBook(Mockito.any());
    }

    /**
     * Tests that bookService prevents the addition of a new book if the title is all whitespace.
     */
    @Test
    public void addBookUnsuccessfulWithWhitespaceTitleTestMocked () {
        Book testBook = new Book("Au Thor", "     ");
        Book notAdded = mockBookService.addBook(testBook);

        Assert.assertNull(notAdded);

        Mockito.verify(mockBookDAO, Mockito.times(0)).insertBook(Mockito.any());
    }

    /**
     * Tests that bookService prevents the addition of a new book if the author is not included.
     */
    @Test
    public void addBookUnsuccessfulWithoutAuthorTestMocked () {
        Book testBook = new Book("", "No Author Useless Title");
        Book notAdded = mockBookService.addBook(testBook);

        Assert.assertNull(notAdded);

        Mockito.verify(mockBookDAO, Mockito.times(0)).insertBook(Mockito.any());
    }

    /**
     * Tests that bookService prevents the addition of a new book if the author is all whitespace.
     */
    @Test
    public void addBookUnsuccessfulWithWhitespaceAuthorTestMocked () {
        Book testBook = new Book("     ", "Space Author Useless Title");
        Book notAdded = mockBookService.addBook(testBook);

        Assert.assertNull(notAdded);

        Mockito.verify(mockBookDAO, Mockito.times(0)).insertBook(Mockito.any());
    }

    /**
     * Tests that bookService prevents the addition of a new book if title and author is not included.
     */
    @Test
    public void addBookUnsuccessfulWithoutTitleAndAuthorTestMocked () {
        Book testBook = new Book("", "");
        Book notAdded = mockBookService.addBook(testBook);

        Assert.assertNull(notAdded);

        Mockito.verify(mockBookDAO, Mockito.times(0)).insertBook(Mockito.any());
    }

    /**
     * Tests that bookService prevents the addition of a new book if title and author are all whitespace.
     */
    @Test
    public void addBookUnsuccessfulWithWhitespaceTitleAndAuthorTestMocked () {
        Book testBook = new Book("     ", "          ");
        Book notAdded = mockBookService.addBook(testBook);

        Assert.assertNull(notAdded);

        Mockito.verify(mockBookDAO, Mockito.times(0)).insertBook(Mockito.any());
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