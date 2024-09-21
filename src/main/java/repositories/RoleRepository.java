package repositories;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import models.Role;

import java.util.List;

public class RoleRepository {
    private EntityManager entityManager;

    public RoleRepository(){
        entityManager = Persistence.createEntityManagerFactory("jpa-mariadb").createEntityManager();
    }

    public boolean save(Role role) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(role);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Role role) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(role);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void delete(Role role) {
        entityManager.getTransaction().begin();
        entityManager.remove(role);
        entityManager.getTransaction().commit();
    }

    public Role findById(String roleId) {
        return entityManager.find(Role.class, roleId);
    }

    public List<Role> findAll() {
        return entityManager.createQuery("SELECT r FROM Role r", Role.class).getResultList();
    }
}
