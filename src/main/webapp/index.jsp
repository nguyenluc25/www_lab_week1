<%@ page import="models.Account" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
            crossorigin="anonymous"></script>
    <title>Login</title>
</head>
<%
    Account account = (Account) session.getAttribute("account");
    if (account != null) {
        response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
        return;
    }
%>
<body>
<div class="container">
    <h1>Login</h1>
    <form action="control-servlet" method="post">
        <label>
            <input type="text" hidden="hidden" name="action" value="login">
        </label>
        <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">ID Account</label>
            <input
                    type="text"
                    class="form-control"
                    id="exampleInputEmail1"
                    aria-describedby="emailHelp"
                    name="id"
            >
        </div>
        <div class="mb-3">
            <label for="exampleInputPassword1" class="form-label">Password</label>
            <input
                    type="password"
                    class="form-control"
                    id="exampleInputPassword1"
                    name="password"
            >
        </div>
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>
</body>
</html>