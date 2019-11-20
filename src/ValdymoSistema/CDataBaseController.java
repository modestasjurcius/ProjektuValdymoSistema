/**
 *
 * @author Modestas
 */
package ValdymoSistema;

import UserData.CUser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CDataBaseController
{

    private String db_url = "jdbc:mysql://localhost:3306/valdymosistema";
    private String user = "root";
    private String pass = "root";

    public CDataBaseController()
    {
    }

    public CUser getUser(String login, String pass)
    {
        try
        {
            Connection conn = getDBConnection();
            String sql = "SELECT * FROM users WHERE login = ? AND password = ?";
            PreparedStatement prep = conn.prepareStatement(sql);
            prep.setString(1, login);
            prep.setString(2, pass);

            ResultSet rs = prep.executeQuery();

            if (!rs.isBeforeFirst())
            {
                conn.close();
                prep.close();
                rs.close();
                return null;
            }

            int id = 0;
            String name = "";

            while (rs.next())
            {
                id = rs.getInt("id");
                name = rs.getString("login");
            }

            CUser user = new CUser(name, id);

            conn.close();
            prep.close();
            rs.close();

            return user;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    private Connection getDBConnection()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection(this.db_url, this.user, this.pass);

            return conn;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }
}
