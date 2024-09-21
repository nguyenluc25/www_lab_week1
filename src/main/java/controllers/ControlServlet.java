package controllers;


import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.*;
import services.AccountService;
import services.GrantAccessSevice;
import services.RoleService;

import java.io.IOException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ControlServlet", value = "/control-servlet")
public class ControlServlet extends HttpServlet {

    @Inject
    private AccountService accountService;

    @Inject
    private RoleService roleService;

    @Inject
    private GrantAccessSevice grantAccessSevice;

    private static Account account;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        List<Role> roles = roleService.getRoles();
        session.setAttribute("roles", roles);
        String error = "";
        String success = "";
        switch (action) {
            case "login":
                String id = req.getParameter("id");
                String password = req.getParameter("password");
                account = accountService.getAccountByIdAndPassword(id, password);
                if (account != null) {
                    dashboard(account, session);
                    Log log = new Log(account.getAccountId(), Instant.now(), Instant.now(), "Login");
                    accountService.loginLog(log);
                    resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/index.jsp");
                }
                break;
            case "createAccount":
                String accountId = req.getParameter("accountId");
                String fullName = req.getParameter("fullName");
                String emailCreate = req.getParameter("createEmail");
                String passwordCreate = req.getParameter("createPassword");
                String phone = req.getParameter("phone");
                Byte status = Byte.parseByte(req.getParameter("status"));
                Account accountCreate = new Account(accountId, fullName, passwordCreate, emailCreate, phone, status);
                if (accountService.save(accountCreate)) {
                    String roleId = req.getParameter("role");
                    Role role = roleService.getRoleById(roleId);
                    GrantAccess grantAccess = new GrantAccess(role, accountCreate, true, "");
                    grantAccessSevice.save(grantAccess);
                    success = "Account created successfully";
                    session.setAttribute("success", success);
                } else {
                    error = "Account already exists";
                    session.setAttribute("error", error);
                }
                dashboard(account, session);
                resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");
                break;
            case "delete":
                String deleteAccountId = req.getParameter("accountId");
                Account deleteAccount = accountService.getAccountById(deleteAccountId);
                accountService.delete(deleteAccount);
                dashboard(account, session);
                resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");
                break;
            case "logout":
                Log log = accountService.getLogByAccountId(account.getAccountId());
                log.setLogoutTime(Instant.now());
                log.setNotes("Logout");
                accountService.logoutLog(log);
                session.invalidate();
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
                break;
            case "updateAccount":
                String updateAccountId = req.getParameter("accountId");
                System.out.println("updateAccountId: " + updateAccountId);
                String updateFullName = req.getParameter("fullName");
                System.out.println("updateFullName: " + updateFullName);
                String updateEmail = req.getParameter("email");
                String updatePassword = req.getParameter("password");
                String updatePhone = req.getParameter("phone");
                Byte updateStatus = Byte.parseByte(req.getParameter("status"));
                String roleId = req.getParameter("role");
                Account updateAccount = new Account(updateAccountId, updateFullName, updatePassword, updateEmail, updatePhone, updateStatus);
                if (accountService.update(updateAccount)) {
                    Role role = roleService.getRoleById(roleId);
                    GrantAccess grantAccess = new GrantAccess(role, updateAccount, true, "");
                    grantAccessSevice.save(grantAccess);
                    success = "Account updated successfully";
                    session.setAttribute("success", success);
                } else {
                    error = "Account does not exist";
                    session.setAttribute("error", error);
                }
                dashboard(account, session);
                resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");
                break;
            default:
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
                break;
        }
    }

    public void dashboard(Account account, HttpSession session) {
        session.setAttribute("account", account);
        List<GrantAccess> grantAccesses = grantAccessSevice.getGrantAccessByAccountId(account.getAccountId());
        session.setAttribute("grantAccesses", grantAccesses);
        for (GrantAccess ga : grantAccesses) {
            if (ga.getRole().getRoleId().equals("admin") && ga.getIsGrant()) {
                List<Account> accounts = accountService.getAccounts().stream().sorted(Comparator.comparing(Account::getFullName)).toList();
                session.setAttribute("accounts", accounts);
                session.setAttribute("isAdmin", true);
            } else {
                session.setAttribute("isAdmin", false);
            }
            break;
        }
    }
}
