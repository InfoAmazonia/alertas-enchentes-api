package br.edu.ufcg.analytics.infoamazonia.task;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.Duration;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.edu.ufcg.analytics.infoamazonia.model.EntryPk;
import br.edu.ufcg.analytics.infoamazonia.model.Station;
import br.edu.ufcg.analytics.infoamazonia.model.StationEntry;

@Component
public class UpdateRioAcreTasks extends UpdateTasks {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final long RIOBRANCO_ID = 13600002L;
	private static final long XAPURI_ID = 13551000L;
	private static final long RATE = 900000;
	private static final long DELTA = Duration.ofHours(12).getSeconds();
	private static final double ALPHA = 0.717395738210093;
	private static final double BETA = 0.151170920309919;
	
	public UpdateRioAcreTasks() {
		super(RIOBRANCO_ID, XAPURI_ID);
	}

	@Scheduled(initialDelay=1000, fixedRate = RATE)
	@Override
	@Transactional
	public void update() throws FileNotFoundException, ParseException {
		logger.info("Updating Rio Acre");
		long time = System.currentTimeMillis();
		super.update();
		time = System.currentTimeMillis() - time;
		logger.info("Updated Rio Acre in " + time + " millis");
	}

	@Override
	protected StationEntry predict(long timestamp, Map<Long, Station> stations) {
		
		Station stationRioBranco = stations.get(RIOBRANCO_ID);
		Station stationXapuri = stations.get(XAPURI_ID);
		
		StationEntry future = new StationEntry(stationRioBranco, timestamp + DELTA);

		StationEntry pastXapuri = repository.findOne(new EntryPk(timestamp - DELTA,  stationXapuri.id));
		StationEntry pastPastXapuri = repository.findOne(new EntryPk(timestamp - 2*DELTA,  stationXapuri.id));
		
		StationEntry current = repository.findOne(new EntryPk(timestamp,  stationRioBranco.id));
		StationEntry past = repository.findOne(new EntryPk(timestamp - DELTA,  stationRioBranco.id));
		
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
