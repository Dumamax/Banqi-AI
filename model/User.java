
import java.util.ArrayList;

public class User {
	
	private String username, email, password;
	
	public User() {
		//Empty constructor for test purposes
	}
	
	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getUsername() {
		return username;
	}
}