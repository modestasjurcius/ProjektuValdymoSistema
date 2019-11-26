/**
 * @author Modestas
 */
package ProjectData;

import UserData.CUser;
import ValdymoSistema.CEventHandler;
import ValdymoSistema.Main;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class CComment
{
    private CEventHandler eventHandler;
    private ArrayList<String> attachedFilesPaths;
    private String comment;
    private int id;
    private int authorId;
    
    private Date dateOfComment;
    private String dateString;
    
    public CComment(String comment)
    {
        this.comment = comment;
        this.id = -1;
        this.attachedFilesPaths = new ArrayList<String>();
        
        Calendar calendar = Calendar.getInstance();
        this.dateOfComment = calendar.getTime();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateString = formatter.format(this.dateOfComment); 
        
        this.eventHandler = Main.getEventHandler();
        CUser author = this.eventHandler.getCurrentUser();
        
        this.authorId  = author.getId();
    }

    CComment()
    {
        this.attachedFilesPaths = new ArrayList<String>();
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getAuthorId()
    {
        return this.authorId;
    }
    
    public void setComment(String newComment)
    {
        this.comment = newComment;
    }
    
    public String getComment()
    {
        return this.comment;
    }
    
    public boolean attachFile(String path)
    {     
        this.attachedFilesPaths.add(path);
        
        return true;
    }
    
    public void clearAttachedFiles()
    {
        this.attachedFilesPaths.clear();
    }
    
    public ArrayList<String> getAttachedFiles()
    {
        return this.attachedFilesPaths;
    }
    
    public boolean removeFile(String path)
    {
        if(this.attachedFilesPaths.contains(path))
        {
            this.attachedFilesPaths.remove(path);
            return true;
        }
        return false;
    }
    
    public void setDate(Date date)
    {
        if(this.dateOfComment == null)
        {
            this.dateOfComment = new Date();
        }
        this.dateOfComment = date;
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateString = formatter.format(this.dateOfComment); 
    }
    
    public String getDateString()
    {
        return this.dateString;
    }
    
    public void parse(JSONObject data)
    {     
        try
        {
            this.authorId = ((Long) data.get("CommentAuthorId")).intValue();
            this.comment = (String) data.get("Comment");
            this.id = ((Long) data.get("ID")).intValue();
            setDate(new Date((long) data.get("Date")));

            JSONArray attachedFiles = (JSONArray) data.get("AttachedFiles");

            if (attachedFiles != null && !attachedFiles.isEmpty())
            {
                for (Object obj : attachedFiles)
                {
                    this.attachedFilesPaths.add((String) obj);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public JSONObject generateExportJson()
    {
        JSONObject data = new JSONObject();
        
        data.put("CommentAuthorId", this.authorId);
        data.put("Comment", this.comment);
        data.put("ID", this.id);
        data.put("Date", this.dateOfComment.getTime());
        
        if(this.attachedFilesPaths.size() > 0)
        {
            JSONArray attachedFiles = new JSONArray();
            
            for(Object obj : this.attachedFilesPaths)
            {
                attachedFiles.add((String) obj);
            }
            
            data.put("AttachedFiles", attachedFiles);
        }
        
        return data;
    }
}
