package potatoe.exception;

public class PotatoeRuntimeException extends RuntimeException {

    public PotatoeRuntimeException(final String message) {
        super(message);
    }

    public PotatoeRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
