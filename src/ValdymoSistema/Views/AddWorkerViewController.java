/**
 * FXML Controller class
 *
 * @author Modestas
 */
package ValdymoSistema.Views;

import ProjectData.CProject;
import ValdymoSistema.CDataBaseController;
import ValdymoSistema.CEventHandler;
import ValdymoSistema.Main;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class AddWorkerViewController implements Initializable
{
    private CEventHandler eventHandler;
    private CDataBaseController dbController;
    
    @FXML
    private ListView<String> freeWorkersListView;
    @FXML
    private Label workerNameTitleLabel;
    @FXML
    private Label workerNameLabel;
    @FXML
    private Button addWorkerButton;
    @Override
    
    public void initialize(URL url, ResourceBundle rb)
    {
        enableWorkerAdd(false);
        
        this.eventHandler = Main.getEventHandler();
        this.dbController = this.eventHandler.getDataBaseController();
        CProject workingProject = this.eventHandler.getWorkingProject();
        
        if(workingProject != null)
        {
            this.freeWorkersListView.setItems(dbController.getFreeWorkersForProject(workingProject));
        }
    }    
    
    @FXML
    private void onClickedOnFreeWorkersListView(MouseEvent event)
    {
        String selectedWorkerName = this.freeWorkersListView.getSelectionModel().getSelectedItem();
        
        if(selectedWorkerName == null)
        {
            enableWorkerAdd(false);
            return;
        }
        else
        {
            this.workerNameLabel.setText(selectedWorkerName);
            enableWorkerAdd(true);
        }
    }
    
    private void enableWorkerAdd(boolean enable)
    {
        this.workerNameLabel.setVisible(enable);
        this.workerNameTitleLabel.setVisible(enable);
        this.addWorkerButton.setVisible(enable);
    }
    
    private void close()
    {
        Stage stage = (Stage) this.addWorkerButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancel(ActionEvent event)
    {
        close();
    }

    @FXML
    private void onAddWorker(ActionEvent event)
    {
        String selectedWorkerName = this.freeWorkersListView.getSelectionModel().getSelectedItem();
        
        if(selectedWorkerName == null)
        {
            this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_WORKER_NOT_SELECTED);
            return;
        }
        
        CProject workingProject = this.eventHandler.getWorkingProject();
        
        if(workingProject != null)
        {
            if(this.dbController.addWorkerToProject(workingProject, selectedWorkerName))
            {
                this.eventHandler.handleInfo(CEventHandler.eInfoType.INFO_WORKER_ADDED_TO_PROJECT, selectedWorkerName);
                this.freeWorkersListView.getItems().remove(selectedWorkerName);
                Main.getMainController().fillProjectWorkersList();
                onClickedOnFreeWorkersListView(null);
            }
            else
            {
                this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_UNKNOWN);
            }
        }
        else
        {
            this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_WORKING_PROJECT_INVALID);
        }
    }
}
