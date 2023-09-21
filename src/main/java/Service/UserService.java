package Service;

import DAO.UserDAO;
import Model.User;
import java.util.List;

public class UserService {
    UserDAO userDAO;
    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }
//    public int getIdFromName(String name){
//        return authorDAO.getAuthorIdByName(name);
//    }

    public void createUser(User user) {
        userDAO.createUser(user);
    }

    public void deleteUser(String username) {
        userDAO.deleteUser(username);
    }

    public boolean checkUser(String username) {
        return userDAO.userExists(username);
    }

    public List getAllUsers() {
        return userDAO.getAllUsers();
    }
}
