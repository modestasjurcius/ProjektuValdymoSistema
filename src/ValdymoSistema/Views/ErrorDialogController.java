/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ValdymoSistema.Views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author Modestas
 */
public class ErrorDialogController implements Initializable
{
    private Stage errorStage;
    @FXML
    private Label errorMessage;
    @FXML
    private Button closeButton;

    /**
     * Initializes the controller class.
     */
    @Override
    
    public void initialize(URL url, ResourceBundle rb)
    {
    }
    
    public void setErrorMessage(String message)
    {
        this.errorMessage.setText(message);
    }

    @FXML
    private void onErrorBackAction(ActionEvent event)
    {
        closeErrorDialog();
    }
    
    private void closeErrorDialog()
    {
        Stage stage = (Stage) this.closeButton.getScene().getWindow();
        stage.close();
    }
    
    public void showErrorDialog()
    {
        Stage stage = (Stage) this.closeButton.getScene().getWindow();
        stage.show();
    }
}
