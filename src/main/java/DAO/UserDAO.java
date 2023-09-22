package DAO;

import Model.Book;
import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            PreparedStatement ps = conn.prepareStatement("insert into users (userId, username) values (?, ?)");
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
            PreparedStatement ps = conn.prepareStatement("delete from users where username = ?");
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int userId) {
        try {
            PreparedStatement ps = conn.prepareStatement("delete from users where userId = ?");
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Boolean userExists(String username) {
        try {
            PreparedStatement ps = conn.prepareStatement("select * from users where username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean userExists(int userId) {
        try {
            PreparedStatement ps = conn.prepareStatement("select * from users where userId = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List getAllUsers() {
        List<User> userList = new ArrayList<>();
        try{
            PreparedStatement ps = conn.prepareStatement("select * from users");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int dbUserId = rs.getInt("userId");
                String dbUsername = rs.getString("username");

                User dbUser = new User(dbUserId, dbUsername);
                userList.add(dbUser);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return userList;
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
