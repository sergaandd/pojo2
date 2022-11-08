package shpp.mentor;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class Check3InName implements ConstraintValidator<Check3numericInName, String> {

    @Override
    public void initialize(Check3numericInName name) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !s.contains("3");
    }

}
