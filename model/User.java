package edu.colostate.cs.cs414.banqi.model;

import java.util.ArrayList;

import edu.colostate.cs.cs414.banqi.controller.Controller;

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