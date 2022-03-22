package exception;

public class PotatoeException extends Exception {
    
    public PotatoeException(final String message) {
        super(message);
    }

    public PotatoeException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
