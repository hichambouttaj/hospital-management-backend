package com.ghopital.projet.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

import jakarta.validation.ClockProvider;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.messageinterpolation.ExpressionLanguageFeatureLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PasswordValidator.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class PasswordValidatorTest {
    @Autowired
    private PasswordValidator passwordValidator;

    /**
     * Method under test:
     * {@link PasswordValidator#isValid(String, ConstraintValidatorContext)}
     */
    @Test
    void testIsValid() {
        ClockProvider clockProvider = mock(ClockProvider.class);
        assertFalse(passwordValidator.isValid("iloveyou",
                new ConstraintValidatorContextImpl(clockProvider, PathImpl.createRootPath(), null,
                        "Constraint Validator Payload", ExpressionLanguageFeatureLevel.DEFAULT,
                        ExpressionLanguageFeatureLevel.DEFAULT)));
    }

    /**
     * Method under test:
     * {@link PasswordValidator#isValid(String, ConstraintValidatorContext)}
     */
    @Test
    void testIsValid2() {
        ClockProvider clockProvider = mock(ClockProvider.class);
        assertFalse(passwordValidator.isValid(null,
                new ConstraintValidatorContextImpl(clockProvider, PathImpl.createRootPath(), null,
                        "Constraint Validator Payload", ExpressionLanguageFeatureLevel.DEFAULT,
                        ExpressionLanguageFeatureLevel.DEFAULT)));
    }

    /**
     * Method under test: {@link PasswordValidator#initialize(PasswordValid)}
     */
    @Test
    void testInitialize() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Diffblue AI was unable to find a test

        PasswordValidator passwordValidator = new PasswordValidator();
        passwordValidator.initialize(null);
        assertFalse(passwordValidator.isValid("iloveyou", null));
    }

    /**
     * Method under test: {@link PasswordValidator#initialize(PasswordValid)}
     */
    @Test
    void testInitialize2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Diffblue AI was unable to find a test

        PasswordValidator passwordValidator = new PasswordValidator();
        passwordValidator.initialize(mock(PasswordValid.class));
        assertFalse(passwordValidator.isValid("iloveyou", null));
    }
}
