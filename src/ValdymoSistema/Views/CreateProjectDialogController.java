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
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Modestas
 */
public class CreateProjectDialogController implements Initializable
{
    @FXML 
    private TextField projectNameTextField;
    private CEventHandler eventHandler;
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
        }
    }

    @FXML
    private void cancelProjectCreation(ActionEvent event)
    {
    } 
}
