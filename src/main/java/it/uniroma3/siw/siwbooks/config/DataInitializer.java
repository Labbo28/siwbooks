package it.uniroma3.siw.siwbooks.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AutoreRepository autoreRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired 
    private ImmagineRepository immagineRepository;

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
        
            initBooksAndAuthors();
        
    }
    private void initBooksAndAuthors() {
        String pathAutori = "/assets/immaginiAutori/";
        String pathLibri = "/assets/immaginiLibri/";
        
        // Controlla se i dati esistono già
        if (autoreRepository.count() > 0 || libroRepository.count() > 0) {
            System.out.println("Libri e autori già presenti nel database, saltato l'inserimento.");
            return;
        }
        
        try {
            // 1. Crea e salva prima l'autore (senza relazioni)
            Autore autore1 = new Autore("J.R.R.", "Tolkien", "Inglese", "1892", "1973");
            autore1 = autoreRepository.save(autore1);
            
            // 2. Crea e salva il libro (senza relazioni)
            Libro libro1 = new Libro("Il Signore degli Anelli", 1954);
            libro1 = libroRepository.save(libro1);
            
            // 3. Stabilisci le relazioni usando collezioni mutabili
            // Aggiungi il libro alla collezione di libri dell'autore
            autore1.getLibri().add(libro1);
            
            // Aggiungi l'autore alla collezione di autori del libro
            libro1.getAutori().add(autore1);
            
            // 4. Salva di nuovo per aggiornare le relazioni
            autore1 = autoreRepository.save(autore1);
            libro1 = libroRepository.save(libro1);
            
            // 5. Ora crea e salva le immagini (che dipendono dalle entità principali)
            Immagine imgAutore = new Immagine("Tolkien", pathAutori + "tolkien.jpg");
            imgAutore = immagineRepository.save(imgAutore);
            
            Immagine imgLibro = new Immagine("Il Signore degli Anelli", pathLibri + "signore-degli-anelli.jpeg");
            imgLibro = immagineRepository.save(imgLibro);
            
            // 6. Aggiorna le entità con le immagini
            autore1.setImmagine(imgAutore);
            libro1.getCopertina().add(imgLibro);
            
            // 7. Salvataggio finale
            autoreRepository.save(autore1);
            libroRepository.save(libro1);
            
            System.out.println("Autore e libro inizializzati correttamente.");
            
        } catch (Exception e) {
            System.err.println("Errore durante l'inizializzazione di autore e libro: " + e.getMessage());
            e.printStackTrace();
            throw e; // Rilancia l'eccezione per vedere il vero errore
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
            "Labbo", "obbaL", "admin@test.com", "admin123", Ruolo.ADMIN,
            "Utente admin", "THE_GOAT_69"
        );

        printCredentials();
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