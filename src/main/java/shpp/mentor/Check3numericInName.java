package shpp.mentor;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = Check3InName.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Check3numericInName {
    String message() default "No 3 in field Name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}

