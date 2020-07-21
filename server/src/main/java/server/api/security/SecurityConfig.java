package server.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    JWTUtils jwtUtils;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String SIGN_IN_URL = "/users/login";
        String SIGN_UP_URL = "/users";

        http.httpBasic().disable().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers(HttpMethod.POST, SIGN_IN_URL, SIGN_UP_URL).permitAll()
                .anyRequest().authenticated().and()
                .apply(new JWTConfigurer(jwtUtils));
    }

}
