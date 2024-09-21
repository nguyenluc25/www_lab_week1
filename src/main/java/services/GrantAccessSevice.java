package services;

import jakarta.inject.Inject;
import models.GrantAccess;
import repositories.GrantAccessRepository;

import java.util.List;

public class GrantAccessSevice {

    @Inject
    private GrantAccessRepository grantAccessRepository;

    public boolean save(GrantAccess grantAccess) {
        if(grantAccessRepository.findById(grantAccess.getAccount().getAccountId(), grantAccess.getRole().getRoleId()) == null){
            return grantAccessRepository.save(grantAccess);
        }
        return false;
    }

    public GrantAccess findById(String accountId, String roleId) {
        return grantAccessRepository.findById(accountId, roleId);
    }

    public List<GrantAccess> getGrantAccessByAccountId(String accountId) {
        return grantAccessRepository.getGrantAccessByAccountId(accountId);
    }

    public List<GrantAccess> getGrantAccessByRoleId(String roleId) {
        return grantAccessRepository.getGrantAccessByRoleId(roleId);
    }
}
