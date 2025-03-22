package volunteer.plus.backend.config.quartz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;


@Configuration
public class QuartzConfig {
    private final AutowiringSpringBeanJobFactory beanJobFactory;

    public QuartzConfig(AutowiringSpringBeanJobFactory beanJobFactory) {
        this.beanJobFactory = beanJobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(beanJobFactory);
        return schedulerFactoryBean;
    }
}
