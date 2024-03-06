package utility.microservices.transferservice.exceptions;

public class CustomExceptions {

    public static class YouCantDoThatException extends Exception {
        public YouCantDoThatException(String message) {
            super(message);
        }
    }

}
