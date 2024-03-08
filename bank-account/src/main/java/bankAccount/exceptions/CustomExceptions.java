package bankAccount.exceptions;

public class CustomExceptions {

    public static class AccountNotFoundException extends Exception {
        public AccountNotFoundException(String message) {
            super(message);
        }
    }

    public static class OnlyOneBankAccountPerUserException extends Exception {
        public OnlyOneBankAccountPerUserException(String message) {
            super(message);
        }
    }

    public static class NoContentFoundException extends Exception {
        public NoContentFoundException(String message) {
            super(message);
        }
    }

    public static class UnauthorizedAccountException extends Exception {
        public UnauthorizedAccountException(String message) {
            super(message);
        }
    }

    public static class YouCantDoThatException extends Exception {
        public YouCantDoThatException(String message) {
            super(message);
        }
    }
}
