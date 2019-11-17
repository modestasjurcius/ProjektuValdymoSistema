/**
 * FXML Controller class
 *
 * @author Modestas
 */
package ValdymoSistema.Views;

import ProjectData.CComment;
import ProjectData.CTask;
import ValdymoSistema.CEventHandler;
import ValdymoSistema.Main;
import static ValdymoSistema.Main.getMainController;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TaskViewerController implements Initializable
{

    private String taskName;

    @FXML
    private Label taskNameLabel;
    @FXML
    private Label completeLevelLabel;
    @FXML
    private Label parentTaskLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private ListView<String> childTasksListView;
    @FXML
    private ListView<String> commentsListView;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.taskName = getMainController().getSelectedTaskName();

        this.taskNameLabel.setText(this.taskName);

        CEventHandler eventHandler = Main.getEventHandler();
        CTask currentTask = eventHandler.getTaskByName(this.taskName);

        if (currentTask == null)
        {
            eventHandler.handleError(CEventHandler.eErrorCode.ERROR_TASK_NOT_SELECTED);
            return;
        }

        this.completeLevelLabel.setText(String.valueOf(currentTask.getCompleteLevel()));

        if (currentTask.hasParentTask())
        {
            this.parentTaskLabel.setText(currentTask.getParentTask().getTaskName());
        }
        else
        {
            this.parentTaskLabel.setText("Nėra");
        }

        this.descriptionLabel.setText(currentTask.getTaskDescription());

        updateChildTasksListView(currentTask);

        for (Object obj : currentTask.getComments())
        {
            CComment com = (CComment) obj;

            this.commentsListView.getItems().add(String.valueOf(com.getId()));
        }
    }

    @FXML
    private void onAddChildTask(ActionEvent event)
    {
        CTask currentTask = Main.getEventHandler().getTaskByName(this.taskName);
        
        try
        {   
            File f = new File("src/ValdymoSistema/Views/TaskCreatDialog.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("src/ValdymoSistema/Views/TaskCreatDialog.fxml"));
            fxmlLoader.setLocation(f.toURI().toURL());
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            fxmlLoader.<TaskCreatDialogController>getController().setParentTask(currentTask);
            stage.show();
            
            stage.setOnHidden(new EventHandler<WindowEvent> (){
            
            @Override
            public void handle(WindowEvent event)
            {
               updateChildTasksListView(currentTask); 
            }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void onRemoveChildTask(ActionEvent event)
    {
    }

    @FXML
    private void onViewChildTask(ActionEvent event)
    {
    }

    @FXML
    private void onAddComment(ActionEvent event)
    {
    }

    @FXML
    private void onRemoveComment(ActionEvent event)
    {
    }

    @FXML
    private void onViewComment(ActionEvent event)
    {
    }

    private void close()
    {
        Stage stage = (Stage) this.childTasksListView.getScene().getWindow();
        stage.close();
    }
    
    public void updateChildTasksListView(CTask currentTask)
    {
        this.childTasksListView.getItems().clear();
        
        for (Object obj : currentTask.getChildTasks())
        {
            CTask childTask = (CTask) obj;

            this.childTasksListView.getItems().add(childTask.getTaskName());
        }
    }
}
