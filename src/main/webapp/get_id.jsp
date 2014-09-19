<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link href="bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">
    <!-- Add custom CSS here -->
    <title>Get Collection Id</title>
</head>
<body>
<!-- Bootstrap core JavaScript -->
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/bootbox.min.js"></script>

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
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>

        </div>
    </div>
</div>
</body>
</html>
