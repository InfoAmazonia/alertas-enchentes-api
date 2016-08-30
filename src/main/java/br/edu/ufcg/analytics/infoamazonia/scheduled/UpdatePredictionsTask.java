
package br.edu.ufcg.analytics.infoamazonia.scheduled;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ufcg.analytics.infoamazonia.model.Alert;
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
	
	private void populateStation(Long id) {
		
		LocalDateTime start;
		LocalDateTime end;
		
		Station station = stationRepository.findOne(id);

		Alert latest = repository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(station);
		System.out.println("latest: "+latest);
		if(latest == null){
			start = LocalDateTime.parse(station.oldestMeasureDate, formatter);
//			start = LocalDateTime.now().minusDays(3);
			end = LocalDateTime.now().plusDays(1);
		}else{
			start = LocalDateTime.ofInstant(Instant.ofEpochSecond(latest.timestamp), ZoneId.of("America/Recife"));
			end = LocalDateTime.now().plusDays(1);
		}
		
		System.out.println(start);
		System.out.println(end);
		LinkedList<MeasurementPair> measurements = new LinkedList<>();
		try {
			measurements = downloadData(station, start, end);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (MeasurementPair measurementPair : measurements) {
			Long timestamp = measurementPair.timestamp;
			Long quota = measurementPair.quota;
			Alert alert = repository.findFirstByStationAndTimestamp(station, timestamp);
			if(alert == null){
				repository.save(new Alert(station, timestamp, quota));
			}else{
				if(alert.measured == null){
					System.out.println(alert);
					alert.registerQuota(quota);
					repository.save(alert);
					System.out.println("A>" + alert);
					if(station.predict){
						Alert prediction = predict(timestamp);
						System.out.println("P>" + prediction);
						repository.save(prediction);
					}
				}
			}
		}
	}

	protected abstract Alert predict(long timestamp);

	private LinkedList<MeasurementPair> downloadData(Station station, LocalDateTime start, LocalDateTime end) throws ClientProtocolException, IOException {
		
		System.out.println("UpdatePredictionsTask.downloadData() " + Long.toString(station.id));
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		HttpPost httpPost = new HttpPost("http://mapas-hidro.ana.gov.br/Usuario/Exportar.aspx");

		List <NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		nvps.add(new BasicNameValuePair("__EVENTTARGET", "btGerar"));
		nvps.add(new BasicNameValuePair("__VIEWSTATE", station.viewState));
		nvps.add(new BasicNameValuePair("lstBacia", "1"));
		nvps.add(new BasicNameValuePair("lstDisponivel", "2"));
		nvps.add(new BasicNameValuePair("lstEstacao", Long.toString(station.lstStation)));
		nvps.add(new BasicNameValuePair("lstOrigem", "5"));
		nvps.add(new BasicNameValuePair("lstSubBacia", "13"));
		nvps.add(new BasicNameValuePair("txtAnofim", Integer.toString(end.getYear())));
		nvps.add(new BasicNameValuePair("txtAnoini", Integer.toString(start.getYear())));
		nvps.add(new BasicNameValuePair("txtCodigo", Long.toString(station.id)));
		nvps.add(new BasicNameValuePair("txtDiafim", Integer.toString(end.getDayOfMonth())));
		nvps.add(new BasicNameValuePair("txtDiaini", Integer.toString(start.getDayOfMonth())));
		nvps.add(new BasicNameValuePair("txtMesfim", Integer.toString(end.getMonthValue())));
		nvps.add(new BasicNameValuePair("txtMesini", Integer.toString(start.getMonthValue())));

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps);
		httpPost.setEntity(entity);
		
		CloseableHttpResponse response = httpclient.execute(httpPost);
		
		LinkedList<MeasurementPair> result = new LinkedList<>();
		
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			try (
					Scanner input = new Scanner(response.getEntity().getContent());
					){
				if(input.hasNextLine() && input.nextLine().contains("Data e Hora")){
					while(input.hasNextLine()){
						String line = input.nextLine().trim();
						String[] tokens = line.split("\\s+");
						long timestamp = LocalDateTime.parse(tokens[0] + tokens[1], formatter).toEpochSecond(ZoneOffset.of("-3"));
						long quota = tokens.length == 3?Long.valueOf(tokens[2]):0;
						result.add(new MeasurementPair(timestamp, quota));
					}
				}
				// do something useful with the response body
				// and ensure it is fully consumed
				EntityUtils.consume(response.getEntity());
			} finally {
				response.close();
			}
		}
		System.out.println(result.size());
		return result;
	}
}