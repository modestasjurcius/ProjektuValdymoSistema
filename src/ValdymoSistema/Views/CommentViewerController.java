/**
 * FXML Controller class
 *
 * @author Modestas
 */
package ValdymoSistema.Views;

import ProjectData.CComment;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


public class CommentViewerController implements Initializable
{
    private CComment comment;
    @FXML
    private Label dateLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label commentLabel;
    @FXML
    private ListView<String> attachedFilesListView;

    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {}
    
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
}
