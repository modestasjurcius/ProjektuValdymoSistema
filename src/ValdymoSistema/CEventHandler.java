/**
 * @author Modestas
 */
package ValdymoSistema;

import ProjectData.CComment;
import ProjectData.CProject;
import ProjectData.CProjectController;
import ProjectData.CTask;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import org.json.simple.parser.ParseException;

public class CEventHandler
{
    private CProjectController projectController;
    
    private CProject workingProject;
    
    private String strMenu;
    private String pathToMenu;
    private String strUpdateMenu;
    private String pathToUpdateMenu;
    
    private static Scanner inputScanner;
    
    private enum eErrorCode
    {
        ERROR_OBJECT_NOT_FOUND,
        ERROR_INPUT_EXPECTED_NUMERIC,
        ERROR_BAD_INPUT,
        ERROR_UNKNOWN,
        ERROR_WORKING_PROJECT_INVALID,
        
        COUNT
    }
    
    public CEventHandler() throws FileNotFoundException
    {   
        this.projectController = new CProjectController();
        this.inputScanner = new Scanner(System.in);
        
        this.pathToMenu = "ConsoleMenuInfo.txt";
        this.pathToUpdateMenu = "TaskUpdateMenuInfo.txt";
        this.strUpdateMenu = "";
        this.strMenu = "";
       
        parseMenuFiles();
    }
    
    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    ///-------- Event handling methods
    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    
    public void mainEvent() throws IOException, FileNotFoundException, ParseException
    {
        print("\n-- Iveskite norima funkcija : ");
        String input = getInput();
        print("\n");
        
        int returnCode;
        
        if (isNumeric(input))
        {
            returnCode = handleEvent(Integer.parseInt(input));
        } 
        else
        {
            handleError(eErrorCode.ERROR_INPUT_EXPECTED_NUMERIC, input);
            return;
        }
        
        //handle return code
    }
    
    
    public int handleEvent(int eventId) throws IOException, FileNotFoundException, ParseException
    {
        int code = 0;
        
        switch(eventId)
        {
            case 1: createProject(); 
                break;
                
            case 2: createTask(); 
                break;
                
            case 3: removeTask();
                break;
                
            case 4: updateTask();
                break;
                
            case 5: printAllTasksInfo();
                break;
                
            case 6: importProject();
                break;
                
            case 7: exportProject();
                break;
            
            default: handleError(eErrorCode.ERROR_BAD_INPUT, String.valueOf(code));
                break;
        }
        
        return code;
    }
    
    private void handleError(eErrorCode code, String input)
    {
        print("*****Klaida*****\n");
        
        switch (code)
        {          
            case ERROR_OBJECT_NOT_FOUND :
                print("Objektas su id : " + input + " -- nerastas\n");
                break;
            case ERROR_INPUT_EXPECTED_NUMERIC:
                print("Bloga ivestis : " + input + " -- tiketasi skaitines reiksmes\n");
                break;
            case ERROR_BAD_INPUT:
                print("Bloga ivestis : " + input + " -- esamame kontekste tokio pasirinkimo nera!\n");
                break;
            case ERROR_UNKNOWN:
                print("Ivyko nezinoma klaida.\n");
                break;
            case ERROR_WORKING_PROJECT_INVALID:
                print("Nepasirinktas joks darbinis projektas!\n");
                break;
            default : break;
        }
        print("***********\n Griztama i pagrindini menu\n\n");
    }
    
    private void handleError(eErrorCode code)
    {
        handleError(code, "");
    }
    
    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    ///-------- Task handling methods
    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    
    private void removeTask()
    {
        if(!isWorkingProjectValid())
        {
            handleError(eErrorCode.ERROR_WORKING_PROJECT_INVALID);
            return;
        }
        
        if(this.workingProject.getTaskCount() == 0)
        {
            print("-- Nera uzduociu pasirinktame projekte!\n");
            return;
        }
        
        print("\n--Iveskite uzduoties pavadinima : ");
        String input = getInput();
        
        if(this.workingProject.removeTask(input))
        {
            print("\n-- Uzduotis pavadinimu : " + input + " sekmingai panaikinta!");
        }
        else
        {
            handleError(eErrorCode.ERROR_OBJECT_NOT_FOUND, input);
        }
    }
    
