package it.uniroma3.siw.siwbooks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling 
public class SiwbooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiwbooksApplication.class, args);
	}

}
