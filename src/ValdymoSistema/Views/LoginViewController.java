/**
 * FXML Controller class
 *
 * @author Modestas
 */
package ValdymoSistema.Views;

import UserData.CUser;
import ValdymoSistema.CDataBaseController;
import ValdymoSistema.Main;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginViewController implements Initializable
{

    private CDataBaseController dbController;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField loginTextField;
    @FXML
    private Label infoLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.infoLabel.setVisible(false);
        this.dbController = Main.getEventHandler().getDataBaseController();
    }

    @FXML
    private void onLogin(ActionEvent event)
    {
        this.dbController = Main.getEventHandler().getDataBaseController();
        
        String login = this.loginTextField.getText();
        String pass = this.passwordField.getText();
        
        CUser user = this.dbController.getUser(login, pass);
        
        if(user != null)
        {
            Main.getEventHandler().setCurrentUser(user);
            close();
        }
        else
        {
            this.infoLabel.setText("Blogas vartotojo vardas arba slaptažodis!");
            this.infoLabel.setVisible(true);
        }
        
        clearFields();
    }

    @FXML
    private void onRegister(ActionEvent event)
    {
    }
    
    private void close()
    {
        Stage st = (Stage) this.infoLabel.getScene().getWindow();
        st.close();
    }
    
    private void clearFields()
    {
        this.loginTextField.clear();
        this.passwordField.clear();
    }
}
