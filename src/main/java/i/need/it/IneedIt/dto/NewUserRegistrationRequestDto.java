package i.need.it.IneedIt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUserRegistrationRequestDto {

    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String username;
    private String password;

}
