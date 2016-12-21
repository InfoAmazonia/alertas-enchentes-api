/**
 * 
 */
package br.edu.ufcg.analytics.infoamazonia.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

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
public class UpdateTasksTest {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private StationEntryRepository stationEntryRepository;
	
    @Autowired
    private SummaryRepository summaryRepository;
	
    @Autowired
    private AlertRepository alertRepository;
    
    @Value("${infoamazonia.alert.scenario.c}")
    public String c;
    
    @Value("${infoamazonia.alert.scenario.d}")
    public String d;

    private UpdateTasks update;
	
	private Station fakeStation;
    
	
    @Before
	public void setUp() throws Exception {
    	fakeStation = new Station();
    	fakeStation.id = 1L;
    	fakeStation.attentionThreshold = 1000L;
    	fakeStation.warningThreshold = 1500L;
    	fakeStation.floodThreshold = 2000L;
    	fakeStation.predict = true;
    	fakeStation.riverName = "Rio XPTO";
    	fakeStation.name = "Station";
		this.stationRepository.save(fakeStation);

		this.update = new UpdateTasks(fakeStation.id) {
			@Override
			protected StationEntry predict(long timestamp, Map<Long, Station> stationMap) {
				return new StationEntry(fakeStation, timestamp, 0L);
			}
		};
		
		this.update.repository = stationEntryRepository;
		this.update.stationRepository = stationRepository;
		this.update.summaryRepository = summaryRepository;
		this.update.alertRepository = alertRepository;
		this.update.c = c;
		this.update.d = d;
	}
    
    @Test
    public void testAlertEmptyDB() throws FileNotFoundException, ParseException{
    	this.update.updateAlert(fakeStation);
		
		Alert alert = alertRepository.findFirstByStationOrderByTimestampDesc(fakeStation);
		assertNull(alert);
    }

    
    @Test
    public void testAlertFirstEntry() throws FileNotFoundException, ParseException{
    	Long timestamp = 0L;
		
    	StationEntry entry = new StationEntry(fakeStation, timestamp, 1000L);
		this.stationEntryRepository.save(entry);
		entry = new StationEntry(fakeStation, timestamp+43200);
		entry.calculated = 1000L;
		entry.predicted = 1000L;
		this.stationEntryRepository.save(entry);
		
    	this.update.updateAlert(fakeStation);
		
		Alert alert = alertRepository.findFirstByStationOrderByTimestampDesc(fakeStation);
		assertNotNull(alert);
		assertEquals(timestamp+43200L, alert.timestamp.longValue());
		assertNotNull(alert.message);
		assertEquals("Primeiro alerta", alert.message);
    }

    @Test
    @Ignore
    public void testAlertScenarioC() throws FileNotFoundException, ParseException{

    	Long timestamp = 0L;
		
    	StationEntry entry = new StationEntry(fakeStation, timestamp, 500L);
		this.stationEntryRepository.save(entry);
		this.stationEntryRepository.save(predict(timestamp, 1500L));

		this.update.updateAlert(fakeStation);

		Alert alert = alertRepository.findFirstByStationOrderByTimestampDesc(fakeStation);
		assertNotNull(alert);
		assertEquals(timestamp + 43200L, alert.timestamp.longValue());

    	for (int i = 1; i < 48; i++) {
    		entry = new StationEntry(fakeStation, timestamp + i*900, 500L);
    		this.stationEntryRepository.save(entry);
    		this.stationEntryRepository.save(predict(timestamp + i*900, 1500L));
        	
    		this.update.updateAlert(fakeStation);

    		assertEquals(alert, alertRepository.findFirstByStationOrderByTimestampDesc(fakeStation));
		}
    	
		entry = new StationEntry(fakeStation, timestamp + 43200, 1500L, 1500L, 1500L);
		this.stationEntryRepository.save(entry);
		this.stationEntryRepository.save(predict(timestamp + 43200, 1500L));
		
		this.update.updateAlert(fakeStation);
		
		alert = alertRepository.findFirstByStationOrderByTimestampDesc(fakeStation);
		assertNotNull(alert);
		assertEquals(timestamp+43200, alert.timestamp.longValue());
		assertNotNull(alert.message);
		assertEquals(c, alert.message);
    }

	private StationEntry predict(Long timestamp, Long prediction) {
		StationEntry entry = new StationEntry(fakeStation, timestamp+43200);
		entry.calculated = prediction;
		entry.predicted = prediction;
		return entry;
	}

    @Test
    @Ignore
    public void testAlertScenarioD() throws FileNotFoundException, ParseException{
    	Long timestamp = 0L;
		
    	StationEntry entry = new StationEntry(fakeStation, timestamp, 1000L);
		this.stationEntryRepository.save(entry);
    	this.update.updateAlert(fakeStation);

    	entry = new StationEntry(fakeStation, timestamp + 900, 1000L, 1500L, 1500L);
		this.stationEntryRepository.save(entry);
    	this.update.updateAlert(fakeStation);
		
		Alert alert = alertRepository.findFirstByStationOrderByTimestampDesc(fakeStation);
		assertNotNull(alert);
		assertEquals(timestamp+900, alert.timestamp.longValue());
		assertNotNull(alert.message);
		assertEquals("O Rio XPTO, em Station, deve atingir o estado de ALERTA nas próximas 12 horas. Atualmente o nível do rio está em 10.00 metros e a previsão é que o nível chegue aos 15.00 metros.", alert.message);
    }
}
