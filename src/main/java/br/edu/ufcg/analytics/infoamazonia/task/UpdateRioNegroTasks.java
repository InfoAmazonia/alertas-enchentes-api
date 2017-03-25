package br.edu.ufcg.analytics.infoamazonia.task;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.edu.ufcg.analytics.infoamazonia.model.Station;
import br.edu.ufcg.analytics.infoamazonia.model.StationEntry;

@Component
public class UpdateRioNegroTasks extends UpdateTasks {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final long RIONEGRO_ID = 14990000L;
	private static final long RATE = 900000;
	
	
	public UpdateRioNegroTasks() {
		super(RIONEGRO_ID);
	}

	@Scheduled(initialDelay=30000, fixedRate = RATE)
	@Override
	public void update() throws FileNotFoundException, ParseException {
		logger.info("Updating Rio Negro");
		long time = System.currentTimeMillis();
		super.update();
		time = System.currentTimeMillis() - time;
		logger.info("Updated Rio Negro in " + time + " millis");
	}

	@Override
	protected StationEntry predict(long timestamp, Map<Long, Station> stations) {
		
		Station stationRioNegro = stations.get(RIONEGRO_ID);
		
		LocalDateTime now = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.of("America/Recife"));
		
		LocalDateTime predictionDate = LocalDateTime.of(now.getYear(), 6, 15, 0, 0);
		if(predictionDate.isBefore(now)){
			return null;
		}

		LocalDateTime march = LocalDateTime.of(now.getYear(), 4, 1, 0, 0);
		LocalDateTime april = LocalDateTime.of(now.getYear(), 5, 1, 0, 0);
		LocalDateTime may = LocalDateTime.of(now.getYear(), 6, 1, 0, 0);
		
		LocalDateTime predInput = null;
		if(now.equals(march)){
			predInput = march;
		}else if(now.equals(april)){
			predInput = april;
		}else if(now.equals(may)){
			predInput = may;
		}
		
		if(predInput == null){
			return null;
		}
		
		StationEntry prediction = new StationEntry(stationRioNegro, predictionDate.toEpochSecond(ZoneOffset.of("-3")));
		
		Integer predicted = null;
		
		prediction.registerPrediction(predicted);
		
		return prediction;
	}
}
