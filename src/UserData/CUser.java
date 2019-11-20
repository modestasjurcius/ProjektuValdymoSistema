/**
 * @author Modestas
 */
package UserData;

public class CUser
{

    private String userName;
    private int id;

    public CUser(String name, int id)
    {
        this.userName = name;
        this.id = id;
    }

    public String getUserName()
    {
        return this.userName;
    }

    public int getId()
    {
        return this.id;
    }
}
