package bppp.practice.repository;

import bppp.practice.entity.UserHasRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHasRoleRepository extends JpaRepository<UserHasRoleEntity, Integer> {
    public UserHasRoleEntity findUserHasRoleEntitiesByUserIdUser(int id);
}
