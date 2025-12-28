package exe3KnockKnock;
import java.util.Objects;

public class RuppinClient 
{
	private String username;
	private String password;
	private int years_at_ruppin;
	private String academic_status;
	public RuppinClient(String username, String password, int years_at_ruppin, String academic_status)
	{
		this.username = username;
		this.password = password;
		this.years_at_ruppin = years_at_ruppin;
		this.academic_status = academic_status;
	}

	public String getUsername() { return username; }
	public String getPassword() { return password; }
	public int getYearsAtRuppin() { return years_at_ruppin; }
	public String getAcademicStatus() { return academic_status; }
	
	public void setPassword(String password) { this.password = password; }
	public void setYearsAtRuppin(int yearsAtRuppin) { this.years_at_ruppin = yearsAtRuppin; }
	public void setAcademicStatus(String academicStatus) { this.academic_status = academicStatus; }
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RuppinClient)) return false;
        RuppinClient other = (RuppinClient) o;
        return Objects.equals(this.username, other.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}