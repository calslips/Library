package DAO;

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
     * Method creates a new user record in the database based off of the input user object.
     * @param user
     */
    public User createUser(User user) {
        try {
            PreparedStatement ps = conn.prepareStatement("insert into users (userId, username) values (?, ?)");
            ps.setInt(1, user.getUserId());
            ps.setString(2, user.getUsername());
            if (ps.executeUpdate() == 1) {
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method retrieves all users from the database.
     * If there are no users, the returned list will be empty.
     * @return list of users
     */
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

    /**
     * Method retrieves a specified user from the database by their unique id.
     * @param userId
     * @return user or null
     */
    public User getUserById(int userId) {
        try {
            PreparedStatement ps = conn.prepareStatement("select * from users where userId = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("userId"), rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method verifies whether a user exists within the database based on the input username.
     * @param username
     * @return boolean
     */
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

    /**
     * Method verifies whether a user exists within the database based on the input user id.
     * @param userId
     * @return boolean
     */
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

    /**
     * Method deletes specific user record from the database based off the input user's id.
     * @param user
     * @return user or null
     */
    public User deleteUser(User user) {
        try {
            PreparedStatement ps = conn.prepareStatement("delete from users where userId = ?");
            ps.setInt(1, user.getUserId());
            if (ps.executeUpdate() == 1) {
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
