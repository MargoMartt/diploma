package bppp.practice.service;

import bppp.practice.entity.UserHasRoleEntity;
import bppp.practice.repository.UserHasRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserHasRoleServiceImpl implements UserHasRoleService {

    @Autowired
    UserHasRoleRepository repository;

    @Override
    public void saveRoleHasUsers(UserHasRoleEntity role) {
        repository.save(role);
    }

    @Override
    public UserHasRoleEntity getRoleHasUser(int id) {
        UserHasRoleEntity role = null;
        Optional<UserHasRoleEntity> optional = repository.findById(id);
        if (optional.isPresent()) {
            role = optional.get();
        }
        return role;
    }

    @Override
    public void deleteRoleHasUser(int id) {
        repository.deleteById(id);
    }

    @Override
    public List<UserHasRoleEntity> getAllRolesHasUser() {
        return repository.findAll();
    }
}
