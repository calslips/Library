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
     * method that uses JDBC to send a insert statement to my database
     * @param b
     */
    public void insertBook(Book b){
        try{
//            using preparedstatement's ? ? syntax prevents SQL injection
            PreparedStatement ps = conn.prepareStatement("insert into books (bookId, author, title, signedOutBy) values (?, ?, ?, null)");
            ps.setInt(1, b.getBookId());
            ps.setString(2, b.getAuthor());
            ps.setString(3, b.getTitle());
//            ps.setNull(4, null);
            ps.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
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

    public void updateSignedOutBy(int bookId, int userId){
        try{
            PreparedStatement ps = conn.prepareStatement("update books set signedOutBy = ? where bookId = ?");
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void updateReturnBook(int bookId){
        try{
            PreparedStatement ps = conn.prepareStatement("update books set signedOutBy = null where bookId = ?");
//            ps.setInt(1, userId);
//            ps.setString(1, title);
//            ps.setString(2, author);
            ps.setInt(1, bookId);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
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

//    public void updatePainting(Book p){
//        try{
//            PreparedStatement ps = conn.prepareStatement("update painting set author = ? where title = ?");
////            ps.setString(1, p.getAuthor());
//            ps.setString(2, p.getTitle());
//            ps.executeUpdate();
//        }catch(SQLException e){
//            e.printStackTrace();
//        }
//    }

//    /**
//     * return a painting based off of a match in title and author, if no such match occurs in the database,
//     * return null.
//     * @param
//     * @return
//     */
//    public Book queryPaintingsByTitleAndAuthor(String title, int author){
//        try{
//            PreparedStatement ps = conn.prepareStatement("select * from painting where title = ? and made_by = ?");
//            ps.setString(1, title);
//            ps.setInt(2, author);
//            ResultSet rs = ps.executeQuery();
//            if(rs.next()){
//                String dbTitle = rs.getString("title");
//                String dbAuthor = rs.getString("author");
//                Book dbBook = new Book();
//                return dbBook;
//            }
//        }catch(SQLException e){
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void deletePainting(String title){
//        try{
//            PreparedStatement ps = conn.prepareStatement("delete painting where title = ?");
//            ps.setString(1, title);
//            ps.executeUpdate();
//        }catch(SQLException e){
//            e.printStackTrace();
//        }
//    }
//
//    public List<Book> queryPaintingByYear(int year) {
//        List<Book> bookList = new ArrayList<>();
//        try{
//            PreparedStatement ps = conn.prepareStatement("select * from painting where year_made = ? order by year_made asc");
//            ps.setInt(1, year);
//            ResultSet rs = ps.executeQuery();
//            while(rs.next()){
//                Book dbBook = new Book(rs.getInt("painting_id"), rs.getString("title"),
//                        rs.getInt("made_by"), rs.getInt("year_made"));
//                bookList.add(dbBook);
//            }
//        }catch(SQLException e){
//            e.printStackTrace();
//        }
//        return bookList;
//    }
//    public List<Book> queryPaintingBeforeYear(int year) {
//        List<Book> bookList = new ArrayList<>();
//        try{
//            PreparedStatement ps = conn.prepareStatement("select * from painting where year_made < ?");
//            ps.setInt(1, year);
//            ResultSet rs = ps.executeQuery();
//            while(rs.next()){
//                Book dbBook = new Book(rs.getInt("painting_id"), rs.getString("title"),
//                        rs.getInt("made_by"), rs.getInt("year_made"));
//                bookList.add(dbBook);
//            }
//        }catch(SQLException e){
//            e.printStackTrace();
//        }
//        return bookList;
//    }
}
