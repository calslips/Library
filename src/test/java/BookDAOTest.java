import DAO.BookDAO;
import DAO.UserDAO;
import Model.Book;
import Model.User;
import Util.ConnectionSingleton;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

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

    @Test
    public void testSignOutBook() {
        User user = new User(22, "testname");
        userDAO.createUser(user);

        Book testBook = new Book(43, "test author", "test title", 22);
        bookDAO.insertBook(testBook);
        bookDAO.updateSignedOutBy(testBook.getTitle(), testBook.getAuthor(), testBook.getSignedOutBy(), testBook.getBookId());
        Book actualBook = bookDAO.queryBooksByAuthor("test author").get(0);
        int expected = 22;
        int actual = actualBook.getSignedOutBy();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReturnBook() {
        User user = new User(25, "testuser");
        userDAO.createUser(user);

        Book testBook = new Book(45, "test author", "test title", 25);
        bookDAO.insertBook(testBook);
        bookDAO.updateReturnBook(testBook.getTitle(), testBook.getAuthor(), testBook.getBookId());
        Book actualBook = bookDAO.queryBooksByAuthor("test author").get(0);
        int expected = 0;
        int actual = actualBook.getSignedOutBy();
        Assert.assertEquals(expected, actual);
    }



    /**
     * test that all painting that are retrieved are from the expected year.
     */
//    @Test
//    public void testGetByYear(){

//        List<Painting> actual = paintingDAO.queryPaintingByYear(1880);
//        int expectedYear = 1880;
//        for(int i = 0 ; i < actual.size() ; i++){
//            Assert.assertEquals(expectedYear, actual.get(i).getYearMade());
//        }

//    }

    /**
     * test that no paintings that are retrieved are from an unexpected year.
     */
//    @Test
//    public void testGetByYearNotRetrieveUnexpectedYear(){

//        List<Painting> actual = paintingDAO.queryPaintingByYear(1880);
//        int expectedYear = 1890;
//        for(int i = 0 ; i < actual.size() ; i++){
//            Assert.assertNotEquals(expectedYear, actual.get(i).getYearMade());
//        }
//    }
}