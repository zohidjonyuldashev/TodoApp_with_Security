package uz.developer.Daos.auth;

import lombok.NonNull;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import uz.developer.models.AuthRole;

import java.util.Collections;
import java.util.List;

@Component
public class AuthRoleDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AuthRoleDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<AuthRole> findAllByUserId(@NonNull Long userID) {
        var sql = "select ar.* from authuser_authrole auar inner join authrole ar on auar.role_id = ar.id where auar.user_id = :userID";
        var paramSource = new MapSqlParameterSource().addValue("userID", userID);
        var rowMapper = BeanPropertyRowMapper.newInstance(AuthRole.class);
        try {
            return namedParameterJdbcTemplate.query(sql, paramSource, rowMapper);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}