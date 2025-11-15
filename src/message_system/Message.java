package message_system;
import java.util.Date;
import java.util.ArrayList;

public abstract class Message
{
    private String sender;
    private String content;
    private Boolean is_read;
    private Date send_date;

    public String getSender() 
    {
        return this.sender;
    }
    public void setSender(String sender) 
    { 
    	if(sender == null)
            throw new IllegalArgumentException("Sender can't be null");
        this.sender = sender; 
    }
    
    public String getContent(){ return this.content; }
    
    public void setContent(String content)
    {
        if(content==null)
            throw new IllegalArgumentException("Content can't be null");
        this.content = content;
    }
    
    public Boolean getIsRead() { return this.is_read; }
    
    public void setIsRead(Boolean is_read)
    {
    	if(is_read != false && is_read != true)
    		throw new IllegalArgumentException("is_read must be a boolean variable");
    	this.is_read = is_read;
    }

    public Date getDate() { return this.send_date; }

    public void setDate(Date send_date)
    {
        if (send_date == null)
            throw new IllegalArgumentException("תאריך לא יכול להיות null");

        Date now = new Date();
        if (send_date.after(now))
            throw new IllegalArgumentException("תאריך השליחה לא יכול להיות בעתיד");

        Date minDate = new Date(0);
        if (send_date.before(minDate))
            throw new IllegalArgumentException("תאריך מוקדם מדי, לא תקין");

        this.send_date = new Date(send_date.getTime());
    }

    
    public Message(String sender, String content, Boolean is_read, Date send_date)
    {
        setSender(sender);
        setContent(content);
        setIsRead(is_read);
        setDate(send_date);
    }
    
    public Message(String sender, String content, Boolean is_read)
    {
        setSender(sender);
        setContent(content);
        setIsRead(is_read);
        this.send_date = new Date();
    }
    
    @Override
    public String toString()
    { return "Sender is:\n" + this.sender + "\nContent is:\n" + this.content + "\nis read?\n" + this.is_read + "\nThe message sent at:\n" +  this.send_date; }
    
    public boolean find(ArrayList<String> words)
    {
        if (words == null || words.isEmpty()) 
            throw new IllegalArgumentException("The list of words cannot be null or empty");
        
        if (this.content == null) 
            return false;
     
        String messageLower = this.content.toLowerCase();
        
        for (String word : words)
            if (word != null && messageLower.contains(word.toLowerCase())) 
                return true; 
        return false;   
    }
    
    public void deleteContent() //מתודה שאנחנו הוספנו
    {
        this.content = "[Deleted message]";
    }

    public abstract String generatePreview(); 
}