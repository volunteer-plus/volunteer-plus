package volunteer.plus.backend.service.security.impl;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "Пароль не відповідає вимогам складності";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
