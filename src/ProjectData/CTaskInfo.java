/**
 * @author Modestas
 */
package ProjectData;


public class CTaskInfo
{
    private int completeLevel;
    
    private String name;
    private String taskDescription;
    
    private int idNumber;
    
    public CTaskInfo()
    {
        this.completeLevel = 0;
        this.idNumber = -1;
    }
    
    public void setTaskName(String name)
    {
        this.name = name;
    }
    
    public void setTaskCompleteLevel(int value)
    {
        this.completeLevel = value;
    }
    
    public void setTaskDescription(String desc)
    {
        this.taskDescription = desc;
    }
    
    public void setTaskId(int id)
    {
        this.idNumber = id;
    }
}
