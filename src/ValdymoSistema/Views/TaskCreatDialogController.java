/**
 * FXML Controller class
 *
 * @author Modestas
 */
package ValdymoSistema.Views;

import ProjectData.CTask;
import ValdymoSistema.CEventHandler;
import ValdymoSistema.CEventHandler.eErrorCode;
import ValdymoSistema.CEventHandler.eInfoType;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class TaskCreatDialogController implements Initializable
{
    private CEventHandler eventHandler;
    @FXML
    private TextField taskNameTextView;
    @FXML
    private Button confirmCreateTask;
    
    private CTask parentTask;
    @FXML
    private TextArea descriptionTextArea;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.eventHandler = ValdymoSistema.Main.getEventHandler();
        this.parentTask = null;
    }    

    @FXML
    private void onConfirmCreateTask(ActionEvent event)
    {   
        String taskName = this.taskNameTextView.getText();
        String description = this.descriptionTextArea.getText();
        
        if(taskName == null || taskName.length() > 15)
        {
            this.eventHandler.handleError(eErrorCode.ERROR_TOO_LONG_INPUT);
            return;
        }
        
        this.eventHandler.createTask(taskName, description, null);
        
        if(this.parentTask != null)
        {
            CTask childTask = this.eventHandler.getTaskByName(taskName);
            
            this.parentTask.addChildTask(childTask);
            childTask.setParentTask(this.parentTask);
        }
        
        this.eventHandler.handleInfo(eInfoType.INFO_TASK_CREATED, taskName);
        
        closeTaskCreator();
    }
    
    private void closeTaskCreator()
    {
        Stage stage = (Stage) this.confirmCreateTask.getScene().getWindow();
        stage.close();
    }
    
    public void setParentTask(CTask task)
    {
        this.parentTask = task;
    }
}
