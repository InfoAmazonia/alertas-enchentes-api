package br.edu.ufcg.analytics.infoamazonia.scheduled;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ufcg.analytics.infoamazonia.Alert;
import br.edu.ufcg.analytics.infoamazonia.AlertRepository;

public abstract class UpdatePredictionsTask {
	
	@Autowired
	AlertRepository repository;

	protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyyHH:mm:ss");
	protected String stationName;
	protected int stationId;
	protected Map<String, Integer> dependencies;
	
	public UpdatePredictionsTask(String stationName, int stationId, Entry<String, Integer>... dependencies) {
		this.stationName = stationName;
		this.stationId = stationId;
		this.dependencies = Stream.of(dependencies).collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue()));
	}

	public void update() throws FileNotFoundException, ParseException{
		updateDependencies();
		populateStation(stationId, stationName, new Scanner(new File("/tmp/13600002RIOBRANCO_1832013-2182016.txt")));
	}
	
    protected static <K, V> Map.Entry<K, V> dependency(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }
    
	protected void updateDependencies() throws FileNotFoundException, ParseException {
		
		for (Entry<String, Integer> dependency : dependencies.entrySet()) {
			populateStation(dependency.getValue(), dependency.getKey(), new Scanner(new File("/tmp/13551000XAPURI-PCD_1832013-2182016.txt")));
		}
	}
	
	private void populateStation(long id, String name, Scanner input)
			throws ParseException {
		
		LocalDateTime start;
		if(repository.count() == 0){
			start = LocalDateTime.parse(name, formatter);

		}else{
			start = LocalDateTime.ofInstant(Instant.ofEpochSecond(repository.getLatest(id).getTimestamp()), ZoneId.of("America/Recife"));
		}
		
		downloadData(id, start.minusDays(1), start);
		
		input.nextLine();
		while (input.hasNext()) {
			String[] tokens = input.nextLine().trim().split("\\s+");
			System.out.println(Arrays.toString(tokens));
			long timestamp = LocalDateTime.parse(tokens[0] + tokens[1], formatter).toEpochSecond(ZoneId.of("America/Recife"));
			long cota = tokens.length == 3?Long.valueOf(tokens[2]):-1;
			if(repository.find(id, timestamp).isEmpty()){
				repository.save(new Alert(name, id, timestamp, cota));
			}
		}
	}

	private void downloadData(long id, LocalDateTime start, LocalDateTime end) {
		int day = start.getDayOfMonth();
		int month = start.getMonthValue();
		int year = start.getYear();
		
		
	}
}