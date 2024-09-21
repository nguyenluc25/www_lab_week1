package services;

import jakarta.inject.Inject;
import models.Account;
import models.GrantAccess;
import models.Log;
import repositories.AccountRepository;
import repositories.GrantAccessRepository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountService {

    @Inject
    private AccountRepository accountRepository;

    @Inject
    private GrantAccessRepository grantAccessRepository;

    public Account getAccountById(String accountId) {
        return accountRepository.findById(accountId);
    }

    public boolean save(Account account) {
        Account existingAccount = accountRepository.findById(account.getAccountId());
        if (existingAccount != null) {
            return false;
        }
        accountRepository.save(account);
        return true;
    }

    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountByIdAndPassword(String id, String password) {
        return accountRepository.findByIdAndPassword(id, password);
    }

    public Log getLogByAccountId(String accountId) {
        return accountRepository.findLogByAccountId(accountId);
    }

    public void loginLog(Log log) {
        Log existingLog = accountRepository.findLogByAccountId(log.getAccountId());
        if (existingLog == null) {
            accountRepository.saveLog(log);
        } else {
            existingLog.setLoginTime(log.getLoginTime());
            existingLog.setNotes(log.getNotes());
            accountRepository.updateLog(existingLog);
        }
    }

    public void logoutLog(Log log) {
        log.setLogoutTime(log.getLogoutTime());
        log.setNotes(log.getNotes());
        accountRepository.updateLog(log);
    }

    public boolean update(Account account) {
        Account existingAccount = accountRepository.findById(account.getAccountId());
        if (existingAccount == null) {
            return false;
        }
        accountRepository.update(account);
        return true;
    }

    public void delete(Account account) {
        accountRepository.delete(account);
    }

}
