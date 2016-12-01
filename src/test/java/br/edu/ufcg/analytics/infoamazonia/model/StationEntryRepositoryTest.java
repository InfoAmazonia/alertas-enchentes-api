package br.edu.ufcg.analytics.infoamazonia.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class StationEntryRepositoryTest {

    @Autowired
    private StationRepository stationRepository;
	private Station station;

    @Autowired
    private StationEntryRepository alertRepository;

    @Before
	public void setUp() throws Exception {
		this.station = new Station();
		station.id = 0L;
		station.name = "station";
		stationRepository.save(station);
	}

	@Test
	public void testFindFirstByNullStationAndTimestamp() {
		assertNull(alertRepository.findFirstByStationAndTimestamp(null, 0L));
	}

	@Test
	public void testFindFirstByStationAndNullTimestamp() {
		assertNull(alertRepository.findFirstByStationAndTimestamp(station, null));
	}

	@Test
	public void testFindNonExistentFirstByStationAndTimestamp() {
		assertNull(alertRepository.findFirstByStationAndTimestamp(station, 1000L));
	}

	@Test
	public void testFindFirstByStationAndTimestamp() {
		
		StationEntry olderAlert = new StationEntry(station, 1000L, 2000L);
		StationEntry newerAlert = new StationEntry(station, 1000L, 3000L);
		alertRepository.save(olderAlert );
		alertRepository.save(newerAlert);
		assertEquals(1, alertRepository.count());
		assertEquals(newerAlert, alertRepository.findFirstByStationAndTimestamp(station, 1000L));
	}

	@Test
	public void testFindFirstByNullStationAndMeasuredIsNotNullOrderByTimestampDesc() {
		assertNull(alertRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(null));
	}
	
	@Test
	public void testFindFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc() {
		alertRepository.save(new StationEntry(station, 1000L, null) );
		alertRepository.save(new StationEntry(station, 2000L, null) );
		alertRepository.save(new StationEntry(station, 3000L, 100L) );
		StationEntry latestNotNull = new StationEntry(station, 4000L, 200L);
		alertRepository.save(latestNotNull );
		alertRepository.save(new StationEntry(station, 5000L, null) );
		assertEquals(5, alertRepository.count());
		assertEquals(latestNotNull, alertRepository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(station));
	}

	@Test
	public void testFindAllByStationAndTimestampBetween() {
		alertRepository.save(new StationEntry(station, 1000L, 100L) );
		alertRepository.save(new StationEntry(station, 2000L, 200L) );
		alertRepository.save(new StationEntry(station, 3000L, 300L) );
		alertRepository.save(new StationEntry(station, 4000L, 400L) );
		alertRepository.save(new StationEntry(station, 5000L, 500L) );
		assertEquals(1, alertRepository.findAllByStationAndTimestampBetween(station, 1000L, 1000L).size());
		assertEquals(1, alertRepository.findAllByStationAndTimestampBetween(station, 999L, 1000L).size());
		assertEquals(1, alertRepository.findAllByStationAndTimestampBetween(station, 1000L, 1001L).size());
		assertEquals(1, alertRepository.findAllByStationAndTimestampBetween(station, 999L, 1001L).size());
		assertEquals(2, alertRepository.findAllByStationAndTimestampBetween(station, 1999L, 3001L).size());
	}

	@Test
	public void testCountZeroAlertsByStation() {
		assertEquals((Long)0L, alertRepository.countByStation(station));
	}

	@Test
	public void testCountAlertsByStation() {
		alertRepository.save(new StationEntry(station, 1000L, 100L) );
		alertRepository.save(new StationEntry(station, 1000L, 200L) );
		alertRepository.save(new StationEntry(station, 1000L, 300L) );
		assertEquals((Long)1L, alertRepository.countByStation(station));
	}

	@Test
	public void testCountZeroAlertsByNullStation() {
		assertEquals((Long)0L, alertRepository.countByStation(null));
	}

	@Test
	public void testCountByNonExistentStation() {
		Station nonExistentStation = new Station();
		nonExistentStation.id = 1L;
		assertEquals((Long)0L, alertRepository.countByStation(nonExistentStation));
	}

}
