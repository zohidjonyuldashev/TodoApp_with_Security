package uz.developer.Daos.auth;

import org.springframework.jdbc.core.RowMapper;
import uz.developer.models.AuthUser;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthUserRowMapper implements RowMapper<AuthUser> {
    @Override
    public AuthUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthUser authUser = new AuthUser();
        authUser.setId(rs.getLong("id"));
        authUser.setUsername(rs.getString("username"));
        authUser.setPassword(rs.getString("password"));
        authUser.setAccountNonLocked(rs.getBoolean("isAccountNonLocked"));
        return authUser;
    }
}
