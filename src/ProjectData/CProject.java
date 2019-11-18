/**
 * @author Modestas
 */

package ProjectData;

import UserData.CUser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.*; 
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class CProject
{
    private CUser projectOwner;
    
    private ArrayList projectWorkers;
    private ArrayList projectTasks;
    
    private String projectName;
    private String projectFile;
    
    public CProject()
    {   
        this.projectWorkers = new ArrayList();
        this.projectTasks = new ArrayList();
        this.projectName = "";
        this.projectFile = "";
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
        this.projectFile = name + ".json";
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
    
    public String getProjectSaveFile()
    {
        return this.projectFile;
    }
    
    public int getTaskCount()
    {
        return this.projectTasks.size();
    }
    
    public CTask getTaskByName(String name)
    {
        for(int i = 0; i<this.projectTasks.size(); i++)
        {
            CTask task = (CTask) this.projectTasks.get(i);
            if(task != null && task.getTaskName().equals(name))
            {
                return task;
            }
        }
        return null;
    }
    
    public ArrayList<CTask> getAllTasks()
    {
        return this.projectTasks;
    }
    
    public boolean removeTask(String name)
    {
        CTask task = getTaskByName(name);
        
        if(task == null || !this.projectTasks.contains(task))
        {
           return false; 
        }
        
        checkDependencies(task);
        
        this.projectTasks.remove(task);
        return true;
    }
    
    public void checkDependencies(CTask task)
    {
        if(task.hasParentTask())
        {
            CTask parentTask = task.getParentTask();
            ArrayList parentsChildren = parentTask.getChildTasks();
            parentsChildren.remove(task);
        }
        
        for(Object obj : task.getChildTasks())
        {
            CTask childTask = (CTask) obj;
            childTask.setParentTask(null);
        }
    }
    
    public boolean importData(String file) throws FileNotFoundException, IOException, ParseException
    {
        try
        {
            FileReader reader = new FileReader(file);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonData = (JSONObject) jsonParser.parse(reader);

            if (jsonData.isEmpty())
            {
                return false;
            }

            this.projectName = (String) jsonData.get("Name");
            this.projectFile = this.projectName + ".json";

            JSONArray tasks = (JSONArray) jsonData.get("Tasks");
            
            ArrayList parentToChildDependencies = new ArrayList();

            if (!tasks.isEmpty())
            {
                for(Object obj : tasks)
                {
                    JSONObject data = (JSONObject) obj;
                    CTask task = new CTask();
                    
                    task.parse(data, parentToChildDependencies);
                    
                    this.projectTasks.add(task);
                }
            }
            
            if(!parentToChildDependencies.isEmpty())
            {
                for(Object obj : parentToChildDependencies)
                {
                    Pair<String, String> dependency = (Pair)obj;
                    registerTaskDependency(dependency.getKey(), dependency.getValue());
                }
            }
            
            
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException | ParseException e) {
            return false;
        }
        
        return true;
    }
    
    private void registerTaskDependency(String parent, String child)
    {
        CTask parentTask = getTaskByName(parent);
        CTask childTask = getTaskByName(child);
        
        if(parentTask == null || childTask == null)
        {
            return;
        }
        
        childTask.setParentTask(parentTask);
    }
    
    public void exportData()
    {
        JSONObject data = new JSONObject();
    
        data.put("Name", this.projectName);
        
        JSONArray tasks = new JSONArray();
        
        for(Object element : this.projectTasks)
        {
            CTask task = (CTask) element;
            tasks.add(task.generateExportJson());
        }
        
        data.put("Tasks", tasks);

        try (FileWriter file = new FileWriter((this.projectFile))) {
            file.write(data.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }
}
