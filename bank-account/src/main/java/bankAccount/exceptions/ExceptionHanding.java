package bankAccount.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHanding {

    @ExceptionHandler(CustomExceptions.AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(CustomExceptions.AccountNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.OnlyOneBankAccountPerUserException.class)
    public ResponseEntity<ErrorResponse> handleOnlyOneBankAccountPerUserException(CustomExceptions.OnlyOneBankAccountPerUserException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.NoContentFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoContentFoundException(CustomExceptions.NoContentFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.UnauthorizedAccountException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccountException(CustomExceptions.UnauthorizedAccountException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
}
