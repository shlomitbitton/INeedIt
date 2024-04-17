package i.need.it.IneedIt.service;

import i.need.it.IneedIt.dto.NeedingEventResponseDto;
import i.need.it.IneedIt.dto.UserResponseDto;
import i.need.it.IneedIt.model.User;
import i.need.it.IneedIt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserResponseDto getUserDetailsById(String userId) {
        Optional<User> user = userRepository.findUserById(Long.valueOf(userId));
        return user.map(value -> UserResponseDto.builder()
                .userFirstName(value.getFirstName())
                .userLastName(value.getLastName())
                .userEmail(value.getEmail())
                .build()).orElse(null);
    }
}
