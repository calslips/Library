package DAO;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorDAO {
    Connection conn;
    public AuthorDAO(Connection conn){
        this.conn = conn;
    }
    /**
     * method that retrieves an author's id based off of their name. If no author is found, return 0.
     */
    public int getAuthorIdByName(String name){
        try{
            PreparedStatement ps = conn.prepareStatement("select author_id from author where author.name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int id = rs.getInt("author_id");
                return id;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
}
