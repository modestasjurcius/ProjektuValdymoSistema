/**
 * @author Modestas
 */
package ProjectData;

import java.util.ArrayList;
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CTask
{

    private CTask parentTask;
    private STaskInfo taskInfo;

    private ArrayList childTasks;
    private ArrayList<CComment> comments;

    public CTask()
    {
        this.taskInfo = new STaskInfo();
        this.childTasks = new ArrayList();
        this.comments = new ArrayList<CComment>();
    }

    public void addChildTask(CTask task)
    {
        this.childTasks.add(task);
    }

    private void removeChildTask(CTask task)
    {
        this.childTasks.remove(task);
    }

    public ArrayList<CTask> getChildTasks()
    {
        return this.childTasks;
    }

    public void setParentTask(CTask task)
    {
        if (this.parentTask != null)
        {
            this.parentTask.removeChildTask(this);
        }

        this.parentTask = task;

        if (this.parentTask != null)
        {
            task.addChildTask(this);
        }
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
        comment.setId(this.comments.size());

        this.comments.add(comment);
    }

    public CComment getCommentById(int id)
    {
        return this.comments.get(id);
    }

    public int getCompleteLevel()
    {
        return this.taskInfo.completeLevel;
    }

    public void setCompleteLevel(int level)
    {
        this.taskInfo.completeLevel = level;
    }

    public void removeComment(int index)
    {
        this.comments.remove(index);

        for (int i = 0; i < this.comments.size(); i++)
        {
            CComment comment = (CComment) this.comments.get(i);
            comment.setId(i);
        }
    }

    public ArrayList<CComment> getComments()
    {
        return this.comments;
    }

    public int getCommentsCount()
    {
        return this.comments.size();
    }

    //=======================================
    public void parse(JSONObject data, ArrayList parentToChild)
    {
        try
        {
            this.taskInfo.name = (String) data.get("TaskName");
            this.taskInfo.completeLevel = ((Long) data.get("CompleteLevel")).intValue();
            this.taskInfo.idNumber = ((Long) data.get("ID")).intValue();
            this.taskInfo.taskDescription = (String) data.get("Description");

            if (data.containsKey("Comments"))
            {
                for (Object obj : (JSONArray) data.get("Comments"))
                {
                    JSONObject commentData = (JSONObject) obj;
                    CComment comment = new CComment();
                    comment.parse(commentData);

                    this.comments.add(comment);
                }
            }

            if (data.containsKey("ParentTask"))
            {
                String parentTaskName = (String) data.get("ParentTask");
                parentToChild.add(new Pair(parentTaskName, this.taskInfo.name));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public JSONObject generateExportJson()
    {
        JSONObject data = new JSONObject();

        data.put("TaskName", this.taskInfo.name);
        data.put("CompleteLevel", this.taskInfo.completeLevel);
        data.put("ID", this.taskInfo.idNumber);
        data.put("Description", this.taskInfo.taskDescription);

        if (hasParentTask())
        {
            data.put("ParentTask", this.parentTask.getTaskName());
        }

        if (this.comments.size() > 0)
        {
            JSONArray comments = new JSONArray();

            for (Object obj : this.comments)
            {
                CComment comment = (CComment) obj;
                comments.add(comment.generateExportJson());
            }

            data.put("Comments", comments);
        }

        return data;
    }

    public String generateTaskInfoOutput()
    {
        String out = "";

        out += "\n~~Pavadinimas : " + this.taskInfo.name + "\n";
        out += "~~ID numeris : " + this.taskInfo.idNumber + "\n";
        out += "~~Uzbaigtumo lygis : " + this.taskInfo.completeLevel + " %\n";
        out += "~~Tevine uzduotis : ";

        if (hasParentTask())
        {
            out += this.parentTask.getTaskName() + "\n";
        }
        else
        {
            out += "Nera\n";
        }

        out += "~~Vaikines uzduotys : ";

        int childTasksCount = this.childTasks.size();
        if (childTasksCount > 0)
        {
            for (int i = 0; i < childTasksCount; i++)
            {
                if (i > 0)
                {
                    out += ", ";
                }

                CTask childTask = (CTask) this.childTasks.get(i);
                out += childTask.getTaskName();
            }
        }
        else
        {
            out += "Nera";
        }

        out += "\n~~Aprasymas : " + this.taskInfo.taskDescription + "\n";
        out += "~~Komentarai : ";

        if (this.comments.isEmpty())
        {
            out += "Nera\n";
        }
        else
        {
            out += "\n";

            for (Object obj : this.comments)
            {
                CComment com = (CComment) obj;
                out += com.generateCommentOutput();
            }
        }

        out += "\n";

        return out;
    }
}
