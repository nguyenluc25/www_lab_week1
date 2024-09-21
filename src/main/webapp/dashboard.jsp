<%@ page import="models.Account" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Role" %>
<%@ page import="models.GrantAccess" %>
<%--
  Created by IntelliJ IDEA.
  User: Luc
  Date: 9/15/2024
  Time: 8:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
            crossorigin="anonymous"></script>
    <title>Dash Board</title>
</head>
<script>
    <%
        String success = (String) session.getAttribute("success");
        if (success != null && !success.isEmpty()) {
    %>
    alert("<%= success %>");
    <%
        session.removeAttribute("success");
        }
    %>

    <%
        String error = (String) session.getAttribute("error");
        if (error != null && !error.isEmpty()) {
    %>
    alert("<%= error %>");
    <%
    session.removeAttribute("error");
        }
    %>
</script>
<%
    Account account = (Account) session.getAttribute("account");
    List<GrantAccess> grantAccesses = (List<GrantAccess>) session.getAttribute("grantAccesses");

    if (account == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
%>
<body>
<div class="container">

    <h1>Dash Board</h1>
    <div>
        <h3>Account Information</h3>
        <h4>ID: <%=account.getAccountId() %>
        </h4>
        <h4>Full Name: <%=account.getFullName() %>
        </h4>
        <h4>Email: <%=account.getEmail() %>
        </h4>
        <h4>Phone: <%=account.getPhone() %>
        </h4>
        <h4>Status: <%=account.getStatus() %>
        </h4>
        <h4>
            Role:
            <%
                for (GrantAccess ga : grantAccesses) {
                    %>
                    <span> <%= ga.getRole().getRoleName() %> | </span>
                    <%
                }
            %>
        </h4>

    </div>

    <form action="control-servlet" method="POST">
        <input type="hidden" name="action" value="logout"/>
        <button type="submit" class="btn btn-warning">Logout</button>
    </form>

    <%
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        Account updateAccount = null;
        if (isAdmin) {
    %>
    <!-- Button trigger modal -->
    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal">
        Add Account
    </button>
    <div>
        <div>
            <label class="form-label">Role</label>
            <select aria-label="Role" name="role">
                <%
                    List<Role> roles = (List<Role>) session.getAttribute("roles");
                    for (Role r : roles) {
                %>
                <option value="<%=r.getRoleId() %>"><%=r.getRoleName() %>
                </option>
                <%
                    }
                %>
            </select>

        </div>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">ID</th>
                <th scope="col">Full Name</th>
                <th scope="col">Email</th>
                <th scope="col">Phone</th>
                <th scope="col">Status</th>
                <th scope="col">Action</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Account> accounts = (List<Account>) session.getAttribute("accounts");
                for (Account a : accounts) {
            %>
            <tr>
                <td><%=a.getAccountId() %>
                </td>
                <td><%=a.getFullName() %>
                </td>
                <td><%=a.getEmail() %>
                </td>
                <td><%=a.getPhone() %>
                </td>
                <td><%=a.getStatus() %>
                </td>
                <td>
                    <button
                            onclick="setModalData('<%= a.getAccountId() %>', '<%= a.getFullName() %>', '<%= a.getEmail() %>','<%= a.getPassword()%>', '<%= a.getPhone() %>', '<%= a.getStatus() %>')"
                            type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#modal-update">
                        Update
                    </button>
                    <form action="control-servlet" method="POST">
                        <input type="hidden" name="action" value="delete"/>
                        <input type="hidden" name="accountId" value="<%= a.getAccountId() %>"/>
                        <button type="submit" class="btn btn-danger">Delete</button>
                    </form>
                </td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>
    <%
        }
    %>
</div>

<!-- Modal Add Account -->
<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Add Account</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form action="control-servlet" method="post">
                    <label>
                        <input type="text" hidden="hidden" name="action" value="createAccount">
                    </label>
                    <div class="mb-3">
                        <label class="form-label">Account ID</label>
                        <input
                                type="text"
                                class="form-control"
                                name="accountId"
                        >
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Full Name</label>
                        <input
                                type="text"
                                class="form-control"
                                name="fullName"
                        >
                    </div>
                    <div class="mb-3">
                        <label for="exampleInputEmail1" class="form-label">Email</label>
                        <input
                                type="email"
                                class="form-control"
                                id="exampleInputEmail1"
                                aria-describedby="emailHelp"
                                name="createEmail"
                        >
                    </div>
                    <div class="mb-3">
                        <label for="exampleInputPassword1" class="form-label">Password</label>
                        <input
                                type="password"
                                class="form-control"
                                id="exampleInputPassword1"
                                name="createPassword"
                        >
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Phone</label>
                        <input
                                type="text"
                                class="form-control"
                                name="phone"
                        >
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Status</label>
                        <input
                                type="text"
                                class="form-control"
                                name="status"
                        >
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Role</label>
                        <select class="form-select" aria-label="Role" name="role">
                            <%
                                List<Role> roles = (List<Role>) session.getAttribute("roles");
                                for (Role r : roles) {
                            %>
                            <option value="<%=r.getRoleId() %>"><%=r.getRoleName() %>
                            </option>
                            <%
                                }
                            %>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">Save</button>
                </form>
            </div>
        </div>
    </div>
</div>


<!-- Modal update account -->
<div class="modal fade" id="modal-update" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel1">Update Account</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form action="control-servlet" method="POST">
                    <input type="hidden" name="action" value="updateAccount">
                    <div class="mb-3">
                        <label class="form-label">Account ID</label>
                        <input type="text" class="form-control" id="modalAccountId" name="accountId" readonly>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Full Name</label>
                        <input type="text" class="form-control" id="modalFullName" name="fullName">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="text" class="form-control" id="modalEmail" name="email">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Password</label>
                        <input type="password" class="form-control" id="modalPassword" name="password">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Phone</label>
                        <input type="text" class="form-control" id="modalPhone" name="phone">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Status</label>
                        <input type="text" class="form-control" id="modalStatus" name="status">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Role</label>
                        <select class="form-select" aria-label="Role" name="role">
                            <%
                                for (Role r : roles) {
                            %>
                            <option value="<%=r.getRoleId() %>"><%=r.getRoleName() %>
                            </option>
                            <%
                                }
                            %>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">Save changes</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    function setModalData(accountId, fullName, email, password, phone, status) {
        document.getElementById("modalAccountId").value = accountId;
        document.getElementById("modalFullName").value = fullName;
        document.getElementById("modalEmail").value = email;
        document.getElementById("modalPassword").value = password;
        document.getElementById("modalPhone").value = phone;
        document.getElementById("modalStatus").value = status;
    }
</script>
</body>
</html>
