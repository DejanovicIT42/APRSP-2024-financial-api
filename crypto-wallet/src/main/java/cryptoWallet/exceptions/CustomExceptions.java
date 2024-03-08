package cryptoWallet.exceptions;

public class CustomExceptions {

    public static class NotFoundException extends Exception {
        public NotFoundException(String message) {
            super(message);
        }
    }

    public static class OnlyOneWalletPerUserException extends Exception {
        public OnlyOneWalletPerUserException(String message) {
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
