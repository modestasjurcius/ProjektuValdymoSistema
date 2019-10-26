/**
 * @author Modestas
 */

package ProjectData;

import java.util.ArrayList;

public class CTask
{
    private CTask parentTask;
    private STaskInfo taskInfo;

    private ArrayList childTasks;
    private ArrayList comments;
    
    public CTask()
    {
        this.taskInfo = new STaskInfo();
        this.childTasks = new ArrayList();
    }
    
    public void addChildTask(CTask task)
    {
       this.childTasks.add(task);
    }
    
    public void setParentTask(CTask task)
    {
        this.parentTask = task;
    }
    
    public void setTaskName(String name)
    {
        this.taskInfo.name = name;
    }
    
    public String getTaskName()
    {
        return this.taskInfo.name;
    }
    
    public void setTaskDescription(String desc)
    {
        this.taskInfo.taskDescription = desc;
    }
    
    public void setTaskId(int id)
    {
        this.taskInfo.idNumber = id;
    }
}
