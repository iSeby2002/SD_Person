package demo.person.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UpdatePersonDTO {
    private UUID id;
    private String name;
    private String username;
    private String role;
}
