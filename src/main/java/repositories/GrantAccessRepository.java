package repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import models.GrantAccess;

import java.util.List;

public class GrantAccessRepository {
    private final EntityManager entityManager;

    public GrantAccessRepository(){
        entityManager = Persistence.createEntityManagerFactory("jpa-mariadb").createEntityManager();
    }

    public boolean save(GrantAccess grantAccess) {
        try {
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("INSERT INTO grant_access (account_id, role_id, is_grant) VALUES (?, ?, ?)")
                    .setParameter(1, grantAccess.getAccount().getAccountId())
                    .setParameter(2, grantAccess.getRole().getRoleId())
                    .setParameter(3, grantAccess.getIsGrant())
                    .executeUpdate();
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public GrantAccess findById(String accountId, String roleId) {
        try {
            return entityManager.createQuery("SELECT ga FROM GrantAccess ga WHERE ga.account.accountId = :accountId AND ga.role.roleId = :roleId", GrantAccess.class)
                    .setParameter("accountId", accountId)
                    .setParameter("roleId", roleId)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<GrantAccess> getGrantAccessByAccountId(String accountId) {
        return entityManager.createQuery("SELECT ga FROM GrantAccess ga WHERE ga.account.accountId = :accountId", GrantAccess.class)
                .setParameter("accountId", accountId)
                .getResultList();
    }

    public List<GrantAccess> getGrantAccessByRoleId(String roleId) {
        return entityManager.createQuery("SELECT ga FROM GrantAccess ga WHERE ga.role.roleId = :roleId", GrantAccess.class)
                .setParameter("roleId", roleId)
                .getResultList();
    }
}
