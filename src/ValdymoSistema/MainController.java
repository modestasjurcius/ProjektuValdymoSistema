/**
 * FXML Controller class
 *
 * @author Modestas
 */
package ValdymoSistema;

import ProjectData.CProject;
import ProjectData.CTask;
import UserData.CUser;
import ValdymoSistema.CEventHandler.eErrorCode;
import ValdymoSistema.Views.ProjectImporterDialogController;
import ValdymoSistema.Views.UserViewerController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MainController implements Initializable
{

    private CEventHandler eventHandler;
    private String selectedTaskName;

    @FXML
    private Button createProjectButton;
    @FXML
    private ListView<String> tasksListView;
    @FXML
    private TabPane projectInfoTabPane;
    @FXML
    private Button taskViewerButton;
    @FXML
    private Button taskRemoverButton;
    @FXML
    private Label userNameLabel;
    @FXML
    private ListView<String> workersListView;
    @FXML
    private Tab tasksTab;
    @FXML
    private Tab workersTab;
    @FXML
    private Button addWorkerButton;
    @FXML
    private Button removeWorkerButton;
    @FXML
    private Button checkWorkerButton;
    @FXML
    private Label workingProjectNameLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.eventHandler = ValdymoSistema.Main.getEventHandler();
        this.workingProjectNameLabel.setText("Nepasirinktas");

        enableTaskViewButtons(false);
        enableUserViewButtons(false);

        this.tasksListView.setItems(FXCollections.observableArrayList());
        this.tasksListView.setOnMouseClicked(new EventHandler<MouseEvent>()
        {

            @Override
            public void handle(MouseEvent event)
            {
                selectedTaskName = tasksListView.getSelectionModel().getSelectedItem();

                if (selectedTaskName != null && selectedTaskName != "")
                {
                    taskViewerButton.setVisible(true);
                    taskRemoverButton.setVisible(true);
                }
            }
        });

    }

    private void terminate()
    {
        clearTaskList();
        clearWorkersList();
    }

    private void enableTaskViewButtons(boolean enable)
    {
        this.taskViewerButton.setVisible(enable);
        this.taskRemoverButton.setVisible(enable);
    }

    private void enableUserViewButtons(boolean enable)
    {
        this.addWorkerButton.setVisible(enable);
        this.removeWorkerButton.setVisible(enable);
        this.checkWorkerButton.setVisible(enable);
    }

    @FXML
    private void createProjectAction(ActionEvent event)
    {
        try
        {
            openView("src/ValdymoSistema/Views/CreateProjectDialog.fxml");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void openProjectAction(ActionEvent event) throws IOException
    {
        try
        {
            File f = new File("src/ValdymoSistema/Views/ProjectImporterDialog.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("src/ValdymoSistema/Views/ProjectImporterDialog.fxml"));
            fxmlLoader.setLocation(f.toURI().toURL());
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            Map savedProjects = this.eventHandler.getSavedProjects(this.eventHandler.getCurrentUser(), true, true);

            if (!savedProjects.isEmpty())
            {
                fxmlLoader.<ProjectImporterDialogController>getController().setSavedProjectList(savedProjects);
            }
            else
            {
                fxmlLoader.<ProjectImporterDialogController>getController().setValidImportMode(false);
            }

            stage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getSelectedTaskName()
    {
        String checkValue = this.tasksListView.getSelectionModel().getSelectedItem();

        if (checkValue == null)
        {
            return null;
        }
        else if (this.selectedTaskName == null)
        {
            this.selectedTaskName = checkValue;
            return checkValue;
        }
        else if (!checkValue.equals(this.selectedTaskName))
        {
            this.selectedTaskName = checkValue;
        }
        return this.selectedTaskName;
    }

    public void selectTask(String taskName)
    {
        try
        {
            this.tasksListView.getSelectionModel().select(taskName);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setWorkingProjectName(String name)
    {
        this.workingProjectNameLabel.setText(name);
    }

    public void fillTasksList(ObservableList<String> list)
    {
        this.tasksListView.setItems(list);
    }

    public void addTaskToList(CTask task)
    {
        this.tasksListView.getItems().add(task.getTaskName());
    }

    public void removeTaskFromList(CTask task)
    {
        this.tasksListView.getItems().remove(task.getTaskName());
    }

    public void clearTaskList()
    {
        this.tasksListView.getItems().clear();
    }

    private void clearWorkersList()
    {
        this.workersListView.getItems().clear();
    }

    public void refreshTasksListView()
    {
        this.tasksListView.getItems().clear();

        for (CTask task : this.eventHandler.getAllWorkingProjectTasks())
        {
            this.tasksListView.getItems().add(task.getTaskName());
        }
    }

    public void fillProjectWorkersList()
    {
        CProject proj = this.eventHandler.getWorkingProject();
        if (proj == null)
        {
            return;
        }

        ObservableList<String> projectWorkers = this.eventHandler.getDataBaseController().getProjectWorkers(proj);

        this.workersListView.setItems(projectWorkers);
    }

    @FXML
    private void createTaskAction(ActionEvent event) throws IOException
    {
        if (!this.eventHandler.isWorkingProjectValid())
        {
            this.eventHandler.handleError(eErrorCode.ERROR_WORKING_PROJECT_INVALID);
            return;
        }

        openView("src/ValdymoSistema/Views/TaskCreatDialog.fxml");
    }

    @FXML
    private void onViewTask(ActionEvent event) throws IOException
    {
        if (getSelectedTaskName() == null)
        {
            this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_TASK_NOT_SELECTED);
            return;
        }
        openTaskViewer();
    }

    public void openTaskViewer() throws IOException
    {
        openView("src/ValdymoSistema/Views/TaskViewer.fxml");
    }

    private void openView(String path) throws IOException
    {
        try
        {
            File f = new File(path);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(path));
            fxmlLoader.setLocation(f.toURI().toURL());
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void exportPojectData(ActionEvent event)
    {
        if (!this.eventHandler.isWorkingProjectValid())
        {
            this.eventHandler.handleError(eErrorCode.ERROR_WORKING_PROJECT_INVALID);
            return;
        }

        try
        {
            openView("src/ValdymoSistema/Views/ExportProjectDialog.fxml");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onRemoveTask(ActionEvent event)
    {
        if (this.selectedTaskName == null || this.selectedTaskName == "")
        {
            this.eventHandler.handleError(eErrorCode.ERROR_TASK_NOT_SELECTED);
            return;
        }

        this.eventHandler.removeTask(this.selectedTaskName);
    }

    @FXML
    private void onCheckUser(ActionEvent event)
    {
        openUserViewer(this.eventHandler.getCurrentUser());
    }
    
    private void openUserViewer(CUser user)
    {
        try
        {
            File f = new File("src/ValdymoSistema/Views/UserViewer.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("src/ValdymoSistema/Views/UserViewer.fxml"));
            fxmlLoader.setLocation(f.toURI().toURL());
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            fxmlLoader.<UserViewerController>getController().setUser(user);

            stage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setUserName(String name)
    {
        this.userNameLabel.setText(name);
    }

    @FXML
    private void onSelectionChanged(Event event)
    {
        if (this.workersTab.isSelected() && this.eventHandler.getWorkingProject() != null)
        {
            enableButtonsOnTabChange(false);
        }
        else
        {
            if (getSelectedTaskName() == null)
            {
                enableUserViewButtons(false);
            }
            else
            {
                enableButtonsOnTabChange(true);
            }
        }
    }

    private void enableButtonsOnTabChange(boolean value)
    {
        enableTaskViewButtons(value);
        enableUserViewButtons(!value);
    }

    @FXML
    private void onAddWorker(ActionEvent event)
    {
        try
        {
            openView("src/ValdymoSistema/Views/AddWorkerView.fxml");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onRemoveWorker(ActionEvent event)
    {
        String selectedWorkerName = this.workersListView.getSelectionModel().getSelectedItem();
        CProject workingProject = this.eventHandler.getWorkingProject();

        if (selectedWorkerName == null)
        {
            this.eventHandler.handleError(eErrorCode.ERROR_WORKER_NOT_SELECTED);
            return;
        }
        else if (workingProject == null)
        {
            this.eventHandler.handleError(eErrorCode.ERROR_WORKING_PROJECT_INVALID);
            return;
        }

        CDataBaseController dbController = this.eventHandler.getDataBaseController();

        if (dbController.removeWorkerFromProject(workingProject, selectedWorkerName))
        {
            this.eventHandler.handleInfo(CEventHandler.eInfoType.INFO_WORKER_REMOVED_FROM_PROJECT, selectedWorkerName);
            this.workersListView.getItems().remove(selectedWorkerName);
        }
        else
        {
            this.eventHandler.handleError(eErrorCode.ERROR_UNKNOWN);
        }
    }

    @FXML
    private void onLogOut(ActionEvent event)
    {
        this.eventHandler.onLogOut();

        terminate();

        try
        {
            close();
            Main.logOut();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onCheckWorker(ActionEvent event)
    {
        String selectedWorkerName = this.workersListView.getSelectionModel().getSelectedItem();
        
        if(selectedWorkerName == null)
        {
            this.eventHandler.handleError(eErrorCode.ERROR_WORKER_NOT_SELECTED);
            return;
        }
        
        CDataBaseController dbController = this.eventHandler.getDataBaseController();
        CUser worker = dbController.getUserByName(selectedWorkerName);
        
        if(worker == null)
        {
            this.eventHandler.handleError(eErrorCode.ERROR_OBJECT_NOT_FOUND, selectedWorkerName);
            return;
        }
        else
        {
           openUserViewer(worker);
        }
    }
    
    private void close()
    {
        Stage stage = (Stage) this.addWorkerButton.getScene().getWindow();
        stage.close();
    }
}
