package DAO;

import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    Connection conn;
    public UserDAO(Connection conn){
        this.conn = conn;
    }

    /**
     * method creates a new user record in the database based off of user object
     * @param user
     */
    public void createUser(User user) {
        try {
            PreparedStatement ps = conn.prepareStatement("insert into user (userId, username) values (?, ?)");
            ps.setInt(1, user.getUserId());
            ps.setString(2, user.getUsername());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * method deletes user from database using their unique username
     * @param username
     */
    public void deleteUser(String username) {
        try {
            PreparedStatement ps = conn.prepareStatement("delete from user where username = ?");
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * method that retrieves an author's id based off of their name. If no author is found, return 0.
//     */
//    public int getAuthorIdByName(String name){
//        try{
//            PreparedStatement ps = conn.prepareStatement("select author_id from author where author.name = ?");
//            ps.setString(1, name);
//            ResultSet rs = ps.executeQuery();
//            if(rs.next()){
//                int id = rs.getInt("author_id");
//                return id;
//            }
//        }catch(SQLException e){
//            e.printStackTrace();
//        }
//        return 0;
//    }
}
