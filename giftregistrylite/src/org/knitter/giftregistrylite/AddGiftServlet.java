package org.knitter.giftregistrylite;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AddGiftServlet extends HttpServlet {


	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		
		
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    
	    if (user==null) 
	    	resp.sendRedirect("/");
	    
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    Query q = new Query("FamilyMember");
		q.addFilter("user", FilterOperator.EQUAL, user);
		PreparedQuery pq = datastore.prepare(q);
		
		Entity familymember=pq.asSingleEntity();
		
		if (familymember == null)
			resp.sendRedirect("/");
		
		
		Entity gift = new Entity("Gift", familymember.getKey());
	    
		gift.setProperty("description", req.getParameter("d")); 
		gift.setProperty("priority", req.getParameter("p"));
		gift.setProperty("link", new Link(req.getParameter("l")));
		gift.setProperty("comment", new Text(req.getParameter("c")));
		gift.setProperty("price", new Double(req.getParameter("m")));
		gift.setProperty("quantity", new Integer(req.getParameter("q")));
				
	    
	    datastore.put(gift);
	    
	    Key giftkey=gift.getKey();
	    
    
	    // wait until the record shows up
	    int cnt=0; int limit=10;
	    while (cnt < 1 && limit-- > 0) {
	    	Query qgift =  new Query("Gift")
            	.addFilter(Entity.KEY_RESERVED_PROPERTY,
                       Query.FilterOperator.EQUAL,
                       giftkey);
    		PreparedQuery pqgift = datastore.prepare(qgift);
			cnt = pqgift.countEntities();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

    	resp.sendRedirect("/");

	}
	
}
