package br.edu.ufcg.analytics.infoamazonia.scheduled;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.Duration;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.edu.ufcg.analytics.infoamazonia.model.StationEntry;
import br.edu.ufcg.analytics.infoamazonia.model.StationEntryPk;
import br.edu.ufcg.analytics.infoamazonia.model.Station;

@Component
public class UpdateRioMadeira extends UpdatePredictionsTask {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final long MADEIRA_ID = 13600002L;
	private static final long XAPURI_ID = 13551000L;
	private static final long RATE = 900000;
	private static final long DELTA = Duration.ofHours(12).getSeconds();
	private static final double ALPHA = 0.717395738210093;
	private static final double BETA = 0.151170920309919;
	
	public UpdateRioMadeira() {
		super(MADEIRA_ID, XAPURI_ID);
	}

	@Scheduled(initialDelay=1000, fixedRate = RATE)
	@Override
	@Transactional
	public void update() throws FileNotFoundException, ParseException {
		logger.debug("UpdateRioMadeira.update()");
		long time = System.currentTimeMillis();
		super.update();
		time = System.currentTimeMillis() - time;
		logger.debug("Updated RioMadeira in " + time + " millis");
	}

	@Override
	protected StationEntry predict(long timestamp, Map<Long, Station> stations) {
		
		Station stationMadeira = stations.get(MADEIRA_ID);
		Station stationXapuri = stations.get(XAPURI_ID);
		
		StationEntry future = new StationEntry(stationMadeira, timestamp + DELTA);

		StationEntry pastXapuri = repository.findOne(new StationEntryPk(timestamp - DELTA,  stationXapuri.id));
		StationEntry pastPastXapuri = repository.findOne(new StationEntryPk(timestamp - 2*DELTA,  stationXapuri.id));
		
		StationEntry current = repository.findOne(new StationEntryPk(timestamp,  stationMadeira.id));
		StationEntry past = repository.findOne(new StationEntryPk(timestamp - DELTA,  stationMadeira.id));
		
		if(!isAnyAlertNull(current, past, pastXapuri, pastPastXapuri)){
			long calculated  = (long) (current.measured + 
					ALPHA * (current.measured - past.measured) + 
					BETA * (pastXapuri.measured - pastPastXapuri.measured));
			long predicted = calculated + ((current.calculated == null || current.calculated == 0) ? 0
					: (current.measured - current.calculated));

			future.registerPrediction(calculated, predicted);
		}else{
			future.registerPrediction(null, null);
		}
		
		return future;
	}
}
