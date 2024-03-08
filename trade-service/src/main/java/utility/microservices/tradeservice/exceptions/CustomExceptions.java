package utility.microservices.tradeservice.exceptions;

public class CustomExceptions {

    public static class NoContentFoundException extends Exception {
        public NoContentFoundException(String message) {
            super(message);
        }
    }

    public static class YouCantDoThatException extends Exception {
        public YouCantDoThatException(String message) {
            super(message);
        }
    }
}
