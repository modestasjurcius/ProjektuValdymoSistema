/**
 * FXML Controller class
 *
 * @author Modestas
 */
package ValdymoSistema.Views;

import UserData.CUser;
import ValdymoSistema.CDataBaseController;
import ValdymoSistema.CEventHandler;
import ValdymoSistema.Main;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UserViewerController implements Initializable
{

    private CUser user;
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
    @FXML
    private Button editUserInfoButton;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField fullUserNameTextField;
    @FXML
    private TextArea userContactInfoTextArea;
    @FXML
    private Button confirmEditButton;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        setSingleUserViewMode(false);
        enableEditorMode(false);
    }

    public void setUser(CUser user)
    {
        if (user == null)
        {
            return;
        }

        this.user = user;
        
        updateInfo();
    }
    
    private void updateInfo()
    {
        this.userNameLabel.setText(this.user.getUserName());
        this.userContactsLabel.setText(this.user.getUserContacts());
        this.fullUserNameLabel.setText(this.user.getUserFullName());

        setSingleUserViewMode(this.user.isSingleUser());

        CEventHandler eHandler = Main.getEventHandler();

        determineCanEditUserInfo(eHandler, this.user);

        Map workingProjects = eHandler.getSavedProjects(this.user, true, false);
        Map owningProjects = eHandler.getSavedProjects(this.user, false, true);

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
        for (Object obj : map.keySet())
        {
            String key = (String) obj;
            list.getItems().add(key);
        }
    }

    private void enableEditorMode(boolean enable)
    {
        //editor mode items
        this.confirmEditButton.setVisible(enable);
        this.fullUserNameTextField.setVisible(enable);
        this.userContactInfoTextArea.setVisible(enable);
        this.userNameTextField.setVisible(enable);

        //view mode items
        this.editUserInfoButton.setVisible(!enable);
        this.fullUserNameLabel.setVisible(!enable);
        this.userContactsLabel.setVisible(!enable);
        this.userNameLabel.setVisible(!enable);

        if (enable)
        {
            this.fullUserNameTextField.setText(this.fullUserNameLabel.getText());
            this.userContactInfoTextArea.setText(this.userContactsLabel.getText());
            this.userNameTextField.setText(this.userNameLabel.getText());
        }
    }

    private void determineCanEditUserInfo(CEventHandler eHandler, CUser user)
    {
        CUser loggedUser = eHandler.getCurrentUser();

        if (user != loggedUser)
        {
            this.editUserInfoButton.setVisible(false);
        }
    }

    @FXML
    private void onEditUserInfo(ActionEvent event)
    {
        enableEditorMode(true);
    }

    @FXML
    private void onConfirmEditUserInfo(ActionEvent event)
    {
        CEventHandler eHandler = Main.getEventHandler();

        String editedName = this.userNameTextField.getText();
        String editedFullName = this.fullUserNameTextField.getText();
        String editedContacts = this.userContactInfoTextArea.getText();

        if (!determineEditInfoCorrection(editedName, editedFullName, editedContacts))
        {
            eHandler.handleError(CEventHandler.eErrorCode.ERROR_MISSING_INPUT);
            return;
        }

        if (!determineIfEditedInfoIsSame(editedName, editedFullName, editedContacts))
        {
            CDataBaseController dbController = eHandler.getDataBaseController();
            
            int userId = this.user.getId();
            boolean isSingleUser = this.user.isSingleUser();
            
            if(dbController.updateUserInfo(userId, editedName, editedFullName, editedContacts))
            {
                this.user = new CUser(editedName, userId, isSingleUser, editedFullName, editedContacts);
                eHandler.setCurrentUser(this.user);
                setUser(this.user);
            }
            else
            {
                eHandler.handleError(CEventHandler.eErrorCode.ERROR_UNKNOWN);
            }
        }
        
        enableEditorMode(false);
    }

    private boolean determineEditInfoCorrection(String name, String fullName, String contacts)
    {
        boolean value = true;

        value &= name.length() > 0 && name.length() < 51;
        value &= fullName.length() > 0 && fullName.length() < 151;
        value &= contacts.length() > 0 && contacts.length() < 101;

        return value;
    }

    private boolean determineIfEditedInfoIsSame(String name, String fullName, String contacts)
    {
        boolean value;

        boolean sameName = this.user.getUserName().equals(name);
        boolean sameFullName = this.user.getUserFullName().equals(fullName);
        boolean sameContacts = this.user.getUserContacts().equals(contacts);

        value = sameName && sameFullName && sameContacts;

        return value;
    }
}
