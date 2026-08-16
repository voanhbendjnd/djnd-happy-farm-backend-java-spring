package djnd.happy.farm.service;

import java.io.Serial;

public class AccountAlreadyUsedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AccountAlreadyUsedException(String message) {
        super(message);
    }
}
