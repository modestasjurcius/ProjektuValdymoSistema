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

        mainController = loader.<MainController>getController();

        try
        {
            File f = new File("src/ValdymoSistema/Views/LoginView.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("src/ValdymoSistema/Views/LoginView.fxml"));
            fxmlLoader.setLocation(f.toURI().toURL());
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
            
            stage.setOnHidden(new EventHandler<WindowEvent>()
            {
                @Override
                public void handle(WindowEvent event)
                {
                    primaryStage.show();
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
}
