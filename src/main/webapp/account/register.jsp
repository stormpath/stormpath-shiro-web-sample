<!--
~ Copyright (c) 2014 Stormpath, Inc. and contributors
~
~ Licensed under the Apache License, Version 2.0 (the "License");
~ you may not use this file except in compliance with the License.
~ You may obtain a copy of the License at
~
~     http://www.apache.org/licenses/LICENSE-2.0
~
~ Unless required by applicable law or agreed to in writing, software
~ distributed under the License is distributed on an "AS IS" BASIS,
~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
~ See the License for the specific language governing permissions and
~ limitations under the License.
-->
<%@ include file="../include.jsp" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.stormpath.shiro.web.utils.Constants" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="<c:url value="../style.css"/>"/>
    <title>Registration</title>
</head>
<body>

<%
    String errorDescription = (String) application.getAttribute(Constants.REGISTRATION_ERROR_FLAG);
    application.removeAttribute(Constants.REGISTRATION_ERROR_FLAG);
    if (errorDescription!=null) {
%>
<p style="padding-top:5px;padding-bottom:10px;color:red">Registration error: <%=errorDescription%></p>
<%
    }
%>


<form name="registrationForm" action="/account/register" method="post" onSubmit="return validateForm()">
    <table align="left" border="0" cellspacing="0" cellpadding="3">
        <thead>
        <tr>
            <th colspan="2">Enter Account Information</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>Username</td>
            <td><input type="text" name="username" /></td>
        </tr>
        <tr>
            <td>First Name</td>
            <td><input type="text" name="firstName" /></td>
        </tr>
        <tr>
            <td>Middle Name</td>
            <td><input type="text" name="middleName" /></td>
        </tr>
        <tr>
            <td>Last Name</td>
            <td><input type="text" name="lastName" /></td>
        </tr>
        <tr>
            <td>Email</td>
            <td><input type="text" name="email" /></td>
        </tr>
        <tr>
            <td>Status</td>
            <td><select name="status">
                <option value="ENABLED">Enabled</option>
                <option value="DISABLED">Disabled</option>
            </select>
            </td>

        </tr>
        <tr>
            <td>Password</td>
            <td><input type="password" name="password" value="" /></td>
        </tr>
        <tr>
            <td>Suppress Verification Email</td>
            <td><input type="checkbox" name="suppressVerificationEmail" /></td>
        </tr>
        <td width="20">&nbsp;<td/>
        <tr>
            <td><input type="submit" value="Submit" /></td>
            <td><input type="reset" value="Reset" /></td>
        </tr>
        <tr/><td>&nbsp;<td/><tr/>
        <tr>
            <td colspan="2">Already registered? <a href="../login.jsp">Login Here</a></td>
        </tr>
        </tbody>
    </table>
    </center>
</form>

<script>
    function validateForm()
    {
        if(document.registrationForm.username.value=="")
        {
            alert("Username cannot be empty");
            document.registrationForm.username.focus();
            return false;
        }
        if(document.registrationForm.firstName.value=="")
        {
            alert("First Name cannot be empty");
            document.registrationForm.firstName.focus();
            return false;
        }
        if(document.registrationForm.lastName.value=="")
        {
            alert("Last Name cannot be empty");
            document.registrationForm.lastName.focus();
            return false;
        }
        if(document.registrationForm.email.value == "" || !document.registrationForm.email.value.contains("@"))
        {
            alert("Email is not valid");
            document.registrationForm.email.focus();
            return false;
        }
        if(document.registrationForm.password.value=="")
        {
            alert("Password cannot be empty");
            document.registrationForm.password.focus();
            return false;
        }
    }
</script>

</body>
</html>