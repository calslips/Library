import DAO.PaintingDAO;
import Model.Painting;
import Util.ConnectionSingleton;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

public class PaintingDAOTest {
    Connection conn;
    PaintingDAO paintingDAO;
    @Before
    public void setUp(){
        conn = ConnectionSingleton.getConnection();
        ConnectionSingleton.resetTestDatabase();
        paintingDAO = new PaintingDAO(conn);
    }

    /**
     * test that all painting that are retrieved are from the expected year.
     */
    @Test
    public void testGetByYear(){

        List<Painting> actual = paintingDAO.queryPaintingByYear(1880);
        int expectedYear = 1880;
        for(int i = 0 ; i < actual.size() ; i++){
            Assert.assertEquals(expectedYear, actual.get(i).getYearMade());
        }

    }

    /**
     * test that no paintings that are retrieved are from an unexpected year.
     */
    @Test
    public void testGetByYearNotRetrieveUnexpectedYear(){

        List<Painting> actual = paintingDAO.queryPaintingByYear(1880);
        int expectedYear = 1890;
        for(int i = 0 ; i < actual.size() ; i++){
            Assert.assertNotEquals(expectedYear, actual.get(i).getYearMade());
        }
    }
}