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
        this.comments = new ArrayList();
    }
    
    public void addChildTask(CTask task)
    {
       this.childTasks.add(task);
    }
    
    public ArrayList<CTask> getChildTasks()
    {
        return this.childTasks;
    }
    
    public void setParentTask(CTask task)
    {
        this.parentTask = task;
    }
    
    public boolean hasParentTask()
    {
        return this.parentTask != null;
    }
    
    public CTask getParentTask()
    {
        return this.parentTask;
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
    
    public String getTaskDescription()
    {
        return this.taskInfo.taskDescription;
    }
    
    public void setTaskId(int id)
    {
        this.taskInfo.idNumber = id;
    }
    
    public void addComment(CComment comment)
    {
        this.comments.add(comment);
    }
    
    public int getCompleteLevel()
    {
        return this.taskInfo.completeLevel;
    }
    
    public void setCompleteLevel(int level)
    {
        this.taskInfo.completeLevel = level;
    }
    
    //=======================================
    
    public String generateTaskInfo()
    {
        String out = "";
        
        out += "~~Pavadinimas : " + this.taskInfo.name + "\n";
        out += "~~ID numeris : "+ this.taskInfo.idNumber + "\n";
        out += "~~Uzbaigtumo lygis : " + this.taskInfo.completeLevel + "\n";
        out += "~~Tevine uzduotis : ";
        
        if(hasParentTask())
        {
            out += this.parentTask.getTaskName() + "\n";
        }
        else
        {
            out += "Nera\n";
        }
        
        out += "~~Vaikines uzduotys : ";
        
        int childTasksCount = this.childTasks.size();
        if(childTasksCount > 0)
        {
            for(int i = 0; i < childTasksCount; i++)
            {
                CTask childTask = (CTask) this.childTasks.get(i);
                out += childTask.getTaskName();
            }
        }
        else
        {
            out += "Nera\n";
        }
        
        out += "~~Aprasymas : \n" + this.taskInfo.taskDescription + "\n";
        return out;
    }
}
