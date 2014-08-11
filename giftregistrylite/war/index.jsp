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

<html ng-app="giftreg" lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/giftregistrylite.css" rel="stylesheet">
    <title>Gift Registry Lite</title>
    
    
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
    <script src="/js/angular-1.2.21.min.js"></script>
    <script src="/js/app.js"></script>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="/js/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="/js/bootstrap.min.js"></script>

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user == null) {
    	response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
    	return;
    }
%>

<p>(<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>)</p>


<div ng-controller='FamilyMemberController as famMemCtrl'>

	<h3>
		Welcome <%=user.getNickname()%>!!
	</h3>

	<div ng-hide="famMemCtrl.isFamilyMember()">
		Join or create a family:
		<form name="joinFamilyForm" ng-controller='FamilyController as famCtrl'
								ng-submit='joinFamilyForm.$valid && famCtrl.joinFamily()' novalidate>
		<label>Family:</label>
		<input type="text" ng-model="famCtrl.family.name" required/>
		<br/>
		<label>Token:</label>
		<input type="text" ng-model="famCtrl.family.token"/>
		<br/>
		<input type="submit" />
		</form>
	</div>
</div>



<ul ng-controller='GiftController as giftCtrl'>
	<li ng-repeat="gift in giftCtrl.gifts">
		here: {{gift.description}}
	</li>
</ul>



</body>
</html>