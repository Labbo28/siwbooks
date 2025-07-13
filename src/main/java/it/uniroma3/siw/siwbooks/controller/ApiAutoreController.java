package it.uniroma3.siw.siwbooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import it.uniroma3.siw.siwbooks.dto.AutoreDTO;
import it.uniroma3.siw.siwbooks.repository.AutoreRepository;

          
@RequestMapping("/api/autori")
@RestController
public class ApiAutoreController {

    @Autowired
    private AutoreRepository autoreRepository;

    // Restituisce al massimo 10 autori il cui nome contiene il termine di ricerca
    @GetMapping("/cerca")
    public List<AutoreDTO> search(
            @RequestParam("term") String term) {
        return autoreRepository.findByNomeContainingIgnoreCase(term, PageRequest.of(0,10))
                .stream()
                .map(AutoreDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
