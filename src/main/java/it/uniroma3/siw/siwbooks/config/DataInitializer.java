package it.uniroma3.siw.siwbooks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.siwbooks.model.Utente;
import it.uniroma3.siw.siwbooks.model.enums.Ruolo;
import it.uniroma3.siw.siwbooks.repository.UtenteRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            initializeDatabase();
        } catch (Exception e) {
            System.err.println("Errore durante l'inizializzazione del database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeDatabase() {
        long userCount = utenteRepository.count();
        System.out.println("Numero di utenti esistenti nel database: " + userCount);

        if (userCount == 0) {
            System.out.println("Database vuoto, creazione utenti di test...");
            createInitialUsers();
        } else {
            System.out.println("Database già popolato, verifica presenza utenti di test...");
            checkAndCreateMissingTestUsers();
        }
    }

    private void createInitialUsers() {
        // Utente normale
        createUserIfNotExists(
            "Mario", "Rossi", "user@test.com", "password123", Ruolo.USER,
            "Utente normale"
        );

        // Utente admin  
        createUserIfNotExists(
            "Giulia", "Bianchi", "admin@test.com", "admin123", Ruolo.ADMIN,
            "Utente admin"
        );

        printCredentials();
    }

    private void checkAndCreateMissingTestUsers() {
        // Verifica e crea utente normale se mancante
        if (!utenteRepository.existsByEmail("user@test.com")) {
            createUserIfNotExists(
                "Mario", "Rossi", "user@test.com", "password123", Ruolo.USER,
                "Utente normale (aggiunto)"
            );
        }

        // Verifica e crea utente admin se mancante
        if (!utenteRepository.existsByEmail("admin@test.com")) {
            createUserIfNotExists(
                "Giulia", "Bianchi", "admin@test.com", "admin123", Ruolo.ADMIN,
                "Utente admin (aggiunto)"
            );
        }
    }

    private void createUserIfNotExists(String nome, String cognome, String email, 
                                     String plainPassword, Ruolo ruolo, String description) {
        try {
            if (!utenteRepository.existsByEmail(email)) {
                String encodedPassword = passwordEncoder.encode(plainPassword);
                
                Utente utente = new Utente(nome, cognome, email, encodedPassword, ruolo);
                utenteRepository.save(utente);
                
                System.out.println(description + " creato con successo:");
                System.out.println("  Email: " + email);
                System.out.println("  Password: " + plainPassword);
                System.out.println("  Nome: " + nome + " " + cognome);
                System.out.println("  Ruolo: " + ruolo);
                System.out.println();
                
            } else {
                System.out.println("Utente " + email + " già esistente, saltato.");
            }
        } catch (Exception e) {
            System.err.println("Errore nella creazione dell'utente " + email + ": " + e.getMessage());
        }
    }

    private void printCredentials() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("         CREDENZIALI DI ACCESSO");
        System.out.println("=".repeat(50));
        System.out.println("UTENTE NORMALE:");
        System.out.println("  📧 Email: user@test.com");
        System.out.println("  🔑 Password: password123");
        System.out.println("  👤 Nome: Mario Rossi");
        System.out.println("  🏷️  Ruolo: USER");
        System.out.println();
        System.out.println("UTENTE AMMINISTRATORE:");
        System.out.println("  📧 Email: admin@test.com");
        System.out.println("  🔑 Password: admin123");
        System.out.println("  👤 Nome: Giulia Bianchi");
        System.out.println("  🏷️  Ruolo: ADMIN");
        System.out.println("=".repeat(50));
        System.out.println("Inizializzazione database completata!\n");
    }
}