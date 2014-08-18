package org.knitter.giftregistrylite.family;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.persistence.Id;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.User;

import javax.persistence.Entity;


@Entity
public class Family {

	
	
	public User getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}	
	public Key getId() {
		return id;
	}
	public void setId(Key id) {
		this.id = id;
	}
	public static Family getFamily (String name) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    Query qf = new Query("Family");
		Filter f1 = new FilterPredicate("name", FilterOperator.EQUAL, name);
		qf.setFilter(f1);
	    PreparedQuery pq = datastore.prepare(qf);
	    com.google.appengine.api.datastore.Entity family=pq.asSingleEntity();
	    
	    if (family == null)
	    	return null;
	    
	    Family r=new Family();
	    r.id=family.getKey();
	    r.createdBy=(User)family.getProperty("createdBy");
	    r.name=(String)family.getProperty("name");
	    r.token=(String)family.getProperty("token");
	    return r;
	}
	
	public static Family makeFamily(User user, String name) {
	    if (getFamily(name)!=null)
	    	return null;
	    
	    Family r=new Family();
	    r.createdBy=user;
	    r.name=name;
	    r.token=r.generateToken();

	    com.google.appengine.api.datastore.Entity family = new com.google.appengine.api.datastore.Entity("Family");
	    family.setProperty("createdby", r.getCreatedBy());
	    family.setProperty("name", r.getName());
	    family.setProperty("token", r.getToken());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    datastore.put(family);
	    
	    return r;
	}
	
	private SecureRandom random = new SecureRandom();
	private String generateToken() {
		return new BigInteger(40, random).toString(32);
	}
	
	@Id
	private Key id;
	private User createdBy;
	private String name;
	private String token;
}
