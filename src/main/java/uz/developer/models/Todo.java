package uz.developer.models;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Todo {
    private Integer id;
    private String title;
    private String priority;
    private String createdAt;
    private Long userId;


    @Builder
    public Todo(Integer id, String title, String priority, Long userId) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        this.userId = userId;
    }
}
