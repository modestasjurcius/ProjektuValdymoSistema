/**
 * FXML Controller class
 *
 * @author Modestas
 */
package ValdymoSistema.Views;

import ValdymoSistema.CEventHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ExportProjectDialogController implements Initializable
{

    private CEventHandler eventHandler;

    @FXML
    private TextField projectNameTextField;
    @FXML
    private Button closeButton;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.eventHandler = ValdymoSistema.Main.getEventHandler();
    }

    @FXML
    private void confimProjectExport(ActionEvent event)
    {
        String projectName = this.projectNameTextField.getText();

        if (projectName == null || projectName.isEmpty())
        {
            this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_MISSING_INPUT);
            return;
        } else if (projectName.length() > 15)
        {
            this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_TOO_LONG_INPUT);
            return;
        }

        this.eventHandler.exportProject(projectName);
        closeDialog();
    }

    @FXML
    private void cancelProjectExport(ActionEvent event)
    {
        closeDialog();
    }

    private void closeDialog()
    {
        Stage stage = (Stage) this.closeButton.getScene().getWindow();
        stage.close();
    }
}
