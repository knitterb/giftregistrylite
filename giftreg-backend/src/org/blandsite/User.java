package org.blandsite;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class User {
	
	@Persistent
	@PrimaryKey
	private String username;
	
	@Persistent
	private String full_name;
	@Persistent
	private String email_address;
	@Persistent
	private Date birthday; // passed as: 1985-04-12T23:20:50.520Z
	
	@Persistent
	private String password_hash="//";
	@Persistent
	private String password_salt;
	
	@Persistent
	private Set<String> user_gifts;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	public String getEmail_address() {
		return email_address;
	}
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public void addGift(Gift gift) {
		if (this.user_gifts == null) {
			this.user_gifts = new HashSet<String>();
		}
		user_gifts.add(gift.getKey());
		gift.setUsername(this.getUsername());
	}

	public void setPassword(String p) {
		this.password_hash=p;
		this.password_salt=p;
	}
	
	
}
