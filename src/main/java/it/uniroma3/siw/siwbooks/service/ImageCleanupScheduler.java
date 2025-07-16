package it.uniroma3.siw.siwbooks.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.siwbooks.model.Immagine;
import it.uniroma3.siw.siwbooks.repository.ImmagineRepository;

@Component
public class ImageCleanupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ImageCleanupScheduler.class);

    @Autowired
    private ImmagineRepository immagineRepository;

    @Value("${upload.dir.libri}")
    private String uploadDirLibri;

    @Value("${upload.dir.autori}")
    private String uploadDirAutori;

    /**
     * Scheduler che viene eseguito ogni giorno alle 2:00 AM
     * Cron expression: secondo minuto ora giorno mese giorno_settimana
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupUnlinkedImages() {
        logger.info("Avvio pulizia immagini non linkate alle {}", LocalDateTime.now());
        
        try {
            // Trova tutte le immagini marcate come unlinked
            List<Immagine> immaginiUnlinked = immagineRepository.findByUnlinkedTrue();
            
            logger.info("Trovate {} immagini da eliminare", immaginiUnlinked.size());
            
            int eliminateSuccesso = 0;
            int erroriEliminazione = 0;
            
            for (Immagine immagine : immaginiUnlinked) {
                try {
                    // Elimina il file fisico
                    if (eliminaFileFisico(immagine.getPath())) {
                        // Se l'eliminazione del file è riuscita, elimina l'record dal database
                        immagineRepository.delete(immagine);
                        eliminateSuccesso++;
                        logger.debug("Eliminata immagine: {}", immagine.getPath());
                    } else {
                        erroriEliminazione++;
                        logger.warn("Impossibile eliminare il file fisico: {}", immagine.getPath());
                    }
                } catch (Exception e) {
                    erroriEliminazione++;
                    logger.error("Errore durante l'eliminazione dell'immagine {}: {}", 
                               immagine.getPath(), e.getMessage());
                }
            }
            
            logger.info("Pulizia completata. Eliminate: {}, Errori: {}", 
                       eliminateSuccesso, erroriEliminazione);
            
        } catch (Exception e) {
            logger.error("Errore durante la pulizia delle immagini: {}", e.getMessage(), e);
        }
    }

    /**
     * Scheduler alternativo che viene eseguito ogni ora (per test)
     * Decommentare se si vuole testare più frequentemente
     */
    // @Scheduled(fixedRate = 3600000) // ogni ora
    // public void cleanupUnlinkedImagesHourly() {
    //     cleanupUnlinkedImages();
    // }

    /**
     * Elimina il file fisico dal filesystem
     * @param imagePath il path dell'immagine (es: /uploads/immaginiAutori/filename.jpg)
     * @return true se l'eliminazione è riuscita, false altrimenti
     */
    private boolean eliminaFileFisico(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            logger.warn("Path dell'immagine vuoto o null");
            return false;
        }
        
        try {
            // Converti il path pubblico in path fisico
            String pathFisico = convertiPathPubblicoInFisico(imagePath);
            
            if (pathFisico == null) {
                logger.warn("Impossibile convertire il path pubblico in fisico: {}", imagePath);
                return false;
            }
            
            Path filePath = Paths.get(pathFisico);
            
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                logger.debug("File eliminato: {}", pathFisico);
                return true;
            } else {
                logger.warn("File non trovato: {}", pathFisico);
                return true; // Consideriamo come successo se il file non esiste già
            }
            
        } catch (IOException e) {
            logger.error("Errore durante l'eliminazione del file {}: {}", imagePath, e.getMessage());
            return false;
        }
    }

    /**
     * Converte un path pubblico (es: /uploads/immaginiAutori/filename.jpg) 
     * in un path fisico del filesystem
     */
    private String convertiPathPubblicoInFisico(String pathPubblico) {
        if (pathPubblico.startsWith("/uploads/immaginiLibri/")) {
            String filename = pathPubblico.replace("/uploads/immaginiLibri/", "");
            return uploadDirLibri + "/" + filename;
        } else if (pathPubblico.startsWith("/uploads/immaginiAutori/")) {
            String filename = pathPubblico.replace("/uploads/immaginiAutori/", "");
            return uploadDirAutori + "/" + filename;
        }
        
        return null;
    }

    /**
     * Metodo per forzare la pulizia manualmente (utile per test)
     */
    public void forceCleanup() {
        logger.info("Pulizia forzata delle immagini non linkate");
        cleanupUnlinkedImages();
    }
}