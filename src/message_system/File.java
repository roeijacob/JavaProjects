package message_system;
import java.util.Objects;

public class File 
{
	private String fileName;
	private String fileType;
	
	public String getFileName() { return this.fileName; }
	
	public void setFileName(String fileName)
	{
		if(fileName == null)
			throw new IllegalArgumentException("File Name Can not be Null");
		this.fileName = fileName;
	}
	
	public String getFileType() { return this.fileType; }
	
	public void setFileType(String fileType)
	{
		if(fileType == null)
			throw new IllegalArgumentException("File Type Can not be Null");
		this.fileType = fileType;
	}
	
	public File(String fileName, String fileType)
	{
		setFileName(fileName);
		setFileType(fileType);
	}
	
	@Override public String toString() { return "File name: " + this.fileName + ", 1"
			+ "File Type: " + this.fileType; }
	
	public String getExtension() //אנחנו הוספנו
	{
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex == -1 || dotIndex == fileName.length() - 1)
			return "";
		return fileName.substring(dotIndex + 1).toLowerCase();
	}
	
	public boolean isImage() //אנחנו הוספנו
	{
	    String ext = getExtension();
	    return ext.equals("jpg") || ext.equals("png") || ext.equals("jpeg");
	}

	public boolean isDocument() //אנחנו הוספנו
	{
	    String ext = getExtension();
	    return ext.equals("pdf") || ext.equals("docx") || ext.equals("txt");
	}
    @Override public boolean equals(Object o)
    {
        if (this == o) return true;               
        if (!(o instanceof File)) return false;   

        File other = (File) o;
        return Objects.equals(this.fileName, other.fileName) &&
               Objects.equals(this.fileType, other.fileType);
    }

    @Override public int hashCode() 
    {
        return Objects.hash(fileName, fileType);
    }

}


