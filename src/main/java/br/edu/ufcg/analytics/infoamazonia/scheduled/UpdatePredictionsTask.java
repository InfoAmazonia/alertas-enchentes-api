
package br.edu.ufcg.analytics.infoamazonia.scheduled;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Scanner;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ufcg.analytics.infoamazonia.model.Station;
import br.edu.ufcg.analytics.infoamazonia.model.StationEntry;
import br.edu.ufcg.analytics.infoamazonia.model.StationEntryRepository;
import br.edu.ufcg.analytics.infoamazonia.model.StationRepository;
import br.edu.ufcg.analytics.infoamazonia.model.Summary;
import br.edu.ufcg.analytics.infoamazonia.model.SummaryRepository;

public abstract class UpdatePredictionsTask {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected StationEntryRepository repository;

	@Autowired
	protected SummaryRepository summaryRepository;

	@Autowired
	protected StationRepository stationRepository;

	protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyyHH:mm:ss");
	protected DateTimeFormatter summaryFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
	protected List<Long> dependencies;
	protected Long stationId;

	protected String stationCacheDir;

	
	public UpdatePredictionsTask(Long stationId, Long... dependenciesIds) {
		this.stationId = stationId;
		this.dependencies = Arrays.asList(dependenciesIds);
	}

	public void update() throws FileNotFoundException, ParseException{
		updateDependencies();
		long time = System.currentTimeMillis();
		populateStation(stationId);
		this.stationCacheDir = null;
		logger.debug("Updated station " + stationId + " in " + (System.currentTimeMillis() - time) + " millis");
	}
	
	protected static <K, V> Map.Entry<K, V> dependency(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }
    
	protected void updateDependencies() throws FileNotFoundException, ParseException {
		for (Long dependency : dependencies) {
			populateStation(dependency);
		}
	}
	
	private void populateStation(Long id) {
		
		logger.debug("station: "+id);
		Station station = stationRepository.findOne(id);

		StationEntry latest = repository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(station);
		logger.debug("latest: "+latest);
		

		try {
			long a = System.currentTimeMillis();
			List<Measurement> measurements = getMeasurements(station, latest);
			logger.debug("READ " + id + " DATA in " + (System.currentTimeMillis() - a) + "ms");
			logger.info("linhas: " + measurements.size());
			a = System.currentTimeMillis();
			updateData(station, measurements);
			logger.debug("INSERTED " + id + " DATA in " + (System.currentTimeMillis() - a) + "ms");
		} catch (IOException e) {
			logger.error("Problem updating data for station " + station.id, e);
		}

	}
	
	protected List<Measurement> getMeasurements(Station station, StationEntry latest) throws ClientProtocolException, IOException{
		LocalDateTime start;
		LocalDateTime end;
		
		if(this.stationCacheDir != null){
			return getFromCache(station);
		}

		start = LocalDateTime.ofInstant(Instant.ofEpochSecond(latest.timestamp), ZoneId.of("America/Recife"));
		end = LocalDateTime.now().plusDays(1);
		return downloadData(station, start, end);
	}

