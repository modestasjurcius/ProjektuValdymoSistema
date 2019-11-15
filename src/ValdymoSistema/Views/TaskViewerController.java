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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


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
        
        if(currentTask == null)
        {
            eventHandler.handleError(CEventHandler.eErrorCode.ERROR_UNKNOWN);
            return;
        }
        
        this.completeLevelLabel.setText(String.valueOf(currentTask.getCompleteLevel()));
        
        if(currentTask.hasParentTask())
        {
            this.parentTaskLabel.setText(currentTask.getParentTask().getTaskName());
        }
        else
        {
            this.parentTaskLabel.setText("Nėra");
        }
        
        this.descriptionLabel.setText(currentTask.getTaskDescription());
        
        for(Object obj : currentTask.getChildTasks())
        {
            CTask childTask = (CTask) obj;
            
            this.childTasksListView.getItems().add(childTask.getTaskName());
        }
        
        for(Object obj : currentTask.getComments())
        {
            CComment com = (CComment) obj;
            
            this.commentsListView.getItems().add(String.valueOf(com.getId()));
        }
    }    
    
}
