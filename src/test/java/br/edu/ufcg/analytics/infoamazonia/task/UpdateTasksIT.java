/**
 * 
 */
package br.edu.ufcg.analytics.infoamazonia.task;

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
public class UpdateTasksIT {
	
	private static String stationFile = "src/test/resources/stations.json";
	
	@DataPoints
	public static Station[] stations() throws JsonParseException, JsonMappingException, IOException {
		return new StationLoader().loadFromJSON(stationFile);
	}
	
	/**
	 * Theory for {@link br.edu.ufcg.analytics.infoamazonia.task.UpdateTasks#downloadData(Station, java.time.LocalDateTime, java.time.LocalDateTime)}.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Theory
	public void thereIsPublishedDataForStation(Station station) throws ClientProtocolException, IOException {
		
		UpdateTasks UpdateTasks = new UpdateTasks(station.id){
			@Override
			protected StationEntry predict(long timestamp, Map<Long, Station> stationMap) {
				return null;
			}
		};
		
		LocalDateTime end = LocalDateTime.now();
		LocalDateTime start = end.minusMonths(1);

		List<Measurement> measurements = UpdateTasks.downloadData(station, start, end);
		assertNotNull(measurements);
		assertFalse("Should not be empty for station " + station, measurements.isEmpty());
		
		List<Integer> data = measurements.stream().map(measurement -> measurement.quota).collect(Collectors.toList());
		
		assertThat("Should have at least one valid data point in the last month", data, Matchers.hasItem(Matchers.notNullValue(Integer.class)));
	}

}
