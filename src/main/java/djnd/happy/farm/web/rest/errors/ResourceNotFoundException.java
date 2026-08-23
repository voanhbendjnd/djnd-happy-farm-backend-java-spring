package djnd.happy.farm.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.io.Serial;

public class ResourceNotFoundException extends AbstractThrowableProblem {
    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(ErrorConstants.NOT_FOUND, message, Status.NOT_FOUND);
    }
}
