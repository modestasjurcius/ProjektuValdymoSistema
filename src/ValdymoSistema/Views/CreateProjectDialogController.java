/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ValdymoSistema.Views;

import ValdymoSistema.CEventHandler;
import ValdymoSistema.CEventHandler.eErrorCode;
import static ValdymoSistema.Main.getEventHandler;
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
public class CreateProjectDialogController implements Initializable
{
    private CEventHandler eventHandler;
    
    @FXML 
    private TextField projectNameTextField; 
    @FXML
    private Button closeButton;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.eventHandler = ValdymoSistema.Main.getEventHandler();
    }    

    @FXML
    private void confimProjectCreation(ActionEvent event)
    {
        String projectName = this.projectNameTextField.getText();
        
        if(projectName == null || projectName.isEmpty())
        {
            this.eventHandler.handleError(eErrorCode.ERROR_MISSING_INPUT);
            return;
        }
        else if(projectName.length() > 15)
        {
            this.eventHandler.handleError(eErrorCode.ERROR_TOO_LONG_INPUT);
            return;
        }
        
        this.eventHandler.createProject(projectName);
        closeProjectCreationDialog();
    }

    @FXML
    private void cancelProjectCreation(ActionEvent event)
    {
        closeProjectCreationDialog();
    }
    
    private void closeProjectCreationDialog()
    {
       Stage stage = (Stage) this.closeButton.getScene().getWindow();
       stage.close(); 
    }
}
