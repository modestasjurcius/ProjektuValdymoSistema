/**
 *
 * @author Modestas
 */
package ValdymoSistema;

import ProjectData.CProject;
import UserData.CUser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
            String fullName = "";
            String contacts = "";
            boolean isSingleUser = false;

            while (rs.next())
            {
                id = rs.getInt("id");
                name = rs.getString("login");
                isSingleUser = rs.getByte("is_single_user") != 0;
                fullName = rs.getString("full_user_name");
                contacts = rs.getString("user_contact");
            }

            CUser user = new CUser(name, id, isSingleUser, fullName, contacts);

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

    private ObservableList<String> getAllUserList()
    {
        ObservableList list = FXCollections.observableArrayList();

        try
        {
            Connection conn = getDBConnection();
            Statement stat = conn.createStatement();
            String sql = "SELECT id, login FROM users";
            ResultSet rs = stat.executeQuery(sql);

            while (rs.next())
            {
                String name = rs.getString("login");
                list.add(name);
            }

            conn.close();
            stat.close();
            rs.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return list;
    }

    private Connection getDBConnection()
    {
        try
        {
            Connection conn = DriverManager.getConnection(this.db_url, this.user, this.pass);

            return conn;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

    public Map getSavedProjects(CUser user, boolean workingProjects, boolean owningProjects)
    {
        Map map = new Hashtable<>();

        if (user == null)
        {
            return map;
        }

        try
        {
            Connection conn = getDBConnection();

            String sql = "";
            PreparedStatement prep = null;
            ResultSet rs = null;

            if (workingProjects)
            {
                sql = "SElECT * FROM projects_workers WHERE user_id = ?";
                prep = conn.prepareStatement(sql);
                prep.setInt(1, user.getId());
                rs = prep.executeQuery();

                parseSavedProjectsInMap(map, rs);
            }

            if (owningProjects)
            {
                sql = "SElECT * FROM projects WHERE project_owner_id = ?";
                prep = conn.prepareStatement(sql);
                prep.setInt(1, user.getId());
                rs = prep.executeQuery();

                parseSavedProjectsInMap(map, rs);
            }

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
            while (rs.next())
            {
                ret = rs.getString("project_file");
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

    public ObservableList<String> getProjectWorkers(CProject proj)
    {
        ObservableList list = FXCollections.observableArrayList();
        int project_id = getProjectIdByName(proj.getName());

        if (project_id < 0)
        {
            return list;
        }

        try
        {
            Connection conn = getDBConnection();
            String sql = "SELECT * FROM projects_workers WHERE project_id = ?";
            PreparedStatement prep = conn.prepareStatement(sql);
            prep.setInt(1, project_id);
            ResultSet rs = prep.executeQuery();

            while (rs.next())
            {
                int user_id = rs.getInt("user_id");
                CUser worker = getUserById(user_id);

                if (worker != null)
                {
                    list.add(worker.getUserName());
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return list;
    }

    private int getProjectIdByName(String name)
    {
        int project_id = -1;

        try
        {
            Connection conn = getDBConnection();
            String sql = "SELECT project_id FROM projects WHERE project_name = ?";
            PreparedStatement prep = conn.prepareStatement(sql);
            prep.setString(1, name);
            ResultSet rs = prep.executeQuery();

            if (rs.next())
            {
                project_id = rs.getInt("project_id");
            }

            conn.close();
            prep.close();
            rs.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return project_id;
    }

    private CUser getUserById(int id)
    {
        try
        {
            Connection conn = getDBConnection();
            String sql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement prep = conn.prepareStatement(sql);
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();

            if (rs.next())
            {
                String name = rs.getString("login");
                boolean isSingleUser = rs.getByte("is_single_user") != 0;
                String fullName = rs.getString("full_user_name");
                String contacts = rs.getString("user_contact");

                conn.close();
                prep.close();
                rs.close();

                return new CUser(name, id, isSingleUser, fullName, contacts);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

    public void createProject(CUser owner, String projectName, String projectFile)
    {
        try
        {
            int owner_id = owner.getId();

            Connection conn = getDBConnection();
            String sql = "INSERT INTO projects " + "VALUES (?, ?, ?, ?)";
            PreparedStatement prep = conn.prepareStatement(sql);

            prep.setInt(1, 0); //id is auto-increment
            prep.setString(2, projectName);
            prep.setString(3, projectFile);
            prep.setInt(4, owner_id);

            prep.executeUpdate();

            conn.close();
            prep.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setProjectOwner(CProject proj)
    {
        try
        {
            Connection conn = getDBConnection();
            String sql = "SELECT project_owner_id FROM projects WHERE project_name = ?";
            PreparedStatement prep = conn.prepareStatement(sql);

            prep.setString(1, proj.getProjectName());

            ResultSet rs = prep.executeQuery();

            if (rs.next())
            {
                int owner_id = rs.getInt("project_owner_id");
                CUser owner = getUserById(owner_id);
                if (owner != null)
                {
                    proj.setOwner(owner);
                }
            }

            conn.close();
            prep.close();
            rs.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public ObservableList<String> getFreeWorkersForProject(CProject project)
    {
        ObservableList<String> projectWorkersList = getProjectWorkers(project);
        ObservableList<String> allWorkersList = getAllUserList();

        String projectOwnerName = project.getOwner().getUserName();

        allWorkersList.removeAll(projectWorkersList);
        allWorkersList.remove(projectOwnerName);

        return allWorkersList;
    }

    public boolean addWorkerToProject(CProject project, String workerName)
    {
        try
        {
            CUser worker = getUserByName(workerName);
            int project_id = getProjectIdByName(project.getProjectName());

            if (worker == null || project_id < 0)
            {
                return false;
            }

            Connection conn = getDBConnection();
            String sql = "INSERT INTO projects_workers VALUES (?, ?)";
            PreparedStatement prep = conn.prepareStatement(sql);

            prep.setInt(1, project_id);
            prep.setInt(2, worker.getId());

            prep.executeUpdate();

            conn.close();
            prep.close();
            return true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return false;
    }

    public boolean removeWorkerFromProject(CProject project, String workerName)
    {
        try
        {
            CUser worker = getUserByName(workerName);
            int project_id = getProjectIdByName(project.getProjectName());

            if (worker == null || project_id < 0)
            {
                return false;
            }

            Connection conn = getDBConnection();
            String sql = "DELETE FROM projects_workers WHERE project_id = ? AND user_id = ?";
            PreparedStatement prep = conn.prepareStatement(sql);

            prep.setInt(1, project_id);
            prep.setInt(2, worker.getId());
            
            prep.executeUpdate();
            
            conn.close();
            prep.close();
            
            return true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }

    private CUser getUserByName(String name)
    {
        CUser user = null;
        try
        {
            Connection conn = getDBConnection();
            String sql = "SELECT * FROM users WHERE login = ?";
            PreparedStatement prep = conn.prepareStatement(sql);

            prep.setString(1, name);

            ResultSet rs = prep.executeQuery();

            while (rs.next())
            {
                int id = rs.getInt("id");
                boolean isSingleUser = rs.getByte("is_single_user") != 0;
                String fullName = rs.getString("full_user_name");
                String contacts = rs.getString("user_contact");

                user = new CUser(name, id, isSingleUser, fullName, contacts);
            }

            conn.close();
            prep.close();
            rs.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return user;
    }
}
