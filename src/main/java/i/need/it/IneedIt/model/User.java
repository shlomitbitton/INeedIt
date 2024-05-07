package i.need.it.IneedIt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "user", schema = "ineedit")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "FIRST_NAME")
    @NotNull
    private String firstName;

    @Column(name = "LAST_NAME")
    @NotNull
    private String lastName;

    @Column(name = "USERNAME")
    @NotNull
    private String username;

    @Column(name = "PASSWORD")
    @NotNull
    private String password;


    @Column(name = "EMAIL")
    @NotNull
    private String email;

    @Column(name= "CREATED_AT")
    @NotNull
    private LocalDateTime dateCreated;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    private List<NeedingEvent> userNeeds;





}
