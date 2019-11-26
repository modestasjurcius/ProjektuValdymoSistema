/**
 * @author Modestas
 */
package ValdymoSistema;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.simple.parser.ParseException;

public class Main extends Application
{
    private static Stage primStage;
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
        primStage = primaryStage;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        mainController = loader.<MainController>getController();

        try
        {
            File f = new File("src/ValdymoSistema/Views/LoginView.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("src/ValdymoSistema/Views/LoginView.fxml"));
            fxmlLoader.setLocation(f.toURI().toURL());
            Parent root1 = (Parent) fxmlLoader.load();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root1));
            loginStage.show();
            
            loginStage.setOnHidden(new EventHandler<WindowEvent>()
            {
                @Override
                public void handle(WindowEvent event)
                {
                    if(eventHandler.getCurrentUser() != null)
                    {
                        primaryStage.show();  
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException
    {
        eventHandler = new CEventHandler();
        launch(args);
    }
    
    public static void logOut() throws IOException, FileNotFoundException, ParseException
    {
        eventHandler = new CEventHandler();
        restart(primStage);
    }
    
    public static void restart(Stage primaryStage) throws IOException
    {

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        mainController = loader.<MainController>getController();

        try
        {
            File f = new File("src/ValdymoSistema/Views/LoginView.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getClassLoader().getResource("src/ValdymoSistema/Views/LoginView.fxml"));
            fxmlLoader.setLocation(f.toURI().toURL());
            Parent root1 = (Parent) fxmlLoader.load();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root1));
            loginStage.show();
            
            loginStage.setOnHidden(new EventHandler<WindowEvent>()
            {
                @Override
                public void handle(WindowEvent event)
                {
                    if(eventHandler.getCurrentUser() != null)
                    {
                        primaryStage.show();  
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
