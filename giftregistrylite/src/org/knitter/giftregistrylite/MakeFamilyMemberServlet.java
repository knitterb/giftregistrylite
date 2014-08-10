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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import static com.google.appengine.api.datastore.FetchOptions.Builder.*;

public class MakeFamilyMemberServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5706520902156989508L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		
		
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    
	    if (user==null) {
	    	resp.sendRedirect("/");
	    	return;
	    }
	    
	    if (req.getParameter("f") == null || req.getParameter("f").length() < 1) {
	    	resp.sendRedirect("/?msg=Error:+You+must+supply+a+family+name");
	    	return;
	    }

	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
	    Query qf = new Query("Family");
		Filter f1 = new FilterPredicate("name", FilterOperator.EQUAL, req.getParameter("f"));
		qf.setFilter(f1);
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
		    	Filter f11 = new FilterPredicate("name", FilterOperator.EQUAL, req.getParameter("f"));
		    	qf1.setFilter(f11);
		    	PreparedQuery pq1 = datastore.prepare(qf1);
				cnt = pq1.countEntities(withLimit(10));
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
		    if (cnt == 0) {
		    	resp.sendRedirect("/");
		    	return;
		    }
	    } else {   
	    	if ( req.getParameter("t") == null || ! family.getProperty("token").equals(req.getParameter("t"))) {
	    		resp.sendRedirect("/?msg=Error:+Please+supply+a+valid+token");
	    		return;
	    	}
	    }
	    
	    Query q = new Query("FamilyMember");
		Filter f = new FilterPredicate("user", FilterOperator.EQUAL, user);
    	q.setFilter(f);
		PreparedQuery pq1 = datastore.prepare(q);
		Entity familymember=pq1.asSingleEntity();
		
		if (familymember == null ) {
		    familymember = new Entity("FamilyMember", family.getKey());
		    familymember.setProperty("user", user);
		    familymember.setProperty("name", user.getNickname());
		}
		familymember.setProperty("family", family.getKey());
	    datastore.put(familymember);
	    
    
	    // wait until the record shows up
	    int cnt=0; int limit=10;
	    while (cnt < 1 && limit-- > 0) {
	    		Query q1 = new Query("FamilyMember");
		    	Filter f11 = new FilterPredicate("user", FilterOperator.EQUAL, user);
		    	q1.setFilter(f11);

	    		PreparedQuery pq11 = datastore.prepare(q1);
				cnt = pq11.countEntities(withLimit(10));
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
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
