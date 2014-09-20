<%@ page import="org.seadva.tools.oreeditor.OREUtils" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link href="bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">
    <!-- Add custom CSS here -->
    <title>Get Collection Id</title>
</head>
<body onload="message()">
<!-- Bootstrap core JavaScript -->
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/bootbox.min.js"></script>
<script type="text/javascript">
    function message() {
        <%
        Object o = request.getSession().getAttribute("status");
        if (o == null) {
        %>
        return;
        <%
        } else {
            int status = (Integer) o;
            if (status == OREUtils.ORE_SUCCESS) {
        %>
        bootbox.alert("Successfully persisted the updated ORE");
        <%
            } else if (status == OREUtils.ORE_NO_UPDATE) {
        %>
        bootbox.alert("There were no updates to the ORE");
        <%
            } else if (status == OREUtils.ORE_FAILURE) {
        %>
        bootbox.error("Error while persisting the updated ORE");
        <%
            }
            request.getSession().removeAttribute("status");
        }
        %>
    }

    function validate() {
        var id = document.getElementById("id").value;
        if (id == null || id.length == 0) {
            bootbox.alert("Please provide a valid collection Id!");
            return false;
        }
        return true;
    }
</script>


<table align="center">
    <tr>
        <td><img src="images/sead_logo_a.png"></td>
        <td><img src="images/sead_logo_b.png"></td>
    </tr>
</table>

<div id="wrapper">
    <div id="page-wrapper">
        <div class="container">
            <h3>SEAD Collection Details</h3>
            <form role='form' action="read_ore" method="post">
                <div class='form-group'>
                    <label for='id'>Collection ID</label>
                    <input type='text' class='form-control' id='id' name="id"
                           placeholder='Enter Collection ID'>
                </div>
                <button type="submit" onclick="return validate()" class="btn btn-primary">Submit</button>
            </form>

        </div>
    </div>
</div>
</body>
</html>
