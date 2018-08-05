package ksbysample.webapp.bootnpmgeb.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.sql.DataSource;

/**
 * ???
 */
@Configuration
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class ApplicationConfig {

    private final MessageSource messageSource;

    /**
     * @param messageSource {@link MessageSource} bean
     */
    public ApplicationConfig(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Controller クラスで直接 {@link Validator} を呼び出すために Bean として定義している
     * また Hibernate Validator のメッセージを ValidationMessages.properties ではなく
     * messages.properties に記述できるようにするためにも使用している
     *
     * @return new {@link LocalValidatorFactoryBean}
     */
    @Bean
    public Validator mvcValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(this.messageSource);
        return localValidatorFactoryBean;
    }

    /**
     * @return HikariCP の DataSource オブジェクト
     */
    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

}
