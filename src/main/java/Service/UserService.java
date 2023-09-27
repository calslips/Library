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

    public boolean checkUser(int userId) {
        return userDAO.userExists(userId);
    }

    public List getAllUsers() {
        return userDAO.getAllUsers();
    }

}
