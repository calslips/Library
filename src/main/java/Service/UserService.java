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

    /**
     * Saves the user to the database.
     * Returns null if username is empty, as it is an invalid input.
     * Generates randomized unique id for the user.
     * Lower-cases username prior to saving user.
     * @param user
     * @return user or null
     */
    public User createUser(User user) {
        if (user.getUsername().isBlank()) {
            return null;
        }

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

        user.setUsername(user.getUsername().toLowerCase());

        return userDAO.createUser(user);
    }

    /**
     * Method calls DAO to retrieve a list of all users in the database.
     */
    public List getAllUsers() {
        return userDAO.getAllUsers();
    }

    /**
     * Method verifies whether user has books signed out.
     * @param userId
     * @return boolean
     */
    public boolean hasBooksSignedOut(int userId) {
        List<Book> books = bookDAO.queryBooksSignedOutByUser(userId);
        return books.size() > 0;
    }

    /**
     * Method verifies whether user is in the database based on the input username
     * @param username
     * @return boolean
     */
    public boolean checkUser(String username) {
        return userDAO.userExists(username);
    }

    /**
     * Method verifies whether user is in the database based on the input user id
     * @param userId
     * @return boolean
     */
    public boolean checkUser(int userId) {
        return userDAO.userExists(userId);
    }

    /**
     * Method calls DAO to delete user from database.
     * If current user credentials match user to delete, delete user from db, IF they do not have any books signed out.
     * Otherwise, throws exception if the user has books signed out.
     * If current user credentials do not match user to delete, perform no action and return null.
     * @param currentUserId
     * @param userToDeleteId
     * @return user or null
     */
    public User deleteUser(int currentUserId, int userToDeleteId) throws UserHasBooksSignedOut {
        if (currentUserId == userToDeleteId) {
            User userToDelete = userDAO.getUserById(userToDeleteId);
            if (!hasBooksSignedOut(userToDeleteId)) {
                return userDAO.deleteUser(userToDelete);
            } else {
                throw new UserHasBooksSignedOut();
            }
        } else {
            return null;
        }
    }
}
