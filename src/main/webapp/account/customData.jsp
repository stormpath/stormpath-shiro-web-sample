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
<%@ include file="../include.jsp" %>
<%@ page session="false" %>

<html>
<head>
    <title>Custom Data</title>
</head>

<body>
<h2>Custom Data</h2>

<table width="100%">
<tr>
    <td><a href="<c:url value="/home.jsp"/>">Return to the home page</a></td>
    <td><a href="<c:url value="/logout"/>">Log out</a></td>
</tr>
</table>


<br/><br/><br/>

<!-- Table used to add data to the account's custom data. If the key does not exists in the custom data, then
it will be added to it. If the key already exists, the value will be appended to the existing values -->
<table id="newCustomFieldTable" width="80%" border="1" rules="none">
    <tr>
        <td>Key : <input type="text" id="newKey" name="newKey" size="50%"/> </td>
        <td>Value : <input type="text" id="newValue" name="newValue" size="50%"/> </td>
        <td align="right"><button id="newCustomDataFieldButton" type="submit">Append Custom Data</button></td>
    </tr>
</table>

<br/><br/>

<h3>Custom Data Fields</h3>

<table class="grid" id="customDataTable" width="80%" border="1">
    <thead>
    <tr>
        <th>Key</th>
        <th>Value</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${customDataFields}" var="customDataField">
        <c:if test="${customDataField.key ne 'href' && customDataField.key ne 'createdAt' && customDataField.key ne 'modifiedAt'}">
            <tr id="${customDataField.key}">
                <td>${customDataField.key}</td>
                <td>${customDataField.value}</td>
                <td align="center"><a href="javascript:;" onclick="deleteCustomDataField('${accountId}','${customDataField.key}')">Delete</a></td>
            </tr>
        </c:if>
    </c:forEach>
    </tbody>
</table>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript">
    //New custom data will be added using this jQuery call.
    $(document).ready(function() {
        $("#newCustomDataFieldButton").click(function(){
            key = $("#newKey").val();
            value = $("#newValue").val();
            accountId = "${accountId}"
            var targetUrl = "/account/customData/"+accountId;
            if (key == "" || key.indexOf(" ") != -1) {
                alert("Custom data keys cannot be empty nor contain spaces")
                return
            }
            if (value == "") {
                alert("Custom data velues cannot be empty")
                return
            }
            var response = $.ajax({ type: 'POST',
                url: targetUrl,
                async: false,
                data: { 'key': key, 'value': value }
            }).responseText;
            returnedValue = response;
            if(returnedValue != value) {
                $('#customDataTable tr[id=' + key + ']').remove()
            }
            $('#customDataTable').append('<tr id="'+key+'"><td>' + key + '</td><td>' + returnedValue + '</td><td align="center"><a href=\"javascript:;\" onclick=\"deleteCustomDataField(\''+ accountId + '\',\'' + key +'\')">Delete</a></td></tr>');
            $('#newKey').val('');
            $('#newValue').val('');
        });
    });
    //Let's detect "Enter" key pressed in the New Value field.
    $(document).ready(function() {
        $("#newValue").keyup(function(event){
            if(event.keyCode == 13){
                $('#newValue').blur();
                $("#newCustomDataFieldButton").click();
            }
        })
    });
    //Deletion of custom data fields will carried out using this jQuery call.
    function deleteCustomDataField(accountId, key) {
        var targetUrl = "/account/customData/"+accountId+"/"+key;
        $.ajax({
            type: 'DELETE',
            url: targetUrl,
            contentType:'application/json; charset=utf-8',
            dataType:"json",
            data:"{}",
            async: true,
            success: function(data, textStatus, jqXHR) {
                $('#customDataTable tr[id=' + key + ']').remove()
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert('There was an error deleting the Custom Data Field.');
            }
        });
    }

</script>
</body>
</html>