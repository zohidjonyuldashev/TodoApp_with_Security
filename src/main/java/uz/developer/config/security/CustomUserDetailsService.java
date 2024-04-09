package uz.developer.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.developer.Daos.auth.AuthRoleDao;
import uz.developer.Daos.auth.AuthUserDao;
import uz.developer.models.AuthRole;
import uz.developer.models.AuthUser;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthUserDao authUserDao;
    private final AuthRoleDao authRoleDao;

    public CustomUserDetailsService(AuthUserDao authUserDao, AuthRoleDao authRoleDao) {
        this.authUserDao = authUserDao;
        this.authRoleDao = authRoleDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authUserDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username '%s'".formatted(username)));
        Long userID = authUser.getId();
        List<AuthRole> roles = authRoleDao.findAllByUserId(userID);
        authUser.setRoles(roles);
        return new CustomUserDetails(authUser, authUserDao);
    }
}
