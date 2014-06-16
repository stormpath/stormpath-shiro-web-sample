<%--
  ~ Copyright (c) 2012 Stormpath, Inc. and contributors
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
  --%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="com.stormpath.shiro.web.utils.Constants" %>
<%@ page import="com.stormpath.shiro.web.utils.Configuration" %>
<%@ include file="include.jsp" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="<c:url value="/style.css"/>"/>
</head>
<body>

<h2>Please Log In</h2>

<p>Login with an account created in your Stormpath tenant assigned to the configured application.</p>

<%
    String errorDescription = null;
    String messageFlag = request.getParameter(Constants.MESSAGE_FLAG);
    if (messageFlag != null) {
        errorDescription = Constants.getMessage(messageFlag);
    }
    if (errorDescription != null) {
%>
<div>
    <p style="padding-top:5px;padding-bottom:10px;color:red"><%=errorDescription%></p>
</div>
<%
    }
%>

<table>
    <tr>
        <td>
            <form name="loginform" action="" method="post">
                <table align="left" border="0" cellspacing="0" cellpadding="3">
                    <tr>
                        <td>Username:</td>
                        <td><input type="text" name="username" maxlength="30"></td>
                    </tr>
                    <tr>
                        <td>Password:</td>
                        <td><input type="password" name="password" maxlength="30"></td>
                    </tr>
                    <tr>
                        <td colspan="2" align="left"><input type="checkbox" name="rememberMe"><font size="2">Remember Me</font></td>
                    </tr>
                    <tr>
                        <td colspan="2" align="right"><input type="submit" name="submit" value="Login"></td>
                    </tr>
                </table>
            </form>
        </td>
        <% if (Configuration.isGoogleEnabled() || Configuration.isGoogleEnabled()) { %>
        <td width="40">&nbsp;<td/>
        <td>
            <table>
                <tr><td height="50" class="divider" ></td></tr>
                <tr>
                    <td style="color: slategrey">or</td>
                </tr>
                <tr><td height="50" class="divider" ></td></tr>
            </table>
        </td>
        <td width="40">&nbsp;<td/>
        <td>
            <table>
                <% if (Configuration.isGoogleEnabled()) { %>
                <tr>

                    <td>
                        <img src="images/Red-signin_Long_base_44dp.png" onclick="googleLogin()"/>
                    </td>
                </tr>

                <%
                    }
                %>
                <% if (Configuration.isFacebookEnabled()) { %>
                <tr>
                    <td>
                        <img src="images/facebook-login-button.png" onclick="facebookLogin()"/>
                    </td>
                </tr>

                <%
                    }
                %>
            </table>
        </td>
        <%
            }
        %>
    </tr>
</table>

<p>Not Yet Registered? <a href="/account/register.jsp">Register Here</a></p>

<span style="display:inline-block; width: 100;"></span>

<script>
    function googleLogin() {
        window.location.replace('https://accounts.google.com/o/oauth2/auth?response_type=code' +
                '&client_id=<%=Configuration.getGoogleClientId()%>&scope=openid%20email' +
                '&redirect_uri=<%=URLEncoder.encode(Configuration.getGoogleRedirectUri(),"UTF-8")%>');
    }
</script>

<script>
    function facebookLogin() {
        var FB = window.FB;

        FB.login(function(response) {
            if (response.status === 'connected') {
                var queryStr = window.location.search.replace('?', '');
                if (queryStr) {
                    window.location.replace("/facebookOauthCallback?queryStr&code=" + FB.getAuthResponse()['accessToken']);
                } else {
                    window.location.replace("/facebookOauthCallback?code=" + FB.getAuthResponse()['accessToken']);
                }
            }
        }, {scope: '<%= Configuration.getFacebookScope() %>'});
    }

    window.fbAsyncInit = function() {
        FB.init({
            appId      : <%= Configuration.getFacebookAppId() %>,
            cookie     : true,
            xfbml      : true,
            version    : 'v2.0'
        });
    };

    (function(d, s, id){
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) {return;}
        js = d.createElement(s); js.id = id;
        js.src = "//connect.facebook.net/en_US/sdk.js";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));
</script>


</body>
</html>
