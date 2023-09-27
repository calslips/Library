package Service;

import DAO.UserDAO;
import DAO.BookDAO;
import Exceptions.UserHasBooksSignedOut;
import Model.Book;
import Model.User;
import java.util.List;

public class UserService {
    UserDAO userDAO;
    BookDAO bookDAO;
    public UserService(UserDAO userDAO, BookDAO bookDAO){
        this.userDAO = userDAO;
        this.bookDAO = bookDAO;
    }
//    public int getIdFromName(String name){
//        return authorDAO.getAuthorIdByName(name);
//    }

    public User createUser(User user) {
        int id = 0;

        if (userDAO.userExists(user.getUsername())) {
            return null;
            // if user id is the default (0), replace it with a unique randomized id
        } else if (user.getUserId() == id) {
            do {
                id = (int) (Math.random() * Integer.MAX_VALUE);
            } while (userDAO.userExists(id));

            user.setUserId(id);
        }
        return userDAO.createUser(user);
    }

//    public void deleteUser(String username) {
//        userDAO.deleteUser(username);
//    }

    /**
     * method calls DAO to delete user from database IF they do not have any books signed out
     * otherwise throws exception
     * @param userId
     */
    public void deleteUser(int userId) throws UserHasBooksSignedOut {
        if (!hasBooksSignedOut(userId)) {
            userDAO.deleteUser(userId);
        } else {
            throw new UserHasBooksSignedOut();
        }
    }

    /**
     * method verifies whether user has books signed out
     * @param userId
     * @return boolean
     */
    public boolean hasBooksSignedOut(int userId) {
        List<Book> books = bookDAO.queryBooksSignedOutByUser(userId);
        return books.size() > 0;
    }

    public boolean checkUser(String username) {
        return userDAO.userExists(username);
    }

    public List getAllUsers() {
        return userDAO.getAllUsers();
    }

}
