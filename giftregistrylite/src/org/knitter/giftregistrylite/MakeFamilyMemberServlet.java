package org.knitter.giftregistrylite;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class MakeFamilyMemberServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5706520902156989508L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		
		
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    
	    if (user==null) {
	    	resp.sendRedirect("/");
	    }

	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
	    Query qf = new Query("Family");
	    qf.addFilter("name", FilterOperator.EQUAL, req.getParameter("f"));
	    PreparedQuery pq = datastore.prepare(qf);
	    Entity family=pq.asSingleEntity();
	    
	    if (family == null) {
	    	// make a family
		    family = new Entity("Family");
		    family.setProperty("createdby", user);
		    family.setProperty("name", req.getParameter("f"));
		    family.setProperty("token", getToken());
		    datastore.put(family);
		    
		    // wait until the record shows up
		    int cnt=0; int limit=10;
		    while (cnt < 1 && limit-- > 0) {
		    	Query qf1 = new Query("Family");
		    	qf1.addFilter("name", FilterOperator.EQUAL, req.getParameter("f"));
		    	PreparedQuery pq1 = datastore.prepare(qf1);
				cnt = pq1.countEntities();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    if (family == null) {
		    	resp.sendRedirect("/");
		    	return;
		    }
	    } else {   
	    	if ( req.getParameter("t") == null || ! family.getProperty("token").equals(req.getParameter("t"))) {
	    		resp.sendRedirect("/");
	    		return;
	    	}
	    }
	    
	    Entity familymember = new Entity("FamilyMember", family.getKey());
	    familymember.setProperty("user", user);
	    familymember.setProperty("name", user.getNickname());
	    familymember.setProperty("family", family.getKey());
	    
	    datastore.put(familymember);
	    
    
	    // wait until the record shows up
	    int cnt=0; int limit=10;
	    while (cnt < 1 && limit-- > 0) {
	    		Query q = new Query("FamilyMember");
	    		q.addFilter("user", FilterOperator.EQUAL, user);
	    		PreparedQuery pq1 = datastore.prepare(q);
				cnt = pq1.countEntities();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    }

    	resp.sendRedirect("/");

	}
	
	  private SecureRandom random = new SecureRandom();
	
	  private String getToken() {
	    return new BigInteger(40, random).toString(32);
	  }
	
}
