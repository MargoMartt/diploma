package bppp.practice.service;

import bppp.practice.entity.RoleEntity;
import bppp.practice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository repository;

    @Override
    public void saveRole(RoleEntity role) {
        repository.save(role);
    }

    @Override
    public RoleEntity getRole(int id) {
        RoleEntity role = null;
        Optional<RoleEntity> optional = repository.findById(id);
        if (optional.isPresent()) {
            role = optional.get();
        }
        return role;
    }

    @Override
    public void deleteRole(int id) {
        repository.deleteById(id);

    }

    @Override
    public List<RoleEntity> getAllRoles() {
        return repository.findAll();
    }
}
