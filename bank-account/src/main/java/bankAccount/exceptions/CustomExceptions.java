package bankAccount.exceptions;

public class CustomExceptions {

    public static class AccountNotFoundException extends Exception {
        public AccountNotFoundException(String message) {
            super(message);
        }
    }
}
