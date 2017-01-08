package br.edu.ufcg.analytics.infoamazonia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
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

import br.edu.ufcg.analytics.infoamazonia.exception.InfoAmazoniaException;
import br.edu.ufcg.analytics.infoamazonia.model.Station;
import br.edu.ufcg.analytics.infoamazonia.model.Summary;
import br.edu.ufcg.analytics.infoamazonia.service.StationService;

@Component
public class StationLoader implements ApplicationListener<ApplicationReadyEvent> {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private StationService service;

	@Value("${infoamazonia.station.file}")
	private String stationsFile;
	
	@Value("${infoamazonia.station.history.dir}")
	private String stationsHistoryDir;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent arg0) {

		logger.info("Loading stations");

		try {
			Station[] stations = loadFromJSON(stationsFile);
			for (Station station : stations) {
				service.save(station);
				if(!service.containsHistory(station)){
					List<Summary> history = loadStationHistory(station, new File(stationsHistoryDir + "/" + station.id + ".csv"));
					service.saveHistory(history);
				}
			}
			logger.debug("Finished loading stations");
		} catch (IOException e) {
			logger.error("Problem occured while loading stations", e);
			throw new InfoAmazoniaException("Problem occured while loading stations", e);
		}
	}
	
	public Station[] loadFromJSON(String file) throws JsonParseException, JsonMappingException, IOException {
		logger.info("Loading stations from: " + file);
		return new ObjectMapper().readValue(new File(file), Station[].class);
	}
	
	public List<Summary> loadStationHistory(Station station, File historyFile) throws FileNotFoundException {
		List<Summary> history = new LinkedList<>();
		logger.info("Loading history for " + station.id);
		if (historyFile.exists() && historyFile.canRead()) {
			try (Scanner input = new Scanner(historyFile);) {
				while (input.hasNextLine()) {
					String[] entry = input.nextLine().trim().split(",");
					String timestamp = entry[0].trim().split(" +")[0];
					Long value = "NA".equals(entry[1].trim()) ? null : Long.valueOf(entry[1].trim());
					history.add(new Summary(station, timestamp, value));
				}
			}
		}
		return history;
	}


}
