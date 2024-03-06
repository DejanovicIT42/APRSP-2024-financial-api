package utility.microservices.transferservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandling {

    @ExceptionHandler(CustomExceptions.YouCantDoThatException.class)
    public ResponseEntity<ErrorResponse> handleYouCantDoThatException(CustomExceptions.YouCantDoThatException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
