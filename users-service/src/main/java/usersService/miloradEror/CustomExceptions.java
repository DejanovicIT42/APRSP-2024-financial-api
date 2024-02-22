package usersService.miloradEror;

public class CustomExceptions {

    public static class EmailAlreadyExistsException extends Exception {
        public EmailAlreadyExistsException(String message){
            super(message);
        }
    }

    public static class OwnerAlreadyExistsException extends Exception {
        public OwnerAlreadyExistsException(String message){
            super(message);
        }
    }

    public static class UnauthorizedAccountException extends Exception{
        public UnauthorizedAccountException(String message){
            super(message);
        }
    }

    public static class NoContentFoundException extends Exception{
        public NoContentFoundException(String message){
            super(message);
        }
    }
}
