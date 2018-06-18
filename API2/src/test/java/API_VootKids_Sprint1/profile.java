package API_VootKids_Sprint1;

public class profile {
	
	private String name;
	private String dob;
	private buddy buddy;
	private Preferences preferences;
	private String pin;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDob() {
		return dob;
	}
	public Preferences getPreferences() {
		return preferences;
	}
	public void setPreferences(Preferences preferences) {
		this.preferences = preferences;
	}
	public void setDob(String dob2) {
		this.dob = dob2;
	}
	public buddy getBuddy() {
		return buddy;
	}
	public void setBuddy(buddy buddy) {
		this.buddy = buddy;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin2) {
		this.pin = pin2;
	}
	
	
	

}

