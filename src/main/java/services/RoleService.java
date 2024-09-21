package services;

import jakarta.inject.Inject;
import models.Role;
import repositories.RoleRepository;

import java.util.List;

public class RoleService {

    @Inject
    private RoleRepository roleRepository;

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(String roleId) {
        try {
            return roleRepository.findById(roleId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save(Role role) {
        if(roleRepository.findById(role.getRoleId()) != null){
            return;
        }
        roleRepository.save(role);
    }

    public void update(Role role) {
        if(roleRepository.findById(role.getRoleId()) == null){
            return;
        }
        roleRepository.update(role);
    }

    public void delete(Role role) {
        roleRepository.delete(role);
    }
}
