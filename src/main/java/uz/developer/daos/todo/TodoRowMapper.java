package uz.developer.daos.todo;

import org.springframework.jdbc.core.RowMapper;
import uz.developer.models.Todo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TodoRowMapper implements RowMapper<Todo> {
    @Override
    public Todo mapRow(ResultSet rs, int rowNum) throws SQLException {
        Todo todo = new Todo();
        todo.setId(rs.getInt("id"));
        todo.setTitle(rs.getString("title"));
        todo.setPriority(rs.getString("priority"));
        todo.setCreatedAt(rs.getString("createdAt"));
        todo.setUserId(rs.getLong("userId"));
        return todo;
    }
}
