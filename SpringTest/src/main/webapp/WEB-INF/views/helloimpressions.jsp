<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Guestbook</title>
</head>
<body>
<h1>Guestbook</h1>

<form:form action="helloimpressions" method="post" modelAttribute="impression">

    <form:label path="name">Enter your name:</form:label>
    <br/>

    <form:input type="text" path="name" placeholder="your name..."></form:input>
    <br/>

    <form:label path="text">Enter your impression:</form:label>
    <br/>

    <form:textarea path="text" placeholder="your impression..." rows="4" cols="50"></form:textarea>
    <br/>

    <input type="submit" value="Submit"/>
</form:form>

<label for="impression_list">All impressions:</label>
<br/>
<textarea id="impression_list" rows="20" cols="100" readonly>${impressions}</textarea>

</body>
</html>