/**
 * @author Modestas
 */

package ProjectData;

import java.util.ArrayList;

public class CTask
{
    private CTask parentTask;
    private CTaskInfo taskInfo;

    private ArrayList childTasks;
    
    public CTask()
    {
        this.taskInfo = new CTaskInfo();
        
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
        this.taskInfo.setTaskName(name);
    }
    
    public void setTaskDescription(String desc)
    {
        this.taskInfo.setTaskDescription(desc);
    }
    
    public void setTaskId(int id)
    {
        this.taskInfo.setTaskId(id);
    }
}
