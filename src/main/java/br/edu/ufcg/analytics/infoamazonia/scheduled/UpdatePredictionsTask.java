
package br.edu.ufcg.analytics.infoamazonia.scheduled;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ufcg.analytics.infoamazonia.model.Alert;
import br.edu.ufcg.analytics.infoamazonia.model.AlertPk;
import br.edu.ufcg.analytics.infoamazonia.model.AlertRepository;
import br.edu.ufcg.analytics.infoamazonia.model.Station;
import br.edu.ufcg.analytics.infoamazonia.model.StationRepository;

public abstract class UpdatePredictionsTask {
	
	@Autowired
	AlertRepository repository;

	@Autowired
	StationRepository stationRepository;

	protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyyHH:mm:ss");
	protected List<Long> dependencies;
	protected Long stationId;

	
	public UpdatePredictionsTask(Long stationId, Long... dependenciesIds) {
		this.stationId = stationId;
		this.dependencies = Arrays.asList(dependenciesIds);
	}

	public void update() throws FileNotFoundException, ParseException{
		updateDependencies();
		populateStation(stationId);
	}
	
	protected static <K, V> Map.Entry<K, V> dependency(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }
    
	protected void updateDependencies() throws FileNotFoundException, ParseException {
		for (Long dependency : dependencies) {
			populateStation(dependency);
		}
	}
	
	private void populateStation(Long id)
			throws ParseException, FileNotFoundException {
		
		LocalDateTime start;
		
		Station station = stationRepository.findOne(id);

		List<Alert> latest = repository.getLatestFromStation(station);
		if(latest.isEmpty()){
			start = LocalDateTime.parse(station.oldestMeasureDate, formatter);
		}else{
			start = LocalDateTime.ofInstant(Instant.ofEpochSecond(latest.get(0).id.timestamp), ZoneId.of("America/Recife"));
		}
		
		String fileName = downloadData(station.id, start.minusDays(1), start);
		
		try(Scanner input = new Scanner(new File(fileName));){
			if(input.hasNext()){
				input.nextLine(); //skip header
			}
			while (input.hasNext()) {
				String line = input.nextLine().trim();
				String[] tokens = line.split("\\s+");
				long timestamp = LocalDateTime.parse(tokens[0] + tokens[1], formatter).toEpochSecond(ZoneOffset.of("-3"));
				long quota = tokens.length == 3?Long.valueOf(tokens[2]):-1;
				if(station.predict){
					Alert alert = repository.exists(new AlertPk(timestamp, station))? 
							repository.findOne(new AlertPk(timestamp, station)): 
								new Alert(station, timestamp);
					alert.registerQuota(quota);
					repository.save(alert);
					Alert prediction = predict(timestamp);
					repository.save(prediction);
				}else{
					if(!repository.exists(new AlertPk(timestamp, station))){
						Alert alert = new Alert(station, timestamp, quota);
						repository.save(alert);
					}
				}
			}
		}
	}

	protected abstract Alert predict(long timestamp);

	private String downloadData(long id, LocalDateTime start, LocalDateTime end) {
//		int day = start.getDayOfMonth();
//		int month = start.getMonthValue();
//		int year = start.getYear();
		
		if(id == 13551000L){
			return "data/13551000XAPURI-PCD_1122014-2282016.txt";
		}else{
			return "data/13600002RIOBRANCO_1122014-2282016.txt";
		}
	}
}