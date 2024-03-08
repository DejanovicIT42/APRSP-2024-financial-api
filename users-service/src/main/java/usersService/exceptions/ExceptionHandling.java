package usersService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(CustomExceptions.EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(CustomExceptions.EmailAlreadyExistsException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.OwnerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleOwnerAlreadyExistsException(CustomExceptions.OwnerAlreadyExistsException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.UnauthorizedAccountException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccountException(CustomExceptions.UnauthorizedAccountException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.NoContentFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoContentFoundException(CustomExceptions.NoContentFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.UserDoesntExistException.class)
    public ResponseEntity<ErrorResponse> handleUserDoesntExistException(CustomExceptions.UserDoesntExistException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
