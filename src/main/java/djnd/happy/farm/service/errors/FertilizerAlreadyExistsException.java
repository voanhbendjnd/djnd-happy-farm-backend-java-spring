package djnd.happy.farm.service.errors;

import djnd.happy.farm.web.rest.errors.ConflictAlertException;
import djnd.happy.farm.web.rest.errors.ErrorConstants;

import java.io.Serial;

public class FertilizerAlreadyExistsException extends ConflictAlertException {
    @Serial
    private static final long serialVersionUID = 1L;
    public FertilizerAlreadyExistsException(String message,  String errorKey) {
        super(ErrorConstants.CONFLICT,message, "fertilizerManagement", errorKey);
    }
}
