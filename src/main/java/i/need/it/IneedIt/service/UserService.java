package i.need.it.IneedIt.service;

import i.need.it.IneedIt.dto.NewUserRegistrationRequestDto;
import i.need.it.IneedIt.dto.UserRegistrationResponseDto;
import i.need.it.IneedIt.dto.UserResponseDto;
import i.need.it.IneedIt.model.User;
import i.need.it.IneedIt.repository.UserRepository;
import i.need.it.IneedIt.utils.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Optional;


@Slf4j
@Component
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserResponseDto getUserDetailsById(String userId) {
        Optional<User> user = userRepository.findUserByUserId(Long.valueOf(userId));
        return user.map(value -> UserResponseDto.builder()
                .userFirstName(value.getFirstName())
                .userLastName(value.getLastName())
                .userEmail(value.getEmail())
                .build()).orElse(null);
    }

    public Long authenticate(String username, String password) {
        Optional<User> user = userRepository.findUserByUsernameIgnoreCase(username).stream().findFirst();
        if(user.isPresent() && BCrypt.checkpw(password, user.get().getPassword())) {
                log.info("User had found");
                return user.get().getUserId();
        }
        return null;
    }

    public void hashExistingPasswords(String plainTextPassword, String username){
        User findUser = userRepository.findUserByUsername(username);
        log.info("findUser " + findUser);
        findUser.setPassword(BCrypt.hashpw(plainTextPassword, BCrypt.gensalt()));
        userRepository.save(findUser);
    }

    private String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public UserRegistrationResponseDto addNewUser(NewUserRegistrationRequestDto newUserRegistrationRequestDto) {
        UserRegistrationResponseDto populateDto = new UserRegistrationResponseDto();
        Long userId = null;
        String status =  "fail";
        String message = "Username is invalid.";

        try {
            if (newUserRegistrationRequestDto.getUsername().isBlank() || newUserRegistrationRequestDto.getUsername().contains(" ")
                || newUserRegistrationRequestDto.getUsername().length() < 5 ||
                        newUserRegistrationRequestDto.getUsername().length() > 20){
                     log.info("Username should have no spaces and between 5 to 20 characters.");
            }else if(userRepository.findUserByUsername(newUserRegistrationRequestDto.getUsername()) == null) {
                    // Create new user as username does not exist
                    User newUser = new User();
                    newUser.setUsername(newUserRegistrationRequestDto.getUsername());
                    newUser.setPassword(hashPassword(newUserRegistrationRequestDto.getPassword()));
                    newUser.setEmail("");
                    newUser.setFirstName("");
                    newUser.setLastName("");
                    newUser.setDateCreated(LocalDateTime.now());
                    userRepository.save(newUser);

                    userId = newUser.getUserId(); // Get the user ID after saving the new user
                    status = "success";
                    message = "User registered successfully.";
                    try{
                        String TO_EMAIL_ADDRESS = "shlomitbitton12@gmail.com";
                        EmailUtil.sendEmail(TO_EMAIL_ADDRESS, "A new user had registered!", "Thanks for registering with us!");
                    }catch(Exception e){
                        log.error("Couldnt send email");
                    }
            } else if(userRepository.findUserByUsername(newUserRegistrationRequestDto.getUsername()) != null){
                    message = "Username already exists.";
                    log.info("Username already exists.");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        populateDto.setUserId(userId); // Set userId, will be null if registration failed
        populateDto.setStatus(status);
        populateDto.setMessage(message);
        return populateDto;
    }

}
