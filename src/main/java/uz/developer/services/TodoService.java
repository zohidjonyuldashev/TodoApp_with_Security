package uz.developer.services;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.developer.daos.auth.AuthUserDao;
import uz.developer.daos.todo.TodoDao;
import uz.developer.models.AuthUser;
import uz.developer.models.Todo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    private final TodoDao todoDao;
    private final AuthUserDao authUserDao;

    public TodoService(TodoDao todoDao, AuthUserDao authUserDao) {
        this.todoDao = todoDao;
        this.authUserDao = authUserDao;
    }

    public List<Todo> getTodosForCurrentUser(String username) {
        Optional<AuthUser> optionalAuthUser = authUserDao.findByUsername(username);
        if (optionalAuthUser.isPresent()) {
            AuthUser currentUser = optionalAuthUser.get();
            return todoDao.findByUserId(currentUser.getId());
        } else {
            return Collections.emptyList();
        }
    }

    public void createTodo(String username, Todo todo) {
        Optional<AuthUser> optionalAuthUser = authUserDao.findByUsername(username);
        if (optionalAuthUser.isPresent()) {
            AuthUser currentUser = optionalAuthUser.get();
            todo.setUserId(currentUser.getId());
            todoDao.save(todo);
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }

    public void updateTodo(String username, Todo todo) {
        AuthUser currentUser = authUserDao.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        if (!todoDao.existsByIdAndUserId(todo.getId(), currentUser.getId())) {
            throw new AuthenticationCredentialsNotFoundException("You are not authorized to update this todo");
        }
        todoDao.update(todo);
    }

    public void deleteTodoById(String username, Integer todoId) {
        AuthUser currentUser = authUserDao.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        if (!todoDao.existsByIdAndUserId(todoId, currentUser.getId())) {
            throw new AuthenticationCredentialsNotFoundException("You are not authorized to delete this todo");
        }
        todoDao.delete(todoId);
    }
}
