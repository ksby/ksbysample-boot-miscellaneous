package ksbysample.webapp.bootnpmgeb.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.sql.DataSource;

/**
 * ???
 */
@Configuration
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
     * @return Tomcat JDBC Connection Pool の DataSource オブジェクト
     */
    @Bean
    @ConfigurationProperties("spring.datasource.tomcat")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .type(org.apache.tomcat.jdbc.pool.DataSource.class)
                .build();
    }

}
