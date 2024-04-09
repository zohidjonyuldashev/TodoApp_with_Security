package uz.developer.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AuthRole {
    private Long id;
    private String name;
    private String code;
}
