package i.need.it.IneedIt.controller;

import i.need.it.IneedIt.dto.NewUserRegistrationRequestDto;
import i.need.it.IneedIt.dto.UserRegistrationResponseDto;
import i.need.it.IneedIt.dto.UserResponseDto;
import i.need.it.IneedIt.dto.LoginRequestDto;
import i.need.it.IneedIt.service.JwtService;
import i.need.it.IneedIt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JwtService GenerateToken;

    public UserController(UserService userService, JwtService GenerateToken) {
        this.userService = userService;
        this.GenerateToken = GenerateToken;
    }

    @GetMapping(value="/user-details")
    public UserResponseDto getUserDetailsById(@RequestParam(value = "user-id") String userId){
        return userService.getUserDetailsById(userId);
    }

    @PostMapping(value="/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDto loginRequestDto){
        log.info("Login endpoint called");
        Long userId = userService.authenticate(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        if (userId != null) {
            String token = GenerateToken.generateToken(userId.toString());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("user-id", String.valueOf(userId));
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping(value="/user-registration")
    public UserRegistrationResponseDto userRegistration(@RequestBody NewUserRegistrationRequestDto newUserRegistrationRequestDto){
        log.info("Registering new user");
        return userService.addNewUser(newUserRegistrationRequestDto);
    }

   


}
