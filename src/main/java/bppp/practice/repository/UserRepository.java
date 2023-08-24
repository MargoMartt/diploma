package bppp.practice.repository;

import bppp.practice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    public UserEntity getByLogin(String login);

}
