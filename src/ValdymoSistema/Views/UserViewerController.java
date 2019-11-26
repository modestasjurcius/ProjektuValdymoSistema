/**
 * FXML Controller class
 *
 * @author Modestas
 */
package ValdymoSistema.Views;

import UserData.CUser;
import ValdymoSistema.CEventHandler;
import ValdymoSistema.Main;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


public class UserViewerController implements Initializable
{

    @FXML
    private Label singleUserNameLabel;
    @FXML
    private Label companyUserNameLabel;
    @FXML
    private Label singleUserContacs;
    @FXML
    private Label companyContactLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label fullUserNameLabel;
    @FXML
    private Label userContactsLabel;
    @FXML
    private ListView<String> projectsOwnerListView;
    @FXML
    private ListView<String> projectsWorkerListView;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        setSingleUserViewMode(false);
    }    
    
    public void setUser(CUser user)
    {
        if(user == null)
        {
            return;
        }
        
        this.userNameLabel.setText(user.getUserName());
        this.userContactsLabel.setText(user.getUserContacts());
        this.fullUserNameLabel.setText(user.getUserFullName());
        
        setSingleUserViewMode(user.isSingleUser());
        
        CEventHandler eHandler = Main.getEventHandler();
        
        Map workingProjects = eHandler.getSavedProjects(user, true, false);
        Map owningProjects  = eHandler.getSavedProjects(user, false, true);
        
        this.projectsOwnerListView.getItems().clear();
        this.projectsWorkerListView.getItems().clear();
        
        fillListView(workingProjects, this.projectsWorkerListView);
        fillListView(owningProjects, this.projectsOwnerListView);
    }
    
    private void setSingleUserViewMode(boolean enable)
    {
        this.singleUserNameLabel.setVisible(enable);
        this.singleUserContacs.setVisible(enable);
        
        this.companyUserNameLabel.setVisible(!enable);
        this.companyContactLabel.setVisible(!enable);
    }
    
    private void fillListView(Map map, ListView list)
    {
        for(Object obj : map.keySet())
        {
            String key = (String) obj;
            list.getItems().add(key);
        }
    }
}
