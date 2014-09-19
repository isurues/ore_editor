<%@ page import="org.seadva.tools.oreeditor.OREResource" %>
<%@ page import="java.util.Map" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link href="bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">
    <!-- Add custom CSS here -->
    <title>Resource Map Details</title>
</head>
<body>
<!-- Bootstrap core JavaScript -->
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/bootbox.min.js"></script>
<%
    OREResource resource = (OREResource) request.getAttribute("resource_map");
%>

<div id="wrapper">
    <div id="page-wrapper">
        <div class="container">
            <%
                String resourceURI = resource.getUri();
            %>
            <h3>Collection : <%=resourceURI%></h3>
            <form role='form' action="#" method="post">
                <table class='table' style="width: 70%">
                    <tr>
                        <th>Property</th>
                        <th>Value</th>
                    </tr>
                    <%
                        Map<String, String> properties = resource.getPredicates();
                        for (Map.Entry<String, String> entry : properties.entrySet()) {
                    %>
                    <tr><td style="width: 30%"><%=entry.getKey() %></td><td>
                        <input type='text' class='form-control' width="150"
                               name="<%=entry.getKey() %>" value="<%= entry.getValue()%>">
                    </td></tr>
                    <%
                        }
                    %>
                </table>
                <h3>Child Resources</h3>
                <%
                    for (OREResource child : resource.getChildResources()) {
                        String childUri = child.getUri();
                %>
                <h4>Resource : <%=childUri%></h4>
                <table class='table' style="width: 70%">
                    <tr>
                        <th>Property</th>
                        <th>Value</th>
                    </tr>
                    <%
                        Map<String, String> childProperties = child.getPredicates();
                        for (Map.Entry<String, String> childEntry : childProperties.entrySet()) {
                    %>
                    <tr><td style="width: 30%"><%=childEntry.getKey() %></td><td>
                        <input type='text' class='form-control' width="150"
                               name="<%=childEntry.getKey() %>" value="<%= childEntry.getValue()%>">
                    </td></tr>
                    <%
                        }
                    %>
                </table>
                <%
                    }
                %>
                <button type="submit" class="btn btn-primary">Persist ORE</button>
                <p></p>
                <p></p>
            </form>
        </div>
    </div>
</div>

</body>
</html>