    private void updateTask()
    {
        if(!isWorkingProjectValid())
        {
            handleError(eErrorCode.ERROR_WORKING_PROJECT_INVALID);
            return;
        }
        
        print("\n -- Iveskite uzduoties pavadinima : ");
        String taskName = getInput();
        
        CTask task = getTask(taskName);
        
        if(task == null)
        {
            handleError(eErrorCode.ERROR_OBJECT_NOT_FOUND, taskName);
            return;
        }
        
        boolean exitTaskUpdater = false;
        while(!exitTaskUpdater)
        {
            printTaskUpdateMenu();
            print("\n-- Iveskite pasirinkima : ");
            String input = getInput();
            
            if(isNumeric(input))
            {
                int choice = Integer.parseInt(input);
                
                switch(choice)
                {
                    case 1: updateTaskName(task);
                        break;
                        
                    case 2: updateTaskDescription(task);
                        break;
                        
                    case 3:
                        CTask childTask = createTask();
                        task.addChildTask(childTask);
                        childTask.setParentTask(task);
                        break;
                        
                    case 4: createParentTask(task);
                        break;
                        
                    case 5: addCommentToTask(task);
                        break;
                        
                    case 6: removeComment(task);
                        break;
                        
                    case 7: updateTaskCompleteness(task);
                        break;
                        
                    case 8: print(task.generateTaskInfoOutput());
                        break;
                        
                    case 9: exitTaskUpdater = true; 
                        break;
                        
                    default: handleError(eErrorCode.ERROR_BAD_INPUT);
                        break;
                }
            }        
        }  
    }
    
    private void updateTaskCompleteness(CTask task)
    {
        print("\n-- Dabartinis uzduoties uzbaigtumo lygis : " + task.getCompleteLevel() + "%");
        print("\n-- Iveskite nauja lygi (0-100) : ");
        
        String input = getInput();
        
        if(!isNumeric(input))
        {
            handleError(eErrorCode.ERROR_INPUT_EXPECTED_NUMERIC);
            return;
        }
        
        int value = 0;
        
        try 
        {
           value = Integer.parseInt(input); 
        }
        catch (Exception e)
        {
            handleError(eErrorCode.ERROR_BAD_INPUT, input);
            return;
        }
        
        if(value >= 0 && value <= 100)
        {
            task.setCompleteLevel(value);
            print("\n-- Uzduoties atlikimo lygis pakeistas sekmingai!\n");
        }
        else
        {
            handleError(eErrorCode.ERROR_BAD_INPUT, input);
        }
    }
    
    private void addCommentToTask(CTask task)
    {
        print("\n-- Irasykite komentara : ");
        String input = getInput();
        
        CComment comment = new CComment(input);
        
        print("\n-- Ar norite prisegti failu prie komentaro ? (y/n) : ");
        input = getInput();
        
        boolean attachFiles = input.equals("y");
        
        while(attachFiles)
        {
            print("\n-- Irasykite failo pavadinima : ");
            comment.attachFile(getInput());
            print("\n-- Ar norite ivesti kita faila ? (y/n) : ");
            input = getInput();
            attachFiles = input.equals("y");
        }
        
        task.addComment(comment);
        print("\n-- Komentaras sekmingai pridetas!");
    }
    
