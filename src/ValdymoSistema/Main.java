/**
 * @author Modestas
 */
package ValdymoSistema;

import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

public class Main extends Application
{
    private static CEventHandler eventHandler;
    private static MainController mainController;
    
    public static CEventHandler getEventHandler()
    {
        return eventHandler;
    }
    
    public static MainController getMainController()
    {
        return mainController;
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        mainController = loader.<MainController>getController();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException
    {
        eventHandler = new CEventHandler();
        launch(args);
    } 
}
