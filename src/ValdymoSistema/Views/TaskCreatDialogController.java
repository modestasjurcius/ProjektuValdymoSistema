/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ValdymoSistema.Views;

import ValdymoSistema.CEventHandler;
import ValdymoSistema.CEventHandler.eErrorCode;
import ValdymoSistema.CEventHandler.eInfoType;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Modestas
 */
public class TaskCreatDialogController implements Initializable
{
    private CEventHandler eventHandler;
    @FXML
    private TextField taskNameTextView;
    @FXML
    private TextField descriptionTextView;
    @FXML
    private Button confirmCreateTask;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.eventHandler = ValdymoSistema.Main.getEventHandler();
    }    

    @FXML
    private void onConfirmCreateTask(ActionEvent event)
    {   
        String taskName = this.taskNameTextView.getText();
        String description = this.descriptionTextView.getText();
        
        if(taskName == null || taskName.length() > 15)
        {
            this.eventHandler.handleError(eErrorCode.ERROR_TOO_LONG_INPUT);
            return;
        }
        
        this.eventHandler.createTask(taskName, description);
        
        this.eventHandler.handleInfo(eInfoType.INFO_TASK_CREATED, taskName);
        
        closeTaskCreator();
    }
    
    private void closeTaskCreator()
    {
        Stage stage = (Stage) this.confirmCreateTask.getScene().getWindow();
        stage.close();
    }
    
}
