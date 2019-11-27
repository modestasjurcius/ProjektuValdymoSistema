/**
 * FXML Controller class
 *
 * @author Modestas
 */
package ValdymoSistema.Views;

import ValdymoSistema.CDataBaseController;
import ValdymoSistema.CEventHandler;
import ValdymoSistema.Main;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterViewController implements Initializable
{

    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField repeatPasswordField;
    @FXML
    private TextField fullUserNameTextField;
    @FXML
    private TextArea contactsTextArea;
    @FXML
    private Button cancelButton;
    @FXML
    private CheckBox companyProfileCheckBox;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

    }

    @FXML
    private void onRegister(ActionEvent event)
    {
        CEventHandler eHandler = Main.getEventHandler();

        String name = this.userNameTextField.getText();
        String password = this.passwordField.getText();
        String repeatedPassword = this.repeatPasswordField.getText();
        String fullName = this.fullUserNameTextField.getText();
        String contacts = this.contactsTextArea.getText();
        boolean isSingleUser = !this.companyProfileCheckBox.isSelected();

        if (!password.equals(repeatedPassword))
        {
            eHandler.handleError(CEventHandler.eErrorCode.ERROR_PASSWORDS_DONT_MATCH);
            return;
        }

        if (determineIfInfoIsCorrect(name, fullName, password, contacts))
        {
            CDataBaseController dbController = eHandler.getDataBaseController();
            if (dbController.registerUser(name, password, fullName, contacts, isSingleUser))
            {
                eHandler.handleInfo(CEventHandler.eInfoType.INFO_USER_REGISTERED, name);
                close();
            }
        }
        else
        {
            eHandler.handleError(CEventHandler.eErrorCode.ERROR_BAD_INPUT_ON_REGISTER);
        }
    }

    private boolean determineIfInfoIsCorrect(String name, String fullName, String pass, String contacts)
    {
        boolean value = true;

        value &= name.length() > 0 && name.length() < 51;
        value &= fullName.length() > 0 && fullName.length() < 151;
        value &= pass.length() > 0 && pass.length() < 51;
        value &= contacts.length() > 0 && contacts.length() < 101;

        return value;
    }

    @FXML
    private void onCancel(ActionEvent event)
    {
        close();
    }

    private void close()
    {
        Stage stage = (Stage) this.cancelButton.getScene().getWindow();
        stage.close();
    }
}
