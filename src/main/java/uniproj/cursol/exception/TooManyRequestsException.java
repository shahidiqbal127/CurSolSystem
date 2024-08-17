
package uniproj.cursol.exception;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException() {
        super("Too Many Requests - Rate limit exceeded.");
    }

    public TooManyRequestsException(String message) {
        super(message);
    }
}
