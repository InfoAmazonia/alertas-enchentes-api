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
		assertEquals("RIO BRANCO", riobranco.name);
		assertEquals("RIO ACRE", riobranco.riverName);
		assertEquals("RIO BRANCO", riobranco.cityName);
		assertEquals(1200, riobranco.attentionThreshold.longValue());
		assertEquals(1350, riobranco.warningThreshold.longValue());
		assertEquals(1400, riobranco.floodThreshold.longValue());
		assertEquals("01/12/201400:00:00", riobranco.oldestMeasureDate);
		assertEquals(true, riobranco.predict);
		assertEquals(95867480, riobranco.lstStation.longValue());
		assertEquals("dDw0MDI2NjM1MTE7dDw7bDxpPDE+Oz47bDx0PDtsPGk8NT47aTwxMT47aTwxMj47aTwxMz47aTwxND47aTwxNj47aTwxOD47aTwyMT47aTwyOT47aTw0OD47aTw0OT47aTw1Mj47PjtsPHQ8cDxwPGw8VGV4dDtGb3JlQ29sb3I7XyFTQjs+O2w8IHxTw6lyaWUgaGlzdMOzcmljYSB8OzI8MCwgMCwgMTIwPjtpPDQ+Oz4+Oz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPHwgUGVzcXVpc2FyIERhZG9zIHw7aTwyNTY+OzE8MTE1cHg+Oz4+Oz47Oz47dDxwPHA8bDxUZXh0O0ZvcmVDb2xvcjtfIVNCO1dpZHRoO0JhY2tDb2xvcjs+O2w8fCBFeHBvcnRhciBEYWRvcyB8OzI8V2hpdGU+O2k8MjY4PjsxPDEwNXB4PjsyPDAsIDAsIDEyMD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7XyFTQjtXaWR0aDs+O2w8XGU7aTwyNTY+OzE8MHB4Pjs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDtfIVNCO1dpZHRoOz47bDxcZTtpPDI1Nj47MTwwcHg+Oz4+Oz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPFxlO2k8MjU2PjsxPDBweD47Pj47Pjs7Pjt0PHQ8O3Q8aTwyOD47QDwxMzk2MjAwMCBBUlVNw4MgLSBKVVNBTlRFOzEzNDUwMDAwIEFTU0lTIEJSQVNJTDsxMzQ1MDAxMCBBU1NJUyBCUkFTSUwgLSBDUFJNOzEzOTkwMDAwIEJFUlVSSTsxMzcwMDAwMCBCT0NBIERPIEFDUkU7MTM0NzAwMDAgQlJBU0lMw4lJQTsxMzQ3MDAxMCBCUkFTSUzDiUlBIC0gQ1BSTTsxMzU2ODAwMCBDQVBJWEFCQSAoQ29sb2Nhw6fDo28gU8OjbyBKb3PDqSk7MTMxNTAwMDMgQ09BUkk7MTM1NDAwMDAgQ09Mw5ROSUEgRE9MT1JFUyAoWEFQVVJJKTsxMzQ5MDAwMCBFUElUQUNJT0zDgk5ESUEgKENvbMO0bmlhIFPDo28gQmVudG8pOzEzNDMwMDAwIEVTRUMgUklPIEFDUkU7MTM1NzIwMDAgRVNQQUxIQSAoU2VyaW5nYWwgQmVsbyBIb3Jpem9udGUpOzEzMjkwMDAwIEZBWkVOREEgUkVQT1VTTyAoUmlvIE1hY2F1w6MpOzEzODcwMDAwIEzDgUJSRUE7MTMxODAwMDAgTUFOT0VMIFVSQkFOTzsxMzE3NDAwMCBQQVJRVUUgQ0hBTkRMRVNTOzEzNjAwMDAyIFJJTyBCUkFOQ087MTM2MDAwMTAgUklPIEJSQU5DTyAtIENQUk07MTM1NzgwMDAgUklPIFJPTEEgKFJhbWFsIGRvIEJhcnJvIEFsdG8pOzk3MDAwMyBTQU5UQSBST1NBOzEzMTY5MDAwIFNBTlRBIFJPU0EgRE8gUFVSVVMoQ29sb25pYSBDaGFtYnVpYWNvKTswMDk2ODAwNiBTRU5BIE1BRFVSRUlSQTsxMzMxMDAwMCBTRU5BIE1BRFVSRUlSQTsxMzQwNTAwMCBTRVJJTkdBTCBHVUFSQU5ZOzEzMTAwMDAwIFNFUklOR0FMIE1PUkVJUkE7MTMzMDAwMDAgU0VSSU5HQUwgU8ODTyBKT1PDiTsxMzU1MDAwMCBYQVBVUkk7PjtAPDQ0NDYyMDkwOzEwNTc2OTM0MDsxMDU2NjkzMzE7MzUyNjEyMTA7ODQ0NjcyNDA7MTEwNjg0NTA7MTEwMTY4NDUwOzEwMjM2NzU2MDs0MDU2MzA0MDsxMDMzNjg0MjA7MTA1MzY4MzYwOzExMzcwMTIwOzEwMDY4MzEwOzkzMjY4NTYwOzcxNTY0NDgwOzg1MjY5MTcwOzkyMTY5NTUwOzk1OTY3NDgwOzk1ODY3NDgwOzEwMzM3NTcwOzkyNjcwMjkwOzkzNDcwMzUwOzkwMzY4MzkwOzk0NjgzOTE7OTIxNjkyNTE7NTA3NjM1OTA7OTE5Njg0MzA7MTAzODY4MzAwOz4+Oz47Oz47dDxwPHA8bDxUZXh0O0ZvcmVDb2xvcjtfIVNCOz47bDxcZTsyPFJlZD47aTw0Pjs+Pjs+Ozs+O3Q8dDw7dDxpPDM+O0A8Q2h1dmE6IDAxLzEyLzIwMTQgLSAwNy8wMy8yMDE3O07DrXZlbDogMDEvMTIvMjAxNCAtIDA3LzAzLzIwMTc7VmF6w6NvOiAwMS8xMi8yMDE0IC0gMDcvMDMvMjAxNzs+O0A8MTsyOzM7Pj47bDxpPDE+Oz4+Ozs+O3Q8dDw7dDxpPDY+O0A8XDx0b2Rvc1w+O0FOQS9JTlBFO0FOQS9TSVZBTTtTZXRvciBFbMOpdHJpY287Q290YU9ubGluZTtQcm9qZXRvc19Fc3BlY2lhaXM7PjtAPDA7MTsyOzM7NDs1Oz4+O2w8aTw1Pjs+Pjs7Pjt0PHQ8O3Q8aTwxMD47QDxcPHRvZG9zXD47MSBSaW8gQW1hem9uYXM7MiBSaW8gVG9jYW50aW5zOzMgQXRsw6JudGljbywgVHJlY2hvIE5vcnRlL05vcmRlc3RlOzQgUmlvIFPDo28gRnJhbmNpc2NvOzUgQXRsw6JudGljbywgVHJlY2hvIExlc3RlOzYgUmlvIFBhcmFuw6HCoDs3IFJpbyBVcnVndWFpOzggQXRsw6JudGljbywgVHJlY2hvIFN1ZGVzdGU7OSBPdXRyYXM7PjtAPDA7MTsyOzM7NDs1OzY7Nzs4Ozk7Pj47bDxpPDE+Oz4+Ozs+O3Q8dDw7dDxpPDExPjtAPFw8dG9kb3NcPjsxMCBSSU8gU09MSU1PRVMsIEpBVkFSSSxJVEFDVUFJOzExIFJJTyBTT0xJTU9FUyxJQ0EsSkFORElBVFVCQSwuLjsxMiBSSU8gU09MSU1PRVMsSlVSVUEsSkFQVVJBLC4uLi47MTMgUklPIFNPTElNT0VTLFBVUlVTLENPQVJJLC4uOzE0IFJJTyBTT0xJTU9FUyxORUdSTyxCUkFOQ08sLi4uLjsxNSBSSU8gQU1BWk9OQVMsTUFERUlSQSxHVUFQT1JFLC47MTYgUklPIEFNQVpPTkFTLFRST01CRVRBUyxPVVRST1M7MTcgUklPIEFNQVpPTkFTLFRBUEFKT1MsSlVSVUVOQS4uOzE4IFJJTyBBTUFaT05BUyxYSU5HVSxJUklSSSxQQVJVOzE5IFJJTyBBTUFaT05BUyxKQVJJLFBBUkEsT1VUUk9TOz47QDwwOzEwOzExOzEyOzEzOzE0OzE1OzE2OzE3OzE4OzE5Oz4+O2w8aTw0Pjs+Pjs7Pjs+Pjs+PjtsPGxzdEVzdGFjYW87YnRBdHVhbGl6YXI7bHN0RGlzcG9uaXZlbDtsc3RPcmlnZW07bHN0QmFjaWE7bHN0U3ViQmFjaWE7Pj7RhkEdqfbBYuTpFQcwXN3e4gzejg==", riobranco.viewState);
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
