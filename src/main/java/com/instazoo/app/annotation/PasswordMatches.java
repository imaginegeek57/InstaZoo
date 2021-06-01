package com.instazoo.app.annotation;

import com.instazoo.app.validation.PasswordMatchesValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {

    String message() default "Password do not be match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
