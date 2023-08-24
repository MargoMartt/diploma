package bppp.practice.service;


import bppp.practice.entity.UserEntity;
import bppp.practice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsersServiceImpl implements UserService {
    @Autowired
    UserRepository repository;

    @Override
    public void saveUser(UserEntity user) {
        repository.save(user);

    }

    @Override
    public UserEntity getUser(int id) {
        UserEntity user = null;
        Optional<UserEntity> optional = repository.findById(id);
        if (optional.isPresent()) {
            user = optional.get();
        }
        return user;
    }

    @Override
    public void deleteUser(int id) {
        repository.deleteById(id);

    }

    @Override
    public List<UserEntity> getAllUser() {
        return repository.findAll();
    }

    @Override
    public UserEntity getByLogin(@NonNull String login) {
        return repository.getByLogin(login);
    }

}
