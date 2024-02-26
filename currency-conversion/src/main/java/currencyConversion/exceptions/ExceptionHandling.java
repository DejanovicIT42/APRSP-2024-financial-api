package currencyConversion.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ExceptionHandling {
    @ExceptionHandler(CustomExceptions.NoContentFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoContentFoundException(CustomExceptions.NoContentFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorResponse);
    }
}
