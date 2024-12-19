package demo.person.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ChangePasswordDTO {
    private UUID personId;
    private String newPassword;
    private String confirmNewPassword;
}
