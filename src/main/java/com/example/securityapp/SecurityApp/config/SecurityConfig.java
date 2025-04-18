package com.example.securityapp.SecurityApp.config;

import com.example.securityapp.SecurityApp.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{



        return http
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("register","login")
                        .permitAll()
                        .anyRequest().authenticated())
//                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session
                -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
               .build();

        /* disable csrf using lambda expression */
//        http.csrf(customizer -> customizer.disable());

        /* getting authentication and authorization for login using lambda expression
        (no on should be able to access the page without authentication) */
//        http.authorizeHttpRequests(request -> request.anyRequest().authenticated());

        /* get a default login form of spring security */
//        http.formLogin(Customizer.withDefaults());

        /* we were getting form in postman api testing coz of form login customizer but if we want to load home page
        after login and to get rest api access in postman we need to use httpBasic(Customizer.withDefaults())*/
//        http.httpBasic(Customizer.withDefaults());

        /* Different ways of handling csrf -> on of them is making http stateless so making this http stateless
        we don't have to worry about the session_id <- now making stateless we cannot log in through browser
        login form because now credentials should be passed everytime being stateless session
        which redirects to log in form again*/
//        http.sessionManagement(session
//                -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // TO DO: To work with form login after making session stateless disable form log in customizer

        /* creating a customizer to disable csrf in imperative way */
//        Customizer<CsrfConfigurer<HttpSecurity>> csrfCustomizer = new Customizer<CsrfConfigurer<HttpSecurity>>() {
//            @Override
//            public void customize(CsrfConfigurer<HttpSecurity> customizer) {
//                customizer.disable();
//            }
//        };
//
//        http.csrf(csrfCustomizer);

//        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
        // daoAuthProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        daoAuthProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        daoAuthProvider.setUserDetailsService(userDetailsService);
        return daoAuthProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//
//        UserDetails user1 = User
//                .withDefaultPasswordEncoder()
//                .username("kaushal")
//                .password("k@123")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user2 = User
//                .withDefaultPasswordEncoder()
//                .username("sumika")
//                .password("sumu@123")
//                .roles("ORGANIZER")
//                .build();
//         return new InMemoryUserDetailsManager(user1, user2);
//    }
}
