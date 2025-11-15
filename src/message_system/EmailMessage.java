package message_system;
import java.util.ArrayList;
import java.util.Date;

public class EmailMessage extends Message implements IDigital 
{
	private String subject;
	private ArrayList<File> attachments;
	
	public String getSubject() { return this.subject; }
	
	public void setSubject(String subject)
	{
		if(subject == null)
	        throw new IllegalArgumentException("The Email Subject Can Not BE Null");
		this.subject = subject;	
	}
	
	public ArrayList<File> getAttachments() { return this.attachments; }
	
	public EmailMessage(String sender, String content, Boolean is_read, Date send_date, String subject, ArrayList<File> attachments)
	{
		super(sender, content, is_read, send_date);
		setSubject(subject);
		this.attachments = (attachments != null) ? attachments : new ArrayList<>();
	}
	
	public EmailMessage(String sender, String content, Boolean is_read, String subject)
	{
		super(sender, content, is_read, new Date());
		setSubject(subject);
		this.attachments = new ArrayList<>();
	}
	
	@Override public String toString() 
	{
		return super.toString() + "\n" +
			       "Subject: " + this.subject + "\n" +
			       "Attachments: " +
			       (attachments.isEmpty() ? "None" : this.attachments);
	}
	
    @Override public String generatePreview()
    {
        String text = getContent();
        if(text.length() <= 15)
        	return "[Email] Subject:" + this.subject + " | " + "From: " + getSender() + "\n" + text;
        String shortText = text.substring(0, 15) + "...";
    	return "[Email] Subject:" + this.subject + " | " + "From: " + getSender() + "\n" + shortText;
    }
    
    public void addAttachment(File file) throws AttachmentException
    {
        if (file == null)
            throw new IllegalArgumentException("Attachment Can Not Be Null");
        if (attachments == null) 
            attachments = new ArrayList<>();
        attachments.add(file);
    }
    
    public void removeAttachment(File target) throws AttachmentException
    {
        if (target == null)
            throw new IllegalArgumentException("Attachment cannot be null");

        if (this.attachments == null || this.attachments.isEmpty())
            throw new AttachmentException("The email does not contain any files");

        boolean removed = this.attachments.remove(target);

        if (!removed)
            throw new AttachmentException("No matching attachment found.");
        
        else
        	System.out.print("The file has been removed");
    }
    
    @Override public String printCommunicationMethod()
    {
        return "Sent via Email Server";
    }
}
