package Service;

import DAO.BookDAO;
//import Exceptions.PaintingAlreadyExistsException;
import Model.Book;

import java.util.List;

/**
 * Service used for CRUD operations on paintings
 * Create Read Update Delete
 * (ie, an application that doesn't require any complicated programming logic - just a path from user input to data layer)
 */
public class BookService {
    private BookDAO BookDAO;
    private UserService userService;

    public BookService(BookDAO bookDAO, UserService userService){
        this.bookDAO = bookDAO;
        this.userService = userService;
    }

    /**
     * save the book (duplicates are not an issue).
     * @param b
     */
    public void addBook(Book b) {
        bookDAO.insertBook(b);

//        int authorId = authorService.getIdFromName(name);
//        i need to check if the painting already exists if i try to save it.
//        Book dbBook = paintingDAO.queryPaintingsByTitleAndAuthor(p.getTitle(), authorId);
//        if it's null, i assume it doesn't exist, and i proceed with the insert.
//        if (dbBook == null) {
////            set the fkey that we just found by name for inserting the painting
//            p.setAuthorFkey(authorId);
//            paintingDAO.insertPainting(p);
//        }else{
////            if it does exist, throw an exception to the user input layer so it may inform the user.
//            throw new PaintingAlreadyExistsException();
//        }
    }

    public List<Book> getBooksByAuthor(String author){
        List<Book> bookList = bookDAO.queryBooksByAuthor(author);
        return bookList;
    }

    public List<Book> getBooksByTitle(String title){
        List<Book> bookList = bookDAO.queryBooksByTitle(title);
        return bookList;
    }

    public void signOutBook(String title, String author, int userId) {
        bookDAO.updateSignedOutBy(title, author, userId);
    }

    public void returnBook(String title, String author, int userId) {
        bookDAO.updateReturnBook(title, author, userId);
    }

//    public void updatePainting(Book p){
//        paintingDAO.updatePainting(p);
//    }
//
//    public void deletePainting(String title){
//        paintingDAO.deletePainting(title);
//    }
//
//    public List<Book> getPaintingsFromYear(int year){
//        return paintingDAO.queryPaintingByYear(year);
//    }
//
//    public List<Book> getPaintingsBeforeYear(int year){
//        return paintingDAO.queryPaintingBeforeYear(year);
//    }

}
