package org.knitter.giftregistrylite.family;

import javax.persistence.Id;

import org.datanucleus.api.jpa.annotations.Extension;

import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.datanucleus.api.jpa.annotations.Extension;


@Entity
public class FamilyMember {
	
	public Family getFamily() {
		return family;
	}

	public void setFamily(Family family) {
		this.family = family;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}


	public static FamilyMember getFamilyMember(User user) throws NotFoundException {
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query("FamilyMember");
		Filter f = new FilterPredicate("user", FilterOperator.EQUAL, user);
	    q.setFilter(f);
		PreparedQuery pq = datastore.prepare(q);
		com.google.appengine.api.datastore.Entity fm=pq.asSingleEntity();
		assert(fm!=null);
		if (fm == null)
			throw new NotFoundException("Cannot find a family member for user: "+user+".  Visit website to create or join family.");
		
		FamilyMember rfm = new FamilyMember();
		rfm.setId(fm.getKey());
		rfm.setName((String)fm.getProperty("name"));
		rfm.setUser(user);
		rfm.parentID=fm.getParent();
		
		return rfm;
	}

	@Id
	private Key id;
	private Family family;
	private User user;
	private String name;
	
	@Extension(vendorName = "datanucleus", key = "gae.parent-pk", value ="true")
	public Key parentID;
	
}
