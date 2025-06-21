package it.uniroma3.siw.siwbooks.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.siwbooks.dto.NuovaImmagineDTO;
import it.uniroma3.siw.siwbooks.dto.TipoImmagine;
import it.uniroma3.siw.siwbooks.model.Immagine;
import it.uniroma3.siw.siwbooks.repository.ImmagineRepository;

@Service
public class ImmagineService {

    private final ImmagineRepository immagineRepository;

    // Inietto i valori delle proprietà definite in application.properties
    @Value("${upload.dir.libri}")
    private String uploadDirLibri;

    @Value("${upload.dir.autori}")
    private String uploadDirAutori;

    public ImmagineService(ImmagineRepository immagineRepository) {
        this.immagineRepository = immagineRepository;
    }

    public Immagine uploadImmagine(NuovaImmagineDTO dto) {
        Immagine img;
        MultipartFile fileImmagine = dto.getFileImmagine();
        if (fileImmagine.isEmpty()) {
            throw new RuntimeException("File è vuoto");
        }
        TipoImmagine tipoImmagine = dto.getTipoImmagine();
        if (tipoImmagine == null) {
            throw new RuntimeException("TipoImmagine non specificato");
        }

        // 1) Scelta della cartella di destinazione
        //    Se è COPERTINA, salviamo in uploadDirLibri; altrimenti in uploadDirAutori
        String dirDestinazione = tipoImmagine == TipoImmagine.COPERTINA
                ? uploadDirLibri
                : uploadDirAutori;

        try {
            // 2) Creo la directory se non esiste
            Path uploadPath = Paths.get(dirDestinazione);
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 3) Estraggo estensione e genero nome univoco
            String originalFilename = fileImmagine.getOriginalFilename();
            String fileExtension = "";
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = originalFilename.substring(dotIndex);
            }

            // Uso UUID per non avere collisioni, oppure potrei usare dto.getNomeImmagine()
            String filenameGenerato = UUID.randomUUID().toString() + fileExtension;

            // 4) Costruisco il path completo del file da salvare
            Path filePath = uploadPath.resolve(filenameGenerato);

            // 5) Salvo il file su disco
            fileImmagine.transferTo(filePath.toFile());
            // Alternativa: Files.copy(fileImmagine.getInputStream(), filePath);

            // 6) Costruisco il “publicPath” che salverò nell’entità Immagine
            //    Se ho esposto tramite ResourceHandler come sopra,
            //    l’URL pubblico sarà /uploads/immaginiLibri/filenameGenerato  (o immaginiAutori)
            String publicPath;
            if (tipoImmagine == TipoImmagine.COPERTINA) {
                publicPath = "/uploads/immaginiLibri/" + filenameGenerato;
            } else {
                publicPath = "/uploads/immaginiAutori/" + filenameGenerato;
            }

            // 7) Creo e salvo l’entità Immagine
            img = new Immagine();
            
            img.setPath(publicPath);
            // Se devi legarla a un Libro o Autore, aggiungi qui il link:
            // Libro libro = libroRepository.findById(dto.getLibroId()).get();
            // img.setLibro(libro);
            // libro.getCopertina().add(img);
            immagineRepository.save(img);


        } catch (IOException e) {
            // Se la directory non esiste o il filesystem non è scrivibile, qui arriva IOException
            e.printStackTrace();
            throw new RuntimeException("Errore nel salvataggio del file: " + e.getMessage());
        }
        return img;
    }
}
