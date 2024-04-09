package uz.developer.Daos.auth;

import lombok.NonNull;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import uz.developer.models.AuthUser;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class AuthUserDaoImpl implements AuthUserDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AuthUserDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Long save(@NonNull AuthUser authUser) {
        String sql = "insert into authUser(username, password) values(:username, :password);";
        var paramSource = new MapSqlParameterSource()
                .addValue("username", authUser.getUsername())
                .addValue("password", authUser.getPassword());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, paramSource, keyHolder, new String[]{"id"});
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void setRole(Long userId) {
        String sql = "insert into authuser_authrole(user_id, role_id) values(:userID, 2)";
        namedParameterJdbcTemplate.update(sql, Map.of("userID", userId));
    }

    @Override
    public List<AuthUser> findAllUsers() {
        String sql = "select au.* from authuser au inner join authuser_authrole auar on au.id = auar.user_id where auar.role_id = 2 order by au.id;";
        return namedParameterJdbcTemplate.query(sql, new AuthUserRowMapper());
    }

    @Override
    public void updateAccountStatus(Long id, boolean isAccountNonLocked) {
        String sql = "update authuser set isaccountnonlocked = :isAccountNonLocked where id = :id;";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("isAccountNonLocked", isAccountNonLocked)
                .addValue("id", id);
        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public Boolean getAccountStatus(Long id) {
        String sql = "select isaccountnonlocked from authuser where id = :id;";
        return namedParameterJdbcTemplate.queryForObject(sql, Map.of("id", id), Boolean.class);
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "select count(*) from authuser where username = :username;";
        return namedParameterJdbcTemplate.queryForObject(sql, Map.of("username", username), Integer.class) > 0;
    }

    @Override
    public Optional<AuthUser> findByUsername(@NonNull String username) {
        var sql = "select * from authuser t where t.username = :username";
        var paramSource = new MapSqlParameterSource().addValue("username", username);
        var rowMapper = BeanPropertyRowMapper.newInstance(AuthUser.class);
        try {
            var authUser = namedParameterJdbcTemplate.queryForObject(sql, paramSource, rowMapper);
            return Optional.of(Objects.requireNonNull(authUser));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public AuthUser findById(Long id) {
        String sql = "select * from authuser t where t.id = :id";
        var paramSource = new MapSqlParameterSource().addValue("id", id);
        var rowMapper = BeanPropertyRowMapper.newInstance(AuthUser.class);
        return namedParameterJdbcTemplate.queryForObject(sql, paramSource, rowMapper);
    }
}