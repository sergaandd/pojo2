package shpp.mentor;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class GetValidator {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final jakarta.validation.Validator validator = factory.getValidator();

    private GetValidator() {
    }

    public static Set<ConstraintViolation<Pojo>> getValidateAge(Pojo pojoTest) {
        return validator.validate(pojoTest);

    }
}
