package DAO;

import Model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object
 * meaning : a style of object intended to contain methods that interact with a database, and, it manages the
 * conversion from database records to/from java objects
 *
 * A DAO should not contain programming logic that isn't database-related. The methods also shouldn't make more than
 * one database statement/query. That should be left to service classes.
 */
public class BookDAO {

    private Connection conn;

    public BookDAO(Connection conn){
        this.conn = conn;
    }

    /**
     * method that uses JDBC to insert a book into the database
     * @param book
     */
    public Book insertBook(Book book){
        try{
            PreparedStatement ps = conn.prepareStatement("insert into books (bookId, author, title, signedOutBy) values (?, ?, ?, null)");
            ps.setInt(1, book.getBookId());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getTitle());
            if (ps.executeUpdate() > 0) {
                return book;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * method that uses JDBC to parse the resultset of a query that selects books with a matching title and author
     * into a java arraylist, and returns it
     * @param author
     * @return
     */
    public List<Book> queryBooksByTitleAndAuthor(String title, String author){
        List<Book> bookList = new ArrayList<>();
        try{
            PreparedStatement ps = conn.prepareStatement("select * from books where title = ? and author = ?");
            ps.setString(1, title);
            ps.setString(2, author);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int dbBookId = rs.getInt("bookId");
                String dbAuthor = rs.getString("author");
                String dbTitle = rs.getString("title");
                int dbSignedOutBy = rs.getInt("signedOutBy");

                Book dbBook = new Book(dbBookId, dbAuthor, dbTitle, dbSignedOutBy);
                bookList.add(dbBook);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return bookList;
    }

    /**
     * method that uses JDBC to parse the resultset of a query that selects books with a matching author
     * into a java arraylist, and returns it
     * @param author
     * @return
     */
    public List<Book> queryBooksByAuthor(String author){
        List<Book> bookList = new ArrayList<>();
        try{
            PreparedStatement ps = conn.prepareStatement("select * from books where author = ?");
            ps.setString(1, author);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int dbBookId = rs.getInt("bookId");
                String dbAuthor = rs.getString("author");
                String dbTitle = rs.getString("title");
                int dbSignedOutBy = rs.getInt("signedOutBy");

                Book dbBook = new Book(dbBookId, dbAuthor, dbTitle, dbSignedOutBy);
                bookList.add(dbBook);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return bookList;
    }

    /**
     * method that uses JDBC to parse the resultset of a query that selects books with a matching title
     * into a java arraylist, and returns it
     * @param title
     * @return
     */
    public List<Book> queryBooksByTitle(String title){
        List<Book> bookList = new ArrayList<>();
        try{
            PreparedStatement ps = conn.prepareStatement("select * from books where title = ?");
            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int dbBookId = rs.getInt("bookId");
                String dbAuthor = rs.getString("author");
                String dbTitle = rs.getString("title");
                int dbSignedOutBy = rs.getInt("signedOutBy");

                Book dbBook = new Book(dbBookId, dbAuthor, dbTitle, dbSignedOutBy);
                bookList.add(dbBook);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return bookList;
    }

    public List<Book> queryBooksSignedOutByUser(int userId) {
        List<Book> bookList = new ArrayList<>();
        try{
            PreparedStatement ps = conn.prepareStatement("select * from books where signedOutBy = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int dbBookId = rs.getInt("bookId");
                String dbAuthor = rs.getString("author");
                String dbTitle = rs.getString("title");
                int dbSignedOutBy = rs.getInt("signedOutBy");

                Book dbBook = new Book(dbBookId, dbAuthor, dbTitle, dbSignedOutBy);
                bookList.add(dbBook);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return bookList;
    }

    /**
     * Executes SQL statement to update book's signedOutBy property in the database to current user.
     * When update is successful, returns updated book.
     * Otherwise, returns null.
     * @param book
     * @param userId
     * @return book or null
     */
    public Book updateSignedOutBy(Book book, int userId){
        try{
            PreparedStatement ps = conn.prepareStatement("update books set signedOutBy = ? where bookId = ?");
            ps.setInt(1, userId);
            ps.setInt(2, book.getBookId());
            if (ps.executeUpdate() > 0) {
                book.setSignedOutBy(userId);
                return book;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Executes SQL statement to update book's signedOutBy property in the database to null (no user).
     * When update is successful, returns updated book.
     * Otherwise, returns null.
     * @param book
     * @return book or null
     */
    public Book updateReturnBook(Book book){
        try{
            PreparedStatement ps = conn.prepareStatement("update books set signedOutBy = null where bookId = ?");
            ps.setInt(1, book.getBookId());
            if (ps.executeUpdate() > 1) {
                book.setSignedOutBy(0);
                return book;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * method that uses JDBC to parse the resultset of a query that selects all books
     * into a java arraylist, and returns it
     * @param
     * @return
     */
    public List<Book> queryAllBooks(){
        List<Book> bookList = new ArrayList<>();
        try{
            PreparedStatement ps = conn.prepareStatement("select * from books");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int dbBookId = rs.getInt("bookId");
                String dbAuthor = rs.getString("author");
                String dbTitle = rs.getString("title");
                int dbSignedOutBy = rs.getInt("signedOutBy");

                Book dbBook = new Book(dbBookId, dbAuthor, dbTitle, dbSignedOutBy);
                bookList.add(dbBook);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return bookList;
    }

    public Book queryBooksById(int bookId){
        try{
            PreparedStatement ps = conn.prepareStatement("select * from books where bookId = ?");
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int dbBookId = rs.getInt("bookId");
                String dbAuthor = rs.getString("author");
                String dbTitle = rs.getString("title");
                int dbSignedOutBy = rs.getInt("signedOutBy");

                Book dbBook = new Book(dbBookId, dbAuthor, dbTitle, dbSignedOutBy);
                return dbBook;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
