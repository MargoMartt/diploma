package bppp.practice.repository;

import bppp.practice.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
public RoleEntity getByIdRole(int id);
}
