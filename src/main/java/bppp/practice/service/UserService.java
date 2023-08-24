package bppp.practice.service;

import bppp.practice.entity.UserEntity;
import org.springframework.lang.NonNull;

import java.util.List;

public interface UserService {
    public void saveUser(UserEntity users);

    public UserEntity getUser(int id);

    public void deleteUser(int id);

    public List<UserEntity> getAllUser();

    public UserEntity getByLogin(@NonNull String login);
}
