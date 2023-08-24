package bppp.practice.service;

import bppp.practice.entity.UserHasRoleEntity;

import java.util.List;

public interface UserHasRoleService {
    public void saveRoleHasUsers(UserHasRoleEntity role);

    public UserHasRoleEntity getRoleHasUser(int id);

    public void deleteRoleHasUser(int id);

    public List<UserHasRoleEntity> getAllRolesHasUser();
    public UserHasRoleEntity getRoleByIdUser(int id);

}

