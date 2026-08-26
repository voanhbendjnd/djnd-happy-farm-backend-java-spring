package djnd.happy.farm.service.errors;

import djnd.happy.farm.web.rest.errors.BadRequestAlertException;
import djnd.happy.farm.web.rest.errors.ErrorConstants;

import java.io.Serial;

public class BadRequestExceptionGlobal extends BadRequestAlertException {
    @Serial
    private static final long serialVersionUID = 1L;

    public BadRequestExceptionGlobal(String message, String entityName, String errorKey) {
        super(ErrorConstants.BAD_REQUEST_TYPE, message, entityName, errorKey);
    }
}
