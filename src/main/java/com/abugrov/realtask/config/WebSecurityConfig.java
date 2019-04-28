package com.abugrov.realtask.config;

import com.abugrov.realtask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;

    @Autowired
    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
                .ignoringAntMatchers("/task/*/paid")
            .and()
                .authorizeRequests().antMatchers(
                    "/",
                    "/registration",
                    "/activate/*",
                    "/user/lostPassword",
                    "/user/recover/**",
                    "/img/**",
                    "/main",
                    "/task/*/paid"
                ).permitAll()
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
            .and()
                .rememberMe()
            .and()
                .logout()
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(userService.passwordEncoder);
    }
}