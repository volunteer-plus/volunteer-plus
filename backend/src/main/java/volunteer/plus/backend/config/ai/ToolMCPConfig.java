package volunteer.plus.backend.config.ai;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Configuration
public class ToolMCPConfig implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public ToolCallbackProvider volunteerMCPTools(final List<ToolCallback> tools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(tools)
                .build();
    }

    @Bean
    public List<ToolCallback> tools() {
        final List<Object> toolBeans = getToolBeans();
        return toolBeans.stream()
                .map(ToolCallbacks::from)
                .map(Arrays::asList)
                .flatMap(Collection::stream)
                .toList();
    }

    private List<Object> getToolBeans() {
        final ConfigurableListableBeanFactory bf = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();

        final List<Object> toolBeans = new ArrayList<>();
        for (final String name : bf.getBeanDefinitionNames()) {
            final BeanDefinition bd = bf.getBeanDefinition(name);
            final String beanClassName = bd.getBeanClassName();

            if (!StringUtils.hasText(beanClassName)) {
                continue;
            }

            // load the class without instantiating the bean
            final Class<?> clazz = ClassUtils.resolveClassName(beanClassName, ClassUtils.getDefaultClassLoader());

            // check for any @Tool on its methods
            final boolean hasTools = Arrays.stream(clazz.getMethods())
                    .anyMatch(m -> m.isAnnotationPresent(Tool.class));

            if (hasTools) {
                toolBeans.add(applicationContext.getBean(name));
            }
        }
        return toolBeans;
    }
}
