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
    String map = (String) request.getAttribute("resource_map");
%>

<div id="wrapper">
    <div id="page-wrapper">
        <div class="container">
            <h3>Collection : <%=map%></h3>
            <table class='table'>
                <tr>
                    <th>Property</th>
                    <th>Value</th>
                </tr>
                <tr>
                    <td>Name</td>
                    <td><%=map%></td>
                </tr>
            </table>
        </div>
    </div>
</div>

</body>
</html>
