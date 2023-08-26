package uz.backall.config.jwt;

public class JwtUsernameException extends RuntimeException {
    public JwtUsernameException(String message) {
        super(message);
    }

    public JwtUsernameException(String message, Throwable cause) {
        super(message, cause);
    }
}
