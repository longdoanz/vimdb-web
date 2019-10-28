package com.viettel.imdb.rest.config;

import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.rest.mock.client.ClientSimulator;
import com.viettel.imdb.rest.mock.server.ClusterSimulator;
import com.viettel.imdb.rest.mock.server.NodeSimulator;
import com.viettel.imdb.rest.mock.server.NodeSimulatorImpl;
import com.viettel.imdb.rest.service.AuthServiceImpl;
import com.viettel.imdb.rest.util.SimulatorMonitorStatisticClient;
import com.viettel.imdb.rest.util.StatisticClient;
import com.viettel.imdb.util.IMDBEncodeDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Config extends WebSecurityConfigurerAdapter {

    @Value("${host}")
    private String host;
    @Value("${security.enabled}")
    private boolean securityEnabled;
    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;

    private ClusterSimulator cluster = new ClusterSimulator();



    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter();
    }
    //@Autowired JwtRequestFilter jwtRequestFilter;


    @Autowired AuthServiceImpl authService;

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider() {
        };
    }


//    @Autowired
//    private AuthenticationProvider authenticationProvider;
//
    private AuthenticationEntryPoint authenticationEntryPoint = new RestAuthenticationEntryPoint();
//    private AuthenticationSuccessHandler successHandler = new MySavedRequestAwareAuthenticationSuccessHandler();
//    private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        /*auth.inMemoryAuthentication()
                .withUser("admin").password(encoder().encode("adminPass")).roles("ADMIN")
                .and()
                .withUser("user").password(encoder().encode("userPass")).roles("USER");*/
//        auth.authenticationProvider(authenticationProvider);
        auth.userDetailsService(authService); // Cung cáp userservice cho spring security
        //.passwordEncoder(passwordEncoder()); // cung cấp password encoder

        auth.authenticationProvider(customAuthenticationProvider());
    }

    private static final String[] AUTH_WHITELIST = {

            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            "/v1/auth/login",
            "/authenticate"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()/// todo need to disable this to edit data (PUT/POST/DELETE)
                .authorizeRequests()
//                .antMatchers(AUTH_WHITELIST)
//                .permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                .anyRequest()
                .permitAll();


        //http.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll();
        http.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
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

    private static List<NodeSimulator> nodes;

    static {
        nodes = new ArrayList<>();

        int clusterSize = ThreadLocalRandom.current().nextInt(3, 7);
        int startIP = ThreadLocalRandom.current().nextInt(65, 72);

        for (int i = 0; i < clusterSize; i++) {
            nodes.add(new NodeSimulatorImpl("172.16.28." + startIP++, 10000));
        }
        nodes.forEach(System.out::println);
    }

    @Bean
    public ClusterSimulator getCluster() {
//        List<NodeSimulator> nodes = new ArrayList<>();
//
//        int clusterSize = ThreadLocalRandom.current().nextInt(3, 7);
//
//        for (int i = 0; i < clusterSize; i++) {
//            nodes.add(new NodeSimulatorImpl("172.16.28." + ThreadLocalRandom.current().nextInt(10, 254), 10000));
//        }
        System.err.println("------------------------------------- NEW CLUSTER SIZE: " + nodes.size() + " --------------------------");
        cluster.setNodes(nodes);

        return cluster;
    }

    @Bean
    public IMDBClient imdbClient() throws Exception {
//        IMDBClient imdbClient = new JavaClient(host, new ClientConfig(securityEnabled, username, password));
//        imdbClient.echo(1).get(Duration.ofDays(1)); // todo do i need to do it here??

        return new ClientSimulator(cluster);
    }

    @Bean
    public StatisticClient statisticClient() throws Exception {
        StatisticClient client = new SimulatorMonitorStatisticClient();
        for(NodeSimulator node : nodes) {
            client.addNode(node.getAddress());
        }
        return client;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IMDBEncodeDecoder encodeDecoder() {
        return IMDBEncodeDecoder.getInstance();
    }
}
