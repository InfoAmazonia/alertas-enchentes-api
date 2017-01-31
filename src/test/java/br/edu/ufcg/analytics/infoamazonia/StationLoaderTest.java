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
		assertEquals(13600002, riobranco.id.longValue());
		assertEquals("RIO BRANCO", riobranco.name);
		assertEquals("RIO ACRE", riobranco.riverName);
		assertEquals("RIO BRANCO", riobranco.cityName);
		assertEquals(1200, riobranco.attentionThreshold.longValue());
		assertEquals(1350, riobranco.warningThreshold.longValue());
		assertEquals(1400, riobranco.floodThreshold.longValue());
		assertEquals("01/08/200500:00:00", riobranco.oldestMeasureDate);
		assertEquals(true, riobranco.predict);
		assertEquals(95867480, riobranco.lstStation.longValue());
		assertEquals("dDw0MDI2NjM1MTE7dDw7bDxpPDE+Oz47bDx0PDtsPGk8NT47aTwxMT47aTwxMj47aTwxMz47aTwxND47aTwxNj47aTwxOD47aTwyMT47aTwyOT47aTw0OD47aTw0OT47aTw1Mj47aTw1ND47PjtsPHQ8cDxwPGw8VGV4dDtGb3JlQ29sb3I7XyFTQjs+O2w8IHxTw6lyaWUgaGlzdMOzcmljYSB8OzI8MCwgMCwgMTIwPjtpPDQ+Oz4+Oz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPHwgUGVzcXVpc2FyIERhZG9zIHw7aTwyNTY+OzE8MTE1cHg+Oz4+Oz47Oz47dDxwPHA8bDxUZXh0O0ZvcmVDb2xvcjtfIVNCO1dpZHRoO0JhY2tDb2xvcjs+O2w8fCBFeHBvcnRhciBEYWRvcyB8OzI8V2hpdGU+O2k8MjY4PjsxPDEwNXB4PjsyPDAsIDAsIDEyMD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7XyFTQjtXaWR0aDs+O2w8XGU7aTwyNTY+OzE8MHB4Pjs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDtfIVNCO1dpZHRoOz47bDxcZTtpPDI1Nj47MTwwcHg+Oz4+Oz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPFxlO2k8MjU2PjsxPDBweD47Pj47Pjs7Pjt0PHQ8O3Q8aTwyPjtAPDEzNjAwMDAyIFJJTyBCUkFOQ087MTM2MDAwMDIgUklPIEJSQU5DTzs+O0A8OTU5Njc0ODA7OTU4Njc0ODA7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7Rm9yZUNvbG9yO18hU0I7PjtsPFxlOzI8UmVkPjtpPDQ+Oz4+Oz47Oz47dDx0PDt0PGk8Mz47QDxDaHV2YTogMDEvMTIvMjAxNCAtIDAzLzExLzIwMTY7TsOtdmVsOiAwMS8xMi8yMDE0IC0gMDMvMTEvMjAxNjtWYXrDo286IDAxLzEyLzIwMTQgLSAwMy8xMS8yMDE2Oz47QDwxOzI7Mzs+PjtsPGk8MT47Pj47Oz47dDx0PDt0PGk8Nj47QDxcPHRvZG9zXD47QU5BL0lOUEU7QU5BL1NJVkFNO1NldG9yIEVsw6l0cmljbztDb3RhT25saW5lO1Byb2pldG9zX0VzcGVjaWFpczs+O0A8MDsxOzI7Mzs0OzU7Pj47bDxpPDU+Oz4+Ozs+O3Q8dDw7dDxpPDEwPjtAPFw8dG9kb3NcPjsxIFJpbyBBbWF6b25hczsyIFJpbyBUb2NhbnRpbnM7MyBBdGzDom50aWNvLCBUcmVjaG8gTm9ydGUvTm9yZGVzdGU7NCBSaW8gU8OjbyBGcmFuY2lzY287NSBBdGzDom50aWNvLCBUcmVjaG8gTGVzdGU7NiBSaW8gUGFyYW7DocKgOzcgUmlvIFVydWd1YWk7OCBBdGzDom50aWNvLCBUcmVjaG8gU3VkZXN0ZTs5IE91dHJhczs+O0A8MDsxOzI7Mzs0OzU7Njs3Ozg7OTs+PjtsPGk8MT47Pj47Oz47dDx0PDt0PGk8MTE+O0A8XDx0b2Rvc1w+OzEwIFJJTyBTT0xJTU9FUywgSkFWQVJJLElUQUNVQUk7MTEgUklPIFNPTElNT0VTLElDQSxKQU5ESUFUVUJBLC4uOzEyIFJJTyBTT0xJTU9FUyxKVVJVQSxKQVBVUkEsLi4uLjsxMyBSSU8gU09MSU1PRVMsUFVSVVMsQ09BUkksLi47MTQgUklPIFNPTElNT0VTLE5FR1JPLEJSQU5DTywuLi4uOzE1IFJJTyBBTUFaT05BUyxNQURFSVJBLEdVQVBPUkUsLjsxNiBSSU8gQU1BWk9OQVMsVFJPTUJFVEFTLE9VVFJPUzsxNyBSSU8gQU1BWk9OQVMsVEFQQUpPUyxKVVJVRU5BLi47MTggUklPIEFNQVpPTkFTLFhJTkdVLElSSVJJLFBBUlU7MTkgUklPIEFNQVpPTkFTLEpBUkksUEFSQSxPVVRST1M7PjtAPDA7MTA7MTE7MTI7MTM7MTQ7MTU7MTY7MTc7MTg7MTk7Pj47bDxpPDQ+Oz4+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8MTM2MDAwMDI7Pj47Pjs7Pjs+Pjs+PjtsPGxzdEVzdGFjYW87YnRBdHVhbGl6YXI7bHN0RGlzcG9uaXZlbDtsc3RPcmlnZW07bHN0QmFjaWE7bHN0U3ViQmFjaWE7Pj6hfOMdNBJIW8nXsAHG2o/aVK2emw==", riobranco.viewState);
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
