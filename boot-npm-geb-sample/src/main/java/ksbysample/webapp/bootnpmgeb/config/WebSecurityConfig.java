package ksbysample.webapp.bootnpmgeb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.regex.Pattern;

/**
 * ???
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Pattern H2_CONSOLE_URI_PATTERN = Pattern.compile("^/h2-console");

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
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();

        // spring.h2.console.enabled=true に設定されている場合には H2 Console を表示するために必要な設定を行う
        if (springH2ConsoleEnabled) {
            http.csrf()
                    .requireCsrfProtectionMatcher(request -> {
                        // H2 Console は CSRF対策の対象外にする
                        if (H2_CONSOLE_URI_PATTERN.matcher(request.getRequestURI()).lookingAt()) {
                            return false;
                        }
                        return true;
                    });
            http.headers().frameOptions().sameOrigin();
        }
    }

}
