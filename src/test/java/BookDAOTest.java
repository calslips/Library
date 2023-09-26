import DAO.BookDAO;
import DAO.UserDAO;
import Model.Book;
import Model.User;
import Util.ConnectionSingleton;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

public class BookDAOTest {
    Connection conn;
    BookDAO bookDAO;
    UserDAO userDAO;
    @Before
    public void setUp(){
        conn = ConnectionSingleton.getConnection();
        ConnectionSingleton.resetTestDatabase();
        bookDAO = new BookDAO(conn);
        userDAO = new UserDAO(conn);
    }

    @Test
    public void testSearchBookByAuthor() {
        Book testBook = new Book("test author 1", "test title 1");
        bookDAO.insertBook(testBook);
        Book actualBook = bookDAO.queryBooksByAuthor("test author 1").get(0);
        String expectedAuthor = "test author 1";
        Assert.assertEquals(expectedAuthor, actualBook.getAuthor());
    }

    @Test
    public void testSearchBookByTitle() {
        Book testBook = new Book("test author 2", "test title 2");
        bookDAO.insertBook(testBook);
        Book actualBook = bookDAO.queryBooksByTitle("test title 2").get(0);
        String expectedTitle = "test title 2";
        Assert.assertEquals(expectedTitle, actualBook.getTitle());
    }

    /**
     * Tests interaction with the bookDAO when user successfully signs out a book.
     */
    @Test
    public void testSignOutBook() {
        User user = new User(22, "testname");
        userDAO.createUser(user);

        Book testBook = new Book(43, "test author", "test title", 22);
        bookDAO.insertBook(testBook);
        bookDAO.updateSignedOutBy(testBook, testBook.getSignedOutBy());
        Book actualBook = bookDAO.queryBooksByAuthor("test author").get(0);
        int expected = 22;
        int actual = actualBook.getSignedOutBy();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Tests interaction with the bookDAO when user successfully returns a book.
     */
    @Test
    public void testReturnBook() {
        User user = new User(25, "testuser");
        userDAO.createUser(user);

        Book testBook = new Book(45, "test author", "test title", 25);
        bookDAO.insertBook(testBook);
        bookDAO.updateReturnBook(testBook);
        Book actualBook = bookDAO.queryBooksByAuthor("test author").get(0);
        int expected = 0;
        int actual = actualBook.getSignedOutBy();
        Assert.assertEquals(expected, actual);
    }
}