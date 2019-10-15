package com.viettel.imdb.rest.config;

import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.JavaClient;
import com.viettel.imdb.rest.mock.client.ClientSimulator;
import com.viettel.imdb.util.CBOREncodeDecoderNew;
import com.viettel.imdb.util.IMDBEncodeDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.xml.ws.EndpointReference;
import java.time.Duration;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Config extends WebSecurityConfigurerAdapter {

    @Value("${host}")
    private String host;

//    @Autowired
//    private AuthenticationProvider authenticationProvider;
//
//    private AuthenticationEntryPoint authenticationEntryPoint = new RestAuthenticationEntryPoint();
//    private AuthenticationSuccessHandler successHandler = new MySavedRequestAwareAuthenticationSuccessHandler();
//    private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        /*auth.inMemoryAuthentication()
                .withUser("admin").password(encoder().encode("adminPass")).roles("ADMIN")
                .and()
                .withUser("user").password(encoder().encode("userPass")).roles("USER");*/
//        auth.authenticationProvider(authenticationProvider);
    }

    private static final String[] AUTH_WHITELIST = {

            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()/// todo need to disable this to edit data (PUT/POST/DELETE)
                .authorizeRequests()
                .anyRequest()
                .permitAll();


        http.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll();
//        http.csrf().disable()
//                .exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPoint)
//                .and()
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/authenticate")
//                .permitAll()
//                .successHandler(successHandler)
//                .failureHandler(failureHandler)
//                .and()
//                .logout();
    }

    @Bean
    public IMDBClient imdbClient() throws Exception {
//        IMDBClient imdbClient = new JavaClient(host);
//        imdbClient.echo(1).get(Duration.ofDays(1));
//        return imdbClient;
        return new ClientSimulator();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IMDBEncodeDecoder encodeDecoder() {
        return new CBOREncodeDecoderNew();
    }
}
