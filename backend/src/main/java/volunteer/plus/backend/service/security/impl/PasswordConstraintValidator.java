package volunteer.plus.backend.service.security.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.Arrays;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private final PasswordValidator validator;

    public PasswordConstraintValidator() {
        this.validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 30),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule(),
                new IllegalRegexRule(".*(.)\\1{2}.*")
        ));
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            // null обробляйте окремо через @NotNull якщо потрібно
            return true;
        }

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }

        // Замінюємо стандартне повідомлення деталями Passay
        context.disableDefaultConstraintViolation();
        String messages = String.join("; ", validator.getMessages(result));
        context.buildConstraintViolationWithTemplate(messages)
                .addConstraintViolation();
        return false;
    }
}