	private void updateData(Station station, List<Measurement> measurements) {
		
		Map<Long, Station> stationMap = new HashMap<>();
		stationMap.put(station.id, station);
		for (Long dependencyId : dependencies) {
			stationMap.put(dependencyId, stationRepository.findOne(dependencyId));
		}

		LocalDateTime last = null;
		long i = 0;
		for (Measurement measurementPair : measurements) {
			i++;
			Long timestamp = measurementPair.timestamp;
			Long quota = measurementPair.quota;
			repository.save(new StationEntry(station, timestamp, quota));

			if(station.predict){
				if(i % 1000 == 0){
					System.err.println(System.currentTimeMillis() + "> " + i);
				}
				repository.save(predict(timestamp, stationMap));
			}
			LocalDateTime now = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.of("America/Recife"));
			if(last != null && last.getDayOfYear() != now.getDayOfYear()){
				populateSummary(station, last);
			}
			last = now;
		}
	}

	protected abstract StationEntry predict(long timestamp, Map<Long, Station> stationMap);
	
	protected List<Measurement> getFromCache(Station station) throws ClientProtocolException, IOException {

		File cacheFile = new File(stationCacheDir + "/" + station.id);
		logger.info("Cache file: " + cacheFile.getAbsolutePath());
		try (Scanner input = new Scanner(cacheFile);) {
			return parseResults(input);
		}
	}

	protected List<Measurement> downloadData(Station station, LocalDateTime start, LocalDateTime end) throws ClientProtocolException, IOException {

		try (CloseableHttpClient httpclient = HttpClients.createDefault();) {

			HttpPost httpPost = new HttpPost("http://mapas-hidro.ana.gov.br/Usuario/Exportar.aspx");

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
			nvps.add(new BasicNameValuePair("__EVENTTARGET", "btGerar"));
			nvps.add(new BasicNameValuePair("__VIEWSTATE", station.viewState));
			nvps.add(new BasicNameValuePair("lstBacia", Integer.toString(station.bacia)));
			nvps.add(new BasicNameValuePair("lstDisponivel", "2"));
			nvps.add(new BasicNameValuePair("lstEstacao", Long.toString(station.lstStation)));
			nvps.add(new BasicNameValuePair("lstOrigem", "5"));
			nvps.add(new BasicNameValuePair("lstSubBacia", Integer.toString(station.subbacia)));
			nvps.add(new BasicNameValuePair("txtAnofim", Integer.toString(end.getYear())));
			nvps.add(new BasicNameValuePair("txtAnoini", Integer.toString(start.getYear())));
			nvps.add(new BasicNameValuePair("txtCodigo", Long.toString(station.id)));
			nvps.add(new BasicNameValuePair("txtDiafim", Integer.toString(end.getDayOfMonth())));
			nvps.add(new BasicNameValuePair("txtDiaini", Integer.toString(start.getDayOfMonth())));
			nvps.add(new BasicNameValuePair("txtMesfim", Integer.toString(end.getMonthValue())));
			nvps.add(new BasicNameValuePair("txtMesini", Integer.toString(start.getMonthValue())));

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps);
			httpPost.setEntity(entity);

			try (CloseableHttpResponse response = httpclient.execute(httpPost);) {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					try (Scanner input = new Scanner(response.getEntity().getContent());) {
						return parseResults(input);
					}
				}
			}
		}

		return new LinkedList<>();
	}

	private List<Measurement> parseResults(Scanner input) {
		List<Measurement> result = new LinkedList<>();
		if (input.hasNextLine() && input.nextLine().contains("Data e Hora")) {
			while (input.hasNextLine()) {
				String line = input.nextLine().trim();
				String[] tokens = line.split("\\s+");
				LocalDateTime now = LocalDateTime.parse(tokens[0] + tokens[1], formatter);

				Long timestamp = now.toEpochSecond(ZoneOffset.of("-3"));
				Long quota = tokens.length == 3 ? Math.round(Double.valueOf(tokens[2])) : null;
				result.add(new Measurement(timestamp, quota));
			}
		}
		return result;
	}
	
	private void populateSummary(Station station, LocalDateTime timestamp) {
		
		LocalDateTime start = timestamp.truncatedTo(ChronoUnit.DAYS);
		LocalDateTime end = start.plusDays(1);
		List<StationEntry> alerts = repository.findAllByStationAndTimestampBetween(station, start.toEpochSecond(ZoneOffset.of("-3")), end.toEpochSecond(ZoneOffset.of("-3")));
		OptionalDouble average = alerts.stream().filter(a -> a.measured != null).mapToLong(a -> a.measured).average();
		Long averageMeasurement = average.isPresent()? Math.round(average.getAsDouble()): null;
		summaryRepository.save(new Summary(station, start.format(summaryFormatter), averageMeasurement));
	}

	protected boolean isAnyAlertNull(StationEntry... alerts){
		for (StationEntry alert : alerts) {
			if(alert == null || alert.measured == null){
				return true;
			}
		}
		
		return false;
	}
}