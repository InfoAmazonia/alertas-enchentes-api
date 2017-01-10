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
	private static final long RIOBRANCO_ID = 13600002L;
	private static final long XAPURI_ID = 13551000L;
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyyHH:mm:ss").withZone(ZoneId.systemDefault());

	
    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private StationEntryRepository stationEntryRepository;
	
    @Autowired
    private SummaryRepository summaryRepository;
	
    private Station rioBranco;
	private Station xapuri;
	
	private UpdateRioAcreTasks update;
    
	
    @Before
	public void setUp() throws Exception {
    	System.setProperty("user.timezone", "UTC");
    	Station[] stations = new StationLoader().loadFromJSON(stationFile);
		this.stationRepository.save(Arrays.asList(stations));
		rioBranco = stationRepository.findOne(RIOBRANCO_ID);
		xapuri = stationRepository.findOne(XAPURI_ID);

		update = new UpdateRioAcreTasks();
		update.repository = stationEntryRepository;
		update.stationRepository = stationRepository;
		update.summaryRepository = summaryRepository;
	}
    
	@Test
	public void testUpdateWithEmptyDatabase() throws FileNotFoundException, ParseException {
		update.stationCacheDir = "src/test/resources/" + rioBranco.id + "_ok";
		
		StationEntry latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(rioBranco);
		assertNull("Rio Branco station should be null for empty database", latest);
		latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(xapuri);
		assertNull("Xapuri station should be null for empty database", latest);

		update.update();
		
		Long expectedTimestamp = ZonedDateTime.parse("03/01/201623:45:00", formatter).toEpochSecond();

		latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(rioBranco);
		assertNotNull("Rio Branco station should not be null after inserting", latest);
		assertEquals(expectedTimestamp, latest.timestamp);
		
		latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(xapuri);
		assertNotNull("Xapuri station should not be null after inserting", latest);
		assertEquals(expectedTimestamp, latest.timestamp);
		
		assertEquals(Long.valueOf(287+49), stationEntryRepository.countByStation(rioBranco));

		long timestamp = latest.timestamp;
		List<StationEntry> entries = stationEntryRepository.findAllByStationAndTimestampBetween(rioBranco, timestamp, timestamp + 43200);
		assertNotNull(entries);
		assertFalse(entries.isEmpty());
		assertEquals(49, entries.size());
		
		StationEntry first = entries.remove(0);
		assertEquals(Long.valueOf(529), first.measured);
		
		for (StationEntry stationEntry : entries) {
			assertNull(stationEntry.measured);
			assertNotNull(stationEntry.calculated);
			assertThat(stationEntry.calculated, Matchers.greaterThanOrEqualTo(0L));
			assertNotNull(stationEntry.predicted);
			assertThat(stationEntry.predicted, Matchers.greaterThanOrEqualTo(0L));
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
		assertNotNull(latest.calculated);
		assertThat(latest.calculated, Matchers.greaterThanOrEqualTo(0L));
		assertNotNull(latest.predicted);
		assertThat(latest.predicted, Matchers.greaterThanOrEqualTo(0L));

		
		latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(xapuri);
		assertNotNull("Xapuri station should not be null after inserting", latest);
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
		assertNull(latest.calculated);
		assertNull(latest.predicted);

		
		latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(xapuri);
		assertNotNull("Xapuri station should not be null after inserting", latest);
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
		assertNotNull(latest.calculated);
		assertNotNull(latest.predicted);
		
		latest = stationEntryRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(xapuri);
		assertNotNull("Xapuri station should not be null after inserting", latest);
		expectedTimestamp = ZonedDateTime.parse("03/01/201623:45:00", formatter).toEpochSecond();
		assertEquals(expectedTimestamp, latest.timestamp);
		assertNotNull(latest.measured);
		
		all = stationEntryRepository.findAllByStationAndTimestampBetween(xapuri, latest.timestamp+300, latest.timestamp+1200);
		latest = all.get(all.size()-1);
		assertNotNull("Xapuri station should not be null after inserting", latest);
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
		assertNull(latest.calculated);
		assertNull(latest.predicted);
	}
	
	public void testAlertScenarioD() throws FileNotFoundException, ParseException{
		update.stationCacheDir = "src/test/resources/" + rioBranco.id + "_ok";
		update.update();
		
		update.stationCacheDir = "src/test/resources/" + rioBranco.id + "_null_dependency_pastentry";
		update.update();
	}
}
