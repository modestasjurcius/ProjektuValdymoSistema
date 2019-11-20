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
import java.util.Hashtable;
import java.util.Map;

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

    public Map getSavedProjects(CUser user)
    {
        Map map = new Hashtable<>();

        if (user == null)
        {
            return map;
        }

        try
        {
            Connection conn = getDBConnection();
            String sql = "SElECT * FROM projects_workers WHERE user_id = ?";
            PreparedStatement prep = conn.prepareStatement(sql);
            prep.setInt(1, user.getId());
            ResultSet rs = prep.executeQuery();

            parseSavedProjectsInMap(map, rs);

            sql = "SElECT * FROM projects_owners WHERE user_id = ?";
            prep = conn.prepareStatement(sql);
            prep.setInt(1, user.getId());
            rs = prep.executeQuery();

            parseSavedProjectsInMap(map, rs);

            conn.close();
            prep.close();
            rs.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return map;
    }
    
    private void parseSavedProjectsInMap(Map map, ResultSet rs)
    {
        try
        {
            while (rs.next())
            {
                int project_id = rs.getInt("project_id");

                String name = getProjectStringValue(project_id, "project_name");
                String file = getProjectStringValue(project_id, "project_file");

                if (name != null && file != null && !map.containsKey(name))
                {
                    map.put(name, file);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private String getProjectStringValue(int proj_id, String column)
    {
        String ret = null;
        try
        {
            Connection conn = getDBConnection();
            String sql = "SELECT * FROM projects WHERE project_id = ?";
            PreparedStatement prep = conn.prepareStatement(sql);

            prep.setInt(1, proj_id);

            ResultSet rs = prep.executeQuery();

            while (rs.next())
            {
                ret = rs.getString(column);
            }

            conn.close();
            prep.close();
            rs.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return ret;
    }

    public String getProjectSaveFile(String projectName)
    {
        String ret = null;
        try
        {
            Connection conn = getDBConnection();
            String sql = "SELECT * FROM projects WHERE project_name = ?";
            PreparedStatement prep = conn.prepareStatement(sql);
            
            prep.setString(1, projectName);
            
            ResultSet rs = prep.executeQuery();
            while(rs.next())
            {
               ret = rs.getString("project_file");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
        return ret;
    }
}
