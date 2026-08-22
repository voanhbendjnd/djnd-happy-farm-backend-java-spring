package djnd.happy.farm.service.errors;

import djnd.happy.farm.rest.errors.ErrorConstants;
import djnd.happy.farm.rest.errors.NotFoundAlertException;

import java.io.Serial;

public class SpeciesNotFoudException extends NotFoundAlertException {
    @Serial
    private static final long serialVersionUID = 1L;
    public SpeciesNotFoudException(){
        super(ErrorConstants.NOT_FOUND, "Species taxonomy not found!", "taxonomyManagement", "speciesnotfound");
    }

}
