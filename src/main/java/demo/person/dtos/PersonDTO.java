package demo.person.dtos;

import demo.person.entities.Person;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDTO {
    private Person person;
    private String msg;
    private String token;
    private long expiresIn;
}
