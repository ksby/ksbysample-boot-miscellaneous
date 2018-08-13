package ksbysample.webapp.bootnpmgeb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * ???
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ACTUATOR_ANT_PATTERN = "/actuator/**";
    private static final String H2_CONSOLE_ANT_PATTERN = "/h2-console/**";

    @Value("${spring.h2.console.enabled:false}")
    private boolean springH2ConsoleEnabled;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 認証の対象外にしたいURLがある場合には、以下のような記述を追加します
                // 複数URLがある場合はantMatchersメソッドにカンマ区切りで対象URLを複数列挙します
                // .antMatchers("/country/**").permitAll()
                //
                // この Web アプリケーションでは Spring Security を CSRF対策で使用したいだけなので、
                // 全ての URL を認証の対象外にする

                // アクセス元のホストIPアドレスで指定する場合
                // .antMatchers(ACTUATOR_ANT_PATTERN).hasIpAddress("127.0.0.1")
                // .antMatchers(ACTUATOR_ANT_PATTERN).hasIpAddress("192.168.56.1")
                // アクセス元のネットワークIPアドレスで指定する場合
                // .antMatchers(ACTUATOR_ANT_PATTERN).hasIpAddress("192.168.56.0/24")
                // アクセス元を複数指定する場合
                // .antMatchers(ACTUATOR_ANT_PATTERN)
                //     .access("hasIpAddress('127.0.0.1') or hasIpAddress('192.168.56.0/24')")

                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();

        // Spring Boot Actuator のパスは CSRF チェックの対象外にする
        http.csrf().ignoringAntMatchers(ACTUATOR_ANT_PATTERN);

        // spring.h2.console.enabled=true に設定されている場合には H2 Console を表示するために必要な設定を行う
        if (springH2ConsoleEnabled) {
            http.csrf().ignoringAntMatchers(H2_CONSOLE_ANT_PATTERN);
            http.headers().frameOptions().sameOrigin();
        }
    }

}
