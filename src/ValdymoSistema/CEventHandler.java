/**
 * @author Modestas
 */
package ValdymoSistema;

import ProjectData.CProject;
import ProjectData.CTask;
import static ValdymoSistema.Main.getMainController;
import ValdymoSistema.Views.ErrorDialogController;
import ValdymoSistema.Views.SuccessDialogController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CEventHandler
{
    private CProject workingProject;

    private String strMenu;
    private String pathToMenu;
    private String strUpdateMenu;
    private String pathToUpdateMenu;
    private String pathToSavedProjects;
    
    private Map<String, String> savedProjectList;

    private static Scanner inputScanner;

    public enum eErrorCode
    {
        ERROR_OBJECT_NOT_FOUND,
        ERROR_INPUT_EXPECTED_NUMERIC,
        ERROR_BAD_INPUT,
        ERROR_UNKNOWN,
        ERROR_WORKING_PROJECT_INVALID,
        ERROR_MISSING_INPUT,
        ERROR_TOO_LONG_INPUT,
        ERROR_TASK_NOT_SELECTED,
        ERROR_COMMENT_NOT_SELECTED,
        COUNT
    }

    public enum eInfoType
    {
        INFO_PROJECT_CREATED,
        INFO_PROJECT_IMPORTED,
        INFO_TASK_CREATED,
        INFO_TASK_REMOVED,
        INFO_PROJECT_EXPORTED,
        COUNT
    }

    public CEventHandler() throws FileNotFoundException, IOException, ParseException
    {
        this.inputScanner = new Scanner(System.in);

        this.pathToSavedProjects = "SavedProjects.json";
        this.pathToMenu = "ConsoleMenuInfo.txt";
        this.pathToUpdateMenu = "TaskUpdateMenuInfo.txt";
        this.strUpdateMenu = "";
        this.strMenu = "";

        this.savedProjectList = new Hashtable<>();

        parseMenuFiles();
        parseSavedProjects();
    }

    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    ///-------- Event handling methods
    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    public void handleError(eErrorCode code, String input)
    {
        print("*****Klaida*****\n");
        Stage errorStage = new Stage();
        ErrorDialogController eDialogController = new ErrorDialogController();
        try
        {
            File f = new File("src/ValdymoSistema/Views/ErrorDialog.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("src/ValdymoSistema/Views/ErrorDialog.fxml"));
            fxmlLoader.setLocation(f.toURI().toURL());
            Parent root1 = (Parent) fxmlLoader.load();
            errorStage.setScene(new Scene(root1));

            eDialogController = fxmlLoader.<ErrorDialogController>getController();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String message = "";

        switch (code)
        {
            case ERROR_OBJECT_NOT_FOUND:
                message = "Objektas su id : " + input + " -- nerastas\n";
                print("Objektas su id : " + input + " -- nerastas\n");
                break;
            case ERROR_INPUT_EXPECTED_NUMERIC:
                message = "Bloga ivestis : " + input + " -- tiketasi skaitines reiksmes\n";
                print("Bloga ivestis : " + input + " -- tiketasi skaitines reiksmes\n");
                break;
            case ERROR_BAD_INPUT:
                message = "Bloga ivestis : " + input + " -- esamame kontekste tokio pasirinkimo nera!\n";
                print("Bloga ivestis : " + input + " -- esamame kontekste tokio pasirinkimo nera!\n");
                break;
            case ERROR_UNKNOWN:
                message = "Ivyko nezinoma klaida.\n";
                print("Ivyko nezinoma klaida.\n");
                break;
            case ERROR_WORKING_PROJECT_INVALID:
                message = "Nepasirinktas joks darbinis projektas!\n";
                print("Nepasirinktas joks darbinis projektas!\n");
                break;
            case ERROR_MISSING_INPUT:
                message = "Ne visi privalomi teksto laukai yra užpildyti !";
                print("[Error] Missing input\n");
                break;
            case ERROR_TOO_LONG_INPUT:
                message = "Į lauką(-us) įrašytas(-i) per ilgas(-i) kintamasis(-ieji)!\n"
                        + "Leistinas kintamųjų ilgis : 15 simbolių";
                break;
            case ERROR_TASK_NOT_SELECTED:
                message = "Nepasirinkta jokia užduotis !";
                break;
            case ERROR_COMMENT_NOT_SELECTED:
                message = "Nepasirinktas joks komentaras !";
                break;

            default:
                break;
        }
        print("***********\n Griztama i pagrindini menu\n\n");

        eDialogController.setErrorMessage(message);
        errorStage.show();
    }

    public void handleInfo(eInfoType type, String info)
    {
        Stage successStage = new Stage();
        SuccessDialogController eDialogController = new SuccessDialogController();
        try
        {
            File f = new File("src/ValdymoSistema/Views/SuccessDialog.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("src/ValdymoSistema/Views/SuccessDialog.fxml"));
            fxmlLoader.setLocation(f.toURI().toURL());
            Parent root1 = (Parent) fxmlLoader.load();
            successStage.setScene(new Scene(root1));

            eDialogController = fxmlLoader.<SuccessDialogController>getController();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String message = "";

        switch (type)
        {
            case INFO_PROJECT_CREATED:
                message = "Sukurtas naujas projektas pavadinimu : " + info
                        + "\nDarbinis projektas nustatytas į naujai sukurtą projektą.";
                break;

            case INFO_PROJECT_IMPORTED:
                message = "Projektas sėkmingai importuotas!\nImportuotas projektas nustatytas į darbinį projektą.";
                break;

            case INFO_TASK_CREATED:
                message = "Užduotis pavadinimu : " + info
                        + " - sėkmingai sukurta!\nUžduotis pridėta prie darbinio projekto.";
                break;

            case INFO_PROJECT_EXPORTED:
                message = "Projektas pavadinimu : " + info
                        + " - sėkmingai exportuotas  į failą : " + info + ".json";
                break;

            case INFO_TASK_REMOVED:
                message = "Užduotis pavadinimu : " + info
                        + " - sėkmingai panaikinta!";

            default:
                break;
        }

        eDialogController.setMessage(message);
        successStage.show();
    }

    public void handleError(eErrorCode code)
    {
        handleError(code, "");
    }

    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    ///-------- Task handling methods
    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    public void removeTask(String taskName)
    {
        if (!isWorkingProjectValid())
        {
            handleError(eErrorCode.ERROR_WORKING_PROJECT_INVALID);
            return;
        }

        if (this.workingProject.getTaskCount() == 0)
        {
            return;
        }

        CTask task = this.workingProject.getTaskByName(taskName);
        Main.getMainController().removeTaskFromList(task);

        if (this.workingProject.removeTask(taskName))
        {
            print("\n-- Uzduotis pavadinimu : " + taskName + " sekmingai panaikinta!");
            handleInfo(eInfoType.INFO_TASK_REMOVED, taskName);
        }
        else
        {
            handleError(eErrorCode.ERROR_OBJECT_NOT_FOUND, taskName);
        }
    }

    public CTask createTask(String name, String description, CTask parentTask)
    {
        if (!isWorkingProjectValid())
        {
            handleError(eErrorCode.ERROR_WORKING_PROJECT_INVALID);
            return null;
        }

        CTask task = new CTask();

        print("\n-- Iveskite uzduoties pavadinima : ");
        task.setTaskName(name);

        print("\n-- Iveskite uzduoties aprasyma : ");
        task.setTaskDescription(description);

        int id = this.workingProject.getTaskCount();
        task.setTaskId(id);

        print("\n-- Uzduotis sekmingai sukurta ir prideta prie darbinio projekto!\n\n");

        if (parentTask != null)
        {
            task.setParentTask(parentTask);
            parentTask.addChildTask(task);
        }

        this.workingProject.addTask(task);

        Main.getMainController().addTaskToList(task);

        return task;
    }

    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    ///-------- Project handling methods
    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    public boolean createProject(String projectName)
    {
        CProject project = new CProject();
        project.setProjectName(projectName);

        //print("\n-- Iveskite projekto pavadinima : ");
        //String name = getInput();
        onWorkingProjectChange(project);

        print("\n-- Projektas pavadinimu : " + projectName + " - Sekmingai sukurtas!");
        print("\n-- Darbinis projektas nustatytas i naujai sukurta projekta.\n\n");

        handleInfo(eInfoType.INFO_PROJECT_CREATED, projectName);

        return true;
    }

    public void importProject(String projectName) throws IOException, FileNotFoundException, ParseException
    {
        //print("\n-- Iveskite failo pavadinima : ");
        //String input = getInput();

        CProject project = new CProject();

        String projectFile = this.savedProjectList.get(projectName);

        if (project.importData(projectFile))
        {
            onWorkingProjectChange(project);

            print("\n Projektas sekmingai importuotas !\n");
        }
        else
        {
            handleError(eErrorCode.ERROR_UNKNOWN);
        }
    }

    private void onWorkingProjectChange(CProject project)
    {
        this.workingProject = project;

        MainController controller = getMainController();

        controller.setWorkingProjectName(project.getProjectName());

        controller.refreshTasksListView();
    }

    public ArrayList<CTask> getAllWorkingProjectTasks()
    {
        return this.workingProject.getAllTasks();
    }

    public String getWorkingProjectSaveFile()
    {
        if (!isWorkingProjectValid())
        {
            handleError(eErrorCode.ERROR_WORKING_PROJECT_INVALID);
            return null;
        }

        return this.workingProject.getProjectSaveFile();
    }
    
    public CProject getWorkingProject()
    {
        return this.workingProject;
    }

    public void exportWorkingProject()
    {
        if (!isWorkingProjectValid())
        {
            handleError(eErrorCode.ERROR_WORKING_PROJECT_INVALID);
            return;
        }

        saveWorkingProject();
        this.workingProject.exportData();

        handleInfo(eInfoType.INFO_PROJECT_EXPORTED, this.workingProject.getProjectName());
    }

    public void saveWorkingProject()
    {
        this.savedProjectList.put(this.workingProject.getProjectName(), this.workingProject.getProjectSaveFile());
        exportSavedProjects();
    }

    private void exportSavedProjects()
    {
        try
        {
            JSONObject json = new JSONObject();
            JSONArray arr = new JSONArray();

            for (String name : this.savedProjectList.keySet())
            {
                JSONObject obj = new JSONObject();
                obj.put("Name", name);
                obj.put("File", this.savedProjectList.get(name));
                arr.add(obj);
            }

            json.put("Projects", arr);

            FileWriter file = new FileWriter(this.pathToSavedProjects);
            file.write(json.toJSONString());
            file.flush();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    ///-------- Printer methods
    ///--------<<<<<<<<<<<<<<<<<<<<<<<<
    public void printMenu()
    {
        print(this.strMenu);

        String workingProjectInfo = "\n===>>>> Darbinis projektas : ";

        if (this.workingProject == null)
        {
            workingProjectInfo += "Nepasirinktas\n";
        }
        else
        {
            String projectName = this.workingProject.getProjectName();
            if (!projectName.isEmpty())
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

    private void parseSavedProjects() throws FileNotFoundException, IOException, ParseException
    {
        try
        {
            FileReader reader = new FileReader(this.pathToSavedProjects);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonData = (JSONObject) jsonParser.parse(reader);

            JSONArray projects = (JSONArray) jsonData.get("Projects");

            for (Object obj : projects)
            {
                JSONObject project = (JSONObject) obj;

                if (project.containsKey("Name") && project.containsKey("File"))
                {
                    String name = (String) project.get("Name");
                    String file = (String) project.get("File");
                    this.savedProjectList.put(name, file);
                }
                else
                {
                    print("[ERROR] Projects parsing error");
                }
            }
        }
        catch (Exception ex)
        {
           ex.printStackTrace();
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

    public CTask getTaskByName(String taskName)
    {
        return this.workingProject.getTaskByName(taskName);
    }

    public boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public boolean isWorkingProjectValid()
    {
        return this.workingProject != null;
    }

    public boolean hasSavedProjects()
    {
        return !this.savedProjectList.isEmpty();
    }

    public Map getSavedProjects()
    {
        return this.savedProjectList;
    }
}
