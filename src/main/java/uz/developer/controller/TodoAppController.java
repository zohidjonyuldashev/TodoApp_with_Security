package uz.developer.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import uz.developer.config.security.CustomUserDetails;
import uz.developer.services.TodoService;
import uz.developer.Daos.auth.AuthUserDao;
import uz.developer.Daos.todo.TodoDao;
import uz.developer.models.AuthUser;
import uz.developer.models.Todo;

import java.util.List;
import java.util.Optional;

@Controller
public class TodoAppController {
    private final TodoService todoService;
    private final TodoDao todoDao;
    private final AuthUserDao authUserDao;

    public TodoAppController(TodoService todoService, TodoDao todoDao, AuthUserDao authUserDao) {
        this.todoService = todoService;
        this.todoDao = todoDao;
        this.authUserDao = authUserDao;
    }


    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/todos")
    public String todos(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        List<Todo> todos = todoService.getTodosForCurrentUser(username);
        model.addAttribute("todos", todos);
        return "todo/todos";
    }

    @GetMapping("/todos/new")
    public String createTodo(Model model) {
        Todo todo = new Todo();
        model.addAttribute("todo", todo);
        return "todo/create";
    }

    @PostMapping("/todos/new")
    public String saveTodo(@AuthenticationPrincipal CustomUserDetails userDetails, @ModelAttribute("todo") Todo todo) {
        String username = userDetails.getUsername();
        Todo newTodo = Todo.builder()
                .title(todo.getTitle())
                .priority(todo.getPriority())
                .userId(todo.getUserId())
                .build();
        todoService.createTodo(username, newTodo);
        return "redirect:/todos";
    }

    @GetMapping("/todos/{todoId}/edit")
    public String editTodoForm(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("todoId") Integer todoId, Model model) {
        Optional<AuthUser> username = authUserDao.findByUsername(userDetails.getUsername());
        if (username.isPresent()) {
            AuthUser authUser = username.get();
            Todo todo = todoDao.findById(todoId);
            if (todo.getUserId().equals(authUser.getId())) {
                model.addAttribute("todo", todo);
                return "todo/update";
            }
        }
        return "404";
    }


    @PostMapping("/todos/{todoId}/edit")
    public String updateTodo(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("todoId") Integer todoId, @ModelAttribute("todo") Todo todo) {
        String username = userDetails.getUsername();
        todo.setId(todoId);
        todoService.updateTodo(username, todo);
        return "redirect:/todos";
    }

    @GetMapping("/todos/{todoId}/delete")
    public String deleteTodo(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("todoId") Integer todoId) {
        String username = userDetails.getUsername();
        todoService.deleteTodoById(username, todoId);
        return "redirect:/todos";
    }
}
