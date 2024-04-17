package i.need.it.IneedIt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class UserResponseDto {

    private String userFirstName;
    private String userLastName;
    private String userEmail;


}
