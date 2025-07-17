package it.uniroma3.siw.siwbooks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import it.uniroma3.siw.siwbooks.service.MyUserDetailsService;
import it.uniroma3.siw.siwbooks.service.OAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MyUserDetailsService userDetailsService;
    
    @Autowired
    private OAuth2UserService oauth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/libri", "/autori", "/register", "/login", "/css/**", "/js/**", "/images/**", "/api/**", "/assets/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/libri/new", "/libri/*/edit", "/libri/*/delete").hasRole("ADMIN")
                .requestMatchers("/autori/new", "/immagini/new", "/autori/*/edit", "/autori/*/delete").hasRole("ADMIN")
                .requestMatchers("/libri/{id}", "/autori/{id}").permitAll()
                .requestMatchers("/libri/*/recensioni", "/libri/*/recensioni/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(oauth2UserService)
                )
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }
}