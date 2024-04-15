package uz.developer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRegisterDTO {
    @NotBlank(message = "Username can not be empty")
    private String username;
    @NotBlank(message = "Password can not be empty")
    private String password;
}
