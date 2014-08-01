<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>

<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.PreparedQuery" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Query.FilterOperator" %>
<%@ page import="com.google.appengine.api.datastore.Query.SortDirection" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.Text" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>


<%
	boolean isThisYou=true;
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user == null) {
%>

<p>Hello!
    <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
    to include your name with greetings you post.</p>

<%
} else {
        pageContext.setAttribute("user", user);
%>
<p>Hello, ${fn:escapeXml(user.nickname)}! (You can
    <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>


<%
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	Query q = new Query("FamilyMember");
	q.addFilter("user", FilterOperator.EQUAL, user);
	PreparedQuery pq = datastore.prepare(q);
	Entity userfamilymember=pq.asSingleEntity();
	
	q = new Query("FamilyMember");
	if (request.getParameter("fm") == null) {
		q.addFilter("user", FilterOperator.EQUAL, user);
	} else {
		q.addFilter(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, 
				KeyFactory.stringToKey(request.getParameter("fm")));
		isThisYou=false;
	}
	pq = datastore.prepare(q);
	
	Entity familymember=pq.asSingleEntity();
	if (familymember == null) {
%>
<p>You are not a FamilyMember</p>

<form action="/makefamilymember" method="post">
	<div>Family: <input type="text" name="f"/></div>
	<div>Token: <input type="text" name="t"/> (optional, if provided)</div>
    <div><input type="submit" value="Make FamilyMember"/></div>
</form>

<%		} else { 

	q=new Query("Family");
	q.addFilter(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, 
			familymember.getProperty("family"));
	pq = datastore.prepare(q);
	Entity family=pq.asSingleEntity();
	
	// check to make sure these are in the same family
	q=new Query("Family");
	q.addFilter(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, 
			userfamilymember.getProperty("family"));
	pq = datastore.prepare(q);
	Entity userfamily=pq.asSingleEntity();
	
	if ( ! family.equals(userfamily)) {
		family=userfamily;
		familymember=userfamilymember;
		isThisYou=true;
		response.sendError(503, "Cannot access this Family Member, wrong Family");
		return;
	}
	
%>
<p>You are a member of the <span title="<%= family.getProperty("token")%>"><b><i><%= family.getProperty("name") %></i></b></span> family.

<p>Other members of your family:</p>

<%

	q=new Query("FamilyMember");
	q.addFilter("family", Query.FilterOperator.EQUAL, familymember.getProperty("family"));
	pq = datastore.prepare(q);

%>
<ul>
<% for (Entity fm: pq.asIterable()) {
		if (! fm.getProperty("user").equals(user)) {
			
%>
	<li> <a href="/index.jsp?fm=<%=KeyFactory.keyToString(fm.getKey())%>"><%= fm.getProperty("name")%></a></li>
<%
		}
}
%>
</ul>


<%

	Query qgifts = new Query("Gift")
		.setAncestor(familymember.getKey())
		.addSort("priority", SortDirection.ASCENDING)
		.addSort(Entity.KEY_RESERVED_PROPERTY, SortDirection.DESCENDING);
	PreparedQuery pqgifts = datastore.prepare(qgifts);
	List<Entity> gifts = pqgifts.asList(FetchOptions.Builder.withDefaults());
	int numgifts=gifts.size();
	
%>
<br/><br/>

<% if (isThisYou) { %>
You have <%= numgifts %> gifts.<br/>
<% } else { %>
<%= familymember.getProperty("name") %> has <%= numgifts %> gifts. 
(this is not <a href="/">your</a> list)<br/>
<% } %>
 




<%
if (numgifts>0){
%>
<table border=1>
<tr><th>Description</th><th>Priority</th><th>Price</th><th>Quantity</th><th>Comment</th></tr>
<%
for (Entity g: gifts) {
	Text c=(Text)g.getProperty("comment");
%>
<tr>
<td><a href="<%= g.getProperty("link") %>"><%= g.getProperty("description") %></a></td>
<td><%= g.getProperty("priority") %></td>
<td><%= g.getProperty("price") %></td>
<td><%= g.getProperty("quantity") %></td>
<td><%= c.getValue() %></td>
</tr>


<%
}
%>
</table>
<% } %>


<br/>
<% if (isThisYou) { %>
<a href="/addgift.jsp">Add Gift</a>
<% } else { %>
Go back to <a href="/">your profile</a> to add gifts
<% } %>

















<%		} %>

<%
    }
%>


</body>
</html>