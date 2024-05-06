package i.need.it.IneedIt.exception;

import i.need.it.IneedIt.dto.StatusResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        // Create the error response object
        StatusResponseDto errorResponse = new StatusResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());

        // Return the error response with a 500 status code
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
