package it.uniroma3.siw.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.siwbooks.service.ImageCleanupScheduler;

@RestController
public class TestController {
    
    @Autowired
    private ImageCleanupScheduler imageCleanupScheduler;
    
    @PostMapping("/admin/cleanup-images")
    public ResponseEntity<String> forceImageCleanup() {
        imageCleanupScheduler.forceCleanup();
        return ResponseEntity.ok("Pulizia forzata avviata");
    }
}