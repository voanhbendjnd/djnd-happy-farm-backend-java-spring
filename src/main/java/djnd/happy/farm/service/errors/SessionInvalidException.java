package djnd.happy.farm.service.errors;

import djnd.happy.farm.web.rest.errors.ErrorConstants;
import djnd.happy.farm.web.rest.errors.UnauthorizedAlertException;

import java.io.Serial;

public class SessionInvalidException extends UnauthorizedAlertException {
    @Serial
    private static final long serialVersionUID = 1L;
    public SessionInvalidException() {
        super(ErrorConstants.INVALID_PASSWORD_TYPE, "Session invalid", "authManagement", "invalidsession");
    }
}
