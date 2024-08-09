package com.fonctionpublique.validators;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import com.fonctionpublique.exception.ObjectValidationException;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ObjectValidator<T> {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public void validate(T objectValidate) {
        Set<ConstraintViolation<T>> validations = validator.validate(objectValidate);
        if (!validations.isEmpty()) {
            Set<String> errorMessages = validations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet());
            throw new ObjectValidationException(errorMessages, objectValidate.getClass().getName());
        }
    }
}
