package currencyConversion.exceptions;

public class CustomExceptions {
    public static class NoContentFoundException extends Exception {
        public NoContentFoundException(String message) {
            super(message);
        }
    }
}
