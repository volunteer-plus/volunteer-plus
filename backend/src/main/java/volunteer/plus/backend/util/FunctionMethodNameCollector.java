package volunteer.plus.backend.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

@Component
public class FunctionMethodNameCollector {

    /**
     * Retrieves the names of all methods in the given configuration class that are annotated
     * with @Bean and return a type assignable to Function<?, ?>.
     *
     * @param configClass the configuration class to inspect
     * @return a set of method names as strings
     */
    public Set<String> getFunctionBeanMethodNames(final Class<?> configClass) {
        final Set<String> methodNames = new HashSet<>();

        // Iterate over all declared methods of the configuration class
        for (final Method method : configClass.getDeclaredMethods()) {
            // Check if the method is annotated with @Bean and returns a Function
            if (method.isAnnotationPresent(Bean.class) && Function.class.isAssignableFrom(method.getReturnType())) {
                methodNames.add(method.getName());
            }
        }

        return methodNames;
    }
}
