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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationError", "");
        return "login-register";
    }

    @PostMapping("/register")
    @PermitAll
    public String registerForm(
            @RequestParam(name = "username", required = true)
            @Email(message = "Введите правильный адрес электронной почты") String username,
            @RequestParam(name = "password", required = true)
            @NotBlank(message = "Пароль не может быть пустым") String password,
            Model model) {

        UserEntity getUser = usersService.getByLogin(username);

        if (getUser != null){
            model.addAttribute("registrationError", "Пользователь с таким логином уже существует.");
            return "login-register";
        }

        UserEntity user = new UserEntity();
        user.setLogin(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus("Active");
        usersService.saveUser(user);

        UserEntity returnUser = usersService.getByLogin(username);
        UserHasRoleEntity role = new UserHasRoleEntity();
        role.setRoleIdRole(1);
        role.setUserIdUser(returnUser.getUserId());
        roleHasUsersService.saveRoleHasUsers(role);

        return "redirect:/";
    }
}


