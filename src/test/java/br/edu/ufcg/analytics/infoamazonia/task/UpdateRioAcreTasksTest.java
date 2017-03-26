/**
 * 
 */
package br.edu.ufcg.analytics.infoamazonia.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.edu.ufcg.analytics.infoamazonia.StationLoader;
import br.edu.ufcg.analytics.infoamazonia.model.Alert;
import br.edu.ufcg.analytics.infoamazonia.model.AlertRepository;
import br.edu.ufcg.analytics.infoamazonia.model.Station;
import br.edu.ufcg.analytics.infoamazonia.model.StationEntry;
import br.edu.ufcg.analytics.infoamazonia.model.StationEntryRepository;
import br.edu.ufcg.analytics.infoamazonia.model.StationRepository;
import br.edu.ufcg.analytics.infoamazonia.model.SummaryRepository;

/**
 * @author Ricardo Ara&eacute;jo Santos - ricoaraujosantos@gmail.com
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UpdateRioAcreTasksTest {

	private static String stationFile = "src/test/resources/stations.json";
	private static final long RIOBRANCO_ID = 13600010L;
	private static final long CAPIXABA_ID = 13568000L;
	private static final long RIOROLA_ID = 13578000L;
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyyHH:mm:ss").withZone(ZoneId.systemDefault());

	
    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private StationEntryRepository stationEntryRepository;
	
    @Autowired
    private SummaryRepository summaryRepository;
	
    @Autowired
    private AlertRepository alertRepository;
	
    private Station rioBranco;
	private Station capixaba;
	private Station riorola;
	
	private UpdateRioAcreCPRMTasks update;
    
	
    @Before
	public void setUp() throws Exception {
    	System.setProperty("user.timezone", "UTC");
    	Station[] stations = new StationLoader().loadFromJSON(stationFile);
		this.stationRepository.save(Arrays.asList(stations));
		for (Station station : stations) {
			this.alertRepository.save(new Alert(station, 0L, "Serviço indisponível."));
		}
		rioBranco = stationRepository.findOne(RIOBRANCO_ID);
		capixaba = stationRepository.findOne(CAPIXABA_ID);
		riorola = stationRepository.findOne(RIOROLA_ID);

		update = new UpdateRioAcreCPRMTasks();
		update.repository = stationEntryRepository;
		update.stationRepository = stationRepository;
		update.summaryRepository = summaryRepository;
		update.alertRepository = alertRepository;
	}
    
	@Test
	public void testUpdateWithEmptyDatabase() throws FileNotFoundException, ParseException {
		update.stationCacheDir = "src/test/resources/" + rioBranco.id + "_ok";
		
		StationEntry latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(rioBranco);
		assertNull("Rio Branco station should be null for empty database", latest);
		latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(capixaba);
		assertNull("Capixaba station should be null for empty database", latest);
		latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(riorola);
		assertNull("Rio Rola station should be null for empty database", latest);

		update.update();
		
		Long expectedTimestamp = ZonedDateTime.parse("03/01/201623:45:00", formatter).toEpochSecond();

		latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(rioBranco);
		assertNotNull("Rio Branco station should not be null after inserting", latest);
		assertEquals(expectedTimestamp, latest.timestamp);
		
		latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(capixaba);
		assertNotNull("Capixaba station should not be null after inserting", latest);
		assertEquals(expectedTimestamp, latest.timestamp);
		
		latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(riorola);
		assertNotNull("Rio Rola station should not be null after inserting", latest);
		assertEquals(expectedTimestamp, latest.timestamp);
		
		assertEquals(Long.valueOf(287+49), stationEntryRepository.countByStation(rioBranco));

		long timestamp = latest.timestamp;
		List<StationEntry> entries = stationEntryRepository.findAllByStationAndTimestampBetween(rioBranco, timestamp, timestamp + 43200);
		assertNotNull(entries);
		assertFalse(entries.isEmpty());
		assertEquals(49, entries.size());
		
		StationEntry first = entries.remove(0);
		assertEquals(Integer.valueOf(529), first.measured);
		
		for (StationEntry stationEntry : entries) {
			assertNull(stationEntry.measured);
			assertNotNull(stationEntry.predicted);
			assertThat(stationEntry.predicted, Matchers.greaterThanOrEqualTo(0));
		}
	}
	
	@Test
	public void testUpdateWithNewEntry() throws FileNotFoundException, ParseException {
		update.stationCacheDir = "src/test/resources/" + rioBranco.id + "_ok";
		update.update();
		
		assertEquals(Long.valueOf(287+49), stationEntryRepository.countByStation(rioBranco));

		update.stationCacheDir = "src/test/resources/" + rioBranco.id + "_ok_single_entry";
		update.update();

		assertEquals(Long.valueOf(288+49), stationEntryRepository.countByStation(rioBranco));

		StationEntry latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(rioBranco);
		assertNotNull("Rio Branco station should not be null after inserting", latest);
		Long expectedTimestamp = ZonedDateTime.parse("04/01/201600:00:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);
		
		List<StationEntry> all = stationEntryRepository.findAllByStationAndTimestampBetween(rioBranco, latest.timestamp, latest.timestamp+43200);
		latest = all.get(all.size()-1);
		assertNotNull("Rio Branco station should not be null after inserting", latest);
		expectedTimestamp = ZonedDateTime.parse("04/01/201612:00:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);
		assertNull(latest.measured);
		assertNotNull(latest.predicted);
		assertThat(latest.predicted, Matchers.greaterThanOrEqualTo(0));

		
		latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(capixaba);
		assertNotNull("Capixaba station should not be null after inserting", latest);
		expectedTimestamp = ZonedDateTime.parse("04/01/201600:00:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);

		latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(riorola);
		assertNotNull("Rio Rola station should not be null after inserting", latest);
		expectedTimestamp = ZonedDateTime.parse("04/01/201600:00:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);
}
	
	@Test
	public void testUpdateWithNullEntry() throws FileNotFoundException, ParseException {
		update.stationCacheDir = "src/test/resources/" + rioBranco.id + "_ok";
		update.update();
		
		assertEquals(Long.valueOf(287+49), stationEntryRepository.countByStation(rioBranco));

		update.stationCacheDir = "src/test/resources/" + rioBranco.id + "_null_single_entry";
		update.update();

		assertEquals(Long.valueOf(288+49), stationEntryRepository.countByStation(rioBranco));

		StationEntry latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(rioBranco);
		assertNotNull("Rio Branco station should not be null after inserting", latest);
		Long expectedTimestamp = ZonedDateTime.parse("03/01/201623:45:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);
		
		List<StationEntry> all = stationEntryRepository.findAllByStationAndTimestampBetween(rioBranco, latest.timestamp+300, latest.timestamp+1200);
		latest = all.get(all.size()-1);
		assertNotNull("Rio Branco station should not be null after inserting", latest);
		expectedTimestamp = ZonedDateTime.parse("04/01/201600:00:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);
		assertNull(latest.measured);
		
		all = stationEntryRepository.findAllByStationAndTimestampBetween(rioBranco, latest.timestamp, latest.timestamp+43200);
		latest = all.get(all.size()-1);
		assertNotNull("Rio Branco station should not be null after inserting", latest);
		expectedTimestamp = ZonedDateTime.parse("04/01/201612:00:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);
		assertNull(latest.measured);
		assertNull(latest.predicted);

		
		latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(capixaba);
		assertNotNull("Capixaba station should not be null after inserting", latest);
		expectedTimestamp = ZonedDateTime.parse("04/01/201600:00:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);
		assertNotNull(latest.measured);
		
		latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(riorola);
		assertNotNull("Rio Rola station should not be null after inserting", latest);
		expectedTimestamp = ZonedDateTime.parse("04/01/201600:00:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);
		assertNotNull(latest.measured);
	}
	
	@Test
	public void testUpdateWithNullDependencyEntry() throws FileNotFoundException, ParseException {
		update.stationCacheDir = "src/test/resources/" + rioBranco.id + "_ok";
		update.update();
		
		assertEquals(Long.valueOf(287+49), stationEntryRepository.countByStation(rioBranco));

		update.stationCacheDir = "src/test/resources/" + rioBranco.id + "_null_dependency_entry";
		update.update();

		assertEquals(Long.valueOf(288+49), stationEntryRepository.countByStation(rioBranco));

		StationEntry latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(rioBranco);
		assertNotNull("Rio Branco station should not be null after inserting", latest);
		Long expectedTimestamp = ZonedDateTime.parse("04/01/201600:00:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);

		List<StationEntry> all = stationEntryRepository.findAllByStationAndTimestampBetween(rioBranco, latest.timestamp, latest.timestamp+43200);
		latest = all.get(all.size()-1);
		assertNotNull("Rio Branco station should not be null after inserting", latest);
		expectedTimestamp = ZonedDateTime.parse("04/01/201612:00:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);
		assertNull(latest.measured);
		assertNull(latest.predicted);
		
		latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(capixaba);
		assertNotNull("Capixaba station should not be null after inserting", latest);
		expectedTimestamp = ZonedDateTime.parse("03/01/201623:45:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);
		assertNotNull(latest.measured);
		
		all = stationEntryRepository.findAllByStationAndTimestampBetween(capixaba, latest.timestamp+300, latest.timestamp+1200);
		latest = all.get(all.size()-1);
		assertNotNull("Capixaba station should not be null after inserting", latest);
		expectedTimestamp = ZonedDateTime.parse("04/01/201600:00:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);
		assertNull(latest.measured);

		latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(riorola);
		assertNotNull("Rio Rola station should not be null after inserting", latest);
		expectedTimestamp = ZonedDateTime.parse("03/01/201623:45:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);
		assertNotNull(latest.measured);
		
		all = stationEntryRepository.findAllByStationAndTimestampBetween(riorola, latest.timestamp+300, latest.timestamp+1200);
		latest = all.get(all.size()-1);
		assertNotNull("Rio Rola station should not be null after inserting", latest);
		expectedTimestamp = ZonedDateTime.parse("04/01/201600:00:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);
		assertNull(latest.measured);
}
	
	public void testUpdateWithNullDependencyEntryForPrediction() throws FileNotFoundException, ParseException {
		update.stationCacheDir = "src/test/resources/" + rioBranco.id + "_ok";
		update.update();
		
		assertEquals(Long.valueOf(287+49), stationEntryRepository.countByStation(rioBranco));

		update.stationCacheDir = "src/test/resources/" + rioBranco.id + "_null_dependency_pastentry";
		update.update();

		assertEquals(Long.valueOf(288+49), stationEntryRepository.countByStation(rioBranco));

		StationEntry latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(rioBranco);
		assertNotNull("Rio Branco station should not be null after inserting", latest);
		Long expectedTimestamp = ZonedDateTime.parse("04/01/201612:00:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);

		List<StationEntry> all = stationEntryRepository.findAllByStationAndTimestampBetween(rioBranco, latest.timestamp, latest.timestamp+43200);
		latest = all.get(all.size()-1);
		assertNotNull("Rio Branco station should not be null after inserting", latest);
		expectedTimestamp = ZonedDateTime.parse("05/01/201600:00:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);
		assertNull(latest.measured);
		assertNull(latest.predicted);
	}
	
	public void testAlertScenarioD() throws FileNotFoundException, ParseException{
		update.stationCacheDir = "src/test/resources/" + rioBranco.id + "_ok";
		update.update();
		
		update.stationCacheDir = "src/test/resources/" + rioBranco.id + "_null_dependency_pastentry";
		update.update();
	}
}
