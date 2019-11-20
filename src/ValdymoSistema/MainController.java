/**
 * FXML Controller class
 *
 * @author Modestas
 */
package ValdymoSistema;

import ProjectData.CTask;
import ValdymoSistema.CEventHandler.eErrorCode;
import ValdymoSistema.Views.ProjectImporterDialogController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
    private Label workingProjectName;
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

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.eventHandler = ValdymoSistema.Main.getEventHandler();
        this.workingProjectName.setText("Nepasirinktas");

        this.taskViewerButton.setVisible(false);
        this.taskRemoverButton.setVisible(false);

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

            if (this.eventHandler.hasSavedProjects())
            {
                fxmlLoader.<ProjectImporterDialogController>getController().setSavedProjectList(eventHandler.getSavedProjects());
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

        if (!checkValue.equals(this.selectedTaskName))
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
        this.workingProjectName.setText(name);
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

    public void refreshTasksListView()
    {
        this.tasksListView.getItems().clear();

        for (CTask task : this.eventHandler.getAllWorkingProjectTasks())
        {
            this.tasksListView.getItems().add(task.getTaskName());
        }
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
    }
    
    public void setUserName(String name)
    {
        this.userNameLabel.setText(name);
    }
}
