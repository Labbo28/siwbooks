/* package it.uniroma3.siw.siwbooks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.siwbooks.model.Autore;
import it.uniroma3.siw.siwbooks.model.Immagine;
import it.uniroma3.siw.siwbooks.model.Libro;
import it.uniroma3.siw.siwbooks.repository.AutoreRepository;
import it.uniroma3.siw.siwbooks.repository.ImmagineRepository;
import it.uniroma3.siw.siwbooks.repository.LibroRepository;
import jakarta.transaction.Transactional;

@Component
@Order(2)
public class BookAndAuthorsDataInitializer implements CommandLineRunner {

    private String fetchAutoriPath="/uploads/immaginiAutori";
    
    private String fetchLibriPath="/uploads/immaginiLibri";

    @Autowired ImmagineRepository immagineRepository;
    @Autowired AutoreRepository autoreRepository;

    @Autowired LibroRepository libroRepository;

    @Override
    @Transactional
    public void run(String... args){
            init();
    }
   

    private void init(){
           Immagine rw = new Immagine(fetchAutoriPath+"/rowling.jpeg");
           immagineRepository.save(rw);
    Autore rowling = new Autore("J.K.", "Rowling", 
"Regno Unito", "1965-07-31", null, rw);
        autoreRepository.save(rowling);
        immagineRepository.save(rw);
       

        Libro hp1 = new Libro("Harry Potter and the Philosopher’s Stone", 1997);
        hp1.getAutori().add(rowling);
        libroRepository.save(hp1);

        Libro hp2 = new Libro("Harry Potter and the Chamber of Secrets", 1998);
        hp2.getAutori().add(rowling);
        libroRepository.save(hp2);

    }

}
*/