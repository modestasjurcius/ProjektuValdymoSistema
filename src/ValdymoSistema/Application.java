/**
 * @author Modestas
 */

package ValdymoSistema;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.parser.ParseException;

public class Application
{
    private static CEventHandler eventHandler;


    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException
    {
        Initialize();
        
        boolean bExit = false;
        
        while (!bExit)
        {
            eventHandler.printMenu();
            
            eventHandler.mainEvent();
        }     
    }
    
    public static void Initialize() throws FileNotFoundException, IOException, ParseException
    {
       eventHandler = new CEventHandler();
    }
}
