package djnd.happy.farm.service.errors;

import djnd.happy.farm.rest.errors.BadRequestAlertException;
import djnd.happy.farm.rest.errors.ErrorConstants;

import java.io.Serial;

public class EmailAlreadyUsedException extends BadRequestAlertException {
    @Serial
    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email address already in use", "userManagement", "emailexists");
    }
}
