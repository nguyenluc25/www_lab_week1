package repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import models.Account;
import models.GrantAccess;
import models.Log;

import java.util.List;
import java.util.Optional;

public class AccountRepository {

    private final EntityManager entityManager;

    public AccountRepository(){
        entityManager = Persistence.createEntityManagerFactory("jpa-mariadb").createEntityManager();
    }

    public Account findById(String accountId) {
        try {
            return entityManager.find(Account.class, accountId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<Account> findAll() {
        return entityManager.createQuery("SELECT a FROM Account a", Account.class).getResultList();
    }

    public Account findByIdAndPassword(String id, String password){
        Account account = null;
        try {
            account = entityManager.createQuery("SELECT a FROM Account a WHERE a.accountId = :accountId AND a.password = :password", Account.class)
                    .setParameter("accountId", id)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }

    public boolean save(Account account) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(account);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public void saveLog(Log log) {
        entityManager.getTransaction().begin();
        entityManager.persist(log);
        entityManager.getTransaction().commit();
    }

    public void updateLog(Log log) {
        entityManager.getTransaction().begin();
        entityManager.merge(log);
        entityManager.getTransaction().commit();
    }

    public Log findLogByAccountId(String accountId) {
        Log log = null;
        try {
            log = entityManager.createQuery("SELECT l FROM Log l WHERE l.accountId = :accountId", Log.class)
                    .setParameter("accountId", accountId)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return log;
    }

    public void delete(Account account) {
        entityManager.getTransaction().begin();
        entityManager.remove(account);
        entityManager.getTransaction().commit();
    }

    public boolean update(Account account) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(account);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
