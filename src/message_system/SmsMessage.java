package message_system;
import java.util.Date;

public class SmsMessage extends Message implements IDigital {

    private String phone;

    public SmsMessage(String sender, String content, boolean isRead, Date date, String phone) throws SmsException 
    {
        super(sender, content, isRead, date);
        setPhone(phone);
    }

    public SmsMessage(String sender, String content, boolean isRead, String phone) throws SmsException 
    {
        super(sender, content, isRead, new Date());
        setPhone(phone);
    }

    public void setPhone(String phone) throws SmsException 
    {
        if (phone == null || phone.isBlank())
            throw new SmsException("Phone number can't be empty");
        this.phone = phone;
    }

    public String getPhone() { return phone; }

    @Override public String toString() {
        return super.toString() + ", phone=" + phone;
    }

    @Override public String printCommunicationMethod() { return "Sent via SMS"; }

    @Override public String generatePreview()
    {
        String txt = getContent();
        if (txt.length() > 20)
            txt = txt.substring(0, 20) + "...";
        return "[SMS] " + getSender() + ": " + txt;
    }
}
