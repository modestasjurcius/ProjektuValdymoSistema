/**
 * @author Modestas
 */
package ValdymoSistema;

import ProjectData.CComment;
import ProjectData.CProject;
import ProjectData.CProjectController;
import ProjectData.CTask;
import static ValdymoSistema.Main.getMainController;
import ValdymoSistema.Views.ErrorDialogController;
import ValdymoSistema.Views.SuccessDialogController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CEventHandler
{

    private CProjectController projectController;

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
        COUNT
    }

    public enum eEventType
    {
        EVENT_NONE,
        EVENT_CREATE_PROJECT,
        EVENT_CREATE_TASK,
        EVENT_REMOVE_TASK,
        EVENT_INSPECT_TASK,
        EVENT_GET_ALL_TASKS_INFO,
        EVENT_IMPORT_PROJECT,
        EVENT_EXPORT_PROJECT,
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
        this.projectController = new CProjectController();
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
        return 0;
    }

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

    private void updateTask()
    {
        if (!isWorkingProjectValid())
        {
            handleError(eErrorCode.ERROR_WORKING_PROJECT_INVALID);
            return;
        }

        print("\n -- Iveskite uzduoties pavadinima : ");
        String taskName = getInput();

        CTask task = getTaskByName(taskName);

        if (task == null)
        {
            handleError(eErrorCode.ERROR_OBJECT_NOT_FOUND, taskName);
            return;
        }

        boolean exitTaskUpdater = false;
        while (!exitTaskUpdater)
        {
            printTaskUpdateMenu();
            print("\n-- Iveskite pasirinkima : ");
            String input = getInput();

            if (isNumeric(input))
            {
                int choice = Integer.parseInt(input);

                switch (choice)
                {
                    case 1:
                        updateTaskName(task);
                        break;

                    case 2:
                        updateTaskDescription(task);
                        break;

                    case 3:
                        CTask childTask = createTask("", "");
                        task.addChildTask(childTask);
                        childTask.setParentTask(task);
                        break;

                    case 4:
                        createParentTask(task);
                        break;

                    case 5:
                        addCommentToTask(task);
                        break;

                    case 6:
                        removeComment(task);
                        break;

                    case 7:
                        updateTaskCompleteness(task);
                        break;

                    case 8:
                        print(task.generateTaskInfoOutput());
                        break;

                    case 9:
                        exitTaskUpdater = true;
                        break;

                    default:
                        handleError(eErrorCode.ERROR_BAD_INPUT);
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

        if (!isNumeric(input))
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

        if (value >= 0 && value <= 100)
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

        while (attachFiles)
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

        if (!isNumeric(input))
        {
            handleError(eErrorCode.ERROR_INPUT_EXPECTED_NUMERIC, input);
            return;
        }

        try
        {
            int commentId = Integer.parseInt(input);

            if (!task.removeComment(commentId))
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
        if (childTask.hasParentTask())
        {
            print("\n-- Uzduotis jau turi tevine uzduoti !\n");
            return;
        }
        CTask parentTask = createTask("", "");

        childTask.setParentTask(parentTask);
        parentTask.addChildTask(childTask);
    }

    public CTask createTask(String name, String description)
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

        this.workingProject.addTask(task);

        Main.getMainController().addTaskToList(task);

        return task;
    }

    private void printAllTasksInfo()
    {
        if (!isWorkingProjectValid())
        {
            handleError(eErrorCode.ERROR_WORKING_PROJECT_INVALID);
            return;
        }

        if (this.workingProject.getTaskCount() == 0)
        {
            print("-- Nera uzduociu pasirinktame projekte!\n");
            return;
        }

        for (Object obj : this.workingProject.getAllTasks())
        {
            CTask task = (CTask) obj;

            if (task == null)
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

        controller.clearTaskList();

        for (Object obj : this.workingProject.getAllTasks())
        {
            controller.addTaskToList((CTask) obj);
        }
    }

    public void exportProject(String projectName)
    {
        if (!isWorkingProjectValid())
        {
            handleError(eErrorCode.ERROR_WORKING_PROJECT_INVALID);
            return;
        }

        //print("\n-- Iveskite failo pavadinima : ");
        //String input = getInput();
        this.workingProject.exportData(projectName);

        handleInfo(eInfoType.INFO_PROJECT_EXPORTED, projectName);
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
