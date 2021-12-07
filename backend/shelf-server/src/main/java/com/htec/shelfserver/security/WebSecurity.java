package com.htec.shelfserver.security;

import com.htec.shelfserver.repository.cassandra.InvalidJwtTokenRepository;
import com.htec.shelfserver.repository.mysql.UserRepository;
import com.htec.shelfserver.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final InvalidJwtTokenRepository invalidJwtTokenRepository;

    public WebSecurity(UserService userService,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       UserRepository userRepository,
                       InvalidJwtTokenRepository invalidJwtTokenRepository) {

        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.invalidJwtTokenRepository = invalidJwtTokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
                .permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_IN_URL)
                .permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_IN_MICROSOFT_URL)
                .permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_MICROSOFT_URL)
                .permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_CONFIRM_EMAIL_URL)
                .permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_RESEND_TOKEN_URL)
                .permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_REQUEST_URL)
                .permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_URL)
                .permitAll()
                .antMatchers(HttpMethod.PUT, "/users/update/photo")
                .permitAll()
                .antMatchers(
                        "/v2/api-docs",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/swagger.json")
                .permitAll()
                .anyRequest()
                .authenticated().and()
                .addFilter(new AuthorizationFilter(authenticationManager(), invalidJwtTokenRepository))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

