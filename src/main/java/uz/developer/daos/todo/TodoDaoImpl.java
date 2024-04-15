package uz.developer.daos.todo;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import uz.developer.models.Todo;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component
public class TodoDaoImpl implements TodoDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TodoDaoImpl(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    @Override
    public void save(Todo todo) {
        String sql = "insert into todos(title, priority, createdAt, userId) values(:title, :priority, :createdAt, :userId);";
        var source = new BeanPropertySqlParameterSource(todo);
        namedParameterJdbcTemplate.update(sql, source);
    }

    @Override
    public void update(Todo todo) {
        var sql = "update todos set title = :title, priority = :priority where id = :id;";
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(todo);
        namedParameterJdbcTemplate.update(sql, source);
    }

    @Override
    public void delete(Integer id) {
        String sql = "delete from todos where id = :id;";
        namedParameterJdbcTemplate.update(sql, Map.of("id", id));
    }

    @Override
    public Todo findById(Integer id) {
        String sql = "select * from todos t where id = :id;";
        return namedParameterJdbcTemplate.queryForObject(sql, Map.of("id", id), new TodoRowMapper());
    }

    @Override
    public List<Todo> findByUserId(Long userId) {
        String sql = "SELECT * FROM todos WHERE userId = :userId order by id desc;";
        return namedParameterJdbcTemplate.query(sql, Map.of("userId", userId), new TodoRowMapper());
    }

    @Override
    public boolean existsByIdAndUserId(Integer id, Long userId) {
        String sql = "SELECT COUNT(*) FROM todos WHERE id = :id AND userId = :userId";
        Map<String, Object> params = Map.of("id", id, "userId", userId);
        Integer count = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
}
