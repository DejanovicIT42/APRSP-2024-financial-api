package usersService.exceptions;

public class CustomExceptions {

    public static class EmailAlreadyExistsException extends Exception {
        public EmailAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class OwnerAlreadyExistsException extends Exception {
        public OwnerAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class UnauthorizedAccountException extends Exception {
        public UnauthorizedAccountException(String message) {
            super(message);
        }
    }

    public static class NoContentFoundException extends Exception {
        public NoContentFoundException(String message) {
            super(message);
        }
    }

    public static class UserDoesntExistException extends Exception {
        public UserDoesntExistException(String message) {
            super(message);
        }
    }
}
