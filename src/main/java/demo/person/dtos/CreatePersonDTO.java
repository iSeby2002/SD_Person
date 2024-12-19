package demo.person.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreatePersonDTO {
    private String name;
    private String username;
    private String password;
    private String confirmPassword;
    private String role;
}
