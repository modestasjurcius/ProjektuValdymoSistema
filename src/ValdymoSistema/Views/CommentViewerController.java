/**
 * FXML Controller class
 *
 * @author Modestas
 */
package ValdymoSistema.Views;

import ProjectData.CComment;
import ProjectData.CTask;
import UserData.CUser;
import ValdymoSistema.CDataBaseController;
import ValdymoSistema.Main;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CommentViewerController implements Initializable
{

    private String addCommentText = "Pridėti";

    private boolean isEditModeEnabled;
    private boolean isNewCommentMode;

    private CTask newCommentTask;

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
    private TextArea commentTextArea;
    @FXML
    private Button addPathButton;
    @FXML
    private Button removePathButton;
    @FXML
    private Label enterPathTitleLabel;
    @FXML
    private TextField enterPathTextField;
    @FXML
    private Button editCommentButton;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        enableEditorMode(false);
        this.isNewCommentMode = false;
        this.comment = null;
        this.newCommentTask = null;
    }

    public void setComment(CComment comment)
    {
        this.comment = comment;

        this.dateLabel.setText(this.comment.getDateString());
        this.commentLabel.setText(this.comment.getComment());

        this.attachedFilesListView.getItems().clear();

        for (String file : comment.getAttachedFiles())
        {
            this.attachedFilesListView.getItems().add(file);
        }

        CDataBaseController dbController = Main.getEventHandler().getDataBaseController();
        CUser author = dbController.getUserById(comment.getAuthorId());

        this.authorLabel.setText(author.getUserFullName());
    }

    @FXML
    private void onAddPath(ActionEvent event)
    {
        String path = this.enterPathTextField.getText();
        this.attachedFilesListView.getItems().add(path);
        this.enterPathTextField.clear();
    }

    @FXML
    private void onRemoveSelectedPath(ActionEvent event)
    {
        if (!this.attachedFilesListView.getSelectionModel().isEmpty())
        {
            int id = this.attachedFilesListView.getSelectionModel().getSelectedIndex();
            this.attachedFilesListView.getItems().remove(id);
        }
    }

    public void setNewCommentMode(CTask commentHolder)
    {
        this.isNewCommentMode = true;
        this.editCommentButton.setText(this.addCommentText);
        enableEditorMode(true);
        this.newCommentTask = commentHolder;

        CUser author = Main.getEventHandler().getCurrentUser();
        this.authorLabel.setText(author.getUserFullName());
    }

    private void enableEditorMode(boolean enable)
    {
        this.isEditModeEnabled = enable;
        //editor items
        this.addPathButton.setVisible(enable);
        this.removePathButton.setVisible(enable);
        this.enterPathTextField.setVisible(enable);
        this.commentTextArea.setVisible(enable);
        this.enterPathTitleLabel.setVisible(enable);

        if (enable)
        {
            if (this.comment != null)
            {
                this.commentTextArea.setText(this.comment.getComment());
            }
        }

        //viewer items
        this.commentLabel.setVisible(!enable);
        this.dateLabel.setVisible(!enable);
    }

    @FXML
    private void onEditComment(ActionEvent event)
    {
        if (!this.isNewCommentMode && !this.isEditModeEnabled)
        {
            enableEditorMode(!this.isEditModeEnabled);
            return;
        }

        String commentText = this.commentTextArea.getText();

        if (this.isNewCommentMode)
        {
            this.comment = new CComment(commentText);
        }
        else
        {
            this.comment.setComment(commentText);
        }

        this.comment.clearAttachedFiles();

        for (String file : this.attachedFilesListView.getItems())
        {
            this.comment.attachFile(file);
        }

        if (this.isNewCommentMode)
        {
            this.newCommentTask.addComment(this.comment);
            close();
        }
        else
        {
            setComment(this.comment);
            enableEditorMode(!this.isEditModeEnabled);
        }
    }

    private void close()
    {
        Stage stage = (Stage) this.editCommentButton.getScene().getWindow();
        stage.close();
    }
}
