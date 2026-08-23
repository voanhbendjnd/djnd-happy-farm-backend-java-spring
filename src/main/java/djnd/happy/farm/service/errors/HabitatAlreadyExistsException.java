package djnd.happy.farm.service.errors;

import djnd.happy.farm.web.rest.errors.ConflictAlertException;
import djnd.happy.farm.web.rest.errors.ErrorConstants;

import java.io.Serial;

public class HabitatAlreadyExistsException extends ConflictAlertException {
    @Serial
    private static final long serialVersionUID = 1L;

    public HabitatAlreadyExistsException(String defaultMessage, String entityName, String errorKey) {
        super(ErrorConstants.CONFLICT,defaultMessage, entityName, errorKey);
    }
}
