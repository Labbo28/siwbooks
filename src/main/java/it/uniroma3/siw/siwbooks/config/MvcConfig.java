package it.uniroma3.siw.siwbooks.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${upload.dir.libri}")
    private String uploadDirLibri;

    @Value("${upload.dir.autori}")
    private String uploadDirAutori;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Espone come /uploads/immaginiLibri/** il contenuto di uploadDirLibri
        registry
            .addResourceHandler("/uploads/immaginiLibri/**")
            .addResourceLocations("file:" + uploadDirLibri + "/");

        // Espone come /uploads/immaginiAutori/** il contenuto di uploadDirAutori
        registry
            .addResourceHandler("/uploads/immaginiAutori/**")
            .addResourceLocations("file:" + uploadDirAutori + "/");
    }
}
