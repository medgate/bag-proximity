package ch.bag.screening.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(final WebSecurity webSecurity) {
    webSecurity
        .ignoring()
        .requestMatchers()
        .antMatchers("/actuator/**")
        .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
        .antMatchers("/v1/screening/**")
        .antMatchers("/v1/recommendations/**");
  }

  @Override
  public void configure(final HttpSecurity http) throws Exception {
    // Avoid clickjacking attacks by denying all attempts to load the page in a (i)frame
    http.headers().frameOptions().deny();
    // Avoid XSS attacks
    http.headers().xssProtection().block(true);
    // Disable CSRF
    http.csrf().disable();

    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        // Allow pre-flight requests
        .antMatchers(HttpMethod.OPTIONS, "/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .httpBasic()
        .and()
        .formLogin()
        .disable()
        .logout()
        .disable();
  }
}
