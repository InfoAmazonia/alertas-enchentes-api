package br.edu.ufcg.analytics.infoamazonia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InfoAmazoniaREST {

	public static void main(String args[]) {
		
        SpringApplication.run(InfoAmazoniaREST.class, args);
    }
}
