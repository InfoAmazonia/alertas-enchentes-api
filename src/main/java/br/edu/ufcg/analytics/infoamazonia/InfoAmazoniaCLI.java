package br.edu.ufcg.analytics.infoamazonia;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class InfoAmazoniaCLI {//implements CommandLineRunner {

	private static final Logger log = LoggerFactory
			.getLogger(InfoAmazoniaCLI.class);

//	@Autowired
	AlertRepository repository;

	private SimpleDateFormat sdf;
	
	public static void main(String args[]) {
        SpringApplication.run(InfoAmazoniaCLI.class, args);
    }

	@Override
	public void run(String... args) throws Exception {
		
		if(args.length < 1){
			System.err.println("Usage: java InfoAmazoniaCLI <CREATE|POPULATE>");
			System.exit(1);
		}
		
		sdf = new SimpleDateFormat("dd/MM/yyyyHH:mm:ss");
		
		File file = new File(args[0]);
		String stationCode = file.getName();
		long id = Long.valueOf(stationCode.substring(0, 8));
		String name = stationCode.substring(8, stationCode.indexOf('_'));
		Scanner input = new Scanner(file);
		System.out.println(name);
		switch (name) {
		case "RIOBRANCO":
			predictRioBranco(id, name, input);
			break;

		default:
			populateStation(id, name, input);
			break;
		}
		
        System.exit(0);
        
        
	}

	private void populateStation(long id, String name, Scanner input)
			throws ParseException {
		input.nextLine();
		while (input.hasNext()) {
			String[] tokens = input.nextLine().trim().split("\\s+");
			System.out.println(Arrays.toString(tokens));
			long timestamp = sdf.parse(tokens[0] + tokens[1]).toInstant().getEpochSecond();
			long cota = tokens.length == 3?Long.valueOf(tokens[2]):-1;
			if(repository.find(id, timestamp).isEmpty()){
				repository.save(new Alert(name, id, timestamp, cota));
			}
		}
	}

	private void predictRioBranco(long id, String name,
			Scanner input) throws ParseException {
		long delta = 12 * 3600000;
		long alerta = 1350;
		long inundacao = 1400;
		double ALPHA = 0.717395738210093;
		double BETA = 0.151170920309919;
		
		input.nextLine();
		int i = 0;
		while (input.hasNext()) {
			String[] tokens = input.nextLine().trim().split("\\s+");
			System.out.println(Arrays.toString(tokens));
			long timestamp = sdf.parse(tokens[0] + tokens[1]).toInstant().getEpochSecond();
			long cota = tokens.length == 3?Long.valueOf(tokens[2]):-1;
			if(i++ < 48){
				repository.save(new Alert(name, id, timestamp, cota, cota, cota, "NORMAL"));
			}else{
				if(repository.find(id, timestamp).isEmpty()){
					Alert alertaAnterior = repository.find(id, timestamp-delta).get(0);
					long cotaAntiga = alertaAnterior.getCurrent();
					long cotaMaisAntiga = repository.find(id, timestamp-delta-delta).get(0).getCurrent();
					
					long cotaAntigaX = repository.find(13551000L, timestamp-delta).get(0).getCurrent();
					long cotaMaisAntigaX = repository.find(13551000L, timestamp-delta-delta).get(0).getCurrent();
					
					long calculated = (long) (cotaAntiga + ALPHA * (cotaAntiga - cotaMaisAntiga) + BETA * (cotaAntigaX - cotaMaisAntigaX));
					long predicted = calculated + (alertaAnterior.getCurrent() - alertaAnterior.getCalculated());
					
					String status = getStatus(predicted , alerta, inundacao);
					repository.save(new Alert(name, id, timestamp, cota, calculated, predicted, status));
				}
			}
		}
	}

	private String getStatus(long predicted, long alerta, long inundacao) {
		if(predicted < alerta){
			return "NORMAL";
		}else if (alerta <= predicted && predicted < inundacao){
			return "ALERTA";
		}else{
			return "INUNDACAO";
		}
	}
}
