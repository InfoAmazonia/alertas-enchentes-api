package br.edu.ufcg.analytics.infoamazonia.scheduled;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.Duration;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.edu.ufcg.analytics.infoamazonia.model.Alert;
import br.edu.ufcg.analytics.infoamazonia.model.AlertPk;
import br.edu.ufcg.analytics.infoamazonia.model.Station;

@Component
public class UpdateRioMadeira extends UpdatePredictionsTask {

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
		long time = System.currentTimeMillis();
		System.out.println("UpdateRioMadeira.update()");
		super.update();
		time = System.currentTimeMillis() - time;
		System.out.println("Updated RioMadeira in " + time + " millis");
	}

	@Override
	protected Alert predict(long timestamp, Map<Long, Station> stations) {
		
		Station stationMadeira = stations.get(MADEIRA_ID);
		Station stationXapuri = stations.get(XAPURI_ID);
		
		Alert future = new Alert(stationMadeira, timestamp + DELTA);

		Alert pastXapuri = repository.findOne(new AlertPk(timestamp - DELTA,  stationXapuri.id));
		Alert pastPastXapuri = repository.findOne(new AlertPk(timestamp - 2*DELTA,  stationXapuri.id));
		
		Alert current = repository.findOne(new AlertPk(timestamp,  stationMadeira.id));
		Alert past = repository.findOne(new AlertPk(timestamp - DELTA,  stationMadeira.id));
		
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
