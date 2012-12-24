package com.s5games.mud.beans;

/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
public class Account {
	private Long id;
	private String userName;
	private String password;

    public Account() {
	}
	
	public Long getId() {
		return id;
	}
	public String getPassword() {
		return password;
	}
	public String getUserName() {
		return userName;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
