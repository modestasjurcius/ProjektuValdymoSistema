/**
 * @author Modestas
 */
package ValdymoSistema;

import ProjectData.CProject;
import ProjectData.CProjectController;
import ProjectData.CTask;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

public class CEventHandler
{
    private CProjectController projectController;
    
    private CProject workingProject;
    
    private String strMenu;
    private String pathToMenu;
    
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
        this.strMenu = "";
       
        parseMenu();
    }
    
    public void mainEvent()
    {
        print("\n Iveskite norima funkcija : ");
        String input = getInput();
        print("\n");
        
        if (isNumeric(input))
        {
            handleEvent(Integer.parseInt(input));
        } 
        else
        {
            handleError(eErrorCode.ERROR_INPUT_EXPECTED_NUMERIC, input);
        } 
    }
    
    public int handleEvent(int eventId)
    {
        int code = 0;
        
        switch(eventId)
        {
            case 1: createProject(); 
                break;
                
            case 2: createTask(); 
                break;
           
            default: break;
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
    
    private void createTask()
    {
        if(!isWorkingProjectValid())
        {
            handleError(eErrorCode.ERROR_WORKING_PROJECT_INVALID);
            return;
        }
        
        CTask task = new CTask();
        
        print("\n Iveskite uzduoties pavadinima : ");
        String input = getInput();
        task.setTaskName(input);
        
        print("\n Iveskite uzduoties aprasyma : ");
        input = getInput();
        task.setTaskDescription(input);
        
        int id = this.workingProject.getTaskCount();
        task.setTaskId(id);
        
        print("\n Uzduotis sekmingai sukurta ir prideta prie darbinio projekto!\n\n");
        
        this.workingProject.addTask(task);
    }
    
    private boolean createProject()
    {
        this.workingProject = new CProject();
        
        print("\n Iveskite projekto pavadinima : ");
        String name = getInput();
        
        this.workingProject.setProjectName(name);
        
        print("\n Projektas pavadinimu : " + name + " - Sekmingai sukurtas!");
        print("\n Darbinis projektas nustatytas i naujai sukurta projekta.\n\n");
        
        return true;
    }
    
    private void handleError(eErrorCode code)
    {
        handleError(code, "");
    }
    
    public void printMenu()
    {
        print(this.strMenu);
        
        String workingProjectInfo = "\n Darbinis projektas : ";
        
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
    
    private boolean isWorkingProjectValid()
    {
        return this.workingProject != null;
    }
    
    private void parseMenu() throws FileNotFoundException
    {   
        File file = new File(this.pathToMenu);
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine())
        {
            this.strMenu += sc.nextLine();
            this.strMenu += "\n";
        }
    }
    
    private void print(String str)
    {
        System.out.print(str);
    }
    
    public String getInput()
    {
        if (inputScanner != null)
        {
            return inputScanner.nextLine();
        }
        return null;
    }

    public boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
