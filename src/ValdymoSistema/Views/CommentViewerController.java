/**
 * FXML Controller class
 *
 * @author Modestas
 */
package ValdymoSistema.Views;

import ProjectData.CComment;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class CommentViewerController implements Initializable
{
    private boolean isEditModeEnabled;
    private CComment comment;
    @FXML
    private Label dateLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label commentLabel;
    @FXML
    private ListView<String> attachedFilesListView;
    @FXML
    private ChoiceBox<String> authorCheckBox;
    @FXML
    private TextArea commentTextArea;
    @FXML
    private Button addPathButton;
    @FXML
    private Button removePathButton;
    @FXML
    private Label enterPathTitleLabel;
    @FXML
    private TextField enterPathTextField;

    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        enableEditorMode(false);
    }
    
    public void setComment(CComment comment)
    {
        this.comment = comment;
        
        this.dateLabel.setText(this.comment.getDateString());
        this.commentLabel.setText(this.comment.getComment());
        
        for(String file : comment.getAttachedFiles())
        {
            this.attachedFilesListView.getItems().add(file);
        }
    }

    @FXML
    private void onAddPath(ActionEvent event)
    {
    }

    @FXML
    private void onRemoveSelectedPath(ActionEvent event)
    {
    }
    
    private void enableEditorMode(boolean enable)
    {
        this.isEditModeEnabled = enable;
        //editor items
        this.authorCheckBox.setVisible(enable);
        this.addPathButton.setVisible(enable);
        this.removePathButton.setVisible(enable);
        this.enterPathTextField.setVisible(enable);
        this.commentTextArea.setVisible(enable);
        this.enterPathTitleLabel.setVisible(enable);
        
        //viewer items
        this.authorLabel.setVisible(!enable);
        this.commentLabel.setVisible(!enable);
        this.dateLabel.setVisible(!enable);
    }

    @FXML
    private void onEditComment(ActionEvent event)
    {
        enableEditorMode(!this.isEditModeEnabled);
    }
}
