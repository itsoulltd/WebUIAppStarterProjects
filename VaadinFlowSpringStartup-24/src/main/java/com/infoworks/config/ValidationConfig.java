package com.infoworks.config;

import com.vaadin.flow.component.UI;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ValidationConfig {

    public static final String BEAN_VALIDATION_KEY = "Bean_Validation_Key";

    /**
     * ValidatorFactory: Implementations are thread-safe and instances are typically cached and reused.
     * Validator: Validates bean instances. Implementations of this interface must be thread-safe.
     * Since this is a session based UI application, so validator object can be reused from session.
     * @return
     */
    public static Validator getValidator() {
        Object existing = UI.getCurrent().getSession().getAttribute(BEAN_VALIDATION_KEY);
        if (existing == null) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            UI.getCurrent().getSession().setAttribute(BEAN_VALIDATION_KEY, validator);
            existing = validator;
        }
        return (Validator) existing;
    }

    public static <T> String[] validate(Validator validator, T target) {
        if (target == null) return new String[]{"target param must not be null!"};
        List<String> messages = new ArrayList<>();
        Set<ConstraintViolation<T>> violations = validator.validate(target);
        for (ConstraintViolation<T> violation : violations) {
            messages.add(violation.getMessage());
        }
        return messages.toArray(new String[0]);
    }

    public static <T> String validateWithMessage(Validator validator, T target) {
        String[] messages = validate(validator, target);
        return messages.length > 0
                ? String.join("; \n", messages)
                : null;
    }
}
