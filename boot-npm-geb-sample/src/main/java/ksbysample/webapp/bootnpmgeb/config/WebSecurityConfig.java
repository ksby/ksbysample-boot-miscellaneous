package ksbysample.webapp.bootnpmgeb.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * ???
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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
    }

}
