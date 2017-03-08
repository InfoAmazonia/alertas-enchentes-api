/**
 * 
 */
package br.edu.ufcg.analytics.infoamazonia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.edu.ufcg.analytics.infoamazonia.model.Station;

/**
 * @author Ricardo Ara&eacute;jo Santos - ricoaraujosantos@gmail.com
 *
 */
public class StationLoaderTest {

	private String stationFile = "src/test/resources/stations.json";
	private String emptyFile = "src/test/resources/empty.json";
	private String noStationFile = "src/test/resources/no_station.json";
	private String malformedFile = "src/test/resources/malformed.json";

	/**
	 * Test method for {@link br.edu.ufcg.analytics.infoamazonia.StationLoader#loadFromJSON(java.lang.String)}.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@Test
	public void testloadFromJSON() throws JsonParseException, JsonMappingException, IOException {
		Station[] stations = new StationLoader().loadFromJSON(stationFile);
		assertNotEquals("Should have loaded stations", 0, stations.length);
		
		Station riobranco = stations[2];
		assertEquals(13600010, riobranco.id.longValue());
		assertEquals("Rio Branco", riobranco.name);
		assertEquals("Rio Acre", riobranco.riverName);
		assertEquals("Rio Branco", riobranco.cityName);
		assertEquals(1200, riobranco.attentionThreshold.longValue());
		assertEquals(1350, riobranco.warningThreshold.longValue());
		assertEquals(1400, riobranco.floodThreshold.longValue());
		assertEquals("01/12/201400:00:00", riobranco.oldestMeasureDate);
		assertEquals(true, riobranco.predict);
		assertEquals(95867480, riobranco.lstStation.longValue());
		assertEquals(1, riobranco.bacia);
		assertEquals(13, riobranco.subbacia);
	}

	/**
	 * Test method for {@link br.edu.ufcg.analytics.infoamazonia.StationLoader#loadFromJSON(java.lang.String)}.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@Test(expected=JsonMappingException.class)
	public void testLoadStationsFromEmptyFile() throws JsonParseException, JsonMappingException, IOException {
		new StationLoader().loadFromJSON(emptyFile);
	}

	/**
	 * Test method for {@link br.edu.ufcg.analytics.infoamazonia.StationLoader#loadFromJSON(java.lang.String)}.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@Test
	public void testLoadNoStationsFromFile() throws JsonParseException, JsonMappingException, IOException {
		Station[] stations = new StationLoader().loadFromJSON(noStationFile);
		assertEquals("Should not have loaded stations", 0, stations.length);

	}

	/**
	 * Test method for {@link br.edu.ufcg.analytics.infoamazonia.StationLoader#loadFromJSON(java.lang.String)}.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@Test(expected=JsonMappingException.class)
	public void testLoadStationsFromMalformedFile() throws JsonParseException, JsonMappingException, IOException {
		new StationLoader().loadFromJSON(malformedFile);
	}

}
