package Service;

import DAO.UserDAO;
import Model.User;

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
}
