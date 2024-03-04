package cryptoWallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHanding {

    @ExceptionHandler(CustomExceptions.NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(CustomExceptions.NotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.OnlyOneWalletPerUserException.class)
    public ResponseEntity<ErrorResponse> handleOnlyOneWalletPerUserException(CustomExceptions.OnlyOneWalletPerUserException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.UnauthorizedAccountException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccountException(CustomExceptions.UnauthorizedAccountException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.YouCantDoThatException.class)
    public ResponseEntity<ErrorResponse> handleYouCantDoThatException(CustomExceptions.YouCantDoThatException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
