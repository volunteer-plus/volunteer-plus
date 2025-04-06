package volunteer.plus.backend.config.quartz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;


@Configuration
public class QuartzConfig {
    private final AutowiringSpringBeanJobFactory beanJobFactory;
    private final DataSource dataSource;

    public QuartzConfig(final AutowiringSpringBeanJobFactory beanJobFactory,
                        final DataSource dataSource) {
        this.beanJobFactory = beanJobFactory;
        this.dataSource = dataSource;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(beanJobFactory);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setOverwriteExistingJobs(false);
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
        return schedulerFactoryBean;
    }
}
