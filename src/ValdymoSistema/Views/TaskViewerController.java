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
import ValdymoSistema.CEventHandler;
import ValdymoSistema.Main;
import static ValdymoSistema.Main.getMainController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TaskViewerController implements Initializable
{
    
    private CEventHandler eventHandler;
    
    private CTask currentTask;
    private String taskName;
    
    private boolean isEditorMode;
    
    @FXML
    private Label taskNameLabel;
    @FXML
    private Label completeLevelLabel;
    @FXML
    private Label parentTaskLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private ListView<String> childTasksListView;
    @FXML
    private ListView<String> commentsListView;
    @FXML
    private Button addChildTaskButton;
    @FXML
    private Button removeChildTaskButton;
    @FXML
    private Button viewChildTaskButton;
    @FXML
    private Button addCommentButton;
    @FXML
    private Button removeCommentButton;
    @FXML
    private TextField taskNameTextField;
    @FXML
    private TextField completeLevelTextField;
    @FXML
    private ChoiceBox<String> selectParentTaskChoiceBox;
    @FXML
    private TextArea taskDescriptionTextArea;
    @FXML
    private Label authorLabel;
    @FXML
    private Button viewCommentButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        enableEditorMode(false);
        
        this.eventHandler = Main.getEventHandler();
        
        this.taskName = getMainController().getSelectedTaskName();
        this.taskNameLabel.setText(this.taskName);
        this.currentTask = eventHandler.getTaskByName(this.taskName);
        
        if (this.currentTask == null)
        {
            this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_TASK_NOT_SELECTED);
            return;
        }
        
        updateViewInfo();
    }
    
    private void updateViewInfo()
    {
        if (this.currentTask == null)
        {
            this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_OBJECT_NOT_FOUND, this.taskName);
            return;
        }
        
        this.taskNameLabel.setText(this.taskName);
        this.completeLevelLabel.setText(String.valueOf(this.currentTask.getCompleteLevel()));
        this.descriptionLabel.setText(this.currentTask.getTaskDescription());
        
        CDataBaseController dbController = this.eventHandler.getDataBaseController();
        CUser author = dbController.getUserById(this.currentTask.getAuthorId());
        if(author != null)
        {
            this.authorLabel.setText(author.getUserFullName());
        }
        
        if (this.currentTask.hasParentTask())
        {
            this.parentTaskLabel.setText(this.currentTask.getParentTask().getTaskName());
        }
        else
        {
            this.parentTaskLabel.setText("Nėra");
        }
        
        updateChildTasksListView();
        updateCommentsListView();
        updateParentTasksChoiceBox();
    }
    
    @FXML
    private void onAddChildTask(ActionEvent event)
    {
        try
        {
            File f = new File("src/ValdymoSistema/Views/TaskCreatDialog.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("src/ValdymoSistema/Views/TaskCreatDialog.fxml"));
            fxmlLoader.setLocation(f.toURI().toURL());
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            
            fxmlLoader.<TaskCreatDialogController>getController().setParentTask(this.currentTask);
            stage.show();
            
            stage.setOnHidden(new EventHandler<WindowEvent>()
            {
                @Override
                public void handle(WindowEvent event)
                {
                    updateChildTasksListView();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onRemoveChildTask(ActionEvent event)
    {
        String selectedTaskName = this.childTasksListView.getSelectionModel().getSelectedItem();
        
        if (selectedTaskName == null || selectedTaskName.isEmpty())
        {
            this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_TASK_NOT_SELECTED);
            return;
        }
        
        CTask selectedTask = this.eventHandler.getTaskByName(selectedTaskName);
        
        if (selectedTask == null)
        {
            this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_UNKNOWN);
            return;
        }
        
        this.eventHandler.removeTask(selectedTaskName);
        updateChildTasksListView();
    }
    
    @FXML
    private void onViewChildTask(ActionEvent event) throws IOException
    {
        String selectedTaskName = this.childTasksListView.getSelectionModel().getSelectedItem();
        
        if (selectedTaskName == null || selectedTaskName.isEmpty())
        {
            this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_TASK_NOT_SELECTED);
            return;
        }
        
        Main.getMainController().selectTask(selectedTaskName);
        Main.getMainController().openTaskViewer();
        close();
    }
    
    @FXML
    private void onAddComment(ActionEvent event)
    {
        try
        {
            File f = new File("src/ValdymoSistema/Views/CommentViewer.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("src/ValdymoSistema/Views/CommentViewer.fxml"));
            fxmlLoader.setLocation(f.toURI().toURL());
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
            
            CTask task = this.eventHandler.getTaskByName(this.taskName);
            
            fxmlLoader.<CommentViewerController>getController().setNewCommentMode(task);
            
            stage.setOnHidden(new EventHandler<WindowEvent>()
            {
                @Override
                public void handle(WindowEvent event)
                {
                    updateCommentsListView();
                }
                
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onRemoveComment(ActionEvent event)
    {
        if (this.commentsListView.getSelectionModel().isEmpty())
        {
            this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_COMMENT_NOT_SELECTED);
            return;
        }
        
        int commentId = this.commentsListView.getSelectionModel().getSelectedIndex();
        
        if (this.currentTask == null)
        {
            this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_OBJECT_NOT_FOUND, this.taskName);
            return;
        }
        
        this.currentTask.removeComment(commentId);
        updateCommentsListView();
    }
    
    @FXML
    private void onViewComment(ActionEvent event)
    {
        if (this.commentsListView.getSelectionModel().isEmpty())
        {
            this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_COMMENT_NOT_SELECTED);
            return;
        }
        
        try
        {
            CTask task = this.eventHandler.getTaskByName(this.taskName);
            CComment comment = null;
            
            if (task != null)
            {
                comment = task.getCommentById(this.commentsListView.getSelectionModel().getSelectedIndex());
                if (comment == null)
                {
                    this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_UNKNOWN);
                    return;
                }
            }
            else
            {
                this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_UNKNOWN);
                return;
            }
            
            File f = new File("src/ValdymoSistema/Views/CommentViewer.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("src/ValdymoSistema/Views/CommentViewer.fxml"));
            fxmlLoader.setLocation(f.toURI().toURL());
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            
            fxmlLoader.<CommentViewerController>getController().setComment(comment);
            stage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void close()
    {
        Stage stage = (Stage) this.childTasksListView.getScene().getWindow();
        stage.close();
    }
    
    public void updateChildTasksListView()
    {
        this.childTasksListView.getItems().clear();
        
        for (Object obj : this.currentTask.getChildTasks())
        {
            CTask childTask = (CTask) obj;
            
            this.childTasksListView.getItems().add(childTask.getTaskName());
        }
    }
    
    private void updateCommentsListView()
    {
        this.commentsListView.getItems().clear();
        
        for (CComment com : this.currentTask.getComments())
        {
            this.commentsListView.getItems().add(String.valueOf(com.getId()));
        }
    }
    
    private void updateParentTasksChoiceBox()
    {
        this.selectParentTaskChoiceBox.getItems().clear();
        this.selectParentTaskChoiceBox.getItems().add("Nėra");
        
        ArrayList<CTask> tasks = this.eventHandler.getWorkingProject().getAllTasks();
        
        for (CTask task : tasks)
        {
            String name = task.getTaskName();
            if (!this.currentTask.getTaskName().equals(name))
            {
                this.selectParentTaskChoiceBox.getItems().add(name);
            }
        }
        
        if(this.currentTask.hasParentTask())
        {
            this.selectParentTaskChoiceBox.getSelectionModel().select(this.currentTask.getParentTask().getTaskName());
        }
        else
        {
           this.selectParentTaskChoiceBox.getSelectionModel().selectFirst(); 
        }
    }
    
    private void enableEditorMode(boolean value)
    {
        this.isEditorMode = value;

        //view mode items
        this.authorLabel.setVisible(!value);
        this.taskNameLabel.setVisible(!value);
        this.completeLevelLabel.setVisible(!value);
        this.descriptionLabel.setVisible(!value);
        this.parentTaskLabel.setVisible(!value);
        this.viewChildTaskButton.setVisible(!value);
        this.viewCommentButton.setVisible(!value);

        //editor mode items
        this.addChildTaskButton.setVisible(value);
        this.removeChildTaskButton.setVisible(value);
        this.addCommentButton.setVisible(value);
        this.removeCommentButton.setVisible(value);
        
        this.taskNameTextField.setVisible(value);
        this.completeLevelTextField.setVisible(value);
        this.taskDescriptionTextArea.setVisible(value);
        this.selectParentTaskChoiceBox.setVisible(value);
        
        if (value)
        {
            this.taskNameTextField.setText(this.taskName);
            this.taskDescriptionTextArea.setText(this.currentTask.getTaskDescription());
            this.completeLevelTextField.setText(String.valueOf(this.currentTask.getCompleteLevel()));
        }
    }
    
    @FXML
    private void onEditTask(ActionEvent event)
    {
        enableEditorMode(!this.isEditorMode);
        
        CTask task = this.eventHandler.getTaskByName(this.taskName);
        
        if (task == null)
        {
            this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_OBJECT_NOT_FOUND, this.taskName);
            return;
        }
        
        int completeLevel;
        try
        {
            completeLevel = Integer.valueOf(this.completeLevelTextField.getText());
            
            if (completeLevel < 0 || completeLevel > 100)
            {
                throw new Exception("--Bad complete level input--");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            this.eventHandler.handleError(CEventHandler.eErrorCode.ERROR_INPUT_EXPECTED_NUMERIC,
                    "Užbaigtumo lygio įvestis turi būti sveikas skaičius (0-100)!\n");
            
            return;
        }
        
        task.setCompleteLevel(completeLevel);
        
        this.taskName = this.taskNameTextField.getText();
        task.setTaskName(this.taskName);
        
        task.setTaskDescription(this.taskDescriptionTextArea.getText());
        
        String parentTaskName = this.selectParentTaskChoiceBox.getSelectionModel().getSelectedItem();
        
        if (!parentTaskName.equals("Nėra"))
        {
            task.setParentTask(this.eventHandler.getTaskByName(parentTaskName));
        }
        else
        {
            task.setParentTask(null);
        }
        
        updateViewInfo();
        Main.getMainController().refreshTasksListView();
    }
}
