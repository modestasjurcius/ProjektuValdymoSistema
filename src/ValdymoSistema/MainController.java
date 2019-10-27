/**
 * FXML Controller class
 *
 * @author Modestas
 */
package ValdymoSistema;

import ProjectData.CTask;
import ValdymoSistema.CEventHandler.eErrorCode;
import ValdymoSistema.CEventHandler.eEventType;
import ValdymoSistema.Views.ProjectImporterDialogController;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;


public class MainController implements Initializable
{
    private CEventHandler eventHandler;
    
    @FXML
    private Button createProjectButton;
    @FXML
    private Label workingProjectName;
    @FXML
    private ListView<String> tasksListView;
    @FXML
    private TabPane projectInfoTabPane;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.eventHandler = ValdymoSistema.Main.getEventHandler();
        this.workingProjectName.setText("Nepasirinktas");
        
        this.tasksListView.setItems(FXCollections.observableArrayList());
    }    

    @FXML
    private void createProjectAction(ActionEvent event)
    {
        try
        {
            File f = new File("src/ValdymoSistema/Views/CreateProjectDialog.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("src/ValdymoSistema/Views/CreateProjectDialog.fxml"));
            fxmlLoader.setLocation(f.toURI().toURL());
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void openProjectAction(ActionEvent event)
    {
        if(!this.eventHandler.hasSavedProjects())
        {
            this.eventHandler.handleError(eErrorCode.ERROR_UNKNOWN);
            return;
        }
        
        try
        {
            File f = new File("src/ValdymoSistema/Views/ProjectImporterDialog.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("src/ValdymoSistema/Views/ProjectImporterDialog.fxml"));
            fxmlLoader.setLocation(f.toURI().toURL());
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            
            fxmlLoader.<ProjectImporterDialogController>getController().setSavedProjectList(eventHandler.getSavedProjects());
            stage.show();
        } catch (Exception e)
        {
            e.printStackTrace();
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
    
    public void clearTaskList()
    {
        this.tasksListView.getItems().clear();
    }

    @FXML
    private void createTaskAction(ActionEvent event)
    {
        if(!this.eventHandler.isWorkingProjectValid())
        {
            this.eventHandler.handleError(eErrorCode.ERROR_WORKING_PROJECT_INVALID);
            return;
        }
        
        try
        {
            File f = new File("src/ValdymoSistema/Views/TaskCreatDialog.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("src/ValdymoSistema/Views/TaskCreatDialog.fxml"));
            fxmlLoader.setLocation(f.toURI().toURL());
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
