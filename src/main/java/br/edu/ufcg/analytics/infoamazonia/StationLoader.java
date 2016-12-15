package br.edu.ufcg.analytics.infoamazonia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ufcg.analytics.infoamazonia.model.Station;
import br.edu.ufcg.analytics.infoamazonia.model.StationRepository;
import br.edu.ufcg.analytics.infoamazonia.model.Summary;
import br.edu.ufcg.analytics.infoamazonia.model.SummaryRepository;

@Component
public class StationLoader implements ApplicationListener<ApplicationReadyEvent> {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private StationRepository repository;

	@Autowired
	private SummaryRepository summaryRepository;
	
	@Value("${infoamazonia.station.file}")
	private String stationsFilePath;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent arg0) {

		logger.info("Loading stations");
		Station[] stations = {};

		try {
			stations = loadStationsFromFile(stationsFilePath);
			repository.save(Arrays.asList(stations));
			logger.debug("Finished loading stations");
			
			logger.info("Loading history");
			for (Station station : stations) {
				if(summaryRepository.countByStation(station) == 0){
					loadHistory(station.id);
				}
			}
			logger.info("Finished loading");

		} catch (IOException e) {
			logger.error("Problem occured while loading stations", e);
			throw new RuntimeException(e);
		}
	}

	public Station[] loadStationsFromFile(String fileName) throws IOException, JsonParseException, JsonMappingException {
		logger.info("Loading stations from: " + fileName);
		return new ObjectMapper().readValue(new File(fileName), Station[].class);
	}

	private void loadHistory(Long stationId) {
		logger.debug("Loading history for " + stationId);
		Station station = repository.findOne(stationId);
		File historyFile = new File("data/history/" + station.id + ".csv");
		if (historyFile.exists() && historyFile.canRead()) {
			try (Scanner input = new Scanner(historyFile);) {
				while (input.hasNextLine()) {
					String[] entry = input.nextLine().trim().split(",");
					String timestamp = entry[0].trim().split(" +")[0];
					Long value = "NA".equals(entry[1].trim()) ? null : Long.valueOf(entry[1].trim());
					summaryRepository.save(new Summary(station, timestamp, value));
				}
			} catch (FileNotFoundException e) {
				logger.error("Problem occured while loading station history", e);
			}
		}
	}
}
