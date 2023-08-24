package bppp.practice.service;

import bppp.practice.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleService{
    public void saveRole(RoleEntity role);
    public RoleEntity getRole(int id);

    public void deleteRole (int id);
    public List<RoleEntity> getAllRoles();
}
