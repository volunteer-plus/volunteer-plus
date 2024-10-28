package volunteer.plus.backend.service.email.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import volunteer.plus.backend.exceptions.EmailException;
import volunteer.plus.backend.service.email.EmailContentEvaluationService;
import volunteer.plus.backend.util.JacksonUtil;

import java.util.Map;

@Slf4j
@Service
public class EmailContentEvaluationServiceImpl implements EmailContentEvaluationService {
    private final TemplateEngine templateEngine;

    public EmailContentEvaluationServiceImpl(@Qualifier("htmlTemplateEngine") TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String evaluateContent(String html, String data) {
        try {
            if (!StringUtils.hasText(data)) {
                return html;
            }

            log.info("Evaluating email template content ...");

            final Map<String, Object> templateVariables = JacksonUtil.deserialize(data, new TypeReference<>(){});

            final var ctx = new Context();
            templateVariables.forEach(ctx::setVariable);

            return templateEngine.process(html, ctx);
        } catch (Exception e) {
            throw new EmailException("Error has occurred during email content processing");
        }
    }
}
