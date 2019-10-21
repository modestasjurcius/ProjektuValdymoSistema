/**
 * @author Modestas
 */

package ValdymoSistema;

import java.io.FileNotFoundException;

public class Application
{
    private static CEventHandler eventHandler;


    public static void main(String[] args) throws FileNotFoundException
    {
        Initialize();
        
        boolean bExit = false;
        
        while (!bExit)
        {
            eventHandler.printMenu();
            
            eventHandler.mainEvent();
        }     
    }
    
    public static void Initialize() throws FileNotFoundException
    {
       eventHandler = new CEventHandler();
    }
}
