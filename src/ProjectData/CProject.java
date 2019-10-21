/**
 * @author Modestas
 */

package ProjectData;

import UserData.CUser;

import java.util.*; 

public class CProject
{
    private CUser projectOwner;
    
    private ArrayList projectWorkers;
    private ArrayList projectTasks;
    
    private String projectName;
    
    public CProject()
    {   
        this.projectWorkers = new ArrayList();
        this.projectTasks = new ArrayList();
        this.projectName = "";
    }
    
    public void setOwner(CUser user)
    {
        this.projectOwner = user;
    }
    
    public void addWorker(CUser worker)
    {
        projectWorkers.add(worker);
    }
    
    public void setProjectName(String name)
    {
        this.projectName = name;
    }
    
    public void addTask(CTask task)
    {
        this.projectTasks.add(task);
    }
    
    public String getProjectName()
    {
        if(this.projectName.isEmpty())
        {
            return "No working project";
        }
        else
        {
            return this.projectName;
        }
    }
    
    public int getTaskCount()
    {
        return this.projectTasks.size();
    }
}
