package uz.developer.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.developer.Daos.auth.AuthUserDao;
import uz.developer.models.AuthUser;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private final AuthUser authUser;
    private final AuthUserDao authUserDao;

    public CustomUserDetails(AuthUser authUser, AuthUserDao authUserDao) {
        this.authUser = authUser;
        this.authUserDao = authUserDao;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authUser.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getCode())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return authUser.getPassword();
    }

    @Override
    public String getUsername() {
        return authUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        Long id = authUser.getId();
        return authUserDao.getAccountStatus(id);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
