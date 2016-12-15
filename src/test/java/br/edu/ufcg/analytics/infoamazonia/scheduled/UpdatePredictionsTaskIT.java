/**
 * 
 */
package br.edu.ufcg.analytics.infoamazonia.scheduled;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.client.ClientProtocolException;
import org.hamcrest.Matchers;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.edu.ufcg.analytics.infoamazonia.StationLoader;
import br.edu.ufcg.analytics.infoamazonia.model.Station;
import br.edu.ufcg.analytics.infoamazonia.model.StationEntry;

/**
 * @author Ricardo Ara&eacute;jo Santos - ricoaraujosantos@gmail.com
 *
 */
@RunWith(Theories.class)
public class UpdatePredictionsTaskIT {
	
	private static String stationFile = "src/test/resources/stations.json";
	
	@DataPoints
	public static Station[] stations() throws JsonParseException, JsonMappingException, IOException {
		return new StationLoader().loadStationsFromFile(stationFile);
	}
	
	/**
	 * Theory for {@link br.edu.ufcg.analytics.infoamazonia.scheduled.UpdatePredictionsTask#downloadData(Station, java.time.LocalDateTime, java.time.LocalDateTime)}.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Theory
	public void thereIsPublishedDataForStation(Station station) throws ClientProtocolException, IOException {
		
		UpdatePredictionsTask updatePredictionsTask = new UpdatePredictionsTask(station.id){
			@Override
			protected StationEntry predict(long timestamp, Map<Long, Station> stationMap) {
				return null;
			}
		};
		
		LocalDateTime start = LocalDateTime.of(2016, 10, 1, 0, 0);
		LocalDateTime end = LocalDateTime.of(2016, 10, 3, 0, 0);

		List<Measurement> measurements = updatePredictionsTask.downloadData(station, start, end);
		assertNotNull(measurements);
		assertFalse("Should not be empty for station " + station, measurements.isEmpty());
		
		List<Long> data = measurements.stream().mapToLong(measurement -> measurement.quota).boxed().collect(Collectors.toList());;
		
		assertThat(data, Matchers.hasItem(Matchers.notNullValue(Long.class)));
	}

}
