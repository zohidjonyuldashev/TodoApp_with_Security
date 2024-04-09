package uz.developer.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.developer.Daos.auth.AuthUserDao;
import uz.developer.config.dto.UserRegisterDTO;
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
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserRegisterDTO dto, RedirectAttributes redirectAttributes) {
        if (dto.username() == null || dto.username().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Username cannot be empty");
            return "redirect:/auth/register";
        }

        if (authUserDao.existsByUsername(dto.username())) {
            redirectAttributes.addFlashAttribute("error", "Username already exists");
            return "redirect:/auth/register";
        }

        AuthUser authUser = AuthUser.builder()
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .build();

        authUserDao.save(authUser);
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
