package djnd.happy.farm.service.errors;

import djnd.happy.farm.web.rest.errors.ErrorConstants;
import djnd.happy.farm.web.rest.errors.NotFoundAlertException;

import java.io.Serial;

public class NotFoundExceptionGlobal extends NotFoundAlertException {
    @Serial
    private static final long serialVersionUID = 1L;


    public NotFoundExceptionGlobal(String message, String entityName, String errorKey) {
        super(ErrorConstants.NOT_FOUND,message, entityName, errorKey);
    }
}
