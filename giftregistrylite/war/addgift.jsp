<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>







	<form action="/addgift" method="post">
		Description: <input name="d" >
		<br/>
		Priority:
		<select name="p">
			<option value="1">1 = WANT!!</option>
			<option value="5">5 = Nice To Have</option>
		</select>
		<br/>
		Price: <input name="m">
		<br/>
		Quantity: <input name="q">
		<br/>
		Link: <input name="l">
		<br />
		Comment: <textarea name="c"></textarea>
		<br/>
		<input type="submit" value="Add Gift"/>
	</form>
</body>
</html>