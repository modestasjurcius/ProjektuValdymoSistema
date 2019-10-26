/**
 * @author Modestas
 */
package ProjectData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class CComment
{
    private ArrayList attachedFilesPaths;
    private String comment;
    
    private Date dateOfComment;
    private String dateString;
    
    public CComment(String comment)
    {
        this.comment = comment;
        
        this.attachedFilesPaths = new ArrayList();
        
        Calendar calendar = Calendar.getInstance();
        this.dateOfComment = calendar.getTime();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateString = formatter.format(this.dateOfComment); 
    }

    CComment()
    {
        this.attachedFilesPaths = new ArrayList();
    }
    
    public void setComment(String newComment)
    {
        this.comment = newComment;
    }
    
    public boolean attachFile(String path)
    {     
        this.attachedFilesPaths.add(path);
        
        return true;
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
    
    public String generateCommentOutput()
    {
        String out = "";
        
        out += "\n~~Komentaras : " + this.comment + "\n";
        out += "~~Data : " + this.dateString + "\n";
        out += "~~Prisegti failai : ";
        
        if(this.attachedFilesPaths.isEmpty())
        {
            out += "Nera";
        }
        else
        {
            out += "\n";
            for(Object obj : this.attachedFilesPaths)
            {
                String value = (String) obj;
                out += obj + "\n";
            }
        }
        
        return out;
    }
    
    public void parse(JSONObject data)
    {     
        try
        {
            this.comment = (String) data.get("Comment");
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
}
