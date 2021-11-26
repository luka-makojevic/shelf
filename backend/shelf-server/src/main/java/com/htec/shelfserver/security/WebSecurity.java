package com.htec.shelfserver.security;

import com.htec.shelfserver.repository.UserRepository;
import com.htec.shelfserver.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    public WebSecurity(UserService userService,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       UserRepository userRepository
    ) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
                .permitAll()
                .antMatchers(HttpMethod.POST, "/users/register/microsoft")
                .permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_CONFIRM_EMAIL_URL)
                .permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_RESEND_TOKEN_URL)
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
                .addFilter(getAuthenticationFilter())
                .addFilter(new AuthorizationFilter(authenticationManager()))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    protected AuthenticationFilter getAuthenticationFilter() throws Exception {
        final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager(), userService, userRepository);
        filter.setFilterProcessesUrl(SecurityConstants.SIGN_IN_URL);
        return filter;
    }
}

