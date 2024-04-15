package uz.developer.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.developer.daos.auth.AuthUserDao;
import uz.developer.dto.UserRegisterDTO;
import uz.developer.models.AuthUser;

import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthUserDao authUserDao;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthUserDao authUserDao, PasswordEncoder passwordEncoder) {
        this.authUserDao = authUserDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("dto", new UserRegisterDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("dto") UserRegisterDTO dto, BindingResult errors) {
        if (errors.hasErrors()) {
            return "auth/register";
        }

        AuthUser authUser = AuthUser.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        Long saved = authUserDao.save(authUser);
        authUserDao.setRole(saved);
        return "redirect:/auth/login";
    }


    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("errorMessage", error);
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "auth/logout";
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String users(Model model) {
        List<AuthUser> users = authUserDao.findAllUsers();
        model.addAttribute("users", users);
        return "auth/users";
    }

    @GetMapping("/admin/users/{userId}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public String block(@PathVariable("userId") Long userId) {
        AuthUser user = authUserDao.findById(userId);
        user.setAccountNonLocked(false);
        authUserDao.updateAccountStatus(userId, false);
        return "redirect:/auth/admin/users";
    }

    @GetMapping("/admin/users/{userId}/unblock")
    @PreAuthorize("hasRole('ADMIN')")
    public String unblock(@PathVariable("userId") Long userId) {
        AuthUser user = authUserDao.findById(userId);
        user.setAccountNonLocked(true);
        authUserDao.updateAccountStatus(userId, true);
        return "redirect:/auth/admin/users";
    }
}
