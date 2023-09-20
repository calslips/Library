package DAO;

import Model.Painting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object
 * meaning : a style of object intended to contain methods that interact with a database, and, it manages the
 * conversion from database records to/from java objects
 *
 * A DAO should not contain programming logic that isn't database-related. The methods also shouldn't make more than
 * one database statement/query. That should be left to service classes.
 */
public class PaintingDAO {

    private Connection conn;

    public PaintingDAO(Connection conn){
        this.conn = conn;
    }

    /**
     * method that uses JDBC to send a insert statement to my database
     * @param p
     */
    public void insertPainting(Painting p){
        try{
//            using preparedstatement's ? ? syntax prevents SQL injection
            PreparedStatement ps = conn.prepareStatement("insert into painting (painting_id, title, year_made, made_by) values (?, ?, ?, ?)");
            ps.setInt(1, p.getPaintingId());
            ps.setString(2, p.getTitle());
            ps.setInt(3, p.getYearMade());
            ps.setInt(4, p.getAuthorFkey());
            ps.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * method that uses JDBC to parse the resultset of a query that selects paintings with a matching author
     * into a java arraylist, and returns it
     * @param author
     * @return
     */
    public List<Painting> queryPaintingsByAuthor(String author){
        List<Painting> paintingList = new ArrayList<>();
        try{
            PreparedStatement ps = conn.prepareStatement("select * from painting inner join author on painting.made_by = author.author_id where name = ?");
            ps.setString(1, author);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int dbAuthor = rs.getInt("author_id");
                String dbTitle = rs.getString("title");
                int yearMade = rs.getInt("year_made");
                int paintingId = rs.getInt("painting_id");
                Painting dbPainting = new Painting(paintingId, dbTitle, dbAuthor, yearMade);
                paintingList.add(dbPainting);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return paintingList;
    }

    /**
     * return a painting based off of a match in title and author, if no such match occurs in the database,
     * return null.
     * @param
     * @return
     */
    public Painting queryPaintingsByTitleAndAuthor(String title, int author){
        try{
            PreparedStatement ps = conn.prepareStatement("select * from painting where title = ? and made_by = ?");
            ps.setString(1, title);
            ps.setInt(2, author);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String dbTitle = rs.getString("title");
                String dbAuthor = rs.getString("author");
                Painting dbPainting = new Painting();
                return dbPainting;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void updatePainting(Painting p){
        try{
            PreparedStatement ps = conn.prepareStatement("update painting set author = ? where title = ?");
//            ps.setString(1, p.getAuthor());
            ps.setString(2, p.getTitle());
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void deletePainting(String title){
        try{
            PreparedStatement ps = conn.prepareStatement("delete painting where title = ?");
            ps.setString(1, title);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public List<Painting> queryPaintingByYear(int year) {
        List<Painting> paintingList = new ArrayList<>();
        try{
            PreparedStatement ps = conn.prepareStatement("select * from painting where year_made = ? order by year_made asc");
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Painting dbPainting = new Painting(rs.getInt("painting_id"), rs.getString("title"),
                        rs.getInt("made_by"), rs.getInt("year_made"));
                paintingList.add(dbPainting);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return paintingList;
    }
    public List<Painting> queryPaintingBeforeYear(int year) {
        List<Painting> paintingList = new ArrayList<>();
        try{
            PreparedStatement ps = conn.prepareStatement("select * from painting where year_made < ?");
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Painting dbPainting = new Painting(rs.getInt("painting_id"), rs.getString("title"),
                        rs.getInt("made_by"), rs.getInt("year_made"));
                paintingList.add(dbPainting);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return paintingList;
    }
}
