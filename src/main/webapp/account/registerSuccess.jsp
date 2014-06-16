<%--
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
  --%>
<%@ include file="../include.jsp" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="<c:url value="../style.css"/>"/>
    <title>Registration Success</title>
</head>
<body>

<h2>Congratulations</h2>

<p>You are now registered.</p>

<p>You can now <a href="<c:url value="/login.jsp"/>">login</a> or continue as <a href="<c:url value="/"/>">guest</a>.</p>

<p>NOTE: Depending on the configuration of this application you may need to verify your email address. If you received a verification email, you need to click the received link before you are allowed to login.</p>

</body>
</html>