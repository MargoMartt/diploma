package bppp.practice.security;

import bppp.practice.entity.RoleEntity;
import bppp.practice.entity.UserEntity;
import bppp.practice.repository.RoleRepository;
import bppp.practice.repository.UserHasRoleRepository;
import bppp.practice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHasRoleRepository roleHasUsersService;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.getByLogin(username);
        System.out.println("loadUsersByUsername");
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        System.out.println("success");
        System.out.println(user);
        return new User(user.getLogin(), user.getPassword(), getAuthorities(user));
    }

    private Set<GrantedAuthority> getAuthorities(UserEntity user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        int userId = user.getUserId();
        System.out.println("vov");
        int roleId = roleHasUsersService.findUserHasRoleEntitiesByUserIdUser(userId).getRoleIdRole();
        RoleEntity role = roleRepository.getByIdRole(roleId);

        authorities.add(new SimpleGrantedAuthority(role.getRoleName()));

        return authorities;
    }
}
