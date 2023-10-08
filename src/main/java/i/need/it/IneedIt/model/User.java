package i.need.it.IneedIt.model;

import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class User {

    private String firstName;
    private String lastName;
    private String username;
    private String email;

    @OneToMany
    private List<Needing> usersNeeds;



}
