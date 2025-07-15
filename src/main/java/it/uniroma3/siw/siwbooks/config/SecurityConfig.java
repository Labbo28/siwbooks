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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // OK per sviluppo
            .authorizeHttpRequests(auth -> auth
                // Permetti a tutti (anche non autenticati) di accedere alle pagine pubbliche:
                // Home, lista libri, lista autori, registrazione, login, risorse statiche (CSS, JS, immagini, assets)
                .requestMatchers("/", "/libri", "/autori", "/register", "/login", "/css/**", "/js/**", "/images/**", "/api/**", "/assets/**").permitAll()
                
                // SOLO per ADMIN: Accesso alla dashboard admin e gestione (creazione/modifica/eliminazione) di libri e autori
                                 
                // SOLO per utenti con ruolo USER (o ADMIN se specificato con hasAnyRole): Gestione recensioni
                // Questo copre gli endpoint POST per l'invio della recensione, modifica ed eliminazione
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/libri/new", "/libri/*/edit", "/libri/*/delete").hasRole("ADMIN")
                .requestMatchers("/autori/new", "/immagini/new", "/autori/*/edit", "/autori/*/delete").hasRole("ADMIN")
                // Permetti a tutti (anche non autenticati) di VEDERE il dettaglio di UN SINGOLO libro o UN SINGOLO autore
                // NOTA BENE: Questo permette di vedere la pagina, ma la logica di Thymeleaf deciderà cosa visualizzare
                // all'interno della pagina in base al ruolo dell'utente (es. form recensione solo per ROLE_USER)
                .requestMatchers("/libri/{id}", "/autori/{id}").permitAll() // Dettaglio libro e autore visualizzabili da tutti
                
                .requestMatchers("/libri/*/recensioni", "/libri/*/recensioni/**").hasAnyRole("USER", "ADMIN")
                // Se vuoi che gli ADMIN possano anche recensire:
                // .requestMatchers("/libri/*/recensioni", "/libri/*/recensioni/**").hasAnyRole("USER", "ADMIN")
                
                // Tutte le altre richieste (che non rientrano nelle regole sopra) richiedono autenticazione.
                // Questa è una regola di "catch-all" per proteggere tutto il resto.
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