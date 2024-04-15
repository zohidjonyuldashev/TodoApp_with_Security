package uz.developer.daos.todo;

import uz.developer.models.Todo;

import java.util.List;

public interface TodoDao {
    void save(Todo todo);

    void update(Todo todo);

    void delete(Integer id);

    Todo findById(Integer id);

    List<Todo> findByUserId(Long id);

    boolean existsByIdAndUserId(Integer id, Long userId);
}
