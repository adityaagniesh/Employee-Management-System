package com.springboot.employeeManagment.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    @Autowired
    DataSource dataSource;

    @Autowired
    RoleBasedAuthenticationSuccessHandler successHandler;

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/login", "/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/employees/user/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/employees/admin/**").hasRole("ADMIN")
                        .requestMatchers("/employees/admin/showFormForAdd").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/employees/admin/showFormForUpdate").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/employees/admin/save").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/employees/admin/delete").hasRole("ADMIN")
                        .anyRequest().authenticated() // Others need authentication
                )
                .formLogin(form -> form
                        .successHandler(successHandler)
                        .permitAll()) // Enable login form
                .httpBasic(withDefaults()) // Enable basic auth

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );


        return http.build();  // Builds and returns the SecurityFilterChain
    }


    @Bean
    public CommandLineRunner initData(UserDetailsService userDetailsService) {
        return args -> {
            JdbcUserDetailsManager manager = (JdbcUserDetailsManager) userDetailsService;
                UserDetails user1 = User.withUsername("user1")
                        .password(passwordEncoder().encode("userPass"))
                        .roles("USER")
                        .build();

            UserDetails admin = User.withUsername("admin")
                    .password(passwordEncoder().encode("adminPass"))
                    .roles("ADMIN")
                    .build();

            // Create or update user1
            try {
                if (manager.userExists(user1.getUsername())) {
                    // If you want to ensure password/roles are updated on startup:
                    manager.updateUser(user1);
                    System.out.println("Updated existing user: " + user1.getUsername());
                } else {
                    manager.createUser(user1);
                    System.out.println("Created user: " + user1.getUsername());
                }
            } catch (DuplicateKeyException ex) {
                // safe fallback if a concurrent startup created the user between the check and create
                System.out.println("User " + user1.getUsername() + " already exists (race).");
            }

            // Create or update admin
            try {
                if (manager.userExists(admin.getUsername())) {
                    manager.updateUser(admin);
                    System.out.println("Updated existing user: " + admin.getUsername());
                } else {
                    manager.createUser(admin);
                    System.out.println("Created user: " + admin.getUsername());
                }
            } catch (DuplicateKeyException ex) {
                System.out.println("User " + admin.getUsername() + " already exists (race).");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

}
