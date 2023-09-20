package Service;

import DAO.UserDAO;

public class UserService {
    UserDAO userDAO;
    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }
//    public int getIdFromName(String name){
//        return authorDAO.getAuthorIdByName(name);
//    }

    public void createUser(String username) {
        userDAO.createUser(username);
    }

    public void deleteUser(String username) {
        userDAO.deleteUser(username);
    }
}
