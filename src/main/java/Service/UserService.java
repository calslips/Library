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
        int id = 0;

        do{
            id = (int) (Math.random() * Integer.MAX_VALUE);
        }while(userDAO.userExists(id));

        user.setUserId(id);
        userDAO.createUser(user);
    }

    public void deleteUser(String username) {
        userDAO.deleteUser(username);
    }

    public void deleteUser(int userId) {
        userDAO.deleteUser(userId);
    }

    public boolean checkUser(String username) {
        return userDAO.userExists(username);
    }

    public List getAllUsers() {
        return userDAO.getAllUsers();
    }

}
