package djnd.happy.farm.service;

import djnd.happy.farm.rest.errors.BadRequestAlertException;
import djnd.happy.farm.rest.errors.ErrorConstants;

import java.io.Serial;

public class LoginAlreadyUsedException extends BadRequestAlertException {
    @Serial
    private static final long serialVersionUID = 1L;

    public LoginAlreadyUsedException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Login already in use", "userManagement", "userexists");
    }
}
