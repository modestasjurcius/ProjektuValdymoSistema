/**
 * @author Modestas
 */
package UserData;

public class CUser
{

    private String userName;
    private String fullUserName;
    private String userContacts;
    private boolean isSingleUser;
    private int id;

    public CUser(String name, int id, boolean isSingleUser, String fullName, String contacts)
    {
        this.userName = name;
        this.id = id;
        this.isSingleUser = isSingleUser;
        this.fullUserName = fullName;
        this.userContacts = contacts;
    }

    public String getUserName()
    {
        return this.userName;
    }

    public int getId()
    {
        return this.id;
    }
    
    public String getUserFullName()
    {
        return this.fullUserName;
    }
    
    public String getUserContacts()
    {
        return this.userContacts;
    }
    
    public boolean isSingleUser()
    {
        return this.isSingleUser;
    }
}
