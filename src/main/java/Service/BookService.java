package Service;

import DAO.BookDAO;
import Exceptions.BookSignedOutException;
import Model.Book;

import java.util.List;

/**
 * Service used for CRUD operations on paintings
 * Create Read Update Delete
 * (ie, an application that doesn't require any complicated programming logic - just a path from user input to data layer)
 */
public class BookService {
    private BookDAO bookDAO;
    private UserService userService;

    public BookService(BookDAO bookDAO){
        this.bookDAO = bookDAO;
        this.userService = userService;
    }

    /**
     * save the book (duplicates are not an issue).
     * @param b
     */
    public void addBook(Book b) {
        int id = 0;

        // if book id is the default (0), replace it with a unique randomized id
        if (b.getBookId() == id) {
            do {
                id = (int) (Math.random() * Integer.MAX_VALUE);
            } while (bookDAO.queryBooksById(id) != null);

            b.setBookId(id);
        }
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

    public List<Book> getBooksByTitleAndAuthor(String title, String author){
        List<Book> bookList = bookDAO.queryBooksByTitleAndAuthor(title, author);
        return bookList;
    }

    public List<Book> getBooksByAuthor(String author){
        List<Book> bookList = bookDAO.queryBooksByAuthor(author);
        return bookList;
    }

    public List<Book> getBooksByTitle(String title){
        List<Book> bookList = bookDAO.queryBooksByTitle(title);
        return bookList;
    }

    public void signOutBook(int bookId, int userId) throws BookSignedOutException{
        if (isSignedOut(bookId)) {
            throw new BookSignedOutException();
        } else {
            bookDAO.updateSignedOutBy(bookId, userId);
        }
    }

    public boolean isSignedOut(int bookId) {
        Book b = bookDAO.queryBooksById(bookId);
        return b.getSignedOutBy() != 0;
    }

    public void returnBook(int bookId) {
        bookDAO.updateReturnBook(bookId);
    }

    public List<Book> getAllBooks(){
        List<Book> bookList = bookDAO.queryAllBooks();
        return bookList;
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
