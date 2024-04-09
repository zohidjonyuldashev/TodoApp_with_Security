package uz.developer.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUser {
    private Long Id;
    private String username;
    private String password;
    private List<AuthRole> roles;
    private boolean isAccountNonLocked;
}