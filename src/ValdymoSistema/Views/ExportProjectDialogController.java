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
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ExportProjectDialogController implements Initializable
{

    private CEventHandler eventHandler;

    @FXML
    private Button closeButton;
    @FXML
    private Label projectFileLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.eventHandler = ValdymoSistema.Main.getEventHandler();
        this.projectFileLabel.setText(this.eventHandler.getWorkingProjectSaveFile());
    }

    @FXML
    private void confimProjectExport(ActionEvent event)
    {
        this.eventHandler.exportWorkingProject(true);
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
