package message_system;
import java.util.Date;

public class BoardMessage extends Message
{
	private PriorityType priority;
	
    public BoardMessage(String sender, String content, Boolean is_read, Date send_date, PriorityType priority)
    {
    	super(sender, content, is_read, send_date);
    	this.priority = priority;
    }
    
    public BoardMessage(String sender, String content, Boolean is_read)
    {
    	super(sender, content, is_read, new Date());
    	this.priority = PriorityType.REGULAR;
    	
    }
    
    @Override public String generatePreview()
    {
        String text = getContent();
        if(text.length() <= 15)
        	return "[Board] " + getSender() + ": " + "\n" + text;
        String shortText = text.substring(0, 15) + "...";
    	return "[Board] " + getSender() + ": " +  "\n" + shortText;
    }
    
    @Override public String toString()
    {
    	 return super.toString() + "\nPriority Type:\n" + this.priority;
    }
    
    public void markAsUrgent() //אנחנו הוספנו
    {
        this.priority = PriorityType.URGENT;
    }

}
