package br.edu.ufcg.analytics.infoamazonia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InfoAmazoniaREST {

	private static final Logger log = LoggerFactory
			.getLogger(InfoAmazoniaREST.class);

	public static void main(String args[]) {
        SpringApplication.run(InfoAmazoniaREST.class, args);
    }
}
