package uz.developer.Daos.auth;

import uz.developer.models.AuthUser;

import java.util.List;
import java.util.Optional;

public interface AuthUserDao {
    Optional<AuthUser> findByUsername(String username);

    AuthUser findById(Long id);

    Long save(AuthUser authUser);

    void setRole(Long userId);

    List<AuthUser> findAllUsers();

    void updateAccountStatus(Long id, boolean status);

    Boolean getAccountStatus(Long id);

    boolean existsByUsername(String username);
}
