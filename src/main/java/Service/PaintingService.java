package Service;

import DAO.PaintingDAO;
import Exceptions.PaintingAlreadyExistsException;
import Model.Book;

import java.util.List;

/**
 * Service used for CRUD operations on paintings
 * Create Read Update Delete
 * (ie, an application that doesn't require any complicated programming logic - just a path from user input to data layer)
 */
public class PaintingService {
    private PaintingDAO paintingDAO;
    private AuthorService authorService;

    public PaintingService(PaintingDAO paintingDAO, AuthorService authorService){
        this.paintingDAO = paintingDAO;
        this.authorService = authorService;
    }

    /**
     * check if painting match already exists in the database, if it does, throw an exception.
     * otherwise, save the painting.
     * @param p
     */
    public void savePainting(Book p, String name) throws PaintingAlreadyExistsException {
        int authorId = authorService.getIdFromName(name);
//        i need to check if the painting already exists if i try to save it.
        Book dbBook = paintingDAO.queryPaintingsByTitleAndAuthor(p.getTitle(), authorId);
//        if it's null, i assume it doesn't exist, and i proceed with the insert.
        if (dbBook == null) {
//            set the fkey that we just found by name for inserting the painting
            p.setAuthorFkey(authorId);
            paintingDAO.insertPainting(p);
        }else{
//            if it does exist, throw an exception to the user input layer so it may inform the user.
            throw new PaintingAlreadyExistsException();
        }
    }

    public List<Book> getPaintingsByAuthor(String author){
        List<Book> bookList = paintingDAO.queryPaintingsByAuthor(author);
        return bookList;
    }

    public void updatePainting(Book p){
        paintingDAO.updatePainting(p);
    }

    public void deletePainting(String title){
        paintingDAO.deletePainting(title);
    }

    public List<Book> getPaintingsFromYear(int year){
        return paintingDAO.queryPaintingByYear(year);
    }

    public List<Book> getPaintingsBeforeYear(int year){
        return paintingDAO.queryPaintingBeforeYear(year);
    }

}
