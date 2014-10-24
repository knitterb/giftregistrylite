package org.blandsite;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

public class Token {

	@Persistent
	@PrimaryKey
	private String key;
	
	@Persistent
	private Date created;
	@Persistent
	private Date expiration;
	@Persistent
	private String username;
	
	public static final long DEFAULT_EXPIRATION = 60*60*24*90; // 90 days in seconds
	private static SecureRandom srand = new SecureRandom();
	
	public static Token generate(User u) {
		Token t=new Token();
		t.created = new Date();
		t.expiration = new Date(t.created.getTime() + DEFAULT_EXPIRATION);
		t.username = u.getUsername();
		t.key = new BigInteger(130, srand).toString(32);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(t);
		pm.close();
		
		return t;
	}
	
	
}
