package i.need.it.IneedIt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationResponseDto {

    private String status; //success or fail
    private Long userId;
    private String message; //'User registered successfully.'

}
