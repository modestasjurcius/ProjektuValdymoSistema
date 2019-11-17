/**
 * FXML Controller class
 *
 * @author Modestas
 */
package ValdymoSistema.Views;

import ValdymoSistema.CEventHandler;
import ValdymoSistema.Main;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;


public class ProjectImporterDialogController implements Initializable
{

    private CEventHandler eventHandler;
    @FXML
    private ListView<String> savedProjectsListView;
    private String selectedProjectName;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.eventHandler = Main.getEventHandler();
        this.savedProjectsListView.setOnMouseClicked(new EventHandler<MouseEvent>()
        {

            @Override
            public void handle(MouseEvent event)
            {
                if (event.getClickCount() == 2)
                {
                    selectedProjectName = savedProjectsListView.getSelectionModel().getSelectedItem();

                    try
                    {
                        eventHandler.importProject(selectedProjectName);
                    } catch (IOException ex)
                    {
                        Logger.getLogger(ProjectImporterDialogController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex)
                    {
                        Logger.getLogger(ProjectImporterDialogController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    eventHandler.handleInfo(CEventHandler.eInfoType.INFO_PROJECT_IMPORTED, selectedProjectName);
                    closeProjectImporter();

                    return;
                }
            }
        });
    }

    public String getSelectedProjectName()
    {
        return this.selectedProjectName;
    }

    public void setSavedProjectList(Map map)
    {
        for (Object name : map.keySet())
        {
            this.savedProjectsListView.getItems().add((String) name);
        }
    }

    private void closeProjectImporter()
    {
        Stage stage = (Stage) this.savedProjectsListView.getScene().getWindow();
        stage.close();
    }
}
