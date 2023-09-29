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
import java.util.ArrayList;
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

        Mockito.when(mockBookDAO.insertBook(testBook)).thenReturn(testBook);

        Book addedTestBook = mockBookService.addBook(testBook);

        Assert.assertEquals(testBook, addedTestBook);
        Mockito.verify(mockBookDAO).insertBook(testBook);
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
     * Tests that the bookService interacts with the DAO to retrieve all books.
     */
    @Test
    public void getAllBooksTestMocked() {
        List<Book> expectedBooks = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            Book book = new Book(i, "author" + i, "title" + i);
            expectedBooks.add(book);
        }
        Mockito.when(mockBookDAO.queryAllBooks()).thenReturn(expectedBooks);
        List<Book> actualBooks = mockBookService.getAllBooks();

        Assert.assertEquals(expectedBooks, actualBooks);
        Mockito.verify(mockBookDAO).queryAllBooks();
    }

    /**
     * Tests that the bookService interacts with the DAO to retrieve all books of the same author and title.
     */
    @Test
    public void getAllBooksByTitleAndAuthorTestMocked() {
        List<Book> allBooks = new ArrayList<>();
        List<Book> expectedBooks = new ArrayList<>();
        String expectedTitle = "title1";
        String expectedAuthor = "author1";

        for (int i = 1; i < 11; i++) {
            Book book = new Book(i, "author" + i % 2, "title" + i % 4);
            allBooks.add(book);
        }
        for (Book book : allBooks) {
            if (book.getTitle().equals(expectedTitle) && book.getAuthor().equals(expectedAuthor)) {
                expectedBooks.add(book);
            }
        }

        Mockito.when(mockBookDAO.queryBooksByTitleAndAuthor(expectedTitle, expectedAuthor)).thenReturn(expectedBooks);
        List<Book> actualBooks = mockBookService.getBooksByTitleAndAuthor("title1", "author1");

        Assert.assertEquals(3, actualBooks.size());
        Assert.assertEquals(expectedBooks, actualBooks);
        Mockito.verify(mockBookDAO).queryBooksByTitleAndAuthor("title1", "author1");
    }

    /**
     * Tests that the bookService interacts with the DAO to retrieve all books of the same author.
     */
    @Test
    public void getAllBooksByAuthorTestMocked() {
        List<Book> allBooks = new ArrayList<>();
        List<Book> expectedBooks = new ArrayList<>();
        String expectedAuthor = "author1";

        for (int i = 1; i < 11; i++) {
            Book book = new Book(i, "author" + i % 5, "title");
            allBooks.add(book);
        }
        for (Book book : allBooks) {
            if (book.getAuthor().equals(expectedAuthor)) {
                expectedBooks.add(book);
            }
        }

        Mockito.when(mockBookDAO.queryBooksByAuthor(expectedAuthor)).thenReturn(expectedBooks);
        List<Book> actualBooks = mockBookService.getBooksByAuthor("author1");

        Assert.assertEquals(2, actualBooks.size());
        Assert.assertEquals(expectedBooks, actualBooks);
        Mockito.verify(mockBookDAO).queryBooksByAuthor("author1");
    }

    /**
     * Tests that the bookService interacts with the DAO to retrieve all books of the same title.
     */
    @Test
    public void getAllBooksByTitleTestMocked() {
        List<Book> allBooks = new ArrayList<>();
        List<Book> expectedBooks = new ArrayList<>();
        String expectedTitle = "title1";

        for (int i = 1; i < 11; i++) {
            Book book = new Book(i, "author", "title"  + i % 5);
            allBooks.add(book);
        }
        for (Book book : allBooks) {
            if (book.getTitle().equals(expectedTitle)) {
                expectedBooks.add(book);
            }
        }

        Mockito.when(mockBookDAO.queryBooksByTitle(expectedTitle)).thenReturn(expectedBooks);
        List<Book> actualBooks = mockBookService.getBooksByTitle("title1");

        Assert.assertEquals(2, actualBooks.size());
        Assert.assertEquals(expectedBooks, actualBooks);
        Mockito.verify(mockBookDAO).queryBooksByTitle("title1");
    }

    /**
     * Tests that the bookService interacts with the DAO to retrieve a book by its id.
     */
    @Test
    public void getBookByIdMocked() {
        Book testBook = new Book(98657, "unknown", "the lost book");

        Mockito.when(mockBookDAO.queryBooksById(testBook.getBookId())).thenReturn(testBook);
        Book foundBook = mockBookService.getBookById(98657);

        Assert.assertEquals(testBook, foundBook);
        Mockito.verify(mockBookDAO).queryBooksById(98657);
    }

    /**
     * Tests that the bookService interacts with the DAO to retrieve a book by its id,
     * but returns null when there is no match.
     */
    @Test
    public void getBookByIdFailMocked() {
        Book testBook = new Book(43276, "off by 1 author", "off by 1 title");

        Mockito.when(mockBookDAO.queryBooksById(testBook.getBookId() + 1)).thenReturn(null);
        Book nonexistentBook = mockBookService.getBookById(43276 + 1);

        Assert.assertNull(nonexistentBook);
        Mockito.verify(mockBookDAO).queryBooksById(43276 + 1);
    }

    /**
     * the bookService should allow a user to sign out a book when it is available.
     */
    @Test
    public void signOutBookSuccessfulTestUnmocked() throws BookSignedOutException {
        Book testBook = new Book("testAuthor444", "testTitle444");
        User signedOutUser = new User("mybooknow");

        realBookService.addBook(testBook);
        realUserService.createUser(signedOutUser);
        Book signedOutBook = realBookService.updateBookSignedOutBy(testBook.getBookId(), signedOutUser.getUserId());

        Assert.assertEquals(signedOutUser.getUserId(), signedOutBook.getSignedOutBy());
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

    /**
     * the bookService should allow a user to return a book if they have it signed out.
     */
    @Test
    public void returnBookSuccessfulTestUnmocked() throws BookSignedOutException {
        Book testBook = new Book("testAuthorReturn", "testTitleReturn");
        User testUser = new User("borrowtoreturn");

        realBookService.addBook(testBook);
        realUserService.createUser(testUser);

        Book signedOutBook = realBookService.updateBookSignedOutBy(testBook.getBookId(), testUser.getUserId());
        Assert.assertEquals(testUser.getUserId(), signedOutBook.getSignedOutBy());

        Book returnedBook = realBookService.returnBook(signedOutBook);
        Assert.assertEquals(0, returnedBook.getSignedOutBy());
    }
}