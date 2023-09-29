package Service;

import DAO.BookDAO;
import Exceptions.BookSignedOutException;
import Model.Book;

import java.util.List;

/**
 * Service used for CRUD operations on books
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
     * Saves the book to the database (duplicates are not an issue).
     * Returns null if either the book title or author are empty, as they are invalid inputs.
     * Generates randomized unique id for the book.
     * Title and author are 'sanitized' by removing leading and trailing white space, as well as lower-casing inputs.
     * @param book
     * @return book or null
     */
    public Book addBook(Book book) {
        if (book.getTitle().isBlank() || book.getAuthor().isBlank()) {
            return null;
        }

        int id = 0;

        // if book id is the default (0), replace it with a unique randomized id
        if (book.getBookId() == id) {
            do {
                id = (int) (Math.random() * Integer.MAX_VALUE);
            } while (bookDAO.queryBooksById(id) != null);

            book.setBookId(id);
        }

        book.setTitle(book.getTitle().trim().toLowerCase());
        book.setAuthor(book.getAuthor().trim().toLowerCase());

        return bookDAO.insertBook(book);
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

    /**
     * Intermediary method determining how to update book's signedOutBy property.
     * When user has already signed out book, will call method to return book.
     * When book is not currently signed out, will call method to sign book out.
     * When book is currently signed out by another user, will throw exception.
     * @param bookId
     * @param userId
     */
    public Book updateBookSignedOutBy(int bookId, int userId) throws BookSignedOutException {
        Book bookToUpdate = bookDAO.queryBooksById(bookId);

        if (bookToUpdate.getSignedOutBy() == userId) {
            return returnBook(bookToUpdate);
        } else if (bookToUpdate.getSignedOutBy() == 0) {
            return signOutBook(bookToUpdate, userId);
        } else {
            throw new BookSignedOutException();
        }
    }

    /**
     * Makes call to bookDAO to update book's signedOutBy property to current user
     * @param book
     * @param userId
     */
    public Book signOutBook(Book book, int userId) {
        return bookDAO.updateSignedOutBy(book, userId);
    }

    /**
     * Makes call to bookDAO to update book's signedOutBy property to null (no user)
     * @param book
     */
    public Book returnBook(Book book) {
        return bookDAO.updateReturnBook(book);
    }

    public List<Book> getAllBooks(){
        List<Book> bookList = bookDAO.queryAllBooks();
        return bookList;
    }

    /**
     * Makes call to bookDAO to retrieve a single book by its id if it exists, null otherwise.
     * @param bookId
     * @return book or null
     */
    public Book getBookById(int bookId){
        Book book = bookDAO.queryBooksById(bookId);
        return book;
    }
}