    private void removeComment(CTask task)
    {
        print("\n-- Irasykite komentaro ID: ");
        String input = getInput();
        
        if(!isNumeric(input))
        {
            handleError(eErrorCode.ERROR_INPUT_EXPECTED_NUMERIC, input);
            return;
        }
        
        try
        {
            int commentId = Integer.parseInt(input);
            
            if(!task.removeComment(commentId))
            {
                handleError(eErrorCode.ERROR_BAD_INPUT, input);
                return;
            }
            else
            {
                print("\n-- Komentaras su id : " + input + " -- sekmingai panaikintas!\n");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void updateTaskName(CTask task)
    {
        print("\n-- Dabartinis uzduoties pavadinimas : " + task.getTaskName());
        print("\n-- Iveskite nauja pavadinima : ");
        String newName = getInput();
        task.setTaskName(newName);
        print("\n-- Uzduoties pavadinimas sekmingai pakeistas!\n");
    }
    
    private void updateTaskDescription(CTask task)
    {
        print("\n-- Dabartinis uzduoties aprasymas : \n" + task.getTaskDescription());
        print("\n-- Iveskite nauja aprasyma : ");
        String newDesc = getInput();
        task.setTaskDescription(newDesc);
        print("\n-- Uzduoties aprasymas sekmingai pakeistas!\n");
    }
    
    private void createParentTask(CTask childTask)
    {
        if(childTask.hasParentTask())
        {
            print("\n-- Uzduotis jau turi tevine uzduoti !\n");
            return;
        }
        CTask parentTask = createTask();
        
        childTask.setParentTask(parentTask);
        parentTask.addChildTask(childTask);
    }
    
    private CTask createTask()
    {
        if(!isWorkingProjectValid())
        {
            handleError(eErrorCode.ERROR_WORKING_PROJECT_INVALID);
            return null;
        }
        
        CTask task = new CTask();
        
        print("\n-- Iveskite uzduoties pavadinima : ");
        String input = getInput();
        task.setTaskName(input);
        
        print("\n-- Iveskite uzduoties aprasyma : ");
        input = getInput();
        task.setTaskDescription(input);
        
        int id = this.workingProject.getTaskCount();
        task.setTaskId(id);
        
        print("\n-- Uzduotis sekmingai sukurta ir prideta prie darbinio projekto!\n\n");
        
        this.workingProject.addTask(task);
        return task;
    }
    
    private void printAllTasksInfo()
    {
        if(!isWorkingProjectValid())
        {
            handleError(eErrorCode.ERROR_WORKING_PROJECT_INVALID);
            return;
        }
        
        if(this.workingProject.getTaskCount() == 0)
        {
            print("-- Nera uzduociu pasirinktame projekte!\n");
            return;
        }
        
        for(Object obj : this.workingProject.getAllTasks())
        {
            CTask task = (CTask) obj;
            
            if(task == null)
            {
                handleError(eErrorCode.ERROR_UNKNOWN);
                return;
            }
            
            print(task.generateTaskInfoOutput());
        }
    }
     
    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    ///-------- Project handling methods
    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    
    private boolean createProject()
    {
        this.workingProject = new CProject();
        
        print("\n-- Iveskite projekto pavadinima : ");
        String name = getInput();
        
        this.workingProject.setProjectName(name);
        
        print("\n-- Projektas pavadinimu : " + name + " - Sekmingai sukurtas!");
        print("\n-- Darbinis projektas nustatytas i naujai sukurta projekta.\n\n");
        
        return true;
    }
    
    private void importProject() throws IOException, FileNotFoundException, ParseException
    {
        print("\n-- Iveskite failo pavadinima : ");
        String input = getInput();
        
        if(!input.endsWith(".json"))
        {
           input += ".json"; 
        }
        
        CProject project = new CProject();
        
        if(project.importData(input))
        {
            this.workingProject = project;

            print("\n Projektas sekmingai importuotas !\n");
        }
        else
        {
            handleError(eErrorCode.ERROR_UNKNOWN);
        }
    }
    
    private void exportProject()
    {
        if(!isWorkingProjectValid())
        {
            handleError(eErrorCode.ERROR_WORKING_PROJECT_INVALID);
            return;
        }
        
        print("\n-- Iveskite failo pavadinima : ");
        String input = getInput();
        
        this.workingProject.exportData(input);
    }
    
    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    ///-------- Printer methods
    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    
    public void printMenu()
    {
        print(this.strMenu);
        
        String workingProjectInfo = "\n===>>>> Darbinis projektas : ";
        
        if(this.workingProject == null)
        {
            workingProjectInfo += "Nepasirinktas\n";
        }
        else
        {
            String projectName = this.workingProject.getProjectName();
            if(!projectName.isEmpty())
            {
                workingProjectInfo += projectName + "\n";
            }
            else
            {
                workingProjectInfo += "Be pavadinimo \n";
            }
        }
        
        print(workingProjectInfo);
    }
    
    public void printTaskUpdateMenu()
    {
        print("\n" + this.strUpdateMenu);
    }
    
    private void print(String str)
    {
        System.out.print(str);
    }
    
    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    ///-------- Parsing methods
    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    
    private void parseMenuFiles() throws FileNotFoundException
    {   
        File file = new File(this.pathToMenu);
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine())
        {
            this.strMenu += sc.nextLine();
            this.strMenu += "\n";
        }
        
        file = new File(this.pathToUpdateMenu);
        sc = new Scanner(file);
        while (sc.hasNextLine())
        {
            this.strUpdateMenu += sc.nextLine();
            this.strUpdateMenu += "\n";
        }
    }
    
    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    ///-------- Utils
    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    
    public String getInput()
    {
        if (inputScanner != null)
        {
            return inputScanner.nextLine();
        }
        return null;
    }

    private CTask getTask(String taskName)
    {
        return this.workingProject.getTaskByName(taskName);
    }

    public boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
    
    private boolean isWorkingProjectValid()
    {
        return this.workingProject != null;
    }
}
