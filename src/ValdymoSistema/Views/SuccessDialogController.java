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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Modestas
 */
public class SuccessDialogController implements Initializable
{

    @FXML
    private Button closeButton;
    @FXML
    private Label messageTextField;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
    }
    
    public void setMessage(String msg)
    {
        this.messageTextField.setText(msg);
    }

    @FXML
    private void onClose(ActionEvent event)
    {
        Stage stage = (Stage) this.closeButton.getScene().getWindow();
        stage.close();
    }
    
}
