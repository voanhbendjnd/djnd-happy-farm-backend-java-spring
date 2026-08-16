package djnd.happy.farm.service;

import java.io.Serial;

public class InvalidPasswordException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public InvalidPasswordException() {
        super("Password is invalid");
    }
}
