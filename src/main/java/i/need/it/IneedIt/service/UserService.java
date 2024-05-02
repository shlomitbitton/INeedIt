package i.need.it.IneedIt.service;

import i.need.it.IneedIt.dto.UserResponseDto;
import i.need.it.IneedIt.model.User;
import i.need.it.IneedIt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class UserService {

    private final UserRepository userRepository;
    private final SecurityFilterChain securityFilterChain;


    public UserService(UserRepository userRepository, SecurityFilterChain securityFilterChain) {
        this.userRepository = userRepository;
        this.securityFilterChain = securityFilterChain;
    }


    public UserResponseDto getUserDetailsById(String userId) {
        Optional<User> user = userRepository.findUserById(Long.valueOf(userId));
        return user.map(value -> UserResponseDto.builder()
                .userFirstName(value.getFirstName())
                .userLastName(value.getLastName())
                .userEmail(value.getEmail())
                .build()).orElse(null);
    }

    public Long authenticate(String username, String password) {
        Optional<User> user = userRepository.findUserByUsername(username).stream().findFirst();
        if(user.isPresent() && Objects.equals(user.get().getPassword(), password)){
            log.info("User had found");
            return user.get().getId();
        }
        return null;
    }
}
