package bppp.practice.controller;


import bppp.practice.entity.UserEntity;
import bppp.practice.entity.UserHasRoleEntity;
import bppp.practice.service.UserHasRoleService;
import bppp.practice.service.UserService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

@Component
@Controller
public class LoginController {
    @Autowired
    UserService usersService;

    @Autowired
    UserHasRoleService roleHasUsersService;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/login")
    public String login() {
        return "login-register";
    }

    @GetMapping("/register")
    @PermitAll
    public String showRegistrationForm(WebRequest request, Model model) {
        return "login-register";
    }

    @PostMapping("/register")
    @PermitAll
    public String registerForm(
            @RequestParam(name = "username", required = true) String username,
            @RequestParam(name = "password", required = true) String password,
//            @RequestParam(name = "name", required = true) String name,
//            @RequestParam(name = "surname", required = true) String surname,
            Model model
    ) {
        UserEntity getUser = usersService.getByLogin(username);

        if (getUser != null)
            return "redirect:/login";

        UserEntity user = new UserEntity();
        user.setLogin(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus("Active");
//        user.setUserName(name);
//        user.setUserSurname(surname);
//        user.setBalance(0.0);
        System.out.println(user);
        usersService.saveUser(user);

        UserEntity returnUser = usersService.getByLogin(username);
        UserHasRoleEntity role = new UserHasRoleEntity();
        role.setRoleIdRole(1);
        role.setUserIdUser(returnUser.getUserId());
        roleHasUsersService.saveRoleHasUsers(role);

        return "redirect:/";
    }
}
