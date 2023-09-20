package Service;

import DAO.AuthorDAO;

public class AuthorService {
    AuthorDAO authorDAO;
    public AuthorService(AuthorDAO authorDAO){
        this.authorDAO = authorDAO;
    }
    public int getIdFromName(String name){
        return authorDAO.getAuthorIdByName(name);
    }
}
