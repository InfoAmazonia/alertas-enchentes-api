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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ufcg.analytics.infoamazonia.Alert;
import br.edu.ufcg.analytics.infoamazonia.AlertPk;
import br.edu.ufcg.analytics.infoamazonia.AlertRepository;

public abstract class UpdatePredictionsTask {
	
	@Autowired
	AlertRepository repository;

	protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyyHH:mm:ss");
	protected String stationName;
	protected Long stationId;
	protected Map<String, Long> dependencies;
	
	public UpdatePredictionsTask(String stationName, Long stationId, Entry<String, Long> entry) {
		this.stationName = stationName;
		this.stationId = stationId;
		this.dependencies = Stream.of(entry).collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue()));
	}

	public void update() throws FileNotFoundException, ParseException{
		updateDependencies();
		populateStation(stationId, stationName, true);
	}
	
    protected static <K, V> Map.Entry<K, V> dependency(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }
    
	protected void updateDependencies() throws FileNotFoundException, ParseException {
		
		for (Entry<String, Long> dependency : dependencies.entrySet()) {
			populateStation(dependency.getValue(), dependency.getKey(), false);
		}
	}
	
	private void populateStation(long id, String name, boolean predict)
			throws ParseException, FileNotFoundException {
		LocalDateTime start;
		List<Alert> latest = repository.getLatest(id);
		if(latest.isEmpty()){
			start = LocalDateTime.parse(name, formatter);
		}else{
			start = LocalDateTime.ofInstant(Instant.ofEpochSecond(latest.get(0).getId().getTimestamp()), ZoneId.of("America/Recife"));
		}
		
		String fileName = downloadData(id, start.minusDays(1), start);
		try(Scanner input = new Scanner(new File(fileName));){
			if(input.hasNext()){
				input.nextLine(); //skip header
			}
			while (input.hasNext()) {
				String line = input.nextLine().trim();
				String[] tokens = line.split("\\s+");
				long timestamp = LocalDateTime.parse(tokens[0] + tokens[1], formatter).toEpochSecond(ZoneOffset.of("-3"));
				long cota = tokens.length == 3?Long.valueOf(tokens[2]):-1;
				if(predict){
					Alert alert = repository.findOne(new AlertPk(id, timestamp));
					if(alert == null){
						alert = new Alert();
						alert.setId(new AlertPk(id, timestamp));
					}
					alert = setMeasuredStatus(cota, alert);
					repository.save(alert);
					
					Alert prediction = predict(id, timestamp);
					repository.save(prediction);
				}else{
					if(repository.findOne(new AlertPk(id, timestamp)) == null){
						repository.save(new Alert(new AlertPk(id, timestamp), cota));
					}
				}
			}
		}
		
	}

	protected abstract Alert setMeasuredStatus(long cota, Alert alert);

	protected abstract Alert predict(long id, long timestamp);

	private String downloadData(long id, LocalDateTime start, LocalDateTime end) {
//		int day = start.getDayOfMonth();
//		int month = start.getMonthValue();
//		int year = start.getYear();
		
		if(id == 13551000){
			return "data/13551000XAPURI-PCD_1122014-2282016.txt";
		}else{
			return "data/13600002RIOBRANCO_1122014-2282016.txt";
		}
	}
	
	protected String calculateStatus(long predicted, long alerta, long inundacao) {
		if(predicted < alerta){
			return "NORMAL";
		}else if (alerta <= predicted && predicted < inundacao){
			return "ALERTA";
		}else{
			return "INUNDACAO";
		}
	}

}