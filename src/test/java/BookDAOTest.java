import DAO.BookDAO;
import DAO.UserDAO;
import Model.Book;
import Model.User;
import Service.BookService;
import Util.ConnectionSingleton;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

public class BookDAOTest {
    Connection conn;
    BookDAO bookDAO;
    BookService bookService;
    UserDAO userDAO;
    @Before
    public void setUp(){
        conn = ConnectionSingleton.getConnection();
        ConnectionSingleton.resetTestDatabase();
        bookDAO = new BookDAO(conn);
        bookService = new BookService(bookDAO);
        userDAO = new UserDAO(conn);
    }

    /**
     * Tests inserting a new book into the database
     */
    @Test
    public void testInsertBook() {
        Book testBook = new Book("test author 1", "test title 1");
        bookDAO.insertBook(testBook);
        List<Book> allBooks = bookDAO.queryAllBooks();
        String expectedAuthor = "test author 1";
        String expectedTitle = "test title 1";
        int expectedLength = 1;
        Assert.assertEquals(expectedAuthor, allBooks.get(0).getAuthor());
        Assert.assertEquals(expectedTitle, allBooks.get(0).getTitle());
        Assert.assertEquals(expectedLength, allBooks.size());
    }

    /**
     * Tests querying all books in the database
     */
    @Test
    public void testSearchAllBooksEmpty() {
        List<Book> allBooks = bookDAO.queryAllBooks();
        int expectedLength = 0;
        Assert.assertEquals(expectedLength, allBooks.size());
    }

    /**
     * Tests querying all books in the database
     */
    @Test
    public void testSearchAllBooksNotEmpty() {
        bookDAO.insertBook(new Book(1, "author1", "title1"));
        bookDAO.insertBook(new Book(2, "author2", "title2"));
        bookDAO.insertBook(new Book(3, "author3", "title3"));
        List<Book> allBooks = bookDAO.queryAllBooks();
        int expectedLength = 3;
        Assert.assertEquals(expectedLength, allBooks.size());
    }

    /**
     * Tests querying the database for books by title and author via bookDAO
     */
    @Test
    public void testSearchBookByTitleAndAuthor() {
        Book testBook = new Book("test author 2", "test title 2");
        bookDAO.insertBook(testBook);
        Book actualBook = bookDAO.queryBooksByTitleAndAuthor("test title 2", "test author 2").get(0);
        String expectedAuthor = "test author 2";
        String expectedTitle = "test title 2";
        Assert.assertEquals(expectedAuthor, actualBook.getAuthor());
        Assert.assertEquals(expectedTitle, actualBook.getTitle());
    }

    /**
     * Tests querying the database for books by author via bookDAO
     */
    @Test
    public void testSearchBookByAuthor() {
        Book testBook = new Book("test author 3", "test title 3");
        bookDAO.insertBook(testBook);
        Book actualBook = bookDAO.queryBooksByAuthor("test author 3").get(0);
        String expectedAuthor = "test author 3";
        Assert.assertEquals(expectedAuthor, actualBook.getAuthor());
    }

    /**
     * Tests querying the database for books by title via bookDAO
     */
    @Test
    public void testSearchBookByTitle() {
        Book testBook = new Book("test author 4", "test title 4");
        bookDAO.insertBook(testBook);
        Book actualBook = bookDAO.queryBooksByTitle("test title 4").get(0);
        String expectedTitle = "test title 4";
        Assert.assertEquals(expectedTitle, actualBook.getTitle());
    }

    /**
     * Tests querying the database for book by their id via bookDAO
     */
    @Test
    public void testSearchBookById() {
        Book expectedBook = bookDAO.insertBook(new Book(8675309, "test author 4.5", "test title 4.5"));
        Book actualBook = bookDAO.queryBooksById(expectedBook.getBookId());
        Assert.assertEquals(expectedBook, actualBook);
    }

    /**
     * Tests interaction with the bookDAO when user successfully signs out a book.
     */
    @Test
    public void testSignOutBook() {
        User user = new User(22, "testname");
        userDAO.createUser(user);

        Book testBook = new Book(43, "test author 5", "test title 5", 22);
        bookDAO.insertBook(testBook);
        bookDAO.updateSignedOutBy(testBook, testBook.getSignedOutBy());
        Book actualBook = bookDAO.queryBooksByAuthor("test author 5").get(0);
        int expected = 22;
        int actual = actualBook.getSignedOutBy();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Tests querying the database for books signed out by the current user
     */
    @Test
    public void testSearchBooksSignedOutByUser() {
        User user = new User(35, "testada");
        userDAO.createUser(user);

        Book b1 = bookDAO.insertBook(new Book(654, "test auth 213", "test title 213"));
        Book b2 = bookDAO.insertBook(new Book(645, "test auth 213", "test title 213"));
        Book b3 = bookDAO.insertBook(new Book(564, "test auth 543", "test title 543"));
        Book b4 = bookDAO.insertBook(new Book(546, "test auth 765", "test title 765"));
        Book b5 = bookDAO.insertBook(new Book(456, "test auth 987", "test title 987"));
        Book b6 = bookDAO.insertBook(new Book(465, "test auth 143", "test title 143"));

        bookDAO.updateSignedOutBy(b1, user.getUserId());
        bookDAO.updateSignedOutBy(b2, user.getUserId());
        bookDAO.updateSignedOutBy(b3, user.getUserId());

        List<Book> allBooks = bookDAO.queryAllBooks();
        List<Book> booksSignedOutByUser = bookDAO.queryBooksSignedOutByUser(user.getUserId());
        int expectedLengthAllBooks = 6;
        int expectedLengthSignedOutByUser = 3;

        Assert.assertEquals(expectedLengthAllBooks, allBooks.size());
        Assert.assertEquals(expectedLengthSignedOutByUser, booksSignedOutByUser.size());
    }

    /**
     * Tests interaction with the bookDAO when user successfully returns a book.
     */
    @Test
    public void testReturnBook() {
        User user = new User(25, "testuser");
        userDAO.createUser(user);

        Book testBook = new Book(45, "test author 6", "test title 6", 25);
        bookDAO.insertBook(testBook);
        bookDAO.updateReturnBook(testBook);
        Book actualBook = bookDAO.queryBooksByAuthor("test author 6").get(0);
        int expected = 0;
        int actual = actualBook.getSignedOutBy();
        Assert.assertEquals(expected, actual);
    }
}