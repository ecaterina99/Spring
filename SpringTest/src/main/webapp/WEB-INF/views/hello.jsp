<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <title>Form</title>
</head>
<body>

<h2>User Form</h2>

<form:form method="post" action="hello" modelAttribute="user">
    <div>
        <form:label path="userName">Name</form:label>
        <form:input path="userName"/>
        <form:errors path="userName"/>
    </div>
    <div>
        <form:label path="date_of_birth">Date of Birth</form:label>
        <form:input path="date_of_birth" type="date"/>
        <form:errors path="date_of_birth"/>
    </div>
    <div>
        <form:label path="email">Email</form:label>
        <form:input path="email"/>
        <form:errors path="email"/>
    </div>
    <input type="submit" value="Submit"/>
</form:form>

</body>
</html>