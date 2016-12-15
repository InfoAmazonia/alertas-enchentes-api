/**
 * 
 */
package br.edu.ufcg.analytics.infoamazonia.scheduled;

import java.util.Arrays;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.edu.ufcg.analytics.infoamazonia.StationLoader;
import br.edu.ufcg.analytics.infoamazonia.model.Station;
import br.edu.ufcg.analytics.infoamazonia.model.StationEntryRepository;
import br.edu.ufcg.analytics.infoamazonia.model.StationRepository;

/**
 * @author Ricardo Ara&eacute;jo Santos - ricoaraujosantos@gmail.com
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UpdatePredictionsTaskTest {

	private static String stationFile = "src/test/resources/stations.json";
	
    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private StationEntryRepository alertRepository;
	
    @Before
	public void setUp() throws Exception {
    	Station[] stations = new StationLoader().loadStationsFromFile(stationFile);
		this.stationRepository.save(Arrays.asList(stations));
	}
    
    

}
