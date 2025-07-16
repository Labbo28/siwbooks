package it.uniroma3.siw.siwbooks.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.siwbooks.model.Autore;
import it.uniroma3.siw.siwbooks.model.Immagine;
import it.uniroma3.siw.siwbooks.model.Libro;
import it.uniroma3.siw.siwbooks.model.Utente;
import it.uniroma3.siw.siwbooks.model.enums.Ruolo;
import it.uniroma3.siw.siwbooks.repository.AutoreRepository;
import it.uniroma3.siw.siwbooks.repository.ImmagineRepository;
import it.uniroma3.siw.siwbooks.repository.LibroRepository;
import it.uniroma3.siw.siwbooks.repository.UtenteRepository;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${upload.dir.autori}")
    private String pathAutori;

    @Value("${upload.dir.libri}")
    private String pathLibri;

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
            "Utente normale", "Mario1337"
        );

        // Utente admin  
        createUserIfNotExists(
            "Ricccardo", "Labonia", "admin@test.com", "admin123", Ruolo.ADMIN,
            "Utente admin", "labbo"
        );

       
    }

    private void checkAndCreateMissingTestUsers() {
        // Verifica e crea utente normale se mancante
        if (!utenteRepository.existsByEmail("user@test.com")) {
            createUserIfNotExists(
                "Mario", "Rossi", "user@test.com", "password123", Ruolo.USER,
                "Utente normale (aggiunto)","Mario1337"
            );
        }

        // Verifica e crea utente admin se mancante
        if (!utenteRepository.existsByEmail("admin@test.com")) {
            createUserIfNotExists(
                "Giulia", "Bianchi", "admin@test.com", "admin123", Ruolo.ADMIN,
                "Utente admin (aggiunto)", "THE_GOAT_69"
            );
        }
    }

    private void createUserIfNotExists(String nome, String cognome, String email, 
                                     String plainPassword, Ruolo ruolo, String description,String username) {
        try {
            if (!utenteRepository.existsByEmail(email)) {
                String encodedPassword = passwordEncoder.encode(plainPassword);
                
                Utente utente = new Utente(nome, cognome, email, encodedPassword, ruolo,username);
                utenteRepository.save(utente);
                
            } else {
                System.out.println("Utente " + email + " già esistente, saltato.");
            }
        } catch (Exception e) {
            System.err.println("Errore nella creazione dell'utente " + email + ": " + e.getMessage());
        }
    }

}

